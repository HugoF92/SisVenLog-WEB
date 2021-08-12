/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Hugo
 */
@Stateless
public class ClientesFacade extends AbstractFacade<Clientes> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientesFacade() {
        super(Clientes.class);
    }

    public List<Clientes> buscarPorFiltro(String filtro) {

        Query q = getEntityManager().createNativeQuery("select * from clientes "
                + "where xnombre like ?1 "
                + "or xcedula like ?1 "
                + "or xruc like ?1 "
                + "or cod_cliente like ?1", Clientes.class);

        //System.out.println(q.toString());
        List<Clientes> respuesta = q.setParameter(1, "%" + filtro + "%").getResultList();

        return respuesta;
    }

    public Clientes buscarPorCodigo(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes\n"
                + "where cod_cliente =  " + filtro + " ", Clientes.class);

        //System.out.println(q.toString());
        Clientes respuesta;

        if (q.getResultList().size() <= 0) {
            respuesta = null;
        } else {
            respuesta = (Clientes) q.getSingleResult();
        }

        return respuesta;
    }

    public List<Clientes> buscarPorCodigoNombre(Integer codigoCliente, String nombreCliente) {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes\n"
                + "where cod_cliente = " + codigoCliente + "\n"
                + "and upper(xnombre) like '%" + nombreCliente.toUpperCase() + "%'", Clientes.class);
        List<Clientes> respuesta = q.getResultList();
        if (respuesta.size() <= 0) {
            return null;
        } else {
            return respuesta;
        }

    }

    public List<Clientes> buscarClientesActivos() {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes where mestado = 'A' ORDER BY xnombre ", Clientes.class);

        System.out.println(q.toString());
        List<Clientes> respuesta = q.getResultList();
        return respuesta;
    }

    public BigDecimal clienteMaxDescuento(Short codSublinea, String codCliente) {
        try {
            Query q = getEntityManager().createNativeQuery("select pdesc_max from clientes_descuentos where cod_cliente = " + codCliente
                    + " and cod_sublinea = " + codSublinea);
            System.out.println(q.toString());
            return (BigDecimal) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
