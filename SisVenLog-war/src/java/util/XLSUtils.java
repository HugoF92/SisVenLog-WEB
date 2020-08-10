package util;

import dao.ExcelFacade;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;

/**
 * Clase para crear archivos MS Excel ".xls"
 * @author Ing. Inf. Marcos Brizuela
 */
public class XLSUtils {
    
    private enum TiposDeDato { TEXT, LONG, DOUBLE, DATE, BOOLEAN }
    private static TiposDeDato[] tiposDatosColumnas;
    private static HSSFWorkbook wb;
    
    @EJB
    private ExcelFacade excelFacade;  
    
    public void exportarExcelGenerico(String sql, String nombre) throws Exception {
        
        Workbook wbk = excelFacade.crearExcelGenerico(sql);
        
        HttpServletResponse respuesta = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        respuesta.setContentType("application/vnd.ms-excel");
        respuesta.setHeader("Content-disposition", "attachment; filename=" + nombre + ".xls");

        try (ServletOutputStream out = respuesta.getOutputStream()) {
            wbk.write(out);
            out.flush();
        }
        
        FacesContext faces = FacesContext.getCurrentInstance();
        faces.responseComplete();
    }
    
    public static void crearExcelGenerico(ResultSet rs, String nombre) throws Exception{
        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Hoja 1");
        ResultSetMetaData metaDatos = rs.getMetaData();
        tiposDatosColumnas = new TiposDeDato[metaDatos.getColumnCount()];
                
        int filaActual = 0;
        HSSFRow fila = sheet.createRow(filaActual);
        for (int i = 0; i < metaDatos.getColumnCount(); i++) {
            String tituloColumna = metaDatos.getColumnName(i + 1);
            escribirCelda(fila, i, tituloColumna, TiposDeDato.TEXT);
            Class claseTipoDato = Class.forName(metaDatos.getColumnClassName(i + 1));
            tiposDatosColumnas[i] = getTipoDeDato(claseTipoDato);
        }

        filaActual++;
        while (rs.next()) {
            fila = sheet.createRow(filaActual++);
            for (int i = 0; i < metaDatos.getColumnCount(); i++) {
                Object value = rs.getObject(i + 1);
                if (value != null) escribirCelda(fila, i, value, tiposDatosColumnas[i]);
            }
        }

        for (int i = 0; i < metaDatos.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }
        
        HttpServletResponse respuesta = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        respuesta.setContentType("application/vnd.ms-excel");
        respuesta.setHeader("Content-disposition", "attachment; filename=" + nombre + ".xls");

        try (ServletOutputStream out = respuesta.getOutputStream()) {
            wb.write(out);
            out.flush();
        }
        
        FacesContext faces = FacesContext.getCurrentInstance();
        faces.responseComplete();
    }
    
    private static TiposDeDato getTipoDeDato(Class tipoClase) {
        if (tipoClase == Integer.class || tipoClase == Long.class || tipoClase == BigDecimal.class || tipoClase == Byte.class || tipoClase == Short.class) {
            return TiposDeDato.LONG;
        } else if (tipoClase == Float.class || tipoClase == Double.class) {
            return TiposDeDato.DOUBLE;
        } else if (tipoClase == java.sql.Timestamp.class || tipoClase == java.sql.Date.class || tipoClase == java.sql.Time.class) {
            return TiposDeDato.DATE;
        } else if (tipoClase == Boolean.class){
            return TiposDeDato.BOOLEAN;
        } else {
            return TiposDeDato.TEXT;
        }
    }
    
    private static void escribirCelda(HSSFRow fila, int columna, Object valor, TiposDeDato tipoDeDato) throws Exception {
        Cell celda = CellUtil.createCell(fila, columna, null);
        switch (tipoDeDato) {
            case TEXT:
                celda.setCellValue(valor.toString());
                break;
            case LONG:
                celda.setCellValue(((Number) valor).longValue());
                CellUtil.setCellStyleProperty(celda, wb, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat(("#,##0")));
                break;
            case DOUBLE:
                celda.setCellValue(((Number) valor).doubleValue());
                CellUtil.setCellStyleProperty(celda, wb, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat(("#,##0.00")));
                break;
            case BOOLEAN:
                celda.setCellValue((Boolean) valor);
                break;
            case DATE:
                celda.setCellValue((java.sql.Timestamp) valor);
                CellUtil.setCellStyleProperty(celda, wb, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat(("m/d/yy")));
        }
    }
    
}