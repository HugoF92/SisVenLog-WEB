/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.LiVentas;
import dto.LiVentasCab;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    
    public List<LiVentas> obtenerListadoExcell(String consultaVenta, String consultaNotas){         
        String consultaSql = " select facturas.cod_vendedor, facturas.xnombre, facturas.cod_zona, facturas.xdesc_zona, facturas.cod_ruta, facturas.xdesc_ruta, "+
            " facturas.cod_cliente,   "+
            " facturas.xrazon_social, "+
            " facturas.nmes, " +
            " case when notas.ctipo_docum IS NOT NULL THEN  "+
            " facturas.iventas - notas.ttotal "+
            " ELSE "+
            " facturas.iventas "+
            " END as total_ventas "+
            " FROM ";
        consultaSql = consultaSql.concat("("+consultaVenta+") facturas  LEFT JOIN ");
        
        consultaSql = consultaSql.concat("("+ consultaNotas +") notas  ");
        String sqlJoin = " ON "+
            "   (notas.nmes= facturas.nmes "+
            "   and notas.cod_cliente = facturas.cod_cliente "+
            "   and notas.cod_vendedor = facturas.cod_vendedor and notas.cod_ruta = facturas.cod_ruta and notas.cod_zona = facturas.cod_zona) "+
            " order by facturas.cod_vendedor, facturas.cod_zona, facturas.cod_ruta, facturas.cod_cliente ";        
        consultaSql = consultaSql.concat(sqlJoin);
        System.out.println("CONSULTA SQL FINAL DE VENDEDOR");
        System.out.println(consultaSql);
        Query qVenta = em.createNativeQuery(consultaSql);
        List<Object[]> resultados = qVenta.getResultList();
        List<LiVentas> ventas = new ArrayList<LiVentas>();
        for(Object[] o : resultados) {
            LiVentas venta = new LiVentas();            
            if(o[0]!=null) {
                venta.setCodVendedor(new Short(o[0].toString()));
            }
            if(o[1]!=null) {
                venta.setDescripcionVendedor(o[1].toString());
            }
            if(o[2]!=null) {
                venta.setCodZona(o[2].toString());
            }
            if(o[3]!=null) {
                venta.setDescripcionZona(o[3].toString());
            }
            if(o[4]!=null) {
                venta.setCodRuta(new Short(o[4].toString()));
            }
            if(o[5]!=null) {
                venta.setDescripcionRunta(o[5].toString());
            }
            if(o[6]!=null) {
                venta.setCodCliente(Integer.parseInt(o[6].toString()));                
            }
            if(o[7]!=null) {
                venta.setRazonSocialCliente(o[7].toString());                
            }
            if(o[8]!=null) {
                venta.setNmes(Integer.parseInt(o[8].toString()));                
            }           
            if(o[9]!=null) {
                venta.setMonto(new BigDecimal(o[9].toString()));                
            }             
            ventas.add(venta);
        }  
        //Agrupacion por cliente para hacer un totalizador de meses
        Map<Short, Map<String, Map<Short, Map<Integer,List<LiVentas>>>>> map = ventas.stream().collect(Collectors.groupingBy(LiVentas::getCodVendedor,
        Collectors.groupingBy(LiVentas::getCodZona, Collectors.groupingBy(LiVentas::getCodRuta, Collectors.groupingBy(LiVentas::getCodCliente)))));
        //Map<Integer, List<LiVentas>> map = ventas.stream().collect(Collectors.groupingBy(LiVentas::getCodCliente));
        List<LiVentas> ventasClientes = new ArrayList<LiVentas>();
        /*for (Map.Entry<Short, Map<String, Map<Short, Map<Integer,List<LiVentas>>>>> agrupPorCiente : map.entrySet()) {
            BigDecimal sumEne = new BigDecimal(0);BigDecimal sumFeb = new BigDecimal(0);BigDecimal sumMar = new BigDecimal(0);BigDecimal sumAbr = new BigDecimal(0);
            BigDecimal sumMay = new BigDecimal(0);BigDecimal sumJun = new BigDecimal(0);BigDecimal sumJul = new BigDecimal(0);BigDecimal sumAgo = new BigDecimal(0);
            BigDecimal sumSep = new BigDecimal(0);BigDecimal sumOct = new BigDecimal(0);BigDecimal sumNov = new BigDecimal(0);BigDecimal sumDic = new BigDecimal(0);
            BigDecimal sumTotal = new BigDecimal(0);*/
            //representa un solo cliente con todos los meses que puede tener
        for(Map.Entry<Short, Map<String, Map<Short, Map<Integer,List<LiVentas>>>>> vendedor : map.entrySet()){
            Short codVendedor = vendedor.getKey();
            for(Map.Entry<String, Map<Short, Map<Integer,List<LiVentas>>>> zona : vendedor.getValue().entrySet()){
                String codZona = zona.getKey();
                for(Map.Entry<Short, Map<Integer,List<LiVentas>>> ruta : zona.getValue().entrySet()){
                    Short codRuta = ruta.getKey();
                    for(Map.Entry<Integer,List<LiVentas>> agrupPorCliente : ruta.getValue().entrySet()){ 
                        BigDecimal sumEne = new BigDecimal(0);BigDecimal sumFeb = new BigDecimal(0);BigDecimal sumMar = new BigDecimal(0);BigDecimal sumAbr = new BigDecimal(0);
                        BigDecimal sumMay = new BigDecimal(0);BigDecimal sumJun = new BigDecimal(0);BigDecimal sumJul = new BigDecimal(0);BigDecimal sumAgo = new BigDecimal(0);
                        BigDecimal sumSep = new BigDecimal(0);BigDecimal sumOct = new BigDecimal(0);BigDecimal sumNov = new BigDecimal(0);BigDecimal sumDic = new BigDecimal(0);
                        for(LiVentas venta : agrupPorCliente.getValue()){
                            switch (venta.getNmes()) {
                                case 1:                                
                                    sumEne = sumEne.add(venta.getMonto());
                                    break;
                                case 2:
                                    sumFeb = sumFeb.add(venta.getMonto());
                                    break;
                                case 3:
                                    sumMar = sumMar.add(venta.getMonto());
                                    break;
                                case 4:
                                    sumAbr = sumAbr.add(venta.getMonto());
                                    break;
                                case 5:
                                    sumMay = sumMay.add(venta.getMonto());
                                    break;
                                case 6:
                                    sumJun = sumJun.add(venta.getMonto());
                                    break;
                                case 7:
                                    sumJul = sumJul.add(venta.getMonto());
                                    break;
                                case 8:
                                    sumAgo = sumAgo.add(venta.getMonto());
                                    break;
                                case 9:
                                    sumSep = sumSep.add(venta.getMonto());
                                    break;
                                case 10:
                                    sumOct = sumOct.add(venta.getMonto());
                                    break;
                                case 11:
                                    sumNov = sumNov.add(venta.getMonto());
                                    break;
                                default:
                                    sumDic = sumDic.add(venta.getMonto());
                                    break;
                            }
                        }
                        //se debe generar los 12 meses para cada cliente
                        //group by mes
                        Map<Integer,List<LiVentas>> porMes = agrupPorCliente.getValue().stream().collect(Collectors.groupingBy(LiVentas::getNmes));
                        for(Map.Entry<Integer,List<LiVentas>> nmes : porMes.entrySet()){
                            LiVentas vCliente = new LiVentas();
                            vCliente.setCodCliente(agrupPorCliente.getValue().get(0).getCodCliente()); agrupPorCliente.getValue().get(0);
                            vCliente.setCodRuta(agrupPorCliente.getValue().get(0).getCodRuta());
                            vCliente.setCodVendedor(agrupPorCliente.getValue().get(0).getCodVendedor());
                            vCliente.setCodZona(agrupPorCliente.getValue().get(0).getCodZona());
                            vCliente.setDescripcionRunta(agrupPorCliente.getValue().get(0).getDescripcionRunta());
                            vCliente.setDescripcionVendedor(agrupPorCliente.getValue().get(0).getDescripcionVendedor());
                            vCliente.setDescripcionZona(agrupPorCliente.getValue().get(0).getDescripcionZona());
                            vCliente.setRazonSocialCliente(agrupPorCliente.getValue().get(0).getRazonSocialCliente());
                            vCliente.setNmes(nmes.getKey());
                            switch (nmes.getKey()) {
                                case 1:                                                        
                                    vCliente.setMonto(sumEne);
                                    break;
                                case 2:
                                    vCliente.setMonto(sumFeb);
                                    break;
                                case 3:
                                    vCliente.setMonto(sumMar);
                                    break;
                                case 4:
                                    vCliente.setMonto(sumAbr);
                                    break;
                                case 5:
                                    vCliente.setMonto(sumMay);
                                    break;
                                case 6:
                                    vCliente.setMonto(sumJun);
                                    break;
                                case 7:
                                    vCliente.setMonto(sumJul);
                                    break;
                                case 8:
                                    vCliente.setMonto(sumAgo);
                                    break;
                                case 9:
                                    vCliente.setMonto(sumSep);
                                    break;
                                case 10:
                                    vCliente.setMonto(sumOct);
                                    break;
                                case 11:
                                    vCliente.setMonto(sumNov);
                                    break;
                                default:
                                    vCliente.setMonto(sumDic);
                                    break;
                            }
                            ventasClientes.add(vCliente);
                        }                         
                    }
                }
            }
        }
        return ventasClientes;
    }
    
    public List<LiVentasCab> obtenerListado(String consultaVenta, String consultaNotas){         
        String consultaSql = " select facturas.cod_vendedor, facturas.xnombre, facturas.cod_zona, facturas.xdesc_zona, facturas.cod_ruta, facturas.xdesc_ruta, "+
            " facturas.cod_cliente,   "+
            " facturas.xrazon_social, "+
            " facturas.nmes, " +
            " case when notas.ctipo_docum IS NOT NULL THEN  "+
            " facturas.iventas - notas.ttotal "+
            " ELSE "+
            " facturas.iventas "+
            " END as total_ventas "+
            " FROM ";
        consultaSql = consultaSql.concat("("+consultaVenta+") facturas  LEFT JOIN ");
        
        consultaSql = consultaSql.concat("("+ consultaNotas +") notas  ");
        String sqlJoin = " ON "+
            "   (notas.nmes= facturas.nmes "+
            "   and notas.cod_cliente = facturas.cod_cliente "+
            "   and notas.cod_vendedor = facturas.cod_vendedor and notas.cod_ruta = facturas.cod_ruta and notas.cod_zona = facturas.cod_zona) "+
            " order by facturas.cod_vendedor, facturas.cod_zona, facturas.cod_ruta, facturas.cod_cliente ";        
        consultaSql = consultaSql.concat(sqlJoin);
        System.out.println("CONSULTA SQL FINAL DE VENDEDOR");
        System.out.println(consultaSql);
        Query qVenta = em.createNativeQuery(consultaSql);
        List<Object[]> resultados = qVenta.getResultList();
        List<LiVentas> ventas = new ArrayList<LiVentas>();
        List<LiVentasCab> ventasMes = new ArrayList<LiVentasCab>();
        for(Object[] o : resultados) {
            LiVentas venta = new LiVentas();            
            if(o[0]!=null) {
                venta.setCodVendedor(new Short(o[0].toString()));
            }
            if(o[1]!=null) {
                venta.setDescripcionVendedor(o[1].toString());
            }
            if(o[2]!=null) {
                venta.setCodZona(o[2].toString());
            }
            if(o[3]!=null) {
                venta.setDescripcionZona(o[3].toString());
            }
            if(o[4]!=null) {
                venta.setCodRuta(new Short(o[4].toString()));
            }
            if(o[5]!=null) {
                venta.setDescripcionRunta(o[5].toString());
            }
            if(o[6]!=null) {
                venta.setCodCliente(Integer.parseInt(o[6].toString()));                
            }
            if(o[7]!=null) {
                venta.setRazonSocialCliente(o[7].toString());                
            }
            if(o[8]!=null) {
                venta.setNmes(Integer.parseInt(o[8].toString()));                
            }           
            if(o[9]!=null) {
                venta.setMonto(new BigDecimal(o[9].toString()));                
            }             
            ventas.add(venta);
            
        }          
        Map<Short, Map<String, Map<Short, List<LiVentas>>>> map = ventas.stream().collect(Collectors.groupingBy(LiVentas::getCodVendedor,
        Collectors.groupingBy(LiVentas::getCodZona, Collectors.groupingBy(LiVentas::getCodRuta))));
        
        for(Map.Entry<Short, Map<String, Map<Short, List<LiVentas>>>> vendedor : map.entrySet()){
            Short codVendedor = vendedor.getKey();
            for(Map.Entry<String, Map<Short, List<LiVentas>>> zona : vendedor.getValue().entrySet()){
                String codZona = zona.getKey();
                for(Map.Entry<Short, List<LiVentas>> ruta : zona.getValue().entrySet()){
                    Short codRuta = ruta.getKey();
                    LiVentasCab venta = new LiVentasCab();
                    venta.setCodVendedor(codVendedor);
                    venta.setCodZona(codZona);
                    venta.setCodRuta(codRuta);
                    List<LiVentas> detalles = new ArrayList<LiVentas>();
                    LiVentas cab = ruta.getValue().get(0);
                    venta.setDescripcionVendedor(cab.getDescripcionVendedor());
                    venta.setDescripcionZona(cab.getDescripcionZona());
                    venta.setDescripcionRunta(cab.getDescripcionRunta());
                    BigDecimal sumEne = new BigDecimal(0);BigDecimal sumFeb = new BigDecimal(0);BigDecimal sumMar = new BigDecimal(0);BigDecimal sumAbr = new BigDecimal(0);
                    BigDecimal sumMay = new BigDecimal(0);BigDecimal sumJun = new BigDecimal(0);BigDecimal sumJul = new BigDecimal(0);BigDecimal sumAgo = new BigDecimal(0);
                    BigDecimal sumSep = new BigDecimal(0);BigDecimal sumOct = new BigDecimal(0);BigDecimal sumNov = new BigDecimal(0);BigDecimal sumDic = new BigDecimal(0);
                    BigDecimal sumTotal = new BigDecimal(0);
                    for(LiVentas m : ruta.getValue()){
                        switch (m.getNmes()) {
                            case 1:                                
                                sumEne = sumEne.add(m.getMonto());
                                break;
                            case 2:
                                sumFeb = sumFeb.add(m.getMonto());
                                break;
                            case 3:
                                sumMar = sumMar.add(m.getMonto());
                                break;
                            case 4:
                                sumAbr = sumAbr.add(m.getMonto());
                                break;
                            case 5:
                                sumMay = sumMay.add(m.getMonto());
                                break;
                            case 6:
                                sumJun = sumJun.add(m.getMonto());
                                break;
                            case 7:
                                sumJul = sumJul.add(m.getMonto());
                                break;
                            case 8:
                                sumAgo = sumAgo.add(m.getMonto());
                                break;
                            case 9:
                                sumSep = sumSep.add(m.getMonto());
                                break;
                            case 10:
                                sumOct = sumOct.add(m.getMonto());
                                break;
                            case 11:
                                sumNov = sumNov.add(m.getMonto());
                                break;
                            default:
                                sumDic = sumDic.add(m.getMonto());
                                break;
                        }
                    }
                    //TOTAL POR ZONA QUE REQUIERE LA AGRUPACION
                    Map<String, BigDecimal> montoPorMes = new HashMap<String, BigDecimal>();
                    montoPorMes.put("sumEne", sumEne);montoPorMes.put("sumFeb", sumFeb);montoPorMes.put("sumMar", sumMar);montoPorMes.put("sumAbr", sumAbr);montoPorMes.put("sumMay", sumMay);
                    montoPorMes.put("sumJun", sumJun);montoPorMes.put("sumJul", sumJul);montoPorMes.put("sumAgo", sumAgo);montoPorMes.put("sumSep", sumSep);montoPorMes.put("sumOct", sumOct);
                    montoPorMes.put("sumNov", sumNov);montoPorMes.put("sumDic", sumDic);
                    sumTotal = sumTotal.add(sumEne).add(sumFeb).add(sumMar).add(sumAbr).add(sumMay).add(sumJun).add(sumJul).add(sumAgo).add(sumSep).add(sumOct).add(sumNov).add(sumDic);
                    montoPorMes.put("sumTotal", sumTotal);
                    //CALCULAR LISTADO CLIENTES CON MES TRASPUESTOS
                    List<Map<String, Object>> listadoClientes = calcularDetallesClientes(ruta.getValue());
                    venta.setClientesPorMes(listadoClientes);
                    venta.setMontoPorMes(montoPorMes);
                    ventasMes.add(venta);
                }
            }        
        }        
        return ventasMes;
    }
    
    public List<Map<String, Object>> calcularDetallesClientes(List<LiVentas> vendedor){
        List<Map<String, Object>> listadoClientes = new ArrayList<Map<String, Object>>();
        Map<Integer, List<LiVentas>> collect = vendedor.stream().collect(Collectors.groupingBy(LiVentas::getCodCliente));
        for (Map.Entry<Integer, List<LiVentas>> agrupPorCliente : collect.entrySet()) {
            BigDecimal sumEne = new BigDecimal(0);BigDecimal sumFeb = new BigDecimal(0);BigDecimal sumMar = new BigDecimal(0);BigDecimal sumAbr = new BigDecimal(0);
            BigDecimal sumMay = new BigDecimal(0);BigDecimal sumJun = new BigDecimal(0);BigDecimal sumJul = new BigDecimal(0);BigDecimal sumAgo = new BigDecimal(0);
            BigDecimal sumSep = new BigDecimal(0);BigDecimal sumOct = new BigDecimal(0);BigDecimal sumNov = new BigDecimal(0);BigDecimal sumDic = new BigDecimal(0);
            BigDecimal sumTotal = new BigDecimal(0);
            for(LiVentas venta : agrupPorCliente.getValue()){
                switch (venta.getNmes()) {
                    case 1:                                
                        sumEne = sumEne.add(venta.getMonto());
                        break;
                    case 2:
                        sumFeb = sumFeb.add(venta.getMonto());
                        break;
                    case 3:
                        sumMar = sumMar.add(venta.getMonto());
                        break;
                    case 4:
                        sumAbr = sumAbr.add(venta.getMonto());
                        break;
                    case 5:
                        sumMay = sumMay.add(venta.getMonto());
                        break;
                    case 6:
                        sumJun = sumJun.add(venta.getMonto());
                        break;
                    case 7:
                        sumJul = sumJul.add(venta.getMonto());
                        break;
                    case 8:
                        sumAgo = sumAgo.add(venta.getMonto());
                        break;
                    case 9:
                        sumSep = sumSep.add(venta.getMonto());
                        break;
                    case 10:
                        sumOct = sumOct.add(venta.getMonto());
                        break;
                    case 11:
                        sumNov = sumNov.add(venta.getMonto());
                        break;
                    default:
                        sumDic = sumDic.add(venta.getMonto());
                        break;
                }
            }
            Map<String, Object> datos = new HashMap<String, Object>();
            datos.put("textCliente", agrupPorCliente.getValue().get(0).getRazonSocialCliente());
            datos.put("codCliente", agrupPorCliente.getValue().get(0).getCodCliente());
            datos.put("sumEne", sumEne);
            datos.put("sumFeb", sumFeb);
            datos.put("sumMar", sumMar);
            datos.put("sumAbr", sumAbr);
            datos.put("sumMay", sumMay);
            datos.put("sumJun", sumJun);
            datos.put("sumJul", sumJul);
            datos.put("sumAgo", sumAgo);
            datos.put("sumSep", sumSep);
            datos.put("sumOct", sumOct);
            datos.put("sumNov", sumNov);
            datos.put("sumDic", sumDic);
            sumTotal = sumTotal.add(sumEne).add(sumFeb).add(sumMar).add(sumAbr).add(sumMay).add(sumJun).add(sumJul).add(sumAgo).add(sumSep).add(sumOct).add(sumNov).add(sumDic);
            datos.put("sumTotal", sumTotal);
            listadoClientes.add(datos);
        }
        return listadoClientes;
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
            "   and notas.cod_zona = facturas.cod_zona " +
            " and notas.cod_empr = facturas.cod_empr )" ;
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
