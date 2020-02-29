/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entidad.Empleados;
import entidad.CanalesVenta;
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
import dao.ListadoPedidosFacade;
import dao.EmpleadosFacade;
import dao.CanalesVentaFacade;
import dao.ZonasFacade;
import javax.ejb.EJB;
import util.DateUtil;
/**
 *
 * @author jvera
 */

@ManagedBean
@SessionScoped
public class ListadoPedidosBean implements Serializable{
    private Date fechaPedidoDesde;
    private Date fechaPedidoHasta;
    private Empleados vendedor;
    private CanalesVenta canal;
    private Zonas zona;                    
    private long nroPedidoDesde;
    private long nroPedidoHasta;
    private String seleccionFecha;
    private String seleccionTipo;
    private Boolean conDetalle;
    
    @EJB
    private ListadoPedidosFacade listadoPedidosFacade;
    
    @EJB
    private EmpleadosFacade empleadosFacade;
    
    @EJB
    private CanalesVentaFacade canalesVentaFacade;
    
    @EJB
    private ZonasFacade zonasFacade;
    
    @PostConstruct
    public void instanciar(){
        limpiarFormulario();
    }
    
    public void limpiarFormulario(){
        fechaPedidoDesde = null;
        fechaPedidoHasta = null;
        nroPedidoDesde = 0;
        nroPedidoHasta = 0;
    }
    
