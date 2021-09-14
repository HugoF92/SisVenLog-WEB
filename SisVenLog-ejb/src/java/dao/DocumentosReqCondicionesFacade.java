/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import entidad.DocumentosReqCondiciones;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lmore
 */
@Stateless
public class DocumentosReqCondicionesFacade extends AbstractFacade<DocumentosReqCondiciones> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentosReqCondicionesFacade() {
        super(DocumentosReqCondiciones.class);
    }
    
    
    public List<DocumentosReqCondiciones> getDocumentosReqCondiciones(Clientes clientes){
        
        List<DocumentosReqCondiciones> resultado = em
                .createNamedQuery("DocumentosReqCondiciones.findByMtipoPersona")
                .setParameter("mtipoPersona",((clientes.getMtipoPersona())))
                .getResultList();
        
        
        return resultado;
    }
    
}
