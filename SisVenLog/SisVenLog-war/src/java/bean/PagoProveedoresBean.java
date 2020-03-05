/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ChequesEmitidosFacade;
import dao.CuentasProveedoresFacade;
import dto.ChequeProveedorDto;
import entidad.Bancos;
import entidad.ChequesEmitidos;
import entidad.ChequesEmitidosPK;
import entidad.CuentasProveedores;
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
import util.ExceptionHandlerView;

/**
 *
 * @author Edu
 */

@ManagedBean
@SessionScoped
public class PagoProveedoresBean implements Serializable{
    
    @EJB
    private ChequesEmitidosFacade chequesEmitidosFacade;
    
    @EJB
    private CuentasProveedoresFacade cuentasProveedoresFacade;
        
    private List<ChequeProveedorDto> listadoChequesProvedores;
    private short codigoBanco;
    private String numeroCheque;
    private Long montoCheque;
    private Date fechaCobro;
    private Date fechaInicio;
    private Date fechaFin;
    private Short codigoProveedor;
    private Bancos bancoSeleccionado;
    private ChequesEmitidos chequesEmitidos;
    private CuentasProveedores cuentasProveedores;
    private Proveedores proveedorSeleccionado;
    private ChequeProveedorDto chequeProveedorDto;
    private String filtro;
    private String contenidoError;
    private String tituloError;
            
    
    @PostConstruct
    public void instanciar(){
        limpiarVariables();
        listadoChequesProvedores = new ArrayList<ChequeProveedorDto>();
        chequesEmitidos = new ChequesEmitidos();
        chequesEmitidos.setChequesEmitidosPK(new ChequesEmitidosPK());
        bancoSeleccionado = new Bancos();
        cuentasProveedores = new CuentasProveedores();
        chequeProveedorDto = new ChequeProveedorDto();
    }
    
    private void limpiarVariables(){
        this.codigoBanco = 0;
        this.numeroCheque = "";
        this.montoCheque = new Long("0");
        this.fechaCobro = new Date();
        this.fechaInicio = null;
        this.fechaFin = null;
        this.codigoProveedor = 0;
    }
    
    public String limpiarFormulario(){
        limpiarVariables();
        listadoChequesProvedores = new ArrayList<ChequeProveedorDto>();
        chequesEmitidos = new ChequesEmitidos();
        chequesEmitidos.setChequesEmitidosPK(new ChequesEmitidosPK());
        bancoSeleccionado = new Bancos();
        cuentasProveedores = new CuentasProveedores();
        chequeProveedorDto = new ChequeProveedorDto();
        proveedorSeleccionado = new Proveedores();
        return null;
    }
    
    public String listarChequesProveedoresNoCobrados(){
        try{
            
            codigoBanco = bancoSeleccionado != null ? bancoSeleccionado.getCodBanco() : 0;
            codigoProveedor = proveedorSeleccionado != null ? proveedorSeleccionado.getCodProveed() : 0;
            
            listadoChequesProvedores.clear();
            listadoChequesProvedores = chequesEmitidosFacade.listadoChequesNoPagadosProveedores(    Short.parseShort("2"), 
                                                                                                    codigoBanco, 
                                                                                                    numeroCheque, 
                                                                                                    montoCheque, 
                                                                                                    fechaInicio, 
                                                                                                    fechaFin, 
                                                                                                    codigoProveedor, 
                                                                                                    fechaCobro);
            
        }catch(Exception e){
//            RequestContext.getCurrentInstance().update("exceptionDialog");
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de cheques.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
        }
        return null;
    }
    
    public void seleccionarChequeNoCobrado() {
    }
    
