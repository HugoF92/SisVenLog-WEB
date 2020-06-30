/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Bancos;
import entidad.Cheques;
import entidad.Clientes;
import entidad.Zonas;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author admin
 */
@Stateless
public class LiChequesFacade extends AbstractFacade<Cheques> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @EJB
    private ClientesFacade clientesFacade;

    public LiChequesFacade() {
        super(Cheques.class);
    }
    
    public String crearSQL(String disc, String tipo, String cob, Bancos banco, String cobroDesde, String cobroHasta,
            String emisionDesde, String emisionHasta, String chequeDesde, String chequeHasta, Zonas zona, Boolean todosCliente,
            String nomCliente, Integer codCliente){
        
        String sql = crearSqlBusqueda(disc, tipo, cob, banco, cobroDesde, cobroHasta,
            emisionDesde, emisionHasta, chequeDesde, chequeHasta, zona, todosCliente,
            nomCliente, codCliente);
        
        sql = crearSqlReporte(sql, disc, tipo, cob);
        
        return sql;
    }

    public String crearSqlBusqueda(String disc, String tipo, String cob, Bancos banco, String cobroDesde, String cobroHasta,
            String emisionDesde, String emisionHasta, String chequeDesde, String chequeHasta, Zonas zona, Boolean todosCliente,
            String nomCliente, Integer codCliente) {
        // disc --> (N) No discriminar, (B) Por Banco, (R) Con Recibo
        // tipo --> (T) Todos, (D) Diferidos, (C) Cobro Credito, (A) Al Dia
        // cob -- > (T) Todos, (C) Cobrados, (N) No cobrados

        String sql;

        if (disc.equals("N")) {
            sql = " SELECT c.*, b.cod_banco as cod_banco2, b.xdesc as xdesc_banco,  "
                    + " b.xdesc as xdesc2_banco, t.xnombre, r.ipagado, r.nrecibo "
                    + " FROM cheques c INNER JOIN recibos_cheques r "
                    + " ON c.nro_cheque = r.nro_cheque AND c.cod_banco = r.cod_banco AND r.cod_empr= 2 INNER JOIN recibos rb "
                    + " ON r.nrecibo = rb.nrecibo and rb.mestado ='A' AND rb.cod_empr = 2, bancos b, clientes t "
                    + " WHERE c.cod_empr= 2  and c.cod_banco = b.cod_banco "
                    + " AND t.cod_cliente = c.cod_cliente ";
        } else {
            sql = " SELECT c.*, b.cod_banco as cod_banco2, b.xdesc as xdesc_banco, "
                    + " b.xdesc as xdesc2_banco, t.xnombre "
                    + " FROM cheques c , bancos b, clientes t "
                    + " WHERE c.cod_empr= 2 and c.cod_banco = b.cod_banco "
                    + " AND t.cod_cliente = c.cod_cliente ";
        }

        switch (tipo) {
            case "C":
                sql += " AND c.fcobro IS NOT NULL ";
            case "N":
                sql += " AND c.fcobro IS NULL ";
            default:
                break;
        }

        switch (tipo) {
            case "D":
                sql += " AND c.mtipo = 'D' ";
                break;
            case "C":
                sql += " AND c.mtipo = 'R' ";
                break;
            case "A":
                sql += " AND c.mtipo = 'I' ";
                break;
            default:
                break;
        }
        
        if(banco != null && banco.getCodBanco() != null){
            Short cod_banco = banco.getCodBanco();
            sql += " AND c.cod_banco = " + cod_banco + " ";
        }
        
        if(!cobroDesde.isEmpty() || !cobroHasta.isEmpty()){
            sql += " AND c.fcobro BETWEEN '" + cobroDesde + "' AND '" + cobroHasta + "' ";
        }
        
        if(!emisionDesde.isEmpty() || !emisionHasta.isEmpty()){
            sql += " AND c.femision BETWEEN '" + emisionDesde + "' AND '" + emisionHasta + "' ";
        }
    		
        if(!chequeDesde.isEmpty() || !chequeHasta.isEmpty()){
            sql += " AND c.fcheque BETWEEN '" + chequeDesde + "' AND '" + chequeHasta + "' ";
        }
    	
        if(zona!= null && !zona.getZonasPK().getCodZona().isEmpty()){
            String cod_zona = zona.getZonasPK().getCodZona();
            sql += " AND c.cod_zona = '" + cod_zona  + "' ";
        }
        
        //Control de cliente
        List<Clientes> clientesVar = new ArrayList<Clientes>();
        if(todosCliente == true || (codCliente == null && nomCliente.isEmpty())){
            clientesVar.addAll(clientesFacade.findAll());
        }else if(codCliente == null){
            clientesVar.addAll(clientesFacade.buscarPorNombre(nomCliente));
        }else{
            clientesVar.addAll(clientesFacade.buscarPorCodigoNombre(codCliente, nomCliente));
        }
        
        String codigosClientes = convertirCodigosClientesString(clientesVar);
        
        if(!clientesVar.isEmpty()) sql += " AND c.cod_cliente IN (" + codigosClientes + ") ";

        return sql;
    }
    
    public String crearSqlReporte(String sql, String disc, String tipo, String cob){
        
        String sent;
        
        // disc --> (N) No discriminar, (B) Por Banco, (R) Con Recibo
        // tipo --> (T) Todos, (D) Diferidos, (C) Cobro Credito, (A) Al Dia
        // cob -- > (T) Todos, (C) Cobrados, (N) No cobrados        
        
                
        switch (disc) {
            case "N":
                sent = "SELECT '' as cod_banco, '' as xdesc_banco, nro_cheque, xcuenta_bco, "
                        + " fcheque, icheque, cod_cliente, xnombre, cod_banco2, xdesc2_banco, "
                        + " frechazo, fcobro, femision, xtitular,mtipo, cod_zona "
                        + " FROM (" + sql + ") as s "
                        + " ORDER BY s.fcheque, s.nro_cheque ";
                break;
            case "B":
                sent = " SELECT  cod_banco, xdesc_banco, nro_cheque, xcuenta_bco, "
                        + " fcheque, icheque, cod_cliente, xnombre, cod_banco2, xdesc2_banco, "
                        + " frechazo, fcobro, femision, xtitular,mtipo, cod_zona "
                        + " FROM (" + sql + ") as s "
                        + " ORDER BY s.cod_banco, s.fcheque, s.nro_cheque ";
                break;
                
             case "R":
                sent = " SELECT  cod_banco, xdesc_banco, nro_cheque, xcuenta_bco, "
                        + " fcheque, icheque, cod_cliente, xnombre, cod_banco2, xdesc2_banco, "
                        + " frechazo, fcobro, femision, xtitular,mtipo, cod_zona, NRECIBO, ipagado "
                        + " FROM (" + sql + ") as s "
                        + " ORDER BY s.nro_cheque ";
                break;
            default:
                sent = null;
                break;
        }

        return sent;
    }
    
    public List<Object[]> ejecutarSql(String sql){
        Query q = getEntityManager().createNativeQuery(sql);
        
        System.out.println(q.toString());
        
        List<Object[]> respuesta = q.getResultList();
        
        return respuesta;
    }
    
    private String convertirCodigosClientesString(List<Clientes> clientes) {
        String listaCodigos = "";
        for (int i = 0; i < clientes.size(); i++) {
            if (i == 0) {
                listaCodigos += clientes.get(i).getCodCliente().toString();
            } else {
                listaCodigos += "," + clientes.get(i).getCodCliente().toString();
            }
        }
        return listaCodigos;
    }
}
