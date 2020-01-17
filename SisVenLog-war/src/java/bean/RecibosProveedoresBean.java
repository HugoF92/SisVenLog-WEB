/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.BancosFacade;
import dao.ChequesEmitidosFacade;
import dao.ComprasFacade;
import dao.CuentasProveedoresFacade;
import dao.NotasComprasFacade;
import dao.ProveedoresFacade;
import dao.RecibosProvChequesFacade;
import dao.RecibosProvDetFacade;
import dao.RecibosProvFacade;
import dao.TiposDocumentosFacade;
import dto.ChequeProveedorDto;
import dto.CompraDto;
import dto.DocumentoCompraDto;
import dto.NotasComprasDto;
import entidad.Bancos;
import entidad.ChequesEmitidos;
import entidad.ChequesEmitidosPK;
import entidad.Proveedores;
import entidad.RecibosProv;
import entidad.RecibosProvCheques;
import entidad.RecibosProvDet;
import entidad.TiposDocumentos;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import util.DateUtil;
import util.ExceptionHandlerView;

/**
 *
 * @author dadob
 */

@ManagedBean
@SessionScoped 
public class RecibosProveedoresBean implements Serializable{
    
    @EJB
    private RecibosProvFacade recibosProvFacade;
    
    @EJB
    private ProveedoresFacade proveedoresFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private ComprasFacade comprasFacade;
    
    @EJB
    private NotasComprasFacade notasComprasFacade;
    
    @EJB
    private RecibosProvDetFacade recibosProvDetFacade;
    
    @EJB
    private CuentasProveedoresFacade cuentasProveedoresFacade;
    
    @EJB
    private ChequesEmitidosFacade chequesEmitidosFacade;
    
    @EJB
    private RecibosProvChequesFacade recibosProvChequesFacade;
    
    @EJB
    private BancosFacade bancosFacade;
    
    private List<RecibosProv> listadoRecibosProv;
    private RecibosProv reciboProvSeleccionado;
    private List<Proveedores> listadoProveedores;
    private List<TiposDocumentos> listadoTiposDocumentos;
    private List<DocumentoCompraDto> listadoDocumentoCompraDto;
    private List<CompraDto> listadoCompraDto;
    private List<CompraDto> listadoCompraAuxiliarDto;
    private List<NotasComprasDto> listadoNotasComprasDto;
    private List<NotasComprasDto> listadoNotasComprasAuxiliarDto;
    private List<ChequeProveedorDto> listadoChequesEmitidos;
    private String contenidoError;
    private String tituloError;
    private boolean agregar;
    private boolean anular;
    private boolean eliminar;
    private Proveedores proveedorSeleccionadoLbl;
    private Date fechaReciboLbl;
    private long nroReciboLbl;
    private String observacionLbl;
    private Character estadoLbl;
    private int estadoLblSeleccionado;
    private long totalReciboLbl;
    private TiposDocumentos tipoDocumentoSeleccionadoDet;
    private Date fechaDocumentoDet;
    private long nroDocumentoDet;
    private long montoAPagarDet;
    private long montoFacturaDet;
    private long saldoDocumento;
    private DocumentoCompraDto documentoCompraDto;
    private long totalEfectivo;
    private long totalRetencion;
    private long totalCheques;
    private Bancos bancoSeleccionadoDet;
    private String nroChequeDet;
    private String nroCtaBancaria;
    private Date fechaChequeDet;
    private Date fechaEmisionDet;
    private long importeChequeDet;
    private ChequesEmitidos chequeEmitidoSeleccionadoDet;
    private long importeAPagarChequeDet;    
    private String comCtipoDocum;
    private long comNrofact;
    private Date comFfactur;
    private String canalCompraDet;
    private Date fechaVencDet;
    private boolean habilitaBotonEliminar;
    
    @PostConstruct
    public void init(){
        limpiarFormulario();
    }
    
    private void limpiarFormulario(){
        listadoRecibosProv = new ArrayList<>();
        listadoProveedores = new ArrayList<>();
        listadoTiposDocumentos = new ArrayList<>();
        contenidoError = "";
        tituloError = "";
        agregar = false;
        anular = false;
        eliminar = false;
        reciboProvSeleccionado = new RecibosProv();
        proveedorSeleccionadoLbl = new Proveedores();
        fechaReciboLbl = null;
        nroReciboLbl = 0;
        observacionLbl = null;
        estadoLblSeleccionado = 0;
        totalReciboLbl = 0;
        tipoDocumentoSeleccionadoDet = new TiposDocumentos();
        listadoDocumentoCompraDto = new ArrayList<>();
        listadoCompraDto = new ArrayList<>();
        saldoDocumento = 0;
        comCtipoDocum = "";
        comFfactur = null;
        comNrofact = 0;
        listadoNotasComprasDto = new ArrayList<>();
        documentoCompraDto = new DocumentoCompraDto();
        calcularTotalRecibo();
        totalEfectivo = 0;
        totalRetencion = 0;
        totalCheques = 0;
        bancoSeleccionadoDet = new Bancos();
        nroChequeDet = "";
        nroCtaBancaria = "";
        fechaChequeDet = null;
        fechaEmisionDet = null;
        importeChequeDet = 0;
        listadoChequesEmitidos = new ArrayList<>();
        chequeEmitidoSeleccionadoDet = new ChequesEmitidos();
        importeAPagarChequeDet = 0;
        listadoCompraAuxiliarDto = new ArrayList<>();
        listadoNotasComprasAuxiliarDto = new ArrayList<>();
        canalCompraDet = null;
        fechaVencDet = null;
        fechaDocumentoDet = null;
        nroDocumentoDet = 0;
        habilitaBotonEliminar = true;
        listarRecibosProv();
    }
    
    public static String convertidorFecha(Date fecha){
        return DateUtil.formaterDateToString(fecha);
    }
    
