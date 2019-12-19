/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.MarcasComerciales;
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
public class MarcasComercialesFacade extends AbstractFacade<MarcasComerciales> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MarcasComercialesFacade() {
        super(MarcasComerciales.class);
    }
    
    
    public void insertarMarca(MarcasComerciales marca) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarMarca");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", marca.getXdesc());
        q.setParameter("falta", marca.getFalta());
        q.setParameter("cusuario", marca.getCusuario());
        
        
        q.execute();

        
    }

}
