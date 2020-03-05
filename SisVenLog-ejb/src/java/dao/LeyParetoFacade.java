/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import entidad.CanalesVenta;
import entidad.Lineas;
import entidad.TiposClientes;
import entidad.Zonas;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;

/**
 *
 * @author jvera
 * 
 */
@Stateless
public class LeyParetoFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    protected EntityManager getEntityManager() {
        return em;
    }

    public LeyParetoFacade() {
        
    }
    
    //metodo para armar archivo Excel
    public List<Object[]> generarLeyParetoExcel(Date fechaFacturacionDesde, 
                                                Date fechaFacturacionHasta, 
                                                Lineas linea, 
                                                TiposClientes tipoCliente, 
                                                Zonas zonas, 
                                                CanalesVenta canalesVenta, 
                                                String discriminar){
        String sql = "";
        if ("PC".equals(discriminar)) {
            sql += "SELECT d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona," +
                " SUM(d.igravadas+d.iexentas+d.impuestos) as itotal" +
                " FROM movimientos_merca d, clientes c, zonas z, mercaderias m, rutas r, merca_canales mc, sublineas s, lineas l" +
                " WHERE d.cod_empr = 2" +
                " AND r.cod_zona = z.cod_zona" +
                " AND c.cod_ruta = r.cod_ruta" +
                " AND d.cod_empr = m.cod_empr" +
                " AND d.cod_merca = m.cod_merca" +
                " AND d.cod_cliente  = c.cod_cliente" +
                " AND m.cod_merca = mc.cod_merca" +
                " AND d.ctipo_docum IN ('FCO','FCR','CPV','NCV','NDV')" +
                " AND d.fmovim BETWEEN '" + DateUtil.dateToString(fechaFacturacionDesde) + "' AND '" + DateUtil.dateToString(fechaFacturacionHasta) + "'";
            if (tipoCliente != null){
                sql += " AND c.ctipo_cliente  = '" + tipoCliente.getCtipoCliente() + "'";
            }
            if (zonas != null) {
                sql += " AND z.cod_zona = '" + zonas.getZonasPK().getCodZona() + "'";
            }
            if (canalesVenta != null) {
                sql += " AND mc.cod_canal = '" + canalesVenta.getCodCanal() + "'";
            }
            if (linea != null) {
                sql += " AND l.cod_linea = " + linea.getCodLinea();
            }
            sql += " GROUP BY d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc";
        }else{ //discriminar por linea
            sql += "SELECT d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona," +
                " l.cod_linea, l.xdesc as xdesc_linea, d.cod_merca," +
                " SUM(d.igravadas+d.iexentas+d.impuestos) as itotal" +
                " FROM movimientos_merca d, clientes c, zonas z, sublineas s, mercaderias m," +
                " lineas l, rutas r , merca_canales mc" +
                " WHERE d.cod_empr = 2" +
                " AND r.cod_zona = z.cod_zona" +
                " AND c.cod_ruta = r.cod_ruta" +
                " AND d.cod_empr = m.cod_empr" +
                " AND d.cod_merca = m.cod_merca" +
                " AND m.cod_sublinea = s.cod_sublinea" +
                " AND s.cod_linea = l.cod_linea" +
                " AND d.cod_cliente = c.cod_cliente" +
                " AND m.cod_merca = mc.cod_merca" +
                " AND d.ctipo_docum IN ('FCO','FCR','CPV','NCV','NDV')" +
                " AND d.fmovim BETWEEN '" + DateUtil.dateToString(fechaFacturacionDesde) + "' AND '" + DateUtil.dateToString(fechaFacturacionHasta) + "'";
            if (tipoCliente != null){
                sql += " AND c.ctipo_cliente  = '" + tipoCliente.getCtipoCliente() + "'";
            }
            if (zonas != null) {
                sql += " AND z.cod_zona = '" + zonas.getZonasPK().getCodZona() + "'";
            }
            if (canalesVenta != null) {
                sql += " AND mc.cod_canal = '" + canalesVenta.getCodCanal() + "'";
            }
            if (linea != null) {
                sql += " AND l.cod_linea = " + linea.getCodLinea();
            }
            sql += " GROUP BY d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc, l.cod_linea, l.xdesc, d.cod_merca";
        }
        
        Query q = em.createNativeQuery(sql);      
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        
       return resultados;
    }
}