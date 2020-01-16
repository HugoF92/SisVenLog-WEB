/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.PagareDto;
import entidad.Pagares;
import entidad.PagaresPK;
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
public class PagaresFacade extends AbstractFacade<Pagares> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @EJB
    private ClientesFacade clientesFacade;
    
    @EJB
    private ZonasFacade zonasFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PagaresFacade() {
        super(Pagares.class);
    }
    
    public List<PagareDto> listarPagaresNoCobrados( String codZona,
                                                    long nPagare,
                                                    Long iPagare,
                                                    Date fVencInicio,
                                                    Date fVencFin,
                                                    Integer codCliente,
                                                    Date fechaCobro){
        
        String sql =    "SELECT p.fvenc, p.ipagare, p.femision, p.cod_entregador, p.cod_zona, p.mestado, p.falta, p.cusuario, p.fcobro, p.npagare, p.cod_cliente, c.xnombre" +
                        " FROM pagares p, clientes c, rutas r" +
                        " WHERE p.cod_empr = 2" +
                        " AND p.cod_empr = c.cod_empr" +
                        " AND c.cod_ruta = r.cod_ruta" +
                        " AND p.cod_cliente = c.cod_cliente" +
                        " AND (p.fcobro is null or p.fcobro = '')";
        
        if(!codZona.equals("") || !codZona.isEmpty()){
            sql += " AND p.cod_zona = ?";
        }
        
        if(nPagare != 0){
            sql += " AND p.npagare = ?";
        }
        
        if(iPagare != null){
            if(iPagare > 0){
                sql += " AND p.ipagare = ?";
            }
        }
        
        if(fVencInicio != null){
            sql += " AND p.fvenc >= ?";
        }
        
        if(fVencFin != null){
            sql += " AND p.fvenc <= ?";
        }
        
        if(codCliente != null){
            if(codCliente > 0){
                sql += " AND p.cod_cliente = ?";
            }
        }
        
        sql += " ORDER BY p.fvenc, p.npagare";
        
        Query q = em.createNativeQuery(sql);
        
        int i = 1;
        
        if(!codZona.equals("") || !codZona.isEmpty()){
            i++;
            q.setParameter(i, codZona);
        }
        
        if(nPagare != 0){
            i++;
            q.setParameter(i, nPagare);
        }
        
        if(iPagare != null){
            if(iPagare > 0){
                i++;
                q.setParameter(i, iPagare);
            }
        }
        
        if(fVencInicio != null){
            i++;
            q.setParameter(i, fVencInicio);
        }
        
        if(fVencFin != null){
            i++;
            q.setParameter(i, fVencFin);
        }
        
        if(codCliente != null){
            if(codCliente > 0){
                i++;
                q.setParameter(i, codCliente);
            }
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<PagareDto> listadoPagares = new ArrayList<>();
        for(Object[] resultado: resultados){
            //clave primaria pagaré
            PagaresPK pagaresPk = new PagaresPK();
            pagaresPk.setCodEmpr(Short.parseShort("2"));
            pagaresPk.setNpagare(Long.parseLong(resultado[9].toString()));
            //pagare
            Pagares pagare = new Pagares();
            pagare.setPagaresPK(pagaresPk);
            //fvenc            
            if(resultado[0] != null){
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());
                pagare.setFvenc(dateResult_0);
            }else{
                pagare.setFvenc(null);
            }
            //ipagare
            pagare.setIpagare(resultado[1] == null ? null: Long.parseLong(resultado[1].toString()));
            //femision
            if(resultado[2] != null){
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                pagare.setFemision(dateResult_2);
            }else{
                pagare.setFemision(null);
            }
            //fcobro
            if(resultado[8] != null){
                Timestamp timeStamp_8 = (Timestamp) resultado[8];
                java.util.Date dateResult_8 = new Date(timeStamp_8.getTime());
                pagare.setFcobro(dateResult_8);
            }else{
                pagare.setFcobro(null);
            }
            //cod_cliente
            pagare.setCodCliente(resultado[10] == null ? null : clientesFacade.find(Integer.parseInt(resultado[10].toString())));
            //nombre cliente
            String nombreCliente = null;
            if(resultado[11] != null){
                nombreCliente = resultado[11].toString();
            }
            //zona del cliente
            pagare.setCodZona(resultado[4] == null ? null : zonasFacade.buscarPorCodigo(resultado[4].toString()).getZonasPK().getCodZona());
            //fecha de cobro
            pagare.setFcobro(fechaCobro);
            
            PagareDto pagareDto = new PagareDto();
            pagareDto.setPagare(pagare);
            pagareDto.setNombreCliente(nombreCliente);
            pagareDto.setPagareSeleccionado(false);
            listadoPagares.add(pagareDto);
            
        }
        
        return listadoPagares;
    }
    
    public int actualizarPagaresNoCobrados(Pagares pagare){
        String sql = "UPDATE pagares SET fcobro = ? WHERE cod_empr = 2 AND npagare = ?";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, pagare.getFcobro());
        q.setParameter(2, pagare.getPagaresPK().getNpagare());
        return q.executeUpdate();
    }
    
    
}
