package bean;

import dao.EmpleadosFacade;
import dao.FacturasFacade;
import dao.RecaudacionDetFacade;
import dao.RecaudacionFacade;
import dao.ZonasFacade;
import entidad.Empleados;
import entidad.Recaudacion;
import entidad.RecaudacionDet;
import entidad.RecaudacionPK;
import entidad.Zonas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.SortEvent;

@ManagedBean
@SessionScoped
public class RecaudacionBean implements Serializable {

    private List<Recaudacion> listaRecaudacion = new ArrayList<>();
    private Recaudacion recaudacion = new Recaudacion();
    @EJB
    private EmpleadosFacade entregadorFacade;
    @EJB
    private RecaudacionFacade recaudacionFacade;
    @EJB
    private RecaudacionDetFacade recaudacionDetFacade;
    private List<Zonas> listaZonas;
    @EJB
    private ZonasFacade zonasFacade;
    private List<Empleados> listaEntregadores;
    @EJB
    private FacturasFacade facturasFacade;
    
    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;
    private boolean nuevo;

    public RecaudacionBean() {
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

    @PostConstruct
    public void instanciar() {
        listaRecaudacion = this.recaudacionFacade.findAll();
        this.recaudacion = new Recaudacion();
        RecaudacionPK recaudacionPK = new RecaudacionPK(new Short("2"),this.recaudacionFacade.getNroPlanilla().add(BigDecimal.ONE).longValueExact());
        this.recaudacion.setRecaudacionPK(recaudacionPK);
        this.recaudacion.setFplanilla(new Date());
        this.listaZonas = zonasFacade.listarZonasActivas();
        this.listaEntregadores = new ArrayList<>();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);       
        this.nuevo = true;
    }
    
    public void prepareEdit(){
        this.nuevo = false;
    }

    public Empleados getEntregador(short codEntregador){
        return this.entregadorFacade.getEntregadorByCod(codEntregador);
    }
    
