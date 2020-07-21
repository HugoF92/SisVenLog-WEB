package dao;

import entidad.Depositos;
import entidad.Divisiones;
import entidad.Sublineas;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Objects;
import entidad.Mercaderias;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;
import java.time.Duration;
import java.time.Instant;

/**
 *
 * @author Arsenio
 */
@Stateless
public class StockValorizadoFacade {
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    //TODO: cambiar fecha para entorno local yyyy/dd/MM y para el server
    // de prueba yyyy/MM/dd
    static final private String DATE_FORMAT = "yyyy/MM/dd";
    
    public void generateTableMostrar(Connection conexion, Date fechaHasta,
            Depositos deposito, Sublineas sublinea, Divisiones division,
            String discriminar, Boolean conPrecioCosto, Boolean sinIVA) throws SQLException {
        Statement stmt = conexion.createStatement();
        this.recreateExistenciaInTempCurmerca(stmt, fechaHasta, deposito);
        this.insertCuriiiIntoCurmerca(stmt);
        this.calcularPPP(stmt, fechaHasta, conPrecioCosto);
        String sql;
        // Si se desea sin IVA, se debe restar el impuesto al precio 
        if(sinIVA) {
            sql = " UPDATE #curmerca "
                + " SET iprecio_caja = iprecio_caja - impuestos, "
                + " iprecio_unid = iprecio_unid - impuestos ";
            System.out.println(sql);
            stmt.execute(sql);
        }
        // Preparar las columnas de gravadas discriminado en 10 y 5, y el
        // impuesto discriminado en 10 y 5%
        sql = "UPDATE #curmerca "
            + "SET tgrav_10 = (iprecio_caja * cant_cajas) + (iprecio_unid * cant_unid), "
            + "tgrav_5 = (iprecio_caja * cant_cajas) + (iprecio_unid * cant_unid),  "
            + "texe = (iprecio_caja * cant_cajas) + (iprecio_unid * cant_unid) "
            + "WHERE (cant_cajas != 0 OR cant_unid != 0) "
            + "AND pimpues in (0,5,10) ";
            System.out.println(sql);
            stmt.execute(sql);
        // Si se desea sin IVA,  se borran las columnas de impuestos
        if(sinIVA) {
            sql = "UPDATE #curmerca "
                + "SET timpu_10 = 0, timpu_5 = 0  ";
        } else {
            sql = "UPDATE #curmerca "
            + "SET timpu_10 = ROUND(tgrav_10 - (tgrav_10/(1+(pimpues/100))) ,0), "
            + "timpu_5 = ROUND(tgrav_5 - (tgrav_5/(1+(pimpues/100))) ,0)  "
            + "WHERE (cant_cajas != 0 OR cant_unid != 0) "
            + "AND pimpues in (5,10) ";
            System.out.println(sql);
            stmt.execute(sql);
            sql = "UPDATE #curmerca "
                + "SET tgrav_10 = (tgrav_10 - timpu_10), tgrav_5 = (tgrav_5 - timpu_5)";
        }
        System.out.println(sql);
        stmt.execute(sql);
        // Calcular el total
        sql = "UPDATE #curmerca "
            + "SET ttotal = (tgrav_10 + tgrav_5 + texe + timpu_10 + timpu_5)";
        System.out.println(sql);
        stmt.execute(sql);
        this.createtableMostrar(stmt, discriminar);
    }

