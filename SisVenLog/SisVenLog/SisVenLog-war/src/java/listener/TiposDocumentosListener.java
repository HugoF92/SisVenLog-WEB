/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import bean.GenDocuAnul;
import entidad.TiposDocumentos;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
/**
 *
 * @author Asus
 */
public class TiposDocumentosListener implements ValueChangeListener {
   
   @Override
   public void processValueChange(ValueChangeEvent event)
      throws AbortProcessingException {
      
      //access country bean directly
      GenDocuAnul genDocuAnul = (GenDocuAnul) FacesContext.getCurrentInstance().
      getExternalContext().getSessionMap().get("genDocuAnul");
      genDocuAnul.setTiposDocumentos((TiposDocumentos)event.getNewValue());
   }
}
