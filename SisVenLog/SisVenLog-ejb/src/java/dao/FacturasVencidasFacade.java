package dao;

import entidad.Clientes;
import entidad.Empleados;
import entidad.TiposDocumentos;
import entidad.Zonas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.DateUtil;
import util.StringUtil;

/**
 *
 * @author Arsenio
 */
@Stateless
public class FacturasVencidasFacade {
    
    //TODO: cambiar fecha para entorno local yyyy/dd/MM y para el server
    // de prueba yyyy/MM/dd
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateTmpTableMostrar(Statement stmt, Date fechaVencimiento,
            Date fechaHasta, Zonas zona, String discriminar, Empleados vendedor,
            Integer plazoCreditoDesde, Integer plazoCreditoHasta, TiposDocumentos tipoDocumento,
            Boolean todosClientes, List<Clientes> listadoClientesSeleccionados) throws SQLException, SQLException {
        String tdoc = tipoDocumento == null? "": tipoDocumento.getCtipoDocum();
        createTableMostrar(tdoc, fechaVencimiento, fechaHasta, plazoCreditoDesde,
                plazoCreditoHasta, zona, vendedor, todosClientes,
                listadoClientesSeleccionados, stmt);
        createTableCurclit(discriminar, stmt);
        createTableDatosinf(stmt, fechaHasta, discriminar);
        createTableFinal(stmt, discriminar);
    }

