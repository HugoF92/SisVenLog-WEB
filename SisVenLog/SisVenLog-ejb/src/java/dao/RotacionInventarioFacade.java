package dao;

import entidad.Depositos;
import entidad.Divisiones;
import entidad.Mercaderias;
import entidad.Sublineas;
import java.sql.CallableStatement;
import java.sql.Connection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.ejb.Stateless;
import util.DateUtil;

/**
 *
 * @author Arsenio
 */
@Stateless
public class RotacionInventarioFacade {
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    //TODO: cambiar fecha para entorno local yyyy/dd/MM y para el server
    // de prueba yyyy/MM/dd
    static final private String DATE_FORMAT = "yyyy/dd/MM";
    
    public void generateTableMostrar(Connection conexion, Date fechaDesde,
            Date fechaHasta, Depositos deposito, Sublineas sublinea,
            Divisiones division, String discriminar, Boolean conPrecioCosto) throws SQLException {
        Statement stmt = conexion.createStatement();
        this.createTempTableCurImp(stmt, fechaDesde, fechaHasta);
        this.createTempTableMercasel(stmt);
        this.recreateExistencia2(stmt, fechaHasta, deposito);
        this.calculateVentas(stmt, fechaDesde, fechaHasta, sublinea, division,
                conPrecioCosto);
        this.calculateStockValorizado(stmt, fechaHasta, conPrecioCosto);
        this.juntarTablasTemp(stmt);
        this.crearTableMostrar(stmt, discriminar);
    }

    private void createTempTableCurImp(Statement stmt, Date desde, Date hasta) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        // l_finicial = '01/01/'+STR(YEAR(Thisform.ffINAL.Value),4)
        calendar.setTime(desde != null? desde: new Date());
        calendar.set(calendar.get(Calendar.YEAR), 1, 1);
        Date finicial = calendar.getTime();
        // l_ffinal = '31/12/'+STR(YEAR(Thisform.ffINAL.Value),4)
        calendar.setTime(hasta != null? hasta: new Date());
        calendar.set(calendar.get(Calendar.YEAR), 12, 31);
        Date ffinal = calendar.getTime();

