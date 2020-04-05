/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.TiposDocumentosFacade;
import entidad.TiposDocumentos;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.*;
import javax.faces.bean.*;
import javax.faces.context.*;

/**
 *
 * @author Asus
 */

@ManagedBean
@SessionScoped
public class GenDocuAnul implements Serializable {

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    /***
     * atributos para manejo del front end
     */
    /* Fechas del formulario */
    private Date fechaDocumento;
    private Date fechaFactura;
    
    /* Tipo de documento de la factura */
    private TiposDocumentos tiposDocumentos;
    private List<TiposDocumentos> listaTiposDocumentos;
    
    /* Variables para numero de factura */
    private Integer estabFactInicial;
    private Integer expedFactInicial;
    private Integer secueFactFinal;
    /* Variables para numeracion de documento */
    private Integer estabInicial;
    private Integer expedInicial;
    private Integer secueInicial;
    private Integer secueFinal;

    //para manejo de errores
    private String contenidoError;
    private String tituloError;

    public GenDocuAnul() {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.fechaFactura = new Date();
        this.fechaDocumento = new Date();
        
        this.tiposDocumentos = new TiposDocumentos();
        this.listaTiposDocumentos = new ArrayList<TiposDocumentos>();
        
        estabInicial = 1;
        expedInicial = 1;
        secueInicial = 1;
        secueFinal = 1;
    }
    
    public void ejecutar(){
        if(!( (estabInicial == null || expedInicial == null || secueInicial == null || secueFinal == null) || (estabInicial == 1 && expedInicial == 1 && secueInicial == 1 && secueFinal == 1 )) ){
            if(secueInicial + 50 < secueFinal){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El rango maximo es solo de 50.", tituloError));
                return ;
            }
        }
        if(tiposDocumentos.getCtipoDocum().toUpperCase().equals("NCV")){
            if(fechaFactura == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ingrese la fecha de factura.", tituloError));
                return ;
            }else if( (estabFactInicial == null || expedFactInicial == null || secueFactFinal == null) || (estabFactInicial == 1 && expedFactInicial == 1 && secueFactFinal == 1)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ingrese Nro.Factura Contado Relacionada.", tituloError));
                return ;
            }
        }
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fin de Grabacion.", tituloError));
    }
    
    public List<TiposDocumentos> listarTipoDocumentoGenDocuAnul() {
        this.listaTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoGenDocuAnul();
        return this.listaTiposDocumentos;
    }
    
    private String dateToString(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    public TiposDocumentosFacade getTiposDocumentosFacade() {
        return tiposDocumentosFacade;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public List<TiposDocumentos> getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public Integer getEstabFactInicial() {
        return estabFactInicial;
    }

    public Integer getExpedFactInicial() {
        return expedFactInicial;
    }

    public Integer getSecueFactFinal() {
        return secueFactFinal;
    }

    public Integer getEstabInicial() {
        return estabInicial;
    }

    public Integer getExpedInicial() {
        return expedInicial;
    }

    public Integer getSecueInicial() {
        return secueInicial;
    }

    public Integer getSecueFinal() {
        return secueFinal;
    }

    public String getContenidoError() {
        return contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public void setTiposDocumentosFacade(TiposDocumentosFacade tiposDocumentosFacade) {
        this.tiposDocumentosFacade = tiposDocumentosFacade;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

    public void setEstabFactInicial(Integer estabFactInicial) {
        this.estabFactInicial = estabFactInicial;
    }

    public void setExpedFactInicial(Integer expedFactInicial) {
        this.expedFactInicial = expedFactInicial;
    }

    public void setSecueFactFinal(Integer secueFactFinal) {
        this.secueFactFinal = secueFactFinal;
    }

    public void setEstabInicial(Integer estabInicial) {
        this.estabInicial = estabInicial;
    }

    public void setExpedInicial(Integer expedInicial) {
        this.expedInicial = expedInicial;
    }

    public void setSecueInicial(Integer secueInicial) {
        this.secueInicial = secueInicial;
    }

    public void setSecueFinal(Integer secueFinal) {
        this.secueFinal = secueFinal;
    }

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }
}
