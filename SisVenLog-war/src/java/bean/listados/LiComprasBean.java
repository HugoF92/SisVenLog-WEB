package bean.listados;

import dao.CanalesVentaFacade;
import dao.ExcelFacade;
import dao.ProveedoresFacade;
import dao.TiposDocumentosFacade;
import entidad.CanalesVenta;
import entidad.Proveedores;
import entidad.TiposDocumentos;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import util.DateUtil;
import util.LlamarReportes;

/**
 *
 * @author bbrizuela
 */
@ManagedBean
@ViewScoped
public class LiComprasBean {
    
    private Date facturacionDesde;
    private Date facturacionHasta;
    private Date vencimientoDesde;
    private Date vencimientoHasta;
    private TiposDocumentos tipoFactura;
    private CanalesVenta canal;
    private Proveedores proveedor;
    private List<TiposDocumentos> listaTiposFactura;
    private List<CanalesVenta> listaCanales;
    private List<Proveedores> listaProveedores;
    private String seleccion;
    @EJB
    private TiposDocumentosFacade tipoFacturaFacade;
    @EJB
    private CanalesVentaFacade canalFacade;
    @EJB
    private ProveedoresFacade proveedoresFacade;
    @EJB
    private ExcelFacade excelFacade;
    
    @PostConstruct
    public void instanciar() {
        this.seleccion = "1";        
        this.facturacionDesde = new Date();
        this.facturacionHasta = new Date();
        this.vencimientoDesde = new Date();
        this.vencimientoHasta = new Date();
        this.listaTiposFactura = tipoFacturaFacade.listarTipoDocumentoParaConsultaDeCompras();
        this.listaCanales = canalFacade.listarCanalesOrdenadoXDesc();
        this.listaProveedores = proveedoresFacade.getProveedoresActivos();
    }

