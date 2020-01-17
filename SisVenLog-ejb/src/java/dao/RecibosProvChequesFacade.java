/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.RecibosProvCheques;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author admin
 */
@Stateless
public class RecibosProvChequesFacade extends AbstractFacade<RecibosProvCheques> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecibosProvChequesFacade() {
        super(RecibosProvCheques.class);
    }
    
    public void insertarChequeReciboProveedor(  short gCodEmpresa,
                                                Short lCodProveed,
                                                long lNroRecibo,
                                                Short lCodbanco,
                                                String lNroCheque,
                                                long lIPagado,
                                                String lFRecibo){
        String sql =    "INSERT INTO recibos_prov_cheques(cod_empr, cod_proveed, nrecibo, " +
                        "cod_banco, nro_cheque, ipagado, frecibo) " +
                        "values ("+gCodEmpresa+", "+lCodProveed+", "+lNroRecibo+", " +
                        ""+lCodbanco+", '"+lNroCheque+"', "+lIPagado+", '"+lFRecibo+"')";
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        q.executeUpdate();
    }
    
}
