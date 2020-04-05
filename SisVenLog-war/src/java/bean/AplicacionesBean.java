package bean;

import dao.AplicacionesFacade;
import entidad.Aplicaciones;
import entidad.AplicacionesPK;
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
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class AplicacionesBean implements Serializable {

    @EJB
    private AplicacionesFacade aplicacionesFacade;

    private String filtro = "";

    private Aplicaciones aplicaciones = new Aplicaciones();
    private List<Aplicaciones> listaAplicaciones = new ArrayList<Aplicaciones>();
    

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public AplicacionesBean() {

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

    public Aplicaciones getAplicaciones() {
        return aplicaciones;
    }

    public void setAplicaciones(Aplicaciones aplicaciones) {
        this.aplicaciones = aplicaciones;
    }

    public List<Aplicaciones> getListaAplicaciones() {
        return listaAplicaciones;
    }

    public void setListaAplicaciones(List<Aplicaciones> listaAplicaciones) {
        this.listaAplicaciones = listaAplicaciones;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaAplicaciones = new ArrayList<Aplicaciones>();
        this.aplicaciones = new Aplicaciones(new AplicacionesPK());
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

    RequestContext.getCurrentInstance().update("formAplicaciones");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.aplicaciones = new Aplicaciones();
    }

    public List<Aplicaciones> listar() {
        listaAplicaciones = aplicacionesFacade.findAll();
        return listaAplicaciones;
    }
    
  //  public void getDatosEditar() {
      //  setAplicaciones(aplicaciones.setAplicacionesPK(aplicaciones));
        
   // }


    public void insertar() {
        try {

            aplicaciones.setXdesc(aplicaciones.getXdesc().toUpperCase());
            aplicaciones.setFalta(new Date());
            aplicaciones.setCusuario("admin");
            

             aplicacionesFacade.create(aplicaciones);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevAplicaciones').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.aplicaciones.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                aplicaciones.setXdesc(aplicaciones.getXdesc().toUpperCase());
                aplicacionesFacade.edit(aplicaciones);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditAplicaciones').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            aplicacionesFacade.remove(aplicaciones);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacAplicaciones').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.aplicaciones.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (aplicaciones != null) {

            if (aplicaciones.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarAplicaciones').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevAplicaciones').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarAplicaciones').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevAplicaciones').hide();");

    }
    
   

}
