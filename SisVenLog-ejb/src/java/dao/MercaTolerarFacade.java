/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package dao;

import entidad.MercaTolerar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author admin
 */
@Stateless
public class MercaTolerarFacade extends AbstractFacade<MercaTolerar> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MercaTolerarFacade() {
        super(MercaTolerar.class);
    }
/*
   public void insertarMercaTolerar(MercaTolerar mercatolerar) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarMercaTolerar");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_zona", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("mtipo", Character.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("npeso_max", Double.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("fultim_inven", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("fultim_modif", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario_modif", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("npunto_est", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("npunto_exp", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_conductor", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_transp", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xchapa", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xmarca", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xobs", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", depositos.getXdesc());
        q.setParameter("cod_zona", depositos.getZonas().getZonasPK().getCodZona());
        q.setParameter("mtipo", depositos.getMtipo());
        q.setParameter("npeso_max", depositos.getNpesoMax());
        q.setParameter("fultim_inven", depositos.getFultimInven());
        q.setParameter("falta", depositos.getFalta());
        q.setParameter("cusuario", depositos.getCusuario());
        q.setParameter("fultim_modif", depositos.getFultimModif());
        q.setParameter("cusuario_modif", depositos.getCusuarioModif());
        q.setParameter("npunto_est", depositos.getNpuntoEst());
        q.setParameter("npunto_exp", depositos.getNpuntoExp());
        q.setParameter("cod_conductor", depositos.getCodConductor().getCodConductor());
        q.setParameter("cod_transp", depositos.getCodTransp().getCodTransp());
        q.setParameter("xchapa", depositos.getXchapa());
        q.setParameter("xmarca", depositos.getXmarca());
        q.setParameter("xobs", depositos.getXobs());
        
        q.execute();

        
    } 
*/
}
