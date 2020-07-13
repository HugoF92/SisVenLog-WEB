/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.CanalesCompra;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Hugo
 */
@Stateless
public class CanalesCompraFacade extends AbstractFacade<CanalesCompra> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CanalesCompraFacade() {
        super(CanalesCompra.class);
    }
    
    public List<CanalesCompra> listarCanalesOrdenadoXDesc(){
        Query q = getEntityManager().createNativeQuery("SELECT c.* FROM canales_compra c " +
                " where mestado = 'A' order by c.xdesc ", CanalesCompra.class);
        System.out.println(q.toString());
        List<CanalesCompra> resp = new ArrayList<>();
        resp = q.getResultList();
        return resp;
    }
    
    public CanalesCompra getCanalCompraFromList(CanalesCompra pk, List<CanalesCompra> lista){
        return lista.stream()
                .filter(obj -> obj.getCanalesCompraPK().equals(pk.getCanalesCompraPK()))
                .findAny().orElse(null);
    }
    
}