    public String guardarChequesNoCobradosDeProveedores(){
        int contadorChequesSeleccionados = 0;
        List<CuentasProveedores> listadoCuentasProveedores = null;
        try{
            if(!listadoChequesProvedores.isEmpty()){
                for(ChequeProveedorDto cp: listadoChequesProvedores){
                    if(cp.isChequeEmitidoSeleccionado()){
                        contadorChequesSeleccionados++; 
                    }
                }
                if(contadorChequesSeleccionados == 0){
                    //no se selecciono ningun cheque para cobrar
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Seleccione al menos un cheque para cobrar.", null));
                    return null;
                }else{
                    for(ChequeProveedorDto cp: listadoChequesProvedores){
                        if(cp.isChequeEmitidoSeleccionado()){
                            if(cp.getChequeEmitido().getFcobro().before(cp.getChequeEmitido().getFemision()) || cp.getChequeEmitido().getFcobro().before(cp.getChequeEmitido().getFcheque())){
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fecha de cobro debe ser inferior a Fecha de emisión/vencimiento.", null));
                                return null;
                            }else{
                                if(chequesEmitidos != null){
                                    chequesEmitidos.setFcobro(cp.getChequeEmitido().getFcobro());
                                    chequesEmitidos.getChequesEmitidosPK().setNroCheque(cp.getChequeEmitido().getChequesEmitidosPK().getNroCheque());
                                    chequesEmitidos.getChequesEmitidosPK().setCodBanco(cp.getChequeEmitido().getChequesEmitidosPK().getCodBanco());
                                    //listar registros de cuentas proveedores
                                    try{
                                        listadoCuentasProveedores = cuentasProveedoresFacade.listarCuentasProveedoresPorNroChequeBanco(chequesEmitidos.getChequesEmitidosPK().getNroCheque(), chequesEmitidos.getChequesEmitidosPK().getCodBanco());
                                        if(listadoCuentasProveedores == null){
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe recibo/factura asociadas al nro. de cheque: "+chequesEmitidos.getChequesEmitidosPK().getNroCheque(), null));
                                            return null;
                                        }
                                    }catch(Exception e){
//                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        PrimeFaces.current().ajax().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en la lectura de cuentas proveedores.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                                        PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                    
                                    //actualizar cheques no cobrados
                                    try{
                                        int resultado = chequesEmitidosFacade.actualizarChequesEmitidosNoCobrados(chequesEmitidos);
                                    }catch(Exception e){
//                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        PrimeFaces.current().ajax().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error en la actualización de cheques.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                                        PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                    
                                    try{
                                        if(cuentasProveedores != null){
                                            cuentasProveedores.setCtipoDocum("CHP");
                                            cuentasProveedores.setMindice(Short.parseShort("-1"));
                                            cuentasProveedores.setFvenc(cp.getChequeEmitido().getFcheque());
                                            cuentasProveedores.setIpagado(cp.getChequeEmitido().getIcheque());
                                            cuentasProveedores.setTexentas(0);
                                            cuentasProveedores.setCodProveed(cp.getChequeEmitido().getCodProveed());
                                            cuentasProveedores.setFmovim(cp.getChequeEmitido().getFcobro());
                                            cuentasProveedores.setCodEmpr(Short.parseShort("2"));
                                            cuentasProveedores.setNdocumCheq(cp.getChequeEmitido().getChequesEmitidosPK().getNroCheque());
                                            cuentasProveedores.setIretencion(0);
                                            cuentasProveedores.setCodBanco(cp.getChequeEmitido().getBancos());
                                            cuentasProveedores.setIsaldo(0);
                                            cuentasProveedores.setTgravadas(0);
                                            cuentasProveedores.setTimpuestos(0);
                                            cuentasProveedores.setManulado(new Short("1"));
                                            cuentasProveedoresFacade.insertarEnCuentasProveedores(cuentasProveedores);
                                        }
                                    }catch(Exception e){
//                                        RequestContext.getCurrentInstance().update("exceptionDialog");
                                        PrimeFaces.current().ajax().update("exceptionDialog");
                                        contenidoError = ExceptionHandlerView.getStackTrace(e);
                                        tituloError = "Error de inserción de movimientos en cuenta proveedor.";
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                                        PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
                                        return null;
                                    }
                                }
                            }
                        }
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos grabados.", null));
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
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
            return null;
        }
    }

    public List<ChequeProveedorDto> getListadoChequesProvedores() {
        return listadoChequesProvedores;
    }

    public void setListadoChequesProvedores(List<ChequeProveedorDto> listadoChequesProvedores) {
        this.listadoChequesProvedores = listadoChequesProvedores;
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

    public Short getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(Short codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public Bancos getBancoSeleccionado() {
        return bancoSeleccionado;
    }

    public void setBancoSeleccionado(Bancos bancoSeleccionado) {
        this.bancoSeleccionado = bancoSeleccionado;
    }

    public ChequesEmitidos getChequesEmitidos() {
        return chequesEmitidos;
    }

    public void setChequesEmitidos(ChequesEmitidos chequesEmitidos) {
        this.chequesEmitidos = chequesEmitidos;
    }

    public CuentasProveedores getCuentasProveedores() {
        return cuentasProveedores;
    }

    public void setCuentasProveedores(CuentasProveedores cuentasProveedores) {
        this.cuentasProveedores = cuentasProveedores;
    }

    public Proveedores getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(Proveedores proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    public ChequeProveedorDto getChequeProveedorDto() {
        return chequeProveedorDto;
    }

    public void setChequeProveedorDto(ChequeProveedorDto chequeProveedorDto) {
        this.chequeProveedorDto = chequeProveedorDto;
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
