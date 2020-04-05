/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Categorias;
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
public class CategoriasFacade extends AbstractFacade<Categorias> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriasFacade() {
        super(Categorias.class);
    }
    
    public void insertarCategorias(Categorias categorias) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarCategorias");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_division", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("norden_envio", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", categorias.getXdesc());
        q.setParameter("cod_division",categorias.getCodDivision().getCodDivision());
        q.setParameter("norden_envio", categorias.getNordenEnvio());
        q.setParameter("falta", categorias.getFalta());
        q.setParameter("cusuario", categorias.getCusuario());
        
        
        q.execute();
}
    
}