    private void createtableMostrar(Statement stmt, String discriminar) throws SQLException {
        String sql = null;
        if("PM".equals(discriminar)) {
            sql = "SELECT * "
                + "INTO #mostrar "
                + "FROM #curmerca "
                + "WHERE cant_cajas != 0 "
                + "OR cant_unid != 0";
        }
        if("PS".equals(discriminar)) {
            sql = "SELECT "
                + "    m.cod_sublinea, s.xdesc, SUM(m.cant_cajas) as cant_cajas, "
                + "    SUM(m.cant_unid) as cant_unid, "
                + "    SUM((m.cant_cajas * m.iprecio_caja) + (m.cant_unid * m.iprecio_unid)) as ttotal  "
                + "INTO #mostrar "
                + "FROM #curmerca m, sublineas s "
                + "WHERE m.cod_sublinea = s.cod_sublinea "
                + "AND (m.cant_cajas > 0 OR m.cant_unid > 0) "
                + "GROUP BY m.cod_sublinea, s.xdesc "
                + "ORDER BY m.cod_sublinea ";
        }
        if("PD".equals(discriminar)) {
            sql = "SELECT "
                + "    d.cod_division as cod_sublinea, d.xdesc, "
                + "    SUM(m.cant_cajas) as cant_cajas, "
                + "    SUM(m.cant_unid) as cant_unid, "
                + "    SUM((m.cant_cajas * m.iprecio_caja) + (m.cant_unid * m.iprecio_unid)) as ttotal "
                + "INTO #mostrar "
                + "FROM #curmerca m, divisiones d "
                + "WHERE m.cod_division = d.cod_division "
                + "AND (m.cant_cajas > 0 OR m.cant_unid > 0) "
                + "GROUP BY d.cod_division, d.xdesc ";
        }
        System.out.println(sql);
        Instant start = Instant.now();
        stmt.execute(sql);
        Instant end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
    }
    
    private void insertCuriiiIntoCurmerca(Statement stmt) throws SQLException {
        String sql = "SELECT m.cod_merca, i.pimpues, t.xdesc, t.mgrav_exe "
                    + "INTO #curiii "
                    + "FROM merca_impuestos m, impuestos i, tmp_mercaderias t "
                    + "WHERE m.cod_empr = 2 "
                    + "AND m.cod_merca = t.cod_merca  "
                    + "AND m.cod_impu = i.cod_impu ";
        System.out.println(sql);
        Instant start = Instant.now();
        stmt.execute(sql);
        Instant end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
        sql = "UPDATE #curmerca "
                    + "SET pimpues  =  ci.pimpues, "
                    + "mgrav_exe  =  ci.mgrav_exe,"
                    + "xdesc = ci.xdesc "
                    + "FROM  "
                    + "    #curmerca c "
                    + "    INNER JOIN #curiii ci "
                    + "        ON c.cod_merca COLLATE DATABASE_DEFAULT = ci.cod_merca COLLATE DATABASE_DEFAULT ";
        System.out.println(sql);
        start = Instant.now();
        stmt.execute(sql);
        end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
        sql = "UPDATE #curmerca "
            + "SET "
            + "    pimpues  =  10 "
            + "WHERE pimpues != 10 AND pimpues != 5";
        System.out.println(sql);
        start = Instant.now();
        stmt.execute(sql);
        end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
    }
    
    private class Curmerca {
        private String codMerca;
        private Integer cantCajas;
        private Integer cantUnid;
        private String gravExe;
        private Double pimpues;
        private Double precioUnid;
        public Curmerca(){}

        public String getCodMerca() {
            return codMerca;
        }

        public void setCodMerca(String codMerca) {
            this.codMerca = codMerca;
        }

        public Integer getCantCajas() {
            return cantCajas;
        }

        public void setCantCajas(Integer cantCajas) {
            this.cantCajas = cantCajas;
        }

        public Integer getCantUnid() {
            return cantUnid;
        }

        public void setCantUnid(Integer cantUnid) {
            this.cantUnid = cantUnid;
        }

        public String getGravExe() {
            return gravExe;
        }

        public void setGravExe(String gravExe) {
            this.gravExe = gravExe;
        }

        public Double getPimpues() {
            return pimpues;
        }

        public void setPimpues(Double pimpues) {
            this.pimpues = pimpues;
        }

        public Double getPrecioUnid() {
            return precioUnid;
        }

        public void setPrecioUnid(Double precioUnid) {
            this.precioUnid = precioUnid;
        }
    }
    
