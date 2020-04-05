/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.TiposEmpleados;
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
public class TiposEmpleadosFacade extends AbstractFacade<TiposEmpleados> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiposEmpleadosFacade() {
        super(TiposEmpleados.class);
    }
    
    
    public void insertarTipoEmpleado(TiposEmpleados tipoEmpleados) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarTipoEmpleado");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", tipoEmpleados.getXdesc());
        q.setParameter("falta", tipoEmpleados.getFalta());
        q.setParameter("cusuario", tipoEmpleados.getCusuario());
        
        
        q.execute();

        
    }

}
