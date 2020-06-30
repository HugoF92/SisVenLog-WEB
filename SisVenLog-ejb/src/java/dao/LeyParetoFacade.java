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
    
    public void prepararDatosListadoPareto(Date fechaFacturacionDesde, 
                                                Date fechaFacturacionHasta, 
                                                Lineas linea, 
                                                TiposClientes tipoCliente, 
                                                Zonas zonas, 
                                                CanalesVenta canalesVenta, 
                                                String discriminar,
                                                String factorSeleccionado,
                                                Integer factor){
        
        Integer totalCli = 0;
        Integer kcliente = 0;
        
        String sql = "IF OBJECT_ID('tpareto') IS NOT NULL DROP TABLE tpareto;";
        
        if ("PC".equals(discriminar)) {
            //creamos y cargamos el cursor tpareto
            sql += " SELECT d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, SUM(d.igravadas+d.iexentas+d.impuestos) as itotal" +
            " INTO tpareto" +
            " FROM movimientos_merca d, clientes c, zonas z, mercaderias m, rutas r, merca_canales mc, sublineas s, lineas l" +
            " WHERE d.cod_empr = 2" +
            " AND r.cod_zona = z.cod_zona" +
            " AND c.cod_ruta = r.cod_ruta" +
            " AND d.cod_empr = m.cod_empr" +
            " AND d.cod_merca = m.cod_merca" +
            " AND d.cod_cliente = c.cod_cliente" +
            " AND m.cod_merca = mc.cod_merca" +
            " AND m.cod_sublinea = s.cod_sublinea" +
            " AND s.cod_linea = l.cod_linea" +
            " AND d.ctipo_docum IN ('FCO','FCR','CPV','NCV','NDV')" +
            " AND d.fmovim BETWEEN '" + DateUtil.dateToString(fechaFacturacionDesde) + "' AND '" + DateUtil.dateToString(fechaFacturacionHasta) + "'";

            if (linea != null) {
                sql += " AND l.cod_linea = " + linea.getCodLinea();
            }
            if (tipoCliente != null){
                sql += " AND c.ctipo_cliente  = '" + tipoCliente.getCtipoCliente() + "'";
            }
            if (zonas != null) {
                sql += " AND z.cod_zona = '" + zonas.getZonasPK().getCodZona() + "'";
            }
            if (canalesVenta != null) {
                sql += " AND mc.cod_canal = '" + canalesVenta.getCodCanal() + "'";
            }
            sql +=" GROUP BY d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc";
        }else{ // discriminado por linea
            sql +=" SELECT d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona, l.cod_linea, l.xdesc as xdesc_linea, d.cod_merca," +
            " SUM(d.igravadas+d.iexentas+d.impuestos) as itotal" +
            " INTO tpareto" +
            " FROM movimientos_merca d, clientes c, zonas z, sublineas s, mercaderias m, lineas l, rutas r, merca_canales mc" +
            " WHERE d.cod_empr = 2" +
            " AND r.cod_zona = z.cod_zona" +
            " AND c.cod_ruta = r.cod_ruta" +
            " AND d.cod_empr = m.cod_empr" +
            " AND d.cod_merca = m.cod_merca" +
            " AND m.cod_sublinea = s.cod_sublinea" +
            " AND s.cod_linea = l.cod_linea" +
            " AND d.cod_cliente  = c.cod_cliente" +
            " AND m.cod_merca = mc.cod_merca" +
            " AND d.ctipo_docum IN ('FCO','FCR','CPV','NCV','NDV')" +
            " AND d.fmovim BETWEEN '" + DateUtil.dateToString(fechaFacturacionDesde) + "' AND '" + DateUtil.dateToString(fechaFacturacionHasta) + "'";
            if (linea != null) {
                sql += " AND l.cod_linea = " + linea.getCodLinea();
            }
            if (tipoCliente != null){
                sql += " AND c.ctipo_cliente  = '" + tipoCliente.getCtipoCliente() + "'";
            }
            if (zonas != null) {
                sql += " AND z.cod_zona = '" + zonas.getZonasPK().getCodZona() + "'";
            }
            if (canalesVenta != null) {
                sql += " AND mc.cod_canal = '" + canalesVenta.getCodCanal() + "'";
            }
            sql +=" GROUP BY d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc, l.cod_linea, l.xdesc, d.cod_merca;";
        } 
        //agregamos las columns sTotal y pvta a tpareto
        sql +=" ALTER TABLE tpareto ADD sTotal NUMERIC(38,0);";
        sql +=" ALTER TABLE tpareto ADD pvta NUMERIC(38,2);";
        
        //agregamos las columnas totalCli y kcliente a pareto
        sql +=" ALTER TABLE tpareto ADD totalCli NUMERIC(38,0);";
        sql +=" ALTER TABLE tpareto ADD kcliente NUMERIC(38,0);";
        
        //calculamos los valores para las columnas agregadas
        sql +=" UPDATE tpareto SET sTotal = (SELECT SUM(itotal) AS sTotal FROM tpareto);";
        sql +=" UPDATE tpareto SET pvta = ROUND(itotal*100/sTotal,2);";
        
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
        em.clear();
        if ("PC".equals(discriminar)) {
            //obtenemos totalCli
            sql = "IF OBJECT_ID('tmpPareto') IS NOT NULL DROP TABLE tmpPareto; SELECT DISTINCT cod_cliente INTO tmpPareto FROM tpareto;";
            q = em.createNativeQuery(sql);
            q.executeUpdate();
            em.clear();
            
            sql = "SELECT COUNT(cod_cliente) AS cantCli FROM tmpPareto;";
            q = em.createNativeQuery(sql);
            totalCli = (Integer)q.getSingleResult();
            em.clear();
            //System.out.println("LeyParetoFacade..::::::::::::::_____ totalCli: " + totalCli);
            
            if ("COE".equals(factorSeleccionado)) { //POR COEFICIENTE
                //obtenemos kcliente
                sql = "DELETE FROM tpareto WHERE pvta > " + factor + "; IF OBJECT_ID('tmpPareto') IS NOT NULL DROP TABLE tmpPareto; SELECT DISTINCT cod_cliente INTO tmpPareto FROM tpareto;";
                q = em.createNativeQuery(sql);
                q.executeUpdate();
                em.clear();
                
                sql = "SELECT COUNT(cod_cliente) AS cantCli FROM tmpPareto;";
                q = em.createNativeQuery(sql);
                kcliente = (Integer)q.getSingleResult();
                em.clear();
                
            }else{  // POR MONTO
                //obtenemos kcliente
                sql = "DELETE FROM tpareto WHERE itotal < " + factor + "; IF OBJECT_ID('tmpPareto') IS NOT NULL DROP TABLE tmpPareto; SELECT DISTINCT cod_cliente INTO tmpPareto FROM tpareto;";
                q = em.createNativeQuery(sql);
                q.executeUpdate();
                em.clear();
                
                sql = "SELECT COUNT(cod_cliente) AS cantCli FROM tmpPareto;";
                q = em.createNativeQuery(sql);
                kcliente = (Integer)q.getSingleResult(); 
                em.clear();
            }
            
            sql = "UPDATE tpareto SET totalCli = " + totalCli + ";"; 
            sql += " UPDATE tpareto SET kcliente = " + kcliente + ";";
            q = em.createNativeQuery(sql);
            q.executeUpdate();
        }
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
            /*sql += "SELECT d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona," +
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
            sql += " GROUP BY d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc";*/
            
            sql = "SELECT * FROM tpareto ORDER BY itotal DESC";
        }else{ //discriminar por linea
            /*sql += "SELECT d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc as xdesc_zona," +
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
            sql += " GROUP BY d.cod_cliente, c.xnombre, z.cod_zona, z.xdesc, l.cod_linea, l.xdesc, d.cod_merca";*/
            
            sql = "SELECT * FROM tpareto ORDER BY itotal DESC";
        }
        
        Query q = em.createNativeQuery(sql);      
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        
       return resultados;
    }
}