/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.GenDatosContablesFacade;
import dto.RecibosVentasDto;
import entidad.Recibos;
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
import org.primefaces.context.RequestContext;
import util.ExceptionHandlerView;
import util.LlamarReportes;

/**
 *
 * @author Asus
 */

@ManagedBean
@SessionScoped
public class GenDatosContables implements Serializable {

    @EJB
    private GenDatosContablesFacade genDatosContablesFacade;
    /***
     * atributos para manejo del front end
     */
    private Date fechaInicial;
    private Date fechaFinal;
    private String documentosAnulados;

    private List<RecibosVentasDto> listaRecibosCompras;

    private String contenidoError;
    private String tituloError;

    public GenDatosContables() {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.fechaInicial = new Date();
        this.fechaFinal = new Date();
        setDocumentosAnulados("DV");
    }

    public void generarDatos(){
        try{
            if(documentosAnulados.equals("DV")){
                listaRecibosCompras = genDatosContablesFacade.busquedaDatosRecibosVentas(dateToString(getFechaInicial()), dateToString(getFechaFinal()));
                if(listaRecibosCompras.size() == 0){
                    //RequestContext.getCurrentInstance().update("exceptionDialog");
                    //contenidoError = "Resultado de la ejecucion del script es vacio. \nPor favor verifique datos de entrada.";//ExceptionHandlerView.getStackTrace(e);
                    //tituloError = "Datos no encontrados, lista vacia!.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados, lista vacia!.", tituloError));            
                    //RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
                }
            }
            //else if(documentosAnulados.equals("DC"))
        }
        catch (Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en la lectura de datos.", tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
    }

    private String dateToString(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    public GenDatosContablesFacade getGenDatosContablesFacade() {
        return genDatosContablesFacade;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public String getDocumentosAnulados() {
        return documentosAnulados;
    }

    public List<RecibosVentasDto> getListaRecibosCompras() {
        return listaRecibosCompras;
    }

    public String getContenidoError() {
        return contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public void setGenDatosContablesFacade(GenDatosContablesFacade genDatosContablesFacade) {
        this.genDatosContablesFacade = genDatosContablesFacade;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void setDocumentosAnulados(String documentosAnulados) {
        this.documentosAnulados = documentosAnulados;
    }

    public void setListaRecibosCompras(List<RecibosVentasDto> listaRecibosCompras) {
        this.listaRecibosCompras = listaRecibosCompras;
    }

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }
}
