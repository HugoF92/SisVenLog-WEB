/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dto.RecibosComprasDto;
import dto.RecibosFacturasComprasIvaInclNoIncl;
import dto.RecibosFacturasVentasIvaInclNoIncl;
import dto.RecibosVentasDto;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Asus
 */
public class FileWritter {

    public static void guardarDatosRecibosVentas(String path, List<RecibosVentasDto> datos) {
        String nombreArchivo = "revtacont.txt"; //contenido en formato csv
        String saltoDeLinea = "\n";
        String separador = ",";
        String aGuardarEnArchivo = "ctipo_docum,nro_cuota,frecibo,nrecibo,ctipo,ndocum ,ffactur,iefectivo,nro_cheque,ipagado,moneda,cotizacion";

        int i = 0;
        while (datos.size() > 0) {
            String linea = datos.get(i).getCtipoDocum()
                    + separador + Long.toString(datos.get(i).getNcuota())
                    + separador + dateToString(datos.get(i).getFrecibo())
                    + separador + Long.toString(datos.get(i).getNrecibo())
                    + separador + datos.get(i).getCtipo()
                    + separador + Long.toString(datos.get(i).getNdocum())
                    + separador + dateToString(datos.get(i).getFfactur())
                    + separador + Double.toString(datos.get(i).getIefectivo())
                    + separador + datos.get(i).getNroCheque()
                    + separador + Double.toString(datos.get(i).getIpagado())
                    + separador + Double.toString(datos.get(i).getMoneda())
                    + separador + Double.toString(datos.get(i).getCotizacion());
            aGuardarEnArchivo += saltoDeLinea + linea;
            i++;
        }

        writeUsingFileWriter(path + nombreArchivo, aGuardarEnArchivo);
    }
    
    public static void guardarDatosRecibosCompras(String path, List<RecibosComprasDto> datos) {
        String nombreArchivo = "recomcont.txt"; //contenido en formato csv
        String saltoDeLinea = "\n";
        String separador = ",";
        String aGuardarEnArchivo = "ctipo_docum,nro_cuota,frecibo,nrecibo,nrofact,ctipo,ffactur,iefectivo,nro_cheque,ipagado,"
                + "cod_contable,moneda,cotizacion,cod_proveed,itotal,ntimbrado,fact_timbrado,ntimbrado,nota_timbrado";

        int i = 0;
        while (datos.size() > 0) {
            String linea = datos.get(i).getCtipoDocum()
                    + separador + Long.toString(datos.get(i).getNcuota())
                    + separador + dateToString(datos.get(i).getFrecibo())
                    + separador + Long.toString(datos.get(i).getNrecibo())
                    + separador + datos.get(i).getNrofact()
                    + separador + datos.get(i).getCtipo()
                    + separador + dateToString(datos.get(i).getFfactur())
                    + separador + Long.toString(datos.get(i).getIefectivo())
                    + separador + datos.get(i).getNroCheque()
                    + separador + Long.toString(datos.get(i).getIpagado())
                    + separador + Long.toString(datos.get(i).getMoneda())
                    + separador + Long.toString(datos.get(i).getCotizacion())
                    + separador + Short.toString(datos.get(i).getCodProveed())
                    + separador + Long.toString(datos.get(i).getItotal())
                    + separador + datos.get(i).getNtimbrado().toString()
                    + separador + datos.get(i).getFactTimbrado().toString()
                    + separador + datos.get(i).getNotaTimbrado();
            aGuardarEnArchivo += saltoDeLinea + linea;
            i++;
        }

        writeUsingFileWriter(path + nombreArchivo, aGuardarEnArchivo);
    }
    
    public static void guardarDatosFacturasVentas(String path, List<RecibosFacturasVentasIvaInclNoIncl> datos) {
        String nombreArchivo = "vtascont.txt"; //contenido en formato csv
        String saltoDeLinea = "\n";
        String separador = ",";
        String aGuardarEnArchivo = "xrazon_social,ffactur,nrofact,ctipo_docum,mtipo_papel,nro_docum_ini,nro_docum_fin,"
                + "ntimbrado,TTOTAL,xruc,xfactura,texentas,tgravadas_10,tgravadas_5,timpuestos_10,timpuestos_5";

        int i = 0;
        while (datos.size() > 0) {
            String linea = datos.get(i).getXrazonSocial()
                    + separador + dateToString(datos.get(i).getFfactur())
                    + separador + Long.toString(datos.get(i).getNrofact())
                    + separador + datos.get(i).getCtipoDocum()
                    + separador + Character.toString(datos.get(i).getMtipoPapel())
                    + separador + Long.toString(datos.get(i).getNroDocumIni())
                    + separador + Long.toString(datos.get(i).getNroDocumFin())
                    + separador + datos.get(i).getNtimbrado()
                    + separador + Long.toString(datos.get(i).getTtotal())
                    + separador + datos.get(i).getXruc()
                    + separador + datos.get(i).getXfactura()
                    + separador + Double.toString(datos.get(i).getTexentas())
                    + separador + Double.toString(datos.get(i).getTgravadas10())
                    + separador + Double.toString(datos.get(i).getTgravadas5())
                    + separador + Double.toString(datos.get(i).getTimpuestos10())
                    + separador + Double.toString(datos.get(i).getTimpuestos5());
            aGuardarEnArchivo += saltoDeLinea + linea;
            i++;
        }

        writeUsingFileWriter(path + nombreArchivo, aGuardarEnArchivo);
    }
    
    public static void guardarDatosFacturasCompras(String path, List<RecibosFacturasComprasIvaInclNoIncl> datos) {
        String nombreArchivo = "compcont.txt"; //contenido en formato csv
        String saltoDeLinea = "\n";
        String separador = ",";
        String aGuardarEnArchivo = "xnombre,ffactur,nrofact,ctipo_docum,TTOTAL,xruc,xfactura,"
                + "ntimbrado,texentas,tgravadas_10,tgravadas_5,timpuestos_10,timpuestos_5";

        int i = 0;
        while (datos.size() > 0) {
            String linea = datos.get(i).getXnombre()
                    + separador + dateToString(datos.get(i).getFfactur())
                    + separador + Long.toString(datos.get(i).getNrofact())
                    + separador + datos.get(i).getCtipoDocum()
                    + separador + Double.toString(datos.get(i).getTtotal())
                    + separador + datos.get(i).getXruc()
                    + separador + datos.get(i).getXfactura()
                    + separador + datos.get(i).getNtimbrado()
                    + separador + Double.toString(datos.get(i).getTexentas())
                    + separador + Double.toString(datos.get(i).getTgravadas10())
                    + separador + Double.toString(datos.get(i).getTgravadas5())
                    + separador + Double.toString(datos.get(i).getTimpuestos10())
                    + separador + Double.toString(datos.get(i).getTimpuestos5());
            aGuardarEnArchivo += saltoDeLinea + linea;
            i++;
        }

        writeUsingFileWriter(path + nombreArchivo, aGuardarEnArchivo);
    }

    private static void writeUsingFileWriter(String path, String data) {
        File file = new File(path);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String dateToString(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }
}
