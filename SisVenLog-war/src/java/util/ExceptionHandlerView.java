/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author Edu
 */

public class ExceptionHandlerView {
    /**
     * Esta funcion nos permite convertir el stackTrace en un String, necesario
     * para poder imprimirlos al log debido a cambios en como Java los maneja
     * internamente
     *
     * @param e Excepcion de la que queremos el StackTrace
     * @return StackTrace de la excepcion en forma de String
     */
    public static String getStackTrace(Exception e) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        e.printStackTrace(pWriter);
        return sWriter.toString();
    }
}
