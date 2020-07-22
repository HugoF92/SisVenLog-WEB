/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.CanalesVenta;
import java.util.ArrayList;
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
        Query q = getEntityManager().createNativeQuery("select * from canales_venta where cod_canal = '" + codigo + "'" , CanalesVenta.class);

        System.out.println(q.toString());

        CanalesVenta canalVenta = new CanalesVenta();

        canalVenta = (CanalesVenta)q.getSingleResult();

        return canalVenta;
    }
    
    //JLVC 15-04-2020
    public CanalesVenta buscarPorCodigo(String filtro) {

        Query q = getEntityManager().createNativeQuery("select * from canales_venta where cod_canal =  upper('" + filtro + "') ", CanalesVenta.class);

        //System.out.println(q.toString());
        CanalesVenta respuesta = new CanalesVenta();

        if (q.getResultList().size() <= 0) {
            respuesta = null;
        } else {
            respuesta = (CanalesVenta) q.getSingleResult();
        }

        return respuesta;
    }
    
    public List<CanalesVenta> listarCanalesOrdenadoXDesc(){
        Query q = getEntityManager().createNativeQuery("SELECT c.* FROM canales_venta c " +
                " order by c.xdesc ", CanalesVenta.class);
        System.out.println(q.toString());
        List<CanalesVenta> resp = new ArrayList<>();
        resp = q.getResultList();
        return resp;
    }
    
    public CanalesVenta getCanalVentaFromList(CanalesVenta pk, List<CanalesVenta> lista){
        return lista.stream()
                .filter(obj -> obj.getCodCanal().equals(pk.getCodCanal()))
                .findAny().orElse(null);
    }
    
}
