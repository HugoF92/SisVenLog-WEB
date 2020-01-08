/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RecibosProv;
import entidad.Proveedores;
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
