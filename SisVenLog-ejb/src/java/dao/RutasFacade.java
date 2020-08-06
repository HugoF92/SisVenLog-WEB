/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Rutas;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author admin
 */
@Stateless
public class RutasFacade extends AbstractFacade<Rutas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RutasFacade() {
        super(Rutas.class);
    }
    
    public void insertarRutas(Rutas rutas) {
        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarRutas");
        q.registerStoredProcedureParameter("cod_zona", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        
        q.setParameter("cod_zona", rutas.getZonas().getZonasPK().getCodZona());
        q.setParameter("xdesc", rutas.getXdesc());
        q.setParameter("falta", rutas.getFalta());
        q.setParameter("cusuario", rutas.getCusuario());
        
        q.execute();
    }
    
    public List<Rutas> listarRutas() {
        Query q = getEntityManager().createNativeQuery("select * from rutas order by xdesc", Rutas.class);
        return q.getResultList();
    }
    
}
