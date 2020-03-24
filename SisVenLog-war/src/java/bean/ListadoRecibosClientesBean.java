/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;


import dao.ClientesFacade;
import dao.RecibosFacade;
import dao.ZonasFacade;
import entidad.Clientes;
import entidad.Zonas;
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
import util.StringUtil;


/**
 *
 * @author jvera
 */

@ManagedBean
@SessionScoped
public class ListadoRecibosClientesBean implements Serializable{
    private Date fechaReciboDesde;
    private Date fechaReciboHasta;
    private List<Clientes> listadoClientes;
    private List<Clientes> listadoClientesSeleccionados;
    private String discriminar;
    private Boolean conDetalle;
    private long nroReciboDesde;
    private long nroReciboHasta;
    private String clientesRepo;
    private Zonas zona;
    private String zonaDes;
    private Boolean todosClientes;
    private Boolean seleccionarClientes;
    
            
    @EJB
    private ClientesFacade clientesFacade;
    
    @EJB
    private RecibosFacade recibosFacade;
    
    @EJB
    private ZonasFacade zonasFacade;
    
    @PostConstruct
    public void instanciar(){
        limpiarFormulario();
        listadoClientes = listarClientes();
        setDiscriminar("ND");
        setTodosClientes(true);
    }
    
    public void limpiarFormulario(){
        
        listadoClientes = new ArrayList<>();
        fechaReciboDesde = null;
        fechaReciboHasta = null;
        nroReciboDesde = 0;
        nroReciboHasta = 0;
        zona = null;
    }
    
    private List<Clientes> listarClientes(){
        List<Clientes> clientes = new ArrayList<>();
        try {    
            clientes = clientesFacade.findAll();
        } catch (Exception e) {
            System.out.println("bean.ListadoRecibosClientesBean.listarClientes()" + e.getMessage());
        }
        return clientes;     
    }
    
