/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import dao.MercaderiasFacade;
import entidad.Mercaderias;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import java.util.List;

/**
 *
 * @author SAMACACE
 */
@Named(value = "MercaderiasConverter")
@ApplicationScoped
public class MercaderiasConverter  implements Converter, Serializable{

    @EJB
    private MercaderiasFacade facade;

    private List<Mercaderias> mercaderias;

    @PostConstruct
    public void instanciar() {
        mercaderias = facade.listarMercaderiasActivas();
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.trim().equals("")) {
            return null;

        } else {

            return mercaderias.stream()
                    .filter(m -> m.getMercaderiasPK().getCodMerca().equals(value))
                    .findAny()
                    .orElse(null);
            //return new Mercaderias(new MercaderiasPK(new Short("2"), value));
            //return new Mercaderias(new MercaderiasPK(Integer.parseInt(value), 0));
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
            return String.valueOf(((Mercaderias) value).getMercaderiasPK().getCodMerca());
        }
    }

    
}