    private List<Curmerca> recreateExistenciaInTempCurmerca(Statement stmt, Date fechaHasta, Depositos deposito) throws SQLException {
        String sql = "select * from calc_exist_deposito('"+DateUtil.dateToString(fechaHasta, DATE_FORMAT)+"', "
                +(deposito != null ? deposito.getDepositosPK().getCodDepo() : null)+")";
        System.out.println(sql);
        Instant start = Instant.now();
        ResultSet rs = stmt.executeQuery(sql);
        Instant end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
        List<Curmerca> curmercaList = new ArrayList<>();
        while (rs.next()) {
            Curmerca curmerca = new Curmerca();
            curmerca.setCodMerca(rs.getString("cod_merca"));
            curmerca.setCantCajas(rs.getInt("cant_cajas"));
            curmerca.setCantUnid(rs.getInt("cant_unid"));
            curmercaList.add(curmerca);
        }
        // Creamos la tabla temporal Curmerca
        sql = "CREATE TABLE #curmerca ( "
                    + "cod_merca CHAR(13), "
                    + "xdesc CHAR(50), "
                    + "cant_cajas integer, "
                    + "iprecio_caja integer, "
                    + "cant_unid integer, "
                    + "iprecio_unid integer,"
                    + "tgrav_10 integer,"
                    + "tgrav_5 integer,"
                    + "texe integer,"
                    + "timpu_10 integer,"
                    + "timpu_5 integer,"
                    + "ttotal integer,"
                    + "pimpues decimal(12,2), "
                    + "impuestos decimal(12,2), "
                    + "mgrav_exe CHAR(1),"
                    + "cod_sublinea integer,"
                    + "cod_division integer"
                    + ")";
        System.out.println(sql);
        start = Instant.now();
        stmt.execute(sql);
        end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
        for(Curmerca c: curmercaList){
            sql = "INSERT INTO #curmerca (cod_merca, cant_cajas, cant_unid) "
                        + "VALUES ('" + c.getCodMerca().trim() + "', " + c.getCantCajas() + ", " + c.getCantUnid()+" )";
            System.out.println(sql);
            start = Instant.now();
            stmt.execute(sql);
            end = Instant.now();
            System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
        }
        return curmercaList;
    }
    