    public void ejecutarListado(String tipo){
        try{
            LlamarReportes rep = new LlamarReportes();
            String fFacturacionDesde = DateUtil.formaterDateToString(facturacionDesde, "yyyy/MM/dd");
            String fFacturacionHasta = DateUtil.formaterDateToString(facturacionHasta, "yyyy/MM/dd");
            
            String fVencimientoDesde = null;            
            if(vencimientoDesde!=null) fVencimientoDesde = DateUtil.formaterDateToString(vencimientoDesde, "yyyy/MM/dd");
            
            String fVencimientoHasta = null;
            if(vencimientoHasta!=null) fVencimientoHasta = DateUtil.formaterDateToString(vencimientoHasta, "yyyy/MM/dd");
                
            
            String titulo = "";
            String reporte = "";
            String[] columnas = null;
            
            String query; //cursor mostrar
            String queryTmpNum;
            String query2; //cursor curdet
            String query3; //cursor totdet
            String query4; //cursor infototdoc
            String query5; //cursor curfinx
            String query6;
            String queryReport = ""; //cursor curfin
            String orderBy = "";
            String extraWhere = "";
            
            if (proveedor != null){
                extraWhere += "AND c.cod_proveed = " + proveedor.getCodProveed() + " ";
            }       
            if (fVencimientoDesde != null) { 
                extraWhere += "AND c.fvenc >= '" + fVencimientoDesde + "' ";
            }
            if (fVencimientoHasta != null) { 
                extraWhere += "AND c.fvenc <= '" + fVencimientoHasta + "' ";
            }
            if (canal != null){
                extraWhere += "AND c.ccanal_compra = '" + canal.getCodCanal() + "' ";
            }
            if (tipoFactura != null){
                extraWhere += "AND c.ctipo_docum = '" + tipoFactura.getCtipoDocum() + "' ";
            }
            
            if (seleccion.equals("1")) {
                orderBy += "ORDER BY m.ffactur, m.ctipo_docum, m.nrofact";
            } else if (seleccion.equals("2")) {
                orderBy += "ORDER BY m.ccanal_compra, m.fmovim, m.ctipo_docum, m.ndocum";
            } else if (seleccion.equals("3")) {
                orderBy += "ORDER BY m.fmovim, m.ndocum, m.norden";
            } else if (seleccion.equals("4")) {
                orderBy += "ORDER BY m.cod_proveed, m.ndocum";
            } else if (seleccion.equals("5")) {
                orderBy += "ORDER BY r.cod_proveed, r.nrofact";
            } else if (seleccion.equals("6")) {
                orderBy += "ORDER BY r.cod_proveed, r.ffactur, r.nrofact";
            } else if (seleccion.equals("7")) {
                orderBy += "ORDER BY c.ctipo_docum, c.ndocum, c.cod_proveed";
            } else {
                orderBy += "ORDER BY m.ffactur, m.ccanal_compra, m.ctipo_docum, m.nrofact";
            }
            
            if (seleccion.equals("1")) {
                reporte = "RLISCOMPRAS2";
                titulo = "FACTURAS DE COMPRA CON DETALLES";
                
                columnas = new String[21];
                columnas[0] = "fvenc";
                columnas[1] = "ctipo_docum";
                columnas[2] = "cod_proveed";
                columnas[3] = "tgravadas";
                columnas[4] = "ttotal";
                columnas[5] = "timpuestos";
                columnas[6] = "ntimbrado";
                columnas[7] = "xdesc_docum";
                columnas[8] = "xdesc_depo";
                columnas[9] = "fmovim";
                columnas[10] = "xnombre";
                columnas[11] = "xruc";
                columnas[12] = "ndocum";
                columnas[13] = "xdesc_canal2";
                columnas[14] = "ccanal_compra";
                columnas[15] = "xdesc_canal";
                columnas[16] = "tgravadas_10";
                columnas[17] = "tgravadas_5";
                columnas[18] = "timpuestos_10";
                columnas[19] = "timpuestos_5";
                columnas[20] = "texentas";
                
                query = 
                    "SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, t.xdesc AS xdesc_docum, " +
                        "e.xdesc AS xdesc_depo, c.ffactur AS fmovim, p.xnombre, p.xruc, c.nrofact AS ndocum, '' AS xdesc_canal2, '' AS ccanal_compra, " +
                        "'' AS xdesc_canal " +
                    "FROM " +
                        "compras c, depositos e, proveedores p, tipos_documentos t " +
                    "WHERE " +
                        "c.mestado = 'A' AND c.cod_empr = 2 " +
                        "AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND c.cod_depo = e.cod_depo AND c.cod_empr = e.cod_empr " +
                        "AND c.cod_proveed = p.cod_proveed AND c.ctipo_docum = t.ctipo_docum " +
                        extraWhere + " ";
                queryTmpNum = 
                    "SELECT DISTINCT ctipo_docum, ndocum, cod_proveed, fmovim FROM ( " + query + " ) t ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d.pimpues = 5 AND d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, ISNULL(SUM(d .igravadas), 0) AS tgravadas_10, " +
                        "0 AS timpuestos_5, ISNULL(SUM(impuestos), 0) AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d .cod_empr = 2 AND d.pimpues = 10 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, ISNULL(SUM(iexentas), 0) AS texentas, " +
                        "0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM  " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim AND d.iexentas > 0 " +
                    "WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    "SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, SUM(texentas) AS texentas, " +
                        "SUM(tgravadas_5 + timpuestos_5) AS tgravadas_5, SUM(tgravadas_10 + timpuestos_10) AS tgravadas_10, " +
                        "ROUND(SUM(ABS(timpuestos_5)), 0) AS timpuestos_5, ROUND(SUM(ABS(timpuestos_10)), 2) AS timpuestos_10 " +
                    "FROM " +
                        "( " + query2 + ") r " +
                    "GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed ";
                queryReport = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas " +
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed ";
            } else if (seleccion.equals("2")) {
                reporte = "RLISCOMPRAS2";
                titulo = "LISTADO DE FACTURAS DE COMPRA";
                
                columnas = new String[21];
                columnas[0] = "fvenc";
                columnas[1] = "ctipo_docum";
                columnas[2] = "cod_proveed";
                columnas[3] = "tgravadas";
                columnas[4] = "ttotal";
                columnas[5] = "timpuestos";
                columnas[6] = "ntimbrado";
                columnas[7] = "xdesc_docum";
                columnas[8] = "xdesc_depo";
                columnas[9] = "fmovim";
                columnas[10] = "xnombre";
                columnas[11] = "xruc";
                columnas[12] = "ndocum";
                columnas[13] = "xdesc_canal2";
                columnas[14] = "ccanal_compra";
                columnas[15] = "xdesc_canal";
                columnas[16] = "tgravadas_10";
                columnas[17] = "tgravadas_5";
                columnas[18] = "timpuestos_10";
                columnas[19] = "timpuestos_5";
                columnas[20] = "texentas";
                
                query = 
                    "SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, " +
                        "t.xdesc AS xdesc_docum, e.xdesc AS xdesc_depo, c.ffactur AS fmovim, p.xnombre, p.xruc, c.nrofact AS ndocum, " +
                        "a.xdesc AS xdesc_canal2, a.ccanal_compra AS ccanal_compra, ISNULL(a.xdesc,'*') AS xdesc_canal " +
                    "FROM " +
                        "compras c " +
                        "LEFT OUTER JOIN canales_compra a ON c.cod_proveed = a.cod_proveed AND c.ccanal_compra = a.ccanal_compra, " +
                        "depositos e, proveedores p, tipos_documentos t " +
                    "WHERE " +
                        "c.mestado = 'A' AND c.cod_empr = 2 " +
                        "AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND c.cod_depo = e.cod_depo AND c.cod_empr = e.cod_empr " +
                        "AND c.cod_proveed = p.cod_proveed AND c.ctipo_docum = t.ctipo_docum " +
                        extraWhere + " ";
                queryTmpNum = 
                    "SELECT DISTINCT ctipo_docum, ndocum, cod_proveed, fmovim FROM ( " + query + " ) t ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d.pimpues = 5 AND d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, ISNULL(SUM(d .igravadas), 0) AS tgravadas_10, " +
                        "0 AS timpuestos_5, ISNULL(SUM(impuestos), 0) AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d .cod_empr = 2 AND d.pimpues = 10 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, ISNULL(SUM(iexentas), 0) AS texentas, " +
                        "0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim AND d.iexentas > 0 " +
                    "WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    "SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, SUM(texentas) AS texentas, " +
                        "SUM(tgravadas_5 + timpuestos_5) AS tgravadas_5, SUM(tgravadas_10 + timpuestos_10) AS tgravadas_10, " +
                        "ROUND(SUM(ABS(timpuestos_5)), 0) AS timpuestos_5, ROUND(SUM(ABS(timpuestos_10)), 2) AS timpuestos_10 " +
                    "FROM " +
                        "( " + query2 + ") r " +
                    "GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed ";
                queryReport = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas " +
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed " +
                    orderBy;
                    //" ORDER BY m.ccanal_compra, m.fmovim, m.ndocum ";
            } else if (seleccion.equals("3")) {
                reporte = "RCOMPRASDET2";
                titulo = "FACTURA DE COMPRA CON DETALLES";
                
                columnas = new String[36];
                columnas[0] = "fvenc";
                columnas[1] = "ctipo_docum";
                columnas[2] = "cod_proveed";            
                columnas[3] = "tgravadas";
                columnas[4] = "ttotal";
                columnas[5] = "timpuestos";
                columnas[6] = "ntimbrado";
                columnas[7] = "cod_merca";
                columnas[8] = "xdesc";
                columnas[9] = "cant_cajas";
                columnas[10] = "cant_unid";
                columnas[11] = "igravadas";
                columnas[12] = "iexentas";
                columnas[13] = "itotal";
                columnas[14] = "impuestos";
                columnas[15] = "pimpues";
                columnas[16] = "xdesc_docum";
                columnas[17] = "xdesc_depo";
                columnas[18] = "fmovim";
                columnas[19] = "xdesc_merca";
                columnas[20] = "xnombre";
                columnas[21] = "xruc";
                columnas[22] = "ndocum";
                columnas[23] = "nrelacion";
                columnas[24] = "xdesc_canal2";
                columnas[25] = "iprecio_cx";
                columnas[26] = "iprecio_ux";
                columnas[27] = "ccanal2";
                columnas[28] = "norden";
                columnas[29] = "ccanal_compra";
                columnas[30] = "xdesc_canal";
                columnas[31] = "tgravadas_10";
                columnas[32] = "tgravadas_5";
                columnas[33] = "i.timpuestos_10";
                columnas[34] = "timpuestos_5";
                columnas[35] = "texentas";
                
                query = 
                    "SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, " +
                        "d.cod_merca, d.xdesc, d.cant_cajas, d.cant_unid, d.igravadas, d.iexentas, d.itotal, " +
                        "d.impuestos, d.pimpues, t.xdesc AS xdesc_docum, e.xdesc AS xdesc_depo, " +
                        "C.ffactur AS fmovim, d.xdesc AS xdesc_merca, p.xnombre, p.xruc, c.nrofact AS ndocum, " +
                        "m.nrelacion, a.xdesc AS xdesc_canal2, pr.iprecio_caja AS iprecio_cx, " +
                        "pr.iprecio_unidad AS iprecio_ux, a.ccanal_compra AS ccanal2, d.norden, " +
                        "a.ccanal_compra AS ccanal_compra, ISNULL(a.xdesc,'*') AS xdesc_canal " +
                    "FROM compras c " +
                        "INNER JOIN compras_det d ON C.nrofact = d.nrofact AND c.cod_proveed = d.cod_proveed AND c.ctipo_docum = d.ctipo_docum AND c.cod_empr = c.cod_empr " +
                        "INNER JOIN depositos e ON C.cod_depo = e.cod_depo AND c.cod_empr = e.cod_empr " +
                        "LEFT OUTER JOIN canales_compra a ON c.cod_proveed = a.cod_proveed AND c.ccanal_compra = a.ccanal_compra " +
                        "INNER JOIN proveedores p ON C.cod_proveed = p.cod_proveed " +
                        "INNER JOIN tipos_documentos t ON c.ctipo_docum = t.ctipo_docum, " +
                        "precios pr, mercaderias m " +
                    "WHERE " +
                        "c.mestado = 'A' AND d.cod_empr = 2 AND c.cod_empr = 2 " +
                        "AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND d.cod_merca = pr.cod_merca AND pr.cod_depo = 1 " +
                        "AND pr.ctipo_vta = 'X' AND c.ffactur BETWEEN pr.frige_desde AND pr.frige_hasta " +
                        "AND d.cod_merca = m.cod_merca AND c.ffactur = d.ffactur " + 
                        extraWhere + " ";
                queryTmpNum = 
                    "SELECT DISTINCT ctipo_docum, ndocum, cod_proveed, fmovim FROM ( " + query + " ) t ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d.pimpues = 5 AND d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, ISNULL(SUM(d .igravadas), 0) AS tgravadas_10, " +
                        "0 AS timpuestos_5, ISNULL(SUM(impuestos), 0) AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d .cod_empr = 2 AND d.pimpues = 10 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, ISNULL(SUM(iexentas), 0) AS texentas, " +
                        "0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim AND d.iexentas > 0 " +
                    "WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    "SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, cod_merca, SUM(texentas) AS texentas, " +
                        "SUM(tgravadas_5) AS tgravadas_5, SUM(tgravadas_10) AS tgravadas_10, " +
                        "ROUND(SUM(timpuestos_5), 0) AS timpuestos_5, ROUND(SUM(timpuestos_10), 2) AS timpuestos_10 " +
                    "FROM " +
                        "( " + query2 + ") r " +
                    "GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed, cod_merca ";
                queryReport = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas " +
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed AND m.cod_merca = i.cod_merca " +
                        orderBy;
            } else if (seleccion.equals("4")) {
                reporte = "RCOMPRASDET2B";
                titulo = "DIFERENCIAS DE PRECIOS EN FACTURAS DE COMPRA";
                
                columnas = new String[28];
                columnas[0] = "ndocum";
                columnas[1] = "cod_proveed";
                columnas[2] = "xnombre";            
                columnas[3] = "nrelacion";
                columnas[4] = "ntimbrado";
                columnas[5] = "ccanal_compra";
                columnas[6] = "xdesc_canal";
                columnas[7] = "ctipo_docum";
                columnas[8] = "fmovim";
                columnas[9] = "fvenc";
                columnas[10] = "xdesc_depo";
                columnas[11] = "cod_merca";
                columnas[12] = "xdesc_merca";
                columnas[13] = "cant_cajas";
                columnas[14] = "cant_unid";
                columnas[15] = "tgravadas_10";
                columnas[16] = "tgravadas_5";
                columnas[17] = "timpuestos_10";
                columnas[18] = "texentas";
                columnas[19] = "iprecio_ux";
                columnas[20] = "iprecio_caja";
                columnas[21] = "iprecio_unid";
                columnas[22] = "xdesc_canal2";
                columnas[23] = "timpuestos_5";
                columnas[24] = "iexentas";
                columnas[25] = "idif_unidad";
                columnas[26] = "idif_caja";
                columnas[27] = "iprecio_cx";
                
                query = 
                    "SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, d.cod_merca, " +
                        "d.xdesc, d.cant_cajas, d.cant_unid, d.igravadas, d.iexentas, d.itotal, d.impuestos, d.pimpues, " +
                        "t.xdesc AS xdesc_docum, e.xdesc AS xdesc_depo, c.ffactur AS fmovim, d.xdesc AS xdesc_merca, " +
                        "p.xnombre, p.xruc, c.nrofact AS ndocum, m.nrelacion, a.xdesc AS xdesc_canal2, pr.iprecio_caja AS iprecio_cx, " +
                        "pr.iprecio_unidad AS iprecio_ux, a.ccanal_compra AS ccanal2, d.norden, a.ccanal_compra AS ccanal_compra, " +
                        "ISNULL(a.xdesc,'*') AS xdesc_canal " +
                    "FROM " +
                        "compras c " +
                        "INNER JOIN compras_det d ON c.nrofact = d.nrofact AND c.cod_proveed = d.cod_proveed AND c.ctipo_docum = d.ctipo_docum AND c.cod_empr = d.cod_empr " +
                        "INNER JOIN depositos e ON c.cod_depo = e.cod_depo AND c.cod_empr = e.cod_empr " +
                        "LEFT OUTER JOIN canales_compra a ON c.cod_proveed = a.cod_proveed AND c.ccanal_compra = a.ccanal_compra " +
                        "INNER JOIN proveedores p ON c.cod_proveed = p.cod_proveed " +
                        "INNER JOIN tipos_documentos t ON c.ctipo_docum = t.ctipo_docum, " +
                        "precios pr, mercaderias m " +
                    "WHERE " +
                        "c.mestado = 'A' AND d.cod_empr = 2 AND c.cod_empr = 2 " +
                        "AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND d.cod_merca = pr.cod_merca AND pr.cod_depo = 1 " +
                        "AND pr.ctipo_vta = 'X' AND c.ffactur BETWEEN pr.frige_desde AND pr.frige_hasta " +
                        "AND d.cod_merca = m.cod_merca AND c.ffactur = d.ffactur " +
                        extraWhere + " ";
                queryTmpNum = 
                    "SELECT DISTINCT ctipo_docum, ndocum, cod_proveed, fmovim FROM ( " + query + " ) t ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d.pimpues = 5 AND d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, ISNULL(SUM(d .igravadas), 0) AS tgravadas_10, " +
                        "0 AS timpuestos_5, ISNULL(SUM(impuestos), 0) AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d .cod_empr = 2 AND d.pimpues = 10 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, ISNULL(SUM(iexentas), 0) AS texentas, " +
                        "0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim AND d.iexentas > 0 " +
                    "WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    "SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, cod_merca, SUM(texentas) AS texentas, " +
                        "CASE WHEN ROUND(SUM(timpuestos_5), 0) < 0 THEN SUM(tgravadas_5) + ROUND(SUM(timpuestos_5), 0) ELSE SUM(tgravadas_5) END AS tgravadas_5, " +
                        "CASE WHEN ROUND(SUM(timpuestos_10), 2) < 0 THEN SUM(tgravadas_10) + ROUND(SUM(timpuestos_10), 2) ELSE SUM(tgravadas_10) END AS tgravadas_10, " +
                        "ROUND(SUM(timpuestos_5), 0) AS timpuestos_5, ROUND(SUM(timpuestos_10), 2) AS timpuestos_10 " +
                    "FROM " +
                        "( " + query2 + ") r " +
                    "GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed, cod_merca ";
                query4 = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, " +
                        "i.tgravadas_10 / ((m.cant_cajas * m.nrelacion) + m.cant_unid) AS iprecio_unid, " +
                        "(i.tgravadas_10 / ((m.cant_cajas * m.nrelacion) + m.cant_unid)) * m.nrelacion AS iprecio_caja, " +
                        "i.timpuestos_5, i.texentas " + 
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed " +
                        "AND m.cod_merca = i.cod_merca AND i.tgravadas_10 > 0 OR i.texentas > 0 " +
                    "UNION ALL " +
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, " +
                        "i.tgravadas_5 / ((m.cant_cajas * m.nrelacion) + m.cant_unid) AS iprecio_unid, " +
                        "(i.tgravadas_5 / ((m.cant_cajas * m.nrelacion) + m.cant_unid)) * m.nrelacion AS iprecio_caja, " +
                        "i.timpuestos_5, i.texentas " +
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed " +
                        "AND m.cod_merca = i.cod_merca AND i.tgravadas_5 > 0 ";
                query5 = 
                    "SELECT " +
                        "m.ndocum, m.cod_proveed, m.xnombre, m.nrelacion, m.ntimbrado, m.ccanal_compra, m.xdesc_canal, m.ctipo_docum, " +
                        "m.fmovim, m.fvenc, m.xdesc_depo, m.cod_merca, m.xdesc_merca, m.cant_cajas, m.cant_unid, m.tgravadas_10, m.tgravadas_5, " +
                        "m.timpuestos_10, m.texentas, iprecio_ux  AS iprecio_ux, iprecio_caja, iprecio_unid, m.xdesc_canal2, " +
                        "m.timpuestos_5, m.iexentas, iprecio_unid - (iprecio_ux ) AS idif_unidad, " +
                        "iprecio_caja - (iprecio_cx  ) AS idif_caja, iprecio_cx  AS iprecio_cx " +
                    "FROM " +
                        "( " + query4 + " ) m " +
                    "WHERE " +
                        "m.nrelacion = 1 ";
                queryReport = 
                    "SELECT " + 
                        "m.* " +
                    "FROM " +
                        "( " + query5 + " ) m " +
                    "WHERE " +
                        "idif_unidad != 0 OR idif_caja != 0 "
                        + orderBy;
            } else if (seleccion.equals("5")) {
                reporte = "RCOMPRASRES";
                titulo = "RESUMEN DE FACTURAS DE COMPRA";
                
                columnas = new String[13];
                columnas[0] = "ccanal_compra";
                columnas[1] = "xdesc_canal";
                columnas[2] = "nrofact";            
                columnas[3] = "ffactur";
                columnas[4] = "cod_proveed";
                columnas[5] = "xnombre";
                columnas[6] = "xruc";
                columnas[7] = "tgravadas_10";
                columnas[8] = "tgravadas_5";
                columnas[9] = "timpuestos_10";
                columnas[10] = "timpuestos_5";
                columnas[11] = "texentas";
                columnas[12] = "ttotal";
                
                query = 
                    "SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, " +
                        "t.xdesc AS xdesc_docum, e.xdesc AS xdesc_depo, c.ffactur AS fmovim, p.xnombre, p.xruc, c.nrofact AS ndocum, " +
                        "a.xdesc AS xdesc_canal2, a.ccanal_compra AS ccanal_compra, ISNULL(a.xdesc,'*') AS xdesc_canal " +
                    "FROM " +
                        "compras c " +
                        "LEFT OUTER JOIN canales_compra a ON c.cod_proveed = a.cod_proveed AND C.ccanal_compra = a.ccanal_compra, " +
                        "depositos e, proveedores p, tipos_documentos t " +
                    "WHERE " +
                        "c.mestado = 'A' AND c.cod_empr = 2 AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND c.cod_depo = e.cod_depo AND c.cod_empr = e.cod_empr " +
                        "AND c.cod_proveed = p.cod_proveed AND c.ctipo_docum = t.ctipo_docum " +
                        extraWhere + " ";
                queryTmpNum = 
                    "SELECT DISTINCT ctipo_docum, ndocum, cod_proveed, fmovim FROM ( " + query + " ) t ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d.pimpues = 5 AND d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, ISNULL(SUM(d .igravadas), 0) AS tgravadas_10, " +
                        "0 AS timpuestos_5, ISNULL(SUM(impuestos), 0) AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d .cod_empr = 2 AND d.pimpues = 10 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, ISNULL(SUM(iexentas), 0) AS texentas, " +
                        "0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim AND d.iexentas > 0 " +
                    "WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    "SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, SUM(texentas) AS texentas, " +
                        "SUM(tgravadas_5 + timpuestos_5) AS tgravadas_5, SUM(tgravadas_10 + timpuestos_10) AS tgravadas_10, " +
                        "ROUND(SUM(ABS(timpuestos_5)), 0) AS timpuestos_5, ROUND(SUM(ABS(timpuestos_10)), 2) AS timpuestos_10 " +
                    "FROM " +
                        "( " + query2 + ") r " +
                    "GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed ";
                query4 = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas " +
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed ";
                query5 = 
                    "SELECT " +
                        "ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, cod_proveed, xnombre, xruc, " +
                        "tgravadas_10 + timpuestos_10 AS tgravadas_10, tgravadas_5 + timpuestos_5 AS tgravadas_5, " +
                        "ABS(timpuestos_10) AS timpuestos_10, ABS(timpuestos_5) AS timpuestos_5, texentas, " +
                        "(tgravadas_10 + timpuestos_10) + (tgravadas_5 + timpuestos_5) + ABS(timpuestos_10) + ABS(timpuestos_5) + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + ") r " +
                    "WHERE " +
                        "timpuestos_10 < 0 OR timpuestos_5 < 0 OR texentas > 0 " +
                    "UNION ALL " +
                    "SELECT " +
                        "ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, cod_proveed, xnombre, xruc, " +
                        "tgravadas_10 AS tgravadas_10, tgravadas_5 AS tgravadas_5, timpuestos_10 AS timpuestos_10, Timpuestos_5 AS timpuestos_5, " +
                        "texentas, tgravadas_10 + tgravadas_5 + timpuestos_10 + timpuestos_5 + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) r " +
                    "WHERE " +
                        "timpuestos_10 > 0 OR timpuestos_5 > 0 ";
                queryReport = 
                    "SELECT " +
                        "* " +
                    "FROM " +
                        "( " + query5 + " ) r " +
                    orderBy;
            } else if (seleccion.equals("6")) {
                reporte = "RRESUDIA";
                titulo = "RESUMEN DE FACTURAS DE COMPRA";
                
                columnas = new String[11];
                columnas[0] = "nrofact";
                columnas[1] = "ffactur";
                columnas[2] = "cod_proveed";            
                columnas[3] = "xnombre";
                columnas[4] = "xruc";
                columnas[5] = "tgravadas_10";
                columnas[6] = "tgravadas_5";
                columnas[7] = "timpuestos_10";
                columnas[8] = "timpuestos_5";
                columnas[9] = "texentas";
                columnas[10] = "ttotal";
                
                query = 
                    "SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, " +
                        "t.xdesc AS xdesc_docum, e.xdesc AS xdesc_depo, c.ffactur AS fmovim, p.xnombre, p.xruc, " +
                        "c.nrofact AS ndocum, a.xdesc AS xdesc_canal2, a.ccanal_compra AS ccanal_compra, " +
                        "ISNULL(a.xdesc,'*') AS xdesc_canal " +
                    "FROM " +
                        "compras c LEFT OUTER JOIN canales_compra a ON c.cod_proveed = a.cod_proveed AND C.ccanal_compra = a.ccanal_compra, " +
                        "depositos e, proveedores p, tipos_documentos t " +
                    "WHERE " +
                        "c.mestado = 'A' AND c.cod_empr = 2 AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND c.cod_depo = e.cod_depo AND C.cod_empr = e.cod_empr  " +
                        "AND c.cod_proveed = p.cod_proveed AND c.ctipo_docum = t.ctipo_docum " + 
                        extraWhere + " ";
                queryTmpNum = 
                    "SELECT DISTINCT ctipo_docum, ndocum, cod_proveed, fmovim FROM ( " + query + " ) t ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d.pimpues = 5 AND d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, ISNULL(SUM(d .igravadas), 0) AS tgravadas_10, " +
                        "0 AS timpuestos_5, ISNULL(SUM(impuestos), 0) AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d .cod_empr = 2 AND d.pimpues = 10 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, ISNULL(SUM(iexentas), 0) AS texentas, " +
                        "0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim AND d.iexentas > 0 " +
                    "WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    "SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, SUM(texentas) AS texentas, " +
                        "SUM(tgravadas_5 + timpuestos_5) AS tgravadas_5, SUM(tgravadas_10 + timpuestos_10) AS tgravadas_10, " +
                        "ROUND(SUM(ABS(timpuestos_5)), 0) AS timpuestos_5, ROUND(SUM(ABS(timpuestos_10)), 2) AS timpuestos_10 " +
                    "FROM " +
                        "( " + query2 + ") r " +
                    "GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed ";
                query4 = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas " + 
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed ";
                query5 = 
                    "SELECT " +
                        "ndocum AS nrofact, fmovim AS ffactur, cod_proveed, xnombre, xruc, tgravadas_10 + timpuestos_10 AS tgravadas_10, " +
                        "tgravadas_5 + timpuestos_5 AS tgravadas_5, ABS(timpuestos_10) AS timpuestos_10, ABS(timpuestos_5) AS timpuestos_5, " +
                        "texentas, (tgravadas_10 + timpuestos_10) + (tgravadas_5 + timpuestos_5) + ABS(timpuestos_10) + ABS(timpuestos_5) + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) r " +
                    "WHERE " +
                        "timpuestos_10 < 0 OR timpuestos_5 < 0 " +
                    "UNION ALL " +
                    "SELECT " +
                        "ndocum AS nrofact, fmovim AS ffactur, cod_proveed, xnombre, xruc, tgravadas_10 AS tgravadas_10, " +
                        "tgravadas_5 AS tgravadas_5, timpuestos_10 AS timpuestos_10, Timpuestos_5 AS timpuestos_5, texentas, " +
                        "tgravadas_10 + tgravadas_5 + timpuestos_10 + timpuestos_5 + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) r " +
                    "WHERE " +
                        "timpuestos_10 > 0 OR timpuestos_5 > 0 ";
                queryReport = 
                    "SELECT " +
                        "* " +
                    "FROM " +
                        "( " + query5 + " ) r " +
                    orderBy;
            } else if (seleccion.equals("7")) {
                reporte = "rliscompras2n";
                titulo = "LISTADO DE FACTURAS DE COMPRA";
                
                columnas = new String[27];
                columnas[0] = "fvenc";
                columnas[1] = "ctipo_docum";
                columnas[2] = "cod_proveed";            
                columnas[3] = "tgravadas";
                columnas[4] = "ttotal";
                columnas[5] = "timpuestos";
                columnas[6] = "ntimbrado";
                columnas[7] = "xdesc_docum";
                columnas[8] = "xdesc_depo";
                columnas[9] = "fmovim";
                columnas[10] = "xnombre";
                columnas[11] = "xruc";
                columnas[12] = "ndocum";
                columnas[13] = "xdesc_canal2";
                columnas[14] = "ccanal_compra";
                columnas[15] = "xdesc_canal";
                columnas[16] = "tgravadas_10";
                columnas[17] = "tgravadas_5";
                columnas[18] = "timpuestos_10";
                columnas[19] = "timpuestos_5";
                columnas[20] = "texentas";
                columnas[21] = "nro_nota";
                columnas[22] = "fdocum";
                columnas[23] = "cconc";
                columnas[24] = "ctipo_nota";
                columnas[25] = "tnota";
                columnas[26] = "cor";
                
                query = 
                    " SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, t.xdesc AS xdesc_docum, e.xdesc AS xdesc_depo, " +
                        "c.ffactur AS fmovim, p.xnombre, p.xruc, c.nrofact AS ndocum, a.xdesc AS xdesc_canal2, a.ccanal_compra AS ccanal_compra, " +
                        "ISNULL(a.xdesc,'*') AS xdesc_canal " +
                    " FROM " +
                        "compras c LEFT OUTER JOIN canales_compra a ON c.cod_proveed = a.cod_proveed AND c.ccanal_compra = a.ccanal_compra, " +
                        "depositos e, proveedores p, tipos_documentos t " +
                    " WHERE " +
                        "c.mestado = 'A' AND c.cod_empr = 2 AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND c.cod_depo = e.cod_depo AND C.cod_empr = e.cod_empr " +
                        "AND c.cod_proveed = p.cod_proveed AND c.ctipo_docum = t.ctipo_docum " +
                        extraWhere + " ";
                queryTmpNum = 
                    "SELECT DISTINCT ctipo_docum, ndocum, cod_proveed, fmovim FROM ( " + query + " ) t ";
                query2 = 
                    " SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    " FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    " WHERE " +
                        "d.pimpues = 5 AND d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    " GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    " UNION ALL " +
                    " SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, ISNULL(SUM(d .igravadas), 0) AS tgravadas_10, " +
                        "0 AS timpuestos_5, ISNULL(SUM(impuestos), 0) AS timpuestos_10 " +
                    " FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    " WHERE " +
                        "d .cod_empr = 2 AND d.pimpues = 10 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    " GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    " UNION ALL " +
                    " SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, ISNULL(SUM(iexentas), 0) AS texentas, " +
                        "0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " +
                    " FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim AND d.iexentas > 0 " +
                    " WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    " GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    " SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, SUM(texentas) AS texentas, " +
                        "SUM(tgravadas_5 + timpuestos_5) AS tgravadas_5, SUM(tgravadas_10 + timpuestos_10) AS tgravadas_10, " +
                        "ROUND(SUM(ABS(timpuestos_5)), 0) AS timpuestos_5, ROUND(SUM(ABS(timpuestos_10)), 2) AS timpuestos_10 " +
                    " FROM " +
                        " ( " + query2 + ") r " +
                    " GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed ";
                query4 = 
                    " SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas" +
                    " FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    " WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed ";
                query5 = 
                    " SELECT " +
                        "n.nrofact, n.com_ctipo_docum, n.nro_nota, n.cconc, n.fdocum, n.cod_proveed, n.ctipo_docum, n.ttotal " +
                    " FROM " +
                        "notas_compras n, tmp_numeros t " +
                    " WHERE " +
                        "n.cod_empr = 2 AND n.nrofact = t.ndocum AND n.mestado ='A' AND n.cod_proveed = t.cod_proveed " +
                        "AND n.ffactur = t.ffactur AND n.com_ctipo_docum = t.ctipo_docum AND n.ctipo_docum IN ('NCC','NDP') ";
                queryReport = 
                    " IF EXISTS " +
                        "(SELECT COUNT(*) " +
                            "FROM notas_compras n, tmp_numeros t " +
                            "WHERE n.cod_empr = 2 AND n.nrofact = t.ndocum AND n.mestado ='A' AND n.cod_proveed = t.cod_proveed AND n.ffactur = t.ffactur " +
                            "AND n.com_ctipo_docum = t.ctipo_docum AND n.ctipo_docum IN ('NCC','NDP')) " +
                        " SELECT DISTINCT " +
                            "c.*, " +
                            "n.nro_nota, " +
                            "n.fdocum, " +
                            "n.cconc, " +
                            "n.ctipo_docum AS ctipo_nota, " +
                            "n.ttotal AS tnota, " +
                            "0 AS cor " +
                        " FROM " +
                            "( " + query4 + " ) c " +
                            "LEFT OUTER JOIN ( " + query5 + " ) n ON c.ndocum = n.nrofact AND c.cod_proveed = n.cod_proveed AND c.ctipo_docum = n.com_ctipo_docum " +
                    " ELSE " +
                        " SELECT " +
                            "c.*, " +
                            "0 AS nro_nota, " +
                            "'' AS fdocum, " +
                            "'' AS cconc, " +
                            "'' AS ctipo_nota, " +
                            "0 AS tnota, " +
                            "1 AS cor " +
                        " FROM " +
                            "( " + query4 + " ) c " +
                orderBy;
            } else if (seleccion.equals("8")) {// no reporte
                reporte = "librocom";
                columnas = new String[12];
                columnas[0] = "ctipo_docum";
                columnas[1] = "nrofact";
                columnas[2] = "cconc";            
                columnas[3] = "ffactur";
                columnas[4] = "xnombre";
                columnas[5] = "xruc";
                columnas[6] = "tgrav_10";
                columnas[7] = "tgrav_5";
                columnas[8] = "timp_10";
                columnas[9] = "timp_5";
                columnas[10] = "texentas";
                columnas[11] = "ttotal";
                
                query = 
                    "SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, t.xdesc AS xdesc_docum, " +
                        "e.xdesc AS xdesc_depo, c.ffactur AS fmovim, p.xnombre, p.xruc, c.nrofact AS ndocum, '' AS xdesc_canal2, '' AS ccanal_compra, " +
                        "'' AS xdesc_canal " +
                    "FROM " +
                        "compras c, depositos e, proveedores p, tipos_documentos t " +
                    "WHERE " +
                        "c.mestado = 'A' AND c.cod_empr = 2 " +
                        "AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND c.cod_depo = e.cod_depo AND c.cod_empr = e.cod_empr " +
                        "AND c.cod_proveed = p.cod_proveed AND c.ctipo_docum = t.ctipo_docum " +
                        extraWhere + " ";
                queryTmpNum = 
                    "SELECT DISTINCT ctipo_docum, ndocum, cod_proveed, fmovim FROM ( " + query + " ) t ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d.pimpues = 5 AND d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, ISNULL(SUM(d .igravadas), 0) AS tgravadas_10, " +
                        "0 AS timpuestos_5, ISNULL(SUM(impuestos), 0) AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim " +
                    "WHERE " +
                        "d .cod_empr = 2 AND d.pimpues = 10 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca " +
                    "UNION ALL " +
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, ISNULL(SUM(iexentas), 0) AS texentas, " +
                        "0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN ( " + queryTmpNum + " ) t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.fmovim AND d.iexentas > 0 " +
                    "WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    "SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, SUM(texentas) AS texentas, " +
                        "SUM(tgravadas_5 + timpuestos_5) AS tgravadas_5, SUM(tgravadas_10 + timpuestos_10) AS tgravadas_10, " +
                        "ROUND(SUM(ABS(timpuestos_5)), 0) AS timpuestos_5, ROUND(SUM(ABS(timpuestos_10)), 2) AS timpuestos_10 " +
                    "FROM " +
                        "( " + query2 + ") r " +
                    "GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed ";
                query4 = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas " +
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed ";
                query5 = 
                    "SELECT " +
                        "n.ctipo_docum, n.nro_nota AS nrofact, n.fdocum AS ffactur, n.cconc, p.xnombre, p.xruc, " +
                        "SUM(d.igravadas) * -1 AS tgrav_10, 0 AS tgrav_5, SUM(ABS(d.impuestos)) * -1 " +
                        "AS timp_10, 0 AS timp_5, 0 AS texentas, n.ttotal * -1 AS ttotal " +
                    "FROM " +
                        "notas_compras n INNER JOIN notas_compras_det d ON N.nro_nota = d.nro_nota AND n.cod_proveed = d.cod_proveed, " +
                        "proveedores p " +
                    "WHERE " +
                        "n.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND n.mestado = 'A' AND n.cod_proveed = p.cod_proveed AND n.fdocum = d.fdocum AND d.impuestos > 0 AND d.pimpues = 10 " +
                    "GROUP BY " +
                        "n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, p.xnombre, p.xruc " +
                    "UNION ALL " +
                    "SELECT " +
                        "n.ctipo_docum, n.nro_nota AS nrofact, n.fdocum AS ffactur, n.cconc, p.xnombre, p.xruc, " +
                        "SUM(d.igravadas + d.impuestos) * -1 AS tgrav_10, 0 AS tgrav_5, SUM(ABS(d.impuestos)) * -1 AS timp_10, " +
                        "0 AS timp_5, 0 AS texentas, n.ttotal * -1 AS ttotal " +
                    "FROM " +
                        "notas_compras n INNER JOIN notas_compras_det d ON n.nro_nota = d.nro_nota AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, " +
                        "proveedores p " +
                    "WHERE " +
                        "n.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND n.mestado = 'A' AND n.cod_proveed = p.cod_proveed AND d.impuestos < 0 AND d.pimpues = 10 " +
                    "GROUP BY " +
                        "n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, p.xnombre, p.xruc " +
                    "UNION ALL " +
                    "SELECT " + 
                        "n.ctipo_docum, n.nro_nota AS nrofact, n.fdocum AS ffactur, n.cconc, p.xnombre, p.xruc, " +
                        "0 AS tgrav_10, SUM(d.igravadas) * -1 AS tgrav_5, 0 AS timp_10, SUM(ABS(d.impuestos)) * -1 AS timp_5, " +
                        "0 AS texentas, n.ttotal * -1 AS ttotal " +
                    "FROM " +
                        "notas_compras n INNER JOIN notas_compras_det d ON n.nro_nota = d.nro_nota AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, " +
                        "proveedores p " +
                    "WHERE " +
                        "n.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND n.mestado = 'A' AND n.cod_proveed = p.cod_proveed AND d.impuestos > 0 AND d.pimpues = 5 " +
                    "GROUP BY " +
                        "n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, p.xnombre, p.xruc " +
                    "UNION ALL " +
                    "SELECT " +
                        "n.ctipo_docum, n.nro_nota AS nrofact, n.fdocum AS ffactur, n.cconc, p.xnombre, p.xruc, " +
                        "0 AS tgrav_10, SUM(d.igravadas + d.impuestos) * -1 AS tgrav_5, 0 AS timp_10, SUM(ABS(d.impuestos)) * -1 AS timp_5, " +
                        "0 AS texentas, n.ttotal * -1 AS ttotal " +
                    "FROM " +
                        "notas_compras n INNER JOIN notas_compras_det d ON n.nro_nota = d.nro_nota AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, " +
                        "proveedores p " +
                    "WHERE " +
                        "n.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND n.mestado = 'A' AND n.cod_proveed = p.cod_proveed AND d.impuestos < 0 AND d.pimpues = 5 " +
                    "GROUP BY " +
                        "n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, p.xnombre, p.xruc " +
                    "UNION ALL " +
                    "SELECT " +
                        "n.ctipo_docum, n.nro_nota AS nrofact, n.fdocum AS ffactur, n.cconc, p.xnombre, p.xruc, " +
                        "0 AS tgrav_10, 0 AS tgrav_5, 0 AS timp_10, 0 AS timp_5, sum(d.iexentas) * -1 AS texentas, n.ttotal * -1 AS ttotal " +
                    "FROM " +
                        "notas_compras n INNER JOIN notas_compras_det d ON n.nro_nota = d.nro_nota AND n.cod_proveed = d.cod_proveed AND n.fdocum = d.fdocum, " +
                        "proveedores p " +
                    "WHERE " +
                        "n.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND n.mestado = 'A' AND n.cod_proveed = p.cod_proveed AND d.impuestos = 0 AND d.pimpues = 0 " +
                    "GROUP BY " + 
                        "n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, p.xnombre, p.xruc ";
                query6 = 
                    "SELECT " +
                        "ctipo_docum, ndocum AS nrofact, fmovim AS ffactur, xnombre, xruc, tgravadas_10 AS tgrav_10, tgravadas_5 AS tgrav_5, timpuestos_10 AS timp_10, " +
                        "timpuestos_5 AS timp_5, texentas, tgravadas_10 + tgravadas_5 + timpuestos_10 + timpuestos_5 + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) r ";
                queryReport = 
                    "SELECT " +
                        "ctipo_docum, nrofact, cconc, CONVERT(VARCHAR(10), ffactur, 103) AS ffactur, xnombre, xruc, tgrav_10, tgrav_5, timp_10, timp_5, texentas, ttotal " +
                    "FROM " +
                        "( " + query5 + " ) r " +
                    "UNION ALL " +
                    "SELECT " +
                        "ctipo_docum, nrofact, '' AS cconc, CONVERT(VARCHAR(10), ffactur, 103) AS ffactur, xnombre, xruc, tgrav_10, tgrav_5, timp_10, timp_5, texentas, ttotal " +
                    "FROM " + 
                        "( " + query6 + " ) r " +
                    "ORDER BY 4" ;
            }
            
            System.out.println("QUERY: " + queryReport);
            
            if (tipo.equals("VIST") && !seleccion.equals("8") ) {
                String usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                Map param = new HashMap();
                param.put("sql", queryReport);
                param.put("fdesde", facturacionDesde);
                param.put("fhasta", facturacionHasta);
                param.put("fvtodesde", vencimientoDesde);
                param.put("fvtohasta", vencimientoHasta);
                param.put("titulo", titulo);
                param.put("usuImprime", usuImprime);
                param.put("nombreRepo", reporte); 

                if (this.tipoFactura != null) param.put("tipo", tipoFacturaFacade.getTipoDocumentoFromList(this.tipoFactura, this.listaTiposFactura).getXdesc());
                else param.put("vendedor", "TODOS");
                
                if (this.canal != null) param.put("canal", canalFacade.getCanalVentaFromList(this.canal, this.listaCanales).getXdesc()); 
                else param.put("canal", "TODOS");
                
                if (this.proveedor != null) param.put("proveedor", proveedoresFacade.getProveedorFromList(this.proveedor, this.listaProveedores).getXnombre()); 
                else param.put("proveedor", "TODOS");
            
                rep.reporteLiContClientes(param, tipo, reporte);
            } else {
                List<Object[]> lista = new ArrayList<Object[]>();
                lista = excelFacade.listarParaExcel(queryReport);
                rep.exportarExcel(columnas, lista, reporte);
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al ejecutar listado"));
        }        
    }
    
