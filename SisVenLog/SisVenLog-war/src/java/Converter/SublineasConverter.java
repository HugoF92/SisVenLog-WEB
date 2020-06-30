/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import dao.SublineasFacade;
import entidad.Sublineas;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
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
@Named(value = "SublineasConverter")
@ApplicationScoped
public class SublineasConverter  implements Converter, Serializable{

    @EJB
    private SublineasFacade facade;
    
    private List<Sublineas> sublineas;

    @PostConstruct
    public void instanciar() {
        sublineas = facade.listarSublineasActivas();
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.trim().equals("")) {
            return null;
        } else {
            return sublineas.stream()
                    .filter(s -> s.getCodSublinea().equals(Short.parseShort(value)))
                    .findAny()
                    .orElse(null);
            //return new Sublineas(Short.parseShort(value));
            //return new Sublineas(new SublineasPK(Integer.parseInt(value), 0));
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
            return String.valueOf(((Sublineas) value).getCodSublinea());
        }
    }

    
}
