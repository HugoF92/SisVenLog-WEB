/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import bean.selectorVentas.SelectorDatosBean;
import dao.CanalesVentaFacade;
import dao.CiudadesFacade;
import dao.DivisionesFacade;
import dao.EmpleadosFacade;
import dao.LiPagaresFacade;
import dao.LiVentasFacade;
import dao.LineasFacade;
import dao.ProveedoresFacade;
import dao.RutasFacade;
import dao.SublineasFacade;
import dao.TiposClientesFacade;
import dao.TiposVentasFacade;
import dao.ZonasFacade;
import dto.LiVentas;
import dto.LiVentasCab;
import entidad.CanalesVenta;
import entidad.Ciudades;
import entidad.Clientes;
import entidad.Divisiones;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Proveedores;
import entidad.Rutas;
import entidad.Sublineas;
import entidad.TiposClientes;
import entidad.TiposVentas;
import entidad.TmpDatos;
import entidad.Zonas;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.context.RequestContext;
import util.StringUtil;
/**
 *
 * @author Clara
 */
@ManagedBean
@SessionScoped
public class LiVentasMesBean {
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
    /*--------------------------------------------*/
    private static final String SQL_GROUP_BY_TOTAL = " GROUP BY f.cod_vendedor, e.xnombre, f.cod_zona, z.xdesc, f.cod_ruta, r.xdesc, f.cod_cliente, f.xrazon_social, MONTH(f.ffactur)";
    private static final String SQL_NOTAS_GROUP_BY_TOTAL = " GROUP BY f.cod_vendedor, e.xnombre, f.cod_zona, z.xdesc, f.cod_ruta, r.xdesc, f.cod_cliente, f.xrazon_social, MONTH(n.fdocum), n.ctipo_docum ";
    /*------------------------------------------------------------------------------------ */
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
    //
    private Boolean seleccionarClientes;
    
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
    @EJB
    private LiPagaresFacade pagareFacade;
    
    
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
    private Boolean checkCliente;
    private List<Clientes> listadoClientesSeleccionados;
    
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
        this.seleccionarClientes = false;
        this.checkCliente = true;
        this.listadoClientesSeleccionados = new ArrayList<Clientes>();
    }

    
    
    public void generarAchivoExcell(){
        if(checkCliente){
            this.listadoClientesSeleccionados = new ArrayList<Clientes>();
        }else{
            for(TmpDatos t: pagareFacade.getDatosSelctor("select * from tmp_datos order by codigo")){
                Clientes c = new Clientes();
                c.setCodCliente(Integer.valueOf(t.getCodigo()));
                c.setXnombre(t.getDescripcion());
                this.listadoClientesSeleccionados.add(c);
            }
        }
        if(this.desde != null && this.hasta != null){
            String consultaVentaTotal = generarConsultaAgrupacion("TOTAL", "VENTA");       
            String consultaNotaTotal = generarConsultaAgrupacion("TOTAL", "NOTAS");
            List<LiVentas> listadoVentasMes = this.ventasFacade.obtenerListadoExcell(consultaVentaTotal, consultaNotaTotal);
            try {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet();                
                List<LiVentas> statFilterResults = listadoVentasMes;
                Iterator<LiVentas> statsIterator = statFilterResults.iterator();
                int i = 0;
                HSSFRow row;
                row = sheet.createRow((short) 0);
                row.createCell((short) 0).setCellValue("CodVendedor");
                row.createCell((short) 1).setCellValue("DescripcionVendedor");
                row.createCell((short) 2).setCellValue("CodZona");
                row.createCell((short) 3).setCellValue("DescripcionZona");
                row.createCell((short) 4).setCellValue("CodRuta");
                row.createCell((short) 5).setCellValue("DescripcionRuta");
                row.createCell((short) 6).setCellValue("CodCliente");
                row.createCell((short) 7).setCellValue("DescripcionCliente");
                row.createCell((short) 8).setCellValue("Mes");
                row.createCell((short) 9).setCellValue("Total");
                while (statsIterator.hasNext()) {
                    i++;
                    row = sheet.createRow(i);
                    LiVentas perfBean = statsIterator.next();
                    row.createCell((short)0).setCellValue(perfBean.getCodVendedor());
                    row.createCell((short) 1).setCellValue(perfBean.getDescripcionVendedor());
                    row.createCell((short) 2).setCellValue(perfBean.getCodZona());
                    row.createCell((short) 3).setCellValue(perfBean.getDescripcionZona());
                    row.createCell((short) 4).setCellValue(perfBean.getCodRuta());
                    row.createCell((short) 5).setCellValue(perfBean.getDescripcionRunta());
                    row.createCell((short) 6).setCellValue(perfBean.getCodCliente());
                    row.createCell((short) 7).setCellValue(perfBean.getRazonSocialCliente());                    
                    row.createCell((short) 8).setCellValue(getMesDescripcion(perfBean.getNmes()));
                    row.createCell((short) 9).setCellValue(perfBean.getMonto().doubleValue());
                }
                HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                res.setContentType("application/vnd.ms-excel");
                res.setHeader("Content-disposition", "attachment; filename=rventas_mes.xls");

                try {
                    ServletOutputStream out = res.getOutputStream();
                    wb.write(out);
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                FacesContext faces = FacesContext.getCurrentInstance();
                faces.responseComplete();
            } catch (Exception e) {
                System.out.println(e);
            }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Rango de fechas es un campo obligatorio"));            
        }
        this.listadoClientesSeleccionados = new ArrayList<Clientes>();
    }
    
    public void generarAchivoPDF(){
        if(checkCliente){
            this.listadoClientesSeleccionados = new ArrayList<Clientes>();
        }else{
            for(TmpDatos t: pagareFacade.getDatosSelctor("select * from tmp_datos order by codigo")){
                Clientes c = new Clientes();
                c.setCodCliente(Integer.valueOf(t.getCodigo()));
                c.setXnombre(t.getDescripcion());
                this.listadoClientesSeleccionados.add(c);
            }
        }
        if(this.desde != null && this.hasta != null){
            String consultaVentaTotal = generarConsultaAgrupacion("TOTAL", "VENTA");       
            String consultaNotaTotal = generarConsultaAgrupacion("TOTAL", "NOTAS");
            List<LiVentasCab> listadoVentasMes = this.ventasFacade.obtenerListado(consultaVentaTotal, consultaNotaTotal);
            List<Map<String, ?>> data;
            Map<String, Object> datos = new HashMap<String, Object>();;
            DateFormat formatoFecha =new SimpleDateFormat("dd/MM/yyyy");
            HashMap parameters = new HashMap();
            List<Map<String, ?>> cabecera = new ArrayList<Map<String, ?>>();   
            Map<String, Object> detalleVendedores = new HashMap<String, Object>();
            String TITULO = "VENTAS A CLIENTES POR MES";
            String NOMBRE_REPORTE = "rventas_mes";
            parameters.put("fechaDesde", this.desde);
            parameters.put("fechaHasta", this.hasta);
            parameters.put("titulo", TITULO);
            parameters.put("nombreRepo", NOMBRE_REPORTE);
            parameters.put("usu_imprime", "admin");
            parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
            if(!this.listadoClientesSeleccionados.isEmpty()){
                parameters.put("txtCliente", StringUtil.convertirListaAString(this.listadoClientesSeleccionados));
            }else{
                parameters.put("txtCliente", "TODOS");
            } 
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
            //totalizador
            BigDecimal sumEneT = new BigDecimal(0);BigDecimal sumFebT = new BigDecimal(0);BigDecimal sumMarT = new BigDecimal(0);BigDecimal sumAbrT = new BigDecimal(0);
            BigDecimal sumMayT = new BigDecimal(0);BigDecimal sumJunT = new BigDecimal(0);BigDecimal sumJulT = new BigDecimal(0);BigDecimal sumAgoT = new BigDecimal(0);
            BigDecimal sumSepT = new BigDecimal(0);BigDecimal sumOctT = new BigDecimal(0);BigDecimal sumNovT = new BigDecimal(0);BigDecimal sumDicT = new BigDecimal(0);
            BigDecimal sumTotalT = new BigDecimal(0);
            for(LiVentasCab liCab : listadoVentasMes){
                data = new ArrayList<Map<String, ?>>();
                detalleVendedores = new HashMap<String, Object>();
                data.addAll(liCab.getClientesPorMes());
                //System.out.println(liCab.getClientesPorMes().size());                
                //System.out.println(liCab.getDescripcionVendedor().concat(liCab.getDescripcionZona()).concat(liCab.getDescripcionRunta()));
                //encabezado de grupo
                detalleVendedores.put("txtCodVendedor", liCab.getCodVendedor());
                detalleVendedores.put("txtNombreVendedor", liCab.getDescripcionVendedor());
                detalleVendedores.put("txtCodZona", liCab.getCodZona());
                detalleVendedores.put("txtNombreZona", liCab.getDescripcionZona());
                detalleVendedores.put("txtCodRuta", liCab.getCodRuta());
                detalleVendedores.put("txtNombreRuta", liCab.getDescripcionRunta());
                //--------------                
                for(Map.Entry<String, BigDecimal> m : liCab.getMontoPorMes().entrySet()){
                    switch (m.getKey()) {
                            case "sumEne":                              
                                sumEneT = sumEneT.add(m.getValue());
                                break;
                            case "sumFeb":
                                sumFebT = sumFebT.add(m.getValue());
                                break;
                            case "sumMar":
                                sumMarT = sumMarT.add(m.getValue());
                                break;
                            case "sumAbr":
                                sumAbrT = sumAbrT.add(m.getValue());
                                break;
                            case "sumMay":
                                sumMayT = sumMayT.add(m.getValue());
                                break;
                            case "sumJun":
                                sumJunT = sumJunT.add(m.getValue());
                                break;
                            case "sumJul":
                                sumJulT = sumJulT.add(m.getValue());
                                break;
                            case "sumAgo":
                                sumAgoT = sumAgoT.add(m.getValue());
                                break;
                            case "sumSep":
                                sumSepT = sumSepT.add(m.getValue());
                                break;
                            case "sumOct":
                                sumOctT = sumOctT.add(m.getValue());
                                break;
                            case "sumNov":
                                sumNovT = sumNovT.add(m.getValue());
                                break;                                
                            case "sumDic":
                                sumDicT = sumDicT.add(m.getValue());
                                break;
                            default:                                
                                break;
                        }
                }                
                detalleVendedores.putAll(liCab.getMontoPorMes());
                detalleVendedores.put("detalleVendedor", new JRMapCollectionDataSource(data));
                cabecera.add(detalleVendedores);
            }
            //TOTALIZADOR PARA EL REPORTE GENERAL                        
            Map<String, BigDecimal> montoPorReporte = new HashMap<String, BigDecimal>();            
            montoPorReporte.put("sumTotalEne", sumEneT);
            montoPorReporte.put("sumTotalFeb", sumFebT);
            montoPorReporte.put("sumTotalMar", sumMarT);
            montoPorReporte.put("sumTotalAbr", sumAbrT);
            montoPorReporte.put("sumTotalMay", sumMayT);
            montoPorReporte.put("sumTotalJun", sumJunT);
            montoPorReporte.put("sumTotalJul", sumJulT);
            montoPorReporte.put("sumTotalAgo", sumAgoT);
            montoPorReporte.put("sumTotalSep", sumSepT);
            montoPorReporte.put("sumTotalOct", sumOctT);
            montoPorReporte.put("sumTotalNov", sumNovT);
            montoPorReporte.put("sumTotalDic", sumDicT);            
            sumTotalT = sumTotalT.add(sumEneT).add(sumFebT).add(sumMarT).add(sumAbrT).add(sumMayT).add(sumJunT).add(sumJulT).add(sumAgoT).add(sumSepT).add(sumOctT).add(sumNovT).add(sumDicT);
            montoPorReporte.put("sumTotalVenta", sumTotalT);                       
            parameters.putAll(montoPorReporte);
            //--------------
            try{
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
                String disposition = "inline";
                response.addHeader("Content-disposition", disposition + "; filename=rventas_mes.pdf");
                response.addHeader("Content-type", "application/pdf");
                ServletOutputStream servletStream = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);   
                FacesContext faces = FacesContext.getCurrentInstance();                
                faces.responseComplete(); 
                limpiarDatos();
            }catch(Exception ex){
                System.out.println(ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar reporte.", "Ocurrió un error al generar el reporte"));                
            }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Rango de fechas es un campo obligatorio"));            
        }
        this.listadoClientesSeleccionados = new ArrayList<Clientes>();
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

    public Boolean getSeleccionarClientes() {
        return seleccionarClientes;
    }

    public Boolean getCheckCliente() {
        return checkCliente;
    }

    public void setSeleccionarClientes(Boolean seleccionarClientes) {
        this.seleccionarClientes = seleccionarClientes;
    }

    public void setCheckCliente(Boolean checkCliente) {
        this.checkCliente = checkCliente;
    }
    
    
    
    public String getMesDescripcion(Integer mes){
        switch(mes)
        {
            case 1:
                return "Enero";    
            case 2:
                return "Febrero";                 
            case 3:
                return "Marzo"; 
            case 4:
                return "Abril"; 
            case 5:
                return "Mayo"; 
            case 6:
                return "Junio"; 
            case 7:
                return "Julio"; 
            case 8:
                return "Agosto"; 
            case 9:
                return "Septiembre"; 
            case 10:
                return "Octubre"; 
            case 11:
                return "Noviembre"; 
            case 12:
                return "Diciembre";
            default:
                return "Mes 13";
                        
        }
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
            default:
                if(tipo.equalsIgnoreCase("VENTA")){
                    String SQL_BASE_VENTAS_TOTAL = " SELECT  f.cod_vendedor, e.xnombre, f.cod_zona, z.xdesc as xdesc_zona, "+
                        " f.cod_ruta, r.xdesc as xdesc_ruta,  f.cod_cliente,  f.xrazon_social, MONTH(f.ffactur) AS nmes,  SUM(d.itotal) AS iventas   "+
                        " FROM   facturas f INNER JOIN  "+
                        " empleados e ON f.cod_vendedor = e.cod_empleado, zonas z , rutas r, facturas_det d, mercaderias m, sublineas s, lineas l, divisiones v, categorias g, clientes c "+
                        " WHERE    f.cod_empr = 2 AND e.cod_empr = 2 AND  (f.ffactur BETWEEN '" + formatter.format(this.desde) + "' AND '" + formatter.format(this.hasta) + "') AND (f.mestado = 'A')  "+
                        " AND f.cod_zona = z.cod_zona  "+
                        " AND f.cod_ruta = r.cod_ruta  "+
                        " AND f.cod_cliente = c.cod_cliente "+
                        "  AND f.nrofact = d.nrofact  "+
                        "  AND f.ffactur = d.ffactur  "+
                        "  AND f.cod_empr = d.cod_empr  "+
                        "  AND d.cod_merca = m.cod_merca  "+
                        "  AND m.cod_sublinea = s.cod_sublinea  "+
                        "  AND s.cod_linea = l.cod_linea  "+
                        "  AND l.cod_categoria = g.cod_categoria  "+
                        "  AND g.cod_division = v.cod_division  "+
                        "  AND d.cod_empr = 2  "+
                        "  AND f.mestado = 'A' ";
                    consultaGenerada = generarConsulta(SQL_BASE_VENTAS_TOTAL, "VENTA");
                    consultaGenerada = consultaGenerada.concat(SQL_GROUP_BY_TOTAL);     
                }else{
                    String SQL_BASE_NOTAS_CREDITO_TOTAL = " SELECT f.cod_vendedor, e.xnombre, f.cod_zona, z.xdesc as xdesc_zona, f.cod_ruta, r.xdesc as xdesc_ruta, "+
                        "    f.cod_cliente,  f.xrazon_social, MONTH(n.fdocum) AS nmes,  SUM(d.igravadas+d.iexentas) AS ttotal, n.ctipo_docum  "+
                        "    FROM  notas_ventas n , empleados e, zonas z , rutas r, facturas f, notas_ventas_det d, mercaderias m, sublineas s, lineas l, categorias g, divisiones v  "+
                        "    WHERE f.cod_empr = 2 AND e.cod_empr = 2 AND  (n.fdocum BETWEEN '" + formatter.format(this.desde) + "' AND '" + formatter.format(this.hasta) + "') AND (f.mestado = 'A')  "+
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
                    consultaGenerada = generarConsulta(SQL_BASE_NOTAS_CREDITO_TOTAL, "NOTA");
                    consultaGenerada = consultaGenerada.concat(SQL_NOTAS_GROUP_BY_TOTAL);
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
        if(!this.listadoClientesSeleccionados.isEmpty() && this.seleccionarClientes){
            consultaBase = consultaBase.concat(" AND f.cod_cliente in (" + StringUtil.convertirListaAString(this.listadoClientesSeleccionados) + ")");
        }
        //--------------------------------------------------------------------------------------------------------------------------
        //System.out.println(String.format("Consulta Base Final: [%s]", ""));
        return consultaBase;
    }
    
    public void llamarSelectorDatos()  {
        //cambiar el selector de todos               
        if(this.seleccionarClientes){
            this.checkCliente = false;
            //RequestContext.getCurrentInstance().update("ocultarBtnPag");
            System.out.println("Mostrar selector de cliente");
            SelectorDatosBean.sql = "select cod_cliente, xnombre \n"
                + "from clientes\n"
                + "where cod_estado in ('A', 'S') ";        
            SelectorDatosBean.tabla_temporal = "tmp_datos";

            SelectorDatosBean.campos_tabla_temporal = "codigo, descripcion";
            
            RequestContext.getCurrentInstance().execute("PF('dlgSelDatosVta').show();");
        }else{
            this.checkCliente = true;
            //RequestContext.getCurrentInstance().update("ocultarBtnPag");
            System.out.println("Ocultar selector de cliente");
        }
        RequestContext.getCurrentInstance().update("mostrarBtnVta");
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
    
}