    private void calcularPPP(Statement stmt, Date fechaHasta, Boolean conPrecioCosto) throws SQLException {
        String sql = "select cod_merca, cant_cajas, cant_unid, pimpues, iprecio_unid, mgrav_exe from #curmerca";
        System.out.println(sql);
        Instant start = Instant.now();
        ResultSet rs = stmt.executeQuery(sql);
        Instant end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
        List<Curmerca> curmercaList = new ArrayList<>();
        while (rs.next()) {
            Curmerca curmerca = new Curmerca();
            curmerca.setCodMerca(rs.getString("cod_merca"));
            curmerca.setCantCajas(rs.getInt("cant_cajas"));
            curmerca.setCantUnid(rs.getInt("cant_unid"));
            curmerca.setPimpues(rs.getDouble("pimpues"));
            curmerca.setPrecioUnid(rs.getDouble("iprecio_unid"));
            curmerca.setGravExe(rs.getString("mgrav_exe"));
            curmercaList.add(curmerca);
        }
        String l_ffinal = DateUtil.dateToString(fechaHasta, DATE_FORMAT);
        Double l_nprecio_ppp, l_nppp_caja, l_impuesto = 0.0, l_importe, l_pimpues;
        Boolean exist;
        for(Curmerca curmerca: curmercaList) {
            if(!conPrecioCosto) {
                sql = "SELECT TOP 1 p.fppp, p.nprecio_ppp, p.nppp_caja "
                    + "FROM ppp p, merca_impuestos mi, impuestos i "
                    + "WHERE p.cod_merca = '" + curmerca.getCodMerca() +"' "
                    + "AND p.cod_empr = 2 "
                    + "AND p.cod_merca = mi.cod_merca "
		    + "AND mi.cod_impu = i.cod_impu "
                    + "AND p.fppp <= '" + l_ffinal + "' "
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
                    + "WHERE cod_merca = '" + curmerca.getCodMerca() +"' "
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
                
                if(curmerca.getPimpues() > 0.0){
                    Precio precio = this.calcularPrecio(stmt, curmerca.getCodMerca(), l_nppp_caja, l_nprecio_ppp, "X", l_ffinal);
                    l_nprecio_ppp = new Double(Math.round(precio.getPrecioUnid()));
                    l_nppp_caja = new Double(Math.round(precio.getPrecioCaja()));
                }
            }
            sql = " UPDATE #curmerca "
                + " SET iprecio_unid = " + l_nprecio_ppp + ", iprecio_caja = " + l_nppp_caja
                + " WHERE cod_merca = '" + curmerca.getCodMerca() + "'";
            System.out.println(sql);
            stmt.execute(sql);
            
            if("G".equals(curmerca.getGravExe())){
                l_importe = curmerca.getPrecioUnid();
                if(curmerca.getPimpues() == 0.0) {
                    if(l_importe > 0){
                        l_impuesto = calcularImpuestos(curmerca.getCodMerca(),l_importe, "S");    
                        l_pimpues = Math.round((Math.abs(l_impuesto)/(curmerca.getPrecioUnid() - l_impuesto)) * 100.0) / 100.0;
                        l_pimpues = l_pimpues *100;
                    } else {
                        l_pimpues = 1.0;
                    }
                    sql = " UPDATE #curmerca "
                        + " SET pimpues = " + l_pimpues
                        + " WHERE cod_merca = '" + curmerca.getCodMerca() + "'";
                    System.out.println(sql);
                    stmt.execute(sql);
                } else {
                    l_impuesto = l_importe - (l_importe / (1+(curmerca.getPimpues()/100)));
                }
            } else {
                l_impuesto = 0.0;
            }
            sql = " UPDATE #curmerca "
                + " SET impuestos = " + l_impuesto
                + " WHERE cod_merca = '" + curmerca.getCodMerca() + "'";
            System.out.println(sql);
            stmt.execute(sql);
        }
    }
    
    private Double calcularImpuestos(String codMercaderia, Double importe,
            String incluyeIva) {
        String sql = "SELECT "
                + "    SUM(b.ifijo) as l_ifijo,  "
                + "    SUM(b.pimpues/100) as l_pimpues, "
                + "FROM merca_impuestos a, impuestos b   "
                + "WHERE a.cod_empr = 2 "
                + "AND a.cod_impu = b.cod_impu "
                + "AND a.cod_merca = '" + codMercaderia +"' "
                + "GROUP BY a.cod_merca ";
        Query query = getEntityManager().createNativeQuery(sql);
        Object[] result = (Object[]) query.getSingleResult();
        Double ifijo = Double.valueOf(result[0].toString());
        Double pimpues = Double.valueOf(result[1].toString());
        Double limporte;
        if("S".equals(incluyeIva)){
            limporte = importe - ((importe / (1+pimpues)) + ifijo);
        }else{
            limporte = ((importe * (1+pimpues)) + ifijo) - importe;
        }
        return limporte;
    }
    
    private class Precio {
        private final Double precioCaja;
        private final Double precioUnid;
        public Precio(Double precioCaja, Double precioUnid){
            this.precioCaja = precioCaja;
            this.precioUnid = precioUnid;
        }

        public Double getPrecioCaja() {
            return precioCaja;
        }

        public Double getPrecioUnid() {
            return precioUnid;
        }
    }

