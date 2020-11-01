/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ConceptosDocumentosFacade;
import dao.CuentasCorrientesFacade;
import dao.FacturasDetFacade;
import dao.FacturasFacade;
import dao.MercaImpuestosFacade;
import dao.MercaderiasFacade;
import dao.MovimientosMercaFacade;
import dao.NotasVentasFacade;
import dao.NotasVentasDetFacade;
import dao.PersonalizedFacade;
import dao.PromocionesFacade;
import dao.RangosDocumentosFacade;
import dao.TiposDocumentosFacade;
import dao.TiposVentasFacade;
import entidad.Clientes;
import entidad.ConceptosDocumentos;
import entidad.ConceptosDocumentosPK;
import entidad.CuentasCorrientes;
import entidad.Facturas;
import entidad.FacturasDet;
import entidad.FacturasPK;
import entidad.Impuestos;
import entidad.Mercaderias;
import entidad.MovimientosMerca;
import entidad.NotasVentas;
import entidad.NotasVentasDet;
import entidad.NotasVentasDetPK;
import entidad.NotasVentasPK;
import entidad.Promociones;
import entidad.RangosDocumentos;
import entidad.TiposDocumentos;
import entidad.TiposVentas;
import entidad.TiposVentasPK;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import util.DateUtil;
import util.ExceptionHandlerView;

/**
 *
 * @author Bruno Brizuela
 */
@ManagedBean
@ViewScoped
@TransactionManagement(TransactionManagementType.BEAN)
public class NotasCreditoMercBean implements Serializable {
        
    private LazyDataModel<NotasVentas> modelNotas;
    
    //listas
    private List<TiposDocumentos> listadoTiposNotas;
    private List<TiposDocumentos> listadoTiposFacturas;
    private List<ConceptosDocumentos> listadoConceptosDocumentos;
    private List<NotasVentas> listadoNotasVentas; 
    private List<Mercaderias> listaMercaderias;    
    private List<TiposVentas> listaTiposVentas;    
    
    private Short puntoEstabNota;
    private Short puntoExpedNota;
    private long nroNota;
    private Short puntoEstabFact;
    private Short puntoExpedFact;
    private long nroFact;
    private Date ffactura;
    private NotasVentas notaVenta;
    private NotasVentas notaVentaSeleccionada;
    private NotasVentasDet notaDet;
    private Double notaDetDesc;
    private Facturas factura;    
    private TiposDocumentos tipoDocumentoFactura;
    
    private String contenidoError;
    private String tituloError;
    
    private long totalExentas;
    private long totalGravadas;
    private long totalImpuestos;
    private long totalNota;
    private long totalGrabada5;
    private long totalImpuestosIva5;
    private long totalGrabada10;
    private long totalImpuestosIva10;
    
    private String titulo;
    
    private boolean agregar;
    private boolean modificar;
    private boolean anular;
    private boolean eliminar;    
    private boolean habilitaBotonAnular;
    private boolean habilitaBotonEliminar;
    private boolean habilitaBotonBusquedaDetalleFacturas;
    private boolean habilitaPromo;
    
    @EJB
    private NotasVentasFacade notasVentasFacade;
    
    @EJB
    private NotasVentasDetFacade notasVentasDetFacade;
    
    @EJB
    private FacturasFacade facturasFacade;
    
    @EJB
    private FacturasDetFacade facturasDetFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private ConceptosDocumentosFacade conceptosDocumentosFacade;
    
    @EJB
    private MercaderiasFacade mercaderiasFacade;
    
    @EJB
    private RangosDocumentosFacade rangosDocumentosFacade;
        
    @EJB
    private PromocionesFacade promocionesFacade;
    
    @EJB
    private MovimientosMercaFacade movimientosMercaFacade;
    
    @EJB
    private CuentasCorrientesFacade cuentasCorrientesFacade;
    
    @EJB
    private MercaImpuestosFacade mercaImpuestosFacade;
    
    @EJB
    private PersonalizedFacade personalizedFacade;
    
    @EJB
    private TiposVentasFacade tipoVentasFacade;
    
    @Resource
    private UserTransaction userTransaction;
    
    @PostConstruct
    public void init() {
        listadoTiposNotas = tiposDocumentosFacade.listarTipoDocumentoPorTipo("'NCV'");
        limpiarFormulario();        
        listadoTiposFacturas = tiposDocumentosFacade.listarTipoDocumentoPorTipo("'FCR','CPV','FCO'");
        listadoConceptosDocumentos = conceptosDocumentosFacade.obtenerConceptosPorTipoYConcepto("'NCV'","'DES','DPC','DPR'");
        listaTiposVentas = tipoVentasFacade.listarTiposVentasOrdenadoXDesc();
        listarNotasVentas();
    }
    
    public void limpiarFormulario(){
        NotasVentasPK notaVentaPK = new NotasVentasPK();
        notaVentaPK.setCtipoDocum(listadoTiposNotas.get(0).getCtipoDocum());
        notaVentaPK.setDescripcionTipoDocumento(listadoTiposNotas.get(0).getXdesc());
        notaVenta = new NotasVentas();
        notaVenta.setNotasVentasPK(notaVentaPK);
        notaVenta.getNotasVentasPK().setFdocum(new Date());
        notaVenta.setNotasVentasDetCollection(new ArrayList<>());
        notaVenta.setPorcentajeDesc(new Double(0.0));
        ConceptosDocumentosPK conceptosPK = new ConceptosDocumentosPK();
        ConceptosDocumentos conceptos = new ConceptosDocumentos();
        conceptos.setConceptosDocumentosPK(conceptosPK);
        notaVenta.setConceptosDocumentos(conceptos);
        notaVenta.setXobs("");
        notaVenta.setMestado('A');
        FacturasPK facturaPK = new FacturasPK();
        factura = new Facturas();
        factura.setFacturasPK(facturaPK);
        factura.setFacturasSerCollection(new ArrayList<>());
        ffactura = null;
        listaMercaderias = new ArrayList();
        titulo = "Ver nota";
        notaDet = new NotasVentasDet();
        notaDet.setPdesc(new BigDecimal("0"));
        notaDetDesc=0.0;
        totalImpuestos = 0; totalNota = 0; 
        puntoEstabNota = 1; puntoExpedNota = 1; nroNota = 0;
        puntoEstabFact = 1; puntoExpedFact = 1; nroFact = 0;totalExentas = 0; totalGravadas = 0;
        habilitaBotonBusquedaDetalleFacturas = true;
        habilitaBotonAnular = true; habilitaBotonEliminar = true; habilitaPromo = true;
        anular = false; eliminar = false; modificar = false; agregar = false;
        listadoNotasVentas = new ArrayList<>();
        calcularTotales();        
    }
    
