/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.CompraDto;
import entidad.Compras;
import entidad.ComprasPK;
import entidad.DepositosPK;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author admin
 */
@Stateless
public class ComprasFacade extends AbstractFacade<Compras> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @EJB
    private DepositosFacade depositosFacade;

    @EJB
    private ProveedoresFacade proveedoresFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComprasFacade() {
        super(Compras.class);
    }

    public List<CompraDto> listadoDeComprasDelProveedor(String fechaInicial,
            String fechaFinal,
            String tipoDocumento,
            Character estado,
            Short codigoProveedor,
            long numerofactura) {
        String sql = "";

        if (fechaInicial.equals("") && fechaFinal.equals("")) {
            sql = "SELECT f.ccanal_compra, f.fprov, f.fvenc, f.mestado, f.texentas, f.tgravadas, f.timpuestos, f.tdescuentos, "
                    + "f.ttotal, f.isaldo, f.nrofact, f.cod_proveed, f.ffactur, d.xdesc, p.xnombre, f.ctipo_docum, f.cod_depo "
                    + "FROM compras f, Depositos d, proveedores p "
                    + "WHERE f.cod_depo = d.cod_depo AND f.cod_empr = 2 "
                    + "AND f.cod_proveed = p.cod_proveed "
                    + "AND f.ctipo_docum = '" + tipoDocumento + "'";
        } else if (!fechaInicial.equals("") && fechaFinal.equals("")) {
            sql = "SELECT f.ccanal_compra, f.fprov, f.fvenc, f.mestado, f.texentas, f.tgravadas, f.timpuestos, f.tdescuentos, "
                    + "f.ttotal, f.isaldo, f.nrofact, f.cod_proveed, f.ffactur, d.xdesc, p.xnombre, f.ctipo_docum, f.cod_depo "
                    + "FROM compras f, Depositos d, proveedores p "
                    + "WHERE f.cod_depo = d.cod_depo AND f.cod_empr = 2 "
                    + "AND f.cod_proveed = p.cod_proveed "
                    + "AND ffactur >= '" + fechaInicial + "' "
                    + "AND f.ctipo_docum = '" + tipoDocumento + "'";
        } else if (fechaInicial.equals("") && !fechaFinal.equals("")) {
            sql = "SELECT f.ccanal_compra, f.fprov, f.fvenc, f.mestado, f.texentas, f.tgravadas, f.timpuestos, f.tdescuentos, "
                    + "f.ttotal, f.isaldo, f.nrofact, f.cod_proveed, f.ffactur, d.xdesc, p.xnombre, f.ctipo_docum, f.cod_depo "
                    + "FROM compras f, Depositos d, proveedores p "
                    + "WHERE f.cod_depo = d.cod_depo AND f.cod_empr = 2 "
                    + "AND f.cod_proveed = p.cod_proveed "
                    + "AND ffactur <= '" + fechaFinal + "' "
                    + "AND f.ctipo_docum = '" + tipoDocumento + "'";
        } else if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
            sql = "SELECT f.ccanal_compra, f.fprov, f.fvenc, f.mestado, f.texentas, f.tgravadas, f.timpuestos, f.tdescuentos, "
                    + "f.ttotal, f.isaldo, f.nrofact, f.cod_proveed, f.ffactur, d.xdesc, p.xnombre, f.ctipo_docum, f.cod_depo "
                    + "FROM compras f, Depositos d, proveedores p "
                    + "WHERE f.cod_depo = d.cod_depo AND f.cod_empr = 2 "
                    + "AND f.cod_proveed = p.cod_proveed "
                    + "AND ffactur BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "' "
                    + "AND f.ctipo_docum = '" + tipoDocumento + "'";
        }

        if (codigoProveedor != null) {
            if (codigoProveedor > 0) {
                sql += " AND f.cod_proveed = ?";
            }
        }

        if (numerofactura != 0) {
            sql += " AND f.nrofact = ?";
        }

        if (estado != 0) {
            sql += " AND f.mestado = ? ";
        }

        Query q = em.createNativeQuery(sql);

        int i = 0;
        if (codigoProveedor != null) {
            if (codigoProveedor > 0) {
                i++;
                q.setParameter(i, codigoProveedor);
            }
        }

        if (numerofactura != 0) {
            i++;
            q.setParameter(i, numerofactura);
        }

        if (estado != 0) {
            i++;
            q.setParameter(i, estado);
        }

        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CompraDto> listadoCompras = new ArrayList<>();
        resultados.stream().map((resultado) -> {
            //clve primaria compras
            ComprasPK comprasPk = new ComprasPK();
            comprasPk.setCodEmpr(Short.parseShort("2"));
            comprasPk.setCodProveed(Short.parseShort(resultado[11].toString()));
            comprasPk.setCtipoDocum(resultado[15].toString());
            if (resultado[12] != null) {
                Timestamp timeStamp_12 = (Timestamp) resultado[12];
                java.util.Date dateResult_12 = new Date(timeStamp_12.getTime());
                comprasPk.setFfactur(dateResult_12);
            } else {
                comprasPk.setFfactur(null);
            }
            comprasPk.setNrofact(Long.parseLong(resultado[10].toString()));
            Compras compra = new Compras();
            compra.setComprasPK(comprasPk);
            compra.setCcanalCompra(resultado[0] == null ? null : resultado[0].toString());
            DepositosPK depositosPk = new DepositosPK();
            depositosPk.setCodEmpr(Short.parseShort("2"));
            depositosPk.setCodDepo(Short.parseShort(resultado[16].toString()));
            compra.setDepositos(depositosFacade.find(depositosPk));
            compra.setProveedores(proveedoresFacade.find(Short.parseShort(resultado[11].toString())));
            if (resultado[2] != null) {
                Timestamp timeStamp_2 = (Timestamp) resultado[2];
                java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
                compra.setFvenc(dateResult_2);
            } else {
                compra.setFvenc(null);
            }
            char cEstado = resultado[3] == null ? 0 : resultado[3].toString().charAt(0);
            compra.setMestado(cEstado);
            CompraDto comprasDto = new CompraDto();
            comprasDto.setCompra(compra);
            //nombre deposito
            String nombreDeposito = null;
            if (resultado[13] != null) {
                nombreDeposito = resultado[13].toString();
            }
            //nombre del proveedor
            String nombreProveedor = null;
            if (resultado[14] != null) {
                nombreProveedor = resultado[14].toString();
            }
            comprasDto.setDescripcionDeposito(nombreDeposito);
            comprasDto.setNombreProveedor(nombreProveedor);
            return comprasDto;
        }).forEachOrdered((comprasDto) -> {
            listadoCompras.add(comprasDto);
        });

        return listadoCompras;
    }

    public List<CompraDto> maximaCompraContadoDelProveedor(long lNroFact, Short lCodProveed) {

        String sql = "SELECT ffactur, nrofact, cod_proveed, ctipo_docum, ttotal, isaldo, ccanal_compra, fvenc FROM compras WHERE cod_empr = 2 "
                + "AND ctipo_docum = 'FCC' "
                + "AND nrofact = " + lNroFact + " "
                + "AND ffactur = (SELECT MAX(ffactur) FROM compras c2 "
                + "                          WHERE c2.cod_empr = 2 AND c2.nrofact = " + lNroFact + " "
                + "                            AND c2.cod_proveed = " + lCodProveed + " "
                + "                            AND c2.mestado = 'A' "
                + "                            AND c2.ctipo_docum = 'FCC') "
                + "AND cod_proveed = " + lCodProveed
                + "AND mestado ='A' ";

        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CompraDto> listadoCompras = new ArrayList<>();
        resultados.stream().map((resultado) -> {
            //clve primaria compras
            ComprasPK comprasPk = new ComprasPK();
            comprasPk.setCodEmpr(Short.parseShort("2"));
            comprasPk.setCodProveed(Short.parseShort(resultado[2].toString()));
            comprasPk.setCtipoDocum(resultado[3].toString());
            if (resultado[0] != null) {
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());
                comprasPk.setFfactur(dateResult_0);
            } else {
                comprasPk.setFfactur(null);
            }
            comprasPk.setNrofact(Long.parseLong(resultado[1].toString()));
            Compras compra = new Compras();
            compra.setComprasPK(comprasPk);
            compra.setTtotal(Long.parseLong(resultado[4].toString()));
            compra.setIsaldo(Long.parseLong(resultado[5].toString()));
            compra.setCcanalCompra(resultado[6] == null ? null : resultado[6].toString());
            if (resultado[7] != null) {
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());
                compra.setFvenc(dateResult_7);
            } else {
                compra.setFvenc(null);
            }
            return compra;
        }).map((compra) -> {
            CompraDto compraDto = new CompraDto();
            compraDto.setCompra(compra);
            return compraDto;
        }).forEachOrdered((compraDto) -> {
            listadoCompras.add(compraDto);
        });
        return listadoCompras;
    }

    public List<CompraDto> comprasContadoPorFechaNroFacturaProveedor(long lNrofact, Short lCodProveed, String lFFact) {

        String sql = "SELECT ffactur, nrofact, cod_proveed, ctipo_docum, ttotal, isaldo, ccanal_compra, fvenc FROM compras WHERE cod_empr = 2 "
                + "AND ctipo_docum = 'FCC' AND nrofact = " + lNrofact + " "
                + "AND ffactur = '" + lFFact + "' "
                + "AND cod_proveed = " + lCodProveed + " "
                + "AND mestado = 'A' ";

        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CompraDto> listadoCompras = new ArrayList<>();
        resultados.stream().map((resultado) -> {
            //clve primaria compras
            ComprasPK comprasPk = new ComprasPK();
            comprasPk.setCodEmpr(Short.parseShort("2"));
            comprasPk.setCodProveed(Short.parseShort(resultado[2].toString()));
            comprasPk.setCtipoDocum(resultado[3].toString());
            if (resultado[0] != null) {
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());
                comprasPk.setFfactur(dateResult_0);
            } else {
                comprasPk.setFfactur(null);
            }
            comprasPk.setNrofact(Long.parseLong(resultado[1].toString()));
            Compras compra = new Compras();
            compra.setComprasPK(comprasPk);
            compra.setTtotal(Long.parseLong(resultado[4].toString()));
            compra.setIsaldo(Long.parseLong(resultado[5].toString()));
            compra.setCcanalCompra(resultado[6] == null ? null : resultado[6].toString());
            if (resultado[7] != null) {
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());
                compra.setFvenc(dateResult_7);
            } else {
                compra.setFvenc(null);
            }
            return compra;
        }).map((compra) -> {
            CompraDto compraDto = new CompraDto();
            compraDto.setCompra(compra);
            return compraDto;
        }).forEachOrdered((compraDto) -> {
            listadoCompras.add(compraDto);
        });
        return listadoCompras;
    }

    public List<CompraDto> buscarFacturaCompraPorNroProveedorFechaFactura(long lNrofact, Short lCodProveed, String lFFact) {

        String sql = "SELECT ffactur, nrofact, cod_proveed, ctipo_docum, ttotal, isaldo, ccanal_compra, fvenc FROM compras WHERE cod_empr = 2 "
                + "AND ctipo_docum = 'FCC' AND nrofact = " + lNrofact + " "
                + "AND ffactur = '" + lFFact + "' "
                + "AND cod_proveed = " + lCodProveed;

        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CompraDto> listadoCompras = new ArrayList<>();
        resultados.stream().map((resultado) -> {
            //clve primaria compras
            ComprasPK comprasPk = new ComprasPK();
            comprasPk.setCodEmpr(Short.parseShort("2"));
            comprasPk.setCodProveed(Short.parseShort(resultado[2].toString()));
            comprasPk.setCtipoDocum(resultado[3].toString());
            if (resultado[0] != null) {
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());
                comprasPk.setFfactur(dateResult_0);
            } else {
                comprasPk.setFfactur(null);
            }
            comprasPk.setNrofact(Long.parseLong(resultado[1].toString()));
            Compras compra = new Compras();
            compra.setComprasPK(comprasPk);
            compra.setTtotal(Long.parseLong(resultado[4].toString()));
            compra.setIsaldo(Long.parseLong(resultado[5].toString()));
            compra.setCcanalCompra(resultado[6] == null ? null : resultado[6].toString());
            if (resultado[7] != null) {
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());
                compra.setFvenc(dateResult_7);
            } else {
                compra.setFvenc(null);
            }
            return compra;
        }).map((compra) -> {
            CompraDto compraDto = new CompraDto();
            compraDto.setCompra(compra);
            return compraDto;
        }).forEachOrdered((compraDto) -> {
            listadoCompras.add(compraDto);
        });
        return listadoCompras;
    }

    public List<Compras> obtenerComprasPorNroFechaCodProveedorYTipoDocumento(long lNroFactura, String lFFactura, Short lCodProveed, String lCTipoDocum) {
        String sql = "SELECT * FROM compras "
                + "WHERE cod_empr = 2 and nrofact = " + lNroFactura + " "
                + "AND cod_proveed = " + lCodProveed + " AND ctipo_docum = '" + lCTipoDocum + "' ";

        if (!lFFactura.equals("")) {
            sql += "AND ffactur = '" + lFFactura + "' ";
        }

        System.out.println(getEntityManager().createNativeQuery(sql, Compras.class));
        return getEntityManager().createNativeQuery(sql, Compras.class).getResultList();
    }

    public List<Compras> obtenerComprasPorNroFechaCodProveedorYTipoDocumento(long lNroFactura, Short lCodProveed, String lCTipoDocum) {
        String sql = "SELECT * FROM compras "
                + "WHERE cod_empr = 2 and nrofact = " + lNroFactura + " "
                + "AND ffactur > '20130601' "
                + "AND cod_proveed = " + lCodProveed + " AND ctipo_docum = '" + lCTipoDocum + "' ";
        System.out.println(getEntityManager().createNativeQuery(sql, Compras.class));
        return getEntityManager().createNativeQuery(sql, Compras.class).getResultList();
    }

    public List<Compras> buscarComprasEnUnRango(int[] range) {
        Query q = em.createNativeQuery("select * from compras where ctipo_docum in ('COC', 'CVC', 'FCC') and cod_empr = 2 order by ffactur desc", Compras.class);
        System.out.println(q.toString());
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        List<Compras> resultado = q.getResultList();
        return resultado;
    }

    public int obtenerCantidadCompras() {
        String sql = "select count(*) from compras where ctipo_docum in ('COC', 'CVC', 'FCC') and cod_empr = 2 ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Integer> resultados = q.getResultList();
        int respuesta = 0;
        for (Integer resultado : resultados) {
            if (resultado != null) {
                respuesta = Integer.parseInt(resultado.toString());
            }
        }
        return respuesta;
    }

    public List<Compras> buscarComprasEnUnRango(long lNroFact,
            String lFFactura,
            String lNombreProveedor,
            Character lEstado,
            int[] range) {
        List<Compras> resultado;
        String sql = " select * "
                + " from compras as c LEFT JOIN "
                + " proveedores as p "
                + " ON p.cod_proveed = c.cod_proveed "
                + " where "
                + " c.ctipo_docum in ('COC', 'CVC', 'FCC') "
                + " and c.cod_empr = 2 ";

        if (lNroFact != 0) {
            sql += "and c.nrofact = " + lNroFact + " ";
        }

        if (!lFFactura.equals("")) {
            sql += "and c.ffactur = '" + lFFactura + "' ";
        }

        if (!lNombreProveedor.equals("")) {
            sql += "and upper(p.xnombre) like '%" + lNombreProveedor.toUpperCase() + "%' ";
        }

        if (lEstado != 0) {
            sql += "and c.mestado = '" + lEstado + "' ";
        }

        sql += "order by c.ffactur desc";

        Query q = em.createNativeQuery(sql, Compras.class);
        System.out.println(q.toString());
        q.setMaxResults(range[1]);
        q.setFirstResult(range[0]);
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        resultado = q.getResultList();
        return resultado;
    }

    public int obtenerCantidadComprasEnUnRango(long lNroFact,
            String lFFactura,
            String lNombreProveedor,
            Character lEstado,
            int[] range) {

        String sql = " select count(*) "
                + " from compras as c LEFT JOIN "
                + " proveedores as p "
                + " ON p.cod_proveed = c.cod_proveed "
                + " where "
                + " c.ctipo_docum in ('COC', 'CVC', 'FCC') "
                + " and c.cod_empr = 2 ";

        if (lNroFact != 0) {
            sql += "and c.nrofact = " + lNroFact + " ";
        }

        if (!lFFactura.equals("")) {
            sql += "and c.ffactur = '" + lFFactura + "' ";
        }

        if (!lNombreProveedor.equals("")) {
            sql += "and upper(p.xnombre) like '%" + lNombreProveedor.toUpperCase() + "%' ";
        }

        if (lEstado != 0) {
            sql += "and c.mestado = '" + lEstado + "' ";
        }

        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Integer> resultados = q.getResultList();
        int respuesta = 0;
        for (Integer resultado : resultados) {
            if (resultado != null) {
                respuesta = Integer.parseInt(resultado.toString());
            }
        }
        return respuesta;
    }

    public void insertarCompras(String lCTipoDocum,
            long lNroFactura,
            Short lCodProveed,
            String lCanalCompra,
            short lCodDepo,
            String lFFactura, //fecha ingreso
            String lFProv, //fecha factura
            String lObs,
            long lTExentas,
            long lTGravadas,
            long lTImpuestos,
            long lTDescuentos,
            long lTSaldo,
            String lFechaVenc,
            long lTTotal,
            Short lNDiasPlazo,
            String lXFactura,
            BigInteger lNroTimbrado,
            Long lNroPedido) {
        String sql;
        if ("".equals(lCanalCompra) || lCanalCompra == null) {
            sql = "INSERT INTO compras (cod_empr, ctipo_docum, nrofact, "
                    + "cod_proveed, ccanal_compra, cod_depo, ffactur, "
                    + "fprov, xobs, mestado, texentas, tgravadas, timpuestos, tdescuentos, "
                    + "isaldo, fvenc, ttotal, ndias_plazo, xfactura, ntimbrado, nro_pedido ) values ( "
                    + "2, '" + lCTipoDocum + "', " + lNroFactura + ", " + lCodProveed + ", "
                    + "null, " + lCodDepo + ", '" + lFFactura + "', '" + lFProv + "', "
                    + "'" + lObs + "', 'N', " + lTExentas + ", "
                    + "" + lTGravadas + ", " + lTImpuestos + ", " + lTDescuentos + ", " + lTSaldo + ", '" + lFechaVenc + "', " + lTTotal + ", "
                    + "" + lNDiasPlazo + ",  '" + lXFactura + "', " + lNroTimbrado + ", " + lNroPedido + " )";
        } else if (lFechaVenc == null || lFechaVenc.equals("")) {
            sql = "INSERT INTO compras (cod_empr, ctipo_docum, nrofact, "
                    + "cod_proveed, ccanal_compra, cod_depo, ffactur, "
                    + "fprov, xobs, mestado, texentas, tgravadas, timpuestos, tdescuentos, "
                    + "isaldo, fvenc, ttotal, ndias_plazo, xfactura, ntimbrado, nro_pedido ) values ( "
                    + "2, '" + lCTipoDocum + "', " + lNroFactura + ", " + lCodProveed + ", "
                    + "'" + lCanalCompra + "', " + lCodDepo + ", '" + lFFactura + "', '" + lFProv + "', "
                    + "'" + lObs + "', 'N', " + lTExentas + ", "
                    + "" + lTGravadas + ", " + lTImpuestos + ", " + lTDescuentos + ", " + lTSaldo + ", null, " + lTTotal + ", "
                    + "" + lNDiasPlazo + ",  '" + lXFactura + "', " + lNroTimbrado + ", " + lNroPedido + " )";
        } else if ("".equals(lObs) || lObs == null) {
            sql = "INSERT INTO compras (cod_empr, ctipo_docum, nrofact, "
                    + "cod_proveed, ccanal_compra, cod_depo, ffactur, "
                    + "fprov, xobs, mestado, texentas, tgravadas, timpuestos, tdescuentos, "
                    + "isaldo, fvenc, ttotal, ndias_plazo, xfactura, ntimbrado, nro_pedido ) values ( "
                    + "2, '" + lCTipoDocum + "', " + lNroFactura + ", " + lCodProveed + ", "
                    + "'" + lCanalCompra + "', " + lCodDepo + ", '" + lFFactura + "', '" + lFProv + "', "
                    + "null, 'N', " + lTExentas + ", "
                    + "" + lTGravadas + ", " + lTImpuestos + ", " + lTDescuentos + ", " + lTSaldo + ", '" + lFechaVenc + "', " + lTTotal + ", "
                    + "" + lNDiasPlazo + ",  '" + lXFactura + "', " + lNroTimbrado + ", " + lNroPedido + " )";
        } else if (lNDiasPlazo == 0 || lNDiasPlazo == null) {
            sql = "INSERT INTO compras (cod_empr, ctipo_docum, nrofact, "
                    + "cod_proveed, ccanal_compra, cod_depo, ffactur, "
                    + "fprov, xobs, mestado, texentas, tgravadas, timpuestos, tdescuentos, "
                    + "isaldo, fvenc, ttotal, ndias_plazo, xfactura, ntimbrado, nro_pedido ) values ( "
                    + "2, '" + lCTipoDocum + "', " + lNroFactura + ", " + lCodProveed + ", "
                    + "'" + lCanalCompra + "', " + lCodDepo + ", '" + lFFactura + "', '" + lFProv + "', "
                    + "'" + lObs + "', 'N', " + lTExentas + ", "
                    + "" + lTGravadas + ", " + lTImpuestos + ", " + lTDescuentos + ", " + lTSaldo + ", '" + lFechaVenc + "', " + lTTotal + ", "
                    + "null, '" + lXFactura + "', " + lNroTimbrado + ", " + lNroPedido + " )";
        } else if (lNroTimbrado == null) {
            sql = "INSERT INTO compras (cod_empr, ctipo_docum, nrofact, "
                    + "cod_proveed, ccanal_compra, cod_depo, ffactur, "
                    + "fprov, xobs, mestado, texentas, tgravadas, timpuestos, tdescuentos, "
                    + "isaldo, fvenc, ttotal, ndias_plazo, xfactura, ntimbrado, nro_pedido ) values ( "
                    + "2, '" + lCTipoDocum + "', " + lNroFactura + ", " + lCodProveed + ", "
                    + "'" + lCanalCompra + "', " + lCodDepo + ", '" + lFFactura + "', '" + lFProv + "', "
                    + "'" + lObs + "', 'N', " + lTExentas + ", "
                    + "" + lTGravadas + ", " + lTImpuestos + ", " + lTDescuentos + ", " + lTSaldo + ", '" + lFechaVenc + "', " + lTTotal + ", "
                    + "" + lNDiasPlazo + ",  '" + lXFactura + "', null, " + lNroPedido + " )";
        } else if (lNroPedido == 0 || lNroPedido == null) {
            sql = "INSERT INTO compras (cod_empr, ctipo_docum, nrofact, "
                    + "cod_proveed, ccanal_compra, cod_depo, ffactur, "
                    + "fprov, xobs, mestado, texentas, tgravadas, timpuestos, tdescuentos, "
                    + "isaldo, fvenc, ttotal, ndias_plazo, xfactura, ntimbrado, nro_pedido ) values ( "
                    + "2, '" + lCTipoDocum + "', " + lNroFactura + ", " + lCodProveed + ", "
                    + "'" + lCanalCompra + "', " + lCodDepo + ", '" + lFFactura + "', '" + lFProv + "', "
                    + "'" + lObs + "', 'N', " + lTExentas + ", "
                    + "" + lTGravadas + ", " + lTImpuestos + ", " + lTDescuentos + ", " + lTSaldo + ", '" + lFechaVenc + "', " + lTTotal + ", "
                    + "" + lNDiasPlazo + ",  '" + lXFactura + "', " + lNroTimbrado + ", null )";
        } else {
            sql = "INSERT INTO compras (cod_empr, ctipo_docum, nrofact, "
                    + "cod_proveed, ccanal_compra, cod_depo, ffactur, "
                    + "fprov, xobs, mestado, texentas, tgravadas, timpuestos, tdescuentos, "
                    + "isaldo, fvenc, ttotal, ndias_plazo, xfactura, ntimbrado, nro_pedido ) values ( "
                    + "2, '" + lCTipoDocum + "', " + lNroFactura + ", " + lCodProveed + ", "
                    + "'" + lCanalCompra + "', " + lCodDepo + ", '" + lFFactura + "', '" + lFProv + "', "
                    + "'" + lObs + "', 'N', " + lTExentas + ", "
                    + "" + lTGravadas + ", " + lTImpuestos + ", " + lTDescuentos + ", " + lTSaldo + ", '" + lFechaVenc + "', " + lTTotal + ", "
                    + "" + lNDiasPlazo + ",  '" + lXFactura + "', " + lNroTimbrado + ", " + lNroPedido + " )";
        }
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }

    public void actualizarCompras(String lCTipoDocum,
            long lNroFactura,
            Short lCodProveed,
            String lCanalCompra,
            short lCodDepo,
            String lFFactura, //fecha ingreso
            String lFProv, //fecha factura
            String lObs,
            long lTExentas,
            long lTGravadas,
            long lTImpuestos,
            long lTDescuentos,
            long lTSaldo,
            String lFechaVenc,
            long lTTotal,
            Short lNDiasPlazo,
            String lXFactura,
            BigInteger lNroTimbrado,
            Long lNroPedido) {

        String sql;
        if ("".equals(lCanalCompra) || lCanalCompra == null) {
            sql = "UPDATE compras SET ccanal_compra = null, cod_depo = " + lCodDepo + ", ffactur = '" + lFFactura + "', ntimbrado = " + lNroTimbrado + ", fprov = '" + lFProv + "', tdescuentos = " + lTDescuentos + ", ttotal = " + lTTotal + ", "
                    + "xobs = '" + lObs + "', texentas = " + lTExentas + ", tgravadas = " + lTGravadas + ", timpuestos = " + lTImpuestos + ", isaldo = " + lTSaldo + ", fvenc = '" + lFechaVenc + "', ndias_plazo = " + lNDiasPlazo + ", nro_pedido = " + lNroPedido + " "
                    + "WHERE cod_empr = 2 AND ctipo_docum = '" + lCTipoDocum + "' AND ffactur = '" + lFFactura + "' "
                    + "AND nrofact = " + lNroFactura + " AND cod_proveed = " + lCodProveed;
        } else if (lFechaVenc == null || lFechaVenc.equals("")) {
            sql = "UPDATE compras SET ccanal_compra = '" + lCanalCompra + "', cod_depo = " + lCodDepo + ", ffactur = '" + lFFactura + "', ntimbrado = " + lNroTimbrado + ", fprov = '" + lFProv + "', tdescuentos = " + lTDescuentos + ", ttotal = " + lTTotal + ", "
                    + "xobs = '" + lObs + "', texentas = " + lTExentas + ", tgravadas = " + lTGravadas + ", timpuestos = " + lTImpuestos + ", isaldo = " + lTSaldo + ", fvenc = null, ndias_plazo = " + lNDiasPlazo + ", nro_pedido = " + lNroPedido + " "
                    + "WHERE cod_empr = 2 AND ctipo_docum = '" + lCTipoDocum + "' AND ffactur = '" + lFFactura + "' "
                    + "AND nrofact = " + lNroFactura + " AND cod_proveed = " + lCodProveed;
        } else if ("".equals(lObs) || lObs == null) {
            sql = "UPDATE compras SET ccanal_compra = '" + lCanalCompra + "', cod_depo = " + lCodDepo + ", ffactur = '" + lFFactura + "', ntimbrado = " + lNroTimbrado + ", fprov = '" + lFProv + "', tdescuentos = " + lTDescuentos + ", ttotal = " + lTTotal + ", "
                    + "xobs = null, texentas = " + lTExentas + ", tgravadas = " + lTGravadas + ", timpuestos = " + lTImpuestos + ", isaldo = " + lTSaldo + ", fvenc = '" + lFechaVenc + "', ndias_plazo = " + lNDiasPlazo + ", nro_pedido = " + lNroPedido + " "
                    + "WHERE cod_empr = 2 AND ctipo_docum = '" + lCTipoDocum + "' AND ffactur = '" + lFFactura + "' "
                    + "AND nrofact = " + lNroFactura + " AND cod_proveed = " + lCodProveed;
        } else if (lNDiasPlazo == 0 || lNDiasPlazo == null) {
            sql = "UPDATE compras SET ccanal_compra = '" + lCanalCompra + "', cod_depo = " + lCodDepo + ", ffactur = '" + lFFactura + "', ntimbrado = " + lNroTimbrado + ", fprov = '" + lFProv + "', tdescuentos = " + lTDescuentos + ", ttotal = " + lTTotal + ", "
                    + "xobs = '" + lObs + "', texentas = " + lTExentas + ", tgravadas = " + lTGravadas + ", timpuestos = " + lTImpuestos + ", isaldo = " + lTSaldo + ", fvenc = '" + lFechaVenc + "', ndias_plazo = null, nro_pedido = " + lNroPedido + " "
                    + "WHERE cod_empr = 2 AND ctipo_docum = '" + lCTipoDocum + "' AND ffactur = '" + lFFactura + "' "
                    + "AND nrofact = " + lNroFactura + " AND cod_proveed = " + lCodProveed;
        } else if (lNroTimbrado == null) {
            sql = "UPDATE compras SET ccanal_compra = '" + lCanalCompra + "', cod_depo = " + lCodDepo + ", ffactur = '" + lFFactura + "', ntimbrado = null, fprov = '" + lFProv + "', tdescuentos = " + lTDescuentos + ", ttotal = " + lTTotal + ", "
                    + "xobs = '" + lObs + "', texentas = " + lTExentas + ", tgravadas = " + lTGravadas + ", timpuestos = " + lTImpuestos + ", isaldo = " + lTSaldo + ", fvenc = '" + lFechaVenc + "', ndias_plazo = " + lNDiasPlazo + ", nro_pedido = " + lNroPedido + " "
                    + "WHERE cod_empr = 2 AND ctipo_docum = '" + lCTipoDocum + "' AND ffactur = '" + lFFactura + "' "
                    + "AND nrofact = " + lNroFactura + " AND cod_proveed = " + lCodProveed;
        } else if (lNroPedido == 0 || lNroPedido == null) {
            sql = "UPDATE compras SET ccanal_compra = '" + lCanalCompra + "', cod_depo = " + lCodDepo + ", ffactur = '" + lFFactura + "', ntimbrado = " + lNroTimbrado + ", fprov = '" + lFProv + "', tdescuentos = " + lTDescuentos + ", ttotal = " + lTTotal + ", "
                    + "xobs = '" + lObs + "', texentas = " + lTExentas + ", tgravadas = " + lTGravadas + ", timpuestos = " + lTImpuestos + ", isaldo = " + lTSaldo + ", fvenc = '" + lFechaVenc + "', ndias_plazo = " + lNDiasPlazo + ", nro_pedido = null "
                    + "WHERE cod_empr = 2 AND ctipo_docum = '" + lCTipoDocum + "' AND ffactur = '" + lFFactura + "' "
                    + "AND nrofact = " + lNroFactura + " AND cod_proveed = " + lCodProveed;
        } else {
            sql = "UPDATE compras SET ccanal_compra = '" + lCanalCompra + "', cod_depo = " + lCodDepo + ", ffactur = '" + lFFactura + "', ntimbrado = " + lNroTimbrado + ", fprov = '" + lFProv + "', tdescuentos = " + lTDescuentos + ", ttotal = " + lTTotal + ", "
                    + "xobs = '" + lObs + "', texentas = " + lTExentas + ", tgravadas = " + lTGravadas + ", timpuestos = " + lTImpuestos + ", isaldo = " + lTSaldo + ", fvenc = '" + lFechaVenc + "', ndias_plazo = " + lNDiasPlazo + ", nro_pedido = " + lNroPedido + " "
                    + "WHERE cod_empr = 2 AND ctipo_docum = '" + lCTipoDocum + "' AND ffactur = '" + lFFactura + "' "
                    + "AND nrofact = " + lNroFactura + " AND cod_proveed = " + lCodProveed;
        }
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }

    public void activarFacturaCompra(String lCTipoDocum,
            long lNroFactura,
            Short lCodProveed,
            String lFFactura) {
        String sql = "UPDATE compras SET mestado = 'A' "
                + "WHERE  cod_empr = 2 AND nrofact = " + lNroFactura + " "
                + "AND ctipo_docum = '" + lCTipoDocum + "' AND cod_proveed = " + lCodProveed + " AND ffactur = '" + lFFactura + "' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }

    public void borrarCompras(String lCTipoDocum,
            long lNroFactura,
            Short lCodProveed,
            String lFFactura) {
        String sql = "DELETE FROM  compras "
                + "WHERE cod_empr = 2 AND nrofact = " + lNroFactura + " AND ffactur = '" + lFFactura + "' AND "
                + "cod_proveed = " + lCodProveed + " AND ctipo_docum = '" + lCTipoDocum + "' ";
        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        q.executeUpdate();
    }

    public List<Compras> obtenerComprasPorNroFacturaYProveedor(long lNroFactura, Short lCodProveedor) {
        String sql = "SELECT c.*, t.xdesc "
                + "FROM compras c, tipos_documentos t "
                + "WHERE cod_empr = 2 "
                + "AND c.ctipo_docum = t.ctipo_docum\n"
                + "AND c.nrofact = " + lNroFactura + " "
                + "AND c.ffactur = (select MAX(ffactur)\n"
                + "FROM compras\n"
                + "WHERE cod_empr = 2 AND nrofact = " + lNroFactura + " "
                + "AND cod_proveed = " + lCodProveedor + ") "
                + "AND c.cod_proveed = " + lCodProveedor;
        System.out.println(getEntityManager().createNativeQuery(sql, Compras.class));
        return getEntityManager().createNativeQuery(sql, Compras.class).getResultList();
    }

    public List<Compras> obtenerComprasPorNroFacturaProveedorFecha(long lNroFactura, Short lCodProveedor, String lFFactura) {
        String sql = "SELECT c.* , t.xdesc "
                + "FROM compras c, tipos_documentos t "
                + "WHERE cod_empr = 2 "
                + "AND c.ctipo_docum = t.ctipo_docum "
                + "AND c.nrofact = " + lNroFactura + " "
                + "AND c.ffactur = '" + lFFactura + "' "
                + "AND c.cod_proveed = " + lCodProveedor;
        System.out.println(getEntityManager().createNativeQuery(sql, Compras.class));
        return getEntityManager().createNativeQuery(sql, Compras.class).getResultList();
    }

    public List<Compras> comprasPorCodEmprCtipoDocumCodProveedFfacturMestado(
            short codEmpr, String ctipoDocum, short codProveed, Date ffactur,
            char mestado) {
        String sql = "SELECT c FROM Compras c WHERE c.comprasPK.codEmpr = ?1 "
                + (ctipoDocum == null || ctipoDocum.isEmpty() ? "AND c.comprasPK.ctipoDocum IN ('COC','CVC') " : "AND c.comprasPK.ctipoDocum = ?2 ")
                + "AND c.comprasPK.codProveed = ?3 AND c.comprasPK.ffactur = ?4 "
                + "AND c.mestado = ?5 AND c.isaldo > 0 ORDER BY c.comprasPK.ffactur";
        TypedQuery<Compras> query = em.createQuery(sql, Compras.class);
        query.setParameter(1, codEmpr);
        if (ctipoDocum != null && !ctipoDocum.isEmpty()) {
            query.setParameter(2, ctipoDocum);
        }
        query.setParameter(3, codProveed);
        query.setParameter(4, ffactur);
        query.setParameter(5, mestado);
        return query.getResultList();
    }

    public List<Compras> comprasPorCodEmprCtipoDocumCodProveedMestado(
            short codEmpr, String ctipoDocum, short codProveed,
            char mestado) {
        String sql = "SELECT c FROM Compras c WHERE c.comprasPK.codEmpr = ?1 "
                + (ctipoDocum == null || ctipoDocum.isEmpty() ? "AND c.comprasPK.ctipoDocum IN ('COC','CVC') " : "AND c.comprasPK.ctipoDocum = ?2 ")
                + "AND c.comprasPK.codProveed = ?3 AND c.mestado = ?4 AND c.isaldo > 0 "
                + "ORDER BY c.comprasPK.ffactur";
        TypedQuery<Compras> query = em.createQuery(sql, Compras.class);
        query.setParameter(1, codEmpr);
        if (ctipoDocum != null && !ctipoDocum.isEmpty()) {
            query.setParameter(2, ctipoDocum);
        }
        query.setParameter(3, codProveed);
        query.setParameter(4, mestado);
        return query.getResultList();
    }
    
     public List<CompraDto> buscarFacturaCompraPorNroFactura(long lNrofact) {

//         String sql = "SELECT ffactur, nrofact, cod_proveed, ctipo_docum, ttotal, isaldo, ccanal_compra, fvenc FROM compras WHERE cod_empr = 2 "
//                + "AND ctipo_docum = 'FCC' AND nrofact = " + lNrofact;
        String sql = "SELECT ffactur, nrofact, cod_proveed, ctipo_docum, ttotal, isaldo, ccanal_compra, fvenc FROM compras WHERE cod_empr = 2 "
                + "AND nrofact = " + lNrofact;        

        Query q = em.createNativeQuery(sql);
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        List<CompraDto> listadoCompras = new ArrayList<>();
        for (Object[] resultado : resultados) {
            //clve primaria compras
            ComprasPK comprasPk = new ComprasPK();
            comprasPk.setCodEmpr(Short.parseShort("2"));
            comprasPk.setCodProveed(Short.parseShort(resultado[2].toString()));
            comprasPk.setCtipoDocum(resultado[3].toString());
            if (resultado[0] != null) {
                Timestamp timeStamp_0 = (Timestamp) resultado[0];
                java.util.Date dateResult_0 = new Date(timeStamp_0.getTime());
                comprasPk.setFfactur(dateResult_0);
            } else {
                comprasPk.setFfactur(null);
            }
            comprasPk.setNrofact(Long.parseLong(resultado[1].toString()));
            Compras compra = new Compras();
            compra.setComprasPK(comprasPk);
            compra.setTtotal(Long.parseLong(resultado[4].toString()));
            compra.setIsaldo(Long.parseLong(resultado[5].toString()));
            compra.setCcanalCompra(resultado[6] == null ? null : resultado[6].toString());
            if (resultado[7] != null) {
                Timestamp timeStamp_7 = (Timestamp) resultado[7];
                java.util.Date dateResult_7 = new Date(timeStamp_7.getTime());
                compra.setFvenc(dateResult_7);
            } else {
                compra.setFvenc(null);
            }
            CompraDto compraDto = new CompraDto();
            compraDto.setCompra(compra);
            listadoCompras.add(compraDto);
        }
        return listadoCompras;
    }
    
}
