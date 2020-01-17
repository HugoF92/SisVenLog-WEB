/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.EnvioDto;
import entidad.Envios;
import entidad.EnviosPK;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
 * @author admin
 */
@Stateless
public class EnviosFacade extends AbstractFacade<Envios> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnviosFacade() {
        super(Envios.class);
    }

    public void insertarEnvios(Envios envios) {
        em.persist(envios);
    }

    public BigDecimal getMaxNroEnvio() {
        Query q = getEntityManager().createNativeQuery("select MAX(nro_envio)+1 as maximo\n"
                + "from envios");

        System.out.println(q.toString());

        BigDecimal respuesta = (BigDecimal) q.getSingleResult();

        return respuesta;
    }

    public void insertarEnvios2(Envios envios) {

        StoredProcedureQuery q = getEntityManager().createStoredProcedureQuery("InsertarEnvios");
        q.registerStoredProcedureParameter("cod_entregador", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("nro_envio", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cod_canal", Character.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("depo_origen", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("depo_destino", Short.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("fenvio", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("fanul", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("tot_peso", Double.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("ffactur", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("mestado", Character.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("xobs", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("mtipo", Character.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("falta", Date.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("cusuario", String.class, ParameterMode.IN);

        q.setParameter("cod_entregador", envios.getCodEntregador());
        q.setParameter("nro_envio", envios.getEnviosPK().getNroEnvio());
        q.setParameter("cod_canal", envios.getCodCanal());
        q.setParameter("depo_origen", envios.getDepoOrigen());
        q.setParameter("depo_destino", envios.getDepoDestino());
        q.setParameter("fenvio", envios.getFenvio());
        q.setParameter("fanul", envios.getFanul());
        q.setParameter("tot_peso", envios.getTotPeso());
        q.setParameter("ffactur", envios.getFfactur());
        q.setParameter("mestado", envios.getMestado());
        q.setParameter("xobs", envios.getXobs());
        q.setParameter("mtipo", envios.getMtipo());
        q.setParameter("falta", envios.getFalta());
        q.setParameter("cusuario", envios.getCusuario());

        q.execute();
    }

    public List<EnvioDto> listadoDeEnviosPorNroPedido(long nroPedido) {

        String sql = "SELECT e.nro_envio, e.cod_entregador, e.cod_canal, e.depo_origen, e.depo_destino, e.fenvio, e.fanul, e.ffactur, e.tot_peso, "
                + "e.mtipo, v.xdesc as xcanal, m.xnombre, o.xdesc as xorigen, d.xdesc as xdestino "
                + "FROM pedidos_envios p, envios e, canales_venta v, empleados m, depositos d, depositos o "
                + "WHERE p.cod_empr = 2 "
                + "AND p.cod_empr = e.cod_empr "
                + "AND e.cod_empr = m.cod_empr "
                + "AND p.nro_envio = e.nro_envio "
                + "AND e.cod_entregador = m.cod_empleado "
                + "AND e.depo_origen = o.cod_depo "
                + "AND e.cod_empr = o.cod_empr "
                + "AND e.cod_canal = v.cod_canal "
                + "AND e.cod_empr = d.cod_empr "
                + "AND e.depo_destino = d.cod_depo";

        if (nroPedido != 0) {
            sql += " AND e.nro_pedido = ?";
        }

        Query q = em.createNativeQuery(sql);

        int i = 1;
        if (nroPedido != 0) {
            i++;
            q.setParameter(i, nroPedido);
        }

        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<EnvioDto> listadoEnvios = new ArrayList<EnvioDto>();
        for (Object[] resultado : resultados) {
            //clave primaria de envios
            EnviosPK enviosPk = new EnviosPK();
            enviosPk.setCodEmpr(Short.parseShort("2"));
            enviosPk.setNroEnvio(Long.parseLong(resultado[0].toString()));
            //envios
            Envios e = new Envios();
            e.setEnviosPK(enviosPk);
            e.setCodEntregador(resultado[1] == null ? null : Short.parseShort(resultado[1].toString()));
            e.setCodCanal(resultado[2].toString());
            e.setDepoOrigen(Short.parseShort(resultado[3].toString()));
            e.setDepoDestino(Short.parseShort(resultado[4].toString()));
            if (resultado[5] != null) {
                Timestamp timeStamp_5 = (Timestamp) resultado[5];
                java.util.Date dateResult_5 = new Date(timeStamp_5.getTime());
                e.setFenvio(dateResult_5);
            } else {
                e.setFenvio(null);
            }
            if (resultado[6] != null) {
                Timestamp timeStamp_6 = (Timestamp) resultado[6];
                java.util.Date dateResult_6 = new Date(timeStamp_6.getTime());
                e.setFanul(dateResult_6);
            } else {
                e.setFanul(null);
            }
            if (resultado[7] != null) {
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());
                e.setFfactur(dateResult_7);
            } else {
                e.setFfactur(null);
            }
            e.setTotPeso(resultado[8] == null ? null : BigDecimal.valueOf(Long.parseLong(resultado[8].toString())));
            e.setMtipo((Character) resultado[9]);
            //nombre canal
            String nombreCanal = null;
            if (resultado[10] != null) {
                nombreCanal = resultado[10].toString();
            }
            //nombre empleado
            String nombreEmpleado = null;
            if (resultado[11] != null) {
                nombreEmpleado = resultado[11].toString();
            }
            //deposito origen
            String depositoOrigen = null;
            if (resultado[12] != null) {
                depositoOrigen = resultado[12].toString();
            }
            //deposito destino
            String depositoDestino = null;
            if (resultado[13] != null) {
                depositoDestino = resultado[13].toString();
            }
            EnvioDto edto = new EnvioDto();
            edto.setEnvio(e);
            edto.setDescripcionCanal(nombreCanal);
            edto.setNombreEmpleado(nombreEmpleado);
            edto.setDepositoOrigen(depositoOrigen);
            edto.setDepositoDestino(depositoDestino);
            listadoEnvios.add(edto);
        }

        return listadoEnvios;

    }

    public List<Envios> buscarEnviosPorNroEnvioFecha(String nroEnvio, int[] range) {
        try {
            Query q = getEntityManager().createNativeQuery("select *from envios where nro_envio =  " + nroEnvio + "\n",
                    Envios.class);
            q.setMaxResults(range[1]);
            q.setFirstResult(range[0]);
            System.out.println(q.toString());

            List<Envios> respuesta = q.getResultList();
            return respuesta;

        } catch (Exception e) {
            return null;
        }

    }

    public int CountBuscarEnviosPorNroEnvioFecha(String nroEnvio) {
        try {
            Query q = getEntityManager().createNativeQuery("select *from envios where nro_envio =  " + nroEnvio + "\n",
                    Envios.class);

            System.out.println(q.toString());

            int respuesta = q.getResultList().size();
            return respuesta;

        } catch (Exception e) {
            return 0;
        }

    }

    public List<Envios> findRangeSort(int[] range) {
        Query q = getEntityManager().createNativeQuery("select *from envios order by nro_envio desc ",
                Envios.class);
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);

        return q.getResultList();
    }

    public Integer buscarFacturasActivas(Long nroEnvio) {
        /*Select count(*) as krows "+;
	 				" FROM pedidos_envios p, facturas f "+;
	 				" WHERE p.nro_envio = ?l_nro_envio "+;
	 				" AND p.nro_pedido = f.nro_pedido "+;
	 				" AND f.cod_empr = 2 "+;
	 				" AND p.cod_empr = 2 "+;
	 				" AND f.mestado = 'A' "*/
        try {
            Query q = getEntityManager().createNativeQuery("select count(*) as krows\n"
                    + "FROM pedidos_envios p, facturas f\n"
                    + "WHERE p.nro_pedido = f.nro_pedido\n"
                    + "AND f.cod_empr = 2\n"
                    + "AND p.cod_empr = 2\n"
                    + "AND f.mestado = 'A'\n"
                    + "AND p.nro_envio =  " + nroEnvio + "\n");

            System.out.println(q.toString());

            Integer respuesta = (Integer) q.getSingleResult();

            return respuesta;

        } catch (Exception e) {
            return -1;
        }

    }

    public Integer updatePedidosPorNroEnvio(Long nroEnvio) {
        /*UPDATE pedidos SET mestado = 'N' "+;
									" WHERE cod_empr = 2 "+;
				"   AND nro_pedido IN (SELECT DISTINCT nro_pedido "+;
						"   FROM pedidos_envios "+;
					"   WHERE nro_envio = ?l_nro_envio "+;
					"   AND cod_empr = 2 ) ") */
        try {
            Query q = getEntityManager().createNativeQuery("UPDATE pedidos SET mestado = 'N'\n"
                    + "WHERE cod_empr = 2\n"
                    + "AND nro_pedido IN (SELECT DISTINCT nro_pedido FROM pedidos_envios\n"
                    + "WHERE cod_empr = 2 AND nro_envio =  " + nroEnvio + ")\n");

            System.out.println(q.toString());

            Integer respuesta = q.executeUpdate();

            return respuesta;

        } catch (Exception e) {
            return -1;
        }

    }
}