    public void listarNotasVentas(){
        modelNotas = new LazyDataModel<NotasVentas>() {
            private static final long serialVersionUID = 1L;
            @Override
            public List<NotasVentas> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                int count = 0;
                if (filters.size() == 0) {
                    listadoNotasVentas = notasVentasFacade.buscarNotaVentaPorConceptoYRango(new int[]{first, pageSize}, "NCV", "'DES','DPC','DPR'");
                    count = listadoNotasVentas.size();
                } else {
                    if (filters.size() < 7) {
                        String filtroFDocum = (String) filters.get("notasVentasPK.fdocum") == null ? "" : (String) filters.get("notasVentasPK.fdocum");
                        String filtroNroNota = (String) filters.get("notasVentasPK.nroNota") == null ? "0" : (String) filters.get("notasVentasPK.nroNota");
                        String filtroCodigoCliente = (String) filters.get("codCliente") == null ? "0" : (String) filters.get("codCliente");
                        String filtroNombreCliente = (String) filters.get("nombreCliente") == null ? "" : (String) filters.get("nombreCliente");
                        String filtroTotal = (String) filters.get("ttotal") == null ? "0" : (String) filters.get("ttotal");
                        if (!filtroFDocum.equals("")) {
                            if (filtroFDocum.length() == 10) {
                                listadoNotasVentas = notasVentasFacade.buscarNotaVentaPorConceptoYRango(filtroFDocum, Integer.parseInt(filtroCodigoCliente), filtroNombreCliente, Long.parseLong(filtroTotal), Long.parseLong(filtroNroNota), new int[]{first, pageSize}, "NCV", "'DES','DPC','DPR'");
                                count = listadoNotasVentas.size();
                            }
                        } else {
                            listadoNotasVentas = notasVentasFacade.buscarNotaVentaPorConceptoYRango(filtroFDocum, Integer.parseInt(filtroCodigoCliente), filtroNombreCliente, Long.parseLong(filtroTotal), Long.parseLong(filtroNroNota), new int[]{first, pageSize}, "NCV", "'DES','DPC','DPR'");
                            count = listadoNotasVentas.size();
                        }
                    }
                }
                modelNotas.setRowCount(count);
                return listadoNotasVentas;
            }
            
            @Override
            public NotasVentas getRowData(String rowKey) {
                String tempIndex = rowKey;
                if (tempIndex != null) {
                    for (NotasVentas nv : listadoNotasVentas) {
                        String fechaDocum = DateUtil.dateToString(nv.getNotasVentasPK().getFdocum());
                        if (String.valueOf(nv.getNotasVentasPK().getNroNota()).concat(" ").concat(nv.getNotasVentasPK().getCtipoDocum()).concat(" ").concat(fechaDocum).equals(rowKey)) {
                            return nv;
                        }
                    }
                }

                return null;
            }

            @Override
            public Object getRowKey(NotasVentas nota) {
                NotasVentasPK pk = nota != null ? nota.getNotasVentasPK() : null;
                return pk != null ? pk.getNroNota() + " " + pk.getCtipoDocum() + " " + DateUtil.dateToString(pk.getFdocum()) : null;
            }
            
        };
    }
    
    public void nuevaNotaVenta(){
        limpiarFormulario();
        this.titulo = "Agregar nota";
        agregar = true;
        modificar = true;
        cambiaNroNota();
        notaDet = new NotasVentasDet();
    }
    
    public void anular(String proceso){
        limpiarFormulario();
        buscarNota(this.notaVentaSeleccionada);
        if ("N".equals(proceso)) {
            this.titulo = "Anular Nota";
            anular = true;
        }
        if ("E".equals(proceso)) {
            this.titulo = "Eliminar Nota";
            eliminar = true;
        }
        
    }    
        
    public void agregarDetalle(){
        try{            
            this.notaDet.setMercaderia(mercaderiasFacade.getMercaderiaFromList(
                        this.notaDet.getMercaderia(), 
                        this.listaMercaderias));
            
            NotasVentasDetPK nvdetpk = new NotasVentasDetPK();
            nvdetpk.setCodEmpr(this.notaDet.getMercaderia().getMercaderiasPK().getCodEmpr());
            nvdetpk.setCodMerca(this.notaDet.getMercaderia().getMercaderiasPK().getCodMerca());
            nvdetpk.setCtipoDocum(this.notaVenta.getNotasVentasPK().getCtipoDocum());
            nvdetpk.setFdocum(this.notaVenta.getNotasVentasPK().getFdocum());
            nvdetpk.setNroNota(obtenerNroNotaCompleto());
            this.notaDet.setNotasVentasDetPK(nvdetpk);
            
            if (this.notaVenta.getNotasVentasDetCollection()==null)
                this.notaVenta.setNotasVentasDetCollection(new ArrayList<>());
            
            if (this.notaVenta.getNotasVentasDetCollection().contains(this.notaDet)){
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "AtenciÃ³n", 
                            "Mercaderia ya existe en la Nota "));
                return;
            }
            
            this.notaDet.setNotasVentas(this.notaVenta);            
            this.notaVenta.getNotasVentasDetCollection().add(this.notaDet);
            for (FacturasDet fdet : this.factura.getFacturasDetCollection()){
                if (this.notaDet.getMercaderia().equals(fdet.getMercaderias())){
                    
                    this.notaDet.setIprecioCaja(new BigDecimal(fdet.getIprecioCaja() - (fdet.getIprecioCaja() * (fdet.getPdesc().divide(new BigDecimal(100)).longValue()))));
                    this.notaDet.setIprecioUnid(new BigDecimal(fdet.getIprecioUnidad()- (fdet.getIprecioUnidad() * (fdet.getPdesc().divide(new BigDecimal(100)).longValue()))));
                    this.notaDet.setCantCajas((int) fdet.getCantCajas() + (int) fdet.getCajasBonif());
                    this.notaDet.setCantUnid((int) fdet.getCantUnid() + (int) fdet.getUnidBonif());
                    
                    this.notaDet.setExentaCredito(this.notaDet.getIexentas());
                    this.notaDet.setGravadaCredito(this.notaDet.getIgravadas());
                    this.notaDet.setImpuestoCredito(this.notaDet.getImpuestos().longValue());
                    
                    this.notaDet.setIexentas( (fdet.getIexentas()>0) ? fdet.getIexentas()-fdet.getInotas() : fdet.getIexentas());
                    this.notaDet.setIgravadas((fdet.getIgravadas()>0) ? fdet.getIgravadas() + (fdet.getImpuestos()!=null ? fdet.getImpuestos().longValue() : 0) - fdet.getInotas() : fdet.getIgravadas());
                    this.notaDet.setImpuestos(fdet.getImpuestos());
                    this.notaDet.setPdesc(new BigDecimal(this.notaDetDesc.toString()));
                }
            }
            
            calcularTotales();
            
            this.notaDet = new NotasVentasDet();
            this.notaDet.setPdesc(BigDecimal.ZERO);
            this.notaDetDesc = 0.0;
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al agregar un servicio en la grilla.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }        
    }
    
    public void borrarNotaVentaDet(NotasVentasDet notaABorrar) {
        try {
            this.notaVenta.getNotasVentasDetCollection().remove(notaABorrar);            
            calcularTotales();            
        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al borrar un detalle.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    public void eliminarTodos(){
        try{
            this.notaVenta.setNotasVentasDetCollection(new ArrayList());            
            calcularTotales();            
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al agregar un servicio en la grilla.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }        
    }
    
    private void calcularTotales(){
        totalExentas = 0; totalGravadas = 0; totalImpuestos = 0;
        totalGrabada5 = 0; totalGrabada10 = 0; totalImpuestosIva5 = 0;
        totalImpuestosIva10 = 0; totalNota = 0;
        for(NotasVentasDet nvs: notaVenta.getNotasVentasDetCollection()){
            System.out.println("merc " + nvs.getMercaderia().getXdesc() + 
                    " importe: " + nvs.getIgravadas() + " grava credi: " + nvs.getGravadaCredito() );
            //System.out.println("merc " + nvs.getPdesc());
            //System.out.println("merc " + nvs.getExentaCredito());
            //System.out.println("merc " + nvs.getGravadaCredito());            
            totalExentas += nvs.getExentaCredito();
            totalGravadas += nvs.getGravadaCredito();
            if(nvs.getImpuestos()!=null) totalImpuestos += Math.abs(nvs.getImpuestoCredito());
            
            if(nvs.getPimpues().intValue()==5){
                this.totalGrabada5 += nvs.getGravadaCredito();
                if(nvs.getImpuestos()!=null)
                    this.totalImpuestosIva5 += Math.abs(nvs.getImpuestoCredito());
            }
            if(nvs.getPimpues().intValue()==10){
                this.totalGrabada10 += nvs.getGravadaCredito();
                if(nvs.getImpuestos()!=null)
                    this.totalImpuestosIva10 += Math.abs(nvs.getImpuestoCredito());
            }                
        }
        if (tipoDocumentoFactura!=null) {
            if ('S' == tipoDocumentoFactura.getMincluIva() ){
                totalNota = totalExentas + totalGravadas ;
            } else {
                totalNota = totalExentas + totalGravadas + totalImpuestos;
            }
        } 
    }
    
    public String guardarNotaVenta() throws IllegalStateException, SystemException {
        try{
            
            if (this.notaVenta == null || this.nroNota==0  | obtenerNroNotaCompleto() == 0){
                FacesContext.getCurrentInstance().addMessage(null, 
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                                    "Nro.de Documento Invalido."));
                return null;
            }
            /*Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, 0);
            Date fechaMaxima = c.getTime();
            c.add(Calendar.DAY_OF_MONTH, -5);
            Date fechaMinima = c.getTime();
            
            if(this.notaVenta.getFfactur().after(fechaMaxima) | 
                    this.notaVenta.getFfactur().before(fechaMinima)){
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "AtenciÃ³n", "Ingrese una fecha entre " 
                            + DateUtil.formaterDateToString(fechaMinima) 
                            + " y el " + DateUtil.formaterDateToString(fechaMaxima) ));
                return null;
            }
                */       
            if (!validarTimbradoVencido(this.notaVenta.getNotasVentasPK().getCtipoDocum(), 
                    obtenerNroNotaCompleto(), this.notaVenta.getNotasVentasPK().getFdocum(), 
                    'M')){
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                            "Timbrado del documento está vencido/no existe."));
                return null;
            }
                        
            if ( !new Short("1").equals(this.puntoExpedNota ) ){
                if (!this.puntoEstabNota.equals(this.factura.getDepositos().getNpuntoEst())) {
                    FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                            "El Establecimiento debe ser " + this.factura.getDepositos().getNpuntoEst().toString()));
                    return null;
                }
                if (!this.puntoExpedNota.equals(this.factura.getDepositos().getNpuntoExp())) {
                    FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                            "El Punto de Expedicion debe ser " + this.factura.getDepositos().getNpuntoExp().toString()));
                    return null;
                }
            }
            
            if (this.factura.getIsaldo() <= 0 ){
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                            "Factura Cancelada."));
                return null;
            }
            
            if (this.factura.getIsaldo() < this.totalNota ){
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                            "Saldo de la Factura es inferior a la Nota Credito."));
                return null;
            }
            
            if (this.notaVenta.getNotasVentasDetCollection() == null 
                    | this.notaVenta.getNotasVentasDetCollection().isEmpty() ){
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                            "Nota de Credito sin Detalles."));
                return null;
            }
            
            boolean existeMerca = false;
            long lnotasunid = 0;
            long lfactunid = 0;
            for(NotasVentasDet ndet : this.notaVenta.getNotasVentasDetCollection()){
                existeMerca = false;
                lnotasunid = ndet.getMercaderia().getNrelacion().multiply(new BigDecimal(ndet.getCantCajas())).longValue() + ndet.getCantUnid();
                for(FacturasDet fdet : this.factura.getFacturasDetCollection()){
                   if (ndet.getMercaderia().equals(fdet.getMercaderias())){
                       existeMerca = true;
                       lfactunid = fdet.getMercaderias().getNrelacion().multiply(new BigDecimal(fdet.getCantCajas()+fdet.getCajasBonif())).longValue() + fdet.getCantUnid() + fdet.getUnidBonif() ;
                       break;
                   }
                }
                if (!existeMerca){
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                                "No existe Mercaderia: " + ndet.getMercaderia().getXdesc()));
                    return null;
                }
                if (lnotasunid>lfactunid){
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                                "La mercaderia " + ndet.getMercaderia().getXdesc() + " excede la cantidad de factura Original"));
                    return null;
                }
            }            
            List<NotasVentas> listadoNotas = notasVentasFacade.obtenerNotasVentasPorNroYTpDoc(
                        obtenerNroNotaCompleto(), 
                        this.notaVenta.getNotasVentasPK().getCtipoDocum(), 
                        DateUtil.formaterDateToString(this.notaVenta.getFfactur(), "yyyyMMdd"));
            System.out.println("validacion nota nueva " + listadoNotas);
            if (agregar) {
                if(!listadoNotas.isEmpty()){
                    FacesContext.getCurrentInstance()
                            .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                    "AtenciÃ³n", "Ya existe este nÃºmero de nota de crÃ©dito."));
                    return null;
                }
            } else { 
                if(listadoNotas==null | listadoNotas.isEmpty()){
                    FacesContext.getCurrentInstance()
                            .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                    "AtenciÃ³n", "No existe Nota Debito/Credito en la Base de Datos."));
                    return null;
                }
            }
            
            List<Promociones> listPromo = null;
            if ("DPR".equals(this.notaVenta.getConceptosDocumentos().getConceptosDocumentosPK().getCconc())){
                if (this.notaVenta.getNroPromo()==null){
                    FacesContext.getCurrentInstance()
                            .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                    "AtenciÃ³n", "Ingrese el Nro. de Promocion Correspondiente."));
                    return null;
                }
                
                listPromo = promocionesFacade.findByNroPromoYFechaFacturaDos(
                        this.notaVenta.getNroPromo(), 
                        DateUtil.formaterDateToString(this.notaVenta.getFfactur(), "yyyyMMdd"));

                if (listPromo==null | listPromo.isEmpty()){
                    FacesContext.getCurrentInstance()
                                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                        "AtenciÃ³n", "No existe promocion vigente."));
                        return null;
                }
                /* FALTA CONTROL PROMO ZONA*/
            }
            // fin validaciones 
            userTransaction.begin();
            
            this.notaVenta.getNotasVentasPK().setNroNota(this.obtenerNroNotaCompleto());
            this.notaVenta.setXnroNota(this.obtenerNroNotaCompletoConFormato());
            this.notaVenta.setEmpleados(this.factura.getEmpleados());
            this.notaVenta.setCodDepo(this.factura.getDepositos().getDepositosPK().getCodDepo());
            this.notaVenta.setTexentas(this.totalExentas);
            this.notaVenta.setTgravadas(this.totalGravadas);
            this.notaVenta.setTimpuestos(this.totalImpuestos*-1);
            this.notaVenta.setTtotal(this.totalNota);
            this.notaVenta.setIsaldo(0);
            // insertamos la nota
            //try{
                notasVentasFacade.insertarNotaVenta(notaVenta);
            /*}catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al insertar nota venta.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return null;
            }*/
            
            /*if (1==1) {
                throw new Exception("Exception message");
            }*/
            
            int lCantCajas = 0;
            int lCantUnid = 0;
            long lExentas = 0;            
            long lGravadas = 0; 
            long ltotal = 0;
            long lImpuestos = 0;
            long lNPeso = 0;
            // insertamos detalles
            TiposDocumentos td = getTipoDocFromList(this.notaVenta.getNotasVentasPK().getCtipoDocum());
            Short lMIndice = Short.parseShort("1");
            for(NotasVentasDet det: notaVenta.getNotasVentasDetCollection()){
                System.out.println("det " + det.getMercaderia().getXdesc());
                System.out.println("det " + det.getImpuestos());
                /*FALTA CONTROL PROMO CURSOR PROMO*/
                if (det.getImpuestos().doubleValue()>0) det.setImpuestos(det.getImpuestos().multiply(new BigDecimal("-1")));
                
                det.setIprecioCaja(det.getIprecioCaja().subtract(det.getIprecioCaja().multiply(det.getPdesc().divide(new BigDecimal("100")))));
                det.setIprecioUnid(det.getIprecioUnid().subtract(det.getIprecioUnid().multiply(det.getPdesc().divide(new BigDecimal("100")))));
                
                //try{
                    det.setIexentas(det.getExentaCredito());
                    det.setIgravadas(det.getGravadaCredito());
                    det.setImpuestos(new BigDecimal(det.getImpuestoCredito()));
                    notasVentasDetFacade.insertarNotaVentaDet(det);
                /*}catch(Exception e){
                   RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al insertar nota venta servicio.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return null;
                }*/
                ConceptosDocumentos cdoc = getTipoDocFromList(notaVenta.getConceptosDocumentos());
                MovimientosMerca mov = new MovimientosMerca();
                mov.setNroMovim(1L);
                mov.setCodEmpr(Short.parseShort("2"));
                mov.setCodMerca(det.getMercaderia().getMercaderiasPK().getCodMerca());                    
                mov.setCodCliente(factura.getCodCliente().getCodCliente());
                mov.setCodDepo(factura.getDepositos().getDepositosPK().getCodDepo());
                mov.setCodZona(factura.getZonas().getZonasPK().getCodZona());
                mov.setCodEntregador(factura.getEmpleados().getEmpleadosPK().getCodEmpleado());
                mov.setCodVendedor(factura.getEmpleados1().getEmpleadosPK().getCodEmpleado());
                mov.setCodRuta(factura.getCodRuta());
                mov.setFalta(new Date());                    
                mov.setFmovim(new Date() /* det.getNotasVentasDetPK().getFdocum()*/ );
                mov.setCtipoDocum(det.getNotasVentasDetPK().getCtipoDocum());
                mov.setNdocum(det.getNotasVentasDetPK().getNroNota());
                mov.setNrofact(factura.getFacturasPK().getNrofact());
                mov.setFacCtipoDocum(factura.getFacturasPK().getCtipoDocum());
                mov.setPrecioCaja(det.getIprecioUnid().longValue());
                mov.setPrecioUnidad(det.getIprecioUnid().longValue());
                mov.setManulado(Short.parseShort("-1"));                    
                lCantCajas = det.getCantCajas();
                lCantUnid = det.getCantUnid(); 
                if(cdoc.getMafectaStock() == 'N'){
                    lCantCajas = 0;
                    lCantUnid = 0;
                }
                lNPeso = new BigDecimal(lCantCajas).multiply(det.getMercaderia().getNpesoCaja()).abs().longValue()
                        + new BigDecimal(lCantUnid).multiply(det.getMercaderia().getNpesoUnidad()).longValue();
                mov.setCantCajas((long)lCantCajas);
                mov.setCantUnid((long)lCantUnid);                
                lExentas = det.getExentaCredito();
                lImpuestos = Math.abs(det.getImpuestoCredito());
                lGravadas = det.getGravadaCredito() - lImpuestos;
                
                if(td.getMdebCred() == 'C'){
                    lExentas = lExentas*-1;
                    lGravadas = lGravadas*-1;
                }  
                mov.setIgravadas(lGravadas);
                mov.setIexentas(lExentas);
                mov.setImpuestos(lImpuestos*-1);                    
                mov.setNpeso(new BigDecimal(lNPeso));
                String usuario = "user";//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                mov.setCusuario(usuario);
                //personalizedFacade.ejecutarSentenciaSQL("set dateformat ymd");
                movimientosMercaFacade.insertarMovimientosMerca(mov);
                
                if(td.getMafectaCtacte() == 'S'){
                    ltotal = (lExentas + lImpuestos + lGravadas)*-1;
                    facturasDetFacade.disminuirImporteNotas(notaVenta.getNrofact(),
                        notaVenta.getFacCtipoDocum(),
                        DateUtil.formaterDateToString(factura.getFacturasPK().getFfactur(), "yyyyMMdd"),
                        det.getMercaderia().getMercaderiasPK().getCodMerca(),
                        ltotal
                        );
                }
            }
            long lTExentas = totalExentas > 0 ? totalExentas * -1 : totalExentas;
            long lTGravadas = totalGravadas - Math.abs(totalImpuestos);
            long lTImpuestos = totalImpuestos > 0 ? totalImpuestos * -1 : totalImpuestos;
            if(td.getMafectaCtacte() == 'S'){
                if(td.getMdebCred() == 'C'){
                    lMIndice = Short.parseShort("-1");
                }
                //try{
                    CuentasCorrientes cc = new CuentasCorrientes();
                    cc.setCodEmpr(Short.parseShort("2"));
                    cc.setCtipoDocum(notaVenta.getNotasVentasPK().getCtipoDocum());
                    cc.setFmovim(new Date() /* notaVenta.getNotasVentasPK().getFdocum() */ );
                    cc.setNdocumCheq(String.valueOf( notaVenta.getNotasVentasPK().getNroNota()));
                    cc.setNrofact(notaVenta.getNrofact());
                    cc.setCtipoDocum(notaVenta.getFacCtipoDocum());
                    cc.setFfactur(notaVenta.getFfactur());
                    cc.setIsaldo(factura.getIsaldo());
                    cc.setFvenc(factura.getFvenc());                    
                    cc.setCodCliente(new Clientes(notaVenta.getCodCliente()));
                    cc.setIpagado(0);
                    cc.setIretencion(0);
                    cc.setManulado(Short.parseShort("-1"));
                    cc.setTexentas(lTExentas);
                    cc.setTgravadas(lTGravadas);
                    cc.setTimpuestos(lTImpuestos);
                    cc.setMindice(lMIndice);                    
                    //personalizedFacade.ejecutarSentenciaSQL("set dateformat ymd");
                    cuentasCorrientesFacade.insertarCuentas(cc);
                    
                /*}catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al insertar cuenta corriente.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return null;
                }*/
            } else {
                //try{
                    facturasFacade.aumentarTotalNotas(this.notaVenta.getTtotal(), 
                            this.factura.getFacturasPK().getNrofact(), 
                            DateUtil.formaterDateToString(this.factura.getFacturasPK().getFfactur(), "yyyyMMdd"),
                            this.factura.getFacturasPK().getCtipoDocum());
                /*}catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al actualizar facturas.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return null;
                }*/
            }
            userTransaction.commit();
            //** si no hubo errores en las inserciones anteriores, hacer COMMIT y avisar al usuario
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos grabados.", ""));
            limpiarFormulario(); //volvemos a instanciar
            listarNotasVentas();
            RequestContext.getCurrentInstance().execute("PF('dlgNuevaNota').hide();");
            return null;
        }catch(Exception e){
            userTransaction.rollback();
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al guardar nota venta.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        } 
        return null;
    }
    
    public void anularNota() throws IllegalStateException, SystemException {
        try{
            
            if(notaVentaSeleccionada.getMestado() != 'A'){
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                "AtenciÃ³n", "Estado invÃ¡lido de la factura."));
                return;
            }
            //actualizar notas ventas
            userTransaction.begin();
            //try{
                notasVentasFacade.anularNotaVenta(notaVentaSeleccionada.getNotasVentasPK().getCtipoDocum(), 
                        notaVentaSeleccionada.getNotasVentasPK().getNroNota(),
                        DateUtil.formaterDateToString(notaVentaSeleccionada.getNotasVentasPK().getFdocum(), "yyyyMMdd"));
            /*}catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al actualizar nota venta.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return;
            }*/
            int lCantCajas = 0;
            int lCantUnid = 0;
            long lExentas = 0;            
            long lGravadas = 0; 
            long ltotal = 0;
            long lImpuestos = 0;
            long lNPeso = 0;
            //insertar en movimientos merca            
            for(NotasVentasDet d: notaVenta.getNotasVentasDetCollection()){
                //try{
                    MovimientosMerca mov = new MovimientosMerca();
                    mov.setNroMovim(1L);
                    mov.setCodEmpr(Short.parseShort("2"));
                    mov.setCodMerca(d.getMercaderia().getMercaderiasPK().getCodMerca());                    
                    mov.setCodCliente(factura.getCodCliente().getCodCliente());
                    mov.setCodDepo(factura.getDepositos().getDepositosPK().getCodDepo());
                    mov.setCodZona(factura.getZonas().getZonasPK().getCodZona());
                    mov.setCodEntregador(factura.getEmpleados().getEmpleadosPK().getCodEmpleado());
                    mov.setCodVendedor(factura.getEmpleados1().getEmpleadosPK().getCodEmpleado());
                    mov.setCodRuta(factura.getCodRuta());
                    mov.setFalta(new Date());                    
                    mov.setFmovim(new Date() /*d.getNotasVentasDetPK().getFdocum()*/);
                    mov.setCtipoDocum(d.getNotasVentasDetPK().getCtipoDocum());
                    mov.setNdocum(d.getNotasVentasDetPK().getNroNota());
                    mov.setNrofact(factura.getFacturasPK().getNrofact());
                    mov.setFacCtipoDocum(factura.getFacturasPK().getCtipoDocum());
                    mov.setPrecioCaja(d.getIprecioUnid().longValue());
                    mov.setPrecioUnidad(d.getIprecioUnid().longValue());
                    mov.setManulado(Short.parseShort("-1"));                    
                    lCantCajas = d.getCantCajas();
                    lCantUnid = d.getCantUnid();                    
                    if(notaVenta.getConceptosDocumentos().getMafectaStock() == 'N'){
                        lCantCajas = 0;
                        lCantUnid = 0;
                    }
                    lNPeso = new BigDecimal(lCantCajas).multiply(d.getMercaderia().getNpesoCaja()).abs().longValue()
                            + new BigDecimal(lCantUnid).multiply(d.getMercaderia().getNpesoUnidad()).longValue();
                    mov.setCantCajas((long)lCantCajas);
                    mov.setCantUnid((long)lCantUnid);
                    mov.setNpeso(new BigDecimal(lNPeso));
                    lExentas = d.getIexentas();
                    lImpuestos = d.getImpuestos().abs().longValue();                    
                    lGravadas = d.getIgravadas() - lImpuestos;
                    mov.setIgravadas(lGravadas);
                    mov.setIexentas(lExentas);
                    mov.setImpuestos(lImpuestos);
                    String usuario = "user";//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                    mov.setCusuario(usuario);
                    movimientosMercaFacade.insertarMovimientosMerca(mov);
                    
                    ltotal = lExentas + lImpuestos + lGravadas;
                    facturasDetFacade.disminuirImporteNotas(
                            notaVenta.getNrofact(),
                            notaVenta.getFacCtipoDocum(),
                            DateUtil.formaterDateToString(factura.getFacturasPK().getFfactur(), "yyyyMMdd"),
                            d.getMercaderia().getMercaderiasPK().getCodMerca(),
                            ltotal*-1);
                    
                /*}catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al insertar en movimiento merca.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
                    return;
                }*/
            }
            TiposDocumentos td = getTipoDocFromList(this.notaVenta.getNotasVentasPK().getCtipoDocum());
            Short lMIndice = Short.parseShort("1");
            if(td.getMdebCred() == 'C'){
                lMIndice = Short.parseShort("-1");
            }            
            long lTExentas = totalExentas > 0 ? totalExentas * -1 : totalExentas;
            long lTGravadas = totalGravadas - Math.abs(totalImpuestos);
            long lTImpuestos = totalImpuestos > 0 ? totalImpuestos * -1 : totalImpuestos;
            if(td.getMafectaCtacte() == 'S'){
                //se inserta en cuentas corrientes
                //try{
                    CuentasCorrientes c = new CuentasCorrientes();
                    c.setCodEmpr(Short.parseShort("2"));
                    c.setCtipoDocum(notaVenta.getNotasVentasPK().getCtipoDocum());
                    c.setFmovim(new Date() /*notaVenta.getNotasVentasPK().getFdocum()*/ );
                    c.setNdocumCheq(String.valueOf( notaVenta.getNotasVentasPK().getNroNota()));
                    c.setNrofact(notaVenta.getNrofact());
                    c.setCtipoDocum(notaVenta.getFacCtipoDocum());
                    c.setFfactur(notaVenta.getFfactur());
                    c.setIsaldo(factura.getIsaldo());
                    c.setFvenc(factura.getFvenc());                    
                    c.setCodCliente(new Clientes(notaVenta.getCodCliente()));
                    c.setIpagado(0);
                    c.setIretencion(0);
                    c.setManulado(Short.parseShort("-1"));
                    c.setTexentas(lTExentas);
                    c.setTgravadas(lTGravadas);
                    c.setTimpuestos(lTImpuestos);
                    c.setMindice(lMIndice);                    
                    //personalizedFacade.ejecutarSentenciaSQL("set dateformat dmy");
                    cuentasCorrientesFacade.insertarCuentas(c);
                /*}catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al insertar cuenta corriente.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return;
                }*/
            }else{
                //actualizamos facturas
                //try{
                    facturasFacade.aumentarTotalNotas(totalNota*-1, 
                            notaVenta.getNotasVentasPK().getNroNota(), 
                            DateUtil.formaterDateToString(this.factura.getFacturasPK().getFfactur(), "yyyyMMdd"),
                            notaVenta.getNotasVentasPK().getCtipoDocum());
                /*}catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error al actualizar facturas.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    return;
                }*/
            }            
            userTransaction.commit();
            //** si no hubo errores en las inserciones anteriores, hacer COMMIT y avisar al usuario
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nota anulada.", ""));
            limpiarFormulario(); 
            listarNotasVentas();
            RequestContext.getCurrentInstance().execute("PF('dlgNuevaNota').hide();");
            RequestContext.getCurrentInstance().execute("PF('dlgConfirmarAnulacion').hide();");
        }catch(Exception e){
            userTransaction.rollback();
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al anular nota venta.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    public void eliminarNota() throws IllegalStateException, SystemException{
        try{
            if(notaVentaSeleccionada.getMestado() != 'X'){
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "AtenciÃ³n", "El documento debe anularse antes de su eliminaciÃ³n."));
                return;
            }
            //nota de venta
            userTransaction.begin();
            //try{
                notasVentasFacade.borrarNotaVenta(notaVentaSeleccionada.getNotasVentasPK().getNroNota(),
                        notaVentaSeleccionada.getNotasVentasPK().getCtipoDocum(),
                        DateUtil.formaterDateToString(notaVentaSeleccionada.getNotasVentasPK().getFdocum(), "yyyyMMdd"));
            /*}catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al eliminar nota venta.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return;
            }*/
            // detalles
            //try{
                notasVentasDetFacade.borrarNotaVentaDet(notaVentaSeleccionada.getNotasVentasPK().getNroNota(),
                        notaVentaSeleccionada.getNotasVentasPK().getCtipoDocum(),
                        DateUtil.formaterDateToString(notaVentaSeleccionada.getNotasVentasPK().getFdocum(), "yyyyMMdd"));
            /*}catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al eliminar nota venta.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return;
            } */           
            //cuentas corrientes
            //try{
            //if (1==1) throw new Exception("AAAAAAAAAAA");
            
                cuentasCorrientesFacade.borrarCuentaCorriente(notaVentaSeleccionada.getNotasVentasPK().getCtipoDocum(), 
                        notaVentaSeleccionada.getNotasVentasPK().getNroNota());
            /*}catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al eliminar cuenta corriente.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return;
            }*/
            //movimientos merca
            //try{
                movimientosMercaFacade.borrarMovimientosMerca(notaVentaSeleccionada.getNotasVentasPK().getNroNota(),
                        DateUtil.formaterDateToString(new Date(), "yyyyMMdd"),
                        notaVentaSeleccionada.getNotasVentasPK().getCtipoDocum());
            /*}catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al eliminar cuenta corriente.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return;
            }*/
            userTransaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos eliminados.", ""));
            limpiarFormulario(); //volvemos a instanciar
            RequestContext.getCurrentInstance().execute("PF('dlgNuevaNota').hide();");
        }catch(Exception e){
            userTransaction.rollback();
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al eliminar nota venta.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    public void cambiaNroNota(){
        if(agregar){
            try{
                /*Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, 0);
                Date fechaMaxima = c.getTime();
                c.add(Calendar.DAY_OF_MONTH, -5);
                Date fechaMinima = c.getTime();
                
                if(this.notaVenta.getNotasVentasPK().getFdocum().after(fechaMaxima) | 
                        this.notaVenta.getNotasVentasPK().getFdocum().before(fechaMinima)){
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                "AtenciÃ³n", "Ingrese una fecha entre " 
                                + DateUtil.formaterDateToString(fechaMinima) 
                                + " y el " + DateUtil.formaterDateToString(fechaMaxima) ));
                    return;
                }
                */
                if(nroNota == 0){
                    //Si no ingresa datos el usuario en Nro Nota, busca el valor mÃ¡ximo en la tabla de Notas Ventas y le suma  1
                    long nroNotaMaxima = notasVentasFacade.obtenerMaximoNroNota();
                    if(nroNotaMaxima == 0){
                        puntoEstabNota = 0;
                        puntoExpedNota = 0;
                        nroNota = 1;
                    }else{
                        puntoEstabNota = Short.parseShort(String.valueOf(nroNotaMaxima).substring(0, 1));
                        puntoExpedNota = Short.parseShort(String.valueOf(nroNotaMaxima).substring(1, 3));
                        nroNota = Long.parseLong(String.valueOf(nroNotaMaxima).substring(3))+1;
                    }                    
                }else{
                    List<NotasVentas> listadoNotas = null;
                    if (this.notaVenta.getNotasVentasPK().getFdocum()==null)
                        listadoNotas = notasVentasFacade.obtenerNotasVentasPorNroYTpDoc(obtenerNroNotaCompleto(), 
                            this.notaVenta.getNotasVentasPK().getCtipoDocum(), null);
                    else 
                        listadoNotas = notasVentasFacade.obtenerNotasVentasPorNroYTpDoc(obtenerNroNotaCompleto(), 
                            this.notaVenta.getNotasVentasPK().getCtipoDocum(), "01/01/2015");
                    
                    if(!listadoNotas.isEmpty()){
                        FacesContext.getCurrentInstance()
                                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                        "AtenciÃ³n", "Ya existe este nÃºmero de nota de crÃ©dito."));
                    }
                    
                    listadoNotas = notasVentasFacade.obtenerNotasVentasPorNroYTpDoc(obtenerNroNotaCompleto(), 
                        this.notaVenta.getNotasVentasPK().getCtipoDocum(), "01/06/2013");
                    if(!listadoNotas.isEmpty()){
                        FacesContext.getCurrentInstance()
                                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                    "AtenciÃ³n", "Ya existe este Nro.de Nota con Fecha "
                                    +DateUtil.formaterDateToString(listadoNotas.get(0).getNotasVentasPK().getFdocum(), "dd/MM/yyyy"))
                                );
                    }
                    
                    if (!validarTimbradoVencido(this.notaVenta.getNotasVentasPK().getCtipoDocum(), 
                            obtenerNroNotaCompleto(), this.notaVenta.getNotasVentasPK().getFdocum(), 
                            'M')){
                        FacesContext.getCurrentInstance().addMessage(null, 
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", 
                                    "Timbrado del documento está vencido/no existe."));
                    }
                    	
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en nÃºmero de nota.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            }
        }
    }
            
    private void limpiarDatosFactura(){
        FacturasPK facturaPK = new FacturasPK();
        factura = new Facturas();
        factura.setFacturasPK(facturaPK);
        this.notaVenta.setCodCliente(0);
        this.notaVenta.setNombreCliente("");
        this.notaVenta.setFacCtipoDocum("");
        this.notaVenta.setNrofact(0L);
        this.notaVenta.setFfactur(null);
        TiposVentasPK tvpk = new TiposVentasPK();
        this.factura.setCtipoVtaDesc("");
        this.factura.setCtipoDocumDesc("");
        this.ffactura = null;
        habilitaBotonBusquedaDetalleFacturas = true;
    }
    
    public int buscarFactura(){
        try{
            this.notaVenta.setNotasVentasDetCollection(new ArrayList<>());                
            this.listaMercaderias = new ArrayList();
                
            if(nroFact == 0) {
                limpiarDatosFactura();
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "AtenciÃ³n", 
                            "No existe factura "+obtenerNroFacturaCompletoConFormato()));
                return 1;
            }
            
            Character lMEstado = 0;
            try{
                //cabecera de la factura
                List<Facturas> listadoFacturas = null;
                if (this.ffactura == null) {
                    listadoFacturas = facturasFacade.buscarFactuaPorNroYFecha(
                        obtenerNroFacturaCompleto(puntoEstabFact, puntoExpedFact, nroFact), 
                        null);
                } else {
                    listadoFacturas = facturasFacade.buscarFactuaPorNroYFecha(
                        obtenerNroFacturaCompleto(puntoEstabFact, puntoExpedFact, nroFact), 
                        this.ffactura);
                }
                
                if(listadoFacturas.isEmpty()){
                    limpiarDatosFactura();
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "AtenciÃ³n", 
                                "No existe factura "+obtenerNroFacturaCompletoConFormato()));
                    return 1;
                }
                
                for(Facturas fact: listadoFacturas){
                    lMEstado = fact.getMestado();
                    if(lMEstado != 'A'){
                        break;
                    }
                    factura = fact;
                }
                if(lMEstado != 'A'){
                    limpiarDatosFactura();
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "AtenciÃ³n", 
                                "La factura no estÃ¡ activa. Estado actual "+lMEstado));
                    return 1;
                }

                if(factura.getIsaldo() == 0){
                    limpiarDatosFactura();
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "AtenciÃ³n", 
                                "Factura Cancelada "));
                    return 1;
                }
                
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al buscar facturas.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            }
            System.out.println("fin busqueda cabecera, cargamos datos");
            
            this.notaVenta.setCodCliente(this.factura.getCodCliente().getCodCliente() == null ? null : factura.getCodCliente().getCodCliente());
            this.notaVenta.setNombreCliente(this.factura.getCodCliente().getCodCliente() == null ? null : factura.getCodCliente().getXnombre());
            this.notaVenta.setFacCtipoDocum(this.factura.getFacturasPK().getCtipoDocum());
            this.notaVenta.setNrofact(this.factura.getFacturasPK().getNrofact());
            this.notaVenta.setFfactur(this.factura.getFacturasPK().getFfactur());
            TiposVentasPK tvpk = new TiposVentasPK();
            //tvpk.setCodEmpr(2);
            tvpk.setCtipoVta(factura.getCtipoVta());
            this.factura.setCtipoVtaDesc(tipoVentasFacade.getTipoVentaFromList(new TiposVentas(tvpk), listaTiposVentas).getXdesc());
            
            TiposDocumentos td = new TiposDocumentos();
            td.setCtipoDocum(this.factura.getFacturasPK().getCtipoDocum());
            tipoDocumentoFactura = tiposDocumentosFacade.getTipoDocumentoFromList(td, listadoTiposFacturas);
            this.factura.setCtipoDocumDesc(tipoDocumentoFactura.getXdesc());
            this.ffactura = this.factura.getFacturasPK().getFfactur();
            habilitaBotonBusquedaDetalleFacturas = false;
            // buscamos los detalles 
            try{
                System.out.println("buscando detalles");
                List<FacturasDet> listadoFacturasDet = facturasDetFacade
                        .obtenerDetallesDeFacturasMerc(this.factura.getFacturasPK().getCtipoDocum(), 
                                this.factura.getFacturasPK().getNrofact(),
                                this.factura.getFacturasPK().getFfactur());
                //BigDecimal divPor100 = null;
                //BigDecimal multiplicaPorPU = null;
                //BigDecimal multiplicaPorPC = null;
                
                for (FacturasDet det : listadoFacturasDet) {
                    this.listaMercaderias.add(det.getMercaderias());
                    
                    NotasVentasDet notadet = new NotasVentasDet();
                    notadet.setMercaderia(det.getMercaderias());
                    NotasVentasDetPK nvdetpk = new NotasVentasDetPK();
                    nvdetpk.setCodEmpr(notadet.getMercaderia().getMercaderiasPK().getCodEmpr());
                    nvdetpk.setCodMerca(notadet.getMercaderia().getMercaderiasPK().getCodMerca());                    
                    nvdetpk.setCtipoDocum(this.notaVenta.getNotasVentasPK().getCtipoDocum());
                    nvdetpk.setFdocum(this.notaVenta.getNotasVentasPK().getFdocum());
                    nvdetpk.setNroNota(obtenerNroNotaCompleto());                    
                    notadet.setNotasVentasDetPK(nvdetpk);
                    notadet.setPimpues(det.getPimpues());
                    notadet.setIprecioUnid(new BigDecimal(det.getIprecioUnidad()));
                    notadet.setIprecioCaja(new BigDecimal(det.getIprecioCaja()));
                    notadet.setCantCajas((int) det.getCantCajas() + (int) det.getCajasBonif());
                    notadet.setCantUnid((int) det.getCantUnid() + (int) det.getUnidBonif());
                    
                    notadet.setIexentas((det.getIexentas()>0) ? det.getIexentas()-det.getInotas() : det.getIexentas());
                    notadet.setIgravadas((det.getIgravadas()>0) ? det.getIgravadas() + (det.getImpuestos()!=null ? det.getImpuestos().longValue() : 0) - det.getInotas() : det.getIgravadas());
                    notadet.setImpuestos(det.getImpuestos());
                    
                    notadet.setPdesc(new BigDecimal("0"));
                    notadet.setExentaCredito(new Long("0"));
                    notadet.setGravadaCredito(new Long("0"));
                    notadet.setImpuestoCredito(0);

                    if (this.notaVenta.getPorcentajeDesc()>0){ 
                        notadet.setPdesc(new BigDecimal(this.notaVenta.getPorcentajeDesc()));
                        notadet.setExentaCredito(new Double(notadet.getIexentas() * (notadet.getPdesc().divide(new BigDecimal("100")).doubleValue())).longValue());
                        notadet.setGravadaCredito(new Double((notadet.getIgravadas() * (notadet.getPdesc().divide(new BigDecimal("100")).doubleValue()))).longValue());
                        notadet.setImpuestoCredito((long) (notadet.getGravadaCredito() -  ( notadet.getGravadaCredito() / ( new BigDecimal("1").add(det.getPimpues().divide(new BigDecimal("100"))).doubleValue())  )));                        
                        System.out.println("gravada " + notadet.getIgravadas());
                        System.out.println("gravada " + notadet.getImpuestoCredito());
                        //divPor100 = new BigDecimal(this.notaVenta.getPorcentajeDesc()/100);
                        //multiplicaPorPU = divPor100.multiply(new BigDecimal(det.getIprecioUnidad()));                        
                        //multiplicaPorPC = divPor100.multiply(new BigDecimal(det.getIprecioCaja()));                        
                    }
                    
                    //if(multiplicaPorPU!=null) notadet.setIprecioUnid(new BigDecimal(det.getIprecioUnidad()).subtract(multiplicaPorPU));
                    //else notadet.setIprecioUnid(new BigDecimal(det.getIprecioUnidad()));
                    //if (multiplicaPorPC!=null) notadet.setIprecioCaja(new BigDecimal(det.getIprecioCaja()).subtract(multiplicaPorPC));
                    //else notadet.setIprecioCaja(new BigDecimal(det.getIprecioCaja()));
                    
                    this.notaVenta.getNotasVentasDetCollection().add(notadet);
                }
                
                this.factura.setFacturasDetCollection(listadoFacturasDet);         
                
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error al buscar facturas servicios.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            }            
            //***devuelve en CurMer los saldos de cajas y unidades
            //calc_merca_fact
            try{
                long lTotalUnidad = 0; int lCantCajas = 0; int lCantUnid = 0;
                System.out.println("buscando movimiento de factura");
                List<MovimientosMerca> curMer = movimientosMercaFacade.calcularMercaFactDos(
                            this.factura.getFacturasPK().getCtipoDocum(), 
                            this.factura.getFacturasPK().getNrofact(), 
                            this.factura.getFacturasPK().getFfactur());
                
                for(NotasVentasDet det: this.notaVenta.getNotasVentasDetCollection()){
                    if(!curMer.isEmpty()){
                        for(MovimientosMerca mmerca: curMer){
                            if(det.getNotasVentasDetPK().getCodMerca().equals(mmerca.getCodMerca())){                                
                                lTotalUnidad = (mmerca.getCantCajas()*det.getMercaderia().getNrelacion().longValue())+mmerca.getCantUnid();
                                if(lTotalUnidad < 0){
                                   lTotalUnidad = lTotalUnidad *-1; 
                                }
                                lCantCajas = (int)(lTotalUnidad/det.getMercaderia().getNrelacion().longValue());
                                lCantUnid = (int)(lTotalUnidad%det.getMercaderia().getNrelacion().longValue());
                                det.setCantCajas(lCantCajas);
                                det.setCantUnid(lCantUnid);                                
                            }
                        }
                    }
                }
            }catch(Exception e){
                RequestContext.getCurrentInstance().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en calcular_merca_fact";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                return 1;
            }
            
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en buscar factura.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        System.out.println("fin busqueda");
        calcularTotales();
        
        System.out.println("sizq : " + this.listaMercaderias.size());
        
        return 0;
    }
    
    public void onCellEdit(CellEditEvent event) {
    //public void onCellEdit(RowEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        
        NotasVentasDet det = ((List<NotasVentasDet>)this.notaVenta.getNotasVentasDetCollection()).get(event.getRowIndex());
        
        if (det.getPdesc() == null){ 
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "AtenciÃ³n", 
                        "No se puede realizar calculo con porcentaje descuento de detalle vacio "));
            throw new RuntimeException("A");
        }
        
        switch (event.getColumn().getHeaderText()){
            case "% Desc":
                if (det.getPdesc()!=null){ 
                    det.setExentaCredito(new Double(det.getIexentas() * (det.getPdesc().divide(new BigDecimal("100")).doubleValue())).longValue());
                    det.setGravadaCredito(new Double((det.getIgravadas() * (det.getPdesc().divide(new BigDecimal("100")).doubleValue()))).longValue());
                    det.setImpuestoCredito((long) (det.getGravadaCredito() -  ( det.getGravadaCredito() / ( new BigDecimal("1").add(det.getPimpues().divide(new BigDecimal("100"))).doubleValue())  )));
                }
                break;
            case "Gravada Credito":
                if (det.getGravadaCredito() > ((det.getIgravadas() * det.getPdesc().divide(new BigDecimal("100")).doubleValue()) +300) ){
                    System.out.println("entra aca");
                    det.setGravadaCredito(new Double((det.getIgravadas() * (det.getPdesc().divide(new BigDecimal("100")).doubleValue()))).longValue());
                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    tituloError = "Alerta";
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_WARN, tituloError, "Maximo Importe es: " + det.getGravadaCredito()+300));
                    //RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    throw new RuntimeException("A");
                }
                break;
        }
        
        calcularTotales();
        
        /*if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }*/        
        //RequestContext context = RequestContext.getCurrentInstance();
	//context.update("panel_nuevo_articulo");
        //context.execute("cellCars.clearSelection()");
        //context.update("tablaRecibosComprasDet");        

    }
    
    private void buscarNota(NotasVentas notaPk){
        List<Facturas> listadoFacturas = null;
        // cargamos listas combos
        try {
            limpiarFormulario();
            habilitaBotonBusquedaDetalleFacturas = false;
            modificar = false;
            System.err.println("notaPk "+notaPk);
            System.err.println("notaPk.getNotasVentasPK() "+notaPk.getNotasVentasPK());
            this.notaVenta = notasVentasFacade.buscarNotaPorId(notaPk.getNotasVentasPK());
            this.notaVenta.getNotasVentasPK().setDescripcionTipoDocumento(listadoTiposNotas.get(0).getXdesc());
        
            if (String.valueOf(notaVenta.getNotasVentasPK().getNroNota()).length()>3) {
                puntoEstabNota = Short.parseShort(String.valueOf(notaVenta.getNotasVentasPK().getNroNota()).substring(0, 1));
                puntoExpedNota = Short.parseShort(String.valueOf(notaVenta.getNotasVentasPK().getNroNota()).substring(1, 3));
                nroNota = Long.parseLong(String.valueOf(notaVenta.getNotasVentasPK().getNroNota()).substring(3));
            } else {
                nroNota = notaVenta.getNotasVentasPK().getNroNota();
            }
                    
            if (notaVenta.getNrofact() != null) {
                listadoFacturas = facturasFacade.buscarFactuaPorNroYFecha(notaVenta.getNrofact(), notaVenta.getFfactur());
                for(Facturas fact: listadoFacturas) {
                    this.factura = fact;
                }
                
                if (this.factura.getFacturasPK().getNrofact() != 0){
                    if (String.valueOf(this.factura.getFacturasPK().getNrofact()).length()>3) {
                        puntoEstabFact = Short.parseShort(String.valueOf(this.factura.getFacturasPK().getNrofact()).substring(0, 1));
                        puntoExpedFact = Short.parseShort(String.valueOf(this.factura.getFacturasPK().getNrofact()).substring(1, 3));
                        nroFact = Long.parseLong(String.valueOf(this.factura.getFacturasPK().getNrofact()).substring(3));                
                    } else {
                        nroFact = this.factura.getFacturasPK().getNrofact();
                    }
                    List<FacturasDet> listadoFacturasDet = facturasDetFacade
                            .obtenerDetallesDeFacturasMerc(this.factura.getFacturasPK().getCtipoDocum(), 
                                    this.factura.getFacturasPK().getNrofact(),
                                    this.factura.getFacturasPK().getFfactur());
                    
                    this.factura.setFacturasDetCollection(listadoFacturasDet);
                
                    for(NotasVentasDet notadet: this.notaVenta.getNotasVentasDetCollection()){

                        notadet.setExentaCredito(notadet.getIexentas());
                        notadet.setGravadaCredito(notadet.getIgravadas());
                        notadet.setImpuestoCredito((notadet.getImpuestos()!=null) ? notadet.getImpuestos().longValue(): 0);

                        for (FacturasDet det : listadoFacturasDet) {
                            if (notadet.getMercaderia().equals(det.getMercaderias())){
                                //notadet.setIprecioUnid(new BigDecimal(det.getIprecioUnidad()));
                                //notadet.setIprecioCaja(new BigDecimal(det.getIprecioCaja()));
                                //notadet.setCantCajas((int) det.getCantCajas() + (int) det.getCajasBonif());
                                //notadet.setCantUnid((int) det.getCantUnid() + (int) det.getUnidBonif());
                                notadet.setIexentas((det.getIexentas()>0) ? det.getIexentas()-det.getInotas() : det.getIexentas());
                                notadet.setIgravadas((det.getIgravadas()>0) ? det.getIgravadas() + (det.getImpuestos()!=null ? det.getImpuestos().longValue() : 0) - det.getInotas() : det.getIgravadas());
                                notadet.setImpuestos(det.getImpuestos());
                                break;
                            }
                        }
                    }

                    if (factura.getCodCliente() != null) this.notaVenta.setNombreCliente(factura.getCodCliente().getXnombre());

                    TiposVentasPK tvpk = new TiposVentasPK();
                    //tvpk.setCodEmpr(2);
                    tvpk.setCtipoVta(factura.getCtipoVta());            
                    this.factura.setCtipoVtaDesc(tipoVentasFacade.getTipoVentaFromList(new TiposVentas(tvpk), listaTiposVentas).getXdesc());
                    //this.notaVenta.setFacCtipoDocum(this.factura.getFacturasPK().getCtipoDocum());
                    //this.notaVenta.setNrofact(this.factura.getFacturasPK().getNrofact());
                    //this.notaVenta.setFfactur(this.factura.getFacturasPK().getFfactur());
                    //tvpk.setCodEmpr(2);
                    this.ffactura = this.factura.getFacturasPK().getFfactur();
                    TiposDocumentos td = new TiposDocumentos();
                    td.setCtipoDocum(this.factura.getFacturasPK().getCtipoDocum());
                    tipoDocumentoFactura = tiposDocumentosFacade.getTipoDocumentoFromList(td, listadoTiposFacturas);
                    this.factura.setCtipoDocumDesc(tipoDocumentoFactura.getXdesc());
                }
                
            } else {
                puntoEstabFact = 0;
                puntoExpedFact = 0;
                nroFact = 0;
            }
            
            calcularTotales();
            
            RequestContext.getCurrentInstance().execute("PF('dlgNuevaNota').show();");
        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al visualizar notas ventas.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    public void visualizarNota(final SelectEvent event) {
        NotasVentas notaPk = (NotasVentas) event.getObject();
        buscarNota(notaPk);
    }
    
    public void onRowSelect(SelectEvent event) {
        if (notaVentaSeleccionada != null) {
            NotasVentasPK pk = notaVentaSeleccionada.getNotasVentasPK();
            if (pk != null) {
                if (!pk.getCtipoDocum().equals("") && pk.getFdocum() != null && pk.getNroNota() != 0) {
                    setHabilitaBotonEliminar(false);
                    setHabilitaBotonAnular(false);
                } else {
                    setHabilitaBotonEliminar(true);
                    setHabilitaBotonAnular(true);
                }
            }
        }
    }
    
    public void onConceptoSelectedListener(SelectEvent event){
        ConceptosDocumentos cconcep = (ConceptosDocumentos) event.getObject();
        habilitaPromo = Boolean.TRUE;
        if ("DPR".equals(cconcep.getConceptosDocumentosPK().getCconc())){
            habilitaPromo = Boolean.FALSE;
        }        
    }
    
    public void onMercaderiaSelectedListener(SelectEvent event){
        Mercaderias merc = (Mercaderias) event.getObject();
        
        merc = mercaderiasFacade.getMercaderiaFromList(
                        merc, 
                        this.listaMercaderias);
        
        for (NotasVentasDet det : this.notaVenta.getNotasVentasDetCollection()) {
            if (det.getMercaderia().equals(merc)) {
                FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                    "Atencion", "Ya existe mercaderia " + det.getMercaderia().getXdesc()) );
                break;
            }                
        }
        
        for (FacturasDet det : this.factura.getFacturasDetCollection()) {
            if (det.getMercaderias().equals(merc)){
                
                this.notaDet.setMercaderia(merc);
                NotasVentasDetPK nvdetpk = new NotasVentasDetPK();
                nvdetpk.setCodEmpr(merc.getMercaderiasPK().getCodEmpr());
                nvdetpk.setCodMerca(merc.getMercaderiasPK().getCodMerca());                    
                nvdetpk.setCtipoDocum(this.notaVenta.getNotasVentasPK().getCtipoDocum());
                nvdetpk.setFdocum(this.notaVenta.getNotasVentasPK().getFdocum());
                nvdetpk.setNroNota(obtenerNroNotaCompleto());                    
                this.notaDet.setNotasVentasDetPK(nvdetpk);
                
                this.notaDet.setPimpues(det.getPimpues());                
                this.notaDet.setIexentas((det.getIexentas()>0) ? det.getIexentas()-det.getInotas() : det.getIexentas());
                this.notaDet.setIgravadas((det.getIgravadas()>0) ? det.getIgravadas() + (det.getImpuestos()!=null ? det.getImpuestos().longValue() : 0) - det.getInotas() : det.getIgravadas());
                this.notaDet.setImpuestos(det.getImpuestos());

                this.notaDet.setExentaCredito(new Long("0"));
                this.notaDet.setGravadaCredito(new Long("0"));
                this.notaDet.setImpuestoCredito(0);
                
                if (this.notaDet.getMercaderia().getMgravExe() == 'G'){
                    Impuestos impuesto = mercaImpuestosFacade
                            .obtenerPorcentajeImpuestoPorMercaderiaDos(
                                this.notaDet.getMercaderia().getMercaderiasPK().getCodMerca());
                    this.notaDet.setPimpues(impuesto.getPimpues());
                } else {
                    this.notaDet.setPimpues(new BigDecimal("0"));
                }

                if (this.notaDetDesc !=null & notaDetDesc.intValue() >0 ){
                    Double por =  notaDetDesc / new Double("100");
                    Double div = null;
                    if (this.notaDet.getMercaderia().getMgravExe() == 'E'){
                        div = this.notaDet.getIexentas() * por;
                        this.notaDet.setIexentas( div.longValue() );                    
                        this.notaDet.setImpuestoCredito(0);
                    } else {
                        div = this.notaDet.getIgravadas() * por;
                        this.notaDet.setIgravadas(div.longValue());                        
                        div = 1+det.getPimpues().divide(new BigDecimal("100")).doubleValue();
                        div = this.notaDet.getIgravadas() / div;
                        div = this.notaDet.getIgravadas() - div;
                        this.notaDet.setImpuestos(new BigDecimal( div.toString() ));
                    }
                }

                /*if (this.notaDetDesc !=null & notaDetDesc.intValue() >0 ){ 
                    Double por =  notaDetDesc / new Double("100");
                    Double div = this.notaDet.getIexentas() * por;
                    this.notaDet.setIexentas( div.longValue() );
                    div = this.notaDet.getIgravadas() * por;
                    this.notaDet.setIgravadas(div.longValue());
                    div = 1+det.getPimpues().divide(new BigDecimal("100")).doubleValue();
                    div = this.notaDet.getIgravadas() / div;
                    div = this.notaDet.getIgravadas() - div;
                    this.notaDet.setImpuestos(new BigDecimal( div.toString() ));
                    System.err.println("this.notaDet.getIgravadas() " + this.notaDet.getIgravadas());
                } */
                break;
            }
        }
    }
    
    public void onPorcentajeBlur(){
        if (this.notaDetDesc!=null & this.notaDetDesc.intValue() >0 ){ 
            Double por =  notaDetDesc / new Double("100");
            Double div = null;
            for (FacturasDet det : this.factura.getFacturasDetCollection()) {
                if (det.getMercaderias().equals(this.notaDet.getMercaderia())) {
                    if (this.notaDet.getMercaderia().getMgravExe() == 'E'){
                        div = this.notaDet.getIexentas() * por;
                        this.notaDet.setIexentas( div.longValue() );                    
                        this.notaDet.setImpuestoCredito(0);
                    } else {
                        div = this.notaDet.getIgravadas() * por;
                        this.notaDet.setIgravadas(div.longValue());                        
                        div = 1+det.getPimpues().divide(new BigDecimal("100")).doubleValue();
                        div = this.notaDet.getIgravadas() / div;
                        div = this.notaDet.getIgravadas() - div;
                        this.notaDet.setImpuestos(new BigDecimal( div.toString() ));
                    }
                    break;
                }
            }
            
        }        
    }
    
    /* estos metodos deben se deben unificar en una clase util y ser estaticos si se pueden */
    private boolean validarTimbradoVencido(String lCTipoDocum, long lNewNroFactura, Date lFFactura, Character tipoPapel){
        try{
            try{
                Calendar c = Calendar.getInstance();
                c.setTime(lFFactura);
                int anhoFactura = c.get(Calendar.YEAR);
                List<RangosDocumentos> listadosRangos = rangosDocumentosFacade
                        .obtenerCursorImpuestos(lCTipoDocum, lNewNroFactura, tipoPapel, anhoFactura);
                if(listadosRangos.isEmpty()){
                    return false;
                }else{
                    for(RangosDocumentos r: listadosRangos){
                        if(r.getFvtoTimbrado().before(lFFactura)){
                            return false;
                        }else{
                            return true;
                        }
                    }
                }
            }catch(Exception e){
                PrimeFaces.current().ajax().update("exceptionDialog");
                contenidoError = ExceptionHandlerView.getStackTrace(e);
                tituloError = "Error en la generación de cursor de impuestos.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
                PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
                return false;
            }
        }catch(Exception e){
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en función timbrado vencido.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
            return false;
        }
        return true;
    }
    
    private long obtenerNroFacturaCompleto(long puntoEstabFact, long puntoExpedFact, long nroFact) {
        long nroFacturaCompleto = (long) (puntoEstabFact * 1000000000.00 + puntoExpedFact * 10000000.00 + nroFact);
        return nroFacturaCompleto;        
    }
    
    private String obtenerNroFacturaCompletoConFormato(){
        String puntoEstablec = String.valueOf(puntoEstabFact);
        String puntoExped = String.valueOf(puntoExpedFact);
        String nroFact = String.valueOf(this.nroFact);
        String resultado = rellenarConCeros(puntoEstablec, 3)+"-"+rellenarConCeros(puntoExped, 3)+"-"+rellenarConCeros(nroFact, 7);
        return resultado;
    }
    
    private long obtenerNroNotaCompleto() {
        long nroNotaCompleto = (long) (puntoEstabNota * 1000000000.00 + puntoExpedNota * 10000000.00 + nroNota);
        return nroNotaCompleto;
    }
    
    private String obtenerNroNotaCompletoConFormato(){
        String puntoEstablec = String.valueOf(puntoEstabNota);
        String puntoExped = String.valueOf(puntoEstabNota);
        String nroFact = String.valueOf(nroNota);
        String resultado = rellenarConCeros(puntoEstablec, 3)+"-"+rellenarConCeros(puntoExped, 3)+"-"+rellenarConCeros(nroFact, 7);
        return resultado;
    }
    
    private TiposDocumentos getTipoDocFromList(String tipoDoc){
        TiposDocumentos td = null;
        for(TiposDocumentos tdo : listadoTiposNotas){
            if (tdo.getCtipoDocum().equals(tipoDoc)){
                td = tdo;
                break;
            }
        }
        return td;
    }
    
    private ConceptosDocumentos getTipoDocFromList(ConceptosDocumentos cdoc){
        ConceptosDocumentos td = null;
        for(ConceptosDocumentos tdo : listadoConceptosDocumentos){
            if (tdo.getConceptosDocumentosPK().equals(cdoc.getConceptosDocumentosPK())){
                td = tdo;
                break;
            }
        }
        return td;
    }
    
    private static String rellenarConCeros(String cadena, int numCeros) {
        String ceros = "";
        for (int i = cadena.length(); i < numCeros; i++) {
            ceros += "0";
        }
        return ceros + cadena;
    }
    
    //getters && setters
    public Short getPuntoEstabNota() {
        return puntoEstabNota;
    }

    public void setPuntoEstabNota(Short puntoEstabNota) {
        this.puntoEstabNota = puntoEstabNota;
    }

    public Short getPuntoExpedNota() {
        return puntoExpedNota;
    }

    public void setPuntoExpedNota(Short puntoExpedNota) {
        this.puntoExpedNota = puntoExpedNota;
    }

    public long getNroNota() {
        return nroNota;
    }

    public void setNroNota(long nroNota) {
        this.nroNota = nroNota;
    }

    public Short getPuntoEstabFact() {
        return puntoEstabFact;
    }

    public void setPuntoEstabFact(Short puntoEstabFact) {
        this.puntoEstabFact = puntoEstabFact;
    }

    public Short getPuntoExpedFact() {
        return puntoExpedFact;
    }

    public void setPuntoExpedFact(Short puntoExpedFact) {
        this.puntoExpedFact = puntoExpedFact;
    }

    public long getNroFact() {
        return nroFact;
    }

    public void setNroFact(long nroFact) {
        this.nroFact = nroFact;
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

    public NotasVentas getNotaVenta() {
        return notaVenta;
    }

    public void setNotaVenta(NotasVentas notaVenta) {
        this.notaVenta = notaVenta;
    }

    public boolean isAgregar() {
        return agregar;
    }

    public void setAgregar(boolean agregar) {
        this.agregar = agregar;
    }

    public boolean isModificar() {
        return modificar;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    public Facturas getFactura() {
        return factura;
    }

    public void setFactura(Facturas factura) {
        this.factura = factura;
    }

    public List<TiposDocumentos> getListadoTiposNotas() {
        return listadoTiposNotas;
    }

    public void setListadoTiposNotas(List<TiposDocumentos> listadoTiposNotas) {
        this.listadoTiposNotas = listadoTiposNotas;
    }

    public List<TiposDocumentos> getListadoTiposFacturas() {
        return listadoTiposFacturas;
    }

    public void setListadoTiposFacturas(List<TiposDocumentos> listadoTiposFacturas) {
        this.listadoTiposFacturas = listadoTiposFacturas;
    }

    public List<ConceptosDocumentos> getListadoConceptosDocumentos() {
        return listadoConceptosDocumentos;
    }

    public void setListadoConceptosDocumentos(List<ConceptosDocumentos> listadoConceptosDocumentos) {
        this.listadoConceptosDocumentos = listadoConceptosDocumentos;
    }

    public boolean isHabilitaBotonAnular() {
        return habilitaBotonAnular;
    }

    public void setHabilitaBotonAnular(boolean habilitaBotonAnular) {
        this.habilitaBotonAnular = habilitaBotonAnular;
    }

    public boolean isHabilitaBotonEliminar() {
        return habilitaBotonEliminar;
    }

    public void setHabilitaBotonEliminar(boolean habilitaBotonEliminar) {
        this.habilitaBotonEliminar = habilitaBotonEliminar;
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

    public long getTotalImpuestos() {
        return totalImpuestos;
    }

    public void setTotalImpuestos(long totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }

    public long getTotalNota() {
        return totalNota;
    }

    public void setTotalNota(long totalNota) {
        this.totalNota = totalNota;
    }

    public NotasVentas getNotaVentaSeleccionada() {
        return notaVentaSeleccionada;
    }

    public void setNotaVentaSeleccionada(NotasVentas notaVentaSeleccionada) {
        this.notaVentaSeleccionada = notaVentaSeleccionada;
    }

    public LazyDataModel<NotasVentas> getModelNotas() {
        return modelNotas;
    }

    public void setModelNotas(LazyDataModel<NotasVentas> modelNotas) {
        this.modelNotas = modelNotas;
    }

    public List<NotasVentas> getListadoNotasVentas() {
        return listadoNotasVentas;
    }

    public void setListadoNotasVentas(List<NotasVentas> listadoNotasVentas) {
        this.listadoNotasVentas = listadoNotasVentas;
    }

    public boolean isHabilitaBotonBusquedaDetalleFacturas() {
        return habilitaBotonBusquedaDetalleFacturas;
    }

    public void setHabilitaBotonBusquedaDetalleFacturas(boolean habilitaBotonBusquedaDetalleFacturas) {
        this.habilitaBotonBusquedaDetalleFacturas = habilitaBotonBusquedaDetalleFacturas;
    }
    
    public boolean isHabilitaPromo() {
        return habilitaPromo;
    }

    public void setHabilitaPromo(boolean habilitaPromo) {
        this.habilitaPromo = habilitaPromo;
    }

    public NotasVentasDet getNotaDet() {
        return notaDet;
    }

    public void setNotaDet(NotasVentasDet notaDet) {
        this.notaDet = notaDet;
    }

    public List<Mercaderias> getListaMercaderias() {
        return listaMercaderias;
    }

    public void setListaMercaderias(List<Mercaderias> listaMercaderias) {
        this.listaMercaderias = listaMercaderias;
    }

    public long getTotalGrabada5() {
        return totalGrabada5;
    }

    public void setTotalGrabada5(long totalGrabada5) {
        this.totalGrabada5 = totalGrabada5;
    }

    public long getTotalImpuestosIva5() {
        return totalImpuestosIva5;
    }

    public void setTotalImpuestosIva5(long totalImpuestosIva5) {
        this.totalImpuestosIva5 = totalImpuestosIva5;
    }

    public long getTotalGrabada10() {
        return totalGrabada10;
    }

    public void setTotalGrabada10(long totalGrabada10) {
        this.totalGrabada10 = totalGrabada10;
    }

    public long getTotalImpuestosIva10() {
        return totalImpuestosIva10;
    }

    public void setTotalImpuestosIva10(long totalImpuestosIva10) {
        this.totalImpuestosIva10 = totalImpuestosIva10;
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

    public Date getFfactura() {
        return ffactura;
    }

    public void setFfactura(Date ffactura) {
        this.ffactura = ffactura;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getNotaDetDesc() {
        return notaDetDesc;
    }

    public void setNotaDetDesc(Double notaDetDesc) {
        this.notaDetDesc = notaDetDesc;
    }
    
    
}

