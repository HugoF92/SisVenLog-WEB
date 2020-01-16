/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.BancosFacade;
import dao.ChequesFacade;
import dao.ClientesFacade;
import dao.CuentasCorrientesFacade;
import dao.FacturasFacade;
import dao.NotasVentasFacade;
import dao.RecibosChequesFacade;
import dao.RecibosDetFacade;
import dao.RecibosFacade;
import dao.TiposDocumentosFacade;
import dao.ZonasFacade;
import dto.ChequeDetalleDto;
import dto.DocumentoVentaDto;
import dto.NotaVentaDto;
import entidad.Bancos;
import entidad.Cheques;
import entidad.ChequesPK;
import entidad.Clientes;
import entidad.CuentasCorrientes;
import entidad.Facturas;
import entidad.NotasVentas;
import entidad.Recibos;
import entidad.RecibosDet;
import entidad.TiposDocumentos;
import entidad.Zonas;
import entidad.ZonasPK;
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
public class RecibosClientesBean implements Serializable{
    
    @EJB
    private RecibosFacade recibosFacade;
    
    @EJB
    private ZonasFacade zonasFacade;
    
    @EJB
    private BancosFacade bancosFacade;
    
    @EJB
    private ClientesFacade clientesFacade;
    
    @EJB
    private FacturasFacade facturasFacade;
    
    @EJB
    private NotasVentasFacade notasVentasFacade;
    
    @EJB
    private ChequesFacade chequesFacade;
    
    @EJB
    private RecibosChequesFacade recibosChequesFacade;
    
    @EJB
    private RecibosDetFacade recibosDetFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private CuentasCorrientesFacade cuentasCorrientesFacade;
    
    private List<DocumentoVentaDto> listaDocumentoVentaDto;
    private DocumentoVentaDto documentoVentaDtoSeleccionadoDet;
    private List<ChequeDetalleDto> listaChequesDet;
    private ChequeDetalleDto chequeSeleccionadoDet;
    private String contenidoError;
    private String tituloError;
    private Date fechaReciboLbl;
    private long nroReciboLbl;
    private String observacionLbl;
    private long totalReciboLbl;
    private TiposDocumentos tipoDocumentoSeleccionadoDet;
    private Date fechaDocumentoDet;
    private long nroDocumentoDet;
    private long montoAPagarDet;
    private long montoFacturaDet;
    private long saldoDocumentoDet;
    private long interesDocumentoDet;
    private Date fechaVencimientoDocDet;
    private long totalEfectivo;
    private long totalRetencion;
    private long totalCheques;
    private String nroChequeDet;
    private String nroCtaBancaria;
    private Date fechaChequeDet;
    private Date fechaEmisionDet;
    private long importeChequeDet;
    private long importeAPagarChequeDet; 
    private boolean habilitaBotonEliminar;
    private List<Recibos> listadoRecibos;
    private String codigoZonaSeleccionadoLbl;
    private Zonas zonaSeleccionadaLbl;
    private Integer clienteSeleccionadoLbl;
    private Bancos bancoSeleccionadoDet;
    private List<Facturas> listadoFacturas;
    private List<NotaVentaDto> listadoNotasVentasDto;
    private long iSaldoFact;
    private Character tipoChequeDet;
    private List<Cheques> listadoCheques;
    private String titularChequeDet;
    private Recibos reciboSeleccionado;
    private String nombreClienteLbl;
    private Clientes clientes;
    private List<Clientes> listaClientes;
    private String filtro;
    private List<TiposDocumentos> listadoTiposDocumentos;
    
    @PostConstruct
    public void init(){
        limpiarFormulario();
    }
    
    private void limpiarFormulario(){
        listaDocumentoVentaDto = new ArrayList<>();
        documentoVentaDtoSeleccionadoDet = new DocumentoVentaDto();
        listaChequesDet = new ArrayList();
        chequeSeleccionadoDet = new ChequeDetalleDto();
        contenidoError = null;
        tituloError = null;
        fechaReciboLbl = null;
        nroReciboLbl = 0;
        observacionLbl = null;
        totalReciboLbl = 0;
        tipoDocumentoSeleccionadoDet = new TiposDocumentos();
        fechaDocumentoDet = null;
        nroDocumentoDet = 0;
        montoAPagarDet = 0;
        montoFacturaDet = 0;
        saldoDocumentoDet = 0;
        interesDocumentoDet = 0;
        totalEfectivo = 0;
        totalRetencion = 0;
        totalCheques = 0;
        nroChequeDet = null;
        nroCtaBancaria = null;
        fechaChequeDet = null;
        fechaEmisionDet = null;
        importeChequeDet = 0;
        importeAPagarChequeDet = 0;
        listadoRecibos = new ArrayList<>();
        zonaSeleccionadaLbl = new Zonas();
        codigoZonaSeleccionadoLbl = null;
        clienteSeleccionadoLbl = null;
        bancoSeleccionadoDet = new Bancos();
        listadoFacturas = new ArrayList<>();
        listadoNotasVentasDto = new ArrayList<>();
        iSaldoFact = 0;
        tipoChequeDet = 'D';
        listadoCheques = new ArrayList<>();
        titularChequeDet = null;
        fechaVencimientoDocDet = null;
        reciboSeleccionado = new Recibos();
        nombreClienteLbl = null;
        clientes = new Clientes();
        listaClientes = new ArrayList<>();
        filtro = null;
        listadoTiposDocumentos = new ArrayList<>();
        habilitaBotonEliminar = true;
        listarRecibos();
    }
    
    public void limpiarReciboAClientes(){
        limpiarFormulario();
    }
    
    public void onRowClientesSelect(SelectEvent event) {
        if (getClientes() != null) {
            if (getClientes().getXnombre() != null) {
                clienteSeleccionadoLbl = getClientes().getCodCliente();
                nombreClienteLbl = getClientes().getXnombre();
                RequestContext.getCurrentInstance().update("panel_grid_nuevo_recibo_compra");
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').hide();");
            }
        }
    }
    
