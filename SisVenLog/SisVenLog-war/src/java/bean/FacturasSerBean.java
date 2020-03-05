package bean;

import dao.ClientesFacade;
import dao.CuentasCorrientesFacade;
import dao.FacturasDetFacade;
import dao.FacturasFacade;
import dao.FacturasSerFacade;
import dao.MovimientosMercaFacade;
import dao.ProveedoresFacade;
import entidad.Clientes;
import entidad.CuentasCorrientes;
import entidad.Facturas;
import entidad.FacturasDet;
import entidad.FacturasSer;
import entidad.MovimientosMerca;
import entidad.Proveedores;
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
public class FacturasSerBean implements Serializable {

    @EJB
    
    private FacturasDetFacade facturaDetFacade;
    private FacturasSerFacade facturasSerFacade;
    private ProveedoresFacade proveedoresFacade;
    private FacturasFacade facturasFacade;
    private ClientesFacade  clientesFacade;
    private CuentasCorrientesFacade cuentasCorrientesFacade;
    private MovimientosMercaFacade movimientosMercaFacade;
   
        
    private Facturas facturas = new Facturas();
    private List<Facturas> listaFacturas = new ArrayList<Facturas>();
    
    private FacturasDet facturasDet = new FacturasDet();
    private List<FacturasDet> listaFacturasDet = new ArrayList<FacturasDet>();
    
    private FacturasSer facturasSer = new FacturasSer();
    private List<FacturasSer> listaFacturasSer = new ArrayList<FacturasSer>();
    
    private Proveedores proveedores = new Proveedores();
    private List<Proveedores> listaProveedores = new ArrayList<Proveedores>();
    
    private Clientes clientes = new Clientes();
    private List<Clientes> listaClientes = new ArrayList<Clientes>();
        
    private CuentasCorrientes cuentasCorrientes = new CuentasCorrientes();
    private List<CuentasCorrientes> listaCuentasCorrientes = new ArrayList<CuentasCorrientes>();
    
    private MovimientosMerca movimientosMerca = new MovimientosMerca();
    private List<MovimientosMerca> listaMovimientosMerca = new ArrayList<MovimientosMerca>();
    

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public FacturasSerBean() {

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

    public Facturas getFacturas() {
        return facturas;
    }

    public void setFacturas(Facturas facturas) {
        this.facturas = facturas;
    }

    public List<Facturas> getListaFacturas() {
        return listaFacturas;
    }

    public void setListaFacturas(List<Facturas> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }

    public FacturasFacade getFacturasFacade() {
        return facturasFacade;
    }

    public void setFacturasFacade(FacturasFacade facturasFacade) {
        this.facturasFacade = facturasFacade;
    }

    public FacturasDetFacade getFacturaDetFacade() {
        return facturaDetFacade;
    }

    public void setFacturaDetFacade(FacturasDetFacade facturaDetFacade) {
        this.facturaDetFacade = facturaDetFacade;
    }

    public FacturasSerFacade getFacturasSerFacade() {
        return facturasSerFacade;
    }

    public void setFacturasSerFacade(FacturasSerFacade facturasSerFacade) {
        this.facturasSerFacade = facturasSerFacade;
    }

    public FacturasDet getFacturasDet() {
        return facturasDet;
    }

    public void setFacturasDet(FacturasDet facturasDet) {
        this.facturasDet = facturasDet;
    }

    public List<FacturasDet> getListaFacturasDet() {
        return listaFacturasDet;
    }

    public void setListaFacturasDet(List<FacturasDet> listaFacturasDet) {
        this.listaFacturasDet = listaFacturasDet;
    }

    public FacturasSer getFacturasSer() {
        return facturasSer;
    }

    public void setFacturasSer(FacturasSer facturasSer) {
        this.facturasSer = facturasSer;
    }

    public List<FacturasSer> getListaFacturasSer() {
        return listaFacturasSer;
    }

    public void setListaFacturasSer(List<FacturasSer> listaFacturasSer) {
        this.listaFacturasSer = listaFacturasSer;
    }

    public ProveedoresFacade getProveedoresFacade() {
        return proveedoresFacade;
    }

    public void setProveedoresFacade(ProveedoresFacade proveedoresFacade) {
        this.proveedoresFacade = proveedoresFacade;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public ClientesFacade getClientesFacade() {
        return clientesFacade;
    }

    public void setClientesFacade(ClientesFacade clientesFacade) {
        this.clientesFacade = clientesFacade;
    }

     public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public CuentasCorrientesFacade getCuentasCorrientesFacade() {
        return cuentasCorrientesFacade;
    }

    public void setCuentasCorrientesFacade(CuentasCorrientesFacade cuentasCorrientesFacade) {
        this.cuentasCorrientesFacade = cuentasCorrientesFacade;
    }

    public MovimientosMercaFacade getMovimientosMercaFacade() {
        return movimientosMercaFacade;
    }

    public void setMovimientosMercaFacade(MovimientosMercaFacade movimientosMercaFacade) {
        this.movimientosMercaFacade = movimientosMercaFacade;
    }

    public CuentasCorrientes getCuentasCorrientes() {
        return cuentasCorrientes;
    }

    public void setCuentasCorrientes(CuentasCorrientes cuentasCorrientes) {
        this.cuentasCorrientes = cuentasCorrientes;
    }

    public List<CuentasCorrientes> getListaCuentasCorrientes() {
        return listaCuentasCorrientes;
    }

    public void setListaCuentasCorrientes(List<CuentasCorrientes> listaCuentasCorrientes) {
        this.listaCuentasCorrientes = listaCuentasCorrientes;
    }

    public MovimientosMerca getMovimientosMerca() {
        return movimientosMerca;
    }

    public void setMovimientosMerca(MovimientosMerca movimientosMerca) {
        this.movimientosMerca = movimientosMerca;
    }

    public List<MovimientosMerca> getListaMovimientosMerca() {
        return listaMovimientosMerca;
    }

    public void setListaMovimientosMerca(List<MovimientosMerca> listaMovimientosMerca) {
        this.listaMovimientosMerca = listaMovimientosMerca;
    }

       
    
    
        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaFacturas = new ArrayList<Facturas>();
        this.facturas = new Facturas();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.facturas = new Facturas();
        listaFacturas = new ArrayList<Facturas>();
 
    }

    public List<Facturas> listar() {
        listaFacturas = facturasFacade.findAll();
        return listaFacturas;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Facturas> aux = new ArrayList<Facturas>();

            if ("".equals(this.facturas.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = facturasFacade.buscarPorDescripcion(facturas.getXdesc());

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


                facturas.setXrazonSocial(facturas.getXrazonSocial().toUpperCase());
                facturas.setFalta(new Date());
                facturas.setCusuario("admin");
               
            //    facturasFacade.insertarFacturas(facturas);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                PrimeFaces.current().executeScript("PF('dlgNuevFactSer').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.facturas.getXrazonSocial())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                facturas.setXrazonSocial(facturas.getXrazonSocial().toUpperCase());
                facturasFacade.edit(facturas);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditFactSer').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            facturasFacade.remove(facturas);
            this.facturas = new Facturas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacFactSer').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.facturas.getXrazonSocial()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (facturas != null) {

            if (facturas.getXrazonSocial() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarFactSer').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevFactSer').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarFactSer').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevFactSer').hide();");

    }

}
