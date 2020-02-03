/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RangosDocumentos;
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
    
    public RangosDocumentos getRangosDocumentosByYearDocument(Long lDocum, String lFDocum) {
        Query q = getEntityManager().createNativeQuery(" SELECT nro_docum_ini, nro_docum_fin, ntimbrado \n"
                + " FROM rangos_documentos \n"
                + " WHERE nro_docum_fin > " + lDocum.toString() + " AND YEAR(" + lFDocum + ") BETWEEN nano_inicial AND nano_final \n", RangosDocumentos.class);

        System.out.println(q.toString());

        RangosDocumentos respuesta = (RangosDocumentos) q.getSingleResult();

        return respuesta;
    }
    
}
