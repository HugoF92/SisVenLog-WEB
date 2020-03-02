/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Bancos;
import entidad.Motivos;
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
public class MotivosFacade extends AbstractFacade<Motivos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MotivosFacade() {
        super(Motivos.class);
    }
    
     public void insertarMotivos(Motivos motivos) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarMotivos");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", motivos.getXdesc());
        q.setParameter("falta", motivos.getFalta());
        q.setParameter("cusuario", motivos.getCusuario());
        
        
        q.execute();

        
    }
    
}
