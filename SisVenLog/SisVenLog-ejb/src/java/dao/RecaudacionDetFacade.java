/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RecaudacionDet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class RecaudacionDetFacade extends AbstractFacade<RecaudacionDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecaudacionDetFacade() {
        super(RecaudacionDet.class);
    }
    
    public void deleteRecaudacionDet(long nroPlanilla){
        Query q = this.em.createNativeQuery("delete from recaudacion_det where nro_planilla = "+nroPlanilla);
        System.out.println(q.toString());
        q.executeUpdate();
    }
}
