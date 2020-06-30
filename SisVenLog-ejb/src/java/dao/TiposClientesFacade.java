/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Depositos;
import entidad.Lineas;
import entidad.TiposClientes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    
    public List<TiposClientes> listarTiposClientes() {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM tipos_clientes", TiposClientes.class);

        System.out.println(q.toString());

        List<TiposClientes> respuesta = new ArrayList<TiposClientes>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    //JLVC 15-04-2020
    public TiposClientes buscarPorCodigo(String filtro) {

        Query q = getEntityManager().createNativeQuery("select * from tipos_clientes where ctipo_cliente = upper('" + filtro + "') ", TiposClientes.class);

        //System.out.println(q.toString());
        TiposClientes respuesta = new TiposClientes();

        if (q.getResultList().size() <= 0) {
            respuesta = null;
        } else {
            respuesta = (TiposClientes) q.getSingleResult();
        }

        return respuesta;
    }
}