    private Precio calcularPrecio(Statement stmt, String codMercaderia, Double l_precio_caja,
            Double l_precio_unidad, String l_ctipo_vta, String l_fecha) throws SQLException {
        Boolean exist;
        ResultSet rs;
        Double l_ifijo, l_pimpues, iretorno_caja, iretorno_unidad;
        String sql = "SELECT "
                + "    SUM(b.ifijo) as l_ifijo,  "
                + "    SUM(b.pimpues/100) as l_pimpues "
                + "FROM merca_impuestos a, impuestos b   "
                + "WHERE  a.cod_empr = 2 "
                + "AND a.cod_merca = '" + codMercaderia + "' "
                + "AND a.cod_impu = b.cod_impu ";
        System.out.println(sql);
        rs = stmt.executeQuery(sql);
        exist = rs.next();
        l_ifijo = exist? rs.getDouble("l_ifijo"): 0;
        l_pimpues = exist? rs.getDouble("l_pimpues"): 0;
        if(l_ifijo > 0 || l_pimpues > 0){
            l_precio_caja = (l_precio_caja * (1+l_pimpues)) + l_ifijo;
            l_precio_unidad = (l_precio_unidad * (1+l_pimpues)) + l_ifijo;
        }
        sql = "SELECT "
            + "  iretorno_caja, iretorno_unidad "
            + "FROM retornos_precios a  "
            + "WHERE  a.cod_empr = 2 "
            + "AND a.cod_merca = '" + codMercaderia + "' "
            + "AND a.ctipo_vta = '" + l_ctipo_vta + "' "
            + "AND '" + l_fecha + "' BETWEEN a.frige_desde AND a.frige_hasta ";
        System.out.println(sql);
        rs = stmt.executeQuery(sql);
        exist = rs.next();
        iretorno_caja = exist? rs.getDouble("iretorno_caja"): 0;
        iretorno_unidad = exist? rs.getDouble("iretorno_unidad"): 0;
        
        l_precio_caja = l_precio_caja - iretorno_caja;
        l_precio_unidad  = l_precio_unidad - iretorno_unidad;
        return new Precio(l_precio_caja, l_precio_unidad);
    }
    
    public void insertarMercaderiasSeleccionadas(Statement stmt,
            List<Mercaderias> mercaderias, List<Mercaderias> mercaderiasActivas) throws SQLException {
        String sql = "IF OBJECT_ID('dbo.tmp_mercaderias', 'U') IS NOT NULL DROP TABLE tmp_mercaderias";
        System.out.println(sql);
        Instant start = Instant.now();
        stmt.execute(sql);
        Instant end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
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
        start = Instant.now();
        stmt.execute(sql);
        end = Instant.now();
        System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
        if (mercaderias.size() > 0) {
            Mercaderias aux;
            for (Mercaderias m : mercaderias) {
                aux = mercaderiasActivas.stream()
                        .filter(ma -> ma.equals(m))
                        .findAny()
                        .orElse(null);
                if(Objects.nonNull(aux)){
                    sql = "INSERT INTO tmp_mercaderias (cod_merca, "
                        + "cod_barra, xdesc, nrelacion,cant_cajas, cant_unid, mgrav_exe ) "
                        + "VALUES ('" + aux.getMercaderiasPK().getCodMerca()
                        + "', '" + aux.getCodBarra() + "', '" + aux.getXdesc().replace("'", "''")
                        + "', " + aux.getNrelacion() + ",0,0, '" + aux.getMgravExe()+"' )";
                    System.out.println(sql);
                    start = Instant.now();
                    stmt.execute(sql);
                    end = Instant.now();
                    System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
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
            start = Instant.now();
            stmt.execute(sql);
            end = Instant.now();
            System.out.println("Duracion en segundos: " + Duration.between(start, end).getSeconds());
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
            Logger.getLogger(StockValorizadoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    public String generateSelectMostrar() {
        return "SELECT "
                + "    cod_merca, xdesc, cant_cajas, cant_unid, "
                + "    iprecio_caja, iprecio_unid, tgrav_10, tgrav_5, timpu_10, timpu_5, texe, ttotal "
                + "FROM #mostrar";
    }
}
