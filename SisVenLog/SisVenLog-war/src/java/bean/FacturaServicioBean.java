/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ClientesFacade;
import dao.CuentasCorrientesFacade;
import dao.FacturasFacade;
import dao.FacturasSerFacade;
import dao.MotivosFacade;
import dao.RangosDocumentosFacade;
import dao.ServiciosFacade;
import dao.TiposDocumentosFacade;
import entidad.Clientes;
import entidad.CuentasCorrientes;
import entidad.Facturas;
import entidad.FacturasPK;
import entidad.FacturasSer;
import entidad.FacturasSerPK;
import entidad.Motivos;
import entidad.RangosDocumentos;
import entidad.Servicios;
import entidad.TiposDocumentos;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.inject.Inject;
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
public class FacturaServicioBean extends LazyDataModel<Facturas> implements Serializable{
    
    @EJB
    private RangosDocumentosFacade rangosDocumentosFacade;
    
    @EJB
    private FacturasFacade facturasFacade;
    
    @EJB
    private CuentasCorrientesFacade cuentasCorrientesFacade;
    
    @EJB
    private FacturasSerFacade facturasSerFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private ClientesFacade clientesFacade;
    
    @EJB
    private ServiciosFacade serviciosFacade;
    
    @EJB
    private MotivosFacade motivosFacade;
    
    //variables de clase
    private List<TiposDocumentos> listadoTiposDocumentos;
    private TiposDocumentos tipoDocumentoSeleccionadoLbl;
    private List<Servicios> listadoServicios;
    private Servicios servicioSeleccionadoDet;
    private Date fechaFactLbl;
    private Integer codClienteLbl;
    private String nombreClienteLbl;
    private Short nPuntoEstabLbl;
    private Short nPuntoExpedLbl;
    private long nroFactLbl;
    private String contenidoError;
    private String tituloError;
    private Clientes clienteBuscado;
    private long totalFinal;
    private long totalIva;
    private long totalExentas;
    private long totalGravadas;
    private long totalDescuentos;
    private Short codServicioDet;
    private short codDepositoLbl;
    private short codVendedorLbl;
    private String codCanalVentaLbl;
    private String codZonaLbl;
    private String observacionLbl;
    private Facturas facturas;
    private Short codRutaLbl;
    private String direccionLbl;
    private String razonSocialLbl;
    private String telefonoLbl;
    private String ciudadLbl;
    private String lXRuc;
    private Date fechaVencLbl;
    private Date fechaAnulacionLbl;
    private Short cMotivoLbl;
    private long exentasDet;
    private long gravadasDet;
    private long descuentosDet;
    private boolean habilitaExentas;
    private boolean habilitaGravadas;
    private Clientes clientes;
    private List<Clientes> listaClientes;
    private String filtro;
    private LazyDataModel<Facturas> model;
    private List<Facturas> listadoFacturas;
    private boolean habilitaBotonEliminar;
    private boolean habilitaBotonAnular;
    HashMap<String, Servicios> servicioEnMemoria;
    private FacturasSer facturasSerSeleccionado;
    private List<Motivos> listadoMotivos;

    public FacturaServicioBean() {
    }
    
    @PostConstruct
    public void inicializar(){
        limpiarFormulario();
        model = new LazyDataModel<Facturas>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Facturas> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                int count = 0;
                if (filters.size() == 0) {
                    listadoFacturas = facturasFacade.buscarFacturasServiciosEnUnRango(new int[]{first, pageSize});
                    count = facturasFacade.obtenerCantidadFacturasServicios();
                } else {
                    if (filters.size() < 2) {
                        String filtroNroRecibo = (String) filters.get("facturasPK.nrofact");
                        listadoFacturas = facturasFacade.obtenerFacturasServiciosPorNroEnUnRango(Long.parseLong(filtroNroRecibo), new int[]{first, pageSize});
                        count = facturasFacade.obtenerCantidadFacturasServiciosPorNro(Long.parseLong(filtroNroRecibo));
                    }
                }
                model.setRowCount(count);
                return listadoFacturas;
            }

