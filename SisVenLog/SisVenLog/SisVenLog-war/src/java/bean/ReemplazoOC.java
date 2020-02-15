/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ReemplazoOCFacade;
import dto.CantidadesOrdenCargaDto;
import java.io.IOException;
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
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import util.ExceptionHandlerView;
import util.LlamarReportes;

/**
 *
 * @author Asus
 */
@ManagedBean
@SessionScoped
public class ReemplazoOC implements Serializable {

    @EJB
    private ReemplazoOCFacade reemplazoOCFacade;

    /**
     * *
     * atributos para manejo del front end
     */
    /* Datos originales */
    private Long nroEnvioInicial;
    private Long nroEnvioFinal;
    private Date fechaEnvioOriginal;

    /* Datos nuevos */
    private Long nroEnvioNuevo;
    private Date fechaEnvioNuevo;

    private String contenidoError;
    private String tituloError;
    
    private CantidadesOrdenCargaDto cantidadesOrdenCargaDto;

    public ReemplazoOC() {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.fechaEnvioOriginal = null;
        this.fechaEnvioNuevo = new Date();
        
        this.nroEnvioInicial = null;
        this.nroEnvioFinal = null;
        this.nroEnvioNuevo = null;
        
        this.cantidadesOrdenCargaDto = null;
    }

    public void validarDatosOC() throws Exception {

        try {

            if (this.nroEnvioInicial == null || this.nroEnvioFinal == null)
                return;

            this.cantidadesOrdenCargaDto = reemplazoOCFacade.buscarCantidadesOC(this.nroEnvioInicial, this.nroEnvioFinal);

            if (this.cantidadesOrdenCargaDto.getCantExistenteOC() != (this.nroEnvioFinal - this.nroEnvioInicial + 1))
                return ;
            else if (this.cantidadesOrdenCargaDto.getCantFechasDistintasOC() > 1)
                return ;

            this.fechaEnvioOriginal = reemplazoOCFacade.buscarFechaOC(this.nroEnvioInicial, this.nroEnvioFinal);

            if (this.fechaEnvioOriginal == null)
                return ;

        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }

    }

    public void procesarReemplazoOC() throws Exception {

        try {
            
            Boolean error = false;

            if ((!error) && ((this.nroEnvioInicial == null) || (this.nroEnvioFinal == null) || (this.fechaEnvioNuevo == null))) {
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "Algunos campos estan vacios!.";
            } else if ((!error) && (cantidadesOrdenCargaDto.getCantExistenteOC() == 0 || cantidadesOrdenCargaDto.getCantFechasDistintasOC() == 0)) {
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "No existe Nro.de Envio!.";
            } else if ((!error) && (this.cantidadesOrdenCargaDto.getCantExistenteOC() != (this.nroEnvioFinal - this.nroEnvioInicial + 1))) {
                error = true;
                tituloError = "Error en la lectura de datos.";
                contenidoError = "No existen todos los Nros de OC Originales!.";
            } else if ((!error) && (cantidadesOrdenCargaDto.getCantFechasDistintasOC() > 1)) {
                error = true;
                tituloError = "Error en la lectura de datos.";
                contenidoError = "Las OC deben pertenecer a una sola fecha!.";
            } else if ((!error) && (this.fechaEnvioOriginal == null)) {
                error = true;
                tituloError = "Error en la lectura de datos.";
                contenidoError = "No existen Nros.de OC!.";
            } else if ((!error) && (this.nroEnvioNuevo == null)){
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "Ingrese el Nuevo Numero!.";
            } else if ((!error) && ((this.nroEnvioInicial == this.nroEnvioNuevo) || (this.fechaEnvioOriginal == this.fechaEnvioNuevo))){
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "Todos los datos son iguales!.";
            }

            Integer cantidadExistenteNroNuevo = 0;

            if(!error) cantidadExistenteNroNuevo = reemplazoOCFacade.buscarCantidadesNuevoOC(this.nroEnvioNuevo, this.cantidadesOrdenCargaDto.getCantExistenteOC());
                
            if ((!error) && (cantidadExistenteNroNuevo > 0)) {
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "Ya existen los nuevos Nro.de OC!.";
            }
            
            if ((!error) && ( (this.fechaEnvioOriginal.getMonth() != this.fechaEnvioNuevo.getMonth()) || (this.fechaEnvioOriginal.getYear() != this.fechaEnvioNuevo.getYear()) )) {
                error = true;
                tituloError = "Error en la lectura de datos.";
                contenidoError = "La Nueva Fecha debe estar en el mismo mes y año de Fecha Original!.";
            }
            
            if (error) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            }

            if (!error) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizando Envios ...", "Estado: PROCESO."));
                
                reemplazoOCFacade.procesarEnviosOC(this.nroEnvioInicial, this.nroEnvioNuevo, this.fechaEnvioNuevo, this.cantidadesOrdenCargaDto.getCantExistenteOC());
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizando Movimientos Mercaderias ...", "Estado: PROCESO."));
                
                reemplazoOCFacade.procesarMovimientosMercaderiasOC(this.nroEnvioInicial, this.nroEnvioNuevo, this.fechaEnvioOriginal, this.fechaEnvioNuevo, this.cantidadesOrdenCargaDto.getCantExistenteOC());
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fin de Actualizacion!", "Estado: FINALIZADO."));
            }

        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }

    public Long getNroEnvioInicial() {
        return nroEnvioInicial;
    }

    public Long getNroEnvioFinal() {
        return nroEnvioFinal;
    }

    public Date getFechaEnvioOriginal() {
        return fechaEnvioOriginal;
    }

    public Long getNroEnvioNuevo() {
        return nroEnvioNuevo;
    }

    public Date getFechaEnvioNuevo() {
        return fechaEnvioNuevo;
    }

    public String getContenidoError() {
        return contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public CantidadesOrdenCargaDto getCantidadesOrdenCargaDto() {
        return cantidadesOrdenCargaDto;
    }

    public void setNroEnvioInicial(Long nroEnvioInicial) {
        this.nroEnvioInicial = nroEnvioInicial;
    }

    public void setNroEnvioFinal(Long nroEnvioFinal) {
        this.nroEnvioFinal = nroEnvioFinal;
    }

    public void setFechaEnvioOriginal(Date fechaEnvioOriginal) {
        this.fechaEnvioOriginal = fechaEnvioOriginal;
    }

    public void setNroEnvioNuevo(Long nroEnvioNuevo) {
        this.nroEnvioNuevo = nroEnvioNuevo;
    }

    public void setFechaEnvioNuevo(Date fechaEnvioNuevo) {
        this.fechaEnvioNuevo = fechaEnvioNuevo;
    }

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }

    public void setCantidadesOrdenCargaDto(CantidadesOrdenCargaDto cantidadesOrdenCargaDto) {
        this.cantidadesOrdenCargaDto = cantidadesOrdenCargaDto;
    }
}
