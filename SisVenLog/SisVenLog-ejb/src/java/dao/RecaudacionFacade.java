/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Recaudacion;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class RecaudacionFacade extends AbstractFacade<Recaudacion> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecaudacionFacade() {
        super(Recaudacion.class);
    }
    
    public BigDecimal getNroPlanilla(){
        Query q = this.getEntityManager().createNativeQuery("select max(nro_planilla) from recaudacion");
        System.out.println(q.toString());
        return (BigDecimal) q.getSingleResult();
    }
    
    public void deleteRecaudacion(long nroPlanilla){
        Query q = this.em.createNativeQuery(" delete from recaudacion where nro_planilla = "+nroPlanilla);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
}