    public void insertar() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = format.format(this.recaudacion.getFplanilla());
        Integer entregador = this.facturasFacade.findFacturaByCodEntregadorFecha(this.recaudacion.getCodEntregador().getEmpleadosPK().getCodEmpleado(),fecha);
        if(entregador==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "No existen facturas de venta para este entregador en la fecha ."+fecha));
            PrimeFaces.current().executeScript("PF('dlgNuevRecaudacion').hide();");
        }else{
            this.recaudacion.setTventas(0);
            this.recaudacion.setTnotasDev(0);
            this.recaudacion.setTcreditos(0);
            this.recaudacion.setTnotasOtras(0);
            this.recaudacion.setTchequesDif(0);
            this.recaudacion.setTpagares(new Long("0"));
            this.recaudacion.setTnotasAtras(new Long("0"));
            this.recaudacion.setTventas(this.facturasFacade.totaldeFacturasPorFechaEntregador(fecha,this.recaudacion.getCodEntregador().getEmpleadosPK().getCodEmpleado()));
            this.recaudacion.setTnotasDev(this.facturasFacade.totaldeDevolucionesPorFechaEntregador(fecha,this.recaudacion.getCodEntregador().getEmpleadosPK().getCodEmpleado()));
            this.recaudacion.setTcreditos(this.facturasFacade.totaldeCreditosPorFechaEntregador(fecha,this.recaudacion.getCodEntregador().getEmpleadosPK().getCodEmpleado()));
            this.recaudacion.setTnotasOtras(this.facturasFacade.totaldeNotasOtrasPorFechaEntregador(fecha,this.recaudacion.getCodEntregador().getEmpleadosPK().getCodEmpleado()));
            this.recaudacion.setTchequesDif(this.facturasFacade.totaldeChequesDiffPorFechaEntregador(fecha,this.recaudacion.getCodEntregador().getEmpleadosPK().getCodEmpleado()));
            this.recaudacion.setTpagares(this.facturasFacade.totaldePagaresPorFechaEntregador(fecha,this.recaudacion.getCodEntregador().getEmpleadosPK().getCodEmpleado()).longValue());
            this.recaudacion.setTnotasAtras(this.facturasFacade.totaldeNotasAtrasPorFechaEntregador(fecha,this.recaudacion.getCodEntregador().getEmpleadosPK().getCodEmpleado()).longValue());
            if(this.nuevo){
                this.recaudacionFacade.create(this.recaudacion);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Recaudacion creada exitosamente."));
            }else{
                this.recaudacionFacade.edit(this.recaudacion);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Recaudacion editada exitosamente."));
            }
            this.recaudacionDetFacade.deleteRecaudacionDet(this.recaudacion.getRecaudacionPK().getNroPlanilla());
            RecaudacionDet det = new RecaudacionDet(new Short("2"),this.recaudacion.getRecaudacionPK().getNroPlanilla(),this.recaudacion.getCodZona());
            det.setTventas(this.recaudacion.getTventas());
            det.setTnotasDev(this.recaudacion.getTnotasDev());
            det.setTcreditos(this.recaudacion.getTcreditos());
            det.setTnotasOtras(this.recaudacion.getTnotasOtras());
            det.setTnotasAtras(this.recaudacion.getTnotasAtras());
            this.recaudacionDetFacade.create(det);
            PrimeFaces.current().executeScript("PF('dlgNuevRecaudacion').hide();");
        }
    }

    public void editar() {
//        try {

//            if ("".equals(this.bancos.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

//            } else {

//                bancos.setXdesc(bancos.getXdesc().toUpperCase());
//                bancosFacade.edit(bancos);

//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

//                instanciar();

//                listar();

//                RequestContext.getCurrentInstance().execute("PF('dlgEditBanc').hide();");
//                PrimeFaces.current().executeScript("PF('dlgEditBanc').hide();");
//            }

//        } catch (Exception e) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
//        }
    }

    public void borrar() {
//        this.recaudacionFacade.remove(this.recaudacion);
        this.recaudacionFacade.deleteRecaudacion(this.recaudacion.getRecaudacionPK().getNroPlanilla());
        this.listaRecaudacion = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
        PrimeFaces.current().ajax().update("tablaRecaudacion");
        PrimeFaces.current().executeScript("PF('dlgInacRecaudacion').hide();");
    }

    public void onRowSelect(SelectEvent event) {
        if (this.recaudacion.getRecaudacionPK()!=null) {
            System.out.println("EL: "+event.getObject());
            this.recaudacion = (Recaudacion) event.getObject();
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }
    }

    public void cerrarDialogosAgregar() {
        PrimeFaces.current().executeScript("PF('dlgSinGuardarBanc').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevBanc').hide();");
    }

    public List<Recaudacion> getListaRecaudacion() {
        if(this.listaRecaudacion==null){
            return this.recaudacionFacade.findAll();
        }else{
            return this.listaRecaudacion;
        }
    }

    public void setListaRecaudacion(List<Recaudacion> listaRecaudacion) {
        this.listaRecaudacion = listaRecaudacion;
    }

    public Recaudacion getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(Recaudacion recaudacion) {
        this.recaudacion = recaudacion;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public List<Empleados> getListaEntregadores() {
        return this.entregadorFacade.findEntregadores();
//        if(this.recaudacion !=null && this.recaudacion.getCodZona()!=null){
//            return this.entregadorFacade.findEntregadorByZona(this.recaudacion.getCodZona());
//        }
//        return this.listaEntregadores;
    }

    public void setListaEntregadores(List<Empleados> listaEntregadores) {
        this.listaEntregadores = listaEntregadores;
    }

    public void ordenacion( SortEvent event) {
        if(event.getSortColumn().getHeaderText().equals("Fecha")){
            if(event.isAscending()){
                Collections.sort(this.listaRecaudacion, new Comparator<Recaudacion>() {
                    public int compare(Recaudacion h1, Recaudacion h2) {
                        return h1.getFplanilla().compareTo(h2.getFplanilla());
                    }
                });                 
            }else{
                //descending
                Collections.sort(this.listaRecaudacion, new Comparator<Recaudacion>() {
                    public int compare(Recaudacion h1, Recaudacion h2) {
                        return h1.getFplanilla().compareTo(h2.getFplanilla())*-1;
                    }
                });                 
            }
        }
    }

}
