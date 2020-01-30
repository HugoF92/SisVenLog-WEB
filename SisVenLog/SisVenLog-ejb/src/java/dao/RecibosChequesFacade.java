/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ChequeDetalleDto;
import entidad.RecibosCheques;
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
public class RecibosChequesFacade extends AbstractFacade<RecibosCheques> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecibosChequesFacade() {
        super(RecibosCheques.class);
    }
    
    public List<ChequeDetalleDto> listadoDeRecibosPorNroFacturaYFecha(Long numeroFactura, String fechaFactura){
        
        String sql =    "select 'REC' as ctipo_docum, a.nro_cheque, b.fvenc as fvenc, a.ipagado, b.fmovim as fmovim, c.xdesc  "+
                        "from recibos_cheques a, cuentas_corrientes b, bancos c " +
                        "where b.fac_ctipo_docum = 'FCR' " +
                        "and b.ffactur = '"+fechaFactura+"' "+
                        "and b.ctipo_docum = 'REC' " +
                        "AND b.ndocum_cheq = a.nrecibo "+
                        "AND a.cod_banco = c.cod_banco";
        
        if(numeroFactura != null){
            if(numeroFactura > 0){
                sql += " AND b.nrofact = ?";
            }
        }
        
        Query q = em.createNativeQuery(sql);
        
        int i = 0;
        if(numeroFactura != null){
            if(numeroFactura > 0){
                i++;
                q.setParameter(i, numeroFactura);
            }
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<ChequeDetalleDto> listadoCheques = new ArrayList<ChequeDetalleDto>();
        for(Object[] resultado: resultados){
            ChequeDetalleDto cddto = new ChequeDetalleDto();
            cddto.setTipoDocumento(resultado[0].toString());
            cddto.setNroCheque(resultado[1].toString());
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                                
                cddto.setFechaVencimiento(dateResult_2);
            }else{              
                cddto.setFechaVencimiento(null);
            }
            cddto.setImportePagado(Long.parseLong(resultado[3].toString()));
            if(resultado[4] != null){
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());                                
                cddto.setFechaEmision(dateResult_4);
            }else{              
                cddto.setFechaEmision(null);
            }
            cddto.setNombreBanco(resultado[5] == null ? null : resultado[5].toString());
            listadoCheques.add(cddto);
        }
        
        return listadoCheques;
    }
    
    public long obtenerImportePagadoRecibosCheques(String lNroCheque, short lCodBanco){
        String sql =    "SELECT sum(r.ipagado) " +
                        "from recibos_cheques r, recibos e " +
                        "WHERE r.nro_cheque = '" + lNroCheque + "' "+
                        "and r.nrecibo = e.nrecibo "+
                        "AND e.mestado = 'A' "+
                        "AND r.cod_banco = "+lCodBanco;
        
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        long importePagado = 0;
        for(Object[] resultado: resultados){
            importePagado = resultado == null ? 0 : Long.parseLong(resultado[0].toString());
        }
        return importePagado;
    }
    
    public void insertarReciboCheque(   long lNroRecibo,
                                        Short lCodBanco,
                                        String lNroCheque,
                                        long lIPagado){
        String sql =    "INSERT INTO recibos_cheques(cod_empr, nrecibo, " +
                        "cod_banco, nro_cheque, ipagado) " +
                        "values(2, "+lNroRecibo+", " +
                        ""+lCodBanco+", '"+lNroCheque+"', "+lIPagado+")";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }
    
}
