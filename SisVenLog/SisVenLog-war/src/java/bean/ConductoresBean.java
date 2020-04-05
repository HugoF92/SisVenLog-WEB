package bean;

import dao.ConductoresFacade;
import entidad.Conductores;
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
public class ConductoresBean implements Serializable {

    @EJB
    private ConductoresFacade conductoresFacade;

    private String filtro = "";

    private Conductores conductores = new Conductores();
    private List<Conductores> listaConductores = new ArrayList<Conductores>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public ConductoresBean() {

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

    public Conductores getConductores() {
        return conductores;
    }

    public void setConductores(Conductores conductores) {
        this.conductores = conductores;
    }

    public List<Conductores> getListaConductores() {
        return listaConductores;
    }

    public void setListaConductores(List<Conductores> listaConductores) {
        this.listaConductores = listaConductores;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaConductores = new ArrayList<Conductores>();
        this.conductores = new Conductores();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

//    RequestContext.getCurrentInstance().update("formConductores");
        PrimeFaces.current().ajax().update("formConductores");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.conductores = new Conductores();
    }

    public List<Conductores> listar() {
        listaConductores = conductoresFacade.findAll();
        return listaConductores;
    }


    public void insertar() {
        try {

            conductores.setXconductor(conductores.getXconductor().toUpperCase());
            conductores.setXdocum(conductores.getXdocum().toUpperCase());
            conductores.setXdomicilio(conductores.getXdomicilio().toUpperCase());
            conductores.setFalta(new Date());
            conductores.setCusuario("admin");

            conductoresFacade.insertarConductores(conductores);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

//            RequestContext.getCurrentInstance().execute("PF('dlgNuevConductores').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevConductores').hide();");
            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.conductores.getXconductor())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                conductores.setXconductor(conductores.getXconductor().toUpperCase());
                conductoresFacade.edit(conductores);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

//                RequestContext.getCurrentInstance().execute("PF('dlgEditConductores').hide();");
                PrimeFaces.current().executeScript("PF('dlgEditConductores').hide();");
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            conductoresFacade.remove(conductores);
            this.conductores = new Conductores();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
//            RequestContext.getCurrentInstance().execute("PF('dlgInacConductores').hide();");
            PrimeFaces.current().executeScript("PF('dlgInacConductores').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.conductores.getXconductor()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (conductores != null) {

            if (conductores.getXconductor()!= null) {
                cargado = true;
            }
        }

        if (cargado) {
//            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarConductores').show();");
            PrimeFaces.current().executeScript("PF('dlgSinGuardarConductores').show();");
        } else {
//            RequestContext.getCurrentInstance().execute("PF('dlgNuevConductores').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevConductores').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
//        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarConductores').hide();");
        PrimeFaces.current().executeScript("PF('dlgSinGuardarConductores').hide();");
//        RequestContext.getCurrentInstance().execute("PF('dlgNuevConductores').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevConductores').hide();");
    }

}