    public void onRowSelect(SelectEvent event) {
        if(reciboSeleccionado != null){
            if(reciboSeleccionado.getRecibosPK().getNrecibo() != 0){
                setHabilitaBotonEliminar(false);
            }else{
                setHabilitaBotonEliminar(true);
            }
        }
    }
    
    public String anularRecibo(){
        try{
            boolean notasActualizadas = true;
            if(reciboSeleccionado.getMestado() == 'A'){
                Collection<RecibosDet> listaRecibosDet =  reciboSeleccionado.getRecibosDetCollection();
                //Collection<RecibosCheques> listaRecibosCheques = reciboSeleccionado.getRecibosChequesCollection();
                for(RecibosDet rd: listaRecibosDet){
                    TiposDocumentos tipoDocumento = tiposDocumentosFacade.find(rd.getRecibosDetPK().getCtipoDocum());
                    if(rd.getRecibosDetPK().getCtipoDocum().equals("FCR") || rd.getRecibosDetPK().getCtipoDocum().equals("FCP")){
                        if(tipoDocumento.getMafectaCtacte() == 'S'){
                            try{
                                CuentasCorrientes cc = new CuentasCorrientes();
                                cc.setCodEmpr(Short.parseShort("2"));
                                cc.setCtipoDocum("REC");
                                cc.setCodCliente(clientesFacade.find(reciboSeleccionado.getCodCliente()));
                                cc.setFmovim(reciboSeleccionado.getFrecibo());
                                cc.setNdocumCheq(String.valueOf(reciboSeleccionado.getRecibosPK().getNrecibo()));
                                cc.setFacCtipoDocum(rd.getRecibosDetPK().getCtipoDocum());
                                cc.setNrofact(rd.getRecibosDetPK().getNdocum());
                                cc.setIpagado(rd.getItotal() * -1); //ideuda
                                cc.setIretencion(reciboSeleccionado.getIretencion());
                                cc.setIsaldo(rd.getIsaldo() - rd.getItotal());
                                if(tipoDocumento.getMdebCred() == 'C'){
                                    cc.setMindice(Short.parseShort("-1"));
                                }else{
                                    cc.setMindice(Short.parseShort("1"));
                                }
                                cc.setManulado(Short.parseShort("-1"));
                                cc.setFvenc(null);  //no se de donde se obtiene.
                                cc.setFfactur(rd.getFfactur());
                                cuentasCorrientesFacade.insertarCuentas(cc);
                            }catch(Exception e){
                                RequestContext.getCurrentInstance().update("exceptionDialog");
                                contenidoError = ExceptionHandlerView.getStackTrace(e);
                                tituloError = "Error en inserción de cuentas corrientes recibos detalles.";
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                return null;
                            }
                        }
                    }else if(rd.getRecibosDetPK().getCtipoDocum().equals("NCV")){
                        notasActualizadas = actualizarNotas(rd.getRecibosDetPK().getCtipoDocum(), rd.getRecibosDetPK().getNdocum(), rd.getTtotal());
                    } 
                }
                if(notasActualizadas){
                    try{
                        recibosFacade.actualizarEstadoRegistro(reciboSeleccionado.getRecibosPK().getNrecibo());
                    }catch(Exception e){
                        RequestContext.getCurrentInstance().update("exceptionDialog");
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error en anulación de recibos.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return null;
                    }
                    //operacion exitosa
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Recibo anulado."));
                    limpiarFormulario();
                    RequestContext.getCurrentInstance().execute("PF('dlgAnularReciboCompra').hide();");
                    return null;
                }
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Estado inválido del recibo."));
                RequestContext.getCurrentInstance().execute("PF('dlgAnularReciboCompra').hide();");
                return null;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al anular un recibo.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return null;
    }
    
    public String agregarRecibo(){
        try{
            for(ChequeDetalleDto cd: listaChequesDet){
                if(cd.getCheque().getFcheque().compareTo(cd.getCheque().getFemision()) < 0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "La fecha del cheque debe ser mayor o igual a la fecha de emisión."));
                    return null;
                }
            }
            if(nroReciboLbl == 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Nro de recibo inválido."));
                return null;
            }else{
                long cantRecibos = 0;
                try{
                    cantRecibos = recibosFacade.obtenerCantidadRecibos(nroReciboLbl);
                    if(cantRecibos > 0){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Ya existe este nro. de recibo."));
                        return null;
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la validación del nro. de recibo.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                }
            }
            if(clienteSeleccionadoLbl == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Cliente inválido."));
                return null;
            }else{
                if(clienteSeleccionadoLbl == 0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Cliente inválido."));
                    return null;
                }else{
                    if(fechaReciboLbl == null){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Fecha inválida de recibo."));
                        return null;
                    }else{
                        long total = 0;
                        for(DocumentoVentaDto dv: listaDocumentoVentaDto){
                            total += dv.getMontoAPagarDocumento();
                        }
                        if(total != totalReciboLbl){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Total monto a pagar no coincide con el total recibo."));
                            return null;
                        }
                        if((totalEfectivo + totalRetencion + totalCheques) != totalReciboLbl){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Total deudas no coincide con el total recibo."));
                            return null;
                        }
                        for(DocumentoVentaDto dv: listaDocumentoVentaDto){
                            if(dv.getNroDcumento() != 0){
                                if(dv.getTipoDocumento().getCtipoDocum().equals("FCR") || dv.getTipoDocumento().getCtipoDocum().equals("FCP")){
                                    try{
                                        String lFFacturaStr = dateToString(dv.getFechaDocumento());
                                        listadoFacturas = facturasFacade.listadoDeFacturas(dv.getTipoDocumento().getCtipoDocum(), lFFacturaStr, dv.getNroDcumento());
                                    }catch(Exception e){
                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en la lectura de facturas.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                }
                                if(dv.getTipoDocumento().getCtipoDocum().equals("NCV")){
                                    try{
                                        listadoNotasVentasDto = notasVentasFacade.listadoDeNotasDeVentasPorNroNota(dv.getNroRecibo());
                                    }catch(Exception e){
                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en la lectura de notas de créditos.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                    if(listadoNotasVentasDto == null){
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe nota de crédito."));
                                        return null;
                                    }else{
                                        if(listadoNotasVentasDto.isEmpty()){
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe nota de crédito."));
                                            return null;
                                        }else{
                                            for(NotaVentaDto nvdto: listadoNotasVentasDto){
                                                if(nvdto.getNotaVenta().getIsaldo() <= 0){
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Nota de crédito ya utilizada totalmente."));
                                                    return null;
                                                }
                                                if(nvdto.getNotaVenta().getCodCliente() != Integer.parseInt(String.valueOf(clienteSeleccionadoLbl))){
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "La nota de crédito no pertenece al cliente del recibo."));
                                                    return null;
                                                }
                                                long saldo = 0;
                                                if(nvdto.getNotaVenta().getNrofact() > 0){
                                                    try{
                                                        String lFFacturaStr = dateToString(nvdto.getNotaVenta().getFfactur());
                                                        saldo = facturasFacade.obtenerSaldoDeFacturas(lFFacturaStr, nvdto.getNotaVenta().getNotasVentasPK().getCtipoDocum(), nvdto.getNotaVenta().getNrofact());
                                                        if(saldo == 0){
                                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe factura "+nvdto.getNotaVenta().getNrofact()+" de la NCV."));
                                                            return null;
                                                        }
                                                        if(saldo > 0){
                                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "La factura "+nvdto.getNotaVenta().getNrofact()+" de la NCV está pendiente."));
                                                            return null;
                                                        }
                                                    }catch(Exception e){
                                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                                        tituloError = "Error en la lectura de saldos de facturas.";
                                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                                        return null;
                                                    }
                                                }
                                            }
                                            
                                        }
                                    }
                                }
                                //validar montos de cheques
                                long importe = 0;
                                for(ChequeDetalleDto cd: listaChequesDet){
                                    if(cd.getCheque().getChequesPK().getNroCheque() != null || !cd.getCheque().getChequesPK().getNroCheque().equals("")){
                                        try{
                                            importe = recibosChequesFacade.obtenerImportePagadoRecibosCheques(cd.getCheque().getChequesPK().getNroCheque(), cd.getCheque().getChequesPK().getCodBanco());
                                            long tTotal = cd.getCheque().getIcheque() - importe;
                                            if(tTotal < cd.getImportePagado()){
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "El nro. de cheque "+cd.getCheque().getChequesPK().getNroCheque()+" ya tiene utilizado "+importe+" en otras facturas."));
                                                return null;
                                            }
                                        }catch(Exception e){
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en control de totales de cheques.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                    }
                                }
                                //insertamos y/o actualizamos
                                try{
                                    String lFRecibo = dateToString(fechaReciboLbl);
                                    recibosFacade.insertarRecibo(   nroReciboLbl, 
                                                                    lFRecibo, 
                                                                    clienteSeleccionadoLbl, 
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
                                //detalle recibos
                                for(DocumentoVentaDto docventa: listaDocumentoVentaDto){
                                    if(docventa.getTipoDocumento().getCtipoDocum().equals("FCR") || docventa.getTipoDocumento().getCtipoDocum().equals("FCP")){
                                        String lFFactura = dateToString(docventa.getFechaDocumento());
                                        try{
                                            recibosDetFacade.insertarReciboDetalle( nroReciboLbl, 
                                                                                    docventa.getNroDcumento(), 
                                                                                    docventa.getTipoDocumento().getCtipoDocum(), 
                                                                                    docventa.getMontoAPagarDocumento(),
                                                                                    lFFactura, 
                                                                                    docventa.getInteresDocumento(), 
                                                                                    docventa.getMontoDocumento(), 
                                                                                    docventa.getSaldoDocumento());
                                        }catch(Exception e){
                                            RequestContext.getCurrentInstance().update("exceptionDialog");
                                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                                            tituloError = "Error en inserción de recibos detalles.";
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                            return null;
                                        }
                                        docventa.setTipoDocumento(tiposDocumentosFacade.find(docventa.getTipoDocumento().getCtipoDocum()));
                                        if(docventa.getTipoDocumento().getMafectaCtacte() == 'S'){
                                            Short lMIndice = null;
                                            if(docventa.getTipoDocumento().getMdebCred() == 'D'){
                                                lMIndice = Short.parseShort("1");
                                            }else{
                                                lMIndice = Short.parseShort("-1");
                                            }
                                            try{
                                                CuentasCorrientes cuentaCorriente = new CuentasCorrientes();
                                                cuentaCorriente.setCodEmpr(Short.parseShort("2"));
                                                cuentaCorriente.setCtipoDocum(docventa.getTipoDocumento().getCtipoDocum());
                                                cuentaCorriente.setFvenc(docventa.getFechaVencimiento());
                                                cuentaCorriente.setFmovim(fechaReciboLbl);
                                                cuentaCorriente.setNdocumCheq(String.valueOf(nroReciboLbl));
                                                cuentaCorriente.setIpagado(docventa.getMontoAPagarDocumento());
                                                cuentaCorriente.setIretencion(totalRetencion);
                                                cuentaCorriente.setIsaldo(docventa.getSaldoDocumento() - docventa.getMontoAPagarDocumento());
                                                cuentaCorriente.setMindice(lMIndice);
                                                cuentaCorriente.setManulado(Short.parseShort("1"));
                                                cuentaCorriente.setFfactur(docventa.getFechaDocumento());
                                                cuentaCorriente.setCodCliente(clientesFacade.find(docventa.getCodigoCliente()));
                                                cuentaCorriente.setFacCtipoDocum(docventa.getTipoDocumento().getCtipoDocum());
                                                cuentaCorriente.setNrofact(docventa.getNroDcumento());
                                                cuentasCorrientesFacade.insertarCuentas(cuentaCorriente);
                                            }catch(Exception e){
                                                RequestContext.getCurrentInstance().update("exceptionDialog");
                                                contenidoError = ExceptionHandlerView.getStackTrace(e);
                                                tituloError = "Error en inserción de cuentas corrientes recibos detalles.";
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                                return null;
                                            }
                                        }
                                    }
                                    if(docventa.getTipoDocumento().getCtipoDocum().equals("NCV")){
                                        //actualizar notas
                                        if(actualizarNotas(docventa.getTipoDocumento().getCtipoDocum(), docventa.getNroDcumento(), docventa.getMontoAPagarDocumento())){
                                            //cheques
                                            for(ChequeDetalleDto cd: listaChequesDet){
                                                //cheques
                                                try{
                                                    String lFCheque = dateToString(cd.getCheque().getFcheque());
                                                    String lFEmision = dateToString(cd.getCheque().getFemision());
                                                    chequesFacade.insertarCheque(cd.getCheque().getChequesPK().getCodBanco(), 
                                                                                 cd.getCheque().getChequesPK().getNroCheque(), 
                                                                                 cd.getCheque().getChequesPK().getXcuentaBco(), 
                                                                                 lFCheque, lFEmision, cd.getCheque().getIcheque(), 
                                                                                 clienteSeleccionadoLbl, cd.getCheque().getXtitular(), cd.getCheque().getMtipo());
                                                }catch(Exception e){
                                                    RequestContext.getCurrentInstance().update("exceptionDialog");
                                                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                                                    tituloError = "Error en inserción de nuevos cheques.";
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                                    return null;
                                                }
                                                TiposDocumentos tipoDocumento = tiposDocumentosFacade.find("CHQ");
                                                if(tipoDocumento.getMafectaCtacte() == 'S'){
                                                    //cuentas corrientes
                                                    try{
                                                        CuentasCorrientes cc = new CuentasCorrientes();
                                                        cc.setCodEmpr(Short.parseShort("2"));
                                                        cc.setCtipoDocum("CHQ");
                                                        cc.setCodCliente(clientesFacade.find(clienteSeleccionadoLbl));
                                                        cc.setFmovim(cd.getCheque().getFemision());
                                                        cc.setNdocumCheq(String.valueOf(cd.getCheque().getChequesPK().getNroCheque()));
                                                        cc.setIpagado(cd.getImportePagado());
                                                        cc.setMindice(Short.parseShort("1"));
                                                        cc.setFvenc(cd.getCheque().getFcheque());
                                                        cc.setCodBanco(bancosFacade.find(cd.getCheque().getChequesPK().getCodBanco()));
                                                        cuentasCorrientesFacade.insertarCuentas(cc);
                                                    }catch(Exception e){
                                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                                        tituloError = "Error en inserción de cuentas corrientes.";
                                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                                        return null;
                                                    }
                                                }
                                                //recibos cheques
                                                try{
                                                    recibosChequesFacade.insertarReciboCheque(  nroReciboLbl, 
                                                                                                cd.getCheque().getChequesPK().getCodBanco(), 
                                                                                                cd.getCheque().getChequesPK().getNroCheque(), 
                                                                                                cd.getImportePagado());
                                                }catch(Exception e){
                                                    RequestContext.getCurrentInstance().update("exceptionDialog");
                                                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                                                    tituloError = "Error en inserción de recibos cheques.";
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                                    return null;
                                                }
                                                //operacion exitosa
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Datos Grabados."));
                                                return null;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //operacion exitosa
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Datos Grabados."));
                        limpiarFormulario(); //volvemos a instanciar
                        RequestContext.getCurrentInstance().execute("PF('dlgNuevReciboCompra').hide();");
                        return null;
                    }
                }
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al agregar un recibo.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            return null;
        }
    }
    
    private boolean actualizarNotas(String lCTipoDocum, long lNroDocum, long lIDeuda){
        List<NotasVentas> listadoNotasVentas = new ArrayList<>();
        if(lCTipoDocum.equals("FCR") || lCTipoDocum.equals("FCP")){
            try{
                listadoNotasVentas = notasVentasFacade.listadoDeNotasDeVentasPorNroFactura(lNroDocum);
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en la lectura de notas de ventas.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return false;
            }
            if(!listadoNotasVentas.isEmpty() && lIDeuda > 0){
                long lIRestar = 0;
                for(NotasVentas nv: listadoNotasVentas){
                    if(nv.getIsaldo() > lIDeuda){
                        lIRestar = lIDeuda;
                        lIDeuda = 0;
                    }else{
                        lIRestar = nv.getIsaldo();
                        lIDeuda -= nv.getIsaldo();
                    }
                    try{
                        notasVentasFacade.disminuirSaldoNotasVentas(nv.getNotasVentasPK().getNroNota(), lIRestar);
                    }catch(Exception e){
                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                        tituloError = "Error en la actualización de saldos de notas.";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        return false;
                    }
                }
            }
        }else if(lCTipoDocum.equals("NCV")){
            try{
                long saldo = 0;
                if(lIDeuda > 0){
                    saldo = notasVentasFacade.obtenerSaldoNotasVentasMayorACero(lNroDocum);
                }else{
                    saldo = notasVentasFacade.obtenerSaldoNotasVentas(lNroDocum);
                }
                long lIRestar = 0;
                if(saldo >= lIDeuda ){
                    lIRestar = lIDeuda;
                }else{
                    lIRestar = saldo;
                }
                try{
                    if(lIDeuda > 0){
                        notasVentasFacade.disminuirSaldoNotasVentas(lNroDocum, lIRestar);
                    }else{
                        notasVentasFacade.aumentarSaldoNotasVentas(lNroDocum, lIRestar);
                    }
                }catch(Exception e){
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la actualización de saldos de notas.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return false;
                }
            }catch(Exception e){
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en la lectura de notas de ventas.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return false;
            }
            
        }
        return true;
    }
    
    private boolean validarNroDocumento() {
        if (tipoDocumentoSeleccionadoDet == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un tipo de documento."));
            return false;
        } else {
            if (tipoDocumentoSeleccionadoDet.getCtipoDocum() == null || tipoDocumentoSeleccionadoDet.getCtipoDocum().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un tipo de documento."));
                return false;
            } else {
                if (clienteSeleccionadoLbl == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar un cliente."));
                    return false;
                } else {
                    if (nroDocumentoDet == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar un nro. de documento"));
                        return false;
                    } else {
                        if (tipoDocumentoSeleccionadoDet.getCtipoDocum().equals("FCR") || tipoDocumentoSeleccionadoDet.getCtipoDocum().equals("FCP")) {
                            if (montoAPagarDet == 0) {    //monto a pagar en la grilla (ideuda).
                                try {
                                    if (fechaDocumentoDet == null) {
                                        listadoFacturas = facturasFacade.listadoDeFacturasPorTipoNroFactura(tipoDocumentoSeleccionadoDet.getCtipoDocum(), nroDocumentoDet);
                                    } else {
                                        String lFFacturaStr = dateToString(fechaDocumentoDet);
                                        listadoFacturas = facturasFacade.listadoDeFacturasActivas(tipoDocumentoSeleccionadoDet.getCtipoDocum(), lFFacturaStr, nroDocumentoDet);
                                    }
                                    if (listadoFacturas == null) {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "No existe factura crédito."));
                                        return false;
                                    } else {
                                        if (listadoFacturas.isEmpty()) {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "No existe factura crédito."));
                                            return false;
                                        } else {
                                            for (Facturas f : listadoFacturas) {
                                                if (f.getIsaldo() <= 0) {
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Factura sin saldo pendiente."));
                                                    return false;
                                                }
                                            }
                                            for (Facturas f : listadoFacturas) {
                                                if (f.getCodCliente().getCodCliente().compareTo(clienteSeleccionadoLbl) != 0) {
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "La factura no pertenece al cliente del recibo."));
                                                    return false;
                                                }
                                            }
                                            for (Facturas f : listadoFacturas) {
                                                fechaDocumentoDet = f.getFacturasPK().getFfactur();
                                                montoAPagarDet = f.getIsaldo();
                                                fechaVencimientoDocDet = f.getFvenc();
                                                saldoDocumentoDet = montoAPagarDet;
                                                montoFacturaDet = f.getTtotal();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    RequestContext.getCurrentInstance().update("exceptionDialog");
                                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                                    tituloError = "Error en la lectura de facturas.";
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                }
                            }
                            if(!listaDocumentoVentaDto.isEmpty()){
                                for (DocumentoVentaDto dv : listaDocumentoVentaDto) {
                                    if (dv.getNroDcumento() == nroDocumentoDet && dv.getTipoDocumento().getCtipoDocum().equals(tipoDocumentoSeleccionadoDet.getCtipoDocum())) {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Esta factura ya ha sido registrada."));
                                        return false;
                                    }
                                }
                            }
                        } else {
                            if (tipoDocumentoSeleccionadoDet.getCtipoDocum().equals("NCV")) {
                                if (montoAPagarDet == 0) {  //monto a pagar en la grilla (ideuda).
                                    try {
                                        if (fechaDocumentoDet == null) {
                                            listadoNotasVentasDto = notasVentasFacade.listadoDeNotasDeVentasConMaximaFechaPorNroNota(nroDocumentoDet);
                                        } else {
                                            String lFDocum = dateToString(fechaDocumentoDet);
                                            listadoNotasVentasDto = notasVentasFacade.listadoDeNotasDeVentasPorFechaNroNota(lFDocum, nroDocumentoDet);
                                        }
                                    } catch (Exception e) {
                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en la lectura de notas de créditos.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                        return false;
                                    }
                                    if (listadoNotasVentasDto == null) {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "No existe nota de crédito."));
                                        return false;
                                    } else {
                                        if (listadoNotasVentasDto.isEmpty()) {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "No existe nota de crédito."));
                                            return false;
                                        } else {
                                            for (NotaVentaDto nv : listadoNotasVentasDto) {
                                                if (nv.getNotaVenta().getIsaldo() <= 0) {
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Nota de crédito ya utilizada totalmente."));
                                                    return false;
                                                }
                                            }
                                            for (NotaVentaDto nv : listadoNotasVentasDto) {
                                                if (nv.getNotaVenta().getCodCliente() != Integer.parseInt(clienteSeleccionadoLbl.toString())) {
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "La nota de crédito no pertenece al cliente del recibo."));
                                                    return false;
                                                }
                                            }
                                            long saldo = 0;
                                            for (NotaVentaDto nv : listadoNotasVentasDto) {
                                                if (nv.getNotaVenta().getNrofact() > 0) {
                                                    try {
                                                        String lFFacturaStr = dateToString(nv.getNotaVenta().getNotasVentasPK().getFdocum());   //fecha del documento?
                                                        //String lFFacturaStr = dateToString(nv.getNotaVenta().getFfactur()); fecha de la factura?
                                                        saldo = facturasFacade.obtenerSaldoDeFacturas(lFFacturaStr, nv.getNotaVenta().getFacCtipoDocum(), nv.getNotaVenta().getNrofact());
                                                    } catch (Exception e) {
                                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                                        tituloError = "Error en la lectura de saldo de facturas.";
                                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                                        return false;
                                                    }
                                                    if (saldo == 0) {
                                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "No existe factura " + nv.getNotaVenta().getNrofact() + " de la NCV."));
                                                        return false;
                                                    }
                                                    if (nv.getNotaVenta().getIsaldo() > 0) {
                                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "La factura " + nv.getNotaVenta().getNrofact() + " de la NCV está pendiente."));
                                                        return false;
                                                    } else if (nv.getNotaVenta().getIsaldo() == 0) {
                                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "La factura " + nv.getNotaVenta().getNrofact() + " de la NCV está cancelada."));
                                                        return false;
                                                    } else if (nv.getNotaVenta().getIsaldo() < 0) {
                                                        iSaldoFact = nv.getNotaVenta().getIsaldo();
                                                        montoAPagarDet = nv.getNotaVenta().getIsaldo() * -1;   //isaldo(de dónde se obtiene?)
                                                        saldoDocumentoDet = nv.getNotaVenta().getIsaldo();
                                                        montoFacturaDet = nv.getNotaVenta().getTtotal();
                                                    }
                                                } else {
                                                    montoAPagarDet = nv.getNotaVenta().getIsaldo() * -1;    //isaldo(de dónde se obtiene?)
                                                    saldoDocumentoDet = nv.getNotaVenta().getIsaldo();
                                                    iSaldoFact = nv.getNotaVenta().getIsaldo();
                                                    montoFacturaDet = nv.getNotaVenta().getTtotal();
                                                }
                                            }
                                        }
                                    }
                                    if(!listaDocumentoVentaDto.isEmpty()){
                                        for (DocumentoVentaDto dv : listaDocumentoVentaDto) {
                                            if (dv.getNroDcumento() == nroDocumentoDet && dv.getTipoDocumento().getCtipoDocum().equals(tipoDocumentoSeleccionadoDet.getCtipoDocum())) {
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Esta nota de crédito ya ha sido registrada."));
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
        }
        return true;
    }
    
    private boolean validarMontoAPagar(DocumentoVentaDto documentoVentaDto, long montoAPagarNuevo){
        if((documentoVentaDto.getTipoDocumento().getCtipoDocum().equals("FCR") || documentoVentaDto.getTipoDocumento().getCtipoDocum().equals("FCP")) 
                && montoAPagarNuevo > documentoVentaDto.getSaldoDocumento() ){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "El monto a pagar debe ser menor o igual a "+documentoVentaDto.getSaldoDocumento()));
            return false;
        }
        
