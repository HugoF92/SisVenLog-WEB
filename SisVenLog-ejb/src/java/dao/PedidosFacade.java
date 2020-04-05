/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.PedidoDto;
import entidad.Pedidos;
import entidad.PedidosPK;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class PedidosFacade extends AbstractFacade<Pedidos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidosFacade() {
        super(Pedidos.class);
    }
    
    public List<PedidoDto> listadoDePedidos(Date fechaInicio,
                                            Date fechaFin,
                                            Character estado,
                                            Integer codigoCliente,
                                            long nroDocumento){
        
        String sql =    "SELECT p.ctipo_docum, p.nro_pedido, p.cod_cliente, "+
                        "c.xnombre, p.cod_canal, p.cod_depo, p.npeso_acum, p.mestado "+
                        "FROM pedidos p, clientes c "+    
                        "WHERE p.cod_cliente = c.cod_cliente "+
                        "AND p.cod_empr = 2";
        
        if(fechaInicio != null){
            sql += " AND p.fpedido >= ?";
        }
        
        if(fechaFin != null){
            sql += " AND p.fpedido <= ?";
        }
        
        if(estado == 'A'){
            sql += " AND p.mestado != 'X'";
        }else{
            sql += " AND p.mestado = 'X'";
        }
        
        if(codigoCliente != null){
            if(codigoCliente > 0){
                sql += " AND p.cod_cliente = ?";
            }
        }
        
        if(nroDocumento != 0){
            sql += " AND p.nro_pedido = ?";
        }
        
        Query q = em.createNativeQuery(sql);
        
        int i = 1;
        if(fechaInicio != null){
            i++;
            q.setParameter(i, fechaInicio);
        }
        
        if(fechaFin != null){
            i++;
            q.setParameter(i, fechaFin);
        }
        
        i++;
        q.setParameter(i, estado);
        
        if(codigoCliente != null){
            if(codigoCliente > 0){
                i++;
                q.setParameter(i, codigoCliente);
            }
        }
        
        if(nroDocumento != 0){
            i++;
            q.setParameter(i, nroDocumento);
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<PedidoDto> listadoPedidos = new ArrayList<PedidoDto>();
        for(Object[] resultado: resultados){
            //clave primaria de pedidos
            PedidosPK pedidosPk = new PedidosPK();
            pedidosPk.setCodEmpr(Short.parseShort("2"));
            pedidosPk.setNroPedido(Long.parseLong(resultado[1].toString()));
            //pedidos
            Pedidos pedido = new Pedidos();
            pedido.setPedidosPK(pedidosPk);
            pedido.setCtipoDocum(resultado[0].toString());
            pedido.setCodCliente(resultado[2] == null ? null : Integer.parseInt(resultado[2].toString()));
            pedido.setCodCanal(resultado[4].toString());
            pedido.setCodDepo(resultado[5] == null ? null : Short.parseShort(resultado[5].toString()));
            pedido.setNpesoAcum(resultado[6] == null ? null : BigDecimal.valueOf(Long.parseLong(resultado[6].toString())));
            pedido.setMestado((Character)resultado[7]);
            //nombre cliente
            String nombreCliente = null;
            if(resultado[3] != null){
                nombreCliente = resultado[3].toString();
            }
            PedidoDto pdto = new PedidoDto();
            pdto.setPedido(pedido);
            pdto.setNombreCliente(nombreCliente);
            listadoPedidos.add(pdto);
        }
        
        return listadoPedidos;
    }
    
    public long obtenerTotalPedidosPorClienteFecha(Integer lCodCliente, String lFechaPedido){
        String sql =    "SELECT ISNULL(SUM(ttotal),0) as tot_ped " +
                        "FROM pedidos " +
                        "WHERE cod_cliente = "+lCodCliente+" "+
                        "AND cod_empr = 2 " +
                        "AND fpedido >= '"+lFechaPedido+"' "+
                        "AND mestado IN ('N','E')";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        Long total = Long.parseLong("0");
        for(Object[] resultado: resultados){
            total = resultado[0] == null ? Long.parseLong("0") : Long.parseLong(resultado[0].toString());
        }
        return total;
    }
    
    public void actualizarPedidosPorNro(long lNroPedido){
        String sql =    "UPDATE pedidos SET mestado = 'E' "+
                        "WHERE nro_pedido = "+lNroPedido+" "+
                        "AND cod_empr = 2";
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
    }
    
}
