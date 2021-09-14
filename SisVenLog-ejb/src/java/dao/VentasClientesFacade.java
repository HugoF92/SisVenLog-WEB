/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import entidad.VentasClientes;
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
public class VentasClientesFacade extends AbstractFacade<VentasClientes> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VentasClientesFacade() {
        super(VentasClientes.class);
    }
    
    public List<VentasClientes> getVentasClientesCodCliente(Clientes cliente){
    
        List<VentasClientes> resultado = em
                .createNamedQuery("VentasClientes.findByCodCliente")
                .setParameter("codCliente", cliente.getCodCliente())
                .getResultList();
        
        return resultado;
    
    }



    public void guardarVentasClientes(List<VentasClientes> ventasClientesGuardar, Clientes clientes) {
        
        Query query = em.createQuery(
                "DELETE FROM VentasClientes c WHERE c.ventasClientesPK.codCliente = :p");
        int deletedCount = query.setParameter("p", clientes.getCodCliente()).executeUpdate();
        
        for (VentasClientes elemento: ventasClientesGuardar){
        
            em.persist(elemento);
        
        }
        
    }
    
}
