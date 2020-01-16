/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RolesAplicaciones;
import java.util.Date;
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
public class RolesAplicacionesFacade extends AbstractFacade<RolesAplicaciones> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolesAplicacionesFacade() {
        super(RolesAplicaciones.class);
    }

    public void insertarRolesAplicaciones(RolesAplicaciones rolesAplicaciones) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarRolesAplicaciones");

        q.registerStoredProcedureParameter("cod_rol", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_aplicacion", Short.class, ParameterMode.IN);

        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);

        q.setParameter("cod_rol", rolesAplicaciones.getRolesAplicacionesPK().getCodRol());
        q.setParameter("cod_aplicacion", rolesAplicaciones.getRolesAplicacionesPK().getCodAplicacion());

        q.setParameter("falta", rolesAplicaciones.getFalta());
        q.setParameter("cusuario", rolesAplicaciones.getCusuario());

        q.execute();

    }

    public RolesAplicaciones validarPermisosMenu(String usuario, String aplicacion) {

        RolesAplicaciones respuesta = new RolesAplicaciones();

        Query q = getEntityManager().createNativeQuery(" select ra.*\n"
                + "from roles_aplicaciones ra, roles_usuarios ru\n"
                + "where ra.cod_rol = ru.cod_rol\n"
                + "and ra.cod_aplicacion = '"+aplicacion+"'\n"
                + "and ru.cod_usuario = '"+usuario+"' ", RolesAplicaciones.class);

        //System.out.println(q.toString());
        if (q.getResultList().size() <= 0) {
            respuesta = null;
        } else {
            respuesta = (RolesAplicaciones) q.getSingleResult();
        }

        return respuesta;

    }
}
