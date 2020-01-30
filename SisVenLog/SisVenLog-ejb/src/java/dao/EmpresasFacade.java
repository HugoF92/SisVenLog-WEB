/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Empresas;
import entidad.Proveedores;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author Hugo
 */
@Stateless
public class EmpresasFacade extends AbstractFacade<Empresas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresasFacade() {
        super(Empresas.class);
    }
   
    public void insertarEmpresa(Empresas empresas) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarEmpresa");
        q.registerStoredProcedureParameter("xrazon_social", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xruc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xtelef", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xdirec", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xcontacto", String.class, ParameterMode.IN);
                
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        
        q.setParameter("xrazon_social", empresas.getXrazonSocial());
        q.setParameter("xruc", empresas.getXruc());
        q.setParameter("xtelef", empresas.getXtelef());
        q.setParameter("xdirec", empresas.getXdirec());
        q.setParameter("xcontacto", empresas.getXcontacto());
                
        q.setParameter("falta", empresas.getFalta());
        q.setParameter("cusuario", empresas.getCusuario());
        
        
        q.execute();

    }
}
