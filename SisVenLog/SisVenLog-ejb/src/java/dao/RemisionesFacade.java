/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Remisiones;
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
public class RemisionesFacade extends AbstractFacade<Remisiones> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RemisionesFacade() {
        super(Remisiones.class);
    }

    public List<Remisiones> buscarNroInicialRemision() {
        Query q = getEntityManager().createNativeQuery("select top 1 *\n"
                + " from remisiones\n"
                + " where cod_empr=2\n"
                + " order by fremision desc, nro_remision desc", Remisiones.class);

        System.out.println(q.toString());

        List<Remisiones> respuesta = new ArrayList<Remisiones>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public List<Object[]> grabarRemisionComplementaria(String l_fremision, Integer l_nro_envio_ini, Integer l_nro_envio_fin) {
        Query q = getEntityManager().createNativeQuery("SELECT d.cod_merca, m.xdesc, m.nrelacion,SUM(d.cant_cajas) as cant_cajas,"
                + " SUM(d.cant_unid) as cant_unid "
                + " FROM envios e, envios_det d, mercaderias m "
                + " WHERE  d.cod_empr = 2 "
                + " and e.depo_destino = 1  "
                + " AND e.fenvio =CONVERT(SMALLDATETIME,'" + l_fremision + "',103) "
                + " and d.cod_merca = m.cod_merca "
                + " AND e.nro_envio = d.nro_envio "
                + " AND e.cod_empr = d.cod_empr "
                + " AND e.nro_envio BETWEEN  " + l_nro_envio_ini + " AND " + l_nro_envio_fin
                + " AND e.mestado IN ('A','I') "
                + " GROUP BY d.cod_merca, m.xdesc, m.nrelacion");
        System.out.println(q.toString());
        List<Object[]> results = q.getResultList();
        return results;
    }

    public List<Object[]> insertarFacturasRemisiones(String l_fremision, short l_cod_depo_destino, String l_merca) {
        Query q = getEntityManager().createNativeQuery("SELECT  DISTINCT f.ctipo_docum, f.nrofact as ndocum "
                + " FROM facturas f, facturas_det d "
                + " WHERE  f.cod_empr = 2  AND d.cod_empr = 2 "
                + " AND f.ctipo_docum IN ('FCO','FCR','CPV') "
                + " AND f.mestado = 'A' "
                + " AND f.ffactur =  CONVERT(SMALLDATETIME,'" + l_fremision + "',103)"
                + " AND f.nrofact = d.nrofact \n"
                + " AND f.ctipo_docum = d.ctipo_docum \n"
                + " AND d.cod_empr = 2 \n"
                + " AND f.cod_depo = " + String.valueOf(l_cod_depo_destino)
                + " AND d.cod_merca = '" + l_merca+"'");
        System.out.println(q.toString());
        List<Object[]> results = q.getResultList();
        return results;
    }

    public int verificarNroRemision(Integer l_fremision) {
        Query q = getEntityManager().createNativeQuery("SELECT * "
                + "FROM remisiones "
                + " WHERE cod_empr = 2 "
                + " AND nro_remision  = " + l_fremision);
        System.out.println(q.toString());
        return q.getResultList().size();
    }

    public int verificarNroRemisionDet(String l_fremision, String cod_merca) {
        Query q = getEntityManager().createNativeQuery("SELECT * "
                + "FROM remisiones_det "
                + " WHERE cod_empr = 2 "
                + " AND nro_remision  = " + l_fremision
                + " AND  cod_merca = '" + cod_merca + "'");
        System.out.println(q.toString());
        return q.getResultList().size();
    }

    public int verificarRemisionFactura(String l_remision, String ctipo_docum, String l_nro_factura) {
        Query q = getEntityManager().createNativeQuery("SELECT nrofact "
                + " FROM remisiones_facturas "
                + " WHERE cod_empr = 2 "
                + " AND nro_remision  = "+l_remision
                + " AND ctipo_docum = '"+ctipo_docum+"'"
                + " AND nrofact = "+l_nro_factura);
        System.out.println(q.toString());
        return q.getResultList().size();
    }

}
