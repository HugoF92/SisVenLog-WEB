/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ClientesDescuentosDto;
import entidad.Clientes;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Hugo
 */
@Stateless
public class ClientesFacade extends AbstractFacade<Clientes> {

    private static final Logger LOGGER = Logger.getLogger(ClientesFacade.class.getName());

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
     String msg = "";

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
                + "where ( CAST(cod_cliente as varchar) like  '%"+filtro+"%'\n"
                + "        or xnombre like '%"+filtro+"%'\n"
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
     
     public List<Clientes> buscarPorNombre(String nombreCliente){
        List<Clientes> respuesta = null;
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes\n"
                + "where upper(xnombre) like '%"+nombreCliente.toUpperCase()+"%'", Clientes.class);
        
        respuesta = q.getResultList();
        if(respuesta.size() <= 0){
            return null;
        }else{
            return respuesta;
        }
   
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
        List<Clientes> respuesta = new ArrayList<Clientes>();
        respuesta = q.getResultList();
        return respuesta;
    }
    
    public List<Clientes> getListaClientesPorFechaAltaDesc() {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from clientes order by falta desc ", Clientes.class);

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
    
    public String remover(Clientes entity) {
        Object respuesta = 0;
        Query query = null;
        try {
            System.out.println(entity.toString());
            if (!getEntityManager().contains(entity)) {
                entity = getEntityManager().merge(entity);
            }
            query = getEntityManager().createNativeQuery("delete from clientes where cod_cliente=? ");
            query.setParameter(1, entity.getCodCliente());
            int a = query.executeUpdate();
            System.out.println(a);
            //getEntityManager().remove(entity);
            this.getEntityManager().flush();
        } catch (PersistenceException p) {
            p.printStackTrace();
            System.out.println("---------------PersistenceException-------------");
            return p.getMessage();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("---------------Exception-------------");
            return e.getMessage();

        }
        return "Eliminado con exito";
    }
    
     public Integer tmpClientesCodNuevo(String cliCod, short codVendedor){
        try{
            Query q = getEntityManager().createNativeQuery(" select codnuevo  from tmp_clientes where clicod = "+cliCod+" and cod_vendedor = "+codVendedor+" ");
            System.out.println(q.toString());
            Integer codNuevo = (Integer) q.getSingleResult();
            return codNuevo;
        }catch(NoResultException ex){
            return null;
        }
    }
    
    public BigDecimal clienteMaxDescuento(Short codSublinea,Integer codCliente){
        try{
            Query q = getEntityManager().createNativeQuery("select pdesc_max from clientes_descuentos where cod_cliente = "+codCliente
                    +" and cod_sublinea = "+codSublinea);
            System.out.println(q.toString());
            return (BigDecimal) q.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
    
    public List<ClientesDescuentosDto> descuentosHabilitadosParaClientesYSubLineas(Integer lCodCliente, Short lCodSublinea){
        String sql = "select cod_cliente, cod_sublinea, pdesc_max from clientes_descuentos where cod_cliente = "+lCodCliente+" "
                    +"and (cod_sublinea = "+lCodSublinea+" or cod_sublinea is null)";
        Query q = getEntityManager().createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<ClientesDescuentosDto> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            ClientesDescuentosDto cd = new ClientesDescuentosDto();
            cd.setCodCliente(resultado[0] == null ? Integer.parseInt("0") : Integer.parseInt(resultado[0].toString()));
            cd.setCodSublinea(resultado[1] == null ? Short.parseShort("0") : Short.parseShort(resultado[1].toString()));
            cd.setpDescMax(resultado[2] == null ? null : BigDecimal.valueOf(Long.parseLong(resultado[2].toString())));
            listado.add(cd);
        }
        return listado;
    }
    
    public Double getDeudaPedidosPendientes(Integer codCliente) {

        Double resultado = 0.0;

        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyyMMdd");
        String stringDate = DateFor.format(date);

        String sql = "CALL dbo.calc_saldo_inicial_cliente(2," + codCliente.toString() + ",'" + stringDate + "')";
        LOGGER.log(Level.INFO, "clientesFacade-getDeudaPedidosPendientes: " + sql);

        java.sql.Connection conexion = em.unwrap(java.sql.Connection.class); // unwraps the Connection class.

        try (CallableStatement cstmt = conexion.prepareCall("{? = call dbo.calc_saldo_inicial_cliente(?,?,?)}");) {
            cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);
            cstmt.setInt(1, 2);
            cstmt.setInt(2, codCliente);
            cstmt.setString(3, stringDate);
            cstmt.execute();
            resultado = (Double) cstmt.getDouble(1);

        } // Handle any errors that may have occurred.
        catch (SQLException e) {
}

        LOGGER.log(Level.INFO, "clientesFacade-getDeudaPedidosPendientes: " + String.valueOf(resultado));
        return resultado;
    }

    public Date getFechaPrimeraFactura(Integer codCliente) {

        Date resultado = null;
        int updateResultado;
        String sql = "SELECT MIN(ffactur) as fmin from facturas fl "
                + "where cod_empr=2 and cod_cliente=" + codCliente.toString() + " and (mestado='A')";
        LOGGER.log(Level.INFO, "clientesFacade-getFechaPrimeraFactura: " + sql);

        Query q = getEntityManager().createNativeQuery(sql);

        resultado = (Date) q.getSingleResult();
        LOGGER.log(Level.INFO, "clientesFacade-getFechaPrimeraFactura: " + String.valueOf(resultado));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        sql = "update clientes set fprim_fact='" + sdf.format(resultado) + "' where cod_cliente=" + codCliente.toString();
        q = getEntityManager().createNativeQuery("update clientes set fprim_fact='?' where cod_cliente=?");
        q.setParameter(1, sdf.format(resultado));
        q.setParameter(2, codCliente);

        updateResultado = q.executeUpdate();
        LOGGER.log(Level.INFO, "clientesFacade-getFechaPrimeraFactura: resultadoUpdate=" + updateResultado);

        return resultado;

    }

    public void updateDatosComerciales(Clientes clientes) {
        
        em.merge(clientes);

//        String sql = "";
//        Query q;
//        int updateResultado;
//        sql = "update clientes set "
//                + "ilimite_compra=" + String.valueOf(clientes.getIlimiteCompra()) + ","
//                + "ilimite_temp=" + String.valueOf(clientes.getiLimiteTemp()) + ","
//                + "flimite_temp=" + String.valueOf(clientes.getfLimiteTemp()) + ","
//                + "cod_grupo=" + String.valueOf(clientes.getCodGrupo()) + ","
//                + "nplazo_credito=" + String.valueOf(clientes.getNplazoCredito()) + ","
//                + "nplazo_impresion=" + String.valueOf(clientes.getNplazoImpresion()) + ","
//                + "mforma_pago='" + String.valueOf(clientes.getMformaPago()) + "'" + ","
//                + "nriesgo=" + String.valueOf(clientes.getNriesgo()) + ","
//                + "cod_banco=" + String.valueOf(clientes.getCodBanco()) + ","
//                + "xctacte='" + String.valueOf(clientes.getXctacte()) + "'" + ","
//                + "cod_canal='" + String.valueOf(clientes.getCodCanal()) + "'" + ","
//                + "ccateg_cliente='" + String.valueOf(clientes.getCcategCliente()).replace("null", "") + "'" + ","
//                + "xrazon_estado='" + String.valueOf(clientes.getXrazonEstado()).trim() + "'" + " "
//                + "where cod_cliente=" + String.valueOf(clientes.getCodCliente())
//                + " and cod_empr=2";
//        LOGGER.log(Level.INFO, "clientesFacade-updateDatosComerciales: sql=" + sql);
//         LOGGER.log(Level.INFO, "clientesFacade-updateDatosComerciales: getxrazonestado=" + String.valueOf(clientes.getXrazonEstado()));
//        q = getEntityManager().createNativeQuery(sql);
//
//        updateResultado = q.executeUpdate();
//        LOGGER.log(Level.INFO, "clientesFacade-getFechaPrimeraFactura: resultadoUpdate=" + updateResultado);

    }

}
