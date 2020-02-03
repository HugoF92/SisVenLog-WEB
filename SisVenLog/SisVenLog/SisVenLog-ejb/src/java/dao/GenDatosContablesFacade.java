/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Recibos;
import dto.RecibosVentasDto;
import dto.RecibosComprasDto;
import dto.RecibosFacturasVentasIvaInclNoIncl;
import dto.RecibosFacturasComprasIvaInclNoIncl;
import entidad.DatosGenerales;
import dao.DatosGeneralesFacade;
import entidad.RangosDocumentos;
import entidad.TiposDocumentos;
import java.math.BigInteger;
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
    
    @EJB
    DatosGeneralesFacade datosGeneralesFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    RangosDocumentosFacade rangosDocumentosFacade;

    public GenDatosContablesFacade() {
        super(Recibos.class);
    }
    
    @PreDestroy
    public void destruct() {
        getEntityManager().close();
    }

    public List<Object[]> busquedaDatosRecibosVentas(String fechaInicial, String fechaFinal) throws Exception { 
        Query q = getEntityManager().createNativeQuery(" SELECT 'REC' as ctipo_docum, 1 as nro_cuota,"
                + " f.frecibo, f.nrecibo, d.ctipo_docum as ctipo, d.ndocum ,   "
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
        
        List<Object[]> resultadoFinal = new ArrayList();
        
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
            rvdto.setIefectivo(Double.parseDouble(resultado[7].toString()));
            rvdto.setNroCheque(resultado[8].toString());
            rvdto.setIpagado(Double.parseDouble(resultado[9].toString()));
            rvdto.setMoneda(Double.parseDouble(resultado[10].toString()));
            rvdto.setCotizacion(Double.parseDouble(resultado[11].toString()));
            
            String xNroFact = String.format("%03d", rvdto.getNdocum()) + "-" + String.format("%03d", rvdto.getNdocum()) + "-" + String.format("%07d", rvdto.getNdocum());
            
            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rvdto.getCtipoDocum());
            
            String lCuenta;
            
            if(rvdto.getIefectivo() > 0)
                lCuenta = tipoDocumento.getCodCtacble10();
            else lCuenta = tipoDocumento.getCodCtacble5();
            
            Object[] obj = {
                (Object)1,
                (Object)rvdto.getFrecibo(),
                (Object)xNroFact,
                (Object)0,
                (Object)rvdto.getFrecibo(),
                (Object)rvdto.getIefectivo(),
                (Object)lCuenta,
                (Object)rvdto.getIpagado(),
                (Object)rvdto.getNroCheque(),
                (Object)0,
                (Object)0,
                (Object)1,
                (Object)0
            };
            
            resultadoFinal.add(obj);
        }

        return resultadoFinal;
    }

    // no se hace por el momento
    public List<Object[]> busquedaDatosRecibosCompras(String fechaInicial, String fechaFinal) throws Exception {
        Query q = getEntityManager().createNativeQuery(" SELECT    'REP' as ctipo_docum,  1 as nro_cuota,  f.frecibo, f.nrecibo, d.nrofact , d.ctipo_docum as ctipo,   "
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
                + " SELECT   'REP' as ctipo_docum,   1 as nro_cuota,  f.frecibo, f.nrecibo, d.nrofact, d.ctipo_docum as ctipo,  "
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

        return q.getResultList();
    }

    public List<Object[]> busquedaDatosFacturasVentas(String fechaInicial, String fechaFinal) throws Exception {
        Query qActivaIvaNoIncl = getEntityManager().createNativeQuery(" SELECT * FROM (SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin,  r.ntimbrado, "
                + " F.TTOTAL, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + " AS timpuestos_10, 0 AS timpuestos_5 "
                + " FROM         facturas f INNER JOIN "
                + " rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos > 0 AND d.pimpues = 10 AND r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, F.Xfactura , r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact,  f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado, "
                + " f.ttotal, f.xruc, f.xfactura,  SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 "
                + " AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + " FROM         facturas f INNER JOIN "
                + " rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos > 0 AND d.pimpues = 5 AND r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado  "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado,  "
                + "  f.ttotal, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, 0 as tgravadas_5, 0 "
                + " AS timpuestos_10, 0 AS timpuestos_5 "
                + " FROM         facturas f INNER JOIN "
                + " rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos = 0 AND d.pimpues = 0 and r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura, r.ntimbrado  "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado, "
                + " f.ttotal, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + " AS timpuestos_10, 0 AS timpuestos_5 "
                + " FROM         facturas f INNER JOIN "
                + " rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos > 0 AND d.pimpues = 10 AND r.mtipo_papel = 'M' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado  "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado,  "
                + " f.ttotal, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 "
                + " AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + " FROM         facturas f INNER JOIN "
                + " rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos > 0 AND d.pimpues = 5 AND r.mtipo_papel = 'M' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura, r.ntimbrado  "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado, "
                + " f.ttotal, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, 0 as tgravadas_5, 0 "
                + " AS timpuestos_10, 0 AS timpuestos_5 "
                + " FROM         facturas f INNER JOIN "
                + " rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos = 0 AND d.pimpues = 0 and r.mtipo_papel = 'M' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura , r.ntimbrado "
                + " ) AS t ORDER BY t.ffactur, t.mtipo_papel, t.nro_docum_ini, t.nro_docum_fin ");

        System.out.println(qActivaIvaNoIncl.toString());
        
        List<Object[]> listaFinal = new ArrayList();
        
        List<Object[]> lista1 = qActivaIvaNoIncl.getResultList();
        
        for (Object[] resultado : lista1) {
            RecibosFacturasVentasIvaInclNoIncl rfvdto = new RecibosFacturasVentasIvaInclNoIncl();
            rfvdto.setXrazonSocial(resultado[0].toString());
            if (resultado[1] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[1];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfvdto.setFfactur(dateResult_2);
            } else {
                rfvdto.setFfactur(null);
            }
            rfvdto.setNrofact(Long.parseLong(resultado[2].toString()));
            rfvdto.setCtipoDocum(resultado[3].toString());
            rfvdto.setMtipoPapel(resultado[4].toString().charAt(0));
            rfvdto.setNroDocumIni(Long.parseLong(resultado[5].toString()));
            rfvdto.setNroDocumFin(Long.parseLong(resultado[6].toString()));
            rfvdto.setTtotal(Long.parseLong(resultado[7].toString()));
            rfvdto.setXruc(resultado[8].toString());
            rfvdto.setXfactura(resultado[9].toString());
            rfvdto.setNtimbrado(BigInteger.valueOf(Long.parseLong(resultado[10].toString())));
            rfvdto.setTexentas(Double.parseDouble(resultado[11].toString()));
            rfvdto.setTgravadas10(Double.parseDouble(resultado[12].toString()));
            rfvdto.setTgravadas5(Double.parseDouble(resultado[13].toString()));
            rfvdto.setTimpuestos10(Double.parseDouble(resultado[14].toString()));
            rfvdto.setTimpuestos5(Double.parseDouble(resultado[15].toString()));

//            String tGrabadas5;
            String lCuenta = "";
            Short lFormap;

//            if (rfvdto.getTimpuestos5() > 0) {
//                tGrabadas5 = "3.01.01.02.000";
//            } else {
//                tGrabadas5 = "1.01.10.01.001";
//            }

            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfvdto.getCtipoDocum());

            if (tipoDocumento != null) {
                if (rfvdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfvdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                lFormap = tipoDocumento.getCodContado();
            } else {
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfvdto.getNrofact(),
                (Object)2,
                (Object)rfvdto.getFfactur(),
                (Object)lFormap,
                (Object)rfvdto.getXruc(),
                (Object)1,
                (Object)rfvdto.getXfactura(),
                (Object)1,
                (Object)0,
                (Object)rfvdto.getTexentas(),
                (Object)rfvdto.getTgravadas5(),
                (Object)rfvdto.getTgravadas10(),
                (Object)rfvdto.getTtotal(),
                (Object)lCuenta,
                (Object)"Ventas",
                (Object)rfvdto.getTexentas(),
                (Object)(rfvdto.getTgravadas5()+rfvdto.getTgravadas10()),
                (Object)( (rfvdto.getTexentas()>0) ? "" : "N" ),
                (Object)1,
                (Object)( (rfvdto.getTexentas()>0) ? 0 : ( (rfvdto.getTgravadas10()>0) ? 5 : 10 ) ),
                (Object)rfvdto.getTimpuestos5(),
                (Object)rfvdto.getTimpuestos10(),
                (Object)( rfvdto.getTimpuestos5()+rfvdto.getTimpuestos10() ),
                (Object)rfvdto.getXrazonSocial(),
                (Object)rfvdto.getMtipoPapel(),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("FCR")) ? 1 : 0 ),
                (Object)rfvdto.getNtimbrado()
            };
            listaFinal.add(obj);
        }
        
        Query qInactivaIvaNoIncl = getEntityManager().createNativeQuery(" SELECT * FROM (SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado,   "
                + " F.TTOTAL, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos > 0 AND d.pimpues = 10 AND r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  F.Xfactura , r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact,  f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado, "
                + " f.ttotal, f.xruc, f.xfactura,  SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos > 0 AND d.pimpues = 5 AND r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura , r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin,r.ntimbrado,   "
                + "  f.ttotal, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, 0 as tgravadas_5, 0 "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum  AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos = 0 AND d.pimpues = 0 and r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin,r.ntimbrado,  "
                + " f.ttotal, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos > 0 AND d.pimpues = 10 AND r.mtipo_papel = 'M' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado  "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado,  "
                + " f.ttotal, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos > 0 AND d.pimpues = 5 AND r.mtipo_papel = 'M' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin,r.ntimbrado,  "
                + " f.ttotal, f.xruc, f.xfactura, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, 0 as tgravadas_5, 0 "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum  AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos = 0 AND d.pimpues = 0 and r.mtipo_papel = 'M' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura , r.ntimbrado "
                + " ) AS t ORDER BY t.ffactur, t.mtipo_papel, t.nro_docum_ini, t.nro_docum_fin ");

        System.out.println(qInactivaIvaNoIncl.toString());
        
        List<Object[]> lista2 = qInactivaIvaNoIncl.getResultList();
        
        for (Object[] resultado : lista2) {
            RecibosFacturasVentasIvaInclNoIncl rfvdto = new RecibosFacturasVentasIvaInclNoIncl();
            rfvdto.setXrazonSocial(resultado[0].toString());
            if (resultado[1] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[1];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfvdto.setFfactur(dateResult_2);
            } else {
                rfvdto.setFfactur(null);
            }
            rfvdto.setNrofact(Long.parseLong(resultado[2].toString()));
            rfvdto.setCtipoDocum(resultado[3].toString());
            rfvdto.setMtipoPapel(resultado[4].toString().charAt(0));
            rfvdto.setNroDocumIni(Long.parseLong(resultado[5].toString()));
            rfvdto.setNroDocumFin(Long.parseLong(resultado[6].toString()));
            rfvdto.setTtotal(Long.parseLong(resultado[7].toString()));
            rfvdto.setXruc(resultado[8].toString());
            rfvdto.setXfactura(resultado[9].toString());
            rfvdto.setNtimbrado(BigInteger.valueOf(Long.parseLong(resultado[10].toString())));
            rfvdto.setTexentas(Double.parseDouble(resultado[11].toString()));
            rfvdto.setTgravadas10(Double.parseDouble(resultado[12].toString()));
            rfvdto.setTgravadas5(Double.parseDouble(resultado[13].toString()));
            rfvdto.setTimpuestos10(Double.parseDouble(resultado[14].toString()));
            rfvdto.setTimpuestos5(Double.parseDouble(resultado[15].toString()));

            String lCuenta = "";
            Short lFormap;

            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfvdto.getCtipoDocum());

            if (tipoDocumento != null) {
                if (rfvdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfvdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                lFormap = tipoDocumento.getCodContado();
            } else {
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfvdto.getNrofact(),
                (Object)2,
                (Object)rfvdto.getFfactur(),
                (Object)lFormap,
                (Object)"1",
                (Object)1,
                (Object)rfvdto.getXfactura(),
                (Object)1,
                (Object)0,
                (Object)0L,
                (Object)0L,
                (Object)0L,
                (Object)0L,
                (Object)lCuenta,
                (Object)"Ventas",
                (Object)0L,
                (Object)0L,
                (Object)( (rfvdto.getTexentas()>0) ? "" : "N" ),
                (Object)1,
                (Object)( (rfvdto.getTexentas()>0) ? 0 : ( (rfvdto.getTgravadas10()>0) ? 5 : 10 ) ),
                (Object)0L,
                (Object)0L,
                (Object)0L,
                (Object)"ANULADO",
                (Object)rfvdto.getMtipoPapel(),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("FCR")) ? 1 : 0 ),
                (Object)rfvdto.getNtimbrado()
            };
            listaFinal.add(obj);
        }
        
        Query qActivaIvaIncl = getEntityManager().createNativeQuery(" SELECT * FROM (SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura,  r.ntimbrado,  "
                + " SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos < 0 AND d.pimpues = 10 AND r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact,  f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura , r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur,  f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado,"
                + " SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos < 0 AND d.pimpues = 5 AND r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum,  r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura, r.ntimbrado,  "
                + " SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum  AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos < 0 AND d.pimpues = 10 AND r.mtipo_papel = 'M' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura,r.ntimbrado, "
                + " SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum  AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " AND d.impuestos < 0 AND d.pimpues = 5 AND r.mtipo_papel = 'M' "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " ) AS t ORDER BY t.ffactur, t.mtipo_papel, t.nro_docum_ini, t.nro_docum_fin ");

        System.out.println(qActivaIvaIncl.toString());
        
        List<Object[]> lista3 = qActivaIvaIncl.getResultList();
        
        for (Object[] resultado : lista3) {
            RecibosFacturasVentasIvaInclNoIncl rfvdto = new RecibosFacturasVentasIvaInclNoIncl();
            rfvdto.setXrazonSocial(resultado[0].toString());
            if (resultado[1] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[1];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfvdto.setFfactur(dateResult_2);
            } else {
                rfvdto.setFfactur(null);
            }
            rfvdto.setNrofact(Long.parseLong(resultado[2].toString()));
            rfvdto.setCtipoDocum(resultado[3].toString());
            rfvdto.setMtipoPapel(resultado[4].toString().charAt(0));
            rfvdto.setNroDocumIni(Long.parseLong(resultado[5].toString()));
            rfvdto.setNroDocumFin(Long.parseLong(resultado[6].toString()));
            rfvdto.setTtotal(Long.parseLong(resultado[7].toString()));
            rfvdto.setXruc(resultado[8].toString());
            rfvdto.setXfactura(resultado[9].toString());
            rfvdto.setNtimbrado(BigInteger.valueOf(Long.parseLong(resultado[10].toString())));
            rfvdto.setTexentas(Double.parseDouble(resultado[11].toString()));
            rfvdto.setTgravadas10(Double.parseDouble(resultado[12].toString()));
            rfvdto.setTgravadas5(Double.parseDouble(resultado[13].toString()));
            rfvdto.setTimpuestos10(Double.parseDouble(resultado[14].toString()));
            rfvdto.setTimpuestos5(Double.parseDouble(resultado[15].toString()));

            String lCuenta = "";
            Short lFormap;

            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfvdto.getCtipoDocum());

            if (tipoDocumento != null) {
                if (rfvdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfvdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                lFormap = tipoDocumento.getCodContado();
            } else {
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfvdto.getNrofact(),
                (Object)2,
                (Object)rfvdto.getFfactur(),
                (Object)lFormap,
                (Object)rfvdto.getXruc(),
                (Object)1,
                (Object)rfvdto.getXfactura(),
                (Object)1,
                (Object)0,
                (Object)rfvdto.getTexentas(),
                (Object)rfvdto.getTgravadas5(),
                (Object)rfvdto.getTgravadas10(),
                (Object)rfvdto.getTtotal(),
                (Object)lCuenta,
                (Object)"Ventas",
                (Object)rfvdto.getTexentas(),
                (Object)(rfvdto.getTgravadas5()+rfvdto.getTgravadas10()),
                (Object)( (rfvdto.getTexentas()>0) ? "" : "I" ),
                (Object)1,
                (Object)( (rfvdto.getTexentas()>0) ? 0 : ( (rfvdto.getTgravadas10()>0) ? 5 : 10 ) ),
                (Object)rfvdto.getTimpuestos5(),
                (Object)rfvdto.getTimpuestos10(),
                (Object)( rfvdto.getTimpuestos5()+rfvdto.getTimpuestos10() ),
                (Object)rfvdto.getXrazonSocial(),
                (Object)rfvdto.getMtipoPapel(),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("FCR")) ? 1 : 0 ),
                (Object)rfvdto.getNtimbrado()
            };
            listaFinal.add(obj);
        }
        
        Query qInactivaIvaIncl = getEntityManager().createNativeQuery(" SELECT * FROM (SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura,r.ntimbrado,  "
                + " SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos < 0 AND d.pimpues = 10 AND r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact,  f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur,  f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado, "
                + " SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos < 0 AND d.pimpues = 5 AND r.mtipo_papel = 'F' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum,  r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura, r.ntimbrado,  "
                + " SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND d.impuestos < 0 AND d.pimpues = 10 AND r.mtipo_papel = 'M' "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " UNION ALL "
                + " SELECT     f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura, r.ntimbrado, "
                + " SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         facturas f INNER JOIN "
                + "    rangos_documentos r ON f.ctipo_docum = r.ctipo_docum AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final INNER JOIN facturas_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'X') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " AND d.impuestos < 0 AND d.pimpues = 5 AND r.mtipo_papel = 'M' "
                + " GROUP BY f.xrazon_social, f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado "
                + " ) AS t ORDER BY t.ffactur, t.mtipo_papel, t.nro_docum_ini, t.nro_docum_fin ");

        System.out.println(qInactivaIvaIncl.toString());
        
        List<Object[]> lista4 = qInactivaIvaIncl.getResultList();
        
        for (Object[] resultado : lista4) {
            RecibosFacturasVentasIvaInclNoIncl rfvdto = new RecibosFacturasVentasIvaInclNoIncl();
            rfvdto.setXrazonSocial(resultado[0].toString());
            if (resultado[1] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[1];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfvdto.setFfactur(dateResult_2);
            } else {
                rfvdto.setFfactur(null);
            }
            rfvdto.setNrofact(Long.parseLong(resultado[2].toString()));
            rfvdto.setCtipoDocum(resultado[3].toString());
            rfvdto.setMtipoPapel(resultado[4].toString().charAt(0));
            rfvdto.setNroDocumIni(Long.parseLong(resultado[5].toString()));
            rfvdto.setNroDocumFin(Long.parseLong(resultado[6].toString()));
            rfvdto.setTtotal(Long.parseLong(resultado[7].toString()));
            rfvdto.setXruc(resultado[8].toString());
            rfvdto.setXfactura(resultado[9].toString());
            rfvdto.setNtimbrado(BigInteger.valueOf(Long.parseLong(resultado[10].toString())));
            rfvdto.setTexentas(Double.parseDouble(resultado[11].toString()));
            rfvdto.setTgravadas10(Double.parseDouble(resultado[12].toString()));
            rfvdto.setTgravadas5(Double.parseDouble(resultado[13].toString()));
            rfvdto.setTimpuestos10(Double.parseDouble(resultado[14].toString()));
            rfvdto.setTimpuestos5(Double.parseDouble(resultado[15].toString()));

            String lCuenta = "";
            Short lFormap;

            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfvdto.getCtipoDocum());

            if (tipoDocumento != null) {
                if (rfvdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfvdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                lFormap = tipoDocumento.getCodContado();
            } else {
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfvdto.getNrofact(),
                (Object)2,
                (Object)rfvdto.getFfactur(),
                (Object)lFormap,
                (Object)"1",
                (Object)1,
                (Object)rfvdto.getXfactura(),
                (Object)1,
                (Object)0,
                (Object)0L,
                (Object)0L,
                (Object)0L,
                (Object)0L,
                (Object)lCuenta,
                (Object)"Ventas",
                (Object)0L,
                (Object)0L,
                (Object)( (rfvdto.getTexentas()>0) ? "" : "I" ),
                (Object)1,
                (Object)( (rfvdto.getTexentas()>0) ? 0 : ( (rfvdto.getTgravadas10()>0) ? 5 : 10 ) ),
                (Object)0L,
                (Object)0L,
                (Object)0L,
                (Object)"ANULADO",
                (Object)rfvdto.getMtipoPapel(),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("FCR")) ? 1 : 0 ),
                (Object)rfvdto.getNtimbrado()
            };
            listaFinal.add(obj);
        }
        
        Query qNotasCreditoInactivaIvaNoIncl = getEntityManager().createNativeQuery(" SELECT f.xrazon_social, f.xruc,  n.fac_ctipo_docum, n.fdocum as ffactur "
                + ",  n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, 'F' as mtipo_papel, "
                + " 0 as nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         notas_ventas n INNER JOIN notas_ventas_det d "
                + " ON N.nro_nota = d.nro_nota and n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum , facturas f "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND d.impuestos > 0 AND d.pimpues = 10 "
                + " AND f.cod_empr = 2 "
                + " AND f.nrofact = n.nrofact "
                + " AND f.ctipo_docum = n.fac_ctipo_docum "
                + " AND f.ffactur = n.ffactur "
                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY  f.xrazon_social, f.xruc, n.fac_ctipo_docum, n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, n.xnro_nota "
                + " UNION ALL "
                + " SELECT   f.xrazon_social, f.xruc, n.fac_ctipo_docum, n.fdocum as ffactur, "
                + " n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, 'F' as mtipo_papel, 0 AS nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, "
                + " SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         notas_ventas n  INNER JOIN notas_ventas_det d "
                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum, facturas f "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND f.cod_empr = 2 "
                + " AND f.nrofact = n.nrofact "
                + " AND f.ctipo_docum = n.fac_ctipo_docum "
                + " AND f.ffactur = n.ffactur "
                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
                + " AND d.impuestos > 0 AND d.pimpues = 5 "
                + " GROUP BY  f.xrazon_social, f.xruc, n.fac_ctipo_docum, n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota,n.ttotal , n.xnro_nota "
                + " ORDER BY 4, n.ctipo_docum, n.cconc ");

        System.out.println(qNotasCreditoInactivaIvaNoIncl.toString());
        
        List<Object[]> lista5 = qNotasCreditoInactivaIvaNoIncl.getResultList();
        
        for (Object[] resultado : lista5) {
            RecibosFacturasVentasIvaInclNoIncl rfvdto = new RecibosFacturasVentasIvaInclNoIncl();
            rfvdto.setXrazonSocial(resultado[0].toString());
            rfvdto.setXruc(resultado[1].toString());
            rfvdto.setFacCtipoDocum(resultado[2].toString());
            if (resultado[3] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[3];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfvdto.setFfactur(dateResult_2);
            } else {
                rfvdto.setFfactur(null);
            }
            rfvdto.setCtipoDocum(resultado[4].toString());
            rfvdto.setNrofact(Long.parseLong(resultado[5].toString()));
            rfvdto.setCconc(resultado[6].toString());
            rfvdto.setTtotal(Long.parseLong(resultado[7].toString()));
            rfvdto.setMtipoPapel(resultado[8].toString().charAt(0));
            rfvdto.setNroDocumIni(Long.parseLong(resultado[9].toString()));
            rfvdto.setNroDocumFin(Long.parseLong(resultado[10].toString()));
            rfvdto.setXfactura(resultado[11].toString());
            rfvdto.setTexentas(Double.parseDouble(resultado[12].toString()));
            rfvdto.setTgravadas10(Double.parseDouble(resultado[13].toString()));
            rfvdto.setTgravadas5(Double.parseDouble(resultado[14].toString()));
            rfvdto.setTimpuestos10(Double.parseDouble(resultado[15].toString()));
            rfvdto.setTimpuestos5(Double.parseDouble(resultado[16].toString()));

            String lCuenta;
            Short lFormap;
            
            RangosDocumentos rangosDocumentos = rangosDocumentosFacade.getRangosDocumentosByYearDocument(rfvdto.getNrofact(), String.valueOf(rfvdto.getFfactur().getYear()+1900));
            
            if(rangosDocumentos != null) rfvdto.setNtimbrado(rangosDocumentos.getNtimbrado());
            else rfvdto.setNtimbrado(BigInteger.ZERO);

            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfvdto.getCtipoDocum());

            if (tipoDocumento != null) {
                
                if (rfvdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfvdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                
                if (rfvdto.getFacCtipoDocum().equalsIgnoreCase("FCO") || rfvdto.getFacCtipoDocum().equalsIgnoreCase("CPV")) {
                    lFormap = tipoDocumento.getCodContado();
                } else {
                    lFormap = tipoDocumento.getCodCredito();
                }
                
            } else {
                lCuenta = "";
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfvdto.getNrofact(),
                (Object)2,
                (Object)rfvdto.getFfactur(),
                (Object)lFormap,
                (Object)rfvdto.getXruc(),
                (Object)1,
                (Object)rfvdto.getXfactura(),
                (Object)1,
                (Object)0,
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTexentas() : rfvdto.getTexentas() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTgravadas5() : rfvdto.getTgravadas5() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NCV")) ? rfvdto.getTgravadas10() : rfvdto.getTgravadas10() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTtotal(): rfvdto.getTtotal() * -1 ),
                (Object)lCuenta,
                (Object)"Ventas",
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTexentas() : rfvdto.getTexentas() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? (rfvdto.getTgravadas5()+rfvdto.getTgravadas10()) : (rfvdto.getTgravadas5()+rfvdto.getTgravadas10()) * -1 ),
                (Object)( (rfvdto.getTexentas()>0) ? "" : "N" ),
                (Object)1,
                (Object)( (rfvdto.getTexentas()>0) ? 0 : ( (rfvdto.getTgravadas10()>0) ? 5 : 10 ) ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTimpuestos5() : rfvdto.getTimpuestos5() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTimpuestos10() : rfvdto.getTimpuestos10() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? ( rfvdto.getTimpuestos5()+rfvdto.getTimpuestos10() ) : ( rfvdto.getTimpuestos5()+rfvdto.getTimpuestos10() ) * -1 ),
                (Object)rfvdto.getXrazonSocial(),
                (Object)rfvdto.getMtipoPapel(),
                (Object)0,
                (Object)rfvdto.getNtimbrado()
            };
            listaFinal.add(obj);
        }
        
        Query qNotasCreditoInactivaIvaIncl = getEntityManager().createNativeQuery(" SELECT    f.xrazon_social, f.xruc, n.fac_ctipo_docum, n.fdocum as ffactur, "
                + "  n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, 'F' as mtipo_papel, 0 as nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         notas_ventas n INNER JOIN notas_ventas_det d "
                + " ON N.nro_nota = d.nro_nota AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum, facturas f "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND d.impuestos < 0 AND d.pimpues = 10 "
                + " AND f.cod_empr = 2 "
                + " AND n.nrofact = f.nrofact "
                + " AND n.fac_ctipo_docum = f.ctipo_docum "
                + " AND f.ffactur = n.ffactur "
                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.xruc, n.fac_ctipo_docum,n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, n.xnro_nota "
                + " UNION ALL "
                + " SELECT    f.xrazon_social, f.xruc, n.fac_ctipo_docum, n.fdocum as ffactur, n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, "
                + " 'F' as mtipo_papel, 0 AS nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         notas_ventas n  INNER JOIN notas_ventas_det d "
                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum, facturas f "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND d.impuestos < 0 AND d.pimpues = 5 "
                + " AND f.cod_empr = 2 "
                + " AND n.nrofact = f.nrofact "
                + " AND n.fac_ctipo_docum = f.ctipo_docum "
                + " AND f.ffactur = n.ffactur "
                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY f.xrazon_social, f.xruc, n.fac_ctipo_docum,n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal, n.xnro_nota  "
                + " UNION ALL "
                + " SELECT   f.xrazon_social, f.xruc, n.fac_ctipo_docum, n.fdocum as ffactur, n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, "
                + " 'F' as mtipo_papel, 0 AS nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, 0 AS tgravadas_5, 0 "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         notas_ventas n  INNER JOIN notas_ventas_det d "
                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum, facturas f "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND d.impuestos = 0 AND d.pimpues = 0 "
                + " AND f.cod_empr = 2 "
                + " AND n.nrofact = f.nrofact "
                + " AND f.ffactur = n.ffactur "
                + " AND n.fac_ctipo_docum = f.ctipo_docum "
                + " GROUP BY  f.xrazon_social, f.xruc, n.fac_ctipo_docum,n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal, n.xnro_nota  "
                + " ORDER BY 4, n.ctipo_docum, n.cconc ");

        System.out.println(qNotasCreditoInactivaIvaIncl.toString());

        List<Object[]> lista6 = qNotasCreditoInactivaIvaIncl.getResultList();
        
        for (Object[] resultado : lista6) {
            RecibosFacturasVentasIvaInclNoIncl rfvdto = new RecibosFacturasVentasIvaInclNoIncl();
            rfvdto.setXrazonSocial(resultado[0].toString());
            rfvdto.setXruc(resultado[1].toString());
            rfvdto.setFacCtipoDocum(resultado[2].toString());
            if (resultado[3] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[3];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfvdto.setFfactur(dateResult_2);
            } else {
                rfvdto.setFfactur(null);
            }
            rfvdto.setCtipoDocum(resultado[4].toString());
            rfvdto.setNrofact(Long.parseLong(resultado[5].toString()));
            rfvdto.setCconc(resultado[6].toString());
            rfvdto.setTtotal(Long.parseLong(resultado[7].toString()));
            rfvdto.setMtipoPapel(resultado[8].toString().charAt(0));
            rfvdto.setNroDocumIni(Long.parseLong(resultado[9].toString()));
            rfvdto.setNroDocumFin(Long.parseLong(resultado[10].toString()));
            rfvdto.setXfactura(resultado[11].toString());
            rfvdto.setTexentas(Double.parseDouble(resultado[12].toString()));
            rfvdto.setTgravadas10(Double.parseDouble(resultado[13].toString()));
            rfvdto.setTgravadas5(Double.parseDouble(resultado[14].toString()));
            rfvdto.setTimpuestos10(Double.parseDouble(resultado[15].toString()));
            rfvdto.setTimpuestos5(Double.parseDouble(resultado[16].toString()));

            String lCuenta;
            Short lFormap;
            
            RangosDocumentos rangosDocumentos = rangosDocumentosFacade.getRangosDocumentosByYearDocument(rfvdto.getNrofact(), String.valueOf(rfvdto.getFfactur().getYear()+1900));
            
            if(rangosDocumentos != null) rfvdto.setNtimbrado(rangosDocumentos.getNtimbrado());
            else rfvdto.setNtimbrado(BigInteger.ZERO);

            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfvdto.getCtipoDocum());

            if (tipoDocumento != null) {
                
                if (rfvdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfvdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                
                if (rfvdto.getFacCtipoDocum().equalsIgnoreCase("FCO") || rfvdto.getFacCtipoDocum().equalsIgnoreCase("CPV")) {
                    lFormap = tipoDocumento.getCodContado();
                } else {
                    lFormap = tipoDocumento.getCodCredito();
                }
                
            } else {
                lCuenta = "";
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfvdto.getNrofact(),
                (Object)2,
                (Object)rfvdto.getFfactur(),
                (Object)lFormap,
                (Object)rfvdto.getXruc(),
                (Object)1,
                (Object)rfvdto.getXfactura(),
                (Object)1,
                (Object)0,
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTexentas() : rfvdto.getTexentas() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTgravadas5() : rfvdto.getTgravadas5() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTgravadas10() : rfvdto.getTgravadas10() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTtotal(): rfvdto.getTtotal() * -1 ),
                (Object)lCuenta,
                (Object)"Ventas",
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTexentas() : rfvdto.getTexentas() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? (rfvdto.getTgravadas5()+rfvdto.getTgravadas10()) : (rfvdto.getTgravadas5()+rfvdto.getTgravadas10()) * -1 ),
                (Object)( (rfvdto.getTexentas()>0) ? "" : "I" ),
                (Object)1,
                (Object)( (rfvdto.getTexentas()>0) ? 0 : ( (rfvdto.getTgravadas10()>0) ? 5 : 10 ) ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTimpuestos5() : rfvdto.getTimpuestos5() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? rfvdto.getTimpuestos10() : rfvdto.getTimpuestos10() * -1 ),
                (Object)( (rfvdto.getCtipoDocum().equalsIgnoreCase("NDV")) ? ( rfvdto.getTimpuestos5()+rfvdto.getTimpuestos10() ) : ( rfvdto.getTimpuestos5()+rfvdto.getTimpuestos10() ) * -1 ),
                (Object)rfvdto.getXrazonSocial(),
                (Object)rfvdto.getMtipoPapel(),
                (Object)0,
                (Object)rfvdto.getNtimbrado()
            };
            listaFinal.add(obj);
        }
        
        return listaFinal;
    }
    
    public List<Object[]> busquedaDatosFacturasCompras(String fechaInicial, String fechaFinal) throws Exception {
        Query qIvaNoIncl = getEntityManager().createNativeQuery(" SELECT     p.xnombre, f.ffactur, f.nrofact, f.ctipo_docum,   "
                + " F.TTOTAL, P.xruc, f.xfactura, f.ntimbrado, SUM(d.iexentas) AS texentas, "
                + " SUM(d.itotal) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         compras f  INNER JOIN compras_det d "
                + " ON f.nrofact = d.nrofact AND f.cod_proveed = d.cod_proveed  AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur, proveedores p "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A')  "
                + " AND f.cod_proveed = p.cod_proveed "
                + " AND d.impuestos > 0 AND d.pimpues = 10  "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY p.xnombre, f.ffactur, f.nrofact, f.ntimbrado,f.ctipo_docum,  f.ttotal, p.xruc,  F.xfactura  "
                + " UNION ALL "
                + " SELECT     p.xnombre, f.ffactur, f.nrofact,  f.ctipo_docum,  "
                + " f.ttotal, p.xruc, f.xfactura,  f.ntimbrado, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.itotal) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         compras f  INNER JOIN compras_det d  "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum and f.cod_proveed = d.cod_proveed AND f.ffactur = d.ffactur, PROVEEDORES P "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A')  "
                + " AND f.cod_proveed = p.cod_proveed "
                + " AND d.impuestos > 0 AND d.pimpues = 5  "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY p.xnombre, f.ffactur, f.nrofact, f.ntimbrado, f.ctipo_docum,  f.ttotal, p.xruc,  f.xfactura "
                + " UNION ALL "
                + " SELECT     p.xnombre, f.ffactur, f.nrofact, f.ctipo_docum,  "
                + "  f.ttotal, p.xruc, f.xfactura, f.ntimbrado, SUM(d.itotal) AS texentas, "
                + " 0 AS tgravadas_10, 0 as tgravadas_5, 0 "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         compras f  INNER JOIN compras_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.cod_proveed = d.cod_proveed AND f.ffactur = d.ffactur, PROVEEDORES P "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND f.cod_proveed = p.cod_proveed "
                + " AND (f.mestado = 'A')  "
                + " AND d.impuestos = 0 AND d.pimpues = 0  "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY p.xnombre, f.ffactur, f.nrofact, f.ntimbrado, f.ctipo_docum, f.ttotal, p.xruc,  f.xfactura ");

        System.out.println(qIvaNoIncl.toString());
        
        List<Object[]> listaFinal = new ArrayList();

        List<Object[]> lista1 = qIvaNoIncl.getResultList();
        
        for (Object[] resultado : lista1) {
            RecibosFacturasComprasIvaInclNoIncl rfcdto = new RecibosFacturasComprasIvaInclNoIncl();
            rfcdto.setXnombre(resultado[0].toString());
            if (resultado[1] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[1];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfcdto.setFfactur(dateResult_2);
            } else {
                rfcdto.setFfactur(null);
            }
            rfcdto.setNrofact(Long.parseLong(resultado[2].toString()));
            rfcdto.setCtipoDocum(resultado[3].toString());
            rfcdto.setTtotal(Double.parseDouble(resultado[4].toString()));
            rfcdto.setXruc(resultado[5].toString());
            rfcdto.setXfactura(resultado[6].toString());
            rfcdto.setNtimbrado(BigInteger.valueOf(Long.parseLong(resultado[7].toString())));
            rfcdto.setTexentas(Double.parseDouble(resultado[8].toString()));
            rfcdto.setTgravadas10(Double.parseDouble(resultado[9].toString()));
            rfcdto.setTgravadas5(Double.parseDouble(resultado[10].toString()));
            rfcdto.setTimpuestos10(Double.parseDouble(resultado[11].toString()));
            rfcdto.setTimpuestos5(Double.parseDouble(resultado[12].toString()));

            String lCuenta;
            Short lFormap;
            
            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfcdto.getCtipoDocum());

            if (tipoDocumento != null) {
                
                if (rfcdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfcdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                
                if (rfcdto.getCtipoDocum().equalsIgnoreCase("FCC")) {
                    lFormap = tipoDocumento.getCodCredito();
                } else {
                    lFormap = tipoDocumento.getCodContado();
                }
                
            } else {
                lCuenta = "";
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfcdto.getNrofact(),
                (Object)2,
                (Object)rfcdto.getFfactur(),
                (Object)rfcdto.getXfactura(),
                (Object)rfcdto.getXruc(),
                (Object)lFormap,
                (Object)1,
                (Object)0,
                (Object)1,
                (Object)1,
                (Object)rfcdto.getTexentas(),
                (Object)rfcdto.getTgravadas5(),
                (Object)rfcdto.getTgravadas10(),
                (Object)rfcdto.getTtotal(),
                (Object)lCuenta,
                (Object)"Compras",
                (Object)rfcdto.getTexentas(),
                (Object)(rfcdto.getTgravadas5()+rfcdto.getTgravadas10()),
                (Object)( (rfcdto.getTexentas()>0) ? "" : "N" ),
                (Object)2,
                (Object)( (rfcdto.getTexentas()>0) ? 0 : ( (rfcdto.getTgravadas5()>0) ? 5 : 10 ) ),
                (Object)rfcdto.getTimpuestos5(),
                (Object)rfcdto.getTimpuestos10(),
                (Object)( rfcdto.getTimpuestos5()+rfcdto.getTimpuestos10() ),
                (Object)rfcdto.getXnombre(),
                (Object)( (rfcdto.getCtipoDocum().equalsIgnoreCase("FCC")) ? 1 : 0 ),
                (Object)( (rfcdto.getNtimbrado()!=null) ? rfcdto.getNtimbrado() : 0 )
            };
            listaFinal.add(obj);
        }
        
        Query qIvaIncl = getEntityManager().createNativeQuery(" SELECT     p.xnombre, f.ffactur, f.nrofact, f.ctipo_docum,   "
                + " F.TTOTAL, p.xruc, f.xfactura, f.ntimbrado, SUM(d.iexentas) AS texentas, "
                + " SUM(d.itotal+d.impuestos)AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         compras f  INNER JOIN compras_det d "
                + " ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.cod_proveed = d.cod_proveed AND f.ffactur = d.ffactur, "
                + "  proveedores p "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A')  "
                + " AND f.cod_proveed = p.cod_proveed "
                + " AND d.impuestos < 0 AND d.pimpues = 10  "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY p.xnombre, f.ffactur, f.nrofact, f.ntimbrado, f.ctipo_docum,  f.ttotal, p.xruc,  F.xfactura "
                + " UNION ALL "
                + " SELECT     p.xnombre, f.ffactur, f.nrofact,  f.ctipo_docum,  "
                + " f.ttotal, p.xruc, f.xfactura, f.ntimbrado, SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.itotal+d.impuestos ) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         compras f  INNER JOIN compras_det d  "
                + " ON f.nrofact = d.nrofact AND f.cod_proveed = d.cod_proveed AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur, proveedores p "
                + " WHERE     (f.ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (f.mestado = 'A')  "
                + " AND f.cod_proveed = p.cod_proveed "
                + " AND d.impuestos < 0 AND d.pimpues = 5  "
                + " AND f.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY p.xnombre, f.ffactur, f.nrofact, f.ntimbrado, f.ctipo_docum,  f.ttotal, p.xruc,  f.xfactura ");

        System.out.println(qIvaIncl.toString());
        
        List<Object[]> lista2 = qIvaIncl.getResultList();
        
        for (Object[] resultado : lista2) {
            RecibosFacturasComprasIvaInclNoIncl rfcdto = new RecibosFacturasComprasIvaInclNoIncl();
            rfcdto.setXnombre(resultado[0].toString());
            if (resultado[1] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[1];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfcdto.setFfactur(dateResult_2);
            } else {
                rfcdto.setFfactur(null);
            }
            rfcdto.setNrofact(Long.parseLong(resultado[2].toString()));
            rfcdto.setCtipoDocum(resultado[3].toString());
            rfcdto.setTtotal(Double.parseDouble(resultado[4].toString()));
            rfcdto.setXruc(resultado[5].toString());
            rfcdto.setXfactura(resultado[6].toString());
            rfcdto.setNtimbrado(BigInteger.valueOf(Long.parseLong(resultado[7].toString())));
            rfcdto.setTexentas(Double.parseDouble(resultado[8].toString()));
            rfcdto.setTgravadas10(Double.parseDouble(resultado[9].toString()));
            rfcdto.setTgravadas5(Double.parseDouble(resultado[10].toString()));
            rfcdto.setTimpuestos10(Double.parseDouble(resultado[11].toString()));
            rfcdto.setTimpuestos5(Double.parseDouble(resultado[12].toString()));

            String lCuenta;
            Short lFormap;
            
            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfcdto.getCtipoDocum());

            if (tipoDocumento != null) {
                
                if (rfcdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfcdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                
                if (rfcdto.getCtipoDocum().equalsIgnoreCase("FCC")) {
                    lFormap = tipoDocumento.getCodCredito();
                } else {
                    lFormap = tipoDocumento.getCodContado();
                }
                
            } else {
                lCuenta = "";
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfcdto.getNrofact(),
                (Object)2,
                (Object)rfcdto.getFfactur(),
                (Object)rfcdto.getXfactura(),
                (Object)rfcdto.getXruc(),
                (Object)lFormap,
                (Object)1,
                (Object)0,
                (Object)1,
                (Object)1,
                (Object)rfcdto.getTexentas(),
                (Object)rfcdto.getTgravadas5(),
                (Object)rfcdto.getTgravadas10(),
                (Object)rfcdto.getTtotal(),
                (Object)lCuenta,
                (Object)"Compras",
                (Object)rfcdto.getTexentas(),
                (Object)(rfcdto.getTgravadas5()+rfcdto.getTgravadas10()),
                (Object)( (rfcdto.getTexentas()>0) ? "" : "S" ),
                (Object)2,
                (Object)( (rfcdto.getTexentas()>0) ? 0 : ( (rfcdto.getTgravadas5()>0) ? 5 : 10 ) ),
                (Object)rfcdto.getTimpuestos5(),
                (Object)rfcdto.getTimpuestos10(),
                (Object)( rfcdto.getTimpuestos5()+rfcdto.getTimpuestos10() ),
                (Object)rfcdto.getXnombre(),
                (Object)( (rfcdto.getCtipoDocum().equalsIgnoreCase("FCC")) ? 1 : 0 ),
                (Object)( (rfcdto.getNtimbrado()!=null) ? rfcdto.getNtimbrado() : 0 )
            };
            listaFinal.add(obj);
        }
        
        Query qNotasCreditoIvaNoIncl = getEntityManager().createNativeQuery(" SELECT    p.xnombre ,  p.xruc, n.ctipo_docum, n.fdocum,  "
                + "  n.nro_nota, n.cconc, n.ttotal,   n.ntimbrado,  "
                + " SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         notas_compras n INNER JOIN notas_compras_det d "
                + " ON N.nro_nota = d.nro_nota and n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum,  proveedores p "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND n.ctipo_docum IN ('NCC','NDC') "
                + " AND n.cod_proveed = p.cod_proveed "
                + " AND d.impuestos > 0 AND d.pimpues = 10 "
                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, n.ntimbrado "
                + " UNION ALL "
                + " SELECT     p.xnombre, p.xruc, n.ctipo_docum, n.fdocum,  n.nro_nota, n.cconc, n.ttotal,  n.ntimbrado, "
                + "  SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         notas_compras n  INNER JOIN notas_compras_det d "
                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, proveedores p "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND n.ctipo_docum IN ('NCC','NDC') "
                + " AND p.cod_proveed = n.cod_proveed "
                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
                + " AND d.impuestos > 0 AND d.pimpues = 5 "
                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota,n.ttotal, n.ntimbrado  ");

        System.out.println(qNotasCreditoIvaNoIncl.toString());
        
        List<Object[]> lista3 = qNotasCreditoIvaNoIncl.getResultList();
        
        for (Object[] resultado : lista3) {
            RecibosFacturasComprasIvaInclNoIncl rfcdto = new RecibosFacturasComprasIvaInclNoIncl();
            rfcdto.setXnombre(resultado[0].toString());
            rfcdto.setXruc(resultado[1].toString());
            rfcdto.setCtipoDocum(resultado[2].toString());
            if (resultado[3] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[3];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfcdto.setFfactur(dateResult_2);
            } else {
                rfcdto.setFfactur(null);
            }
            rfcdto.setNrofact(Long.parseLong(resultado[4].toString()));
            rfcdto.setCconc(resultado[5].toString());
            rfcdto.setTtotal(Double.parseDouble(resultado[6].toString()));
            rfcdto.setNtimbrado(BigInteger.valueOf(Long.parseLong(resultado[6].toString())));            
            rfcdto.setTexentas(Double.parseDouble(resultado[7].toString()));
            rfcdto.setTgravadas10(Double.parseDouble(resultado[8].toString()));
            rfcdto.setTgravadas5(Double.parseDouble(resultado[9].toString()));
            rfcdto.setTimpuestos10(Double.parseDouble(resultado[10].toString()));
            rfcdto.setTimpuestos5(Double.parseDouble(resultado[11].toString()));

            String lCuenta;
            Short lFormap;
            
            String xNroFact = String.format("%03d", rfcdto.getNrofact()) + "-" + String.format("%03d", rfcdto.getNrofact()) + "-" + String.format("%07d", rfcdto.getNrofact());
            
            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfcdto.getCtipoDocum());

            if (tipoDocumento != null) {
                
                if (rfcdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfcdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                
                lFormap = tipoDocumento.getCodContado();
                
            } else {
                lCuenta = "";
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfcdto.getNrofact(),
                (Object)2,
                (Object)rfcdto.getFfactur(),
                (Object)xNroFact,
                (Object)rfcdto.getXruc(),
                (Object)lFormap,
                (Object)1,
                (Object)0,
                (Object)1,
                (Object)1,
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTexentas() : rfcdto.getTexentas() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTgravadas5() : rfcdto.getTgravadas5() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTgravadas10() : rfcdto.getTgravadas10() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTtotal(): rfcdto.getTtotal() * -1 ),
                (Object)lCuenta,
                (Object)( (rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? "NC Compras" : "ND Compras" ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTexentas() : rfcdto.getTexentas() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? (rfcdto.getTgravadas5()+rfcdto.getTgravadas10()) : (rfcdto.getTgravadas5()+rfcdto.getTgravadas10()) * -1 ),
                (Object)( (rfcdto.getTexentas()>0) ? "" : "S" ),
                (Object)2,
                (Object)( (rfcdto.getTexentas()>0) ? 0 : ( (rfcdto.getTgravadas5()>0) ? 5 : 10 ) ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTimpuestos5() : rfcdto.getTimpuestos5() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTimpuestos10() : rfcdto.getTimpuestos10() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? ( rfcdto.getTimpuestos5()+rfcdto.getTimpuestos10() ) : ( rfcdto.getTimpuestos5()+rfcdto.getTimpuestos10() ) * -1 ),
                (Object)rfcdto.getXnombre(),
                (Object)1,
                ( (rfcdto.getNtimbrado()!=null) ? rfcdto.getNtimbrado() : 0 )
            };
            listaFinal.add(obj);
        }

        Query qNotasCreditoIvaIncl = getEntityManager().createNativeQuery(" SELECT    p.xnombre ,  p.xruc, n.ctipo_docum, n.fdocum,  "
                + " n.nro_nota, n.cconc, n.ttotal, n.ntimbrado,  "
                + " SUM(d.iexentas) AS texentas, "
                + " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
                + "        AS timpuestos_10, 0 AS timpuestos_5 "
                + "	FROM         notas_compras n INNER JOIN notas_compras_det d "
                + " ON N.nro_nota = d.nro_nota AND n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, proveedores p "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND p.cod_proveed = n.cod_proveed "
                + " AND n.ctipo_docum IN ('NCC','NDC') "
                + " AND d.impuestos < 0 AND d.pimpues = 10 "
                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal,n.ntimbrado "
                + " UNION ALL "
                + " SELECT     p.xnombre, p.xruc, n.ctipo_docum, n.fdocum, n.nro_nota, n.cconc, n.ttotal, n.ntimbrado, "
                + " SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 "
                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
                + "	FROM         notas_compras n  INNER JOIN notas_compras_det d "
                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, proveedores p "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND d.impuestos < 0 AND d.pimpues = 5 "
                + " AND n.ctipo_docum IN ('NCC','NDC') "
                + " AND p.cod_proveed = n.cod_proveed "
                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal, n.ntimbrado "
                + " UNION ALL "
                + " SELECT     p.xnombre, p.xruc, n.ctipo_docum, n.fdocum, n.nro_nota, n.cconc, n.ttotal, n.ntimbrado, "
                + " SUM(d.iexentas) AS texentas, "
                + " 0 AS tgravadas_10, 0 AS tgravadas_5, 0 "
                + "        AS timpuestos_10, 0 AS timpuestos_5  "
                + "	FROM         notas_compras n  INNER JOIN notas_compras_det d "
                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, proveedores p "
                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
                + " AND (n.mestado = 'A')  "
                + " AND n.ctipo_docum IN ('NCC','NDC') "
                + " AND p.cod_proveed = n.cod_proveed "
                + " AND d.impuestos = 0 AND d.pimpues = 0 "
                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal, n.ntimbrado   ");

        System.out.println(qNotasCreditoIvaIncl.toString());

        List<Object[]> lista4 = qNotasCreditoIvaIncl.getResultList();
        
        for (Object[] resultado : lista4) {
            RecibosFacturasComprasIvaInclNoIncl rfcdto = new RecibosFacturasComprasIvaInclNoIncl();
            rfcdto.setXnombre(resultado[0].toString());
            rfcdto.setXruc(resultado[1].toString());
            rfcdto.setCtipoDocum(resultado[2].toString());
            if (resultado[3] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[3];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                rfcdto.setFfactur(dateResult_2);
            } else {
                rfcdto.setFfactur(null);
            }
            rfcdto.setNrofact(Long.parseLong(resultado[4].toString()));
            rfcdto.setCconc(resultado[5].toString());
            rfcdto.setTtotal(Double.parseDouble(resultado[6].toString()));
            rfcdto.setNtimbrado(BigInteger.valueOf(Long.parseLong(resultado[6].toString())));            
            rfcdto.setTexentas(Double.parseDouble(resultado[7].toString()));
            rfcdto.setTgravadas10(Double.parseDouble(resultado[8].toString()));
            rfcdto.setTgravadas5(Double.parseDouble(resultado[9].toString()));
            rfcdto.setTimpuestos10(Double.parseDouble(resultado[10].toString()));
            rfcdto.setTimpuestos5(Double.parseDouble(resultado[11].toString()));

            String lCuenta;
            Short lFormap;
            
            String xNroFact = String.format("%03d", rfcdto.getNrofact()) + "-" + String.format("%03d", rfcdto.getNrofact()) + "-" + String.format("%07d", rfcdto.getNrofact());
            
            TiposDocumentos tipoDocumento = tiposDocumentosFacade.getTipoDocumentoByCTipoDocumento(rfcdto.getCtipoDocum());

            if (tipoDocumento != null) {
                
                if (rfcdto.getTgravadas10() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble10();
                } else if (rfcdto.getTgravadas5() > 0) {
                    lCuenta = tipoDocumento.getCodCtacble5();
                } else {
                    lCuenta = tipoDocumento.getCodCtacblex();
                }
                
                lFormap = tipoDocumento.getCodContado();
                
            } else {
                lCuenta = "";
                lFormap = 0;
            }
            
            Object[] obj = {
                (Object)rfcdto.getNrofact(),
                (Object)2,
                (Object)rfcdto.getFfactur(),
                (Object)xNroFact,
                (Object)rfcdto.getXruc(),
                (Object)lFormap,
                (Object)1,
                (Object)0,
                (Object)1,
                (Object)1,
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTexentas() : rfcdto.getTexentas() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTgravadas5() : rfcdto.getTgravadas5() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTgravadas10() : rfcdto.getTgravadas10() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTtotal(): rfcdto.getTtotal() * -1 ),
                (Object)lCuenta,
                (Object)( (rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? "NC Compras" : "ND Compras" ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTexentas() : rfcdto.getTexentas() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? (rfcdto.getTgravadas5()+rfcdto.getTgravadas10()) : (rfcdto.getTgravadas5()+rfcdto.getTgravadas10()) * -1 ),
                (Object)( (rfcdto.getTexentas()>0) ? "" : "S" ),
                (Object)2,
                (Object)( (rfcdto.getTexentas()>0) ? 0 : ( (rfcdto.getTgravadas5()>0) ? 5 : 10 ) ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTimpuestos5() : rfcdto.getTimpuestos5() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? rfcdto.getTimpuestos10() : rfcdto.getTimpuestos10() * -1 ),
                (Object)( (!rfcdto.getCtipoDocum().equalsIgnoreCase("NCC")) ? ( rfcdto.getTimpuestos5()+rfcdto.getTimpuestos10() ) : ( rfcdto.getTimpuestos5()+rfcdto.getTimpuestos10() ) * -1 ),
                (Object)rfcdto.getXnombre(),
                (Object)1,
                ( (rfcdto.getNtimbrado()!=null) ? rfcdto.getNtimbrado() : 0 )
            };
            listaFinal.add(obj);
        }
        
        return listaFinal;
    }
    
    public String obtenerPath(){
        return datosGeneralesFacade.findAll().get(0).getTempPath();
    }
    
}
