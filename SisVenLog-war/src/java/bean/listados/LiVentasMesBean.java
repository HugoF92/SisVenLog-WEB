/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.CanalesVentaFacade;
import dao.CiudadesFacade;
import dao.DivisionesFacade;
import dao.EmpleadosFacade;
import dao.LiVentasFacade;
import dao.LineasFacade;
import dao.ProveedoresFacade;
import dao.RutasFacade;
import dao.SublineasFacade;
import dao.TiposClientesFacade;
import dao.TiposVentasFacade;
import dao.ZonasFacade;
import dto.LiVentas;
import entidad.CanalesVenta;
import entidad.Ciudades;
import entidad.Divisiones;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Proveedores;
import entidad.Rutas;
import entidad.Sublineas;
import entidad.TiposClientes;
import entidad.TiposVentas;
import entidad.Zonas;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
/**
 *
 * @author Clara
 */
@ManagedBean
@SessionScoped
public class LiVentasMesBean {
    private String SQL_BASE_VENTAS = " SELECT   f.cod_ruta, f.cod_zona, MONTH(f.ffactur) AS nmes, f.cod_cliente,  f.xrazon_social, SUM(d.itotal) AS iventas, f.cod_vendedor, e.xnombre, " +
        " z.xdesc as xdesc_zona , r.xdesc as xdesc_ruta " +
        " FROM   facturas f INNER JOIN " +
        " empleados e ON f.cod_vendedor = e.cod_empleado, zonas z , rutas r, facturas_det d, mercaderias m, sublineas s, lineas l, divisiones v, categorias g, clientes c " +
        " WHERE    f.cod_empr = 2 AND e.cod_empr = 2 AND  (f.ffactur BETWEEN :l_finicial AND :l_ffinal) AND (f.mestado = 'A') " +
        " AND f.cod_zona = z.cod_zona " +
        " AND f.cod_ruta = r.cod_ruta " + 
        " AND f.nrofact = d.nrofact " +
        " AND f.ffactur = d.ffactur  " +
        " AND f.cod_empr = d.cod_empr " +
        " AND d.cod_merca = m.cod_merca " +
        " AND m.cod_sublinea = s.cod_sublinea " +
        " AND s.cod_linea = l.cod_linea " +
        " AND l.cod_categoria = g.cod_categoria " +
        " AND g.cod_division = v.cod_division " +
        " AND f.cod_cliente = c.cod_cliente " +
        " AND d.cod_empr = 2 " +
        " AND f.mestado = 'A' ";
    /*-----------------vendedor---------------------------------*/    
    private static final String SQL_GROUP_BY_VENDEDOR = " GROUP BY MONTH(f.ffactur), f.cod_cliente, f.xrazon_social, f.cod_vendedor, e.xnombre ";    
    private static final String SQL_NOTAS_GROUP_BY_VENDEDOR = " GROUP BY MONTH(n.fdocum), f.cod_cliente, f.xrazon_social, f.cod_vendedor, e.xnombre, n.ctipo_docum ";    
    /*--------------------------------------------*/
    private static final String SQL_GROUP_BY_ZONA = "  GROUP BY f.cod_zona, MONTH(f.ffactur), f.cod_cliente, f.xrazon_social, z.xdesc, z.cod_empr ";
    private static final String SQL_NOTAS_GROUP_BY_ZONA = "   GROUP BY f.cod_zona, MONTH(n.fdocum), f.cod_cliente, f.xrazon_social, z.xdesc, n.ctipo_docum, z.cod_empr ";
    /*--------------------------------------------*/
    private static final String SQL_GROUP_BY_RUTA = " GROUP BY MONTH(f.ffactur), f.cod_cliente, f.xrazon_social, f.cod_ruta, r.xdesc ";
    private static final String SQL_NOTAS_GROUP_BY_RUTA = "  GROUP BY MONTH(n.fdocum), f.cod_cliente, f.xrazon_social, f.cod_ruta, r.xdesc, n.ctipo_docum ";
    /*------------------------------------------------------------------------------------ */
    private String SQL_BASE_NOTAS_CREDITO = " SELECT     f.cod_ruta, f.cod_zona, MONTH(n.fdocum) AS nmes, f.cod_cliente, " +
        " f.xrazon_social, SUM(d.igravadas+d.iexentas) AS ttotal, f.cod_vendedor, e.xnombre, z.xdesc as xdesc_zona , r.xdesc as xdesc_ruta, n.ctipo_docum " +
        " FROM         notas_ventas n , empleados e, zonas z , rutas r, facturas f, notas_ventas_det d, mercaderias m, sublineas s, lineas l, categorias g, divisiones v, clientes c " +
        " WHERE    f.cod_empr = 2 AND e.cod_empr = 2 AND  (n.fdocum BETWEEN :l_finicial AND :l_ffinal) AND (f.mestado = 'A') " +
        " AND f.cod_zona = z.cod_zona " +
        " AND n.nro_nota = d.nro_nota " +
        " AND d.cod_empr = 2 " +
        " AND n.cod_empr = 2 " +
        " AND d.cod_merca = m.cod_merca " +
        " AND f.cod_ruta = r.cod_ruta " +
        " AND n.nrofact = f.nrofact " +
        " AND n.ffactur = f.ffactur " +
        " AND n.fdocum = d.fdocum " +
        " AND n.ctipo_docum = 'NCV' " +
        " AND m.cod_sublinea = s.cod_sublinea " +
        " AND s.cod_linea = l.cod_linea " +
        " AND l.cod_categoria = g.cod_categoria " +
        " AND g.cod_division = v.cod_division " +
        " AND f.cod_vendedor = e.cod_empleado " +
        " AND f.cod_cliente = c.cod_cliente " +
        " AND n.mestado = 'A' " +
        " AND n.fac_ctipo_docum = f.ctipo_docum ";
    //valores iniciales para las fechas del reporte
    private Date desde;
    private Date hasta;
    //Rutas
    private Rutas ruta;
    private List<Rutas> rutas;
    //Canal venta
    private CanalesVenta canalVenta;
    private List<CanalesVenta> canalesVenta; 
    //zona
    private Zonas zona;
    private List<Zonas> zonas;
    //Proveedor
    private Proveedores proveedor;
    private List<Proveedores> proveedores;
    //sublinea
    private Sublineas sublinea;
    private List<Sublineas> sublineas; 
    //linea
    private Lineas linea;
    private List<Lineas> lineas;
    //Division
    private Divisiones division;
    private List<Divisiones> divisiones;
    //Tipo Cliente
    private TiposClientes tipoCliente;
    private List<TiposClientes> tipoClientes;
    //Ciudad Cliente
    private Ciudades ciudadCliente;
    private List<Ciudades> ciudadClientes;
    //TipoVenta
    private TiposVentas tipoVenta;
    private List<TiposVentas> tiposVentas;  
    //TipoReporte
    private String tipoReporte;
    private boolean notaDev;
    //Vendedores
    private Empleados vendedor;
    private List<Empleados> vendedores; 
    
    @EJB
    private RutasFacade rutasFacade;
    @EJB
    private CanalesVentaFacade canalesVentaFacade;
    @EJB
    private ZonasFacade zonaFacade;
    @EJB
    private ProveedoresFacade proveedoresFacade;
    @EJB
    private SublineasFacade subLineasFacade;
    @EJB
    private LineasFacade lineasFacade;
    @EJB
    private DivisionesFacade divisionesFacade;
    @EJB
    private TiposClientesFacade tiposClientesFacade;
    @EJB
    private CiudadesFacade ciudadFacade;
    @EJB
    private TiposVentasFacade tiposVentasFacade;
    @EJB
    private EmpleadosFacade empleadosFacade;
    @EJB
    private LiVentasFacade ventasFacade;
    
    
    //valores
    private Short rutaSelected = 0;
    private String zonaSelected = "0";
    private String canalVentaSelected = "0";
    private Short proveedorSelected = 0;
    private Short subLineaSelected =  0;
    private Short lineaSelected =  0;
    private Short divisionSelected =  0;
    private String tipoClienteSelected = "0";
    private Short ciudadClienteSelected = 0;
    private String tipoVentaSelected = "0";
    private Short vendedorSelected = 0;
    
