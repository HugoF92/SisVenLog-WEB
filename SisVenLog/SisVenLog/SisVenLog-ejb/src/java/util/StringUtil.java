/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

    import java.util.List;
    import entidad.Clientes;

/**
 *
 * @author jvera
 */
public class StringUtil {
    public static String convertirListaAString(List<Clientes> lista){
        String listaString = "";
        if (!lista.isEmpty()) {
            for (int i = 0; i < lista.size(); i++) {
                if (i == (lista.size()-1)) {
                    listaString += lista.get(i).getCodCliente();
                }else{
                    listaString += lista.get(i).getCodCliente()+ ", ";
                }
            }
        }            
        return listaString;
    }
}
