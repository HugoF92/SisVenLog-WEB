/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.EmpleadosZonas;
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
public class EmpleadosZonasFacade extends AbstractFacade<EmpleadosZonas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpleadosZonasFacade() {
        super(EmpleadosZonas.class);
    }
    
    public List<EmpleadosZonas> obtenerEmpleadosZonas(){
        String sql =    "SELECT * " +
                        "FROM empleados_zonas z, EMPLEADOS e " +
                        "WHERE z.cod_empr = 2 " +
                        "AND z.cod_empleado = e.cod_empleado " +
                        "AND e.Ctipo_emp LIKE 'V%' " +
                        "AND e.mestado = 'A'";
        Query q = em.createNativeQuery(sql, EmpleadosZonas.class);
        return q.getResultList();
    }
    
}
