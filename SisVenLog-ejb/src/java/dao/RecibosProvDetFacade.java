/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RecibosProvDet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class RecibosProvDetFacade extends AbstractFacade<RecibosProvDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecibosProvDetFacade() {
        super(RecibosProvDet.class);
    }
    
    public void insertarReciboProveedorDetalle( short gCodEmpresa, 
                                                Short lCodProveed,
                                                long lNroRecibo,
                                                long lNroFact,
                                                String ctipoDocum,
                                                long lIDeuda,
                                                String lFFactura,
                                                long lInteres,
                                                long lTTotal,
                                                long lISaldo,
                                                String lFRecibo){
        //lITotal es igual a l_ideuda
        String sql =    "INSERT INTO recibos_prov_det(cod_empr, cod_proveed, nrecibo, " +
                        "nrofact, ctipo_docum, itotal, ffactur, interes, ttotal, isaldo, frecibo) " +
                        "values ("+gCodEmpresa+", "+lCodProveed+", "+lNroRecibo+", " +
                        ""+lNroFact+", '"+ctipoDocum+"', "+lIDeuda+", '"+lFFactura+"', "+lInteres+", "+lTTotal+", "+lISaldo+", '"+lFRecibo+"')";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
}
