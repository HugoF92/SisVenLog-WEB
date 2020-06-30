/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.ExistenciasSaldos;
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
public class ExistenciasSaldosFacade extends AbstractFacade<ExistenciasSaldos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExistenciasSaldosFacade() {
        super(ExistenciasSaldos.class);
    }
    
    public List<ExistenciasSaldos> findExistenciasSaldos(String fInicial){
        String sql = " SELECT e.cod_depo, e.cod_merca, e.fmovim,  e.saldo_cajas + e.cant_cajas AS saldo_cajas, e.saldo_unid + e.cant_unid AS saldo_unid,"
            +" m.nrelacion FROM existencias_saldos e INNER JOIN mercaderias m ON e.cod_merca = m.cod_merca AND e.cod_empr = m.cod_empr "
            +" WHERE (e.cod_empr = 2) AND ( e.fmovim = (SELECT TOP 1 fmovim FROM existencias_saldos s WHERE s.cod_empr = 2 "
            +" AND e.cod_merca = s.cod_merca AND e.cod_depo = s.cod_depo AND e.cod_empr = s.cod_empr "
            +" AND fmovim < convert(smalldatetime,'"+fInicial+"',121) ORDER BY fmovim DESC)) "
            +" ORDER BY e.cod_depo, e.cod_merca";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        return q.getResultList();
    }
}
