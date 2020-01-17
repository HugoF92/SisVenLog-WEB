/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ChequeProveedorDto;
import entidad.ChequesEmitidos;
import entidad.ChequesEmitidosPK;
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
 * @author Edu
 */
@Stateless
public class ChequesEmitidosFacade extends AbstractFacade<ChequesEmitidos>{
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @EJB
    private ProveedoresFacade proveedoresFacade;
    
    @EJB
    private BancosFacade bancosFacade;
    
    public ChequesEmitidosFacade() {
        super(ChequesEmitidos.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<ChequeProveedorDto> listadoChequesNoPagadosProveedores( short codigoEmpresa, 
                                                                        short codigoBanco, 
                                                                        String numeroCheque, 
                                                                        Long montoCheque, 
                                                                        Date fechaInicio, 
                                                                        Date fechaFin, 
                                                                        Short codigoProveedor, 
                                                                        Date fechaCobro){
        
        String sql =  " SELECT h.cod_empr, h.cod_banco, h.nro_cheque, h.xcuenta_bco, h.fcheque, h.icheque, h.cod_proveed, h.fcobro, h.femision, h.iretencion, b.xdesc, c.xnombre"+
                        " FROM cheques_emitidos h, bancos b, proveedores c "+
                        " WHERE h.cod_empr = ?"+
                        " AND h.cod_proveed = c.cod_proveed"+
                        " AND (h.fcobro is null or h.fcobro = '')"+
                        " AND h.cod_banco = b.cod_banco"+
                        " AND h.fcheque >= '01/12/2008'";
        
        if(codigoBanco != 0){
            sql += " AND h.cod_banco = ?";
        }
        
        if(!numeroCheque.equals("") || !numeroCheque.isEmpty()){
            sql += " AND h.nro_cheque = ?";
        }
        
        if(montoCheque != 0){
            sql += " AND h.icheque = ?";
        }
        
        if(fechaInicio != null){
            sql += " AND h.fcheque >= ?";
        }
        
        if(fechaFin != null){
            sql += " AND h.fcheque <= ?";
        }
        
        if(codigoProveedor != 0){
            sql += " AND h.cod_proveed = ?";
        }
        
        sql += " ORDER BY h.fcheque, h.cod_banco";
        
        Query q = em.createNativeQuery(sql);
        
        int i = 1;
        q.setParameter(i, codigoEmpresa);
        
        if(codigoBanco != 0){
            i++;
            q.setParameter(i, codigoBanco);
        }
        
        if(!numeroCheque.equals("") || !numeroCheque.isEmpty()){
            i++;
            q.setParameter(i, numeroCheque);
        }
        
        if(montoCheque != 0){
            i++;
            q.setParameter(i, montoCheque);
        }
        
        if(fechaInicio != null){
            i++;
            q.setParameter(i, fechaInicio);
        }
        
        if(fechaFin != null){
            i++;
            q.setParameter(i, fechaFin);
        }
        
        if(codigoProveedor != null){
            i++;
            q.setParameter(i, codigoProveedor);
        }
        
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<ChequeProveedorDto> listaChequesProveedores = new ArrayList<ChequeProveedorDto>();
        for(Object[] resultado: resultados){
            //clave primaria de cheques emitidos
            ChequesEmitidosPK chequeEmitidoPK = new ChequesEmitidosPK();
            chequeEmitidoPK.setCodEmpr(Short.parseShort(resultado[0].toString()));
            chequeEmitidoPK.setCodBanco(Short.parseShort(resultado[1].toString()));
            chequeEmitidoPK.setNroCheque(resultado[2].toString());
            //cheques emitidos
            ChequesEmitidos chequeEmitido = new ChequesEmitidos();
            chequeEmitido.setChequesEmitidosPK(chequeEmitidoPK);
            chequeEmitido.setXcuentaBco(resultado[3] == null || resultado[3].equals("") ? null: resultado[3].toString());
            
            if(resultado[4] != null){
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());
                chequeEmitido.setFcheque(dateResult_4);
            }else{
                chequeEmitido.setFcheque(null);
            }
            
            chequeEmitido.setIcheque(resultado[5] == null ? null: Long.parseLong(resultado[5].toString()));
            chequeEmitido.setCodProveed(resultado[6] == null ? null : proveedoresFacade.find(Short.parseShort(resultado[6].toString())));
            chequeEmitido.setBancos(resultado[1] == null ? null : bancosFacade.find(Short.parseShort(resultado[1].toString())));
            chequeEmitido.setFcobro(fechaCobro);    //fecha de cobro recibido por parametro desde la vista
            
            if(resultado[8] != null){
                Timestamp timeStamp_8 = (Timestamp) resultado[8];
                java.util.Date dateResult_8 = new Date(timeStamp_8.getTime());
                chequeEmitido.setFemision(dateResult_8);
            }else{
                chequeEmitido.setFemision(null);
            }
                        
            chequeEmitido.setIretencion(resultado[9] == null ? null : Long.parseLong(resultado[9].toString()));
            
            //nombre banco
            String nombreBanco = null;
            if(resultado[10] != null){
                nombreBanco = resultado[10].toString();
            }
            
            //nombre proveedor
            String nombreProveedor = null;
            if(resultado[11] != null){
                nombreProveedor = resultado[11].toString();
            }
            
            ChequeProveedorDto chequeProveedorDto = new ChequeProveedorDto();
            chequeProveedorDto.setChequeEmitido(chequeEmitido);
            chequeProveedorDto.setNombreBanco(nombreBanco);
            chequeProveedorDto.setNombreProveedor(nombreProveedor);
            chequeProveedorDto.setChequeEmitidoSeleccionado(false);
            
            listaChequesProveedores.add(chequeProveedorDto);
            
        }
        
        return listaChequesProveedores;
    }
    
    public int actualizarChequesEmitidosNoCobrados(ChequesEmitidos chequeEmitido){
        String sql = "UPDATE cheques_emitidos SET fcobro = ? WHERE cod_empr = 2 AND nro_cheque = ? AND cod_banco = ?";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, chequeEmitido.getFcobro());
        q.setParameter(2, chequeEmitido.getChequesEmitidosPK().getNroCheque());
        q.setParameter(3, chequeEmitido.getChequesEmitidosPK().getCodBanco());
        return q.executeUpdate();
    }
    
    public void insertarChequeEmitido(  short gCodEmpresa,
                                        short lCodBanco,
                                        String lNroCheque,
                                        String lXCuentaBco,
                                        String lFCheque,
                                        String lFemision,
                                        long lICheque,
                                        Short lCodProveed){
        String sql =    "INSERT INTO cheques_emitidos (cod_empr, cod_banco, " +
                        "nro_cheque, xcuenta_bco, fcheque, femision, icheque, cod_proveed) " +
                        "VALUES ("+gCodEmpresa+", "+lCodBanco+", '"+lNroCheque+"', " +
                        "'"+lXCuentaBco+"', '"+lFCheque+"', '"+lFemision+"', "+lICheque+", "+lCodProveed+")";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
}
