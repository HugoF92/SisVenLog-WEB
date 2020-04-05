/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.FacturasSer;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class FacturasSerFacade extends AbstractFacade<FacturasSer> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacturasSerFacade() {
        super(FacturasSer.class);
    }
	
	public void insertarFacturasSer(String lCTipoDocum, 
                                    long lNroFact, 
                                    short lCodServicio, 
                                    Long lExentas, 
                                    Long lGravadas, 
                                    BigDecimal lImpuestos, 
                                    Long lTotal,
                                    BigDecimal pImpues,
                                    String lFFactura){
        String sql =    "INSERT INTO facturas_ser ( cod_empr, ctipo_docum, nrofact, " +
                        "cod_servicio, iexentas, igravadas, " +
                        "impuestos, itotal, pimpues, ffactur ) values ( " +
                        "2, '"+lCTipoDocum+"', "+lNroFact+", "+lCodServicio+", " +
                        ""+lExentas+", "+lGravadas+", "+lImpuestos+",  "+lTotal+", " +
                        ""+pImpues+", '"+lFFactura+"' )";
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
    }
    
    
}
