package bean;

import dao.ServiciosFacade;
import entidad.Servicios;
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
public class ServiciosBean implements Serializable {

    @EJB
    private ServiciosFacade serviciosFacade;

   
    private Servicios servicios = new Servicios();
    private List<Servicios> listaServicios = new ArrayList<Servicios>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public ServiciosBean() {

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

    public Servicios getServicios() {
        return servicios;
    }

    public void setServicios(Servicios servicios) {
        this.servicios = servicios;
    }

    public List<Servicios> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<Servicios> listaServicios) {
        this.listaServicios = listaServicios;
    }

        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaServicios = new ArrayList<Servicios>();
        this.servicios = new Servicios();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.servicios = new Servicios();
        listaServicios = new ArrayList<Servicios>();
 
    }

    public List<Servicios> listar() {
        listaServicios = serviciosFacade.findAll();
        return listaServicios;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Servicios> aux = new ArrayList<Servicios>();

            if ("".equals(this.servicios.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = serviciosFacade.buscarPorDescripcion(servicios.getXdesc());

                /*if (aux.size() > 0) {
                    valido = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ya existe."));
                }
            }
        } catch (Exception e) {
            valido = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al validar los datos."));
        }

        return valido;
    }
*/ 
    public void insertar() {
        try {


                servicios.setXdesc(servicios.getXdesc().toUpperCase());
                servicios.setFalta(new Date());
                servicios.setCsuario("admin");
               
                serviciosFacade.insertarServicios(servicios);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                RequestContext.getCurrentInstance().execute("PF('dlgNuevServicios').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.servicios.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                servicios.setXdesc(servicios.getXdesc().toUpperCase());
                serviciosFacade.edit(servicios);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditServicios').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            serviciosFacade.remove(servicios);
            this.servicios = new Servicios();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacServicios').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.servicios.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (servicios != null) {

            if (servicios.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarServicios').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevServicios').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarServicios').hide();");
            RequestContext.getCurrentInstance().execute("PF('dlgNuevServicios').hide();");

    }

}
