/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;


import dao.ClientesFacade;
import dao.LiPagaresFacade;
import dto.LiPagares;
import dto.LiPagaresCab;
import entidad.Clientes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import util.ExceptionHandlerView;
/**
 *
 * @author Clara
 */
@ManagedBean
@SessionScoped
public class LiPagaresBean {
    
    //valores iniciales para las fechas del reporte
    private Date desdeEmision;
    private Date hastaEmision;
    private Date desdeVencimiento;
    private Date hastaVencimiento;
    private Date desdeCobro;
    private Date hastaCobro;
    private Boolean checkPagareDetalle;
    private Boolean checkCliente;
    private Integer codigoCliente;
    private String nombreCliente;
    private String contenidoError;
    private String tituloError;
    private String estadoPagare;
    private Clientes clientes;
    private List<Clientes> listaClientes;
    private String filtro;
    private Boolean seleccionarClientes;
    
    @EJB
    private ClientesFacade clientesFacade;
    @EJB
    private LiPagaresFacade pagareFacade;
    
    
    @PostConstruct
    public void instanciar() {
        //Rango de fechas del mes actual
        this.desdeEmision = new Date();
        this.hastaEmision = new Date();
        this.checkPagareDetalle = false;     
        this.checkCliente = true;
        this.seleccionarClientes = false;
    }

    
    
    private void limpiarDatos(){
        //limpiar filtros
        this.desdeEmision = new Date();
        this.hastaEmision = new Date();
        this.desdeVencimiento = null;
        this.hastaVencimiento = null;
        this.desdeCobro = null;
        this.hastaCobro = null;
        this.checkPagareDetalle = false;
        this.estadoPagare = null;
        this.codigoCliente = null;
        this.nombreCliente = null;
        this.checkCliente = true;
        this.clientes = null;
        //RequestContext.getCurrentInstance().update("pnlLiPagares");
    }
    
    public void generarArchivo(String tipoArchivo){        
        List<LiPagares> cabeceraPagare = new ArrayList<LiPagares>();
        Map<Integer, LiPagaresCab> cabDetalle = new TreeMap<Integer, LiPagaresCab>();
        if(this.desdeEmision != null && this.hastaEmision != null){
            if(this.checkCliente){
                this.codigoCliente = null;
                this.nombreCliente = null;
                this.clientes = null;
            }
            if(this.checkPagareDetalle==true){
                if(tipoArchivo.equalsIgnoreCase("PDF")){
                    cabDetalle = this.pagareFacade.getPagaresCabDetalle(this.desdeEmision, this.hastaEmision, this.desdeVencimiento, this.hastaVencimiento, this.desdeCobro, this.hastaCobro, this.codigoCliente, this.estadoPagare);
                    if(cabDetalle.isEmpty()){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados", "No se pudo obtener datos con los filtros establecidos"));
                    }else{
                        generarArchivoDetalle(tipoArchivo, cabDetalle);                        
                    }
                }else{
                    cabeceraPagare = this.pagareFacade.getPagaresCabDetalleExcell(this.desdeEmision, this.hastaEmision, this.desdeVencimiento, this.hastaVencimiento, this.desdeCobro, this.hastaCobro, this.codigoCliente, this.estadoPagare);
                    if(cabeceraPagare.isEmpty()){
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados", "No se pudo obtener datos con los filtros establecidos"));
                    }else{
                        this.generarAchivoExcell(cabeceraPagare, this.checkPagareDetalle);                      
                    }
                }
            }else{
                cabeceraPagare = this.pagareFacade.getPageresCabecera(this.desdeEmision, this.hastaEmision, this.desdeVencimiento, this.hastaVencimiento, this.desdeCobro, this.hastaCobro, this.codigoCliente, this.estadoPagare);                                
                if(cabeceraPagare.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados", "No se pudo obtener datos con los filtros establecidos"));
                }else{
                    if(tipoArchivo.equalsIgnoreCase("PDF")){
                        generarArchivoCabecera(tipoArchivo, cabeceraPagare);
                    }else{
                        this.generarAchivoExcell(cabeceraPagare, this.checkPagareDetalle);
                    }
                }
            }
        }else{          
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar archivo.", "Rango de fechas es un campo obligatorio"));            
        }
    }
    
