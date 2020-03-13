/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.TiposVentas;
import entidad.TiposVentasPK;
import java.util.ArrayList;
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
public class TiposVentasFacade extends AbstractFacade<TiposVentas> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiposVentasFacade() {
        super(TiposVentas.class);
    }
    
    public List<TiposVentas> obtenerTiposVentasDelCliente(Integer lCodCliente){
        String sql =    "SELECT t.ctipo_vta, t.xdesc FROM ventas_clientes v, tipos_ventas t WHERE " +
                        "cod_cliente = "+lCodCliente+" AND v.ctipo_vta = t.ctipo_vta";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<TiposVentas> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            char cEstado = resultado[0] == null ? 0 : resultado[0].toString().charAt(0);
            TiposVentas tv = new TiposVentas();
            TiposVentasPK tvPK = new TiposVentasPK();
            tvPK.setCtipoVta(cEstado);
            tvPK.setCodEmpr(Short.parseShort("2"));
            tv.setTiposVentasPK(tvPK);
            tv.setXdesc(resultado[1] == null ? "" : resultado[1].toString());
            listado.add(tv);
        }
        return listado;
    }
    
     public List<TiposVentas> obtenerTiposVentasPorTipo(String tipo){
        String sql =    "SELECT * FROM tipos_ventas WHERE " +
                        "ctipo_vta = '" + tipo + "' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<TiposVentas> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            char cEstado = resultado[0] == null ? 0 : resultado[0].toString().charAt(0);
            TiposVentas tv = new TiposVentas();
            TiposVentasPK tvPK = new TiposVentasPK();
            tvPK.setCtipoVta(cEstado);
            tvPK.setCodEmpr(Short.parseShort("2"));
            tv.setTiposVentasPK(tvPK);
            tv.setXdesc(resultado[1] == null ? "" : resultado[1].toString());
            listado.add(tv);
        }
        return listado;
    }
    
}
