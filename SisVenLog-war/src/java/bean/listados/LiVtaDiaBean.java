package bean.listados;

import dao.CanalesVentaFacade;
import dao.ExcelFacade;
import dao.ProveedoresFacade;
import dao.RutasFacade;
import dao.TiposVentasFacade;
import dao.ZonasFacade;
import entidad.CanalesVenta;
import entidad.Proveedores;
import entidad.Rutas;
import entidad.TiposVentas;
import entidad.Zonas;
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
 * @author mbrizuela
 */
@ManagedBean
@ViewScoped
public class LiVtaDiaBean {
    
    private Date facturacionDesde;
    private Date facturacionHasta;
    private TiposVentas tipoVenta;
    private CanalesVenta canal;
    private Zonas zona;
    private Rutas ruta;
    private Proveedores proveedor;
    private List<TiposVentas> listaTiposVentas;
    private List<CanalesVenta> listaCanales;
    private List<Zonas> listaZonas;
    private List<Rutas> listaRutas;
    private List<Proveedores> listaProveedores;
    private String seleccion;
    private boolean soloNotas;
    @EJB
    private TiposVentasFacade tiposVentasFacade;
    @EJB
    private CanalesVentaFacade canalFacade;
    @EJB
    private ZonasFacade zonasFacade;
    @EJB
    private RutasFacade rutasFacade;
    @EJB
    private ProveedoresFacade proveedoresFacade;
    @EJB
    private ExcelFacade excelFacade;
    
    @PostConstruct
    public void instanciar() {
        this.seleccion = "1";     
        this.soloNotas = false;
        this.facturacionDesde = new Date();
        this.facturacionHasta = new Date();
        this.listaTiposVentas = tiposVentasFacade.listarTiposVentasOrdenadoXDesc();
        this.listaCanales = canalFacade.listarCanalesOrdenadoXDesc();
        this.listaZonas = zonasFacade.listarZonas();
        this.listaRutas = rutasFacade.listarRutas();
        this.listaProveedores = proveedoresFacade.listarProveedoresActivos();
    }