    public void generarArchivoCabecera(String tipoArchivo, List<LiPagares> cabeceraPagare ){        
        String NOMBRE_REPORTE = "RPAGARE";
        String TITULO = "PAGARES DE CLIENTES";
        HashMap parameters = new HashMap();
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
        Map<String, Object> datos = new HashMap<String, Object>();;
        InputStream reporte = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/classes/pdf/liPagaresCabecera.jasper");        
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse(); 
        BigDecimal totalPagare = new BigDecimal(0);
        for(LiPagares p: cabeceraPagare){                
            datos = new HashMap<String, Object>();
            datos.put("nroPagare", p.getNroPagare());
            datos.put("fEmision", p.getFechEmision());
            datos.put("fVencimiento", p.getFechVencimiento());
            datos.put("cliente", p.getCodCliente() + " " + p.getNombreCliente());
            datos.put("entregador", p.getCodEntregador() + " " + p.getNombreEntregador());
            datos.put("importe", p.getiPagare());
            if (p.getEstado().equalsIgnoreCase("A")){
                datos.put("estado", "Activo");
            }else{
                datos.put("estado", "Inactivo");
            }
            totalPagare = totalPagare.add(p.getiPagare());
            data.add(datos);                
        }
        try {
            parameters.put("fechaDesdeEmision", this.desdeEmision);
            parameters.put("fechaHastaEmision", this.hastaEmision);
            parameters.put("fechaDesdeVencimiento", this.desdeVencimiento);
            parameters.put("fechaHastaVencimiento", this.hastaVencimiento);
            parameters.put("fechaDesdeCobro", this.desdeCobro);
            parameters.put("fechaHastaCobro", this.hastaCobro);
            if(this.estadoPagare!=null){
                if (this.estadoPagare.equalsIgnoreCase("A")){
                    parameters.put("txtEstado", "Activo");
                }else if(this.estadoPagare.equalsIgnoreCase("I")){
                    parameters.put("txtEstado", "Inactivo");
                }else{
                    parameters.put("txtEstado", "TODOS");
                }
            }else{
                parameters.put("txtEstado", "TODOS");
            }         
            if(!cabeceraPagare.isEmpty()){
                parameters.put("nroInicial", cabeceraPagare.get(0).getNroPagare());
                parameters.put("nroFinal", cabeceraPagare.get(cabeceraPagare.size() - 1).getNroPagare());
            }            
            if(this.codigoCliente!=null){
                parameters.put("txtCliente", this.codigoCliente + " " + this.nombreCliente);
            }else{
                parameters.put("txtCliente", "TODOS");
            }                
            parameters.put("titulo", TITULO);
            parameters.put("nombreRepo", NOMBRE_REPORTE);
            parameters.put("usu_imprime", "admin");
            parameters.put("sumTotalPagare", totalPagare.longValue());
            parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, new JRMapCollectionDataSource(data));        
            //exportar excell
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();                
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setRemoveEmptySpaceBetweenColumns(true);
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setFontSizeFixEnabled(true);
            configuration.setCollapseRowSpan(false);
            configuration.setCellLocked(true);
            switch(tipoArchivo)
            {                
                case "PDF" :
                    String disposition = "inline";
                    response.addHeader("Content-disposition", disposition + "; filename=rpagares.pdf");
                    response.addHeader("Content-type", "application/pdf");
                    ServletOutputStream servletStream = response.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);                  
                    break; 
                default : 
                    break; 
            }
            FacesContext faces = FacesContext.getCurrentInstance();                
            faces.responseComplete();
            this.limpiarDatos();
        }catch(Exception ex){
            this.limpiarDatos();
            System.out.println(ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar reporte.", "Ocurrió un error al generar el reporte"));
        }
    }
    
    public void generarAchivoExcell(List<LiPagares> cabeceraPagare, Boolean checkPagareDetalle){       
        
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();                
            List<LiPagares> statFilterResults = cabeceraPagare;
            Iterator<LiPagares> statsIterator = statFilterResults.iterator();
            int i = 0;
            HSSFRow row;
            row = sheet.createRow((short) 0);
            row.createCell((short) 0).setCellValue("NroPagare");
            row.createCell((short) 1).setCellValue("FechEmision");
            row.createCell((short) 2).setCellValue("FechVencimiento");
            row.createCell((short) 3).setCellValue("CodCliente");
            row.createCell((short) 4).setCellValue("DescripcionCliente");
            row.createCell((short) 5).setCellValue("CodEntregador");
            row.createCell((short) 6).setCellValue("DescripcionEntregador");
            row.createCell((short) 7).setCellValue("ImportePagare");
            row.createCell((short) 8).setCellValue("Estado");
            if(checkPagareDetalle){
                row.createCell((short) 9).setCellValue("TipoDocumento");
                row.createCell((short) 10).setCellValue("NroFactura");
                row.createCell((short) 11).setCellValue("FechFactura");
                row.createCell((short) 12).setCellValue("ImporteTotal");
            }
            while (statsIterator.hasNext()) {
                i++;
                row = sheet.createRow((short) i);
                LiPagares perfBean = statsIterator.next();
                row.createCell((short) 0).setCellValue(perfBean.getNroPagare());
                row.createCell((short) 1).setCellValue(perfBean.getFechEmision());
                row.createCell((short) 2).setCellValue(perfBean.getFechVencimiento());
                row.createCell((short) 3).setCellValue(perfBean.getCodCliente());
                row.createCell((short) 4).setCellValue(perfBean.getNombreCliente());                
                row.createCell((short) 5).setCellValue(perfBean.getCodEntregador());
                row.createCell((short) 6).setCellValue(perfBean.getNombreEntregador());                    
                row.createCell((short) 7).setCellValue(perfBean.getiPagare().doubleValue());
                if(perfBean.getEstado().equalsIgnoreCase("A")){
                    row.createCell((short) 8).setCellValue("Activo");
                }else{
                    row.createCell((short) 8).setCellValue("Inactivo");
                }
                if(checkPagareDetalle){
                    row.createCell((short) 9).setCellValue(perfBean.getTipoDocum());
                    row.createCell((short) 10).setCellValue(perfBean.getNrofact());
                    row.createCell((short) 11).setCellValue(perfBean.getFechaFactur());
                    row.createCell((short) 12).setCellValue(perfBean.getiTotal().doubleValue());
                }
            }
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            if(checkPagareDetalle){
                res.setHeader("Content-disposition", "attachment; filename=RpagareDET.xls");
            }else{
                res.setHeader("Content-disposition", "attachment; filename=rpagare.xls");
            }
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
    }
    
    public void generarArchivoDetalle(String tipoArchivo, Map<Integer, LiPagaresCab> cabDetalle ){        
        String NOMBRE_REPORTE = "RpagareDET";
        String TITULO = "PAGARES DE CLIENTES";
        HashMap parameters = new HashMap();
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
        Map<String, Object> datos = new HashMap<String, Object>();
        Map<String, Object> detallePagares = new HashMap<String, Object>();
        List<Map<String, ?>> cabeceraPagares = new ArrayList<Map<String, ?>>();   
        String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/liPagaresSub.jasper");
        parameters.put("SUBREPORT_DIR", report); 
        InputStream reporte = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/classes/pdf/liPagares.jasper");        
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();             
        BigDecimal totalPagare = new BigDecimal(0);        
        for(Map.Entry<Integer, LiPagaresCab> pagares : cabDetalle.entrySet()){
            LiPagaresCab pagaresCab = pagares.getValue();
            detallePagares = new HashMap<String, Object>();
            data = new ArrayList<Map<String, ?>>();            
            detallePagares.put("nroPagare", pagaresCab.getNroPagare());
            detallePagares.put("fEmision", pagaresCab.getFechEmision());
            detallePagares.put("fVencimiento", pagaresCab.getFechVencimiento());
            detallePagares.put("cliente", pagaresCab.getCodCliente() + " " + pagaresCab.getNombreCliente());
            detallePagares.put("entregador", pagaresCab.getCodEntregador() + " " + pagaresCab.getNombreEntregador());
            detallePagares.put("importe", pagaresCab.getiPagare());
            if (pagaresCab.getEstado().equalsIgnoreCase("A")){
                    detallePagares.put("estado", "Activo");
            }else{
                detallePagares.put("estado", "Inactivo");
            }
            totalPagare = totalPagare.add(pagaresCab.getiPagare());
            //Rango Pagare
            parameters.put("nroInicial", pagaresCab.getNroInicial());
            parameters.put("nroFinal", pagaresCab.getNroFinal());
            for(LiPagares p: pagaresCab.getDetalles()){                
                datos = new HashMap<String, Object>();                                
                datos.put("txtTipoDocum", p.getTipoDocum());
                datos.put("txtNroFactura", p.getNrofact());
                datos.put("txtFechFactura", p.getFechaFactur());
                datos.put("txtTotal", p.getiTotal());                
                data.add(datos);                
            }
            detallePagares.put("detallePagares", new JRMapCollectionDataSource(data));
            cabeceraPagares.add(detallePagares);
        }                
        try {
            parameters.put("fechaDesdeEmision", this.desdeEmision);
            parameters.put("fechaHastaEmision", this.hastaEmision);
            parameters.put("fechaDesdeVencimiento", this.desdeVencimiento);
            parameters.put("fechaHastaVencimiento", this.hastaVencimiento);
            parameters.put("fechaDesdeCobro", this.desdeCobro);
            parameters.put("fechaHastaCobro", this.hastaCobro);
            if(this.estadoPagare!=null){
                if (this.estadoPagare.equalsIgnoreCase("A")){
                    parameters.put("txtEstado", "Activo");
                }else if(this.estadoPagare.equalsIgnoreCase("I")){
                    parameters.put("txtEstado", "Inactivo");
                }else{
                    parameters.put("txtEstado", "TODOS");
                }
            }else{
                parameters.put("txtEstado", "TODOS");
            }  
            
            if(this.codigoCliente!=null){
                parameters.put("txtCliente", this.codigoCliente + " " + this.nombreCliente);
            }else{
                parameters.put("txtCliente", "TODOS");
            }                
            parameters.put("titulo", TITULO);
            parameters.put("nombreRepo", NOMBRE_REPORTE);
            parameters.put("usu_imprime", "admin");
            parameters.put("sumTotalPagare", totalPagare.longValue());
            parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, new JRMapCollectionDataSource(cabeceraPagares));        
            //exportar excell
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();                
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setRemoveEmptySpaceBetweenColumns(true);
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setFontSizeFixEnabled(true);
            configuration.setCollapseRowSpan(false);
            configuration.setCellLocked(true);
            switch(tipoArchivo)
            {
                case "EXCELL" :
                    JRXlsExporter xlsExporter = new JRXlsExporter();
                    xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                    //config
                    xlsExporter.setConfiguration(configuration);
                    xlsExporter.exportReport();
                    //
                    response.setContentType("application/vnd.ms-excel"); //fill in contentType  
                    response.setHeader("Content-disposition", "attachment; filename=RpagareDET.xls");
                    OutputStream os = response.getOutputStream();  
                    os.write(out.toByteArray()); //fill in bytes  
                    os.flush();  
                    os.close();                          
                    break;
                case "PDF" :
                    String disposition = "inline";
                    response.addHeader("Content-disposition", disposition + "; filename=RpagareDET.pdf");
                    response.addHeader("Content-type", "application/pdf");
                    ServletOutputStream servletStream = response.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);                  
                    break; 
                default : 
                    break; 
            }
            FacesContext faces = FacesContext.getCurrentInstance();                
            faces.responseComplete();
            this.limpiarDatos();
        }catch(Exception ex){
            System.out.println(ex);
            this.limpiarDatos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al generar reporte.", "Ocurrió un error al generar el reporte"));
        }        
    }
    
    public void verificarCheckCliente(){
      System.out.println("se ha realizado verificar check: "+ this.checkCliente.toString());
      if(this.checkCliente){
          this.codigoCliente = null;
          this.nombreCliente = "";
      }
    }
    public void verificarCliente(){
        if(codigoCliente != null){
            if(codigoCliente == 0){
                //mostrar busqueda de clientes
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaPagare').show();");
            }else{
                try{
                    Clientes clienteBuscado = clientesFacade.find(codigoCliente);
                    if(clienteBuscado == null){
                        //mostrar busqueda de clientes
                        RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaPagare').show();");
                    }else{
                        this.nombreCliente = clienteBuscado.getXnombre();
                    }
                }catch(Exception e){
                    RequestContext.getCurrentInstance().update("exceptionDialogPagare");
                    contenidoError = ExceptionHandlerView.getStackTrace(e);
                    tituloError = "Error en la lectura de datos de clientes.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
                    RequestContext.getCurrentInstance().execute("PF('exceptionDialogPagare').show();");
                }
            }
        }
    }
    
    /*public void llamarSelectorDatos() {
        
        System.out.println("llamarSelectorDatos");
        SelectorDatosBean.sql = "select cod_cliente, xnombre \n"
                + "from clientes\n"
                + "where cod_estado in ('A', 'S') ";
        
        SelectorDatosBean.tabla_temporal = "tmp_datos";
        
        SelectorDatosBean.campos_tabla_temporal = "codigo, descripcion";
        //RequestContext.getCurrentInstance().update("pnlGridPagare");
        RequestContext.getCurrentInstance().execute("PF('dlgSelDatos').show();");
    }*/
    
    public void inicializarBuscadorClientes(){
        this.listaClientes = new ArrayList<>();
        this.clientes = new Clientes();
        this.filtro = "";
        listarClientesBuscador();
    }
    
    public void listarClientesBuscador(){
        try{
            listaClientes = clientesFacade.buscarPorFiltro(filtro);
        }catch(Exception e){
            RequestContext.getCurrentInstance().update("exceptionDialogPagare");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de clientes.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
            RequestContext.getCurrentInstance().execute("PF('exceptionDialogPagare').show();");
        }   
    }

    public void onRowSelect(SelectEvent event) {
        if (getClientes() != null) {
            if (getClientes().getXnombre() != null) {
                codigoCliente = getClientes().getCodCliente();
                nombreCliente = getClientes().getXnombre();
                RequestContext.getCurrentInstance().update("pnlGridPagare");
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaPagare').hide();");
            }
        }
    }
    
    public Date getDesdeEmision() {
        return desdeEmision;
    }

    public Date getHastaEmision() {
        return hastaEmision;
    }

    public Date getDesdeVencimiento() {
        return desdeVencimiento;
    }

    public Date getHastaVencimiento() {
        return hastaVencimiento;
    }

    public Date getDesdeCobro() {
        return desdeCobro;
    }

    public Date getHastaCobro() {
        return hastaCobro;
    }

    public Boolean getCheckPagareDetalle() {
        return checkPagareDetalle;
    }

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getContenidoError() {
        return contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public String getEstadoPagare() {
        return estadoPagare;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public String getFiltro() {
        return filtro;
    }

    public Boolean getCheckCliente() {
        return checkCliente;
    }

    public void setDesdeEmision(Date desdeEmision) {
        this.desdeEmision = desdeEmision;
    }

    public void setHastaEmision(Date hastaEmision) {
        this.hastaEmision = hastaEmision;
    }

    public void setDesdeVencimiento(Date desdeVencimiento) {
        this.desdeVencimiento = desdeVencimiento;
    }

    public void setHastaVencimiento(Date hastaVencimiento) {
        this.hastaVencimiento = hastaVencimiento;
    }

    public void setDesdeCobro(Date desdeCobro) {
        this.desdeCobro = desdeCobro;
    }

    public void setHastaCobro(Date hastaCobro) {
        this.hastaCobro = hastaCobro;
    }

    public void setCheckPagareDetalle(Boolean checkPagareDetalle) {
        this.checkPagareDetalle = checkPagareDetalle;
    }

    public void setCodigoCliente(Integer codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }
    
    public void setEstadoPagare(String estadoPagare) {
        this.estadoPagare = estadoPagare;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void setCheckCliente(Boolean checkCliente) {
        this.checkCliente = checkCliente;
    }

    public Boolean getSeleccionarClientes() {
        return seleccionarClientes;
    }

    public void setSeleccionarClientes(Boolean seleccionarClientes) {
        this.seleccionarClientes = seleccionarClientes;
    }
    
}
