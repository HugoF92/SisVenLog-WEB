/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ClientesFacade;
import dao.CuentasCorrientesFacade;
import dao.PagaresFacade;
import dto.PagareDto;
import entidad.Clientes;
import entidad.CuentasCorrientes;
import entidad.Pagares;
import entidad.PagaresPK;
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
import org.primefaces.component.datatable.DataTable;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import util.ExceptionHandlerView;

/**
 *
 * @author Edu
 */

@ManagedBean
@SessionScoped  
public class CobroPagaresBean implements Serializable{
    
    @EJB
    private PagaresFacade pagaresFacade;
    
    @EJB
    private CuentasCorrientesFacade cuentasCorrientesFacade;
    
    @EJB
    private ClientesFacade clientesFacade;
    
    private Pagares pagare;
    private PagareDto pagareDto;
    private CuentasCorrientes cuentasCorrientes;
    private List<PagareDto> listadoPagaresNoCobrados;
    private long numeroPagare;
    private Long montoPagare;
    private Date fechaCobro;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer codigoCliente;
    private String nombreCliente;
    private String codigoZona;
    private String contenidoError;
    private String tituloError;
    private Clientes clientes;
    private List<Clientes> listaClientes;
    private String filtro;
    
    public CobroPagaresBean() {
    }
    
    @PostConstruct
    public void instanciar(){
        limpiarVariables();
        listadoPagaresNoCobrados = new ArrayList<>();
        pagare = new Pagares();
        pagare.setPagaresPK(new PagaresPK());
        pagareDto = new PagareDto();
        pagareDto.setPagare(new Pagares());
        cuentasCorrientes = new CuentasCorrientes();        
    }
    
