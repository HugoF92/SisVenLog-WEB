/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.CanalesVendedoresFacade;
import dao.ClientesFacade;
import dao.CuentasCorrientesFacade;
import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.EmpleadosZonasFacade;
import dao.ExistenciasFacade;
import dao.FacturasDetFacade;
import dao.FacturasFacade;
import dao.MercaImpuestosFacade;
import dao.MercaderiasFacade;
import dao.MotivosFacade;
import dao.MovimientosMercaFacade;
import dao.PedidosFacade;
import dao.PreciosFacade;
import dao.PromocionesFacade;
import dao.RangosDocumentosFacade;
import dao.RetornosPreciosFacade;
import dao.SaldosClientesFacade;
import dao.TiposDocumentosFacade;
import dao.TiposVentasFacade;
import dto.FacturaDetDto;
import dto.MovimientoMercaDto;
import dto.PromocionDto;
import dto.PromocionesDetDto;
import entidad.CanalesVendedores;
import entidad.Clientes;
import entidad.CuentasCorrientes;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Empleados;
import entidad.EmpleadosZonas;
import entidad.Existencias;
import entidad.Facturas;
import entidad.FacturasDet;
import entidad.FacturasDetPK;
import entidad.FacturasPK;
import entidad.Impuestos;
import entidad.Mercaderias;
import entidad.Motivos;
import entidad.MovimientosMerca;
import entidad.Precios;
import entidad.Promociones;
import entidad.PromocionesPK;
import entidad.RangosDocumentos;
import entidad.RetornosPrecios;
import entidad.SaldosClientes;
import entidad.TiposDocumentos;
import entidad.TiposVentas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.DateUtil;
import util.ExceptionHandlerView;

/**
 *
 * @author dadob
 */

@ManagedBean
@SessionScoped 
public class FacturasBean implements Serializable{
    
    @EJB
    private ClientesFacade clientesFacade;
    
    @EJB
    private TiposVentasFacade tiposVentasFacade;
    
    @EJB
    private DepositosFacade depositosFacade;
    
    @EJB
    private PedidosFacade pedidosFacade;
    
    @EJB
    private SaldosClientesFacade saldosClientesFacade;
    
    @EJB
    private CuentasCorrientesFacade cuentasCorrientesFacade;
    
    @EJB
    private FacturasFacade facturasFacade;
    
    @EJB
    private RangosDocumentosFacade rangosDocumentosFacade;
    
    @EJB
    private ExistenciasFacade existenciasFacade;
    
    @EJB
    private MercaderiasFacade mercaderiasFacade;
    
    @EJB
    private FacturasDetFacade facturasDetFacade;
    
    @EJB
    private PromocionesFacade promocionesFacade;
    
    @EJB
    private MovimientosMercaFacade movimientosMercaFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private PreciosFacade preciosFacade;
    
    @EJB
    private EmpleadosZonasFacade empleadosZonasFacade;
    
    @EJB
    private CanalesVendedoresFacade canalesVendedoresFacade;
    
    @EJB
    private EmpleadosFacade empleadosFacade;
    
    @EJB
    private MercaImpuestosFacade mercaImpuestosFacade;
    
    @EJB
    private RetornosPreciosFacade retornosPreciosFacade;
    
    @EJB
    private MotivosFacade motivosFacade;
    
    private Date fechaFactLbl;
    private Integer codClienteLbl;
    private String nombreClienteLbl;
    private Short nPuntoEstabLbl;
    private Short nPuntoExpedLbl;
    private long nroFactLbl;
    private short codDepositoLbl;
    private long nroPedidoLbl;
    private Character ctipoVtaLbl;
    private Date fechaVencLbl;
    private Date fechaVencImpresLbl;
    private int cantDiasChequesLbl;
    private int cantDiasImpresionChequesLbl;
    private short plazosChequesLbl;
    private short codVendedorLbl;
    private String codCanalVentaLbl;
    private TiposDocumentos tipoDocumentoSeleccionadoLbl;
    private String contenidoError;
    private String tituloError;
    private List<TiposVentas> listadoTiposVentas;
    private List<Depositos> listadoDepositos;
    private Clientes clienteBuscado;
    private boolean mostrarCantDiasChequesLbl;
    private boolean mostrarCantDiasImpresionChequesLbl;
    private boolean mostrarPlazosChequesLbl;
    private boolean mostrarTotalIva;
    private boolean mostrarFechaVencimiento;
    private boolean mostrarFechaVencimientoImpresion;
    private long totalFinal;
    private long totalIva;
    private long totalExentas;
    private long totalGravadas;
    private long totalDescuentos;
    private Depositos depositoSeleccionado;
    private String codZonaLbl;
    private String observacionLbl;
    private short codEntregadorLbl;
    private String direccionLbl;
    private String razonSocialLbl;
    private String telefonoLbl;
    private String ciudadLbl;
    private short codRutaLbl;
    private Facturas facturaSeleccionada;
    private List<Facturas> listadoFacturas;
    private Short cMotivoLbl;
    private Date fechaAnulacionLbl;
    //variables del detalle
    private String codMercaDet;
    private int cantCajasDet;
    private int cantUnidDet;
    private int cantCajasBonifDet;
    private int cantUnidBonifDet;
    private Mercaderias mercaderiaDet;
    private List<FacturaDetDto> listadoDetalle;
    private List<FacturasDet> listadoFacturasDet;
    private String descMercaDet;
    private long precioCajaDet;
    private long precioUnidDet;
    private long gravadasDet;
    private long exentasDet;
    private long impuestosDet;
    private long descuentosDet;
    private long totalDet;
    private Integer nroPromoDet;
    private String descPromoDet;
    private BigDecimal pdescDet;
    private BigDecimal pimpuesDet;
    private Short nRelacionDet;
    private Character mPromoPackDet;
    private Short codSublineaDet;
    private BigDecimal nPesoCajaDet;
    private BigDecimal nPesoUnidadDet;
    private boolean habilitaBotonEliminar;
    private List<TiposDocumentos> listadoTiposDocumentos;
    private List<EmpleadosZonas> listadoVendedores;
    private List<CanalesVendedores> listadoCanales;
    private List<Empleados> listadoEntregadores;
    private String filtro;
    private FacturaDetDto facturaDetDtoSeleccionada;
    private Character mGravExeDet;
    private long precioCajaSimDet;
    private long precioUnidadSimDet;
    private long gravadas2Det;
    private Clientes clientes;
    private List<Clientes> listaClientes;
    private List<Existencias> listaExistencias;
    private Existencias existencias;
    private LazyDataModel<Existencias> model;
    private boolean habilitaBotonVisualizar;
    HashMap<String, TiposDocumentos> tipoDocumentoEnMemoria;
    HashMap<String, Depositos> depositoEnMemoria;
    private List<Motivos> listadoMotivos;
    
    @PostConstruct
    public void init(){
        limpiarFormulario();
    }
    
