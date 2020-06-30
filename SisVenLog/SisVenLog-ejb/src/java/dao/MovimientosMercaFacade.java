/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.MovimientoMercaDto;
import entidad.MovimientosMerca;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author admin
 */
@Stateless
public class MovimientosMercaFacade extends AbstractFacade<MovimientosMerca> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MovimientosMercaFacade() {
        super(MovimientosMerca.class);
    }
    
    
    public void insertarMovimientosMerca(MovimientosMerca movimientosMerca) {
        try {
            em.persist(movimientosMerca);
        } catch (ConstraintViolationException e) {
            // Aqui tira los errores de constraint
            for (ConstraintViolation actual : e.getConstraintViolations()) {
                System.out.println(actual.toString());
            }
        }
    }
    
    public List<MovimientoMercaDto> obtenerMovimientosMercaderiasPorTipoNroFechaMovimiento(String lCTipoDocum, long lNDocum, String lFFactura){
        String sql =    "SELECT COD_MERCA, SUM(cant_cajas) as cant_cajas, SUM(cant_unid) as cant_unid " +
                        "FROM movimientos_merca " +
                        "WHERE cod_empr = 2 AND ctipo_docum = '"+lCTipoDocum+"' "+
                        "AND ndocum = "+lNDocum+" and fmovim >= '"+lFFactura+"' " +
                        "GROUP BY cod_merca";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<MovimientoMercaDto> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            if(resultado != null){
                MovimientoMercaDto m = new MovimientoMercaDto();
                m.setCodMerca(resultado[0] == null ? "" : resultado[0].toString());
                m.setCantCajas(resultado[1] == null ? Long.parseLong("0") : Long.parseLong(resultado[1].toString()));
                m.setCantUnid(resultado[2] == null ? Long.parseLong("0") : Long.parseLong(resultado[2].toString()));
                listado.add(m);
            }
        }
        return listado;
    }
    
    public Date maxFechaMovimientosMerca(){
        String sql = "select max(fmovim) from movimientos_merca";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        return (Date) q.getSingleResult();
    }
   
}
