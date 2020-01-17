/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Bancos;
import entidad.Bancos;
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
 * @author HOME
 */
@Stateless
public class BancosFacade extends AbstractFacade<Bancos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BancosFacade() {
        super(Bancos.class);
    }
    
    public void insertarBancos(Bancos bancos) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarBancos");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", bancos.getXdesc());
        q.setParameter("falta", bancos.getFalta());
        q.setParameter("cusuario", bancos.getCusuario());
        
        
        q.execute();

        
    }
    
    public List<Bancos> listarBancos(){
        Query q = getEntityManager().createNativeQuery("select * from bancos", Bancos.class);
        System.out.println(q.toString());
        List<Bancos> respuesta = new ArrayList<Bancos>();
        respuesta = q.getResultList();
        return respuesta;
    }

}
