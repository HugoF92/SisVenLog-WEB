/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.CuentasProveedores;
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
     
}
