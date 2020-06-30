/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import entidad.Empleados;
import entidad.CanalesVenta;
import entidad.Clientes;
import entidad.Zonas;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;
import util.StringUtil;

/**
 *
 * @author jvera
 * 
 */
@Stateless
public class ListadoPedidosClientesFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    protected EntityManager getEntityManager() {
        return em;
    }

    public ListadoPedidosClientesFacade() {
        
    }
    
    //metodo para armar archivo Excel
    public List<Object[]> generarListadoPedidosExcel(Date fechaPedidoDesde, 
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
        String sql = "";
        if (!conDetalle){
            sql += "SELECT p.*, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal, r.xdesc as xdesc_ruta" +
                " FROM pedidos p, clientes c, zonas z, empleados e, canales_venta l, rutas r" +
                " WHERE p.cod_empr = 2" +
                " AND p.cod_cliente = c.cod_cliente" +
                " AND p.cod_ruta = r.cod_ruta" +
                " AND r.cod_zona = z.cod_zona" +
                " AND p.cod_canal = l.cod_canal" +
                " AND p.cod_vendedor = e.cod_empleado" +
                " AND p.nro_pedido BETWEEN " + nroPedidoDesde + " AND " + nroPedidoHasta ;
        }else{
            sql += "SELECT p.*,c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, e.xnombre as xnomb_vendedor, l.xdesc as xdesc_canal," +
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
        
        //si se discrimina por vendedor
        if (vendedor != null){
            sql += " ORDER BY p.cod_cliente, p.fpedido, p.nro_pedido"; 
        }
        
        Query q = em.createNativeQuery(sql);      
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        
       return resultados;
    }
}