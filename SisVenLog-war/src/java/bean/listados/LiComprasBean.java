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
        this.listaTiposFactura = tipoFacturaFacade.listarTipoDocumentoFactura();
        this.listaCanales = canalFacade.listarCanalesOrdenadoXDesc();
        this.listaProveedores = proveedoresFacade.getProveedoresActivos();
    }

    public void ejecutarListado(String tipo){
        try{
            LlamarReportes rep = new LlamarReportes();
            String fFacturacionDesde = DateUtil.formaterDateToString(facturacionDesde, "yyyy/MM/dd");
            String fFacturacionHasta = DateUtil.formaterDateToString(facturacionHasta, "yyyy/MM/dd");
            
            String fVencimientoDesde = DateUtil.formaterDateToString(vencimientoDesde, "yyyy/MM/dd");
            String fVencimientoHasta = DateUtil.formaterDateToString(vencimientoHasta, "yyyy/MM/dd");
            
            String titulo = "";
            String reporte = "";
            String[] columnas;
            
            String query = ""; //cursor mostrar
            String query2; //cursor curdet
            String query3; //cursor totdet
            String query4; //cursor infototdoc
            String query5; //cursor curfinx
            String query6;
            String queryReport = ""; //cursor curfin
            String orderBy = "";
            String extraWhere = "";
            
            if (proveedor != null){
                extraWhere += "AND m.cod_proveed = " + proveedor.getCodProveed() + " ";
            }       
            if (fVencimientoDesde != null) { 
                extraWhere += "AND c.fvenc >= '" + fVencimientoDesde + "' ";
            }
            if (fVencimientoHasta != null) { 
                extraWhere += "AND c.fvenc <= '" + fVencimientoHasta + "' ";
            }
            if (canal != null){
                extraWhere += "AND mc.cod_canal = " + canal.getCodCanal() + " ";
            }
            if (tipoFactura != null){
                extraWhere += "AND c.ctipo_docum = " + tipoFactura.getCtipoDocum() + " ";
            }
            
            if (seleccion.equals("1")) {
                orderBy += "ORDER BY ffactur, ctipo_docum, nrofact";
            } else if (seleccion.equals("2")) {
                orderBy += "ORDER BY ccanal_compra, ffactur, ctipo_docum, nrofact";
            } else if (seleccion.equals("3")) {
                orderBy += "ORDER BY ffactur, nrofact, norden";
            } else if (seleccion.equals("4")) {
                orderBy += "ORDER BY cod_proveed, ndocum";
            } else if (seleccion.equals("5")) {
                orderBy += "ORDER BY cod_proveed, nrofact";
            } else if (seleccion.equals("6")) {
                orderBy += "ORDER BY cod_proveed, ffactur, nrofact";
            } else if (seleccion.equals("7")) {
                orderBy += "ORDER BY ctipo_docum, ndocum, cod_proveed";
            } else {
                orderBy += "ORDER BY ffactur, ccanal_compra, ctipo_docum, nrofact";
            }
            
            if (seleccion.equals("1")) {
                reporte = "RLISCOMPRAS3";
                titulo = "FACTURAS DE COMPRA CON DETALLES";
                
                columnas = new String[13];
                columnas[0] = "ctipo_docum";
                columnas[1] = "ccanal_compra";
                columnas[2] = "xdesc_canal";
                columnas[3] = "nrofact";
                columnas[4] = "ffactur";
                columnas[5] = "xnombre";
                columnas[6] = "xruc";
                columnas[7] = "tgrav_10";
                columnas[8] = "tgrav_5";
                columnas[9] = "timp_10";
                columnas[10] = "timp_5";
                columnas[11] = "texentas";
                columnas[12] = "ttotal";
                
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
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur AND d.iexentas > 0 " +
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
                queryReport = 
                    "SELECT " +
                        "ctipo_docum, ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, xnombre, xruc, " +
                        "tgravadas_10 + timpuestos_10 AS tgrav_10, tgravadas_5 + timpuestos_5 AS tgrav_5, " +
                        "ABS(timpuestos_10) AS timp_10, ABS(timpuestos_5) AS timp_5, texentas, " +
                        "(tgravadas_10 + timpuestos_10) + (tgravadas_5 + timpuestos_5) + ABS(timpuestos_10) + ABS(timpuestos_5) + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) c " +
                    "WHERE " +
                        "timpuestos_10 < 0 OR timpuestos_5 < 0 OR TEXENTAS > 0 " +
                    "UNION ALL " +
                    "SELECT " +
                        "ctipo_docum, ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, xnombre, xruc, " +
                        "tgravadas_10 AS tgrav_10, tgravadas_5 AS tgrav_5, timpuestos_10 AS timp_10, Timpuestos_5 AS timp_5, " +
                        "texentas, tgravadas_10 + tgravadas_5 + timpuestos_10 + timpuestos_5 + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) c " +
                    "WHERE " +
                        "timpuestos_10 > 0 OR timpuestos_5 > 0 " +
                        orderBy;
            } else if (seleccion.equals("2")) {
                reporte = "RLISCOMPRAS3";
                columnas = new String[13];
                columnas[0] = "ctipo_docum";
                columnas[1] = "ccanal_compra";
                columnas[2] = "xdesc_canal";            
                columnas[3] = "nrofact";
                columnas[4] = "ffactur";
                columnas[5] = "xnombre";
                columnas[6] = "xruc";
                columnas[7] = "tgrav_10";
                columnas[8] = "tgrav_5";
                columnas[9] = "timp_10";
                columnas[10] = "timp_5";
                columnas[11] = "texentas";
                columnas[12] = "ttotal";
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
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur AND d.iexentas > 0 " +
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
                queryReport = 
                    "SELECT " +
                        "ctipo_docum, ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, xnombre, xruc, " +
                        "tgravadas_10 + timpuestos_10 AS tgrav_10, tgravadas_5 + timpuestos_5 AS tgrav_5, " +
                        "ABS(timpuestos_10) AS timp_10, ABS(timpuestos_5) AS timp_5, texentas, " +
                        "(tgravadas_10 + timpuestos_10) + (tgravadas_5 + timpuestos_5) + ABS(timpuestos_10) + ABS(timpuestos_5) + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) c " +
                    "WHERE " +
                        "timpuestos_10 < 0 OR timpuestos_5 < 0 OR TEXENTAS > 0 " +
                    "UNION ALL " +
                    "SELECT " +
                        "ctipo_docum, ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, xnombre, xruc, " +
                        "tgravadas_10 AS tgrav_10, tgravadas_5 AS tgrav_5, timpuestos_10 AS timp_10, Timpuestos_5 AS timp_5, " +
                        "texentas, tgravadas_10 + tgravadas_5 + timpuestos_10 + timpuestos_5 + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) c " +
                    "WHERE " +
                        "timpuestos_10 > 0 OR timpuestos_5 > 0 " +
                        orderBy;
            } else if (seleccion.equals("3")) {
                reporte = "RCOMPRASDET2";
                columnas = new String[13];
                columnas[0] = "ctipo_docum";
                columnas[1] = "ccanal_compra";
                columnas[2] = "xdesc_canal";            
                columnas[3] = "nrofact";
                columnas[4] = "ffactur";
                columnas[5] = "xnombre";
                columnas[6] = "xruc";
                columnas[7] = "tgrav_10";
                columnas[8] = "tgrav_5";
                columnas[9] = "timp_10";
                columnas[10] = "timp_5";
                columnas[11] = "texentas";
                columnas[12] = "ttotal";               
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
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur AND d.iexentas > 0 " +
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
                query4 = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas " +
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed AND m.cod_merca = i.cod_merca ";
                queryReport = 
                    "SELECT " +
                        "ctipo_docum, ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, xnombre, xruc, " +
                        "tgravadas_10 + timpuestos_10 AS tgrav_10, tgravadas_5 + timpuestos_5 AS tgrav_5, " +
                        "ABS(timpuestos_10) AS timp_10, ABS(timpuestos_5) AS timp_5, texentas, " +
                        "(tgravadas_10 + timpuestos_10) + (tgravadas_5 + timpuestos_5) + ABS(timpuestos_10) + ABS(timpuestos_5) + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) c " +
                    "WHERE " +
                        "timpuestos_10 < 0 OR timpuestos_5 < 0 OR TEXENTAS > 0 " +
                    "UNION ALL " +
                    "SELECT " +
                        "ctipo_docum, ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, xnombre, xruc, " +
                        "tgravadas_10 AS tgrav_10, tgravadas_5 AS tgrav_5, timpuestos_10 AS timp_10, Timpuestos_5 AS timp_5, " +
                        "texentas, tgravadas_10 + tgravadas_5 + timpuestos_10 + timpuestos_5 + texentas AS ttotal " +
                    "FROM " +
                        "( " + query4 + " ) c " +
                    "WHERE " +
                        "timpuestos_10 > 0 OR timpuestos_5 > 0 " +
                        orderBy;
            } else if (seleccion.equals("4")) {
                reporte = "RCOMPRASDET2B";
                columnas = new String[21];
                columnas[0] = "";
                columnas[1] = "";
                columnas[2] = "";            
                columnas[3] = "";
                columnas[4] = "";
                columnas[5] = "";
                columnas[6] = "";
                columnas[7] = "";
                columnas[8] = "";
                columnas[9] = "";
                columnas[10] = "";
                columnas[11] = "";
                columnas[12] = "";
                columnas[13] = "";
                columnas[14] = "";
                columnas[15] = "";
                columnas[16] = "";
                columnas[17] = "";
                columnas[18] = "";
                columnas[19] = "";
                columnas[20] = "";
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
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur AND d.iexentas > 0 " +
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
                        "i.timpuestos_5, i.texentas, i.idescuentos, i.idesc_unid " +
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
                        "i.timpuestos_5, i.texentas , i.idescuentos, i.idesc_unid " +
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
                        "m.timpuestos_10, m.texentas, iprecio_ux - idesc_unid AS iprecio_ux, iprecio_caja, iprecio_unid, m.xdesc_canal2, " +
                        "m.timpuestos_5, m.iexentas, iprecio_unid - (iprecio_ux - idesc_unid) AS idif_unidad, " +
                        "iprecio_caja - (iprecio_cx  - idescuentos) AS idif_caja, iprecio_cx - idescuentos AS iprecio_cx " +
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
                columnas = new String[14];
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
                columnas[13] = "new_f";
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
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur AND d.iexentas > 0 " +
                    "WHERE " +
                        "d.cod_empr = 2 AND t.ctipo_docum IN ('FCC','CVC','COC') " +
                    "GROUP BY " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca ";
                query3 = 
                    "SELECT " +
                        "ctipo_docum, ndocum, cod_proveed, ccanal_compra, nsemana, " +
                        "SUM(texentas) AS texentas, SUM(tgravadas_5) AS tgravadas_5, SUM(tgravadas_10) AS tgravadas_10, " +
                        "ROUND(SUM(timpuestos_5),0) AS timpuestos_5, ROUND(SUM(timpuestos_10),2) AS timpuestos_10 " +
                    "FROM " +
                        "( " + query2 + ") r " +
                    "GROUP BY " +
                        "ctipo_docum, ndocum, cod_proveed, ccanal_compra, nsemana ";
                query4 = 
                    "SELECT " +
                        "m.*, " +
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas, i.mestado " +
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
                        "(tgravadas_10 + timpuestos_10) + (tgravadas_5 + timpuestos_5) + ABS(timpuestos_10) + ABS(timpuestos_5) + texentas AS ttotal, " +
                        "mestado AS new_f " +
                    "FROM " +
                        "( " + query4 + ") r " +
                    "WHERE " +
                        "timpuestos_10 < 0 OR timpuestos_5 < 0 OR texentas > 0 " +
                    "UNION ALL " +
                    "SELECT " +
                        "ccanal_compra, xdesc_canal, ndocum AS nrofact, fmovim AS ffactur, cod_proveed, xnombre, xruc, " +
                        "tgravadas_10 AS tgravadas_10, tgravadas_5 AS tgravadas_5, timpuestos_10 AS timpuestos_10, Timpuestos_5 AS timpuestos_5, " +
                        "texentas, tgravadas_10 + tgravadas_5 + timpuestos_10 + timpuestos_5 + texentas AS ttotal, mestado AS new_f " +
                    "FROM " +
                        "( " + query4 + " ) r " +
                    "WHERE " +
                        "timpuestos_10 > 0 OR timpuestos_5 > 0 ";
                queryReport = 
                    "SELECT " +
                        " * " +
                    "FROM " +
                        "( " + query5 + " ) r " +
                    orderBy;
            } else if (seleccion.equals("6")) {
                reporte = "RRESUDIA";
                columnas = new String[12];
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
                columnas[11] = "new_f";
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
                        "AND c.cod_proveed = p.cod_proveed AND c.ctipo_docum = t.ctipo_docum" + 
                        extraWhere + " ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur AND d.iexentas > 0 " +
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
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas, i.mestado " + 
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed ";
                query5 = 
                    "SELECT " +
                        "ndocum AS nrofact, fmovim AS ffactur, cod_proveed, xnombre, xruc, tgravadas_10 + timpuestos_10 AS tgravadas_10, " +
                        "tgravadas_5 + timpuestos_5 AS tgravadas_5, ABS(timpuestos_10) AS timpuestos_10, ABS(timpuestos_5) AS timpuestos_5, " +
                        "texentas, (tgravadas_10 + timpuestos_10) + (tgravadas_5 + timpuestos_5) + ABS(timpuestos_10) + ABS(timpuestos_5) + texentas AS ttotal, " +
                        "mestado AS new_f " +
                    "FROM " +
                        "( " + query4 + " ) r " +
                    "WHERE " +
                        "timpuestos_10 < 0 OR timpuestos_5 < 0 " +
                    "UNION ALL " +
                    "SELECT " +
                        "ndocum AS nrofact, fmovim AS ffactur, cod_proveed, xnombre, xruc, tgravadas_10 AS tgravadas_10, " +
                        "tgravadas_5 AS tgravadas_5, timpuestos_10 AS timpuestos_10, Timpuestos_5 AS timpuestos_5, texentas, " +
                        "tgravadas_10 + tgravadas_5 + timpuestos_10 + timpuestos_5 + texentas AS ttotal, mestado as new_f " +
                    "FROM " +
                        "( " + query4 + " ) r " +
                    "WHERE " +
                        "timpuestos_10 > 0 OR timpuestos_5 > 0 ";
                queryReport = 
                    "SELECT " +
                        " * " +
                    "FROM " +
                        "( " + query5 + " ) r" +
                    orderBy;
            } else if (seleccion.equals("7")) {
                reporte = "RlisCOMPRAS2n";
                columnas = new String[21];
                columnas[0] = "";
                columnas[1] = "";
                columnas[2] = "";            
                columnas[3] = "";
                columnas[4] = "";
                columnas[5] = "";
                columnas[6] = "";
                columnas[7] = "";
                columnas[8] = "";
                columnas[9] = "";
                columnas[10] = "";
                columnas[11] = "";
                columnas[12] = "";
                columnas[13] = "";
                columnas[14] = "";
                columnas[15] = "";
                columnas[16] = "";
                columnas[17] = "";
                columnas[18] = "";
                columnas[19] = "";
                columnas[20] = "";
                query = 
                    "SELECT " +
                        "c.fvenc, c.ctipo_docum, c.cod_proveed, c.tgravadas, c.ttotal, c.timpuestos, c.ntimbrado, t.xdesc AS xdesc_docum, e.xdesc AS xdesc_depo, " +
                        "c.ffactur AS fmovim, p.xnombre, p.xruc, c.nrofact AS ndocum, a.xdesc AS xdesc_canal2, a.ccanal_compra AS ccanal_compra, " +
                        "ISNULL(a.xdesc,'*') AS xdesc_canal " +
                    "FROM " +
                        "compras c LEFT OUTER JOIN canales_compra a ON c.cod_proveed = a.cod_proveed AND c.ccanal_compra = a.ccanal_compra, " +
                        "depositos e, proveedores p, tipos_documentos t " +
                    "WHERE " +
                        "c.mestado = 'A' AND c.cod_empr = 2 AND c.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND c.cod_depo = e.cod_depo AND C.cod_empr = e.cod_empr " +
                        "AND c.cod_proveed = p.cod_proveed AND c.ctipo_docum = t.ctipo_docum " +
                        extraWhere + " ";
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur AND d.iexentas > 0 " +
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
                        "i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, i.texentas, i.mestado " +
                    "FROM " +
                        "( " + query + " ) m, " +
                        "( " + query3 + " ) i " +
                    "WHERE " +
                        "m.ndocum = i.ndocum AND m.ctipo_docum = i.ctipo_docum AND m.cod_proveed = i.cod_proveed ";
                query5 = 
                    "SELECT " +
                        "n.nrofact, n.com_ctipo_docum, n.nro_nota, n.cconc, n.fdocum, n.cod_proveed, n.ctipo_docum, n.ttotal " +
                    "FROM " +
                        "notas_compras n, tmp_numeros t " +
                    "WHERE " +
                        "n.cod_empr = 2 AND n.nrofact = t.ndocum AND n.mestado ='A' AND n.cod_proveed = t.cod_proveed " +
                        "AND n.ffactur = t.ffactur AND n.com_ctipo_docum = t.ctipo_docum AND n.ctipo_docum IN ('NCC','NDP') ";
                
            } else if (seleccion.equals("8")) {// no reporte
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
                query2 = 
                    "SELECT " +
                        "t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, ISNULL(SUM(d.igravadas), 0) AS tgravadas_5, " +
                        "0 AS tgravadas_10, ISNULL(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 " +
                    "FROM " +
                        "compras_det d " +
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur " +
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
                        "INNER JOIN tmp_numeros t ON d.nrofact = t.ndocum AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.ffactur = t.ffactur AND d.iexentas > 0 " +
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
                        "SUM(d.igravadas) * -1 AS tgrav_10, 0 AS tgrav_5, SUM(ABS(d.impuestos)) * -1" +
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
                        "n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, p.xnombre, p.xruc  " +
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
                        "n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal, p.xnombre, p.xruc  " +
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
                        "ctipo_docum, nrofact, cconc, DTOC(ffactur) AS ffactur, xnombre, xruc, tgrav_10, tgrav_5, timp_10, timp_5, texentas, ttotal " +
                    "FROM " +
                        "( " + query5 + " ) r " +
                    "UNION ALL " +
                    "SELECT " +
                        "ctipo_docum, nrofact, '' AS cconc, DTOC(ffactur) AS ffactur, xnombre, xruc, tgrav_10, tgrav_5, timp_10, timp_5, texentas, ttotal " +
                    "FROM " + 
                        "( " + query6 + " ) r " +
                    "ORDER BY 4" ;
            }
            
            System.out.println(" - - - - - - - - - - - -");
            System.out.println("QUERY: " + queryReport);
            System.out.println(" - - - - - - - - - - - -");
            
            if (tipo.equals("VIST")) {
                String usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                Map param = new HashMap();
                param.put("sql", queryReport);
                param.put("fdesde", fFacturacionDesde);
                param.put("fhasta", fFacturacionHasta);
                param.put("titulo", titulo);
                param.put("usuImprime", usuImprime);
                param.put("nombreRepo", reporte); 

                if (this.tipoFactura != null) param.put("tipo", tipoFacturaFacade.getTipoDocumentoFromList(this.tipoFactura, this.listaTiposFactura).getXdesc());
                else param.put("vendedor", "TODOS");
                
                if (this.canal != null) param.put("canal", canalFacade.getCanalVentaFromList(this.canal, this.listaCanales).getXdesc()); 
                else param.put("canal", "TODOS");
                
                if (this.proveedor != null) param.put("proveedor", proveedoresFacade.getProveedorFromList(this.proveedor, this.listaProveedores).getXnombre()); 
                else param.put("proveedor", "TODOS");
            
                //rep.reporteLiContClientes(param, tipo, reporte);
            } else {
                List<Object[]> lista = new ArrayList<Object[]>();
                lista = excelFacade.listarParaExcel(query);
                //rep.exportarExcel(columnas, lista, reporte);
            }
            
        } catch (ParseException e) {
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
