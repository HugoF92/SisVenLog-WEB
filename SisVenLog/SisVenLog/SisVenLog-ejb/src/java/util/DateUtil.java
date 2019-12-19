/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Edu
 */
public class DateUtil {
    
    private DateUtil(){}
    
    public static String getFechaActual() throws ParseException{
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(ahora);
    }
    
    public static String formaterDateToString(Date fecha){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String date = DATE_FORMAT.format(fecha);
        return date;
    }
    
    public static String obtenerDiaSemana(Date fecha) {
        String[] dias = {"domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int numeroDia = cal.get(Calendar.DAY_OF_WEEK);
        return dias[numeroDia - 1];
    }
    
    public static String getHoraActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("HH:mm");
        return formateador.format(ahora);
    }
    
     public static String formaterDateTimeToString(Date fecha){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh.mm.ss");
        String date = DATE_FORMAT.format(fecha);
        return date;
    }
}
