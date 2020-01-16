/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RecibosDet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class RecibosDetFacade extends AbstractFacade<RecibosDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecibosDetFacade() {
        super(RecibosDet.class);
    }
    
    public void insertarReciboDetalle(  long lNroRecibo,
                                        long lNroFact,
                                        String lCTipoDocum,
                                        long lIDeuda,
                                        String lFFactura,
                                        long lInteres,
                                        long lTotal,
                                        long lISaldo){
        String sql =    "INSERT INTO recibos_det(cod_empr, nrecibo, " +
                        "ndocum, ctipo_docum, itotal, ffactur, interes, ttotal, isaldo) " +
                        "values (2, "+lNroRecibo+", " +
                        ""+lNroFact+", '"+lCTipoDocum+"', "+lIDeuda+", '"+lFFactura+"', "+lInteres+", "+lTotal+", "+lISaldo+")";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
}
