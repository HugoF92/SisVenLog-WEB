/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FacturaDto;
import entidad.Facturas;
import entidad.FacturasPK;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class FacturasFacade extends AbstractFacade<Facturas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @EJB
    private DepositosFacade depositosFacade;
    
    @EJB
    private EmpleadosFacade empleadosFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturasFacade() {
        super(Facturas.class);
    }
    
    public List<FacturaDto> listadoDeFacturas(  Date fechaInicio,
                                                Date fechaFin,
                                                Character estado,
                                                long nroDocumento,
                                                String tipoDocumento,
                                                Integer codigoCliente){
        
        String sql =    "SELECT f.nrofact, f.ctipo_docum, f.ffactur, f.cod_ruta, f.mestado, f.fvenc, f.texentas, f.tgravadas, f.timpuestos, "+
                        "f.tdescuentos, f.ttotal, f.isaldo, f.cod_depo, f.cod_vendedor, d.xdesc as xdepo, e.xnombre as xvendedor, v.xdesc as xcanal " +
                        "FROM facturas f, Depositos d, empleados e, canales_venta v " +
                        "WHERE f.cod_depo = d.cod_depo " +
                        "AND f.cod_empr = 2 " +
                        "AND f.cod_vendedor = e.cod_empleado " +
                        "AND f.cod_canal = v.cod_canal";
        
        if(fechaInicio != null){
            sql += " AND f.ffactur >= ?";
        }
        
        if(fechaFin != null){
            sql += " AND f.ffactur <= ?";
        }
        
        if(estado != 0){
            sql += " AND f.mestado = ?";
        }
        
        if(nroDocumento != 0){
            sql += " AND f.nrofact = ?";
        }
        
        if(!tipoDocumento.equals("") || !tipoDocumento.isEmpty()){
            sql += " AND f.ctipo_docum = ?";
        }
        
        if(codigoCliente != null){
            if(codigoCliente > 0){
                sql += " AND f.cod_cliente = ?";
            }
        }
        
        Query q = em.createNativeQuery(sql);
        
        int i = 1;
        if(fechaInicio != null){
            i++;
            q.setParameter(i, fechaInicio);
        }
        
        if(fechaFin != null){
            i++;
            q.setParameter(i, fechaFin);
        }
        
        if(estado != 0){
            i++;
            q.setParameter(i, estado);
        }
        
        if(nroDocumento != 0){
            i++;
            q.setParameter(i, nroDocumento);
        }
        
        if(!tipoDocumento.equals("") || !tipoDocumento.isEmpty()){
            i++;
            q.setParameter(i, tipoDocumento);
        }
        
        if(codigoCliente != null){
            if(codigoCliente > 0){
                i++;
                q.setParameter(i, codigoCliente);
            }
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<FacturaDto> listadoFacturas = new ArrayList<FacturaDto>();
        for(Object[] resultado: resultados){
            //clave primaria de facturas
            FacturasPK facturasPk = new FacturasPK();
            facturasPk.setCodEmpr(Short.parseShort("2"));
            facturasPk.setNrofact(resultado[0] == null ? null : Long.parseLong(resultado[0].toString()));
            facturasPk.setCtipoDocum(resultado[1] == null ? null : resultado[1].toString());
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                
                facturasPk.setFfactur(dateResult_2);           
            }else{
                facturasPk.setFfactur(null); 
            }
            //facturas
            Facturas factura = new Facturas();
            factura.setFacturasPK(facturasPk);
            factura.setDepositos(resultado[12] == null ? null :  depositosFacade.find(Short.parseShort(resultado[12].toString())));
            factura.setEmpleados1(resultado[13] == null ? null :  empleadosFacade.find(Short.parseShort(resultado[13].toString())));
            //nombre deposito
            String nombreDeposito = null;
            if(resultado[14] != null){
                nombreDeposito = resultado[14].toString();
            }
            //nombre vendedor
            String nombreVendedor = null;
            if(resultado[15] != null){
                nombreVendedor = resultado[15].toString();
            }
            //nombre canal
            String nombreCanal = null;
            if(resultado[16] != null){
                nombreCanal = resultado[16].toString();
            }
            factura.setMestado((Character)resultado[4]);
            FacturaDto fdto = new FacturaDto();
            fdto.setFactura(factura);
            fdto.setNombreDeposito(nombreDeposito);
            fdto.setNombreVendedor(nombreVendedor);
            fdto.setDescripcionCanal(nombreCanal);
            listadoFacturas.add(fdto);
        }
        
        return listadoFacturas;
    }
    
    public List<FacturaDto> listadoDeFacturasPorNroPedido(long nroPedido){
        
        String sql =    "SELECT f.nrofact, f.ctipo_docum, f.ffactur, f.cod_ruta, f.mestado, f.fvenc, f.texentas, f.tgravadas, f.timpuestos, f.tdescuentos, "+
                        "f.ttotal, f.isaldo, f.cod_depo, f.cod_vendedor, d.xdesc as xdepo " +
                        "FROM facturas f, depositos d " +
                        "WHERE f.cod_empr = d.cod_empr " +
                        "AND f.cod_empr = 2 " +
                        "AND f.cod_depo = d.cod_depo";
        
        if(nroPedido != 0){
            sql += " AND f.nro_pedido = ?";
        }
        
        Query q = em.createNativeQuery(sql);
        
        int i = 1;
        if(nroPedido != 0){
            i++;
            q.setParameter(i, nroPedido);
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<FacturaDto> listadoFacturas = new ArrayList<FacturaDto>();
        for(Object[] resultado: resultados){
            //clave primaria de facturas
            FacturasPK facturasPk = new FacturasPK();
            facturasPk.setCodEmpr(Short.parseShort("2"));
            facturasPk.setNrofact(resultado[0] == null ? null : Long.parseLong(resultado[0].toString()));
            facturasPk.setCtipoDocum(resultado[1] == null ? null : resultado[1].toString());
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                
                facturasPk.setFfactur(dateResult_2);           
            }else{
                facturasPk.setFfactur(null); 
            }
            //facturas
            Facturas factura = new Facturas();
            factura.setFacturasPK(facturasPk);
            factura.setDepositos(resultado[12] == null ? null :  depositosFacade.find(Short.parseShort(resultado[12].toString())));
            factura.setEmpleados1(resultado[13] == null ? null :  empleadosFacade.find(Short.parseShort(resultado[13].toString())));
            //nombre deposito
            String nombreDeposito = null;
            if(resultado[14] != null){
                nombreDeposito = resultado[14].toString();
            }
            FacturaDto fdto = new FacturaDto();
            fdto.setFactura(factura);
            fdto.setNombreDeposito(nombreDeposito);
            listadoFacturas.add(fdto);
        }
        
        return listadoFacturas;
    }
    
    public List<Facturas> listadoDeFacturas(String cTipoDocum, String lFFactura, long nFactura){
        String sql =    "SELECT * FROM facturas WHERE cod_empr = 2 " +
                        "AND ctipo_docum = '"+cTipoDocum+"' AND FFACTUR = '"+lFFactura+"' " +
                        "AND nrofact = "+nFactura;
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        List<Facturas> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
    
    public long obtenerSaldoDeFacturas( String lFFactura,
                                        String lCTipoDocum,
                                        long lNroFact){
        String sql =    "SELECT * " +
                        "FROM facturas " +
                        "WHERE cod_empr = 2 " +
                        "AND ffactur = '"+lFFactura+"' "+
                        "AND ctipo_docum = '"+lCTipoDocum+"' "+
                        "AND nrofact = "+lNroFact;
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        List<Facturas> resultados = q.getResultList();
        long saldo = 0;
        for(Facturas f: resultados){
            saldo = f.getIsaldo();
        }
        return saldo;        
    }
    
    public List<Facturas> listadoDeFacturasActivas(String cTipoDocum, String lFFactura, long nFactura){
        String sql =    "SELECT * FROM facturas WHERE cod_empr = 2 " +
                        "AND ctipo_docum = '"+cTipoDocum+"' AND FFACTUR = '"+lFFactura+"' " +
                        "AND nrofact = "+nFactura+" "+
                        "AND mestado = 'A'";
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        List<Facturas> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
    
    public List<Facturas> listadoDeFacturasPorTipoNroFactura(String lCTipoDocum, long lNroFact){
        String sql =    "SELECT * FROM facturas " +
                        "WHERE cod_empr = 2 " +
                        "AND ctipo_docum = '"+lCTipoDocum+"' "+
                        "AND mestado = 'A' "+
                        "AND nrofact = "+lNroFact+" "+
                        "AND ffactur = (select MAX(ffactur) FROM facturas " +
                        "WHERE cod_empr = 2 "+
                        "and mestado = 'A' "+
                        "AND ctipo_docum = '"+lCTipoDocum+"' "+
                        "AND nrofact = "+lNroFact+")";
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        List<Facturas> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
    
    public List<Facturas> obtenerFacturasPorNroYFecha(long lNroFactura, String lFechaFactura){
        String sql =    "SELECT * FROM facturas " +
                        "WHERE nrofact = "+lNroFactura+" "+
                        "AND FFACTUR = '"+lFechaFactura+"' "+
                        "AND cod_empr = 2";
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        List<Facturas> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
    
    public void insertarFactura(    String lCTipoDoc,
                                    long lNroFact,
                                    Integer lCodCliente,
                                    String lCodCanal,
                                    short lCodDepo,
                                    String lCodZona,
                                    Short lCodRuta,
                                    String lFFactura,
                                    Character lCTipoVta,
                                    String lXObs,
                                    short lCodVendedor,
                                    long lTExentas,
                                    long lTGravadas,
                                    long lTImpuestos,
                                    long lTTotal,
                                    short lCodEntregador,
                                    long lISaldo,
                                    long lTDescuentos,
                                    String lXDirec,
                                    String lXRazonSocial,
                                    String lXRuc,
                                    String lXTelef,
                                    String lXCiudad,
                                    String lFVenc,
                                    String lFvencImpre,
                                    long lTGravadas10,
                                    long lTGravadas5,
                                    BigDecimal lTImpuestos10,
                                    BigDecimal lTImpuestos5,
                                    String lXFactura,
                                    short lNPlazoCheque,
                                    Long lNroPedido){
        String sql = "";
        if(lFVenc.equals("") && lFvencImpre.equals("")){
            sql =    "INSERT INTO facturas (cod_empr, ctipo_docum, nrofact, cod_cliente, cod_canal, cod_depo, cod_zona, cod_ruta, " +
                        "ffactur, ctipo_vta, xobs, cod_vendedor, mestado, texentas, tgravadas, " +
                        "timpuestos, ttotal, cod_entregador, isaldo, tdescuentos, xdirec, xrazon_social, xruc, xtelef, xciudad, fvenc, fvenc_impre, tgravadas_10, tgravadas_5, " +
                        "timpuestos_10, timpuestos_5, xfactura, nplazo_cheque, nro_pedido ) values ( " +
                        "2, '"+lCTipoDoc+"', "+lNroFact+", "+lCodCliente+", " +
                        "'"+lCodCanal+"', "+lCodDepo+", '"+lCodZona+"', "+lCodRuta+", '"+lFFactura+"', " +
                        "'"+lCTipoVta+"', '"+lXObs+"', "+lCodVendedor+", 'A', "+lTExentas+", " +
                        ""+lTGravadas+", "+lTImpuestos+", "+lTTotal+", "+lCodEntregador+", "+lISaldo+", "+lTDescuentos+", '"+lXDirec+"', " +
                        "'"+lXRazonSocial+"', '"+lXRuc+"', '"+lXTelef+"', '"+lXCiudad+"', null, null, "+lTGravadas10+", "+lTGravadas5+", " +
                        ""+lTImpuestos10+", "+lTImpuestos5+", '"+lXFactura+"', "+lNPlazoCheque+", "+lNroPedido+" )";
        }else{
            sql =    "INSERT INTO facturas (cod_empr, ctipo_docum, nrofact, cod_cliente, cod_canal, cod_depo, cod_zona, cod_ruta, " +
                        "ffactur, ctipo_vta, xobs, cod_vendedor, mestado, texentas, tgravadas, " +
                        "timpuestos, ttotal, cod_entregador, isaldo, tdescuentos, xdirec, xrazon_social, xruc, xtelef, xciudad, fvenc, fvenc_impre,  tgravadas_10, tgravadas_5, " +
                        "timpuestos_10, timpuestos_5, xfactura, nplazo_cheque, nro_pedido ) values ( " +
                        "2, '"+lCTipoDoc+"', "+lNroFact+", "+lCodCliente+", " +
                        "'"+lCodCanal+"', "+lCodDepo+", '"+lCodZona+"', "+lCodRuta+", '"+lFFactura+"', " +
                        "'"+lCTipoVta+"', '"+lXObs+"', "+lCodVendedor+", 'A', "+lTExentas+", " +
                        ""+lTGravadas+", "+lTImpuestos+", "+lTTotal+", "+lCodEntregador+", "+lISaldo+", "+lTDescuentos+", '"+lXDirec+"', " +
                        "'"+lXRazonSocial+"', '"+lXRuc+"', '"+lXTelef+"', '"+lXCiudad+"', '"+lFVenc+"', '"+lFvencImpre+"', "+lTGravadas10+", "+lTGravadas5+", " +
                        ""+lTImpuestos10+", "+lTImpuestos5+", '"+lXFactura+"', "+lNPlazoCheque+", "+lNroPedido+" )";
        }
                
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public List<Facturas> obtenerFacturasActivasPorNroPedidoYFactura(long lNroPedido, long lNroFact){
        String sql =    "SELECT * FROM facturas " + //nrofact
                        "WHERE nro_pedido = "+lNroPedido+" AND cod_empr = 2 " +
                        "AND nrofact != "+lNroFact+" AND mestado = 'A'";
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        List<Facturas> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
    
    public void actualizarFacturas( String lFAnulacion, 
                                    String lUsuario, 
                                    Short lCMotivo,
                                    String lFFact,
                                    long lNroFact,
                                    String lCTipoDocum){
        String sql =    "UPDATE facturas SET mestado = 'X', fanul = '"+lFAnulacion+"', cusuario_anul = '"+lUsuario+"', cmotivo = '"+lCMotivo+"' " +
                        "WHERE  cod_empr = 2 AND ffactur = '"+lFFact+"' AND nrofact = "+lNroFact+" " +
                        "AND ctipo_docum = '"+lCTipoDocum+"' ";
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public List<Facturas> listadoDeFacturas(String lCTipoDocum, long lNroFact){
        String sql =    "SELECT * " +
                        "FROM facturas f INNER JOIN " +
                        "depositos d ON f.cod_depo = d.cod_depo INNER JOIN " +
                        "empleados e ON f.cod_vendedor = e.cod_empleado LEFT OUTER JOIN " +
                        "empleados e2 ON f.cod_entregador = e2.cod_empleado INNER JOIN clientes c ON f.cod_cliente = c.cod_cliente " +
                        "WHERE (f.cod_empr = 2) and nrofact = "+lNroFact+" AND ctipo_docum = '"+lCTipoDocum+"' " +
                        "AND FFACTUR = (SELECT MAX(ffactur) FROM facturas " +
                        "WHERE cod_empr = 2 AND nrofact = "+lNroFact+" AND ctipo_docum = '"+lCTipoDocum+"' )";
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        List<Facturas> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
    
    public List<Facturas> listadoDeFacturas(String lCTipoDocum, long lNroFact, String lFFactura){
        String sql =    "SELECT * " +
                        "FROM facturas f INNER JOIN " +
                        "depositos d ON f.cod_depo = d.cod_depo INNER JOIN " +
                        "empleados e ON f.cod_vendedor = e.cod_empleado LEFT OUTER JOIN " +
                        "empleados e2 ON f.cod_entregador = e2.cod_empleado INNER JOIN " +
                        "clientes c ON f.cod_cliente = c.cod_cliente " +
                        "WHERE (f.cod_empr = 2) and ffactur = '"+lFFactura+"' and nrofact = "+lNroFact+" AND ctipo_docum = '"+lCTipoDocum+"' ";
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        List<Facturas> resultados = new ArrayList<>();
        resultados = q.getResultList();
        return resultados;
    }
    
    public List<Facturas> listadoDeFacturas(){
        String sql = "SELECT nrofact, ffactur, ttotal, mestado, ctipo_docum FROM facturas";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<Facturas> listadoFacturas = new ArrayList<>();
        for(Object[] resultado: resultados){
            FacturasPK facPK = new FacturasPK();
            facPK.setNrofact(Long.parseLong(resultado[0].toString()));
            if(resultado[1] != null){
                Timestamp timeStamp_1 = (Timestamp) resultado[1];
                java.util.Date dateResult_1 = new Date(timeStamp_1.getTime());                
                facPK.setFfactur(dateResult_1);           
            }else{
                facPK.setFfactur(null); 
            }
            facPK.setCtipoDocum(resultado[4] == null ? "" : resultado[4].toString());
            Facturas f = new Facturas();
            f.setFacturasPK(facPK);
            f.setTtotal(resultado[2] == null ? 0 : Long.parseLong(resultado[2].toString()));
            char cEstado = resultado[3] == null ? 0 : resultado[3].toString().charAt(0);
            f.setMestado(cEstado);
            listadoFacturas.add(f);
        }
        return listadoFacturas;
    }
    
    public List<Facturas> buscarFacturasServiciosEnUnRango(int[] range) {
        List<Facturas> resultado = new ArrayList<>();
        Query q = em.createNativeQuery("select * from facturas where ctipo_docum in ('FCS', 'FCP') and cod_empr = 2 order by nrofact desc",Facturas.class);
        System.out.println(q.toString());
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        resultado = q.getResultList();
        return resultado;
    }
    
    public int obtenerCantidadFacturasServicios(){
        String sql =    "select count(*) from facturas where ctipo_docum in ('FCS', 'FCP') " +
                        "and cod_empr = 2 ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Integer> resultados = q.getResultList();
        int respuesta = 0;
        for(Integer resultado: resultados){
            if(resultado != null){
                respuesta = Integer.parseInt(resultado.toString());
            }
        }
        return respuesta;
    }
        
    public List<Facturas> obtenerFacturasServiciosPorNroEnUnRango(long lNroFactura, int[] range){
        String sql =    "select * from facturas where ctipo_docum in ('FCS', 'FCP') " +
                        "and nrofact = "+lNroFactura+" "+
                        "and cod_empr = 2 ";
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }
    
    public int obtenerCantidadFacturasServiciosPorNro(long lNroFact){
        String sql =    "select count(*) from facturas where ctipo_docum in ('FCS', 'FCP') " +
                        "and nrofact = "+lNroFact+" "+
                        "and cod_empr = 2 ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Integer> resultados = q.getResultList();
        int respuesta = 0;
        for(Integer resultado: resultados){
            if(resultado != null){
                respuesta = Integer.parseInt(resultado.toString());
            }
        }
        return respuesta;
    }
    
    public List<Facturas> buscarFacturasClientesEnUnRango(int[] range) {
        List<Facturas> resultado = new ArrayList<>();
        Query q = em.createNativeQuery("select * from facturas where ctipo_docum in ('FCO', 'FCR') and cod_empr = 2 order by nrofact desc",Facturas.class);
        System.out.println(q.toString());
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        resultado = q.getResultList();
        return resultado;
    }
    
    public int obtenerCantidadFacturasClientes(){
        String sql = "select count(*) from facturas where ctipo_docum in ('FCO', 'FCR') and cod_empr = 2 ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Integer> resultados = q.getResultList();
        int respuesta = 0;
        for(Integer resultado: resultados){
            if(resultado != null){
                respuesta = Integer.parseInt(resultado.toString());
            }
        }
        return respuesta;
    }
        
    public List<Facturas> obtenerFacturasClientesPorNroEnUnRango(long lNroFactura, int[] range){
        String sql =    "select * from facturas where ctipo_docum in ('FCO', 'FCR') " +
                        "and nrofact = "+lNroFactura+" "+
                        "and cod_empr = 2 ";
        Query q = em.createNativeQuery(sql, Facturas.class);
        System.out.println(q.toString());
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }
    
    public int obtenerCantidadFacturasClientesPorNro(long lNroFact){
        String sql =    "select count(*) from facturas where ctipo_docum in ('FCO', 'FCR') " +
                        "and nrofact = "+lNroFact+" "+
                        "and cod_empr = 2 ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Integer> resultados = q.getResultList();
        int respuesta = 0;
        for(Integer resultado: resultados){
            if(resultado != null){
                respuesta = Integer.parseInt(resultado.toString());
            }
        }
        return respuesta;
    }
    
    public void borrarFactura(long lNroFact, String lFFactura, String lCTipoDoc){
        String sql =    "DELETE FROM facturas " +
                        "WHERE cod_empr = 2 AND nrofact = "+lNroFact+" "+
                        "AND ffactur = '"+lFFactura+"' AND ctipo_docum = '"+lCTipoDoc+"' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
    public void insertarFactura(    String lCTipoDoc,
                                    long lNroFact,
                                    Integer lCodCliente,
                                    String lFFactura,
                                    Character lCTipoVta,
                                    String lXObs,
                                    long lTExentas,
                                    long lTGravadas,
                                    long lTImpuestos,
                                    long lTTotal,
                                    long lISaldo,
                                    long lTDescuentos,
                                    String lXDirec,
                                    String lXRazonSocial,
                                    String lXRuc,
                                    String lXTelef,
                                    String lXCiudad,
                                    String lFVenc,
                                    long lTGravadas10,
                                    long lTGravadas5,
                                    BigDecimal lTImpuestos10,
                                    BigDecimal lTImpuestos5,
                                    String lXFactura){
        String sql = "";
        if(!lFVenc.equals("")){
            sql =    "INSERT INTO facturas (cod_empr, ctipo_docum, nrofact, cod_cliente, cod_canal, cod_depo, cod_zona, cod_ruta, " +
                "ffactur, ctipo_vta, xobs, cod_vendedor, mestado, texentas, tgravadas, " +
                "timpuestos, ttotal, cod_entregador, isaldo, tdescuentos, xdirec, xrazon_social, xruc, xtelef, xciudad, fvenc, fvenc_impre,  tgravadas_10, tgravadas_5, " +
                "timpuestos_10, timpuestos_5, xfactura) values ( " +
                "2, '"+lCTipoDoc+"', "+lNroFact+", "+lCodCliente+", " +
                "null, null, null, null, '"+lFFactura+"', " +
                "'"+lCTipoVta+"', '"+lXObs+"', null, 'A', "+lTExentas+", " +
                ""+lTGravadas+", "+lTImpuestos+", "+lTTotal+", null, "+lISaldo+", "+lTDescuentos+", '"+lXDirec+"', " +
                "'"+lXRazonSocial+"', '"+lXRuc+"', '"+lXTelef+"', '"+lXCiudad+"', '"+lFVenc+"', null, "+lTGravadas10+", "+lTGravadas5+", " +
                ""+lTImpuestos10+", "+lTImpuestos5+", '"+lXFactura+"')";
        }else{
            sql =    "INSERT INTO facturas (cod_empr, ctipo_docum, nrofact, cod_cliente, cod_canal, cod_depo, cod_zona, cod_ruta, " +
                "ffactur, ctipo_vta, xobs, cod_vendedor, mestado, texentas, tgravadas, " +
                "timpuestos, ttotal, cod_entregador, isaldo, tdescuentos, xdirec, xrazon_social, xruc, xtelef, xciudad, fvenc, fvenc_impre,  tgravadas_10, tgravadas_5, " +
                "timpuestos_10, timpuestos_5, xfactura) values ( " +
                "2, '"+lCTipoDoc+"', "+lNroFact+", "+lCodCliente+", " +
                "null, null, null, null, '"+lFFactura+"', " +
                "'"+lCTipoVta+"', '"+lXObs+"', null, 'A', "+lTExentas+", " +
                ""+lTGravadas+", "+lTImpuestos+", "+lTTotal+", null, "+lISaldo+", "+lTDescuentos+", '"+lXDirec+"', " +
                "'"+lXRazonSocial+"', '"+lXRuc+"', '"+lXTelef+"', '"+lXCiudad+"', null, null, "+lTGravadas10+", "+lTGravadas5+", " +
                ""+lTImpuestos10+", "+lTImpuestos5+", '"+lXFactura+"')";
        }
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
    public Integer findFacturaByCodEntregadorFecha(Short codEntregador,String fecha){
        try{
            Query q = getEntityManager().createNativeQuery("select Top 1 nrofact from facturas where cod_empr = 2 and cod_entregador ="+codEntregador+
                " and ffactur =convert(smalldatetime,'"+fecha+"',102)");
            System.out.println(q.toString());
            return (Integer) q.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
    
    public Integer totaldeFacturasPorFechaEntregador(String fecha,Short codEntregador){
        try{
            Query q = getEntityManager().createNativeQuery("SELECT SUM(ttotal) as ttotal FROM facturas WHERE mestado = 'A' "
                    + "AND ffactur =convert(smalldatetime,'"+fecha+"',102) AND cod_entregador ="+codEntregador+" GROUP BY cod_zona");
            System.out.println(q.toString());
            List<Object[]> rows = q.getResultList();
            Integer sum  = 0;
            return rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(sum, Integer::sum);
        }catch(NoResultException ex){
            return 0;
        }
    }
    
    public Integer totaldeDevolucionesPorFechaEntregador(String fecha,Short codEntregador){
        try{
            Query q = getEntityManager().createNativeQuery("SELECT ISNULL(SUM(N.ttotal),0) as ttotal "
                +" FROM notas_ventas N, facturas f WHERE n.mestado = 'A' AND n.fdocum =convert(smalldatetime,'"+fecha+"',102) "
                +" AND f.cod_empr = 2 AND f.ffactur = n.ffactur AND f.ctipo_docum = n.fac_ctipo_docum AND f.nrofact = n.nrofact " 
                +" AND n.ctipo_docum = 'NCV' AND n.cconc = 'DEV' AND n.cod_empr = 2 AND n.cod_entregador = "+codEntregador
                +" AND (EXISTS (SELECT * FROM facturas f where n.fac_ctipo_docum = f.ctipo_docum AND n.nrofact = f.nrofact AND "
                +" n.ffactur = f.ffactur AND f.ctipo_docum ='FCO' AND cod_empr = 2 AND f.cod_entregador ="+codEntregador
                +" ) OR (EXISTS (SELECT * FROM facturas f where n.fac_ctipo_docum = f.ctipo_docum AND n.nrofact = f.nrofact AND "
                +" n.ffactur = f.ffactur AND f.ctipo_docum ='FCR' AND f.ffactur = convert(smalldatetime,'"+fecha+"',102) AND "
                +" cod_empr = 2 AND f.cod_entregador = "+codEntregador+" ))) GROUP BY f.cod_zona");
            System.out.println(q.toString());
            List<Object[]> rows = q.getResultList();
            Integer sum  = 0;
            return rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(sum, Integer::sum);
        }catch(NoResultException ex){
            return 0;
        }
    }
    
    public Integer totaldeCreditosPorFechaEntregador(String fecha,Short codEntregador){
        try{
            Query q = getEntityManager().createNativeQuery("SELECT ISNULL(SUM(F.ttotal),0) as ttotal FROM facturas f "
                +" WHERE f.mestado = 'A' AND cod_empr = 2 AND ffactur = convert(smalldatetime,'"+fecha+"',102) AND ctipo_docum = 'FCR' "
                +" AND cod_entregador = "+codEntregador+" and NOT EXISTS (SELECT * FROM recibos_det D, recibos r where d.nrecibo = r.nrecibo "
                +" AND r.frecibo = convert(smalldatetime,'"+fecha+"',102) AND r.mestado = 'A' AND r.cod_empr = 2 AND f.cod_cliente = r.cod_cliente"
                +" AND d.ctipo_docum = f.ctipo_docum AND d.ndocum = f.nrofact ) GROUP BY f.cod_zona");
            System.out.println(q.toString());
            List<Object[]> rows = q.getResultList();
            Integer sum  = 0;
            sum += rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(sum, Integer::sum);
            q = getEntityManager().createNativeQuery("SELECT ISNULL(SUM(f.ttotal - d.itotal),0) as itotal "
                +" FROM facturas f , recibos_det d, recibos r WHERE f.mestado = 'A' AND f.cod_empr = 2 AND "
                +" f.ffactur = convert(smalldatetime,'"+fecha+"',102) AND f.ctipo_docum = 'FCR' AND f.cod_entregador = "+codEntregador
                +" AND f.ttotal > d.itotal and d.nrecibo = r.nrecibo AND r.frecibo = convert(smalldatetime,'"+fecha+"',102) "
                +" AND r.mestado = 'A' AND r.cod_empr = 2 AND f.cod_cliente = r.cod_cliente AND d.ctipo_docum = f.ctipo_docum "
                +" AND d.ndocum = f.nrofact GROUP BY f.cod_zona");
            System.out.println(q.toString());
            rows = q.getResultList();
            sum += rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(0, Integer::sum);
            q = getEntityManager().createNativeQuery("SELECT ISNULL(SUM(N.ttotal),0) as ttotal FROM notas_ventas N, facturas f "
                +" WHERE n.mestado = 'A' AND n.cod_empr = 2 AND n.ffactur = convert(smalldatetime,'"+fecha+"',102) AND "
                +" n.fdocum = convert(smalldatetime,'"+fecha+"',102) AND n.ctipo_docum = 'NCV' AND n.fac_ctipo_docum = 'FCR' AND n.ffactur = f.ffactur "
                +" AND n.fac_ctipo_docum = f.ctipo_docum AND n.nrofact = f.nrofact AND n.cod_entregador = "+codEntregador+" and f.cod_empr = 2 "
                +" GROUP BY f.cod_zona");
            System.out.println(q.toString());
            rows = q.getResultList();
            sum += rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(0, Integer::sum);
            return sum;
        }catch(NoResultException ex){
            return 0;
        }
    }
    
    public Integer totaldeNotasOtrasPorFechaEntregador(String fecha,Short codEntregador){
        try{
            Query q = getEntityManager().createNativeQuery("SELECT ISNULL(SUM(n.ttotal),0) as ttotal "
                +" FROM notas_ventas n, facturas f WHERE n.mestado = 'A' AND n.fdocum = convert(smalldatetime,'"+fecha+"',102) "
                +" AND n.ctipo_docum = 'NCV' AND n.cconc != 'DEV' AND n.cod_empr = 2 AND n.cod_entregador = "+codEntregador+" "
                +" AND n.fac_ctipo_docum = f.ctipo_docum AND n.nrofact = f.nrofact AND f.ffactur = n.fdocum AND f.cod_empr= 2 GROUP BY f.cod_zona");
            System.out.println(q.toString());
            List<Object[]> rows = q.getResultList();
            Integer sum  = 0;
            return rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(sum, Integer::sum);
        }catch(NoResultException ex){
            return 0;
        }
    }
    
    public Integer totaldeChequesDiffPorFechaEntregador(String fecha,Short codEntregador){
        try{
            Query q = getEntityManager().createNativeQuery("SELECT ISNULL(SUM(icheque),0) as ttotal FROM cheques c "
                +" WHERE femision = convert(smalldatetime,'"+fecha+"',102) AND mtipo = 'D' AND cod_empr= 2 "
                +" AND cod_entregador = "+codEntregador+" ");
            System.out.println(q.toString());
            List<Object[]> rows = q.getResultList();
            Integer sum  = 0;
            return rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(sum, Integer::sum);
        }catch(NoResultException ex){
            return 0;
        }
    }
    
    public Integer totaldeNotasAtrasPorFechaEntregador(String fecha,Short codEntregador){
        try{
            Query q = getEntityManager().createNativeQuery("SELECT ISNULL(SUM(n.ttotal),0) as ttotal "
                +" FROM notas_ventas N, facturas f WHERE n.mestado = 'A' AND f.mestado = 'A' AND fdocum = convert(smalldatetime,'"+fecha+"',102) "
                +" AND n.ctipo_docum = 'NCV' AND n.cod_empr = 2 AND n.nrofact = f.nrofact AND n.ffactur = f.ffactur "
                +" AND n.fac_ctipo_docum = f.ctipo_docum AND f.cod_empr = 2 AND n.cod_entregador = "+codEntregador
                +" AND n.cod_entregador = f.cod_entregador AND n.fac_ctipo_docum = 'FCR' AND n.fdocum != f.ffactur GROUP BY f.cod_zona ");
            System.out.println(q.toString());
            List<Object[]> rows = q.getResultList();
            Integer sum  = 0;
            return rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(sum, Integer::sum);
        }catch(NoResultException ex){
            return 0;
        }
    }
    
    public Integer totaldePagaresPorFechaEntregador(String fecha,Short codEntregador){
        try{
            Query q = getEntityManager().createNativeQuery(" SELECT ISNULL(SUM(ipagare),0) as ipagare FROM pagares p "
                +" WHERE p.mestado = 'A' AND femision = convert(smalldatetime,'"+fecha+"',102) AND p.cod_empr = 2 "
                +" AND p.cod_entregador = "+codEntregador+" ");
            System.out.println(q.toString());
            List<Object[]> rows = q.getResultList();
            Integer sum  = 0;
            return rows.stream().map((row) -> ((BigDecimal) row[0]).intValue()).reduce(sum, Integer::sum);
        }catch(NoResultException ex){
            return 0;
        }
    }
}
