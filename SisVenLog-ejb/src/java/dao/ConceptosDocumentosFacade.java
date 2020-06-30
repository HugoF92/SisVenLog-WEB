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
    
    public List<ConceptosDocumentos> listarConceptoDocumentoListadoNotaCompras() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from conceptos_documentos\n"
                + "where ctipo_docum in ('NDC','NCC','NDP')", ConceptosDocumentos.class);

        System.out.println(q.toString());

        List<ConceptosDocumentos> respuesta = new ArrayList<>();

        respuesta = q.getResultList();

        return respuesta;
    }
}
