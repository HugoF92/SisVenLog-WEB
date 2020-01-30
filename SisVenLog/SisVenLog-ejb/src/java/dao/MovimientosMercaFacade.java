/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.MovimientosMerca;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author admin
 */
@Stateless
public class MovimientosMercaFacade extends AbstractFacade<MovimientosMerca> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MovimientosMercaFacade() {
        super(MovimientosMerca.class);
    }
    
    
    public void insertarMovimientosMerca(MovimientosMerca movimientosMerca) {
          try {
        em.persist(movimientosMerca);
    } catch (ConstraintViolationException e) {
        // Aqui tira los errores de constraint
        for (ConstraintViolation actual : e.getConstraintViolations()) {
            System.out.println(actual.toString());
        }
    }
        
    }
    
   
}
