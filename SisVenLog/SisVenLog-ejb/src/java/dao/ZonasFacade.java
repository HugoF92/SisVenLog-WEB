/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Zonas;
import java.util.ArrayList;
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
 * @author Hugo
 */
@Stateless
public class ZonasFacade extends AbstractFacade<Zonas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZonasFacade() {
        super(Zonas.class);
    }

    public Zonas buscarPorCodigo(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from dbo.zonas\n"
                + "where cod_zona =  upper('" + filtro + "') ", Zonas.class);

        //System.out.println(q.toString());
        Zonas respuesta = new Zonas();

        if (q.getResultList().size() <= 0) {
            respuesta = null;
        } else {
            respuesta = (Zonas) q.getSingleResult();
        }

        return respuesta;
    }

    public List<Zonas> buscarPorFiltro(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from dbo.zonas\n"
                + "where xdesc like  '%" + filtro + "%' ", Zonas.class);

        //System.out.println(q.toString());
        List<Zonas> respuesta = new ArrayList<Zonas>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public void insertarZonas(Zonas zonas) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarZonas");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);

        q.setParameter("xdesc", zonas.getXdesc());
        q.setParameter("falta", zonas.getFalta());
        q.setParameter("cusuario", zonas.getCusuario());

        q.execute();
    }

    public List<Zonas> listarZonas() {
        Query q = getEntityManager().createNativeQuery("select * from dbo.zonas ", Zonas.class);
        System.out.println(q.toString());
        return q.getResultList();
    }

    public List<Zonas> listarZonasActivas() {
        Query q = getEntityManager().createNativeQuery("select * from zonas order by  xdesc", Zonas.class);
        System.out.println(q.toString());
        return q.getResultList();
    }
}
