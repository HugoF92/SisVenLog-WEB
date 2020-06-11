package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.LineasFacade;
import dao.MercaderiasFacade;
import dao.ZonasFacade;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.Lineas;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Zonas;
import entidad.ZonasPK;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import util.LlamarReportes;

/**
 *
 * @author Nico
 */
@ManagedBean
@SessionScoped
public class LiConCliBean {
    
    private Date desde;    
    private Date hasta;    
    private Zonas zonas;
    private Empleados vendedor;
    private Lineas linea;
    private Mercaderias mercaderia;
    
    private List<Zonas> listaZonas;
    private List<Empleados> listaVendedor;
    private List<Lineas> listaLineas;
    
    private List<Mercaderias> listaMercaderias;
    private List<Mercaderias> listaMercaderiasSeleccionadas;
    private DualListModel<Mercaderias> mercaderias;
    
    private DefaultStreamedContent download;
        
    @EJB
    private ZonasFacade zonasFacade;
    @EJB
    private EmpleadosFacade empleadoFacade;
    @EJB
    private ExcelFacade excelFacade;
    @EJB
    private LineasFacade lineasFacade;
    @EJB
    private MercaderiasFacade mercaderiasFacade;
        
    private String seleccion,nuevos;
    private Boolean nuevo;
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.seleccion = new String("1");
        this.vendedor = new Empleados(new EmpleadosPK());
        this.zonas = new Zonas(new ZonasPK());
        this.nuevo =  false;
        this.desde = new Date();
        this.hasta = new Date();
        this.listaVendedor = empleadoFacade.getEmpleadosVendedoresActivosPorCodEmp(2);
        this.listaZonas = zonasFacade.listarZonas();
        this.listaLineas = lineasFacade.listarLineasOrdenadoXCategoria();
        
        List<Mercaderias> mercaSource = new ArrayList<Mercaderias>();
        List<Mercaderias> mercaTarget = new ArrayList<Mercaderias>();

        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();

