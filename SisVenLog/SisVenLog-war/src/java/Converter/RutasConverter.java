/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import dao.RutasFacade;
import entidad.Rutas;
import entidad.RutasPK;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;


@Named(value = "RutasConverter")
@ApplicationScoped

public class RutasConverter  implements Converter, Serializable{

    @EJB
    private RutasFacade facade;
    
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return facade.find(new RutasPK(new Short("2"), Short.parseShort(value)));
            //return new Rutas(new RutasPK(new Short("2"), Short.parseShort(value)));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null || (value instanceof String && ((String) value).length() == 0)) {
            return "";
        }
        if (value instanceof Rutas) {
            Rutas r = (Rutas) value;
            return r.getRutasPK().getCodRuta() + "";
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    "object {0} is of type {1}; expected type: {2}",
                    new Object[]{value, value.getClass().getName(),
                        Rutas.class.getName()});
            return null;
        }
    }

    
}
