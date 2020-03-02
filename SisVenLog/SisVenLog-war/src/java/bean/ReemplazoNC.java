 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ReemplazoNCFacade;
import dto.CantidadesOrdenCargaDto;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import util.ExceptionHandlerView;

/**
 *
 * @author Asus
 */
@ManagedBean
@SessionScoped
public class ReemplazoNC implements Serializable {

    @EJB
    private ReemplazoNCFacade reemplazoNCFacade;

    /**
     * *
     * atributos para manejo del front end
     */
    /* Datos originales */
    private Integer estabInicial;
    private Integer expInicial;
    private Integer nroNotaInicial;
    private Integer estabFinal;
    private Integer expFinal;
    private Integer nroNotaFinal;
    
    private Long nroNCRInicial;
    private Long nroNCRFinal;
    private Long nroNCRNuevo;
    
    private Date fechaNCROriginal;

    /* Datos nuevos */
    private Long estabNuevo;
    private Long expNuevo;
    private Long nroNotaNuevo;
    private Date fechaNCRNuevo;

    private String contenidoError;
    private String tituloError;

    private CantidadesOrdenCargaDto cantidadesOrdenCargaDto;

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.fechaNCROriginal = null;
        this.fechaNCRNuevo = new Date();

        this.estabInicial = null;
        this.expInicial = null;
        this.nroNotaInicial = null;
        this.estabFinal = null;
        this.expFinal = null;
        this.nroNotaFinal = null;
        
        this.nroNCRInicial = null;
        this.nroNCRFinal = null;
        this.nroNCRNuevo = null;
        
        this.estabNuevo = null;
        this.expNuevo = null;
        this.nroNotaNuevo = null;

