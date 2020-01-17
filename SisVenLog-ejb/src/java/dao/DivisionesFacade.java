/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Divisiones;
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
public class DivisionesFacade extends AbstractFacade<Divisiones> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DivisionesFacade() {
        super(Divisiones.class);
    }

    public List<Divisiones> listarSubdiviciones() {

        Query q = getEntityManager().createNativeQuery("SELECT DISTINCT d.cod_division, d.xdesc \n"
                + "FROM divisiones d, categorias g, lineas l, sublineas s, mercaderias m \n"
                + " WHERE d.cod_division = g.cod_division   AND g.cod_categoria = l.cod_categoria \n"
                + " AND l.cod_linea = s.cod_linea 						 \n"
                + "  AND s.cod_sublinea = m.cod_sublinea \n"
                + "  AND m.mestado = 'A' 						\n"
                + "ORDER BY d.xdesc", Divisiones.class);

        //System.out.println(q.toString());
        List<Divisiones> respuesta = new ArrayList<Divisiones>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public void insertarDivisiones(Divisiones divisiones) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarDivisiones");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);

        q.setParameter("xdesc", divisiones.getXdesc());
        q.setParameter("falta", divisiones.getFalta());
        q.setParameter("cusuario", divisiones.getCusuario());

        q.execute();

    }
}
