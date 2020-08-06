package Converter;

import entidad.Rutas;
import entidad.RutasPK;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author mbrizuela
 */
@Named(value = "RutasConverter")
@ApplicationScoped

public class RutasConverter  implements Converter, Serializable {
    
   @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            return new Rutas(new RutasPK(new Short("2"), new Short(value)));
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
            return String.valueOf(((Rutas) value).getRutasPK().getCodRuta());
        }
    }

}
