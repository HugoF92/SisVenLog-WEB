/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.NotaVentaDto;
import entidad.NotasVentas;
import entidad.NotasVentasPK;
import java.sql.Timestamp;
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
public class NotasVentasFacade extends AbstractFacade<NotasVentas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotasVentasFacade() {
        super(NotasVentas.class);
    }
    
    public List<NotaVentaDto> listadoDeNotasDeVentas(   Date fechaInicio,
                                                        Date fechaFin,
                                                        Character estado,
                                                        long nroDocumento,
                                                        String tipoDocumento,
                                                        Integer codigoCliente){
        
        String sql =    "SELECT p.ctipo_docum, p.fdocum, p.nro_nota, p.cconc, p.fac_ctipo_docum, p.nrofact, p.texentas, c.xnombre, p.mestado " +
                        "FROM notas_ventas p, clientes c " +
                        "WHERE p.cod_cliente = c.cod_cliente " +
                        "AND p.cod_empr = 2";
        
        if(fechaInicio != null){
            sql += " AND p.fdocum >= ?";
        }
        
        if(fechaFin != null){
            sql += " AND p.fdocum <= ?";
        }
        
        if(estado == 'A'){
            sql += " AND p.mestado != 'X'";
        }else{
            sql += " AND p.mestado = 'X'";
        }
        
        if(nroDocumento != 0){
            sql += " AND p.nro_nota = ?";
        }
        
        if(!tipoDocumento.equals("") || !tipoDocumento.isEmpty()){
            sql += " AND p.ctipo_docum = ?";
        }
        
        if(codigoCliente != null){
            if(codigoCliente > 0){
                sql += " AND p.cod_cliente = ?";
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
        
        i++;
        q.setParameter(i, estado);
        
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
        List<NotaVentaDto> listadoNotaVenta = new ArrayList<NotaVentaDto>();
        for(Object[] resultado: resultados){
            //clave primaria nota de venta
            NotasVentasPK notaVentaPk = new NotasVentasPK();
            notaVentaPk.setCodEmpr(Short.parseShort("2"));
            notaVentaPk.setCtipoDocum(resultado[0].toString());
            if(resultado[1] != null){
                Timestamp timeStamp_1 = (Timestamp) resultado[1];
                java.util.Date dateResult_1 = new Date(timeStamp_1.getTime());                                
                notaVentaPk.setFdocum(dateResult_1);
            }else{              
                notaVentaPk.setFdocum(null);
            }
            notaVentaPk.setNroNota(Long.parseLong(resultado[2].toString()));
            //nota de venta
            NotasVentas notaVenta = new NotasVentas();
            notaVenta.setNotasVentasPK(notaVentaPk);
            notaVenta.getConceptosDocumentos().getConceptosDocumentosPK().setCconc(resultado[3].toString());
            notaVenta.setFacCtipoDocum(resultado[4].toString());
            notaVenta.setNrofact(resultado[5] == null ? null : Long.parseLong(resultado[5].toString()));
            notaVenta.setTexentas(resultado[6] == null ? null : Long.parseLong(resultado[6].toString()));
            //nombre cliente
            String nombreCliente = null;
            if(resultado[7] != null){
                nombreCliente = resultado[7].toString();
            }
            notaVenta.setMestado((Character)resultado[8]);
            NotaVentaDto nvdto = new NotaVentaDto();
            nvdto.setNotaVenta(notaVenta);
            nvdto.setNombreCliente(nombreCliente);
            listadoNotaVenta.add(nvdto);
        }
        
        return listadoNotaVenta;
    }
   
}
