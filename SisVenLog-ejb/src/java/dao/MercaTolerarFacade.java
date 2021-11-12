/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.MercaTolerar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;

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

    public List<MercaTolerar> obtenerListadoMercaTolerar(long lNroFactura, Short lCodProveedor, String lCodMerca) {
        String sql = "SELECT * "
                + "FROM merca_tolerar "
                + "WHERE nrofact = " + lNroFactura + " "
                + "AND cod_empr = 2 "
                + "AND cod_proveed = " + lCodProveedor + " "
                + "and upper(cod_merca) like upper('" + lCodMerca + "') ";
        System.out.println(getEntityManager().createNativeQuery(sql, MercaTolerar.class));
        return getEntityManager().createNativeQuery(sql, MercaTolerar.class).getResultList();
    }

    public void newInsertarMercaTolerar(MercaTolerar mercatolerar) throws Exception {
        EntityTransaction trx = getEntityManager().getTransaction();
        try {
            trx.begin();
            String sql = "";

            Query q = getEntityManager().createNativeQuery(sql);
            q.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            trx.rollback();
            throw e;
        }
    }
}
