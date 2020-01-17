/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.DocumVariosDet;
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
public class DocumVariosDetFacade extends AbstractFacade<DocumVariosDet> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumVariosDetFacade() {
        super(DocumVariosDet.class);
    }

    public List<DocumVariosDet> listarDocumentosVarioDetPorNroDocument(int nro_documento) {

        Query q = getEntityManager().createNativeQuery("SELECT d.*,p.*"
                + "  FROM docum_varios_det d, promociones p "
                + "  WHERE d.cod_empr = 2 AND d.ctipo_docum = 'SG' AND d.nro_promo = p.nro_promo "
                + "  AND p.cod_empr = 2 "
                + "  AND d.ndocum = " + nro_documento,
                DocumVariosDet.class);
        System.out.println(q.toString());
        return q.getResultList();
    }

}
