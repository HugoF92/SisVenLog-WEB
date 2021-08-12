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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
        for (Object[] resultado : resultados) {
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
            listadoCompras.add(comprasDto);
        }

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

    public List<CompraDto> buscarFacturaCompraPorNroProveedorFechaFactura(long lNrofact, Short lCodProveed, String lFFact) {

        String sql = "SELECT ffactur, nrofact, cod_proveed, ctipo_docum, ttotal, isaldo, ccanal_compra, fvenc FROM compras WHERE cod_empr = 2 "
                + "AND ctipo_docum = 'FCC' AND nrofact = " + lNrofact + " "
                + "AND ffactur = '" + lFFact + "' "
                + "AND cod_proveed = " + lCodProveed;

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

    public List<Compras> comprasPorCodEmprCtipoDocumCodProveedFfacturMestado(
            short codEmpr, String ctipoDocum, short codProveed, Date ffactur,
            char mestado) {
        String sql = "SELECT c FROM Compras c WHERE c.comprasPK.codEmpr = ?1 "
                + "AND c.comprasPK.ctipoDocum = ?2 AND c.comprasPK.codProveed = ?3 "
                + "AND c.comprasPK.ffactur = ?4 AND c.mestado = ?5 "
                + "AND c.isaldo > 0 ORDER BY c.comprasPK.ffactur";
        TypedQuery<Compras> query = em.createQuery(sql, Compras.class);
        query.setParameter(1, codEmpr);
        query.setParameter(2, ctipoDocum);
        query.setParameter(3, codProveed);
        query.setParameter(4, ffactur);
        query.setParameter(5, mestado);
        return query.getResultList();
    }
    
    public List<Compras> comprasPorCodEmprCtipoDocumCodProveedMestado(
            short codEmpr, String ctipoDocum, short codProveed,
            char mestado) {
        String sql = "SELECT c FROM Compras c WHERE c.comprasPK.codEmpr = ?1 "
                + "AND c.comprasPK.ctipoDocum = ?2 AND c.comprasPK.codProveed = ?3 "
                + "AND c.mestado = ?4 AND c.isaldo > 0 ORDER BY c.comprasPK.ffactur";
        TypedQuery<Compras> query = em.createQuery(sql, Compras.class);
        query.setParameter(1, codEmpr);
        query.setParameter(2, ctipoDocum);
        query.setParameter(3, codProveed);
        query.setParameter(4, mestado);
        return query.getResultList();
    }
}
