/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.CuentaProveedorDto;
import entidad.CuentasProveedores;
import java.sql.Timestamp;
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
 * @author Edu
 */
@Stateless
public class CuentasProveedoresFacade extends AbstractFacade<CuentasProveedores>{
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    public CuentasProveedoresFacade() {
        super(CuentasProveedores.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<CuentasProveedores> listarCuentasProveedoresPorNroChequeBanco(String numeroCheque, Short codigoBanco){
        String sql = "select u from CuentasProveedores u where u.codEmpr = 2 and u.ctipoDocum = 'CHE' ";
        
        if(numeroCheque != null || !numeroCheque.equals("")){
           sql += "and u.ndocumCheq = :numeroCheque "; 
        }
        
        if(codigoBanco != 0){
           sql += "and u.codBanco.codBanco = :codigoBanco "; 
        }
        
        Query query = em.createQuery(sql);
        
        if(numeroCheque != null || !numeroCheque.equals("")){
            query.setParameter("numeroCheque", numeroCheque);
        }
        
        if(codigoBanco != 0){
            query.setParameter("codigoBanco", codigoBanco);
        }
            
        List<CuentasProveedores> resultado = query.getResultList();
        if(resultado.size() <= 0){
            return null;
        }else{
            return resultado;
        }
    }
    
    public void insertarCuentasProveedores(CuentasProveedores cuentaProveedor){
        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarCuentasProveedores");
        q.registerStoredProcedureParameter("fmovim", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ctipo_docum", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_banco", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ndocum_cheq", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_empr", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("nrofact", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_proveed", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("fac_ctipo_docum", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("texentas", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ipagado", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("iretencion", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("isaldo", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("manulado", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("tgravadas", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("timpuestos", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("mindice", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("fvenc", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ffactur", Date.class, ParameterMode.IN);
        q.setParameter("fmovim", cuentaProveedor.getFmovim());
        q.setParameter("ctipo_docum", cuentaProveedor.getCtipoDocum());
        q.setParameter("cod_banco", cuentaProveedor.getCodBanco().getCodBanco());
        q.setParameter("ndocum_cheq", cuentaProveedor.getNdocumCheq());
        q.setParameter("cod_empr", cuentaProveedor.getCodEmpr());
        q.setParameter("nrofact", cuentaProveedor.getNrofact());
        q.setParameter("cod_proveed", cuentaProveedor.getCodProveed().getCodProveed());
        q.setParameter("fac_ctipo_docum", cuentaProveedor.getFacCtipoDocum());
        q.setParameter("texentas", cuentaProveedor.getTexentas());
        q.setParameter("ipagado", cuentaProveedor.getIpagado());
        q.setParameter("iretencion", cuentaProveedor.getIretencion());
        q.setParameter("isaldo", cuentaProveedor.getIsaldo());
        q.setParameter("falta", cuentaProveedor.getFalta());
        q.setParameter("manulado", cuentaProveedor.getManulado());
        q.setParameter("cusuario", cuentaProveedor.getCusuario());
        q.setParameter("tgravadas", cuentaProveedor.getTgravadas());
        q.setParameter("timpuestos", cuentaProveedor.getTimpuestos());
        q.setParameter("mindice", cuentaProveedor.getMindice());
        q.setParameter("fvenc", cuentaProveedor.getFvenc());
        q.setParameter("ffactur", cuentaProveedor.getFfactur());
        q.execute();
    }
    
    public void insertarEnCuentasProveedores(CuentasProveedores cuentaProveedor){
        String sql = "INSERT INTO cuentas_proveedores(cod_empr, ctipo_docum, fvenc, fmovim, ndocum_cheq, ipagado, iretencion, cod_banco, cod_proveed, isaldo, mindice, manulado, texentas, tgravadas, timpuestos) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, cuentaProveedor.getCodEmpr());
        q.setParameter(2, cuentaProveedor.getCtipoDocum());
        q.setParameter(3, cuentaProveedor.getFvenc());
        q.setParameter(4, cuentaProveedor.getFmovim());
        q.setParameter(5, cuentaProveedor.getNdocumCheq());
        q.setParameter(6, cuentaProveedor.getIpagado());
        q.setParameter(7, cuentaProveedor.getIretencion());
        q.setParameter(8, cuentaProveedor.getCodBanco().getCodBanco());
        q.setParameter(9, cuentaProveedor.getCodProveed().getCodProveed());
        q.setParameter(10, cuentaProveedor.getIsaldo());
        q.setParameter(11, cuentaProveedor.getMindice());
        q.setParameter(12, cuentaProveedor.getManulado());
        q.setParameter(13, cuentaProveedor.getTexentas());
        q.setParameter(14, cuentaProveedor.getTgravadas());
        q.setParameter(15, cuentaProveedor.getTimpuestos());
        q.executeUpdate();    
    }
    
    public List<CuentaProveedorDto> listadoDeCuentasProveedoresEnNotasCompras(long nroFactura, Short codigoProveedor, String tipoDocumentoFactura, String fechaFactura){
        
        String sql =    "SELECT c.ctipo_docum, c.ndocum_cheq, c.fmovim, c.texentas * c.mindice as texentas, c.tgravadas * c.mindice as tgravadas, c.timpuestos * c.mindice as timpuestos, ipagado * c.mindice as ipagado, "+                
                        "t.cconc, c.fvenc, c.otr_ndocum, c.otr_ctipo_docum, e.xdesc "+
                        "FROM cuentas_proveedores c LEFT JOIN notas_compras  t "+
                        "ON c.ndocum_cheq = t.nro_nota "+
                        "LEFT JOIN tipos_documentos e "+    
                        "ON c.ctipo_docum = e.ctipo_docum "+
                        "WHERE c.cod_empr = 2 "+
                        "AND c.nrofact = "+nroFactura+" "+
                        "AND c.cod_proveed = "+codigoProveedor+" "+
                        "AND c.fac_ctipo_docum = '"+tipoDocumentoFactura+"' "+
                        "AND c.ffactur = '"+fechaFactura+"' ";
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CuentaProveedorDto> listadoCuentasProveedores = new ArrayList<>();
        for(Object[] resultado: resultados){
            CuentaProveedorDto cpdto = new CuentaProveedorDto();
            cpdto.setTipoDocumento(resultado[0] == null ? null : resultado[0].toString());
            cpdto.setNumeroDocumentoCheque(resultado[1] == null ? null : resultado[1].toString());
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                
                cpdto.setFechaMovimiento(dateResult_2);
            }else{
                cpdto.setFechaMovimiento(null);
            }
            cpdto.setGravadas(resultado[4] == null ? 0 : Long.parseLong(resultado[4].toString()));
            cpdto.setExentas(resultado[3] == null ? 0 : Long.parseLong(resultado[3].toString()));
            cpdto.setImportePagado(resultado[6] == null ? 0 : Long.parseLong(resultado[6].toString()));
            cpdto.setImpuestos(resultado[5] == null ? 0 : Long.parseLong(resultado[5].toString()));
            cpdto.setOtroTipoDocumento(resultado[10] == null ? null : resultado[10].toString());
            cpdto.setOtroNumeroDocumento(resultado[9] == null ? null : Long.parseLong(resultado[9].toString()));
            cpdto.setConcepto(resultado[7] == null ? null : resultado[7].toString());
            if(resultado[8] != null){
                Timestamp timeStamp_8 = (Timestamp) resultado[8];
                java.util.Date dateResult_8 = new Date(timeStamp_8.getTime());                
                cpdto.setFechaVencimiento(dateResult_8);
            }else{
                cpdto.setFechaVencimiento(null);
            }
            cpdto.setTipoDocumentoDescripcion(resultado[11] == null ? null : resultado[11].toString());
            listadoCuentasProveedores.add(cpdto);
        }
        
        return listadoCuentasProveedores;
    }
    
    public List<CuentaProveedorDto> listadoDeCuentasProveedoresEnCuentas(   String tipoDocumento,
                                                                            String nroNota, 
                                                                            long nroFactura,
                                                                            Short codigoProveedor,
                                                                            String fechaDocumento){
        
        String sql =    "SELECT c.ctipo_docum, c.ndocum_cheq, " +
                        "c.fmovim, c.texentas * c.mindice as texentas, " +
                        "c.tgravadas * c.mindice as tgravadas, " +
                        "c.timpuestos * c.mindice as timpuestos, ipagado * c.mindice as ipagado, " +
                        "c.fvenc, c.otr_ndocum, c.otr_ctipo_docum, e.xdesc " +
                        "FROM cuentas_proveedores c LEFT JOIN tipos_documentos e "+
                        "ON c.ctipo_docum = e.ctipo_docum "+
                        "WHERE RTRIM(LTRIM(c.ndocum_cheq)) like '"+"%"+nroNota.trim()+"%"+"' "+
                        "AND c.cod_proveed = "+codigoProveedor+" "+
                        "AND c.ctipo_docum = '"+tipoDocumento+"' "+
                        "AND c.cod_empr = 2 "+
                        "UNION ALL "+
                        "SELECT c.ctipo_docum, c.ndocum_cheq, " +
                        "c.fmovim, c.texentas * c.mindice as texentas, " +
                        "c.tgravadas * c.mindice as tgravadas, " +
                        "c.timpuestos * c.mindice as timpuestos, ipagado * c.mindice as ipagado, " +
                        "c.fvenc, c.otr_ndocum, c.otr_ctipo_docum, e.xdesc " +
                        "FROM cuentas_proveedores c LEFT JOIN tipos_documentos e "+
                        "ON c.otr_ctipo_docum = e.ctipo_docum "+
                        "WHERE RTRIM(LTRIM(STR(c.otr_ndocum,16))) like '"+"%"+nroNota.trim()+"%"+"' "+
                        "AND c.cod_proveed = "+codigoProveedor+" "+
                        "AND c.otr_ctipo_docum = '"+tipoDocumento+"' "+
                        "AND c.otr_fdocum = '"+fechaDocumento+"' "+
                        "AND c.cod_empr = 2";
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CuentaProveedorDto> listadoCuentasProveedores = new ArrayList<>();
        for(Object[] resultado: resultados){
            CuentaProveedorDto cpdto = new CuentaProveedorDto();
            cpdto.setTipoDocumento(resultado[0] == null ? null : resultado[0].toString());
            cpdto.setNumeroDocumentoCheque(resultado[1] == null ? null : resultado[1].toString());
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                
                cpdto.setFechaMovimiento(dateResult_2);
            }else{
                cpdto.setFechaMovimiento(null);
            }
            cpdto.setGravadas(resultado[4] == null ? 0 : Long.parseLong(resultado[4].toString()));
            cpdto.setExentas(resultado[3] == null ? 0 : Long.parseLong(resultado[3].toString()));
            cpdto.setImportePagado(resultado[6] == null ? 0 : Long.parseLong(resultado[6].toString()));
            cpdto.setImpuestos(resultado[5] == null ? 0 : Long.parseLong(resultado[5].toString()));
            cpdto.setOtroTipoDocumento(resultado[9] == null ? null : resultado[9].toString());
            cpdto.setOtroNumeroDocumento(resultado[8] == null ? null : Long.parseLong(resultado[8].toString()));
            if(resultado[7] != null){
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());                
                cpdto.setFechaVencimiento(dateResult_7);
            }else{
                cpdto.setFechaVencimiento(null);
            }
            cpdto.setTipoDocumentoDescripcion(resultado[10] == null ? null : resultado[10].toString());
            listadoCuentasProveedores.add(cpdto);
        }
        
        return listadoCuentasProveedores;
        
    }
    
    public void borrarRecibosProveedores(Short lCodProveed, String ndocumCheq, Long nrofact, String facCtipoDocum){
        String sql =    "DELETE FROM cuentas_proveedores " +
                        "WHERE cod_empr = 2 AND ctipo_docum = 'REP' " +
                        "AND cod_proveed = "+lCodProveed+" "+
                        "AND ndocum_cheq like '"+"%"+ndocumCheq.trim()+"%"+"' "+
                        "AND nrofact = '"+nrofact+"' "+
                        "AND fac_ctipo_docum like '"+"%"+facCtipoDocum.trim()+"%"+"' ";    
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public void borrarOtrosRecibosProveedores(Short lCodProveed, String ndocumCheq, Long nrofact, String facCtipoDocum){
        String sql =    "DELETE FROM cuentas_proveedores " +
                        "WHERE cod_empr = 2 AND ctipo_docum = 'REP' " +
                        "AND cod_proveed = "+lCodProveed+" "+
                        "AND ndocum_cheq like '"+"%"+ndocumCheq.trim()+"%"+"' "+
                        "AND otr_ndocum = "+nrofact+" "+
                        "AND otr_ctipo_docum like '"+"%"+facCtipoDocum.trim()+"%"+"' ";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public void borrarChequesProveedores(Short lCodProveed, String lNroDocmCheque, Short lCodbanco){
        String sql =    "DELETE FROM cuentas_proveedores " +
                        "WHERE cod_empr = 2 AND ctipo_docum = 'CHE' " +
                        "AND cod_proveed = "+lCodProveed+" "+
                        "AND ndocum_cheq like '"+"%"+lNroDocmCheque.trim()+"%"+"' "+
                        "AND cod_banco = "+lCodbanco;
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public void insertarCuentaProveedor(    short gCodEmpresa,
                                            String lCTipoDocum,
                                            Short lCodProveed,
                                            String lFMovim,
                                            String lNDocmCheque,
                                            String lFacCTipoDocum,
                                            long lNroFact,
                                            long lIPagado,
                                            long lTExentas,
                                            long lTImpuestos,
                                            long lTGravadas,
                                            long lIRetencion,
                                            long lISaldo,
                                            short lMIndice,
                                            String lFvenc,
                                            String lOtroCTipoDocum,
                                            Long lOtroNDocum,
                                            String lOtroFDocum,
                                            String lCCanalCompra,
                                            String lFFactura){
        String sql =    "INSERT INTO cuentas_proveedores(cod_empr, ctipo_docum, cod_proveed, " +
                        "fmovim, ndocum_cheq, fac_ctipo_docum, nrofact, ipagado, texentas, timpuestos, tgravadas, " +
                        "iretencion, isaldo, manulado, mindice, fvenc, otr_ctipo_docum, otr_ndocum, otr_fdocum, ccanal_compra, ffactur) " +
                        "values ("+gCodEmpresa+", '"+lCTipoDocum+"', "+lCodProveed+", '"+lFMovim+"', '"+lNDocmCheque+"', '"+lFacCTipoDocum+"', " +
                        ""+lNroFact+", "+lIPagado+", 0, 0, 0, 0, "+lISaldo+", 1, "+lMIndice+", '"+lFvenc+"', '"+lOtroCTipoDocum+"', "+lOtroNDocum+", '"+lOtroFDocum+"', '"+lCCanalCompra+"', '"+lFFactura+"')";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public void insertarCuentaProveedor(String lFMovim,
                                        String lNDocmCheque,
                                        long lICheque,
                                        Short lCodBanco,
                                        Short lCodProveed,
                                        short lMIndice,
                                        String lFechaCheque,
                                        String lFactTipo,
                                        long lNroFact,
                                        String lFFactura){
        String sql =    "INSERT INTO cuentas_proveedores(cod_empr, ctipo_docum, " +
                        "fmovim, ndocum_cheq, ipagado, iretencion, cod_banco, cod_proveed, isaldo, manulado, " +
                        "texentas, tgravadas, timpuestos, mindice, fvenc, fac_ctipo_docum, nrofact, ffactur) " +
                        "values (2, 'CHE', '"+lFMovim+"', '"+lNDocmCheque+"', " +
                        ""+lICheque+", 0, "+lCodBanco+", "+lCodProveed+", 0, 1, 0, 0, 0, "+lMIndice+", '"+lFechaCheque+"', '"+lFactTipo+"', "+lNroFact+", '"+lFFactura+"')";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public String obtenerNroDocumentoCheque(String ndocumCheq, Short lCodbanco){
        String sql =    "SELECT ndocum_cheq " +
                        "FROM cuentas_proveedores a " +
                        "WHERE a.ctipo_docum = 'CHE' and a.cod_empr = 2 " +
                        "AND a.ndocum_cheq like '"+"%"+ndocumCheq.trim()+"%"+"' "+
                        "AND a.cod_banco = "+lCodbanco;
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        String nroCheque = null;
        for(Object[] resultado: resultados){
            if(resultado != null){
                nroCheque = resultado[0].toString();
            }
        }
        return nroCheque;
    }
}
