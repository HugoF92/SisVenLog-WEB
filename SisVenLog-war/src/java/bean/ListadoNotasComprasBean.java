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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.LlamarReportes;
import dao.ConceptosDocumentosFacade;
import dao.ListadoNotasComprasFacade;
import dao.ProveedoresFacade;
import dao.SublineasFacade;
import dao.TiposDocumentosFacade;
import entidad.Clientes;
import entidad.ConceptosDocumentos;
import entidad.Proveedores;
import entidad.Sublineas;
import entidad.TiposDocumentos;
import javax.ejb.EJB;
import util.DateUtil;
import util.StringUtil;
/**
 *
 * @author jvera
 */

@ManagedBean
@SessionScoped
public class ListadoNotasComprasBean implements Serializable{
    private Date fechaDesde;
    private Date fechaHasta;
    private Proveedores proveedor;
    private TiposDocumentos tipoDocumento;           
    private ConceptosDocumentos concepto;
    private Sublineas sublinea;
    private String tipoListado;
    private Boolean conDetalle;
    
    @EJB
    private ProveedoresFacade proveedoresFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private ConceptosDocumentosFacade conceptosDocumentosFacade;
    
    @EJB
    private SublineasFacade sublineasFacade;
    
    @EJB
    private ListadoNotasComprasFacade listadoNotasComprasFacade;
    
    @PostConstruct
    public void instanciar(){
        limpiarFormulario();
        setConDetalle(false);
    }
    
    public void limpiarFormulario(){
        fechaDesde = null;
        fechaHasta = null;
    }
    
