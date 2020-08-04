/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.TiposDocumentos;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public List<TiposDocumentos> listarTipoDocumentoParaConsultaDeCompras() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCC', 'COC', 'CVC', 'NCC', 'NDC', 'NDP')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public List<TiposDocumentos> listarTipoDocumentoCambiarEntregador() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCO','CPV','FCR', 'NCV', 'EN')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public List<TiposDocumentos> listarTipoDocumentoReciboProveedor() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('REP')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public List<TiposDocumentos> listarTipoDocumentoParaReciboCompraProveedor() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCC', 'NCC', 'NDC')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
     public List<TiposDocumentos> listarTipoDocumentoParaReciboVentaCliente() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCR','NCV','FCP')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public List<TiposDocumentos> listarTipoDocumentoReciboCliente() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('REC')", TiposDocumentos.class);

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
     
     public List<TiposDocumentos> listarTipoDocumentoFactura() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCO','FCR')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }
     
      public List<TiposDocumentos> listarTipoDocumentoFacturaServicio() {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from tipos_documentos\n"
                + "where ctipo_docum in ('FCP','FCS')", TiposDocumentos.class);

        System.out.println(q.toString());

        List<TiposDocumentos> respuesta = new ArrayList<TiposDocumentos>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public List<TiposDocumentos> listarTiposDocumentosLiDocumentosFaltantes() {
        Query q = getEntityManager().createNativeQuery("select * "
                + "from tipos_documentos "
                + "where ctipo_docum in ('FCR','FCO','CPV','NCV','NDV','EN','REC')", TiposDocumentos.class);
        return q.getResultList();
    }


    public List<TiposDocumentos> listarTiposDocumentosLiFacPromo() {
        Query q = getEntityManager().createNativeQuery("select * "
                + "from tipos_documentos "
                + "where ctipo_docum in ('FCR','FCO','CPV','NCV')", TiposDocumentos.class);

        return q.getResultList();
    }
      
    public List<TiposDocumentos> getTipoDocumentosByCtipo(List<String> tiposDocumentos) {
        String tipos = tiposDocumentos.stream().map(tipo -> "'" +  tipo + "'").collect(Collectors.joining(", "));
        Query q = getEntityManager().createNativeQuery("select * "
                + "from tipos_documentos "
                + "where ctipo_docum in ("+ tipos +") order by xdesc", TiposDocumentos.class);
        return q.getResultList();
    }
      
    public TiposDocumentos getTipoDocumentosByCtipo(String ctipoDocum){
        try{
            Query q = getEntityManager().createNativeQuery("select * from tipos_documentos where ctipo_docum = "+ctipoDocum);
            System.out.println(q.toString());
            return (TiposDocumentos) q.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
      
}