    public void cambiaDeposito(){
        try{
            if(codDepositoLbl != 0){
                DepositosPK dPK = new DepositosPK();
                dPK.setCodEmpr(Short.parseShort("2"));
                dPK.setCodDepo(codDepositoLbl);
                depositoSeleccionado = depositosFacade.find(dPK);
                depositoEnMemoria.put("deposito", depositoSeleccionado);
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al cambiar depósito.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }        
    }
    
    public void cambiaPlazoCheque() {
        try {
            if (clienteBuscado != null) {
                if (plazosChequesLbl > clienteBuscado.getNplazoCredito()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Plazo máximo del cliente es: " + clienteBuscado.getNplazoCredito() + " días."));
                }
            }
        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al cambiar plazo cheque.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    public void cambiaTipoDocumento() {
        try {
            if(tipoDocumentoSeleccionadoLbl != null){
                tipoDocumentoSeleccionadoLbl = tiposDocumentosFacade.find(this.tipoDocumentoSeleccionadoLbl.getCtipoDocum());
                tipoDocumentoEnMemoria.put("tipo_documento", tipoDocumentoSeleccionadoLbl);
                mostrarTotalIva = this.tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S';
                if (tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCR")) {
                    mostrarFechaVencimiento = false;
                    mostrarFechaVencimientoImpresion = false;
                } else {
                    mostrarFechaVencimiento = true;
                    mostrarFechaVencimientoImpresion = true;
                }
            }
        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al cambiar tipos de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    public void cambiaVendedor() {
        listadoCanales = new ArrayList<>();
        if (codVendedorLbl != 0) {
            try {
                listadoCanales = canalesVendedoresFacade.obtenerCanalesVendedores(codVendedorLbl);
                //obtener codZonaLbl
                for(EmpleadosZonas ez: listadoVendedores){
                    if(ez.getEmpleados().getEmpleadosPK().getCodEmpleado() == codVendedorLbl){
                        codZonaLbl = ez.getZonas().getZonasPK().getCodZona();
                        break;  //salimos del for.
                    }
                }
            } catch (Exception e) {
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en la lectura de datos de canales_vendedores.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            }
        }
    }
   
    public void limpiarFormulario(){
        listadoTiposVentas = new ArrayList<>();
        listadoDepositos = new ArrayList<>();
        clienteBuscado = new Clientes();
        mostrarCantDiasChequesLbl = false;
        mostrarCantDiasImpresionChequesLbl = false;
        mostrarPlazosChequesLbl = false;
        mostrarTotalIva = false;
        depositoSeleccionado = new Depositos();
        mercaderiaDet = new Mercaderias();
        listadoDetalle = new ArrayList<>();
        facturaSeleccionada = new Facturas();
        listadoFacturas = new ArrayList<>();
        listadoFacturasDet = new ArrayList<>();
        //variables factura
        fechaFactLbl = new Date();
        codClienteLbl = null;
        nombreClienteLbl = null;
        nPuntoEstabLbl = 0;
        nPuntoExpedLbl = 0;
        nroFactLbl = 0;
        codDepositoLbl = 0;
        nroPedidoLbl = 0;
        ctipoVtaLbl = 0;
        fechaVencLbl = null;
        fechaVencImpresLbl = null;
        cantDiasChequesLbl = 0;
        cantDiasImpresionChequesLbl = 0;
        plazosChequesLbl = 0;
        codVendedorLbl = 0;
        codCanalVentaLbl = null;
        tipoDocumentoSeleccionadoLbl = null;
        contenidoError = null;
        tituloError = null;
        mostrarCantDiasChequesLbl = true;
        mostrarCantDiasImpresionChequesLbl = true;
        mostrarPlazosChequesLbl = true;
        totalFinal = 0;
        totalIva = 0;
        totalExentas = 0;
        totalGravadas = 0;
        totalDescuentos = 0;
        codZonaLbl = null;
        observacionLbl = null;
        codEntregadorLbl = 0;
        direccionLbl = null;
        razonSocialLbl = null;
        telefonoLbl = null;
        ciudadLbl = null;
        codRutaLbl = 0;
        cMotivoLbl = 0;
        fechaAnulacionLbl = null;
        //variables del detalle
        codMercaDet = null;
        cantCajasDet = 0;
        cantUnidDet = 0;
        cantCajasBonifDet = 0;
        cantUnidBonifDet = 0;
        mercaderiaDet = new Mercaderias();
        descMercaDet = null;
        precioCajaDet = 0;
        precioUnidDet = 0;
        gravadasDet = 0;
        exentasDet = 0;
        impuestosDet = 0;
        descuentosDet = 0;
        nroPromoDet = 0;
        descPromoDet = null;
        pdescDet = BigDecimal.ZERO;
        pimpuesDet = BigDecimal.ZERO;
        nRelacionDet = 0;
        mPromoPackDet = 0;
        codSublineaDet = 0;
        nPesoUnidadDet = null;
        nPesoCajaDet = null;
        habilitaBotonEliminar = true;
        habilitaBotonVisualizar = true;
        listadoTiposDocumentos = new ArrayList<>();
        listadoVendedores = new ArrayList<>();
        listadoCanales = new ArrayList<>();
        listadoEntregadores = new ArrayList<>();
        filtro = null;
        totalDet = 0;
        facturaDetDtoSeleccionada = new FacturaDetDto();
        mostrarFechaVencimiento = false;
        mostrarFechaVencimientoImpresion = false;
        mGravExeDet = 0;
        precioCajaSimDet = 0;
        precioUnidadSimDet = 0;
        gravadas2Det = 0;
        clientes = new Clientes();
        listaClientes = new ArrayList<>();
        listaExistencias = new ArrayList<>();
        existencias = new Existencias();
        tipoDocumentoEnMemoria = new HashMap<>();
        depositoEnMemoria = new HashMap<>();
        listadoMotivos = new ArrayList<>();
        listarFacturas();
    }
    
    public List<Motivos> listarMotivos(){
        listadoMotivos = new ArrayList<>();
        try{
            listadoMotivos = motivosFacade.findAll();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de motivos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoMotivos;
    }
    
    public List<Empleados> listarEntregadores(){
        listadoEntregadores = new ArrayList<>();
        try{
            listadoEntregadores = empleadosFacade.listarEntregador();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de empleados.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoEntregadores;
    }
    
    public List<CanalesVendedores> listarCanales(){
        listadoCanales = new ArrayList<>();
        try{
            listadoCanales = canalesVendedoresFacade.obtenerCanalesVendedores(codVendedorLbl);
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de canales_vendedores.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoCanales;
    }
    
    public List<EmpleadosZonas> listarVendedoresZonas(){
        listadoVendedores = new ArrayList<>();
        try{
            listadoVendedores = empleadosZonasFacade.obtenerEmpleadosZonas();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de empleados_zonas.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoVendedores;
    }
        
    public List<TiposDocumentos> listarTiposDocumentos(){
        listadoTiposDocumentos = new ArrayList<>();
        try{
            listadoTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoFactura();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de tipos de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoTiposDocumentos;
    }
    
    public void listarFacturas(){
        try{
            listadoFacturas = facturasFacade.listadoDeFacturas();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de facturas.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
    }
    
    public String comprobarNroPromo(){
        descPromoDet = null;
        if(pdescDet.compareTo(BigDecimal.ZERO) != 0){
            //si hay descuento, es obligatorio el nro. de promocion.
            if(nroPromoDet == 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe ingresar el nro. de promoción."));
                return null;
            }else{
                try{
                    String lFFactura = dateToString(fechaFactLbl);
                    List<Promociones> listadoPromo = promocionesFacade.findByNroPromoYFechaFactura(nroPromoDet, lFFactura);
                    if (listadoPromo.isEmpty()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No existe promoción"));
                        return null;
                    }
                    if (cantCajasDet > 0 || cantUnidDet > 0) {
                        //validar promocion
                        if (mPromoPackDet == 'N') {
                            if (validarPromocion(nroPromoDet,
                                    codZonaLbl,
                                    codMercaDet,
                                    cantCajasDet,
                                    cantUnidDet,
                                    cantUnidBonifDet,
                                    clienteBuscado.getCtipoCliente(),
                                    pdescDet,
                                    codSublineaDet,
                                    ctipoVtaLbl,
                                    "",
                                    nRelacionDet,
                                    obtenerNroFacturaCompleto())){
                                for(Promociones p: listadoPromo){
                                    descPromoDet = p.getXdesc();
                                }
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Ingrese cajas/unidades facturadas."));
                            return null;
                        }
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la lectura de datos de promociones.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return null;
                }
            }
        }
        return null;
    }
    
    public String comprobarCodMerca(){
        
            if(codMercaDet.equals("") || codMercaDet == null){
                //mostrar busqueda de mercaderías
                RequestContext.getCurrentInstance().execute("PF('dlgBusMercaFact').show();");
            }else{
                try{
                    if(codDepositoLbl == 0){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe seleccionar el depósito."));
                        return null;
                    }
                    if(codCanalVentaLbl.equals("") || codCanalVentaLbl == null){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe seleccionar el canal de venta."));
                        return null;
                    }
                    listaExistencias = existenciasFacade.buscarPorCodigoDepositoOrigenMerca(codMercaDet, codDepositoLbl, codCanalVentaLbl);
                    if(!listaExistencias.isEmpty()){
                        for(Existencias e: listaExistencias){
                            descMercaDet = e.getMercaderias().getXdesc();
                            mPromoPackDet = e.getMercaderias().getMpromoPack();
                            codSublineaDet = e.getMercaderias().getCodSublinea().getCodSublinea();
                            nRelacionDet = e.getMercaderias().getNrelacion() == null ? 0 : e.getMercaderias().getNrelacion().shortValue();
                        }
                    }else{
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Mercadería no existe."));
                        return null;
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la lectura de datos de mercaderías.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return null;
                }
            }
     
        return null;
    }
    
    public void inicializarBuscadorClientes(){
        listaClientes = new ArrayList<>();
        clientes = new Clientes();
        filtro = "";
        listarClientesBuscador();
    }
    
    public void listarClientesBuscador(){
        try{
            listaClientes = clientesFacade.buscarPorFiltro(filtro);
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de clientes.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        
    }
    
    public void inicializarBuscadorMercaderia(){
        listaExistencias = new ArrayList<>();
        existencias = new Existencias();
        filtro = "";
        if(codDepositoLbl == 0){
            RequestContext.getCurrentInstance().execute("PF('dlgBusMercaFact').hide();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe seleccionar el depósito."));
            return;
        }
        if(codCanalVentaLbl.equals("") || codCanalVentaLbl == null){
            RequestContext.getCurrentInstance().execute("PF('dlgBusMercaFact').hide();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe seleccionar el canal de venta."));
            return;
        }
        model = new LazyDataModel<Existencias>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Existencias> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                //List<Envios> envioss;
                int count;
                
                if("".equals(filtro)){
                
                    listaExistencias = existenciasFacade.buscarPorCodigoDepositoOrigenTodasMerca(codDepositoLbl, new int[]{first, pageSize}, codCanalVentaLbl);
                    count = existenciasFacade.CountBuscarPorCodigoDepositoOrigenTodasMerca(codDepositoLbl, codCanalVentaLbl);
                    model.setRowCount(count);
                }else{
                    listaExistencias = existenciasFacade.buscarListaPorCodigoDepositoOrigenMercaDescripcion(filtro,codDepositoLbl,new int[]{first, pageSize}, codCanalVentaLbl );
                    count = existenciasFacade.CountBuscarPorCodigoDepositoOrigenTodasMercaConFiltro(filtro,codDepositoLbl,codCanalVentaLbl);
                    model.setRowCount(count);
                }
                return listaExistencias;
            }

            @Override
            public Existencias getRowData(String rowKey) {
                String tempIndex = rowKey;
                System.out.println("1");
                if (tempIndex != null) {
                    for (Existencias inc : listaExistencias) {
                        if (inc.getExistenciasPK().getCodMerca().equals(rowKey)) {
                            existencias = inc;
                            return inc;
                        }
                    }
                }

                return null;
            }

            @Override
            public Object getRowKey(Existencias existenciass) {
                return existenciass.getExistenciasPK().getCodMerca();
            }
        };
    }
    
    public void listarMercaderiasEnBuscador(){
    }
    
    public void onRowMercaderiasSelect(SelectEvent event) {
        if (this.existencias != null) {
            if (this.existencias.getExistenciasPK().getCodMerca() != null) {
                codMercaDet = this.existencias.getExistenciasPK().getCodMerca();
                descMercaDet = this.existencias.getMercaderias().getXdesc();
                mPromoPackDet = this.existencias.getMercaderias().getMpromoPack();
                codSublineaDet = this.existencias.getMercaderias().getCodSublinea().getCodSublinea();
                nRelacionDet = this.existencias.getMercaderias().getNrelacion() == null ? 0 : this.existencias.getMercaderias().getNrelacion().shortValue();
                RequestContext.getCurrentInstance().update("panel_buscador_articulo");
                RequestContext.getCurrentInstance().execute("PF('dlgBusMercaFact').hide();");
            }
        }
    }
    
    public void onRowClientesSelect(SelectEvent event) {
        if (getClientes() != null) {
            if (getClientes().getXnombre() != null) {
                codClienteLbl = getClientes().getCodCliente();
                nombreClienteLbl = getClientes().getXnombre();
                RequestContext.getCurrentInstance().update("panel_grid_nueva_factura");
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaFactura').hide();");
            }
        }
    }
    
    public void cambiarFechaFactura(){
        fechaVencLbl = null;
        fechaVencImpresLbl = null;
        try {
            if (tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCR")) {
                fechaVencLbl = DateUtil.sumarRestarDiasFecha(fechaFactLbl, clienteBuscado.getNplazoCredito());
                fechaVencImpresLbl = DateUtil.sumarRestarDiasFecha(fechaFactLbl, clienteBuscado.getNplazoImpresion());
            }
        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al sumar plazos de crédito a la fecha de la factura.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
        
    }
    
    public String verificarCliente(){
        if(codClienteLbl != null){
            if(codClienteLbl == 0){
                //mostrar busqueda de clientes
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaFactura').show();");
            }else{
                try{
                    clienteBuscado = clientesFacade.find(codClienteLbl);
                    if(clienteBuscado == null){
                        //mostrar busqueda de clientes
                        RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaFactura').show();");
                    }else{
                        this.nombreClienteLbl = clienteBuscado.getXnombre();
                        if(clienteBuscado.getMformaPago() == 'C' && !tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCO")){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Tipo inválido de factura."));
                            return null;
                        }
                        if(clienteBuscado.getMformaPago() == 'F' && !tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCR")){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Tipo inválido de factura."));
                            return null;
                        }
                        if(clienteBuscado.getMformaPago() != 0 && !tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCO")){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Tipo inválido de factura."));
                            return null;
                        }
                        listadoTiposVentas = new ArrayList<>();
                        try{
                            //buscar los tipos de venta habilitados para ese cliente para poblar el combo
                            listadoTiposVentas = tiposVentasFacade.obtenerTiposVentasDelCliente(codClienteLbl);
                        }catch(Exception e){
                            RequestContext.getCurrentInstance().update("exceptionDialog");
                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                            tituloError = "Error en la lectura de tipos de ventas de clientes.";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                            return null;
                        }
                        try {
                            //busar los depositos habilitados para poblar el combo
                            listadoDepositos = depositosFacade.obtenerDepositosPorTipoCliente(clienteBuscado.getCtipoCliente());
                        } catch (Exception e) {
                            RequestContext.getCurrentInstance().update("exceptionDialog");
                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                            tituloError = "Error en la lectura de depósitos.";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                            return null;
                        }
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la lectura de datos de clientes.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return null;
                }
            }
        }
        return null;
    }
    
    public String verificarFechaVencimiento(){
        try{
            if(clienteBuscado.getMformaPago() == 'C'){
                if(tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCO")){
                    mostrarCantDiasChequesLbl = true;
                    mostrarCantDiasImpresionChequesLbl = true;
                    mostrarPlazosChequesLbl = false;
                }else{
                    mostrarCantDiasChequesLbl = false;
                    mostrarCantDiasImpresionChequesLbl = false;
                    mostrarPlazosChequesLbl = true;
                }
            }else{
                plazosChequesLbl = 0;
                mostrarCantDiasChequesLbl = true;
                mostrarCantDiasImpresionChequesLbl = true;
                mostrarPlazosChequesLbl = true;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al verificar la fecha de vencimiento.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
            return null;
        }
        return null;
    }
    
    public String verificarFechaVencimientoImpresion(){
        try{
            if(clienteBuscado.getMformaPago() == 'C'){
                if(tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCO")){
                    mostrarCantDiasChequesLbl = true;
                    mostrarCantDiasImpresionChequesLbl = true;
                    mostrarPlazosChequesLbl = false;
                }else{
                    mostrarCantDiasChequesLbl = false;
                    mostrarCantDiasImpresionChequesLbl = false;
                    mostrarPlazosChequesLbl = true;
                }
            }else{
                plazosChequesLbl = 0;
                mostrarCantDiasChequesLbl = true;
                mostrarCantDiasImpresionChequesLbl = true;
                mostrarPlazosChequesLbl = true;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al verificar la fecha de vencimiento de impresión.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
            return null;
        }
        return null;
    }
    
    public String visualizarFactura(){
        //limpiarFormulario();    //inicialmente.
        try{
            FacturasPK facturaPK = new FacturasPK();
            facturaPK.setCodEmpr(Short.parseShort("2"));
            facturaPK.setCtipoDocum(facturaSeleccionada.getFacturasPK().getCtipoDocum());
            facturaPK.setFfactur(facturaSeleccionada.getFacturasPK().getFfactur());
            facturaPK.setNrofact(facturaSeleccionada.getFacturasPK().getNrofact());
            facturaSeleccionada = facturasFacade.find(facturaPK);
            if (facturaSeleccionada.getFacturasPK().getFfactur() == null) {
                //cabecera de la factura
                try {
                    List<Facturas> listado = facturasFacade.listadoDeFacturas(facturaSeleccionada.getFacturasPK().getCtipoDocum(), facturaSeleccionada.getFacturasPK().getNrofact());
                    if (listado.isEmpty()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "El nro. de factura no existe."));
                        return null;
                    } else {
                        for (Facturas f : listado) {
                            tipoDocumentoSeleccionadoLbl = tiposDocumentosFacade.find(f.getFacturasPK().getCtipoDocum());
                            fechaFactLbl = f.getFacturasPK().getFfactur();
                            codClienteLbl = f.getCodCliente().getCodCliente();
                            nombreClienteLbl = f.getCodCliente().getXnombre();
                            nPuntoEstabLbl = Short.parseShort(String.valueOf(f.getFacturasPK().getNrofact()).substring(0, 1));
                            nPuntoExpedLbl = Short.parseShort(String.valueOf(f.getFacturasPK().getNrofact()).substring(1, 3));
                            nroFactLbl = Short.parseShort(String.valueOf(f.getFacturasPK().getNrofact()).substring(3));
                            ctipoVtaLbl = f.getCtipoVta();
                            codVendedorLbl = f.getEmpleados1().getEmpleadosPK().getCodEmpleado();
                            codDepositoLbl = f.getDepositos().getDepositosPK().getCodDepo();
                            fechaVencLbl = f.getFvenc();
                            fechaVencImpresLbl = f.getFvencImpre();
                            codCanalVentaLbl = f.getCodCanal().getCodCanal();
                            nroPedidoLbl = f.getPedidos() != null ? f.getPedidos().getPedidosPK().getNroPedido() : 0;
                            plazosChequesLbl = f.getNplazoCheque();
                            codEntregadorLbl = f.getEmpleados().getEmpleadosPK().getCodEmpleado();
                            observacionLbl = f.getXobs();
                            codZonaLbl = f.getZonas().getZonasPK().getCodZona();
                            totalExentas = f.getTexentas();
                            totalGravadas = f.getTgravadas();
                            totalIva = f.getTimpuestos();
                            totalFinal = f.getTtotal();
                            totalDescuentos = f.getTdescuentos();
                        }
                        //detalles de la factura
                        try {
                            listadoDetalle = new ArrayList<>();
                            String lFFacturaStr = dateToString(facturaSeleccionada.getFacturasPK().getFfactur());
                            List<FacturasDet> listadoDet = facturasDetFacade.obtenerDetallesDeFacturas(facturaSeleccionada.getFacturasPK().getCtipoDocum(), facturaSeleccionada.getFacturasPK().getNrofact(), lFFacturaStr);
                            for(FacturasDet f: listadoDet){
                                FacturaDetDto fddto = new FacturaDetDto();
                                fddto.setFacturasDet(f);
                                PromocionesPK promoPK = new PromocionesPK();
                                promoPK.setCodEmpr(Short.parseShort("2"));
                                promoPK.setNroPromo(f.getNroPromo());
                                fddto.setDescPromo(promocionesFacade.find(promoPK).getXdesc());
                                listadoDetalle.add(fddto);
                            }
                        } catch (Exception e) {
                            RequestContext.getCurrentInstance().update("exceptionDialog");
                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                            tituloError = "Error al obtener detalles de la factura.";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                            return null;
                        }
                    }
                } catch (Exception e) {
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al obtener datos de la factura.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return null;
                }
            }else{
                String lFFacturaStr = dateToString(facturaSeleccionada.getFacturasPK().getFfactur());
                List<Facturas> listado = facturasFacade.listadoDeFacturas(facturaSeleccionada.getFacturasPK().getCtipoDocum(), facturaSeleccionada.getFacturasPK().getNrofact(), lFFacturaStr);
                if(listado.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "El nro. de factura no existe."));
                    return null;
                }else{
                    for (Facturas f : listado) {
                        tipoDocumentoSeleccionadoLbl = tiposDocumentosFacade.find(f.getFacturasPK().getCtipoDocum());
                        fechaFactLbl = f.getFacturasPK().getFfactur();
                        codClienteLbl = f.getCodCliente().getCodCliente();
                        nombreClienteLbl = f.getCodCliente().getXnombre();
                        nPuntoEstabLbl = Short.parseShort(String.valueOf(f.getFacturasPK().getNrofact()).substring(0, 1));
                        nPuntoExpedLbl = Short.parseShort(String.valueOf(f.getFacturasPK().getNrofact()).substring(1, 3));
                        nroFactLbl = Long.parseLong(String.valueOf(f.getFacturasPK().getNrofact()).substring(3));
                        ctipoVtaLbl = f.getCtipoVta();
                        codVendedorLbl = f.getEmpleados1().getEmpleadosPK().getCodEmpleado();
                        codDepositoLbl = f.getDepositos().getDepositosPK().getCodDepo();
                        fechaVencLbl = f.getFvenc();
                        fechaVencImpresLbl = f.getFvencImpre();
                        codCanalVentaLbl = f.getCodCanal().getCodCanal();
                        nroPedidoLbl = f.getPedidos() != null ? f.getPedidos().getPedidosPK().getNroPedido() : 0;
                        plazosChequesLbl = f.getNplazoCheque();
                        codEntregadorLbl = f.getEmpleados().getEmpleadosPK().getCodEmpleado();
                        observacionLbl = f.getXobs();
                        codZonaLbl = f.getZonas().getZonasPK().getCodZona();
                        totalExentas = f.getTexentas();
                        totalGravadas = f.getTgravadas();
                        totalIva = f.getTimpuestos();
                        totalFinal = f.getTtotal();
                        totalDescuentos = f.getTdescuentos();
                    }
                    //detalles de la factura
                    try {
                        listadoDetalle = new ArrayList<>();
                        lFFacturaStr = dateToString(facturaSeleccionada.getFacturasPK().getFfactur());
                        List<FacturasDet> listadoDet = facturasDetFacade.obtenerDetallesDeFacturas(facturaSeleccionada.getFacturasPK().getCtipoDocum(), facturaSeleccionada.getFacturasPK().getNrofact(), lFFacturaStr);
                        for(FacturasDet f: listadoDet){
                            FacturaDetDto fddto = new FacturaDetDto();
                            fddto.setFacturasDet(f);
                            if(f.getNroPromo() != null){
                                PromocionesPK promoPK = new PromocionesPK();
                                promoPK.setCodEmpr(Short.parseShort("2"));
                                promoPK.setNroPromo(f.getNroPromo());
                                fddto.setDescPromo(promocionesFacade.find(promoPK).getXdesc());
                            }else{
                                fddto.setDescPromo(null);
                            }
                            
                            listadoDetalle.add(fddto);
                        }
                    } catch (Exception e) {
                        RequestContext.getCurrentInstance().update("exceptionDialog");
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al obtener detalles de la factura.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return null;
                    }
                }
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al visualizar la factura.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return null;
        }
        return null;
    }
    
    public String anularFactura(){
        try{
            FacturasPK facturaPK = new FacturasPK();
            facturaPK.setCodEmpr(Short.parseShort("2"));
            facturaPK.setCtipoDocum(facturaSeleccionada.getFacturasPK().getCtipoDocum());
            facturaPK.setFfactur(facturaSeleccionada.getFacturasPK().getFfactur());
            facturaPK.setNrofact(facturaSeleccionada.getFacturasPK().getNrofact());
            facturaSeleccionada = facturasFacade.find(facturaPK);
            long lNroFactura = facturaSeleccionada.getFacturasPK().getNrofact();
            Date lFFactura = facturaSeleccionada.getFacturasPK().getFfactur();
            String lFFacturaStr = dateToString(lFFactura);
            String lCTipoDocum = facturaSeleccionada.getFacturasPK().getCtipoDocum();
            TiposDocumentos td = tiposDocumentosFacade.find(lCTipoDocum);
            if(facturaSeleccionada.getMestado() == 'A'){
                if(facturaSeleccionada.getTnotas() > 0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Esta factura tiene notas de créditos relacionadas."));
                    return null;
                }
                if(validarDatosAnulacion()){
                   try{
                       //String lFFactura = dateToString(fechaFactLbl);
                       listadoFacturasDet = facturasDetFacade.obtenerDetallesDeFacturas(lCTipoDocum, lNroFactura, lFFacturaStr);
                   }catch(Exception e){
                       RequestContext.getCurrentInstance().update("exceptionDialog");
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al obtener los datos del detalle de factura.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return null;
                   }
                   if(!listadoFacturasDet.isEmpty()){
                       for(FacturasDet f: listadoFacturasDet){
                           //INSERCION DE MOVIMIENTOS MERCADERIAS
                           try{
                                MovimientosMerca mov = new MovimientosMerca();
                                mov.setNroMovim(1L);
                                mov.setCodEmpr(Short.parseShort("2"));
                                mov.setCodMerca(f.getFacturasDetPK().getCodMerca());
                                mov.setCodDepo(facturaSeleccionada.getDepositos().getDepositosPK().getCodDepo());
                                if(td.getMindice() == null){
                                    td.setMindice(Float.parseFloat("0"));
                                }
                                mov.setCantCajas(((f.getCantCajas() + f.getCajasBonif())*td.getMindice().intValue())*-1);
                                mov.setCantUnid(((f.getCantUnid()+ f.getUnidBonif())*td.getMindice().intValue())*-1);
                                mov.setPrecioCaja(f.getIprecioCaja());
                                mov.setPrecioUnidad(f.getIprecioUnidad());
                                long lGravadas = 0; BigDecimal lImpuestos = null;
                                if(f.getImpuestos().compareTo(BigDecimal.ZERO) == -1){
                                    lGravadas = f.getIgravadas() - (f.getImpuestos().multiply(BigDecimal.valueOf(-1))).longValue();
                                }else{
                                    lImpuestos = (f.getImpuestos().multiply(BigDecimal.valueOf(-1)));
                                }
                                long lExentas = f.getIexentas() * -1;
                                lGravadas = lGravadas * -1;
                                mov.setIgravadas(lGravadas);
                                mov.setIexentas(lExentas);
                                mov.setImpuestos(lImpuestos == null ? 0 : lImpuestos.longValue());
                                mov.setCodVendedor(facturaSeleccionada.getEmpleados1().getEmpleadosPK().getCodEmpleado());
                                mov.setCodCliente(facturaSeleccionada.getCodCliente().getCodCliente());
                                mov.setCodZona(facturaSeleccionada.getZonas().getZonasPK().getCodZona());
                                mov.setCodRuta(facturaSeleccionada.getCodRuta());
                                mov.setNpeso(BigDecimal.ZERO);
                                mov.setManulado(Short.parseShort("-1"));
                                mov.setFmovim(fechaAnulacionLbl);
                                mov.setCtipoDocum(lCTipoDocum);
                                mov.setNdocum(lNroFactura);
                                mov.setNrofact(lNroFactura);
                                mov.setFacCtipoDocum(lCTipoDocum);
                                mov.setFalta(new Date());
                                String usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                                mov.setCusuario(usuario);
                                mov.setCodEntregador(facturaSeleccionada.getEmpleados().getEmpleadosPK().getCodEmpleado());
                                movimientosMercaFacade.insertarMovimientosMerca(mov);
                           }catch(Exception e){
                                contenidoError = ExceptionHandlerView.getStackTrace(e);
                                tituloError = "Error al insertar en movimientos mercaderías.";
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                return null;
                           }
                           //reservar las cantidades en camion si el motivo es 15
                           if(cMotivoLbl == 15){
                               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Actualizando reserva de mercadería "+f.getFacturasDetPK().getCodMerca()));                               
                           }   
                       }
                   }
                   try{
                       Date lFFacturaMenosDos = DateUtil.sumarRestarDiasFecha(lFFactura, -2);
                       String lFFacturaMenosDosStr = dateToString(lFFacturaMenosDos);
                       List<MovimientoMercaDto> listadoMovimientosMerca = movimientosMercaFacade.obtenerMovimientosMercaderiasPorTipoNroFechaMovimiento(lCTipoDocum, lNroFactura, lFFacturaMenosDosStr);
                       if(!listadoMovimientosMerca.isEmpty()){
                           for(MovimientoMercaDto m: listadoMovimientosMerca){
                               if(m.getCantCajas() != 0 || m.getCantUnid() != 0){
                                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Sin movimientos mercaderías de la factura anulada."));
                                   return null;
                               }
                           }
                       }
                   }catch(Exception e){
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al obtener datos en movimientos mercaderías.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return null;
                   }
                   //INSERCION EN CUENTAS CORRIENTES
                   if(td.getMafectaCtacte() == 'S'){
                       try{
                            CuentasCorrientes c = new CuentasCorrientes();
                            c.setCodEmpr(Short.parseShort("2"));
                            c.setCtipoDocum(lCTipoDocum);
                            c.setFmovim(facturaSeleccionada.getFacturasPK().getFfactur());
                            c.setNdocumCheq(String.valueOf(lNroFactura));
                            c.setIpagado(0);
                            c.setIretencion(0);
                            c.setCodCliente(facturaSeleccionada.getCodCliente());
                            c.setIsaldo(facturaSeleccionada.getTtotal());
                            c.setManulado(Short.parseShort("-1"));
                            c.setTexentas(facturaSeleccionada.getTexentas()*-1);
                            long lTImpuestos = 0; long lTGravadas = 0;
                            lTImpuestos = facturaSeleccionada.getTimpuestos();
                            if(lTImpuestos < 0){
                                lTImpuestos *= -1; 
                                lTGravadas -= lTImpuestos;
                            }
                            lTGravadas *= -1;
                            c.setTgravadas(lTGravadas);
                            lTImpuestos = facturaSeleccionada.getTimpuestos() < 0 ? facturaSeleccionada.getTimpuestos() : (facturaSeleccionada.getTimpuestos() * -1);
                            c.setTimpuestos(lTImpuestos);
                            c.setFvenc(facturaSeleccionada.getFvenc());
                            c.setCtipoDocum(lCTipoDocum);
                            c.setNrofact(lNroFactura);
                            if(td.getMindice() == null){
                                td.setMindice(Float.parseFloat("0"));
                            }
                            c.setMindice((short)(td.getMindice().intValue() * -1));
                            c.setFfactur(facturaSeleccionada.getFacturasPK().getFfactur());
                            cuentasCorrientesFacade.insertarCuentas(c);
                       }catch(Exception e){
                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                            tituloError = "Error al insertar en cuentas corrientes.";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                            return null;
                       }
                   }
                   //ACTUALIZACION DE PEDIDOS
                   try{
                       List<Facturas> listado = new ArrayList<>();
                       if(facturaSeleccionada.getPedidos() != null){
                            listado = facturasFacade.obtenerFacturasActivasPorNroPedidoYFactura(facturaSeleccionada.getPedidos().getPedidosPK().getNroPedido(), lNroFactura);
                       }
                       if(listado.isEmpty()){
                           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "El Pedido no cambia de estado. Existen otras facturas a anular."));
                       }else{
                           try{
                                pedidosFacade.actualizarPedidosPorNro(facturaSeleccionada.getPedidos().getPedidosPK().getNroPedido());
                           }catch(Exception e){
                                contenidoError = ExceptionHandlerView.getStackTrace(e);
                                tituloError = "Error al actualizar pedidos.";
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                return null;
                           }
                       }
                   }catch(Exception e){
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al obtener datos de facturas.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return null;
                   }
                    //actualizar facturas
                    try {
                        String lFAnulacion = dateToString(fechaAnulacionLbl);
                        String usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                        facturasFacade.actualizarFacturas(lFAnulacion, usuario, cMotivoLbl, lFFacturaStr, lNroFactura, lCTipoDocum);
                    } catch (Exception e) {
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al actualizar facturas.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return null;
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Factura anulada.", ""));
                    limpiarFormulario();
                    RequestContext.getCurrentInstance().execute("PF('dlgAnularFactura').hide();");
                    return null;
                }
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Estado inválido de la factura."));
                return null;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al anular la factura.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return null;
        }
        return null;
    }
    
    public void inicializarDatosAnulacion(){
        fechaAnulacionLbl = null;
        cMotivoLbl = 0;
    }
    
    private boolean validarDatosAnulacion(){
        if(fechaAnulacionLbl == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Debe ingresar fecha de anulación."));
            return false;
        }else{
            if(cMotivoLbl == 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Debe seleccionar un motivo de anulación."));
                return false;
            }else{
                if(fechaAnulacionLbl.compareTo(DateUtil.sumarRestarDiasFecha(fechaFactLbl, 2)) == 1){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Fecha máxima de anulación: "+DateUtil.sumarRestarDiasFecha(fechaFactLbl, 2)));
                    return false;
                }
            }
        }
        return true;
    }
    
    public void borrarFilaFactura(FacturaDetDto facturaABorrar){
        try{
            Iterator itr = listadoDetalle.iterator();
            while (itr.hasNext()) {
                FacturaDetDto facturaDetDtoEnLista = (FacturaDetDto) itr.next();
                if(facturaDetDtoEnLista.getFacturasDet().getFacturasDetPK().getCtipoDocum().equals(facturaABorrar.getFacturasDet().getFacturasDetPK().getCtipoDocum()) &&
                    facturaDetDtoEnLista.getFacturasDet().getFacturasDetPK().getNrofact() == facturaABorrar.getFacturasDet().getFacturasDetPK().getNrofact() &&
                    facturaDetDtoEnLista.getFacturasDet().getFacturasDetPK().getCodMerca().equals(facturaABorrar.getFacturasDet().getFacturasDetPK().getCodMerca()) &&
                    facturaDetDtoEnLista.getFacturasDet().getFacturasDetPK().getFfactur().compareTo(facturaABorrar.getFacturasDet().getFacturasDetPK().getFfactur()) == 0){
                    itr.remove();
                    calcularTotales();
                    return;
                }
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al borrar un detalle de la grilla de facturas.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
    }
    
    public String agregarMercaderia(){
        try{
            if(verificarArticulo()){
                FacturasDet f = new FacturasDet();
                FacturasDetPK pk = new FacturasDetPK();
                pk.setCodEmpr(Short.parseShort("2"));
                pk.setCodMerca(codMercaDet);
                pk.setCtipoDocum(tipoDocumentoSeleccionadoLbl.getCtipoDocum());
                pk.setFfactur(fechaFactLbl);
                pk.setNrofact(obtenerNroFacturaCompleto());
                f.setFacturasDetPK(pk);
                f.setXdesc(descMercaDet);
                f.setCantCajas(cantCajasDet);
                f.setCantUnid(cantUnidDet);
                f.setIprecioCaja(precioCajaDet);
                f.setIprecioUnidad(precioUnidDet);
                f.setIgravadas(gravadasDet);
                f.setIexentas(exentasDet);
                f.setIdescuentos(descuentosDet);
                f.setImpuestos(BigDecimal.valueOf(impuestosDet));
                f.setItotal(gravadasDet);
                f.setPimpues(pimpuesDet); //tipo de iva (5, 10, etc.)
                f.setPdesc(pdescDet); //% de descuento.
                f.setNroPromo(nroPromoDet); //nro de promocion.
                f.setCajasBonif(cantCajasBonifDet);
                f.setUnidBonif(cantUnidBonifDet);
                FacturaDetDto fdto = new FacturaDetDto();
                fdto.setFacturasDet(f);
                fdto.setnRelacion(nRelacionDet);
                fdto.setDescPromo(descPromoDet);
                fdto.setmPromoPackDet(mPromoPackDet);
                fdto.setCodSublineaDet(codSublineaDet);
                fdto.setnPesoCajas(nPesoCajaDet);
                fdto.setnPesoUnidad(nPesoUnidadDet);
                listadoDetalle.add(fdto);
                calcularTotales();
                //limpiar variables
                codMercaDet = null;
                descMercaDet = null;
                cantCajasDet = 0;
                cantUnidDet = 0;
                cantCajasBonifDet = 0;
                cantUnidBonifDet = 0;
                pdescDet = BigDecimal.ZERO;
                nroPromoDet = null;
                descPromoDet = null;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al agregar una mercadería.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
            return null;
        }
        return null;
    }
    
    public boolean verificarArticulo() {
        List<Existencias> listado = new ArrayList<>();
        long hayUnidad = 0;
        Mercaderias mercaderia = null;
        try{
            if(pdescDet.compareTo(BigDecimal.ZERO) != 0){
                //si hay descuento, es obligatorio el nro. de promocion.
                if(nroPromoDet == 0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe ingresar el nro. de promoción."));
                    return false;
                }
            }
            //Recupera la existencia actual    
            listado = existenciasFacade.buscarPorCodigoDepositoOrigenYArticulo(codDepositoLbl, codMercaDet);
            if(listado.isEmpty()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No existe el articulo en depósito "+codDepositoLbl));
                return false;
            }else{
                //Busca Mercaderia 
                try{
                    mercaderia = mercaderiasFacade.buscarPorCodigoMercaderia(codMercaDet);
                    if(mercaderia == null){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "La mercadería no existe."));
                        return false;
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al leer datos de mercaderías.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return false;
                }
                //BUSCA SI YA EXISTE EN LA FACTURA 
                if(!verificarMecaderiaDuplicada(codMercaDet, obtenerNroFacturaCompleto())){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Mercadería ya existe en la factura."));
                    return false;
                }
                //CALCULAR EXISTENCIA 
                for(Existencias e: listado){
                    long unidadesExist = e.getCantCajas()*e.getNrelacion() + e.getCantUnid();
                    long unidadesReser = e.getReserCajas()*e.getNrelacion() + e.getReserUnid();
                    hayUnidad = unidadesExist - unidadesReser;
                    nRelacionDet = e.getMercaderias().getNrelacion() == null ? 0 : e.getMercaderias().getNrelacion().shortValue();
                    nPesoCajaDet = e.getMercaderias().getNpesoCaja();
                    nPesoUnidadDet = e.getMercaderias().getNpesoUnidad();
                    mGravExeDet = e.getMercaderias().getMgravExe();
                    if(hayUnidad < ((cantCajasDet * e.getNrelacion())+cantUnidDet)){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Solo existen "+hayUnidad+" en depósito."));
                        return false;
                    }
                }
                //Verifica Canal de Venta 
                //la mercadería debe existir en merca_canales
                try{
                    if(!mercaderiasFacade.buscarMercaderiaEnCanalDeVenta(codMercaDet, codCanalVentaLbl)){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "La mercadería no está relacionada con el canal: "+codCanalVentaLbl));
                        return false;
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al leer datos de mercaderías_canales.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return false;
                }
                //PRECIOS
                try{
                    String lFFactura = dateToString(fechaFactLbl);
                    List<Precios> listadoPrecios = preciosFacade.obtenerPreciosPorFechaFacturaCodigoMercaYTipoVenta(lFFactura, codMercaDet, ctipoVtaLbl);
                    if(listadoPrecios.isEmpty()){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No existe precio vigente para: cod_empresa = 2, cod_merca: "+codMercaDet+", y tipo de venta: "+ctipoVtaLbl));
                        return false;
                    }else{
                        for(Precios p: listadoPrecios){
                            if(p.getIprecioCaja() <= 0  || p.getIprecioUnidad() <= 0){
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Precio caja/unidad es igual a cero."));
                                return false;
                            }else{
                                precioCajaDet = p.getIprecioCaja();
                                precioUnidDet = p.getIprecioUnidad();
                                precioCajaSimDet = precioCajaDet;
                                precioUnidadSimDet = precioUnidDet;
                            }
                        }
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al leer datos de precios.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return false;
                }
                //El precio recuperado es SIN IVA    
                //Calcular el impuesto
                if(mGravExeDet == 'G'){ //si el tipo de IVA de la mercaderia es gravada.
                    String lFFactura = dateToString(fechaFactLbl);
                    HashMap<String,Long> resultado = calcularPrecio(codMercaDet, 
                                                                    precioCajaDet, 
                                                                    precioUnidDet, 
                                                                    ctipoVtaLbl, 
                                                                    lFFactura);
                    long calculaBien = resultado.get("calcula_bien");
                    if(calculaBien == 0){
                        return false;
                    }
                    precioCajaDet = resultado.get("precio_caja");
                    precioUnidDet = resultado.get("precio_unidad");
                    pimpuesDet = BigDecimal.valueOf(resultado.get("pimpuest"));
                }
                //Validación de columna Cajas
                //validar cantidad con el stock :  la columna "hay_unid" se cargó en la validación de la mercadería
                //iexentas es  la columna que se visualiza en pantalla 
                if(mGravExeDet == 'E'){
                    exentasDet = precioCajaDet * cantCajasDet + precioUnidDet * cantUnidDet;
                    exentasDet = exentasDet - (exentasDet * (pdescDet.longValue())/100);
                }else{
                    /*
                    ** igravadas2 NO se visualiza en pantalla . Es para guardar el importe sin el impuesto (pre_caja_sim es el precio asi como está guardado en la tabla PRECIOS
                    */
                    gravadas2Det = precioCajaSimDet * cantCajasDet + precioUnidadSimDet * cantUnidDet;
                    tipoDocumentoSeleccionadoLbl = tipoDocumentoEnMemoria.get("tipo_documento");
                    // igravadas es  la columna que se visualiza en pantalla 
                    if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                        gravadasDet = precioCajaDet * cantCajasDet + precioUnidDet * cantUnidDet;
                        impuestosDet = pimpuesDet.compareTo(BigDecimal.valueOf(10)) == 0 ? (gravadasDet/11) : (gravadasDet/21);
                    }else{
                        gravadasDet = gravadas2Det;
                        impuestosDet = gravadas2Det * (pimpuesDet.longValue()/100);
                    }
                    //Aplica el porcentaje ingresado
                    gravadasDet = gravadasDet - (gravadasDet * (pdescDet.longValue())/100);
                    //Calcula el impuesto nuevamente ya con el descuento aplicado
                    if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                        impuestosDet = pimpuesDet.compareTo(BigDecimal.valueOf(10)) == 0 ? (gravadasDet/11) : (gravadasDet/21);
                    }else{
                        gravadasDet = gravadas2Det;
                        impuestosDet = gravadas2Det * (pimpuesDet.longValue()/100);
                    }
                }
                //nPesoCajaDet = cantCajasDet * nPesoCajaDet;
                //Validación de columna Unidades
                //validar cantidad con el stock :  la columna "hay_unid" se cargó en la validación de la mercadería
                if(mGravExeDet == 'E'){
                    exentasDet = precioCajaDet * cantCajasDet + precioUnidDet * cantUnidDet;
                    //Aplica el descuento 
                    exentasDet = exentasDet - (exentasDet * (pdescDet.longValue())/100);
                }else{
                    //guardar el monto sin impuesto
                    gravadas2Det = precioCajaSimDet * cantCajasDet + precioUnidadSimDet * cantUnidDet;
                    // igravadas es  la columna que se visualiza en pantalla 
                    if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                        gravadasDet = precioCajaDet * cantCajasDet + precioUnidDet * cantUnidDet;
                        impuestosDet = pimpuesDet.compareTo(BigDecimal.valueOf(10)) == 0 ? (gravadasDet/11) : (gravadasDet/21);
                    }else{
                        gravadasDet = gravadas2Det;
                        impuestosDet = gravadas2Det * (pimpuesDet.longValue()/100);
                    }
                    // Aplica el descuento
                    gravadasDet = gravadasDet - (gravadasDet * (pdescDet.longValue())/100);
                    gravadas2Det = gravadas2Det - (gravadas2Det * (pdescDet.longValue())/100);
                    //vuelve a calcular el impuesto con el descuento aplicado
                    if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                        impuestosDet = pimpuesDet.compareTo(BigDecimal.valueOf(10)) == 0 ? (gravadasDet/11) : (gravadasDet/21);
                    }else{
                        gravadasDet = gravadas2Det;
                        impuestosDet = gravadas2Det * (pimpuesDet.longValue()/100);
                    }
                }
                //Validación de columna % de Descuento
                //Validar que cajas y/o unidades se hayan ingresado
                if(cantCajasDet <= 0 && cantUnidDet <= 0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Ingrese cantidad cajas/unidades."));
                    return false;
                }else{
                    mPromoPackDet = 'N';
                    codSublineaDet = mercaderia.getCodSublinea().getCodSublinea();
                    // Recalcular los importes de exentas y gravadas aplicando el descuento
                    if(mGravExeDet == 'E'){
                        exentasDet = precioCajaDet * cantCajasDet + precioUnidDet * cantUnidDet;
                        descuentosDet = descuentosDet + (exentasDet * (pdescDet.longValue())/100);
                        //Aplica el descuento 
                        exentasDet = exentasDet - (exentasDet * (pdescDet.longValue())/100);
                    }
                    if(gravadasDet != 0){
                        descuentosDet = descuentosDet + (gravadasDet * (pdescDet.longValue())/100);
                        gravadasDet = gravadasDet - (gravadasDet * (pdescDet.longValue())/100);
                        gravadas2Det = gravadas2Det - (gravadas2Det * (pdescDet.longValue())/100);
                        if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                            impuestosDet = pimpuesDet.compareTo(BigDecimal.valueOf(10)) == 0 ? (gravadasDet/11) : (gravadasDet/21);
                        }else{
                            gravadasDet = gravadas2Det;
                            impuestosDet = gravadas2Det * (pimpuesDet.longValue()/100);
                        }
                    }
                }
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al verificar una mercadería.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
            return false;
        }
        return true;
    }
    
    public void calcularTotales(){
        //Procedimiento interno Suma Total
        //suma las columnas EXENTAS, GRAVADAS, IMPUESTOS
        totalExentas = 0; totalGravadas = 0; totalIva = 0; totalDescuentos = 0; totalFinal = 0;
        for(FacturaDetDto f: listadoDetalle){
            totalExentas += f.getFacturasDet().getIexentas();
            totalGravadas += f.getFacturasDet().getIgravadas();
            totalIva += f.getFacturasDet().getImpuestos().longValue();
            totalDescuentos += f.getFacturasDet().getIdescuentos();
        }
        totalFinal = totalExentas + totalGravadas + totalIva;
    }
    
    public HashMap<String,Long> calcularPrecio( String lCodMerca, 
                                long lPrecioCaja, 
                                long lPrecioUnidad, 
                                Character lTipoVenta, 
                                String lFechaFactura){
        HashMap<String,Long> retorno = new HashMap<>();
        BigDecimal gImpuesto = BigDecimal.ZERO;
        try{
            try{
                List<Impuestos> listadoImpuestos = mercaImpuestosFacade.obtenerTipoImpuestoPorMercaderia(codMercaDet);
                if(!listadoImpuestos.isEmpty()){
                    for(Impuestos i: listadoImpuestos){
                        if(i.getPimpues().compareTo(BigDecimal.ZERO) > 0 || i.getIfijo().compareTo(Integer.parseInt("0")) > 0){
                            lPrecioCaja = ((new BigDecimal(lPrecioCaja).multiply(i.getPimpues().add(new BigDecimal("1")))).add(new BigDecimal(i.getIfijo()))).longValue();
                            //lPrecioUnidad = (lPrecioUnidad * (i.getPimpues().longValue() + 1)) + i.getIfijo().longValue();
                            lPrecioUnidad = ((new BigDecimal(lPrecioUnidad).multiply(i.getPimpues().add(new BigDecimal("1")))).add(new BigDecimal(i.getIfijo()))).longValue();
                        }
                        gImpuesto = i.getPimpues().multiply(BigDecimal.valueOf(100));
                    }
                }
                List<RetornosPrecios> listadoRetornos = retornosPreciosFacade.obtenerRetornosPrecios(lCodMerca, lTipoVenta, lFechaFactura);
                if(!listadoRetornos.isEmpty()){
                    for(RetornosPrecios rp: listadoRetornos){
                        lPrecioCaja = lPrecioCaja - rp.getIretornoCaja();
                        lPrecioUnidad = lPrecioUnidad - rp.getIretornoUnidad();
                    }
                }
                retorno.put("precio_caja", lPrecioCaja);
                retorno.put("precio_unidad", lPrecioUnidad);
                retorno.put("pimpuest", gImpuesto.longValue());
                retorno.put("calcula_bien", Long.parseLong("1"));
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en la generación del cursor de impuestos.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                retorno.put("precio_caja", lPrecioCaja);
                retorno.put("precio_unidad", lPrecioUnidad);
                retorno.put("pimpuest", gImpuesto.longValue());
                retorno.put("calcula_bien", Long.parseLong("0"));
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al calcular el precio.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            retorno.put("precio_caja", lPrecioCaja);
            retorno.put("precio_unidad", lPrecioUnidad);
            retorno.put("pimpuest", gImpuesto.longValue());
            retorno.put("calcula_bien", Long.parseLong("0"));
        }
        return retorno;
    }
    
    public boolean verificarMecaderiaDuplicada(String lCodMerca, long lNroFact){
        for(FacturaDetDto f: listadoDetalle){
            if(f.getFacturasDet().getFacturasDetPK().getCodMerca().equals(lCodMerca) && f.getFacturasDet().getFacturasDetPK().getNrofact() == lNroFact){
                return false;
            }
        }
        return true;
    }
    
    public String agregarNuevaFactura(){
        try{
            if(clienteBuscado.getMformaPago() == 'C' && !tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCO")){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Tipo inválido de factura."));
                return null;
            }
            if(clienteBuscado.getMformaPago() == 'F' && !tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCR")){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Tipo inválido de factura."));
                return null;
            }
            if(clienteBuscado.getMformaPago() != 0 && !tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCO")){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Tipo inválido de factura."));
                return null;
            }
            //no ingresó el nro de factura
            if(nroFactLbl == 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Nro factura inválido."));
                return null;
            }
            //total = 0 o negativo
            if(totalFinal == 0 || totalFinal < 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Monto total inválido."));
                return null;
            }
            //validar que el establecimiento corresponda al deposito relacionado a la factura.. Buscar en la tabla deposito el punto de establecimiento correspondiente
            depositoSeleccionado = depositoEnMemoria.get("deposito");
            if(depositoSeleccionado.getNpuntoEst().compareTo(nPuntoEstabLbl) != 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "El establecimiento debe ser "+depositoSeleccionado.getNpuntoEst()));
                return null;
            }
            //validar el limite de compra del cliente
            if(clienteBuscado.getIlimiteCompra() > 0){
                long deuda = 0;
                deuda = calcularDeudaCliente();
                if((deuda + totalFinal) > clienteBuscado.getIlimiteCompra()){
                    long dif = (deuda + totalFinal) - clienteBuscado.getIlimiteCompra();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Límite de compra superado en "+dif));
                    return null;
                }
            }
            //verificar si ya existe el nro. de factura
            List<Facturas> listadoFacturas = new ArrayList<>();
            long nroFacturaCompleto = 0;
            try{
                String fechaFacturaStr = dateToString(fechaFactLbl);
                nroFacturaCompleto = obtenerNroFacturaCompleto();
                listadoFacturas = facturasFacade.obtenerFacturasPorNroYFecha(nroFacturaCompleto, fechaFacturaStr);
                if(!listadoFacturas.isEmpty()){
                    if(listadoFacturas.size() > 0){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Ya existe factura de venta."));
                        return null;
                    }
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al obtener facturas.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                return null;
            }
            //validar rango de documentos
            try{
                List<RangosDocumentos> listadoRangos = rangosDocumentosFacade.obtenerRangosDeDocumentos(tipoDocumentoSeleccionadoLbl.getCtipoDocum(), nroFacturaCompleto);
                if(listadoRangos.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Nro. no definido como factura formulario continuo en rangos documentos."));
                    return null;
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al obtener rango de documentos.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                return null;
            }
            //validar existencia
            //para cada detalle
            for(FacturaDetDto d: listadoDetalle){
                if(!d.getFacturasDet().getFacturasDetPK().getCodMerca().equals("") || d.getFacturasDet().getFacturasDetPK().getCodMerca() != null){
                    List<Existencias> listado = new ArrayList<>();
                    try{
                        listado = existenciasFacade.buscarPorCodigoDepositoOrigenYArticulo(codDepositoLbl, d.getFacturasDet().getFacturasDetPK().getCodMerca());
                        if(listado.isEmpty()){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No existe el articulo en depósito "+codDepositoLbl));
                            return null;
                        }else{
                            for(Existencias e: listado){
                                long unidadesExist = e.getCantCajas()*e.getNrelacion() + e.getCantUnid();
                                long unidadesReser = e.getReserCajas()*e.getNrelacion() + e.getReserUnid();
                                long hayUnidad = unidadesExist - unidadesReser;
                                if(hayUnidad < ((d.getFacturasDet().getCantCajas() * e.getNrelacion())+d.getFacturasDet().getCantUnid())){
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Solo existen "+hayUnidad+" en depósito."));
                                    return null;
                                }
                            }
                        }
                    }catch(Exception e){
                        RequestContext.getCurrentInstance().update("exceptionDialog");
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al validar existencia.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                        return null;
                    }
                }
            }
            //Calcular total factura sumando los detalles
            long lTotalImpuestos = 0;
            tipoDocumentoSeleccionadoLbl = tipoDocumentoEnMemoria.get("tipo_documento");
            if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                lTotalImpuestos = totalIva * -1;
                totalFinal = totalExentas + totalGravadas;
            }else{
                lTotalImpuestos = totalIva;
                totalFinal = totalExentas + totalGravadas + lTotalImpuestos;
            }
            if(!tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCR")){
                fechaVencLbl = null;
                fechaVencImpresLbl = null;
            }
            Short lCodRuta = clienteBuscado.getCodRuta();
            long lTGravadas10 = 0; long lGravadas10 = 0;
            long lTGravadas5 = 0; long lGravadas5 = 0;
            long lTIva10 = 0; long lIva5 = 0;
            long lTIva5 = 0; long lIva10 = 0;
            //Calcular total IVA 10 e IVA 5 de los detalles (en memoria)
            for (FacturaDetDto f : listadoDetalle) {
                if (f.getFacturasDet().getFacturasDetPK().getNrofact() == obtenerNroFacturaCompleto()
                        && f.getFacturasDet().getFacturasDetPK().getCtipoDocum().equals(tipoDocumentoSeleccionadoLbl.getCtipoDocum())) {
                    if (f.getFacturasDet().getPimpues().compareTo(BigDecimal.valueOf(10)) == 0) {
                        lGravadas10 += f.getFacturasDet().getIgravadas();
                        lIva10 += f.getFacturasDet().getImpuestos() == null ? 0 : f.getFacturasDet().getImpuestos().longValue();
                    }
                    if (f.getFacturasDet().getPimpues().compareTo(BigDecimal.valueOf(5)) == 0) {
                        lGravadas5 += f.getFacturasDet().getIgravadas();
                        lIva5 += f.getFacturasDet().getImpuestos() == null ? 0 : f.getFacturasDet().getImpuestos().longValue();
                    }
                }
            }
            
            lTGravadas10 = lGravadas10;
            lTGravadas5 = lGravadas5;
            lTIva10 = lIva10;
            lTIva5 = lIva5;

            String lXFactura = obtenerNroFacturaCompletoConFormato();
            long lNewfactura = obtenerNroFacturaCompleto();
            String lXRuc = "";
            if(clienteBuscado.getXruc().equals("") || clienteBuscado.getXruc() == null){
                if(clienteBuscado.getXcedula() != null || clienteBuscado.getXcedula() != 0){
                    lXRuc = "C.I:"+clienteBuscado.getXcedula();
                }   
            }else{
                lXRuc = clienteBuscado.getXruc();
            }
            String lFFactura = dateToString(fechaFactLbl);
            String lFVenc = fechaVencLbl != null ? dateToString(fechaVencLbl) : "";
            String lFVencImpre = fechaVencImpresLbl != null ? dateToString(fechaVencImpresLbl) : "";
            //insertar en FACTURAS
            try{    
                facturasFacade.insertarFactura( tipoDocumentoSeleccionadoLbl.getCtipoDocum(), 
                                                lNewfactura, 
                                                codClienteLbl, 
                                                codCanalVentaLbl, 
                                                codDepositoLbl, 
                                                codZonaLbl, 
                                                lCodRuta, 
                                                lFFactura, 
                                                ctipoVtaLbl, 
                                                observacionLbl, 
                                                codVendedorLbl, 
                                                totalExentas, 
                                                totalGravadas, 
                                                lTotalImpuestos, 
                                                totalFinal, 
                                                codEntregadorLbl, 
                                                totalFinal, 
                                                totalDescuentos, 
                                                direccionLbl == null ? "" : direccionLbl, 
                                                razonSocialLbl == null ? "" : razonSocialLbl, 
                                                lXRuc == null ? "" : lXRuc, 
                                                telefonoLbl == null ? "" : telefonoLbl, 
                                                ciudadLbl == null ? "" : ciudadLbl, 
                                                lFVenc, 
                                                lFVencImpre, 
                                                lTGravadas10,
                                                lTGravadas5, 
                                                BigDecimal.valueOf(lTIva10), 
                                                BigDecimal.valueOf(lTIva5), 
                                                lXFactura, 
                                                plazosChequesLbl,
                                                nroPedidoLbl == 0 ? null: nroPedidoLbl);
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al insertar en facturas.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                return null;
            }
            if(tipoDocumentoSeleccionadoLbl.getMafectaCtacte() == 'S'){
                //INSERTAR EN CUENTAS CORRIENTES
                try{
                    if(lTotalImpuestos < 0){
                        lTotalImpuestos *= -1;
                        totalGravadas -= lTotalImpuestos;
                    }
                    CuentasCorrientes c = new CuentasCorrientes();
                    c.setCodEmpr(Short.parseShort("2"));
                    c.setCtipoDocum(tipoDocumentoSeleccionadoLbl.getCtipoDocum());
                    c.setFvenc(fechaVencLbl);
                    c.setFmovim(fechaFactLbl);
                    c.setNdocumCheq(String.valueOf(obtenerNroFacturaCompleto()));
                    c.setIpagado(0);
                    c.setIretencion(0);
                    c.setCodCliente(clienteBuscado);
                    c.setIsaldo(totalFinal);
                    c.setManulado(Short.parseShort("1"));
                    c.setTexentas(totalExentas);
                    c.setTgravadas(totalGravadas);
                    c.setTimpuestos(lTotalImpuestos);
                    c.setCtipoDocum(tipoDocumentoSeleccionadoLbl.getCtipoDocum());
                    c.setNrofact(obtenerNroFacturaCompleto());
                    if(tipoDocumentoSeleccionadoLbl.getMdebCred() == 'D'){
                        c.setMindice(Short.parseShort("1"));
                    }else{
                        c.setMindice(Short.parseShort("-1"));
                    }
                    c.setFfactur(fechaFactLbl);
                    cuentasCorrientesFacade.insertarCuentas(c);
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al insertar en cuentas corrientes.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return null;
                }
            }
            //INSERTAR DETALLES
            for(FacturaDetDto f: listadoDetalle){
                if(f.getFacturasDet().getFacturasDetPK().getCodMerca() != null || !f.getFacturasDet().getFacturasDetPK().getCodMerca().equals("")){
                    long lExentas = f.getFacturasDet().getIexentas();
                    long lImpuestos = f.getFacturasDet().getImpuestos() != null ? f.getFacturasDet().getImpuestos().longValue() : 0;
                    long lGravadas = f.getFacturasDet().getIgravadas();
                    
                    long lPrecioCaja = 0, lPrecioUnid = 0;
                    long lItotal = 0;
                    if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                        lImpuestos = lImpuestos * -1;
                        lPrecioCaja = f.getFacturasDet().getIprecioCaja();
                        lPrecioUnid = f.getFacturasDet().getIprecioUnidad();
                        lItotal = lGravadas + lExentas;
                    }else{
                        lPrecioCaja = f.getFacturasDet().getIprecioCaja();
                        lPrecioUnid = f.getFacturasDet().getIprecioUnidad();
                        lItotal = lGravadas + lExentas + lImpuestos;
                    }
                    
                    int lCantCajas = f.getFacturasDet().getCantCajas();
                    int lCantUnid = f.getFacturasDet().getCantUnid();
                    long lPDescLong = f.getFacturasDet().getPdesc() != null ? f.getFacturasDet().getPdesc().longValue() : 0;
                    long lIDescuentos = f.getFacturasDet().getIdescuentos();
                    String lCodMerca = f.getFacturasDet().getFacturasDetPK().getCodMerca();
                    String lXDesc = f.getFacturasDet().getXdesc();
                    String lCTipo = tipoDocumentoSeleccionadoLbl.getCtipoDocum();
                    BigDecimal lPImpues = f.getFacturasDet().getPimpues();
                    long nPeso = 0;
                    //todas las filas de la grilla si tiene descuento debe tener una promo que respalde el descuento
                    if(lPDescLong > 0 || f.getmPromoPackDet() == 'S'){
                        if(f.getFacturasDet().getNroPromo() == null){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Ingrese el nro. de promoción"));
                            return null;
                        }else{
                            try{
                                List<Promociones> listadoPromo = promocionesFacade.findByNroPromoYFechaFactura(f.getFacturasDet().getNroPromo(), lFFactura);
                                if(listadoPromo.isEmpty()){
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No existe promoción"));
                                    return null;
                                }
                                if(lCantCajas > 0 || lCantUnid > 0){
                                    //validar promocion
                                    if(f.getmPromoPackDet() == 'N'){
                                        if(validarPromocion(f.getFacturasDet().getNroPromo(), 
                                                            codZonaLbl, 
                                                            f.getFacturasDet().getFacturasDetPK().getCodMerca(), 
                                                            lCantCajas, 
                                                            lCantUnid, 
                                                            f.getFacturasDet().getUnidBonif(), 
                                                            clienteBuscado.getCtipoCliente(), 
                                                            f.getFacturasDet().getPdesc(), 
                                                            f.getCodSublineaDet(), 
                                                            ctipoVtaLbl, 
                                                            "", 
                                                            f.getnRelacion(), 
                                                            f.getFacturasDet().getFacturasDetPK().getNrofact()));
                                    }else{
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Ingrese cajas/unidades facturadas."));
                                        return null;
                                    }
                                }
                            }catch(Exception e){
                                RequestContext.getCurrentInstance().update("exceptionDialog");
                                contenidoError = ExceptionHandlerView.getStackTrace(e);
                                tituloError = "Error al obtener promociones.";
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                                return null;
                            }
                        }
                    }
                    nroPromoDet = (f.getFacturasDet().getNroPromo() == null || f.getFacturasDet().getNroPromo().compareTo(Integer.parseInt("0")) == 0) ? null : f.getFacturasDet().getNroPromo();
                    //INSERTA EL DETALLE
                    try{
                        facturasDetFacade.insertarFacturasDet(  lCTipo, 
                                                                obtenerNroFacturaCompleto(), 
                                                                lCodMerca, 
                                                                lXDesc, 
                                                                lCantCajas, 
                                                                lCantUnid, 
                                                                lPrecioCaja, 
                                                                lPrecioUnid, 
                                                                lExentas, 
                                                                lGravadas, 
                                                                lImpuestos, 
                                                                lIDescuentos, 
                                                                lItotal, 
                                                                BigDecimal.valueOf(lPDescLong), 
                                                                lPImpues, 
                                                                nroPromoDet, 
                                                                lFFactura);
                    }catch(Exception e){
                        RequestContext.getCurrentInstance().update("exceptionDialog");
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al insertar un detalle de facturas.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                        return null;
                    }
                    //INSERTAR EN MOVIMIENTOS MERCA
                    try{
                        MovimientosMerca mov = new MovimientosMerca();
                        mov.setNroMovim(1L);
                        mov.setCodEmpr(Short.parseShort("2"));
                        mov.setCodMerca(lCodMerca);
                        mov.setCodDepo(codDepositoLbl);
                        mov.setCantCajas(Long.parseLong(String.valueOf(lCantCajas)));
                        mov.setCantUnid(Long.parseLong(String.valueOf(lCantUnid)));
                        mov.setPrecioCaja(lPrecioCaja);
                        mov.setPrecioUnidad(lPrecioUnid);
                        mov.setIgravadas(lGravadas);
                        mov.setIexentas(lExentas);
                        mov.setImpuestos(lImpuestos);
                        mov.setCodVendedor(codVendedorLbl);
                        mov.setCodCliente(codClienteLbl);
                        mov.setCodZona(codZonaLbl);
                        mov.setCodRuta(codRutaLbl);
                        mov.setNpeso(BigDecimal.valueOf(nPeso));
                        mov.setManulado(Short.parseShort("1"));
                        mov.setFmovim(fechaFactLbl);
                        mov.setCtipoDocum(lCTipo);
                        mov.setNdocum(obtenerNroFacturaCompleto());
                        mov.setNrofact(obtenerNroFacturaCompleto());
                        mov.setFacCtipoDocum(lCTipo);
                        mov.setCodEntregador(codEntregadorLbl);
                        mov.setFalta(new Date());
                        String usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                        mov.setCusuario(usuario);
                        movimientosMercaFacade.insertarMovimientosMerca(mov);
                    }catch(Exception e){
                         RequestContext.getCurrentInstance().update("exceptionDialog");
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al insertar un movimiento merca.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                        return null;
                    }
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Datos grabados con éxito."));
            limpiarFormulario(); //volvemos a instanciar
            RequestContext.getCurrentInstance().execute("PF('dlgNuevFactura').hide();");
            return null;
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al agregar una nueva factura.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
            return null;
        }
    }
    
    public boolean validarPromocion(Integer lNroPromo,
                                    String lCodZona,
                                    String lCodMerca,
                                    int lCantCajas,
                                    int lCantUnid,
                                    long lBonifPedido,
                                    Character lCTipoCliente,
                                    BigDecimal lPDescPedido,
                                    Short codSubLinea,
                                    Character lCTipoVenta,
                                    String lAccion,
                                    Short nRelacion,
                                    long lNroFactura){
        try{
            int lBonifPedidoY = 0; long lTotalKilos = 0;
            int lCantCajasY = 0; int lCantUnidY = 0; int lTotalUnidPedidoY = 0; lBonifPedidoY = 0;
            int lTotalUnidPedido = lCantCajas * nRelacion + lCantUnid;
            Date lFPedido = fechaFactLbl;
            lCTipoVenta = ctipoVtaLbl;
            int lError = 1;
            try{
                String lFPedidoStr = dateToString(lFPedido);
                List<PromocionesDetDto> promociones = promocionesFacade.obtenerPromociones(codSubLinea, lCodZona, lNroPromo, lCTipoVenta, lFPedidoStr, lCodMerca);
                if(promociones.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No existen promociones para esta mercadería."));
                    return false;
                }
                for(PromocionesDetDto p: promociones){
                    if(p.getmTodos() == 'S'){
                        if(lAccion.equals("") || lAccion == null){
                            descPromoDet = p.getxDesc();
                            nroPromoDet = p.getNroPromo();
                            return true;
                        }
                        List<PromocionDto> listadoMercaPromo = new ArrayList<>();
                        try{
                            listadoMercaPromo = promocionesFacade.obtenerMercaderiasPromociones(lNroPromo);
                        }catch(Exception e){
                            RequestContext.getCurrentInstance().update("exceptionDialog");
                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                            tituloError = "Error en la lectura de mercaderías de promoción.";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                            return false;
                        }
                        try{
                            listadoMercaPromo = promocionesFacade.obtenerMercaderiasPromocionesPorSublinea(lNroPromo);
                        }catch(Exception e){
                            RequestContext.getCurrentInstance().update("exceptionDialog");
                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                            tituloError = "Error en la lectura de mercaderías de promoción por sublinea.";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                            return false;
                        }
                        List<String> listaCodMercaDet = new ArrayList<>();
                        for(PromocionDto pro: listadoMercaPromo){
                            if(pro.getXdesc().equals("") || pro.getXdesc() == null){
                                listaCodMercaDet.add(p.getCodMerca());
                            }
                        }
                        //se verifica todas las mercaderias sin la marca de Y                        
                        lCantCajas = 0; lCantUnid = 0; lTotalUnidPedido = 0; lBonifPedido = 0;
                        for(FacturaDetDto fddto: listadoDetalle){
                            if( fddto.getFacturasDet().getFacturasDetPK().getNrofact() == lNroFactura && 
                                verificarCodMercaEnListado(fddto.getFacturasDet().getFacturasDetPK().getCodMerca(), listaCodMercaDet)){
                                lCantCajas += fddto.getFacturasDet().getCantCajas();
                                lTotalUnidPedido += (fddto.getFacturasDet().getCantCajas()*fddto.getnRelacion())+fddto.getFacturasDet().getCantUnid();
                                lCantUnid += fddto.getFacturasDet().getCantUnid();
                                lBonifPedido += (fddto.getFacturasDet().getCajasBonif()*fddto.getnRelacion())+fddto.getFacturasDet().getUnidBonif();
                            }
                        }
                        //calcular total kilos
                        listaCodMercaDet = new ArrayList<>();
                        for(PromocionDto pro: listadoMercaPromo){
                            if(!verificarCodMercaEnListado(pro.getCodMerca(), listaCodMercaDet)){
                                listaCodMercaDet.add(pro.getCodMerca());
                            }
                        }
                        lCantCajas = 0; lCantUnid = 0; lTotalKilos = 0;
                        if(p.getkKilosIni().compareTo(BigDecimal.ZERO) == 1 || p.getkKilosFin().compareTo(BigDecimal.ZERO) == 1){
                            //p.getkKilosIni() > 0 || p.getkKilosFin() > 0
                            for(FacturaDetDto fddto: listadoDetalle){
                                if( fddto.getFacturasDet().getFacturasDetPK().getNrofact() == lNroFactura && 
                                    verificarCodMercaEnListado(fddto.getFacturasDet().getFacturasDetPK().getCodMerca(), listaCodMercaDet)){
                                    if(fddto.getnPesoCajas() == null){
                                        fddto.setnPesoCajas(BigDecimal.ZERO);
                                    }
                                    lCantCajas += fddto.getFacturasDet().getCantCajas() * fddto.getnPesoCajas().intValue();
                                    if(fddto.getnPesoUnidad() == null){
                                        fddto.setnPesoUnidad(BigDecimal.ZERO);
                                    }
                                    lCantUnid += fddto.getFacturasDet().getCantUnid() * fddto.getnPesoUnidad().intValue();
                                }
                            }
                            lTotalKilos = lCantCajas + lCantUnid;
                        }
                        //SE VERIFICA TODAS LAS MERCADERIAS CON MARCA DE Y
                        listaCodMercaDet = new ArrayList<>();
                        for(PromocionDto pro: listadoMercaPromo){
                            if(!pro.getXdesc().equals("") || pro.getXdesc() != null){
                                listaCodMercaDet.add(p.getCodMerca());
                            }
                        }
                        
                        for(FacturaDetDto fddto: listadoDetalle){
                            if( fddto.getFacturasDet().getFacturasDetPK().getNrofact() == lNroFactura && 
                                verificarCodMercaEnListado(fddto.getFacturasDet().getFacturasDetPK().getCodMerca(), listaCodMercaDet)){
                                lCantCajasY += fddto.getFacturasDet().getCantCajas();
                                lTotalUnidPedidoY += (fddto.getFacturasDet().getCantCajas()*fddto.getnRelacion())+fddto.getFacturasDet().getCantUnid();
                                lCantUnidY += fddto.getFacturasDet().getCantUnid();
                                lBonifPedidoY += (fddto.getFacturasDet().getCajasBonif()*fddto.getnRelacion())+fddto.getFacturasDet().getUnidBonif();
                            }
                        }
                    }
                }
                short lKMaxBonif = 0; 
                BigDecimal pMaxDesc = null;
                for(PromocionesDetDto p: promociones){
                    if(p.getmY() == 0){
                        if(p.getkKilosIni().compareTo(BigDecimal.ZERO) == 1 || p.getkKilosFin().compareTo(BigDecimal.ZERO) == 1){
                            if(p.getkKilosIni().compareTo(BigDecimal.valueOf(lTotalKilos)) <= 0 && p.getkKilosFin().compareTo(BigDecimal.valueOf(lTotalKilos)) >= 0){
                                if(p.getPorKUnidad() > 0){
                                    int lNroMax = lTotalUnidPedido/p.getPorKUnidad();
                                    lKMaxBonif = (short) (p.getkUnidBonif().intValue()*lNroMax);
                                    pMaxDesc = p.getpDesc();
                                }else if(p.getPorKCajas() > 0){
                                    int lNroMax = lCantCajas/p.getPorKCajas();
                                    lKMaxBonif = (short) (p.getkUnidBonif().intValue()*lNroMax);
                                    pMaxDesc = p.getpDesc();
                                }else{
                                    lKMaxBonif = p.getkMaxUnidBonif();
                                    pMaxDesc = p.getpDesc();
                                }
                                if(lKMaxBonif > p.getkMaxUnidBonif()){
                                   lKMaxBonif = p.getkMaxUnidBonif(); 
                                }
                            }
                        }else{
                            if( p.getkCajasIni().compareTo(BigDecimal.valueOf(lCantCajas)) <= 0 &&
                                (p.getkCajasFin().compareTo(BigDecimal.valueOf(lCantCajas)) >= 0 || p.getkCajasFin().compareTo(BigDecimal.ZERO) == 0) &&
                                (p.getkUnidIni().compareTo(BigDecimal.valueOf(lCantUnid)) <= 0 || p.getkUnidIni().compareTo(BigDecimal.valueOf(lTotalUnidPedido)) <= 0) &&
                                (p.getkUnidFin().compareTo(BigDecimal.valueOf(lCantUnid)) >= 0 || p.getkUnidFin().compareTo(BigDecimal.valueOf(lTotalUnidPedido)) >= 0 || p.getkUnidFin().compareTo(BigDecimal.ZERO) == 0) &&
                                (p.getcTipoCliente() == lCTipoCliente || p.getcTipoCliente() == 0) &&
                                (p.getmTodos() == 'N' && p.getCodMerca().equals(lCodMerca)) || (p.getmTodos() == 'S') ){
                                if (p.getPorKUnidad() > 0) {
                                    int lNroMax = lTotalUnidPedido / p.getPorKUnidad();
                                    lKMaxBonif = (short) (p.getkUnidBonif().intValue() * lNroMax);
                                    pMaxDesc = p.getpDesc();
                                } else if (p.getPorKCajas() > 0) {
                                    int lNroMax = lCantCajas / p.getPorKCajas();
                                    lKMaxBonif = (short) (p.getkUnidBonif().intValue() * lNroMax);
                                    pMaxDesc = p.getpDesc();
                                } else {
                                    lKMaxBonif = p.getkMaxUnidBonif();
                                    pMaxDesc = p.getpDesc();
                                }
                                if (lKMaxBonif > p.getkMaxUnidBonif()) {
                                    lKMaxBonif = p.getkMaxUnidBonif();
                                }
                            }
                        }
                        descPromoDet = p.getxDesc();
                        if(lBonifPedido <= lKMaxBonif && lPDescPedido.compareTo(pMaxDesc) <= 0){
                            lError = 0;
                            nroPromoDet = p.getNroPromo();
                            return true;
                        }
                    }else{
                        if (p.getkCajasIni().compareTo(BigDecimal.valueOf(lCantCajasY)) <= 0
                                && (p.getkCajasFin().compareTo(BigDecimal.valueOf(lCantCajasY)) >= 0 || p.getkCajasFin().compareTo(BigDecimal.ZERO) == 0)
                                && (p.getkUnidIni().compareTo(BigDecimal.valueOf(lCantUnidY)) <= 0)
                                && (p.getkUnidFin().compareTo(BigDecimal.valueOf(lCantUnidY)) >= 0 || p.getkUnidFin().compareTo(BigDecimal.ZERO) == 0)
                                && (p.getcTipoCliente() == lCTipoCliente || p.getcTipoCliente() == 0)
                                && (p.getmTodos() == 'N' && p.getCodMerca().equals(lCodMerca)) || (p.getmTodos() == 'S')) {
                            if (p.getPorKUnidad() > 0) {
                                int lNroMax = lTotalUnidPedido / p.getPorKUnidad();
                                lKMaxBonif = (short) (p.getkUnidBonif().intValue() * lNroMax);
                                pMaxDesc = p.getpDesc();
                            } else if (p.getPorKCajas() > 0) {
                                int lNroMax = lCantCajas / p.getPorKCajas();
                                lKMaxBonif = (short) (p.getkUnidBonif().intValue() * lNroMax);
                                pMaxDesc = p.getpDesc();
                            } else {
                                lKMaxBonif = p.getkMaxUnidBonif();
                                pMaxDesc = p.getpDesc();
                            }
                            if (lKMaxBonif > p.getkMaxUnidBonif()) {
                                lKMaxBonif = p.getkMaxUnidBonif();
                            }
                        }
                        descPromoDet = p.getxDesc();
                        if(lBonifPedido <= lKMaxBonif && lPDescPedido.compareTo(pMaxDesc) <= 0){
                            lError = 0;
                            nroPromoDet = p.getNroPromo();
                            return true;
                        }
                    }
                }
                if(lError == 1){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Max. descuento: "+pMaxDesc+". Max unidad bonificación: "+lKMaxBonif+". Mercadería: "+lCodMerca));
                    return false;
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al obtener datos de promoción.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                return false;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al validar una promoción.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
            return false;
        }
        return true;
    }
    
    private boolean verificarCodMercaEnListado(String codMerca, List<String> listado){
        for(String m: listado){
            if(codMerca.equals(m)){
                return true;
            }
        }
        return false;
    }
    
    private long obtenerNroFacturaCompleto(){
        long nroFacturaCompleto = (long) (nPuntoEstabLbl*1000000000.00 + nPuntoExpedLbl * 10000000.00 + nroFactLbl);
        return nroFacturaCompleto;
    }
    
    private String obtenerNroFacturaCompletoConFormato(){
        String puntoEstablec = String.valueOf(nPuntoEstabLbl);
        String puntoExped = String.valueOf(nPuntoExpedLbl);
        String nroFact = String.valueOf(nroFactLbl);
        String resultado = rellenarConCeros(puntoEstablec, 3)+"-"+rellenarConCeros(puntoExped, 3)+"-"+rellenarConCeros(nroFact, 7);
        return resultado;
    }
    
    private static String rellenarConCeros(String cadena, int numCeros) {
        String ceros = "";
        for (int i = cadena.length(); i < numCeros; i++) {
            ceros += "0";
        }
        return ceros + cadena;
    }
    
    private long calcularDeudaCliente(){
        long deuda = 0;
        try{
            String fechaStr = dateToString(fechaFactLbl);
            long saldoInicial = calcularSaldoInicial(codClienteLbl, fechaStr);
            long totalPedido = pedidosFacade.obtenerTotalPedidosPorClienteFecha(codClienteLbl, fechaStr);
            deuda = saldoInicial + totalPedido;
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al calcular la deuda del cliente.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
        return deuda;
    }
    
    private long calcularSaldoInicial(Integer codCliente, String fecha){
        long saldoInicial = 0;
        String fechaSaldo = null;
        try{
            List<SaldosClientes> listadoSaldos = saldosClientesFacade.obtenerSaldosClientesPorClienteFechaMovimiento(codCliente, fecha);
            if(listadoSaldos.isEmpty()){
                fechaSaldo = "20050831";
                saldoInicial = 0;
            }else{
                for(SaldosClientes s: listadoSaldos){
                    saldoInicial += (s.getIsaldo() + s.getAcumDebi() - s.getAcumCredi());
                    fechaSaldo = dateToString(s.getSaldosClientesPK().getFmovim());
                }
            }
            if(!fecha.equals(fechaSaldo)){
                long totalMovim = cuentasCorrientesFacade.obtenerTotalCtaCtePorClienteEnRangoDeFechas(codCliente, fechaSaldo, fecha);
                saldoInicial += totalMovim;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al calcular el saldo inicial.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
        return saldoInicial;
    }
    
    private String dateToString(Date fecha) {
        String resultado = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            resultado = dateFormat.format(fecha);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }
    
    public void onRowSelect(SelectEvent event) {
        if(facturaSeleccionada != null){
            if(facturaSeleccionada.getFacturasPK().getNrofact() != 0){
                setHabilitaBotonEliminar(false);
                setHabilitaBotonVisualizar(false);
            }else{
                setHabilitaBotonEliminar(true);
                setHabilitaBotonVisualizar(true);
            }
        }
    }
    
    public void cerrarDialogoSinGuardar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarRecibos').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevReciboCompra').hide();");
    }
    
    public static String convertidorFecha(Date fecha){
        return DateUtil.formaterDateToString(fecha);
    } 
    
    public Date getFechaFactLbl() {
        return fechaFactLbl;
    }

    public void setFechaFactLbl(Date fechaFactLbl) {
        this.fechaFactLbl = fechaFactLbl;
    }

    public Integer getCodClienteLbl() {
        return codClienteLbl;
    }

    public void setCodClienteLbl(Integer codClienteLbl) {
        this.codClienteLbl = codClienteLbl;
    }

    public String getNombreClienteLbl() {
        return nombreClienteLbl;
    }

    public void setNombreClienteLbl(String nombreClienteLbl) {
        this.nombreClienteLbl = nombreClienteLbl;
    }

    public Short getNPuntoEstabLbl() {
        return nPuntoEstabLbl;
    }

    public void setNPuntoEstabLbl(Short nPuntoEstabLbl) {
        this.nPuntoEstabLbl = nPuntoEstabLbl;
    }

    public Short getNPuntoExpedLbl() {
        return nPuntoExpedLbl;
    }

    public void setNPuntoExpedLbl(Short nPuntoExpedLbl) {
        this.nPuntoExpedLbl = nPuntoExpedLbl;
    }

    public long getNroFactLbl() {
        return nroFactLbl;
    }

    public void setNroFactLbl(long nroFactLbl) {
        this.nroFactLbl = nroFactLbl;
    }

    public short getCodDepositoLbl() {
        return codDepositoLbl;
    }

    public void setCodDepositoLbl(short codDepositoLbl) {
        this.codDepositoLbl = codDepositoLbl;
    }

    public long getNroPedidoLbl() {
        return nroPedidoLbl;
    }

    public void setNroPedidoLbl(long nroPedidoLbl) {
        this.nroPedidoLbl = nroPedidoLbl;
    }

    public Character getCtipoVtaLbl() {
        return ctipoVtaLbl;
    }

    public void setCtipoVtaLbl(Character ctipoVtaLbl) {
        this.ctipoVtaLbl = ctipoVtaLbl;
    }

    public Date getFechaVencLbl() {
        return fechaVencLbl;
    }

    public void setFechaVencLbl(Date fechaVencLbl) {
        this.fechaVencLbl = fechaVencLbl;
    }

    public Date getFechaVencImpresLbl() {
        return fechaVencImpresLbl;
    }

    public void setFechaVencImpresLbl(Date fechaVencImpresLbl) {
        this.fechaVencImpresLbl = fechaVencImpresLbl;
    }

    public int getCantDiasChequesLbl() {
        return cantDiasChequesLbl;
    }

    public void setCantDiasChequesLbl(int cantDiasChequesLbl) {
        this.cantDiasChequesLbl = cantDiasChequesLbl;
    }

    public int getCantDiasImpresionChequesLbl() {
        return cantDiasImpresionChequesLbl;
    }

    public void setCantDiasImpresionChequesLbl(int cantDiasImpresionChequesLbl) {
        this.cantDiasImpresionChequesLbl = cantDiasImpresionChequesLbl;
    }

    public short getCodVendedorLbl() {
        return codVendedorLbl;
    }

    public void setCodVendedorLbl(short codVendedorLbl) {
        this.codVendedorLbl = codVendedorLbl;
    }

    public String getCodCanalVentaLbl() {
        return codCanalVentaLbl;
    }

    public void setCodCanalVentaLbl(String codCanalVentaLbl) {
        this.codCanalVentaLbl = codCanalVentaLbl;
    }

    public short getPlazosChequesLbl() {
        return plazosChequesLbl;
    }

    public void setPlazosChequesLbl(short plazosChequesLbl) {
        this.plazosChequesLbl = plazosChequesLbl;
    }

    public TiposDocumentos getTipoDocumentoSeleccionadoLbl() {
        return tipoDocumentoSeleccionadoLbl;
    }

    public void setTipoDocumentoSeleccionadoLbl(TiposDocumentos tipoDocumentoSeleccionadoLbl) {
        this.tipoDocumentoSeleccionadoLbl = tipoDocumentoSeleccionadoLbl;
    }

    public String getContenidoError() {
        return contenidoError;
    }

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }

    public List<TiposVentas> getListadoTiposVentas() {
        return listadoTiposVentas;
    }

    public void setListadoTiposVentas(List<TiposVentas> listadoTiposVentas) {
        this.listadoTiposVentas = listadoTiposVentas;
    }

    public List<Depositos> getListadoDepositos() {
        return listadoDepositos;
    }

    public void setListadoDepositos(List<Depositos> listadoDepositos) {
        this.listadoDepositos = listadoDepositos;
    }

    public Clientes getClienteBuscado() {
        return clienteBuscado;
    }

    public void setClienteBuscado(Clientes clienteBuscado) {
        this.clienteBuscado = clienteBuscado;
    }

    public boolean isMostrarCantDiasChequesLbl() {
        return mostrarCantDiasChequesLbl;
    }

    public void setMostrarCantDiasChequesLbl(boolean mostrarCantDiasChequesLbl) {
        this.mostrarCantDiasChequesLbl = mostrarCantDiasChequesLbl;
    }

    public boolean isMostrarCantDiasImpresionChequesLbl() {
        return mostrarCantDiasImpresionChequesLbl;
    }

    public void setMostrarCantDiasImpresionChequesLbl(boolean mostrarCantDiasImpresionChequesLbl) {
        this.mostrarCantDiasImpresionChequesLbl = mostrarCantDiasImpresionChequesLbl;
    }

    public boolean isMostrarPlazosChequesLbl() {
        return mostrarPlazosChequesLbl;
    }

    public void setMostrarPlazosChequesLbl(boolean mostrarPlazosChequesLbl) {
        this.mostrarPlazosChequesLbl = mostrarPlazosChequesLbl;
    }

    public long getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(long totalFinal) {
        this.totalFinal = totalFinal;
    }

    public long getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(long totalIva) {
        this.totalIva = totalIva;
    }

    public long getTotalExentas() {
        return totalExentas;
    }

    public void setTotalExentas(long totalExentas) {
        this.totalExentas = totalExentas;
    }

    public long getTotalGravadas() {
        return totalGravadas;
    }

    public void setTotalGravadas(long totalGravadas) {
        this.totalGravadas = totalGravadas;
    }

    public long getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(long totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public Depositos getDepositoSeleccionado() {
        return depositoSeleccionado;
    }

    public void setDepositoSeleccionado(Depositos depositoSeleccionado) {
        this.depositoSeleccionado = depositoSeleccionado;
    }

    public String getCodZonaLbl() {
        return codZonaLbl;
    }

    public void setCodZonaLbl(String codZonaLbl) {
        this.codZonaLbl = codZonaLbl;
    }

    public String getObservacionLbl() {
        return observacionLbl;
    }

    public void setObservacionLbl(String observacionLbl) {
        this.observacionLbl = observacionLbl;
    }

    public short getCodEntregadorLbl() {
        return codEntregadorLbl;
    }

    public void setCodEntregadorLbl(short codEntregadorLbl) {
        this.codEntregadorLbl = codEntregadorLbl;
    }

    public String getDireccionLbl() {
        return direccionLbl;
    }

    public void setDireccionLbl(String direccionLbl) {
        this.direccionLbl = direccionLbl;
    }

    public String getRazonSocialLbl() {
        return razonSocialLbl;
    }

    public void setRazonSocialLbl(String razonSocialLbl) {
        this.razonSocialLbl = razonSocialLbl;
    }

    public String getTelefonoLbl() {
        return telefonoLbl;
    }

    public void setTelefonoLbl(String telefonoLbl) {
        this.telefonoLbl = telefonoLbl;
    }

    public String getCiudadLbl() {
        return ciudadLbl;
    }

    public void setCiudadLbl(String ciudadLbl) {
        this.ciudadLbl = ciudadLbl;
    }

    public short getCodRutaLbl() {
        return codRutaLbl;
    }

    public void setCodRutaLbl(short codRutaLbl) {
        this.codRutaLbl = codRutaLbl;
    }

    public Facturas getFacturaSeleccionada() {
        return facturaSeleccionada;
    }

    public void setFacturaSeleccionada(Facturas facturaSeleccionada) {
        this.facturaSeleccionada = facturaSeleccionada;
    }

    public List<Facturas> getListadoFacturas() {
        return listadoFacturas;
    }

    public void setListadoFacturas(List<Facturas> listadoFacturas) {
        this.listadoFacturas = listadoFacturas;
    }

    public Short getMotivoLbl() {
        return cMotivoLbl;
    }

    public void setMotivoLbl(Short cMotivoLbl) {
        this.cMotivoLbl = cMotivoLbl;
    }

    public Date getFechaAnulacionLbl() {
        return fechaAnulacionLbl;
    }

    public void setFechaAnulacionLbl(Date fechaAnulacionLbl) {
        this.fechaAnulacionLbl = fechaAnulacionLbl;
    }

    public String getCodMercaDet() {
        return codMercaDet;
    }

    public void setCodMercaDet(String codMercaDet) {
        this.codMercaDet = codMercaDet;
    }

    public int getCantCajasDet() {
        return cantCajasDet;
    }

    public void setCantCajasDet(int cantCajasDet) {
        this.cantCajasDet = cantCajasDet;
    }

    public int getCantUnidDet() {
        return cantUnidDet;
    }

    public void setCantUnidDet(int cantUnidDet) {
        this.cantUnidDet = cantUnidDet;
    }

    public int getCantCajasBonifDet() {
        return cantCajasBonifDet;
    }

    public void setCantCajasBonifDet(int cantCajasBonifDet) {
        this.cantCajasBonifDet = cantCajasBonifDet;
    }

    public int getCantUnidBonifDet() {
        return cantUnidBonifDet;
    }

    public void setCantUnidBonifDet(int cantUnidBonifDet) {
        this.cantUnidBonifDet = cantUnidBonifDet;
    }

    public Mercaderias getMercaderiaDet() {
        return mercaderiaDet;
    }

    public void setMercaderiaDet(Mercaderias mercaderiaDet) {
        this.mercaderiaDet = mercaderiaDet;
    }

    public List<FacturaDetDto> getListadoDetalle() {
        return listadoDetalle;
    }

    public void setListadoDetalle(List<FacturaDetDto> listadoDetalle) {
        this.listadoDetalle = listadoDetalle;
    }

    public List<FacturasDet> getListadoFacturasDet() {
        return listadoFacturasDet;
    }

    public void setListadoFacturasDet(List<FacturasDet> listadoFacturasDet) {
        this.listadoFacturasDet = listadoFacturasDet;
    }

    public String getDescMercaDet() {
        return descMercaDet;
    }

    public void setDescMercaDet(String descMercaDet) {
        this.descMercaDet = descMercaDet;
    }

    public long getPrecioCajaDet() {
        return precioCajaDet;
    }

    public void setPrecioCajaDet(long precioCajaDet) {
        this.precioCajaDet = precioCajaDet;
    }

    public long getPrecioUnidDet() {
        return precioUnidDet;
    }

    public void setPrecioUnidDet(long precioUnidDet) {
        this.precioUnidDet = precioUnidDet;
    }

    public long getGravadasDet() {
        return gravadasDet;
    }

    public void setGravadasDet(long gravadasDet) {
        this.gravadasDet = gravadasDet;
    }

    public long getExentasDet() {
        return exentasDet;
    }

    public void setExentasDet(long exentasDet) {
        this.exentasDet = exentasDet;
    }

    public long getImpuestosDet() {
        return impuestosDet;
    }

    public void setImpuestosDet(long impuestosDet) {
        this.impuestosDet = impuestosDet;
    }

    public long getDescuentosDet() {
        return descuentosDet;
    }

    public void setDescuentosDet(long descuentosDet) {
        this.descuentosDet = descuentosDet;
    }

    public Integer getNroPromoDet() {
        return nroPromoDet;
    }

    public void setNroPromoDet(Integer nroPromoDet) {
        this.nroPromoDet = nroPromoDet;
    }

    public String getDescPromoDet() {
        return descPromoDet;
    }

    public void setDescPromoDet(String descPromoDet) {
        this.descPromoDet = descPromoDet;
    }

    public BigDecimal getPdescDet() {
        return pdescDet;
    }

    public void setPdescDet(BigDecimal pdescDet) {
        this.pdescDet = pdescDet;
    }

    public BigDecimal getPimpuesDet() {
        return pimpuesDet;
    }

    public void setPimpuesDet(BigDecimal pimpuesDet) {
        this.pimpuesDet = pimpuesDet;
    }

    public Short getnRelacionDet() {
        return nRelacionDet;
    }

    public void setnRelacionDet(Short nRelacionDet) {
        this.nRelacionDet = nRelacionDet;
    }

    public Character getmPromoPackDet() {
        return mPromoPackDet;
    }

    public void setmPromoPackDet(Character mPromoPackDet) {
        this.mPromoPackDet = mPromoPackDet;
    }

    public Short getCodSublineaDet() {
        return codSublineaDet;
    }

    public void setCodSublineaDet(Short codSublineaDet) {
        this.codSublineaDet = codSublineaDet;
    }

    public BigDecimal getnPesoCajaDet() {
        return nPesoCajaDet;
    }

    public void setnPesoCajaDet(BigDecimal nPesoCajaDet) {
        this.nPesoCajaDet = nPesoCajaDet;
    }

    public BigDecimal getnPesoUnidadDet() {
        return nPesoUnidadDet;
    }

    public void setnPesoUnidadDet(BigDecimal nPesoUnidadDet) {
        this.nPesoUnidadDet = nPesoUnidadDet;
    }

    public boolean isHabilitaBotonEliminar() {
        return habilitaBotonEliminar;
    }

    public void setHabilitaBotonEliminar(boolean habilitaBotonEliminar) {
        this.habilitaBotonEliminar = habilitaBotonEliminar;
    }

    public List<TiposDocumentos> getListadoTiposDocumentos() {
        return listadoTiposDocumentos;
    }

    public void setListadoTiposDocumentos(List<TiposDocumentos> listadoTiposDocumentos) {
        this.listadoTiposDocumentos = listadoTiposDocumentos;
    }

    public List<EmpleadosZonas> getListadoVendedores() {
        return listadoVendedores;
    }

    public void setListadoVendedores(List<EmpleadosZonas> listadoVendedores) {
        this.listadoVendedores = listadoVendedores;
    }

    public List<CanalesVendedores> getListadoCanales() {
        return listadoCanales;
    }

    public void setListadoCanales(List<CanalesVendedores> listadoCanales) {
        this.listadoCanales = listadoCanales;
    }

    public List<Empleados> getListadoEntregadores() {
        return listadoEntregadores;
    }

    public void setListadoEntregadores(List<Empleados> listadoEntregadores) {
        this.listadoEntregadores = listadoEntregadores;
    }

    public List<Existencias> getListaExistencias() {
        return listaExistencias;
    }

    public void setListaExistencias(List<Existencias> listaExistencias) {
        this.listaExistencias = listaExistencias;
    }

    public Existencias getExistencias() {
        return existencias;
    }

    public void setExistencias(Existencias existencias) {
        this.existencias = existencias;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public FacturaDetDto getFacturaDetDtoSeleccionada() {
        return facturaDetDtoSeleccionada;
    }

    public void setFacturaDetDtoSeleccionada(FacturaDetDto facturaDetDtoSeleccionada) {
        this.facturaDetDtoSeleccionada = facturaDetDtoSeleccionada;
    }

    public boolean isMostrarTotalIva() {
        return mostrarTotalIva;
    }

    public void setMostrarTotalIva(boolean mostrarTotalIva) {
        this.mostrarTotalIva = mostrarTotalIva;
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

    public LazyDataModel<Existencias> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<Existencias> model) {
        this.model = model;
    }

    public boolean isHabilitaBotonVisualizar() {
        return habilitaBotonVisualizar;
    }

    public void setHabilitaBotonVisualizar(boolean habilitaBotonVisualizar) {
        this.habilitaBotonVisualizar = habilitaBotonVisualizar;
    }

    public boolean isMostrarFechaVencimiento() {
        return mostrarFechaVencimiento;
    }

    public void setMostrarFechaVencimiento(boolean mostrarFechaVencimiento) {
        this.mostrarFechaVencimiento = mostrarFechaVencimiento;
    }

    public boolean isMostrarFechaVencimientoImpresion() {
        return mostrarFechaVencimientoImpresion;
    }

    public void setMostrarFechaVencimientoImpresion(boolean mostrarFechaVencimientoImpresion) {
        this.mostrarFechaVencimientoImpresion = mostrarFechaVencimientoImpresion;
    }

    public Short getcMotivoLbl() {
        return cMotivoLbl;
    }

    public void setcMotivoLbl(Short cMotivoLbl) {
        this.cMotivoLbl = cMotivoLbl;
    }

    public List<Motivos> getListadoMotivos() {
        return listadoMotivos;
    }

    public void setListadoMotivos(List<Motivos> listadoMotivos) {
        this.listadoMotivos = listadoMotivos;
    }
    
}
