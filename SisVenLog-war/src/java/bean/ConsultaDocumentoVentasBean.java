/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ClientesFacade;
import dao.CuentasCorrientesFacade;
import dao.FacturasFacade;
import dao.NotasVentasFacade;
import dao.PedidosFacade;
import dao.TiposDocumentosFacade;
import dto.CuentaCorrienteDto;
import dto.FacturaDto;
import dto.NotaVentaDto;
import dto.PedidoDto;
import entidad.Clientes;
import entidad.TiposDocumentos;
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
import util.ExceptionHandlerView;

/**
 *
 * @author dadob
 */

@ManagedBean
@SessionScoped  
public class ConsultaDocumentoVentasBean implements Serializable{
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private ClientesFacade clientesFacade;
    
    @EJB
    private PedidosFacade pedidosFacade;
    
    @EJB
    private FacturasFacade facturasFacade;
    
    @EJB
    private NotasVentasFacade notasVentasFacade;
    
    @EJB
    private CuentasCorrientesFacade cuentasCorrientesFacade;
    
    private Date fechaInicialLab;
    private Date fechaFinalLab;
    private Integer codigoClienteLab;
    private String nombreClientLabe;
    private String tipoDocumentoLab;
    private long nroDocumentoLab;
    private Character estadoLab;
    private int estadoSeleccionado;
    
    private boolean mostrarGrillaPedido;
    private boolean mostrarGrillaFactura;
    private boolean mostrarGrillaNotaVenta;
    private String contenidoError;
    private String tituloError;
    private PedidoDto pedidoDto;
    private Clientes clientes;
    private List<Clientes> listaClientes;
    private String filtro;
    private FacturaDto facturaDto;
    private NotaVentaDto notaVentaDto;
    
    private TiposDocumentos tipoDocumentoSeleccionado;
    private List<TiposDocumentos> listadoTiposDocumentos;
    private List<PedidoDto> listadoPedidosDto;
    private List<FacturaDto> listadoFacturasDto;
    private List<NotaVentaDto> listadoNotaVentaDto; 
    List<CuentaCorrienteDto> listadoCtaCteDto;
    
    
    @PostConstruct
    public void init(){
        limpiarFormulario();
        listadoPedidosDto = new ArrayList<>();
        listadoFacturasDto = new ArrayList<>();
        listadoNotaVentaDto = new ArrayList<>();
        listadoCtaCteDto = new ArrayList<>();
    }
    
