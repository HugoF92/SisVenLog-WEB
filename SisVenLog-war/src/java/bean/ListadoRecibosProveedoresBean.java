/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ProveedoresFacade;
import dao.RecibosProvFacade;
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
import util.DateUtil;
import util.LlamarReportes;


/**
 *
 * @author jvera
 */

@ManagedBean
@SessionScoped
public class ListadoRecibosProveedoresBean implements Serializable{
    private Date fechaReciboDesde;
    private Date fechaReciboHasta;
    private long nroReciboDesde;
    private long nroReciboHasta;
    private String discriminar;
    private Boolean conDetalle;
    private Proveedores proveedorSeleccionado;
   
    @EJB
    private ProveedoresFacade proveedoresFacade;
    
    @EJB
    private RecibosProvFacade recibosProvFacade; 
    
    @PostConstruct
    public void instanciar(){
        limpiarFormulario();
        proveedorSeleccionado = new Proveedores();
        setDiscriminar("ND");
        setConDetalle(false);
    }
    
    public void limpiarFormulario(){
        fechaReciboDesde = null;
        fechaReciboHasta = null;
        nroReciboDesde = 0;
        nroReciboHasta = 0;
    }
    
    public void ejecutar(String tipo) {        
        if (validar()) {
            try {
                LlamarReportes rep = new LlamarReportes();
                if (tipo.equals("VIST")) {
                    if (conDetalle) {
                        String nombreRepo = "ND".equals(discriminar) ? "reciboProvDetND" : "reciboProvDetPP";
                        String sqlConDet = armarSqlConDetalle(fechaReciboDesde, fechaReciboHasta, nroReciboDesde, nroReciboHasta, discriminar, proveedorSeleccionado);
                        String sqlReciboProDet = armarSqlReciboProvDet(fechaReciboDesde, fechaReciboHasta, nroReciboDesde, nroReciboHasta, discriminar, proveedorSeleccionado);
                        String sqlReciboProDetRec = armarSqlReciboProvDetRec(fechaReciboDesde, fechaReciboHasta, nroReciboDesde, nroReciboHasta, discriminar, proveedorSeleccionado);
                        
                        rep.reporteLiRecibosCom(sqlConDet,
                                fechaReciboDesde,
                                fechaReciboHasta,
                                nroReciboDesde,
                                nroReciboHasta,
                                proveedorSeleccionado,
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString(), tipo, nombreRepo, "Rreciboscomdet",
                                sqlReciboProDet, 
                                sqlReciboProDetRec);
                    }else { //sin detalle
                        String nombreRepo = "ND".equals(discriminar) ? "reciboProvND" : "reciboProvPP";
                        String sqlSinDet = armarSqlSinDetalle(fechaReciboDesde, fechaReciboHasta, nroReciboDesde, nroReciboHasta, discriminar, proveedorSeleccionado);
                        rep.reporteLiRecibosCom(sqlSinDet, 
                                fechaReciboDesde, fechaReciboHasta, nroReciboDesde, nroReciboHasta, proveedorSeleccionado,
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString(), tipo, nombreRepo, "Rreciboscom", null, null);
                    }

                } else { //Excel
                    if (conDetalle) {
                        List<Object[]> lista = new ArrayList<>();
                        String[] columnas = new String[18];

                        columnas[0] = "nrecibo";
                        columnas[1] = "cod_proveed";
                        columnas[2] = "frecibo";
                        columnas[3] = "irecibo";
                        columnas[4] = "iefectivo";
                        columnas[5] = "iretencion";
                        columnas[6] = "icheques";
                        columnas[7] = "xobs";
                        columnas[8] = "mestado";
                        columnas[9] = "xnombre";
                        columnas[10] = "ctipo_docum";
                        columnas[11] = "ndocum";
                        columnas[12] = "xdesc_banco";
                        columnas[13] = "fanul";
                        columnas[14] = "tipodet";
                        columnas[15] = "cod_proveed2";
                        columnas[16] = "xnombre2";
                        columnas[17] = "itotal";

                        lista = recibosProvFacade.listaProveedores(fechaReciboDesde, 
                                                                   fechaReciboHasta, 
                                                                   nroReciboDesde, 
                                                                   nroReciboHasta, 
                                                                   discriminar, 
                                                                   conDetalle, 
                                                                   proveedorSeleccionado);
                                
                        rep.exportarExcel(columnas, lista, "Rreciboscomdet");
                        
                    } else { //sin detalle
                        List<Object[]> lista = new ArrayList<>();
                        String[] columnas = new String[16];

                        columnas[0] = "cod_empr";
                        columnas[1] = "nrecibo";
                        columnas[2] = "cod_proveed";
                        columnas[3] = "frecibo";
                        columnas[4] = "cusuario";
                        columnas[5] = "falta";
                        columnas[6] = "fanul";
                        columnas[7] = "irecibo";
                        columnas[8] = "iefectivo";
                        columnas[9] = "iretencion";
                        columnas[10] = "icheques";
                        columnas[11] = "xobs";
                        columnas[12] = "mestado";
                        columnas[13] = "cod_proveed2";
                        columnas[14] = "xnombre";
                        columnas[15] = "xnombre2";
                        
                       lista = recibosProvFacade.listaProveedores(fechaReciboDesde, 
                                                                   fechaReciboHasta, 
                                                                   nroReciboDesde, 
                                                                   nroReciboHasta, 
                                                                   discriminar, 
                                                                   conDetalle, 
                                                                   proveedorSeleccionado);
                       rep.exportarExcel(columnas, lista, "Rreciboscom");
                    }
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Error al generar archivo."));
            }
        }
        
    }
    private Boolean validar(){
        if (fechaReciboDesde == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha desde.", "Debe ingresar fecha desde."));            
            return false;
        }else{
            if (fechaReciboHasta == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha hasta.", "Debe ingresar fecha hasta."));            
                return false;
            }else{
                if (nroReciboDesde < 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar un número de recibo desde.", "Debe ingresar un número de recibo desde."));            
                    return false;
                }else{
                    if (nroReciboHasta <= 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar un número de recibo hasta.", "Debe ingresar un número de recibo hasta."));            
                        return false;
                    }
                }
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
                " AND r.cod_proveed = c.cod_proveed";
                if (provSeleccionado != null) {
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
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
                " AND r.cod_proveed = c.cod_proveed";
                if (provSeleccionado != null) {
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " AND r.icheques > 0";
                
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
                " AND r.cod_proveed = c.cod_proveed";
                if (provSeleccionado != null) {
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
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
                " AND r.cod_proveed = c.cod_proveed";
                if (provSeleccionado != null) {
                    sql += " AND r.cod_proveed = " + provSeleccionado.getCodProveed();
                }
                sql += " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN '" + DateUtil.dateToString(fechaReciboDesde) + "' AND '" + DateUtil.dateToString(fechaReciboHasta) + "'" +
                " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
                " AND r.icheques > 0";
                
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
    
    public Date getFechaReciboDesde() {
        return fechaReciboDesde;
    }

    public void setFechaReciboDesde(Date fechaReciboDesde) {
        this.fechaReciboDesde = fechaReciboDesde;
    }

    public Date getFechaReciboHasta() {
        return fechaReciboHasta;
    }

    public void setFechaReciboHasta(Date fechaReciboHasta) {
        this.fechaReciboHasta = fechaReciboHasta;
    }

    public String getDiscriminar() {
        return discriminar;
    }

    public void setDiscriminar(String discriminar) {
        this.discriminar = discriminar;
    }

    public Boolean getConDetalle() {
        return conDetalle;
    }

    public void setConDetalle(Boolean conDetalle) {
        this.conDetalle = conDetalle;
    }

    public long getNroReciboDesde() {
        return nroReciboDesde;
    }

    public void setNroReciboDesde(long nroReciboDesde) {
        this.nroReciboDesde = nroReciboDesde;
    }

    public long getNroReciboHasta() {
        return nroReciboHasta;
    }

    public void setNroReciboHasta(long nroReciboHasta) {
        this.nroReciboHasta = nroReciboHasta;
    }

    public Proveedores getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(Proveedores proveedorSeleccionado) {
        if (proveedorSeleccionado != null) {
            this.proveedorSeleccionado = proveedoresFacade.proveedorById(proveedorSeleccionado.getCodProveed());
        }else{            
            this.proveedorSeleccionado = proveedorSeleccionado;
        }
    }
}