    @PostConstruct
    public void instanciar() {
        //Rango de fechas del mes actual
        this.desde = new Date();
        this.hasta = new Date();
        this.ruta = new Rutas();
        this.rutas = new ArrayList<Rutas>();
        this.canalVenta = new CanalesVenta();
        this.canalesVenta = new ArrayList<CanalesVenta>();
        this.zona = new Zonas();
        this.zonas = new ArrayList<Zonas>();
        this.proveedor = new Proveedores(); 
        this.proveedores = new ArrayList<Proveedores>();
        this.sublinea = new Sublineas();
        this.sublineas = new ArrayList<Sublineas>();
        this.linea = new Lineas(); 
        this.lineas = new ArrayList<Lineas>();
        this.division = new Divisiones(); 
        this.divisiones = new ArrayList<Divisiones>();
        this.tipoCliente = new TiposClientes(); 
        this.tipoClientes = new ArrayList<TiposClientes>();
        this.ciudadCliente = new Ciudades();
        this.ciudadClientes = new ArrayList<Ciudades>();
        this.tipoVenta = new TiposVentas();
        this.tiposVentas = new ArrayList<TiposVentas>();
        this.notaDev = false;
        this.vendedor = new Empleados();
        this.vendedores = new ArrayList<Empleados>();
        this.rutaSelected = 0;
        this.zonaSelected = "0";
        this.canalVentaSelected = "0";
        this.proveedorSelected = 0;
        this.subLineaSelected =  0;
        this.lineaSelected =  0;
        this.divisionSelected =  0;
        this.tipoClienteSelected = "0";
        this.ciudadClienteSelected = 0;
        this.tipoVentaSelected = "0";
        this.vendedorSelected = 0;
    }

    public String getZonaSelected() {
        for(Zonas z : this.zonas){
            if(z.getCodZona().equalsIgnoreCase(this.zonaSelected)){
                this.zona = z;
            }
        }
        return zonaSelected;
    }

    public void setZonaSelected(String zonaSelected) {
        this.zonaSelected = zonaSelected;
    }
    
    public List<Rutas> listarRutas(){ 
        this.rutas = rutasFacade.findAll();
        return this.rutas;
    }

    public void setCanalVentaSelected(String canalVentaSelected) {
        this.canalVentaSelected = canalVentaSelected;
    }

    public void setProveedorSelected(Short proveedorSelected) {
        this.proveedorSelected = proveedorSelected;
    }

    public void setSubLineaSelected(Short subLineaSelected) {
        this.subLineaSelected = subLineaSelected;
    }

    public void setLineaSelected(Short lineaSelected) {
        this.lineaSelected = lineaSelected;
    }

    public void setDivisionSelected(Short divisionSelected) {
        this.divisionSelected = divisionSelected;
    }

    public void setTipoClienteSelected(String tipoClienteSelected) {
        this.tipoClienteSelected = tipoClienteSelected;
    }

    public void setCiudadClienteSelected(Short ciudadClienteSelected) {
        this.ciudadClienteSelected = ciudadClienteSelected;
    }

    public void setTipoVentaSelected(String tipoVentaSelected) {
        this.tipoVentaSelected = tipoVentaSelected;
    }

    
    
    public String getCanalVentaSelected() {
        for(CanalesVenta cv : this.canalesVenta){
            if(cv.getCodCanal().equalsIgnoreCase(this.canalVentaSelected)){
                this.canalVenta = cv;
            }
        }
        return canalVentaSelected;
    }

    public Short getProveedorSelected() {
        for(Proveedores cv : this.proveedores){
            if(cv.getCodProveed().compareTo(this.proveedorSelected)==0){
                this.proveedor = cv;
            }
        }
        return proveedorSelected;
    }

    public Short getSubLineaSelected() {
        for(Sublineas cv : this.sublineas){
            if(cv.getCodSublinea().compareTo(this.subLineaSelected)==0){
                this.sublinea = cv;
            }
        }
        return subLineaSelected;
    }

    public Short getLineaSelected() {
        for(Lineas cv : this.lineas){
            if(cv.getCodLinea().compareTo(this.lineaSelected)==0){
                this.linea = cv;
            }
        }
        return lineaSelected;
    }

    public Short getDivisionSelected() {
        for(Divisiones cv : this.divisiones){
            if(cv.getCodDivision().compareTo(this.divisionSelected)==0){
                this.division = cv;
            }
        }
        return divisionSelected;
    }

    public String getTipoClienteSelected() {
        for(TiposClientes cv : this.tipoClientes){
            if(cv.getCtipoCliente().equalsIgnoreCase(this.tipoClienteSelected)){
                this.tipoCliente = cv;
            }
        }
        return tipoClienteSelected;
    }

    public Short getCiudadClienteSelected() {
        for(Ciudades cv : this.ciudadClientes){
            if(cv.getCodCiudad().compareTo(this.ciudadClienteSelected)==0){
                this.ciudadCliente = cv;
            }
        }
        return ciudadClienteSelected;
    }

    public String getTipoVentaSelected() {
        for(TiposVentas cv : this.tiposVentas){
            if(cv.getCtipoVta().equalsIgnoreCase(this.tipoVentaSelected)){
                this.tipoVenta = cv;
            }
        }
        return tipoVentaSelected;
    }

    
    public Short getRutaSelected() {
        for(Rutas ruta :  this.rutas){
            if(this.rutaSelected.compareTo(ruta.getCodRuta())==0){
                this.ruta = ruta;
            }
        }
        return rutaSelected;
    }

    public void setRutaSelected(Short rutaSelected) {
        this.rutaSelected = rutaSelected;
    }
    
    public List<CanalesVenta> listarCanalesVenta(){ 
        this.canalesVenta =  this.canalesVentaFacade.findAll();
        return this.canalesVenta;
    }
    
    public List<Zonas> listarZonas(){
        this.zonas = this.zonaFacade.findAll();
        this.zonas.stream().sorted();
        Collections.sort(this.zonas, (Zonas z1, Zonas z2) -> z1.getXdesc().compareTo(z2.getXdesc()) );        
        return this.zonas;
    }
     
    public List<Proveedores> listarProveedores(){
        this.proveedores = this.proveedoresFacade.listarPrveedores();
        Collections.sort(this.proveedores, (Proveedores p1, Proveedores p2) -> p1.getXnombre().compareTo(p2.getXnombre()) );
        return this.proveedores;
    }
    
    public List<Sublineas> listarSubLineas(){
        this.sublineas = this.subLineasFacade.listarSublineasActivas();
        return this.sublineas;
    }
    
    public List<Lineas> listarLineas(){
        this.lineas = this.lineasFacade.listarLineasActivas();
        return this.lineas;
    }
    
    public List<Divisiones> listarDivisiones(){
        this.divisiones = divisionesFacade.listarDivisionesActivas();
        return this.divisiones;
    }
    
    public List<TiposClientes> listarTipoClientes(){
        this.tipoClientes = this.tiposClientesFacade.findAll();
        Collections.sort(this.tipoClientes, (TiposClientes tc1, TiposClientes tc2) -> tc1.getXdesc().compareTo(tc2.getXdesc()) );
        return this.tipoClientes;
    }
    
    public List<Ciudades> listarCiudadClientes(){
        this.ciudadClientes = this.ciudadFacade.findAll();
        return this.ciudadClientes;
    }
    
    public List<TiposVentas> listarTipoVenta(){
        this.tiposVentas = this.tiposVentasFacade.findAll();
        return this.tiposVentas;
    }
    
    

    public Short getVendedorSelected() {        
        for(Empleados e: this.vendedores){
            if(e.getCodEmpleado().compareTo(this.vendedorSelected) == 0){
                this.vendedor = e;    
                System.out.println("getvendedorselected" + e.getCodEmpleado() + e.getXnombre());
                
            }
        }
        return vendedorSelected;
    }

