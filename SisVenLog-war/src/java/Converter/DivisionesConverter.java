/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import entidad.Divisiones;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author SAMACACE
 */
@Named(value = "DivisionesConverter")
@ApplicationScoped

public class DivisionesConverter  implements Converter, Serializable{
    
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return new Divisiones(Short.parseShort(value));
            //return new Divisiones(new DivisionesPK(Integer.parseInt(value), 0));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null || value.equals(""))
        {
            return "";
        }
        else
        {
            return String.valueOf(((Divisiones) value).getCodDivision());
        }
    }

    
}
