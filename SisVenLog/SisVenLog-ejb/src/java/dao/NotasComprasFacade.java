/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.NotasComprasDto;
import entidad.Compras;
import entidad.ComprasPK;
import entidad.ConceptosDocumentosPK;
import entidad.DepositosPK;
import entidad.NotasCompras;
import entidad.NotasComprasPK;
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
public class NotasComprasFacade extends AbstractFacade<NotasCompras> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @EJB
    private ConceptosDocumentosFacade conceptosDocumentosFacade;
    
    @EJB
    private DepositosFacade depositoFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotasComprasFacade() {
        super(NotasCompras.class);
    }
    
    public List<NotasComprasDto> listadoNotasDeCreditoCompras(  String fechaInicial, 
                                                                String fechaFinal, 
                                                                String tipoDocumento, 
                                                                Character estado, 
                                                                Short codigoProveedor, 
                                                                long numeroNota){
        
        String sql = "";
        
        if (fechaInicial.equals("") && fechaFinal.equals("")) {
            sql = "SELECT f.nro_nota, f.cod_proveed, f.ctipo_docum, f.fdocum, f.texentas, f.tgravadas, f.timpuestos, f.cconc, p.xnombre, f.nrofact, f.mestado, f.cod_depo "
                    + "FROM notas_compras f, proveedores p "
                    + "WHERE f.cod_proveed = p.cod_proveed AND f.cod_empr = 2 AND f.ctipo_docum = '" + tipoDocumento + "' ";
        } else if (!fechaInicial.equals("") && fechaFinal.equals("")) {
            sql = "SELECT f.nro_nota, f.cod_proveed, f.ctipo_docum, f.fdocum, f.texentas, f.tgravadas, f.timpuestos, f.cconc, p.xnombre, f.nrofact, f.mestado, f.cod_depo "
                    + "FROM notas_compras f, proveedores p "
                    + "WHERE f.cod_proveed = p.cod_proveed AND f.cod_empr = 2 AND f.ctipo_docum = '" + tipoDocumento + "' AND f.fdocum >= '" + fechaInicial + "' ";
        } else if (fechaInicial.equals("") && !fechaFinal.equals("")) {
            sql = "SELECT f.nro_nota, f.cod_proveed, f.ctipo_docum, f.fdocum, f.texentas, f.tgravadas, f.timpuestos, f.cconc, p.xnombre, f.nrofact, f.mestado, f.cod_depo "
                    + "FROM notas_compras f, proveedores p "
                    + "WHERE f.cod_proveed = p.cod_proveed AND f.cod_empr = 2 AND f.ctipo_docum = '" + tipoDocumento + "' AND f.fdocum <= '" + fechaFinal + "' ";
        } else if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
            sql = "SELECT f.nro_nota, f.cod_proveed, f.ctipo_docum, f.fdocum, f.texentas, f.tgravadas, f.timpuestos, f.cconc, p.xnombre, f.nrofact, f.mestado, f.cod_depo "
                    + "FROM notas_compras f, proveedores p "
                    + "WHERE f.cod_proveed = p.cod_proveed AND f.cod_empr = 2 AND f.ctipo_docum = '" + tipoDocumento + "' AND f.fdocum >= '" + fechaInicial + "' AND f.fdocum <= '" + fechaFinal + "' ";
        }
        
        if(estado != 0){
            if(estado == 'A'){
                sql += " AND f.mestado != 'X'";
            }else{
                sql += " AND f.mestado = 'X'";
            }
        }
                
        if (codigoProveedor != null) {
            if (codigoProveedor > 0) {
                sql += " AND f.cod_proveed = ?";
            }
        }

        if (numeroNota != 0) {
            sql += " AND f.nro_nota = ?";
        }
        
        Query q = em.createNativeQuery(sql);
        
        int i = 0;
        if(codigoProveedor != null){
            if(codigoProveedor > 0){
                i++;
                q.setParameter(i, codigoProveedor);
            }
        }
        
        if(numeroNota != 0){
            i++;
            q.setParameter(i, numeroNota);
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<NotasComprasDto> listadoCompras = new ArrayList<>();
        for(Object[] resultado: resultados){
            NotasComprasPK notasComprasPk = new NotasComprasPK();
            notasComprasPk.setCodEmpr(Short.parseShort("2"));
            notasComprasPk.setCodProveed(Short.parseShort(resultado[1].toString()));
            notasComprasPk.setCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            if(resultado[3] != null){
                Timestamp timeStamp_3 = (Timestamp) resultado[3];
                java.util.Date dateResult_3 = new Date(timeStamp_3.getTime());                
                notasComprasPk.setFdocum(dateResult_3);
            }else{
                notasComprasPk.setFdocum(null);
            }
            notasComprasPk.setNroNota(Long.parseLong(resultado[0].toString()));
            NotasCompras notasCompras = new NotasCompras();
            notasCompras.setNotasComprasPK(notasComprasPk);
            notasCompras.setTexentas(resultado[4] == null ? 0 : Long.parseLong(resultado[4].toString()));
            notasCompras.setTgravadas(resultado[5] == null ? 0 : Long.parseLong(resultado[5].toString()));
            notasCompras.setTimpuestos(resultado[6] == null ? 0 : Long.parseLong(resultado[6].toString()));
            ConceptosDocumentosPK conceptosDocumentosPk = new ConceptosDocumentosPK();
            conceptosDocumentosPk.setCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            conceptosDocumentosPk.setCconc(resultado[7] == null ? null : resultado[7].toString());
            notasCompras.setConceptosDocumentos(resultado[7] == null ? null : conceptosDocumentosFacade.find(conceptosDocumentosPk));
            char cEstado = resultado[10] == null ? 0 : resultado[10].toString().charAt(0);
            notasCompras.setMestado(cEstado);
            //deposito
            DepositosPK depositoPk = new DepositosPK();
            depositoPk.setCodEmpr(Short.parseShort("2"));
            depositoPk.setCodDepo(resultado[11] == null ? 0 : Short.parseShort(resultado[11].toString()));
            notasCompras.setDepositos(depositoFacade.find(depositoPk));
            //nombre proveedor
            String nombreProveedor = null;
            if(resultado[8] != null){
                nombreProveedor = resultado[8].toString();
            }
            ComprasPK comprasPk = new ComprasPK();
            comprasPk.setNrofact(resultado[9] == null ? 0 : Long.parseLong(resultado[9].toString()));
            Compras compras = new Compras();
            compras.setComprasPK(comprasPk);
            notasCompras.setCompras(compras);
            NotasComprasDto ncdto = new NotasComprasDto();
            ncdto.setNotaCompra(notasCompras);
            ncdto.setNombreProveedor(nombreProveedor);
            listadoCompras.add(ncdto);            
        }
        
        return listadoCompras;
        
    }
    
    public List<NotasComprasDto> maximaCompraCreditoDelProveedor(long lNroNota, Short lCodProveed){
        String sql =    "SELECT fdocum, nro_nota, com_ctipo_docum, nrofact, ffactur, ttotal, isaldo FROM notas_compras WHERE cod_empr = 2 " +
                        "AND ctipo_docum = 'NCC' AND nro_nota = "+lNroNota+" "+
                        " AND fdocum = (SELECT MAX(fdocum) FROM notas_compras n2 " +
                        "                WHERE n2.cod_empr = 2 " +
                        "                  AND n2.nro_nota = "+lNroNota+" "+
                        "                  AND n2.cod_proveed = "+lCodProveed+" "+
                        "                  AND n2.ctipo_docum = 'NCC' " +
                        "                  AND n2.meStado ='A') " +
                        "AND cod_proveed = "+lCodProveed+" "+
                        "AND mestado = 'A'";
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<NotasComprasDto> listadoNotasCompras = new ArrayList<>();
        for(Object[] resultado: resultados){
            NotasComprasPK notasComprasPk = new NotasComprasPK();
            if(resultado[0] != null){
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());                
                notasComprasPk.setFdocum(dateResult_0);
            }else{
                notasComprasPk.setFdocum(null);
            }
            notasComprasPk.setNroNota(Long.parseLong(resultado[1].toString()));
            NotasCompras notasCompras = new NotasCompras();
            notasCompras.setNotasComprasPK(notasComprasPk);
            
            Compras c = new Compras();
            c.getComprasPK().setCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            if(resultado[4] != null){
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());                
                c.getComprasPK().setFfactur(dateResult_4);
            }else{
                c.getComprasPK().setFfactur(null);
            }
            c.getComprasPK().setNrofact(Long.parseLong(resultado[3].toString()));
            notasCompras.setCompras(c);
            notasCompras.setTtotal(Long.parseLong(resultado[5].toString()));
            notasCompras.setTtotal(Long.parseLong(resultado[6].toString()));
            NotasComprasDto ncdtp = new NotasComprasDto();
            ncdtp.setNotaCompra(notasCompras);
            listadoNotasCompras.add(ncdtp);
        }
        
        return listadoNotasCompras;
    }
    
    public List<NotasComprasDto> comprasCreditoPorNroNotaFechaProveedor(long lNroNota, Short lCodProveed, String lFDocum){
        String sql =    "SELECT fdocum, nro_nota, com_ctipo_docum, nrofact, ffactur, ttotal, isaldo FROM notas_compras WHERE cod_empr = 2 " +
                        "AND ctipo_docum = 'NCC' AND nro_nota = "+lNroNota+" "+
                        "AND cod_proveed = "+lCodProveed+" "+
                        "AND fdocum = '"+lFDocum+"' "+
                        "AND mestado ='A'";
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<NotasComprasDto> listadoNotasCompras = new ArrayList<>();
        for(Object[] resultado: resultados){
            NotasComprasPK notasComprasPk = new NotasComprasPK();
            if(resultado[0] != null){
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());                
                notasComprasPk.setFdocum(dateResult_0);
            }else{
                notasComprasPk.setFdocum(null);
            }
            notasComprasPk.setNroNota(Long.parseLong(resultado[1].toString()));
            NotasCompras notasCompras = new NotasCompras();
            notasCompras.setNotasComprasPK(notasComprasPk);
            
            Compras c = new Compras();
            c.getComprasPK().setCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            if(resultado[4] != null){
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());                
                c.getComprasPK().setFfactur(dateResult_4);
            }else{
                c.getComprasPK().setFfactur(null);
            }
            c.getComprasPK().setNrofact(Long.parseLong(resultado[3].toString()));
            notasCompras.setCompras(c);
            notasCompras.setTtotal(Long.parseLong(resultado[5].toString()));
            notasCompras.setTtotal(Long.parseLong(resultado[6].toString()));
            NotasComprasDto ncdtp = new NotasComprasDto();
            ncdtp.setNotaCompra(notasCompras);
            listadoNotasCompras.add(ncdtp);
        }
        
        return listadoNotasCompras;
    }
    
     public List<NotasComprasDto> maximaCompraDebitoDelProveedor(long lNroNota, Short lCodProveed){
        String sql =    "SELECT fdocum, nro_nota, com_ctipo_docum, nrofact, ffactur, ttotal, isaldo FROM notas_compras WHERE cod_empr = 2 " +
                        "AND ctipo_docum = 'NDC' AND nro_nota = "+lNroNota+" "+ 
                        "AND fdocum = (SELECT MAX(fdocum) FROM notas_compras n2 " +
                        "               WHERE n2.cod_empr = 2 " +
                        "                 AND N2.nro_nota = "+lNroNota+" "+
                        "                 AND n2.cod_proveed = "+lCodProveed+" "+
                        "                 AND n2.ctipo_docum = 'NDC' " +
                        "                 AND n2.mestado ='A' )";
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<NotasComprasDto> listadoNotasCompras = new ArrayList<>();
        for(Object[] resultado: resultados){
            NotasComprasPK notasComprasPk = new NotasComprasPK();
            if(resultado[0] != null){
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());                
                notasComprasPk.setFdocum(dateResult_0);
            }else{
                notasComprasPk.setFdocum(null);
            }
            notasComprasPk.setNroNota(Long.parseLong(resultado[1].toString()));
            NotasCompras notasCompras = new NotasCompras();
            notasCompras.setNotasComprasPK(notasComprasPk);
            
            Compras c = new Compras();
            c.getComprasPK().setCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            if(resultado[4] != null){
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());                
                c.getComprasPK().setFfactur(dateResult_4);
            }else{
                c.getComprasPK().setFfactur(null);
            }
            c.getComprasPK().setNrofact(Long.parseLong(resultado[3].toString()));
            notasCompras.setCompras(c);
            notasCompras.setTtotal(Long.parseLong(resultado[5].toString()));
            notasCompras.setTtotal(Long.parseLong(resultado[6].toString()));
            NotasComprasDto ncdtp = new NotasComprasDto();
            ncdtp.setNotaCompra(notasCompras);
            listadoNotasCompras.add(ncdtp);
        }
        
        return listadoNotasCompras;
    }
     
    public List<NotasComprasDto> comprasDebitoPorNroNotaFechaProveedor(long lNroNota, Short lCodProveed, String lFDocum){
        String sql =    "SELECT fdocum, nro_nota, com_ctipo_docum, nrofact, ffactur, ttotal, isaldo FROM notas_compras WHERE cod_empr = 2 " +
                        "AND ctipo_docum = 'NDC' AND nro_nota = "+lNroNota+" "+
                        "AND cod_proveed = "+lCodProveed+" "+
                        "AND fdocum = '"+lFDocum+"' "+
                        "AND mestado = 'A'";
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<NotasComprasDto> listadoNotasCompras = new ArrayList<>();
        for(Object[] resultado: resultados){
            NotasComprasPK notasComprasPk = new NotasComprasPK();
            if(resultado[0] != null){
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());                
                notasComprasPk.setFdocum(dateResult_0);
            }else{
                notasComprasPk.setFdocum(null);
            }
            notasComprasPk.setNroNota(Long.parseLong(resultado[1].toString()));
            NotasCompras notasCompras = new NotasCompras();
            notasCompras.setNotasComprasPK(notasComprasPk);
            
            Compras c = new Compras();
            c.getComprasPK().setCtipoDocum(resultado[2] == null ? null : resultado[2].toString());
            if(resultado[4] != null){
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());                
                c.getComprasPK().setFfactur(dateResult_4);
            }else{
                c.getComprasPK().setFfactur(null);
            }
            c.getComprasPK().setNrofact(Long.parseLong(resultado[3].toString()));
            notasCompras.setCompras(c);
            notasCompras.setTtotal(Long.parseLong(resultado[5].toString()));
            notasCompras.setTtotal(Long.parseLong(resultado[6].toString()));
            NotasComprasDto ncdtp = new NotasComprasDto();
            ncdtp.setNotaCompra(notasCompras);
            listadoNotasCompras.add(ncdtp);
        }
        
        return listadoNotasCompras;
    } 
    
    public void aumentaSaldoNotasCompras(long lIPagado, String lFFactura, long lNroNota, Short lCodProveed){
        String sql =    "UPDATE notas_compras " +
                        "SET isaldo = isaldo + "+lIPagado+" "+
                        "WHERE cod_empr = 2 " +
                        "AND fdocum = '"+lFFactura+"' "+
                        "AND ctipo_docum= 'NCC' " +
                        "AND (nrofact is null OR nrofact = 0) " +
                        "AND nro_nota = "+lNroNota+" "+
                        "AND cod_proveed = "+lCodProveed+" ";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public void disminuyeSaldoNotasCompras(long lIPagado, String lFFactura, long lNroNota, Short lCodProveed){
        String sql =    "UPDATE notas_compras " +
                        "SET isaldo = isaldo - "+lIPagado+" "+
                        "WHERE cod_empr = 2 " +
                        "AND fdocum = '"+lFFactura+"' "+
                        "AND ctipo_docum = 'NCC' " +
                        "AND (nrofact is null OR nrofact = 0) " +
                        "AND nro_nota = "+lNroNota+" "+
                        "AND cod_proveed = "+lCodProveed+" ";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    
    public NotasCompras getNotasComprasByPK(NotasComprasPK notasComprasPK){
        try{
           Query q = getEntityManager().createQuery("SELECT c from NotasCompras c where "
                   + "c.notasComprasPK.codEmpr=:codEmpr "
                   + "and c.notasComprasPK.nroNota=:nroNota "
                   + "and c.notasComprasPK.codProveed = :codProveed")
                   .setParameter("codEmpr", notasComprasPK.getCodEmpr())
                   .setParameter("nroNota", notasComprasPK.getNroNota())
                   .setParameter("codProveed", notasComprasPK.getCodProveed());
           
           NotasCompras respuesta = (NotasCompras) q.getSingleResult();
           return respuesta;
       }catch(Exception e){
           e.printStackTrace();
           return null;
       }
    }
    
    
}