    private void limpiarVariables(){
        this.numeroPagare = 0;
        this.montoPagare = new Long("0");
        this.fechaCobro = new Date();
        this.fechaInicio = null;
        this.fechaFin = null;
        this.codigoCliente = new Integer("0");
        this.nombreCliente = "";
        this.codigoZona = "";
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
//            RequestContext.getCurrentInstance().update("exceptionDialog");
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de clientes.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
//            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
        }
    }
    
    public String limpiarFormulario(){
        limpiarVariables();
        listadoPagaresNoCobrados = new ArrayList<>();
        pagare = new Pagares();
        pagare.setPagaresPK(new PagaresPK());
        pagareDto = new PagareDto();
        pagareDto.setPagare(new Pagares());
        cuentasCorrientes = new CuentasCorrientes();
        return null;
    }
    
    public void listarPagaresNoCobrados(){
        try{
            
            codigoZona = codigoZona == null ? "" : codigoZona;
            
            if(montoPagare == null){
                montoPagare = Long.parseLong("0");
            }
            
            if(codigoCliente == null){
                codigoCliente = Integer.parseInt("0");
            }
            
            listadoPagaresNoCobrados.clear();
            listadoPagaresNoCobrados = pagaresFacade.listarPagaresNoCobrados(   codigoZona, 
                                                                                numeroPagare, 
                                                                                montoPagare, 
                                                                                fechaInicio, 
                                                                                fechaFin, 
                                                                                codigoCliente,
                                                                                fechaCobro);
        }catch(Exception e){
//            RequestContext.getCurrentInstance().update("exceptionDialog");
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de pagares.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en la lectura de datos de pagares.", tituloError));            
//            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
        }   
    }
    
    public void seleccionarPagareNoCobrado() {
    }
    
    public String guardarPagaresNoCobrados(){
        int contadorPagaresSeleccionados = 0;
        try{
            if(!listadoPagaresNoCobrados.isEmpty()){
                for(PagareDto pdto: listadoPagaresNoCobrados){
                    if(pdto.isPagareSeleccionado()){
                        contadorPagaresSeleccionados++; 
                    }
                }
                if(contadorPagaresSeleccionados == 0){
                    //no se selecciono ningun pagaré para cobrar
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Seleccione al menos un pagaré para cobrar.", null));
                    return null;
                }else{
                    for(PagareDto pdto: listadoPagaresNoCobrados){
                        if(pdto.isPagareSeleccionado()){
                            if(pdto.getPagare().getFcobro().before(pdto.getPagare().getFemision())){
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fecha de cobro debe ser mayor/igual a Fecha Emisión ", null));
                                return null;
                            }else{
                                if(pagare != null){
                                    pagare.setFcobro(pdto.getPagare().getFcobro());
                                    pagare.getPagaresPK().setNpagare(pdto.getPagare().getPagaresPK().getNpagare());
                                    try{
                                        //actualizar pagares no cobrados
                                        int resultado = pagaresFacade.actualizarPagaresNoCobrados(pagare);
                                    }catch(Exception e){
//                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        PrimeFaces.current().ajax().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en la actualización de pagarés.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
//                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                        PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                    
                                    try{
                                        if(cuentasCorrientes != null){
                                            cuentasCorrientes.setCtipoDocum("PAC");
                                            cuentasCorrientes.setMindice(new Short("-1"));
                                            cuentasCorrientes.setFvenc(pdto.getPagare().getFvenc());
                                            cuentasCorrientes.setIpagado(pdto.getPagare().getIpagare());
                                            cuentasCorrientes.setTexentas(0);
                                            cuentasCorrientes.setCodCliente(pdto.getPagare().getCodCliente());
                                            cuentasCorrientes.setFmovim(pdto.getPagare().getFcobro());
                                            cuentasCorrientes.setCodEmpr(new Short("2"));
                                            cuentasCorrientes.setNdocumCheq(Long.toString(pdto.getPagare().getPagaresPK().getNpagare()));
                                            cuentasCorrientes.setIretencion(0);
                                            cuentasCorrientes.setCodBanco(null);
                                            cuentasCorrientes.setIsaldo(0);
                                            cuentasCorrientes.setTgravadas(0);
                                            cuentasCorrientes.setTimpuestos(0);
                                            cuentasCorrientes.setManulado(new Short("1"));
                                            cuentasCorrientesFacade.insertarCuentas(cuentasCorrientes);
                                        }
                                    }catch(Exception sqle){
//                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        PrimeFaces.current().ajax().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(sqle);
                                        tituloError = "Error en la insersión de movimietos en cuentas corrientes.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
//                                        RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                                        PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                }
                            }
                        }
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos Grabados.", null));
                    return null;
                }
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No existen datos.", null));
                return null;
            }
         }catch(Exception e){
//            RequestContext.getCurrentInstance().update("exceptionDialog");
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al confirmar la operación.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
//            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
            return null;
        }
    }
    
    public void verificarCliente(){
        if(codigoCliente != null){
            if(codigoCliente == 0){
                //mostrar busqueda de clientes
//                RequestContext.getCurrentInstance().execute("PF('dlgBusClieCobroPagare').show();");
                PrimeFaces.current().executeScript("PF('dlgBusClieCobroPagare').show();");
            }else{
                try{
                    Clientes clienteBuscado = clientesFacade.find(codigoCliente);
                    if(clienteBuscado == null){
                        //mostrar busqueda de clientes
//                        RequestContext.getCurrentInstance().execute("PF('dlgBusClieCobroPagare').show();");
                        PrimeFaces.current().executeScript("PF('dlgBusClieCobroPagare').show();");
                    }else{
                        this.nombreCliente = clienteBuscado.getXnombre();
                    }
                }catch(Exception e){
//                    RequestContext.getCurrentInstance().update("exceptionDialog");
                    PrimeFaces.current().ajax().update("exceptionDialog");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la lectura de datos de clientes.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
//                    RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                    PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
                }
            }
        }
    }
    
    public void onRowSelect(SelectEvent event) {
        if (getClientes() != null) {
            if (getClientes().getXnombre() != null) {
                codigoCliente = getClientes().getCodCliente();
                nombreCliente = getClientes().getXnombre();
//                RequestContext.getCurrentInstance().update("panel_buscador_pagares");
                PrimeFaces.current().ajax().update("panel_buscador_pagares");
//                RequestContext.getCurrentInstance().execute("PF('dlgBusClieCobroPagare').hide();");
                PrimeFaces.current().executeScript("PF('dlgBusClieCobroPagare').hide();");
            }
        }
    }
    
    public void onCellEdit(CellEditEvent event) {  
        Object oldValue = event.getOldValue();  
        Object newValue = event.getNewValue();  
        if(!oldValue.equals(newValue)){
            DataTable table = (DataTable) event.getSource();
            PagareDto pagareDtoRow = (PagareDto) table.getRowData();
            getPagareDto().getPagare().setFcobro(pagareDtoRow.getPagare().getFcobro());
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Fecha de cobro actualizada!", ""));  
        }
    }

    public Pagares getPagare() {
        return pagare;
    }

    public void setPagare(Pagares pagare) {
        this.pagare = pagare;
    }

    public PagareDto getPagareDto() {
        return pagareDto;
    }

    public void setPagareDto(PagareDto pagareDto) {
        this.pagareDto = pagareDto;
    }

    public List<PagareDto> getListadoPagaresNoCobrados() {
        return listadoPagaresNoCobrados;
    }

    public void setListadoPagaresNoCobrados(List<PagareDto> listadoPagaresNoCobrados) {
        this.listadoPagaresNoCobrados = listadoPagaresNoCobrados;
    }

    public long getNumeroPagare() {
        return numeroPagare;
    }

    public void setNumeroPagare(long numeroPagare) {
        this.numeroPagare = numeroPagare;
    }

    public Long getMontoPagare() {
        return montoPagare;
    }

    public void setMontoPagare(Long montoPagare) {
        this.montoPagare = montoPagare;
    }
    
    public Date getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(Date fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Integer codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCodigoZona() {
        return codigoZona;
    }

    public void setCodigoZona(String codigoZona) {
        this.codigoZona = codigoZona;
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
 
}
