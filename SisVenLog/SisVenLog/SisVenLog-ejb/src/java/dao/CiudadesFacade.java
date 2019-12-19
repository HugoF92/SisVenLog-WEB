/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Ciudades;
import entidad.Ciudades;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author admin
 */
@Stateless
public class CiudadesFacade extends AbstractFacade<Ciudades> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CiudadesFacade() {
        super(Ciudades.class);
    }
    
    public void insertarCiudad(Ciudades ciudades) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarCiudad");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", ciudades.getXdesc());
        
        
        q.execute();

        
    }

}
