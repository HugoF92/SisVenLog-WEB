/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.CanalesVtaDepositos;
import entidad.CanalesVtaDepositosPK;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author dadob
 */
@Stateless
public class CanalesVtaDepositosFacade extends AbstractFacade<CanalesVtaDepositos> { 
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

   
    public CanalesVtaDepositosFacade() {
        super(CanalesVtaDepositos.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<CanalesVtaDepositos> obtenerDepositosPorCanalDeVenta(String lCanalVenta){
        String sql =    "SELECT c.cod_depo, d.xdesc " +
                        "FROM canales_vta_depositos as c, depositos as d " +
                        "where d.cod_depo = c.cod_depo " +
                        "and c.cod_canal = '"+lCanalVenta+"' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CanalesVtaDepositos> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            CanalesVtaDepositos c = new CanalesVtaDepositos();
            CanalesVtaDepositosPK canalesVentasPK = new CanalesVtaDepositosPK();
            canalesVentasPK.setCodDepo(resultado[0] == null ? 0 : Short.parseShort(resultado[0].toString()));
            c.setCanalesVtaDepositosPK(canalesVentasPK);
            c.setDepositoNombre(resultado[1] == null ? "" : resultado[1].toString());
            listado.add(c);
        }
        return listado;
    }
}
