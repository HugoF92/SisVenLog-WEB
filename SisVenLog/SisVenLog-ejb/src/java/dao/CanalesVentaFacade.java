/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.CanalesVenta;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class CanalesVentaFacade extends AbstractFacade<CanalesVenta> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CanalesVentaFacade() {
        super(CanalesVenta.class);
    }
    
    public CanalesVenta getCanalVentaPorCodigo(String codigo) {
        Query q = getEntityManager().createNativeQuery("select * from canales_venta where cod_canal = " + codigo , CanalesVenta.class);

        System.out.println(q.toString());

        CanalesVenta canalVenta = new CanalesVenta();

        canalVenta = (CanalesVenta)q.getSingleResult();

        return canalVenta;
    }
}
