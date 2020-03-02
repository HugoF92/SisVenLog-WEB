package Converter;

import dao.ZonasFacade;
import entidad.Zonas;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author Norio
 */
@Named(value = "zonasConverter2")
@ApplicationScoped
public class ZonasConverter2 implements Converter, Serializable {
    @EJB
    private ZonasFacade ejbFacade;

    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || isDummySelectItem(component, value)) {
            return null;
        }
        return ejbFacade.find(getKey(value));
    }

    entidad.ZonasPK getKey(String value) {
        entidad.ZonasPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new entidad.ZonasPK();
        key.setCodEmpr(Short.parseShort(values[0]));
        key.setCodZona(values[1]);
        return key;
    }

    String getStringKey(entidad.ZonasPK value) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(value.getCodEmpr());
        sb.append(SEPARATOR);
        sb.append(value.getCodZona());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof Zonas) {
            Zonas o = (Zonas) object;
            return getStringKey(o.getZonasPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Zonas.class.getName()});
            return null;
        }
    }

    public boolean isDummySelectItem(UIComponent component, String value) {
        for (UIComponent children : component.getChildren()) {
            if (children instanceof UISelectItem) {
                UISelectItem item = (UISelectItem) children;
                if (item.getItemValue() == null && item.getItemLabel().equals(value)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

}
