/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.LiVentas;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Clara
 */
@Stateless
public class LiVentasFacade {
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    protected EntityManager getEntityManager() {
        return em;
    }

    public LiVentasFacade() {
        
    }
    
    public Map<String, Map<String, List<LiVentas>>> obtenerConsultaVendedores(String consultaVenta, String consultaNotas, String ventaZona, String notaZona, String ventaRuta, String notaRuta){   
        //realizar agrupacion por vendedor
        Map<String, Map<String, List<LiVentas>>> retorno = new HashMap<String, Map<String, List<LiVentas>>>();
        Map<String, List<LiVentas>> vendedoresGroup = new HashMap<String, List<LiVentas>>();
        Map<String, List<LiVentas>> zonasGroup = new HashMap<String, List<LiVentas>>();
        Map<String, List<LiVentas>> rutasGroup = new HashMap<String, List<LiVentas>>();
        vendedoresGroup = obtenerVendedor(consultaVenta, consultaNotas);
        retorno.put("VENDEDOR", vendedoresGroup);       
        rutasGroup = obtenerRuta(ventaRuta, notaRuta);
        retorno.put("RUTA", rutasGroup);    
        zonasGroup = obtenerZona(ventaZona, notaZona);
        retorno.put("ZONA", zonasGroup); 
        Map<Integer, BigDecimal> montoTotal = new HashMap<Integer, BigDecimal>();
        for(Integer i = 1; i < 13; i++){
            //inicializamos con todos los meses a valor 0
            montoTotal.put(i, new BigDecimal(0));                
        }
        for (Map.Entry<String, List<LiVentas>> entry : zonasGroup.entrySet()) {
            List<LiVentas> values = entry.getValue();
            Map<Integer, List<LiVentas>> groupMes = new HashMap<Integer, List<LiVentas>>();
            for (LiVentas r : values) {
                Integer key = r.getNmes();
                if(groupMes.containsKey(key)){
                    List<LiVentas> list = groupMes.get(key);
                    list.add(r);
                }else{
                    List<LiVentas> list = new ArrayList<LiVentas>();
                    list.add(r);
                    groupMes.put(key, list);
                }
            }
            Map<Integer, BigDecimal> montoPorMes = new HashMap<Integer, BigDecimal>();
            //sumarizacion para la zona
            for(Integer i = 1; i < 13; i++){
                BigDecimal suma = new BigDecimal(0);
                if(groupMes.containsKey(i)){
                    List<LiVentas> valuesMes = groupMes.get(i);                    
                    for(LiVentas m : valuesMes){
                        suma = suma.add(m.getMonto());
                    }
                    montoPorMes.put(i, suma);
                }else{
                    montoPorMes.put(i, suma); 
                }
                //sumarizacion del total de ventas
                //montoTotal.put(i, suma); 
                //System.out.print(i);
                if(montoTotal.containsKey(i)){
                   BigDecimal valor = montoTotal.get(i);
                   BigDecimal sumaTotal = valor.add(suma);
                   montoTotal.put(i, sumaTotal);
                }else{
                    montoTotal.put(i, suma); 
                }
            }
            LiVentas ventaMonto = new LiVentas();
            ventaMonto.setNmes(13);
            ventaMonto.setRazonSocialCliente("ZZONATOTAL");
            ventaMonto.setMontoPorMes(montoPorMes);
            values.add(ventaMonto);
        }
        /*---------------------------------------------------------------------------------*/
        //enviar un valor mas en el mapa para representar el total de la venta en el rango de fechas
        Map<String, List<LiVentas>> totalesVenta = new HashMap<String, List<LiVentas>>();
        LiVentas v = new LiVentas();
        v.setNmes(13);
        v.setMontoPorMes(montoTotal);
        List<LiVentas> ventas = new ArrayList<LiVentas>();
        ventas.add(v);
        totalesVenta.put("13", ventas);
        /*----------------------------------------------------------------------------------*/
        //incluir total de la venta al map que se retorna para generar el reporte
        retorno.put("TOTAL_VENTA", totalesVenta); 
        return retorno;
    }
    
