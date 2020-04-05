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

    public GenDatosContablesFacade() {
        super(Recibos.class);
    }
    
    @PreDestroy
    public void destruct() {
        getEntityManager().close();
    }

    public List<Object[]> busquedaDatosRecibosVentas(String fechaInicial, String fechaFinal) throws Exception { 
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

        return q.getResultList();
    }

    public List<Object[]> busquedaDatosRecibosCompras(String fechaInicial, String fechaFinal) throws Exception {
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

        return q.getResultList();
    }

    public List<Object[]> busquedaDatosFacturasVentas(String fechaInicial, String fechaFinal) throws Exception {
        Query qActivaIvaNoIncl = getEntityManager().createNativeQuery(" SELECT * FROM (SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin,  r.ntimbrado, "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact,  f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado, "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10), f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado,  "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10), f.ffactur,10) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado, "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10), f.ffactur, 103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado,  "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado, "
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
        
        List<Object[]> lista;

        lista = qActivaIvaNoIncl.getResultList();
        
        Query qInactivaIvaNoIncl = getEntityManager().createNativeQuery(" SELECT * FROM (SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado,   "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact,  f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado, "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10), f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin,r.ntimbrado,   "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10), f.ffactur,10) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin,r.ntimbrado,  "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10), f.ffactur, 103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, r.ntimbrado,  "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin,r.ntimbrado,  "
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

        lista.addAll(qInactivaIvaNoIncl.getResultList());
        
        Query qActivaIvaIncl = getEntityManager().createNativeQuery(" SELECT * FROM (SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura,  r.ntimbrado,  "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur,  f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado,"
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
                + " SELECT     f.xrazon_social, CONVERT(char(10), f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura, r.ntimbrado,  "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura,r.ntimbrado, "
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

        lista.addAll(qActivaIvaIncl.getResultList());
        
        Query qInactivaIvaIncl = getEntityManager().createNativeQuery(" SELECT * FROM (SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura,r.ntimbrado,  "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur,  f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc,  f.xfactura, r.ntimbrado, "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10), f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura, r.ntimbrado,  "
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
                + " SELECT     f.xrazon_social, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, f.xruc, f.xfactura, r.ntimbrado, "
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

        lista.addAll(qInactivaIvaIncl.getResultList());
        
//        Query qNotasCreditoInactivaIvaNoIncl = getEntityManager().createNativeQuery(" SELECT f.xrazon_social, f.xruc,  n.fac_ctipo_docum, convert(char(10), "
//                + " n.fdocum,103) as ffactur,  n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, 'F' as mtipo_papel, "
//                + " 0 as nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, SUM(d.iexentas) AS texentas, "
//                + " SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
//                + "        AS timpuestos_10, 0 AS timpuestos_5 "
//                + "	FROM         notas_ventas n INNER JOIN notas_ventas_det d "
//                + " ON N.nro_nota = d.nro_nota and n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum , facturas f "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND d.impuestos > 0 AND d.pimpues = 10 "
//                + " AND f.cod_empr = 2 "
//                + " AND f.nrofact = n.nrofact "
//                + " AND f.ctipo_docum = n.fac_ctipo_docum "
//                + " AND f.ffactur = n.ffactur "
//                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
//                + " GROUP BY  f.xrazon_social, f.xruc, n.fac_ctipo_docum, n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, n.xnro_nota "
//                + " UNION ALL "
//                + " SELECT   f.xrazon_social, f.xruc, n.fac_ctipo_docum, CONVERT(char(10),n.fdocum,103) as ffactur, "
//                + " n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, 'F' as mtipo_papel, 0 AS nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, "
//                + " SUM(d.iexentas) AS texentas, "
//                + " 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 "
//                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
//                + "	FROM         notas_ventas n  INNER JOIN notas_ventas_det d "
//                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum, facturas f "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND f.cod_empr = 2 "
//                + " AND f.nrofact = n.nrofact "
//                + " AND f.ctipo_docum = n.fac_ctipo_docum "
//                + " AND f.ffactur = n.ffactur "
//                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
//                + " AND d.impuestos > 0 AND d.pimpues = 5 "
//                + " GROUP BY  f.xrazon_social, f.xruc, n.fac_ctipo_docum, n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota,n.ttotal , n.xnro_nota "
//                + " ORDER BY 4, n.ctipo_docum, n.cconc ");

//        System.out.println(qNotasCreditoInactivaIvaNoIncl.toString());
//
//        lista.addAll(qNotasCreditoInactivaIvaNoIncl.getResultList());
        
//        Query qNotasCreditoInactivaIvaIncl = getEntityManager().createNativeQuery(" SELECT    f.xrazon_social, f.xruc, n.fac_ctipo_docum, CONVERT(char(10), n.fdocum,103)  as ffactur, "
//                + "  n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, 'F' as mtipo_papel, 0 as nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, SUM(d.iexentas) AS texentas, "
//                + " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
//                + "        AS timpuestos_10, 0 AS timpuestos_5 "
//                + "	FROM         notas_ventas n INNER JOIN notas_ventas_det d "
//                + " ON N.nro_nota = d.nro_nota AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum, facturas f "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND d.impuestos < 0 AND d.pimpues = 10 "
//                + " AND f.cod_empr = 2 "
//                + " AND n.nrofact = f.nrofact "
//                + " AND n.fac_ctipo_docum = f.ctipo_docum "
//                + " AND f.ffactur = n.ffactur "
//                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
//                + " GROUP BY f.xrazon_social, f.xruc, n.fac_ctipo_docum,n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, n.xnro_nota "
//                + " UNION ALL "
//                + " SELECT    f.xrazon_social, f.xruc, n.fac_ctipo_docum, CONVERT(char(10),n.fdocum,103) as ffactur, n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, "
//                + " 'F' as mtipo_papel, 0 AS nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, SUM(d.iexentas) AS texentas, "
//                + " 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 "
//                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
//                + "	FROM         notas_ventas n  INNER JOIN notas_ventas_det d "
//                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum, facturas f "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND d.impuestos < 0 AND d.pimpues = 5 "
//                + " AND f.cod_empr = 2 "
//                + " AND n.nrofact = f.nrofact "
//                + " AND n.fac_ctipo_docum = f.ctipo_docum "
//                + " AND f.ffactur = n.ffactur "
//                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
//                + " GROUP BY f.xrazon_social, f.xruc, n.fac_ctipo_docum,n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal, n.xnro_nota  "
//                + " UNION ALL "
//                + " SELECT   f.xrazon_social, f.xruc, n.fac_ctipo_docum, CONVERT(char(10),n.fdocum,103) as ffactur, n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, "
//                + " 'F' as mtipo_papel, 0 AS nro_docum_ini, 0 as nro_docum_fin, n.xnro_nota, SUM(d.iexentas) AS texentas, "
//                + " 0 AS tgravadas_10, 0 AS tgravadas_5, 0 "
//                + "        AS timpuestos_10, 0 AS timpuestos_5 "
//                + "	FROM         notas_ventas n  INNER JOIN notas_ventas_det d "
//                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum, facturas f "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND d.impuestos = 0 AND d.pimpues = 0 "
//                + " AND f.cod_empr = 2 "
//                + " AND n.nrofact = f.nrofact "
//                + " AND f.ffactur = n.ffactur "
//                + " AND n.fac_ctipo_docum = f.ctipo_docum "
//                + " GROUP BY  f.xrazon_social, f.xruc, n.fac_ctipo_docum,n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal, n.xnro_nota  "
//                + " ORDER BY 4, n.ctipo_docum, n.cconc ");
//
//        System.out.println(qNotasCreditoInactivaIvaIncl.toString());
//
//        lista.addAll(qNotasCreditoInactivaIvaIncl.getResultList());
        
        return lista;
    }
    
    public List<Object[]> busquedaDatosFacturasCompras(String fechaInicial, String fechaFinal) throws Exception {
        Query qIvaNoIncl = getEntityManager().createNativeQuery(" SELECT     p.xnombre, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum,   "
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
                + " SELECT     p.xnombre, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact,  f.ctipo_docum,  "
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
                + " SELECT     p.xnombre, CONVERT(char(10), f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum,  "
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

        List<Object[]> lista = qIvaNoIncl.getResultList();
//        List<RecibosFacturasComprasIvaInclNoIncl> listadoFacturas = new ArrayList<RecibosFacturasComprasIvaInclNoIncl>();
//        
//        for(Object[] resultado: resultadoIvaNoIncl){
//            RecibosFacturasComprasIvaInclNoIncl rfcdto = new RecibosFacturasComprasIvaInclNoIncl();
//            rfcdto.setCtipoDocum(resultado[0].toString());
//            rfcdto.setNrofact(Long.parseLong(resultado[1].toString()));
//            if(resultado[2] != null){
//                Timestamp timeStamp_2 = (Timestamp) resultado[2];
//                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                                
//                rfcdto.setFfactur(dateResult_2);
//            }else{              
//                rfcdto.setFfactur(null);
//            }
//            rfcdto.setXnombre(resultado[3].toString());
//            rfcdto.setCtipoDocum(resultado[4].toString());
//            rfcdto.setTgravadas10(Long.parseLong(resultado[7].toString()));
//            rfcdto.setXfactura(resultado[8].toString());
//            rfcdto.setTexentas(Long.parseLong(resultado[9].toString()));
//            rfcdto.setTtotal(Long.parseLong(resultado[10].toString()));
//            rfcdto.setTimpuestos5(Long.parseLong(resultado[11].toString()));
//            listadoFacturas.add(rfcdto);
//        }
        
        Query qIvaIncl = getEntityManager().createNativeQuery(" SELECT     p.xnombre, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact, f.ctipo_docum,   "
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
                + " SELECT     p.xnombre, CONVERT(char(10),f.ffactur,103) as ffactur, f.nrofact,  f.ctipo_docum,  "
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

//        List<Object[]> resultadoIvaIncl = 
        lista.addAll(qIvaIncl.getResultList());
//        for(Object[] resultado: resultadoIvaIncl){
//            RecibosFacturasComprasIvaInclNoIncl rfcdto = new RecibosFacturasComprasIvaInclNoIncl();
//            rfcdto.setCtipoDocum(resultado[0].toString());
//            rfcdto.setNrofact(Long.parseLong(resultado[1].toString()));
//            if(resultado[2] != null){
//                Timestamp timeStamp_2 = (Timestamp) resultado[2];
//                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                                
//                rfcdto.setFfactur(dateResult_2);
//            }else{              
//                rfcdto.setFfactur(null);
//            }
//            rfcdto.setXnombre(resultado[3].toString());
//            rfcdto.setCtipoDocum(resultado[4].toString());
//            rfcdto.setTgravadas10(Long.parseLong(resultado[7].toString()));
//            rfcdto.setXfactura(resultado[8].toString());
//            rfcdto.setTexentas(Long.parseLong(resultado[9].toString()));
//            rfcdto.setTtotal(Long.parseLong(resultado[10].toString()));
//            rfcdto.setTimpuestos5(Long.parseLong(resultado[11].toString()));
//            listadoFacturas.add(rfcdto);
//        }
        
//        Query qNotasCreditoIvaNoIncl = getEntityManager().createNativeQuery(" SELECT    p.xnombre ,  p.xruc, n.ctipo_docum,   convert(char(10), n.fdocum,103) as fdocum,  "
//                + "  n.nro_nota, n.cconc, n.ttotal,   n.ntimbrado,  "
//                + " SUM(d.iexentas) AS texentas, "
//                + " SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
//                + "        AS timpuestos_10, 0 AS timpuestos_5 "
//                + "	FROM         notas_compras n INNER JOIN notas_compras_det d "
//                + " ON N.nro_nota = d.nro_nota and n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum,  proveedores p "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND n.ctipo_docum IN ('NCC','NDC') "
//                + " AND n.cod_proveed = p.cod_proveed "
//                + " AND d.impuestos > 0 AND d.pimpues = 10 "
//                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
//                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, n.ntimbrado "
//                + " UNION ALL "
//                + " SELECT     p.xnombre, p.xruc, n.ctipo_docum, CONVERT(char(10),n.fdocum,103) as fdocum,  n.nro_nota, n.cconc, n.ttotal,  n.ntimbrado, "
//                + "  SUM(d.iexentas) AS texentas, "
//                + " 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 "
//                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
//                + "	FROM         notas_compras n  INNER JOIN notas_compras_det d "
//                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, proveedores p "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND n.ctipo_docum IN ('NCC','NDC') "
//                + " AND p.cod_proveed = n.cod_proveed "
//                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
//                + " AND d.impuestos > 0 AND d.pimpues = 5 "
//                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota,n.ttotal, n.ntimbrado  ");
//
//        System.out.println(qNotasCreditoIvaNoIncl.toString());

//        List<Object[]> resultadoNotasCreditoIvaNoIncl = 
//        lista.addAll(qNotasCreditoIvaNoIncl.getResultList());
//        for(Object[] resultado: resultadoNotasCreditoIvaNoIncl){
//            RecibosFacturasComprasIvaInclNoIncl rfcdto = new RecibosFacturasComprasIvaInclNoIncl();
//            rfcdto.setCtipoDocum(resultado[0].toString());
//            rfcdto.setNrofact(Long.parseLong(resultado[1].toString()));
//            if(resultado[2] != null){
//                Timestamp timeStamp_2 = (Timestamp) resultado[2];
//                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                                
//                rfcdto.setFfactur(dateResult_2);
//            }else{              
//                rfcdto.setFfactur(null);
//            }
//            rfcdto.setXnombre(resultado[3].toString());
//            rfcdto.setCtipoDocum(resultado[4].toString());
//            rfcdto.setTgravadas10(Long.parseLong(resultado[7].toString()));
//            rfcdto.setXfactura(resultado[8].toString());
//            rfcdto.setTexentas(Long.parseLong(resultado[9].toString()));
//            rfcdto.setTtotal(Long.parseLong(resultado[10].toString()));
//            rfcdto.setTimpuestos5(Long.parseLong(resultado[11].toString()));
//            listadoFacturas.add(rfcdto);
//        }
        
//        Query qNotasCreditoIvaIncl = getEntityManager().createNativeQuery(" SELECT    p.xnombre ,  p.xruc,   convert(char(10), n.fdocum,103) as fdocum,  "
//                + " n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, n.ntimbrado,  "
//                + " SUM(d.iexentas) AS texentas, "
//                + " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, SUM(ABS(d.impuestos)) "
//                + "        AS timpuestos_10, 0 AS timpuestos_5 "
//                + "	FROM         notas_compras n INNER JOIN notas_compras_det d "
//                + " ON N.nro_nota = d.nro_nota AND n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, proveedores p "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND p.cod_proveed = n.cod_proveed "
//                + " AND n.ctipo_docum IN ('NCC','NDC') "
//                + " AND d.impuestos < 0 AND d.pimpues = 10 "
//                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
//                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal,n.ntimbrado "
//                + " UNION ALL "
//                + " SELECT     p.xnombre, p.xruc,  CONVERT(char(10),n.fdocum,103) as fdocum, n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, n.ntimbrado, "
//                + " SUM(d.iexentas) AS texentas, "
//                + " 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 "
//                + "        AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "
//                + "	FROM         notas_compras n  INNER JOIN notas_compras_det d "
//                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, proveedores p "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND d.impuestos < 0 AND d.pimpues = 5 "
//                + " AND n.ctipo_docum IN ('NCC','NDC') "
//                + " AND p.cod_proveed = n.cod_proveed "
//                + " AND n.cod_empr = 2 and d.cod_empr = 2 "
//                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal, n.ntimbrado "
//                + " UNION ALL "
//                + " SELECT     p.xnombre, p.xruc, CONVERT(char(10),n.fdocum,103) as fdocum, n.ctipo_docum, n.nro_nota, n.cconc, n.ttotal, n.ntimbrado, "
//                + " SUM(d.iexentas) AS texentas, "
//                + " 0 AS tgravadas_10, 0 AS tgravadas_5, 0 "
//                + "        AS timpuestos_10, 0 AS timpuestos_5  "
//                + "	FROM         notas_compras n  INNER JOIN notas_compras_det d "
//                + " ON n.nro_nota = d.nro_nota  AND n.ctipo_docum = d.ctipo_docum AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, proveedores p "
//                + " WHERE     (n.fdocum BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "') "
//                + " AND (n.mestado = 'A')  "
//                + " AND n.ctipo_docum IN ('NCC','NDC') "
//                + " AND p.cod_proveed = n.cod_proveed "
//                + " AND d.impuestos = 0 AND d.pimpues = 0 "
//                + " GROUP BY p.xnombre, p.xruc, n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal, n.ntimbrado   ");
//
//        System.out.println(qNotasCreditoIvaIncl.toString());
//
//        List<Object[]> resultadoNotasCreditoIvaIncl = qNotasCreditoIvaIncl.getResultList();
//        for(Object[] resultado: resultadoNotasCreditoIvaIncl){
//            RecibosFacturasComprasIvaInclNoIncl rfcdto = new RecibosFacturasComprasIvaInclNoIncl();
//            rfcdto.setCtipoDocum(resultado[0].toString());
//            rfcdto.setNrofact(Long.parseLong(resultado[1].toString()));
//            if(resultado[2] != null){
//                Timestamp timeStamp_2 = (Timestamp) resultado[2];
//                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                                
//                rfcdto.setFfactur(dateResult_2);
//            }else{              
//                rfcdto.setFfactur(null);
//            }
//            rfcdto.setXnombre(resultado[3].toString());
//            rfcdto.setCtipoDocum(resultado[4].toString());
//            rfcdto.setTgravadas10(Long.parseLong(resultado[7].toString()));
//            rfcdto.setXfactura(resultado[8].toString());
//            rfcdto.setTexentas(Long.parseLong(resultado[9].toString()));
//            rfcdto.setTtotal(Long.parseLong(resultado[10].toString()));
//            rfcdto.setTimpuestos5(Long.parseLong(resultado[11].toString()));
//            listadoFacturas.add(rfcdto);
//        }
        
        return lista;
    }
    
    public String obtenerPath(){
        return datosGeneralesFacade.findAll().get(0).getTempPath();
    }
    
}
