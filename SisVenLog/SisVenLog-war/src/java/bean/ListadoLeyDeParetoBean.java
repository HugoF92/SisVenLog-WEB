/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.LeyParetoFacade;
import entidad.Proveedores;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.DateUtil;
import util.LlamarReportes;

import entidad.Lineas;
import entidad.TiposClientes;
import entidad.Zonas;
import entidad.CanalesVenta;
import javax.ejb.EJB;


/**
 *
 * @author jvera
 */

@ManagedBean
@SessionScoped
public class ListadoLeyDeParetoBean implements Serializable{
    private Date fechaFacturacionDesde;
    private Date fechaFacturacionHasta;
    private Lineas linea;
    private TiposClientes tipoCliente;
    private Zonas zonas;
    private CanalesVenta canalesVenta;
    private String discriminar;
    private String filtar;
    private String factorSeleccionado;
    private Integer factor;
    
    @EJB
    private LeyParetoFacade leyParetoFacade;
    
    @PostConstruct
    public void instanciar(){
        limpiarFormulario();
    }
    
    public void limpiarFormulario(){
        fechaFacturacionDesde = null;
        fechaFacturacionHasta = null;
    }
    
    public void ejecutar(String tipo) {        
        if (validar()) {
            try {
                LlamarReportes rep = new LlamarReportes();
                if (tipo.equals("VIST")) {
                    if ("PC".equals(discriminar)) {
                        
                    }else{ //por linea
                        
                    }
 
                } else { //Excel
                    if ("PC".equals(discriminar)) {
                        List<Object[]> lista = new ArrayList<>();
                        String[] columnas = new String[5];

                        columnas[0] = "cod_cliente";
                        columnas[1] = "xnombre";
                        columnas[2] = "cod_zona";
                        columnas[3] = "xdesc_zona";
                        columnas[4] = "itotal";
                       

                        lista = leyParetoFacade.generarLeyParetoExcel(fechaFacturacionDesde, 
                                                                      fechaFacturacionHasta, 
                                                                      linea, 
                                                                      tipoCliente, 
                                                                      zonas, 
                                                                      canalesVenta, 
                                                                      discriminar);
                        rep.exportarExcel(columnas, lista, "RPARETO1");
                        
                    } else { //discriminar por linea
                        List<Object[]> lista = new ArrayList<>();
                        String[] columnas = new String[8];

                        columnas[0] = "cod_cliente";
                        columnas[1] = "xnombre";
                        columnas[2] = "cod_zona";
                        columnas[3] = "xdesc_zona";
                        columnas[4] = "cod_linea";
                        columnas[5] = "xdesc_linea";
                        columnas[6] = "cod_merca";
                        columnas[7] = "itotal";
                        
                        lista = leyParetoFacade.generarLeyParetoExcel(fechaFacturacionDesde, 
                                                                     fechaFacturacionHasta, 
                                                                     linea, 
                                                                     tipoCliente, 
                                                                     zonas, 
                                                                     canalesVenta, 
                                                                     discriminar);
                        rep.exportarExcel(columnas, lista, "RPARETO2");
                    }
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Error al generar archivo."));
            }
        }
        
    }
    private Boolean validar(){
        if (fechaFacturacionDesde == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha desde.", "Debe ingresar fecha de facturación desde."));            
            return false;
        }else{
            if (fechaFacturacionHasta == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha hasta.", "Debe ingresar fecha de facturación hasta."));            
                return false;
            }
        }
        return true;
    }
    private String armarSqlSinDetalle(Date fechaReciboDesde, 
                                            Date fechaReciboHasta, 
                                            long nroReciboDesde, 
                                            long nroReciboHasta, 
                                            String discriminar,
                                            Proveedores provSeleccionado){
        
        String sql = "";
        try {
            if ("ND".equals(discriminar)) {
                sql += "SELECT r.*, t.cod_proveed as cod_proveed2, t.xnombre as xnombre, t.xnombre as xnombre2" +
                " FROM recibos_prov r, proveedores t" +
                " WHERE r.cod_empr = 2" +
                " AND r.cod_proveed = t.cod_proveed" +
                " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
                if (provSeleccionado != null){
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " ORDER BY nrecibo, frecibo";
            }else{ //discriminar por Proveedor
                sql += ";WITH principalCTE" +
                    " AS(" +
                    " SELECT r.*, t.cod_proveed as cod_proveed2, t.xnombre as xnombre, t.xnombre as xnombre2" +
                    " FROM recibos_prov r, proveedores t" +
                    " WHERE r.cod_empr = 2" +
                    " AND r.cod_proveed = t.cod_proveed" +
                    " AND r.fanul IS NULL" +
                    " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                    " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
                    if (provSeleccionado != null){
                        sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                    }
                    sql += " ) SELECT DISTINCT cod_proveed from principalCTE ORDER BY cod_proveed;";
            }
        }catch (Exception e) {
            System.out.println("Error al generar sql sin detalle: " + e.getMessage());
        }
        return sql;
    }
    
    private String armarSqlConDetalle(Date fechaReciboDesde, 
                                            Date fechaReciboHasta, 
                                            long nroReciboDesde, 
                                            long nroReciboHasta, 
                                            String discriminar,
                                            Proveedores provSeleccionado){
        
        String sql = "";
        try {
            if ("ND".equals(discriminar)) {
                sql += ";WITH principalCTE" +
                " AS(" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques," +
                " r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.nrofact as ndocum, '' as xdesc_banco, r.fanul," +
                " 'F' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2, d.itotal" +
                " FROM recibos_prov r, recibos_prov_det d, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND r.fanul IS NULL" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " UNION ALL" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo," +
                " r.iretencion, r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum," +
                " b.xdesc as xdesc_banco, r.fanul, 'C' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2," +
                " d.ipagado as itotal" +
                " FROM recibos_prov r, recibos_prov_cheques d, bancos b, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND d.cod_banco = b.cod_banco" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " AND r.icheques > 0";
                if (provSeleccionado != null){
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " )" +
                " SELECT DISTINCT nrecibo, frecibo, mestado, iefectivo, icheques, iretencion, irecibo, xnombre2, cod_proveed2, xobs from principalCTE ORDER BY nrecibo;";
            }else{ //discriminar por Proveedor
                sql += ";WITH principalCTE" +
                " AS(" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques," +
                " r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.nrofact as ndocum, '' as xdesc_banco, r.fanul," +
                " 'F' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2, d.itotal" +
                " FROM recibos_prov r, recibos_prov_det d, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND r.fanul IS NULL" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " UNION ALL" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo," +
                " r.iretencion, r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum," +
                " b.xdesc as xdesc_banco, r.fanul, 'C' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2," +
                " d.ipagado as itotal" +
                " FROM recibos_prov r, recibos_prov_cheques d, bancos b, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND d.cod_banco = b.cod_banco" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " AND r.icheques > 0";
                if (provSeleccionado != null){
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " )" +
                " SELECT DISTINCT cod_proveed from principalCTE ORDER BY cod_proveed;";
            }
        }catch (Exception e) {
            System.out.println("Error al generar sql con detalle: " + e.getMessage());
        }
        return sql;
    }
    
    private String armarSqlReciboProvDet(Date fechaReciboDesde, 
                                            Date fechaReciboHasta, 
                                            long nroReciboDesde, 
                                            long nroReciboHasta, 
                                            String discriminar,
                                            Proveedores provSeleccionado){
        
        String sql = "";
        try {
            if ("ND".equals(discriminar)) {
                sql += ";WITH principalCTE" +
                " AS(" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques," +
                " r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.nrofact as ndocum, '' as xdesc_banco, r.fanul," +
                " 'F' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2, d.itotal" +
                " FROM recibos_prov r, recibos_prov_det d, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND r.fanul IS NULL" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " UNION ALL" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo," +
                " r.iretencion, r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum," +
                " b.xdesc as xdesc_banco, r.fanul, 'C' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2," +
                " d.ipagado as itotal" +
                " FROM recibos_prov r, recibos_prov_cheques d, bancos b, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND d.cod_banco = b.cod_banco" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " AND r.icheques > 0";
                if (provSeleccionado != null){
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " )" +
                " SELECT DISTINCT * from principalCTE WHERE nrecibo = ";
            }else{ //discriminar por Proveedor
                sql += ";WITH principalCTE" +
                " AS(" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques," +
                " r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.nrofact as ndocum, '' as xdesc_banco, r.fanul," +
                " 'F' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2, d.itotal" +
                " FROM recibos_prov r, recibos_prov_det d, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND r.fanul IS NULL" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " UNION ALL" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo," +
                " r.iretencion, r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum," +
                " b.xdesc as xdesc_banco, r.fanul, 'C' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2," +
                " d.ipagado as itotal" +
                " FROM recibos_prov r, recibos_prov_cheques d, bancos b, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND d.cod_banco = b.cod_banco" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " AND r.icheques > 0";
                if (provSeleccionado != null){
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " )" +
                " SELECT DISTINCT nrecibo, xnombre, cod_proveed, frecibo, mestado, iefectivo, icheques, iretencion, irecibo, xnombre2, cod_proveed2, xobs from principalCTE Where cod_proveed = ";
            }
        }catch (Exception e) {
            System.out.println("Error al generar el detale del reporte de proveedores: " + e.getMessage());
        }
        return sql;
    }
    
    private String armarSqlReciboProvDetRec(Date fechaReciboDesde, 
                                            Date fechaReciboHasta, 
                                            long nroReciboDesde, 
                                            long nroReciboHasta, 
                                            String discriminar,
                                            Proveedores provSeleccionado){
        
        String sql = "";
        try {
            if ("ND".equals(discriminar)) {
                sql = null; 
            }else{ //discriminar por Proveedor
                sql += ";WITH principalCTE" +
                " AS(" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques," +
                " r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.nrofact as ndocum, '' as xdesc_banco, r.fanul," +
                " 'F' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2, d.itotal" +
                " FROM recibos_prov r, recibos_prov_det d, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2" +
                " AND r.fanul IS NULL" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " UNION ALL" +
                " SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo," +
                " r.iretencion, r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum," +
                " b.xdesc as xdesc_banco, r.fanul, 'C' as tipodet, c.cod_proveed as cod_proveed2, c.xnombre as xnombre2," +
                " d.ipagado as itotal" +
                " FROM recibos_prov r, recibos_prov_cheques d, bancos b, proveedores c" +
                " WHERE r.nrecibo = d.nrecibo" +
                " AND r.frecibo = d.frecibo" +
                " AND r.cod_empr = d.cod_empr" +
                " AND r.cod_empr = 2\n" +
                " AND d.cod_banco = b.cod_banco" +
                " AND r.cod_proveed = c.cod_proveed" +
                " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " AND r.icheques > 0";
                if (provSeleccionado != null){
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " )" +
                " SELECT DISTINCT * from principalCTE WHERE nrecibo = ";
            }
        }catch (Exception e) {
            System.out.println("Error al generar el detale del reporte de proveedores: " + e.getMessage());
        }
        return sql;
    }

    public Date getFechaFacturacionDesde() {
        return fechaFacturacionDesde;
    }

    public void setFechaFacturacionDesde(Date fechaFacturacionDesde) {
        this.fechaFacturacionDesde = fechaFacturacionDesde;
    }

    public Date getFechaFacturacionHasta() {
        return fechaFacturacionHasta;
    }

    public void setFechaFacturacionHasta(Date fechaFacturacionHasta) {
        this.fechaFacturacionHasta = fechaFacturacionHasta;
    }

    public Lineas getLinea() {
        return linea;
    }

    public void setLinea(Lineas linea) {
        this.linea = linea;
    }

    public TiposClientes getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TiposClientes tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public CanalesVenta getCanalesVenta() {
        return canalesVenta;
    }

    public void setCanalesVenta(CanalesVenta canalesVenta) {
        this.canalesVenta = canalesVenta;
    }

    public String getDiscriminar() {
        return discriminar;
    }

    public void setDiscriminar(String discriminar) {
        this.discriminar = discriminar;
    }

    public String getFiltar() {
        return filtar;
    }

    public void setFiltar(String filtar) {
        this.filtar = filtar;
    }
    
    public String getFactorSeleccionado(){
        return factorSeleccionado;
    }
    
    public void setFactorSeleccionado(String factorSeleccionado){
        this.factorSeleccionado = factorSeleccionado;
    }
    
    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }
}