        String sql = "SELECT DISTINCT t.cod_merca, d.pimpues "
                + "INTO #curimp "
                + "FROM tmp_mercaderias t, facturas_det d, facturas f "
                + "WHERE f.cod_empr = 2 "
                + "AND d.cod_empr = 2 "
                + "AND t.cod_merca = d.cod_merca "
                + "AND d.nrofact = f.nrofact "
                + "AND d.ctipo_docum = f.ctipo_docum "
                + "AND d.cod_empr = f.cod_empr "
                + "AND f.ffactur BETWEEN '" + DateUtil.dateToString(finicial, DATE_FORMAT)
                + "' AND '" + DateUtil.dateToString(ffinal, DATE_FORMAT) + "' ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void createTempTableMercasel(Statement stmt) throws SQLException {
        stmt.execute("IF OBJECT_ID('dbo.mercasel', 'U') IS NOT NULL DROP TABLE dbo.mercasel;");
        stmt.execute("CREATE TABLE mercasel ( "
                    + "cod_merca CHAR(13), "
                    + "xdesc CHAR(50), "
//                    + "cajas_mov integer, "
                    + "cant_cajas integer, "
//                    + "iprecio_caja integer, "
//                    + "unid_mov integer,"
                    + "cant_unid integer, "
//                    + "iprecio_unid integer,"
//                    + "ttotal integer,"
//                    + "tcosto integer,"
                    + "pimpues decimal(12,2), "
                    + "mgrav_exe CHAR(1)"
                    + ")");
        String sql = "INSERT INTO mercasel (cod_merca, xdesc, cant_cajas, cant_unid, pimpues, mgrav_exe) "
                    + "SELECT tm.cod_merca, tm.xdesc, tm.cant_cajas, tm.cant_unid, "
                    + "CASE "
                    + "	WHEN ci.pimpues != 10 AND ci.pimpues != 5 "
                    + "	THEN 10 "
                    + "	ELSE ci.pimpues "
                    + "END as pimpues, "
                    + "tm.mgrav_exe "
                    + "FROM tmp_mercaderias tm, #curimp ci "
                    + "WHERE tm.cod_merca = ci.cod_merca "
                    + "ORDER BY tm.cod_merca ";
        System.out.println(sql);
        stmt.execute(sql);
    }
    
    public void insertarMercaderiasSeleccionadas(Statement stmt,
            List<Mercaderias> mercaderias, List<Mercaderias> mercaderiasActivas) throws SQLException {
        stmt.execute("IF OBJECT_ID('dbo.tmp_mercaderias', 'U') IS NOT NULL DROP TABLE tmp_mercaderias");
        stmt.execute("CREATE TABLE tmp_mercaderias ( "
                    + "cod_merca CHAR(13), "
                    + "cod_barra CHAR(20), "
                    + "xdesc CHAR(50), "
                    + "nrelacion SMALLINT, "
                    + "cant_cajas integer, "
                    + "cant_unid integer, "
                    + "mgrav_exe CHAR(1)"
                    + ")");
        if (mercaderias.size() > 0) {
            Mercaderias aux;
            for (Mercaderias m : mercaderias) {
                aux = mercaderiasActivas.stream()
                        .filter(ma -> ma.equals(m))
                        .findAny()
                        .orElse(null);
                if(Objects.nonNull(aux)){
                    stmt.execute("INSERT INTO tmp_mercaderias (cod_merca, "
                        + "cod_barra, xdesc, nrelacion,cant_cajas, cant_unid, mgrav_exe ) "
                        + "VALUES ('" + aux.getMercaderiasPK().getCodMerca()
                        + "', '" + aux.getCodBarra() + "', '" + aux.getXdesc()
                        + "', " + aux.getNrelacion() + ",0,0, '" + aux.getMgravExe()+"' )");
                }
            }
        } else {
            stmt.execute("INSERT INTO tmp_mercaderias "
                    + "SELECT m.cod_merca, m.cod_barra, m.xdesc, m.nrelacion, "
                    + "1 as cant_cajas, 1 as cant_unid, m.mgrav_exe "
                    + "FROM mercaderias m, existencias e "
                    + "WHERE m.cod_merca = e.cod_merca "
                    + "AND m.mestado = 'A' "
                    + "AND e.cod_depo = 1");
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
            Logger.getLogger(RotacionInventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    private void calculateVentas(Statement stmt, Date fechaDesde, Date fechaHasta,
            Sublineas sublinea, Divisiones division, Boolean conPrecioCosto) throws SQLException {
//        Calculamos la tabla Totcur
        String sql = "SELECT "
                    + "    m.fmovim, m.cod_merca, e.nrelacion, e.cod_sublinea, e.xdesc, g.cod_division, "
                    + "    ISNULL(SUM(m.cant_cajas*-1),0) as cajas_mov, "
                    + "    ISNULL(SUM(m.cant_unid*-1),0) as unid_mov "
                    + "INTO #totcur "
                    + "FROM movimientos_merca m, mercaderias e, tmp_mercaderias t, sublineas s, lineas l, categorias g "
                    + "WHERE m.cod_empr = 2 "
                    + "AND e.cod_empr = 2 "
                    + "AND fmovim BETWEEN '" +  DateUtil.dateToString(fechaDesde, DATE_FORMAT)
                    + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' "
                    + "AND m.cod_merca = e.cod_merca "
                    + "AND m.cod_empr = e.cod_empr "
                    + "AND m.cod_merca = t.cod_merca "
                    + "AND e.cod_sublinea = s.cod_sublinea "
                    + "AND s.cod_linea = l.cod_linea "
                    + "AND l.cod_categoria = g.cod_categoria "
                    + "AND m.ctipo_docum IN ('CPV','FCR','FCO','NCV','NDV') ";
        if (Objects.nonNull(division)) {
            sql += "AND g.cod_division = " + division.getCodDivision() + " ";
        }
        if (Objects.nonNull(sublinea)) {
            sql += "AND e.cod_sublinea = " + sublinea.getCodSublinea() + " ";
        }
        sql += "GROUP BY m.fmovim, m.cod_merca, e.nrelacion, e.cod_sublinea, e.xdesc, g.cod_division ";
        System.out.println(sql);
        stmt.execute(sql);
//        Creamos una nueva tabla mercasel_totcu segun la logica del programa
        // columnas: cod_merca, xdesc, cajas_mov, unid_mov, cod_sublinea, cod_division
        // tcosto
        sql = "SELECT "
//            + "-- en caso que no exista el registro de totcur en mercasel "
            + "ISNULL(ms.cod_merca, tc.cod_merca) as cod_merca,  "
                // revisar por que no le gusta xdesc
//            + "ISNULL(ms.xdesc, tc.xdesc) as xdesc, "
            + "tc.cajas_mov, tc.unid_mov, tc.cod_sublinea, tc.cod_division, "
            + "((tc.cajas_mov * ISNULL(curppp.nppp_caja, 0))+(tc.unid_mov * ISNULL(curppp.nprecio_ppp, 0))) as tcosto "
            + "INTO #mercasel_totcur "
            + "FROM #totcur tc "
            + "	LEFT JOIN mercasel ms "
            + "		ON tc.cod_merca = ms.cod_merca ";
        if(!conPrecioCosto) {
            sql += "	LEFT JOIN ( "
            + "		SELECT cod_merca, fppp, nprecio_ppp, nppp_caja FROM ppp "
            + "		WHERE cod_empr = 2 "
            + "	) AS curppp "
            + "		ON tc.cod_merca = curppp.cod_merca "
            + "WHERE "
//            + "-- Para ambos casos  de las tablas curppp "
            + "curppp.fppp <= tc.fmovim ";
            
        } else {
            sql += "	LEFT JOIN ( "
            + "			SELECT cod_merca, frige_desde as fppp, frige_hasta,"
            + "                 iprecio_unidad as nprecio_ppp, iprecio_caja as nppp_caja"
            + "                 FROM precios "
            + "			WHERE cod_empr = 2 "
            + "			AND ctipo_vta = 'X' "
            + "			AND cod_depo = 1 "
            + "		) AS curppp "
            + "			ON tc.cod_merca = curppp.cod_merca "
            + "WHERE "
//            + "-- Para ambos casos  de las tablas curppp "
            + "curppp.fppp <= tc.fmovim "
            + "AND (curppp.frige_hasta is null OR curppp.frige_hasta >= '"
            + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "') ";
        }
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void calculateStockValorizado(Statement stmt, Date fechaHasta, Boolean conPrecioCosto) throws SQLException {
        // Primero creamos la tabla impucur
        String sql = "SELECT "
                + "    SUM(b.ifijo) as l_ifijo,  "
                + "    SUM(b.pimpues/100) as l_pimpues, "
                + "    a.cod_merca "
                + "INTO #impucur "
                + "FROM merca_impuestos a, impuestos b   "
                + "WHERE  a.cod_empr = 2 "
                + "AND a.cod_impu = b.cod_impu "
                + "GROUP BY a.cod_merca ";
        System.out.println(sql);
        stmt.execute(sql);
        // luego creamos la tabla temporal mercasel_tot
        String l_ffinal = DateUtil.dateToString(fechaHasta, DATE_FORMAT);
        // impuestos, pimpues, ttotal, iprecio_unid, iprecio_caja
        sql = "SELECT "
            + "ms.cod_merca, "
            + "CASE "
            + "    WHEN ms.mgrav_exe = 'G' "
            + "    THEN "
            + "        CASE "
            + "            WHEN ms.pimpues = 0 "
            + "            THEN "
            + "                CASE "
            + "                    WHEN curppp.nprecio_ppp> 0 "
            + "                    THEN (curppp.nprecio_ppp - ((curppp.nprecio_ppp / (1 + ic.l_pimpues)) + ic.l_ifijo)) "
//            + "                    -- l_importe = a esta funcion sin iva "
            + "                    ELSE 0 "
            + "                END "
            + "            ELSE ISNULL(curppp.nprecio_ppp, 0) - (ISNULL(curppp.nprecio_ppp, 0) / (1+(ms.pimpues/100))) "
            + "        END "
            + "    ELSE 0 "
            + "END as impuestos, "
            + "CASE "
            + "    WHEN ms.mgrav_exe = 'G' "
            + "    THEN "
            + "        CASE "
            + "            WHEN ms.pimpues = 0 "
            + "            THEN "
            + "                CASE "
            + "                    WHEN curppp.nprecio_ppp> 0 "
            + "                    THEN ROUND(ABS((curppp.nprecio_ppp - ((curppp.nprecio_ppp / (1 + ic.l_pimpues)) + ic.l_ifijo)))/(curppp.nprecio_ppp - (curppp.nprecio_ppp - ((curppp.nprecio_ppp / (1 + ic.l_pimpues)) + ic.l_ifijo))) ,2) * 100 "
//            + "                    -- l_impuesto = l_importe "
//            + "                    -- l_pimpues = ROUND(ABS(l_impuesto)/(mercasel->iprecio_unid - l_impuesto) ,2) "
//            + "                    -- en el programa se realiza REPLACE  pimpues WITH l_pimpues * 100 "
            + "                    ELSE 1 "
            + "                END "
            + "            ELSE 0 "
            + "        END "
            + "    ELSE 0 "
            + "END as pimpues, "
            + "((mm.cant_cajas * ISNULL(curppp.nppp_caja, 0))+(mm.cant_unid * ISNULL(curppp.nprecio_ppp, 0))) as ttotal, "
            + "ISNULL(curppp.nprecio_ppp, 0) as iprecio_unid, ISNULL(curppp.nppp_caja,0) as iprecio_caja "
            + "INTO #mercasel_costo "
            + "FROM mercasel ms "
            + "	INNER JOIN movimientos_merca mm  "
            + "		ON ms.cod_merca = mm.cod_merca "
            + "	INNER JOIN #impucur ic "
            + "		ON ms.cod_merca = ic.cod_merca ";
        if(!conPrecioCosto) {
            sql += "	LEFT JOIN ( "
                + "		SELECT cod_merca, fppp, nprecio_ppp, nppp_caja FROM ppp "
                + "		WHERE cod_empr = 2 "
                + "	) AS curppp "
                + "		ON ms.cod_merca = curppp.cod_merca "
                + "WHERE "
//                + "-- Para ambos casos  de las tablas curppp "
                + "curppp.fppp <= '" + l_ffinal + "' ";
        } else {
            sql += "	LEFT JOIN ( "
                + "			SELECT cod_merca, frige_desde as fppp, frige_hasta, iprecio_unidad as nprecio_ppp, iprecio_caja as nppp_caja FROM precios "
                + "			WHERE cod_empr = 2 "
                + "			AND ctipo_vta = 'X' "
                + "			AND cod_depo = 1 "
                + "		) AS curppp "
                + "			ON ms.cod_merca = curppp.cod_merca "
                + "WHERE "
//                + "-- Para ambos casos  de las tablas curppp "
                + "curppp.fppp <= '" + l_ffinal + "' "
                + "AND (curppp.frige_hasta is null OR curppp.frige_hasta >= '" + l_ffinal + "') ";
        }
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void recreateExistencia2(Statement stmt, Date fechaHasta, Depositos deposito) throws SQLException {
        // TODO: ajustar con las tablas que tenes
        String query = "select * from calc_exist_deposito('"+DateUtil.dateToString(fechaHasta, DATE_FORMAT)+"', "
                +(deposito != null ? deposito.getDepositosPK().getCodDepo() : null)+")";
        stmt.execute(query);
    }

    private void crearTableMostrar(Statement stmt, String discriminar) throws SQLException {
        String sql = "";
        // Por Mercaderia
        if("PM".equals(discriminar)) {
            sql = "SELECT "
                + "    cod_merca, xdesc, cajas_mov, cant_cajas, iprecio_caja,"
                + "    cod_sublinea, cod_division, unid_mov, cant_unid, iprecio_unid, tcosto, "
                + "    ttotal, (30/(tcosto/ttotal)) as ndias "
                + "INTO #mostrar "
                + "FROM #mercaselec "
                + "WHERE cant_cajas > 0 "
                + "OR cant_unid > 0 ";
        }
        if("PS".equals(discriminar)) {
            sql = "SELECT "
                + "    m.cod_merca, s.xdesc, SUM(m.cajas_mov) as cajas_mov, "
                + "    SUM(m.cant_cajas) as cant_cajas, m.iprecio_caja, s.cod_sublinea, "
                + "    m.cod_division, SUM(m.unid_mov) as unid_mov, SUM(m.cant_unid) as cant_unid, "
                + "    m.iprecio_unid, SUM(m.tcosto) as tcosto, "
                + "    SUM((m.cant_cajas * m.iprecio_caja) + (m.cant_unid * m.iprecio_unid)) as ttotal,  "
                + "    (30/(SUM(m.tcosto)/SUM((m.cant_cajas * m.iprecio_caja) + (m.cant_unid * m.iprecio_unid)))) as ndias "
                + "INTO #mostrar "
                + "FROM #mercaselec m, sublineas s "
                + "WHERE m.cod_sublinea = s.cod_sublinea "
                + "AND (m.cant_cajas > 0 OR m.cant_unid > 0) "
                + "GROUP BY m.cod_merca, s.cod_sublinea, s.xdesc, m.iprecio_caja, s.cod_sublinea, m.cod_division, m.iprecio_unid "
                + "ORDER BY s.cod_sublinea ";
        }
        if("PD".equals(discriminar)) {
            sql = "SELECT "
                + "    m.cod_merca, d.xdesc, "
                + "    SUM(m.cajas_mov) as cajas_mov, SUM(m.cant_cajas) as cant_cajas, "
                + "    m.iprecio_caja, d.cod_division as cod_sublinea, d.cod_division,"
                + "    SUM(m.unid_mov) as unid_mov, SUM(m.cant_unid) as cant_unid, "
                + "    m.iprecio_unid, SUM(m.tcosto) as tcosto,  "
                + "    SUM((m.cant_cajas * m.iprecio_caja) + (m.cant_unid * m.iprecio_unid)) as ttotal, "
                + "    (30/(SUM(m.tcosto)/SUM((m.cant_cajas * m.iprecio_caja) + (m.cant_unid * m.iprecio_unid)))) as ndias "
                + "INTO #mostrar "
                + "FROM #mercaselec m, divisiones d "
                + "WHERE m.cod_division = d.cod_division "
                + "AND (m.cant_cajas > 0 OR m.cant_unid > 0) "
                + "GROUP BY m.cod_merca, d.cod_division, d.xdesc, m.iprecio_caja, d.cod_division, m.iprecio_unid ";
            
        }
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void juntarTablasTemp(Statement stmt) throws SQLException {
        String sql = "SELECT "
                + "    m.cod_merca, m.xdesc, mt.cajas_mov, m.cant_cajas, "
                + "    mc.iprecio_caja, mt.cod_sublinea, mt.cod_division, mt.unid_mov, "
                + "    m.cant_unid, mc.iprecio_unid, mt.tcosto, mc.ttotal "
                + "INTO #mercaselec "
                + "FROM mercasel m "
                + "    INNER JOIN #mercasel_totcur mt "
                + "        ON m.cod_merca = mt.cod_merca "
                + "    INNER JOIN #mercasel_costo mc "
                + "        ON m.cod_merca = mc.cod_merca ";
        System.out.println(sql);
        stmt.execute(sql);
    }

    public String generateSelectMostrar() {
        return "SELECT "
                + "    cod_merca, xdesc, cajas_mov, cant_cajas, "
                + "    iprecio_caja, cod_sublinea, cod_division, unid_mov, "
                + "    cant_unid, iprecio_unid, tcosto, ttotal, ndias "
                + "FROM #mostrar";
    }
    
}
