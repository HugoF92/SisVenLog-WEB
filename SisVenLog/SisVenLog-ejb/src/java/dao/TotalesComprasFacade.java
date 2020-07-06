package dao;

import entidad.Proveedores;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.ejb.Stateless;
import util.DateUtil;

/**
 *
 * @author Arsenio
 */
@Stateless
public class TotalesComprasFacade {
    
    //TODO: cambiar fecha para entorno local yyyy/dd/MM y para el server
    // de prueba yyyy/MM/dd
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateTableMosdatos(Statement stmt, Date fechaDesde,
            Date fechaHasta, String tipoDocumento, Proveedores proveedor) throws SQLException {
        String sql;
        if ("FCC".equals(tipoDocumento) || "COC".equals(tipoDocumento) ||
                "CVC".equals(tipoDocumento)) {
            sql = "SELECT t.* " 
                + "INTO #mostrar "
                + "FROM "
                + "(SELECT "
                + "  d.ctipo_docum, d.pimpues, SUM(d.iexentas) AS texentas, "
                + "  SUM(d.igravadas + d.impuestos) AS tgravadas, SUM(ABS(d.impuestos)) AS timpuestos  "
                + "FROM compras_det d INNER JOIN compras c "
                + "        ON d.nrofact = c.nrofact AND d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed  and d.ffactur = c.ffactur "
                + "WHERE c.cod_empr = 2 "
                + "AND d.cod_empr = 2 "
                + "AND (c.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' ) "
                + "AND c.mestado = 'A' "
                + "AND c.ctipo_docum = '" + tipoDocumento + "' "
                + "AND c.timpuestos < 0 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND c.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            sql += " GROUP BY d.ctipo_docum, d.pimpues "
                + "UNION ALL "
                + "SELECT "
                + "    d.ctipo_docum, d.pimpues, SUM(d.iexentas) AS texentas, "
                + "    SUM(d.igravadas) AS tgravadas, SUM(ABS(d.impuestos)) AS timpuestos "
                + "FROM compras_det d INNER JOIN compras c "
                + "        ON d.nrofact = c.nrofact AND d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed AND d.ffactur = c.ffactur "
                + "WHERE c.cod_empr= 2 "
                + "AND d.cod_empr = 2  "
                + "AND (c.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                + "AND c.mestado = 'A' "
                + "AND c.ctipo_docum = '" + tipoDocumento + "' "
                + "AND c.timpuestos >= 0 ";
            sql += " GROUP BY d.ctipo_docum, d.pimpues) t ";
            System.out.println(sql);
            stmt.execute(sql);
        }
        if ("NCC".equals(tipoDocumento) || "NDC".equals(tipoDocumento)) {
            sql = "SELECT t.* " 
                + "INTO #mostrar "
                + "FROM "
                + "(SELECT "
                + "  d.ctipo_docum, d.pimpues, SUM(d.iexentas) * -1 AS texentas, "
                + "  SUM(d.igravadas + d.impuestos) * -1 AS tgravadas, "
                + "  SUM(ABS(d.impuestos)) * -1 AS timpuestos "
                + "FROM notas_compras_det d INNER JOIN notas_compras c "
                + "        ON d.nro_nota = c.nro_nota AND d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed AND d.fdocum = c.fdocum "
                + "WHERE c.cod_empr = 2 "
                + "AND d.cod_empr = 2 "
                + "AND (c.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                + "AND c.mestado = 'A' "
                + "AND  c.ctipo_docum = '" + tipoDocumento + "' "
                + "AND c.timpuestos < 0 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND c.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            sql += " GROUP BY d.ctipo_docum, d.pimpues "
                + "UNION ALL "
                + "SELECT "
                + "  d.ctipo_docum, d.pimpues, SUM(d.iexentas) * -1 AS texentas, "
                + "  SUM(d.igravadas) * -1 AS tgravadas, "
                + "  SUM(d.impuestos) * -1 AS timpuestos "
                + "FROM notas_compras_det d INNER JOIN notas_compras c "
                + "        ON d.nro_nota = c.nro_nota AND d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed AND d.fdocum = c.fdocum "
                + "WHERE d.cod_empr = 2 "
                + "AND c.cod_empr = 2  "
                + "AND (c.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                + "AND c.mestado = 'A' "
                + "AND c.ctipo_docum = '" + tipoDocumento + "' "
                + "AND c.timpuestos >= 0 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND c.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            sql += " GROUP BY d.ctipo_docum, d.pimpues) t ";
            System.out.println(sql);
            stmt.execute(sql);
        }
        if ("".equals(tipoDocumento)) {
            sql = "SELECT t.* " 
                + "INTO #mostrar "
                + "FROM "
                + "(SELECT "
                + "    d.ctipo_docum, d.pimpues, SUM(d.iexentas) AS texentas, "
                + "    SUM(d.igravadas + d.impuestos) AS tgravadas, "
                + "    SUM(ABS(d.impuestos)) AS timpuestos "
                + "FROM compras_det d INNER JOIN compras c "
                + "        ON d.nrofact = c.nrofact AND d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed AND d.ffactur = c.ffactur "
                + "WHERE d.cod_empr = 2 "
                + "AND c.cod_empr = 2 "
                + "AND (c.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                + "AND c.mestado = 'A' "
                + "AND c.timpuestos < 0 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND c.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            sql += " GROUP BY d.ctipo_docum, d.pimpues "
                + "UNION ALL "
                + "SELECT "
                + "    d.ctipo_docum, d.pimpues, SUM(d.iexentas) AS texentas, "
                + "    SUM(d.igravadas) AS tgravadas, "
                + "    SUM(ABS(d.impuestos)) AS timpuestos "
                + "FROM compras_det d INNER JOIN compras c "
                + "        ON d.nrofact = c.nrofact AND d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed AND d.ffactur = c.ffactur "
                + "WHERE d.cod_empr=2 "
                + "AND c.cod_empr=2 "
                + "AND (c.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                + "AND c.mestado = 'A' "
                + "AND c.timpuestos >= 0 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND c.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            sql += " GROUP BY d.ctipo_docum, d.pimpues "
                + "UNION ALL "
                + "SELECT "
                + "    d.ctipo_docum, d.pimpues, SUM(d.iexentas) * -1  AS texentas, "
                + "    SUM(d.igravadas + d.impuestos) * -1 AS tgravadas, "
                + "    SUM(ABS(d.impuestos)) * -1 AS timpuestos  "
                + "FROM notas_compras_det d INNER JOIN notas_compras c "
                + "    ON d.nro_nota = c.nro_nota AND d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed AND d.fdocum= c.fdocum  "
                + "WHERE d.cod_empr = 2 "
                + "AND c.cod_empr = 2 "
                + "AND (c.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                + "AND c.mestado = 'A' "
                + "AND c.timpuestos < 0 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND c.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            sql += " GROUP BY d.ctipo_docum, d.pimpues "
                + "UNION ALL "
                + "SELECT "
                + "    d.ctipo_docum, d.pimpues, SUM(d.iexentas) * -1 AS texentas, "
                + "    SUM(d.igravadas) * -1 AS tgravadas, "
                + "    SUM(ABS(d.impuestos)) * -1 AS timpuestos "
                + "FROM notas_compras_det d INNER JOIN notas_compras c "
                + "        ON d.nro_nota = c.nro_nota AND d.ctipo_docum = c.ctipo_docum AND d.cod_proveed = c.cod_proveed and c.fdocum = d.fdocum "
                + "WHERE d.cod_empr = 2 "
                + "AND c.cod_empr = 2 "
                + "AND (c.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                + "AND c.mestado = 'A' "
                + "AND c.timpuestos >= 0 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND c.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            sql += " GROUP BY d.ctipo_docum, d.pimpues) t ";
            System.out.println(sql);
            stmt.execute(sql);
        }
        sql = "SELECT "
        + "    ctipo_docum, SUM(texentas) as texentas, "
        + "    SUM(CASE "
        + "        WHEN pimpues = 10 THEN tgravadas "
        + "        ELSE 0 "
        + "    END) as tgravadas_10, "
        + "    SUM(CASE "
        + "        WHEN pimpues = 10 THEN timpuestos "
        + "        ELSE 0 "
        + "    END) as timpuestos_10, "
        + "    SUM(CASE "
        + "        WHEN pimpues = 5 THEN tgravadas "
        + "        ELSE 0 "
        + "    END) as tgravadas_5, "
        + "    SUM(CASE "
        + "        WHEN pimpues = 5 THEN timpuestos "
        + "        ELSE 0 "
        + "    END) as timpuestos_5 "
        + "INTO #infototdoc "
        + "FROM #mostrar "
        + "GROUP BY ctipo_docum";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "SELECT "
            + "    i.ctipo_docum, t.xdesc as xnombre, i.texentas, "
            + "    i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, i.timpuestos_5, "
            + "    (i.texentas + i.tgravadas_10 + i.tgravadas_5 + i.timpuestos_10 + i.timpuestos_5) as ttotal "
            + "INTO #mosdatos "
            + "FROM #infototdoc i, tipos_documentos t "
            + "WHERE i.ctipo_docum = t.ctipo_docum "
            + "ORDER BY i.ctipo_docum ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    public String generateSelectMosdatos() {
        return "SELECT "
                + "ctipo_docum, "
                + "xnombre, "
                + "texentas, "
                + "tgravadas_10, "
                + "tgravadas_5, "
                + "timpuestos_10, "
                + "timpuestos_5, "
                + "ttotal "
                + "FROM #mosdatos ";
    }

    public List<Object[]> listarParaExcel(Statement stmt, String[] columnas, String sql) {
        List<Object[]> lista = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = new Object[columnas.length];
                for (int i = 0; i < columnas.length; i++) {
                    row[i] = rs.getObject(columnas[i]);
                }
                lista.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TotalesComprasFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
