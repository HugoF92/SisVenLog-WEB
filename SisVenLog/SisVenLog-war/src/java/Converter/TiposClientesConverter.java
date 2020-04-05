/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import dao.TiposClientesFacade;
import entidad.TiposClientes;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author SAMACACE
 */
@Named(value = "TiposClientesConverter")
@ApplicationScoped

public class TiposClientesConverter  implements Converter, Serializable{

    @EJB
    private TiposClientesFacade facade;
    
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return facade.find(value.trim());
            //return new TiposClientes(value.trim());
            //return new Lineas(new LineasPK(Integer.parseInt(value), 0));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null || (value instanceof String && ((String) value).length() == 0)) {
            return "";
        }
        if (value instanceof TiposClientes) {
            TiposClientes tc = (TiposClientes) value;
            return tc.getCtipoCliente();
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    "object {0} is of type {1}; expected type: {2}",
                    new Object[]{value, value.getClass().getName(),
                        TiposClientes.class.getName()});
            return null;
        }
    }

    
}