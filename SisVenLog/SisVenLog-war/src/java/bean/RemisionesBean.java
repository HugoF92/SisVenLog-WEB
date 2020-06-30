package bean;

import dao.ConductoresFacade;
import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.ExistenciasFacade;
import dao.ExistenciasSaldosFacade;
import dao.RemisionesDetFacade;
import dao.RemisionesFacade;
import dao.RemisionesFacturasFacade;
import dao.TiposDocumentosFacade;
import dao.TransportistasFacade;
import dao.ZonasFacade;
import entidad.Conductores;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Empleados;
import entidad.Existencias;
import entidad.ExistenciasSaldos;
import entidad.Remisiones;
import entidad.RemisionesDet;
import entidad.RemisionesFacturas;
import entidad.TiposDocumentos;
import entidad.Transportistas;
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
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class RemisionesBean implements Serializable {

    @EJB
    private RemisionesFacade remisionesFacade;
    private Remisiones remisiones = new Remisiones();
    private List<Remisiones> listaRemisiones = new ArrayList<Remisiones>();
    
    private ZonasFacade zonasFacade;
    private Zonas zonas = new Zonas();
    private List<Zonas> listaZonas = new ArrayList<Zonas>();
    
    private TransportistasFacade transportistasFacade;
    private Transportistas transportistas = new Transportistas();
    private List<Transportistas> listaTransportistas = new ArrayList<Transportistas>();
    
    private ExistenciasFacade existenciasFacade;
    private Existencias existencias = new Existencias();
    private List<Existencias> listaExistencias = new ArrayList<Existencias>();
    
    private ExistenciasSaldosFacade existenciasSaldosFacade;
    private ExistenciasSaldos existenciasSaldos = new ExistenciasSaldos();
    private List<ExistenciasSaldos> listaExistenciasSaldos = new ArrayList<ExistenciasSaldos>();
    
    @EJB
    private DepositosFacade depositosFacade;
    
    private Depositos depositos = new Depositos();
    private List<Depositos> listaDepositos = new ArrayList<Depositos>();    
    
    private TiposDocumentosFacade tipoDocumentoFacade;
    private TiposDocumentos tipoDocumento = new TiposDocumentos();
    private List<TiposDocumentos> listaTipoDocumento = new ArrayList<TiposDocumentos>();
    
    @EJB
    private EmpleadosFacade empleadosFacade;
    
    private Empleados empleados = new Empleados();
    private List<Empleados> listaEmpleados = new ArrayList<Empleados>();
    
    private ConductoresFacade conductoresFacade;
    private Conductores conductores = new Conductores();
    private List<Conductores> listaConductores = new ArrayList<Conductores>();
   
    private RemisionesDetFacade remisionesDetFacade;
    private RemisionesDet remisionesDet = new RemisionesDet();
    private List<RemisionesDet> listaRemisionesDet = new ArrayList<RemisionesDet>();
    
    private RemisionesFacturasFacade remisionesFacturasFacade;
    private RemisionesFacturas remisionesFacturas = new RemisionesFacturas();
    private List<RemisionesFacturas> listaRemisionesFacturas = new ArrayList<RemisionesFacturas>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public RemisionesBean() {

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

    public Remisiones getRemisiones() {
        return remisiones;
    }

    public void setRemisiones(Remisiones remisiones) {
        this.remisiones = remisiones;
    }

    public List<Remisiones> getListaRemisiones() {
        return listaRemisiones;
    }

    public void setListaRemisiones(List<Remisiones> listaRemisiones) {
        this.listaRemisiones = listaRemisiones;
    }

    public RemisionesFacade getRemisionesFacade() {
        return remisionesFacade;
    }

    public void setRemisionesFacade(RemisionesFacade remisionesFacade) {
        this.remisionesFacade = remisionesFacade;
    }

    public RemisionesDetFacade getRemisionesDetFacade() {
        return remisionesDetFacade;
    }

    public void setRemisionesDetFacade(RemisionesDetFacade remisionesDetFacade) {
        this.remisionesDetFacade = remisionesDetFacade;
    }

    public RemisionesDet getRemisionesDet() {
        return remisionesDet;
    }

    public void setRemisionesDet(RemisionesDet remisionesDet) {
        this.remisionesDet = remisionesDet;
    }

    public List<RemisionesDet> getListaRemisionesDet() {
        return listaRemisionesDet;
    }

    public void setListaRemisionesDet(List<RemisionesDet> listaRemisionesDet) {
        this.listaRemisionesDet = listaRemisionesDet;
    }

    public RemisionesFacturasFacade getRemisionesFacturasFacade() {
        return remisionesFacturasFacade;
    }

    public void setRemisionesFacturasFacade(RemisionesFacturasFacade remisionesFacturasFacade) {
        this.remisionesFacturasFacade = remisionesFacturasFacade;
    }

    public RemisionesFacturas getRemisionesFacturas() {
        return remisionesFacturas;
    }

    public void setRemisionesFacturas(RemisionesFacturas remisionesFacturas) {
        this.remisionesFacturas = remisionesFacturas;
    }

    public List<RemisionesFacturas> getListaRemisionesFacturas() {
        return listaRemisionesFacturas;
    }

    public void setListaRemisionesFacturas(List<RemisionesFacturas> listaRemisionesFacturas) {
        this.listaRemisionesFacturas = listaRemisionesFacturas;
    }

    public ZonasFacade getZonasFacade() {
        return zonasFacade;
    }

    public void setZonasFacade(ZonasFacade zonasFacade) {
        this.zonasFacade = zonasFacade;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public DepositosFacade getDepositosFacade() {
        return depositosFacade;
    }

    public void setDepositosFacade(DepositosFacade depositosFacade) {
        this.depositosFacade = depositosFacade;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public List<Depositos> getListaDepositos() {
        return listaDepositos;
    }

    public void setListaDepositos(List<Depositos> listaDepositos) {
        this.listaDepositos = listaDepositos;
    }

    public TiposDocumentosFacade getTipoDocumentoFacade() {
        return tipoDocumentoFacade;
    }

    public void setTipoDocumentoFacade(TiposDocumentosFacade tipoDocumentoFacade) {
        this.tipoDocumentoFacade = tipoDocumentoFacade;
    }

    public TiposDocumentos getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentos tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<TiposDocumentos> getListaTipoDocumento() {
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List<TiposDocumentos> listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public EmpleadosFacade getEmpleadosFacade() {
        return empleadosFacade;
    }

    public void setEmpleadosFacade(EmpleadosFacade empleadosFacade) {
        this.empleadosFacade = empleadosFacade;
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

    public ConductoresFacade getConductoresFacade() {
        return conductoresFacade;
    }

    public void setConductoresFacade(ConductoresFacade conductoresFacade) {
        this.conductoresFacade = conductoresFacade;
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

    public TransportistasFacade getTransportistasFacade() {
        return transportistasFacade;
    }

    public void setTransportistasFacade(TransportistasFacade transportistasFacade) {
        this.transportistasFacade = transportistasFacade;
    }

    public Transportistas getTransportistas() {
        return transportistas;
    }

    public void setTransportistas(Transportistas transportistas) {
        this.transportistas = transportistas;
    }

    public List<Transportistas> getListaTransportistas() {
        return listaTransportistas;
    }

    public void setListaTransportistas(List<Transportistas> listaTransportistas) {
        this.listaTransportistas = listaTransportistas;
    }

    public ExistenciasFacade getExistenciasFacade() {
        return existenciasFacade;
    }

    public void setExistenciasFacade(ExistenciasFacade existenciasFacade) {
        this.existenciasFacade = existenciasFacade;
    }

    public Existencias getExistencias() {
        return existencias;
    }

    public void setExistencias(Existencias existencias) {
        this.existencias = existencias;
    }

    public List<Existencias> getListaExistencias() {
        return listaExistencias;
    }

    public void setListaExistencias(List<Existencias> listaExistencias) {
        this.listaExistencias = listaExistencias;
    }

    public ExistenciasSaldosFacade getExistenciasSaldosFacade() {
        return existenciasSaldosFacade;
    }

    public void setExistenciasSaldosFacade(ExistenciasSaldosFacade existenciasSaldosFacade) {
        this.existenciasSaldosFacade = existenciasSaldosFacade;
    }

    public ExistenciasSaldos getExistenciasSaldos() {
        return existenciasSaldos;
    }

    public void setExistenciasSaldos(ExistenciasSaldos existenciasSaldos) {
        this.existenciasSaldos = existenciasSaldos;
    }

    public List<ExistenciasSaldos> getListaExistenciasSaldos() {
        return listaExistenciasSaldos;
    }

    public void setListaExistenciasSaldos(List<ExistenciasSaldos> listaExistenciasSaldos) {
        this.listaExistenciasSaldos = listaExistenciasSaldos;
    }

    @PostConstruct
    public void instanciar() {
        listaRemisiones = new ArrayList<Remisiones>();
        this.remisiones = new Remisiones();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);
        listar();
    }

    
    public void nuevo() {
        this.remisiones = new Remisiones(new Date());
        listaRemisiones = new ArrayList<Remisiones>();
    }

    public List<Remisiones> listar() {
        this.listaRemisiones = remisionesFacade.findAll();
        return listaRemisiones;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Remisiones> aux = new ArrayList<Remisiones>();

            if ("".equals(this.remisiones.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = remisionesFacade.buscarPorDescripcion(remisiones.getXdesc());

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


                remisiones.setRemisionesPK(remisiones.getRemisionesPK());
                remisiones.setFalta(new Date());
                remisiones.setCusuario("admin");
               
               // remisionesFacade.insertarRemisiones(remisiones);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                PrimeFaces.current().executeScript("PF('dlgNuevRem').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.remisiones.getRemisionesPK())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                remisiones.setRemisionesPK(remisiones.getRemisionesPK());
                remisionesFacade.edit(remisiones);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditRem').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            remisionesFacade.remove(remisiones);
            this.remisiones = new Remisiones();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacRem').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {
        if ("" != this.remisiones.getXnroRemision()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }
    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (remisiones != null) {

            if (remisiones.getRemisionesPK() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarRem').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevRem').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarRem').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevRem').hide();");
    }

    public Empleados findEntregador(Short codEntregador){
        return empleadosFacade.findEntregador(codEntregador);
    }
    
    public void updateDepo(SelectEvent event){
        Depositos depo = depositosFacade.getDepositoPorCodigo(this.remisiones.getCodDepo());
        this.remisiones.setCodConductor(depo.getCodConductor()==null?null:depo.getCodConductor().getCodConductor());
        this.remisiones.setCodTransp(depo.getCodTransp()==null?null:depo.getCodTransp().getCodTransp());
        List<Empleados> lentre = empleadosFacade.listarEntregadorPorDeposito(this.remisiones.getCodDepo());
        if(lentre.isEmpty()){
            this.remisiones.setCodEntregador(null);
        }else{
            Empleados e = lentre.get(0);
            if(e == null){
                this.remisiones.setCodEntregador(null);
            }else{
                this.remisiones.setCodEntregador(e.getCodDepo());
            }
        }
        this.conductores = depo.getCodConductor();
        this.transportistas = depo.getCodTransp();
    }
    
    
}
