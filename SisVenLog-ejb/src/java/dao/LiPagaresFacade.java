/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.LiPagares;
import dto.LiPagaresCab;
import dto.LiVentas;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Clara
 */
@Stateless
public class LiPagaresFacade {
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    protected EntityManager getEntityManager() {
        return em;
    }

    public LiPagaresFacade() {
        
    }
    
    public List<LiPagares> getPageresCabecera(Date desdeEmision, Date hastaEmision, Date desdeVencimiento, Date hastaVencimiento, Date desdeCobro, Date hastaCobro, Integer codigoCliente, String estadoPagare){
        List<LiPagares> pagaresCabecera = new ArrayList<LiPagares>();
        String sqlPageresCabecera = " SELECT DISTINCT p.npagare, p.femision, p.fvenc, p.cod_cliente, c.xnombre, " +
                " p.cod_entregador, e.xnombre as xentregador, p.ipagare, p.mestado,  " +
                " p.fcobro, p.cod_zona  " +
                " FROM pagares p, clientes c, EMPLEADOS E " +
                " WHERE p.cod_empr = 2  " +
                " AND p.cod_entregador = e.cod_empleado " +
                " AND p.cod_cliente = c.cod_cliente " +
                " AND c.cod_empr= 2 ";
        sqlPageresCabecera = generarConsulta(sqlPageresCabecera, desdeEmision, hastaEmision, desdeVencimiento, hastaVencimiento, desdeCobro, hastaCobro, codigoCliente, estadoPagare);
        sqlPageresCabecera = sqlPageresCabecera.concat(" ORDER BY p.npagare ");
        System.out.println(String.format("Consulta Base Final: [%s]", sqlPageresCabecera));
        Query qVenta = em.createNativeQuery(sqlPageresCabecera);
        List<Object[]> resultados = qVenta.getResultList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(Object[] o : resultados) {
            LiPagares pagare = new LiPagares();
            if(o[0]!=null) {
                pagare.setNroPagare(Integer.parseInt(o[0].toString()));            
            }
            if(o[1]!=null) {  
                try {
                    pagare.setFechEmision(df.parse(o[1].toString()));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(o[2]!=null) {
                try {
                    pagare.setFechVencimiento(df.parse(o[2].toString()));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }                
            }
            if(o[3]!=null) {
                pagare.setCodCliente(Integer.parseInt(o[3].toString()));                               
            }
            if(o[4]!=null) {
                pagare.setNombreCliente(o[4].toString());         
            }
            if(o[5]!=null) {
                pagare.setCodEntregador(Integer.parseInt(o[5].toString()));                
            }
            if(o[6]!=null) {
                pagare.setNombreEntregador(o[6].toString());         
            }
            if(o[7]!=null) {
                pagare.setiPagare(new BigDecimal(o[7].toString()));                
            } 
            if(o[8]!=null) {
                pagare.setEstado(o[8].toString());                
            } 
            pagaresCabecera.add(pagare);
        }  
        return pagaresCabecera;
    }
    
    public List<LiPagares> getPagaresCabDetalleExcell(Date desdeEmision, Date hastaEmision, Date desdeVencimiento, Date hastaVencimiento, Date desdeCobro, Date hastaCobro, Integer codigoCliente, String estadoPagare){
        List<LiPagares> pagaresCabDetalle = new ArrayList<>();
        String sqlPageresCabDetalle =" SELECT DISTINCT p.npagare, p.femision, p.fvenc, p.cod_cliente, c.xnombre, p.cod_entregador, e.xnombre as xentregador, "+
            " p.ipagare, p.mestado, d.ctipo_docum, d.nrofact, d.ffactur, d.itotal,  "+
            " p.fcobro, p.cod_zona "+
            " FROM pagares p, pagares_det d, clientes c, EMPLEADOS E  "+
            " WHERE p.cod_empr = 2   "+
            " AND d.cod_empr =2  "+
            " AND p.cod_entregador = e.cod_empleado  "+
            " AND p.npagare = d.npagare "+
            " AND p.cod_cliente = c.cod_cliente  "+
            " AND c.cod_empr= 2  "; 
        sqlPageresCabDetalle = generarConsulta(sqlPageresCabDetalle, desdeEmision, hastaEmision, desdeVencimiento, hastaVencimiento, desdeCobro, hastaCobro, codigoCliente, estadoPagare);
        sqlPageresCabDetalle = sqlPageresCabDetalle.concat(" ORDER BY p.npagare ");
        System.out.println(String.format("Consulta Base Final: [%s]", sqlPageresCabDetalle));
        Query qVenta = em.createNativeQuery(sqlPageresCabDetalle);
        List<Object[]> resultados = qVenta.getResultList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(Object[] o : resultados) {
            LiPagares pagare = new LiPagares();
            if(o[0]!=null) {
                pagare.setNroPagare(Integer.parseInt(o[0].toString()));            
            }
            if(o[1]!=null) {  
                try {
                    pagare.setFechEmision(df.parse(o[1].toString()));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(o[2]!=null) {
                try {
                    pagare.setFechVencimiento(df.parse(o[2].toString()));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }                
            }
            if(o[3]!=null) {
                pagare.setCodCliente(Integer.parseInt(o[3].toString()));                               
            }
            if(o[4]!=null) {
                pagare.setNombreCliente(o[4].toString());         
            }
            if(o[5]!=null) {
                pagare.setCodEntregador(Integer.parseInt(o[5].toString()));                
            }
            if(o[6]!=null) {
                pagare.setNombreEntregador(o[6].toString());         
            }
            if(o[7]!=null) {
                pagare.setiPagare(new BigDecimal(o[7].toString()));                
            } 
            if(o[8]!=null) {
                pagare.setEstado(o[8].toString());                
            } 
            if(o[9]!=null) {
                pagare.setTipoDocum(o[9].toString());                
            }
            if(o[10]!=null) {
                pagare.setNrofact(Integer.parseInt(o[10].toString()));                
            }
            if(o[11]!=null) {
                try {
                    pagare.setFechaFactur(df.parse(o[11].toString()));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }                
            }
            if(o[12]!=null) {
                pagare.setiTotal(new BigDecimal(o[12].toString()));                
            }
            pagaresCabDetalle.add(pagare);
        }
        return pagaresCabDetalle;
    }
    
    public Map<Integer, LiPagaresCab> getPagaresCabDetalle(Date desdeEmision, Date hastaEmision, Date desdeVencimiento, Date hastaVencimiento, Date desdeCobro, Date hastaCobro, Integer codigoCliente, String estadoPagare){
        List<LiPagares> pagaresCabDetalle = new ArrayList<>();
        String sqlPageresCabDetalle =" SELECT DISTINCT p.npagare, p.femision, p.fvenc, p.cod_cliente, c.xnombre, p.cod_entregador, e.xnombre as xentregador, "+
            " p.ipagare, p.mestado, d.ctipo_docum, d.nrofact, d.ffactur, d.itotal,  "+
            " p.fcobro, p.cod_zona "+
            " FROM pagares p, pagares_det d, clientes c, EMPLEADOS E  "+
            " WHERE p.cod_empr = 2   "+
            " AND d.cod_empr =2  "+
            " AND p.cod_entregador = e.cod_empleado  "+
            " AND p.npagare = d.npagare "+
            " AND p.cod_cliente = c.cod_cliente  "+
            " AND c.cod_empr= 2  "; 
        sqlPageresCabDetalle = generarConsulta(sqlPageresCabDetalle, desdeEmision, hastaEmision, desdeVencimiento, hastaVencimiento, desdeCobro, hastaCobro, codigoCliente, estadoPagare);
        sqlPageresCabDetalle = sqlPageresCabDetalle.concat(" ORDER BY p.npagare ");
        System.out.println(String.format("Consulta Base Final: [%s]", sqlPageresCabDetalle));
        Query qVenta = em.createNativeQuery(sqlPageresCabDetalle);
        List<Object[]> resultados = qVenta.getResultList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(Object[] o : resultados) {
            LiPagares pagare = new LiPagares();
            if(o[0]!=null) {
                pagare.setNroPagare(Integer.parseInt(o[0].toString()));            
            }
            if(o[1]!=null) {  
                try {
                    pagare.setFechEmision(df.parse(o[1].toString()));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(o[2]!=null) {
                try {
                    pagare.setFechVencimiento(df.parse(o[2].toString()));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }                
            }
            if(o[3]!=null) {
                pagare.setCodCliente(Integer.parseInt(o[3].toString()));                               
            }
            if(o[4]!=null) {
                pagare.setNombreCliente(o[4].toString());         
            }
            if(o[5]!=null) {
                pagare.setCodEntregador(Integer.parseInt(o[5].toString()));                
            }
            if(o[6]!=null) {
                pagare.setNombreEntregador(o[6].toString());         
            }
            if(o[7]!=null) {
                pagare.setiPagare(new BigDecimal(o[7].toString()));                
            } 
            if(o[8]!=null) {
                pagare.setEstado(o[8].toString());                
            } 
            if(o[9]!=null) {
                pagare.setTipoDocum(o[9].toString());                
            }
            if(o[10]!=null) {
                pagare.setNrofact(Integer.parseInt(o[10].toString()));                
            }
            if(o[11]!=null) {
                try {
                    pagare.setFechaFactur(df.parse(o[11].toString()));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }                
            }
            if(o[12]!=null) {
                pagare.setiTotal(new BigDecimal(o[12].toString()));                
            }
            pagaresCabDetalle.add(pagare);
        }
        if(!pagaresCabDetalle.isEmpty()){
                
        }  
        //agrupación por nroFactura
        Map<Integer, LiPagaresCab> facturasGroup = new TreeMap<Integer, LiPagaresCab>();
        for (LiPagares r : pagaresCabDetalle) {
            Integer key = r.getNrofact();
            
            if(facturasGroup.containsKey(key)){
                LiPagaresCab pagaresCab = facturasGroup.get(key);
                List<LiPagares> listDetalle = pagaresCab.getDetalles();
                listDetalle.add(r);
                pagaresCab.setDetalles(listDetalle);
            }else{
                List<LiPagares> list = new ArrayList<LiPagares>();
                LiPagaresCab cabecera = new LiPagaresCab();
                cabecera.setFechaFactur(r.getFechaFactur());
                cabecera.setNrofact(r.getNrofact());
                cabecera.setTipoDocum(r.getTipoDocum());
                cabecera.setiTotal(r.getiTotal());
                if(!pagaresCabDetalle.isEmpty()){
                    cabecera.setNroInicial(pagaresCabDetalle.get(0).getNroPagare());
                    cabecera.setNroFinal( pagaresCabDetalle.get(pagaresCabDetalle.size() - 1).getNroPagare());
                }
                list.add(r);
                cabecera.setDetalles(list);
                facturasGroup.put(key, cabecera);
            }
        }
        return facturasGroup;
    }
    
    private String generarConsulta(String consultaBase, Date desdeEmision, Date hastaEmision, Date desdeVencimiento, Date hastaVencimiento, 
            Date desdeCobro, Date hastaCobro, Integer codigoCliente, String estadoPagare){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        //--------------------------------------------------------------------------------------------------------------------------
        if(desdeEmision != null){
            consultaBase = consultaBase.concat(" AND p.femision >= '" + formatter.format(desdeEmision) +"'");
        }
        if(hastaEmision != null){
            consultaBase = consultaBase.concat(" AND p.femision <= '" + formatter.format(hastaEmision) +"'");
        }
        if(desdeVencimiento != null){
            consultaBase = consultaBase.concat(" AND p.fvenc >= '" + formatter.format(desdeVencimiento) +"'");
        }
        if(hastaVencimiento != null){
            consultaBase = consultaBase.concat(" AND p.fvenc <= '" + formatter.format(hastaVencimiento) +"'");
        }
        if(desdeCobro != null){
            consultaBase = consultaBase.concat(" AND p.fcobro >= '" + formatter.format(desdeCobro) +"'");
        }
        if(hastaCobro != null){
            consultaBase = consultaBase.concat(" AND p.fcobro <= '" + formatter.format(hastaCobro) +"'");
        }
        if(codigoCliente != null){
            consultaBase = consultaBase.concat(" AND p.cod_cliente = " + codigoCliente);
        }
        if(!estadoPagare.equalsIgnoreCase("TODOS")){
            consultaBase = consultaBase.concat(" AND p.mestado = '" + estadoPagare +"'");
        }
        //--------------------------------------------------------------------------------------------------------------------------        
        return consultaBase;
    }
}
