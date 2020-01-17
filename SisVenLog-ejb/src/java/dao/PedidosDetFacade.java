/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.PedidosDet;
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
public class PedidosDetFacade extends AbstractFacade<PedidosDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidosDetFacade() {
        super(PedidosDet.class);
    }

    public List<PedidosDet> listaPedidosDetPorNroPedido(Long nro_pedido) {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM pedidos_det WHERE cod_empr =2  AND nro_pedido=" + nro_pedido,
                PedidosDet.class);
        System.out.println(q.toString());
        return q.getResultList();
    }
}
