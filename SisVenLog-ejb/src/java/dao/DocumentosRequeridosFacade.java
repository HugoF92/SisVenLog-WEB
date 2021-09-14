/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import entidad.DocumentosReqCondiciones;
import entidad.DocumentosRequeridos;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lmore
 */
@Stateless
public class DocumentosRequeridosFacade extends AbstractFacade<DocumentosRequeridos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @EJB
    DocumentosReqCondicionesFacade documentosReqCondicionesFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentosRequeridosFacade() {
        super(DocumentosRequeridos.class);
    }

    public List<DocumentosRequeridos> buscarDocumentosRequeridos(Clientes clientes, List<DocumentosReqCondiciones> drc) {
        

        List<DocumentosRequeridos> resultado = new ArrayList<DocumentosRequeridos>();
        
        for(DocumentosReqCondiciones elemento : drc){
        
            List<DocumentosRequeridos> resultadoTemp = em
                    .createNamedQuery("DocumentosRequeridos.findByCdocum")
                    .setParameter("cdocum", elemento.getDocumentosReqCondicionesPK().getCdocum())
                    .getResultList();
            resultado.addAll(resultadoTemp);
        
        }
        return resultado;
    }
    
}
