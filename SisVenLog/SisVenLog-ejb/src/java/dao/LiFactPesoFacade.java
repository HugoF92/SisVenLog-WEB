/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Mercaderias;
import entidad.Promociones;
import entidad.Sublineas;
import entidad.TiposDocumentos;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;

/**
 *
 * @author Hugo
 */
@Stateless
public class LiFactPesoFacade extends AbstractFacade<Mercaderias> {
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @EJB
    private ClientesFacade clientesFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public LiFactPesoFacade() {
        super(Mercaderias.class);
    }
    
    public List<Object[]> ejecutar(Lineas lineas, Sublineas sublineas, Promociones promocion, TiposDocumentos tipoDocumento,
            Empleados vendedor, Clientes cliente, Date fechaDesde, Date fechaHasta, Boolean todosCliente, Integer codCliente, String nombreCliente, List<Mercaderias> mercaderias) {

        String sql = " SELECT f.ctipo_docum, f.nrofact , f.ffactur, "
                + " f.cod_cliente,f.xrazon_social as xnombre, "
                + " d.cod_merca, m.xdesc, d.cant_cajas, d.cant_unid, d.cant_cajas "
                + " * m.npeso_caja as tpeso_cajas, d.cant_unid * m.npeso_unidad as tpeso_unid, "
                + " d.itotal, f.cod_vendedor, v.xnombre as xvendedor, 0 as "
                + " nro_promo, f.nrofact as nfactura, 0 as pdesc "
                + " FROM facturas f, facturas_det d, mercaderias m, "
                + " empleados v, sublineas s "
                + " WHERE (f.cod_empr = 2) "
                + " AND v.cod_empleado = f.cod_vendedor "
                + " AND (f.mestado = 'A') "
                + " AND f.cod_empr = 2 "
                + " AND d.cod_empr = 2 "
                + " AND (f.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde) + "' AND '" + DateUtil.dateToString(fechaHasta) + "') "
                + " AND f.nrofact = d.nrofact "
                + " AND AND d.cod_merca IN ( " + convertirCodigosMercaderiasString(mercaderias) + " ) "
                + " AND f.ctipo_docum = d.ctipo_docum "
                + " AND f.cod_empr = d.cod_empr "
                + " AND d.cod_merca = m.cod_merca "
                + " AND m.cod_sublinea = s.cod_sublinea ";
        
        if(promocion.getNroPromoGral() != null){
            Long nro_promo = promocion.getNroPromoGral();
            sql += " AND d.cod_merca in (SELECT cod_merca "
                + " FROM promociones_det "
                + " WHERE nro_promo = " + nro_promo + ") ";
        }
        if(!tipoDocumento.getCtipoDocum().isEmpty()){
            String ctipo_docum = tipoDocumento.getCtipoDocum();
            sql += " AND f.ctipo_docum = " + ctipo_docum + " ";
        }
        if(vendedor.getEmpleadosPK() != null){
            Short cod_empleado = vendedor.getEmpleadosPK().getCodEmpleado();
            sql += " AND f.cod_vendedor = " + cod_empleado + " ";
        }
        if(sublineas.getCodSublinea() != null){
            Short cod_sublinea = sublineas.getCodSublinea();
            sql += " AND m.cod_sublinea = " + cod_sublinea + " ";
        }
        if(lineas.getCodLinea() != null){
            Short cod_linea = lineas.getCodLinea();
            sql += " AND s.cod_linea = " + cod_linea + " ";
        }
        
        //Control de cliente
        List<Clientes> clientesVar;
        if(todosCliente == true){
            clientesVar = clientesFacade.findAll();
        }else{
            clientesVar = clientesFacade.buscarPorCodigoNombre(codCliente, nombreCliente);
        }
        
        String codigosClientes = convertirCodigosClientesString(clientesVar);
        
        if(!clientesVar.isEmpty()) sql += " AND f.cod_cliente IN (" + codigosClientes + ") ";
        
        sql += " UNION ALL "
            + " SELECT  n.ctipo_docum, n.nro_nota as nrofact, n.fdocum as ffactur, n.cod_cliente, f.xrazon_social as xnombre, "
            + " d.cod_merca, m.xdesc, d.cant_cajas, d.cant_unid, 0 tpeso_cajas, 0 as tpeso_unid, "
            + " d.igravadas + d.iexentas as itotal, f.cod_vendedor, v.xnombre  as xvendedor, n.nro_promo, n.nrofact as nfactura, d.pdesc " 
            + " FROM notas_ventas n, notas_ventas_det d, mercaderias m, empleados v, sublineas s, facturas f "
            + " WHERE     (n.cod_empr = 2) "
            + " AND v.cod_empleado = f.cod_vendedor "
            + " AND (f.mestado = 'A') "
            + " AND f.cod_empr = 2 "
            + " AND d.cod_empr  = 2 "
            + " AND (n.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde) + "' AND '" + DateUtil.dateToString(fechaHasta) + "') "
            + " AND n.cconc = 'DPR' "
            + " AND n.fac_ctipo_docum = f.ctipo_docum "
            + " AND n.nrofact = f.nrofact "
            + " AND n.nro_nota = d.nro_nota "
            + " AND d.cod_merca IN ( " + convertirCodigosMercaderiasString(mercaderias) + " ) "
            + " AND n.ctipo_docum = d.ctipo_docum "
            + " AND n.cod_empr = d.cod_empr "
            + " AND d.cod_merca = m.cod_merca "
            + " AND m.cod_sublinea = s.cod_sublinea ";
        
        if(!tipoDocumento.getCtipoDocum().isEmpty()){
            String ctipo_docum = tipoDocumento.getCtipoDocum();
            sql += " AND n.ctipo_docum  = " + ctipo_docum + " ";
        }

        if (vendedor.getEmpleadosPK() != null) {
            Short cod_empleado = vendedor.getEmpleadosPK().getCodEmpleado();
            sql += " AND f.cod_vendedor  = " + cod_empleado + " ";
        }
        if (sublineas.getCodSublinea() != null) {
            Short cod_sublinea = sublineas.getCodSublinea();
            sql += " AND m.cod_sublinea  = " + cod_sublinea + " ";

        }
        if (promocion.getNroPromoGral() != null) {
            Long nro_promo = promocion.getNroPromoGral();
            sql += " AND n.nro_promo  = " + nro_promo + " ";
        }

        if (lineas.getCodLinea() != null) {
            Short  cod_linea = lineas.getCodLinea();
            sql += " AND s.cod_linea  = " + cod_linea + " ";
        }
        //codigo cliente
        if (!codigosClientes.isEmpty()) {
            sql += " AND f.cod_cliente IN ( " + codigosClientes + ") ";
        }

        sql += " ORDER BY 4, 3, 1 ";
        
        Query q = getEntityManager().createNativeQuery(sql);
        
        System.out.println(q.toString());

        List<Object[]> respuesta = q.getResultList();

        return respuesta;
    }
    
    private String convertirCodigosClientesString(List<Clientes> clientes) {
        String listaCodigos = "";
        for (int i = 0; i < clientes.size(); i++) {
            if (i == 0) {
                listaCodigos += clientes.get(i).toString();
            } else {
                listaCodigos += "," + clientes.get(i).toString();
            }
        }
        return listaCodigos;
    }
    
    private String convertirCodigosMercaderiasString(List<Mercaderias> mercaderias) {
        String listaCodigos = "";
        for (int i = 0; i < mercaderias.size(); i++) {
            if (i == 0) {
                listaCodigos += mercaderias.get(i).toString();
            } else {
                listaCodigos += "," + mercaderias.get(i).toString();
            }
        }
        return listaCodigos;
    }
    
}
