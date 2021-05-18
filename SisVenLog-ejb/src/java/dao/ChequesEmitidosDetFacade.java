/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.ChequesEmitidos;
import entidad.ChequesEmitidosDet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author admin
 */
@Stateless
public class ChequesEmitidosDetFacade extends AbstractFacade<ChequesEmitidosDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChequesEmitidosDetFacade() {
        super(ChequesEmitidosDet.class);
    }

    public List<ChequesEmitidosDet> chequesEmitidosDetPorChequesEmitidos(ChequesEmitidos chequesEmitidos) {
        String sql = "SELECT ced from ChequesEmitidosDet ced where ced.chequesEmitidos = ?1";
        TypedQuery<ChequesEmitidosDet> query = em.createQuery(sql, ChequesEmitidosDet.class);
        query.setParameter(1, chequesEmitidos);
        return query.getResultList();
    }

}