    private void createTableFinal(Statement stmt, String discriminar) throws SQLException {
        String sql;
        sql = "SELECT "
            + "    cod_cliente, isaldo_inicial, sum(ideuda) as ideuda "
            + "INTO #curdat "
            + "FROM #datosinf  "
            + "GROUP BY cod_cliente, isaldo_inicial ";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "SELECT "
            + "    cod_cliente "
            + "INTO #curdat2 "
            + "FROM #curdat "
            + "WHERE isaldo_inicial + ideuda != 0 ";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "select d.* "
            + "INTO #datosinf2 "
            + "FROM #datosinf d "
            + "WHERE cod_cliente in (SELECT cod_cliente FROM #curdat2) "
            + "ORDER BY cod_cliente ";
        System.out.println(sql);
        stmt.execute(sql);
        class Datosinf2 {
            Integer codCliente;
            Double isaldoInicial;
            Double ideuda;
            public Datosinf2(Integer codCliente, Double isaldoInicial, Double ideuda) {
                this.codCliente = codCliente;
                this.isaldoInicial = isaldoInicial;
                this.ideuda = ideuda;
            }
        }
        List<Datosinf2> datosinf2 = new ArrayList<>();
        sql = "SELECT cod_cliente, isaldo_inicial, ideuda from #datosinf2";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            datosinf2.add(new Datosinf2(rs.getInt("cod_cliente"),
                            rs.getDouble("isaldo_inicial"),
                            rs.getDouble("ideuda")));
        }
        Integer l_corte_cliente = 0;
        Double l_totald = 0.0;
        for(Datosinf2 datosinfdos : datosinf2){
            if(!Objects.equals(l_corte_cliente, datosinfdos.codCliente)) {
                if(l_corte_cliente != 0){
                    if(l_totald == 0){
                        sql = "UPDATE #datosinf2 SET xnombre = '*' "
                                + "WHERE cod_cliente = " + datosinfdos.codCliente;
                        System.out.println(sql);
                        stmt.execute(sql);
                    }
                }
                l_corte_cliente = datosinfdos.codCliente;
                l_totald = datosinfdos.isaldoInicial;
            }
            l_totald += datosinfdos.ideuda;
        }
        if(l_corte_cliente != 0){
            if(l_totald == 0){
                sql = "UPDATE #datosinf2 SET xnombre = '*' "
                        + "WHERE cod_cliente = " + l_corte_cliente;
                System.out.println(sql);
                stmt.execute(sql);
            }
        }
        sql = "SELECT * "
                + "INTO #final "
                + "FROM #datosinf2 "
                + "WHERE xnombre != '*' ";
        if("ND".equals(discriminar)){
            sql += "ORDER BY xnombre, cod_cliente,  fdocum, ctipo_docum, ndocum ";
        } else {
            sql += "ORDER BY cod_zona, xnombre, cod_cliente, fdocum, ctipo_docum, ndocum";
        }
        System.out.println(sql);
        stmt.execute(sql);
        sql = "select column_name  from tempdb.INFORMATION_SCHEMA.COLUMNS " +
            "where table_name like '#final%'";
        rs = stmt.executeQuery(sql);
        System.out.println("Columnas de la tabla temporal final");
        while (rs.next()) {
            System.out.println(rs.getString("column_name"));
        }
    }

    private void createTableDatosinf(Statement stmt, Date fechaHasta, String discriminar) throws SQLException {
        String sql = "SELECT cod_cliente from #curclit";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        List<String> codClientes = new ArrayList<>();
        while (rs.next()) {            
            codClientes.add(rs.getString("cod_cliente"));
        }
        // Creamos la tabla datosinf
        sql = "CREATE TABLE #datosinf ( "
            + "cod_cliente integer, "
            + "isaldo_inicial numeric(10,0), "
            + "cod_zona char(2), "
            + "xdesc_zona varchar(50), "
            + "xnombre varchar(50), "
            + "nplazo_credito numeric(3,0),"
            + "ctipo_docum char(3), "
            + "ndocum char(15), "
            + "fdocum smalldatetime,"
            + "fvenc smalldatetime,"
            + "ideuda numeric(12,0),"
            + "xdesc_zona2 varchar(50),"
            + "xdesc_zona3 varchar(50) "
            + ")";
        System.out.println(sql);
        stmt.execute(sql);
        Boolean exist;
        Double saldoInicial;
        for(String codCliente : codClientes){
            sql = "select dbo.calc_saldo_inicial_cliente(2, '" + codCliente +"', '"+DateUtil.dateToString(fechaHasta, DATE_FORMAT)+"')";
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            exist = rs.next();
            saldoInicial = exist? rs.getDouble(1) : 0;
            sql = "INSERT INTO #datosinf (cod_cliente, isaldo_inicial) "
                    + "VALUES ('" + codCliente +"', " + saldoInicial + ")";
            System.out.println(sql);
            stmt.execute(sql);
        }
        sql = "SELECT cod_cliente, sum(ideuda) as tdeuda "
            + "INTO #rescli "
            + "FROM #mosdatos "
            + "GROUP BY cod_cliente ";
        System.out.println(sql);
        stmt.execute(sql);

        class Rescli {
            Integer codCliente;
            Double tdeuda;

            public Rescli(Integer codCliente, Double tdeuda) {
                this.codCliente = codCliente;
                this.tdeuda = tdeuda;
            }
        }
        List<Rescli> resclients = new ArrayList<>();
        sql = "SELECT cod_cliente, tdeuda from #rescli";
        System.out.println(sql);
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            resclients.add(new Rescli(rs.getInt("cod_cliente"), rs.getDouble("tdeuda")));
        }
        for(Rescli rescli: resclients){
            sql = "UPDATE #datosinf SET isaldo_inicial = isaldo_inicial - " + rescli.tdeuda + " "
                    + "WHERE cod_cliente = " + rescli.codCliente;
            System.out.println(sql);
            stmt.execute(sql);
        }
        // Trabajamos sobre Mosdatos
        class Mosdatos {
            int codCliente;
            String codZona;
            String xdescZona;
            String xnombre;
            double nplazoCredito;
            String ctipoDocum;
            String ndocum;
            java.sql.Date fdocum;
            java.sql.Date fvenc;
            String xdescZona2;
            String xdescZona3;
            double ideuda;

            public Mosdatos(int codCliente, String codZona, String xdescZona,
                    String xnombre, double nplazoCredito, String ctipoDocum,
                    String ndocum, java.sql.Date fdocum, java.sql.Date fvenc, String xdescZona2,
                    String xdescZona3, double ideuda) {
                this.codCliente = codCliente;
                this.codZona = codZona;
                this.xdescZona = xdescZona;
                this.xnombre = xnombre;
                this.nplazoCredito = nplazoCredito;
                this.ctipoDocum = ctipoDocum;
                this.ndocum = ndocum;
                this.fdocum = fdocum;
                this.fvenc = fvenc;
                this.xdescZona2 = xdescZona2;
                this.xdescZona3 = xdescZona3;
                this.ideuda = ideuda;
            }
        }
        List<Mosdatos> mosdatos = new ArrayList<>();
        sql = "SELECT cod_cliente, cod_zona, xdesc_zona, xnombre, nplazo_credito, "
                + "ctipo_docum, ndocum, fdocum, fvenc, ideuda, "
                + "xdesc_zona2, xdesc_zona3 from #mosdatos";
        System.out.println(sql);
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            mosdatos.add(new Mosdatos(rs.getInt("cod_cliente"),
                                      rs.getString("cod_zona"),
                                      rs.getString("xdesc_zona"),
                                      rs.getString("xnombre"),
                                      rs.getDouble("nplazo_credito"),
                                      rs.getString("ctipo_docum"),
                                      rs.getString("ndocum"),
                                      rs.getDate("fdocum"),
                                      rs.getDate("fvenc"),
                                      rs.getString("xdesc_zona2"),
                                      rs.getString("xdesc_zona3"),
                                      rs.getDouble("ideuda")        
            ));
        }
        for(Mosdatos mosdato : mosdatos){
            if("ND".equals(discriminar)){
                sql = "UPDATE #datosinf SET cod_zona = '', xdesc_zona = '', ";
            } else {
                sql = "UPDATE #datosinf SET cod_zona = '" + mosdato.codZona + "',"
                        + "xdesc_zona = '" + mosdato.xdescZona + "', ";
            }
            
            sql += "cod_cliente = " + mosdato.codCliente + ","
                + "xnombre = '" + mosdato.xnombre + "',"
                + "nplazo_credito = " +  mosdato.nplazoCredito + ","
                + "ctipo_docum = '" +  mosdato.ctipoDocum + "',"
                + "ndocum = '" + mosdato.ndocum + "',"
                + "fdocum = '" + DateUtil.dateToString(mosdato.fdocum, DATE_FORMAT) + "',"
                + "fvenc  = '" + DateUtil.dateToString(mosdato.fvenc, DATE_FORMAT) + "',"
                + "ideuda = " + mosdato.ideuda + ","
                + "xdesc_zona2 = '" + mosdato.xdescZona2 + "',"
                + "xdesc_zona3 = '" + mosdato.xdescZona3 + "' "
                + "WHERE cod_cliente = " + mosdato.codCliente;
            System.out.println(sql);
            stmt.execute(sql);
        }
        
    }

    private void createTableCurclit(String discriminar, Statement stmt) throws SQLException {
        String sql;
        if("ND".equals(discriminar)){
            sql = "SELECT "
                    + "    convert(char(2),'') as cod_zona, convert(varchar(50),'') as xdesc_zona, cod_cliente, xnombre, nplazo_credito, "
                    + "	ctipo_docum, ndocum, fdocum, fvenc, ideuda, xdesc_zona2, xdesc_zona3 "
                    + "INTO #mosdatos "
                    + "FROM #mostrar "
                    + "WHERE ideuda != 0 "
                    + "ORDER BY  xnombre, cod_cliente, fdocum, ctipo_docum, ndocum ";
        } else {
            sql = "SELECT "
                    + "    cod_zona, xdesc_zona, cod_cliente, xnombre, nplazo_credito, "
                    + "	ctipo_docum, ndocum, fdocum, fvenc, ideuda, xdesc_zona2, xdesc_zona3 "
                    + "INTO #mosdatos "
                    + "FROM #mostrar "
                    + "WHERE ideuda != 0 "
                    + "ORDER BY cod_zona, xnombre, cod_cliente, fdocum, ctipo_docum, ndocum ";
        }
        System.out.println(sql);
        stmt.execute(sql);
        sql = "SELECT DISTINCT cod_cliente "
            + "INTO #curclit "
            + "FROM #mosdatos ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void createTableMostrar(String tdoc, Date fechaVencimiento,
            Date fechaHasta, Integer plazoCreditoDesde, Integer plazoCreditoHasta,
            Zonas zona, Empleados vendedor, Boolean todosClientes,
            List<Clientes> listadoClientesSeleccionados, Statement stmt) throws SQLException {
        String sql = "";
        if ("FCR".equals(tdoc)){
            sql = "SELECT "
                    + "    f.cod_zona, z.xdesc AS xdesc_zona, t.cod_cliente, c.fac_ctipo_docum AS ctipo_docum, "
                    + "    CAST(c.nrofact AS char(15)) AS ndocum, t.xnombre, t.nplazo_credito, f.ffactur AS fdocum, "
                    + "    c.fvenc, z.cod_zona AS cod_zona2, z.xdesc AS xdesc_zona2, z3.xdesc AS xdesc_zona3, "
                    + "    SUM((c.texentas + c.tgravadas + c.timpuestos) * c.mindice) - SUM(c.ipagado) AS ideuda "
                    + "INTO #mostrar "
                    + "FROM zonas z INNER JOIN facturas f "
                    + "        ON z.cod_zona = f.cod_zona AND z.cod_empr = f.cod_empr "
                    + "    RIGHT OUTER JOIN rutas r INNER JOIN clientes t "
                    + "        ON r.cod_ruta = t.cod_ruta AND r.cod_empr = t.cod_empr "
                    + "    INNER JOIN zonas z3 "
                    + "        ON r.cod_zona = z3.cod_zona AND r.cod_empr = z3.cod_empr "
                    + "    INNER JOIN cuentas_corrientes c "
                    + "        ON r.cod_empr = c.cod_empr AND t.cod_cliente = c.cod_cliente "
                    + "        ON f.ctipo_docum = c.fac_ctipo_docum AND f.nrofact = c.nrofact "
                    + "        AND f.ffactur = c.ffactur AND f.cod_empr = c.cod_empr "
                    + "WHERE (c.fvenc <= '" + DateUtil.dateToString(fechaVencimiento, DATE_FORMAT) + "') "
                    + "AND (c.cod_empr = 2) "
                    + "AND (c.fmovim <= '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                    + "AND (f.cod_empr = 2) "
                    + "AND (c.fac_ctipo_docum = 'FCR') "
                    + "AND  (t.nplazo_credito BETWEEN " + plazoCreditoDesde + " AND " + plazoCreditoHasta + ") "
                    + "AND (f.cod_empr = 2) "
                    + "AND c.fmovim >= '01/09/2005' ";
            if (Objects.nonNull(zona)) {
                sql += " AND z3.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
            }
            
            if (Objects.nonNull(vendedor)) {
                sql += " AND f.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado() + " ";
            }
            
            if (!todosClientes) {
                if (!listadoClientesSeleccionados.isEmpty()) {
                    sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
                }
            }
            sql  += " GROUP BY f.cod_zona, z.xdesc, t.cod_cliente, c.fac_ctipo_docum, "
                    + "c.nrofact, t.xnombre, t.nplazo_credito, f.ffactur, c.fvenc, "
                    + "z.cod_zona, z.xdesc, z3.xdesc ";
        }
        if ("CHQ".equals(tdoc)){
            sql = "SELECT "
                    + "    r.cod_zona, z.xdesc as xdesc_zona, t.cod_cliente, "
                    + "    '' as ctipo_docum , ndocum_cheq as ndocum, "
                    + "    t.xnombre, t.nplazo_credito, h.femision as fdocum, "
                    + "    c.fvenc, z.cod_zona as cod_zona2, z.xdesc as xdesc_zona2, "
                    + "    z.xdesc as xdesc_zona3, SUM(c.ipagado * c.mindice) as ideuda "
                    + "INTO #mostrar "
                    + "FROM cuentas_corrientes c, clientes t, zonas z, cheques h, rutas r "
                    + "WHERE c.fvenc <= '" + DateUtil.dateToString(fechaVencimiento, DATE_FORMAT) + "' "
                    + "AND c.cod_empr = 2 "
                    + "AND c.fmovim <= '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' "
                    + "AND c.fmovim >= '01/09/2005' "
                    + "AND c.cod_empr = h.cod_empr "
                    + "AND t.cod_ruta = r.cod_ruta "
                    + "AND t.cod_empr = r.cod_empr "
                    + "AND r.cod_empr = z.cod_empr "
                    + "AND r.cod_zona  = z.cod_zona "
                    + "AND c.cod_empr = r.cod_empr "
                    + "AND c.cod_cliente = t.cod_cliente "
                    + "AND c.cod_cliente = h.cod_cliente "
                    + "AND c.ndocum_cheq = h.nro_cheque "
                    + "AND ctipo_docum IN ('CHQ','CHC') "
                    + "AND (t.nplazo_credito BETWEEN " + plazoCreditoDesde + " AND " + plazoCreditoHasta + ") ";
            if (Objects.nonNull(zona)) {
                sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
            }
            
            if (!todosClientes) {
                if (!listadoClientesSeleccionados.isEmpty()) {
                    sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
                }
            }
            sql += "GROUP BY r.cod_zona, z.xdesc, t.cod_cliente, c.ndocum_cheq, t.xnombre, "+
                    "t.nplazo_credito, h.femision, c.fvenc, z.cod_zona, z.xdesc ";
        }
        if ("PAG".equals(tdoc)){
            sql = "SELECT "
                    + "    r.cod_zona, z.xdesc as xdesc_zona, z.xdesc as xdesc_zona3, "
                    + "    t.cod_cliente, 'PAG' AS ctipo_docum , ndocum_cheq as ndocum, "
                    + "    t.xnombre, t.nplazo_credito, h.femision as fdocum, c.fvenc, "
                    + "    z.cod_zona as cod_zona2, z.xdesc as xdesc_zona2, SUM(c.ipagado * c.mindice) as ideuda "
                    + "INTO #mostrar "
                    + "FROM cuentas_corrientes c, clientes t, zonas z, pagares h, rutas r "
                    + "WHERE c.fvenc <= '" + DateUtil.dateToString(fechaVencimiento, DATE_FORMAT) + "' "
                    + "AND c.fmovim <= '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' "
                    + "AND c.cod_empr = 2 "
                    + "AND h.cod_empr = 2 "
                    + "AND c.cod_empr = h.cod_empr "
                    + "AND t.cod_ruta = r.cod_ruta "
                    + "AND r.cod_zona = z.cod_zona "
                    + "AND c.cod_cliente = t.cod_cliente "
                    + "AND c.cod_cliente = h.cod_cliente "
                    + "AND c.ndocum_cheq = CAST(h.npagare AS CHAR) "
                    + "AND ctipo_docum IN ('PAG','PAC') "
                    + "AND (t.nplazo_credito BETWEEN " + plazoCreditoDesde + " AND " + plazoCreditoHasta + ") ";
            if (Objects.nonNull(zona)) {
                sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
            }
            
            if (!todosClientes) {
                if (!listadoClientesSeleccionados.isEmpty()) {
                    sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
                }
            }
            sql += "GROUP BY r.cod_zona, z.xdesc, t.cod_cliente, c.ndocum_cheq, t.xnombre, "
                    + "t.nplazo_credito, h.femision, c.fvenc, z.cod_zona ";
        }
        if ("".equals(tdoc)){
            sql = "SELECT t.* "
                    + "INTO #mostrar "
                    + "FROM "
                    + "(SELECT "
                    + "    f.cod_zona, z.xdesc AS xdesc_zona, t.cod_cliente, c.fac_ctipo_docum AS ctipo_docum, "
                    + "    CAST(c.nrofact AS char(15)) AS ndocum, t.xnombre, t.nplazo_credito, f.ffactur AS fdocum, "
                    + "    c.fvenc, z.cod_zona AS cod_zona2, z.xdesc AS xdesc_zona2, z3.xdesc AS xdesc_zona3, "
                    + "    SUM((c.texentas + c.tgravadas + c.timpuestos) * c.mindice) - SUM(c.ipagado) AS ideuda "
                    + "FROM zonas z INNER JOIN facturas f "
                    + "        ON z.cod_zona = f.cod_zona AND z.cod_empr = f.cod_empr "
                    + "    RIGHT OUTER JOIN rutas r INNER JOIN clientes t "
                    + "        ON r.cod_ruta = t.cod_ruta AND r.cod_empr = t.cod_empr "
                    + "    INNER JOIN zonas z3 "
                    + "        ON r.cod_zona = z3.cod_zona AND r.cod_empr = z3.cod_empr "
                    + "    INNER JOIN cuentas_corrientes c "
                    + "        ON r.cod_empr = c.cod_empr AND t.cod_cliente = c.cod_cliente "
                    + "        ON f.ctipo_docum = c.fac_ctipo_docum AND f.nrofact = c.nrofact "
                    + "        AND f.ffactur = c.ffactur AND f.cod_empr = c.cod_empr "
                    + "WHERE (c.fvenc <= '" + DateUtil.dateToString(fechaVencimiento, DATE_FORMAT) + "') "
                    + "AND (c.cod_empr = 2) "
                    + "AND (c.fmovim <= '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') "
                    + "AND (f.cod_empr = 2) "
                    + "AND (c.fac_ctipo_docum = 'FCR') "
                    + "AND  (t.nplazo_credito BETWEEN " + plazoCreditoDesde + " AND " + plazoCreditoHasta + ") "
                    + "AND (f.cod_empr = 2) "
                    + "AND c.fmovim >= '01/09/2005' ";
            if (Objects.nonNull(zona)) {
                sql += " AND z3.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
            }
            
            if (Objects.nonNull(vendedor)) {
                sql += " AND f.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado() + " ";
            }
            
            if (!todosClientes) {
                if (!listadoClientesSeleccionados.isEmpty()) {
                    sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
                }
            }
            sql  += " GROUP BY f.cod_zona, z.xdesc, t.cod_cliente, c.fac_ctipo_docum, "
                    + "c.nrofact, t.xnombre, t.nplazo_credito, f.ffactur, c.fvenc, "
                    + "z.cod_zona, z.xdesc, z3.xdesc "
                    + "UNION ALL "
                    + "SELECT "
                    + "    r.cod_zona, z.xdesc as xdesc_zona, t.cod_cliente, "
                    + "    CAST('' AS char(3)) as ctipo_docum , ndocum_cheq as ndocum, "
                    + "    t.xnombre, t.nplazo_credito, h.femision as fdocum, "
                    + "    c.fvenc, z.cod_zona as cod_zona2, z.xdesc as xdesc_zona2, "
                    + "    z.xdesc as xdesc_zona3, SUM(c.ipagado * c.mindice) as ideuda "
                    + "FROM cuentas_corrientes c, clientes t, zonas z, cheques h, rutas r "
                    + "WHERE c.fvenc <= '" + DateUtil.dateToString(fechaVencimiento, DATE_FORMAT) + "' "
                    + "AND c.cod_empr = 2 "
                    + "AND c.fmovim <= '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' "
                    + "AND c.fmovim >= '01/09/2005' "
                    + "AND c.cod_empr = h.cod_empr "
                    + "AND t.cod_ruta = r.cod_ruta "
                    + "AND t.cod_empr = r.cod_empr "
                    + "AND r.cod_empr = z.cod_empr "
                    + "AND r.cod_zona  = z.cod_zona "
                    + "AND c.cod_empr = r.cod_empr "
                    + "AND c.cod_cliente = t.cod_cliente "
                    + "AND c.cod_cliente = h.cod_cliente "
                    + "AND c.ndocum_cheq = h.nro_cheque "
                    + "AND ctipo_docum IN ('CHQ','CHC') "
                    + "AND (t.nplazo_credito BETWEEN " + plazoCreditoDesde + " AND " + plazoCreditoHasta + ") ";
            if (Objects.nonNull(zona)) {
                sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
            }
            
            if (!todosClientes) {
                if (!listadoClientesSeleccionados.isEmpty()) {
                    sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
                }
            }
            sql += "GROUP BY r.cod_zona, z.xdesc, t.cod_cliente, c.ndocum_cheq, t.xnombre, "+
                    "t.nplazo_credito, h.femision, c.fvenc, z.cod_zona, z.xdesc "
                    + "UNION ALL "
                    + "SELECT "
                    + "    r.cod_zona, z.xdesc as xdesc_zona, t.cod_cliente, "
                    + "    CAST('PAG' AS char(3)) AS ctipo_docum, ndocum_cheq as ndocum, "
                    + "    t.xnombre, t.nplazo_credito, h.femision as fdocum, c.fvenc, "
                    + "    z.cod_zona as cod_zona2, z.xdesc as xdesc_zona2, z.xdesc as xdesc_zona3, SUM(c.ipagado * c.mindice) as ideuda "
                    + "FROM cuentas_corrientes c, clientes t, zonas z, pagares h, rutas r "
                    + "WHERE c.fvenc <= '" + DateUtil.dateToString(fechaVencimiento, DATE_FORMAT) + "' "
                    + "AND c.fmovim <= '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' "
                    + "AND c.cod_empr = 2 "
                    + "AND h.cod_empr = 2 "
                    + "AND c.cod_empr = h.cod_empr "
                    + "AND t.cod_ruta = r.cod_ruta "
                    + "AND r.cod_zona = z.cod_zona "
                    + "AND c.cod_cliente = t.cod_cliente "
                    + "AND c.cod_cliente = h.cod_cliente "
                    + "AND c.ndocum_cheq = CAST(h.npagare AS CHAR) "
                    + "AND ctipo_docum IN ('PAG','PAC') "
                    + "AND (t.nplazo_credito BETWEEN " + plazoCreditoDesde + " AND " + plazoCreditoHasta + ") ";
            if (Objects.nonNull(zona)) {
                sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
            }
            
            if (!todosClientes) {
                if (!listadoClientesSeleccionados.isEmpty()) {
                    sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
                }
            }
            sql += "GROUP BY r.cod_zona, z.xdesc, t.cod_cliente, c.ndocum_cheq, t.xnombre, "
                    + "t.nplazo_credito, h.femision, c.fvenc, z.cod_zona) t ";
        }
        System.out.println(sql);
        stmt.execute(sql);
    }
    
    public String generateSelectFinal() {
        return "SELECT "
                + "cod_cliente,"
                + "isaldo_inicial,"
                + "cod_zona,"
                + "xdesc_zona,"
                + "xnombre,"
                + "nplazo_credito,"
                + "ctipo_docum,"
                + "ndocum,"
                + "fdocum,"
                + "fvenc,"
                + "ideuda,"
                + "xdesc_zona2,"
                + "xdesc_zona3"
                + "FROM #final ";
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
            Logger.getLogger(FacturasVencidasFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
