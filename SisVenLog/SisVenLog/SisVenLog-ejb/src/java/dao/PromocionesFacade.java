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
                + "WHERE (frige_desde >= '"+desde+"'\n"
                + "	or frige_hasta >= '"+hasta+"'  \n"
                + "	or '"+desde+"' BETWEEN frige_desde AND frige_hasta)", Promociones.class);

        System.out.println(q.toString());

        List<Promociones> respuesta = new ArrayList<Promociones>();

        respuesta = q.getResultList();

        return respuesta;
    }

}