        mercaderias = new DualListModel<Mercaderias>(mercaSource, mercaTarget);
    }
    
    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();
        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String extras = "" , extras2 = "";
        if (this.zonas != null){
            extras += " AND f.cod_zona = '"+this.zonas.getZonasPK().getCodZona()+"' ";
            extras2 += " AND f.cod_zona = '"+this.zonas.getZonasPK().getCodZona()+"' ";
        }
        if (this.vendedor != null){
            extras += " AND f.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
        }
        if ( this.nuevo ) {
            String ldesde = fdesde + "00:00:00";
            String lhasta = fhasta + "23:59:00";
            extras += " AND m.falta between '"+ldesde+"' AND '"+lhasta+"' ";
        }
        List<Object[]> lista = new ArrayList<Object[]>();
        String[] columnas = null;
        String sql = "";
        switch ( this.seleccion ) {
            case "1":
                columnas = new String[6];
                columnas[0] = "cod_vendedor";
                columnas[1] = "xnombre";
                columnas[2] = "cod_merca";
                columnas[3] = "xdesc";
                columnas[4] = "fch_alta";
                columnas[5] = "cant_clientes";
                sql = "select f.cod_vendedor, e.xnombre,m.cod_merca, m.xdesc," +
                        "m.falta as fch_alta, count(distinct cod_cliente) as cant_clientes" +
                        " from facturas f, facturas_det d, empleados e, mercaderias m " +
                        "where f.ffactur between '"+fdesde+"' AND '"+fhasta+
                        "' and f.mestado ='A' AND f.ctipo_vta != 'X' " +
                        "AND f.nrofact = d.nrofact and f.ctipo_docum = d.ctipo_docum " +
                        "AND f.ffactur = d.ffactur AND f.cod_empr = d.cod_empr " +
                        "and d.cod_empr = m.cod_empr and f.cod_vendedor = e.cod_empleado " +
                        "AND f.cod_empr = e.cod_empr and d.cod_merca = m.cod_merca " +
                        "AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                        "AND e.cod_empr = 2 AND m.cod_empr = 2 "+ 
                        extras +
                        " group by f.cod_vendedor, e.xnombre, m.cod_merca, m.xdesc, m.falta "; 
                break;
            case "2":
                columnas = new String[7];
                columnas[0] = "cod_vendedor";
                columnas[1] = "xnombre";
                columnas[2] = "cod_sublinea";
                columnas[3] = "xdesc";
                columnas[4] = "ctipo_vta";
                columnas[5] = "ctipo_cliente";
                columnas[6] = "cant_clientes";
                sql = "select f.cod_vendedor, e.xnombre, m.cod_sublinea, s.xdesc," +
                    "f.ctipo_vta, c.ctipo_cliente, count(distinct c.cod_cliente) as cant_clientes " +
                    "from facturas f, facturas_det d, empleados e, sublineas s,mercaderias m, clientes c " +
                    "where f.ffactur between '"+fdesde+"' AND '" +fhasta+
                    "' AND f.cod_cliente = c.cod_cliente  and f.mestado ='A' " +
                    "and f.ctipo_vta !='X' and m.cod_sublinea = s.cod_sublinea " +
                    " AND f.nrofact = d.nrofact and f.ctipo_docum = d.ctipo_docum " +
                    " AND f.ffactur = d.ffactur AND f.cod_empr = d.cod_empr " +
                    " and d.cod_empr = m.cod_empr and f.cod_vendedor = e.cod_empleado " +
                    " AND f.cod_empr = e.cod_empr and d.cod_merca = m.cod_merca " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND e.cod_empr = 2 AND m.cod_empr = 2"+ 
                    extras +
                    " group by f.cod_vendedor, e.xnombre, m.cod_sublinea, s.xdesc, f.ctipo_vta, c.ctipo_cliente "; 
                break;
            case "3":
                columnas = new String[12];
                columnas[0] = "finicial";
                columnas[1] = "ffinal";
                columnas[2] = "cod_zona";
                columnas[3] = "cod_vendedor";
                columnas[4] = "xnombre";
                columnas[5] = "xdes_sublinea";
                columnas[6] = "ctipo_vta";
                columnas[7] = "ctipo_cliente";
                columnas[8] = "itotal";
                columnas[9] = "cant_cajas";
                columnas[10] = "cant_unid";
                columnas[11] = "cant_clientes";
                sql = "SELECT cast('"+fdesde+"' as char) as finicial," +
                    "cast('"+fhasta+"' as char) as ffinal, f.cod_zona, f.cod_vendedor," +
                    "v.xnombre,d.cod_merca, m.xdesc as xdesc_merca, m.cod_sublinea," +
                    "s.xdesc AS xdesc_sublinea, f.ctipo_vta, c.ctipo_cliente," +
                    "SUM(d.itotal) as itotal, SUM(d.cant_cajas) as cant_cajas ," +
                    "SUM(d.cant_unid) AS cant_unid, COUNT(DISTINCT f.cod_cliente) AS cant_clientes " +
                    "FROM facturas f INNER JOIN facturas_det d ON f.nrofact = d.nrofact " +
                    "AND f.ctipo_docum = d.ctipo_docum and F.FFACTUR = D.FFACTUR " +
                    "INNER JOIN empleados v ON f.cod_vendedor = v.cod_empleado " +
                    "INNER JOIN mercaderias m ON d.cod_merca = m.cod_merca" +
                    "INNER JOIN sublineas s ON m.cod_sublinea = s.cod_sublinea" +
                    "INNER JOIN clientes c ON f.cod_cliente = c.cod_cliente" +
                    "WHERE f.ctipo_vta !='X' AND F.MESTADO ='A' AND (f.cod_empr = 2) " +
                    "and f.cod_empr = d.cod_empr AND (f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+ 
                    extras +
                    " GROUP BY f.cod_zona, f.cod_vendedor, v.xnombre, d.cod_merca, m.xdesc, m.cod_sublinea, s.xdesc, f.ctipo_vta, c.ctipo_cliente "; 
                break;
            case "4":
                columnas = new String[6];
                columnas[0] = "finicial";
                columnas[1] = "ffinal";
                columnas[2] = "cod_vendedor";
                columnas[3] = "xnombre";
                columnas[4] = "cant_clientes";
                columnas[5] = "ttotal";
                sql = "SELECT cast('"+fdesde+"' as char) as finicial, cast('"+fhasta+"' as char) as ffinal," +
                    "f.cod_vendedor, v.xnombre, COUNT(DISTINCT f.cod_cliente) AS cant_clientes," +
                    "SUM(f.ttotal - f.tnotas) AS ttotal " +
                    "FROM facturas f " +
                    "INNER JOIN facturas_det d ON f.nrofact = d.nrofact " +
                    " AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur " +   
                    " INNER JOIN empleados v ON f.cod_vendedor = v.cod_empleado " +
                    " INNER JOIN mercaderias m ON d.cod_merca = m.cod_merca " +
                    " INNER JOIN sublineas s ON m.cod_sublinea = s.cod_sublinea " +
                    " INNER JOIN clientes c ON f.cod_cliente = c.cod_cliente " +
                    " where F.CTIPO_VTA !='X' AND F.MESTADO ='A' " +
                    " AND (f.cod_empr = 2) and f.cod_empr = d.cod_empr " +  
                    " AND (f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+ 
                    extras +
                    " GROUP BY f.cod_vendedor, v.xnombre "; 
                break;
            case "5":
                columnas = new String[7];
                columnas[0] = "cod_vendedor";
                columnas[1] = "xnombre";
                columnas[2] = "cod_linea";
                columnas[3] = "xdesc";
                columnas[4] = "ctipo_vta";
                columnas[5] = "ctipo_cliente";
                columnas[6] = "cant_clientes";
                sql = "select f.cod_vendedor, e.xnombre, L.cod_linea, l.xdesc, f.ctipo_vta," +
                    "c.ctipo_cliente, count(distinct c.cod_cliente) as cant_clientes " +
                    "from facturas f, facturas_det d, empleados e, sublineas s, mercaderias m, lineas l, clientes c " +
                    "where f.ffactur between '"+fdesde+"' AND '"+fhasta+
                    "' and f.mestado ='A' and f.ctipo_vta !='X' " +
                    "AND f.cod_cliente = c.cod_cliente AND s.cod_linea = l.cod_linea " +
                    "and m.cod_sublinea = s.cod_sublinea AND f.nrofact = d.nrofact " +
                    " and f.ctipo_docum = d.ctipo_docum  AND f.ffactur = d.ffactur " +
                    " AND f.cod_empr = d.cod_empr and d.cod_empr = m.cod_empr " +
                    " and f.cod_vendedor = e.cod_empleado AND f.cod_empr = e.cod_empr " +
                    " and d.cod_merca = m.cod_merca AND f.cod_empr = 2 " +
                    " AND d.cod_empr = 2 AND e.cod_empr = 2 " +
                    " AND m.cod_empr = 2 "+ 
                    extras +
                    " group by f.cod_vendedor, e.xnombre, l.cod_linea, l.xdesc, f.ctipo_vta, c.ctipo_cliente ";
                break;
            case "6":
                columnas = new String[5];
                columnas[0] = "cod_zona";
                columnas[1] = "cod_sublinea";
                columnas[2] = "xdesc";
                columnas[3] = "cant_clientes";
                columnas[4] = "tot_clien";
                sql = "SELECT v.*, c.tot_clien " +
                    " from ( select f.cod_zona, m.cod_sublinea, s.xdesc, count(distinct f.cod_cliente) as cant_clientes " +
                    " from facturas f, facturas_det d, sublineas s, mercaderias m " +
                    " where f.ffactur between '"+fdesde+"' AND '"+fhasta+
                    "' and f.mestado ='A' and f.ctipo_vta !='X' " +
                    " and m.cod_sublinea = s.cod_sublinea AND f.nrofact = d.nrofact " +
                    " and f.ctipo_docum = d.ctipo_docum ANd f.ffactur = d.ffactur " +
                    " AND f.cod_empr = d.cod_empr and d.cod_empr = m.cod_empr " +
                    " and d.cod_merca = m.cod_merca AND f.cod_empr = 2 " +
                    " AND d.cod_empr = 2 AND m.cod_empr = 2 " +
                    extras +
                    " group by F.COD_ZONA, m.cod_sublinea, s.xdesc ) as v ," +
                    "( SELECT r.cod_zona,COUNT(*) as tot_clien " +
                    " FROM clientes c, rutas r " +
                    " WHERE c.cod_estado = 'A' AND c.cod_ruta = r.cod_ruta " +
                    extras2+
                    " GROUP BY r.cod_zona ) as c "+
                    " WHERE v.cod_zona = c.cod_zona ";
                break;
            case "7":
                columnas = new String[5];
                columnas[0] = "cod_zona";
                columnas[1] = "cod_sublinea";
                columnas[2] = "xdesc";
                columnas[3] = "cant_clientes";
                columnas[4] = "tot_clien";
                sql = "SELECT v.*, c.tot_clien " +
                    "from ( select f.cod_zona, m.cod_sublinea, s.xdesc, c.ctipo_cliente as tipo_neg, count(distinctc.cod_cliente) as cant_clientes " +
                    "from facturas f, facturas_det d, sublineas s, mercaderias m,clientes c " +
                    "where f.ffactur between '"+fdesde+"' AND '"+fhasta+
                    "' AND f.cod_cliente = c.cod_cliente and f.mestado ='A' " +
                    " and f.ctipo_vta !='X' and m.cod_sublinea = s.cod_subline " +
                    " AND f.nrofact = d.nrofact and f.ctipo_docum = d.ctipo_docum " +
                    " AND f.ffactur = d.ffactur AND f.cod_empr = d.cod_empr " +
                    " and d.cod_empr = m.cod_empr and d.cod_merca = m.cod_merca " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 AND m.cod_empr = 2 " +
                    extras +
                    " group by F.COD_ZONA,m.cod_sublinea, s.xdesc, c.ctipo_cliente ) as v , " +
                    " ( SELECT r.cod_zona, c.ctipo_cliente, COUNT(*) as tot_clien " +
                    " FROM clientes c, rutas r " +
                    " WHERE c.cod_estado = 'A' AND c.cod_ruta = r.cod_ruta " +
                    extras2+
                    " GROUP BY r.cod_zona, c.ctipo_cliente ) as c " +
                    " WHERE v.cod_zona = c.cod_zona " +
                    " AND v.tipo_neg = c.ctipo_cliente ";
                break;
            case "8":
                columnas = new String[8];
                columnas[0] = "finicial";
                columnas[1] = "ffinal";
                columnas[2] = "cod_vendedor";
                columnas[3] = "cod_sublinea";
                columnas[4] = "xdesc";
                columnas[5] = "xnombre";
                columnas[6] = "cant_clientes";
                columnas[7] = "ttotal";
                sql = "SELECT cast('"+fdesde+"' as char) as finicial, cast('"+fhasta+"' as char) as ffinal, f.cod_vendedor, s.cod_sublinea," +
                    "s.xdesc, v.xnombre, COUNT(DISTINCT f.cod_cliente) AS cant_clientes, SUM(f.ttotal - f.tnotas) AS ttotal " +
                    "FROM facturas f INNER JOIN facturas_det d ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur " +
                    " INNER JOIN empleados v ON f.cod_vendedor = v.cod_empleado " +
                    " INNER JOIN mercaderias m ON d.cod_merca = m.cod_merca " +
                    " INNER JOIN sublineas s ON m.cod_sublinea = s.cod_sublinea " +
                    " INNER JOIN clientes c ON f.cod_cliente = c.cod_cliente " +
                    " WHERE F.CTIPO_VTA != 'X' AND F.MESTADO ='A' AND (f.cod_empr = 2) " +
                    " and f.cod_empr = d.cod_empr AND (f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+ 
                    extras +
                    " GROUP BY f.cod_vendedor, v.xnombre, s.cod_sublinea, s.xdesc ";
                break;
        }
        lista = excelFacade.listarParaExcel(sql);
        rep.exportarExcel(columnas, lista, "KCLIDOS");
    }
    
    public void ejecutarLiRe(String tipo) {
        LlamarReportes rep = new LlamarReportes();
        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String extras = "";
        String sql = null;
        String[] columnas = null;
        String titulo = null;
        String reporte = null;
        
        if (this.zonas != null){
            extras += " AND f.cod_zona = '"+this.zonas.getZonasPK().getCodZona()+"' ";
        }
        if (this.vendedor != null){
            extras += " AND f.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
        }
        if (this.linea != null){
            extras += " AND l.cod_linea = "+this.linea.getCodLinea()+" ";
        }
        if (mercaderias.getTarget().size() > 0) {
                listaMercaderiasSeleccionadas = mercaderias.getTarget();
                extras += " AND m.cod_merca IN (";
                for (int i = 0; i < listaMercaderiasSeleccionadas.size(); i++) {
                    MercaderiasPK pk = listaMercaderiasSeleccionadas.get(i).getMercaderiasPK();
                    extras += " " + pk.getCodMerca() + ",";
                }
                extras += extras.substring(0, extras.length()-1) + " ) ";
        }
        
        switch ( this.seleccion ) {
            case "1":
                titulo = "POR MERCADERIA";
                reporte = "rkclientes1";
                columnas = new String[5];
                columnas[0] = "cod_vendedor";
                columnas[1] = "xnombre";
                columnas[2] = "cod_merca";
                columnas[3] = "xdesc";
                columnas[4] = "cant_clientes";                
                sql = "select f.cod_vendedor, e.xnombre, m.cod_merca, m.xdesc, " +
                        " count(distinct cod_cliente) as cant_clientes " +
                        " from facturas f, facturas_det d, empleados e, mercaderias m, " +
                        "   sublineas s, lineas l " +
                        " where f.ffactur between '"+fdesde+"' AND '"+fhasta+"'" +
                        " AND f.mestado ='A'  " +
                        " AND f.nrofact = d.nrofact " +
                        " AND f.ctipo_docum = d.ctipo_docum " +
                        " AND f.cod_empr = d.cod_empr " +
                        " AND d.cod_empr = m.cod_empr " +
                        " AND f.ffactur = d.ffactur " +
                        " AND f.cod_vendedor = e.cod_empleado " +
                        " AND f.cod_empr = e.cod_empr " +
                        " AND d.cod_merca = m.cod_merca " +
                        " AND f.cod_empr = 2 " +
                        " AND d.cod_merca = m.cod_merca " +
                        " AND m.cod_sublinea = s.cod_sublinea " +
                        " AND s.cod_linea = l.cod_linea "
                        + extras +
                        " group by f.cod_vendedor, e.xnombre, m.cod_merca, m.xdesc " +
                        " order by f.cod_vendedor, m.cod_merca ";
                break;
            case "2":
                titulo = "POR VENDEDOR Y SUBLINEA";
                reporte = "rkclientes2";
                columnas = new String[5];
                columnas[0] = "cod_vendedor";
                columnas[1] = "xnombre";
                columnas[2] = "cod_sublinea";
                columnas[3] = "xdesc";
                columnas[4] = "cant_clientes";
                sql = " select f.cod_vendedor, e.xnombre, m.cod_sublinea, s.xdesc, count(distinct cod_cliente) as cant_clientes " +
                    " from facturas f, facturas_det d, empleados e, sublineas s, mercaderias m, lineas l "+
                    " where f.ffactur between '"+fdesde+"' AND '" +fhasta+ "'" +
                    " and f.mestado ='A' " + 
                    " and m.cod_sublinea = s.cod_sublinea " +
                    " AND f.nrofact = d.nrofact " + 
                    " and f.ctipo_docum = d.ctipo_docum " + 
                    " AND f.cod_empr = d.cod_empr " + 
                    " and d.cod_empr = m.cod_empr " + 
                    " and f.ffactur  = d.ffactur " + 
                    " and f.cod_vendedor = e.cod_empleado " + 
                    " AND f.cod_empr = e.cod_empr " + 
                    " and d.cod_merca = m.cod_merca " +
                    " AND f.cod_empr = 2 and d.cod_empr = 2 " +
                    " AND s.cod_linea = l.cod_linea " 
                    + extras +
                    " group by f.cod_vendedor, e.xnombre, m.cod_sublinea, s.xdesc " +
                    " ORDER BY f.cod_vendedor, m.cod_sublinea "; 
                break;
            case "3":
                titulo = "POR VENDEDOR";
                reporte = "rkclientes3";
                columnas = new String[6];
                columnas[0] = "finicial";
                columnas[1] = "ffinal";
                columnas[2] = "cod_vendedor";
                columnas[3] = "xnombre";
                columnas[4] = "cant_clientes";
                columnas[5] = "ttotal";
                sql = " SELECT cast('"+fdesde+"' as char) as finicial, " +
                " cast('"+fhasta+"' as char) as ffinal, f.cod_vendedor, v.xnombre, "+
                " COUNT(DISTINCT f.cod_cliente) AS cant_clientes, SUM(f.ttotal - f.tnotas) AS ttotal " + 
                " FROM facturas f INNER JOIN facturas_det d " +
                "   ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur INNER JOIN empleados v " +
                "   ON f.cod_vendedor = v.cod_empleado INNER JOIN mercaderias m " +
                "   ON d.cod_merca = m.cod_merca INNER JOIN sublineas s " +
                "   ON m.cod_sublinea = s.cod_sublinea INNER JOIN clientes c " +
                "   ON f.cod_cliente = c.cod_cliente INNER JOIN lineas l" +
                "   ON s.cod_linea = l.cod_linea " +
                " WHERE  F.MESTADO ='A' AND (f.cod_empr = 2) AND d.cod_empr =2  " +
                "   AND (f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+"') "
                + extras +
                " GROUP BY  f.cod_vendedor, v.xnombre " +
                " ORDER BY f.cod_vendedor";
                break;
            case "4":
                titulo = "POR VENDEDOR Y LINEA";
                reporte = "rkclientes4";
                columnas = new String[5];
                columnas[0] = "cod_vendedor";
                columnas[1] = "xnombre";
                columnas[2] = "cod_linea";
                columnas[3] = "xdesc";
                columnas[4] = "cant_clientes";
                sql  = " select f.cod_vendedor, e.xnombre, l.cod_linea, l.xdesc, count(distinct cod_cliente) as cant_clientes " +
                " from facturas f, facturas_det d, empleados e, sublineas s, mercaderias m, lineas l  " +
                " where f.ffactur between '"+fdesde+"' AND '"+fhasta+"' " +
                " and f.mestado ='A' " +
                " AND s.cod_linea = l.cod_linea " +
                " and m.cod_sublinea = s.cod_sublinea " +
                " AND f.nrofact = d.nrofact " +
                " and f.ctipo_docum = d.ctipo_docum " +
                " AND f.ffactur = d.ffactur " +
                " AND f.cod_empr = d.cod_empr " +
                " and d.cod_empr = m.cod_empr " +
                " and f.cod_vendedor = e.cod_empleado " +
                " AND f.cod_empr = e.cod_empr " +
                " and d.cod_merca = m.cod_merca " +
                " AND f.cod_empr = 2 and d.cod_empr=2 "
                + extras +
                " group by f.cod_vendedor, e.xnombre, l.cod_linea, l.xdesc  " +
                " ORDER BY f.cod_vendedor, l.cod_linea";
                break;
            case "5":
                titulo = "POR ZONA Y SUBLINEA";
                reporte = "rkclientes5";
                columnas = new String[5];
                columnas[0] = "cod_zona";
                columnas[1] = "xdesc_zona";
                columnas[2] = "cod_sublinea";
                columnas[3] = "xdesc";
                columnas[4] = "cant_clientes";
                sql = " select f.cod_zona, z.xdesc as xdesc_zona, m.cod_sublinea, s.xdesc, count(distinct cod_cliente) as cant_clientes " +
                " from facturas f, facturas_det d, sublineas s, mercaderias m, zonas z, lineas l " +
                " where f.ffactur between '"+fdesde+"' AND '"+fhasta+ "' " +
                " AND f.cod_zona = z.cod_zona " +
                " and f.mestado ='A' " +
                " and m.cod_sublinea = s.cod_sublinea " +
                " AND f.nrofact = d.nrofact " +
                " and f.ctipo_docum = d.ctipo_docum " +
                " AND f.ffactur = d.ffactur " +
                " AND f.cod_empr = d.cod_empr " +
                " and d.cod_empr = m.cod_empr " +
                " and d.cod_merca = m.cod_merca " +
                " and f.mestado ='A' " +
                " AND f.cod_empr = 2 " +
                " AND d.cod_empr = 2 "	+
                " and l.cod_linea = s.cod_linea "
                + extras +
                " group by f.cod_zona, z.xdesc, m.cod_sublinea, s.xdesc " +
                " ORDER BY f.cod_zona, m.cod_sublinea ";
                break;
            case "6":
                titulo = "POR PROVEEDOR";
                reporte = "rkclientes6";
                columnas = new String[3];
                columnas[0] = "cod_proveed";
                columnas[1] = "xnombre";
                columnas[2] = "cant_clientes";
                sql = " select m.cod_proveed, p.xnombre,  count(distinct cod_cliente) as cant_clientes " +
                " from facturas f, facturas_det d, sublineas s, mercaderias m, proveedores p, lineas l " +
                " where f.ffactur between '"+fdesde+"' AND '"+fhasta+"' " +
                " and f.mestado ='A' " +
                " AND m.cod_proveed = p.cod_proveed " +
                " and m.cod_sublinea = s.cod_sublinea " +
                " AND f.nrofact = d.nrofact " +
                " and f.ctipo_docum = d.ctipo_docum " +
                " AND f.ffactur  = d.ffactur " +
                " AND f.cod_empr = d.cod_empr " +
                " and d.cod_empr = m.cod_empr " +
                " and d.cod_merca = m.cod_merca " +
                " and f.mestado = 'A' " +
                " AND f.cod_empr = 2 " +
                " AND d.cod_empr = 2 "+
                " and s.cod_linea = l.cod_linea "
                + extras +
                " group by m.cod_proveed, p.xnombre " + 
                " ORDER BY m.cod_proveed ";
                break;
        }
        System.out.println(sql);
        //sql = "select * from mercaderias ";
        if (tipo.equals("VIST")){
            String usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
            Map param = new HashMap();
            param.put("sql", sql);
            param.put("fdesde", this.desde);
            param.put("fhasta", this.hasta);
            param.put("titulo", titulo);
            param.put("usuImprime", usuImprime);
            param.put("nombreRepo", reporte); 
            
            if (this.vendedor != null) param.put("vendedor", getEmpelado(this.vendedor).getXnombre());
            else param.put("vendedor", "TODAS");
            if (this.zonas != null) param.put("zona", getZona(this.zonas).getXdesc());
            else param.put("zona", "TODAS");
            if (this.linea != null) param.put("linea", getLinea(this.linea).getXdesc()); 
            else param.put("linea", "TODAS");
            
            rep.reporteLiContClientes(param, tipo, reporte);
        } else {
            List<Object[]> lista = new ArrayList<Object[]>();
            lista = excelFacade.listarParaExcel(sql);
            rep.exportarExcel(columnas, lista, reporte);
        }
    }
    
    private Zonas getZona(Zonas pk){
        return this.listaZonas.stream()
                .filter(zona -> zona.getZonasPK().getCodEmpr() == pk.getZonasPK().getCodEmpr() && 
                        zona.getZonasPK().getCodZona().equals(pk.getZonasPK().getCodZona()))
                .findAny().orElse(null);
    }
    
    private Empleados getEmpelado(Empleados pk){
        return this.listaVendedor.stream()
                .filter(empleado -> empleado.getEmpleadosPK().getCodEmpr() == pk.getEmpleadosPK().getCodEmpr() && 
                        empleado.getEmpleadosPK().getCodEmpleado() == pk.getEmpleadosPK().getCodEmpleado())
                .findAny().orElse(null);
    }
    
    private Lineas getLinea(Lineas pk){
        return this.listaLineas.stream()
                .filter(lin -> lin.getCodLinea().shortValue() == pk.getCodLinea().shortValue())
                .findAny().orElse(null);
    }
    
    private String dateToString(Date fecha) {
        String resultado = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            resultado = dateFormat.format(fecha);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }
        return resultado;
    }
    
    public List<Zonas> getListZonas(){
        return zonasFacade.findAll();
    }
    
    public List<Empleados> getListVendedores(){
        return empleadoFacade.getEmpleadosVendedoresActivosPorCodEmp(2);
    }
    
    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public Empleados getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    public String getNuevos() {
        return nuevos;
    }

    public void setNuevos(String nuevos) {
        this.nuevos = nuevos;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public List<Empleados> getListaVendedor() {
        return listaVendedor;
    }

    public void setListaVendedor(List<Empleados> listaVendedor) {
        this.listaVendedor = listaVendedor;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public List<Lineas> getListaLineas() {
        return listaLineas;
    }

    public void setListaLineas(List<Lineas> listaLineas) {
        this.listaLineas = listaLineas;
    }

    public Lineas getLinea() {
        return linea;
    }

    public void setLinea(Lineas linea) {
        this.linea = linea;
    }

    public Mercaderias getMercaderia() {
        return mercaderia;
    }

    public void setMercaderia(Mercaderias mercaderia) {
        this.mercaderia = mercaderia;
    }
    
    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }
    
    public List<Mercaderias> getListaMercaderias() {
        return listaMercaderias;
    }

    public void setListaMercaderias(List<Mercaderias> listaMercaderias) {
        this.listaMercaderias = listaMercaderias;
    }

    public List<Mercaderias> getListaMercaderiasSeleccionadas() {
        return listaMercaderiasSeleccionadas;
    }

    public void setListaMercaderiasSeleccionadas(List<Mercaderias> listaMercaderiasSeleccionadas) {
        this.listaMercaderiasSeleccionadas = listaMercaderiasSeleccionadas;
    }
    
    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public DefaultStreamedContent getDownload() throws Exception {
        System.out.println("GET = " + download.getName());
        return download;
    }

    public void prepDownload() throws Exception {
        System.out.println("PREP = " + download.getName());
    }
    
}
