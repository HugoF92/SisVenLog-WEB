package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.ZonasFacade;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.LlamarReportes;

/**
 *
 * @author Nico
 */
@ManagedBean
@SessionScoped
public class LiConCliBean implements Serializable{
    
    private Date desde;
    
    private Date hasta;
    
    private Zonas zonas;
    private Empleados vendedor;
    
    @EJB
    private ZonasFacade zonasFacade;
    @EJB
    private EmpleadosFacade vendedoresFacade;
    @EJB
    private ExcelFacade excelFacade;
        
    private String seleccion,nuevos;
    private Boolean nuevo;
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.seleccion = new String();
        this.vendedor = new Empleados(new EmpleadosPK());
        this.zonas = new Zonas(new ZonasPK());
        this.nuevo =  false;
        this.desde = new Date();
        this.hasta = new Date();
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
        return vendedoresFacade.getEmpleadosVendedoresActivosPorCodEmp(2);
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

}
