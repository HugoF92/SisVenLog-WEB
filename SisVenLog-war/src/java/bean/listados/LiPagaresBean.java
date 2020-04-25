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
import dto.LiVentas;
import entidad.Clientes;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
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
        this.checkCliente = false;
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
        this.checkCliente = false;
        this.clientes = null;
        RequestContext.getCurrentInstance().update("pnlLiPagares");
    }
    
    public void generarArchivo(String tipoArchivo){        
        List<LiPagares> cabeceraPagare = new ArrayList<LiPagares>();
        Map<Integer, LiPagaresCab> cabDetalle = new HashMap<Integer, LiPagaresCab>();
        if(this.desdeEmision != null && this.hastaEmision != null){
            if(this.checkCliente){
                this.codigoCliente = null;
                this.nombreCliente = null;
                this.clientes = null;
            }
            if(this.checkPagareDetalle==true){
                cabDetalle = this.pagareFacade.getPagaresCabDetalle(this.desdeEmision, this.hastaEmision, this.desdeVencimiento, this.hastaVencimiento, this.desdeCobro, this.hastaCobro, this.codigoCliente, this.estadoPagare);
                if(cabDetalle.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados", "No se pudo obtener datos con los filtros establecidos"));
                }else{
                    generarArchivoDetalle(tipoArchivo, cabDetalle);
                }
            }else{
                cabeceraPagare = this.pagareFacade.getPageresCabecera(this.desdeEmision, this.hastaEmision, this.desdeVencimiento, this.hastaVencimiento, this.desdeCobro, this.hastaCobro, this.codigoCliente, this.estadoPagare);                                
                if(cabeceraPagare.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos no encontrados", "No se pudo obtener datos con los filtros establecidos"));
                }else{
                    generarArchivoCabecera(tipoArchivo, cabeceraPagare);
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
        SimpleDateFormat formatear = new SimpleDateFormat("yyyy-MM-dd");       
        BigDecimal totalPagare = new BigDecimal(0);
        for(LiPagares p: cabeceraPagare){                
            datos = new HashMap<String, Object>();
            datos.put("nroPagare", p.getNroPagare());
            datos.put("fEmision", formatear.format(p.getFechEmision()));
            datos.put("fVencimiento", formatear.format(p.getFechVencimiento()));
            datos.put("cliente", p.getCodCliente() + " - " + p.getNombreCliente());
            datos.put("entregador", p.getCodEntregador() + " - " + p.getNombreEntregador());
            datos.put("importe", p.getiPagare().longValue());
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
            if(this.codigoCliente!=null){
                parameters.put("txtCliente", this.codigoCliente + " - " + this.nombreCliente);
            }else{
                parameters.put("txtCliente", "TODOS");
            }                
            parameters.put("titulo", TITULO);
            parameters.put("nombreRepo", NOMBRE_REPORTE);
            parameters.put("usu_imprime", "admin");
            parameters.put("sumTotalPagare", totalPagare.longValue());
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
                    response.setHeader("Content-disposition", "attachment; filename=rpagares.xls");
                    OutputStream os = response.getOutputStream();  
                    os.write(out.toByteArray()); //fill in bytes  
                    os.flush();  
                    os.close();                          
                    break;
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
        SimpleDateFormat formatear = new SimpleDateFormat("yyyy-MM-dd");       
        BigDecimal totalPagare = new BigDecimal(0);
        for(Map.Entry<Integer, LiPagaresCab> pagares : cabDetalle.entrySet()){
            LiPagaresCab pagaresCab = pagares.getValue();
            detallePagares = new HashMap<String, Object>();
            data = new ArrayList<Map<String, ?>>();
            detallePagares.put("txtTipoDocum", pagaresCab.getTipoDocum());
            detallePagares.put("txtNroFactura", pagaresCab.getNrofact());
            detallePagares.put("txtFechFactura", formatear.format(pagaresCab.getFechaFactur()));
            detallePagares.put("txtTotal", pagaresCab.getiTotal().longValue());
            for(LiPagares p: pagaresCab.getDetalles()){                
                datos = new HashMap<String, Object>();                
                datos.put("nroPagare", p.getNroPagare());
                datos.put("fEmision", formatear.format(p.getFechEmision()));
                datos.put("fVencimiento", formatear.format(p.getFechVencimiento()));
                datos.put("cliente", p.getCodCliente() + " - " + p.getNombreCliente());
                datos.put("entregador", p.getCodEntregador() + " - " + p.getNombreEntregador());
                datos.put("importe", p.getiPagare().longValue());
                if (p.getEstado().equalsIgnoreCase("A")){
                    datos.put("estado", "Activo");
                }else{
                    datos.put("estado", "Inactivo");
                }
                totalPagare = totalPagare.add(p.getiPagare());
                data.add(datos);                
            }
            detallePagares.put("detallePagares", new JRMapCollectionDataSource(data));
            cabeceraPagares.add(detallePagares);
        }                
        try {
            parameters.put("fechaDesdeEmision", this.desdeEmision);
            parameters.put("fechaHastaEmision", this.hastaEmision);
            if(this.codigoCliente!=null){
                parameters.put("txtCliente", this.codigoCliente + " - " + this.nombreCliente);
            }else{
                parameters.put("txtCliente", "TODOS");
            }                
            parameters.put("titulo", TITULO);
            parameters.put("nombreRepo", NOMBRE_REPORTE);
            parameters.put("usu_imprime", "admin");
            parameters.put("sumTotalPagare", totalPagare.longValue());
            
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
    
}
