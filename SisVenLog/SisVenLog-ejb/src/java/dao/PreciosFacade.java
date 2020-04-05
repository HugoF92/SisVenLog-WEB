/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Precios;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class PreciosFacade extends AbstractFacade<Precios> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PreciosFacade() {
        super(Precios.class);
    }
    
    public List<Precios> obtenerPreciosPorFechaFacturaCodigoMercaYTipoVenta(String lFFactura, String lCodMerca, Character lCTipoVenta){
        String sql =    "select * from precios " +
                        "where frige_desde <= '"+lFFactura+"' " +
                        "and (frige_hasta IS NULL OR frige_hasta >= '"+lFFactura+"') AND COD_DEPO = 1 " +    //deposito venlog? o le pasamos por parametro el codigo deposito?
                        "and upper(COD_MERCA) like upper('"+lCodMerca.trim()+"') AND CTIPO_VTA = '"+lCTipoVenta+"' ";
        Query q = em.createNativeQuery(sql, Precios.class);
        System.out.println(q.toString());
        List<Precios> resultado = q.getResultList();
        return resultado;
    }
    
    public Precios findPreciosByDepoMerca(String codEmpr,Short codDepo,String codMerca,String ctipVta){
        try{
            Query q = getEntityManager().createNativeQuery("select * from precios where cod_empr = "+codEmpr+" and cod_depo = "+codDepo
                    +" and cod_merca = "+codMerca+" and ctipo_vta = "+ctipVta+" order by  frige_desde desc ");
            System.out.println(q.toString());
            q.setMaxResults(1);
            return (Precios) q.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
    
}
