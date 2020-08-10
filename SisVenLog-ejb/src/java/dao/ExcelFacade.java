package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import dto.LiMercaSinDto;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.*;

@Stateless
public class ExcelFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    private enum TiposDeDato { TEXT, LONG, DOUBLE, DATE, BOOLEAN }
    private static TiposDeDato[] tiposDatosColumnas;
    private static HSSFWorkbook wb;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ExcelFacade() {
    }

    public List<LiMercaSinDto> listarLiMercaSin(String sql) {
        List<LiMercaSinDto> respuesta = new ArrayList<LiMercaSinDto>();

        try {
            
            System.out.println(sql);

            Query q = getEntityManager().createNativeQuery(sql);


            List<Object[]> resultList = q.getResultList();

            if (resultList.size() <= 0) {
                respuesta = new ArrayList<LiMercaSinDto>();
                return respuesta;
            } else {
                for (Object[] obj : resultList) {

                    LiMercaSinDto aux = new LiMercaSinDto();

                    aux.setCodMerca(obj[0].toString());
                    aux.setCodBarra(obj[1].toString());
                    aux.setXdesc(obj[2].toString());
                    aux.setCajas(obj[4].toString());
                    aux.setUnidades(obj[5].toString());

                    respuesta.add(aux);

                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al listar secuencias."));
        }

        return respuesta;

    }
    
    public List<Object[]> listarParaExcel(String sql) {
        List<Object[]> respuesta = new ArrayList<Object[]>();

        try {
            
            System.out.println(sql);

            Query q = getEntityManager().createNativeQuery(sql);
            
            respuesta = q.getResultList();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al listar secuencias."));
        }

        return respuesta;

    }
    
    public ResultSet ejecutarSQLQueryParaExcelGenerico(String sql) {
        try {
            return em.unwrap(Connection.class).createStatement().executeQuery(sql);
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atención", "Error al ejecutar SQL para Excel"));
            return null;
        }
    }
    
    private void crearCabecera(String sql, Workbook wbs, Sheet sheet) throws Exception {
        System.out.println("antes de sql " + System.currentTimeMillis());        
        ResultSet rs = em.unwrap(Connection.class).createStatement().executeQuery(sql);
        System.out.println("despues de sql " + System.currentTimeMillis());
        //HSSFWorkbook wb = new HSSFWorkbook();
        //HSSFSheet sheet = wb.createSheet("Hoja 1");
        ResultSetMetaData metaDatos = rs.getMetaData();
        tiposDatosColumnas = new TiposDeDato[metaDatos.getColumnCount()];
        
        int filaActual = 0;
        Row fila = sheet.createRow(filaActual);
        for (int i = 0; i < metaDatos.getColumnCount(); i++) {
            String tituloColumna = metaDatos.getColumnName(i + 1);
            escribirCelda(fila, i, tituloColumna, TiposDeDato.TEXT, wbs);
            Class claseTipoDato = Class.forName(metaDatos.getColumnClassName(i + 1));
            tiposDatosColumnas[i] = getTipoDeDato(claseTipoDato);
        }
        System.out.println("despues de cabecera " + System.currentTimeMillis());
    }
    
    public Workbook crearExcelGenerico(String sql) throws Exception {
        System.out.println("antes de sql " + System.currentTimeMillis());        
        ResultSet rs = em.unwrap(Connection.class).createStatement().executeQuery(sql);
        System.out.println("despues de sql " + System.currentTimeMillis());
        //HSSFWorkbook wb = new HSSFWorkbook();
        //HSSFSheet sheet = wb.createSheet("Hoja 1");
        ResultSetMetaData metaDatos = rs.getMetaData();
        TiposDeDato[] tiposDatosColumnas = new TiposDeDato[metaDatos.getColumnCount()];
        
        // nuevo metodo
        //Workbook wbs = new XSSFWorkbook();
        Workbook wbs = new HSSFWorkbook();
        Sheet sheet = wbs.createSheet("Hoja 1");
        
        //crearCabecera(sql, wbs, sheet);        
        int filaActual = 0;
        Row fila = sheet.createRow(filaActual);
        for (int i = 0; i < metaDatos.getColumnCount(); i++) {
            String tituloColumna = metaDatos.getColumnName(i + 1);
            escribirCelda(fila, i, tituloColumna, TiposDeDato.TEXT, wbs);
            Class claseTipoDato = Class.forName(metaDatos.getColumnClassName(i + 1));
            tiposDatosColumnas[i] = getTipoDeDato(claseTipoDato);
        }

        filaActual++;        
        System.out.println("despues de cabecera " + System.currentTimeMillis());        
        
        while (rs.next()) {
            fila = sheet.createRow(filaActual++);
            for (int i = 0; i < metaDatos.getColumnCount(); i++) {
                Object value = rs.getObject(i + 1);
                if (value != null) escribirCelda(fila, i, value, tiposDatosColumnas[i], wbs);
            }
        }

        /*for (int i = 0; i < metaDatos.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }*/
        System.out.println("despues de excel " + System.currentTimeMillis());
        return wbs;       
    }
    
    private TiposDeDato getTipoDeDato(Class tipoClase) {
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
    
    private void escribirCelda(Row fila, int columna, Object valor, TiposDeDato tipoDeDato, Workbook wb) throws Exception {
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
