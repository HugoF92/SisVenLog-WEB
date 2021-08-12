/**
 * Converter para entidades JPA. Baseia-se nas anotações @Id e @EmbeddedId para
 * identificar o atributo que representa a identidade da entidade. Capaz de
 * detectar as anotações nas classes superiores.
 *
 * @author Flávio Henrique
 * @version 1.0.3
 * @since 05/10/2010
 */
package Converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;

@Named(value = "entityConverter")
public class EntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
        if (value != null) {
            return component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object obj) {
        if (obj != null && !"".equals(obj)) {
            String id = null;
            try {
                id = this.getId(getClazz(ctx, component), obj);
            } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(EntityConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (id == null) {
                id = "";
            }
            id = id.trim();
            component.getAttributes().put(id, getClazz(ctx, component).cast(obj));
            return id;
        }
        return null;
    }

    /**
     * Obtém, via expression language, a classe do objeto.
     *
     * @param FacesContext facesContext
     * @param UICompoment compoment
     * @return Class<?>
     */
    private Class<?> getClazz(FacesContext facesContext, UIComponent component) {
        return component.getValueExpression("value").getType(facesContext.getELContext());
    }

    /**
     * Retorna a representação em String do retorno do método anotado com @Id ou
     *
     * @param obj
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     * @EmbeddedId do objeto.
     * @param clazz<?> clazz
     * @return String
     */
    public String getId(Class<?> clazz, Object obj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        List<Class<?>> hierarquiaDeClasses = this.getHierarquiaDeClasses(clazz);

        for (Class<?> classeDaHierarquia : hierarquiaDeClasses) {
            for (Field field : classeDaHierarquia.getDeclaredFields()) {
                if ((field.getAnnotation(Id.class)) != null || field.getAnnotation(EmbeddedId.class) != null) {
                    Field privateField = classeDaHierarquia.getDeclaredField(field.getName());
                    privateField.setAccessible(true);
                    if (privateField.get(clazz.cast(obj)) != null) {
                        return (String) field.getType().cast(privateField.get(clazz.cast(obj))).toString();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retorna uma lista com a hierarquia de classes, sem considerar a classe
     * Object.class
     *
     * @param clazz
     *
     * @return List<Class<?>> clazz
     */
    public List<Class<?>> getHierarquiaDeClasses(Class<?> clazz) {

        List<Class<?>> hierarquiaDeClasses = new ArrayList<>();
        Class<?> classeNaHierarquia = clazz;
        while (classeNaHierarquia != Object.class) {
            hierarquiaDeClasses.add(classeNaHierarquia);
            classeNaHierarquia = classeNaHierarquia.getSuperclass();

        }
        return hierarquiaDeClasses;
    }
}
