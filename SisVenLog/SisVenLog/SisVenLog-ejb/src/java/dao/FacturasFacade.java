/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FacturaDto;
import entidad.Facturas;
import entidad.FacturasPK;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class FacturasFacade extends AbstractFacade<Facturas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @EJB
    private DepositosFacade depositosFacade;
    
    @EJB
    private EmpleadosFacade empleadosFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturasFacade() {
        super(Facturas.class);
    }
    
    public List<FacturaDto> listadoDeFacturas(  Date fechaInicio,
                                                Date fechaFin,
                                                Character estado,
                                                long nroDocumento,
                                                String tipoDocumento,
                                                Integer codigoCliente){
        
        String sql =    "SELECT f.nrofact, f.ctipo_docum, f.ffactur, f.cod_ruta, f.mestado, f.fvenc, f.texentas, f.tgravadas, f.timpuestos, "+
                        "f.tdescuentos, f.ttotal, f.isaldo, f.cod_depo, f.cod_vendedor, d.xdesc as xdepo, e.xnombre as xvendedor, v.xdesc as xcanal " +
                        "FROM facturas f, Depositos d, empleados e, canales_venta v " +
                        "WHERE f.cod_depo = d.cod_depo " +
                        "AND f.cod_empr = 2 " +
                        "AND f.cod_vendedor = e.cod_empleado " +
                        "AND f.cod_canal = v.cod_canal";
        
        if(fechaInicio != null){
            sql += " AND f.ffactur >= ?";
        }
        
        if(fechaFin != null){
            sql += " AND f.ffactur <= ?";
        }
        
        if(estado != 0){
            sql += " AND f.mestado = ?";
        }
        
        if(nroDocumento != 0){
            sql += " AND f.nrofact = ?";
        }
        
        if(!tipoDocumento.equals("") || !tipoDocumento.isEmpty()){
            sql += " AND f.ctipo_docum = ?";
        }
        
        if(codigoCliente != null){
            if(codigoCliente > 0){
                sql += " AND f.cod_cliente = ?";
            }
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
        
        if(estado != 0){
            i++;
            q.setParameter(i, estado);
        }
        
        if(nroDocumento != 0){
            i++;
            q.setParameter(i, nroDocumento);
        }
        
        if(!tipoDocumento.equals("") || !tipoDocumento.isEmpty()){
            i++;
            q.setParameter(i, tipoDocumento);
        }
        
        if(codigoCliente != null){
            if(codigoCliente > 0){
                i++;
                q.setParameter(i, codigoCliente);
            }
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<FacturaDto> listadoFacturas = new ArrayList<FacturaDto>();
        for(Object[] resultado: resultados){
            //clave primaria de facturas
            FacturasPK facturasPk = new FacturasPK();
            facturasPk.setCodEmpr(Short.parseShort("2"));
            facturasPk.setNrofact(resultado[0] == null ? null : Long.parseLong(resultado[0].toString()));
            facturasPk.setCtipoDocum(resultado[1] == null ? null : resultado[1].toString());
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                
                facturasPk.setFfactur(dateResult_2);           
            }else{
                facturasPk.setFfactur(null); 
            }
            //facturas
            Facturas factura = new Facturas();
            factura.setFacturasPK(facturasPk);
            factura.setDepositos(resultado[12] == null ? null :  depositosFacade.find(Short.parseShort(resultado[12].toString())));
            factura.setEmpleados1(resultado[13] == null ? null :  empleadosFacade.find(Short.parseShort(resultado[13].toString())));
            //nombre deposito
            String nombreDeposito = null;
            if(resultado[14] != null){
                nombreDeposito = resultado[14].toString();
            }
            //nombre vendedor
            String nombreVendedor = null;
            if(resultado[15] != null){
                nombreVendedor = resultado[15].toString();
            }
            //nombre canal
            String nombreCanal = null;
            if(resultado[16] != null){
                nombreCanal = resultado[16].toString();
            }
            factura.setMestado((Character)resultado[4]);
            FacturaDto fdto = new FacturaDto();
            fdto.setFactura(factura);
            fdto.setNombreDeposito(nombreDeposito);
            fdto.setNombreVendedor(nombreVendedor);
            fdto.setDescripcionCanal(nombreCanal);
            listadoFacturas.add(fdto);
        }
        
        return listadoFacturas;
    }
    
    public List<FacturaDto> listadoDeFacturasPorNroPedido(long nroPedido){
        
        String sql =    "SELECT f.nrofact, f.ctipo_docum, f.ffactur, f.cod_ruta, f.mestado, f.fvenc, f.texentas, f.tgravadas, f.timpuestos, f.tdescuentos, "+
                        "f.ttotal, f.isaldo, f.cod_depo, f.cod_vendedor, d.xdesc as xdepo " +
                        "FROM facturas f, depositos d " +
                        "WHERE f.cod_empr = d.cod_empr " +
                        "AND f.cod_empr = 2 " +
                        "AND f.cod_depo = d.cod_depo";
        
        if(nroPedido != 0){
            sql += " AND f.nro_pedido = ?";
        }
        
        Query q = em.createNativeQuery(sql);
        
        int i = 1;
        if(nroPedido != 0){
            i++;
            q.setParameter(i, nroPedido);
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<FacturaDto> listadoFacturas = new ArrayList<FacturaDto>();
        for(Object[] resultado: resultados){
            //clave primaria de facturas
            FacturasPK facturasPk = new FacturasPK();
            facturasPk.setCodEmpr(Short.parseShort("2"));
            facturasPk.setNrofact(resultado[0] == null ? null : Long.parseLong(resultado[0].toString()));
            facturasPk.setCtipoDocum(resultado[1] == null ? null : resultado[1].toString());
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());                
                facturasPk.setFfactur(dateResult_2);           
            }else{
                facturasPk.setFfactur(null); 
            }
            //facturas
            Facturas factura = new Facturas();
            factura.setFacturasPK(facturasPk);
            factura.setDepositos(resultado[12] == null ? null :  depositosFacade.find(Short.parseShort(resultado[12].toString())));
            factura.setEmpleados1(resultado[13] == null ? null :  empleadosFacade.find(Short.parseShort(resultado[13].toString())));
            //nombre deposito
            String nombreDeposito = null;
            if(resultado[14] != null){
                nombreDeposito = resultado[14].toString();
            }
            FacturaDto fdto = new FacturaDto();
            fdto.setFactura(factura);
            fdto.setNombreDeposito(nombreDeposito);
            listadoFacturas.add(fdto);
        }
        
        return listadoFacturas;
    }
    
}
