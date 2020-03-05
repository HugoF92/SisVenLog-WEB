/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import entidad.CanalesCompra;
import entidad.CanalesCompraPK;
import entidad.Depositos;
import entidad.DepositosPK;
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
@Named(value = "CanalesCompraConverter")
@ApplicationScoped

public class CanalesCompraConverter  implements Converter, Serializable{
    
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return new CanalesCompra(new CanalesCompraPK(new Short("1"), value));
            //return new Depositos(new DepositosPK(Integer.parseInt(value), 0));
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
            //return String.valueOf(((Integer) value));
            return String.valueOf(((CanalesCompra) value).getCanalesCompraPK().getCcanalCompra());
        }
    }

    
}
