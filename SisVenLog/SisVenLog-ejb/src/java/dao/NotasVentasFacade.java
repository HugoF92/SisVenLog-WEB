/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.NotaVentaDto;
import entidad.NotasVentas;
import entidad.NotasVentasPK;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class NotasVentasFacade extends AbstractFacade<NotasVentas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotasVentasFacade() {
        super(NotasVentas.class);
    }
    
    public List<NotaVentaDto> listadoDeNotasDeVentas(   Date fechaInicio,
                                                        Date fechaFin,
                                                        Character estado,
                                                        long nroDocumento,
                                                        String tipoDocumento,
                                                        Integer codigoCliente){
        
        String sql =    "SELECT p.ctipo_docum, p.fdocum, p.nro_nota, p.cconc, p.fac_ctipo_docum, p.nrofact, p.texentas, c.xnombre, p.mestado " +
                        "FROM notas_ventas p, clientes c " +
                        "WHERE p.cod_cliente = c.cod_cliente " +
                        "AND p.cod_empr = 2";
        
        if(fechaInicio != null){
            sql += " AND p.fdocum >= ?";
        }
        
        if(fechaFin != null){
            sql += " AND p.fdocum <= ?";
        }
        
        if(estado == 'A'){
            sql += " AND p.mestado != 'X'";
        }else{
            sql += " AND p.mestado = 'X'";
        }
        
        if(nroDocumento != 0){
            sql += " AND p.nro_nota = ?";
        }
        
        if(!tipoDocumento.equals("") || !tipoDocumento.isEmpty()){
            sql += " AND p.ctipo_docum = ?";
        }
        
        if(codigoCliente != null){
            if(codigoCliente > 0){
                sql += " AND p.cod_cliente = ?";
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
        
        i++;
        q.setParameter(i, estado);
        
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
        List<NotaVentaDto> listadoNotaVenta = new ArrayList<NotaVentaDto>();
        for(Object[] resultado: resultados){
            //clave primaria nota de venta
            NotasVentasPK notaVentaPk = new NotasVentasPK();
            notaVentaPk.setCodEmpr(Short.parseShort("2"));
            notaVentaPk.setCtipoDocum(resultado[0].toString());
            if(resultado[1] != null){
                Timestamp timeStamp_1 = (Timestamp) resultado[1];
                java.util.Date dateResult_1 = new Date(timeStamp_1.getTime());                                
                notaVentaPk.setFdocum(dateResult_1);
            }else{              
                notaVentaPk.setFdocum(null);
            }
            notaVentaPk.setNroNota(Long.parseLong(resultado[2].toString()));
            //nota de venta
            NotasVentas notaVenta = new NotasVentas();
            notaVenta.setNotasVentasPK(notaVentaPk);
            notaVenta.getConceptosDocumentos().getConceptosDocumentosPK().setCconc(resultado[3].toString());
            notaVenta.setFacCtipoDocum(resultado[4].toString());
            notaVenta.setNrofact(resultado[5] == null ? null : Long.parseLong(resultado[5].toString()));
            notaVenta.setTexentas(resultado[6] == null ? null : Long.parseLong(resultado[6].toString()));
            //nombre cliente
            String nombreCliente = null;
            if(resultado[7] != null){
                nombreCliente = resultado[7].toString();
            }
            notaVenta.setMestado((Character)resultado[8]);
            NotaVentaDto nvdto = new NotaVentaDto();
            nvdto.setNotaVenta(notaVenta);
            nvdto.setNombreCliente(nombreCliente);
            listadoNotaVenta.add(nvdto);
        }
        
        return listadoNotaVenta;
    }
    
    public List<NotasVentas> listadoDeNotasDeVentasPorNroFactura(Long lNroFact){
        String sql =    "SELECT * " +
                        "FROM notas_ventas " +
                        "WHERE cod_empr = 2 " +
                        "AND nrofact = "+lNroFact +
                        "AND isaldo > 0";
        Query q = em.createQuery(sql, NotasVentas.class);
        System.out.println(q.toString());
        List<NotasVentas> resultados = q.getResultList();
        return resultados;
    }
    
    public void disminuirSaldoNotasVentas(long lNroNota, long importeARestar){
        String sql =    "UPDATE notas_ventas " +
                        "SET isaldo = isaldo - "+importeARestar+
                        "WHERE cod_empr = 2 " +
                        "AND nro_nota = "+lNroNota;
        Query q = em.createQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
    public void aumentarSaldoNotasVentas(long lNroNota, long importeASumar){
        String sql =    "UPDATE notas_ventas " +
                        "SET isaldo = isaldo - "+importeASumar+
                        "WHERE cod_empr = 2 " +
                        "AND nro_nota = "+lNroNota;
        Query q = em.createQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
    public long obtenerSaldoNotasVentasMayorACero(long lNroNota){
        String sql =    "SELECT * " +
                        "FROM notas_ventas " +
                        "WHERE cod_empr = 2 " +
                        "AND nro_nota = "+lNroNota+
                        "AND isaldo > 0";
        Query q = em.createNativeQuery(sql, NotasVentas.class);
        System.out.println(q.toString());
        List<NotasVentas> resultados = q.getResultList();
        long saldo = 0;
        for(NotasVentas nv: resultados){
            saldo = nv.getIsaldo();
        }
        return saldo;
    }
    
    public long obtenerSaldoNotasVentas(long lNroNota){
        String sql =    "SELECT * " +
                        "FROM notas_ventas " +
                        "WHERE cod_empr = 2 " +
                        "AND nro_nota = "+lNroNota;
        Query q = em.createNativeQuery(sql, NotasVentas.class);
        System.out.println(q.toString());
        List<NotasVentas> resultados = q.getResultList();
        long saldo = 0;
        for(NotasVentas nv: resultados){
            saldo = nv.getIsaldo();
        }
        return saldo;
    }
    
    public List<NotaVentaDto> listadoDeNotasDeVentasPorNroNota(long lNroNota){
        String sql =    "SELECT cod_cliente, ISNULL(nrofact,0) as nro_factura, fac_ctipo_docum, " +
                        "fdocum as ffactur, ttotal, isaldo " +
                        "FROM notas_ventas WHERE cod_empr = 2 " +
                        "AND  mestado = 'A' AND nro_nota = "+lNroNota;
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<NotaVentaDto> listadoNotaVenta = new ArrayList<NotaVentaDto>();
        for(Object[] resultado: resultados){
            NotasVentasPK nvPK = new NotasVentasPK();
            NotasVentas nv = new NotasVentas();
            nv.setCodCliente(resultado[0] == null ? null : Integer.parseInt(resultado[0].toString()));
            nv.setNrofact(resultado[1] == null ? null : Long.parseLong(resultado[1].toString()));
            nv.setFacCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            if(resultado[3] != null){
                Timestamp timeStamp_3 = (Timestamp) resultado[1];
                java.util.Date dateResult_3 = new Date(timeStamp_3.getTime());                                
                nvPK.setFdocum(dateResult_3);
            }else{              
                nvPK.setFdocum(null);
            }
            nv.setTtotal(resultado[4] == null ? 0 : Long.parseLong(resultado[4].toString()));
            nv.setIsaldo(resultado[5] == null ? 0 : Long.parseLong(resultado[5].toString()));
            NotaVentaDto nvdto = new NotaVentaDto();
            nvdto.setNotaVenta(nv);
            listadoNotaVenta.add(nvdto);
        }
        return listadoNotaVenta;
    }
    
    public List<NotaVentaDto> listadoDeNotasDeVentasPorFechaNroNota(String lFDocum, long lNroNota){
        String sql =    "SELECT cod_cliente, ISNULL(nrofact,0) as nro_factura, fac_ctipo_docum, " +
                        "fdocum as ffactur, ttotal, isaldo " +
                        "FROM notas_ventas WHERE cod_empr = 2 " +
                        "AND fdocum = '"+lFDocum+"' "+
                        "AND  mestado = 'A' AND nro_nota = "+lNroNota;
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<NotaVentaDto> listadoNotaVenta = new ArrayList<NotaVentaDto>();
        for(Object[] resultado: resultados){
            NotasVentasPK nvPK = new NotasVentasPK();
            NotasVentas nv = new NotasVentas();
            nv.setCodCliente(resultado[0] == null ? null : Integer.parseInt(resultado[0].toString()));
            nv.setNrofact(resultado[1] == null ? null : Long.parseLong(resultado[1].toString()));
            nv.setFacCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            if(resultado[3] != null){
                Timestamp timeStamp_3 = (Timestamp) resultado[3];
                java.util.Date dateResult_3 = new Date(timeStamp_3.getTime());                                
                nvPK.setFdocum(dateResult_3);
            }else{              
                nvPK.setFdocum(null);
            }
            nv.setNotasVentasPK(nvPK);
            nv.setTtotal(resultado[4] == null ? 0 : Long.parseLong(resultado[4].toString()));
            nv.setIsaldo(resultado[5] == null ? 0 : Long.parseLong(resultado[5].toString()));
            NotaVentaDto nvdto = new NotaVentaDto();
            nvdto.setNotaVenta(nv);
            listadoNotaVenta.add(nvdto);
        }
        return listadoNotaVenta;
    }
    
    public List<NotaVentaDto> listadoDeNotasDeVentasConMaximaFechaPorNroNota(long lNroNota){
        String sql =    "SELECT cod_cliente, ISNULL(nrofact,0) as nro_factura, fac_ctipo_docum, " +
                        "fdocum as ffactur, ttotal, isaldo " +
                        "FROM notas_ventas WHERE cod_empr = 2 " +
                        "AND  mestado = 'A' AND nro_nota = "+lNroNota+" "+
                        "AND fdocum = (SELECT MAX(fdocum) FROM notas_ventas " +
                                      "WHERE cod_empr = 2 "+
                                      "AND mestado = 'A' "+
                                      "AND nro_nota = "+lNroNota+")";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<NotaVentaDto> listadoNotaVenta = new ArrayList<NotaVentaDto>();
        for(Object[] resultado: resultados){
            NotasVentasPK nvPK = new NotasVentasPK();
            NotasVentas nv = new NotasVentas();
            nv.setCodCliente(resultado[0] == null ? null : Integer.parseInt(resultado[0].toString()));
            nv.setNrofact(resultado[1] == null ? null : Long.parseLong(resultado[1].toString()));
            nv.setFacCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            if(resultado[3] != null){
                Timestamp timeStamp_3 = (Timestamp) resultado[3];
                java.util.Date dateResult_3 = new Date(timeStamp_3.getTime());                                
                nvPK.setFdocum(dateResult_3);
            }else{              
                nvPK.setFdocum(null);
            }
            nv.setNotasVentasPK(nvPK);
            nv.setTtotal(resultado[4] == null ? 0 : Long.parseLong(resultado[4].toString()));
            nv.setIsaldo(resultado[5] == null ? 0 : Long.parseLong(resultado[5].toString()));
            NotaVentaDto nvdto = new NotaVentaDto();
            nvdto.setNotaVenta(nv);
            listadoNotaVenta.add(nvdto);
        }
        return listadoNotaVenta;
    }
   
}