        this.cantidadesOrdenCargaDto = null;
    }

    public void validarDatosNC() throws Exception {

        try {

            if (this.estabInicial == null || this.expInicial == null || this.nroNotaInicial == null || this.estabFinal == null 
                    || this.expFinal == null || this.nroNotaFinal == null) {
                return;
            }
            String NCRInicial =  (this.estabInicial).toString() + (this.expInicial).toString() + (this.nroNotaInicial).toString();
            String NCRFinal = (this.estabFinal).toString() + (this.expFinal).toString() + (this.nroNotaFinal).toString();
            String NCRNuevo = (this.estabNuevo).toString() + (this.expNuevo).toString() + (this.nroNotaNuevo).toString();
            nroNCRInicial = Long.valueOf(NCRInicial);
            nroNCRFinal = Long.valueOf(NCRFinal);
            nroNCRNuevo = Long.valueOf(NCRNuevo);
            
            this.cantidadesOrdenCargaDto = reemplazoNCFacade.buscarCantidadesNC(nroNCRInicial, nroNCRFinal);

            if (this.cantidadesOrdenCargaDto.getCantExistenteOC() != (nroNCRFinal - nroNCRInicial + 1)) {
                return;
            } else if (this.cantidadesOrdenCargaDto.getCantFechasDistintasOC() > 1) {
                return;
            }

            this.fechaNCROriginal = reemplazoNCFacade.buscarFechaNC(nroNCRInicial, nroNCRFinal);

            if (this.fechaNCROriginal == null) {
                return;
            }

        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos.";
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }

    }

    public void procesarReemplazoNC() throws Exception {
        
        try {

            Boolean error = false;

            if ((!error) && ((this.nroNCRInicial == null) || (this.nroNCRFinal == null) || (this.fechaNCRNuevo == null))) {
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "Algunos campos estan vacios!.";
            } else if ((!error) && (cantidadesOrdenCargaDto.getCantExistenteOC() == 0 || cantidadesOrdenCargaDto.getCantFechasDistintasOC() == 0)) {
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "No existe Nro.de Nota!.";
            } else if ((!error) && (this.cantidadesOrdenCargaDto.getCantExistenteOC() != (this.nroNCRFinal - this.nroNCRInicial + 1))) {
                error = true;
                tituloError = "Error en la lectura de datos.";
                contenidoError = "No existen todas las Notas de Credito Originales!.";
            } else if ((!error) && (cantidadesOrdenCargaDto.getCantFechasDistintasOC() > 1)) {
                error = true;
                tituloError = "Error en la lectura de datos.";
                contenidoError = "Las NCR deben pertenecer a una sola fecha!.";
            } else if ((!error) && (this.fechaNCROriginal == null)) {
                error = true;
                tituloError = "Error en la lectura de datos.";
                contenidoError = "No existen NOTAS DE CREDITO.";
            } else if ((!error) && (this.nroNCRNuevo == null)) {
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "Ingrese el Nuevo Numero!.";
            } else if ((!error) && ((this.nroNCRInicial == this.nroNCRNuevo) || (this.fechaNCROriginal == this.fechaNCRNuevo))) {
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "Todos los datos son iguales!.";
            }

            Integer cantidadExistenteNroNuevo = 0;

            if (!error) {
                cantidadExistenteNroNuevo = reemplazoNCFacade.buscarCantidadesNuevoNC(this.nroNCRNuevo, this.cantidadesOrdenCargaDto.getCantExistenteOC());
            }

            if ((!error) && (cantidadExistenteNroNuevo > 0)) {
                error = true;
                tituloError = "Error al ingresar datos.";
                contenidoError = "Ya existen los nuevos Numeros de NCR!.";
            }

            if ((!error) && ((this.fechaNCROriginal.getMonth() != this.fechaNCRNuevo.getMonth()) || (this.fechaNCROriginal.getYear() != this.fechaNCRNuevo.getYear()))) {
                error = true;
                tituloError = "Error en la lectura de datos.";
                contenidoError = "La Nueva Fecha debe estar en el mismo mes y año de Fecha Original!.";
            }

            if (error) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            }

            if (!error) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizando Notas de Credito ...", "Estado: PROCESO."));

                reemplazoNCFacade.procesarReemplazoNC(this.nroNCRInicial, this.nroNCRNuevo, this.fechaNCRNuevo, this.cantidadesOrdenCargaDto.getCantExistenteOC());

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizando Movimientos Mercaderias ...", "Estado: PROCESO."));

                reemplazoNCFacade.procesarMovimientosMercaderiasNC(this.nroNCRInicial, this.nroNCRNuevo, this.fechaNCROriginal, this.fechaNCRNuevo, this.cantidadesOrdenCargaDto.getCantExistenteOC());

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fin de Actualizacion!", "Estado: FINALIZADO."));
            }

        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al procesarlos datos.";
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }

    public Integer getEstabInicial() {
        return estabInicial;
    }

    public Integer getExpInicial() {
        return expInicial;
    }

    public Integer getNroNotaInicial() {
        return nroNotaInicial;
    }

    public Integer getEstabFinal() {
        return estabFinal;
    }

    public Integer getExpFinal() {
        return expFinal;
    }

    public Integer getNroNotaFinal() {
        return nroNotaFinal;
    }

    public Long getNroNCRInicial() {
        return nroNCRInicial;
    }

    public Long getNroNCRFinal() {
        return nroNCRFinal;
    }

    public Long getNroNCRNuevo() {
        return nroNCRNuevo;
    }

    public Date getFechaNCROriginal() {
        return fechaNCROriginal;
    }

    public Long getEstabNuevo() {
        return estabNuevo;
    }

    public Long getExpNuevo() {
        return expNuevo;
    }

    public Long getNroNotaNuevo() {
        return nroNotaNuevo;
    }

    public Date getFechaNCRNuevo() {
        return fechaNCRNuevo;
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

    public void setEstabInicial(Integer estabInicial) {
        this.estabInicial = estabInicial;
    }

    public void setExpInicial(Integer expInicial) {
        this.expInicial = expInicial;
    }

    public void setNroNotaInicial(Integer nroNotaInicial) {
        this.nroNotaInicial = nroNotaInicial;
    }

    public void setEstabFinal(Integer estabFinal) {
        this.estabFinal = estabFinal;
    }

    public void setExpFinal(Integer expFinal) {
        this.expFinal = expFinal;
    }

    public void setNroNotaFinal(Integer nroNotaFinal) {
        this.nroNotaFinal = nroNotaFinal;
    }

    public void setNroNCRInicial(Long nroNCRInicial) {
        this.nroNCRInicial = nroNCRInicial;
    }

    public void setNroNCRFinal(Long nroNCRFinal) {
        this.nroNCRFinal = nroNCRFinal;
    }

    public void setNroNCRNuevo(Long nroNCRNuevo) {
        this.nroNCRNuevo = nroNCRNuevo;
    }

    public void setFechaNCROriginal(Date fechaNCROriginal) {
        this.fechaNCROriginal = fechaNCROriginal;
    }

    public void setEstabNuevo(Long estabNuevo) {
        this.estabNuevo = estabNuevo;
    }

    public void setExpNuevo(Long expNuevo) {
        this.expNuevo = expNuevo;
    }

    public void setNroNotaNuevo(Long nroNotaNuevo) {
        this.nroNotaNuevo = nroNotaNuevo;
    }

    public void setFechaNCRNuevo(Date fechaNCRNuevo) {
        this.fechaNCRNuevo = fechaNCRNuevo;
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
