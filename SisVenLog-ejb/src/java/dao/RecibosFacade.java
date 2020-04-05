/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import entidad.Clientes;
import entidad.Recibos;
import entidad.Zonas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.StringUtil;

/**
 *
 * @author admin
 * @author jvera
 * @author dadob
 * 
 */
@Stateless
public class RecibosFacade extends AbstractFacade<Recibos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecibosFacade() {
        super(Recibos.class);
    }
    
    public List<Object[]> listaRecibosSinDetalle(String fechaReciboDesde, 
                                                  String fechaReciboHasta, 
                                                  long nroReciboDesde, 
                                                  long nroReciboHasta, 
                                                  List<Clientes> listaCodCliente,
                                                  Zonas zona,
                                                  String discriminar,
                                                  Boolean todosClientes){
        String sql = "SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques, r.xobs, r.mestado," +
                " t.cod_cliente as cod_cliente2, t.xnombre as xnombre" +
                " FROM recibos r ,  clientes t, rutas a  WHERE r.cod_empr = 2" +
                " AND r.cod_cliente = t.cod_cliente" +
                " AND t.cod_ruta = a.cod_ruta" +
                " AND r.fanul IS NULL" +
                " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" + " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
        if (!todosClientes) {
            if (!listaCodCliente.isEmpty()) {
                sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")";
            }
        }
       
        if (zona != null) {
            sql += " AND a.cod_zona = " + zona.getZonasPK().getCodZona();
        }
        
        if ("ND".equals(discriminar)) {
            //ordenar por Nro Recibo y fecha recibo 
            sql += " ORDER BY r.nrecibo, r.frecibo";
        }else if ("PC".equals(discriminar)) {
            //ordenar por código cliente 
            sql += " ORDER BY r.cod_cliente";
        }
        
        Query q = em.createNativeQuery(sql);      
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        
       return resultados;
    }
    
    public List<Object[]> listaRecibosConDetalle(String fechaReciboDesde, 
                                                  String fechaReciboHasta, 
                                                  long nroReciboDesde, 
                                                  long nroReciboHasta,
                                                  List<Clientes> listaCodCliente,
                                                  Zonas zona,
                                                  String discriminar,
                                                  Boolean todosClientes){
        
        String sql = "SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques, r.xobs, r.mestado," +
                    " c.xnombre, d.ctipo_docum, d.ndocum, '' as xdesc_banco, r.fanul, 'F' as tipodet, c.cod_cliente as cod_cliente2," +
                    " c.xnombre as xnombre2, d.itotal" +
                    " FROM recibos r , recibos_det d, clientes c, rutas a" +
                    " WHERE r.nrecibo = d.nrecibo" +
                    " AND r.cod_empr = d.cod_empr" +
                    " AND c.cod_ruta = a.cod_ruta" +
                    " AND r.cod_empr = 2" +
                    " AND r.fanul IS NULL" +
                    " AND r.cod_cliente = c.cod_cliente" +
                    " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
                    " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta;
        
        if (!todosClientes) {
            if (!listaCodCliente.isEmpty()) {
                sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")";
            }
        }
        
        if (zona != null) {
            sql += " AND a.cod_zona = " + zona.getZonasPK().getCodZona();
        }
        
        sql += " UNION ALL" +
            " SELECT r.nrecibo, r.cod_cliente, r.frecibo, r.irecibo, r.iefectivo, r.iretencion, r.icheques, r.xobs, r.mestado," +
            " c.xnombre, 'CHQ' as ctipo_docum, nro_cheque as ndocum, b.xdesc as xdesc_banco, r.fanul," +
            " 'C' as tipodet, c.cod_cliente as cod_cliente2, c.xnombre as xnombre2, d.ipagado as itotal" +
            " FROM recibos r , recibos_cheques d, bancos b, clientes c, rutas a" +
            " WHERE r.nrecibo = d.nrecibo" +
            " AND r.cod_empr = d.cod_empr" +
            " AND c.cod_ruta = a.cod_ruta" +
            " AND r.cod_empr = 2" +
            " AND d.cod_banco = b.cod_banco" +
            " AND r.cod_cliente = c.cod_cliente" +
            " AND r.fanul IS NULL" +
            " AND r.frecibo BETWEEN " + "'" + fechaReciboDesde + "'" + " AND " + "'" + fechaReciboHasta + "'" +
            " AND r.nrecibo BETWEEN " + nroReciboDesde + " AND " + nroReciboHasta +
            " AND r.icheques > 0 ";
        
        if (!todosClientes) {
            if (!listaCodCliente.isEmpty()) {
                sql += " AND r.cod_cliente IN (" + StringUtil.convertirListaAString(listaCodCliente) + ")";
            }
        }
        
        if (zona != null) {
            sql += " AND a.cod_zona = " + zona.getZonasPK().getCodZona();
        }
        
        if ("ND".equals(discriminar)) {
            //ordenar por Nro Recibo y fecha recibo 
            sql += " ORDER BY r.nrecibo, r.frecibo";
        }else if ("PC".equals(discriminar)) {
            //ordenar por código cliente 
            sql += " ORDER BY r.cod_cliente";
        }
         
        Query q = em.createNativeQuery(sql);      
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        
        return resultados;
    }
    
     public long obtenerCantidadRecibos(long lNroRecibo){
        String sql =    "SELECT * FROM recibos " +
                        "WHERE nrecibo = "+lNroRecibo+" "+
                        "AND cod_empr = 2";
        Query q = em.createNativeQuery(sql, Recibos.class);
        System.out.println(q.toString());
        long cantidadRegistros = q.getResultList().size();
        return cantidadRegistros;
    }
    
    public void insertarRecibo( long lNroRecibo,
                                String lFRecibo,
                                Integer lCodCliente,
                                long lIRecibo,
                                long lIRetencion,
                                long lIEfectivo,
                                long lICheque,
                                String lXObs){
        
        String sql =    "INSERT INTO recibos(cod_empr, nrecibo, " +
                        "frecibo, cod_cliente, irecibo, iretencion, iefectivo, icheques, mestado, xobs) " +
                        "values (2, "+lNroRecibo+", " +
                        "'"+lFRecibo+"', "+lCodCliente+", "+lIRecibo+", "+lIRetencion+", "+lIEfectivo+", " +
                        ""+lICheque+", 'A', '"+lXObs+"')";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
    public void actualizarEstadoRegistro(long lNroRecibo){
        String sql =    "UPDATE recibos SET mestado = 'X' " +
                        "WHERE nrecibo = "+lNroRecibo+" AND cod_empr = 2";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
    public List<Recibos> obtenerRecibos(){
        Query q = getEntityManager().createNativeQuery("select * from recibos order by frecibo desc ",
                Recibos.class);
        return q.getResultList();
    }
    
    public List<Recibos> findRangeSort(int[] range) {
        Query q = getEntityManager().createNativeQuery("select * from recibos order by nrecibo desc ",
                Recibos.class);
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);

        return q.getResultList();
    }
}
