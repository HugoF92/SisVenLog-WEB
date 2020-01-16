package bean;

import dao.DepositosFacade;
import dao.EmpleadosFacade;
import entidad.CanalesVenta;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Empleados;
import entidad.Sublineas;
import entidad.TiposEmpleados;
import entidad.Zonas;
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
public class EmpleadosBean implements Serializable {

    @EJB
    private EmpleadosFacade empleadosFacade;

    @EJB
    private DepositosFacade depositosFacade;

    private String filtro = "";

    private Empleados empleados = new Empleados();
    private Depositos depositos = new Depositos(new DepositosPK());
    private TiposEmpleados tiposEmpleados = new TiposEmpleados();
    private List<Empleados> listaEmpleados = new ArrayList<Empleados>();
    private List<Zonas> listaZonas = new ArrayList<Zonas>();
    private List<CanalesVenta> listaCanalesVenta = new ArrayList<CanalesVenta>();
    private List<Sublineas> listaSublineas = new ArrayList<Sublineas>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public EmpleadosBean() {

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

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public List<Empleados> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public TiposEmpleados getTiposEmpleados() {
        return tiposEmpleados;
    }

    public void setTiposEmpleados(TiposEmpleados tiposEmpleados) {
        this.tiposEmpleados = tiposEmpleados;
    }

    public List<CanalesVenta> getListaCanalesVenta() {
        return listaCanalesVenta;
    }

    public void setListaCanalesVenta(List<CanalesVenta> listaCanalesVenta) {
        this.listaCanalesVenta = listaCanalesVenta;
    }

    public List<Sublineas> getListaSublineas() {
        return listaSublineas;
    }

    public void setListaSublineas(List<Sublineas> listaSublineas) {
        this.listaSublineas = listaSublineas;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaEmpleados = new ArrayList<Empleados>();
        listaZonas = new ArrayList<Zonas>();
        listaSublineas = new ArrayList<Sublineas>();
        listaCanalesVenta = new ArrayList<CanalesVenta>();
        this.empleados = new Empleados();
        depositos = new Depositos(new DepositosPK());
        tiposEmpleados = new TiposEmpleados();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";

        listar();
        RequestContext.getCurrentInstance().update("formEmpleados");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.empleados = new Empleados();
    }

    public List<Empleados> listar() {
        listaEmpleados = empleadosFacade.findAll();
        return listaEmpleados;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public void insertar() {
        try {

            empleados.setXnombre(empleados.getXnombre().toUpperCase());
            empleados.setXnroHand(empleados.getXnroHand().toUpperCase());
            empleados.setCodDepo(depositos.getDepositosPK().getCodDepo());
            empleados.setCtipoEmp(tiposEmpleados);
            empleados.setMestado('A');
            
            empleados.setFalta(new Date());
            empleados.setCusuario("admin");

            empleadosFacade.insertarEmpleados(empleados);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevEmpleados').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.empleados.getXnombre())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                empleados.setXnombre(empleados.getXnombre().toUpperCase());
                empleados.setCtipoEmp(tiposEmpleados);
                empleados.setCodDepo(depositos.getDepositosPK().getCodDepo());
                empleadosFacade.edit(empleados);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditEmpleados').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            empleadosFacade.remove(empleados);
            this.empleados = new Empleados();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacEmpleados').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.empleados.getXnombre()) {
            
            DepositosPK pk = new DepositosPK();
            pk.setCodEmpr(new Short("2"));
            pk.setCodDepo(empleados.getCodDepo());
            
            depositos=depositosFacade.find(pk);
            tiposEmpleados = empleados.getCtipoEmp();
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (empleados != null) {

            if (empleados.getXnombre()!= null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarEmpleados').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevEmpleados').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarEmpleados').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevEmpleados').hide();");

    }

}
