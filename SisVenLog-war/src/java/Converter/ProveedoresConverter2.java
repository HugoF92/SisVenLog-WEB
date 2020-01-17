/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import dao.MarcasComercialesFacade;
import dao.ProveedoresFacade;
import entidad.MarcasComerciales;
import entidad.Proveedores;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Norio
 */
@Named(value = "proveedoresConverter2")
@ApplicationScoped
public class ProveedoresConverter2  implements Converter, Serializable{
    
    @Inject
    private ProveedoresFacade ejbFacade;
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || isDummySelectItem(component, value)) {
            return null;
        }
        System.out.println("value : "+value);
        System.out.println("getKey : "+getKey(value));
        System.out.println("objeto : "+ejbFacade.proveedorById(getKey(value)));
        return ejbFacade.proveedorById(getKey(value));
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
        if (object instanceof Proveedores) {
            Proveedores o = (Proveedores) object;
            return getStringKey(o.getCodProveed());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Proveedores.class.getName()});
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