    public Date getFacturacionDesde() {
        return facturacionDesde;
    }

    public void setFacturacionDesde(Date facturacionDesde) {
        this.facturacionDesde = facturacionDesde;
    }

    public Date getFacturacionHasta() {
        return facturacionHasta;
    }

    public void setFacturacionHasta(Date facturacionHasta) {
        this.facturacionHasta = facturacionHasta;
    }

    public Date getVencimientoDesde() {
        return vencimientoDesde;
    }

    public void setVencimientoDesde(Date vencimientoDesde) {
        this.vencimientoDesde = vencimientoDesde;
    }

    public Date getVencimientoHasta() {
        return vencimientoHasta;
    }

    public void setVencimientoHasta(Date vencimientoHasta) {
        this.vencimientoHasta = vencimientoHasta;
    }
    
    public TiposDocumentos getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(TiposDocumentos tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public CanalesVenta getCanal() {
        return canal;
    }

    public void setCanal(CanalesVenta canal) {
        this.canal = canal;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public List<TiposDocumentos> getListaTiposFactura() {
        return listaTiposFactura;
    }

    public void setListaTiposFactura(List<TiposDocumentos> listaTiposFactura) {
        this.listaTiposFactura = listaTiposFactura;
    }

    public List<CanalesVenta> getListaCanales() {
        return listaCanales;
    }

    public void setListaCanales(List<CanalesVenta> listaCanales) {
        this.listaCanales = listaCanales;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }
}