    public void listarRecibosProv(){
        try{
            listadoRecibosProv = recibosProvFacade.listadoRecibosProveedores();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de recibos del proveedor.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    public void limpiarReciboProveedor(){
        limpiarFormulario();
    }
        
    public String agregarReciboProveedor(){
        agregar = true;
        try{
            if(nroReciboLbl == 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Nro. de recibo inválido."));
                return null;
            }else{
                try{
                    String fechaReciboLblStr = dateToString(fechaReciboLbl);
                    long cantidadRecibos = recibosProvFacade.obtenerCantidadRecibosDelProveedor(nroReciboLbl, fechaReciboLblStr, proveedorSeleccionadoLbl.getCodProveed());
                    if(cantidadRecibos > 0){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Ya existe este nro. de recibo."));
                        return null;
                    }else{
                        if(!agregar){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe este nro. de recibo."));
                            return null;
                        }
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la validación de nro. de recibo.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                }
                if(proveedorSeleccionadoLbl == null){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Proveedor inválido."));
                    return null;
                }else{
                    if(proveedorSeleccionadoLbl.getCodProveed() == 0){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Proveedor inválido."));
                        return null;
                    }else{
                        if(fechaReciboLbl == null){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Fecha inálida de recibo."));
                            return null;
                        }else{
                            for (DocumentoCompraDto dc : listadoDocumentoCompraDto) {
                                if (dc.getNroDcumento() != 0) {
                                    if (dc.getTipoDocumento().getCtipoDocum().equals("FCC")) {
                                        try{
                                            String fechaDocumentoDetStr = dateToString(dc.getFechaDocumento());
                                            listadoCompraAuxiliarDto = comprasFacade.buscarFacturaCompraPorNroProveedorFechaFactura(dc.getNroDcumento(), dc.getProveedor().getCodProveed(), fechaDocumentoDetStr);
                                            if(listadoCompraAuxiliarDto.isEmpty()){
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe factura crédito."));
                                                return null;
                                            }else{
                                                if(!verificarFacturasEnRegistros(dc)){
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "La factura "+dc.getNroDcumento()+" está repetida en el recibo."));
                                                    return null;
                                                }
                                            }
                                        }catch(Exception e){
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en la lectura de compras.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                    } else if (dc.getTipoDocumento().getCtipoDocum().equals("NCC")) {
                                        try {
                                            String fechaDocumentoDetStr = dateToString(dc.getFechaDocumento());
                                            listadoNotasComprasAuxiliarDto = notasComprasFacade.comprasCreditoPorNroNotaFechaProveedor(dc.getNroDcumento(), dc.getProveedor().getCodProveed(), fechaDocumentoDetStr);
                                            if (listadoNotasComprasAuxiliarDto.isEmpty()) {
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe nota de crédito."));
                                                return null;
                                            }
                                        } catch (Exception e) {
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en la lectura de notas de créditos de compras.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                    } else if (dc.getTipoDocumento().getCtipoDocum().equals("NDC")) {
                                        try {
                                            String fechaDocumentoDetStr = dateToString(dc.getFechaDocumento());
                                            listadoNotasComprasAuxiliarDto = notasComprasFacade.comprasDebitoPorNroNotaFechaProveedor(dc.getNroDcumento(), dc.getProveedor().getCodProveed(), fechaDocumentoDetStr);
                                            if (listadoNotasComprasAuxiliarDto.isEmpty()) {
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe nota de débito."));
                                                return null;
                                            }
                                        } catch (Exception e) {
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en la lectura de notas de débitos de compras.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                    }
                                }
                            }
                            //validar totales
                            if((totalRetencion + totalEfectivo + totalCheques) != totalReciboLbl){
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Total recibo no coincide con total forma de pago."));
                                return null; 
                            }
                            long lTotal = 0;
                            for(DocumentoCompraDto dc: listadoDocumentoCompraDto){
                                lTotal += dc.getMontoAPagarDocumento();
                            }
                            if(lTotal != totalReciboLbl){
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Detalles de facturas no coincide con total recibo."));
                                return null; 
                            }
                            //validar montos de cheques
                            for(ChequeProveedorDto cp: listadoChequesEmitidos){
                                try{
                                    long totalPagadoDelProveedor = recibosProvFacade.obtenerImportePagadoDeProveedores(cp.getChequeEmitido().getChequesEmitidosPK().getNroCheque(), cp.getChequeEmitido().getBancos().getCodBanco());
                                    if((totalPagadoDelProveedor + cp.getImporteAPagar()) > cp.getChequeEmitido().getIcheque()){
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "El cheque nro. "+cp.getChequeEmitido().getChequesEmitidosPK().getNroCheque()+" ya tiene utilizado "+totalPagadoDelProveedor+" en otras facturas."));
                                        return null;
                                    }
                                }catch(Exception e){
                                    RequestContext.getCurrentInstance().update("exceptionDialog");
                                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                                    tituloError = "Error en el control de totales de cheques.";
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                    return null;
                                }
                            }
                            //insertar y/o actualizar
                            if(agregar){
                               //insertar
                               try{
                                   String fReciboStr = dateToString(fechaReciboLbl);
                                   recibosProvFacade.insertarReciboProveedor(   Short.parseShort("2"), 
                                                                                nroReciboLbl, 
                                                                                fReciboStr, 
                                                                                proveedorSeleccionadoLbl.getCodProveed(), 
                                                                                totalReciboLbl, 
                                                                                totalRetencion, 
                                                                                totalEfectivo, 
                                                                                totalCheques, 
                                                                                observacionLbl);
                               }catch(Exception e){
                                    RequestContext.getCurrentInstance().update("exceptionDialog");
                                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                                    tituloError = "Error en inserción de recibos.";
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                    return null;
                               }
                                for (DocumentoCompraDto dc : listadoDocumentoCompraDto) {
                                    try {
                                        String fFactura = dateToString(dc.getFechaDocumento());
                                        String fRecibo = dateToString(dc.getFechaRecibo());
                                        recibosProvDetFacade.insertarReciboProveedorDetalle(Short.parseShort("2"), 
                                                                                            dc.getProveedor().getCodProveed(), 
                                                                                            dc.getNroRecibo(), 
                                                                                            dc.getNroDcumento(), 
                                                                                            dc.getTipoDocumento().getCtipoDocum(), 
                                                                                            dc.getMontoAPagarDocumento(), 
                                                                                            fFactura, 
                                                                                            0, 
                                                                                            dc.getMontoDocumento(), 
                                                                                            dc.getSaldoDocumento(), 
                                                                                            fRecibo);
                                    } catch (Exception e) {
                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en inserción de detalles de recibos.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                    dc.setTipoDocumento(tiposDocumentosFacade.find(dc.getTipoDocumento().getCtipoDocum()));
                                    if(dc.getTipoDocumento().getMafectaCtacte() == 'S'){
                                        try{
                                            String lFMovim = dateToString(dc.getFechaRecibo());
                                            String nroReciboStr = String.valueOf(dc.getNroRecibo());
                                            String lFvenc = dateToString(dc.getFechaVencimiento());
                                            String otraFechaDocumStr = dateToString(dc.getFechaDocumento());
                                            String fFacturaStr = dateToString(dc.getComFfactur());
                                            short mIndice = dc.getTipoDocumento().getMindice() == null ? 0 : dc.getTipoDocumento().getMindice().shortValue();
                                            cuentasProveedoresFacade.insertarCuentaProveedor(   Short.parseShort("2"), 
                                                                                                "REP", 
                                                                                                dc.getProveedor().getCodProveed(), 
                                                                                                lFMovim, 
                                                                                                nroReciboStr, 
                                                                                                dc.getComCtipoDocum(), 
                                                                                                dc.getComNrofact(), 
                                                                                                dc.getMontoAPagarDocumento(), 
                                                                                                0, 
                                                                                                0, 
                                                                                                0, 
                                                                                                0, 
                                                                                                dc.getSaldoDocumento(), 
                                                                                                mIndice, 
                                                                                                lFvenc, 
                                                                                                dc.getTipoDocumento().getCtipoDocum(), 
                                                                                                dc.getNroDcumento(), 
                                                                                                otraFechaDocumStr, 
                                                                                                dc.getCanalCompra() == null ? "" : dc.getCanalCompra(), 
                                                                                                fFacturaStr);
                                        }catch(Exception e){
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en inserción de cuentas proveedores.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                    }
                                    //cerar NCC sin factura
                                    if(dc.getTipoDocumento().getCtipoDocum().equals("NCC")){
                                        try{
                                            String fFactura = dateToString(dc.getFechaDocumento());
                                            notasComprasFacade.disminuyeSaldoNotasCompras(dc.getMontoAPagarDocumento(), fFactura, dc.getNroDcumento(), dc.getProveedor().getCodProveed());
                                        }catch(Exception e){
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en actualización de notas de créditos.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                    }
                                }
                                //cheques
                                for (ChequeProveedorDto cp : listadoChequesEmitidos) {
                                    try {
                                        String fCheque = dateToString(cp.getChequeEmitido().getFcheque());
                                        String fEmision = dateToString(cp.getChequeEmitido().getFemision());
                                        chequesEmitidosFacade.insertarChequeEmitido(Short.parseShort("2"), 
                                                                                    cp.getChequeEmitido().getChequesEmitidosPK().getCodBanco(), 
                                                                                    cp.getChequeEmitido().getChequesEmitidosPK().getNroCheque(), 
                                                                                    cp.getChequeEmitido().getXcuentaBco(), 
                                                                                    fCheque, 
                                                                                    fEmision, 
                                                                                    cp.getChequeEmitido().getIcheque(), 
                                                                                    cp.getChequeEmitido().getCodProveed().getCodProveed());
                                    } catch (Exception e) {
                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en la inserción de nuevos cheques.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                    String nroCheque = null;
                                    try{
                                        nroCheque = cuentasProveedoresFacade.obtenerNroDocumentoCheque(  cp.getChequeEmitido().getChequesEmitidosPK().getNroCheque(), 
                                                                                                                cp.getChequeEmitido().getChequesEmitidosPK().getCodBanco());
                                    }catch(Exception e){
                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en lectura de movimiento cta. cte. proveedor de cheque.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                    if(nroCheque == null){
                                        //insertar solamente si aun no existe el movimiento cheque
                                        try{
                                            String fFMovim = dateToString(fechaReciboLbl);
                                            String fFechaCheque = dateToString(cp.getChequeEmitido().getFcheque());
                                            cuentasProveedoresFacade.insertarCuentaProveedor(   fFMovim, 
                                                                                                cp.getChequeEmitido().getChequesEmitidosPK().getNroCheque(), 
                                                                                                cp.getChequeEmitido().getIcheque(), 
                                                                                                cp.getChequeEmitido().getChequesEmitidosPK().getCodBanco(),
                                                                                                cp.getChequeEmitido().getCodProveed().getCodProveed(),
                                                                                                Short.parseShort("1"),
                                                                                                fFechaCheque,
                                                                                                "",
                                                                                                0,
                                                                                                "");
                                        }catch(Exception e){
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en inserción de cuentas proveedores.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                        try{
                                            String lFRecibo = dateToString(fechaReciboLbl);
                                            recibosProvChequesFacade.insertarChequeReciboProveedor( Short.parseShort("2"), 
                                                                                                    cp.getChequeEmitido().getCodProveed().getCodProveed(), 
                                                                                                    nroReciboLbl, 
                                                                                                    cp.getChequeEmitido().getChequesEmitidosPK().getCodBanco(), 
                                                                                                    cp.getChequeEmitido().getChequesEmitidosPK().getNroCheque(), 
                                                                                                    cp.getImporteAPagar(), 
                                                                                                    lFRecibo);
                                        }catch(Exception e){
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en inserción de recibos y cheques.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                    }
                                }
                            }else{
                                //actualizar
                                try{
                                    String lFRecibo = dateToString(fechaReciboLbl);
                                    recibosProvFacade.actualizarReciboProveedor(lFRecibo, 
                                                                                totalReciboLbl, 
                                                                                totalRetencion, 
                                                                                observacionLbl, 
                                                                                proveedorSeleccionadoLbl.getCodProveed(), 
                                                                                nroReciboLbl);
                                }catch(Exception e){
                                    RequestContext.getCurrentInstance().update("exceptionDialog");
                                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                                    tituloError = "Error en actualización de recibos.";
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                    return null;
                                }
                            }
                            //datos grabados
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Datos Grabados"));
                            limpiarFormulario(); //volvemos a instanciar
                            RequestContext.getCurrentInstance().execute("PF('dlgNuevReciboCompra').hide();");
                            return null;
                        }
                    }
                }    
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al agregar un recibo compra.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return null;
        }
    }
    
    private boolean verificarFacturasEnRegistros(DocumentoCompraDto doc){
        long contador = 0;
        if(!listadoDocumentoCompraDto.isEmpty()){
            for(DocumentoCompraDto dc: listadoDocumentoCompraDto){
                if( dc.getNroDcumento() == doc.getNroDcumento() &&
                    dc.getNroRecibo() == doc.getNroRecibo() &&
                    dc.getProveedor().getCodProveed().compareTo(doc.getProveedor().getCodProveed()) == 0){
                    contador += 1;
                }
            }
        }
        if(contador > 1){
            return false;
        }
        return true;
    }
    
    public String borrarReciboProveedor(){
        eliminar = true;
        Collection<RecibosProvDet> recibosProveedoresABorrar = reciboProvSeleccionado.getRecibosProvDetCollection();
        Collection<RecibosProvCheques> recibosChequesDeProveedoresABorrar = reciboProvSeleccionado.getRecibosProvChequesCollection();
        try{
            recibosProvFacade.borrarRecibosDelProveedor(reciboProvSeleccionado.getProveedores().getCodProveed(), reciboProvSeleccionado.getRecibosProvPK().getNrecibo());
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al eliminar un recibo compra.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return null;
        }
        for(RecibosProvDet reciboDet: recibosProveedoresABorrar){
            try{
                if(reciboDet.getRecibosProvDetPK().getCtipoDocum().equals("FCC")){
                    String nroReciboStr = String.valueOf(reciboDet.getRecibosProvDetPK().getNrecibo());
                    cuentasProveedoresFacade.borrarRecibosProveedores(reciboDet.getRecibosProvDetPK().getCodProveed(), nroReciboStr, reciboDet.getRecibosProvDetPK().getNrofact(), reciboDet.getRecibosProvDetPK().getCtipoDocum());
                }else{
                    String nroReciboStr = String.valueOf(reciboDet.getRecibosProvDetPK().getNrecibo());
                    cuentasProveedoresFacade.borrarOtrosRecibosProveedores(reciboDet.getRecibosProvDetPK().getCodProveed(), nroReciboStr, reciboDet.getRecibosProvDetPK().getNrofact(), reciboDet.getRecibosProvDetPK().getCtipoDocum());
                }
                if(reciboDet.getRecibosProvDetPK().getCtipoDocum().equals("NCC")){
                    try{
                        String lFFactura = dateToString(reciboDet.getFfactur());
                        notasComprasFacade.aumentaSaldoNotasCompras(reciboDet.getItotal(), lFFactura, reciboDet.getRecibosProvDetPK().getNrofact(), reciboDet.getRecibosProvDetPK().getCodProveed());
                    }catch(Exception e){
                        RequestContext.getCurrentInstance().update("exceptionDialog");
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error al actualizar notas de créditos.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return null; 
                    }
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en eliminación de cuentas proveedores.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return null; 
            } 
        }
        for(RecibosProvCheques reciboChequeDet: recibosChequesDeProveedoresABorrar){
            try{
                cuentasProveedoresFacade.borrarChequesProveedores(  reciboChequeDet.getRecibosProvChequesPK().getCodProveed(), 
                                                                    reciboChequeDet.getRecibosProvChequesPK().getNroCheque(), 
                                                                    reciboChequeDet.getRecibosProvChequesPK().getCodBanco());
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en eliminación de cheques en cuentas proveedores.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return null;
            }
        }
        limpiarFormulario();
        RequestContext.getCurrentInstance().execute("PF('dlgAnularReciboCompra').hide();");
        return null;
    }
    
    
    public List<TiposDocumentos> listarTiposDocumentos(){
        listadoTiposDocumentos = new ArrayList<>();
        try{
            listadoTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoParaReciboCompraProveedor();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de tipos de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoTiposDocumentos;
    }
    
    public void onRowSelect(SelectEvent event) {
        if(reciboProvSeleccionado != null){
            if(reciboProvSeleccionado.getRecibosProvPK().getNrecibo() != 0){
                setHabilitaBotonEliminar(false);
            }else{
                setHabilitaBotonEliminar(true);
            }
        }
    }
    
    private void estabecerEstado(){
        switch(estadoLblSeleccionado){
            case 1: estadoLbl = 'A';
                break;
            case 2: estadoLbl = 'I';
                break;
            case 3: estadoLbl = 0;
                break;
        }
    }
    
    public void cargarDetalleDocumentos(){
        try{
            if(validarNroDocumentoDet()){
                switch (tipoDocumentoSeleccionadoDet.getCtipoDocum()) {
                    case "FCC":
                        if(procesarFacturaCompra()){
                            //cargar en la lista de documentos
                            DocumentoCompraDto dcdto = new DocumentoCompraDto();
                            dcdto.setTipoDocumento(tipoDocumentoSeleccionadoDet);
                            dcdto.setProveedor(proveedorSeleccionadoLbl);
                            dcdto.setFechaDocumento(fechaDocumentoDet);
                            dcdto.setFechaRecibo(fechaReciboLbl);
                            dcdto.setMontoAPagarDocumento(montoAPagarDet);
                            dcdto.setMontoDocumento(montoFacturaDet);
                            dcdto.setSaldoDocumento(saldoDocumento);
                            dcdto.setNroDcumento(nroDocumentoDet);
                            dcdto.setNroRecibo(nroReciboLbl);
                            dcdto.setObservacion(observacionLbl);
                            dcdto.setComCtipoDocum(comCtipoDocum);
                            dcdto.setComFfactur(comFfactur);
                            dcdto.setComNrofact(comNrofact);
                            dcdto.setCanalCompra(canalCompraDet);
                            dcdto.setFechaVencimiento(fechaVencDet);
                            listadoDocumentoCompraDto.add(dcdto);
                            calcularTotalRecibo();
                            //limpiar
                            tipoDocumentoSeleccionadoDet = null;
                            fechaDocumentoDet = null;
                            nroDocumentoDet = 0;
                        }
                        break;
                    case "NCC":
                        if(procesarNotaCredito()){
                            //cargar en la lista de documentos
                            DocumentoCompraDto dcdto = new DocumentoCompraDto();
                            dcdto.setTipoDocumento(tipoDocumentoSeleccionadoDet);
                            dcdto.setProveedor(proveedorSeleccionadoLbl);
                            dcdto.setFechaDocumento(fechaDocumentoDet);
                            dcdto.setFechaRecibo(fechaReciboLbl);
                            dcdto.setMontoAPagarDocumento(montoAPagarDet);
                            dcdto.setMontoDocumento(montoFacturaDet);
                            dcdto.setSaldoDocumento(saldoDocumento);
                            dcdto.setNroDcumento(nroDocumentoDet);
                            dcdto.setNroRecibo(nroReciboLbl);
                            dcdto.setObservacion(observacionLbl);
                            dcdto.setComCtipoDocum(comCtipoDocum);
                            dcdto.setComFfactur(comFfactur);
                            dcdto.setComNrofact(comNrofact);
                            dcdto.setCanalCompra(canalCompraDet);
                            dcdto.setFechaVencimiento(fechaVencDet);
                            listadoDocumentoCompraDto.add(dcdto);
                            calcularTotalRecibo();
                        }
                        break;
                    case "NDC":
                        if(procesarNotaDebito()){
                            //cargar en la lista de documentos
                            DocumentoCompraDto dcdto = new DocumentoCompraDto();
                            dcdto.setTipoDocumento(tipoDocumentoSeleccionadoDet);
                            dcdto.setProveedor(proveedorSeleccionadoLbl);
                            dcdto.setFechaDocumento(fechaDocumentoDet);
                            dcdto.setFechaRecibo(fechaReciboLbl);
                            dcdto.setMontoAPagarDocumento(montoAPagarDet);
                            dcdto.setMontoDocumento(montoFacturaDet);
                            dcdto.setSaldoDocumento(saldoDocumento);
                            dcdto.setNroDcumento(nroDocumentoDet);
                            dcdto.setNroRecibo(nroReciboLbl);
                            dcdto.setObservacion(observacionLbl);
                            dcdto.setComCtipoDocum(comCtipoDocum);
                            dcdto.setComFfactur(comFfactur);
                            dcdto.setComNrofact(comNrofact);
                            dcdto.setCanalCompra(canalCompraDet);
                            dcdto.setFechaVencimiento(fechaVencDet);
                            listadoDocumentoCompraDto.add(dcdto);
                            calcularTotalRecibo();
                        }
                        break;
                    default:
                        break;
                }
                
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al cargar un detalle de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    public void cargarDetalleCheques(){
        try{
            //listadoChequesEmitidos = new ArrayList<>();
            if(validarIngresoCheques()){
                ChequesEmitidosPK chequeEmitidoPK = new ChequesEmitidosPK();
                chequeEmitidoPK.setCodEmpr(Short.parseShort("2"));
                chequeEmitidoPK.setCodBanco(bancoSeleccionadoDet.getCodBanco());
                chequeEmitidoPK.setNroCheque(nroChequeDet);
                ChequesEmitidos chequeEmitido = new ChequesEmitidos();
                chequeEmitido.setChequesEmitidosPK(chequeEmitidoPK);
                chequeEmitido.setXcuentaBco(nroCtaBancaria);
                chequeEmitido.setIcheque(importeChequeDet);
                chequeEmitido.setFemision(fechaEmisionDet);
                chequeEmitido.setFcheque(fechaChequeDet);
                chequeEmitido.setCodProveed(proveedoresFacade.find(proveedorSeleccionadoLbl.getCodProveed()));
                chequeEmitido.setBancos(bancosFacade.find(bancoSeleccionadoDet.getCodBanco()));
                ChequeProveedorDto cpdto = new ChequeProveedorDto();
                cpdto.setChequeEmitido(chequeEmitido);
                cpdto.setImporteAPagar(importeAPagarChequeDet);
                listadoChequesEmitidos.add(cpdto);
                calcularTotalCheque();
                bancoSeleccionadoDet = null;
                nroChequeDet = null;
                nroCtaBancaria = null;
                fechaChequeDet = null;
                fechaEmisionDet = null;
                importeChequeDet = 0;
                importeAPagarChequeDet = 0;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al cargar un detalle de cheques.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    private boolean validarIngresoCheques(){
        if(bancoSeleccionadoDet == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un banco."));
            return false;
        }else{
            if(bancoSeleccionadoDet.getCodBanco() == 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un banco."));
                return false;
            }else{
                if(nroChequeDet.equals("") || nroChequeDet == null){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar un nro. de cheque."));
                    return false;
                }else{
                    if(nroCtaBancaria.equals("") || nroCtaBancaria == null){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar un nro. de cuenta bancaria."));
                        return false;
                    }else{
                        if(fechaChequeDet == null){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar la fecha del cheque."));
                            return false;
                        }else{
                            if(fechaEmisionDet == null){
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar la fecha de emisión del cheque."));
                                return false;
                            }else{
                                if(importeChequeDet == 0){
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar el importe del cheque."));
                                    return false;
                                }else{
                                    if(proveedorSeleccionadoLbl == null){
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un proveedor."));
                                        return false;
                                    }else{
                                        if(proveedorSeleccionadoLbl.getCodProveed() == 0){
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un proveedor."));
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public void borrarFilaDocumento(DocumentoCompraDto documentoCompraDtoABorrar){
        try{
            Iterator itr = listadoDocumentoCompraDto.iterator();
            while (itr.hasNext()) {
                DocumentoCompraDto documentoComptaDtoEnLista = (DocumentoCompraDto) itr.next();
                if( documentoComptaDtoEnLista.getTipoDocumento().getCtipoDocum().equals(documentoCompraDtoABorrar.getTipoDocumento().getCtipoDocum()) &&
                    documentoComptaDtoEnLista.getProveedor().getCodProveed().compareTo(documentoCompraDtoABorrar.getProveedor().getCodProveed()) == 0 && 
                    documentoComptaDtoEnLista.getNroRecibo() == documentoCompraDtoABorrar.getNroRecibo() &&
                    documentoComptaDtoEnLista.getNroDcumento() == documentoCompraDtoABorrar.getNroDcumento() ){
                    itr.remove();
                    calcularTotalRecibo();
                    return;
                } 
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al borrar un detalle de la grilla de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
    }
    
    public void borrarFilaCheque(ChequeProveedorDto chequeABorrar){
        try{
            Iterator itr = listadoChequesEmitidos.iterator();
            while (itr.hasNext()) {
                ChequeProveedorDto chequeProveedorEnLista = (ChequeProveedorDto) itr.next();
                if( chequeProveedorEnLista.getChequeEmitido().getChequesEmitidosPK().getCodBanco() == chequeABorrar.getChequeEmitido().getChequesEmitidosPK().getCodBanco() &&
                    chequeProveedorEnLista.getChequeEmitido().getChequesEmitidosPK().getNroCheque().equals(chequeABorrar.getChequeEmitido().getChequesEmitidosPK().getNroCheque()) &&
                    chequeProveedorEnLista.getChequeEmitido().getChequesEmitidosPK().getCodEmpr() == chequeABorrar.getChequeEmitido().getChequesEmitidosPK().getCodEmpr()){
                    itr.remove();
                    calcularTotalCheque();
                    return;
                }
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al borrar un detalle de la grilla de cheques.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
    }
    
    private boolean validarNroDocumentoDet() {
        try {
            if(nroReciboLbl == 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar el nro. de recibo de compra."));
                return false;
            }else{
                if(nroDocumentoDet == 0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar el nro. de factura."));
                    return false;
                }else{
                    if(proveedorSeleccionadoLbl == null){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un proveedor."));
                        return false;
                    }else{
                        if(proveedorSeleccionadoLbl.getCodProveed() == 0){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un proveedor."));
                            return false;
                        }else{
                            if(tipoDocumentoSeleccionadoDet == null){
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un tipo de documento."));
                                return false;
                            }else{
                                if(tipoDocumentoSeleccionadoDet.getCtipoDocum() == null || tipoDocumentoSeleccionadoDet.getCtipoDocum().equals("")){
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un tipo de documento."));
                                    return false;
                                }else{
                                    if(verificarDuplicadosEnListadoDeDetalle()){
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Este documento ya ha sido registrado."));
                                        return false;
                                    }else{
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la validacion del nro. de documento.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return false;
        }
    }
    
    private boolean procesarNotaDebito(){
        //listadoNotasComprasDto = new ArrayList<>();
        try{
            if (fechaDocumentoDet == null) {
                listadoNotasComprasDto = notasComprasFacade.maximaCompraDebitoDelProveedor(nroDocumentoDet, proveedorSeleccionadoLbl.getCodProveed());
            }else{
                String fechaDocumentoDetStr = fechaDocumentoDet == null ? "" : dateToString(fechaDocumentoDet);
                listadoNotasComprasDto = notasComprasFacade.comprasDebitoPorNroNotaFechaProveedor(nroDocumentoDet, proveedorSeleccionadoLbl.getCodProveed(), fechaDocumentoDetStr);
            }
            
            if(listadoNotasComprasDto.isEmpty()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe nota de credito."));
                return false;
            }else{
                for(NotasComprasDto ncdto: listadoNotasComprasDto){
                    montoFacturaDet = ncdto.getNotaCompra().getTtotal();
                    montoAPagarDet = ncdto.getNotaCompra().getIsaldo();
                    comCtipoDocum = ncdto.getNotaCompra().getCompras().getComprasPK().getCtipoDocum();
                    comFfactur = ncdto.getNotaCompra().getCompras().getComprasPK().getFfactur();
                    comNrofact = ncdto.getNotaCompra().getCompras().getComprasPK().getNrofact();
                    fechaDocumentoDet = ncdto.getNotaCompra().getNotasComprasPK().getFdocum();  //está bien?
                    montoAPagarDet = ncdto.getNotaCompra().getTtotal();
                    montoFacturaDet = ncdto.getNotaCompra().getTtotal();
                    saldoDocumento = ncdto.getNotaCompra().getIsaldo();
                    canalCompraDet = ncdto.getNotaCompra().getCompras().getCcanalCompra();
                    fechaVencDet = ncdto.getNotaCompra().getCompras().getFvenc();
                }
            }
            return true;
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de notas de debitos de compras.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return false;
        }
    }
    
    private boolean procesarNotaCredito(){
        //listadoNotasComprasDto = new ArrayList<>();
        try{
            if (fechaDocumentoDet == null) {
                listadoNotasComprasDto = notasComprasFacade.maximaCompraCreditoDelProveedor(nroDocumentoDet, proveedorSeleccionadoLbl.getCodProveed());
            }else{
                String fechaDocumentoDetStr = fechaDocumentoDet == null ? "" : dateToString(fechaDocumentoDet);
                listadoNotasComprasDto = notasComprasFacade.comprasCreditoPorNroNotaFechaProveedor(nroDocumentoDet, proveedorSeleccionadoLbl.getCodProveed(), fechaDocumentoDetStr);
            }
            
            if(listadoNotasComprasDto.isEmpty()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe nota de credito."));
                return false;
            }else{
                for(NotasComprasDto ncdto: listadoNotasComprasDto){
                    montoFacturaDet = ncdto.getNotaCompra().getTtotal();
                    montoAPagarDet = ncdto.getNotaCompra().getIsaldo();
                    comCtipoDocum = ncdto.getNotaCompra().getCompras().getComprasPK().getCtipoDocum();
                    comFfactur = ncdto.getNotaCompra().getCompras().getComprasPK().getFfactur();
                    comNrofact = ncdto.getNotaCompra().getCompras().getComprasPK().getNrofact();
                    fechaDocumentoDet = ncdto.getNotaCompra().getNotasComprasPK().getFdocum();  //está bien?
                    montoAPagarDet = ncdto.getNotaCompra().getTtotal() * -1;
                    montoFacturaDet = ncdto.getNotaCompra().getTtotal() * -1;
                    saldoDocumento = ncdto.getNotaCompra().getIsaldo();
                    canalCompraDet = ncdto.getNotaCompra().getCompras().getCcanalCompra();
                    fechaVencDet = ncdto.getNotaCompra().getCompras().getFvenc();
                }
            }
            return true;
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de notas de creditos de compras.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return false;
        }
    }
    
    private boolean procesarFacturaCompra() {
        //listadoDocumentoCompraDto = new ArrayList<>();
        try {
            if (fechaDocumentoDet == null) {
                listadoCompraDto = comprasFacade.maximaCompraContadoDelProveedor(nroDocumentoDet, proveedorSeleccionadoLbl.getCodProveed());
            }else {
                String fechaDocumentoDetStr = fechaDocumentoDet == null ? "" : dateToString(fechaDocumentoDet);
                listadoCompraDto = comprasFacade.comprasContadoPorFechaNroFacturaProveedor(nroDocumentoDet, proveedorSeleccionadoLbl.getCodProveed(), fechaDocumentoDetStr);
            }
            
            if(listadoCompraDto.isEmpty()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe factura credito."));
                return false;
            }else{
                for(CompraDto cdto: listadoCompraDto){
                    montoFacturaDet = cdto.getCompra().getTtotal();
                    montoAPagarDet = cdto.getCompra().getIsaldo();
                    saldoDocumento = cdto.getCompra().getIsaldo();
                    //cargar al detalle
                    comCtipoDocum = cdto.getCompra().getComprasPK().getCtipoDocum();
                    comFfactur = cdto.getCompra().getComprasPK().getFfactur();
                    comNrofact = cdto.getCompra().getComprasPK().getNrofact();
                    canalCompraDet = cdto.getCompra().getCcanalCompra();
                    fechaVencDet = cdto.getCompra().getFvenc();
                }
            }
            return true;
        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de facturas de compras.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return false;
        }
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
    
    private boolean verificarDuplicadosEnListadoDeDetalle(){
        for(DocumentoCompraDto dcdto: listadoDocumentoCompraDto){
            if( dcdto.getNroRecibo() == nroReciboLbl && 
                dcdto.getNroDcumento() == nroDocumentoDet &&
                dcdto.getProveedor().getCodProveed().compareTo(proveedorSeleccionadoLbl.getCodProveed()) == 0 && 
                dcdto.getTipoDocumento().getCtipoDocum().equals(tipoDocumentoSeleccionadoDet.getCtipoDocum())){
                return true;
            }
        }
        return false;
    }
    
    private void calcularTotalRecibo(){
        totalReciboLbl = 0;
        if(!listadoDocumentoCompraDto.isEmpty()){
            for(DocumentoCompraDto dcdto: listadoDocumentoCompraDto){
                totalReciboLbl += dcdto.getMontoDocumento();
            }
        }
    }
    
    private void calcularTotalCheque(){
        totalCheques = 0;
        if(!listadoChequesEmitidos.isEmpty()){
            for(ChequeProveedorDto ce: listadoChequesEmitidos){
                totalCheques += ce.getChequeEmitido().getIcheque();
            }
        }
    }
    
    public void cerrarDialogoSinGuardar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarRecibos').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevReciboCompra').hide();");
    }
    
    public void onCellEdit(CellEditEvent event) {  
        Object oldValue = event.getOldValue();  
        Object newValue = event.getNewValue();  
        if(!oldValue.equals(newValue)){
            DataTable table = (DataTable) event.getSource();
            DocumentoCompraDto documentoDtoRow = (DocumentoCompraDto) table.getRowData();
            getDocumentoCompraDto().setMontoAPagarDocumento(documentoDtoRow.getMontoAPagarDocumento());
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Monto a pagar actualizado.", ""));
        }
    }
    
    public List<RecibosProv> getListadoRecibosProv() {
        return listadoRecibosProv;
    }

    public void setListadoRecibosProv(List<RecibosProv> listadoRecibosProv) {
        this.listadoRecibosProv = listadoRecibosProv;
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

    public boolean isAgregar() {
        return agregar;
    }

    public void setAgregar(boolean agregar) {
        this.agregar = agregar;
    }

    public boolean isAnular() {
        return anular;
    }

    public void setAnular(boolean anular) {
        this.anular = anular;
    }

    public boolean isEliminar() {
        return eliminar;
    }

    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

    public List<Proveedores> getListadoProveedores() {
        return listadoProveedores;
    }

    public void setListadoProveedores(List<Proveedores> listadoProveedores) {
        this.listadoProveedores = listadoProveedores;
    }

    public List<TiposDocumentos> getListadoTiposDocumentos() {
        return listadoTiposDocumentos;
    }

    public void setListadoTiposDocumentos(List<TiposDocumentos> listadoTiposDocumentos) {
        this.listadoTiposDocumentos = listadoTiposDocumentos;
    }

    public RecibosProv getReciboProvSeleccionado() {
        return reciboProvSeleccionado;
    }

    public void setReciboProvSeleccionado(RecibosProv reciboProvSeleccionado) {
        this.reciboProvSeleccionado = reciboProvSeleccionado;
    }

    public Proveedores getProveedorSeleccionadoLbl() {
        return proveedorSeleccionadoLbl;
    }

    public void setProveedorSeleccionadoLbl(Proveedores proveedorSeleccionadoLbl) {
        this.proveedorSeleccionadoLbl = proveedorSeleccionadoLbl;
    }

    public Date getFechaReciboLbl() {
        return fechaReciboLbl;
    }

    public void setFechaReciboLbl(Date fechaReciboLbl) {
        this.fechaReciboLbl = fechaReciboLbl;
    }

    public long getNroReciboLbl() {
        return nroReciboLbl;
    }

    public void setNroReciboLbl(long nroReciboLbl) {
        this.nroReciboLbl = nroReciboLbl;
    }

    public String getObservacionLbl() {
        return observacionLbl;
    }

    public void setObservacionLbl(String observacionLbl) {
        this.observacionLbl = observacionLbl;
    }

    public Character getEstadoLbl() {
        return estadoLbl;
    }

    public void setEstadoLbl(Character estadoLbl) {
        this.estadoLbl = estadoLbl;
    }

    public int getEstadoLblSeleccionado() {
        return estadoLblSeleccionado;
    }

    public void setEstadoLblSeleccionado(int estadoLblSeleccionado) {
        this.estadoLblSeleccionado = estadoLblSeleccionado;
    }

    public long getTotalReciboLbl() {
        return totalReciboLbl;
    }

    public void setTotalReciboLbl(long totalReciboLbl) {
        this.totalReciboLbl = totalReciboLbl;
    }

    public TiposDocumentos getTipoDocumentoSeleccionadoDet() {
        return tipoDocumentoSeleccionadoDet;
    }

    public void setTipoDocumentoSeleccionadoDet(TiposDocumentos tipoDocumentoSeleccionadoDet) {
        this.tipoDocumentoSeleccionadoDet = tipoDocumentoSeleccionadoDet;
    }

    public Date getFechaDocumentoDet() {
        return fechaDocumentoDet;
    }

    public void setFechaDocumentoDet(Date fechaDocumentoDet) {
        this.fechaDocumentoDet = fechaDocumentoDet;
    }

    public long getNroDocumentoDet() {
        return nroDocumentoDet;
    }

    public void setNroDocumentoDet(long nroDocumentoDet) {
        this.nroDocumentoDet = nroDocumentoDet;
    }

    public long getMontoAPagarDet() {
        return montoAPagarDet;
    }

    public void setMontoAPagarDet(long montoAPagarDet) {
        this.montoAPagarDet = montoAPagarDet;
    }

    public long getMontoFacturaDet() {
        return montoFacturaDet;
    }

    public void setMontoFacturaDet(long montoFacturaDet) {
        this.montoFacturaDet = montoFacturaDet;
    }

    public List<DocumentoCompraDto> getListadoDocumentoCompraDto() {
        return listadoDocumentoCompraDto;
    }

    public void setListadoDocumentoCompraDto(List<DocumentoCompraDto> listadoDocumentoCompraDto) {
        this.listadoDocumentoCompraDto = listadoDocumentoCompraDto;
    }

    public long getSaldoDocumento() {
        return saldoDocumento;
    }

    public void setSaldoDocumento(long saldoDocumento) {
        this.saldoDocumento = saldoDocumento;
    }

    public DocumentoCompraDto getDocumentoCompraDto() {
        return documentoCompraDto;
    }

    public void setDocumentoCompraDto(DocumentoCompraDto documentoCompraDto) {
        this.documentoCompraDto = documentoCompraDto;
    }

    public long getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(long totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public long getTotalRetencion() {
        return totalRetencion;
    }

    public void setTotalRetencion(long totalRetencion) {
        this.totalRetencion = totalRetencion;
    }

    public long getTotalCheques() {
        return totalCheques;
    }

    public void setTotalCheques(long totalCheques) {
        this.totalCheques = totalCheques;
    }

    public Bancos getBancoSeleccionadoDet() {
        return bancoSeleccionadoDet;
    }

    public void setBancoSeleccionadoDet(Bancos bancoSeleccionadoDet) {
        this.bancoSeleccionadoDet = bancoSeleccionadoDet;
    }

    public String getNroChequeDet() {
        return nroChequeDet;
    }

    public void setNroChequeDet(String nroChequeDet) {
        this.nroChequeDet = nroChequeDet;
    }

    public String getNroCtaBancaria() {
        return nroCtaBancaria;
    }

    public void setNroCtaBancaria(String nroCtaBancaria) {
        this.nroCtaBancaria = nroCtaBancaria;
    }

    public Date getFechaChequeDet() {
        return fechaChequeDet;
    }

    public void setFechaChequeDet(Date fechaChequeDet) {
        this.fechaChequeDet = fechaChequeDet;
    }

    public Date getFechaEmisionDet() {
        return fechaEmisionDet;
    }

    public void setFechaEmisionDet(Date fechaEmisionDet) {
        this.fechaEmisionDet = fechaEmisionDet;
    }

    public long getImporteChequeDet() {
        return importeChequeDet;
    }

    public void setImporteChequeDet(long importeChequeDet) {
        this.importeChequeDet = importeChequeDet;
    }

    public List<ChequeProveedorDto> getListadoChequesEmitidos() {
        return listadoChequesEmitidos;
    }

    public void setListadoChequesEmitidos(List<ChequeProveedorDto> listadoChequesEmitidos) {
        this.listadoChequesEmitidos = listadoChequesEmitidos;
    }


    public ChequesEmitidos getChequeEmitidoSeleccionadoDet() {
        return chequeEmitidoSeleccionadoDet;
    }

    public void setChequeEmitidoSeleccionadoDet(ChequesEmitidos chequeEmitidoSeleccionadoDet) {
        this.chequeEmitidoSeleccionadoDet = chequeEmitidoSeleccionadoDet;
    }

    public long getImporteAPagarChequeDet() {
        return importeAPagarChequeDet;
    }

    public void setImporteAPagarChequeDet(long importeAPagarChequeDet) {
        this.importeAPagarChequeDet = importeAPagarChequeDet;
    }   

    public boolean isHabilitaBotonEliminar() {
        return habilitaBotonEliminar;
    }

    public void setHabilitaBotonEliminar(boolean habilitaBotonEliminar) {
        this.habilitaBotonEliminar = habilitaBotonEliminar;
    }
    
}