    public void ejecutarListado(String tipo){
        try {
            LlamarReportes rep = new LlamarReportes();
            String fFacturacionDesde = DateUtil.formaterDateToString(facturacionDesde, "yyyy/MM/dd");
            String fFacturacionHasta = DateUtil.formaterDateToString(facturacionHasta, "yyyy/MM/dd");
            
            String titulo;
            String reporte;
            String[] columnas = null;
            
            String query; //cursor mostrar
            String query2;
            String queryReport = ""; //cursor curfin
            String orderBy = "";
            String extraWhere = "";
            String extraWhere2 = "";
            String extraWhere3 = "";
            String extraWhere8 = "";
            String extraWhere9 = "";
            
            if (zona != null){
                extraWhere += "AND z.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
                extraWhere3 += "AND z.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
            }       
            if (canal != null){
                extraWhere += "AND f.cod_canal = '" + canal.getCodCanal() + "' ";
                extraWhere3 += "AND a.cod_canal = '" + canal.getCodCanal() + "' ";
            }
            if (tipoVenta != null){
                extraWhere += "AND f.ctipo_vta = '" + tipoVenta.getTiposVentasPK().getCtipoVta() + "' ";
                extraWhere3 += "AND a.ctipo_vta = '" + tipoVenta.getTiposVentasPK().getCtipoVta() + "' ";
            }
            if (ruta != null){
                extraWhere8 += "AND r.cod_ruta = " + ruta.getRutasPK().getCodRuta()+ " ";
                extraWhere9 += "AND r.cod_ruta = " + ruta.getRutasPK().getCodRuta()+ " ";
            }
            if (proveedor != null){
                extraWhere8 += "AND p.cod_proveed = " + proveedor.getCodProveed() + " ";
                extraWhere9 += "AND m.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            if (!seleccion.equals("1")) {
        	extraWhere += "AND f.mestado = 'A' ";
                extraWhere3 += "AND f.mestado = 'A' ";
            }
            if (soloNotas) { // solo en segundo union
                extraWhere2 += "AND f.cconc = 'DEV' ";
            }
                    
            if (seleccion.equals("1")) {
                reporte = "RVTADIA";
                titulo = "TODAS LAS FACTURAS";
                orderBy += "ORDER BY 8, f.ctipo_docum, 7 ";
            } else if (seleccion.equals("2")) {
                reporte = "RVTARUTA";
                titulo = "AGRUPADAS POR RUTA";
                orderBy += "ORDER BY 1, 4, 8, f.ctipo_docum, 7 ";
            } else if (seleccion.equals("3")) {
                reporte = "RRESDIA";
                titulo = "CONSOLIDADAS POR FECHA";
                orderBy += "ORDER BY 1, 8, f.ctipo_docum, 7 ";
            } else if (seleccion.equals("4")) {
                reporte = "RRESZONA";
                titulo = "CONSOLIDADAS POR ZONA";
                orderBy += "ORDER BY 1 ";
            } else if (seleccion.equals("5")) {
                reporte = "RRESENTRE";
                titulo = "CONSOLIDADAS POR ENTREGADOR";
                orderBy += "ORDER BY f.cod_entregador, e.xnombre ";
            } else if (seleccion.equals("6")) {
                reporte = "RRESZDIV";
                titulo = "CONSOLIDADAS POR ZONA, DIVISION Y TIPO DE PRECIO";
                orderBy += "ORDER BY m.cod_zona, m.cod_division, m.xdesc ";
            } else if (seleccion.equals("7")) {
                reporte = "RRESENTRE";
                titulo = "CONSOLIDADAS POR VENDEDOR";
                orderBy += "ORDER BY cod_vendedor ";
            } else {
                reporte = "RVTA_PROVEED";
                titulo = "AGRUPADAS POR PROVEEDOR";
                orderBy += "ORDER BY p.cod_proveed, p.xnombre, 6, 7";
            }
            
            if (seleccion.equals("1") || seleccion.equals("2") || seleccion.equals("3") || seleccion.equals("4") || seleccion.equals("5") || seleccion.equals("7")) {
                columnas = new String[21];
                columnas[0] = "cod_zona";
                columnas[1] = "xdesc_zona";
                columnas[2] = "cconc";
                columnas[3] = "cod_ruta";
                columnas[4] = "xdesc_ruta";
                columnas[5] = "ctipo_docum";
                columnas[6] = "nrofact";
                columnas[7] = "fmovim";
                columnas[8] = "fac_ctipo_docum";
                columnas[9] = "fac_docum";
                columnas[10] = "timpuestos";
                columnas[11] = "tgravadas";
                columnas[12] = "texentas";
                columnas[13] = "ttotal";
                columnas[14] = "mestado";
                columnas[15] = "xnombre";
                columnas[16] = "cod_entregador";
                columnas[17] = "xentregador";
                columnas[18] = "ctipo_vta";
                columnas[19] = "cod_vendedor";
                columnas[20] = "xvendedor";
                
                query = 
                    "SELECT " + 
                        "f.cod_zona, z.xdesc AS xdesc_zona, '' as cconc, f.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nrofact, f.ffactur AS fmovim, " +
                        "f.ctipo_docum AS fac_ctipo_docum, f.nrofact AS fac_docum, f.timpuestos, f.tgravadas, f.texentas, f.ttotal, f.mestado, f.xrazon_social AS xnombre, " +
                        "f.cod_entregador, e.xnombre AS xentregador, f.ctipo_vta, f.cod_vendedor, e2.xnombre AS xvendedor " +
                    "FROM " +
                        "facturas f, rutas r, zonas z, empleados e, empleados e2 " +
                    "WHERE " +
                        "f.cod_zona = z.cod_zona " +
                        "AND f.cod_ruta = r.cod_ruta " +
                        "AND f.cod_entregador = e.cod_empleado " +
                        "AND f.cod_vendedor = e2.cod_empleado " +
                        "AND f.cod_empr = 2 " +
                        "AND f.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.ctipo_docum != 'FPO' " +
                        extraWhere +
                    "UNION ALL " +
                    "SELECT " +
                        "a.cod_zona, z.xdesc AS xdesc_zona, f.cconc, a.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nro_nota AS nrofact, f.fdocum AS fmovim, " + 
                        "f.fac_ctipo_docum, f.nrofact AS fac_docum, f.timpuestos*-1, f.tgravadas*-1, f.texentas*-1, f.ttotal*-1, f.mestado, c.xnombre, " +
                        "f.cod_entregador, e.xnombre AS xentregador, a.ctipo_vta, a.cod_vendedor, e2.xnombre AS xvendedor " +
                    " FROM " +
                        "notas_ventas f, zonas z, rutas r, facturas a, clientes c, empleados e, empleados e2 " +
                    " WHERE " +
                        "f.cod_empr = 2 " +
                        "AND f.nrofact = a.nrofact " +
                        "AND f.fac_ctipo_docum = a.ctipo_docum " +
                        "AND a.ffactur = f.ffactur " +
                        "AND f.cod_entregador = e.cod_empleado " +
                        "AND z.cod_zona = a.cod_zona " +
                        "AND f.cod_cliente = c.cod_cliente " +
                        "AND a.cod_vendedor = e2.cod_empleado " +
                        "AND a.cod_ruta = r.cod_ruta " + 
                        "AND f.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.ctipo_docum = 'NCV' " +
                        "AND a.ctipo_docum != 'FPO' " +
                        extraWhere3 +
                        extraWhere2 +
                    "UNION ALL " + 
                    "SELECT " +
                        "a.cod_zona, z.xdesc AS xdesc_zona, f.cconc, a.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nro_nota AS nrofact, f.fdocum AS fmovim, " +
                        "f.fac_ctipo_docum, f.nrofact AS fac_docum, f.timpuestos, f.tgravadas, f.texentas, f.ttotal, f.mestado, c.xnombre, f.cod_entregador, " +
                        "e.xnombre AS xentregador, a.ctipo_vta, a.cod_vendedor, e2.xnombre AS xvendedor " +
                    "FROM " +
                        "notas_ventas f, zonas z, rutas r, facturas a, clientes c, empleados e, empleados e2 " +
                    "WHERE " + 
                        "f.cod_empr = 2 " +
                        "AND f.nrofact = a.nrofact " + 
                        "AND f.fac_ctipo_docum = a.ctipo_docum " + 
                        "AND f.ffactur = a.ffactur " +
                        "AND f.cod_entregador = e.cod_empleado " +
                        "AND a.cod_vendedor = e2.cod_empleado " +
                        "AND z.cod_zona = a.cod_zona " +
                        "AND f.cod_cliente = c.cod_cliente " +
                        "AND a.cod_ruta = r.cod_ruta " +
                        "AND f.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.ctipo_docum = 'NDV' " +
                        "AND a.ctipo_docum != 'FPO' " +
                        extraWhere3;
                queryReport = 
                    query + orderBy;
                
            } else if (seleccion.equals("6")) {
                columnas = new String[7];
                columnas[0] = "cod_zona";
                columnas[1] = "cod_division";
                columnas[2] = "xdesc";
                columnas[3] = "vta_mayorista";
                columnas[4] = "vta_detalle";
                columnas[5] = "vta_contado";
                columnas[6] = "vta_otras";
                
                query = 
                    "SELECT " +
                        "f.cod_zona, z.xdesc AS xdesc_zona, '' as cconc, f.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nrofact, f.ffactur AS fmovim, " +
                        "f.ctipo_docum AS fac_ctipo_docum, f.nrofact AS fac_docum, f.timpuestos, f.tgravadas, f.texentas, d.itotal AS ttotal, f.mestado, " +
                        "f.xrazon_social AS xnombre, f.cod_entregador, e.xnombre AS xentregador, f.ctipo_vta, m.cod_sublinea " +
                    "FROM " +
                        "facturas f, rutas r, zonas z, empleados e, facturas_det d, mercaderias m " +
                    "WHERE " +
                        "f.cod_zona = z.cod_zona " +
                        "AND f.cod_ruta = r.cod_ruta " +
                        "AND f.cod_entregador = e.cod_empleado " +
                        "AND f.nrofact = d.nrofact " +
                        "AND f.ffactur = d.ffactur " +
                        "AND f.cod_empr = d.cod_empr " +
                        "AND d.cod_empr= 2 " +
                        "AND d.cod_merca = m.cod_merca " +
                        "AND f.cod_empr = 2 " +
                        "AND f.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.ctipo_docum != 'FPO' " +
                        extraWhere +
                    "UNION ALL " +
                    "SELECT " +
                        "a.cod_zona, z.xdesc AS xdesc_zona, f.cconc, a.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nro_nota AS nrofact, f.fdocum AS fmovim, " +
                        "f.fac_ctipo_docum, f.nrofact AS fac_docum, f.timpuestos*-1, f.tgravadas*-1, f.texentas*-1, (d.iexentas + d.igravadas)*-1 AS ttotal, f.mestado, " +
                        "c.xnombre, f.cod_entregador, e.xnombre AS xentregador, a.ctipo_vta, m.cod_sublinea " +
                    "FROM " +
                        "notas_ventas f, zonas z, rutas r, facturas a, clientes c, empleados e, notas_ventas_det d, mercaderias m " +
                    "WHERE " + 
                        "f.cod_empr = 2 " +
                        "AND f.nrofact = a.nrofact " +
                        "AND f.fac_ctipo_docum = a.ctipo_docum " +
                        "AND a.ffactur = f.ffactur " +
                        "AND f.cod_entregador = e.cod_empleado " +
                        "AND z.cod_zona = a.cod_zona " +
                        "AND f.fdocum = d.fdocum " +
                        "AND f.cod_cliente = c.cod_cliente " +
                        "AND a.cod_ruta = r.cod_ruta " +
                        "AND f.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.nro_nota = d.nro_nota " +
                        "AND F.ctipo_docum = d.ctipo_docum " +
                        "AND f.cod_empr = d.cod_empr " +
                        "AND d.cod_empr= 2 " +
                        "AND d.cod_merca = m.cod_merca " +
                        "AND f.ctipo_docum = 'NCV' " +
                        "AND a.ctipo_docum != 'FPO' " + 
                        extraWhere3 +
                        extraWhere2 +
                    "UNION ALL " + 
                    "SELECT " +
                        "a.cod_zona, z.xdesc AS xdesc_zona, f.cconc, a.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nro_nota AS nrofact, f.fdocum AS fmovim, " +
                        "f.fac_ctipo_docum, f.nrofact AS fac_docum, f.timpuestos, f.tgravadas, f.texentas, d.iexentas + d.igravadas AS ttotal, f.mestado, c.xnombre, " +
                        "f.cod_entregador, e.xnombre AS xentregador, a.ctipo_vta, m.cod_sublinea " +
                    "FROM " +
                        "notas_ventas f, zonas z, rutas r, facturas a, clientes c, empleados e, notas_ventas_det d, mercaderias m " +
                    "WHERE " +
                        "f.cod_empr = 2 " +
                        "AND f.nrofact = a.nrofact " +
                        "AND f.fac_ctipo_docum = a.ctipo_docum " +
                        "AND a.ffactur = f.ffactur " +
                        "AND f.cod_entregador = e.cod_empleado " +
                        "AND f.fdocum = d.fdocum " +
                        "AND z.cod_zona = a.cod_zona " +
                        "AND f.cod_cliente = c.cod_cliente " +
                        "AND a.cod_ruta = r.cod_ruta " +
                        "AND f.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.ctipo_docum = 'NDV' " +
                        "AND f.ctipo_docum = d.ctipo_docum " +
                        "AND f.nro_nota = d.nro_nota " +
                        "AND f.cod_empr = d.cod_empr " +
                        "AND d.cod_empr= 2 " +
                        "AND d.cod_merca = m.cod_merca " +
                        "AND a.ctipo_docum != 'FPO' " +
                        extraWhere3;
                query2 = 
                    "SELECT " + 
                        "m.cod_zona, d.cod_division, d.xdesc, SUM(ttotal) AS vta_mayorista, 0 AS vta_detalle, 0 AS vta_contado, 0 AS vta_otras " +
                    "FROM " +
                        "(" + query + ") m, sublineas s, lineas l, categorias g, divisiones d " +
                    "WHERE " + 
                        "ctipo_vta = 'M' " +
                        "AND m.cod_sublinea = s.cod_sublinea " +
                        "AND s.cod_linea = l.cod_linea " +
                        "AND l.cod_categoria = g.cod_categoria " +
                        "AND g.cod_division = d.cod_division " +
                    "GROUP BY " +
                        "m.cod_zona, d.cod_division, d.xdesc " +
                    "UNION ALL " +
                    "SELECT " + 
                        "m.cod_zona, d.cod_division, d.xdesc, 0 AS vta_mayorista, SUM(ttotal) AS vta_detalle, 0 AS vta_contado, 0 AS vta_otras " +
                    "FROM " +
                        "(" + query + ") m, sublineas s, lineas l, categorias g, divisiones d " +
                    "WHERE " +
                        "ctipo_vta = 'D' " +
                        "AND m.cod_sublinea = s.cod_sublinea " +
                        "AND s.cod_linea = l.cod_linea " +
                        "AND l.cod_categoria = g.cod_categoria " +
                        "AND g.cod_division = d.cod_division " +
                    "GROUP BY " +
                        "m.cod_zona, d.cod_division, d.xdesc " +
                    "UNION ALL " +
                    "SELECT " + 
                        "m.cod_zona, d.cod_division, d.xdesc, 0 AS vta_mayorista, 0 AS vta_detalle, SUM(ttotal) AS vta_contado, 0000000000 AS vta_otras " +
                    "FROM " + 
                        "(" + query + ") m, sublineas s, lineas l, categorias g, divisiones d " +
                    "WHERE " + 
                        "ctipo_vta = 'C' " +
                        "AND m.cod_sublinea = s.cod_sublinea " +
                        "AND s.cod_linea = l.cod_linea " +
                        "AND l.cod_categoria = g.cod_categoria " +
                        "AND g.cod_division = d.cod_division " +
                    "GROUP BY " + 
                        "m.cod_zona, d.cod_division, d.xdesc " +
                    "UNION ALL " +
                    "SELECT " +
                        "m.cod_zona, d.cod_division, d.xdesc, 0 AS vta_mayorista, 0 AS vta_detalle, 0 AS vta_contado, SUM(ttotal) AS vta_otras " +
                    "FROM " +
                        "(" + query + ") m, sublineas s, lineas l, categorias g, divisiones d " +
                    "WHERE " + 
                        "ctipo_vta NOT IN('C','D','M') " +
                        "AND m.cod_sublinea = s.cod_sublinea " +
                        "AND s.cod_linea = l.cod_linea " +
                        "AND l.cod_categoria = g.cod_categoria " +
                        "AND g.cod_division = d.cod_division " +
                    "GROUP BY " + 
                        "m.cod_zona, d.cod_division, d.xdesc";
                queryReport =
                    "SELECT " +
                        "cod_zona, cod_division, xdesc, SUM(vta_mayorista) AS vta_mayorista, SUM(vta_detalle) AS vta_detalle, SUM(vta_contado) AS vta_contado, " +
                        "SUM(vta_otras) AS vta_otras " +
                    "FROM " + 
                        "(" + query2 + ") m " +
                    "GROUP BY " + 
                        "m.cod_zona, m.cod_division, m.xdesc " +
                    orderBy;
            } else if (seleccion.equals("8")) {
                columnas = new String[23];
                columnas[0] = "cod_zona";
                columnas[1] = "xdesc_zona";
                columnas[2] = "cconc";
                columnas[3] = "cod_ruta";
                columnas[4] = "xdesc_ruta";
                columnas[5] = "ctipo_docum";
                columnas[6] = "nrofact";
                columnas[7] = "fmovim";
                columnas[8] = "fac_ctipo_docum";
                columnas[9] = "fac_docum";
                columnas[10] = "impuestos";
                columnas[11] = "igravadas";
                columnas[12] = "iexentas";
                columnas[13] = "itotal";
                columnas[14] = "mestado";
                columnas[15] = "xnombre";
                columnas[16] = "cod_entregador";
                columnas[17] = "xentregador";
                columnas[18] = "ctipo_vta";
                columnas[19] = "cod_vendedor";
                columnas[20] = "xvendedor";
                columnas[21] = "cod_proveed";
                columnas[22] = "xnombre_proveedor";
                
                query = 
                    "SELECT " + 
                        "f.cod_zona, z.xdesc AS xdesc_zona, '' AS cconc, f.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nrofact, f.ffactur AS fmovim, " +
                        "f.ctipo_docum AS fac_ctipo_docum, f.nrofact AS fac_docum, d.impuestos, d.igravadas, d.iexentas, d.itotal, f.mestado, f.xrazon_social AS xnombre, " +
                        "f.cod_entregador, e.xnombre AS xentregador, f.ctipo_vta, f.cod_vendedor, e2.xnombre AS xvendedor, p.cod_proveed, p.xnombre AS xnombre_proveedor " +
                    "FROM " +
                        "facturas f, rutas r, zonas z, empleados e, empleados e2, facturas_det d, mercaderias m, proveedores p " +
                    "WHERE " +
                        "f.cod_zona = z.cod_zona " +
                        "AND f.cod_ruta = r.cod_ruta " +
                        "AND f.cod_entregador = e.cod_empleado " +
                        "AND f.cod_vendedor = e2.cod_empleado " +
                        "AND f.cod_empr = 2 " + 
                        "AND f.ffactur BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.ctipo_docum != 'FPO' " +
                        "AND d.cod_empr = 2 " +
                        "AND f.nrofact = d.nrofact " +
                        "AND f.ffactur = d.ffactur " +
                        "AND f.ctipo_docum = d.ctipo_docum " +
                        "AND d.cod_merca = m.cod_merca " +
                        "AND m.cod_proveed = p.cod_proveed " + 
                    extraWhere +
                    extraWhere8 + 
                    "UNION ALL " +
                    "SELECT " +
                        "a.cod_zona, z.xdesc AS xdesc_zona, f.cconc, a.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nro_nota AS nrofact, f.fdocum AS fmovim, " +
                        "f.fac_ctipo_docum, f.nrofact AS fac_docum, d.impuestos*-1 AS impuestos, d.igravadas*-1 AS igravadas, d.iexentas*-1 AS iexentas, 0 as itotal, " + 
                        "f.mestado, c.xnombre, f.cod_entregador, e.xnombre AS xentregador, a.ctipo_vta, a.cod_vendedor, e2.xnombre AS xvendedor, p.cod_proveed, " +
                        "p.xnombre AS xnombre_proveedor " +
                    "FROM " + 
                        "notas_ventas f, zonas z, rutas r, facturas a, clientes c, empleados e, empleados e2, notas_ventas_det d, mercaderias m, proveedores p " +
                    "WHERE " +
                        "f.cod_empr = 2 " +
                        "AND f.nrofact = a.nrofact " +
                        "AND f.fac_ctipo_docum = a.ctipo_docum " +
                        "AND a.ffactur = f.ffactur " +
                        "AND f.cod_entregador = e.cod_empleado " +
                        "AND z.cod_zona = a.cod_zona " +
                        "AND f.cod_cliente = c.cod_cliente " +
                        "AND a.cod_vendedor = e2.cod_empleado " +
                        "AND a.cod_ruta = r.cod_ruta " + 
                        "AND f.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.ctipo_docum = 'NCV' " +
                        "AND a.ctipo_docum != 'FPO' " + 
                        "AND d.cod_empr = 2 " +
                        "AND f.nro_nota = d.nro_nota " +
                        "AND f.fdocum = d.fdocum " +
                        "AND f.ctipo_docum = d.ctipo_docum " +
                        "AND d.cod_merca = m.cod_merca " +
                        "AND m.cod_proveed = p.cod_proveed " +
                        extraWhere3 + 
                        extraWhere2 +
                        extraWhere9 +
                    "UNION ALL " +
                    "SELECT " +
                        "a.cod_zona, z.xdesc AS xdesc_zona, f.cconc, a.cod_ruta, r.xdesc AS xdesc_ruta, f.ctipo_docum, f.nro_nota AS nrofact, f.fdocum AS fmovim, " + 
                        "f.fac_ctipo_docum, f.nrofact AS fac_docum, d.impuestos, d.igravadas, d.iexentas, 0 AS itotal, f.mestado, c.xnombre, f.cod_entregador, " +
                        "e.xnombre AS xentregador, a.ctipo_vta, a.cod_vendedor, e2.xnombre AS xvendedor, p.cod_proveed, p.xnombre AS xnombre_proveedor " +
                    "FROM " +
                        "notas_ventas f, zonas z, rutas r, facturas a, clientes c, empleados e, empleados e2, notas_ventas_det d, mercaderias m, proveedores p " +
                    "WHERE " +
                        "f.cod_empr = 2 " + 
                        "AND f.nrofact = a.nrofact " + 
                        "AND f.fac_ctipo_docum = a.ctipo_docum " +
                        "AND f.ffactur = a.ffactur " +
                        "AND F.cod_entregador = e.cod_empleado " +
                        "AND a.cod_vendedor = e2.cod_empleado " +
                        "AND z.cod_zona = a.cod_zona " +
                        "AND f.cod_cliente = c.cod_cliente " +
                        "AND a.cod_ruta = r.cod_ruta " + 
                        "AND f.fdocum BETWEEN '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                        "AND f.ctipo_docum = 'NDV' " +
                        "AND a.ctipo_docum != 'FPO' " +
                        "AND d.cod_empr = 2 " +
                        "AND f.nro_nota = d.nro_nota " +
                        "AND f.fdocum = d.fdocum " +
                        "AND f.ctipo_docum = d.ctipo_docum " +
                        "AND d.cod_merca = m.cod_merca " +
                        "AND m.cod_proveed = p.cod_proveed " + 
                        extraWhere3 + 
                        extraWhere9;
                queryReport = 
                    query + orderBy;
            }
            
            System.out.println("QUERY: " + queryReport);
            
            if (tipo.equals("VIST")) {
                String usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                Map param = new HashMap();
                param.put("sql", queryReport);
                param.put("fdesde", facturacionDesde);
                param.put("fhasta", facturacionHasta);
                param.put("l_xdetalle", titulo);
                param.put("seleccion", seleccion);
                param.put("usuImprime", usuImprime);
                param.put("nombreRepo", reporte); 

                if (this.tipoVenta != null) param.put("tipo_venta", tiposVentasFacade.getTipoVentaFromList(this.tipoVenta, this.listaTiposVentas).getXdesc());
                else param.put("tipo_venta", "TODOS");
                
                if (this.canal != null) param.put("canal", canalFacade.getCanalVentaFromList(this.canal, this.listaCanales).getXdesc()); 
                else param.put("canal", "TODOS");
                
                if (this.zona != null) param.put("zona", zonasFacade.getZonaFromList(this.zona, this.listaZonas).getXdesc()); 
                else param.put("zona", "TODOS");
                
                rep.reporteGenerico(param, tipo, reporte);
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
    
    public CanalesVenta getCanal() {
        return canal;
    }

    public void setCanal(CanalesVenta canal) {
        this.canal = canal;
    }

    public List<CanalesVenta> getListaCanales() {
        return listaCanales;
    }

    public void setListaCanales(List<CanalesVenta> listaCanales) {
        this.listaCanales = listaCanales;
    }

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }
    
    public TiposVentas getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(TiposVentas tipoVenta) {
        this.tipoVenta = tipoVenta;
    }
    
    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    public List<TiposVentas> getListaTiposVentas() {
        return listaTiposVentas;
    }

    public void setListaTiposVentas(List<TiposVentas> listaTiposVentas) {
        this.listaTiposVentas = listaTiposVentas;
    }

    public boolean isSoloNotas() {
        return soloNotas;
    }

    public void setSoloNotas(boolean soloNotas) {
        this.soloNotas = soloNotas;
    }

    public Rutas getRuta() {
        return ruta;
    }

    public void setRuta(Rutas ruta) {
        this.ruta = ruta;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public List<Rutas> getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(List<Rutas> listaRutas) {
        this.listaRutas = listaRutas;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }
    
}
