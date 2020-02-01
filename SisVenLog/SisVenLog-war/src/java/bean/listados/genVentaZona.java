package bean.listados;

import bean.CanalesBean;
import dao.CanalesVentaFacade;
import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.RutasFacade;
import dao.ZonasFacade;
import entidad.CanalesVenta;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.Rutas;
import entidad.RutasPK;
import entidad.Zonas;
import entidad.ZonasPK;
import java.math.BigDecimal;
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
public class genVentaZona {
    
    private Date desde;
    
    private Date hasta;
    
    private Zonas zonas;
    private CanalesVenta canales;
    private Rutas rutas;
    
    @EJB
    private ZonasFacade zonasFacade;
    @EJB
    private CanalesVentaFacade canalesFacade;
    @EJB
    private RutasFacade rutasFacade;
    @EJB
    private ExcelFacade excelFacade;
        
    private String totales;
    private Boolean sinIva;
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.totales = new String();
        this.sinIva = false;
        this.zonas = new Zonas(new ZonasPK());
        this.canales = new CanalesVenta();
        this.rutas = new Rutas(new RutasPK());
        this.desde = new Date();
        this.hasta = new Date();
    }
    
    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();
        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String sinIVA = "" , ruta = "" ,zona = "" , totales = "" , canal = "" , groupTotal = "";
        if(this.totales.equals("1")){
            totales += "  s.xdesc as xdesc_sub,a.cod_vendedor as cod_vend , e.xnombre as xnomb_vend ";
            groupTotal = " GROUP BY a.cod_zona, a.cod_merca, s.xdesc,a.cod_vendedor,e.xnombre, ";
        }else{
            totales += " s.xdesc as xdesc_sub ";
            groupTotal = " GROUP BY a.cod_vendedor, e.xnombre, a.cod_zona, a.cod_merca,s.xdesc, ";
        }
        if(this.sinIva){
            sinIVA = " SUM(a.igravadas+a.iexentas) as itotal, ";
        }else {
            sinIVA = " SUM(a.igravadas+a.iexentas+a.impuestos) as itotal, ";
        }
        if(this.rutas!= null){
            ruta = " AND a.cod_ruta = "+this.rutas.getRutasPK().getCodRuta()+" ";
        }
        if (this.zonas != null){
            zona = " AND a.cod_zona = '"+this.zonas.getZonasPK().getCodZona()+"' ";
        }
        if (this.canales != null){
            canal = " AND c.cod_canal = '"+this.canales.getCodCanal()+"' ";
        }
        List<Object[]> ppp = new ArrayList<Object[]>();
        String sql = " SELECT * FROM ppp WHERE cod_empr = 2 AND fppp = (SELECT MAX(fppp) FROM ppp WHERE cod_empr = 2 AND fppp <= '"+fhasta
                    +"') order by cod_merca ";
        List<Object[]> vtaCur = new ArrayList<Object[]>();
        String lsente = " SELECT b.nprecio_ppp as costot,a.cod_zona,a.cod_merca,b.nrelacion,ABS(SUM(a.cant_cajas)) as cancaja,ABS(SUM(a.cant_unid)) as canunid, "
            +" pr.xnombre as xnomb_prov, b.xdesc, " +
            " ISNULL(b.cod_proveed,0) as cod_proveed, "
            +" b.npeso_caja,  b.npeso_unidad as npeso_unid, ISNULL(b.cod_sublinea,0) as " +
            " cod_sublin,v.xdesc as xdesc_div, v.cod_division , l.xdesc as xdesc_lin, " +
            " g.xdesc as xdesc_cat,case when a.cod_zona = 'B' then 'Deposito' else 'Zona' end as origen, "
            +" '"+fdesde+"' as finicial, '"+fhasta+"' as ffinal,((ABS(SUM(a.cant_cajas)) * b.npeso_caja)+(ABS(SUM(a.cant_unid))*b.npeso_unidad))/1000 as npesoton, "
            +" (((ABS(SUM(a.cant_cajas)) * b.nrelacion)+ABS(SUM(a.cant_unid)))*b.npeso_unidad) as npesokil, "+sinIVA+totales
            +" FROM movimientos_merca a,mercaderias b, merca_canales c, rutas r, zonas z, empleados e, " +
            " sublineas s, lineas l, categorias g, divisiones v, proveedores pr " +
            " WHERE fmovim BETWEEN '"+fdesde+"' AND '"+fhasta+"' "+
            " AND ctipo_docum IN ('FCO','FCR','CPV','NCV','NDV') AND a.cod_merca = b.cod_merca "+
            " AND pr.cod_proveed = b.cod_proveed AND a.cod_ruta = r.cod_ruta "+
            " AND b.cod_sublinea = s.cod_sublinea AND s.cod_linea = l.cod_linea "+
            " AND l.cod_categoria = g.cod_categoria AND g.cod_division = v.cod_division "+
            " AND a.cod_vendedor = e.cod_empleado AND a.cod_zona = z.cod_zona "+
            " AND a.cod_empr = 2 AND a.cod_empr = b.cod_empr AND b.cod_merca = c.cod_merca "
            +ruta+zona+canal+groupTotal+
            " b.xdesc, b.cod_proveed, b.nrelacion,b.nprecio_ppp, b.npeso_caja, b.npeso_unidad, "
            +" b.cod_sublinea, l.xdesc, g.xdesc,v.xdesc, v.cod_division, pr.xnombre ";
        ppp = excelFacade.listarParaExcel(sql);
        vtaCur = excelFacade.listarParaExcel(lsente);
        for(Object[] row : ppp){
            for(Object[] row2 : vtaCur){
                if(row[1].equals(row2[2])){
                    if(row[3]==null){
                        row2[0] = BigDecimal.ZERO;
                    }else{
                        row2[0] = row[3];
                    }
                }
            }
        }
        for(Object[] row : vtaCur){
            BigDecimal canCaja = (BigDecimal) row[4];
            BigDecimal canUnid = (BigDecimal) row[5];
            BigDecimal nrelacion = (BigDecimal) row[3];
            canCaja.add((canUnid.divide(nrelacion)));
            canUnid = canUnid.remainder(nrelacion);   
        }
        String[] columnas = null;
        if(this.totales.equals("1")){
            columnas = new String[25];
            columnas[23] = "cod_vend";
            columnas[24] = "xnomb_vend";
        }else{
            columnas = new String[23];
        }
        columnas[0] = "costot";
        columnas[1] = "cod_zona";
        columnas[2] = "cod_merca";                
        columnas[3] = "nrelacion";
        columnas[4] = "cancaja";
        columnas[5] = "canunid";
        columnas[6] = "xnomb_prov";
        columnas[7] = "xdesc";
        columnas[8] = "cod_proveed";
        columnas[9] = "npeso_caja";
        columnas[10] = "npeso_unidad";
        columnas[11] = "cod_sublin";
        columnas[12] = "xdesc_div";
        columnas[13] = "cod_division";
        columnas[14] = "xdesc_lin";
        columnas[15] = "xdesc_cat";
        columnas[16] = "origen";
        columnas[17] = "finicial";
        columnas[18] = "ffinal";
        columnas[19] = "npesoton";
        columnas[20] = "npesokil";
        columnas[21] = "itotal";
        columnas[22] = "xdesc_sub";   
        List<Object[]> lcsv = new ArrayList<Object[]>();
        lcsv.add(columnas);
        lcsv.addAll(vtaCur);
        if(this.totales.equals("1")){
            rep.exportarCSV(lcsv,"vgeneral");
        }else{
            rep.exportarCSV(lcsv,"pdos");
        }
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

    public CanalesVenta getCanales() {
        return canales;
    }

    public void setCanales(CanalesVenta canales) {
        this.canales = canales;
    }

    public Rutas getRutas() {
        return rutas;
    }

    public void setRutas(Rutas rutas) {
        this.rutas = rutas;
    }

    public List<CanalesVenta> getListCanales(){
        return this.canalesFacade.findAll();
    }

    public List<Rutas> getListRutas(){
        return this.rutasFacade.findAll();
    }    

    public Boolean getSinIva() {
        return sinIva;
    }

    public void setSinIva(Boolean sinIva) {
        this.sinIva = sinIva;
    }

    public String getTotales() {
        return totales;
    }

    public void setTotales(String totales) {
        this.totales = totales;
    }
     
}
