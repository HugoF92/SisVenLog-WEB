/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.TiposDocumentos;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author WORK
 */
@Stateless
public class TiposDocumentosFacade extends AbstractFacade<TiposDocumentos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiposDocumentosFacade() {
        super(TiposDocumentos.class);
    }

    public List<TiposDocumentos> listarTipoDocumentoImpresionMasivaFacturas() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCO','FCR','CPV')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public List<TiposDocumentos> listarTipoDocumentoLiAnudoc() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCR','FCO','CPV','CVC','COC', 'FCC','PED','EN','NCV','NDV', 'NDP', 'NCC', 'NDC','AJ','RM','REC','CHQ','REP','CHE')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public List<TiposDocumentos> listarTipoDocumentoLiFacBonif() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCO','CPV','FCR')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public List<TiposDocumentos> listarTipoDocumentoParaConsultaDeVentas() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCO','CPV','FCR', 'PED', 'NCV', 'NDV')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
     
    public List<TiposDocumentos> listarTipoDocumentoGenDocuAnul() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCO', 'EN', 'NCV', 'REM')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public TiposDocumentos getTipoDocumentoByCTipoDocumento(String cTipoDocumento) {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('"+ cTipoDocumento +"')", TiposDocumentos.class);

        System.out.println(q.toString());

        TiposDocumentos respuesta = (TiposDocumentos) q.getSingleResult();

        return respuesta;
    }

}
