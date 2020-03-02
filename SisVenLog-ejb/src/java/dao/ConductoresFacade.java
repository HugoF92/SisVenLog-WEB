/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Conductores;
import entidad.Conductores;
import entidad.Transportistas;
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
 * @author Hugo
 */
@Stateless
public class ConductoresFacade extends AbstractFacade<Conductores> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConductoresFacade() {
        super(Conductores.class);
    }
    
    public Conductores buscarPorCodigo(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from conductores\n"
                + "where cod_conductor =  " + filtro + " ", Conductores.class);

        //System.out.println(q.toString());
        Conductores respuesta = new Conductores();
        
        if (q.getResultList().size() <= 0) {
            respuesta = null;
        }else{
            respuesta = (Conductores) q.getSingleResult();
        }

        

        return respuesta;
    }

    public List<Conductores> buscarPorFiltro(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from conductores\n"
                + "where (xconductor like '%"+filtro+"%'\n"
                + "	   or xdocum like '%"+filtro+"%')", Conductores.class);

        //System.out.println(q.toString());
        List<Conductores> respuesta = new ArrayList<Conductores>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public void insertarConductores(Conductores conductores) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarConductores");
        q.registerStoredProcedureParameter("xconductor", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xdocum", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xdomicilio", String.class, ParameterMode.IN);

        q.setParameter("xconductor", conductores.getXconductor());
        q.setParameter("falta", conductores.getFalta());
        q.setParameter("cusuario", conductores.getCusuario());
        q.setParameter("xdocum", conductores.getXdocum());
        q.setParameter("xdomicilio", conductores.getXdomicilio());

        q.execute();

    }
    
    public List<Conductores> listaConductoresPorDeposito(Short cod_depo) {
        Query q = getEntityManager().createNativeQuery("select c.* from conductores c \n"
                + "inner join depositos d on c.cod_conductor=d.cod_conductor\n"
                + "where d.cod_depo=" + Short.toString(cod_depo), Conductores.class);

        System.out.println(q.toString());

        List<Conductores> respuesta = new ArrayList<Conductores>();

        respuesta = q.getResultList();

        return respuesta;
    }

}
