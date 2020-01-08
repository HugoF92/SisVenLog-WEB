/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Recibos;
import dto.RecibosVentasDto;
import dto.RecibosComprasDto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author WORK
 */
@Stateless
public class GenDatosContablesFacade extends AbstractFacade<Recibos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GenDatosContablesFacade() {
        super(Recibos.class);
    }
    
    @PreDestroy
    public void destruct() {
        getEntityManager().close();
    }

    public List<RecibosVentasDto> busquedaDatosRecibosVentas(String fechaInicial, String fechaFinal) throws Exception { 
        Query q = getEntityManager().createNativeQuery(" SELECT 'REC' as ctipo_docum, 1 as nro_cuota,"
                + " CONVERT(char(10),f.frecibo,103) as frecibo, f.nrecibo, d.ctipo_docum as ctipo, d.ndocum ,   "
                + " d.ffactur, f.iefectivo AS iefectivo, ch.nro_cheque, ch.ipagado,  0 as moneda, 0 as cotizacion  "
                + " FROM         recibos f  INNER JOIN recibos_det d "
                + " ON f.nrecibo = d.nrecibo   , "
                + " recibos_cheques ch,  cheques cq "
                + " WHERE     (f.frecibo BETWEEN '"+ fechaInicial +"' AND '"+ fechaFinal +"') "
                + " AND (f.mestado = 'A')  "
                + " AND ch.cod_banco = cq.cod_banco "
                + " AND ch.nro_cheque = cq.nro_cheque "
                + " AND f.nrecibo = ch.nrecibo "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " AND ch.cod_empr = 2 "
                + " AND f.iefectivo > 0 ");

        System.out.println(q.toString());

        List<Object[]> resultados = q.getResultList();
        List<RecibosVentasDto> listadoReciboVentas = new ArrayList<RecibosVentasDto>();
        for(Object[] resultado: resultados){
            RecibosVentasDto rvdto = new RecibosVentasDto();
            rvdto.setCtipoDocum(resultado[0].toString());
            rvdto.setNcuota(Long.parseLong(resultado[1].toString()));
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                                
                rvdto.setFrecibo(dateResult_2);
            }else{              
                rvdto.setFrecibo(null);
            }
            rvdto.setNrecibo(Long.parseLong(resultado[3].toString()));
            rvdto.setCtipo(resultado[4].toString());
            rvdto.setNdocum(Long.parseLong(resultado[5].toString()));
            if(resultado[6] != null){
                Timestamp timeStamp_6 = (Timestamp) resultado[6];
                java.util.Date dateResult_6 = new Date(timeStamp_6.getTime());                                
                rvdto.setFfactur(dateResult_6);
            }else{              
                rvdto.setFfactur(null);
            }
            rvdto.setIefectivo(Long.parseLong(resultado[7].toString()));
            rvdto.setNroCheque(resultado[8].toString());
            rvdto.setIpagado(Long.parseLong(resultado[9].toString()));
            rvdto.setMoneda(Long.parseLong(resultado[10].toString()));
            rvdto.setCotizacion(Long.parseLong(resultado[11].toString()));
            listadoReciboVentas.add(rvdto);
        }        
        return listadoReciboVentas;
    }

    public List<RecibosComprasDto> busquedaDatosRecibosCompras(String fechaInicial, String fechaFinal) throws Exception {
        Query q = getEntityManager().createNativeQuery(" SELECT    'REP' as ctipo_docum,  1 as nro_cuota,  CONVERT(char(10),f.frecibo,103) as frecibo, f.nrecibo, d.nrofact , d.ctipo_docum as ctipo,   "
                + " d.ffactur, 0 AS iefectivo, ch.nro_cheque, ch.ipagado, cb.cod_contable, 0 as moneda, 0 as cotizacion, f.cod_proveed, d.itotal, 0 as ntimbrado, 0 as fact_timbrado, 0 as ntimbrado, 0 as nota_timbrado  "
                + "	FROM         recibos_prov f  INNER JOIN recibos_prov_det d "
                + " ON f.nrecibo = d.nrecibo AND f.cod_proveed = d.cod_proveed LEFT OUTER JOIN compras c "
                + " ON d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed AND d.nrofact = c.nrofact And c.cod_empr = 2 LEFT OUTER JOIN notas_compras n "
                + " ON d.ctipo_docum = n.ctipo_docum AND d.cod_proveed = n.cod_proveed AND d.nrofact = c.nrofact AND d.ffactur = c.ffactur AND n.cod_empr = 2 , "
                + " recibos_prov_cheques ch, ctas_bancarias cb, cheques_emitidos cq "
                + " WHERE     (f.frecibo BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A')  "
                + " AND cq.xcuenta_bco = cb.xcuenta_bco "
                + " AND cq.cod_banco = cb.cod_banco "
                + " AND ch.cod_banco = cq.cod_banco "
                + " AND ch.nro_cheque = cq.nro_cheque "
                + " AND f.nrecibo = ch.nrecibo "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " AND ch.cod_empr = 2 "
                + " AND f.iCHEQUES > 0 "
                + " UNION ALL "
                + " SELECT   'REP' as ctipo_docum,   1 as nro_cuota,  CONVERT(char(10),f.frecibo,103) as frecibo, f.nrecibo, d.nrofact, d.ctipo_docum as ctipo,  "
                + " d.ffactur, f.iefectivo, ''  as nro_cheque, 0 as ipagado, 0 as cod_contable, 0 as moneda, 0 as cotizacion, f.cod_proveed, d.itotal, 0 as ntimbrado as fact_timbrado, 0 as ntimbrado as nota_timbrado  "
                + "	FROM         recibos_prov f  INNER JOIN recibos_prov_det d "
                + " ON f.nrecibo = d.nrecibo AND f.cod_proveed = d.cod_proveed LEFT OUTER JOIN  compras c "
                + " ON d.nrofact = c.nrofact AND d.ctipo_docum = c.ctipo_docum AND d.ffactur = c.ffactur AND f.cod_proveed = c.cod_proveed LEFT OUTER JOIN notas_compras n "
                + " ON d.nrofact = n.nro_nota AND d.ctipo_docum = n.ctipo_docum AND f.cod_proveed = n.cod_proveed AND d.ffactur = n.fdocum "
                + " WHERE     (f.frecibo BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A')  "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " AND f.iefectivo > 0 ");

        System.out.println(q.toString());

        List<Object[]> resultados = q.getResultList();
        List<RecibosComprasDto> listadoReciboCompras = new ArrayList<RecibosComprasDto>();
        for(Object[] resultado: resultados){
            RecibosComprasDto rcdto = new RecibosComprasDto();
            rcdto.setCtipoDocum(resultado[0].toString());
            rcdto.setNcuota(Long.parseLong(resultado[1].toString()));
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                                
                rcdto.setFrecibo(dateResult_2);
            }else{              
                rcdto.setFrecibo(null);
            }
            rcdto.setNrecibo(Long.parseLong(resultado[3].toString()));
            rcdto.setCtipo(resultado[4].toString());
            if(resultado[6] != null){
                Timestamp timeStamp_6 = (Timestamp) resultado[6];
                java.util.Date dateResult_6 = new Date(timeStamp_6.getTime());                                
                rcdto.setFfactur(dateResult_6);
            }else{              
                rcdto.setFfactur(null);
            }
            rcdto.setIefectivo(Long.parseLong(resultado[7].toString()));
            rcdto.setNroCheque(resultado[8].toString());
            rcdto.setIpagado(Long.parseLong(resultado[9].toString()));
            rcdto.setMoneda(Long.parseLong(resultado[10].toString()));
            rcdto.setCotizacion(Long.parseLong(resultado[11].toString()));
            listadoReciboCompras.add(rcdto);
        }        
        return listadoReciboCompras;
    }
}