    private List<LiVentas> generarDatosPrueba(String tipo){
        Random r = new Random();
        List<LiVentas> listadoVentas = new ArrayList<LiVentas>();
        for (Integer j = 0; j < 5; j++){
            String descripcion = tipo.concat("-").concat(randomIdentifier());
            ///String codigo = r.nextInt((9000 - 1000) + 1) + 1000;
            for(Integer n = 0; n < 5; n++){
                String cliente = "CLIENTE - ".concat(randomIdentifier());
                Integer codigoCliente = r.nextInt((9000 - 1000) + 1) + 1000;
                for(Integer i = 0; i < 5; i++){
                    LiVentas e = new LiVentas();
                    e.setCodCliente(codigoCliente);
                    e.setRazonSocialCliente(cliente);
                    //e.setCodigo(codigo);
                    e.setDescripcion(descripcion);
                    e.setNmes(r.nextInt((12 - 1) + 1) + 1);
                    e.setMonto(generateRandomBigDecimalFromRange(new BigDecimal(1000), new BigDecimal(5000)));
                    listadoVentas.add(e);
                }
            }
            
        }
        System.out.println("DATOS TIPO: " + tipo);
        for(LiVentas v: listadoVentas){
            System.out.println(String.format("MES [%s], COD CLIENTE [%s], RAZON SOCIAL CLIENTE [%s], MONTO [%s], CODIGO [%s], DESCRIPCION [%s]", 
                   String.valueOf( v.getNmes()), String.valueOf(v.getCodCliente()), v.getRazonSocialCliente(), v.getMonto().toString(), String.valueOf(v.getCodigo()), v.getDescripcion()));
        }
        System.out.println("=============================================================================================================================================================");
        return listadoVentas;
    }
    
    public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
    
    public String randomIdentifier() {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
        // consider using a Map<String,Boolean> to say whether the identifier is being used or not 
        final Set<String> identifiers = new HashSet<String>();
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }
    
    private Map<String, List<LiVentas>> obtenerVendedor(String consultaVenta, String consultaNotas){        
        String consultaSql = " select facturas.nmes,  "+
            " facturas.cod_cliente,   "+
            " facturas.xrazon_social, "+
            " case when notas.ctipo_docum IS NOT NULL THEN  "+
            " facturas.iventas - notas.ttotal "+
            " ELSE "+
            " facturas.iventas "+
            " END as total_ventas, "+
            " facturas.cod_vendedor,  "+
            " facturas.xnombre "+
            " FROM ";
        consultaSql = consultaSql.concat("("+consultaVenta+") facturas  LEFT JOIN ");
        
        consultaSql = consultaSql.concat("("+ consultaNotas +") notas  ");
        String sqlJoin = " ON "+
            "   (notas.nmes= facturas.nmes "+
            "   and notas.cod_cliente = facturas.cod_cliente "+
            "   and notas.cod_vendedor = facturas.cod_vendedor) ";
        consultaSql = consultaSql.concat(sqlJoin);
        System.out.println("CONSULTA SQL FINAL DE VENDEDOR");
        System.out.println(consultaSql);
        Query qVenta = em.createNativeQuery(consultaSql);
        List<Object[]> resultados = qVenta.getResultList();
        List<LiVentas> ventas = new ArrayList<LiVentas>();
        for(Object[] o : resultados) {
            LiVentas venta = new LiVentas();
            if(o[0]!=null) {
                venta.setNmes(Integer.parseInt(o[0].toString()));                
            }
            if(o[1]!=null) {
                venta.setCodCliente(Integer.parseInt(o[1].toString()));                
            }
            if(o[2]!=null) {
                venta.setRazonSocialCliente(o[2].toString());                
            }
            if(o[3]!=null) {
                venta.setMonto(new BigDecimal(o[3].toString()));                
            }
            if(o[4]!=null) {
                venta.setCodigo(o[4].toString());                
            }
            if(o[5]!=null) {
                venta.setDescripcion(o[5].toString());                
            }    
            ventas.add(venta);
        }   
        
        Map<String, List<LiVentas>> vendedoresGroup = new HashMap<String, List<LiVentas>>();
        //----------PARA LAS PRUEBAS
        // ventas = generarDatosPrueba("VENDEDOR");
        //----------FIN PRUEBAS
        //group by vendedor        
        for (LiVentas r : ventas) {
            String key = r.getCodigo();
            if(vendedoresGroup.containsKey(key)){
                List<LiVentas> list = vendedoresGroup.get(key);
                list.add(r);
            }else{
                List<LiVentas> list = new ArrayList<LiVentas>();
                list.add(r);
                vendedoresGroup.put(key, list);
            }
        }
        return vendedoresGroup;
    }
    