    public void setVendedorSelected(Short vendedorSelected) {
        this.vendedorSelected = vendedorSelected;
    }
    
    
    public List<Empleados> listarVendedores(){ 
        this.vendedores =  this.empleadosFacade.getEmpleadosVendedoresActivosPorCodEmp(2);  
        return this.vendedores;
    }
    
    public void generarArchivo(String tipoArchivo){
        List<String> prestamo = new ArrayList<String>();
	List<Map<String, ?>> data;
        Map<String, Object> datos = new HashMap<String, Object>();;
        DateFormat formatoFecha =new SimpleDateFormat("dd/MM/yyyy");
        HashMap parameters = new HashMap();
	List<Map<String, ?>> cabecera = new ArrayList<Map<String, ?>>();   
        Map<String, Object> detalleVendedores = new HashMap<String, Object>();
        if(this.desde != null && this.hasta != null){            
            //
            String consultaVentaVendedor = generarConsultaAgrupacion("VENDEDOR", "VENTA");
            //System.out.println(String.format("CONSULTA VENTA VENDEDOR: [%s]", consultaVentaVendedor));
            String consultaNotaVendedor = generarConsultaAgrupacion("VENDEDOR", "NOTAS");
            //System.out.println(String.format("CONSULTA NOTA VENDEDOR: [%s]", consultaNotaVendedor));
            //
            String consultaVentaZona = generarConsultaAgrupacion("ZONA", "VENTA");
            //System.out.println(String.format("CONSULTA VENTA ZONA: [%s]", consultaVentaZona));
            String consultaNotaZona = generarConsultaAgrupacion("ZONA", "NOTAS");
            //System.out.println(String.format("CONSULTA NOTA ZONA: [%s]", consultaNotaZona));
            //
            String consultaVentaRuta = generarConsultaAgrupacion("RUTA", "VENTA");
            //System.out.println(String.format("CONSULTA VENTA RUTA: [%s]", consultaVentaRuta));
            String consultaNotaRuta = generarConsultaAgrupacion("RUTA", "NOTAS");
            //System.out.println(String.format("CONSULTA NOTA RUTA: [%s]", consultaNotaRuta));
            //realizar la llamada al Facade para obtener los datos del reporte 
            Map<String, Map<String, List<LiVentas>>> ventasVendedor = ventasFacade.obtenerConsultaVendedores(consultaVentaVendedor, 
                    consultaNotaVendedor, consultaVentaZona, consultaNotaZona, consultaVentaRuta, consultaNotaRuta);
            //System.out.println("bean.listados.LiVentasMesBean.obtenerConsultaVendedores()");
            //System.out.println(ventasVendedor.toString());
            String TITULO = "VENTAS A CLIENTES POR MES";
            String NOMBRE_REPORTE = "rventas_mes";
            parameters.put("fechaDesde", this.desde);
            parameters.put("fechaHasta", this.hasta);
            parameters.put("titulo", TITULO);
            parameters.put("nombreRepo", NOMBRE_REPORTE);
            parameters.put("usu_imprime", "admin");
            short comparar=0;
            if(this.canalVentaSelected.equalsIgnoreCase("0")){
                 parameters.put("txtCanalVta", "TODOS");
            }else{
                parameters.put("txtCanalVta", this.canalVenta.getCodCanal() + " - " + this.canalVenta.getXdesc());
            }
            if(this.vendedorSelected.compareTo(comparar) == 0){
                parameters.put("txtVendedor", "TODOS");
            }else{
                parameters.put("txtVendedor", this.vendedor.getCodEmpleado() + " - " + this.vendedor.getXnombre());
            }
            
            if(this.zonaSelected.equalsIgnoreCase("0")){
                parameters.put("txtZona", "TODOS");
            }else{
                parameters.put("txtZona", this.zona.getCodZona() + " - " + this.zona.getXdesc());
            }
            if(this.subLineaSelected.compareTo(comparar) == 0){
                parameters.put("txtSubLinea", "TODOS");
            }else{
                parameters.put("txtSubLinea", this.sublinea.getCodSublinea() + " - " + this.sublinea.getXdesc());
            }
            if(this.proveedorSelected.compareTo(comparar) == 0){
                parameters.put("txtProveedor", "TODOS");
            }else{
                parameters.put("txtProveedor", this.proveedor.getCodProveed() + " - " + this.proveedor.getXnombre());
            }
            if(this.lineaSelected.compareTo(comparar) == 0){
                parameters.put("txtLinea", "TODOS");
            }else{
                parameters.put("txtLinea", this.linea.getCodLinea() + " - " + this.linea.getXdesc());
            }             
            if(this.divisionSelected.compareTo(comparar) == 0){
                parameters.put("txtDivision", "TODOS");
            }else{
                parameters.put("txtDivision", this.division.getCodDivision() + " - " + this.division.getXdesc());
            }  
            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/liVentasMesVendedoresDet.jasper");
            parameters.put("SUBREPORT_DIR", report);            
            //-------agregar total del reporte
            Map<String, List<LiVentas>> totalVenta = ventasVendedor.get("TOTAL_VENTA");
            for(Map.Entry<String, List<LiVentas>> vendedor : totalVenta.entrySet()){
                if(vendedor.getKey()=="13"){
                    LiVentas vt = vendedor.getValue().get(0);
                    parameters.put("sumTotalEne", vt.getMontoPorMes().get(1).longValue());
                    parameters.put("sumTotalFeb", vt.getMontoPorMes().get(2).longValue());
                    parameters.put("sumTotalMar", vt.getMontoPorMes().get(3).longValue());
                    parameters.put("sumTotalAbr", vt.getMontoPorMes().get(4).longValue());
                    parameters.put("sumTotalMay", vt.getMontoPorMes().get(5).longValue());
                    parameters.put("sumTotalJun", vt.getMontoPorMes().get(6).longValue());
                    parameters.put("sumTotalJul", vt.getMontoPorMes().get(7).longValue());
                    parameters.put("sumTotalAgo", vt.getMontoPorMes().get(8).longValue());
                    parameters.put("sumTotalSep", vt.getMontoPorMes().get(9).longValue());
                    parameters.put("sumTotalOct", vt.getMontoPorMes().get(10).longValue());
                    parameters.put("sumTotalNov", vt.getMontoPorMes().get(11).longValue());
                    parameters.put("sumTotalDic", vt.getMontoPorMes().get(12).longValue());
                    parameters.put("sumTotalVenta", vt.getMontoPorMes().get(1).longValue()
                                +vt.getMontoPorMes().get(2).longValue()+vt.getMontoPorMes().get(3).longValue()
                                +vt.getMontoPorMes().get(4).longValue()+vt.getMontoPorMes().get(5).longValue()
                                +vt.getMontoPorMes().get(6).longValue()+vt.getMontoPorMes().get(7).longValue()
                                +vt.getMontoPorMes().get(8).longValue()+vt.getMontoPorMes().get(9).longValue()
                                +vt.getMontoPorMes().get(10).longValue()+vt.getMontoPorMes().get(11).longValue()
                                +vt.getMontoPorMes().get(12).longValue());
                }
            }
            try{
                Map<String, List<LiVentas>> vendedores = ventasVendedor.get("VENDEDOR");
                for(Map.Entry<String, List<LiVentas>> vendedor : vendedores.entrySet()){
                    data = new ArrayList<Map<String, ?>>();
                    detalleVendedores = new HashMap<String, Object>();
                    //datos.put("txtNombreVendedor", vendedor.getValue().get(0).getDescripcion());
                    Map<Integer, List<LiVentas>> collect = vendedor.getValue().stream().collect(Collectors.groupingBy(LiVentas::getCodCliente));
                    for (Map.Entry<Integer, List<LiVentas>> agrupPorCliente : collect.entrySet()) {
                        Long sumEne = 0L;
                        Long sumFeb = 0L;
                        Long sumMar = 0L;
                        Long sumAbr = 0L;
                        Long sumMay = 0L;
                        Long sumJun = 0L;
                        Long sumJul = 0L;
                        Long sumAgo = 0L;
                        Long sumSep = 0L;
                        Long sumOct = 0L;
                        Long sumNov = 0L;
                        Long sumDic = 0L;

                        for(LiVentas venta : agrupPorCliente.getValue()){
                            switch (venta.getNmes()) {
                                case 1:
                                    sumEne = sumEne + venta.getMonto().longValue();
                                    break;
                                case 2:
                                    sumFeb = sumFeb + venta.getMonto().longValue();
                                    break;
                                case 3:
                                    sumMar = sumMar + venta.getMonto().longValue();
                                    break;
                                case 4:
                                    sumAbr = sumAbr + venta.getMonto().longValue();
                                    break;
                                case 5:
                                    sumMay = sumMay + venta.getMonto().longValue();
                                    break;
                                case 6:
                                    sumJun = sumJun + venta.getMonto().longValue();
                                    break;
                                case 7:
                                    sumJul = sumJul + venta.getMonto().longValue();
                                    break;
                                case 8:
                                    sumAgo = sumAgo + venta.getMonto().longValue();
                                    break;
                                case 9:
                                    sumSep = sumSep + venta.getMonto().longValue();
                                    break;
                                case 10:
                                    sumOct = sumOct + venta.getMonto().longValue();
                                    break;
                                case 11:
                                    sumNov = sumNov + venta.getMonto().longValue();
                                    break;
                                default:
                                    sumDic = sumDic + venta.getMonto().longValue();
                                    break;
                            }
                        }
                        datos = new HashMap<String, Object>();
                        datos.put("textCliente", agrupPorCliente.getValue().get(0).getRazonSocialCliente());
                        datos.put("sumEne", sumEne);
                        datos.put("sumFeb", sumFeb);
                        datos.put("sumMar", sumMar);
                        datos.put("sumAbr", sumAbr);
                        datos.put("sumMay", sumMay);
                        datos.put("sumJun", sumJun);
                        datos.put("sumJul", sumJul);
                        datos.put("sumAgo", sumAgo);
                        datos.put("sumSep", sumSep);
                        datos.put("sumOct", sumOct);
                        datos.put("sumNov", sumNov);
                        datos.put("sumDic", sumDic);
                        datos.put("sumTotal", sumEne+sumFeb+sumMar+sumAbr+sumMay+sumJun+sumJul+sumAgo+sumSep+sumOct+sumNov+sumDic);
                        data.add(datos);
                    }
                    detalleVendedores.put("codAgrup", "VENDEDOR");
                    detalleVendedores.put("txtCodVendedor", vendedor.getKey());
                    detalleVendedores.put("txtNombreVendedor", vendedor.getValue().get(0).getDescripcion());
                    detalleVendedores.put("detalleVendedor", new JRMapCollectionDataSource(data));
                    cabecera.add(detalleVendedores);
                }
                
                Map<String, List<LiVentas>> rutas = ventasVendedor.get("RUTA");
                for(Map.Entry<String, List<LiVentas>> vendedor : rutas.entrySet()){
                    data = new ArrayList<Map<String, ?>>();
                    detalleVendedores = new HashMap<String, Object>();
                    //datos.put("txtNombreVendedor", vendedor.getValue().get(0).getDescripcion());
                    Map<Integer, List<LiVentas>> collect = vendedor.getValue().stream().collect(Collectors.groupingBy(LiVentas::getCodCliente));
                    for (Map.Entry<Integer, List<LiVentas>> agrupPorCliente : collect.entrySet()) {
                        Long sumEne = 0L;
                        Long sumFeb = 0L;
                        Long sumMar = 0L;
                        Long sumAbr = 0L;
                        Long sumMay = 0L;
                        Long sumJun = 0L;
                        Long sumJul = 0L;
                        Long sumAgo = 0L;
                        Long sumSep = 0L;
                        Long sumOct = 0L;
                        Long sumNov = 0L;
                        Long sumDic = 0L;

                        for(LiVentas venta : agrupPorCliente.getValue()){
                            switch (venta.getNmes()) {
                                case 1:
                                    sumEne = sumEne + venta.getMonto().longValue();
                                    break;
                                case 2:
                                    sumFeb = sumFeb + venta.getMonto().longValue();
                                    break;
                                case 3:
                                    sumMar = sumMar + venta.getMonto().longValue();
                                    break;
                                case 4:
                                    sumAbr = sumAbr + venta.getMonto().longValue();
                                    break;
                                case 5:
                                    sumMay = sumMay + venta.getMonto().longValue();
                                    break;
                                case 6:
                                    sumJun = sumJun + venta.getMonto().longValue();
                                    break;
                                case 7:
                                    sumJul = sumJul + venta.getMonto().longValue();
                                    break;
                                case 8:
                                    sumAgo = sumAgo + venta.getMonto().longValue();
                                    break;
                                case 9:
                                    sumSep = sumSep + venta.getMonto().longValue();
                                    break;
                                case 10:
                                    sumOct = sumOct + venta.getMonto().longValue();
                                    break;
                                case 11:
                                    sumNov = sumNov + venta.getMonto().longValue();
                                    break;
                                default:
                                    sumDic = sumDic + venta.getMonto().longValue();
                                    break;
                            }
                        }
                        datos = new HashMap<String, Object>();
                        datos.put("textCliente", agrupPorCliente.getValue().get(0).getRazonSocialCliente());
                        datos.put("sumEne", sumEne);
                        datos.put("sumFeb", sumFeb);
                        datos.put("sumMar", sumMar);
                        datos.put("sumAbr", sumAbr);
                        datos.put("sumMay", sumMay);
                        datos.put("sumJun", sumJun);
                        datos.put("sumJul", sumJul);
                        datos.put("sumAgo", sumAgo);
                        datos.put("sumSep", sumSep);
                        datos.put("sumOct", sumOct);
                        datos.put("sumNov", sumNov);
                        datos.put("sumDic", sumDic);
                        datos.put("sumTotal", sumEne+sumFeb+sumMar+sumAbr+sumMay+sumJun+sumJul+sumAgo+sumSep+sumOct+sumNov+sumDic);
                        data.add(datos);
                    }
                    detalleVendedores.put("codAgrup", "RUTA");
                    detalleVendedores.put("txtCodVendedor", vendedor.getKey());
                    detalleVendedores.put("txtNombreVendedor", vendedor.getValue().get(0).getDescripcion());
                    detalleVendedores.put("detalleVendedor", new JRMapCollectionDataSource(data));
                    cabecera.add(detalleVendedores);
                }
                //---------------------------ZONA---------------------------------------------------------------
                Map<String, List<LiVentas>> zonas = ventasVendedor.get("ZONA");
                for(Map.Entry<String, List<LiVentas>> vendedor : zonas.entrySet()){
                    data = new ArrayList<Map<String, ?>>();
                    detalleVendedores = new HashMap<String, Object>();
                    //datos.put("txtNombreVendedor", vendedor.getValue().get(0).getDescripcion());
                    //obtener datos del total() por vendedor
                    LiVentas ventaTotalVendedor = new LiVentas();  
                    List<LiVentas> vend = new ArrayList<LiVentas>();
                    for(LiVentas lv : vendedor.getValue()){
                        if(lv.getNmes()==13){                              
                            ventaTotalVendedor.setNmes(lv.getNmes());
                            ventaTotalVendedor.setMontoPorMes(lv.getMontoPorMes());
                        }else{
                            vend.add(lv);
                        }
                    }
                    Map<Integer, List<LiVentas>> collect = vend.stream().collect(Collectors.groupingBy(LiVentas::getCodCliente));                                                           
                    for (Map.Entry<Integer, List<LiVentas>> agrupPorCliente : collect.entrySet()) {
                        Long sumEne = 0L;
                        Long sumFeb = 0L;
                        Long sumMar = 0L;
                        Long sumAbr = 0L;
                        Long sumMay = 0L;
                        Long sumJun = 0L;
                        Long sumJul = 0L;
                        Long sumAgo = 0L;
                        Long sumSep = 0L;
                        Long sumOct = 0L;
                        Long sumNov = 0L;
                        Long sumDic = 0L;
                        for(LiVentas venta : agrupPorCliente.getValue()){                                
                            switch (venta.getNmes()) {
                                case 1:
                                    sumEne = sumEne + venta.getMonto().longValue();                                    
                                    break;
                                case 2:
                                    sumFeb = sumFeb + venta.getMonto().longValue();
                                    break;
                                case 3:
                                    sumMar = sumMar + venta.getMonto().longValue();
                                    break;
                                case 4:
                                    sumAbr = sumAbr + venta.getMonto().longValue();
                                    break;
                                case 5:
                                    sumMay = sumMay + venta.getMonto().longValue();
                                    break;
                                case 6:
                                    sumJun = sumJun + venta.getMonto().longValue();
                                    break;
                                case 7:
                                    sumJul = sumJul + venta.getMonto().longValue();
                                    break;
                                case 8:
                                    sumAgo = sumAgo + venta.getMonto().longValue();
                                    break;
                                case 9:
                                    sumSep = sumSep + venta.getMonto().longValue();
                                    break;
                                case 10:
                                    sumOct = sumOct + venta.getMonto().longValue();
                                    break;
                                case 11:
                                    sumNov = sumNov + venta.getMonto().longValue();
                                    break;
                                case 12:
                                    sumDic = sumDic + venta.getMonto().longValue();
                                    break;
                                default:                                     
                                    break;
                            }                            
                        }
                        if(agrupPorCliente.getKey()!=13){
                            datos = new HashMap<String, Object>();
                            datos.put("textCliente", agrupPorCliente.getValue().get(0).getRazonSocialCliente());
                            datos.put("sumEne", sumEne);
                            datos.put("sumFeb", sumFeb);
                            datos.put("sumMar", sumMar);
                            datos.put("sumAbr", sumAbr);
                            datos.put("sumMay", sumMay);
                            datos.put("sumJun", sumJun);
                            datos.put("sumJul", sumJul);
                            datos.put("sumAgo", sumAgo);
                            datos.put("sumSep", sumSep);
                            datos.put("sumOct", sumOct);
                            datos.put("sumNov", sumNov);
                            datos.put("sumDic", sumDic);
                            datos.put("sumTotal", sumEne+sumFeb+sumMar+sumAbr+sumMay+sumJun+sumJul+sumAgo+sumSep+sumOct+sumNov+sumDic);
                            data.add(datos); 
                        }                                                                      
                    }
                    if(ventaTotalVendedor.getNmes()==13){
                        datos = new HashMap<String, Object>();
                        datos.put("textCliente", "Sub Total");
                        datos.put("sumEne", ventaTotalVendedor.getMontoPorMes().get(1).longValue());
                        datos.put("sumFeb", ventaTotalVendedor.getMontoPorMes().get(2).longValue());
                        datos.put("sumMar", ventaTotalVendedor.getMontoPorMes().get(3).longValue());
                        datos.put("sumAbr", ventaTotalVendedor.getMontoPorMes().get(4).longValue());
                        datos.put("sumMay", ventaTotalVendedor.getMontoPorMes().get(5).longValue());
                        datos.put("sumJun", ventaTotalVendedor.getMontoPorMes().get(6).longValue());
                        datos.put("sumJul", ventaTotalVendedor.getMontoPorMes().get(7).longValue());
                        datos.put("sumAgo", ventaTotalVendedor.getMontoPorMes().get(8).longValue());
                        datos.put("sumSep", ventaTotalVendedor.getMontoPorMes().get(9).longValue());
                        datos.put("sumOct", ventaTotalVendedor.getMontoPorMes().get(10).longValue());
                        datos.put("sumNov", ventaTotalVendedor.getMontoPorMes().get(11).longValue());
                        datos.put("sumDic", ventaTotalVendedor.getMontoPorMes().get(12).longValue());
                        datos.put("sumTotal", ventaTotalVendedor.getMontoPorMes().get(1).longValue()
                                +ventaTotalVendedor.getMontoPorMes().get(2).longValue()+ventaTotalVendedor.getMontoPorMes().get(3).longValue()
                                +ventaTotalVendedor.getMontoPorMes().get(4).longValue()+ventaTotalVendedor.getMontoPorMes().get(5).longValue()
                                +ventaTotalVendedor.getMontoPorMes().get(6).longValue()+ventaTotalVendedor.getMontoPorMes().get(7).longValue()
                                +ventaTotalVendedor.getMontoPorMes().get(8).longValue()+ventaTotalVendedor.getMontoPorMes().get(9).longValue()
                                +ventaTotalVendedor.getMontoPorMes().get(10).longValue()+ventaTotalVendedor.getMontoPorMes().get(11).longValue()
                                +ventaTotalVendedor.getMontoPorMes().get(12).longValue());
                        data.add(datos);
                    }
                    detalleVendedores.put("codAgrup", "ZONA");
                    detalleVendedores.put("txtCodVendedor", vendedor.getKey());
                    detalleVendedores.put("txtNombreVendedor", vendedor.getValue().get(0).getDescripcion());
                    detalleVendedores.put("detalleVendedor", new JRMapCollectionDataSource(data));
                    cabecera.add(detalleVendedores);
                }
                //----------------------------------------------------------------------------------------------
                
                //String reportLiVentas = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/classes/pdf/liVentasMes.jasper");
                //InputStream reporte = getClass().getClassLoader().getResourceAsStream("/WEB-INF/classes/pdf/liVentasMes.jasper");
                InputStream reporte = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/classes/pdf/liVentasMes.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, new JRMapCollectionDataSource(cabecera));        
                //exportar excell
                SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();                
                configuration.setOnePagePerSheet(false);
                configuration.setDetectCellType(true);
                configuration.setRemoveEmptySpaceBetweenColumns(true);
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setFontSizeFixEnabled(true);
                configuration.setCollapseRowSpan(false);
                configuration.setCellLocked(true);
                FacesContext context = FacesContext.getCurrentInstance();  
                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();  
                switch(tipoArchivo)
                {
                    case "EXCELL" :
                        JRXlsExporter xlsExporter = new JRXlsExporter();
                        xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        //xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput("C:/jasperoutput/sample_report.xls"));
                        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                        //config
                        xlsExporter.setConfiguration(configuration);
                        xlsExporter.exportReport();
                        //
                        response.setContentType("application/vnd.ms-excel"); //fill in contentType  
                        response.setHeader("Content-disposition", "attachment; filename=rventas_mes.xls");
                        OutputStream os = response.getOutputStream();  
                        os.write(out.toByteArray()); //fill in bytes  
                        os.flush();  
                        os.close();                          
                        break;
                    case "PDF" :
                        String disposition = "inline";
                        response.addHeader("Content-disposition", disposition + "; filename=rventas_mes.pdf");
                        response.addHeader("Content-type", "application/pdf");
                        ServletOutputStream servletStream = response.getOutputStream();
                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);                  
                        break; 
                    default : 
                        break; 
                }
                FacesContext faces = FacesContext.getCurrentInstance();                
                faces.responseComplete(); 
                limpiarDatos();
            }catch(Exception ex){
                System.out.println(ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar reporte.", "Ocurrió un error al generar el reporte"));
                
            }
        }else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Rango de fechas es un campo obligatorio"));            
        }        
    }
    
    private void limpiarDatos(){
        //limpiar filtros
        this.ruta = new Rutas(); this.canalVenta = new CanalesVenta();this.zona = new Zonas();this.proveedor = new Proveedores(); 
        this.sublinea = new Sublineas();this.linea = new Lineas(); this.division = new Divisiones(); this.tipoCliente = new TiposClientes(); 
        this.ciudadCliente = new Ciudades();this.tipoVenta = new TiposVentas();this.vendedor = new Empleados();
        this.rutaSelected = 0;
        this.zonaSelected = "0";
        this.canalVentaSelected = "0";
        this.proveedorSelected = 0;
        this.subLineaSelected =  0;
        this.lineaSelected =  0;
        this.divisionSelected =  0;
        this.tipoClienteSelected = "0";
        this.ciudadClienteSelected = 0;
        this.tipoVentaSelected = "0";
        this.vendedorSelected = 0;
    }
    
    private String generarConsultaAgrupacion(String agrupacion, String tipo) {
        String consultaGenerada = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
         switch (agrupacion){
            case "VENDEDOR":                
                if(tipo.equalsIgnoreCase("VENTA")){
                    String SQL_BASE_VENTAS_VENDEDOR = " SELECT MONTH(f.ffactur) AS nmes, f.cod_cliente,  f.xrazon_social, SUM(d.itotal) AS iventas, f.cod_vendedor, e.xnombre " +
                        " FROM   facturas f INNER JOIN " +
                        " empleados e ON f.cod_vendedor = e.cod_empleado, zonas z , rutas r, facturas_det d, mercaderias m, sublineas s, lineas l, divisiones v, categorias g, clientes c " +
                        " WHERE    f.cod_empr = 2 AND e.cod_empr = 2 AND  (f.ffactur BETWEEN '" + formatter.format(this.desde) + "' AND '" + formatter.format(this.hasta) + "') AND (f.mestado = 'A') " +
                        " AND f.cod_zona = z.cod_zona " +
                        " AND f.cod_ruta = r.cod_ruta " + 
                        " AND f.nrofact = d.nrofact " +
                        " AND f.ffactur = d.ffactur  " +
                        " AND f.cod_empr = d.cod_empr " +
                        " AND d.cod_merca = m.cod_merca " +
                        " AND m.cod_sublinea = s.cod_sublinea " +
                        " AND s.cod_linea = l.cod_linea " +
                        " AND l.cod_categoria = g.cod_categoria " +
                        " AND g.cod_division = v.cod_division " +
                        " AND f.cod_cliente = c.cod_cliente " +
                        " AND d.cod_empr = 2 " +
                        " AND f.mestado = 'A' ";
                    consultaGenerada = generarConsulta(SQL_BASE_VENTAS_VENDEDOR, "VENTA");
                    consultaGenerada = consultaGenerada.concat(SQL_GROUP_BY_VENDEDOR);
                }else{
                    String SQL_BASE_NOTAS_CREDITO_VENDEDOR = " SELECT MONTH(n.fdocum) AS nmes, f.cod_cliente, " +
                        " f.xrazon_social, SUM(d.igravadas+d.iexentas) AS ttotal, f.cod_vendedor, e.xnombre, n.ctipo_docum " +
                        " FROM  notas_ventas n , empleados e, zonas z , rutas r, facturas f, notas_ventas_det d, mercaderias m, sublineas s, lineas l, categorias g, divisiones v " +
                        " WHERE f.cod_empr = 2 AND e.cod_empr = 2 AND  (n.fdocum BETWEEN '" + formatter.format(this.desde) + "' AND '" + formatter.format(this.hasta) + "') AND (f.mestado = 'A') " +
                        " AND f.cod_zona = z.cod_zona " +
                        " AND n.nro_nota = d.nro_nota " +
                        " AND d.cod_empr = 2 " +
                        " AND n.cod_empr = 2 " +
                        " AND d.cod_merca = m.cod_merca " +
                        " AND f.cod_ruta = r.cod_ruta " +
                        " AND n.nrofact = f.nrofact " +
                        " AND n.ffactur = f.ffactur " +
                        " AND n.fdocum = d.fdocum " +
                        " AND n.ctipo_docum = 'NCV' " +
                        " AND m.cod_sublinea = s.cod_sublinea " +
                        " AND s.cod_linea = l.cod_linea " +
                        " AND l.cod_categoria = g.cod_categoria " +
                        " AND g.cod_division = v.cod_division " +
                        " AND f.cod_vendedor = e.cod_empleado " +
                        " AND n.mestado = 'A' " +
                        " AND n.fac_ctipo_docum = f.ctipo_docum ";
                    consultaGenerada = generarConsulta(SQL_BASE_NOTAS_CREDITO_VENDEDOR, "NOTA");
                    consultaGenerada = consultaGenerada.concat(SQL_NOTAS_GROUP_BY_VENDEDOR);
                }                
                break;
            case "ZONA": 
                if(tipo.equalsIgnoreCase("VENTA")){
                    String SQL_BASE_VENTAS_ZONA = " SELECT   f.cod_zona, MONTH(f.ffactur) AS nmes, f.cod_cliente,  f.xrazon_social, SUM(d.itotal) AS iventas,  "+
                        " z.xdesc as xdesc_zona, z.cod_empr "+
                        " FROM   facturas f INNER JOIN  "+
                        " empleados e ON f.cod_vendedor = e.cod_empleado, zonas z , rutas r, facturas_det d, mercaderias m, sublineas s, lineas l, divisiones v, categorias g, clientes c  "+
                        " WHERE    f.cod_empr = 2 AND e.cod_empr = 2 AND  (f.ffactur BETWEEN '" + formatter.format(this.desde) + "' AND '" + formatter.format(this.hasta) + "') AND (f.mestado = 'A')  "+
                        " AND f.cod_zona = z.cod_zona  "+
                        " AND f.cod_ruta = r.cod_ruta  "+
                        "  AND f.nrofact = d.nrofact  "+
                        "  AND f.ffactur = d.ffactur  "+
                        "  AND f.cod_empr = d.cod_empr  "+
                        "  AND d.cod_merca = m.cod_merca  "+
                        "  AND m.cod_sublinea = s.cod_sublinea  "+
                        "  AND s.cod_linea = l.cod_linea  "+
                        "  AND l.cod_categoria = g.cod_categoria  "+
                        "  AND g.cod_division = v.cod_division  "+
                        "  AND f.cod_cliente = c.cod_cliente "+
                        "  AND d.cod_empr = 2  "+
                        "  AND f.mestado = 'A'  ";                        
                    consultaGenerada = generarConsulta(SQL_BASE_VENTAS_ZONA, "VENTA");
                    consultaGenerada = consultaGenerada.concat(SQL_GROUP_BY_ZONA);                    
                }else{
                    String SQL_BASE_NOTAS_CREDITO_ZONA = " SELECT f.cod_zona, MONTH(n.fdocum) AS nmes, f.cod_cliente,  "+
                        "    f.xrazon_social, SUM(d.igravadas+d.iexentas) AS ttotal, z.xdesc as xdesc_zona , n.ctipo_docum, z.cod_empr "+
                        "    FROM         notas_ventas n , empleados e, zonas z , rutas r, facturas f, notas_ventas_det d, mercaderias m, sublineas s, lineas l, categorias g, divisiones v  "+
                        "    WHERE    f.cod_empr = 2 AND e.cod_empr = 2 AND  (n.fdocum BETWEEN '" + formatter.format(this.desde) + "' AND '" + formatter.format(this.hasta) + "') AND (f.mestado = 'A')  "+
                        "    AND f.cod_zona = z.cod_zona  "+
                        "    AND n.nro_nota = d.nro_nota  "+
                        "    AND d.cod_empr = 2  "+
                        "    AND n.cod_empr = 2  "+
                        "    AND d.cod_merca = m.cod_merca  "+
                        "    AND f.cod_ruta = r.cod_ruta  "+
                        "    AND n.nrofact = f.nrofact  "+
                        "    AND n.ffactur = f.ffactur  "+
                        "    AND n.fdocum = d.fdocum  "+
                        "    AND n.ctipo_docum = 'NCV'  "+
                        "    AND m.cod_sublinea = s.cod_sublinea  "+
                        "    AND s.cod_linea = l.cod_linea  "+
                        "    AND l.cod_categoria = g.cod_categoria  "+
                        "    AND g.cod_division = v.cod_division  "+
                        "    and f.cod_vendedor = e.cod_empleado  "+
                        "    AND n.mestado = 'A'  "+
                        "    AND n.fac_ctipo_docum = f.ctipo_docum ";
                    consultaGenerada = generarConsulta(SQL_BASE_NOTAS_CREDITO_ZONA, "NOTA");
                    consultaGenerada = consultaGenerada.concat(SQL_NOTAS_GROUP_BY_ZONA);
                }
                break;
            case "RUTA":
                if(tipo.equalsIgnoreCase("VENTA")){
                    String SQL_BASE_VENTAS_RUTA = " SELECT   f.cod_ruta, MONTH(f.ffactur) AS nmes, f.cod_cliente,  f.xrazon_social, SUM(d.itotal) AS iventas, r.xdesc as xdesc_ruta   "+
                        " FROM   facturas f INNER JOIN  "+
                        " empleados e ON f.cod_vendedor = e.cod_empleado, zonas z , rutas r, facturas_det d, mercaderias m, sublineas s, lineas l, divisiones v, categorias g, clientes c  "+
                        " WHERE    f.cod_empr = 2 AND e.cod_empr = 2 AND  (f.ffactur BETWEEN '" + formatter.format(this.desde) + "' AND '" + formatter.format(this.hasta) + "') AND (f.mestado = 'A')  "+
                        " AND f.cod_zona = z.cod_zona  "+
                        " AND f.cod_ruta = r.cod_ruta  "+
                        "  AND f.nrofact = d.nrofact  "+
                        "  AND f.ffactur = d.ffactur  "+
                        "  AND f.cod_empr = d.cod_empr  "+
                        "  AND d.cod_merca = m.cod_merca  "+
                        "  AND m.cod_sublinea = s.cod_sublinea  "+
                        "  AND s.cod_linea = l.cod_linea  "+
                        "  AND l.cod_categoria = g.cod_categoria  "+
                        "  AND g.cod_division = v.cod_division  "+
                        "  AND f.cod_cliente = c.cod_cliente "+
                        "  AND d.cod_empr = 2  "+
                        "  AND f.mestado = 'A'  ";                        
                    consultaGenerada = generarConsulta(SQL_BASE_VENTAS_RUTA, "VENTA");
                    consultaGenerada = consultaGenerada.concat(SQL_GROUP_BY_RUTA);                    
                }else{
                    String SQL_BASE_NOTAS_CREDITO_RUTA = " SELECT f.cod_ruta, MONTH(n.fdocum) AS nmes, f.cod_cliente,  "+
                        "   f.xrazon_social, SUM(d.igravadas+d.iexentas) AS ttotal, r.xdesc as xdesc_ruta, n.ctipo_docum  "+
                        "   FROM         notas_ventas n , empleados e, zonas z , rutas r, facturas f, notas_ventas_det d, mercaderias m, sublineas s, lineas l, categorias g, divisiones v  "+
                        "   WHERE    f.cod_empr = 2 AND e.cod_empr = 2 AND  (n.fdocum BETWEEN '" + formatter.format(this.desde) + "' AND '" + formatter.format(this.hasta) + "') AND (f.mestado = 'A')  "+
                        "   AND f.cod_zona = z.cod_zona  "+
                        "   AND n.nro_nota = d.nro_nota  "+
                        "   AND d.cod_empr = 2  "+
                        "   AND n.cod_empr = 2  "+
                        "   AND d.cod_merca = m.cod_merca  "+
                        "   AND f.cod_ruta = r.cod_ruta  "+
                        "   AND n.nrofact = f.nrofact  "+
                        "   AND n.ffactur = f.ffactur  "+
                        "   AND n.fdocum = d.fdocum  "+
                        "   AND n.ctipo_docum = 'NCV'  "+
                        "   AND m.cod_sublinea = s.cod_sublinea  "+
                        "   AND s.cod_linea = l.cod_linea  "+
                        "   AND l.cod_categoria = g.cod_categoria  "+
                        "   AND g.cod_division = v.cod_division  "+
                        "   and f.cod_vendedor = e.cod_empleado  "+
                        "   AND n.mestado = 'A'  "+
                        "   AND n.fac_ctipo_docum = f.ctipo_docum ";
                    consultaGenerada = generarConsulta(SQL_BASE_NOTAS_CREDITO_RUTA, "NOTA");
                    consultaGenerada = consultaGenerada.concat(SQL_NOTAS_GROUP_BY_RUTA);
                }
                break;
        }
        return consultaGenerada;
    }
    
    private String generarConsulta(String consultaBase, String tipo){
        //--------------------------------------------------------------------------------------------------------------------------
        //verificar filtro vendedor
       //System.out.println(String.format("Consulta Base Recibida: [%s]", ""));
       
        //System.out.println(this.vendedorSelected);
        //System.out.println(this.getVendedorSelected());
        if(this.getVendedorSelected() != 0 ){
            consultaBase = consultaBase.concat(" AND f.cod_vendedor = " + this.vendedor.getCodEmpleado());
        }
        //System.out.println(this.subLineaSelected);
        //System.out.println(getSubLineaSelected());
        if(getSubLineaSelected() != 0 ){
            consultaBase = consultaBase.concat(" AND m.cod_sublinea = " + this.sublinea.getCodSublinea());
        }
        //verificar filtro de linea
        //System.out.println(this.lineaSelected);
        if(getLineaSelected() != 0 ){
            consultaBase = consultaBase.concat(" AND l.cod_linea = " + this.linea.getCodLinea());
        }
        //verificar filtro de divisiones
        //System.out.println(this.divisionSelected);
        if(getDivisionSelected() != 0 ){
            consultaBase = consultaBase.concat(" AND v.cod_division = " + this.division.getCodDivision());
        }
        //verificar filtro de zona
        //System.out.println(this.zonaSelected);
        //System.out.println("zona selected get: " + getZonaSelected() + this.zonaSelected);
        if(!getZonaSelected().equalsIgnoreCase("0") ){
            consultaBase = consultaBase.concat(" AND f.cod_zona = '" + this.zona.getCodZona()+"'");
        }
        //verificar filtro canales venta 
        //System.out.println(this.canalVentaSelected);
        if(!getCanalVentaSelected().equalsIgnoreCase("0") ){
            consultaBase = consultaBase.concat(" AND f.cod_canal = '" + this.canalVenta.getCodCanal()+"'");
        }
        //verificar filtro rutas
        //System.out.println(this.rutaSelected);
        if(getRutaSelected() != 0 ){
            consultaBase = consultaBase.concat(" AND f.cod_ruta = " + this.ruta.getCodRuta());
        }
        //verificar filtro proveedor
        //System.out.println(this.proveedorSelected);
        if(getProveedorSelected() != 0){
            if(tipo.equalsIgnoreCase("VENTA")){
                consultaBase = consultaBase.concat(" AND m.cod_proveed = " + this.proveedor.getCodProveed());
            }
        }
        //verificar filtro por codigo cliente solo este falta agregar
        //verificar filtro por tipo ventas 
        //System.out.println(this.tipoVentaSelected);
        if(!getTipoVentaSelected().equalsIgnoreCase("0") ){
            if(tipo.equalsIgnoreCase("VENTA")){
                consultaBase = consultaBase.concat(" AND f.ctipo_vta = '" + this.tipoVenta.getCtipoVta()+"'");
            }
        }
        //verificar filtro por ciudad cliente
        //System.out.println(this.ciudadClienteSelected);
        if(getCiudadClienteSelected() != 0){
            if(tipo.equalsIgnoreCase("VENTA")){
                consultaBase = consultaBase.concat(" AND c.cod_ciudad = " + this.ciudadCliente.getCodCiudad());
            }
        }
        //verificar filtro por tipo cliente
        //System.out.println(this.tipoClienteSelected);
        if(!getTipoClienteSelected().equalsIgnoreCase("0")){
            if(tipo.equalsIgnoreCase("VENTA")){
                consultaBase = consultaBase.concat(" AND c.ctipo_cliente = '" + this.tipoCliente.getCtipoCliente()+"'");
            }
        }
        //--------------------------------------------------------------------------------------------------------------------------
        //System.out.println(String.format("Consulta Base Final: [%s]", ""));
        return consultaBase;
    }
    
    public void generarArchivoUno(String tipoArchivo){
        //Rango de Fechas es obligatorio
        if(this.desde != null && this.hasta != null){
            //--------------------------------------------------------------------------------------------------------------------------
            //verificar filtro vendedor
            if(this.vendedor != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND f.cod_vendedor = " + this.vendedor.getCodEmpleado());
            }
            //verificar filtro de sublinea
            if(this.sublinea != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND m.cod_sublinea = " + this.sublinea.getCodSublinea());
            }
            //verificar filtro de linea
            if(this.linea != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND l.cod_linea = " + this.linea.getCodLinea());
            }
            //verificar filtro de divisiones
            if(this.division != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND v.cod_division = " + this.division.getCodDivision());
            }
            //verificar filtro de zona
            if(this.zona != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND f.cod_zona = '" + this.zona.getCodZona()+"'");
            }
            //verificar filtro canales venta 
            if(this.canalVenta != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND f.cod_canal = '" + this.canalVenta.getCodCanal()+"'");
            }
            //verificar filtro rutas
            if(this.ruta != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND f.cod_ruta = " + this.ruta.getCodRuta());
            }
            //verificar filtro proveedor
            if(this.proveedor != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND m.cod_proveed = " + this.proveedor.getCodProveed());
            }
            
            //verificar filtro por codigo cliente solo este falta agregar
            //verificar filtro por tipo ventas 
            if(this.tipoVenta != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND f.ctipo_vta = '" + this.tipoVenta.getCtipoVta()+"'");
            }
            //verificar filtro por ciudad cliente
            if(this.ciudadCliente != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND c.cod_ciudad = " + this.ciudadCliente.getCodCiudad());
            }
            //verificar filtro por tipo cliente
            if(this.tipoCliente != null){
                this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(" AND c.ctipo_cliente = '" + this.tipoCliente.getCtipoCliente()+"'");
            }
            //--------------------------------------------------------------------------------------------------------------------------
            String groupByVenta = " GROUP BY f.cod_zona, MONTH(f.ffactur), f.cod_cliente, f.xrazon_social, f.cod_vendedor, e.xnombre, z.xdesc, f.cod_ruta, r.xdesc  ";
            this.SQL_BASE_VENTAS = this.SQL_BASE_VENTAS.concat(groupByVenta);
            //OBTENER NOTAS DE CREDITO
            //verificar filtro vendedor
            if(this.vendedor != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND f.cod_vendedor = " + this.vendedor.getCodEmpleado());
            }
            //verificar filtro de zona
            if(this.zona != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND f.cod_zona = '" + this.zona.getCodZona()+"'");
            }
            //verificar filtro canales venta 
            if(this.canalVenta != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND f.cod_canal = '" + this.canalVenta.getCodCanal()+"'");
            }
            //verificar filtro de sublinea
            if(this.sublinea != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND m.cod_sublinea = " + this.sublinea.getCodSublinea());
            }
            //verificar filtro de linea
            if(this.linea != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND l.cod_linea = " + this.linea.getCodLinea());
            }
            //verificar filtro de divisiones
            if(this.division != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND v.cod_division = " + this.division.getCodDivision());
            }
            //verificar filtro rutas
            if(this.ruta != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND f.cod_ruta = " + this.ruta.getCodRuta());
            }            
            //verificar filtro por codigo cliente (FALTA AGREGAR)
            //verificar filtro por tipo ventas
            if(this.tipoVenta != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND f.ctipo_vta = '" + this.tipoVenta.getCtipoVta()+"'");
            }
            //verificar filtro por ciudad cliente
            if(this.ciudadCliente != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND c.cod_ciudad = " + this.ciudadCliente.getCodCiudad());
            }
            //verificar filtro por tipo cliente
            if(this.tipoCliente != null){
                this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(" AND c.ctipo_cliente = '" + this.tipoCliente.getCtipoCliente()+"'");
            }
            String groupByNotaCredito = " GROUP BY f.cod_zona, MONTH(n.fdocum), f.cod_cliente, f.xrazon_social, f.cod_vendedor, e.xnombre, z.xdesc, f.cod_ruta, r.xdesc, n.ctipo_docum  ";
            this.SQL_BASE_NOTAS_CREDITO = this.SQL_BASE_NOTAS_CREDITO.concat(groupByNotaCredito);
            System.out.println("SQL PARA LA GENERACION DEL REPORTE");
            System.out.println(SQL_BASE_VENTAS);
            System.out.println(SQL_BASE_NOTAS_CREDITO);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Rango de fechas es un campo obligatorio"));
        }
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

    public Rutas getRuta() {
        return ruta;
    }

    public void setRuta(Rutas ruta) {
        this.ruta = ruta;
    }

    public List<Rutas> getRutas() {
        return rutas;
    }

    public CanalesVenta getCanalVenta() {
        return canalVenta;
    }

    public List<CanalesVenta> getCanalesVenta() {
        return canalesVenta;
    }

    public void setRutas(List<Rutas> rutas) {
        this.rutas = rutas;
    }

    public void setCanalVenta(CanalesVenta canalVenta) {
        this.canalVenta = canalVenta;
    }

    public void setCanalesVenta(List<CanalesVenta> canalesVenta) {
        this.canalesVenta = canalesVenta;
    }

    public Zonas getZona() {
        return zona;
    }

    public List<Zonas> getZonas() {
        return zonas;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public List<Proveedores> getProveedores() {
        return proveedores;
    }

    public Sublineas getSublinea() {
        return sublinea;
    }

    public List<Sublineas> getSublineas() {
        return sublineas;
    }

    public Lineas getLinea() {
        return linea;
    }

    public List<Lineas> getLineas() {
        return lineas;
    }

    public Divisiones getDivision() {
        return division;
    }

    public List<Divisiones> getDivisiones() {
        return divisiones;
    }

    public TiposClientes getTipoCliente() {
        return tipoCliente;
    }

    public List<TiposClientes> getTipoClientes() {
        return tipoClientes;
    }

    public Ciudades getCiudadCliente() {
        return ciudadCliente;
    }

    public List<Ciudades> getCiudadClientes() {
        return ciudadClientes;
    }

    public TiposVentas getTipoVenta() {
        return tipoVenta;
    }

    public List<TiposVentas> getTiposVentas() {
        return tiposVentas;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public void setZonas(List<Zonas> zonas) {
        this.zonas = zonas;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public void setProveedores(List<Proveedores> proveedores) {
        this.proveedores = proveedores;
    }

    public void setSublinea(Sublineas sublinea) {
        this.sublinea = sublinea;
    }

    public void setSublineas(List<Sublineas> sublineas) {
        this.sublineas = sublineas;
    }

    public void setLinea(Lineas linea) {
        this.linea = linea;
    }

    public void setLineas(List<Lineas> lineas) {
        this.lineas = lineas;
    }

    public void setDivision(Divisiones division) {
        this.division = division;
    }

    public void setDivisiones(List<Divisiones> divisiones) {
        this.divisiones = divisiones;
    }

    public void setTipoCliente(TiposClientes tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public void setTipoClientes(List<TiposClientes> tipoClientes) {
        this.tipoClientes = tipoClientes;
    }

    public void setCiudadCliente(Ciudades ciudadCliente) {
        this.ciudadCliente = ciudadCliente;
    }

    public void setCiudadClientes(List<Ciudades> ciudadClientes) {
        this.ciudadClientes = ciudadClientes;
    }

    public void setTipoVenta(TiposVentas tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public void setTiposVentas(List<TiposVentas> tiposVentas) {
        this.tiposVentas = tiposVentas;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public boolean isNotaDev() {
        return notaDev;
    }

    public void setNotaDev(boolean notaDev) {
        this.notaDev = notaDev;
    }

    public Empleados getVendedor() {
        return vendedor;
    }

    public List<Empleados> getVendedores() {
        return vendedores;
    }

    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
    }

    public void setVendedores(List<Empleados> vendedores) {
        this.vendedores = vendedores;
    }
    
    
    
}
