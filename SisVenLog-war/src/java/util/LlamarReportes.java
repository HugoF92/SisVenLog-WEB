/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dto.LiMercaSinDto;
import entidad.Proveedores;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Admin
 */
public class LlamarReportes {

    public Connection conexion;

    //private String destination = System.getProperty("java.io.tmpdir");
    //private String destination = "C:\\tmp\\";
    public LlamarReportes() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conexion = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=VenlogDB", "sa", "venlog2018CC");
            //conexion = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=venlogDb", "sa", "secreto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void impresionFactura(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/facturaF1.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaFactura(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/facturaF1.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=factura.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void impresionFacturaServicios(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/servicios.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaFacturaServicios(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/servicios.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=serv.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void impresionRemision(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/remision.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaRemision(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/remision.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=remi.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void impresionNotaCredito(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/notaCredito.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaNotaCredito(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/notaCredito.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=NotaCredito.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void impresionSalidas(int fInicial, int fFinal, int zona) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);
            param.put("zona", zona);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/salida.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaSalidas(int fInicial, int fFinal, int zona) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);
            param.put("zona", zona);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/salida.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=NotaCredito.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void impresionOrdenCarga(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/ordenCarga.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaOrdenCarga(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/ordenCarga.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=NotaCredito.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void impresionOrdenPago(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/ordenPago.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaOrdenPago(int fInicial, int fFinal) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/ordenPago.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=NotaCredito.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void impresionChequeDia(int fInicial, int fFinal, Integer banco) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);
            param.put("banco", banco);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/chequeDia.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaChequeDia(int fInicial, int fFinal, Integer banco) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);
            param.put("banco", banco);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/chequeDia.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=ChequeDia.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void impresionChequeDife(int fInicial, int fFinal, Integer banco) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);
            param.put("banco", banco);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/chequeDiferido.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=ChequeDife.pdf");
            //httpServletResponse.addHeader("Content-disposition", "attachment; filename=ExtractoDeudas_" + documento + ".pdf");
            httpServletResponse.addHeader("Content-type", "application/pdf");

            ServletOutputStream servletStream = httpServletResponse.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vistaPreviaChequeDife(int fInicial, int fFinal, Integer banco) {
        try {

            //preparamos los parametros para el JasperPrint
            Map param = new HashMap();
            param.put("desde", fInicial);
            param.put("hasta", fFinal);
            param.put("banco", banco);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/chequeDiferido.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            JasperPrintManager.printReport(jasperPrint, false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiApli(int rolCodigo, String usuaCodigo, String rol, String usuario, String tipo, String usuImprme) {
        try {

            Map param = new HashMap();
            param.put("rol_codigo", rolCodigo);
            param.put("usua_codigo", usuaCodigo);
            param.put("rol", rol);
            param.put("usuario", usuario);
            param.put("usu_imprime", usuImprme);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/aplicaciones.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=raplica.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=raplica.xls");
                    /*httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();*/
                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiAnudoc(String sql, String desde, String hasta, String tipoDoc, String usuImprime, String operacion, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("tipoDoc", tipoDoc);
            param.put("operacion", operacion);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/documentosAnulados.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=lianudoc.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=lianudoc.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiErroresProc(String desde, String hasta, String desde1, String hasta1, Integer empleado, String usuImprime, String tipo, String nomEmple) {
        try {

            Map param = new HashMap();
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("desde1", desde1);
            param.put("hasta1", hasta1);
            param.put("cod_vendedor", empleado);
            param.put("usu_imprime", usuImprime);
            param.put("nomEmple", nomEmple);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/erroresProc.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rerroresproc.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rerroresproc.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiProcesamientos(String desde, String hasta, String desde1, String hasta1, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("desde1", desde1);
            param.put("hasta1", hasta1);
            param.put("usu_imprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/procesamientos.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rprocesa.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rprocesa.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiConsDoc(String desde, String hasta,
            String desde1, String hasta1,
            Integer empleado, String usuImprime, String tipo,
            String nomEmple, String nomDepo, Integer cod_deposito) {
        try {

            Map param = new HashMap();
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("desde1", desde1);
            param.put("hasta1", hasta1);
            param.put("cod_vendedor", empleado);
            param.put("usu_imprime", usuImprime);
            param.put("nomEmple", nomEmple);
            param.put("nomDepo", nomDepo);
            param.put("cod_deposito", cod_deposito);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/consolidadoDocu.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=reconsdoc.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=reconsdoc.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteTotReten(String desde, String hasta, String desde1, String hasta1, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("desde1", desde1);
            param.put("hasta1", hasta1);
            param.put("usu_imprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/totReten.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rtotreten.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rtotreten.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiFacBonif(Integer nroPromo, String tipoDoc, String desde, String hasta, String desde1, String hasta1, String descTipoDoc, String descPromo, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("promo", nroPromo);
            param.put("tipoDoc", tipoDoc);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("desde1", desde1);
            param.put("hasta1", hasta1);
            param.put("descTipoDoc", descTipoDoc);
            param.put("descPromo", descPromo);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/facturasfaltantes.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rfactbonif.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rfactbonif.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiDatosMer(Integer codSublinea, String descSubLinea, Integer codDivision, String descDivision,
            String estado, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("codSublinea", codSublinea);
            param.put("descSubLinea", descSubLinea);
            param.put("codDivision", codDivision);
            param.put("descDivision", descDivision);
            param.put("estado", estado);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/datosMercaderias.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rdatosmer.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rdatosmer.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiDocuRes(Integer cod_deposito, String desc_deposito, String desde, String desde1,
            String hasta, String hasta1, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("cod_deposito", cod_deposito);
            param.put("desc_deposito", desc_deposito);
            param.put("desde", desde);
            param.put("desde1", desde1);
            param.put("hasta", hasta);
            param.put("hasta1", hasta1);
            param.put("usuImprime", usuImprime);

            System.out.println("cod_deposito " + cod_deposito);
            System.out.println("desc_deposito " + desc_deposito);
            System.out.println("desde " + desde);
            System.out.println("desde1 " + desde1);
            System.out.println("hasta " + hasta);
            System.out.println("hasta1 " + hasta1);
            System.out.println("usu_imprime " + usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/documentosReservan.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rdocures.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rdocures.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiExtractoProveedor(Integer cod_proveedor, String desc_proveedor,
            String cod_canal, String desc_canal, String desde, String desde1,
            String hasta, String hasta1, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("cod_proveedor", cod_proveedor);
            param.put("desc_proveedor", desc_proveedor);
            param.put("cod_canal", cod_canal);
            param.put("desc_canal", desc_canal);
            param.put("desde", desde);
            param.put("desde1", desde1);
            param.put("hasta", hasta);
            param.put("hasta1", hasta1);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/extractoProveedor.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rextraxtopro.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rextraxtopro.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiHistoricoPrecios(String desde, String desde1,
            String tipoVta, String descTipoVta, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("desde", desde);
            param.put("desde1", desde1);
            param.put("tipoVta", tipoVta);
            param.put("descTipoVta", descTipoVta);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/historicoPrecios.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rhistprec.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rhistprec.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiNotasRemisiones(BigDecimal nro_desde, BigDecimal nro_hasta,
            Integer cod_deposito, String desc_deposito, String desde, String desde1,
            String hasta, String hasta1, String estado, String conDetalles, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("nro_desde", nro_desde);
            param.put("nro_hasta", nro_hasta);
            param.put("cod_deposito", cod_deposito);
            param.put("desc_deposito", desc_deposito);
            param.put("desde", desde);
            param.put("desde1", desde1);
            param.put("hasta", hasta);
            param.put("hasta1", hasta1);
            param.put("estado", estado);
            param.put("usuImprime", usuImprime);

            String report = "";
            if (conDetalles.equals("S")) {
                report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/notasRemisionesDetalle.jasper");
            } else {
                report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/notasRemisiones.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rextraxtopro.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rextraxtopro.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiRetornoPrecios(Integer cod_sublinea, String desc_sublinea, String desde, String desde1,
            String hasta, String hasta1, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("cod_sublinea", cod_sublinea);
            param.put("desc_sublinea", desc_sublinea);
            param.put("desde", desde);
            param.put("desde1", desde1);
            param.put("hasta", hasta);
            param.put("hasta1", hasta1);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/retornoMercaderias.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rextraxtopro.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }
                if (tipo.equals("ARCH")) {
                    disposition = "attachment";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=rextraxtopro.xls");

                    httpServletResponse.setContentType("application/vnd.ms-excel");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JRXlsExporter exporterXLS = new JRXlsExporter();

                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiMercaSin(String sql, String desde, String hasta, String tipoDoc, String usuImprime, String zona, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("tipoDoc", tipoDoc);
            param.put("zona", zona);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/mercaderiasSinMovimientos.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=limercasin.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiMercaSin2(String sql, String desde, String hasta,
            String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/mercaderiasSinMovimientosCamion.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=limercasin2.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiMercaSin3(String sql, String desde, String hasta, String zona,
            String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("zona", zona);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/mercaderiasSinMovimientosCamionOC.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=limercasin3.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiExtractoCliente(String sql, String desde, String hasta,
            String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/extractoCliente.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=limercasin3.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void excelLiMercaSin(List<LiMercaSinDto> lista) {
        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            List<LiMercaSinDto> statFilterResults = lista;
            Iterator<LiMercaSinDto> statsIterator = statFilterResults.iterator();
            int i = 0;
            HSSFRow row;
            row = sheet.createRow((short) 0);
            row.createCell((short) 0).setCellValue("CodMerca");
            row.createCell((short) 1).setCellValue("CodBarra");
            row.createCell((short) 2).setCellValue("Descripcion");
            row.createCell((short) 3).setCellValue("Unidades");
            row.createCell((short) 4).setCellValue("Cajas");
            while (statsIterator.hasNext()) {
                i++;
                row = sheet.createRow((short) i);
                LiMercaSinDto perfBean = statsIterator.next();
                row.createCell((short) 0).setCellValue(perfBean.getCodMerca());
                row.createCell((short) 1).setCellValue(perfBean.getCodBarra());
                row.createCell((short) 2).setCellValue(perfBean.getXdesc());
                row.createCell((short) 3).setCellValue(perfBean.getUnidades());
                row.createCell((short) 4).setCellValue(perfBean.getCajas());

            }
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment; filename=limercasin.xls");

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

    public void excelLiMercaSin2(List<LiMercaSinDto> lista) {
        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            List<LiMercaSinDto> statFilterResults = lista;
            Iterator<LiMercaSinDto> statsIterator = statFilterResults.iterator();
            int i = 0;
            HSSFRow row;
            row = sheet.createRow((short) 0);
            row.createCell((short) 0).setCellValue("CodMerca");
            row.createCell((short) 1).setCellValue("Descripcion");
            row.createCell((short) 2).setCellValue("Deposito");
            row.createCell((short) 3).setCellValue("Unidades");
            row.createCell((short) 4).setCellValue("Cajas");
            while (statsIterator.hasNext()) {
                i++;
                row = sheet.createRow((short) i);
                LiMercaSinDto perfBean = statsIterator.next();
                row.createCell((short) 0).setCellValue(perfBean.getCodMerca());
                row.createCell((short) 1).setCellValue(perfBean.getCodBarra());
                row.createCell((short) 2).setCellValue(perfBean.getXdesc());
                row.createCell((short) 3).setCellValue(perfBean.getUnidades());
                row.createCell((short) 4).setCellValue(perfBean.getCajas());

            }
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment; filename=limercasin.xls");

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

    public void reporteLiEnvios1(String sql, String desde, String hasta,
            String origen, String destino, String anulados, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("origen", origen);
            param.put("destino", destino);
            param.put("anulados", anulados);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/envios1.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=lienvios.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void excelLiExtractoCliente(List<Object[]> lista) {
        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            List<Object[]> statFilterResults = lista;
            Iterator<Object[]> statsIterator = statFilterResults.iterator();
            int i = 0;
            HSSFRow row;
            row = sheet.createRow((short) 0);
            row.createCell((short) 0).setCellValue("CodCliente");
            row.createCell((short) 1).setCellValue("CtipoDocum");
            row.createCell((short) 2).setCellValue("ndocumCheq");
            row.createCell((short) 3).setCellValue("fac_ctipo_docum");
            row.createCell((short) 4).setCellValue("nrofact");
            row.createCell((short) 5).setCellValue("fvenc");
            row.createCell((short) 6).setCellValue("fmovim");
            row.createCell((short) 7).setCellValue("xnombre");
            row.createCell((short) 8).setCellValue("nplazo_credito");
            row.createCell((short) 9).setCellValue("imovim");
            row.createCell((short) 10).setCellValue("mindice");
            while (statsIterator.hasNext()) {
                i++;
                row = sheet.createRow((short) i);
                Object[] perfBean = statsIterator.next();
                row.createCell((short) 0).setCellValue(perfBean[0].toString());
                row.createCell((short) 1).setCellValue(perfBean[1].toString());
                row.createCell((short) 2).setCellValue(perfBean[2].toString());
                row.createCell((short) 3).setCellValue(perfBean[3].toString());
                row.createCell((short) 4).setCellValue(perfBean[4].toString());
                row.createCell((short) 5).setCellValue(perfBean[5].toString());
                row.createCell((short) 6).setCellValue(perfBean[6].toString());
                row.createCell((short) 7).setCellValue(perfBean[7].toString());
                row.createCell((short) 8).setCellValue(perfBean[8].toString());
                row.createCell((short) 9).setCellValue(perfBean[9].toString());
                row.createCell((short) 10).setCellValue(perfBean[10].toString());

            }
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment; filename=liextractocliente.xls");

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

    public void reporteLiFactServ(String sql, String desde, String hasta,
            String usuImprime, String tipo, boolean conDetalles) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("usuImprime", usuImprime);

            String report = "";

            if (conDetalles) {
                report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/factServDeta.jasper");
            } else {
                report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/factServ.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=lifactserv.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void excelLifactServ(List<Object[]> lista) {
        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            List<Object[]> statFilterResults = lista;
            Iterator<Object[]> statsIterator = statFilterResults.iterator();
            int i = 0;
            HSSFRow row;
            row = sheet.createRow((short) 0);
            row.createCell((short) 0).setCellValue("factura");
            row.createCell((short) 1).setCellValue("fecha");
            row.createCell((short) 2).setCellValue("cliente");
            row.createCell((short) 3).setCellValue("exentas");
            row.createCell((short) 4).setCellValue("gravadas10");
            row.createCell((short) 5).setCellValue("gravadas5");
            row.createCell((short) 6).setCellValue("impuestos10");
            row.createCell((short) 7).setCellValue("impuestos5");
            row.createCell((short) 8).setCellValue("total");
            row.createCell((short) 9).setCellValue("saldo");
            row.createCell((short) 10).setCellValue("estado");
            while (statsIterator.hasNext()) {
                i++;
                row = sheet.createRow((short) i);
                Object[] perfBean = statsIterator.next();
                row.createCell((short) 0).setCellValue(perfBean[0].toString());
                row.createCell((short) 1).setCellValue(perfBean[1].toString());
                row.createCell((short) 2).setCellValue(perfBean[2].toString());
                row.createCell((short) 3).setCellValue(perfBean[3].toString());
                row.createCell((short) 4).setCellValue(perfBean[4].toString());
                row.createCell((short) 5).setCellValue(perfBean[5].toString());
                row.createCell((short) 6).setCellValue(perfBean[6].toString());
                row.createCell((short) 7).setCellValue(perfBean[7].toString());
                row.createCell((short) 8).setCellValue(perfBean[8].toString());
                row.createCell((short) 9).setCellValue(perfBean[9].toString());
                row.createCell((short) 10).setCellValue(perfBean[10].toString());

            }
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment; filename=lifactServ.xls");

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

    public void reporteLiCamiones(String sql, String desde, String hasta,
            String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("usuImprime", usuImprime);

            String report = "";

            report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/camiones.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=lifactserv.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void excelLiCamiones(List<Object[]> lista) {
        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            List<Object[]> statFilterResults = lista;
            Iterator<Object[]> statsIterator = statFilterResults.iterator();
            int i = 0;
            HSSFRow row;
            row = sheet.createRow((short) 0);
            row.createCell((short) 0).setCellValue("cod_merca");
            row.createCell((short) 1).setCellValue("cod_barra");
            row.createCell((short) 2).setCellValue("xdesc");
            row.createCell((short) 3).setCellValue("fecha");
            row.createCell((short) 4).setCellValue("cod_depo");
            row.createCell((short) 5).setCellValue("cajas_ini");
            row.createCell((short) 6).setCellValue("unid_ini");
            row.createCell((short) 7).setCellValue("mov_cajas");
            row.createCell((short) 8).setCellValue("mov_unid");
            while (statsIterator.hasNext()) {
                i++;
                row = sheet.createRow((short) i);
                Object[] perfBean = statsIterator.next();
                row.createCell((short) 0).setCellValue(perfBean[0].toString());
                row.createCell((short) 1).setCellValue(perfBean[1].toString());
                row.createCell((short) 2).setCellValue(perfBean[2].toString());
                row.createCell((short) 3).setCellValue(perfBean[3].toString());
                row.createCell((short) 4).setCellValue(perfBean[4].toString());
                row.createCell((short) 5).setCellValue(perfBean[5].toString());
                row.createCell((short) 6).setCellValue(perfBean[6].toString());
                row.createCell((short) 7).setCellValue(perfBean[7].toString());
                row.createCell((short) 8).setCellValue(perfBean[8].toString());

            }
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment; filename=lifactServ.xls");

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

    public void reporteLiSalidas(String sql, String desde, String hasta,
            Integer nroDesde, Integer nroHasta, String zona, String anulados,
            String usuImprime, String tipo, int opcion) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("nroDesde", nroDesde);
            param.put("nroHasta", nroHasta);
            param.put("zona", zona);
            param.put("anulados", anulados);
            param.put("usuImprime", usuImprime);

            String report = "";
            /*normal*/
            if (opcion == 1) {
                report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/salidas.jasper");
            } else if (opcion == 2) {
                report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/salidasDet.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=lienvios.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void exportarExcel(String[] columnas, List<Object[]> lista, String nombre) {
        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            int i = 0;
            HSSFRow row;
            row = sheet.createRow((short) 0);

            //titulos de las columnas
            for (int j = 0; j < columnas.length; j++) {
                row.createCell((short) j).setCellValue(columnas[j]);
            }

            //datos de la lista proporcionada
            for (int h = 0; h < lista.size(); h++) {
                row = sheet.createRow((short) h + 1);
                Object[] objeto = lista.get(h);

                for (int k = 0; k < columnas.length; k++) {
                    row.createCell((short) k).setCellValue(objeto[k] + "".toString());
                }

            }

            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("Content-disposition", "attachment; filename=" + nombre + ".xls");

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

    public void reporteLiVtaRetorno(String sql, String desde, String hasta, String linea, String usuImprime, String sublinea, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("linea", linea);
            param.put("sublinea", sublinea);
            param.put("usuImprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/ventasRetorno.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=ivtaretorno.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiFactAnul(String sql, String desde, String hasta, String entregador, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("nomEntregador", entregador);
            param.put("usu_imprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/factAnul.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=lifactAnul.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiCheqEmis(String sql, String desde, String hasta,
            String desde1, String hasta1,
            String desde2, String hasta2,
            String proveedor, String banco, String usuImprime, String tipo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("desde", desde);
            param.put("hasta", hasta);
            param.put("desde1", desde1);
            param.put("hasta1", hasta1);
            param.put("desde2", desde2);
            param.put("hasta2", hasta2);
            param.put("proveedor", proveedor);
            param.put("banco", banco);
            param.put("usu_imprime", usuImprime);

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/chequesEmis.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=lifactAnul.pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reporteLiRecibos(String sql, Date fechaDesde, Date fechaHasta,
            Long nroRecDesde, Long nroRecHasta, String clientesRepo, String zonaDes,
            String usuImprime, String tipo, String nombreReporte, String filename, String sqlDetalle, String sqlDetalleRecibo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("fechaDesde", fechaDesde);
            param.put("fechaHasta", fechaHasta);
            param.put("nroReciboDesde", nroRecDesde);
            param.put("nroReciboHasta", nroRecHasta);
            param.put("clientesRepo", clientesRepo);
            param.put("zonaDes", zonaDes);
            param.put("usu_imprime", usuImprime);

            //JLVC 02-01-2020; se obtiene sqlDetalle solo si es distinto a null
            if (sqlDetalle != null) {
                param.put("sqlDet", sqlDetalle);
                if ("reciboFacDetPC".equals(nombreReporte) && sqlDetalleRecibo != null) {
                    param.put("sqlDetRec", sqlDetalleRecibo);
                } else {
                    param.put("sqlDetRec", null);
                }
            } else {
                param.put("sqlDet", null);
            }

            //JLVC 30-12-2019; se obtiene el SUBREPORT_DIR para pasar por parametro al reporte principal y este al subReport
            String subReportDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/") + "\\";
            System.out.println("    subReportDir: " + subReportDir);
            if ("reciboFac".equals(nombreReporte)) {
                param.put("SUBREPORT_DIR", subReportDir);
            }

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/" + nombreReporte + ".jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=" + filename + ".pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }

        } catch (IOException | JRException e) {
            System.out.println(e);
        }
    }

    public void reporteLiRecibosCom(String sql, Date fechaDesde, Date fechaHasta,
            Long nroRecDesde, Long nroRecHasta, Proveedores provSeleccionado, String usuImprime, String tipo, 
            String nombreReporte, String filename, String sqlDetalle, String sqlDetalleRecibo) {
        try {

            Map param = new HashMap();
            param.put("sql", sql);
            param.put("fechaDesde", fechaDesde);
            param.put("fechaHasta", fechaHasta);
            param.put("nroReciboDesde", nroRecDesde);
            param.put("nroReciboHasta", nroRecHasta);
            param.put("usu_imprime", usuImprime);
            
            if (provSeleccionado != null) {
                param.put("provCod", provSeleccionado.getCodProveed());
                param.put("provDesc", (provSeleccionado.getXnombre()!= null)?provSeleccionado.getXnombre():"");
            }else{
                param.put("provCod", "TODOS");
                param.put("provDesc", " ");
            }
            param.put("sqlDet", sqlDetalle);
            param.put("sqlDetRec", sqlDetalleRecibo);
            
            //JLVC 30-12-2019; se obtiene el SUBREPORT_DIR para pasar por parametro al reporte principal y este al subReport
            if ("reciboProvPP".equals(nombreReporte) || "reciboProvDetPP".equals(nombreReporte) || "reciboProvDetND".equals(nombreReporte)) {
                String subReportDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/") + "\\";
                param.put("SUBREPORT_DIR", subReportDir);
            }

            String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/classes/pdf/" + nombreReporte + ".jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, conexion);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            if (tipo.equals("IMPR")) {
                JasperPrintManager.printReport(jasperPrint, false);
            } else {
                String disposition = "";
                if (tipo.equals("VIST")) {
                    disposition = "inline";

                    httpServletResponse.addHeader("Content-disposition", disposition + "; filename=" + filename + ".pdf");
                    httpServletResponse.addHeader("Content-type", "application/pdf");

                    ServletOutputStream servletStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);

                    FacesContext.getCurrentInstance().responseComplete();
                }

            }
        } catch (IOException | JRException e) {
            System.out.println(e);
        }
    }
}