    private String armarSqlSinDetalle(String fechaReciboDesde, 
                                                  String fechaReciboHasta, 
                                                  long nroReciboDesde, 
                                                  long nroReciboHasta, 
                                                  List<Clientes> listaCodCliente,
                                                  Zonas zona){
        String sql = "";
        if ("PC".equals(discriminar)) {
           sql = ";WITH principalCTE " +
            "AS " +
            "(" +
            " SELECT r.*,  t.cod_cliente as cod_cliente2, t.xnombre as xnombre, t.xnombre as xnombre2" +
            " FROM recibos r , clientes t, rutas a" +
            " WHERE r.cod_empr = 2" +
            " AND r.cod_cliente = t.cod_cliente" +
            " AND t.cod_ruta = a.cod_ruta" +
            " AND r.fanul IS NULL" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
                   
            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
            if (zona != null) {
                sql += " AND a.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sql += ") " +
            "SELECT DISTINCT cod_cliente from principalCTE ORDER BY cod_cliente;";
            
        }else{ //no discriminar por cliente
            sql = ";WITH principalCTE" + 
            " AS(SELECT r.*,  t.cod_cliente as cod_cliente2, t.xnombre as xnombre, t.xnombre as xnombre2" +
            " FROM recibos r , clientes t, rutas a" + 
            " WHERE r.cod_empr = 2 AND r.cod_cliente = t.cod_cliente AND t.cod_ruta = a.cod_ruta AND r.fanul IS NULL" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
             if (zona != null) {
                sql += " AND a.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
           sql += " ) SELECT * from principalCTE ORDER BY nrecibo;";
        }
        return sql;
    }
    
    private String armarSqlConDetalle(String fechaReciboDesde, 
                                                  String fechaReciboHasta, 
                                                  long nroReciboDesde, 
                                                  long nroReciboHasta, 
                                                  List<Clientes> listaCodCliente,
                                                  Zonas zona){
        String sql = "";
        if ("ND".equals(discriminar)) {
            sql += ";WITH principalCTE" +
            " AS (" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.ndocum, '' as xdesc_banco," +
            " r.fanul, 'F' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.itotal" +
            " FROM recibos r , recibos_det d, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND r.fanul IS NULL" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
             if (zona != null) {
                sql += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sql += " UNION ALL" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum, b.xdesc as xdesc_banco," +
            " r.fanul, 'C' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.ipagado as itotal" +
            " FROM recibos r , recibos_cheques d, bancos b, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND d.cod_banco = b.cod_banco" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.fanul IS NULL" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta + 
            " AND r.icheques > 0";

            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
             if (zona != null) {
                sql += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sql += ")" +
            " SELECT DISTINCT nrecibo, frecibo, mestado, iefectivo, icheques, iretencion, irecibo, xnombre2, cod_cliente2, xobs FROM principalCTE order by nrecibo;";
        }else{ // discriminar por cliente
            sql += ";WITH principalCTE" +
            " AS (" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.ndocum, '' as xdesc_banco," +
            " r.fanul, 'F' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.itotal" +
            " FROM recibos r , recibos_det d, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND r.fanul IS NULL" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
             if (zona != null) {
                sql += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sql += " UNION ALL" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum, b.xdesc as xdesc_banco," +
            " r.fanul, 'C' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.ipagado as itotal" +
            " FROM recibos r , recibos_cheques d, bancos b, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND d.cod_banco = b.cod_banco" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.fanul IS NULL" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta + 
            " AND r.icheques > 0";

            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
             if (zona != null) {
                sql += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sql += ")" +
            " SELECT DISTINCT cod_cliente FROM principalCTE ORDER BY cod_cliente;";
        }
        return sql;
    }
    
    private String armarSqlDetalle(String fechaReciboDesde, 
                                                  String fechaReciboHasta, 
                                                  long nroReciboDesde, 
                                                  long nroReciboHasta, 
                                                  List<Clientes> listaCodCliente,
                                                  Zonas zona){
        String sqlDetalle = "";    
        if ("ND".equals(discriminar)) {
            sqlDetalle += ";WITH principalCTE" +
            " AS (" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.ndocum, '' as xdesc_banco," +
            " r.fanul, 'F' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.itotal" +
            " FROM recibos r , recibos_det d, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND r.fanul IS NULL" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sqlDetalle += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
            if (zona != null) {
                sqlDetalle += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sqlDetalle += " UNION ALL" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum, b.xdesc as xdesc_banco," +
            " r.fanul, 'C' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.ipagado as itotal" +
            " FROM recibos r , recibos_cheques d, bancos b, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND d.cod_banco = b.cod_banco" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.fanul IS NULL" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta + 
            " AND r.icheques > 0";

            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sqlDetalle += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
            if (zona != null) {
                sqlDetalle += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sqlDetalle += ") " +
            " SELECT DISTINCT * FROM principalCTE where nrecibo = ";           
        }else{  //discrimiar por cliente
            sqlDetalle += ";WITH principalCTE" +
            " AS (" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.ndocum, '' as xdesc_banco," +
            " r.fanul, 'F' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.itotal" +
            " FROM recibos r , recibos_det d, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND r.fanul IS NULL" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sqlDetalle += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
            if (zona != null) {
                sqlDetalle += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sqlDetalle += " UNION ALL" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum, b.xdesc as xdesc_banco," +
            " r.fanul, 'C' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.ipagado as itotal" +
            " FROM recibos r , recibos_cheques d, bancos b, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND d.cod_banco = b.cod_banco" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.fanul IS NULL" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta + 
            " AND r.icheques > 0";

            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sqlDetalle += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
            if (zona != null) {
                sqlDetalle += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sqlDetalle += ") " +
            " SELECT DISTINCT nrecibo, xnombre, cod_cliente, frecibo, mestado, iefectivo, icheques, iretencion, irecibo, xnombre2, cod_cliente2, xobs FROM principalCTE where cod_cliente = ";           
        }
        return sqlDetalle;
    }
    
    private String armarSqlDetalleRecibos(String fechaReciboDesde, 
                                                  String fechaReciboHasta, 
                                                  long nroReciboDesde, 
                                                  long nroReciboHasta, 
                                                  List<Clientes> listaCodCliente,
                                                  Zonas zona){
        String sqlDetalleRec = "";    
        if ("ND".equals(discriminar)) {  //no existe corte de control por cliente
            sqlDetalleRec = null;
        }else{  //discrimiar por cliente
            sqlDetalleRec += ";WITH principalCTE" +
            " AS (" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, d.ctipo_docum, d.ndocum, '' as xdesc_banco," +
            " r.fanul, 'F' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.itotal" +
            " FROM recibos r , recibos_det d, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND r.fanul IS NULL" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sqlDetalleRec += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
            if (zona != null) {
                sqlDetalleRec += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sqlDetalleRec += " UNION ALL" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion," +
            " r.icheques, r.xobs, r.mestado, c.xnombre, '' as ctipo_docum, nro_cheque as ndocum, b.xdesc as xdesc_banco," +
            " r.fanul, 'C' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.ipagado as itotal" +
            " FROM recibos r , recibos_cheques d, bancos b, clientes c, rutas u" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = u.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND d.cod_banco = b.cod_banco" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.fanul IS NULL" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta + 
            " AND r.icheques > 0";

            if (todosClientes) {
                clientesRepo = "TODOS";
            }else{
                if (!listaCodCliente.isEmpty()) {
                    sqlDetalleRec += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")"; 
                    clientesRepo = StringUtil.convertirListaAString(listaCodCliente);
                }
            }
            if (zona != null) {
                sqlDetalleRec += " AND u.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
                zonaDes = zona.getXdesc();
            }else{
                zonaDes = "TODAS";
            }
            sqlDetalleRec += ") " +
            " SELECT DISTINCT * FROM principalCTE where nrecibo = ";           
        }
        return sqlDetalleRec;
    }
    
    public void ejecutar(String tipo) {        
        if (validar()) {
            try {
                LlamarReportes rep = new LlamarReportes();
                if (tipo.equals("VIST")) {
                    if (!conDetalle) {
                        String nombreRepo = "ND".equals(discriminar) ? "reciboFacND" : "reciboFac";
                        rep.reporteLiRecibos(
                                armarSqlSinDetalle(DateUtil.dateToString(fechaReciboDesde), DateUtil.dateToString(fechaReciboHasta), nroReciboDesde, nroReciboHasta, listadoClientesSeleccionados, zona),
                                fechaReciboDesde,
                                fechaReciboHasta,
                                nroReciboDesde,
                                nroReciboHasta,
                                clientesRepo,
                                zonaDes,
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString(),
                                tipo,
                                nombreRepo,
                                "Rrecibos",
                                null,
                                null);
                    } else {
                        String nombreRepoDet = "ND".equals(discriminar) ? "reciboFacDetND" : "reciboFacDetPC";
                        rep.reporteLiRecibos(
                        armarSqlConDetalle(DateUtil.dateToString(fechaReciboDesde), DateUtil.dateToString(fechaReciboHasta), nroReciboDesde, nroReciboHasta, listadoClientesSeleccionados, zona),
                        fechaReciboDesde,
                        fechaReciboHasta,
                        nroReciboDesde,
                        nroReciboHasta,
                        clientesRepo,
                        zonaDes,
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString(),
                        tipo,
                        nombreRepoDet,
                        "RrecibosDet",
                        armarSqlDetalle(DateUtil.dateToString(fechaReciboDesde), DateUtil.dateToString(fechaReciboHasta), nroReciboDesde, nroReciboHasta, listadoClientesSeleccionados, zona),
                        armarSqlDetalleRecibos(DateUtil.dateToString(fechaReciboDesde), DateUtil.dateToString(fechaReciboHasta), nroReciboDesde, nroReciboHasta, listadoClientesSeleccionados, zona));
                    }

                } else {

                    if (conDetalle) {
                        List<Object[]> lista = new ArrayList<>();
                        String[] columnas = new String[18];

                        columnas[0] = "r.nrecibo";
                        columnas[1] = "r.cod_cliente";
                        columnas[2] = "r.frecibo";
                        columnas[3] = "r.irecibo";
                        columnas[4] = "r.iefectivo";
                        columnas[5] = "r.iretencion";
                        columnas[6] = "r.icheques";
                        columnas[7] = "r.xobs";
                        columnas[8] = "r.mestado";
                        columnas[9] = "c.xnombre";
                        columnas[10] = "d.ctipo_docum";
                        columnas[11] = "d.ndocum";
                        columnas[12] = "xdesc_banco";
                        columnas[13] = "r.fanul";
                        columnas[14] = "tipodet";
                        columnas[15] = "cod_cliente2";
                        columnas[16] = "xnombre2";
                        columnas[17] = "d.itotal";

                        lista = recibosFacade.listaRecibosConDetalle(DateUtil.dateToString(fechaReciboDesde), DateUtil.dateToString(fechaReciboHasta), nroReciboDesde, nroReciboHasta, listadoClientesSeleccionados, zona, discriminar, todosClientes);
                        rep.exportarExcel(columnas, lista, "lirecibosDet");

                    } else {
                        List<Object[]> lista = new ArrayList<>();
                        String[] columnas = new String[11];

                        columnas[0] = "nrecibo";
                        columnas[1] = "cod_cliente";
                        columnas[2] = "frecibo";
                        columnas[3] = "irecibo";
                        columnas[4] = "iefectivo";
                        columnas[5] = "iretencion";
                        columnas[6] = "icheques";
                        columnas[7] = "xobs";
                        columnas[8] = "mestado";
                        columnas[9] = "cod_cliente2";
                        columnas[10] = "xnombre";

                        lista = recibosFacade.listaRecibosSinDetalle(DateUtil.dateToString(fechaReciboDesde), DateUtil.dateToString(fechaReciboHasta), nroReciboDesde, nroReciboHasta, listadoClientesSeleccionados, zona, discriminar, todosClientes);
                        rep.exportarExcel(columnas, lista, "lirecibos");
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
                if (nroReciboDesde == 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar un número de recibo desde.", "Debe ingresar un número de recibo desde."));            
                    return false;
                }else{
                    if (nroReciboHasta == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar un número de recibo hasta.", "Debe ingresar un número de recibo hasta."));            
                        return false;
                    }
                }
            }
        }
        return true;
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

    public List<Clientes> getListadoClientesSeleccionados() {
        if (listadoClientesSeleccionados == null) {
            listadoClientesSeleccionados = new ArrayList<>();
        }
        return listadoClientesSeleccionados;
    }

    public void setListadoClientesSeleccionados(List<Clientes> listadoClientesSeleccionados) {
        this.listadoClientesSeleccionados = listadoClientesSeleccionados;
    }

    
    public List<Clientes> getListadoClientes() {
        if (null == this.listadoClientes) {
            listadoClientes = new ArrayList<>();
        }
        return listadoClientes;
    }

    public void setListadoClientes(List<Clientes> listadoClientes) {
        this.listadoClientes = listadoClientes;
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

    public String getClientesRepo() {
        return clientesRepo;
    }

    public void setClientesRepo(String clientesRepo) {
        this.clientesRepo = clientesRepo;
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
    
    public String getZonaDes() {
        return zonaDes;
    }

    public void setZonaDes(String zonaDes) {
        this.zonaDes = zonaDes;
    }
    
    public Boolean getTodosClientes() {
        return todosClientes;
    }

    public void setTodosClientes(Boolean todosClientes) {
        this.todosClientes = todosClientes;
        /*if (this.todosClientes) {
            System.out.println("todosClientes: " + this.todosClientes);
            System.out.println("seleccionarClientes: " + this.seleccionarClientes);
        }*/
    }

    public Boolean getSeleccionarClientes() {
        return seleccionarClientes;
    }

    public void setSeleccionarClientes(Boolean seleccionarClientes) {
        this.seleccionarClientes = seleccionarClientes;
        /*if (this.seleccionarClientes) {
            System.out.println("seleccionarClientes: " + this.seleccionarClientes);
            System.out.println("todosClientes: " + this.todosClientes);
        }*/
    }
}