    public void limpiarFormulario(){
        fechaFinalLab = null;
        fechaFinalLab = null;
        codigoClienteLab = null;
        nombreClientLabe = null;
        tipoDocumentoLab = null;
        nroDocumentoLab = 0;
        estadoSeleccionado = 1; //estado activo por defecto
        tipoDocumentoSeleccionado = new TiposDocumentos();
        contenidoError = null;
        tituloError = null;
        mostrarGrillaPedido = false;
        mostrarGrillaFactura = false;
        mostrarGrillaNotaVenta = false;
        pedidoDto = new PedidoDto();
        facturaDto = new FacturaDto();
        notaVentaDto = new NotaVentaDto();
    }
    
    
    public List<TiposDocumentos> listarTiposDocumentos(){
        listadoTiposDocumentos = new ArrayList<>();
        try{
            listadoTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoParaConsultaDeVentas();
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de tipos de documentos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
        return listadoTiposDocumentos;
    }
    
    public void verificarCliente(){
        if(codigoClienteLab != null){
            if(codigoClienteLab == 0){
                //mostrar busqueda de clientes
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').show();");
            }else{
                try{
                    Clientes clienteBuscado = clientesFacade.find(codigoClienteLab);
                    if(clienteBuscado == null){
                        //mostrar busqueda de clientes
                        RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').show();");
                    }else{
                        this.nombreClientLabe = clienteBuscado.getXnombre();
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
    
    public void listarMovimientos(){
        switch (tipoDocumentoSeleccionado.getCtipoDocum()){
            case "PED": 
                listarPedidos();
                mostrarGrillaPedido = true;
                mostrarGrillaFactura = false;
                mostrarGrillaNotaVenta = false;
                break;
            case "FCR": 
                listarFacturas();
                mostrarGrillaPedido = false;
                mostrarGrillaFactura = true;
                mostrarGrillaNotaVenta = false;
                break;
            case "FCO": 
                listarFacturas();
                mostrarGrillaPedido = false;
                mostrarGrillaFactura = true;
                mostrarGrillaNotaVenta = false;
                break;
            case "CPV": 
                listarFacturas();
                mostrarGrillaPedido = false;
                mostrarGrillaFactura = true;
                mostrarGrillaNotaVenta = false;
                break;   
            case "NCV": 
                listarNotasDeVentas();
                mostrarGrillaPedido = false;
                mostrarGrillaFactura = false;
                mostrarGrillaNotaVenta = true;
                break;      
            case "NDV": 
                listarNotasDeVentas();
                mostrarGrillaPedido = false;
                mostrarGrillaFactura = false;
                mostrarGrillaNotaVenta = true;
                break;    
        }
    }
    
    private List<PedidoDto> listarPedidos(){
        listadoPedidosDto = new ArrayList<>();
        try{
            estabecerEstado();
            listadoPedidosDto = pedidosFacade.listadoDePedidos( fechaInicialLab, 
                                                                fechaFinalLab, 
                                                                estadoLab, 
                                                                codigoClienteLab, 
                                                                nroDocumentoLab);
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en búsqueda de pedidos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
        return listadoPedidosDto;
    }
    
    private List<FacturaDto> listarFacturas(){
        listadoFacturasDto = new ArrayList<>();
        try{
            estabecerEstado();
            tipoDocumentoLab = tipoDocumentoSeleccionado == null ? "" : tipoDocumentoSeleccionado.getCtipoDocum();
            listadoFacturasDto = facturasFacade.listadoDeFacturas(  fechaInicialLab, 
                                                                    fechaFinalLab, 
                                                                    estadoLab, 
                                                                    nroDocumentoLab, 
                                                                    tipoDocumentoLab, 
                                                                    codigoClienteLab);
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en búsqueda de facturas.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
        return listadoFacturasDto;
    }
    
    private List<NotaVentaDto> listarNotasDeVentas(){
        listadoNotaVentaDto = new ArrayList<>();
        try{
            estabecerEstado();
            tipoDocumentoLab = tipoDocumentoSeleccionado == null ? "" : tipoDocumentoSeleccionado.getCtipoDocum();
            listadoNotaVentaDto = notasVentasFacade.listadoDeNotasDeVentas( fechaInicialLab, 
                                                                            fechaFinalLab, 
                                                                            estadoLab, 
                                                                            nroDocumentoLab, 
                                                                            tipoDocumentoLab, 
                                                                            codigoClienteLab);
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en búsqueda de notas de ventas.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();"); 
        }
        return listadoNotaVentaDto;
    }
    
    private void estabecerEstado(){
        switch(estadoSeleccionado){
            case 1: estadoLab = 'A';
                break;
            case 2: estadoLab = 'I';
                break;
            case 3: estadoLab = 0;
                break;
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
    
    public void onRowSelect(SelectEvent event) {
        if (getClientes() != null) {
            if (getClientes().getXnombre() != null) {
                codigoClienteLab = getClientes().getCodCliente();
                nombreClientLabe = getClientes().getXnombre();
                RequestContext.getCurrentInstance().update("panel_buscador_documentos");
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').hide();");
            }
        }
    }
    
    public void inicializarListadoDeCuentasCorrientes(){
        long nroFacturaSeleccionada = 0;
        Date fechaFacturaSeleccionada = null;
        listadoCtaCteDto = new ArrayList<>();
        try{
            nroFacturaSeleccionada = facturaDto == null ? 0 : facturaDto.getFactura().getFacturasPK().getNrofact();
            fechaFacturaSeleccionada = facturaDto == null ? null : facturaDto.getFactura().getFacturasPK().getFfactur();
            listadoCtaCteDto = cuentasCorrientesFacade.listadoDeFacturasCuentasCorrientesPorNrofacturaYFecha(nroFacturaSeleccionada, fechaFacturaSeleccionada);
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la busqueda de datos de cuentas corrientes.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }
    
    //getters and setters
    public TiposDocumentos getTipoDocumentoSeleccionado() {
        return tipoDocumentoSeleccionado;
    }

    public void setTipoDocumentoSeleccionado(TiposDocumentos tipoDocumentoSeleccionado) {
        this.tipoDocumentoSeleccionado = tipoDocumentoSeleccionado;
    }

    public List<TiposDocumentos> getListadoTiposDocumentos() {
        return listadoTiposDocumentos;
    }

    public void setListadoTiposDocumentos(List<TiposDocumentos> listadoTiposDocumentos) {
        this.listadoTiposDocumentos = listadoTiposDocumentos;
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

    public Date getFechaInicialLab() {
        return fechaInicialLab;
    }

    public void setFechaInicialLab(Date fechaInicialLab) {
        this.fechaInicialLab = fechaInicialLab;
    }

    public Date getFechaFinalLab() {
        return fechaFinalLab;
    }

    public void setFechaFinalLab(Date fechaFinalLab) {
        this.fechaFinalLab = fechaFinalLab;
    }

    public Integer getCodigoClienteLab() {
        return codigoClienteLab;
    }

    public void setCodigoClienteLab(Integer codigoClienteLab) {
        this.codigoClienteLab = codigoClienteLab;
    }

    public String getNombreClientLabe() {
        return nombreClientLabe;
    }

    public void setNombreClientLabe(String nombreClientLabe) {
        this.nombreClientLabe = nombreClientLabe;
    }

    public String getTipoDocumentoLab() {
        return tipoDocumentoLab;
    }

    public void setTipoDocumentoLab(String tipoDocumentoLab) {
        this.tipoDocumentoLab = tipoDocumentoLab;
    }

    public long getNroDocumentoLab() {
        return nroDocumentoLab;
    }

    public void setNroDocumentoLab(long nroDocumentoLab) {
        this.nroDocumentoLab = nroDocumentoLab;
    }

    public boolean isMostrarGrillaPedido() {
        return mostrarGrillaPedido;
    }

    public void setMostrarGrillaPedido(boolean mostrarGrillaPedido) {
        this.mostrarGrillaPedido = mostrarGrillaPedido;
    }

    public boolean isMostrarGrillaFactura() {
        return mostrarGrillaFactura;
    }

    public void setMostrarGrillaFactura(boolean mostrarGrillaFactura) {
        this.mostrarGrillaFactura = mostrarGrillaFactura;
    }

    public boolean isMostrarGrillaNotaVenta() {
        return mostrarGrillaNotaVenta;
    }

    public void setMostrarGrillaNotaVenta(boolean mostrarGrillaNotaVenta) {
        this.mostrarGrillaNotaVenta = mostrarGrillaNotaVenta;
    }

    public int getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(int estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public List<PedidoDto> getListadoPedidosDto() {
        return listadoPedidosDto;
    }

    public void setListadoPedidosDto(List<PedidoDto> listadoPedidosDto) {
        this.listadoPedidosDto = listadoPedidosDto;
    }

    public PedidoDto getPedidoDto() {
        return pedidoDto;
    }

    public void setPedidoDto(PedidoDto pedidoDto) {
        this.pedidoDto = pedidoDto;
    }

    public Character getEstadoLab() {
        return estadoLab;
    }

    public void setEstadoLab(Character estadoLab) {
        this.estadoLab = estadoLab;
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

    public List<FacturaDto> getListadoFacturasDto() {
        return listadoFacturasDto;
    }

    public void setListadoFacturasDto(List<FacturaDto> listadoFacturasDto) {
        this.listadoFacturasDto = listadoFacturasDto;
    }

    public FacturaDto getFacturaDto() {
        return facturaDto;
    }

    public void setFacturaDto(FacturaDto facturaDto) {
        this.facturaDto = facturaDto;
    }

    public NotaVentaDto getNotaVentaDto() {
        return notaVentaDto;
    }

    public void setNotaVentaDto(NotaVentaDto notaVentaDto) {
        this.notaVentaDto = notaVentaDto;
    }

    public List<NotaVentaDto> getListadoNotaVentaDto() {
        return listadoNotaVentaDto;
    }

    public void setListadoNotaVentaDto(List<NotaVentaDto> listadoNotaVentaDto) {
        this.listadoNotaVentaDto = listadoNotaVentaDto;
    }

    public List<CuentaCorrienteDto> getListadoCtaCteDto() {
        return listadoCtaCteDto;
    }

    public void setListadoCtaCteDto(List<CuentaCorrienteDto> listadoCtaCteDto) {
        this.listadoCtaCteDto = listadoCtaCteDto;
    }
    
}
