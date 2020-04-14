/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.GruposCarga;
import entidad.Sublineas;
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
 * @author admin
 */
@Stateless
public class SublineasFacade extends AbstractFacade<Sublineas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SublineasFacade() {
        super(Sublineas.class);
    }

    public List<Sublineas> listarSublineasConMercaderias() {

        Query q = getEntityManager().createNativeQuery("SELECT DISTINCT s.cod_sublinea, s.xdesc \n"
                + " FROM sublineas s, mercaderias m  WHERE s.cod_sublinea = m.cod_sublinea \n"
                + "AND m.mestado = 'A'  ORDER BY s.xdesc ", Sublineas.class);

        //System.out.println(q.toString());
        List<Sublineas> respuesta = new ArrayList<Sublineas>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public List<Sublineas> listarSublineasOrdenadoXSublineas() {
        Query q = getEntityManager().createNativeQuery("select s.*\n"
                + "from sublineas s, lineas l\n"
                + "where s.cod_linea = l.cod_linea\n"
                + "order by l.xdesc asc", Sublineas.class);

        System.out.println(q.toString());

        List<Sublineas> respuesta = new ArrayList<Sublineas>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
        public List<GruposCarga> listarGruposCargaOrdenadoXGruposCarga() {
        Query q = getEntityManager().createNativeQuery("select s.*\n"
                + "from sublineas s, grupos_carga g\n"
                + "where s.cod_gcarga = g.cod_gcarga\n"
                + "order by g.xdesc asc", GruposCarga.class);

        System.out.println(q.toString());

        List<GruposCarga> respuesta = new ArrayList<GruposCarga>();

        respuesta = q.getResultList();

        return respuesta;
    }


    public void insertarSublineas(Sublineas sublineas) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarSublineas");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_linea", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_gcarga", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);

        q.setParameter("xdesc", sublineas.getXdesc());
        q.setParameter("cod_linea", sublineas.getCodLinea().getCodLinea());
        q.setParameter("cod_gcarga", sublineas.getCodGcarga().getCodGcarga());
        q.setParameter("falta", sublineas.getFalta());
        q.setParameter("cusuario", sublineas.getCusuario());

        q.execute();

    }
    
    public List<Sublineas> listarSublineas() {
        return getEntityManager().createNativeQuery("SELECT * FROM sublineas ", Sublineas.class).getResultList();
    }
    
    public Sublineas subLineaById(Integer id) {
        List<Sublineas> lista = getEntityManager().createNativeQuery("SELECT * FROM sublineas WHERE cod_sublinea=" + id, Sublineas.class).getResultList();

        if (lista.isEmpty()) {//Si no hay elemento
            return null;
        } else {
            return lista.get(0);
        }
    }

    public List<Sublineas> listarSublineasActivas() {
        return getEntityManager().createNativeQuery("SELECT * FROM sublineas where estado = 'A'", Sublineas.class).getResultList();
    }
    
}
