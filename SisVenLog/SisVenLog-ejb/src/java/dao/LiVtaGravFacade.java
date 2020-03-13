/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Mercaderias;
import entidad.Sublineas;
import entidad.TiposVentas;
import entidad.Zonas;
import java.util.ArrayList;
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
 * @author asus
 */
@Stateless
public class LiVtaGravFacade extends AbstractFacade<Mercaderias> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @EJB
    private TiposVentasFacade tiposVentasFacade;

    public LiVtaGravFacade() {
        super(Mercaderias.class);
    }
    
    public String crearSQL(Sublineas sublineas, TiposVentas tiposVentas, Zonas zonas,
            Date fechaDesde, Date fechaHasta, List<Mercaderias> mercaderias, String tipo) {
        
        String sql = "SELECT datepart(mm, f.ffactur) AS nmes, datepart(yy, f.ffactur) AS nanno, F.CTIPO_VTA, v.xdesc as xdesc_vta, "
                + " d.cod_merca, M.XDESC, SUM(d.igravadas + d.iexentas) AS igravadas FROM facturas f, FACTURAS_DET d, mercaderias m, tmp_mercaderias t, tipos_ventas v "
                + " WHERE f.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde) + "' AND '" + DateUtil.dateToString(fechaHasta) + "' "
                + " AND f.mestado = 'A' AND f.nrofact = d .nrofact AND f.ctipo_docum = d.ctipo_docum "
                + " AND f.cod_empr = 2 AND d .cod_empr = 2 AND d .cod_merca = m.cod_merca "
                + " AND d.cod_merca = t.cod_merca AND f.ctipo_vta = v.ctipo_vta";

        if(zonas != null && !zonas.getZonasPK().getCodZona().isEmpty()){
            String cod_zona = zonas.getZonasPK().getCodZona();
            sql =  sql + " AND f.cod_zona = '" + cod_zona + "' ";
        }
        
        if(sublineas != null && sublineas.getCodSublinea() != null){
            Short cod_sublinea = sublineas.getCodSublinea();
            sql =  sql + " AND m.cod_sublinea = " + cod_sublinea + " ";
        }
        
        //Control de tipos de ventas
        List<TiposVentas> tiposVentasVar = new ArrayList<TiposVentas>();
        if(tiposVentas == null){
            tiposVentasVar = tiposVentasFacade.findAll();
        }else{
            tiposVentasVar = tiposVentasFacade.obtenerTiposVentasPorTipo(tipo);
        }

        String codigosTiposVentas = convertirCodigosTiposVentasString(tiposVentasVar);
        
        if(!codigosTiposVentas.isEmpty())
            sql += " AND f.ctipo_vta IN (" + codigosTiposVentas + ") ";
        
        sql = sql + " GROUP BY datepart(mm, f.ffactur), datepart(yy, f.ffactur), "
                + " F.CTIPO_VTA, v.xdesc, d.cod_merca, M.XDESC "
                + " UNION ALL ";

        sql = sql + " SELECT datepart(mm, f.fdocum) AS nmes, datepart(yy, fa.ffactur) AS nanno,Fa.CTIPO_VTA, "
                + " v.xdesc as xdesc_vta, d .cod_merca, m.xdesc, "
                + " SUM(d.igravadas + d.iexentas) * - 1 AS igravadas "
                + " FROM notas_ventas f, notas_ventas_DET d, mercaderias m, FACTURAS FA, tipos_ventas v, tmp_mercaderias t "
                + " WHERE f.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde) + "' AND '" + DateUtil.dateToString(fechaHasta) + "' "
                + " AND f.mestado = 'A' AND f.fdocum = d.fdocum"
                + " AND f.nro_nota = d.nro_nota AND f.cod_empr = 2 AND d.cod_empr = 2"
                + " AND d.cod_merca = m.cod_merca AND d.cod_merca = t.cod_merca AND f.nrofact  = fa.nrofact "
                + " AND fa.ctipo_vta = v.ctipo_vta ";
        
        if(zonas != null && !zonas.getZonasPK().getCodZona().isEmpty()){
            String cod_zona = zonas.getZonasPK().getCodZona();
            sql =  sql + " AND f.cod_zona = '" + cod_zona + "' ";
        }
        
        if(sublineas != null && sublineas.getCodSublinea() != null){
            Short cod_sublinea = sublineas.getCodSublinea();
            sql =  sql + " AND m.cod_sublinea = " + cod_sublinea + " ";
        }
        
        if(!codigosTiposVentas.isEmpty())
            sql += " AND fa.ctipo_vta IN (" + codigosTiposVentas + ") ";
        
        sql = sql + " GROUP BY datepart(mm, f.fdocum), datepart(yy, fa.ffactur) ,Fa.CTIPO_VTA, v.xdesc, d .cod_merca, M.XDESC ";

        return sql;
    }
    
    public List<Object[]> ejecutarSql(String sql){
        Query q = getEntityManager().createNativeQuery(sql);

        System.out.println(q.toString());

        List<Object[]> respuesta = q.getResultList();

        return respuesta;
    }
    
    private String convertirCodigosTiposVentasString(List<TiposVentas> tiposVentas) {
        String listaCodigos = "";
        for (int i = 0; i < tiposVentas.size(); i++) {
            if (i == 0) {
                listaCodigos += "'" + tiposVentas.get(i).getTiposVentasPK().getCtipoVta() + "'";
            } else {
                listaCodigos += "," + "'" + tiposVentas.get(i).getTiposVentasPK().getCtipoVta().toString() + "'";
            }
        }
        return listaCodigos;
    }
    
    private String convertirCodigosMercaderiasString(List<Mercaderias> mercaderias) {
        String listaCodigos = "";
        for (int i = 0; i < mercaderias.size(); i++) {
            if (i == 0) {
                listaCodigos += "'" + mercaderias.get(i).getMercaderiasPK().getCodMerca().toString() + "'";
            } else {
                listaCodigos += "," + "'" + mercaderias.get(i).getMercaderiasPK().getCodMerca().toString() + "'";
            }
        }
        return listaCodigos;
    }
}
