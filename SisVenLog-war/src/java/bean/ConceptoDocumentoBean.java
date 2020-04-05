package bean;

import dao.ConceptosDocumentosFacade;
import dao.TiposDocumentosFacade;
import entidad.ConceptosDocumentos;
import entidad.ConceptosDocumentosPK;
import entidad.TiposDocumentos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class ConceptoDocumentoBean implements Serializable {

    @EJB
    private ConceptosDocumentosFacade conceptoDocumentoFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    private TiposDocumentos tiposDocumentos = new TiposDocumentos();

    private String filtro = "";

    private ConceptosDocumentos conceptoDocumento = new ConceptosDocumentos();
    private List<ConceptosDocumentos> listaConceptoDocumento = new ArrayList<ConceptosDocumentos>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public ConceptoDocumentoBean() {

        //instanciar();
    }

    public boolean isHabBtnEdit() {
        return habBtnEdit;
    }

    public void setHabBtnEdit(boolean habBtnEdit) {
        this.habBtnEdit = habBtnEdit;
    }

    public boolean isHabBtnAct() {
        return habBtnAct;
    }

    public void setHabBtnAct(boolean habBtnAct) {
        this.habBtnAct = habBtnAct;
    }

    public boolean isHabBtnInac() {
        return habBtnInac;
    }

    public void setHabBtnInac(boolean habBtnInac) {
        this.habBtnInac = habBtnInac;
    }

    public ConceptosDocumentos getConceptoDocumento() {
        return conceptoDocumento;
    }

    public void setConceptoDocumento(ConceptosDocumentos conceptoDocumento) {
        this.conceptoDocumento = conceptoDocumento;
    }

    public List<ConceptosDocumentos> getListaConceptoDocumento() {
        return listaConceptoDocumento;
    }

    public void setListaConceptoDocumento(List<ConceptosDocumentos> listaConceptoDocumento) {
        this.listaConceptoDocumento = listaConceptoDocumento;
    }

    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaConceptoDocumento = new ArrayList<ConceptosDocumentos>();
        this.conceptoDocumento = new ConceptosDocumentos(new ConceptosDocumentosPK());
        this.tiposDocumentos = new TiposDocumentos();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        conceptoDocumento.setMafectaStock('S');
               
        listar();

    RequestContext.getCurrentInstance().update("formConceptoDocumento");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.conceptoDocumento = new ConceptosDocumentos();
    }

    public List<ConceptosDocumentos> listar() {
        listaConceptoDocumento = conceptoDocumentoFacade.findAll();
        return listaConceptoDocumento;
    }


    public void insertar() {
        try {
            
            conceptoDocumento.getConceptosDocumentosPK().setCtipoDocum(tiposDocumentos.getCtipoDocum());
            conceptoDocumento.setXdesc(conceptoDocumento.getXdesc().toUpperCase());
            conceptoDocumento.setFalta(new Date());
            conceptoDocumento.setCusuario("admin");

            conceptoDocumentoFacade.create(conceptoDocumento);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevConceptoDocumento').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.conceptoDocumento.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                conceptoDocumento.setXdesc(conceptoDocumento.getXdesc().toUpperCase());
                conceptoDocumentoFacade.edit(conceptoDocumento);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditConceptoDocumento').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            conceptoDocumentoFacade.remove(conceptoDocumento);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacConceptoDocumento').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.conceptoDocumento.getXdesc()) {
            this.setHabBtnEdit(false);
            this.tiposDocumentos = tiposDocumentosFacade.find(conceptoDocumento.getConceptosDocumentosPK().getCtipoDocum());
        } else {
            this.setHabBtnEdit(true);
        }

        
    }
    
     public String conceptoDocumentoDC(Float indice) {

        String retorno = "";

        Float aux = indice;

        if (aux < 0) {
            retorno = "CREDITO";
        }
        if (aux >= 0) {
            retorno = "DEBITO";
        }
        return retorno;

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (conceptoDocumento != null) {

            if (conceptoDocumento.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarConceptoDocumento').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevConceptoDocumento').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarConceptoDocumento').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevConceptoDocumento').hide();");

    }

}
