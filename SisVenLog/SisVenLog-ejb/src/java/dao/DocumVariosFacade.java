/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.DocumVarios;
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
public class DocumVariosFacade extends AbstractFacade<DocumVarios> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumVariosFacade() {
        super(DocumVarios.class);
    }

    public List<DocumVarios> listarDocumValidos(String sql) {
        Query q = getEntityManager().createNativeQuery(sql, DocumVarios.class);

        System.out.println(q.toString());

        List<DocumVarios> respuesta = new ArrayList<DocumVarios>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public Integer actualizarVigenciaPromocion(int nro_documento,String usuarioAutorize) {
        try {
            Query q = getEntityManager().createNativeQuery(""
                    + " UPDATE docum_varios SET mestado = 'A',  cusuario_autoriz = '"+usuarioAutorize+"'"
                    + " WHERE ndocum ="+nro_documento
                    + " AND cod_empr = 2 AND mestado = 'P' "
                    + " AND ctipo_docum = 'SG' ");
            System.out.println(q.toString());
            Integer respuesta = q.executeUpdate();
            return respuesta;
        } catch (Exception e) {
            System.out.println("MESAJE DE ERROR:");
            return -1;
        }
    }
    
    
    
    public DocumVarios verificarObjecto(DocumVarios entity){
        if (!getEntityManager().contains(entity)) {
                entity = getEntityManager().merge(entity);
        }
        return entity;
    }

}
