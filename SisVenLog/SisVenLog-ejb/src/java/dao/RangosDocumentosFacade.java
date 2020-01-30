/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RangosDocumentos;
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
public class RangosDocumentosFacade extends AbstractFacade<RangosDocumentos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RangosDocumentosFacade() {
        super(RangosDocumentos.class);
    }
    
    public List<RangosDocumentos> obtenerRangosDeDocumentos(String lTipoDocum, Long lNroFact){
        String sql =    "SELECT * FROM rangos_documentos " +
                        "WHERE cod_empr = 2 and ctipo_docum = '"+lTipoDocum+"' "+
                        "and mtipo_papel = 'F' " +
                        "AND "+lNroFact+" BETWEEN nro_docum_ini AND nro_docum_fin";
        Query q = em.createNativeQuery(sql, RangosDocumentos.class);
        System.out.println(q.toString());
        List<RangosDocumentos> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
}
