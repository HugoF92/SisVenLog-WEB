/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.NotaVentaDto;
import entidad.ConceptosDocumentos;
import entidad.ConceptosDocumentosPK;
import entidad.NotasVentas;
import entidad.NotasVentasPK;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;

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
        
    /*desde aca*/
    public long obtenerMaximoNroNota(){
        String sql =    "SELECT MAX(nro_nota) as maxi " +
                        "FROM notas_ventas " +
                        "WHERE cod_empr = 2 " +
                        "AND nro_nota like '101%' " +
                        "AND fdocum >= '20130601'";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<BigDecimal> resultados = q.getResultList();
        long respuesta = 0;
        for(BigDecimal resultado: resultados){
            if(resultado != null){
                respuesta = Long.parseLong(resultado.toString());
            }
        }
        return respuesta;
    }
    
    public void borrarNotaVenta(long lNroNota, String lCTipoDocum, String fdocum){
        String sql =    " DELETE FROM notas_ventas " +
                        " WHERE cod_empr = 2 AND nro_nota = "+lNroNota+" "+
                        " AND ctipo_docum = '"+lCTipoDocum+"' "+
                        " AND fdocum = '"+fdocum+"' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
    public void anularNotaVenta(String lCTipoDocum, long lNroNota, String fdocum){
        String sql =    " UPDATE notas_ventas SET mestado = 'X' " +
                        " WHERE cod_empr = 2 AND ctipo_docum = '"+lCTipoDocum+"' "+
                        " AND nro_nota = "+lNroNota +
                        " AND fdocum = '"+fdocum+"' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
    public void insertarNotaVenta(NotasVentas notaVenta){
        Integer lCodigoCliente = notaVenta.getCodCliente() == 0 ? null : notaVenta.getCodCliente();
        String lCConc = (notaVenta.getConceptosDocumentos().getConceptosDocumentosPK().getCconc().equals("") || notaVenta.getConceptosDocumentos().getConceptosDocumentosPK().getCconc() == null) ? null : "'"+notaVenta.getConceptosDocumentos().getConceptosDocumentosPK().getCconc()+"'";
        Long lNroFactura = (notaVenta.getNrofact() == null) ? null : notaVenta.getNrofact();
        String lFacTipoDocum = ("".equals(notaVenta.getFacCtipoDocum()) || notaVenta.getFacCtipoDocum() == null) ? null : "'"+notaVenta.getFacCtipoDocum()+"'";
        Long lNroPromo = (notaVenta.getNroPromo() == null) ? null : notaVenta.getNroPromo();
        String lFFacturaStr = notaVenta.getFfactur() == null ? null : "'"+DateUtil.dateToString(notaVenta.getFfactur())+"'";
        String sql =    "INSERT INTO notas_ventas (cod_empr, ctipo_docum, nro_nota, " +
                        "fdocum, xobs, mestado, cod_depo, cod_cliente, cconc, nrofact, fac_ctipo_docum, " +
                        "texentas, tgravadas, timpuestos, ttotal, isaldo, cod_entregador, NRO_PROMO, xnro_nota, ffactur) values ( " +
                        "2, '"+notaVenta.getNotasVentasPK().getCtipoDocum()+"', "+notaVenta.getNotasVentasPK().getNroNota()+", '"+DateUtil.dateToString(notaVenta.getNotasVentasPK().getFdocum())+"', '"+notaVenta.getXobs()+"', 'A', " +
                        ""+notaVenta.getCodDepo()+", "+lCodigoCliente+", "+lCConc+", "+lNroFactura+", "+lFacTipoDocum+", " +
                        ""+notaVenta.getTexentas()+", "+notaVenta.getTgravadas()+", "+notaVenta.getTimpuestos()+", "+notaVenta.getTtotal()+", "+notaVenta.getIsaldo()+", null, "+lNroPromo+", '"+notaVenta.getXnroNota()+"', "+lFFacturaStr+" )";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public List<NotasVentas> obtenerNotasVentasPorNroYTpDoc(long lNroNota, String tipoDoc, String fdocum){
        String sql = "SELECT * FROM notas_ventas " 
                    + " WHERE cod_empr = 2 and nro_nota = "+lNroNota 
                    + " and ctipo_docum = '"+tipoDoc+"' ";
        if (fdocum == null){
            sql += " and fdocum = ( select max(fdocum) FROM notas_ventas vv "
                    + "             WHERE vv.cod_empr = 2 and vv.nro_nota = "+lNroNota
                    + "             and vv.ctipo_docum = '"+tipoDoc+"' ) ";
        } else 
            sql += " and fdocum >= '"+fdocum+"' ";
        
        System.out.println(sql);
        Query q = em.createNativeQuery(sql, NotasVentas.class);        
        List<NotasVentas> resultados = q.getResultList();
        return resultados; 
    }
    
    public List<NotasVentas> buscarNotaVentaPorConceptoYRango(int[] range, String concepto, String ccocn){
        String sql =    "SELECT p.ctipo_docum, p.fdocum, p.nro_nota, p.cconc, " +
                        "p.fac_ctipo_docum, p.nrofact, c.cod_cliente, c.xnombre, p.mestado, t.xdesc, d.xdesc, p.ttotal " +
                        "FROM notas_ventas p, clientes c, tipos_documentos t, conceptos_documentos d " +
                        "WHERE p.cod_cliente = c.cod_cliente " +
                        "AND p.ctipo_docum = d.ctipo_docum " +
                        "AND p.cconc = d.cconc " +
                        "AND p.ctipo_docum = t.ctipo_docum " +
                        "AND p.cod_empr = 2 "+
                        "AND p.ctipo_docum in ('"+concepto+"') "+
                        "AND d.cconc in ("+ccocn+") "+
                        "ORDER BY p.fdocum DESC";
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);
        List<Object[]> resultados = q.getResultList();
        List<NotasVentas> listadoNotaVenta = new ArrayList<>();
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
            ConceptosDocumentosPK conceptosDocumentosPK = new ConceptosDocumentosPK();
            ConceptosDocumentos conceptos = new ConceptosDocumentos();
            conceptosDocumentosPK.setCconc(resultado[3].toString());
            conceptos.setConceptosDocumentosPK(conceptosDocumentosPK);
            notaVenta.setConceptosDocumentos(conceptos);
            notaVenta.setFacCtipoDocum(resultado[4].toString());
            notaVenta.setNrofact(resultado[5] == null ? null : Long.parseLong(resultado[5].toString()));
            notaVenta.setCodCliente(Integer.parseInt(resultado[6].toString()));
            //nombre cliente
            String nombreCliente = null;
            if(resultado[7] != null){
                nombreCliente = resultado[7].toString();
            }
            char estado = resultado[8] == null ? 0 : resultado[8].toString().charAt(0);
            notaVenta.setMestado(estado);
            notaVenta.setTtotal(resultado[11] == null ? null : Long.parseLong(resultado[11].toString()));
            notaVenta.setNombreCliente(nombreCliente);
            notaVenta.getNotasVentasPK().setDescripcionTipoDocumento(resultado[9] == null ? null : resultado[9].toString());
            notaVenta.getConceptosDocumentos().setXdesc(resultado[10] == null ? null : resultado[10].toString());
            listadoNotaVenta.add(notaVenta);
        }
        
        return listadoNotaVenta;
        
    }
    
    public List<NotasVentas> buscarNotaVentaPorConceptoYRango( String lFDocum,
                                                        Integer lCodCliente,
                                                        String lNombreCliente,
                                                        long lMonto,
                                                        long lNroNota,
                                                        int[] range,
                                                        String concepto,
                                                        String cconc){
        String sql = "SELECT p.ctipo_docum, p.fdocum, p.nro_nota, p.cconc, " +
                        "p.fac_ctipo_docum, p.nrofact, c.cod_cliente, c.xnombre, p.mestado, t.xdesc, d.xdesc, p.ttotal " +
                        "FROM notas_ventas p, clientes c, tipos_documentos t, conceptos_documentos d " +
                        "WHERE p.cod_cliente = c.cod_cliente " +
                        "AND p.ctipo_docum = d.ctipo_docum " +
                        "AND p.cconc = d.cconc " +
                        "AND p.ctipo_docum = t.ctipo_docum " +
                        "AND p.cod_empr = 2 "+
                        "AND p.ctipo_docum in ('"+concepto+"')"+
                        "AND d.cconc in ("+cconc+")"
                ;
        
        if(!"".equals(lFDocum)){
            Date fDocumDate = DateUtil.formaterStringToDate(lFDocum);
            String nuevaFechaDocum = DateUtil.dateToString(fDocumDate);
            sql += "AND p.fdocum = '"+nuevaFechaDocum+"' ";
        }
        
        if(lNroNota != 0){
            sql += "AND p.nro_nota like '%"+lNroNota+"%' ";
        }
        
        if(!"".equals(lNombreCliente)){
            sql += "and upper(c.xnombre) like '%"+lNombreCliente.toUpperCase()+"%' ";
        }
        
        if(lCodCliente != null){
            if(lCodCliente != 0){
                sql += "AND p.cod_cliente = "+lCodCliente+" ";
            }
        }
        
        if(lMonto != 0){
            sql += "AND p.ttotal = "+lMonto+" ";
        }
        
        sql += "ORDER BY p.fdocum DESC";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);
        List<Object[]> resultados = q.getResultList();
        List<NotasVentas> listadoNotaVenta = new ArrayList<>();
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
            ConceptosDocumentosPK conceptosDocumentosPK = new ConceptosDocumentosPK();
            ConceptosDocumentos conceptos = new ConceptosDocumentos();
            conceptosDocumentosPK.setCconc(resultado[3].toString());
            conceptos.setConceptosDocumentosPK(conceptosDocumentosPK);
            notaVenta.setConceptosDocumentos(conceptos);
            notaVenta.setFacCtipoDocum(resultado[4].toString());
            notaVenta.setNrofact(resultado[5] == null ? null : Long.parseLong(resultado[5].toString()));
            notaVenta.setCodCliente(Integer.parseInt(resultado[6].toString()));
            //nombre cliente
            String nombreCliente = null;
            if(resultado[7] != null){
                nombreCliente = resultado[7].toString();
            }
            char estado = resultado[8] == null ? 0 : resultado[8].toString().charAt(0);
            notaVenta.setMestado(estado);
            notaVenta.setTtotal(resultado[11] == null ? null : Long.parseLong(resultado[11].toString()));
            notaVenta.setNombreCliente(nombreCliente);
            notaVenta.getNotasVentasPK().setDescripcionTipoDocumento(resultado[9] == null ? null : resultado[9].toString());
            notaVenta.getConceptosDocumentos().setXdesc(resultado[10] == null ? null : resultado[10].toString());
            listadoNotaVenta.add(notaVenta);
        }
        return listadoNotaVenta;
    }
    
    
    public NotasVentas buscarNotaPorId(NotasVentasPK notaVentaPk){
        String sql = "SELECT f " +
                    " FROM NotasVentas f left join fetch f.notasVentasDetCollection d join fetch f.conceptosDocumentos left join fetch f.empleados left join fetch d.mercaderia " +
                    " WHERE f.notasVentasPK = :notaVentaPk " ;
        Query q = em.createQuery(sql)
                .setParameter("notaVentaPk", notaVentaPk);
        System.out.println(q.toString());
        NotasVentas resultado = (NotasVentas)q.getSingleResult();
        return resultado;
    }
   
}
