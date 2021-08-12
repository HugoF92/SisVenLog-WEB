/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Empleados;
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
 * @author Hugo
 */
@Stateless
public class EmpleadosFacade extends AbstractFacade<Empleados> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpleadosFacade() {
        super(Empleados.class);
    }

    public List<Empleados> listarEmpleadosActivos() {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM empleados  "
                + "WHERE ctipo_emp LIKE 'V%' "
                + "		 AND mestado = 'A' ", Empleados.class);

        System.out.println(q.toString());

        List<Empleados> respuesta = q.getResultList();

        return respuesta;
    }

    public List<Empleados> listarEntregador() {
        Query q = getEntityManager().createNativeQuery("SELECT * \n"
                + " FROM empleados	\n"
                + " WHERE ctipo_emp LIKE 'E%' \n"
                + " AND mestado = 'A' order by xnombre", Empleados.class);

        System.out.println(q.toString());

        List<Empleados> respuesta = q.getResultList();

        return respuesta;
    }

    public Empleados getNombreEmpleado(Integer codigo) {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from empleados\n"
                + "where cod_empleado = " + codigo, Empleados.class);

        System.out.println(q.toString());

        Empleados respuesta = (Empleados) q.getSingleResult();

        return respuesta;
    }

    public List<Empleados> listarEmpleados() {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM empleados  "
                + "WHERE mestado = 'A' order by xnombre asc", Empleados.class);

        System.out.println(q.toString());

        List<Empleados> respuesta = q.getResultList();

        return respuesta;
    }

    public void insertarEmpleados(Empleados empleados) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarEmpleados");
        q.registerStoredProcedureParameter("xnombre", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_depo", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ctipo_emp", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("mestado", Character.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xnro_hand", String.class, ParameterMode.IN);

        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);

        q.setParameter("xnombre", empleados.getXnombre());
        q.setParameter("cod_depo", empleados.getCodDepo());
        q.setParameter("ctipo_emp", empleados.getCtipoEmp().getCtipoEmp());
        q.setParameter("mestado", empleados.getMestado());
        q.setParameter("xnro_hand", empleados.getXnroHand());

        q.setParameter("falta", empleados.getFalta());
        q.setParameter("cusuario", empleados.getCusuario());

        q.execute();

    }

    public Empleados getNombreEmpleadoEntregador(short codigoDeposito) {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from empleados\n"
                + "where ctipo_emp = 'ER' and mestado = 'A' and cod_depo = " + codigoDeposito, Empleados.class);

        System.out.println(q.toString());

        Empleados respuesta = (Empleados) q.getSingleResult();

        return respuesta;
    }

    public List<Empleados> getEmpleadosVendedoresActivosPorCodEmp(Integer codEmpr) {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM empleados  WHERE cod_empr = " + codEmpr
                + " and mestado ='A' AND CTIPO_EMP LIKE 'V%'", Empleados.class);
        System.out.println(q.toString());
        List<Empleados> resp = q.getResultList();
        return resp;
    }

    public List<Empleados> listarEntregadorPorDeposito(Short cod_depo) {
        Query q = getEntityManager().createNativeQuery("SELECT e.* FROM empleados e\n"
                + " inner join depositos d on e.cod_depo=d.cod_depo\n"
                + " WHERE ctipo_emp LIKE 'E%' \n"
                + " AND mestado = 'A' \n"
                + " and e.cod_depo=" + Short.toString(cod_depo), Empleados.class);

        System.out.println(q.toString());

        List<Empleados> respuesta = q.getResultList();

        return respuesta;
    }

}
