/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Transportistas;
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

@Stateless
public class TransportistasFacade extends AbstractFacade<Transportistas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TransportistasFacade() {
        super(Transportistas.class);
    }

    public Transportistas buscarPorCodigo(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from transportistas\n"
                + "where cod_transp =  " + filtro + " ", Transportistas.class);

        //System.out.println(q.toString());
        Transportistas respuesta = new Transportistas();

        if (q.getResultList().size() <= 0) {
            respuesta = null;
        } else {
            respuesta = (Transportistas) q.getSingleResult();
        }

        return respuesta;
    }

    public List<Transportistas> buscarPorFiltro(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from transportistas\n"
                + "where (xtransp like '%" + filtro + "%'\n"
                + "	   or xruc like '%" + filtro + "%')", Transportistas.class);

        //System.out.println(q.toString());
        List<Transportistas> respuesta = new ArrayList<Transportistas>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public void insertarTransportistas(Transportistas transportistas) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarTransportistas");
        q.registerStoredProcedureParameter("xtransp", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xruc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xdomicilio", String.class, ParameterMode.IN);

        q.setParameter("xtransp", transportistas.getXtransp());
        q.setParameter("falta", transportistas.getFalta());
        q.setParameter("cusuario", transportistas.getCusuario());
        q.setParameter("xruc", transportistas.getXruc());
        q.setParameter("xdomicilio", transportistas.getXdomicilio());

        q.execute();

    }
    public List<Transportistas> listaTransportistasPorDeposito(Short cod_depo) {
        Query q = getEntityManager().createNativeQuery("select t.*\n"
                + "from transportistas t\n"
                + "inner join depositos d on d.cod_transp=t.cod_transp\n"
                + "where d.cod_depo=" + Short.toString(cod_depo), Transportistas.class);

        System.out.println(q.toString());

        List<Transportistas> respuesta = new ArrayList<Transportistas>();

        respuesta = q.getResultList();

        return respuesta;
    }
    

}
