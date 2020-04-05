/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Servicios;
import entidad.Servicios;
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
public class ServiciosFacade extends AbstractFacade<Servicios> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServiciosFacade() {
        super(Servicios.class);
    }
   
    public void insertarServicios(Servicios servicios) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarServicios");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
         q.registerStoredProcedureParameter("pimpues", Double.class, ParameterMode.IN);
        
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("csuario", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", servicios.getXdesc());
        q.setParameter("pimpues", servicios.getPimpues());
        
        q.setParameter("falta", servicios.getFalta());
        q.setParameter("csuario", servicios.getCsuario());
        
        
        q.execute();
    }
}
