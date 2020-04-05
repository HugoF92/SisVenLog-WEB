/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FacturaDetDto;
import entidad.FacturasDet;
import java.math.BigDecimal;
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
public class FacturasDetFacade extends AbstractFacade<FacturasDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturasDetFacade() {
        super(FacturasDet.class);
    }
    
     public List<FacturaDetDto> obtenerTotalesPorNroFacturaYTipo(long lNroFact, String lTipoDoc){
        String sql =    "SELECT pimpues, SUM(impuestos) as timp, sum(igravadas) as tgrav " +
                        "FROM facturas_det " +
                        "WHERE nrofact = "+lNroFact+" "+
                        "AND ctipo_docum = '"+lTipoDoc+"' "+
                        "GROUP BY pimpues";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<FacturaDetDto> listadoFacturas = new ArrayList<>();
        for(Object[] resultado: resultados){
            FacturaDetDto f = new FacturaDetDto();
            f.setPimpues(resultado[0] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[0].toString())));
            f.setTotalImpuestos(resultado[1] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[1].toString())));
            f.setTotalIgravadas(resultado[2] == null ? 0 : Long.parseLong(resultado[2].toString()));
            listadoFacturas.add(f);
        }
        return listadoFacturas;
    }
    
    public void insertarFacturasDet(    String lCTipoDocum,
                                        long lNroFact,
                                        String lCodMerca,
                                        String lXDesc,
                                        int lCantCajas,
                                        int lCantUnid,
                                        long lIPrecioCaja,
                                        long lIPrecioUnidad,
                                        long lIExentas,
                                        long lIGravadas,
                                        long lImpuestos,
                                        long lIDescuentos,
                                        long lITotal,
                                        BigDecimal lPDesc,
                                        BigDecimal lPImpues,
                                        Integer lNroPromo,
                                        String lFFactura){
        String sql =    "INSERT INTO facturas_det (cod_empr, ctipo_docum, nrofact, " +
                        "cod_merca, xdesc, cant_cajas, cant_unid, iprecio_caja, iprecio_unidad, iexentas, igravadas, " +
                        "impuestos, idescuentos, itotal, pdesc,  pimpues, nro_promo, ffactur) values(" +
                        "2, '"+lCTipoDocum+"', "+lNroFact+", '"+lCodMerca+"', " +
                        "'"+lXDesc+"', "+lCantCajas+", "+lCantUnid+", " +
                        ""+lIPrecioCaja+", "+lIPrecioUnidad+", "+lIExentas+", "+lIGravadas+", "+lImpuestos+", "+lIDescuentos+", "+lITotal+", " +
                        "'"+lPDesc+"', "+lPImpues+", "+lNroPromo+", '"+lFFactura+"')";
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public List<FacturasDet> obtenerDetallesDeFacturas(String lCTipoDocum, long lNroFact, String lFFactura){
        String sql =    "SELECT * " +
                        "FROM facturas_det " +
                        "WHERE nrofact = "+lNroFact+" "+
                        "AND ctipo_docum = '"+lCTipoDocum+"' "+
                        "AND ffactur = '"+lFFactura+"'";
        Query q = em.createNativeQuery(sql, FacturasDet.class);
        System.out.println(q.toString());
        List<FacturasDet> resultados = q.getResultList();
        return resultados;
    }
}
