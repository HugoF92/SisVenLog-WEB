/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

//import entidad.Categorias;
import entidad.Bancos;
import entidad.Clientes;
import entidad.ClientesCtasBancarias;
import entidad.ClientesCtasBancariasPK;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

/**
 *
 * @author admin
 */
@Stateless
public class ClientesCtasBancariasFacade extends AbstractFacade<ClientesCtasBancarias> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    private static final Logger LOGGER = Logger.getLogger(ClientesCtasBancariasFacade.class.getName());

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientesCtasBancariasFacade() {
        super(ClientesCtasBancarias.class);
    }

    public List<TablaCuentasBancarias> getCuentasBancarias(Clientes codCliente) {

        List<TablaCuentasBancarias> resultado = new ArrayList<TablaCuentasBancarias>();

        List resultados = em.createNamedQuery("ClientesCtasBancarias.findByCodCliente")
                .setParameter("codCliente", codCliente.getCodCliente())
                .getResultList();
//        List resultados = (List) codCliente.getCtasBancarias();

        for (Object _elemento : resultados) {

            ClientesCtasBancarias elemento = (ClientesCtasBancarias) _elemento;
            TablaCuentasBancarias agregar = new TablaCuentasBancarias();
            agregar.setCodigoBanco(Integer.toString(elemento.getClientesCtasBancariasPK().getCodBanco()));
            agregar.setNroCuentaCorriente(elemento.getXctaBancaria());
            resultado.add(agregar);
        }

        return resultado;
    }

    public void mergeCtasBancarias(List<TablaCuentasBancarias> tablaCuentasBancarias, Integer codCliente, Clientes clientes, List<Bancos> listaBancos) {


        
        Query query = em.createQuery(
                "DELETE FROM ClientesCtasBancarias c WHERE c.clientesCtasBancariasPK.codCliente = :p");
        int deletedCount = query.setParameter("p", clientes.getCodCliente()).executeUpdate();
//        clientes.setCtasBancarias(new ArrayList<ClientesCtasBancarias>());
//        ArrayList<ClientesCtasBancarias> cuentasBancariasCliente = new ArrayList<ClientesCtasBancarias>();
        for (TablaCuentasBancarias elemento : tablaCuentasBancarias) {

            LOGGER.log(Level.INFO, "ctasbancariasmerge: {0}", elemento.getCodigoBanco());
            LOGGER.log(Level.INFO, "ctasbancariasmerge: {0}", elemento.getNroCuentaCorriente());
            LOGGER.log(Level.INFO, "ctasbancariasmerge: {0}", codCliente);

            ClientesCtasBancarias ccb = new ClientesCtasBancarias();
            ClientesCtasBancariasPK clave = new ClientesCtasBancariasPK();

            clave.setCodBanco(Integer.valueOf(elemento.getCodigoBanco()));
            clave.setCodCliente(codCliente);
            for (Bancos elementoBanco : listaBancos) {

                if (Objects.equals(Short.valueOf(elemento.getCodigoBanco()), elementoBanco.getCodBanco())) {
                    
//                    ClientesCtasBancariasPK clave = new ClientesCtasBancariasPK();
//                    ccb.setId(Long.MIN_VALUE);
                    clave.setCodBanco(elementoBanco.getCodBanco());
                    clave.setCodCliente(clientes.getCodCliente());
                    ccb.setXctaBancaria(elemento.getNroCuentaCorriente());
                    ccb.setClientesCtasBancariasPK(clave);
//                    cuentasBancariasCliente.add(ccb);
                    break;
                }
            }

            em.persist(ccb);
            

        }
//        clientes.setCtasBancarias(cuentasBancariasCliente);

    }

}
