/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.GenDatosContablesFacade;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    /**
     * *
     * atributos para manejo del front end
     */
    private Date fechaInicial;
    private Date fechaFinal;
    private String documentosAnulados;

    //para manejo de errores
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

    public void generarDatos() {
        List<Object[]> listaRecibosVentas;
        List<Object[]> listaRecibosCompras;
        List<Object[]> listaRecibosFacturasVentasIvaInclNoIncl;
        List<Object[]> listaRecibosFacturasComprasIvaInclNoIncl;
        try {
            String path = genDatosContablesFacade.obtenerPath();
            if (documentosAnulados.equals("RV")) {
                listaRecibosVentas = genDatosContablesFacade.busquedaDatosRecibosVentas(dateToString(getFechaInicial()), dateToString(getFechaFinal()));
                if (listaRecibosVentas.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados, lista vacia!.", tituloError));
                } else {
                    String[] columnas = {"ncuota", "frecibo", "nrofact", "timbrado", "nrecibo ", "iefectivo", "cta_contable", "icheques", "nro_cheque", "cod_cta_cte", "moneda", "cotizacion"};

                    LlamarReportes rep = new LlamarReportes();

                    rep.exportarExcel(columnas, listaRecibosVentas, "revtacont");
                    
                    //FileWritter.guardarDatosRecibosVentas(path, listaRecibosVentas);
                }
            } else if (documentosAnulados.equals("RC")) { // no se hace por el momento
                listaRecibosCompras = genDatosContablesFacade.busquedaDatosRecibosCompras(dateToString(getFechaInicial()), dateToString(getFechaFinal()));
                if (listaRecibosCompras.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados, lista vacia!.", tituloError));
                } else {

                    String[] columnas = {"ctipo_docum", "nro_cuota", "frecibo", "nrecibo", "nrofact", "ctipo", "ffactur", "iefectivo", "nro_cheque", "ipagado", "cod_contable", "moneda", "cotizacion", "cod_proveed", "itotal", "ntimbrado", "fact_timbrado", "ntimbrado", "nota_timbrado"};

                    LlamarReportes rep = new LlamarReportes();

                    rep.exportarExcel(columnas, listaRecibosCompras, "recomcont");

                    //FileWritter.guardarDatosRecibosCompras(path, listaRecibosCompras);
                }
            } else if (documentosAnulados.equals("DV")) {
                listaRecibosFacturasVentasIvaInclNoIncl = genDatosContablesFacade.busquedaDatosFacturasVentas(dateToString(getFechaInicial()), dateToString(getFechaFinal()));
                if (listaRecibosFacturasVentasIvaInclNoIncl.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados, lista vacia!.", tituloError));
                } else {
                    String[] columnas = {"codvta", "codempr", "ffactur", "formapago", "xruc", "tipo", "nroboleta", "moneda", "cotizacion", "texentas", "gravadas5", "gravadas10", "ttotal", "cuenta", "detalle", "exentas", "gravadas", "incluido", "nrodetalle", "porciva", "iva5", "iva10", "ivamonto", "nombrecli", "mtipo_papel", "cuota", "timbrado"};

                    LlamarReportes rep = new LlamarReportes();

                    rep.exportarExcel(columnas, listaRecibosFacturasVentasIvaInclNoIncl, "vtascont");
                    
                    //FileWritter.guardarDatosFacturasVentas(path, listaRecibosFacturasVentasIvaInclNoIncl);
                }
            } else if (documentosAnulados.equals("DC")) {
                listaRecibosFacturasComprasIvaInclNoIncl = genDatosContablesFacade.busquedaDatosFacturasCompras(dateToString(getFechaInicial()), dateToString(getFechaFinal()));
                if (listaRecibosFacturasComprasIvaInclNoIncl.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados, lista vacia!.", tituloError));
                } else {
                    String[] columnas = {"codcompra", "codempr", "ffactur", "nroboleta", "xruc", "formapago", "moneda", "cotizacion", "tipo", "tipo1", "texentas", "gravadas5", "gravadas10", "ttotal", "cuenta", "detalle", "exentas", "gravadas", "incluido", "nrodetalle", "porciva", "iva5", "iva10", "ivamonto", "nombrepro", "cuota", "timbrado"};

                    LlamarReportes rep = new LlamarReportes();

                    rep.exportarExcel(columnas, listaRecibosFacturasComprasIvaInclNoIncl, "compcont");

                    //FileWritter.guardarDatosFacturasCompras(path, listaRecibosFacturasComprasIvaInclNoIncl);
                }
            }
        } catch (Exception e) {
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

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }
}
