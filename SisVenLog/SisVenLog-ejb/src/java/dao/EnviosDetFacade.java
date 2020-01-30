/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.EnviosDet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Stateless
public class EnviosDetFacade extends AbstractFacade<EnviosDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnviosDetFacade() {
        super(EnviosDet.class);
    }
    
     public void insertarEnviosDetalle(EnviosDet enviosDet) {
        em.persist(enviosDet);
    }
}
