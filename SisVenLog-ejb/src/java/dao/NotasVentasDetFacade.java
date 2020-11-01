/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.NotasVentasDet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;

/**
 *
 * @author admin
 */
@Stateless
public class NotasVentasDetFacade extends AbstractFacade<NotasVentasDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotasVentasDetFacade() {
        super(NotasVentasDet.class);
    }
    
    public void insertarNotaVentaDet(NotasVentasDet notaVentaDet){
        String sql =  "INSERT INTO notas_ventas_det(cod_empr, ctipo_docum, nro_nota, cod_merca, xdesc, "
                + " pdesc, cant_cajas, cant_unid, iexentas, igravadas, impuestos, iprecio_caja, " 
                + " iprecio_unid, pimpues, fdocum) "
                + " VALUES " 
                + " ( " + notaVentaDet.getNotasVentasDetPK().getCodEmpr() + ", '" + notaVentaDet.getNotasVentasDetPK().getCtipoDocum() + "', "
                + notaVentaDet.getNotasVentasDetPK().getNroNota() + ", '" +notaVentaDet.getMercaderia().getMercaderiasPK().getCodMerca() + "', '"
                + notaVentaDet.getMercaderia().getXdesc() + "', " + notaVentaDet.getPdesc() + ", " + notaVentaDet.getCantCajas() + ", "
                + notaVentaDet.getCantUnid() + ", " + notaVentaDet.getIexentas() + ", " + notaVentaDet.getIgravadas() + ", " 
                + notaVentaDet.getImpuestos() + ", " + notaVentaDet.getIprecioCaja() + ", " +notaVentaDet.getIprecioUnid() + ", "
                + notaVentaDet.getPimpues() + ", '" + DateUtil.dateToString(notaVentaDet.getNotasVentasDetPK().getFdocum())
                + "')";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
    public void borrarNotaVentaDet(long lNroNota, String lCTipoDocum, String fdocum){
        String sql =    "DELETE FROM notas_ventas_det " +
                        "WHERE cod_empr = 2 AND nro_nota = "+lNroNota+" "+
                        " AND ctipo_docum = '"+lCTipoDocum+"' " +
                        " AND fdocum = '"+fdocum+"'";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }

}
