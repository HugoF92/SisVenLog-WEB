package dao;

import entidad.Mercaderias;
import entidad.Zonas;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
public class MovimientosMercaderiasFacade {
    
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateTableMostrar(Statement stmt, Date fechaDesde,
            Date fechaHasta, Zonas zona, Mercaderias mercaderia) throws SQLException {
        this.insertarMercaderiaSeleccionada(stmt, mercaderia);
        this.buscarMovimientos(stmt, fechaDesde, fechaHasta, zona, mercaderia);
        this.recreateExistenciaInTempInforme(stmt, fechaDesde, zona);
        this.calcularPPP(stmt, fechaDesde);
    }
    
    private void insertarMercaderiaSeleccionada(Statement stmt, Mercaderias mercaderia) throws SQLException {
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
        sql = "INSERT INTO tmp_mercaderias (cod_merca, "
            + "cod_barra, xdesc, nrelacion,cant_cajas, cant_unid, mgrav_exe ) "
            + "VALUES ('" + mercaderia.getMercaderiasPK().getCodMerca()
            + "', '" + mercaderia.getCodBarra() + "', '" + mercaderia.getXdesc().replace("'", "''")
            + "', " + mercaderia.getNrelacion() + ",0,0, '" + mercaderia.getMgravExe()+"' )";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private void calcularPPP(Statement stmt, Date fechaDesde) throws SQLException {
        String sql = "select ndocum, codigo, inicaja, iniuni, ctipo_docum, relacion from #informe";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        List<Informe> informeList = new ArrayList<>();
        while (rs.next()) {
            Informe informe = new Informe();
            informe.ndocum = rs.getLong("ndocum");
            informe.codigo = rs.getString("codigo");
            informe.iniCaja = rs.getInt("inicaja");
            informe.iniUni = rs.getInt("iniuni");
            informe.relacion = rs.getDouble("relacion");
            informe.ctipoDocum = rs.getString("ctipo_docum");
            informeList.add(informe);
        }
        String l_finicial = DateUtil.dateToString(fechaDesde, DATE_FORMAT);
        String l_cor_codigo = "";
        Double l_uni_ppp=0.0, l_sal_gs=0.0, l_sal_stock=0.0, l_nppp_caja;
        Boolean exist;
        for(Informe informe: informeList) {
            if("".equals(l_cor_codigo) || !l_cor_codigo.equals(informe.codigo)){
                l_cor_codigo = informe.codigo;
                sql = "SELECT TOP 1 p.fppp, p.nprecio_ppp, p.nppp_caja "
                    + "FROM ppp p "
                    + "WHERE p.cod_merca = '" + l_cor_codigo +"' "
                    + "AND p.cod_empr = 2 "
                    + "AND p.fppp <= '" + l_finicial + "' "
                    + "ORDER BY fppp DESC ";
                //System.out.println(sql);
                rs = stmt.executeQuery(sql);
                exist = rs.next();
                l_uni_ppp = exist? rs.getDouble("nprecio_ppp"): 0;
                l_nppp_caja = exist? rs.getDouble("nppp_caja"): 0;
                sql = "UPDATE #informe "
                    + "SET ppp_uni = " + l_uni_ppp + ", "
                    + "nppp_caja = " + l_nppp_caja + ", "
                    + "sal_gs = " + (l_uni_ppp * informe.iniCaja + l_uni_ppp * informe.iniUni) + ", "
                    + "sal_stock = (inicaja * relacion ) + iniuni ";
                //System.out.println(sql);
                stmt.execute(sql);
                l_sal_gs = l_uni_ppp * informe.iniCaja + l_uni_ppp * informe.iniUni;
                l_sal_stock = (informe.iniCaja * informe.relacion) + informe.iniUni;
                //l_uni_ppp = informe.pppUni;
            }
            // ** CALCULAR PPP
            sql = "UPDATE #informe "
                + "SET sal_stock = " + l_sal_stock +" + ((vtacaja * relacion) + vtauni) "
                + "WHERE ndocum = " + informe.ndocum;
            //System.out.println(sql);
            stmt.execute(sql);
            sql = "UPDATE #informe "
                + "SET sal_gs = " + l_sal_gs + " + igravadas + iexentas + impuestos "
                + "WHERE ctipo_docum in ('FCC','NCC' ,'CVC','COC','NDP')"
                + "AND ndocum = " + informe.ndocum;
            //System.out.println(sql);
            stmt.execute(sql);
            if("FCC".equals(informe.ctipoDocum) ||
                    "NCC".equals(informe.ctipoDocum) ||
                    "CVC".equals(informe.ctipoDocum) ||
                    "COC".equals(informe.ctipoDocum)) {
                sql = "UPDATE #informe "
                + "SET ppp_uni = sal_gs/sal_stock "
                + "WHERE ndocum = " + informe.ndocum;
                //System.out.println(sql);
                stmt.execute(sql);
            } else {
                sql = "UPDATE #informe "
                + "SET ppp_uni = " + l_uni_ppp + " "
                + "WHERE ndocum = " + informe.ndocum;
                //System.out.println(sql);
                stmt.execute(sql);
            }
            if("FCC".equals(informe.ctipoDocum) ||
                    "NCC".equals(informe.ctipoDocum) ||
                    "CVC".equals(informe.ctipoDocum) ||
                    "COC".equals(informe.ctipoDocum) ||
                    "NDP.".equals(informe.ctipoDocum)) {
                System.out.println("");
            } else {
                sql = "UPDATE #informe "
                + "SET sal_gs = " + l_sal_gs + " + ppp_uni *(vtacaja*relacion+vtauni) "
                + "WHERE ndocum = " + informe.ndocum;
                //System.out.println(sql);
                stmt.execute(sql);
            }
            sql = "Select sal_stock, sal_gs, ppp_uni from #informe "
                + "WHERE ndocum = " + informe.ndocum;
            //System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            l_sal_gs = rs.getDouble("sal_gs");
            l_sal_stock = rs.getDouble("sal_stock");
            l_uni_ppp = rs.getDouble("ppp_uni");
        }
    }
    
    private class Movcur {
        private String codMerca;
        private Integer movCajas;
        private Integer movUnid;
        private Long ndocum;
        private String xdescDocum;
        private String xnombre;
        private String ctipoDocum;
        private java.sql.Date fmovim;
        private String codZona;
        public Movcur(){}
    }

    private void buscarMovimientos(Statement stmt, Date fechaDesde,
            Date fechaHasta, Zonas zona, Mercaderias mercaderia) throws SQLException {
        // Creamos la tabla temporal Informe
        String sql = "CREATE TABLE #informe ( "
                    + "codigo CHAR(13), "
                    + "inicaja integer, "
                    + "vtacaja integer, "
                    + "iniuni integer, "
                    + "vtauni integer,"
                    + "ndocum numeric(18,0), "
                    + "xdesc_docum varchar(50), "
                    + "xnombre varchar(50), "
                    + "relacion decimal(5,2), "
                    + "igravadas numeric(18,0), "
                    + "iexentas numeric(18,0), "
                    + "impuestos decimal(12,2), "
                    + "sal_stock decimal(12,2), "
                    + "sal_gs decimal(12,2), "
                    + "ppp_uni numeric(8,0),"
                    + "ctipo_docum char(3), "
                    + "fmovim varchar(12), "
                    + "cod_zona char(2),"
                    + "nppp_caja numeric(18,0)"
                    + ")";
        System.out.println(sql);
        stmt.execute(sql);
        sql = "SELECT "
            + "    m.cod_merca,  m.cod_zona, m.ctipo_docum, '' as cod_barra, d.xorden_ppp, m.fmovim,  "
            + "	   m.cant_cajas as mov_cajas , m.cant_unid as mov_unid,  m.fmovim, d.xdesc as xdesc_docum, "
            + "	   m.ndocum, m.cod_depo, ISNULL(c.xnombre, '') as xnombre , m.manulado, m.igravadas as igravadas, "
            + "    m.iexentas  as iexentas, m.impuestos as impuestos "
            + "FROM movimientos_merca m "
            + "LEFT OUTER JOIN clientes c ON m.cod_cliente = c.cod_cliente, tipos_documentos d, depositos p "
            + "WHERE  m.cod_empr = 2 "
            + "AND m.cod_merca = '" + mercaderia.getMercaderiasPK().getCodMerca() + "' "
            + "AND m.cod_depo = p.cod_depo "
            + "AND m.ctipo_docum  = d.ctipo_docum "
            + "AND m.fmovim BETWEEN '" + DateUtil.dateToString(fechaDesde, DATE_FORMAT)
            + "' AND '" + DateUtil.dateToString(fechaHasta, DATE_FORMAT) + "' ";
        if(Objects.nonNull(zona)) {
            sql += "AND p.cod_zona = '" + zona.getZonasPK().getCodZona() +"' ";
        }
        sql += "ORDER BY m.cod_merca, m.fmovim, d.xorden_ppp";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        List<Movcur> movcurs = new ArrayList<>();
        while (rs.next()) {
            Movcur movcur = new Movcur();
            movcur.codMerca = rs.getString("cod_merca");
            movcur.movCajas = rs.getInt("mov_cajas");
            movcur.movUnid = rs.getInt("mov_unid");
            movcur.ndocum = rs.getLong("ndocum");
            movcur.xdescDocum = rs.getString("xdesc_docum");
            movcur.xnombre = rs.getString("xnombre");
            movcur.ctipoDocum = rs.getString("ctipo_docum");
            movcur.fmovim = rs.getDate("fmovim");
            movcur.codZona = rs.getString("cod_zona");
            movcurs.add(movcur);
        }
        Double itotal, l_igravadas, l_iexentas, pimpues;
        Boolean exist;
        String cconc;
        for(Movcur movcur: movcurs) {
            sql = "INSERT INTO #informe (ndocum) VALUES (" + movcur.ndocum + ")";
            //System.out.println(sql);
            stmt.execute(sql);
            sql = " UPDATE #informe "
                + " SET vtacaja = " + movcur.movCajas + ","
                + " vtauni = " + movcur.movUnid + ","
                + " xdesc_docum = '" + movcur.xdescDocum + "',"
                + " xnombre = '" + movcur.xnombre + "', "
                + " codigo = '" + mercaderia.getMercaderiasPK().getCodMerca() + "', "
                + " sal_stock = 0, "
                + " sal_gs = 0, "
                + " ppp_uni = 0, "
                + " inicaja = 0, "
                + " iniuni = 0, "
                + " ctipo_docum = '" + movcur.ctipoDocum+ "', "
                + " fmovim = '" + DateUtil.dateToString(movcur.fmovim, "dd/MM/yy") + "', "
                + " cod_zona = '"+ (movcur.codZona != null? movcur.codZona:"") + "' "
                + "WHERE ndocum = " + movcur.ndocum;
            //System.out.println(sql);
            stmt.execute(sql);
            if("FCC".equals(movcur.ctipoDocum) || "CVC".equals(movcur.ctipoDocum) ||
                    "COC".equals(movcur.ctipoDocum)) {
                sql = "SELECT iprecio_caja, iprecio_unid, cant_cajas, cant_unid, pimpues, itotal "
                    + "FROM compras_det "
                    + "WHERE nrofact = " + movcur.ndocum + " "
                    + "AND cod_empr = 2 "
                    + "AND cod_merca = '" + movcur.codMerca + "' "
                    + "AND ffactur = '" + DateUtil.dateToString(movcur.fmovim, DATE_FORMAT) + "' "
                    + "AND ctipo_docum = '" + movcur.ctipoDocum + "'";
                rs = stmt.executeQuery(sql);
                exist = rs.next();
                itotal = exist? rs.getDouble("itotal"): 0;
                pimpues = exist? rs.getDouble("pimpues"): 0;
                l_igravadas = 0.0;
		l_iexentas = 0.0;
                if(pimpues > 0) {
                    l_igravadas = itotal;
                } else {
                    l_iexentas = itotal;
                }
                sql = " UPDATE #informe "
                + " SET igravadas = " + l_igravadas + ", "
                + " iexentas = " + l_iexentas + ", "
                + " impuestos = 0 "
                + " WHERE ndocum = " + movcur.ndocum;
                //System.out.println(sql);
                stmt.execute(sql);
            }
            if("NCC".equals(movcur.ctipoDocum)) {
                sql = "SELECT n.cconc "
                    + "FROM notas_compras_det d, notas_compras n "
                    + "WHERE d.nro_nota = " + movcur.ndocum + " "
                    + "AND d.nro_nota = n.nro_nota "
                    + "AND d.ctipo_docum = n.ctipo_docum "
                    + "AND n.cod_empr = 2 "
                    + "AND d.cod_empr = 2 "
                    + "AND d.cod_merca = '" + movcur.codMerca + "' "
                    + "AND d.ctipo_docum = '" + movcur.ctipoDocum + "'";
                rs = stmt.executeQuery(sql);
                exist = rs.next();
                cconc = exist? rs.getString("cconc"): "";
                if("DES".equals(cconc) || "AVE".equals(cconc)){
                    sql = " UPDATE #informe "
                        + " SET igravadas = 0, "
                        + " iexentas = 0, "
                        + " impuestos = 0 "
                        + " WHERE ndocum = " + movcur.ndocum;
                    //System.out.println(sql);
                    stmt.execute(sql);
                }
                sql = "UPDATE #informe SET ctipo_docum = '" + cconc + "' "
                    + "WHERE ndocum = " + movcur.ndocum;
                //System.out.println(sql);
                stmt.execute(sql);
            }
        }
        sql = "DELETE FROM #informe "
            + "WHERE ctipo_docum in ('EN', 'DEV') OR fmovim IS NULL";
        System.out.println(sql);
        stmt.execute(sql);
    }

    private class Informe {
        private String codigo;
        private Integer iniCaja;
        private Integer iniUni;
        private Long ndocum;
        private String ctipoDocum;
        private Double relacion;
    }
    
    private void recreateExistenciaInTempInforme(Statement stmt, Date fechaDesde, Zonas zona) throws SQLException {
        LocalDateTime ldt = LocalDateTime.ofInstant(fechaDesde.toInstant(), ZoneId.systemDefault());
        ldt = ldt.minusDays(1);
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        String sql = "select * from calc_exist_deposito('"+DateUtil.dateToString(out, DATE_FORMAT) +"',null, "
                +(zona != null ? "'" + zona.getZonasPK().getCodZona() + "'" : null)+")";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        // obtenemos el resultado
        Boolean exist = rs.next();
        if(exist) {
            sql = " UPDATE #informe "
                + " SET inicaja = " + rs.getInt("cant_cajas") + ", "
                + " iniuni = " + rs.getInt("cant_unid") + "";
                System.out.println(sql);
                stmt.execute(sql);
        }
        sql = "SELECT nrelacion from tmp_mercaderias";
        System.out.println(sql);
        rs = stmt.executeQuery(sql);
        exist = rs.next();
        if(exist) {
            sql = " UPDATE #informe "
                + " SET relacion = " + rs.getInt("nrelacion");
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
            Logger.getLogger(MovimientosMercaderiasFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    public String generateSelectMostrar() {
        return "SELECT "
                + "    codigo, inicaja, vtacaja, iniuni, vtauni, ndocum, "
                + "    xdesc_docum, xnombre, impuestos, relacion, igravadas, "
                + "    iexentas, impuestos, sal_stock, sal_gs, ppp_uni, ctipo_docum "
                + "FROM #informe ";
    }
}
