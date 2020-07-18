/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Depositos;
import entidad.DepositosPK;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author Hugo
 */
@Stateless
public class DepositosFacade extends AbstractFacade<Depositos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepositosFacade() {
        super(Depositos.class);
    }
    
    public List<Depositos> buscarPorDescripcion(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from dbo.depositos\n"
                + "where xdesc =  '" + filtro + "' ", Depositos.class);

        //System.out.println(q.toString());

        List<Depositos> respuesta = new ArrayList<Depositos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public void insertarDeposito(Depositos depositos) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarDeposito");
        q.registerStoredProcedureParameter("xdesc", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_zona", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("mtipo", Character.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("npeso_max", Double.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("fultim_inven", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("fultim_modif", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario_modif", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("npunto_est", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("npunto_exp", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_conductor", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_transp", Integer.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xchapa", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xmarca", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xobs", String.class, ParameterMode.IN);
        
        q.setParameter("xdesc", depositos.getXdesc());
        q.setParameter("cod_zona", depositos.getZonas().getZonasPK().getCodZona());
        q.setParameter("mtipo", depositos.getMtipo());
        q.setParameter("npeso_max", depositos.getNpesoMax());
        q.setParameter("fultim_inven", depositos.getFultimInven());
        q.setParameter("falta", depositos.getFalta());
        q.setParameter("cusuario", depositos.getCusuario());
        q.setParameter("fultim_modif", depositos.getFultimModif());
        q.setParameter("cusuario_modif", depositos.getCusuarioModif());
        q.setParameter("npunto_est", depositos.getNpuntoEst());
        q.setParameter("npunto_exp", depositos.getNpuntoExp());
        q.setParameter("cod_conductor", depositos.getCodConductor().getCodConductor());
        q.setParameter("cod_transp", depositos.getCodTransp().getCodTransp());
        q.setParameter("xchapa", depositos.getXchapa());
        q.setParameter("xmarca", depositos.getXmarca());
        q.setParameter("xobs", depositos.getXobs());
        
        q.execute();

        
    }
    
    
    public List<Depositos> listarDepositosActivos() {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM depositos  ", Depositos.class);

        System.out.println(q.toString());

        List<Depositos> respuesta = new ArrayList<Depositos>();

        respuesta = q.getResultList();

        return respuesta;
    }
    
    public List<Depositos> listarDepositosMercaSin() {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM depositos where mtipo = 'M' ", Depositos.class);

        System.out.println(q.toString());

        List<Depositos> respuesta = new ArrayList<Depositos>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public Depositos getNombreDeposito(Integer codigo) {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from depositos\n"
                + "where cod_depo = "+ codigo , Depositos.class);

        System.out.println(q.toString());

        Depositos respuesta = new Depositos();

        respuesta = (Depositos)q.getSingleResult();

        return respuesta;
    }
    
    public Depositos getDepositoPorCodigo(short codigo) {
        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from depositos\n"
                + "where cod_depo = "+ codigo , Depositos.class);

        System.out.println(q.toString());

        Depositos respuesta = new Depositos();

        respuesta = (Depositos)q.getSingleResult();

        return respuesta;
    }
    
    public List<Depositos> obtenerDepositosPorTipoCliente(Character lTipoCliente){
        String sql =    "SELECT c.cod_depo, d.xdesc FROM tipocli_depositos as c, depositos as d WHERE d.cod_depo = c.cod_depo " +
                        "AND c.ctipo_cliente = '"+lTipoCliente+"' "+
                        "AND c.cod_empr = 2";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<Depositos> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            DepositosPK depositoPK = new DepositosPK();
            depositoPK.setCodEmpr(Short.parseShort("2"));
            depositoPK.setCodDepo(resultado[0] == null ? 0 : Short.parseShort(resultado[0].toString()));
            Depositos d = new Depositos();
            d.setDepositosPK(depositoPK);
            d.setXdesc(resultado[1] == null ? "" : resultado[1].toString());
            listado.add(d);
        }
        return listado;
    }
    
    public Depositos getDepositoFromList(Depositos pk, List<Depositos> lista){
        return lista.stream()
                .filter(obj -> obj.getDepositosPK().getCodDepo()==pk.getDepositosPK().getCodDepo() 
                        //&&  pk.getDepositosPK().getCodEmpr()==pk.getDepositosPK().getCodDepo()
                    )
                .findAny().orElse(null);
    }
    
}
