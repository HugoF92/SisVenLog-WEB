/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Promociones;
import entidad.Promociones;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PromocionesFacade extends AbstractFacade<Promociones> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PromocionesFacade() {
        super(Promociones.class);
    }

    public List<Promociones> listarPromocionesLiFacBonif(String desde, String hasta) {
        Query q = getEntityManager().createNativeQuery("SELECT * \n"
                + "FROM promociones  \n"
                + "WHERE (frige_desde >= '" + desde + "'\n"
                + "	or frige_hasta >= '" + hasta + "'  \n"
                + "	or '" + desde + "' BETWEEN frige_desde AND frige_hasta)", Promociones.class);

        System.out.println(q.toString());

        List<Promociones> respuesta = new ArrayList<Promociones>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public Integer actualizarVigenciaPromocion(String sql) {
        try {
            Query q = getEntityManager().createNativeQuery(sql);

            System.out.println("SQL : " + q.toString());

            Integer respuesta = q.executeUpdate();

            return respuesta;

        } catch (Exception e) {
            System.out.println("MESAJE DE ERROR:");
            System.out.println(e.getLocalizedMessage());
            return -1;
        }
    }

    public List<Promociones> findByNroPromo(String nroPromo) {
        Query q = getEntityManager().createNativeQuery("SELECT * \n"
                + "FROM promociones  \n"
                + "WHERE nro_promo=" + nroPromo, Promociones.class);
        return q.getResultList();

    }
    
    public List<Promociones> findAllOrderXDesc() {
        return getEntityManager().createNativeQuery("SELECT * FROM promociones", Promociones.class).getResultList();
    }
    public List<Promociones> buscarPorFiltro(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from promociones\n"
                + "where (xdesc like '%"+filtro+"%'\n"
                + "	   or nro_promo like '%"+filtro+"%' )", Promociones.class);

        return q.getResultList();
    }

}
