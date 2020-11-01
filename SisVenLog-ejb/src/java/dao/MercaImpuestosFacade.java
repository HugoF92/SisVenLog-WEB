/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Impuestos;
import entidad.MercaImpuestos;
import java.math.BigDecimal;
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
public class MercaImpuestosFacade extends AbstractFacade<MercaImpuestos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MercaImpuestosFacade() {
        super(MercaImpuestos.class);
    }
    
    public List<Impuestos> obtenerTipoImpuestoPorMercaderia(String lCodMerca){
        String sql =    "select SUM(i.ifijo) as l_ifijo, SUM(i.pimpues/100) as lpimpues " +
                        "from merca_impuestos m, impuestos i " +
                        "where i.cod_impu = m.cod_impu " +
                        "and m.cod_empr = 2 " +
                        "and upper(m.cod_merca) like upper('"+lCodMerca+"') ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<Impuestos> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            Impuestos i = new Impuestos();
            i.setIfijo(resultado[0] == null ? Integer.parseInt("0") : Integer.parseInt(resultado[0].toString()));
            BigDecimal bd = new BigDecimal(resultado[1].toString());
            //long l = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            i.setPimpues(resultado[1] == null ? BigDecimal.ZERO : bd);
            listado.add(i);
        }
        return listado;
    }
    
    public Impuestos obtenerPorcentajeImpuestoPorMercaderiaDos(String lCodMerca){
        String sql =    "SELECT sum(i.pimpues) \n" +
                        "FROM merca_impuestos m, impuestos i " +
                        "WHERE m.cod_impu = i.cod_impu " +
                        "AND upper(m.cod_merca) like upper('"+lCodMerca+"') "+
                        "AND m.cod_empr = 2 ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        Object resultados = (Object) q.getSingleResult();
        Impuestos i = new Impuestos();
        BigDecimal bd = new BigDecimal(resultados.toString());
        i.setPimpues(bd);
        return i;
    }
    
}
