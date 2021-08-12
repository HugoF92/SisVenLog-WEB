/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ChequeDetalleDto;
import dto.ChequeNoCobrado;
import entidad.Bancos;
import entidad.Cheques;
import entidad.ChequesPK;
import entidad.Clientes;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Edu
 */
@Stateless
public class ChequesFacade extends AbstractFacade<Cheques> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @EJB
    private ClientesFacade clientesFacade;

    @EJB
    private BancosFacade bancosFacade;

    public ChequesFacade() {
        super(Cheques.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<ChequeNoCobrado> listadoChequesNoCobrados(short codigoEmpresa, short codigoBanco, String numeroCheque, Long montoCheque, Date fechaInicio, Date fechaFin, Integer codigoCliente, Date fechaCobro) throws ParseException {
        String sql = " SELECT h.cod_empr, h.cod_banco, h.nro_cheque, h.xcuenta_bco, h.fcheque, h.icheque, h.cod_cliente, h.frechazo, h.fcobro, h.femision, h.xtitular, h.mtipo, h.cod_entregador, h.cod_zona, b.xdesc, c.xnombre"
                + " FROM cheques h, bancos b, clientes c"
                + " WHERE h.cod_empr = ?"
                + " AND h.cod_empr = c.cod_empr"
                + " AND h.cod_cliente = c.cod_cliente"
                + " AND h.fcobro is null"
                + " AND h.cod_banco = b.cod_banco";

        if (codigoBanco != 0) {
            sql += " AND h.cod_banco = ?";
        }

        if (!numeroCheque.equals("") || !numeroCheque.isEmpty()) {
            sql += " AND h.nro_cheque = ?";
        }

        if (montoCheque != 0) {
            sql += " AND h.icheque = ?";
        }

        if (fechaInicio != null) {
            sql += " AND h.fcheque >= ?";
        }

        if (fechaFin != null) {
            sql += " AND h.fcheque <= ?";
        }

        if (codigoCliente != null) {
            if (codigoCliente > 0) {
                sql += " AND h.cod_cliente = ?";
            }
        }

        sql += " ORDER BY h.fcheque, h.cod_banco";

        Query q = em.createNativeQuery(sql);

        int i = 1;
        q.setParameter(i, codigoEmpresa);

        if (codigoBanco != 0) {
            i++;
            q.setParameter(i, codigoBanco);
        }

        if (!numeroCheque.equals("") || !numeroCheque.isEmpty()) {
            i++;
            q.setParameter(i, numeroCheque);
        }

        if (montoCheque != 0) {
            i++;
            q.setParameter(i, montoCheque);
        }

        if (fechaInicio != null) {
            i++;
            q.setParameter(i, fechaInicio);
        }

        if (fechaFin != null) {
            i++;
            q.setParameter(i, fechaFin);
        }

        if (codigoCliente != null) {
            if (codigoCliente > 0) {
                i++;
                q.setParameter(i, codigoCliente);
            }
        }

        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<ChequeNoCobrado> listadoCheques = new ArrayList<ChequeNoCobrado>();
        for (Object[] resultado : resultados) {
            //clave primaria de cheques
            ChequesPK chequePk = new ChequesPK();
            chequePk.setCodEmpr(Short.parseShort(resultado[0].toString()));
            chequePk.setCodBanco(Short.parseShort(resultado[1].toString()));
            chequePk.setNroCheque(resultado[2].toString());
            chequePk.setXcuentaBco(resultado[3].toString());
            //cheques
            Cheques cheque = new Cheques();
            cheque.setChequesPK(chequePk);
            if (resultado[4] != null) {
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());
                cheque.setFcheque(dateResult_4);
            } else {
                cheque.setFcheque(null);
            }
            cheque.setIcheque(resultado[5] == null ? null : Long.parseLong(resultado[5].toString()));
            cheque.setCodCliente(resultado[6] == null ? null : clientesFacade.find(Integer.parseInt(resultado[6].toString())));
            cheque.setBancos(resultado[1] == null ? null : bancosFacade.find(Short.parseShort(resultado[1].toString())));

            if (resultado[7] != null) {
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());
                cheque.setFrechazo(dateResult_7);
            } else {
                cheque.setFrechazo(null);
            }

            if (resultado[9] != null) {
                Timestamp timeStamp_9 = (Timestamp) resultado[9];
                java.util.Date dateResult_9 = new Date(timeStamp_9.getTime());
                cheque.setFemision(dateResult_9);
            } else {
                cheque.setFemision(null);
            }

            cheque.setXtitular(resultado[10] == null ? null : resultado[10].toString());
            cheque.setCodEntregador(resultado[12] == null ? null : Short.parseShort(resultado[12].toString()));
            cheque.setCodZona(resultado[13] == null ? null : resultado[13].toString());
            cheque.setFcobro(fechaCobro);

            //nombre banco
            String nombreBanco = null;
            if (resultado[14] != null) {
                nombreBanco = resultado[14].toString();
            }

            //nombre cliente
            String nombreCliente = null;
            if (resultado[15] != null) {
                nombreCliente = resultado[15].toString();
            }

            ChequeNoCobrado chequeNoCobrado = new ChequeNoCobrado();
            chequeNoCobrado.setCheque(cheque);
            chequeNoCobrado.setNombreBanco(nombreBanco);
            chequeNoCobrado.setNombreCliente(nombreCliente);
            listadoCheques.add(chequeNoCobrado);
        }

        return listadoCheques;
    }

    public int actualizarChequesNoCobrados(Cheques cheque) {
        String sql = "UPDATE dbo.cheques SET fcobro = ? WHERE cod_empr = 2 AND nro_cheque = ? AND cod_banco = ?";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, cheque.getFcobro());
        q.setParameter(2, cheque.getChequesPK().getNroCheque());
        q.setParameter(3, cheque.getChequesPK().getCodBanco());
        return q.executeUpdate();
    }

    public List<ChequeNoCobrado> listadoCheques(short codigoEmpresa, short codigoBanco, String numeroCheque, Long montoCheque, Date fechaInicio, Date fechaFin, Integer codigoCliente, Date fechaCobro) throws ParseException {
        String sql = "SELECT h.cod_empr, h.cod_banco, h.nro_cheque, h.xcuenta_bco, h.fcheque, h.icheque, h.cod_cliente, h.frechazo, h.fcobro, h.femision, h.xtitular, h.mtipo, h.cod_entregador, h.cod_zona, b.xdesc, c.xnombre, h.falta, h.cusuario"
                + " FROM cheques h, bancos b, clientes c"
                + " WHERE h.cod_empr = ?"
                + " AND h.cod_empr = c.cod_empr"
                + " AND h.cod_cliente = c.cod_cliente"
                + " AND h.cod_banco = b.cod_banco";

        if (codigoBanco != 0) {
            sql += " AND h.cod_banco = ?";
        }

        if (!numeroCheque.equals("") || !numeroCheque.isEmpty()) {
            sql += " AND h.nro_cheque = ?";
        }

        if (montoCheque != 0) {
            sql += " AND h.icheque = ?";
        }

        if (fechaInicio != null) {
            sql += " AND h.fcheque >= ?";
        }

        if (fechaFin != null) {
            sql += " AND h.fcheque <= ?";
        }

        if (codigoCliente != null) {
            if (codigoCliente > 0) {
                sql += " AND h.cod_cliente = ?";
            }
        }

        if (fechaCobro != null) {
            sql += " AND h.fcobro = ?";
        }

        sql += " ORDER BY h.fcheque";

        Query q = em.createNativeQuery(sql);

        int i = 1;
        q.setParameter(i, codigoEmpresa);

        if (codigoBanco != 0) {
            i++;
            q.setParameter(i, codigoBanco);
        }

        if (!numeroCheque.equals("") || !numeroCheque.isEmpty()) {
            i++;
            q.setParameter(i, numeroCheque);
        }

        if (montoCheque != 0) {
            i++;
            q.setParameter(i, montoCheque);
        }

        if (fechaInicio != null) {
            i++;
            q.setParameter(i, fechaInicio);
        }

        if (fechaFin != null) {
            i++;
            q.setParameter(i, fechaFin);
        }

        if (codigoCliente != null) {
            if (codigoCliente > 0) {
                i++;
                q.setParameter(i, codigoCliente);
            }
        }

        if (fechaCobro != null) {
            i++;
            q.setParameter(i, fechaCobro);
        }

        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<ChequeNoCobrado> listadoCheques = new ArrayList<ChequeNoCobrado>();
        for (Object[] resultado : resultados) {
            //clave primaria de cheques
            ChequesPK chequePk = new ChequesPK();
            chequePk.setCodEmpr(Short.parseShort(resultado[0].toString()));
            chequePk.setCodBanco(Short.parseShort(resultado[1].toString()));
            chequePk.setNroCheque(resultado[2].toString());
            chequePk.setXcuentaBco(resultado[3].toString());
            //cheques
            Cheques cheque = new Cheques();
            cheque.setChequesPK(chequePk);
            if (resultado[4] != null) {
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());
                cheque.setFcheque(dateResult_4);
            } else {
                cheque.setFcheque(null);
            }
            cheque.setIcheque(resultado[5] == null ? null : Long.parseLong(resultado[5].toString()));
            cheque.setCodCliente(resultado[6] == null ? null : clientesFacade.find(Integer.parseInt(resultado[6].toString())));
            cheque.setBancos(resultado[1] == null ? null : bancosFacade.find(Short.parseShort(resultado[1].toString())));

            if (resultado[7] != null) {
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());
                cheque.setFrechazo(dateResult_7);
            } else {
                cheque.setFrechazo(null);
            }

            if (resultado[9] != null) {
                Timestamp timeStamp_9 = (Timestamp) resultado[9];
                java.util.Date dateResult_9 = new Date(timeStamp_9.getTime());
                cheque.setFemision(dateResult_9);
            } else {
                cheque.setFemision(null);
            }

            cheque.setXtitular(resultado[10] == null ? null : resultado[10].toString());
            cheque.setCodEntregador(resultado[12] == null ? null : Short.parseShort(resultado[12].toString()));
            cheque.setCodZona(resultado[13] == null ? null : resultado[13].toString());
            cheque.setFcobro(fechaCobro);

            //nombre banco
            String nombreBanco = null;
            if (resultado[14] != null) {
                nombreBanco = resultado[14].toString();
            }

            //nombre cliente
            String nombreCliente = null;
            if (resultado[15] != null) {
                nombreCliente = resultado[15].toString();
            }

            //fecha-hora alta
            if (resultado[16] != null) {
                Timestamp timeStamp_16 = (Timestamp) resultado[16];
                java.util.Date dateResult_16 = new Date(timeStamp_16.getTime());
                cheque.setFalta(dateResult_16);
            } else {
                cheque.setFalta(null);
            }

            //usuario grabacion
            String usuarioGrabacion = null;
            if (resultado[17] != null) {
                usuarioGrabacion = resultado[17].toString();
            }
            cheque.setCusuario(usuarioGrabacion);

            ChequeNoCobrado chequeNoCobrado = new ChequeNoCobrado();
            chequeNoCobrado.setCheque(cheque);
            chequeNoCobrado.setNombreBanco(nombreBanco);
            chequeNoCobrado.setNombreCliente(nombreCliente);
            listadoCheques.add(chequeNoCobrado);
        }

        return listadoCheques;
    }

    public List<ChequeDetalleDto> listadoDeChequesPorNroFacturaYFecha(Long numeroFactura, Date fechaFactura) {

        String sql = "SELECT 'CHQ' as ctipo_docum, c.nro_cheque as ndocum_cheq, h.fcheque as fvenc, c.ipagado, h.femision as fmovim "
                + "FROM cheques_det c, cheques h "
                + "WHERE c.nro_cheque = h.nro_cheque "
                + "AND c.cod_banco = h.cod_banco "
                + "AND h.cod_empr = 2 "
                + "AND c.cod_empr = 2";

        if (numeroFactura != null) {
            if (numeroFactura > 0) {
                sql += " AND c.nrofact = ?";
            }
        }

        if (fechaFactura != null) {
            sql += " AND c.ffactur = ?";
        }

        Query q = em.createNativeQuery(sql);

        int i = 1;
        if (numeroFactura != null) {
            if (numeroFactura > 0) {
                i++;
                q.setParameter(i, numeroFactura);
            }
        }

        if (fechaFactura != null) {
            i++;
            q.setParameter(i, fechaFactura);
        }

        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<ChequeDetalleDto> listadoCheques = new ArrayList<ChequeDetalleDto>();
        for (Object[] resultado : resultados) {
            ChequeDetalleDto cddto = new ChequeDetalleDto();
            cddto.setTipoDocumento(resultado[0].toString());
            cddto.setNroCheque(resultado[1].toString());
            if (resultado[2] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                cddto.setFechaVencimiento(dateResult_2);
            } else {
                cddto.setFechaVencimiento(null);
            }
            cddto.setImportePagado(Long.parseLong(resultado[3].toString()));
            if (resultado[4] != null) {
                Timestamp timeStamp_4 = (Timestamp) resultado[4];
                java.util.Date dateResult_4 = new Date(timeStamp_4.getTime());
                cddto.setFechaEmision(dateResult_4);
            } else {
                cddto.setFechaEmision(null);
            }
            listadoCheques.add(cddto);
        }

        return listadoCheques;
    }

    public List<Cheques> listadoChequesNoRechazadosPorNroBancoCheque(String lNroCheque, short lCodBanco) {
        String sql = "SELECT * FROM cheques "
                + "WHERE nro_cheque = '" + lNroCheque + "' "
                + "AND cod_banco = " + lCodBanco + " "
                + "AND cod_empr = 2 "
                + "AND frechazo IS NULL";
        Query q = em.createNativeQuery(sql, Cheques.class);
        System.out.println(q.toString());
        List<Cheques> resultados = q.getResultList();
        return resultados;
    }

    public void insertarCheque(Short lCodBanco,
            String lNroCheque,
            String lCtaBancaria,
            String lFCheque,
            String lFEmision,
            long lICheque,
            Integer lCodCliente,
            String lMTitular,
            Character lMTipo) {
        String sql = "INSERT INTO cheques (cod_empr, cod_banco, "
                + "nro_cheque, xcuenta_bco, fcheque, femision, icheque, cod_cliente, "
                + "xtitular, mtipo) VALUES (2, " + lCodBanco + ", '" + lNroCheque + "', "
                + "'" + lCtaBancaria + "', '" + lFCheque + "', '" + lFEmision + "', " + lICheque + ", " + lCodCliente + ", "
                + "'" + lMTitular + "', '" + lMTipo + "')";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }

    public List<Cheques> getList(
            String nroCheque,
            String xdesc,
            Date fcheque,
            Long icheque,
            Integer codCliente,
            String xnombre,
            String sortField,
            String sortOrder,
            int[] range
    ) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Cheques> cq = builder.createQuery(Cheques.class);
        Root<Cheques> r = cq.from(Cheques.class);
        Join<Cheques, Bancos> j1 = r.join("bancos");
        Join<Cheques, Clientes> j2 = r.join("codCliente");
        List<Predicate> predicates = new ArrayList<>();
        if (nroCheque != null) {
            predicates.add(builder.like(r.get("chequesPK").get("nroCheque"), "%" + nroCheque + "%"));
        }
        if (xdesc != null) {
            predicates.add(builder.like(j1.get("xdesc"), "%" + xdesc + "%"));
        }
        if (xnombre != null) {
            predicates.add(builder.like(j2.get("xnombre"), "%" + xnombre + "%"));
        }
        if (fcheque != null) {
            predicates.add(builder.equal(r.get("fcheque"), fcheque));
        }
        if (icheque != null) {
            predicates.add(builder.like(r.get("icheque").as(String.class), "%" + icheque + "%"));
        }

        if (!predicates.isEmpty()) {
            Predicate finalPredicate = builder.and(predicates.toArray(new Predicate[predicates.size()]));
            cq.where(finalPredicate);
        }
        if (sortOrder.equalsIgnoreCase("descending")) {
            if (sortField.contains(".")) {
                String field = sortField.split("\\.")[1];
                if (field.equalsIgnoreCase("xdesc")) {
                    cq.orderBy(builder.desc(j1.get(field)));
                } else if (field.equalsIgnoreCase("xnombre")) {
                    cq.orderBy(builder.desc(j2.get(field)));
                } else if (field.equalsIgnoreCase("nroCheque")) {
                    cq.orderBy(builder.desc(r.get("chequesPK").get(field)));
                }
            } else {
                cq.orderBy(builder.desc(r.get(sortField)));
            }
        } else {
            if (sortField.contains(".")) {
                String field = sortField.split("\\.")[1];
                if (field.equalsIgnoreCase("xdesc")) {
                    cq.orderBy(builder.asc(j1.get(field)));
                } else if (field.equalsIgnoreCase("xnombre")) {
                    cq.orderBy(builder.asc(j2.get(field)));
                } else if (field.equalsIgnoreCase("nroCheque")) {
                    cq.orderBy(builder.asc(r.get("chequesPK").get(field)));
                }
            } else {
                cq.orderBy(builder.asc(r.get(sortField)));
            }
        }
        CriteriaQuery<Cheques> select = cq.select(r);
        TypedQuery<Cheques> q = em.createQuery(select)
                .setFirstResult(range[0])
                .setMaxResults(range[1]);
        return q.getResultList();
    }

    public Long countList(
            String nroCheque,
            String xdesc,
            Date fcheque,
            Long icheque,
            Integer codCliente,
            String xnombre
    ) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        Root<Cheques> r = cq.from(Cheques.class);

        List<Predicate> predicates = new ArrayList<>();
        if (xdesc != null) {
            Join<Cheques, Bancos> j1 = r.join("bancos");
            predicates.add(builder.like(j1.get("xdesc"), "%" + xdesc + "%"));
        }
        if (xnombre != null) {
            Join<Cheques, Clientes> j2 = r.join("codCliente");
            predicates.add(builder.like(j2.get("xnombre"), "%" + xnombre + "%"));
        }
        if (nroCheque != null) {
            predicates.add(builder.like(r.get("chequesPK").get("nroCheque"), "%" + nroCheque + "%"));
        }
        if (fcheque != null) {
            predicates.add(builder.equal(r.get("fcheque"), fcheque));
        }
        if (icheque != null) {
            predicates.add(builder.like(r.get("icheque").as(String.class), "%" + icheque + "%"));
        }

        if (!predicates.isEmpty()) {
            Predicate finalPredicate = builder.and(predicates.toArray(new Predicate[predicates.size()]));
            cq.where(finalPredicate);
        }

        CriteriaQuery<Long> select = cq.select(builder.count(r));
        TypedQuery<Long> q = em.createQuery(select);
        return q.getSingleResult();
    }
}