    public void ejecutar(String tipo) {        
        if (validar()) {
            try {
                LlamarReportes rep = new LlamarReportes();
                if (tipo.equals("VIST")) {
                    if (conDetalle) {
                        /*String nombreRepo = "ND".equals(discriminar) ? "reciboProvDetND" : "reciboProvDetPP";
                        String sqlConDet = armarSqlConDetalle(fechaReciboDesde, fechaReciboHasta, nroReciboDesde, nroReciboHasta, discriminar, proveedorSeleccionado);
                        rep.reporteLiRecibosCom(sqlConDet,
                                fechaReciboDesde,
                                fechaReciboHasta,
                                nroReciboDesde,
                                nroReciboHasta,
                                proveedorSeleccionado,
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString(), tipo, nombreRepo, "Rreciboscomdet",
                                armarSqlReciboProvDet(fechaReciboDesde, fechaReciboHasta, nroReciboDesde, nroReciboHasta, discriminar, proveedorSeleccionado), 
                                armarSqlReciboProvDetRec(fechaReciboDesde, fechaReciboHasta, nroReciboDesde, nroReciboHasta, discriminar, proveedorSeleccionado));
                    */
                    }else { //sin detalle
                        String nombreRepo = "listadoPedido";
                        String sql = armarSql(fechaPedidoDesde, fechaPedidoHasta, vendedor, canal, zona, nroPedidoDesde, nroPedidoHasta, seleccionFecha, seleccionTipo, conDetalle);
                        String sqlDetalle = armarSqlDetalle(fechaPedidoDesde, fechaPedidoHasta, vendedor, canal, zona, nroPedidoDesde, nroPedidoHasta, seleccionFecha, seleccionTipo, conDetalle);
                        rep.reporteLiPedidos(sql, sqlDetalle, fechaPedidoDesde, fechaPedidoHasta, seleccionTipo, seleccionFecha, vendedor, canal, zona, FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString(), tipo, nombreRepo, "rlispedidos");
                    }
                        
                } else { //Excel
                    if (conDetalle) {
                        List<Object[]> lista = new ArrayList<>();
                        String[] columnas = new String[47];

                        columnas[0] = "cod_empr";
                        columnas[1] = "nro_pedido";
                        columnas[2] = "ctipo_docum";
                        columnas[3] = "ctipo_vta";
                        columnas[4] = "cod_vendedor";
                        columnas[5] = "cod_depo_envio";
                        columnas[6] = "cod_entregador";
                        columnas[7] = "cod_canal";
                        columnas[8] = "cod_ruta";
                        columnas[9] = "cod_cliente";
                        columnas[10] = "cod_depo";
                        columnas[11] = "fpedido";
                        columnas[12] = "npeso_acum";
                        columnas[13] = "fanul";
                        columnas[14] = "ffactur";
                        columnas[15] = "fenvio";
                        columnas[16] = "tgravadas";
                        columnas[17] = "texentas";
                        columnas[18] = "timpuestos";
                        columnas[19] = "tdescuentos";
                        columnas[20] = "ttotal";
                        columnas[21] = "mestado";
                        columnas[22] = "cusuario";
                        columnas[23] = "falta";
                        columnas[24] = "cusuario_modif";
                        columnas[25] = "fultim_modif";
                        columnas[26] = "finicio_alta";
                        columnas[27] = "morigen";
                        columnas[28] = "nplazo_cheque";
                        columnas[29] = "ffinal_alta";
                        columnas[30] = "xnombre";
                        columnas[31] = "cod_zona";
                        columnas[32] = "xdesc_zona";
                        columnas[33] = "xnomb_vendedor";
                        columnas[34] = "xdesc_canal";
                        columnas[35] = "xdesc_ruta";
                        columnas[36] = "cod_merca";
                        columnas[37] = "xdesc_merca";
                        columnas[38] = "cant_cajas";
                        columnas[39] = "cant_unid";
                        columnas[40] = "cajas_bonif";
                        columnas[41] = "unid_bonif";
                        columnas[42] = "iexentas";
                        columnas[43] = "igravadas";
                        columnas[44] = "impuestos";
                        columnas[45] = "pdesc";
                        columnas[46] = "xdesc";
                        
                       lista = listadoPedidosFacade.generarListadoPedidosExcel(fechaPedidoDesde, 
                                                                               fechaPedidoHasta, 
                                                                               vendedor, 
                                                                               canal, 
                                                                               zona, 
                                                                               nroPedidoDesde, 
                                                                               nroPedidoHasta, 
                                                                               seleccionFecha, 
                                                                               seleccionTipo, 
                                                                               conDetalle);
                       rep.exportarExcel(columnas, lista, "rdetpedidos");
                        
                    } else { //sin detalle
                        List<Object[]> lista = new ArrayList<>();
                        String[] columnas = new String[36];

                        columnas[0] = "cod_empr";
                        columnas[1] = "nro_pedido";
                        columnas[2] = "ctipo_docum";
                        columnas[3] = "ctipo_vta";
                        columnas[4] = "cod_vendedor";
                        columnas[5] = "cod_depo_envio";
                        columnas[6] = "cod_entregador";
                        columnas[7] = "cod_canal";
                        columnas[8] = "cod_ruta";
                        columnas[9] = "cod_cliente";
                        columnas[10] = "cod_depo";
                        columnas[11] = "fpedido";
                        columnas[12] = "npeso_acum";
                        columnas[13] = "fanul";
                        columnas[14] = "ffactur";
                        columnas[15] = "fenvio";
                        columnas[16] = "tgravadas";
                        columnas[17] = "texentas";
                        columnas[18] = "timpuestos";
                        columnas[19] = "tdescuentos";
                        columnas[20] = "ttotal";
                        columnas[21] = "mestado";
                        columnas[22] = "cusuario";
                        columnas[23] = "falta";
                        columnas[24] = "cusuario_modif";
                        columnas[25] = "fultim_modif";
                        columnas[26] = "finicio_alta";
                        columnas[27] = "morigen";
                        columnas[28] = "nplazo_cheque";
                        columnas[29] = "ffinal_alta";
                        columnas[30] = "xnombre";
                        columnas[31] = "cod_zona";
                        columnas[32] = "xdesc_zona";
                        columnas[33] = "xnomb_vendedor";
                        columnas[34] = "xdesc_canal";
                        columnas[35] = "xdesc_ruta";
                        
                       lista = listadoPedidosFacade.generarListadoPedidosExcel(fechaPedidoDesde, 
                                                                               fechaPedidoHasta, 
                                                                               vendedor, 
                                                                               canal, 
                                                                               zona, 
                                                                               nroPedidoDesde, 
                                                                               nroPedidoHasta, 
                                                                               seleccionFecha, 
                                                                               seleccionTipo, 
                                                                               conDetalle);
                       rep.exportarExcel(columnas, lista, "rlispedidos");
                    }
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Error al generar archivo."));
            }
        }
        
    }
    private Boolean validar(){
        if (fechaPedidoDesde == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha desde.", "Debe ingresar fecha desde."));            
            return false;
        }else{
            if (fechaPedidoHasta == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha hasta.", "Debe ingresar fecha hasta."));            
                return false;
            }else{
                if (nroPedidoDesde == 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar un número de pedido desde.", "Debe ingresar un número de pedido desde."));            
                    return false;
                }else{
                    if (nroPedidoHasta == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar un número de pedido hasta.", "Debe ingresar un número de pedido hasta."));            
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private String armarSql(Date fechaPedidoDesde, 
                            Date fechaPedidoHasta, 
                            Empleados vendedor,
                            CanalesVenta canal,
                            Zonas zona,
                            long nroPedidoDesde,
                            long nroPedidoHasta,
                            String seleccionFecha,
                            String seleccionTipo,
                            Boolean conDetalle){
        String sql = ";WITH lipedidos AS(";
        if (!conDetalle){
            sql += "SELECT p.*, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.cod_empleado as codigo_empleado, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal, r.xdesc as xdesc_ruta" +
                " FROM pedidos p, clientes c, zonas z, empleados e, canales_venta l, rutas r" +
                " WHERE p.cod_empr = 2" +
                " AND p.cod_cliente = c.cod_cliente" +
                " AND p.cod_ruta = r.cod_ruta" +
                " AND r.cod_zona = z.cod_zona" +
                " AND p.cod_canal = l.cod_canal" +
                " AND p.cod_vendedor = e.cod_empleado" +
                " AND p.nro_pedido BETWEEN " + nroPedidoDesde + " AND " + nroPedidoHasta ;
        }else{
            sql += "SELECT p.*,c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.cod_empleado as codigo_empleado, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal," +
                " r.xdesc as xdesc_ruta, d.cod_merca, d.xdesc as xdesc_merca, d.cant_cajas, d.cant_unid, d.cajas_bonif, d.unid_bonif," +
                " d.iexentas, d.igravadas, d.impuestos, d.pdesc, d.xdesc" +
                " FROM pedidos p, clientes c, zonas z, empleados e, canales_venta l, rutas r, pedidos_det d" +
                " WHERE p.cod_empr = 2" +
                " AND d.cod_empr= 2" +
                " AND p.cod_cliente = c.cod_cliente" +
                " AND p.cod_ruta = r.cod_ruta" +
                " AND r.cod_zona = z.cod_zona" +
                " AND p.nro_pedido = d.nro_pedido" +
                " AND p.cod_canal = l.cod_canal" +
                " AND p.cod_vendedor = e.cod_empleado" +
                " AND p.nro_pedido BETWEEN " + nroPedidoDesde + " AND " + nroPedidoHasta;
        }
        
        if ("FP".equals(seleccionFecha)) {
            sql += " AND p.fpedido BETWEEN '" + DateUtil.dateToString(fechaPedidoDesde) + "' AND '" + DateUtil.dateToString(fechaPedidoHasta) + "'";
        }else{
            sql += " AND p.falta BETWEEN '" + DateUtil.dateToString(fechaPedidoDesde) + "' AND '" + DateUtil.dateToString(fechaPedidoHasta) + " 23:59:00'";
        }
        
        if (vendedor != null) {
            sql += " AND p.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado();
        }
        
        if (zona != null) {
            sql += " AND r.cod_zona = " + zona.getZonasPK().getCodZona();
        }
        
        if (canal != null) {
            sql += " AND p.cod_canal = " + canal.getCodCanal();
        }
        
        if ("AU".equals(seleccionTipo)) {
            sql += " AND morigen ='P'";
        }else{
            if ("MA".equals(seleccionTipo)){
                sql += " AND morigen = 'C'";
            }
        }
        
        sql += ") SELECT DISTINCT codigo_empleado, xnomb_vendedor FROM lipedidos";
        
        /*//si se discrimina por vendedor
        if (vendedor != null){
            sql += " ORDER BY p.cod_vendedor, p.fpedido, p.nro_pedido"; 
        }*/
        
        return sql;
    }
    
    private String armarSqlDetalle(Date fechaPedidoDesde, 
                            Date fechaPedidoHasta, 
                            Empleados vendedor,
                            CanalesVenta canal,
                            Zonas zona,
                            long nroPedidoDesde,
                            long nroPedidoHasta,
                            String seleccionFecha,
                            String seleccionTipo,
                            Boolean conDetalle){
        String sql = "";
        if (!conDetalle){
            sql += "SELECT p.*, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.cod_empleado as codigo_empleado, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal, r.xdesc as xdesc_ruta" +
                " FROM pedidos p, clientes c, zonas z, empleados e, canales_venta l, rutas r" +
                " WHERE p.cod_empr = 2" +
                " AND p.cod_cliente = c.cod_cliente" +
                " AND p.cod_ruta = r.cod_ruta" +
                " AND r.cod_zona = z.cod_zona" +
                " AND p.cod_canal = l.cod_canal" +
                " AND p.cod_vendedor = e.cod_empleado" +
                " AND p.nro_pedido BETWEEN " + nroPedidoDesde + " AND " + nroPedidoHasta ;
        }else{
            sql += "SELECT p.*,c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.cod_empleado as codigo_empleado, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal," +
                " r.xdesc as xdesc_ruta, d.cod_merca, d.xdesc as xdesc_merca, d.cant_cajas, d.cant_unid, d.cajas_bonif, d.unid_bonif," +
                " d.iexentas, d.igravadas, d.impuestos, d.pdesc, d.xdesc" +
                " FROM pedidos p, clientes c, zonas z, empleados e, canales_venta l, rutas r, pedidos_det d" +
                " WHERE p.cod_empr = 2" +
                " AND d.cod_empr= 2" +
                " AND p.cod_cliente = c.cod_cliente" +
                " AND p.cod_ruta = r.cod_ruta" +
                " AND r.cod_zona = z.cod_zona" +
                " AND p.nro_pedido = d.nro_pedido" +
                " AND p.cod_canal = l.cod_canal" +
                " AND p.cod_vendedor = e.cod_empleado" +
                " AND p.nro_pedido BETWEEN " + nroPedidoDesde + " AND " + nroPedidoHasta;
        }
        
        if ("FP".equals(seleccionFecha)) {
            sql += " AND p.fpedido BETWEEN '" + DateUtil.dateToString(fechaPedidoDesde) + "' AND '" + DateUtil.dateToString(fechaPedidoHasta) + "'";
        }else{
            sql += " AND p.falta BETWEEN '" + DateUtil.dateToString(fechaPedidoDesde) + "' AND '" + DateUtil.dateToString(fechaPedidoHasta) + " 23:59:00'";
        }
        
        if (vendedor != null) {
            sql += " AND p.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado();
        }
        
        if (zona != null) {
            sql += " AND r.cod_zona = " + zona.getZonasPK().getCodZona();
        }
        
        if (canal != null) {
            sql += " AND p.cod_canal = " + canal.getCodCanal();
        }
        
        if ("AU".equals(seleccionTipo)) {
            sql += " AND morigen ='P'";
        }else{
            if ("MA".equals(seleccionTipo)){
                sql += " AND morigen = 'C'";
            }
        }
        
        sql += " AND e.cod_empleado = ";
        
        /*//si se discrimina por vendedor
        if (vendedor != null){
            sql += " ORDER BY p.cod_vendedor, p.fpedido, p.nro_pedido"; 
        }*/
        
        return sql;
    }
    
    public Date getFechaPedidoDesde() {
        return fechaPedidoDesde;
    }

    public void setFechaPedidoDesde(Date fechaPedidoDesde) {
        this.fechaPedidoDesde = fechaPedidoDesde;
    }

    public Date getFechaPedidoHasta() {
        return fechaPedidoHasta;
    }

    public void setFechaPedidoHasta(Date fechaPedidoHasta) {
        this.fechaPedidoHasta = fechaPedidoHasta;
    }

    public Empleados getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleados vendedor) {
        if (vendedor != null) {
            this.vendedor = empleadosFacade.getNombreEmpleado(Integer.parseInt(vendedor.getEmpleadosPK().getCodEmpleado() + ""));
        }else{
            this.vendedor = vendedor;
        }
    }

    public CanalesVenta getCanal() {
        return canal;
    }

    public void setCanal(CanalesVenta canal) {
        if (canal != null) {
            this.canal = canalesVentaFacade.getCanalVentaPorCodigo(canal.getCodCanal());
        } else {
            this.canal = canal;
        }
    }

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        if (canal != null) {
            this.zona = zonasFacade.buscarPorCodigo(zona.getZonasPK().getCodZona());
        } else {
            this.zona = zona;
        }
    }

    public long getNroPedidoDesde() {
        return nroPedidoDesde;
    }

    public void setNroPedidoDesde(long nroPedidoDesde) {
        this.nroPedidoDesde = nroPedidoDesde;
    }

    public long getNroPedidoHasta() {
        return nroPedidoHasta;
    }

    public void setNroPedidoHasta(long nroPedidoHasta) {
        this.nroPedidoHasta = nroPedidoHasta;
    }

    public String getSeleccionFecha() {
        return seleccionFecha;
    }

    public void setSeleccionFecha(String seleccionFecha) {
        this.seleccionFecha = seleccionFecha;
    }

    public String getSeleccionTipo() {
        return seleccionTipo;
    }

    public void setSeleccionTipo(String seleccionTipo) {
        this.seleccionTipo = seleccionTipo;
    }

    public Boolean getConDetalle() {
        return conDetalle;
    }

    public void setConDetalle(Boolean conDetalle) {
        this.conDetalle = conDetalle;
    }    
}