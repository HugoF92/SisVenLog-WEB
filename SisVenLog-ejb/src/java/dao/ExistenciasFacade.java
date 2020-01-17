/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Existencias;
import entidad.ExistenciasPK;
import entidad.Mercaderias;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class ExistenciasFacade extends AbstractFacade<Existencias> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExistenciasFacade() {
        super(Existencias.class);
    }

    //1
    public List<Existencias> buscarPorCodigoDepositoOrigenMerca(String filtro, Short origen, String codCanal) {
        try {
            Query q = getEntityManager().createNativeQuery("select e.*, m.* \n"
                    + "from  existencias e,  mercaderias m, merca_canales ca\n"
                    + "where m.cod_merca = e.cod_merca\n"
                    + "and m.cod_empr = e.cod_empr\n"
                    + "and m.cod_merca = ca.cod_merca\n"
                    + "and m.mestado = 'A'\n"
                    + "and e.cod_depo =  " + origen + "\n"
                    + "and ca.cod_canal =  '" + codCanal + "'\n"
                    + "and upper(e.cod_merca) like upper('" + filtro + "')\n", Existencias.class);

            System.out.println(q.toString());

            List<Existencias> respuesta = q.getResultList();
            return respuesta;

        } catch (Exception e) {
            return null;
        }

    }

    //2
    public List<Existencias> buscarPorCodigoDepositoOrigenTodasMerca(Short origen, int[] range, String codCanal) {
        try {
            Query q = getEntityManager().createNativeQuery("select e.*, m.* \n"
                    + "from  existencias e,  mercaderias m, merca_canales ca\n"
                    + "where m.cod_merca = e.cod_merca\n"
                    + "and m.cod_empr = e.cod_empr\n"
                    + "and m.cod_merca = ca.cod_merca\n"
                    + "and m.mestado = 'A'\n"
                    + "and ca.cod_canal =  '" + codCanal + "'\n"
                    + "and e.cod_depo =  " + origen + "\n",
                    Existencias.class);
            q.setMaxResults(range[1]);
            q.setFirstResult(range[0]);
            System.out.println(q.toString());

            List<Existencias> respuesta = q.getResultList();
            return respuesta;

        } catch (Exception e) {
            return null;
        }

    }

    public List<Existencias> buscarPorCodigoDepositoOrigenTodasMercaSingle(Short origen) {
        try {
            Query q = getEntityManager().createNativeQuery("select e.*, m.* \n"
                    + "from  existencias e,  mercaderias m\n"
                    + "where m.cod_merca = e.cod_merca\n"
                    + "and m.cod_empr = e.cod_empr\n"
                    + "and m.mestado = 'A'\n"
                    + "and e.cod_depo =  " + origen + "\n",
                    Existencias.class);

            System.out.println(q.toString());

            List<Existencias> respuesta = q.getResultList();
            return respuesta;

        } catch (Exception e) {
            return null;
        }

    }

    //3
    public int CountBuscarPorCodigoDepositoOrigenTodasMerca(Short origen, String codCanal) {
        try {
            Query q = getEntityManager().createNativeQuery("select e.*, m.* \n"
                    + "from  existencias e,  mercaderias m, merca_canales ca\n"
                    + "where m.cod_merca = e.cod_merca\n"
                    + "and m.cod_empr = e.cod_empr\n"
                    + "and m.cod_merca = ca.cod_merca\n"
                    + "and m.mestado = 'A'\n"
                    + "and ca.cod_canal =  '" + codCanal + "'\n"
                    + "and e.cod_depo =  " + origen + "\n",
                    Existencias.class);

            System.out.println(q.toString());

            int respuesta = q.getResultList().size();
            return respuesta;

        } catch (Exception e) {
            return 0;
        }

    }

    //4
    public List<Existencias> buscarListaPorCodigoDepositoOrigenMercaDescripcion(String filtro, Short origen, int[] range, String codCanal) {
        try {
            Query q = getEntityManager().createNativeQuery("select e.*, m.* \n"
                    + "from  existencias e,  mercaderias m, merca_canales ca\n"
                    + "where m.cod_merca = e.cod_merca\n"
                    + "and m.cod_empr = e.cod_empr\n"
                    + "and m.cod_merca = ca.cod_merca\n"
                    + "and m.mestado = 'A'\n"
                    + "and ca.cod_canal =  '" + codCanal + "'\n"
                    + "and e.cod_depo =  " + origen + "\n"
                    + "and (upper(m.xdesc) like upper('" + "%" + filtro + "%" + "')\n"
                    + "or upper(e.cod_merca) like upper('" + "%" + filtro + "'))\n", Existencias.class);
            q.setMaxResults(range[1]);
            q.setFirstResult(range[0]);
            System.out.println(q.toString());

            List<Existencias> respuesta = q.getResultList();
            return respuesta;

        } catch (Exception e) {
            return null;
        }

    }

    //5
    public int CountBuscarPorCodigoDepositoOrigenTodasMercaConFiltro(String filtro, Short origen, String codCanal) {
        try {
            Query q = getEntityManager().createNativeQuery("select e.*, m.* \n"
                    + "from  existencias e,  mercaderias m, merca_canales ca\n"
                    + "where m.cod_merca = e.cod_merca\n"
                    + "and m.cod_empr = e.cod_empr\n"
                    + "and m.cod_merca = ca.cod_merca\n"
                    + "and m.mestado = 'A'\n"
                    + "and ca.cod_canal =  '" + codCanal + "'\n"
                    + "and e.cod_depo =  " + origen + "\n"
                    + "and (upper(m.xdesc) like upper('" + "%" + filtro + "%" + "')\n"
                    + "or upper(e.cod_merca) like upper('" + "%" + filtro + "'))\n", Existencias.class);

            System.out.println(q.toString());

            int respuesta = q.getResultList().size();
            return respuesta;

        } catch (Exception e) {
            return 0;
        }

    }

    public List<Existencias> listarExistencias(Short cod_emp, short cod_depo) {
        Query q = getEntityManager().createNativeQuery("select e.* from  existencias e where e.cod_empr=" + cod_emp
                + " and e.cod_depo=" + cod_depo, Existencias.class);
        System.out.println(q.toString());
        return q.getResultList();
    }

    //obtener lista de mercaderias existentes en el deposito seleccionado.
    public List<Mercaderias> listarMercaderiasByExistencias(Short cod_emp, short cod_depo) {
        Query q = getEntityManager().createNativeQuery("select  m.* from  existencias e, mercaderias m "
                + "  where m.cod_merca = e.cod_merca and e.cod_empr=" + cod_emp
                + " and e.cod_depo=" + cod_depo, Mercaderias.class);
        System.out.println(q.toString());
        return q.getResultList();
    }
}