    public void ejecutar(String tipo) {        
        if (validar()) {
            try {
                LlamarReportes rep = new LlamarReportes();
                listadoNotasComprasFacade.prepararDatosListadoNotasCompras(fechaDesde, 
                                                                           fechaHasta, 
                                                                           proveedor, 
                                                                           tipoDocumento, 
                                                                           concepto, 
                                                                           sublinea, 
                                                                           tipoListado);
                if (tipo.equals("VIST")) {
                    String sql, usuImprime, filename, nombreRepo;
                    if ("resumen".equals(tipoListado)){
                        nombreRepo = "rnotascom3";
                        filename = "rnotascom3";
                        sql = "select distinct nctipo_docum, xdesc_docum from CurFin2 order by nctipo_docum";
                        usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                        rep.listadoNotasCompras(sql, 
                                                fechaDesde, 
                                                fechaHasta, 
                                                proveedor, 
                                                tipoDocumento, 
                                                concepto, 
                                                sublinea, 
                                                tipoListado, 
                                                usuImprime, 
                                                tipo, 
                                                nombreRepo, 
                                                filename);
                    }else {
                        nombreRepo = "rnotascomdet";
                        filename = "rnotascomdet";
                        sql = "select distinct fdocum from Curfin2 order by fdocum";
                        usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                        rep.listadoNotasCompras(sql, 
                                                fechaDesde, 
                                                fechaHasta, 
                                                proveedor, 
                                                tipoDocumento, 
                                                concepto, 
                                                sublinea, 
                                                tipoListado, 
                                                usuImprime, 
                                                tipo, 
                                                nombreRepo, 
                                                filename);
                    }
                }
                        /*else { //Excel
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
                        
                       lista = listadoNotasComprasFacade.generarListadoPedidosExcel(fechaDesde, 
                                                                               fechaHasta, 
                                                                               proveedor,
                                                                               tipoDocumento,
                                                                               concepto,
                                                                               sublinea,
                                                                               tipoListado,
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
                        
                       lista = listadoNotasComprasFacade.generarListadoPedidosExcel(fechaDesde, 
                                                                               fechaHasta, 
                                                                               proveedor,
                                                                               tipoDocumento,
                                                                               concepto,
                                                                               sublinea,
                                                                               tipoListado,
                                                                               conDetalle);
                       rep.exportarExcel(columnas, lista, "rlispedidos");
                    }
                }*/
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Error al generar archivo."));
            }
        }
        
    }
    private Boolean validar(){
        if (fechaDesde == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha desde.", "Debe ingresar fecha desde."));            
            return false;
        }else{
            if (fechaHasta == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe ingresar fecha hasta.", "Debe ingresar fecha hasta."));            
                return false;
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
                            Boolean conDetalle,
                            List<Clientes> clientesSeleccionados){
        String sql = ";WITH lipedidos AS(";
        if (conDetalle) {
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
        } else {
            sql += "SELECT p.*, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.cod_empleado as codigo_empleado, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal, r.xdesc as xdesc_ruta" +
            " FROM pedidos p, clientes c, zonas z, empleados e, canales_venta l, rutas r" +
            " WHERE p.cod_empr = 2" +
            " AND p.cod_cliente = c.cod_cliente" +
            " AND p.cod_ruta = r.cod_ruta" +
            " AND r.cod_zona = z.cod_zona" +
            " AND p.cod_canal = l.cod_canal" +
            " AND p.cod_vendedor = e.cod_empleado" +
            " AND p.nro_pedido BETWEEN " + nroPedidoDesde + " AND " + nroPedidoHasta ;
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
            sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
        }
        
        if (canal != null) {
            sql += " AND p.cod_canal = '" + canal.getCodCanal() + "'";
        }
        
        //JLVC 07-05-2020 agregamos el filtro de los clientes seleccionados
        if (!clientesSeleccionados.isEmpty()) {
            sql += " AND p.cod_cliente IN (" + StringUtil.convertirListaAString(clientesSeleccionados) + ")"; 
        }
        
        if ("AU".equals(seleccionTipo)) {
            sql += " AND morigen ='P'";
        }else{
            if ("MA".equals(seleccionTipo)){
                sql += " AND morigen = 'C'";
            }
        }
        
        sql += ") SELECT DISTINCT cod_cliente, xnombre FROM lipedidos";
        
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
        if (conDetalle) {
            sql += "SELECT p.*,c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.cod_empleado as codigo_empleado, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal," +
                " r.xdesc as xdesc_ruta" +
                " FROM pedidos p, clientes c, zonas z, empleados e, canales_venta l, rutas r" +
                " WHERE p.cod_empr = 2" +
                " AND p.cod_cliente = c.cod_cliente" +
                " AND p.cod_ruta = r.cod_ruta" +
                " AND r.cod_zona = z.cod_zona" +
                " AND p.cod_canal = l.cod_canal" +
                " AND p.cod_vendedor = e.cod_empleado" +
                " AND p.nro_pedido BETWEEN " + nroPedidoDesde + " AND " + nroPedidoHasta;
        } else {
            sql += "SELECT p.*, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.cod_empleado as codigo_empleado, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal, r.xdesc as xdesc_ruta" +
                " FROM pedidos p, clientes c, zonas z, empleados e, canales_venta l, rutas r" +
                " WHERE p.cod_empr = 2" +
                " AND p.cod_cliente = c.cod_cliente" +
                " AND p.cod_ruta = r.cod_ruta" +
                " AND r.cod_zona = z.cod_zona" +
                " AND p.cod_canal = l.cod_canal" +
                " AND p.cod_vendedor = e.cod_empleado" +
                " AND p.nro_pedido BETWEEN " + nroPedidoDesde + " AND " + nroPedidoHasta ;   
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
            sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
        }
        
        if (canal != null) {
            sql += " AND p.cod_canal = '" + canal.getCodCanal() + "'";
        }
        
        if ("AU".equals(seleccionTipo)) {
            sql += " AND morigen ='P'";
        }else{
            if ("MA".equals(seleccionTipo)){
                sql += " AND morigen = 'C'";
            }
        }
        
        sql += " AND p.cod_cliente = ";
        return sql;
    }
    private String armarDetalleDet(Date fechaPedidoDesde, 
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

        sql += "SELECT p.*,c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.cod_empleado as codigo_empleado, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal," +
            " r.xdesc as xdesc_ruta, d.nro_pedido, d.cod_merca, d.xdesc as xdesc_merca, d.cant_cajas, d.cant_unid, d.cajas_bonif, d.unid_bonif," +
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
        
        if ("FP".equals(seleccionFecha)) {
            sql += " AND p.fpedido BETWEEN '" + DateUtil.dateToString(fechaPedidoDesde) + "' AND '" + DateUtil.dateToString(fechaPedidoHasta) + "'";
        }else{
            sql += " AND p.falta BETWEEN '" + DateUtil.dateToString(fechaPedidoDesde) + "' AND '" + DateUtil.dateToString(fechaPedidoHasta) + " 23:59:00'";
        }
        
        if (vendedor != null) {
            sql += " AND p.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado();
        }
        
        if (zona != null) {
            sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
        }
        
        if (canal != null) {
            sql += " AND p.cod_canal = '" + canal.getCodCanal() + "'";
        }
        
        if ("AU".equals(seleccionTipo)) {
            sql += " AND morigen ='P'";
        }else{
            if ("MA".equals(seleccionTipo)){
                sql += " AND morigen = 'C'";
            }
        }
        
        sql += " AND d.nro_pedido = ";
        
        /*//si se discrimina por vendedor
        if (vendedor != null){
            sql += " ORDER BY p.cod_vendedor, p.fpedido, p.nro_pedido"; 
        }*/
        
        return sql;
    }
    
    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        if (proveedor != null) {
            this.proveedor = proveedoresFacade.proveedorById(proveedor.getCodProveed());
        }else{
            this.proveedor = proveedor;
        }
    }

    public TiposDocumentos getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentos tipoDocumento) {
        if (tipoDocumento != null) {
            this.tipoDocumento = tiposDocumentosFacade.getTipoDocumentoById(tipoDocumento.getCtipoDocum());
        }
        this.tipoDocumento = tipoDocumento;
    }

    public ConceptosDocumentos getConcepto() {
        return concepto;
    }

    public void setConcepto(ConceptosDocumentos concepto) {
        this.concepto = concepto;
    }

    public Sublineas getSublinea() {
        return sublinea;
    }

    public void setSublinea(Sublineas sublinea) {
        this.sublinea = sublinea;
    }

    public String getTipoListado() {
        return tipoListado;
    }

    public void setTipoListado(String tipoListado) {
        this.tipoListado = tipoListado;
    }
    
    public Boolean getConDetalle() {
        return conDetalle;
    }

    public void setConDetalle(Boolean conDetalle) {
        this.conDetalle = conDetalle;
    }
}