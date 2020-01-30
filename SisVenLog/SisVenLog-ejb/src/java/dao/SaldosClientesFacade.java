/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.SaldosClientes;
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
public class SaldosClientesFacade extends AbstractFacade<SaldosClientes> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SaldosClientesFacade() {
        super(SaldosClientes.class);
    }
    
    public List<SaldosClientes> obtenerSaldosClientesPorClienteFechaMovimiento(Integer lCodCliente, String lFechaMovim){
        String sql =    "SELECT TOP 1 * " +
                        "FROM saldos_clientes " +
                        "WHERE cod_cliente = "+lCodCliente+" "+
                        "AND cod_empr = 2 " +
                        "AND fmovim < '"+lFechaMovim+"' "+
                        "ORDER BY fmovim DESC";
        Query q = em.createNativeQuery(sql, SaldosClientes.class);
        System.out.println(q.toString());
        List<SaldosClientes> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
    
}
