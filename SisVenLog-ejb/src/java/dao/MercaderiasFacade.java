/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Existencias;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Administrador
 */
@Stateless
public class MercaderiasFacade extends AbstractFacade<Mercaderias> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MercaderiasFacade() {
        super(Mercaderias.class);
    }

    public List<Mercaderias> listarMercaderiasActivasDepo1() {
        Query q = getEntityManager().createNativeQuery("select m.*\n"
                + "from  mercaderias m, existencias e\n"
                + "where m.cod_merca = e.cod_merca\n"
                + "and m.mestado = 'A'\n"
                + "and e.cod_depo = 1", Mercaderias.class);

        System.out.println(q.toString());

        List<Mercaderias> respuesta = new ArrayList<Mercaderias>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public List<Mercaderias> buscarPorFiltro(String filtro) {
        Query q = getEntityManager().createNativeQuery("select m.* \n"
                + "from  mercaderias m, existencias e\n"
                + "where m.cod_merca = e.cod_merca\n"
                + "and m.mestado = 'A'\n"
                + "and (upper(m.xdesc) like upper('" + filtro + "')\n"
                + "or upper(m.cod_merca) like upper('" + filtro + "'))\n"
                + "and e.cod_depo = 5", Existencias.class);

        System.out.println(q.toString());

        List<Mercaderias> respuesta = new ArrayList<Mercaderias>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public Mercaderias buscarPobuscarPorFiltroMercaderiaDepositoOrigenrFiltro(String merca, short depositoOrigen) {
        depositoOrigen = 5;
        Query q = getEntityManager().createNativeQuery("select m.*\n"
                + "from  mercaderias m, existencias e\n"
                + "where m.cod_merca = e.cod_merca\n"
                + "and m.mestado = 'A'\n"
                + "and upper(m.cod_merca) like upper('" + merca + "')\n"
                + "and e.cod_depo = " + depositoOrigen + "", Mercaderias.class);

        System.out.println(q.toString());

        Mercaderias respuesta = new Mercaderias();

        respuesta = (Mercaderias) q.getSingleResult();

        return respuesta;
    }

    public Mercaderias buscarPorCodigoMercaderia(String codMerca) {
        Query q = getEntityManager().createNativeQuery("select m.*\n"
                + "from  mercaderias m\n"
                + "where m.mestado = 'A'\n"
                + "and upper(m.cod_merca) like upper('" + codMerca + "')\n",
                Mercaderias.class);

        System.out.println(q.toString());

        Mercaderias respuesta = new Mercaderias();

        respuesta = (Mercaderias) q.getSingleResult();

        return respuesta;
    }

    public List<Mercaderias> listarMercaderias() {
        Query q = getEntityManager().createNativeQuery("select * from  mercaderias", Mercaderias.class);
        return q.getResultList();
    }

    public List<Mercaderias> listarMercaderiasActivas() {
        Query q = getEntityManager().createNativeQuery("select * from  mercaderias where mestado='A'", Mercaderias.class);
        return q.getResultList();
    }

    public int insertarMercaderias(Mercaderias entity) {
        try {
            getEntityManager().persist(entity);
            return 1;
        } catch (ConstraintViolationException e) {
            return 0;
        }
    }

    public int editarMercaderias(Mercaderias entity) {
        try {
            getEntityManager().merge(entity);
            return 1;
        } catch (ConstraintViolationException e) {
            return 0;
        }

    }

    public Mercaderias findByIdMercaderia(MercaderiasPK id) {
        Query q = getEntityManager().createNativeQuery("select m.* "
                + " from  mercaderias m "
                + " where upper(m.cod_merca) like upper('" + id.getCodMerca() + "') and m.cod_empr=" + id.getCodEmpr(),
                Mercaderias.class);
        System.out.println(q.toString());

        Mercaderias respuesta = null;
        try {
            respuesta = (Mercaderias) q.getSingleResult();
        } catch (Exception e) {

        }
        return respuesta;
    }

}
