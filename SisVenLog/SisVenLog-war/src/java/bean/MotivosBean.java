package bean;

import dao.MotivosFacade;
import entidad.Motivos;
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
public class MotivosBean implements Serializable {

    @EJB
    private MotivosFacade motivosFacade;

    private String filtro = "";

    private Motivos motivos = new Motivos();
    private List<Motivos> listaMotivos = new ArrayList<Motivos>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public MotivosBean() {

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

    public Motivos getMotivos() {
        return motivos;
    }

    public void setMotivos(Motivos motivos) {
        this.motivos = motivos;
    }

    public List<Motivos> getListaMotivos() {
        return listaMotivos;
    }

    public void setListaMotivos(List<Motivos> listaMotivos) {
        this.listaMotivos = listaMotivos;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaMotivos = new ArrayList<Motivos>();
        this.motivos = new Motivos();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

//    RequestContext.getCurrentInstance().update("formMotivos");
        PrimeFaces.current().ajax().update("formMotivos");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.motivos = new Motivos();
    }

    public List<Motivos> listar() {
        listaMotivos = motivosFacade.findAll();
        return listaMotivos;
    }


    public void insertar() {
        try {

            motivos.setXdesc(motivos.getXdesc().toUpperCase());
            motivos.setFalta(new Date());
            motivos.setCusuario("admin");

            motivosFacade.insertarMotivos(motivos);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            PrimeFaces.current().executeScript("PF('dlgNuevMotivos').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.motivos.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                motivos.setXdesc(motivos.getXdesc().toUpperCase());
                motivosFacade.edit(motivos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditMotivos').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            motivosFacade.remove(motivos);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacMotivos').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.motivos.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (motivos != null) {

            if (motivos.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarMotivos').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevMotivos').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        PrimeFaces.current().executeScript("PF('dlgSinGuardarMotivos').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevMotivos').hide();");

    }

}
