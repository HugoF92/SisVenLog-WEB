/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Usuarios;
import entidad.Usuarios;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author 
 */
@Stateless
public class UsuariosFacade extends AbstractFacade<Usuarios> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuariosFacade() {
        super(Usuarios.class);
    }
    
    public void insertarUsuarios(Usuarios usuarios) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarUsuarios");
        q.registerStoredProcedureParameter("xnombre", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        
        q.setParameter("xnombre", usuarios.getXnombre());
        q.setParameter("falta", usuarios.getFalta());
        q.setParameter("cusuario", usuarios.getCusuario());
        
        
        q.execute();

    }
    
    
}
