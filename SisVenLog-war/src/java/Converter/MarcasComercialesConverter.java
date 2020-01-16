/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import dao.MarcasComercialesFacade;
import entidad.MarcasComerciales;
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
@Named(value = "marcasComercialesConverter")
@ApplicationScoped
public class MarcasComercialesConverter  implements Converter, Serializable{
    
    @EJB
    private MarcasComercialesFacade ejbFacade;
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || isDummySelectItem(component, value)) {
            return null;
        }
        return ejbFacade.marcacomercialById(getKey(value));
    }

    java.lang.Short getKey(String value) {
        java.lang.Short key;
        key = Short.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Short value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof MarcasComerciales) {
            MarcasComerciales o = (MarcasComerciales) object;
            return getStringKey(o.getCmarca());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), MarcasComerciales.class.getName()});
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
