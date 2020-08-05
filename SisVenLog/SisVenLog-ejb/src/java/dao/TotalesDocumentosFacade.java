package dao;

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
import javax.ejb.Stateless;
import util.DateUtil;

/**
 *
 * @author Arsenio
 */
@Stateless
public class TotalesDocumentosFacade {
    
    //TODO: cambiar fecha para entorno local yyyy/dd/MM y para el server
    // de prueba yyyy/MM/dd
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateTableMosdatos(Statement stmt, Date fechaDesde,
            Date fechaHasta, String tipoDocumento, String opcion) throws SQLException {
        generateMostrar(tipoDocumento, fechaDesde, fechaHasta, opcion, stmt);
        generateTableTmpNumeros(stmt);
        generateTableCurdet(stmt);
        generateTableTotdet(stmt);
        generateTableInfototdoc(stmt);
        insertFacturasNotasDeCredito(stmt);
        insertFacturasNotasDeVenta(stmt);
        String sql = "SELECT * "
                + "INTO #mosdatos "
                + "FROM #infototdoc "
                + "ORDER BY fdocum, ndocum, ctipo_docum ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void generateMostrar(String tipoDocumento, Date fechaDesde,
            Date fechaHasta, String opcion, Statement stmt) throws SQLException {
        String sql;
        if ("FCR".equals(tipoDocumento) || "CPV".equals(tipoDocumento) ||
                "FCO".equals(tipoDocumento) || "FCP".equals(tipoDocumento) || "FCS".equals(tipoDocumento)) {
            sql = "SELECT f.ffactur as fdocum, f.nrofact as ndocum, CAST('' AS char(3)) as cconc, c.cod_cliente, f.ctipo_docum, "
                    + "	 c.xnombre, f.texentas, f.tgravadas , f.timpuestos, f.ttotal, f.mestado "
                    + "INTO #mostrar "
                    + "FROM facturas f, clientes c, rangos_documentos r "
                    + "WHERE f.cod_empr = 2 "
                    + "AND f.cod_cliente = c.cod_cliente "
                    + "AND f.ctipo_docum = r.ctipo_docum "
                    + "AND f.cod_empr = r.cod_empr "
                    + "AND f.ctipo_docum = '" + tipoDocumento +"' "
                    + "AND year(f.ffactur) between r.nano_inicial AND r.nano_final "
                    + "AND f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin  "
                    + "AND (f.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                    + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' ) ";
            switch(opcion){
                case "F":
                    sql += "AND r.mtipo_papel = 'F' ";
                    break;
                case "M":
                    sql += "AND r.mtipo_papel = 'M' ";
                    break;
                case "":
                    sql += " AND r.mtipo_papel IN ('F','M') ";
                    break;
            }
            sql += " ORDER BY 1, 5, 2 ";
            System.out.println(sql);
            stmt.execute(sql);
        }
        if ("NCV".equals(tipoDocumento)) {
            sql = "SELECT "
                    + "    n.fdocum, n.nro_nota as ndocum, n.cconc, n.cod_cliente, "
                    + "    n.ctipo_docum, c.xnombre, n.texentas * -1 as texentas, "
                    + "    (n.tgravadas + n.timpuestos) * -1 as tgravadas, "
                    + "    n.timpuestos as timpuestos, n.ttotal * -1 as ttotal, n.mestado "
                    + "INTO #mostrar "
                    + "FROM clientes c, notas_ventas n, rutas r "
                    + "WHERE n.cod_empr = 2 "
                    + "AND n.cod_cliente = c.cod_cliente "
                    + "AND n.ctipo_docum = '" + tipoDocumento + "' "
                    + "AND (n.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                    + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' ) "
                    + "AND c.cod_ruta = r.cod_ruta ";
            sql += " ORDER BY 1, 5, 2 ";
            System.out.println(sql);
            stmt.execute(sql);
        }
        if ("NDV".equals(tipoDocumento)) {
            sql = "SELECT "
                    + "    n.fdocum, n.nro_nota as ndocum, n.cconc, n.cod_cliente, "
                    + "    n.ctipo_docum, c.xnombre, n.texentas as texentas, "
                    + "    (n.tgravadas + n.timpuestos) as tgravadas, n.timpuestos as timpuestos, "
                    + "    n.ttotal as ttotal, n.mestado "
                    + "INTO #mostrar "
                    + "FROM clientes c, notas_ventas n "
                    + "WHERE n.cod_empr = 2 "
                    + "AND n.cod_cliente = c.cod_cliente "
                    + "AND n.ctipo_docum = '" + tipoDocumento + "' "
                    + "AND (n.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                    + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' ) ";
            sql += " ORDER BY 1, 5, 2 ";
            System.out.println(sql);
            stmt.execute(sql);
        }
        if ("".equals(tipoDocumento)) {
            sql = "SELECT t.* " 
                + "INTO #mostrar "
                + "FROM "
                + "(SELECT "
                + "    f.ffactur as fdocum, f.nrofact as ndocum, CAST('' AS char(3)) as cconc, "
                + "    c.cod_cliente, f.ctipo_docum, c.xnombre, f.texentas, "
                + "    f.tgravadas , f.timpuestos, f.ttotal, f.mestado "
                + "FROM facturas f, clientes c, rangos_documentos r "
                + "WHERE f.cod_empr = 2 "
                + "AND f.cod_cliente = c.cod_cliente "
                + "AND f.ctipo_docum = r.ctipo_docum "
                + "AND f.cod_empr = r.cod_empr "
                + "AND year(f.ffactur) between r.nano_inicial AND r.nano_final "
                + "AND f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin "
                + "AND f.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' ";
            switch(opcion){
                case "F":
                    sql += "AND r.mtipo_papel = 'F' ";
                    break;
                case "M":
                    sql += "AND r.mtipo_papel = 'M' ";
                    break;
                case "":
                    sql += " AND r.mtipo_papel IN ('F','M') ";
                    break;
            }
            sql += "UNION ALL  "
                + "SELECT "
                + "    n.fdocum, n.nro_nota as ndocum, n.cconc, n.cod_cliente,  "
                + "    n.ctipo_docum, c.xnombre, n.texentas * -1 as texentas, "
                + "    (n.tgravadas + n.timpuestos) * -1 as tgravadas, "
                + "    n.timpuestos as timpuestos, n.ttotal * -1 as ttotal, n.mestado "
                + "FROM clientes c, notas_ventas n "
                + "WHERE n.cod_empr = 2 "
                + "AND n.ctipo_docum = 'NCV' "
                + "AND n.cod_cliente = c.cod_cliente "
                + "AND n.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' ";
            sql += "UNION ALL  "
                + "SELECT "
                + "    n.fdocum, n.nro_nota as ndocum, n.cconc, n.cod_cliente,  "
                + "    n.ctipo_docum, c.xnombre, n.texentas as texentas, "
                + "    (n.tgravadas + n.timpuestos) as tgravadas, n.timpuestos as timpuestos, "
                + "    n.ttotal as ttotal, n.mestado "
                + "FROM clientes c, notas_ventas n "
                + "WHERE n.cod_empr = 2 "
                + "AND n.ctipo_docum = 'NDV' "
                + "AND n.cod_cliente = c.cod_cliente "
                + "AND n.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') t ";
            sql += " ORDER BY 1, 5, 2 ";
            System.out.println(sql);
            stmt.execute(sql);
        }
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
            Logger.getLogger(TotalesDocumentosFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    private void generateTableTmpNumeros(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE #tmp_numeros (ndocum NUMERIC(12), ctipo_docum CHAR(3), fdocum datetime)";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "INSERT INTO #tmp_numeros "
            + "SELECT "
            + "    ndocum, ctipo_docum, fdocum "
            + "FROM #mostrar ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void generateTableCurdet(Statement stmt) throws SQLException {
        String sql = "SELECT t.* " 
                + "INTO #curdet "
                + "FROM "
                + "(SELECT "
                + "    t.ctipo_docum, t.ndocum,  0 AS texentas, isnull(SUM(d .igravadas), 0) AS tgravadas_5, "
                + "    0 AS tgravadas_10, isnull(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 "
                + "FROM  facturas_det d  INNER JOIN #tmp_numeros t "
                + "        ON d.nrofact = t.ndocum  "
                + "        AND d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                + "        AND d.ffactur  = t.fdocum "
                + "WHERE d.pimpues = 5 "
                + "AND (d .cod_empr = 2) "
                + "AND t.ctipo_docum IN ('FCR','CPV','FCO') "
                + "GROUP BY  t.ctipo_docum, t.ndocum "
                + "UNION ALL "
                + "SELECT "
                + "    t.ctipo_docum, t.ndocum, 0 AS texentas, 0 AS tgravadas_5, "
                + "    isnull(SUM(d .igravadas), 0) AS tgravadas_10, 0 AS timpuestos_5, "
                + "    isnull(SUM(impuestos), 0) AS timpuestos_10 "
                + "FROM  facturas_det d INNER JOIN #tmp_numeros t "
                + "        ON d.nrofact  = t.ndocum  "
                + "        AND d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                + "        AND d.ffactur  = t.fdocum  "
                + "WHERE  d.pimpues = 10 and (d .cod_empr = 2) "
                + "AND t.ctipo_docum IN ('FCR','CPV','FCO') "
                + "GROUP BY t.ctipo_docum, t.ndocum "
                + "UNION ALL "
                + "SELECT "
                + "    t.ctipo_docum, t.ndocum, isnull(SUM(iexentas), 0) AS texentas, "
                + "    0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 "
                + "FROM facturas_det d INNER JOIN #tmp_numeros t "
                + "    ON d.nrofact  = t.ndocum  "
                + "    AND d.ffactur = t.fdocum AND d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                + "     AND d.iexentas > 0 "
                + "WHERE (d.cod_empr = 2) "
                + "AND t.ctipo_docum IN ('FCR','CPV','FCO') "
                + "GROUP BY t.ctipo_docum, t.ndocum "
                + "UNION ALL "
                + "SELECT "
                + "    t.ctipo_docum, t.ndocum, 0 AS texentas, "
                + "    isnull(SUM(d .igravadas), 0) AS tgravadas_5, "
                + "    0 AS tgravadas_10, isnull(SUM(impuestos), 0) AS timpuestos_5, "
                + "    0 AS timpuestos_10 "
                + "FROM  facturas_ser d INNER JOIN #tmp_numeros t "
                + "    ON d.nrofact = t.ndocum "
                + "     AND d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                + "     AND d.ffactur = t.fdocum "
                + "WHERE d.pimpues = 5 "
                + "AND (d .cod_empr = 2) "
                + "AND t.ctipo_docum IN ('FCP','FCS') "
                + "GROUP BY  t.ctipo_docum, t.ndocum "
                + "UNION ALL "
                + "SELECT "
                + "    t.ctipo_docum, t.ndocum,  0 AS texentas, 0 AS tgravadas_5, "
                + "    isnull(SUM(d .igravadas), 0) AS tgravadas_10, 0 AS timpuestos_5, "
                + "    isnull(SUM(impuestos), 0) AS timpuestos_10 "
                + "FROM  facturas_ser d INNER JOIN #tmp_numeros t "
                + "    ON d.nrofact = t.ndocum "
                + "    AND d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                + "    AND d.ffactur = t.fdocum "
                + "WHERE d.pimpues = 10 "
                + "AND (d .cod_empr = 2) "
                + "AND t.ctipo_docum IN ('FCP','FCS') "
                + "GROUP BY t.ctipo_docum, t.ndocum "
                + "UNION ALL "
                + "SELECT "
                + "    t.ctipo_docum, t.ndocum, isnull(SUM(iexentas), 0) AS texentas, 0 AS tgravadas_5, 0 AS  "
                + "    tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 "
                + "FROM facturas_ser d INNER JOIN #tmp_numeros t "
                + "    ON d.nrofact = t.ndocum AND d.ffactur = t.fdocum "
                + "    AND d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                + "    AND d .iexentas > 0 "
                + "WHERE (d.cod_empr = 2) "
                + "AND t.ctipo_docum IN ('FCP','FCS') "
                + "GROUP BY t.ctipo_docum, t.ndocum) t ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void generateTableTotdet(Statement stmt) throws SQLException {
        String sql = "SELECT "
                + "    ctipo_docum, ndocum, SUM(texentas) as texentas, "
                + "    sum(tgravadas_5) as tgravadas_5, SUM(tgravadas_10) as tgravadas_10, "
                + "    SUM(timpuestos_5) as timpuestos_5, sum(timpuestos_10) as timpuestos_10 "
                + "INTO #totdet "
                + "FROM #curdet "
                + "GROUP BY ctipo_docum, ndocum ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void generateTableInfototdoc(Statement stmt) throws SQLException {
        String sql = "SELECT "
                    + "    td.ndocum, td.ctipo_docum, m.fdocum, m.cconc, "
                    + "    m.cod_cliente, m.xnombre, m.ttotal, m.mestado,"
                    + "    td.texentas, td.tgravadas_5, td.tgravadas_10, "
                    + "    td.timpuestos_5, td.timpuestos_10 "
                    + "INTO #infototdoc "
                    + "FROM #mostrar m INNER JOIN #totdet td "
                    + "    ON m.ndocum = td.ndocum "
                    + "AND m.ctipo_docum COLLATE DATABASE_DEFAULT = td.ctipo_docum COLLATE DATABASE_DEFAULT ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void insertFacturasNotasDeCredito(Statement stmt) throws SQLException {
        String sql = "TRUNCATE TABLE #curdet";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "INSERT INTO #curdet "
                    + "SELECT t.* " 
                    + "FROM "
                    + "(SELECT "
                    + "    t.ctipo_docum, t.ndocum,  0 AS texentas, isnull(SUM(d .igravadas + d.impuestos), 0) * -1 AS tgravadas_5, "
                    + "    0 AS tgravadas_10, isnull(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 "
                    + "FROM notas_ventas_det d INNER JOIN #tmp_numeros t "
                    + "    ON d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                    + "    AND d.fdocum = t.fdocum AND d.nro_nota = t.ndocum AND t.ctipo_docum in ('NCV') "
                    + "WHERE d.pimpues = 5 "
                    + "AND (d.cod_empr = 2) "
                    + "GROUP BY t.ctipo_docum, t.ndocum "
                    + "UNION ALL "
                    + "SELECT "
                    + "    t.ctipo_docum, t.ndocum, 0 AS texentas, 0 AS tgravadas_5, "
                    + "    isnull(SUM(d .igravadas +d.impuestos), 0) * -1 AS tgravadas_10, "
                    + "    0 AS timpuestos_5, isnull(SUM(impuestos), 0)  AS timpuestos_10 "
                    + "FROM notas_ventas_det d INNER JOIN #tmp_numeros t "
                    + "    ON d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                    + "    AND d.fdocum = t.fdocum AND d.nro_nota = t.ndocum AND t.ctipo_docum in ('NCV') "
                    + "WHERE d.pimpues = 10 "
                    + "AND (d .cod_empr = 2) "
                    + "GROUP BY t.ctipo_docum, t.ndocum "
                    + "UNION ALL "
                    + "SELECT "
                    + "    t.ctipo_docum, t.ndocum, isnull(SUM(iexentas), 0) * -1 AS texentas, 0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 "
                    + "FROM notas_ventas_det d INNER JOIN #tmp_numeros t "
                    + "    ON d.nro_nota = t.ndocum AND d.fdocum = t.fdocum "
                    + "     AND d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                    + "     AND d.iexentas > 0 "
                    + "WHERE (d .cod_empr = 2) "
                    + "AND t.ctipo_docum in ('NCV') "
                    + "GROUP BY t.ctipo_docum, t.ndocum) t ";
        System.out.println(sql);
        stmt.execute(sql);

        sql = "TRUNCATE TABLE #totdet";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "INSERT INTO #totdet "
                + "SELECT "
                + "    ctipo_docum, ndocum, SUM(texentas) as texentas, "
                + "    SUM(tgravadas_5) as tgravadas_5, SUM(tgravadas_10) as tgravadas_10, "
                + "    SUM(timpuestos_5) as timpuestos_5, sum(timpuestos_10) as timpuestos_10 "
                + "FROM #curdet "
                + "GROUP BY ctipo_docum, ndocum ";
        System.out.println(sql);
        stmt.execute(sql);

        sql = "INSERT INTO #infototdoc "
                + "SELECT "
                    + "    td.ndocum, td.ctipo_docum, m.fdocum, m.cconc, "
                    + "    m.cod_cliente, m.xnombre, m.ttotal, m.mestado, "
                    + "    td.texentas, td.tgravadas_5, td.tgravadas_10, "
                    + "    td.timpuestos_5, td.timpuestos_10 "
                    + "FROM #mostrar m INNER JOIN #totdet td "
                    + "    ON m.ndocum = td.ndocum "
                    + "    AND m.ctipo_docum COLLATE DATABASE_DEFAULT = td.ctipo_docum COLLATE DATABASE_DEFAULT ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void insertFacturasNotasDeVenta(Statement stmt) throws SQLException {
        String sql = "TRUNCATE TABLE #curdet";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "INSERT INTO #curdet "
                    + "SELECT t.* " 
                    + "FROM "
                    + "(SELECT "
                    + "    t.ctipo_docum, t.ndocum,  0 AS texentas, "
                    + "    isnull(SUM(d .igravadas + d.impuestos), 0) AS tgravadas_5, "
                    + "    0 AS tgravadas_10, isnull(SUM(impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10 "
                    + "FROM notas_ventas_det d INNER JOIN #tmp_numeros t "
                    + "    ON d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                    + "     AND d.fdocum = t.fdocum AND d.nro_nota = t.ndocum AND t.ctipo_docum in ('NDV') "
                    + "WHERE    d.pimpues = 5 and  (d.cod_empr = 2) "
                    + "GROUP BY t.ctipo_docum, t.ndocum "
                    + "UNION ALL "
                    + "SELECT "
                    + "    t.ctipo_docum, t.ndocum, 0 AS texentas, 0 AS tgravadas_5, isnull(SUM(d .igravadas +d.impuestos), 0) AS tgravadas_10, 0 AS timpuestos_5, isnull(SUM(impuestos), 0)  AS timpuestos_10 "
                    + "FROM notas_ventas_det d INNER JOIN #tmp_numeros t "
                    + "    ON d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                    + "     AND d.fdocum = t.fdocum AND d.nro_nota = t.ndocum AND t.ctipo_docum in ('NDV') "
                    + "WHERE d.pimpues = 10 "
                    + "AND (d .cod_empr = 2) "
                    + "GROUP BY t.ctipo_docum, t.ndocum "
                    + "UNION ALL "
                    + "SELECT "
                    + "    t.ctipo_docum , t.ndocum, isnull(SUM(iexentas), 0) AS texentas, "
                    + "    0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 "
                    + "FROM notas_ventas_det d INNER JOIN #tmp_numeros t "
                    + "    ON d.nro_nota = t.ndocum AND d.fdocum = t.fdocum "
                    + "     AND d.ctipo_docum COLLATE DATABASE_DEFAULT = t.ctipo_docum COLLATE DATABASE_DEFAULT "
                    + "     AND d .iexentas > 0 "
                    + "WHERE (d .cod_empr = 2) "
                    + "AND t.ctipo_docum in ('NDV') "
                    + "GROUP BY t.ctipo_docum, t.ndocum) t ";
        System.out.println(sql);
        stmt.execute(sql);

        sql = "TRUNCATE TABLE #totdet";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "INSERT INTO #totdet "
                + "SELECT "
                + "    ctipo_docum, ndocum, SUM(texentas) as texentas, "
                + "    sum(tgravadas_5) as tgravadas_5, SUM(tgravadas_10) as tgravadas_10, "
                + "    SUM(timpuestos_5) as timpuestos_5, sum(timpuestos_10) as timpuestos_10 "
                + "FROM #curdet "
                + "GROUP BY ctipo_docum, ndocum ";
        System.out.println(sql);
        stmt.execute(sql);

        sql = "INSERT INTO #infototdoc "
                + "SELECT "
                    + "    td.ndocum, td.ctipo_docum, m.fdocum, m.cconc, "
                    + "    m.cod_cliente, m.xnombre, m.ttotal, m.mestado, "
                    + "    td.texentas, td.tgravadas_5, td.tgravadas_10, "
                    + "    td.timpuestos_5, td.timpuestos_10 "
                    + "FROM #mostrar m INNER JOIN #totdet td "
                    + "    ON m.ndocum = td.ndocum "
                    + "     AND m.ctipo_docum COLLATE DATABASE_DEFAULT = td.ctipo_docum COLLATE DATABASE_DEFAULT ";
        System.out.println(sql);
        stmt.execute(sql);
    }
    
        public String generateSelectMosdatos() {
        return "SELECT "
                + "ndocum, "
                + "ctipo_docum, "
                + "xnombre, "
                + "fdocum, "
                + "cconc, "
                + "cod_cliente, "
                + "mestado, "
                + "texentas, "
                + "tgravadas_10, "
                + "tgravadas_5, "
                + "timpuestos_10, "
                + "timpuestos_5, "
                + "ttotal "
                + "FROM #mosdatos ";
    }
    
}
