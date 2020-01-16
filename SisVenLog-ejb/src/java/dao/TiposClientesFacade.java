/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Depositos;
import entidad.TiposClientes;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author admin
 */
@Stateless
public class TiposClientesFacade extends AbstractFacade<TiposClientes> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiposClientesFacade() {
        super(TiposClientes.class);
    }
    
    public void insertarTipoCliDeposito(TiposClientes tipoCli, Depositos depositos) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarTipoCli");
        q.registerStoredProcedureParameter("ctipo_cliente", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_depo", Short.class, ParameterMode.IN);
        
        q.setParameter("ctipo_cliente", tipoCli.getCtipoCliente());
        q.setParameter("cod_depo", depositos.getDepositosPK().getCodDepo());
        
        q.execute();

    
}
}
