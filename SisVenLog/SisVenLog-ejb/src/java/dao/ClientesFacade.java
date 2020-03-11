/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes\n"
                + "where (xnombre like '%"+filtro+"%'\n"
                + "	   or xcedula like '%"+filtro+"%' "
                + "	   or xruc like '%"+filtro+"%' )", Clientes.class);

        //System.out.println(q.toString());
        List<Clientes> respuesta = new ArrayList<Clientes>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
     public Clientes buscarPorCodigo(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes\n"
                + "where cod_cliente =  " + filtro + " ", Clientes.class);

        //System.out.println(q.toString());
        Clientes respuesta = new Clientes();
        
        if (q.getResultList().size() <= 0) {
            respuesta = null;
        }else{
            respuesta = (Clientes) q.getSingleResult();
        }

        return respuesta;
    }
     
    public List<Clientes> buscarPorCodigoNombre(Integer codigoCliente, String nombreCliente){
        List<Clientes> respuesta = null;
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes\n"
                + "where cod_cliente = " + codigoCliente + "\n"
                + "and upper(xnombre) like '%"+nombreCliente.toUpperCase()+"%'", Clientes.class);
        respuesta = q.getResultList();
        if(respuesta.size() <= 0){
            return null;
        }else{
            return respuesta;
        }
   
    }
    
    public List<Clientes> buscarClientesActivos() {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes where mestado = 'A' ORDER BY xnombre ", Clientes.class);

        System.out.println(q.toString());
        List<Clientes> respuesta = new ArrayList<>();
        respuesta = q.getResultList();
        return respuesta;
    }
    
    
    
    public Integer getMaxId() {
       try{
           Query q = getEntityManager().createQuery("SELECT MAX(c.codCliente) from Clientes c");
           Integer respuesta = (Integer) q.getSingleResult();
           return respuesta;
       }catch(Exception e){
           e.printStackTrace();
           return null;
       }
    }
    
    @Override
    public void create(Clientes entity) {
        try {
              System.out.println(entity.toString());
              getEntityManager().persist(entity);
        } catch (ConstraintViolationException e) {
            // Aqui tira los errores de constraint
            for (ConstraintViolation actual : e.getConstraintViolations()) {
                System.out.println(actual.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
