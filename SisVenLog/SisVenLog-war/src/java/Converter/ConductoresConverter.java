package Converter;

import entidad.Conductores;
import entidad.Empleados;
import entidad.EmpleadosPK;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author NICO
 */
@Named(value = "ConductoresConverter")
@ApplicationScoped

public class ConductoresConverter  implements Converter, Serializable{
    
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return new Conductores(new Short(value));
            //return new Empleados(new EmpleadosPK(Integer.parseInt(value), 0));
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
            return String.valueOf(((Conductores) value).getCodConductor());
        }
    }

    
}
