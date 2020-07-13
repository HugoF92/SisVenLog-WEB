package dao;

import entidad.Depositos;
import entidad.Divisiones;
import entidad.Mercaderias;
import entidad.Sublineas;
import java.sql.Connection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.Query;
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
    static final private String DATE_FORMAT = "yyyy/MM/dd";
    
    public void generateTableMostrar(Connection conexion, Date fechaDesde,
            Date fechaHasta, Depositos deposito, Sublineas sublinea,
            Divisiones division, String discriminar, Boolean conPrecioCosto) throws SQLException {
        Statement stmt = conexion.createStatement();
        this.createTempTableCurImp(stmt, fechaDesde, fechaHasta);
        this.createTempTableMercasel(stmt);
        //this.recreateExistencia2(stmt);
        this.calculateVentas(stmt, fechaDesde, fechaHasta, sublinea, division,
                conPrecioCosto);
        this.calculateStockValorizado(stmt, fechaHasta, conPrecioCosto);
        this.crearTableMostrar(stmt, discriminar);
    }

    private void createTempTableCurImp(Statement stmt, Date desde, Date hasta) throws SQLException {
        // l_finicial = '01/01/'+STR(YEAR(Thisform.ffINAL.Value),4)
        Date from = desde != null? desde: new Date();
        //default time zone
	ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate f = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date finicial = Date.from(LocalDate.of(f.getYear(), Month.JANUARY, 1).atStartOfDay(defaultZoneId).toInstant());
        // l_ffinal = '31/12/'+STR(YEAR(Thisform.ffINAL.Value),4)
        Date to = hasta != null? hasta: new Date();
        LocalDate t = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date ffinal = Date.from(LocalDate.of(t.getYear(), Month.DECEMBER, 31).atStartOfDay(defaultZoneId).toInstant());

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
        String sql = "IF OBJECT_ID('dbo.mercasel', 'U') IS NOT NULL DROP TABLE dbo.mercasel";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "CREATE TABLE mercasel ( "
                    + "cod_merca CHAR(13), "
                    + "xdesc CHAR(50), "
                    + "cajas_mov integer, "
                    + "cant_cajas integer, "
                    + "iprecio_caja integer, "
                    + "unid_mov integer,"
                    + "cant_unid integer, "
                    + "iprecio_unid integer,"
                    + "ttotal integer,"
                    + "tcosto integer,"
                    + "pimpues decimal(12,2), "
                    + "mgrav_exe CHAR(1),"
                    + "cod_sublinea integer,"
                    + "cod_division integer,"
                    + ")";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "INSERT INTO mercasel (cod_merca, xdesc, cant_cajas, cant_unid, pimpues, mgrav_exe) "
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
        String sql = "IF OBJECT_ID('dbo.tmp_mercaderias', 'U') IS NOT NULL DROP TABLE tmp_mercaderias";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "CREATE TABLE tmp_mercaderias ( "
                    + "cod_merca CHAR(13), "
                    + "cod_barra CHAR(20), "
                    + "xdesc CHAR(50), "
                    + "nrelacion SMALLINT, "
                    + "cant_cajas integer, "
                    + "cant_unid integer, "
                    + "mgrav_exe CHAR(1)"
                    + ")";
        System.out.println(sql);
        stmt.execute(sql);
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
                        + "', '" + aux.getCodBarra() + "', '" + aux.getXdesc().replace("'", "''")
                        + "', " + aux.getNrelacion() + ",0,0, '" + aux.getMgravExe()+"' )");
                }
            }
        } else {
            sql = "INSERT INTO tmp_mercaderias "
                    + "SELECT m.cod_merca, m.cod_barra, m.xdesc, m.nrelacion, "
                    + "1 as cant_cajas, 1 as cant_unid, m.mgrav_exe "
                    + "FROM mercaderias m, existencias e "
                    + "WHERE m.cod_merca = e.cod_merca "
                    + "AND m.mestado = 'A' "
                    + "AND e.cod_depo = 1";
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
            Logger.getLogger(RotacionInventarioFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    private class Totcur {
        private String codMerca;
        private String xdesc;
        private Integer cajasMov;
        private Integer unidMov;
        private Integer codSublinea;
        private Integer codDivision;
        private Date fmovim;
        public Totcur(){}

        public String getCodMerca() {
            return codMerca;
        }

        public void setCodMerca(String codMerca) {
            this.codMerca = codMerca;
        }

        public String getXdesc() {
            return xdesc;
        }

        public void setXdesc(String xdesc) {
            this.xdesc = xdesc;
        }

        public Integer getCajasMov() {
            return cajasMov;
        }

        public void setCajasMov(Integer cajasMov) {
            this.cajasMov = cajasMov;
        }

        public Integer getUnidMov() {
            return unidMov;
        }

        public void setUnidMov(Integer unidMov) {
            this.unidMov = unidMov;
        }

        public Integer getCodSublinea() {
            return codSublinea;
        }

        public void setCodSublinea(Integer codSublinea) {
            this.codSublinea = codSublinea;
        }

        public Integer getCodDivision() {
            return codDivision;
        }

        public void setCodDivision(Integer codDivision) {
            this.codDivision = codDivision;
        }

        public Date getFmovim() {
            return fmovim;
        }

        public void setFmovim(Date fmovim) {
            this.fmovim = fmovim;
        }
        
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
        sql = "SELECT cod_merca, xdesc, cajas_mov, unid_mov, cod_sublinea, cod_division, fmovim FROM #totcur";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        List<Totcur> totcurList = new ArrayList<>();
        while (rs.next()) {
            Totcur totcurItem = new Totcur();
            totcurItem.setCodMerca(rs.getString("cod_merca"));
            totcurItem.setXdesc(rs.getString("xdesc"));
            totcurItem.setCajasMov(rs.getInt("cajas_mov"));
            totcurItem.setUnidMov(rs.getInt("unid_mov"));
            totcurItem.setCodSublinea(rs.getInt("cod_sublinea"));
            totcurItem.setCodDivision(rs.getInt("cod_division"));
            totcurItem.setFmovim(rs.getDate("fmovim"));
            totcurList.add(totcurItem);
        }
        Double l_nprecio_ppp, l_nppp_caja;
        Boolean exist;
        for(Totcur totcur : totcurList){
            // si no existe insertar el registro totcur
            sql = "SELECT COUNT(*) as cant FROM mercasel WHERE cod_merca = '" + totcur.getCodMerca() + "'";
            System.out.println(sql);
            ResultSet res = stmt.executeQuery(sql);
            res.next();
            if(res.getInt("cant") == 0){ // no se encuentra el registro
                sql = "INSERT INTO mercasel (cod_merca, xdesc, tcosto) "
                    + "VALUES('" + totcur.getCodMerca() +"', '" + totcur.getXdesc() +"', 0) ";
                System.out.println(sql);
                stmt.execute(sql);
            }
            sql = "UPDATE mercasel "
                + "SET cajas_mov = (cajas_mov + " + totcur.getCajasMov() + "), "
                + "SET unid_mov = (unid_mov + " + totcur.getUnidMov() + "), "
                + "SET cod_sublinea = " + totcur.getCodSublinea() + ", "
                + "SET cod_division = " + totcur.getCodDivision() + " "
                + "WHERE cod_merca = '" + totcur.getCodMerca() + "'";
            System.out.println(sql);
            stmt.execute(sql);
            // Buscando PPP a la fecha del movimiento
            if(!conPrecioCosto) {
                sql = "SELECT TOP 1 fppp, nprecio_ppp, nppp_caja "
                    + "FROM ppp "
                    + "WHERE cod_merca = '" + totcur.getCodMerca() +"' "
                    + "AND cod_empr = 2 "
                    + "AND fppp <= '" + DateUtil.dateToString(totcur.getFmovim(), DATE_FORMAT) + "' "
                    + "ORDER BY fppp DESC ";
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                exist = rs.next();
                l_nprecio_ppp = exist? rs.getDouble("nprecio_ppp"): 0;
                l_nppp_caja = exist? rs.getDouble("nppp_caja"): 0;
            } else {
                sql = "SELECT "
                    + "  iprecio_caja, iprecio_unidad  "
                    + "FROM precios "
                    + "WHERE cod_merca = '" + totcur.getCodMerca() +"' "
                    + "AND cod_empr = 2 "
                    + "AND frige_desde  <= '" + DateUtil.dateToString(totcur.getFmovim(), DATE_FORMAT) + "' "
                    + "AND ctipo_vta = 'X' "
                    + "AND cod_depo = 1 "
                    + "AND (frige_hasta is null "
                    + "OR frige_hasta >= '" + DateUtil.dateToString(totcur.getFmovim(), DATE_FORMAT) + "') ";
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                rs.next();
                l_nprecio_ppp = rs.getDouble("iprecio_unidad");
                l_nppp_caja = rs.getDouble("iprecio_caja");
            }
            // se actualizan los totales
            sql = "UPDATE mercasel SET tcosto = (tcosto + ((" + totcur.getCajasMov() 
                    + "*" + l_nppp_caja + ") + (" + totcur.getUnidMov() + "*" 
                    + l_nprecio_ppp + "))) ";
            System.out.println(sql);
            getEntityManager().createNativeQuery(sql).executeUpdate();
        }
    }
    
    private class Mercasel {
        private String codMerca;
        private Double pimpues;
        private Integer cantCajas;
        private Double precioCaja;
        private Integer cantUnid;
        private Double precioUnid;
        public Mercasel(){}

        public String getCodMerca() {
            return codMerca;
        }

        public void setCodMerca(String codMerca) {
            this.codMerca = codMerca;
        }

        public Double getPimpues() {
            return pimpues;
        }

        public void setPimpues(Double pimpues) {
            this.pimpues = pimpues;
        }

        public Integer getCantCajas() {
            return cantCajas;
        }

        public void setCantCajas(Integer cantCajas) {
            this.cantCajas = cantCajas;
        }

        public Double getPrecioCaja() {
            return precioCaja;
        }

        public void setPrecioCaja(Double precioCaja) {
            this.precioCaja = precioCaja;
        }

        public Integer getCantUnid() {
            return cantUnid;
        }

        public void setCantUnid(Integer cantUnid) {
            this.cantUnid = cantUnid;
        }

        public Double getPrecioUnid() {
            return precioUnid;
        }

        public void setPrecioUnid(Double precioUnid) {
            this.precioUnid = precioUnid;
        }
    }

    private void calculateStockValorizado(Statement stmt, Date fechaHasta, Boolean conPrecioCosto) throws SQLException {
        String sql = "SELECT "
        + "  cod_merca, pimpues, cant_cajas, iprecio_caja, cant_unid, iprecio_unid "
        + "FROM mercasel ";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        List<Mercasel> mercaselList = new ArrayList<>();
        while (rs.next()) {
            Mercasel m = new Mercasel();
            m.setCodMerca(rs.getString("cod_merca"));
            m.setPimpues(rs.getDouble("pimpues"));
            m.setCantCajas(rs.getInt("cant_cajas"));
            m.setPrecioCaja(rs.getDouble("iprecio_caja"));
            m.setCantUnid(rs.getInt("cant_unid"));
            m.setPrecioUnid(rs.getDouble("iprecio_unid"));
            mercaselList.add(m);
        }
        String l_ffinal = DateUtil.dateToString(fechaHasta, DATE_FORMAT);
        Double l_nprecio_ppp, l_nppp_caja;
        Boolean exist;
        for(Mercasel mercasel: mercaselList) {
            if(!conPrecioCosto) {
                sql = "SELECT TOP 1 fppp, nprecio_ppp, nppp_caja "
                    + "FROM ppp "
                    + "WHERE cod_merca = '" + mercasel.getCodMerca() +"' "
                    + "AND cod_empr = 2 "
                    + "AND fppp <= '" + l_ffinal + "' "
                    + "ORDER BY fppp DESC ";
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                exist = rs.next();
                l_nprecio_ppp = exist? rs.getDouble("nprecio_ppp"): 0;
                l_nppp_caja = exist? rs.getDouble("nppp_caja"): 0;
            } else {
                sql = "SELECT "
                    + "  iprecio_caja, iprecio_unidad  "
                    + "FROM precios "
                    + "WHERE cod_merca = '" + mercasel.getCodMerca() +"' "
                    + "AND cod_empr = 2 "
                    + "AND frige_desde  <= '" + l_ffinal + "' "
                    + "AND ctipo_vta = 'X' "
                    + "AND cod_depo = 1 "
                    + "AND (frige_hasta is null "
                    + "OR frige_hasta >= '" + l_ffinal + "') ";
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                exist = rs.next();
                l_nprecio_ppp = exist? rs.getDouble("iprecio_unidad"): 0;
                l_nppp_caja = exist? rs.getDouble("iprecio_caja"): 0;
            }
            sql = " UPDATE mercasel "
                + " SET iprecio_unid = " + l_nprecio_ppp + ", iprecio_caja = " + l_nppp_caja
                + " WHERE cod_merca = '" + mercasel.getCodMerca() + "'";
            System.out.println(sql);
            getEntityManager().createNativeQuery(sql).executeUpdate();
        }
        // se actualizan los totales
        sql = "UPDATE mercasel SET ttotal = (cant_cajas*iprecio_caja)+(cant_unid*iprecio_unid) ";
        System.out.println(sql);
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    private void recreateExistencia2(Statement stmt) throws SQLException {
//        // TODO: ajustar con las tablas que tenes
//        String query = "select * from calc_exist_deposito('"+DateUtil.dateToString(fechaHasta, DATE_FORMAT)+"', "
//                +(deposito != null ? deposito.getDepositosPK().getCodDepo() : null)+")";
//        stmt.execute(query);
        
        // TODO: ajustar con las tablas que tenes
        // Se calcula la la ultima fecha del mes anterior
        LocalDate today = LocalDate.now();
        LocalDate end = today.minus(1, ChronoUnit.MONTHS).withDayOfMonth(today.lengthOfMonth());
        //default time zone
	ZoneId defaultZoneId = ZoneId.systemDefault();
        
         //local date + atStartOfDay() + default time zone + toInstant() = Date
        Date date = Date.from(end.atStartOfDay(defaultZoneId).toInstant());

        String query = "select * from calc_exist_deposito('"+DateUtil.dateToString(date, DATE_FORMAT)+"',1)";
        System.out.println(query);
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
                + "FROM mercasel "
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
                + "FROM mercasel m, sublineas s "
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
                + "FROM mercasel m, divisiones d "
                + "WHERE m.cod_division = d.cod_division "
                + "AND (m.cant_cajas > 0 OR m.cant_unid > 0) "
                + "GROUP BY m.cod_merca, d.cod_division, d.xdesc, m.iprecio_caja, d.cod_division, m.iprecio_unid ";
            
        }
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
