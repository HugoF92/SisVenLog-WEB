/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entidad.Zonas;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.LlamarReportes;
import dao.ZonasFacade;
import dao.LiVentasCreditoFacade;
import javax.ejb.EJB;

/**
 *
 * @author jvera
 */

@ManagedBean
@SessionScoped
public class ListadoVentasCreditoBean implements Serializable{
    private Date fechaDocumentoDesde;
    private Date fechaDocumentoHasta;
    private Zonas zona;
    private String discriminado;
    
    @EJB
    private LiVentasCreditoFacade liVentasCreditoFacade;
    
    @EJB
    private ZonasFacade zonasFacade;
    
    @PostConstruct
    public void instanciar(){
        limpiarFormulario();
        setDiscriminado("PZ");
    }
    
    public void limpiarFormulario(){
        fechaDocumentoDesde = null;
        fechaDocumentoHasta = null;
    }
    
    public void ejecutar(String tipo) {        
        if (validar()) {
            try {
                LlamarReportes rep = new LlamarReportes();
                if (tipo.equals("VIST")) {
                    
                    
                    String nombreRepo = "";
                    switch (discriminado){
                        case "PZ":
                            nombreRepo = "listadoVentasCredito" ;
                            break;
                        case "PF":
                            nombreRepo = "listadoVentasCreditoPF";
                            break;
                        case "ZF":
                            nombreRepo = "listadoVentasCredito2";
                            break;
                    }
                    String filename = "PZ".equals(discriminado) ? "rvtascred" : "rvtascred2";
                    String usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                    String sql = armarSql(fechaDocumentoDesde, fechaDocumentoHasta, zona, discriminado);
                    rep.listadoVentasCredito(sql, fechaDocumentoDesde, fechaDocumentoHasta, zona, discriminado, usuImprime, tipo, nombreRepo, filename);
                         
                } else { //Excel
                    
                    List<Object[]> lista = new ArrayList<>();
                    String[] columnas = new String[6];

                    columnas[0] = "cod_zona";
                    columnas[1] = "desc_zona";
                    columnas[2] = "ffactur";
                    columnas[3] = "ftotal";
                    columnas[4] = "tcheques";
                    columnas[5] = "tpagares";
                    

                   lista = liVentasCreditoFacade.generarListadoPedidosExcel(fechaDocumentoDesde, fechaDocumentoHasta, zona, discriminado);
                   if("PZ".equals(discriminado)){
                       rep.exportarExcel(columnas, lista, "rvtascred");   
                   }else{
                       rep.exportarExcel(columnas, lista, "rvtascred2");
                   }
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Error al generar archivo."));
            }
        }
        
    }
    private Boolean validar(){
        if (fechaDocumentoDesde == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha desde.", "Debe ingresar fecha desde."));            
            return false;
        }else{
            if (fechaDocumentoHasta == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha hasta.", "Debe ingresar fecha hasta."));            
                return false;
            }
        }
        return true;
    }

    private String armarSql(Date fechaDocumentoDesde, 
                            Date fechaDocumentoHasta, 
                            Zonas zona,
                            String discriminado){
        
        //preparamos los datos
        liVentasCreditoFacade.prepararDatosListadoVentasCredito(fechaDocumentoDesde, fechaDocumentoHasta, zona, discriminado);
        
        //devolvemos el sql para obtener los datos
        String sql ="";
        if ("PZ".equals(discriminado)) {
            sql = "SELECT * FROM curfin ORDER BY cod_zona";
        }else if ("PF".equals(discriminado)) {
            sql = "SELECT DISTINCT ffactur FROM curfin ORDER BY ffactur";
        }else{
            sql = "SELECT DISTINCT cod_zona, desc_zona FROM curfin ORDER BY cod_zona";
        }
        
        return sql;
    }
    
    public Date getFechaDocumentoDesde() {
        return fechaDocumentoDesde;
    }

    public void setFechaDocumentoDesde(Date fechaDocumentoDesde) {
        this.fechaDocumentoDesde = fechaDocumentoDesde;
    }

    public Date getFechaDocumentoHasta() {
        return fechaDocumentoHasta;
    }

    public void setFechaDocumentoHasta(Date fechaDocumentoHasta) {
        this.fechaDocumentoHasta = fechaDocumentoHasta;
    }

    public String getDiscriminado() {
        return discriminado;
    }

    public void setDiscriminado(String discriminado) {
        this.discriminado = discriminado;
    }
    
    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        if (zona != null) {
            this.zona = zonasFacade.buscarPorCodigo(zona.getZonasPK().getCodZona());
        } else {
            this.zona = zona;
        }
    }
}