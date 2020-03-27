package bean;

import dao.DocumVariosFacade;
import entidad.DocumVarios;
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
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class ObservacionesBean2 implements Serializable {

    @EJB
    private DocumVariosFacade xdescFacade;

    private String filtro = "";

    private DocumVarios xdesc = new DocumVarios();
    private List<DocumVarios> listaDocumVarios = new ArrayList<DocumVarios>();
    

    private TiposDocumentos tiposDocumentos = new TiposDocumentos();
    private List<TiposDocumentos> listarTiposDocumentos = new ArrayList<TiposDocumentos>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public ObservacionesBean2() {

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

    public DocumVarios getDocumVarios() {
        return xdesc;
    }

    public void setDocumVarios(DocumVarios xdesc) {
        this.xdesc = xdesc;
    }

    public List<DocumVarios> getListaDocumVarios() {
        return listaDocumVarios;
    }

    public void setListaDocumVarios(List<DocumVarios> listaDocumVarios) {
        this.listaDocumVarios = listaDocumVarios;
    }

    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public List<TiposDocumentos> getListarTiposDocumentos() {
        return listarTiposDocumentos;
    }

    public void setListarTiposDocumentos(List<TiposDocumentos> listarTiposDocumentos) {
        this.listarTiposDocumentos = listarTiposDocumentos;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaDocumVarios = new ArrayList<DocumVarios>();
        this.xdesc = new DocumVarios();
        
        listarTiposDocumentos = new ArrayList<TiposDocumentos>();
        this.tiposDocumentos = new TiposDocumentos();
        
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

//        RequestContext.getCurrentInstance().update("formDocumVarios");
        PrimeFaces.current().ajax().update("formDocumVarios");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.xdesc = new DocumVarios();
    }

    public List<DocumVarios> listar() {
        listaDocumVarios = xdescFacade.findAll();
        return listaDocumVarios;
    }

    public void insertar() {
        try {
            
            if (tiposDocumentos == null ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar una linea"));
                return;
            }
            
            xdesc.setXobs(xdesc.getXobs().toUpperCase());

            xdesc.setFalta(new Date());
            xdesc.setCusuario("admin");
            
          //  xdescFacade.insertarDocumVarios(xdesc);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            PrimeFaces.current().executeScript("PF('dlgNuevDocumVarios').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.xdesc.getXobs())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                xdesc.setXobs(xdesc.getXobs().toUpperCase());
                xdescFacade.edit(xdesc);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditDocumVarios').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            xdescFacade.remove(xdesc);
            this.xdesc = new DocumVarios();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacDocumVarios').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.xdesc.getXobs()) {
            this.setHabBtnEdit(false);
                    }
        else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (xdesc != null) {

            if (xdesc.getXobs() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarDocumVarios').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevDocumVarios').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        PrimeFaces.current().executeScript("PF('dlgSinGuardarDocumVarios').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevDocumVarios').hide();");

    }
    public List<TiposDocumentos> listaTiposDocumentos(){
        return this.getListarTiposDocumentos();
    }

}
