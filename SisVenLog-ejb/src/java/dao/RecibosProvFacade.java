/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RecibosProv;
import entidad.Proveedores;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;

/**
 *
 * @author admin
 */
@Stateless
public class RecibosProvFacade extends AbstractFacade<RecibosProv> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecibosProvFacade() {
        super(RecibosProv.class);
    }
    
    public void borrarRecibosDelProveedor(Short lCodProveed, long lNroRecibo){
        String sql = "DELETE FROM recibos_prov WHERE cod_empr = 2 AND cod_proveed = "+lCodProveed+" AND nrecibo = "+lNroRecibo;
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public long obtenerCantidadRecibosDelProveedor(long lNroRecibo, String lFRecibo, Short lCodProveed){
        String sql =    "SELECT * FROM recibos_prov " +
                        "WHERE nrecibo = "+lNroRecibo+" "+
                        "AND frecibo = '"+lFRecibo+"' "+
                        "AND cod_proveed = "+lCodProveed+" "+
                        "AND cod_empr = 2";
        Query q = em.createNativeQuery(sql, RecibosProv.class);
        System.out.println(q.toString());
        long cantidadRegistros = q.getResultList().size();
        return cantidadRegistros;
    }
    
    public long obtenerImportePagadoDeProveedores(String lNroCheque, short lCodBanco){
        String sql =    "SELECT SUM(r.ipagado) as tusado " +
                        "from recibos_PROV_cheques r, recibos_prov e " +
                        "WHERE  r.cod_empr = 2 and e.cod_empr = 2 " +
                        "AND r.nro_cheque like '"+"%"+lNroCheque.trim()+"%"+"' "+
                        "AND r.cod_banco = "+lCodBanco+" "+
                        "AND r.nrecibo = e.nrecibo "+
                        "AND e.mestado = 'A' "+
                        "UNION ALL "+
                        "SELECT SUM(ipagado) as tusado "+
                        "from cuentas_proveedores c "+
                        "WHERE C.cod_empr = 2 AND ndocum_cheq like '"+"%"+lNroCheque.trim()+"%"+"' "+
                        "AND ctipo_docum = 'CHQ' " +
                        "AND nrofact IS NOT NULL " +
                        "AND cod_banco = "+lCodBanco;
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList(); 
        long importePagado = 0;
        for(Object[] resultado: resultados){
            if(resultado != null){
                importePagado = Long.parseLong(resultado[0] == null ? String.valueOf("0") : resultado[0].toString());
            }
        }
        return importePagado;
    }
    
    public void insertarReciboProveedor(short gCodEmpresa, 
                                        long lNroRecibo, 
                                        String lFRecibo, 
                                        Short lCodProveed, 
                                        long lIRecibo,
                                        long lIRetencion,
                                        long lIEfectivo, 
                                        long lICheques,
                                        String lXObs){
        String sql =    "INSERT INTO recibos_prov(cod_empr, nrecibo, " +
                        "frecibo, cod_proveed, irecibo, iretencion, iefectivo, icheques, mestado, xobs) " +
                        "VALUES ("+gCodEmpresa+", "+lNroRecibo+", '"+lFRecibo+"', "+lCodProveed+", "+lIRecibo+", "+lIRetencion+", "+lIEfectivo+", "+lICheques+", 'A', '"+lXObs+"')";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public void actualizarReciboProveedor(  String lFRecibo,
                                            long lIRecibo,
                                            long lIRetencion,
                                            String lXObs,
                                            Short lCodProveed,
                                            long lNroRecibo){
        String sql =    "UPDATE  recibos_prov SET "+
                        "frecibo = '"+lFRecibo+"', "+
                        "irecibo = "+lIRecibo+", "+
                        "iretencion = "+lIRetencion+", "+
                        "xobs = '"+lXObs+"' "+
                        "WHERE cod_empr = 2 "+
                        "AND cod_proveed = "+lCodProveed+" "+
                        "AND frecibo = '"+lFRecibo+"' "+
                        "AND nrecibo = "+lNroRecibo;
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public List<RecibosProv> listadoRecibosProveedores(){
        String sql = "SELECT * FROM recibos_prov";
        Query q = em.createNativeQuery(sql, RecibosProv.class);
        List<RecibosProv> resultado = new ArrayList<>();
        resultado = q.getResultList();
        return resultado;
    }
    
    public List<Object[]> listaProveedores( Date fechaReciboDesde, 
                                            Date fechaReciboHasta, 
                                            long nroReciboDesde, 
                                            long nroReciboHasta, 
                                            String discriminar,
                                            Boolean conDetalle,
                                            Proveedores provSeleccionado){
        String sql = "";
        if (conDetalle) {
            sql += "SELECT r.nrecibo, r.cod_proveed, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques," +
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
                if ("ND".equals(discriminar)) {
                    sql += " ORDER BY nrecibo, frecibo";
                }else if ("PC".equals(discriminar)) {
                    //ordenar por código cliente 
                    sql += " ORDER BY cod_proveed";
                }
        }else{ //sin detalle
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
            if ("ND".equals(discriminar)) {
                sql += " ORDER BY nrecibo, frecibo";
            }else if ("PC".equals(discriminar)) {
                //ordenar por código cliente 
                sql += " ORDER BY cod_proveed";
            }
        }
        
        Query q = em.createNativeQuery(sql);      
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        
       return resultados;
    }
    
}
