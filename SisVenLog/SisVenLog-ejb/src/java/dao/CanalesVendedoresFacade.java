/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.CanalesVendedores;
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
public class CanalesVendedoresFacade extends AbstractFacade<CanalesVendedores> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CanalesVendedoresFacade() {
        super(CanalesVendedores.class);
    }
    
     public List<CanalesVendedores> obtenerCanalesVendedores(short lCodVendedor){
        String sql =    "SELECT * " +
                        "FROM canales_vendedores v, canales_venta c " +
                        "WHERE v.cod_canal = c.cod_canal " +
                        "AND v.cod_empr = 2 "+
                        "AND v.cod_vendedor = "+lCodVendedor;
        Query q = em.createNativeQuery(sql, CanalesVendedores.class);
        return q.getResultList();
    }
    
}
