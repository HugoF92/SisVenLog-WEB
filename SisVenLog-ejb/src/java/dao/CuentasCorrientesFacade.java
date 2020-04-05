/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.CuentaCorrienteDto;
import entidad.CuentasCorrientes;
import java.sql.SQLException;
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
public class CuentasCorrientesFacade extends AbstractFacade<CuentasCorrientes>{
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    public CuentasCorrientesFacade() {
        super(CuentasCorrientes.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public void insertarCuentasCorrientes(CuentasCorrientes cuentaCorriente) throws SQLException{
        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarCuentasCorrientes");
        q.registerStoredProcedureParameter("fmovim", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ctipo_docum", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_banco", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ndocum_cheq", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_empr", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("nrofact", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_cliente", Integer.class, ParameterMode.IN);
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
        q.registerStoredProcedureParameter("xcuenta_bco", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ffactur", Date.class, ParameterMode.IN);
        q.setParameter("fmovim", cuentaCorriente.getFmovim());
        q.setParameter("ctipo_docum", cuentaCorriente.getCtipoDocum());
        q.setParameter("cod_banco", cuentaCorriente.getCodBanco().getCodBanco());
        q.setParameter("ndocum_cheq", cuentaCorriente.getNdocumCheq());
        q.setParameter("cod_empr", cuentaCorriente.getCodEmpr());
        q.setParameter("nrofact", cuentaCorriente.getNrofact());
        q.setParameter("cod_cliente", cuentaCorriente.getCodCliente().getCodCliente());
        q.setParameter("fac_ctipo_docum", cuentaCorriente.getFacCtipoDocum());
        q.setParameter("texentas", cuentaCorriente.getTexentas());
        q.setParameter("ipagado", cuentaCorriente.getIpagado());
        q.setParameter("iretencion", cuentaCorriente.getIretencion());
        q.setParameter("isaldo", cuentaCorriente.getIsaldo());
        q.setParameter("falta", cuentaCorriente.getFalta());
        q.setParameter("manulado", cuentaCorriente.getManulado());
        q.setParameter("cusuario", cuentaCorriente.getCusuario());
        q.setParameter("tgravadas", cuentaCorriente.getTgravadas());
        q.setParameter("timpuestos", cuentaCorriente.getTimpuestos());
        q.setParameter("mindice", cuentaCorriente.getMindice());
        q.setParameter("fvenc", cuentaCorriente.getFvenc());
        q.setParameter("xcuenta_bco", cuentaCorriente.getXcuentaBco());
        q.setParameter("ffactur", cuentaCorriente.getFfactur());
        System.out.println(q.toString());
        q.execute();
    }
    
    public void insertarCuentas(CuentasCorrientes cuentaCorriente){
        String sql = "INSERT INTO cuentas_corrientes(cod_empr, ctipo_docum, fvenc, fmovim, ndocum_cheq, ipagado, iretencion, cod_banco, cod_cliente, isaldo, mindice, manulado, texentas, tgravadas, timpuestos) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, cuentaCorriente.getCodEmpr());
        q.setParameter(2, cuentaCorriente.getCtipoDocum());
        q.setParameter(3, cuentaCorriente.getFvenc());
        q.setParameter(4, cuentaCorriente.getFmovim());
        q.setParameter(5, cuentaCorriente.getNdocumCheq());
        q.setParameter(6, cuentaCorriente.getIpagado());
        q.setParameter(7, cuentaCorriente.getIretencion());
        q.setParameter(8, cuentaCorriente.getCodBanco() == null ? null : cuentaCorriente.getCodBanco().getCodBanco());
        q.setParameter(9, cuentaCorriente.getCodCliente() == null ? null : cuentaCorriente.getCodCliente().getCodCliente());
        q.setParameter(10, cuentaCorriente.getIsaldo());
        q.setParameter(11, cuentaCorriente.getMindice());
        q.setParameter(12, cuentaCorriente.getManulado());
        q.setParameter(13, cuentaCorriente.getTexentas());
        q.setParameter(14, cuentaCorriente.getTgravadas());
        q.setParameter(15, cuentaCorriente.getTimpuestos());
        q.executeUpdate();
    }
    
    public List<CuentaCorrienteDto> listadoDeFacturasCuentasCorrientesPorNrofacturaYFecha(Long numeroFactura, Date fechaFactura){
        
        String sql =    "SELECT c.ctipo_docum, c.ndocum_cheq, " +
                        "c.fmovim, (c.texentas * mindice) as texentas, " +
                        "(c.tgravadas * mindice) as tgravadas, " +
                        "(c.timpuestos * mindice) as timpuestos, " +
                        "t.cconc, c.fvenc, c.ipagado " +
                        "FROM cuentas_corrientes c LEFT JOIN notas_ventas t " +
                        "ON c.ndocum_cheq = t.nro_nota " +
                        "AND c.ctipo_docum = t.ctipo_docum " +
                        "WHERE c.cod_empr = 2";
        
        if(numeroFactura != null){
            if(numeroFactura > 0){
                sql += " AND c.nrofact = ?";
            }
        }
        
        if(fechaFactura != null){
            sql += " AND c.ffactur = ?";
        }
        
        Query q = em.createNativeQuery(sql);
        
        int i = 1;
        if(numeroFactura != null){
            if(numeroFactura > 0){
                i++;
                q.setParameter(i, numeroFactura);
            }
        }
        
        if(fechaFactura != null){
            i++;
            q.setParameter(i, fechaFactura);
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CuentaCorrienteDto> listadoCuentasCorrientes = new ArrayList<CuentaCorrienteDto>();
        for(Object[] resultado: resultados){
            CuentaCorrienteDto ccdto = new CuentaCorrienteDto();
            ccdto.setTipoDocumento(resultado[0] == null ? null : resultado[0].toString());
            ccdto.setNumeroDocumentoCheque(resultado[1] == null ? null : resultado[1].toString());
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                                
                ccdto.setFechaMovimiento(dateResult_2);
            }else{              
                ccdto.setFechaMovimiento(null);
            }
            ccdto.setExentas(Long.parseLong(resultado[3].toString()));
            ccdto.setGravadas(Long.parseLong(resultado[4].toString()));
            ccdto.setImpuestos(Long.parseLong(resultado[5].toString()));
            ccdto.setConcepto(resultado[6] == null ? null : resultado[6].toString());
            if(resultado[7] != null){
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());                                
                ccdto.setFechaVencimiento(dateResult_7);
            }else{              
                ccdto.setFechaVencimiento(null);
            }
            ccdto.setImportePagado(Long.parseLong(resultado[8].toString()));
            listadoCuentasCorrientes.add(ccdto);
        }        
        return listadoCuentasCorrientes;
    }
    
    public long obtenerTotalCtaCtePorClienteEnRangoDeFechas(Integer lCodCliente, String lFMovimDesde, String lFMovimHasta) {
        String sql = "SELECT ISNULL(SUM((texentas+tgravadas+timpuestos)*mindice),0) + ISNULL(SUM(ipagado * MINDICE),0) as tmovim "
                + "FROM cuentas_corrientes "
                + "WHERE cod_cliente = "+lCodCliente+" "
                + "AND cod_empr = 2 "
                + "AND (fac_ctipo_docum IS NULL OR fac_ctipo_docum != 'FCO') "
                + "AND fmovim > '" + lFMovimDesde + "' "
                + "AND fmovim < '" + lFMovimHasta + "' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        long total = 0;
        for(Object[] resultado: resultados){
            total = resultado[0] == null ? Long.parseLong("0") : Long.parseLong(resultado[0].toString());
        }
        return total;
    }
    
}
