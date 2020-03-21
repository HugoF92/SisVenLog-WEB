/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;



import entidad.Zonas;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;

/**
 *
 * @author jvera
 * 
 */
@Stateless
public class LiVentasCreditoFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    protected EntityManager getEntityManager() {
        return em;
    }

    public LiVentasCreditoFacade() {
        
    }
    
    public void prepararDatosListadoVentasCredito(Date fechaDocumentoDesde, 
                                                Date fechaDocumentoHasta,
                                                Zonas zona,
                                                String discriminado){
        
        String sql = "IF OBJECT_ID('mostrar') IS NOT NULL DROP TABLE mostrar" +
            " IF OBJECT_ID('moschq') IS NOT NULL DROP TABLE moschq" +
            " IF OBJECT_ID('mospag') IS NOT NULL DROP TABLE mospag" +
            " IF OBJECT_ID('curfin1') IS NOT NULL DROP TABLE curfin1" +
            " IF OBJECT_ID('curfin') IS NOT NULL DROP TABLE curfin" +
            " IF OBJECT_ID('zonas_dest') IS NOT NULL DROP TABLE zonas_dest";
        //busqueda de facturas sin recibos
        sql += " SELECT f.cod_zona, f.ffactur, SUM((C1.tgravadas+C1.texentas+C1.timpuestos) * mindice) as ftotal, 0 as tcheques, 0 as tpagares" +
            " INTO mostrar FROM cuentas_corrientes c1, facturas f" +
            " WHERE c1.ctipo_docum IN ('FCR','NCV','NDV')" +
            " AND c1.fmovim BETWEEN '" + DateUtil.dateToString(fechaDocumentoDesde) + "' AND '" + DateUtil.dateToString(fechaDocumentoHasta) + "'" +
            " AND c1.cod_empr = 2" +
            " AND c1.fac_ctipo_docum = f.ctipo_docum" +
            " AND c1.fac_ctipo_docum = 'FCR'" +
            " AND c1.nrofact = f.nrofact" +
            " AND c1.ffactur = f.ffactur" +
            " AND NOT EXISTS (SELECT * FROM cuentas_corrientes c2" +
            " WHERE c2.ctipo_docum = 'REC' AND c2.fmovim = f.ffactur AND c2.cod_empr = 2" +
            " AND c2.nrofact = f.nrofact and c2.fac_ctipo_docum = f.ctipo_docum )";
            if (zona != null) {
                sql += " AND f.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
            }
            sql += " GROUP BY f.cod_zona, f.ffactur" +
            " HAVING" +
            " SUM((C1.tgravadas+C1.texentas+C1.timpuestos) * mindice) > 0" +
            
            //busqueda de cheques
            " SELECT r.cod_zona, c1.fmovim as ffactur, 0 as ftotal, SUM(ipagado) as tcheques, 000000000 as tpagares" +
            " INTO moschq FROM cuentas_corrientes c1, cheques ch, clientes c, rutas r" +
            " WHERE c1.ctipo_docum = 'CHQ'" +
            " AND c1.cod_empr = 2" +
            " AND c1.fmovim BETWEEN '" + DateUtil.dateToString(fechaDocumentoDesde) + "' AND '" + DateUtil.dateToString(fechaDocumentoHasta) + "'" +
            " AND c1.cod_empr = ch.cod_empr" +
            " AND c1.ndocum_cheq = ch.nro_cheque" +
            " AND ch.cod_cliente = c.cod_cliente" +
            " AND c.cod_ruta = r.cod_ruta" +
            " AND c1.cod_banco = ch.cod_banco" +
            " AND ch.mtipo = 'D'";
            if (zona != null){
                sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
            }
            sql += " GROUP BY r.cod_zona, c1.fmovim" +
            " HAVING SUM(ipagado) > 0" +
            
            //busqueda de pagares
            " SELECT z.cod_zona, c.fmovim as ffactur, 0 as ftotal, 0 as tcheques, sum(ipagado) as tpagares" +
            " INTO mospag FROM zonas z , cuentas_corrientes c, pagares h" +
            " WHERE z.cod_zona = h.cod_zona AND h.cod_empr = 2" +
            " AND c.ndocum_cheq = CAST(h.nPAGARE AS CHAR)" +
            " AND(c.fmovim between '" + DateUtil.dateToString(fechaDocumentoDesde) + "' AND '" + DateUtil.dateToString(fechaDocumentoHasta) + "'" + ") AND (c.cod_empr = 2) AND (c.ctipo_docum = 'PAG')";
            if (zona != null){
                sql += " AND h.cod_zona = '" + zona.getZonasPK().getCodZona() + "'";
            }
            sql += " AND NOT EXISTS (select * from cuentas_corrientes c2" +
            " WHERE c2.ctipo_docum = 'PAC'" +
            " AND c2.fmovim BETWEEN '" + DateUtil.dateToString(fechaDocumentoDesde) + "' AND '" + DateUtil.dateToString(fechaDocumentoHasta) + "'" +
            " AND c2.ndocum_cheq = c.ndocum_cheq)" +
            " GROUP BY z.cod_zona, c.fmovim" +
            
            //unimos los 3 cursores: FACTURAS, PAGARES Y CHEQUES en el cursor Curfin1"
            " SELECT cod_zona, ffactur, ftotal, tcheques, tpagares INTO curfin1 FROM mostrar" +
            " UNION ALL" +
            " SELECT cod_zona, ffactur, ftotal, tcheques, tpagares FROM moschq" +
            " UNION ALL" +
            " SELECT cod_zona, ffactur, ftotal, tcheques, tpagares FROM mospag";
            
            //cargamos la tabla temporal de zonas_dest para obtener la descripcion de zona
            sql += " SELECT cod_zona, xdesc INTO zonas_dest FROM zonas";
            
        //verificamos si esta discriminado por zona, por fecha o por zona y fecha
        if ("PZ".equals(discriminado)){
            sql += " SELECT a.cod_zona, b.xdesc as desc_zona, '' as ffactur, sum(a.ftotal) as ftotal, sum(a.tcheques) as tcheques, sum(a.tpagares) as tpagares" +
            " INTO curfin FROM curfin1 a, zonas_dest b" +
            " WHERE a.cod_zona = b.cod_zona" +
            " GROUP BY a.cod_zona, b.xdesc";
        }else{
            sql += " SELECT a.cod_zona, b.xdesc as desc_zona, a.ffactur, sum(a.ftotal) as ftotal, sum(a.tcheques) as tcheques, sum(a.tpagares) as tpagares" +
            " INTO curfin FROM curfin1 a, zonas_dest b" +
            " WHERE a.cod_zona = b.cod_zona" +
            " GROUP BY a.cod_zona, b.xdesc, a.ffactur";
        }
        
        //creamos y cargamos los cursores y preaparamos los datos
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
    }
    
    //metodo para armar archivo Excel
    public List<Object[]> generarListadoPedidosExcel(Date fechaDocumentoDesde, 
                                                Date fechaDocumentoHasta,
                                                Zonas zona,
                                                String discriminado){
        
        //preparamos los datos
        prepararDatosListadoVentasCredito(fechaDocumentoDesde, fechaDocumentoHasta, zona, discriminado);
        
        //obtenemos el resultado
        String sql = "SELECT * FROM curfin ORDER BY cod_zona";
        
        Query q = em.createNativeQuery(sql);
        q = em.createNativeQuery(sql);
        List<Object[]> resultados = q.getResultList();

       return resultados;
    }
}