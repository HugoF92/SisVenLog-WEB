/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.ConceptosDocumentos;
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
public class ConceptosDocumentosFacade extends AbstractFacade<ConceptosDocumentos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConceptosDocumentosFacade() {
        super(ConceptosDocumentos.class);
    }
    
    public List<ConceptosDocumentos> listarTipoDocumentoFacturaVtaCredito() {
        Query q = getEntityManager().createNativeQuery("select * "
                + " FROM conceptos_documentos "
                + " WHERE ctipo_docum IN ('NDV','NCV') ", ConceptosDocumentos.class);

        System.out.println(q.toString());

        List<ConceptosDocumentos> respuesta = new ArrayList<ConceptosDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public List<ConceptosDocumentos> obtenerConceptosPorTipoYConcepto(String tipo, String conceptos){
        String sql = "SELECT * FROM conceptos_documentos "
                + " WHERE ctipo_docum IN ("+tipo+") "
                + " AND cconc IN ("+conceptos+") ";
        Query q = getEntityManager().createNativeQuery(sql, ConceptosDocumentos.class);
        System.out.println(q.toString());
        List<ConceptosDocumentos> respuesta = new ArrayList<>();
        respuesta = q.getResultList();
        return respuesta;
    }
    
    public ConceptosDocumentos getConceptoFromList(ConceptosDocumentos pk, List<ConceptosDocumentos> lista){
        return lista.stream()
                .filter(obj -> obj.getConceptosDocumentosPK().getCtipoDocum().equals(pk.getConceptosDocumentosPK().getCtipoDocum()) && 
                        pk.getConceptosDocumentosPK().getCconc().equals(pk.getConceptosDocumentosPK().getCconc()))
                .findAny().orElse(null);
    }
    
}