    private Map<String, List<LiVentas>> obtenerZona(String consultaVenta, String consultaNotas){        
        String consultaSql = " select facturas.nmes,  "+
            " facturas.cod_cliente,   "+
            " facturas.xrazon_social, "+
            " case when notas.ctipo_docum IS NOT NULL THEN  "+
            " facturas.iventas - notas.ttotal "+
            " ELSE "+
            " facturas.iventas "+
            " END as total_ventas, "+
            " facturas.cod_zona,  "+
            " facturas.xdesc_zona "+
            " FROM ";
        consultaSql = consultaSql.concat("("+consultaVenta+") facturas  LEFT JOIN ");
        
        consultaSql = consultaSql.concat("("+ consultaNotas +") notas  ");
        String sqlJoin = " ON "+
            "   (notas.nmes= facturas.nmes "+
            "   and notas.cod_cliente = facturas.cod_cliente "+
            "   and notas.cod_zona = facturas.cod_zona) ";
        consultaSql = consultaSql.concat(sqlJoin);
        System.out.println("CONSULTA SQL FINAL DE ZONA");
        System.out.println(consultaSql);
        Query qVenta = em.createNativeQuery(consultaSql);
        List<Object[]> resultados = qVenta.getResultList();
        List<LiVentas> ventas = new ArrayList<LiVentas>();
        for(Object[] o : resultados) {
            LiVentas venta = new LiVentas();
            if(o[0]!=null) {
                venta.setNmes(Integer.parseInt(o[0].toString()));                
            }
            if(o[1]!=null) {
                venta.setCodCliente(Integer.parseInt(o[1].toString()));                
            }
            if(o[2]!=null) {
                venta.setRazonSocialCliente(o[2].toString());                
            }
            if(o[3]!=null) {
                venta.setMonto(new BigDecimal(o[3].toString()));                
            }
            if(o[4]!=null) {
                venta.setCodigo(o[4].toString());                
            }
            if(o[5]!=null) {
                venta.setDescripcion(o[5].toString());                
            }    
            ventas.add(venta);
        }   
        
        Map<String, List<LiVentas>> zonasGroup = new HashMap<String, List<LiVentas>>();
        //----------PARA LAS PRUEBAS
        // ventas = generarDatosPrueba("ZONA");
        //----------FIN PRUEBAS
        //group by vendedor        
        for (LiVentas r : ventas) {
            String key = r.getCodigo();
            if(zonasGroup.containsKey(key)){
                List<LiVentas> list = zonasGroup.get(key);
                list.add(r);
            }else{
                List<LiVentas> list = new ArrayList<LiVentas>();
                list.add(r);
                zonasGroup.put(key, list);
            }
        }
        return zonasGroup;
    }
    
    private Map<String, List<LiVentas>> obtenerRuta(String consultaVenta, String consultaNotas){        
        String consultaSql = " select facturas.nmes,  "+
            " facturas.cod_cliente,   "+
            " facturas.xrazon_social, "+
            " case when notas.ctipo_docum IS NOT NULL THEN  "+
            " facturas.iventas - notas.ttotal "+
            " ELSE "+
            " facturas.iventas "+
            " END as total_ventas, "+
            " facturas.cod_ruta,  "+
            " facturas.xdesc_ruta "+
            " FROM ";
        consultaSql = consultaSql.concat("("+consultaVenta+") facturas  LEFT JOIN ");
        
        consultaSql = consultaSql.concat("("+ consultaNotas +") notas  ");
        String sqlJoin = " ON "+
            "   (notas.nmes= facturas.nmes "+
            "   and notas.cod_cliente = facturas.cod_cliente "+
            "   and notas.cod_ruta = facturas.cod_ruta) ";
        consultaSql = consultaSql.concat(sqlJoin);
        System.out.println("CONSULTA SQL FINAL DE RUTA");
        System.out.println(consultaSql);
        Query qVenta = em.createNativeQuery(consultaSql);
        List<Object[]> resultados = qVenta.getResultList();
        List<LiVentas> ventas = new ArrayList<LiVentas>();
        for(Object[] o : resultados) {
            LiVentas venta = new LiVentas();
            if(o[0]!=null) {
                venta.setNmes(Integer.parseInt(o[0].toString()));                
            }
            if(o[1]!=null) {
                venta.setCodCliente(Integer.parseInt(o[1].toString()));                
            }
            if(o[2]!=null) {
                venta.setRazonSocialCliente(o[2].toString());                
            }
            if(o[3]!=null) {
                venta.setMonto(new BigDecimal(o[3].toString()));                
            }
            if(o[4]!=null) {
                venta.setCodigo(o[4].toString());                
            }
            if(o[5]!=null) {
                venta.setDescripcion(o[5].toString());                
            }    
            ventas.add(venta);
        }   
        
        Map<String, List<LiVentas>> rutasGroup = new HashMap<String, List<LiVentas>>();
        //----------PARA LAS PRUEBAS
        // ventas = generarDatosPrueba("RUTA");
        //----------FIN PRUEBAS
        //group by vendedor        
        for (LiVentas r : ventas) {
            String key = r.getCodigo();
            if(rutasGroup.containsKey(key)){
                List<LiVentas> list = rutasGroup.get(key);
                list.add(r);
            }else{
                List<LiVentas> list = new ArrayList<LiVentas>();
                list.add(r);
                rutasGroup.put(key, list);
            }
        }
        return rutasGroup;
    }
    
}
