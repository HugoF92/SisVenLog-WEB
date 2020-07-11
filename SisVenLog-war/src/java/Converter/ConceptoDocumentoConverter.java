/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import entidad.ConceptosDocumentos;
import entidad.ConceptosDocumentosPK;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author Bruno Brizuela
 */
@Named(value = "conceptoDocumentoConverter")
@ApplicationScoped
public class ConceptoDocumentoConverter implements Converter, Serializable {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value){
        if (value == null || value.length() == 0)
        {
            return null;
        }
        else
        {
            String values[] = value.split("#");
            ConceptosDocumentosPK key = new ConceptosDocumentosPK();
            key.setCtipoDocum(values[0]);
            key.setCconc(values[1]);
            return new ConceptosDocumentos(key);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value){
        if (value == null || value.equals(""))
        {
            return "";
        }
        else
        {
            return String.valueOf(
                    ((ConceptosDocumentos) value).getConceptosDocumentosPK().getCtipoDocum() 
                    + "#" +
                    ((ConceptosDocumentos) value).getConceptosDocumentosPK().getCconc());
        }
    }
    
}