        if(documentoVentaDto.getTipoDocumento().getCtipoDocum().equals("NCV")){
            if(montoAPagarNuevo > documentoVentaDto.getMontoDocumento()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "El monto a pagar debe ser menor o igual a "+documentoVentaDto.getMontoDocumento()));
                return false;
            }
            if(montoAPagarNuevo > documentoVentaDto.getSaldoFactura()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "El monto a pagar debe ser menor o igual a saldo a favor en factura relacionada "+documentoVentaDto.getSaldoFactura()));
                return false;
            }
            if(montoAPagarNuevo > 0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "El valor de la nota de crédito debe ser negativo."));
                return false;
            }
        }
        return true;
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
     
    public static String convertidorFecha(Date fecha){
        return DateUtil.formaterDateToString(fecha);
    } 
    
     public void cargarDetalleCheques(){
        try{
            if(validarIngresoCheques()){
                ChequesPK chequePK = new ChequesPK();
                chequePK.setCodEmpr(Short.parseShort("2"));
                chequePK.setCodBanco(bancoSeleccionadoDet.getCodBanco());
                chequePK.setNroCheque(nroChequeDet);
                chequePK.setXcuentaBco(nroCtaBancaria);
                Cheques cheque = new Cheques();
                cheque.setChequesPK(chequePK);
                cheque.setBancos(bancosFacade.find(bancoSeleccionadoDet.getCodBanco()));
                cheque.setCodCliente(clientesFacade.find(clienteSeleccionadoLbl));
                cheque.setFemision(fechaEmisionDet);
                cheque.setFcheque(fechaChequeDet);
                cheque.setIcheque(importeChequeDet);
                cheque.setCodZona(codigoZonaSeleccionadoLbl);
                cheque.setXtitular(titularChequeDet);
                ChequeDetalleDto chequeDetalle = new ChequeDetalleDto();
                chequeDetalle.setCheque(cheque);
                chequeDetalle.setImportePagado(importeAPagarChequeDet);
                listaChequesDet.add(chequeDetalle);
                calcularTotalCheque();
                bancoSeleccionadoDet = null;
                nroChequeDet = null;
                nroCtaBancaria = null;
                fechaChequeDet = null;
                fechaEmisionDet = null;
                titularChequeDet = null;
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
     
     private void calcularTotalCheque(){
        totalCheques = 0;
        if(!listaChequesDet.isEmpty()){
            for(ChequeDetalleDto cddto: listaChequesDet){
                totalCheques += cddto.getCheque().getIcheque();
            }
        }
    }
    
    private boolean validarIngresoCheques() {
        if (clienteSeleccionadoLbl == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un cliente."));
            return false;
        } else {
            if (bancoSeleccionadoDet == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un banco."));
                return false;
            } else {
                if (bancoSeleccionadoDet.getCodBanco() == 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe seleccionar un banco."));
                    return false;
                } else {
                    if (nroChequeDet.equals("") || nroChequeDet == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar un nro. de cheque."));
                        return false;
                    } else {
                        //validar ingreso de nro. de cheque
                        try{
                            listadoCheques = chequesFacade.listadoChequesNoRechazadosPorNroBancoCheque(nroChequeDet, bancoSeleccionadoDet.getCodBanco());
                            for(Cheques c: listadoCheques){
                                importeChequeDet = c.getIcheque();
                                nroCtaBancaria = c.getChequesPK().getXcuentaBco();
                                fechaEmisionDet = c.getFemision();
                                fechaChequeDet = c.getFcheque();
                                importeChequeDet = c.getIcheque();
                                titularChequeDet = c.getXtitular();
                            }
                        }catch(Exception e){
                            RequestContext.getCurrentInstance().update("exceptionDialog");
                            contenidoError = ExceptionHandlerView.getStackTrace(e);
                            tituloError = "Error en la búsqueda de cheques.";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                        }
                        if (nroCtaBancaria.equals("") || nroCtaBancaria == null) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar un nro. de cuenta bancaria."));
                            return false;
                        } else {
                            if (fechaChequeDet == null) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar la fecha del cheque."));
                                return false;
                            } else {
                                if (fechaEmisionDet == null) {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar la fecha de emisión del cheque."));
                                    return false;
                                } else {
                                    if (importeChequeDet == 0) {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "Debe ingresar el importe del cheque."));
                                        return false;
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
    
    public void borrarFilaDocumento(DocumentoVentaDto documentoABorrar){
        try{
            Iterator itr = listaDocumentoVentaDto.iterator();
            while (itr.hasNext()) {
                DocumentoVentaDto documentoVentaDtoEnLista = (DocumentoVentaDto) itr.next();
                if( documentoVentaDtoEnLista.getTipoDocumento().getCtipoDocum().equals(documentoABorrar.getTipoDocumento().getCtipoDocum()) &&
                    documentoVentaDtoEnLista.getCodigoCliente().compareTo(documentoABorrar.getCodigoCliente()) == 0 && 
                    documentoVentaDtoEnLista.getNroRecibo() == documentoABorrar.getNroRecibo() &&
                    documentoVentaDtoEnLista.getNroDcumento() == documentoABorrar.getNroDcumento() ){
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
    
    public void borrarFilaCheque(ChequeDetalleDto chequeABorrar){
        try{
            Iterator itr = listaChequesDet.iterator();
            while (itr.hasNext()) {
                ChequeDetalleDto chequeEnLista = (ChequeDetalleDto) itr.next();
                if( chequeEnLista.getCheque().getChequesPK().getCodBanco() == chequeABorrar.getCheque().getChequesPK().getCodBanco() &&
                    chequeEnLista.getCheque().getChequesPK().getNroCheque().equals(chequeABorrar.getCheque().getChequesPK().getNroCheque()) &&
                    chequeEnLista.getCheque().getChequesPK().getCodEmpr() == chequeABorrar.getCheque().getChequesPK().getCodEmpr()){
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
    
    
    public void cargarDetalleMovimiento(){
        try{
            if(validarNroDocumento()){
                //cargar en la lista de documentos
                DocumentoVentaDto dvdto = new DocumentoVentaDto();
                dvdto.setTipoDocumento(tipoDocumentoSeleccionadoDet);
                if(codigoZonaSeleccionadoLbl != null){
                    ZonasPK zonaPK = new ZonasPK();
                    zonaPK.setCodEmpr(Short.parseShort("2"));
                    zonaPK.setCodZona(codigoZonaSeleccionadoLbl);
                    dvdto.setZona(zonasFacade.find(zonaPK));
                }else{
                    dvdto.setZona(null);
                }
                dvdto.setCodigoCliente(clienteSeleccionadoLbl);
                dvdto.setFechaDocumento(fechaDocumentoDet);
                dvdto.setFechaRecibo(fechaReciboLbl);
                dvdto.setMontoAPagarDocumento(montoAPagarDet);
                dvdto.setMontoDocumento(montoFacturaDet);
                dvdto.setSaldoDocumento(saldoDocumentoDet);
                dvdto.setNroDcumento(nroDocumentoDet);
                dvdto.setNroRecibo(nroReciboLbl);
                dvdto.setObservacion(observacionLbl);
                dvdto.setSaldoFactura(iSaldoFact);
                dvdto.setFechaVencimiento(fechaVencimientoDocDet);
                listaDocumentoVentaDto.add(dvdto);
                calcularTotalRecibo();
                //limpiamos las variables
                tipoDocumentoSeleccionadoDet = null;
                fechaDocumentoDet = null;
                nroDocumentoDet = 0;
            }
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al cargar un detalle de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    private void calcularTotalRecibo(){
        totalReciboLbl = 0;
        if(!listaDocumentoVentaDto.isEmpty()){
            for(DocumentoVentaDto dvdto: listaDocumentoVentaDto){
                totalReciboLbl += dvdto.getMontoDocumento();
            }
        }
    }
    
    public void verificarCliente(){
        if(clienteSeleccionadoLbl != null){
            if(clienteSeleccionadoLbl == 0){
                //mostrar busqueda de clientes
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').show();");
            }else{
                try{
                    Clientes clienteBuscado = clientesFacade.find(clienteSeleccionadoLbl);
                    if(clienteBuscado == null){
                        //mostrar busqueda de clientes
                        RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').show();");
                    }else{
                        this.nombreClienteLbl = clienteBuscado.getXnombre();
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la lectura de datos de clientes.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                }
            }
        }
    }
    
    public void listarRecibos(){
        try{
            listadoRecibos = recibosFacade.obtenerRecibos();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de recibos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
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
    
    public List<TiposDocumentos> listarTiposDocumentos(){
        listadoTiposDocumentos = new ArrayList<>();
        try{
            listadoTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoParaReciboVentaCliente();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de tipos de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoTiposDocumentos;
    }
    
    public void onCellEditDocumento(CellEditEvent event) {  
        Object oldValue = event.getOldValue();  
        Object newValue = event.getNewValue();  
        if(!oldValue.equals(newValue)){
            DataTable table = (DataTable) event.getSource();
            DocumentoVentaDto documentoDtoRow = (DocumentoVentaDto) table.getRowData();
            if(validarMontoAPagar(documentoVentaDtoSeleccionadoDet, documentoDtoRow.getMontoAPagarDocumento())){
                documentoVentaDtoSeleccionadoDet.setMontoAPagarDocumento(documentoDtoRow.getMontoAPagarDocumento());
                FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Monto a pagar actualizado.", ""));
            }
        }
    }
    
    public void onCellEditBanco(CellEditEvent event) {  
        Object oldValue = event.getOldValue();  
        Object newValue = event.getNewValue();  
        if(!oldValue.equals(newValue)){
            DataTable table = (DataTable) event.getSource();
            ChequeDetalleDto chequeSeleccionado = (ChequeDetalleDto) table.getRowData();
            chequeSeleccionadoDet.setImportePagado(chequeSeleccionado.getImportePagado());
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Importe a pagar actualizado.", ""));
        }
    }
    
    public void cerrarDialogoSinGuardar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarRecibos').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevReciboCompra').hide();");
    }
    
    public List<DocumentoVentaDto> getListaDocumentoVentaDto() {
        return listaDocumentoVentaDto;
    }

    public void setListaDocumentoVentaDto(List<DocumentoVentaDto> listaDocumentoVentaDto) {
        this.listaDocumentoVentaDto = listaDocumentoVentaDto;
    }

    public DocumentoVentaDto getDocumentoVentaDtoSeleccionadoDet() {
        return documentoVentaDtoSeleccionadoDet;
    }

    public void setDocumentoVentaDtoSeleccionadoDet(DocumentoVentaDto documentoVentaDtoSeleccionadoDet) {
        this.documentoVentaDtoSeleccionadoDet = documentoVentaDtoSeleccionadoDet;
    }

    public List<ChequeDetalleDto> getListaChequesDet() {
        return listaChequesDet;
    }

    public void setListaChequesDet(List<ChequeDetalleDto> listaChequesDet) {
        this.listaChequesDet = listaChequesDet;
    }

    public ChequeDetalleDto getChequeSeleccionadoDet() {
        return chequeSeleccionadoDet;
    }

    public void setChequeSeleccionadoDet(ChequeDetalleDto chequeSeleccionadoDet) {
        this.chequeSeleccionadoDet = chequeSeleccionadoDet;
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

    public long getSaldoDocumentoDet() {
        return saldoDocumentoDet;
    }

    public void setSaldoDocumentoDet(long saldoDocumentoDet) {
        this.saldoDocumentoDet = saldoDocumentoDet;
    }

    public long getInteresDocumentoDet() {
        return interesDocumentoDet;
    }

    public void setInteresDocumentoDet(long interesDocumentoDet) {
        this.interesDocumentoDet = interesDocumentoDet;
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

    public List<Recibos> getListadoRecibos() {
        return listadoRecibos;
    }

    public void setListadoRecibos(List<Recibos> listadoRecibos) {
        this.listadoRecibos = listadoRecibos;
    }

    public Character getTipoChequeDet() {
        return tipoChequeDet;
    }

    public void setTipoChequeDet(Character tipoChequeDet) {
        this.tipoChequeDet = tipoChequeDet;
    }

    public String getTitularChequeDet() {
        return titularChequeDet;
    }

    public void setTitularChequeDet(String titularChequeDet) {
        this.titularChequeDet = titularChequeDet;
    }

    public Date getFechaVencimientoDocDet() {
        return fechaVencimientoDocDet;
    }

    public void setFechaVencimientoDocDet(Date fechaVencimientoDocDet) {
        this.fechaVencimientoDocDet = fechaVencimientoDocDet;
    }

    public String getCodigoZonaSeleccionadoLbl() {
        return codigoZonaSeleccionadoLbl;
    }

    public void setCodigoZonaSeleccionadoLbl(String codigoZonaSeleccionadoLbl) {
        this.codigoZonaSeleccionadoLbl = codigoZonaSeleccionadoLbl;
    }

    public Zonas getZonaSeleccionadaLbl() {
        return zonaSeleccionadaLbl;
    }

    public void setZonaSeleccionadaLbl(Zonas zonaSeleccionadaLbl) {
        this.zonaSeleccionadaLbl = zonaSeleccionadaLbl;
    }

    public Integer getClienteSeleccionadoLbl() {
        return clienteSeleccionadoLbl;
    }

    public void setClienteSeleccionadoLbl(Integer clienteSeleccionadoLbl) {
        this.clienteSeleccionadoLbl = clienteSeleccionadoLbl;
    }

    public Bancos getBancoSeleccionadoDet() {
        return bancoSeleccionadoDet;
    }

    public void setBancoSeleccionadoDet(Bancos bancoSeleccionadoDet) {
        this.bancoSeleccionadoDet = bancoSeleccionadoDet;
    }

    public long getiSaldoFact() {
        return iSaldoFact;
    }

    public void setiSaldoFact(long iSaldoFact) {
        this.iSaldoFact = iSaldoFact;
    }

    public Recibos getReciboSeleccionado() {
        return reciboSeleccionado;
    }

    public void setReciboSeleccionado(Recibos reciboSeleccionado) {
        this.reciboSeleccionado = reciboSeleccionado;
    }

    public String getNombreClienteLbl() {
        return nombreClienteLbl;
    }

    public void setNombreClienteLbl(String nombreClienteLbl) {
        this.nombreClienteLbl = nombreClienteLbl;
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

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public List<TiposDocumentos> getListadoTiposDocumentos() {
        return listadoTiposDocumentos;
    }

    public void setListadoTiposDocumentos(List<TiposDocumentos> listadoTiposDocumentos) {
        this.listadoTiposDocumentos = listadoTiposDocumentos;
    }

}
