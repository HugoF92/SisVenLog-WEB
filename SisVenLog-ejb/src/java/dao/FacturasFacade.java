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
import javax.persistence.EntityManager;
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
    
    @EJB
    private ClientesFacade clientesFacade;

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
    
    public List<Facturas> buscarFactuaPorNro(Long nroFactura){
        String sql = "SELECT f " +
                    " FROM Facturas f inner join fetch f.codCliente join fetch f.empleados join fetch f.empleados1 join fetch f.depositos " +
                    " WHERE f.facturasPK.codEmpr = 2 " +
                    " AND f.facturasPK.nrofact = :nroFactura "+
                    " AND f.facturasPK.ctipoDocum IN ( 'FCO', 'FCR') " +
                    " AND f.facturasPK.ffactur >= '2013-01-01'";
        
        Query q = em.createQuery(sql).setParameter("nroFactura", nroFactura);
        System.out.println(q.toString());
        List<Facturas> resultados = q.getResultList();
        return resultados;        
    }
    
    public List<Facturas> buscarFactuaPorNroYFecha(Long nroFactura, Date fechaFactura){
        String sql = "SELECT f " +
                    " FROM Facturas f join fetch f.codCliente join fetch f.empleados join fetch f.empleados1  join fetch f.depositos " +
                    " WHERE f.facturasPK.codEmpr = 2 " +
                    " AND f.facturasPK.nrofact = :nroFactura1 "+
                    " AND f.facturasPK.ctipoDocum IN ( 'FCO', 'FCR') ";
        if (fechaFactura == null){
            sql += " AND f.facturasPK.ffactur = ( SELECT max(ff.facturasPK.ffactur) FROM Facturas ff " +
                    "       WHERE ff.facturasPK.codEmpr = 2 " +
                    "           AND ff.facturasPK.nrofact = :nroFactura2 "+
                    "           AND ff.facturasPK.ctipoDocum IN ( 'FCO', 'FCR') ) ";
        } else {
            sql += " AND f.facturasPK.ffactur >= :fechaFactura ";
        }
                    
        Query q = em.createQuery(sql).setParameter("nroFactura1", nroFactura);
        
        if (fechaFactura == null) q.setParameter("nroFactura2", nroFactura);
        else q.setParameter("fechaFactura", fechaFactura);
                
        System.out.println(q.toString());
        List<Facturas> resultados = q.getResultList();
        return resultados;        
    }
    
    public void aumentarTotalNotas(long lTotal, long lNroFact, String fdocum, String lFactTipo){
        String sql =    "UPDATE facturas SET tnotas = tnotas + "+lTotal+" "+
                        " WHERE cod_empr = 2 and nrofact = "+lNroFact+" "+
                        " AND ctipo_docum = '"+lFactTipo+"' " +
                        " AND ffactur = '"+fdocum+"' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
}
