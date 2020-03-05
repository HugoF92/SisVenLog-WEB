/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ChequesFacade;
import dao.ClientesFacade;
import dto.ChequeNoCobrado;
import entidad.Bancos;
import entidad.Cheques;
import entidad.ChequesPK;
import entidad.Clientes;
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
import util.ExceptionHandlerView;

/**
 *
 * @author Edu
 */

@ManagedBean
@SessionScoped  
public class ConsultaChequeBean implements Serializable{
    
    @EJB 
    private ChequesFacade chequesFacade;
    
    @EJB
    private ClientesFacade clientesFacade;
    
    private List<ChequeNoCobrado> listaChequesNoCobrados;
    private ChequeNoCobrado chequeNoCobrado;
    private Cheques cheque;
    private Bancos banco;
            
    private short codigoBanco;
    private String numeroCheque;
    private Long montoCheque;
    private Date fechaCobro;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer codigoCliente;
    private String nombreCliente;
    private Clientes clientes;
    private List<Clientes> listaClientes;
    private String filtro;
    private String contenidoError;
    private String tituloError;
            
    public ConsultaChequeBean() {
    }
    
    @PostConstruct
    public void instanciar(){
        limpiarVariables();
        listaChequesNoCobrados = new ArrayList<>();
        cheque = new Cheques();
        cheque.setChequesPK(new ChequesPK());
        chequeNoCobrado = new ChequeNoCobrado();
        chequeNoCobrado.setCheque(new Cheques());
        banco = new Bancos();
    }
    
    private void limpiarVariables(){
        this.codigoBanco = 0;
        this.numeroCheque = "";
        this.montoCheque = new Long("0");
        this.fechaCobro = null;
        this.fechaInicio = null;
        this.fechaFin = null;
        this.codigoCliente = new Integer("0");
        this.nombreCliente = "";
    }
    
    public String limpiarFormulario(){
        limpiarVariables();
        listaChequesNoCobrados = new ArrayList<>();
        cheque = new Cheques();
        cheque.setChequesPK(new ChequesPK());
        chequeNoCobrado = new ChequeNoCobrado();
        chequeNoCobrado.setCheque(new Cheques());
        banco = new Bancos();
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
//            RequestContext.getCurrentInstance().update("exceptionDialog");
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de clientes.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
//            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
        }
        
    }
    
    public void listarCheques(){
        try{
            
            codigoBanco = banco == null ? 0 : banco.getCodBanco();
            listaChequesNoCobrados.clear();
            listaChequesNoCobrados = chequesFacade.listadoCheques(              Short.parseShort("2"), 
                                                                                codigoBanco, 
                                                                                numeroCheque, 
                                                                                montoCheque, 
                                                                                fechaInicio, 
                                                                                fechaFin, 
                                                                                codigoCliente, 
                                                                                fechaCobro);
            
        }catch(Exception e){
//            RequestContext.getCurrentInstance().update("exceptionDialog");
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de cheques.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
//            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
        } 
        
    }
    
    public void verificarCliente(){
        if(codigoCliente != null){
            if(codigoCliente == 0){
                //mostrar busqueda de clientes
//                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaCheque').show();");
                PrimeFaces.current().executeScript("PF('dlgBusClieConsultaCheque').show();");
            }else{
                try{
                    Clientes clienteBuscado = clientesFacade.find(codigoCliente);
                    if(clienteBuscado == null){
                        //mostrar busqueda de clientes
//                        RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaCheque').show();");
                        PrimeFaces.current().executeScript("PF('dlgBusClieConsultaCheque').show();");
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
//                RequestContext.getCurrentInstance().update("panel_buscador_cheques");
                PrimeFaces.current().ajax().update("panel_buscador_cheques");
//                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaCheque').hide();");
                PrimeFaces.current().executeScript("PF('dlgBusClieConsultaCheque').hide();");
            }
        }
    }

    public List<ChequeNoCobrado> getListaChequesNoCobrados() {
        return listaChequesNoCobrados;
    }

    public void setListaChequesNoCobrados(List<ChequeNoCobrado> listaChequesNoCobrados) {
        this.listaChequesNoCobrados = listaChequesNoCobrados;
    }

    public short getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(short codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public Long getMontoCheque() {
        return montoCheque;
    }

    public void setMontoCheque(Long montoCheque) {
        this.montoCheque = montoCheque;
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

    public ChequeNoCobrado getChequeNoCobrado() {
        return chequeNoCobrado;
    }

    public void setChequeNoCobrado(ChequeNoCobrado chequeNoCobrado) {
        this.chequeNoCobrado = chequeNoCobrado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Bancos getBanco() {
        return banco;
    }

    public void setBanco(Bancos banco) {
        this.banco = banco;
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
    
}
