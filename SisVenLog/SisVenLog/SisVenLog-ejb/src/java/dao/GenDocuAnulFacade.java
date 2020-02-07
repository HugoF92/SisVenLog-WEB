/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Recibos;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author WORK
 */
@Stateless
public class GenDocuAnulFacade extends AbstractFacade<Recibos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @EJB
    DatosGeneralesFacade datosGeneralesFacade;
    
    @EJB
    PersonalizedFacade personalizedFacade;

    public GenDocuAnulFacade() {
        super(Recibos.class);
    }

    @PreDestroy
    public void destruct() {
        getEntityManager().close();
    }

    /* Funcion testeada */
    public List<Integer> getDocAnulREM(Integer estabInicial, Integer expedInicial, String fechaFactura, Integer secueInicial, Integer secueFinal) throws Exception {
        List<Integer> result = new ArrayList();

        for (int i = secueInicial; i <= secueFinal; i++) {
            String sNrofactura = Integer.toString(estabInicial) + String.format("%02d", expedInicial) + String.format("%07d", i);
            Integer nroRemision = Integer.parseInt(sNrofactura);

            Query q = getEntityManager().createNativeQuery("SELECT count(*) as kfilas "
                    + " FROM remisiones "
                    + " WHERE cod_empr = 2 "
                    + " AND fremision = '" + fechaFactura + "' "
                    + " AND nro_remision = " + nroRemision.toString());

            System.out.println(q.toString());

            Integer kfilas = (Integer)q.getSingleResult();

            if (kfilas > 0) {
                result.add(nroRemision);
            }
        }

        return result;
    }

    /* Funcion testeada */
    public List<Integer> getDocAnulFCO(Integer estabInicial, Integer expedInicial, String fechaFactura, Integer secueInicial, Integer secueFinal) throws Exception {
        List<Integer> result = new ArrayList();

        for (int i = secueInicial; i <= secueFinal; i++) {
            String sNrofactura = Integer.toString(estabInicial) + String.format("%02d", expedInicial) + String.format("%07d", i);
            Integer nroFactura = Integer.parseInt(sNrofactura);

            Integer nroFacturaActual = i;
            Query q = getEntityManager().createNativeQuery(" SELECT count(*) as kfilas "
                    + " FROM facturas "
                    + " WHERE cod_empr = 2 "
                    + " AND ffactur = '" + fechaFactura + "' "
                    + " AND nrofact = " + nroFacturaActual.toString());

            System.out.println(q.toString());

            Integer kfilas = (Integer)q.getSingleResult();

            if (kfilas > 0) {
                result.add(nroFactura);
            }
        }

        return result;
    }

    /* Funcion testeada */
    public List<Integer> getDocAnulEN(Integer secueInicial, Integer secueFinal) throws Exception {
        List<Integer> result = new ArrayList();

        for (int i = secueInicial; i <= secueFinal; i++) {
            Integer nroEnvioActual = i;
            Query q = getEntityManager().createNativeQuery(" SELECT count(*) as kfilas "
                    + " FROM envios "
                    + " WHERE cod_empr = 2 "
                    + " AND nro_envio = " + nroEnvioActual.toString());

            System.out.println(q.toString());

            Integer kfilas = (Integer)q.getSingleResult();

            if (kfilas > 0) {
                result.add(nroEnvioActual);
            }
        }

        return result;
    }

    /* Funcion testeada */
    public List<Integer> getDocAnulNCV(Integer estabInicial, Integer expedInicial, String fechaDoc, Integer secueInicial, Integer secueFinal) throws Exception {
        List<Integer> result = new ArrayList<Integer>();

        for (int i = secueInicial; i <= secueFinal; i++) {
            String sNrofactura = Integer.toString(estabInicial) + String.format("%02d", expedInicial) + String.format("%07d", i);
            Integer nroNota = Integer.parseInt(sNrofactura);

            Integer nroNotaActual = i;
            Query q = getEntityManager().createNativeQuery(" SELECT count(*) as kfilas "
                    + " FROM notas_ventas "
                    + " WHERE cod_empr = 2 "
                    + " AND fdocum = '" + fechaDoc + "' "
                    + " AND nro_nota = " + nroNotaActual.toString());

            System.out.println(q.toString());
            
            Integer kfilas = (Integer)q.getSingleResult();

            if (kfilas > 0) {
                result.add(nroNota);
            }
        }

        return result;
    }

    public List<Integer> inDocAnulREM(Integer estabInicial, Integer expedInicial, String fechaDoc, Integer secueInicial, Integer secueFinal) throws Exception {
        Integer nro_remision = secueInicial;
        Integer cod_entregador = 8;
        Integer cod_conductor = 137;
        Integer cod_transp = 109;
        Integer cod_depo = 1;
        String fremision = fechaDoc;
        Integer npunto_est = estabInicial;
        Integer npunto_exp = expedInicial;

        String cod_merca = "'1101'";
        Integer cant_cajas = 0;
        Integer cant_unid = 0;
        Integer iprecio_caja = 0;
        Integer iprecio_unidad = 0;
        Integer exentas = 0;
        Integer gravadas = 0;
        Integer impuestos = 0;
        Integer itotal = 0;
        Integer idescuentos = 0;
        Integer pdesc = 0;
        Integer pimpues = 10;
        Integer nro_promo = null;
        String xdescAs = "HAV.METALIC.PLA.CLARA 37/38";
        String xdesc = "'Caña Fortín 12/930'";

        List<Integer> result = new ArrayList();

        for (int i = secueInicial; i <= secueFinal; i++) {
            String xnro_remision = String.format("%03d", estabInicial) + "-" + String.format("%03d", expedInicial) + "-" + String.format("%07d", i);

            String sNrofactura = Integer.toString(estabInicial) + String.format("%02d", expedInicial) + String.format("%07d", i);
            Integer new_remision = Integer.parseInt(sNrofactura);

            try {
                String q1 = " INSERT INTO remisiones "
                        + "(cod_empr,nro_remision,fremision,cod_conductor, "
                        + "cod_transp, cod_entregador, cod_depo, mtipo, mestado, "
                        + "fanul, cmotivo, xnro_remision) "
                        + "values(2, " + new_remision + ", '" + fremision + "', " + cod_conductor + ", " + cod_transp + ", "
                        + cod_entregador + ", " + cod_depo + ", "
                        + "'O', 'X', '" + fremision + "', null, " + xnro_remision + ")";

                personalizedFacade.ejecutarSentenciaSQL(q1);

                String q2 = "INSERT INTO remisiones_det (cod_empr, nro_remision, "
                        + "cod_merca, xdesc, cant_cajas, cant_unid) values ("
                        + "2, " + new_remision + ", " + cod_merca + ", " + xdesc + ", "
                        + cant_cajas + ", " + cant_unid + ")";

                personalizedFacade.ejecutarSentenciaSQL(q2);

            } catch (Exception e) {
                result.add(new_remision);
            }
        }

        return result;
    }

    public List<Integer> inDocAnulFCO(Integer estabInicial, Integer expedInicial, String fechaDoc, Integer secueInicial, Integer secueFinal) throws Exception {
        Integer nrofact = secueInicial;
        Integer cliente = 496;
        String cod_canal = "'T'";
        Integer cod_depo = 1;
        String cod_zona = "'B'";
        Integer cod_ruta = 25;
        String ffactur = fechaDoc;
        String ctipo_vta = "'D'";
        String xobs = null;
        Integer vendedor = 52;
        Integer texentas = 0;
        Integer tgravadas = 0;
        Integer timpuestos = 0;
        Integer ttotal = 0;
        Integer cod_entregador = 8;
        Integer isaldo = 0;
        Integer tdescuentos = 0;
        String xdirec = null;
        String xrazon_social = null;
        String xruc = null;
        String xtelef = null;
        String xciudad = null;
        String fvenc = null;
        Integer tgravadas_10 = 0;
        Integer tgravadas_5 = 0;
        Integer timpuestos_10 = 0;
        Integer timpuestos_5 = 0;

        Integer npunto_est = estabInicial;

        Integer npunto_exp = expedInicial;

        String cod_mercaAS = "'H04360'";
        String cod_merca = "'1101'";
        Integer cant_cajas = 0;
        Integer cant_unid = 0;
        Integer iprecio_caja = 0;
        Integer iprecio_unidad = 0;
        Integer exentas = 0;
        Integer gravadas = 0;
        Integer impuestos = 0;
        Integer itotal = 0;
        Integer idescuentos = 0;
        Integer pdesc = 0;
        Integer pimpues = 10;
        Integer nro_promo = null;
        String xdescAs = "'HAV.METALIC.PLA.CLARA 37/38'";
        String xdesc = "'Caña Fortín 12/930'";

        List<Integer> result = new ArrayList(); //error

        for (int i = secueInicial; i <= secueFinal; i++) {
            String xfactura = String.format("%03d", estabInicial) + "-" + String.format("%03d", expedInicial) + "-" + String.format("%07d", i);

            String sNrofactura = Integer.toString(estabInicial) + String.format("%02d", expedInicial) + String.format("%07d", i);
            Integer new_nrofact = Integer.parseInt(sNrofactura);

            try {
                String q1 = "INSERT INTO facturas (COD_EMPR, ctipo_docum, nrofact, "
                        + "cod_cliente, cod_canal, cod_depo, cod_zona, cod_ruta, ffactur, ctipo_vta, "
                        + "xobs, cod_vendedor, mestado, texentas, tgravadas, timpuestos, ttotal, cod_entregador, "
                        + "isaldo, tdescuentos, xdirec, xrazon_social, xruc, xtelef, xciudad, fvenc, tgravadas_10, tgravadas_5, "
                        + "timpuestos_10, timpuestos_5, xfactura, fanul, cusuario_anul) values ("
                        + "2, 'FCO', " + new_nrofact + ", " + cliente + ", "
                        + cod_canal + ", " + cod_depo + ", " + cod_zona + ", " + cod_ruta + ", '" + ffactur + "', "
                        + ctipo_vta + ", " + xobs + ", " + vendedor + ", 'X', " + texentas + ", "
                        + tgravadas + ", " + timpuestos + ", " + ttotal + ", " + cod_entregador + ", " + isaldo + ", " + tdescuentos + ", " + xdirec + ", "
                        + xrazon_social + ", " + xruc + ", " + xtelef + ", " + xciudad + ", " + fvenc + ", " + tgravadas_10 + ", " + tgravadas_5 + ", "
                        + timpuestos_10 + ", " + timpuestos_5 + ", " + xfactura + ", '" + ffactur + "', USER_name())";

                personalizedFacade.ejecutarSentenciaSQL(q1);

                String q2 = "INSERT INTO facturas_det (cod_empr, ctipo_docum, nrofact, "
                        + "cod_merca, xdesc, cant_cajas, cant_unid, iprecio_caja, iprecio_unidad, iexentas, igravadas , "
                        + "impuestos, idescuentos, itotal, pdesc, pimpues, NRO_PROMO , ffactur) values ("
                        + "2, 'FCO'," + new_nrofact + ", " + cod_merca + ", "
                        + xdesc + ", " + cant_cajas + ", " + cant_unid + ", "
                        + iprecio_caja + ", " + iprecio_unidad + ", " + exentas + ", " + gravadas + ", " + impuestos + ", " + idescuentos + ", " + itotal + ", "
                        + pdesc + ", " + pimpues + ", " + nro_promo + ", '" + ffactur + "')";

                personalizedFacade.ejecutarSentenciaSQL(q2);

            } catch (Exception e) {
                result.add(new_nrofact); //error
            }
        }

        return result;
    }

    public List<Integer> inDocAnulEN(String fechaDoc, Integer secueInicial, Integer secueFinal) throws Exception {

        String cod_canal = "'T'";
        Integer depo_origen = 1;
        Integer depo_destino = 1;
        String fenvio = fechaDoc;
        String xobs = null;
        Integer tot_peso = 0;
        Integer cod_entregador = 8;

        String cod_merca = "'1101'";
        Integer cant_cajas = 0;
        Integer cant_unid = 0;
        Integer iprecio_caja = 0;
        Integer iprecio_unidad = 0;
        Integer exentas = 0;
        Integer gravadas = 0;
        Integer impuestos = 0;
        Integer itotal = 0;
        Integer idescuentos = 0;
        Integer pdesc = 0;
        Integer pimpues = 10;
        Integer nro_promo = null;
        String xdescAs = "'HAV.METALIC.PLA.CLARA 37/38'";
        String xdesc = "'Caña Fortín 12/930'";

        List<Integer> result = new ArrayList(); //error

        for (int i = secueInicial; i <= secueFinal; i++) {
            Integer nro_envio = i;

            try {
                
                String q1 = "INSERT INTO envios (cod_empr, nro_envio, "
                        + "cod_entregador, cod_canal, depo_origen, depo_destino, "
                        + "fenvio, xobs, mestado, tot_peso, FANUL) values ("
                        + " 2, " + nro_envio + ", " + cod_entregador + ", " + cod_canal + ", " + depo_origen + ", " + depo_destino + ", "
                        + "'" + fenvio + "', " + xobs + ", 'X', " + tot_peso + ", '" + fenvio + "')";
                
                personalizedFacade.ejecutarSentenciaSQL(q1);

                String q2 = "INSERT INTO envios_det (cod_empr, nro_envio, "
                        + "cod_merca, xdesc, cant_cajas, cant_unid) values ("
                        + "2, " + nro_envio + ", " + cod_merca + ", " + xdesc + ", "
                        + cant_cajas + ", " + cant_unid + ")";

                personalizedFacade.ejecutarSentenciaSQL(q2);

            } catch (Exception e) {
                result.add(nro_envio); //error
            }
        }

        return result;
    }

    public List<Integer> inDocAnulNCV(Integer estabInicial, Integer expedInicial, Integer estabFactInicial, Integer expedFactInicial, Integer secueFactFinal,
            String fechaDoc, String fechaFactura, Integer secueInicial, Integer secueFinal) throws Exception {
        String ctipo_docum = "'NCV'";
        String fdocum = fechaDoc;
        String fenvio = fechaDoc;
        String xobs = null;
        Integer cod_cliente = 496;
        String cconc = "'DES'";
        Integer new_nrofact = null;
        String fac_ctipo_docum = "'FCO'";
        Integer texentas = 0;
        Integer tgravadas = 0;
        Integer timpuestos = 0;
        Integer ttotal = 0;
        Integer cod_depo = 1;
        Integer cod_entregador = 8;
        Integer nro_promo = null;

        Integer npunto_est = estabInicial;
        Integer npunto_exp = expedInicial;
        Integer npunto_estf = estabFactInicial;
        Integer npunto_expf = estabFactInicial;

        Integer relnro = secueFactFinal;

        String ffactur = fechaFactura;

        String cod_merca = "'1101'";
        Integer cant_cajas = 0;
        Integer cant_unid = 0;
        Integer iprecio_caja = 0;
        Integer iprecio_unid = 0;
        Integer exentas = 0;
        Integer gravadas = 0;
        Integer impuestos = 0;
        Integer itotal = 0;
        Integer idescuentos = 0;
        Integer pdesc = 0;
        Integer pimpues = 10;
        String xdescAs = "'HAV.METALIC.PLA.CLARA 37/38'";
        String xdesc = "'Caña Fortín 12/930'";

        String sNrofactura = Integer.toString(estabFactInicial) + String.format("%02d", expedFactInicial) + String.format("%07d", secueFactFinal);
        Integer nrofact = Integer.parseInt(sNrofactura);

        List<Integer> result = new ArrayList(); //error

        for (int i = secueInicial; i <= secueFinal; i++) {
            String xnro_nota = String.format("%03d", estabInicial) + "-" + String.format("%03d", expedInicial) + "-" + String.format("%07d", i);

            String sNroNota = Integer.toString(estabInicial) + String.format("%02d", expedInicial) + String.format("%07d", i);
            Integer nro_nota = Integer.parseInt(sNroNota);

            try {
                Integer new_nota = i;
                String q1 = "INSERT INTO notas_ventas (cod_empr, ctipo_docum, nro_nota, "
                        + "fdocum, xobs, mestado, cod_cliente, cconc, nrofact, fac_ctipo_docum, "
                        + "texentas, tgravadas, timpuestos, ttotal, isaldo, cod_depo, cod_entregador, nro_promo, xnro_nota, fanul, cusuario_anul, ffactur ) values ("
                        + "2, " + ctipo_docum + ", " + new_nota + ", '" + fdocum + "', " + xobs + ", 'X', "
                        + cod_cliente + ", " + cconc + ", " + nrofact + ", " + fac_ctipo_docum + ", "
                        + texentas + ", " + tgravadas + ", " + timpuestos + ", " + ttotal + ", " + 0 + ", " + cod_depo + ", " + cod_entregador
                        + ", " + nro_promo + ", '" + xnro_nota + "', '" + fdocum + "', USER_NAME(), '" + ffactur + "')";

                personalizedFacade.ejecutarSentenciaSQL(q1);

                String q2 = "INSERT INTO notas_ventas_det (cod_empr, ctipo_docum, nro_nota, "
                        + "cod_merca, pdesc, xdesc, cant_cajas, cant_unid, iexentas, igravadas, "
                        + "impuestos, iprecio_caja, iprecio_unid, pimpues, fdocum) values ("
                        + " 2, " + ctipo_docum + ", " + nro_nota + ", " + cod_merca + ", " + pdesc + ", " + xdesc + ", "
                        + cant_cajas + ", " + cant_unid + ", " + exentas + ", " + gravadas + ", " + impuestos + ", "
                        + iprecio_caja + ", " + iprecio_unid + ", " + pimpues + ", '" + fdocum + "')";

                personalizedFacade.ejecutarSentenciaSQL(q2);

            } catch (Exception e) {

                result.add(nro_nota); //error
            }
        }

        return result;
    }

    public String obtenerPath() {
        return datosGeneralesFacade.findAll().get(0).getTempPath();
    }
}
