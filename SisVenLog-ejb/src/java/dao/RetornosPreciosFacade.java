/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RetornosPrecios;
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
public class RetornosPreciosFacade extends AbstractFacade<RetornosPrecios> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RetornosPreciosFacade() {
        super(RetornosPrecios.class);
    }
    
     public List<RetornosPrecios> obtenerRetornosPrecios(String lCodMerca, Character lTipoVenta, String lFecha){
        String sql =    "SELECT * " +
                        "FROM retornos_precios a " +
                        "WHERE a.cod_empr = 2 " +
                        "AND upper(a.cod_merca) like upper('"+lCodMerca+"') " +
                        "AND a.ctipo_vta = '"+lTipoVenta+"' "+
                        "AND '"+lFecha+"' BETWEEN a.frige_desde AND a.frige_hasta";
        Query q = em.createNativeQuery(sql, RetornosPrecios.class);
        System.out.println(q.toString());
        return q.getResultList();
    }
    
}
