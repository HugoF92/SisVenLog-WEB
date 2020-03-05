package bean;

import dao.CanalesVentaFacade;
import entidad.CanalesVenta;
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
public class CanalesVenBean implements Serializable {

    @EJB
    private CanalesVentaFacade canalesVenFacade;

   
    private CanalesVenta canalesVen = new CanalesVenta();
    private List<CanalesVenta> listaCanalesVen = new ArrayList<CanalesVenta>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public CanalesVenBean() {

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

    public CanalesVenta getCanales() {
        return canalesVen;
    }

    public void setCanales(CanalesVenta canalesVen) {
        this.canalesVen = canalesVen;
    }

    public List<CanalesVenta> getListaCanales() {
        return listaCanalesVen;
    }

    public void setListaCanales(List<CanalesVenta> listaCanalesVen) {
        this.listaCanalesVen = listaCanalesVen;
    }

        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaCanalesVen = new ArrayList<CanalesVenta>();
        this.canalesVen = new CanalesVenta();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.canalesVen = new CanalesVenta();
        listaCanalesVen = new ArrayList<CanalesVenta>();
 
    }

    public List<CanalesVenta> listar() {
        listaCanalesVen = canalesVenFacade.findAll();
        return listaCanalesVen;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Canales> aux = new ArrayList<Canales>();

            if ("".equals(this.canalesVen.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = canalesVenFacade.buscarPorDescripcion(canalesVen.getXdesc());

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


                canalesVen.setXdesc(canalesVen.getXdesc().toUpperCase());
                canalesVen.setFalta(new Date());
                canalesVen.setCusuario("admin");
               
                canalesVenFacade.create(canalesVen);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

//                RequestContext.getCurrentInstance().execute("PF('dlgNuevCanalesVen').hide();");
                PrimeFaces.current().executeScript("PF('dlgNuevCanalesVen').hide();");
                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.canalesVen.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                canalesVen.setXdesc(canalesVen.getXdesc().toUpperCase());
                canalesVenFacade.edit(canalesVen);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

//                RequestContext.getCurrentInstance().execute("PF('dlgEditCanalesVen').hide();");
                PrimeFaces.current().executeScript("PF('dlgEditCanalesVen').hide();");
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            canalesVenFacade.remove(canalesVen);
            this.canalesVen = new CanalesVenta();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
//            RequestContext.getCurrentInstance().execute("PF('dlgInacCanalesVen').hide();");
            PrimeFaces.current().executeScript("PF('dlgInacCanalesVen').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.canalesVen.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (canalesVen != null) {

            if (canalesVen.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
//            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarCanalesVen').show();");
            PrimeFaces.current().executeScript("PF('dlgSinGuardarCanalesVen').show();");
        } else {
//            RequestContext.getCurrentInstance().execute("PF('dlgNuevCanalesVen').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevCanalesVen').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
//            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarCanales').hide();");
            PrimeFaces.current().executeScript("PF('dlgSinGuardarCanales').hide();");
//            RequestContext.getCurrentInstance().execute("PF('dlgNuevCanales').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevCanales').hide();");
    }

}
