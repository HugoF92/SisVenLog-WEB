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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

@Stateless
public class ExcelFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    private enum TiposDeDato { TEXT, LONG, DOUBLE, DATE, BOOLEAN }
    private TiposDeDato[] tiposDatosColumnas;
    private CellStyle [] formatoDatosColumnas;
    private Integer size;

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
    
    private void crearCabecera(String sql, SXSSFWorkbook wbs, Sheet sheet) throws Exception {        
        /*TODO: aca si existe un order by hay que quitar*/
        Long inicio = System.nanoTime();
        //ResultSet rs = em.unwrap(Connection.class).createStatement().executeQuery("select top 1 t.* from ( " + sql + ") t ");
        sql = sql.replaceFirst("select ", "select top 1 ");
        System.out.println("sql " + sql );
        ResultSet rs = em.unwrap(Connection.class).createStatement().executeQuery(sql);
        Long fin = System.nanoTime();
        System.out.println("cabecera total sql " + (fin-inicio)/1000000 + "msegundos" );
        ResultSetMetaData metaDatos = rs.getMetaData();
        tiposDatosColumnas = new TiposDeDato[metaDatos.getColumnCount()];
        
        int filaActual = 0;
        Row fila = sheet.createRow(filaActual);
        size = metaDatos.getColumnCount();
        for (int i = 0; i < metaDatos.getColumnCount(); i++) {
            String tituloColumna = metaDatos.getColumnName(i + 1);
            escribirCelda(fila, i, tituloColumna, TiposDeDato.TEXT);
            Class claseTipoDato = Class.forName(metaDatos.getColumnClassName(i + 1));
            tiposDatosColumnas[i] = getTipoDeDato(claseTipoDato);
        }
        
        // definir los 3 estilos posibles 
        formatoDatosColumnas = new CellStyle[3];
        formatoDatosColumnas[0] = wbs.createCellStyle();
        formatoDatosColumnas[0].setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        formatoDatosColumnas[1] = wbs.createCellStyle();
        formatoDatosColumnas[1].setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        formatoDatosColumnas[2] = wbs.createCellStyle();
        formatoDatosColumnas[2].setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
                
        fin = System.nanoTime();
        System.out.println("cabecera total " + (fin-inicio)/1000000 + "msegundos" );
    }
    
    public Workbook crearExcelGenerico(String sql) throws Exception {
        Long inicio = System.nanoTime();
        //System.out.println("antes de sql " + System.currentTimeMillis());        
        //ResultSet rs = em.unwrap(Connection.class).createStatement().executeQuery(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        List<Object[]> lista = q.getResultList();
        Long fin = System.nanoTime();
        System.out.println("exel total sql " + (fin-inicio)/1000000 + "msegundos" );
        //HSSFWorkbook wb = new HSSFWorkbook();
        //HSSFSheet sheet = wb.createSheet("Hoja 1");
        //ResultSetMetaData metaDatos = rs.getMetaData();
        //TiposDeDato[] tiposDatosColumnas = new TiposDeDato[metaDatos.getColumnCount()];        
        // nuevo metodo
        SXSSFWorkbook wbs = new SXSSFWorkbook(200);
        // 200 es buen nro para registros < 50000
        Sheet sheet = wbs.createSheet("Hoja 1");        
        crearCabecera(sql, wbs, sheet);        
        int filaActual = 0;
        //HSSFRow fila = sheet.createRow(filaActual);
        Row fila = null;
        /*for (int i = 0; i < metaDatos.getColumnCount(); i++) {
            String tituloColumna = metaDatos.getColumnName(i + 1);
            escribirCelda(fila, i, tituloColumna, TiposDeDato.TEXT, wbs);
            Class claseTipoDato = Class.forName(metaDatos.getColumnClassName(i + 1));
            tiposDatosColumnas[i] = getTipoDeDato(claseTipoDato);
        }
        */
        filaActual++;        
        //System.out.println("despues de cabecera " + System.currentTimeMillis());        
        inicio = System.nanoTime();
        /*while (rs.next()) {
            fila = sheet.createRow(filaActual++);
            for (int i = 0; i < size; i++) {
                Object value = rs.getObject(i + 1);
                if (value != null) escribirCelda(fila, i, value, tiposDatosColumnas[i], wbs);
            }
        }
        */        
        for (int i = 0; i < lista.size(); i++) {
            fila = sheet.createRow(filaActual++);
            Object[] objeto = lista.get(i);
            for (int j = 0; j < size; j++) {
                Object value = objeto[j];
                if (value != null) escribirCelda(fila, j, value, tiposDatosColumnas[j]);
            }

        }
        /*for (int i = 0; i < metaDatos.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }*/
        fin = System.nanoTime();
        System.out.println("cuerpo total " + (fin-inicio)/1000000 + "msegundos" );
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
    
    private void escribirCelda(Row fila, int columna, Object valor, TiposDeDato tipoDeDato) throws Exception {
        //Cell celda = CellUtil.createCell(fila, columna, null);
        Cell celda = fila.createCell(columna);
        switch (tipoDeDato) {
            case LONG:
                //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                celda.setCellStyle(formatoDatosColumnas[0]);
                celda.setCellValue(((Number) valor).longValue());
                //CellUtil.setCellStyleProperty(celda, wb, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat(("#,##0")));
                break;
            case DOUBLE:
                //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                celda.setCellStyle(formatoDatosColumnas[1]);
                celda.setCellValue(((Number) valor).doubleValue());
                //CellUtil.setCellStyleProperty(celda, wb, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat(("#,##0.00")));
                break;
            case BOOLEAN:
                //celda.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
                celda.setCellValue((Boolean) valor);
                break;
            case DATE:
                celda.setCellStyle(formatoDatosColumnas[2]);
                celda.setCellValue((java.sql.Timestamp) valor);                
                //CellUtil.setCellStyleProperty(celda, wb, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat(("m/d/yy")));
                break;
            default: 
                celda.setCellValue(valor.toString());
        }
    }
    
}
