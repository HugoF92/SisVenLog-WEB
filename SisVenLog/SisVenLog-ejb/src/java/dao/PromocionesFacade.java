/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.PromocionDto;
import dto.PromocionesDetDto;
import entidad.Promociones;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PromocionesFacade extends AbstractFacade<Promociones> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PromocionesFacade() {
        super(Promociones.class);
    }

    public List<Promociones> listarPromocionesLiFacBonif(String desde, String hasta) {
        Query q = getEntityManager().createNativeQuery("SELECT * \n"
                + "FROM promociones  \n"
                + "WHERE (frige_desde >= '" + desde + "'\n"
                + "	or frige_hasta >= '" + hasta + "'  \n"
                + "	or '" + desde + "' BETWEEN frige_desde AND frige_hasta)", Promociones.class);

        System.out.println(q.toString());

        List<Promociones> respuesta = new ArrayList<Promociones>();

        respuesta = q.getResultList();

        return respuesta;
    }

    public Integer actualizarVigenciaPromocion(String sql) {
        try {
            Query q = getEntityManager().createNativeQuery(sql);

            System.out.println("SQL : " + q.toString());

            Integer respuesta = q.executeUpdate();

            return respuesta;

        } catch (Exception e) {
            System.out.println("MESAJE DE ERROR:");
            System.out.println(e.getLocalizedMessage());
            return -1;
        }
    }

    public List<Promociones> findByNroPromo(String nroPromo) {
        Query q = getEntityManager().createNativeQuery("SELECT * \n"
                + "FROM promociones  \n"
                + "WHERE nro_promo=" + nroPromo, Promociones.class);
        return q.getResultList();

    }
    
    public List<Promociones> findAllOrderXDesc() {
        return getEntityManager().createNativeQuery("SELECT * FROM promociones", Promociones.class).getResultList();
    }
    public List<Promociones> buscarPorFiltro(String filtro) {

        Query q = getEntityManager().createNativeQuery("select *\n"
                + "from promociones\n"
                + "where (xdesc like '%"+filtro+"%'\n"
                + "	   or nro_promo like '%"+filtro+"%' )", Promociones.class);

        return q.getResultList();
    }
    
    public List<PromocionDto> obtenerMercaderiasPromociones(long lNroPromo){
        String sql =    "SELECT cod_merca, my as xdesc " +
                        "FROM promociones_det " +
                        "WHERE nro_promo = "+lNroPromo+" "+
                        "AND cod_empr = 2 AND cod_merca IS NOT NULL";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<PromocionDto> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            PromocionDto p = new PromocionDto();
            p.setCodMerca(resultado[0] == null ? "" : resultado[0].toString());
            p.setCodMerca(resultado[1] == null ? "" : resultado[1].toString());
            listado.add(p);
        }
        return listado;
    }
    
    public List<PromocionDto> obtenerMercaderiasPromocionesPorSublinea(long lNroPromo){
        String sql =        "SELECT m.cod_merca, d.my as xdesc " +
                            "FROM promociones_det D, mercaderias m " +
                            "WHERE nro_promo = "+lNroPromo+" "+
                            "AND d.cod_sublinea = M.cod_sublinea " +
                            "AND m.mestado = 'A' " +
                            "AND m.cod_empr = d.cod_empr " +
                            "AND d.cod_empr = 2 AND d.cod_merca IS NULL";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<PromocionDto> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            PromocionDto p = new PromocionDto();
            p.setCodMerca(resultado[0] == null ? "" : resultado[0].toString());
            p.setCodMerca(resultado[1] == null ? "" : resultado[1].toString());
            listado.add(p);
        }
        return listado;
    }
    
    public List<Promociones> findByNroPromoYFechaFactura(Integer nroPromo, String lFFactura) {
        Query q = getEntityManager().createNativeQuery("SELECT * "
                + "FROM promociones "
                + "WHERE p.frige_desde <= '"+lFFactura+"' "
                + "AND p.frige_hasta >= '"+lFFactura+"' "
                + "AND p.COD_EMPR = 2 "
                + "AND nro_promo = " + nroPromo, Promociones.class);
        return q.getResultList();
    }
    
    public List<PromocionesDetDto> obtenerPromociones( Short lCodSublinea,
                                    String lCodZona,
                                    Integer lNroPromo,
                                    Character lCTipoVenta,
                                    String lFPedido,
                                    String lCodMerca){
        String sql =    "SELECT d.nro_promo, d.cod_merca, d.cod_sublinea, d.kunid_ini, d.kunid_fin, d.kcajas_ini, d.kcajas_fin, d.kkilos_ini, d.kkilos_fin, "+
                        "d.nrelacion, d.por_kunidad, d.por_kcajas, d.pdesc, d.kunid_bonif, d.kmax_unid_bonif, d.mtodos, d.ctipo_vta, d.ctipo_cliente, d.my, "+
                        "d.nrela_pack, d.idesc, p.xdesc "+
                        "FROM promociones p, promociones_det d " +
                        "WHERE p.cod_empr = 2 " +
                        "AND p.nro_promo = d.nro_promo " +
                        "AND p.cod_empr = d.cod_empr " +
                        "AND (d.cod_sublinea = "+lCodSublinea+" OR d.cod_sublinea IS NULL) " +
                        "AND d.mestado = 'A' " +
                        "AND (d.cod_zona = '"+lCodZona+"' OR  d.cod_zona is null ) " +
                        "AND p.nro_promo = "+lNroPromo+" " +
                        "AND d.ctipo_vta = '"+lCTipoVenta+"' " +
                        "AND p.frige_desde <= '"+lFPedido+"' " +
                        "AND p.frige_hasta >= '"+lFPedido+"' " +
                        "AND (d.cod_merca  = '"+lCodMerca.trim()+"' OR d.cod_merca IS NULL)";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<PromocionesDetDto> listado = new ArrayList<>();
        for(Object[] resultado: resultados){
            PromocionesDetDto p = new PromocionesDetDto();
            p.setNroPromo(resultado[0] == null ? 0 : Integer.parseInt(resultado[0].toString()));
            p.setCodMerca(resultado[1] == null ? "" : resultado[1].toString());
            p.setCodSublinea(resultado[2] == null ? 0 : Short.parseShort(resultado[2].toString()));
            p.setkUnidIni(resultado[3] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[3].toString())));
            p.setkUnidFin(resultado[4] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[4].toString())));
            p.setkCajasIni(resultado[5] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[5].toString())));
            p.setkCajasFin(resultado[6] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[6].toString())));
            p.setkKilosIni(resultado[7] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[7].toString())));
            p.setkKilosFin(resultado[8] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[8].toString())));
            p.setnRelacion(resultado[9] == null ? 0 : Short.parseShort(resultado[9].toString()));
            p.setPorKUnidad(resultado[10] == null ? 0 : Short.parseShort(resultado[10].toString()));
            p.setPorKCajas(resultado[11] == null ? 0 : Short.parseShort(resultado[11].toString()));
            p.setpDesc(resultado[12] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[12].toString())));
            p.setkUnidBonif(resultado[13] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Long.parseLong(resultado[13].toString())));
            p.setkMaxUnidBonif(resultado[14] == null ? 0 : Short.parseShort(resultado[14].toString()));
            char cMTodos = resultado[15] == null ? 0 : resultado[15].toString().charAt(0);
            p.setmTodos(cMTodos);
            char cTipoVenta = resultado[16] == null ? 0 : resultado[16].toString().charAt(0);
            p.setcTipoVenta(cTipoVenta);
            char cTipoCliente = resultado[17] == null ? 0 : resultado[17].toString().charAt(0);
            p.setcTipoCliente(cTipoCliente);
            char mY = resultado[18] == null ? 0 : resultado[18].toString().charAt(0);
            p.setmY(mY);
            p.setxDesc(resultado[21] == null ? "" : resultado[21].toString());
            listado.add(p);
        }
        return listado;
    }

}