            @Override
            public Facturas getRowData(String rowKey) {
                String tempIndex = rowKey;
                System.out.println("row key: "+tempIndex);
                if (tempIndex != null) {
                    for (Facturas fac : listadoFacturas) {
                        String fechaFac = dateToString(fac.getFacturasPK().getFfactur());
                        if (String.valueOf(fac.getFacturasPK().getNrofact()).concat(" ").concat(fac.getFacturasPK().getCtipoDocum()).concat(" ").concat(fechaFac).equals(rowKey)) {
                            return fac;
                        }
                    }
                }

                return null;
            }

            @Override
            public Object getRowKey(Facturas f) {
                FacturasPK pk = f != null ? f.getFacturasPK() : null;
                return pk != null ? pk.getNrofact() + " " + pk.getCtipoDocum() + " " + dateToString(pk.getFfactur()) : null;
            }
        };
    }
    
    
    public void limpiarFormulario(){
        listadoServicios = new ArrayList<>();
        listadoTiposDocumentos = new ArrayList<>();
        servicioSeleccionadoDet = new Servicios();
        tipoDocumentoSeleccionadoLbl = new TiposDocumentos();
        fechaFactLbl = new Date(); contenidoError = null;
        codClienteLbl = null; tituloError = null;
        nombreClienteLbl = null; clienteBuscado = new Clientes();
        nPuntoEstabLbl = 0; totalFinal = 0;
        nPuntoExpedLbl = 0; totalExentas = 0;
        nroFactLbl = 0; totalIva = 0;
        totalGravadas = 0; totalDescuentos = 0;
        codServicioDet = 0; codDepositoLbl = 0;
        codVendedorLbl = 0; codCanalVentaLbl = null;
        codZonaLbl = null; observacionLbl = null;
        facturas = new Facturas();
        facturas.setFacturasSerCollection(new ArrayList<>());      
        codRutaLbl = null;
        direccionLbl = null;
        razonSocialLbl = null;
        telefonoLbl = null;
        ciudadLbl = null;
        lXRuc = null;
        fechaVencLbl = null;
        fechaAnulacionLbl = null;
        cMotivoLbl = null;
        exentasDet = 0; gravadasDet = 0;
        habilitaExentas = false; habilitaGravadas = false;
        clientes = new Clientes(); listaClientes = new ArrayList<>(); filtro = null;
        listadoFacturas = new ArrayList<>();
        habilitaBotonEliminar = true; habilitaBotonAnular = true;
        servicioEnMemoria = new HashMap<>(); facturasSerSeleccionado = new FacturasSer();
        listadoMotivos = new ArrayList<>();
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
    
    public void cerrarDialogoSinGuardar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarRecibos').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevReciboCompra').hide();");
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
    
    public void onRowSelect(SelectEvent event) {
        if(facturas != null){
            FacturasPK pk = facturas.getFacturasPK();
            if(pk != null){
                if(pk.getNrofact() != 0 && !pk.getCtipoDocum().equals("")){
                    setHabilitaBotonEliminar(false);
                    setHabilitaBotonAnular(false);
                }else{
                    setHabilitaBotonEliminar(true);
                    setHabilitaBotonAnular(true);
                }
            }
        }
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
    
    public List<Servicios> listarServicios(){
        try{
            listadoServicios = serviciosFacade.listarServiciosOrdenadosPorDescripcion();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al obtener datos de servicios.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoServicios;
    }
    
    public List<TiposDocumentos> listarTiposDocumentos(){
        try{
            listadoTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoFacturaServicio();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al obtener datos de tipos de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoTiposDocumentos;
    }
    
    public void cambiaServicio(){
        habilitaExentas = false; habilitaGravadas = false;
        exentasDet = 0; gravadasDet = 0;
        if(codServicioDet != null){
            servicioSeleccionadoDet = serviciosFacade.find(codServicioDet);
            servicioEnMemoria.put("servicio", servicioSeleccionadoDet);
            if(servicioSeleccionadoDet.getPimpues().compareTo(BigDecimal.ZERO) == 0){
                habilitaExentas = false;
                habilitaGravadas = true;
            }else{
                habilitaExentas = true;
                habilitaGravadas = false;
            }
        }
    }
    
    public String agregarServicioAlDetalle(){
        try{
            servicioSeleccionadoDet = servicioEnMemoria.get("servicio");
            tipoDocumentoSeleccionadoLbl = tiposDocumentosFacade.find(tipoDocumentoSeleccionadoLbl.getCtipoDocum());
            FacturasSerPK facturaSerPK = new FacturasSerPK();
            facturaSerPK.setCodEmpr(Short.parseShort("2"));
            facturaSerPK.setCodServicio(codServicioDet);
            facturaSerPK.setCtipoDocum(tipoDocumentoSeleccionadoLbl.getCtipoDocum());
            facturaSerPK.setFfactur(fechaFactLbl);
            facturaSerPK.setNrofact(obtenerNroFacturaCompleto());
            FacturasSer servicio = new FacturasSer();
            servicio.setFacturasSerPK(facturaSerPK);
            servicio.setIexentas(exentasDet);
            servicio.setIgravadas(gravadasDet);
            servicio.setPimpues(servicioSeleccionadoDet.getPimpues());
            if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                if(servicio.getPimpues().compareTo(BigDecimal.valueOf(10)) == 0){
                    long gravadas_div11 = servicio.getIgravadas()/11;
                    servicio.setImpuestos(BigDecimal.valueOf(gravadas_div11));
                }else if(servicio.getPimpues().compareTo(BigDecimal.valueOf(5)) == 0){
                    long gravadas_div21 = servicio.getIgravadas()/21;
                    servicio.setImpuestos(BigDecimal.valueOf(gravadas_div21));
                }else{
                    servicio.setImpuestos(BigDecimal.ZERO);
                }    
            }else{
                BigDecimal pImpues_div100 = servicio.getPimpues().divide(BigDecimal.valueOf(100));
                BigDecimal multiplica = BigDecimal.valueOf(servicio.getIgravadas()).multiply(pImpues_div100);
                servicio.setImpuestos(multiplica);
            }
            facturas.getFacturasSerCollection().add(servicio);
            calcularTotales();
            codServicioDet = 0;
            exentasDet = 0;
            gravadasDet = 0;
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al agregar servicio en el detalle.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return null;
    }
    
    private void calcularTotales(){
        totalDescuentos = 0; totalExentas = 0; totalGravadas = 0; totalIva = 0; totalFinal = 0;
        for(FacturasSer f: facturas.getFacturasSerCollection()){
            totalDescuentos += f.getIdescuentos() == null ? 0 : f.getIdescuentos();
            totalExentas += f.getIexentas() == null ? 0 : f.getIexentas();
            totalGravadas += f.getIgravadas() == null ? 0 : f.getIgravadas();
            totalIva += f.getImpuestos() == null ? 0 : f.getImpuestos().longValue();
        }
        totalFinal = totalExentas + totalGravadas + totalIva;
    }
    
    public String eliminarFacturaServicio(){
        try{
            if(facturas.getMestado() == 'X'){
                //Eliminar cabecera. Los detalles se eliminan automáticamente
                try{
                    facturasFacade.borrarFactura(facturas.getFacturasPK().getNrofact(), dateToString(facturas.getFacturasPK().getFfactur()), facturas.getFacturasPK().getCtipoDocum());
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la eliminación de facturas.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return null;
                }
                //eliminar de cuenta corriente si corresponde
                try{
                    cuentasCorrientesFacade.borrarCuentaCorriente(facturas.getFacturasPK().getCtipoDocum(), facturas.getFacturasPK().getNrofact());
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la eliminación de cuentas corrientes.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return null;
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos eliminados.", ""));
                RequestContext.getCurrentInstance().execute("PF('dlgBorrarFactura').hide();");
                inicializar();
                return null;
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "La factura debe anularse antes de eliminarse."));
                return null;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al eliminar una factura de servicio.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return null;
    }
    
    public String anularFacturaServicio(){
        try{
            if(facturas.getMestado() == 'A'){
                if(facturas.getTnotas() > 0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Esta factura tiene notas de créditos relacionadas."));
                    return null;
                }
                if(facturas.getIsaldo() == 0 && facturas.getTtotal() > 0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Esta factura está cancelada."));
                    return null;
                }
                if(validarDatosAnulacion()){
                    TiposDocumentos tiposDoc = tiposDocumentosFacade.find(facturas.getFacturasPK().getCtipoDocum());
                    if(tiposDoc.getMafectaCtacte() == 'S'){
                        long lISaldo = facturas.getIsaldo();
                        long lTImpuestos = facturas.getTimpuestos();
                        long lTGravadas = facturas.getTgravadas();
                        if(tiposDoc.getMincluIva() == 'S'){
                            lTGravadas -= lTImpuestos;
                        }
                        lTImpuestos *= -1;
                        lTGravadas *= -1;
                        long lTExentas = facturas.getTexentas() * -1;
                        //insertar en cuenta corriente
                        try{
                            CuentasCorrientes c = new CuentasCorrientes();
                            c.setCodEmpr(Short.parseShort("2"));
                            c.setCtipoDocum(facturas.getFacturasPK().getCtipoDocum());
                            c.setFvenc(facturas.getFvenc());
                            c.setFmovim(facturas.getFacturasPK().getFfactur());
                            c.setNdocumCheq(String.valueOf(facturas.getFacturasPK().getNrofact()));
                            c.setIpagado(0);
                            c.setIretencion(0);
                            clienteBuscado = clientesFacade.find(facturas.getCodCliente().getCodCliente());
                            c.setCodCliente(clienteBuscado);
                            c.setIsaldo(lISaldo);
                            c.setManulado(Short.parseShort("-1"));
                            c.setTexentas(lTExentas);
                            c.setTgravadas(lTGravadas);
                            c.setTimpuestos(lTImpuestos);
                            c.setCtipoDocum(facturas.getFacturasPK().getCtipoDocum());
                            c.setNrofact(facturas.getFacturasPK().getNrofact());
                            c.setMindice((short)(tiposDoc.getMindice().intValue() * -1));
                            c.setFfactur(facturas.getFacturasPK().getFfactur());
                            cuentasCorrientesFacade.insertarCuentas(c);
                        }catch(Exception e){
                            RequestContext.getCurrentInstance().update("exceptionDialog");
                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                            tituloError = "Error en inserción de movimientos en cuenta corriente.";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                            return null;
                        }
                    }
                    //actualizar el estado de la factura
                    //actualizar facturas
                    try {
                        String lFAnulacion = dateToString(fechaAnulacionLbl);
                        String usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                        facturasFacade.actualizarFacturas(lFAnulacion, usuario, cMotivoLbl, dateToString(facturas.getFacturasPK().getFfactur()), facturas.getFacturasPK().getNrofact(), facturas.getFacturasPK().getCtipoDocum());
                    } catch (Exception e) {
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al actualizar facturas.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return null;
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Factura anulada.", ""));
                    RequestContext.getCurrentInstance().execute("PF('dlgAnularFactura').hide();");
                    inicializar();
                    return null;
                }
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Estado inválido de la factura."));
                return null;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al anular una factura de servicio.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return null;
    }
    
    public void borrarFilaFactura(FacturasSer facturaABorrar){
        try{
            Iterator itr = facturas.getFacturasSerCollection().iterator();
            while (itr.hasNext()) {
                FacturasSer facturaDetDtoEnLista = (FacturasSer) itr.next();
                if(facturaDetDtoEnLista.getFacturasSerPK().getCodServicio() == facturaABorrar.getFacturasSerPK().getCodServicio() &&
                        facturaDetDtoEnLista.getFacturasSerPK().getCtipoDocum().equals(facturaABorrar.getFacturasSerPK().getCtipoDocum()) &&
                        facturaDetDtoEnLista.getFacturasSerPK().getFfactur().compareTo(facturaABorrar.getFacturasSerPK().getFfactur()) == 0 &&
                        facturaDetDtoEnLista.getFacturasSerPK().getNrofact() == facturaABorrar.getFacturasSerPK().getNrofact()){
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
    
    public String agregarNuevaFacturaServicio(){
        try{
            String lFFactura = dateToString(fechaFactLbl);
            //validar nro de factura
            if(nroFactLbl == 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Nro factura inválido."));
                return null;
            }
            // validar monto total
            if(totalFinal == 0 || totalFinal < 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Monto total inválido."));
                return null;
            }
            //validar cliente
            if(codClienteLbl == 0 || codClienteLbl == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Código cliente inválido."));
                return null;
            }
            //validar timbrado
            if(!validarTimbradoVencido(tipoDocumentoSeleccionadoLbl.getCtipoDocum(), obtenerNroFacturaCompleto(), fechaFactLbl, 'F')){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Timbrado del documento está vencido/no existe."));
                return null;
            }
            //verificar si ya existe el nro. de factura 
            try{
                List<Facturas> listadoFacturas = facturasFacade.obtenerFacturasPorNroYFecha(obtenerNroFacturaCompleto(), lFFactura);
                if(listadoFacturas.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No existe factura de venta en la base de datos."));
                    return null;
                }else{
                    for(Facturas f: listadoFacturas){
                        codRutaLbl = f.getCodRuta();
                        direccionLbl = f.getXdirec();
                        lXRuc = f.getXruc();
                        telefonoLbl = f.getXtelef();
                        ciudadLbl = f.getXciudad();
                    }
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en la búsqueda de factura de venta.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return null;
            }
            //validar ingreso de detalles  
            if(facturas.getFacturasSerCollection().isEmpty()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Factura sin detalles."));
                return null;
            }
            //montos
            long lISaldo = totalFinal;
            long lTExentas = totalExentas;
            long lTGravadas = totalGravadas;
            long lTDescuentos = totalDescuentos;
            long lTImpuestos = 0;
            tipoDocumentoSeleccionadoLbl = tiposDocumentosFacade.find(tipoDocumentoSeleccionadoLbl.getCtipoDocum());
            if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                lTImpuestos = totalIva * -1;
            }else{
                lTImpuestos = totalIva;
            }        
            long lTotal = lTExentas + lTGravadas + lTImpuestos;
            //buscar % de impuesto
            long lTGravadas10 = 0, lTGravadas5 = 0;
            long lTIva10 = 0, lTIva5 = 0;
            for(FacturasSer fs: facturas.getFacturasSerCollection()){
                if( fs.getFacturasSerPK().getNrofact() == obtenerNroFacturaCompleto() && 
                    fs.getFacturasSerPK().getCtipoDocum().equals(tipoDocumentoSeleccionadoLbl.getCtipoDocum())){
                    if(fs.getPimpues().compareTo(new BigDecimal("10")) == 0){
                        lTGravadas10 += fs.getIgravadas() == null ? 0 : fs.getIgravadas();
                        lTIva10 += fs.getImpuestos() == null ? 0 : fs.getImpuestos().longValue();
                    }else if(fs.getPimpues().compareTo(new BigDecimal("5")) == 0){
                        lTGravadas5 += fs.getIgravadas() == null ? 0: fs.getIgravadas();
                        lTIva5 += fs.getImpuestos() == null ? 0 : fs.getImpuestos().longValue();
                    }
                }
            }
            if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                lTIva10 = lTIva10 * -1;
                lTIva5 = lTIva5 * -1;
            }
            if(!tipoDocumentoSeleccionadoLbl.getCtipoDocum().equals("FCP")){
                fechaVencLbl = null;
            }
            //insertar en FACTURAS
            try{
                facturasFacade.insertarFactura( tipoDocumentoSeleccionadoLbl.getCtipoDocum(), 
                                                obtenerNroFacturaCompleto(), 
                                                codClienteLbl,
                                                lFFactura, 
                                                'D', 
                                                observacionLbl, 
                                                lTExentas, 
                                                lTGravadas, 
                                                lTImpuestos, 
                                                lTotal, 
                                                lISaldo, 
                                                lTDescuentos, 
                                                direccionLbl == null ? "" : direccionLbl, 
                                                razonSocialLbl == null ? "" : razonSocialLbl, 
                                                lXRuc == null ? "" : lXRuc, 
                                                telefonoLbl == null ? "" : telefonoLbl, 
                                                ciudadLbl == null ? "" : ciudadLbl,  
                                                fechaVencLbl == null ? "" : dateToString(fechaVencLbl), 
                                                lTGravadas10, 
                                                lTGravadas5, 
                                                BigDecimal.valueOf(lTIva10), 
                                                BigDecimal.valueOf(lTIva5), 
                                                obtenerNroFacturaCompletoConFormato());
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en inserción de facturas.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return null;
            }
            //insertar en cuenta corriente
            if(tipoDocumentoSeleccionadoLbl.getMafectaCtacte() == 'S'){
                if(lTImpuestos < 0){
                    lTImpuestos *= -1;
                }else{
                    lTGravadas = lTGravadas - lTImpuestos;
                }
                try{
                    CuentasCorrientes c = new CuentasCorrientes();
                    c.setCodEmpr(Short.parseShort("2"));
                    c.setCtipoDocum(tipoDocumentoSeleccionadoLbl.getCtipoDocum());
                    c.setFvenc(fechaVencLbl);
                    c.setFmovim(fechaFactLbl);
                    c.setNdocumCheq(String.valueOf(obtenerNroFacturaCompleto()));
                    c.setIpagado(0);
                    c.setIretencion(0);
                    c.setCodCliente(clienteBuscado);
                    c.setIsaldo(lISaldo);
                    c.setManulado(Short.parseShort("1"));
                    c.setTexentas(lTExentas);
                    c.setTgravadas(lTGravadas);
                    c.setTimpuestos(lTImpuestos);
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
                    tituloError = "Error en inserción de movimientos en cuenta corriente.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return null;
                }
            }
            //grabar los detalles de factures
            try{
                for(FacturasSer f: facturas.getFacturasSerCollection()){
                    if(f.getFacturasSerPK().getCodServicio() != 0 && (f.getIexentas() > 0 || f.getIgravadas() > 0)){
                        BigDecimal pImpuest = BigDecimal.ZERO;
                        if(f.getIgravadas() != 0){
                            pImpuest = f.getPimpues();
                        }
                        BigDecimal lImpuestos = f.getImpuestos();
                        if(tipoDocumentoSeleccionadoLbl.getMincluIva() == 'S'){
                            lImpuestos = lImpuestos.multiply(BigDecimal.valueOf(-1));
                        }
                        long total = f.getIgravadas() + f.getIexentas() + lImpuestos.longValue();
                        facturasSerFacade.insertarFacturasSer(  tipoDocumentoSeleccionadoLbl.getCtipoDocum(), 
                                                                obtenerNroFacturaCompleto(), 
                                                                f.getFacturasSerPK().getCodServicio(), 
                                                                f.getIexentas(), 
                                                                f.getIgravadas(), 
                                                                lImpuestos, 
                                                                total, 
                                                                pImpuest, 
                                                                lFFactura);
                    }
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en inserción de facturas servicios detalles.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return null;
            }
            //actualizar las existencias por las reglas en los respectivos depositos 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Datos grabados con éxito."));
            RequestContext.getCurrentInstance().execute("PF('dlgNuevFactura').hide();");
            inicializar();
            return null;
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al agregar nueva factura de servicio.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return null;
    }
    
    private boolean validarTimbradoVencido(String lCTipoDocum, long lNewNroFactura, Date lFFactura, Character tipoPapel){
        try{
            try{
                Calendar c = Calendar.getInstance();
                c.setTime(lFFactura);
                int anhoFactura = c.get(Calendar.YEAR);
                List<RangosDocumentos> listadosRangos = rangosDocumentosFacade.obtenerCursorImpuestos(lCTipoDocum, lNewNroFactura, tipoPapel, anhoFactura);
                if(listadosRangos.isEmpty()){
                    return false;
                }else{
                    for(RangosDocumentos r: listadosRangos){
                        if(r.getFvtoTimbrado().compareTo(lFFactura) < 0){
                            return false;
                        }else{
                            return true;
                        }
                    }
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en la generación de cursor de impuestos.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return false;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en función timbrado vencido.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return false;
        }
        return true;
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
    
    private String dateToString(Date fecha) {
        String resultado = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            resultado = dateFormat.format(fecha);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public List<TiposDocumentos> getListadoTiposDocumentos() {
        return listadoTiposDocumentos;
    }

    public void setListadoTiposDocumentos(List<TiposDocumentos> listadoTiposDocumentos) {
        this.listadoTiposDocumentos = listadoTiposDocumentos;
    }

    public TiposDocumentos getTipoDocumentoSeleccionadoLbl() {
        return tipoDocumentoSeleccionadoLbl;
    }

    public void setTipoDocumentoSeleccionadoLbl(TiposDocumentos tipoDocumentoSeleccionadoLbl) {
        this.tipoDocumentoSeleccionadoLbl = tipoDocumentoSeleccionadoLbl;
    }

    public List<Servicios> getListadoServicios() {
        return listadoServicios;
    }

    public void setListadoServicios(List<Servicios> listadoServicios) {
        this.listadoServicios = listadoServicios;
    }

    public Servicios getServicioSeleccionadoDet() {
        return servicioSeleccionadoDet;
    }

    public void setServicioSeleccionadoDet(Servicios servicioSeleccionadoDet) {
        this.servicioSeleccionadoDet = servicioSeleccionadoDet;
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

    public Short getPuntoEstabLbl() {
        return nPuntoEstabLbl;
    }

    public void setPuntoEstabLbl(Short nPuntoEstabLbl) {
        this.nPuntoEstabLbl = nPuntoEstabLbl;
    }

    public Short getPuntoExpedLbl() {
        return nPuntoExpedLbl;
    }

    public void setPuntoExpedLbl(Short nPuntoExpedLbl) {
        this.nPuntoExpedLbl = nPuntoExpedLbl;
    }

    public long getNroFactLbl() {
        return nroFactLbl;
    }

    public void setNroFactLbl(long nroFactLbl) {
        this.nroFactLbl = nroFactLbl;
    }

    public Clientes getClienteBuscado() {
        return clienteBuscado;
    }

    public void setClienteBuscado(Clientes clienteBuscado) {
        this.clienteBuscado = clienteBuscado;
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

    public Short getCodServicioDet() {
        return codServicioDet;
    }

    public void setCodServicioDet(Short codServicioDet) {
        this.codServicioDet = codServicioDet;
    }

    public String getObservacionLbl() {
        return observacionLbl;
    }

    public void setObservacionLbl(String observacionLbl) {
        this.observacionLbl = observacionLbl;
    }

    public Facturas getFacturas() {
        return facturas;
    }

    public void setFacturas(Facturas facturas) {
        this.facturas = facturas;
    }

    public Date getFechaVencLbl() {
        return fechaVencLbl;
    }

    public void setFechaVencLbl(Date fechaVencLbl) {
        this.fechaVencLbl = fechaVencLbl;
    }

    public long getExentasDet() {
        return exentasDet;
    }

    public void setExentasDet(long exentasDet) {
        this.exentasDet = exentasDet;
    }

    public long getGravadasDet() {
        return gravadasDet;
    }

    public void setGravadasDet(long gravadasDet) {
        this.gravadasDet = gravadasDet;
    }

    public long getDescuentosDet() {
        return descuentosDet;
    }

    public void setDescuentosDet(long descuentosDet) {
        this.descuentosDet = descuentosDet;
    }

    public boolean isHabilitaExentas() {
        return habilitaExentas;
    }

    public void setHabilitaExentas(boolean habilitaExentas) {
        this.habilitaExentas = habilitaExentas;
    }

    public boolean isHabilitaGravadas() {
        return habilitaGravadas;
    }

    public void setHabilitaGravadas(boolean habilitaGravadas) {
        this.habilitaGravadas = habilitaGravadas;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public List<Facturas> getListadoFacturas() {
        return listadoFacturas;
    }

    public void setListadoFacturas(List<Facturas> listadoFacturas) {
        this.listadoFacturas = listadoFacturas;
    }

    public LazyDataModel<Facturas> getModel() {
        return model;
    }

    public boolean isHabilitaBotonEliminar() {
        return habilitaBotonEliminar;
    }

    public void setHabilitaBotonEliminar(boolean habilitaBotonEliminar) {
        this.habilitaBotonEliminar = habilitaBotonEliminar;
    }

    public boolean isHabilitaBotonAnular() {
        return habilitaBotonAnular;
    }

    public void setHabilitaBotonAnular(boolean habilitaBotonAnular) {
        this.habilitaBotonAnular = habilitaBotonAnular;
    }

    public FacturasSer getFacturasSerSeleccionado() {
        return facturasSerSeleccionado;
    }

    public void setFacturasSerSeleccionado(FacturasSer facturasSerSeleccionado) {
        this.facturasSerSeleccionado = facturasSerSeleccionado;
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

    public Date getFechaAnulacionLbl() {
        return fechaAnulacionLbl;
    }

    public void setFechaAnulacionLbl(Date fechaAnulacionLbl) {
        this.fechaAnulacionLbl = fechaAnulacionLbl;
    }

    public Short getMotivoLbl() {
        return cMotivoLbl;
    }

    public void setMotivoLbl(Short cMotivoLbl) {
        this.cMotivoLbl = cMotivoLbl;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public FacturasFacade getFacturasFacade() {
        return facturasFacade;
    }

    public void setFacturasFacade(FacturasFacade facturasFacade) {
        this.facturasFacade = facturasFacade;
    }
    
}
