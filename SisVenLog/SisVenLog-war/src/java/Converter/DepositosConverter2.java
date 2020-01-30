/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import dao.DepositosFacade;
import entidad.Depositos;
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
@Named(value = "depositosConverter2")
@ApplicationScoped
public class DepositosConverter2 implements Converter, Serializable {

    @EJB
    private DepositosFacade ejbFacade;

    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || isDummySelectItem(component, value)) {
            return null;
        }
        return ejbFacade.find(getKey(value));
    }

    entidad.DepositosPK getKey(String value) {
        entidad.DepositosPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new entidad.DepositosPK();
        key.setCodEmpr(Short.parseShort(values[0]));
        key.setCodDepo(Short.parseShort(values[1]));
        return key;
    }

    String getStringKey(entidad.DepositosPK value) {
        StringBuffer sb = new StringBuffer();
        
        System.out.println("valor de value : "+value);
        sb.append(value.getCodEmpr());
        sb.append(SEPARATOR);
        sb.append(value.getCodDepo());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof Depositos) {
            Depositos o = (Depositos) object;
            return getStringKey(o.getDepositosPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Depositos.class.getName()});
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
