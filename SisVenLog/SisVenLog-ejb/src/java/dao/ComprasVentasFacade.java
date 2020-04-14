package dao;

import entidad.CanalesVenta;
import entidad.Depositos;
import entidad.Lineas;
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
import entidad.Mercaderias;
import entidad.Proveedores;
import entidad.Sublineas;
import util.DateUtil;

/**
 *
 * @author Arsenio
 */
@Stateless
public class ComprasVentasFacade {
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateTableUltimo(Statement stmt, Date fechaDesde,
            Date fechaHasta, Depositos deposito, Proveedores proveedor,
            Sublineas sublinea, Lineas linea, CanalesVenta canal,
            Boolean sinIva, String discriminado) throws SQLException {
        this.generateFacturasCompras(stmt, fechaDesde, fechaHasta,
                    deposito, proveedor, sublinea, linea, canal, sinIva, discriminado);
        this.generateFacturasVentas(stmt, fechaDesde, fechaHasta,
                    deposito, proveedor, sublinea, linea, canal, sinIva, discriminado);
        this.mergeTmpCompraVenta(stmt);
    }

    private void generateFacturasCompras(Statement stmt, Date fechaDesde,
            Date fechaHasta, Depositos deposito, Proveedores proveedor,
            Sublineas sublinea, Lineas linea, CanalesVenta canal,
            Boolean sinIva, String discriminado) throws SQLException {
        String sql = "";
        String dateFormat = "yyyy/MM/dd";
        String intoMostrar1 = "INTO #MOSTRAR1 ";
        switch (discriminado) {
            case "M":
                if (sinIva) {
                    sql += "SELECT "
                        + "    d.cod_merca as codigo, d.xdesc as xdesc, d.nrelacion, d.npeso_caja, d.npeso_unidad, "
                        + "    (SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) AS cajas_compras, "
                        + "    ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0) AS unid_compras, "
                        + "    SUM(iexentas+igravadas) as ttotal, ((SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) ) * d.npeso_caja AS peso_cajas, "
                        + "    (ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0)) * d.npeso_unidad AS peso_unid "
                        + intoMostrar1
                        + "FROM movimientos_merca m, proveedores p, depositos e, mercaderias d, sublineas s, #tmp_mercaderias t, merca_canales mc ";
                } else {
                    sql += "SELECT "
                        + "    d.cod_merca as codigo, d.xdesc as xdesc, d.nrelacion, d.npeso_caja, d.npeso_unidad, "
                        + "    (SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) AS cajas_compras, "
                        + "    ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0) AS unid_compras, "
                        + "    SUM(iexentas+igravadas+impuestos) as ttotal, ((SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) ) * d.npeso_caja AS peso_cajas, "
                        + "    (ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1))  * d.nrelacion,0)) * d.npeso_unidad AS peso_unid "
                        + intoMostrar1
                        + " FROM movimientos_merca m, proveedores p, depositos e, mercaderias d, sublineas s, #tmp_mercaderias t, merca_canales mc ";
                }
                break;
            case "SL":
                if (sinIva) {
                    sql += "SELECT "
                        + "    d.cod_sublinea as codigo, s.xdesc as xdesc, d.nrelacion, d.npeso_caja, d.npeso_unidad, "
                        + "    (SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) AS cajas_compras, "
                        + "    ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0) AS unid_compras, "
                        + "    SUM(iexentas+igravadas) as ttotal, ((SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1))  ) * d.npeso_caja AS peso_cajas, "
                        + "    (ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1))  * d.nrelacion,0)) * d.npeso_unidad AS peso_unid "
                        + intoMostrar1
                        + "FROM movimientos_merca m, proveedores p, depositos e, mercaderias d, sublineas s, #tmp_mercaderias t, merca_canales mc "; 
                } else {
                    sql += "SELECT "
                        + "    d.cod_sublinea as codigo, s.xdesc as xdesc, d.nrelacion, d.npeso_caja, d.npeso_unidad, "
                        + "    (SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) AS cajas_compras, "
                        + "    ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0) AS unid_compras, "
                        + "    SUM(iexentas+igravadas+impuestos) as ttotal, ((SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1))) * d.npeso_caja AS peso_cajas, "
                        + "    (ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0)) * d.npeso_unidad AS peso_unid "
                        + intoMostrar1
                        + "FROM movimientos_merca m, proveedores p, depositos e, mercaderias d, sublineas s, #tmp_mercaderias t, merca_canales mc ";
                }
                break;
            case "L":
                if (sinIva) {
                    sql += "SELECT "
                        + "    l.cod_linea as codigo, l.xdesc as xdesc, d.nrelacion, d.npeso_caja, d.npeso_unidad, "
                        + "    (SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) AS cajas_compras, "
                        + "    ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0) AS unid_compras, "
                        + "    SUM(iexentas+igravadas) as ttotal, ((SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1))) * d.npeso_caja AS peso_cajas, "
                        + "    (ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0)) * d.npeso_unidad AS peso_unid "
                        + intoMostrar1
                        + " FROM movimientos_merca m, proveedores p, depositos e, mercaderias d, sublineas s, lineas l, #tmp_mercaderias t, merca_canales mc ";
                } else {
                    sql += "SELECT "
                        + "    l.cod_linea as codigo, l.xdesc as xdesc, d.nrelacion, d.npeso_caja, d.npeso_unidad, "
                        + "    (SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) AS cajas_compras, "
                        + "    ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0) AS unid_compras, "
                        + "	SUM(iexentas+igravadas+impuestos) as ttotal, ((SUM(m.cant_cajas) + ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1))) * d.npeso_caja AS peso_cajas, "
                        + "    (ROUND((SUM(m.cant_unid) / d.nrelacion - ROUND(SUM(m.cant_unid) / d.nrelacion, 0, 1)) * d.nrelacion,0)) * d.npeso_unidad AS peso_unid "
                        + intoMostrar1
                        + "FROM movimientos_merca m, proveedores p, depositos e, mercaderias d, sublineas s, lineas l, #tmp_mercaderias t, merca_canales mc ";
                }
        }
        sql += "WHERE m.cod_empr = 2 "
            + "AND m.cod_proveed = p.cod_proveed "
            + "AND m.cod_depo = e.cod_depo "
            + "AND m.cod_empr = e.cod_empr "
            + "AND m.cod_empr = d.cod_empr "
            + "AND m.cod_merca = d.cod_merca "
            + "AND d.cod_sublinea = s.cod_sublinea "
            + "AND d.cod_merca = mc.cod_merca "
            + "AND m.ctipo_docum IN ('COC','CVC','FCC','NDC','NCC','NDP') "
            + "AND (m.fmovim BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat) + "' "
            + "AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "') ";
        if (Objects.nonNull(proveedor)) {
            sql += "AND d.cod_proveed = " + proveedor.getCodProveed() + " ";
        }
        if (Objects.nonNull(sublinea)) {
            sql += "AND d.cod_sublinea = " + sublinea.getCodSublinea() + " ";
        }
        if (Objects.nonNull(linea)) {
            sql += "AND s.cod_linea = " + linea.getCodLinea() + " ";
        }
        if (Objects.nonNull(deposito)) {
            sql += "AND m.cod_depo = " + deposito.getDepositosPK().getCodDepo() + " ";
        }
        if (Objects.nonNull(canal)) {
            sql += "AND mc.cod_canal = '" + canal.getCodCanal() + "' ";
        }
        sql += "AND m.cod_merca COLLATE DATABASE_DEFAULT = t.cod_merca COLLATE DATABASE_DEFAULT ";
        switch(discriminado) {
            case "M":
                sql += "GROUP BY d.cod_merca, d.xdesc,"
                    + "d.nrelacion, d.npeso_caja, d.npeso_unidad ";
                break;
            case "SL":
                sql += "GROUP BY d.cod_sublinea, s.xdesc,"
                    + "d.nrelacion, d.npeso_caja, d.npeso_unidad ";
                break;
            case "L":
                sql += "AND s.cod_linea = l.cod_linea "
                    + "GROUP BY l.cod_linea, l.xdesc,"
                    + "d.nrelacion, d.npeso_caja, d.npeso_unidad ";
        }
        // Generamos la tabla temporal mostrar1
        stmt.execute(sql);
        // Generamos la tabla temporal mostrar agrupado por codigo
        // de mercaderia y descripcion
        sql = "SELECT "
            + "    codigo, xdesc, "
            + "    SUM(cajas_compras) as cajas_compras, " 
            + "    SUM(unid_compras)  as unid_compras , "   
            + "    SUM(ttotal)  as ttotal, " 
            + "    SUM(peso_cajas) as peso_ccajas, " 
            + "    SUM(peso_unid) as peso_cunid " 
            + "INTO #MOSTRAR "
            + "FROM #MOSTRAR1 " 
            + "GROUP BY codigo, xdesc ";
        stmt.execute(sql);
    }

    private void generateFacturasVentas(Statement stmt, Date fechaDesde,
            Date fechaHasta, Depositos deposito, Proveedores proveedor,
            Sublineas sublinea, Lineas linea, CanalesVenta canal,
            Boolean sinIva, String discriminado) throws SQLException {
        String sql = "";
        String dateFormat = "yyyy/MM/dd";
        String intoMostrar21 = "INTO #MOSTRAR21 ";
        switch (discriminado) {
            case "M":
                if (sinIva) {
                    sql += "SELECT "
                        + "    d.cod_merca as codigo, m.xdesc as xdesc, m.nrelacion, m.npeso_caja, m.npeso_unidad, "  
                        + "    (SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) AS cajas_ventas, " 
                        + "    ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0) AS unid_ventas, " 
                        + "    SUM(d.igravadas+d.iexentas) as ttotal, ((SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) ) * m.npeso_caja AS peso_cajas, " 
                        + "    (ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion, 0)) * m.npeso_unidad AS peso_unid " 
                        + intoMostrar21
                        + "FROM movimientos_merca d, mercaderias m, sublineas s, #tmp_mercaderias t , merca_canales mc ";
                } else {
                    sql += "SELECT "
                        + "    d.cod_merca as codigo, m.xdesc as xdesc, m.nrelacion, m.npeso_caja, m.npeso_unidad, "  
                        + "    (SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) AS cajas_ventas, " 
                        + "    ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0) AS unid_ventas, " 
                        + "    SUM(d.igravadas+d.iexentas+d.impuestos) as ttotal, ((SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) ) * m.npeso_caja AS peso_cajas, " 
                        + "    (ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0)) * m.npeso_unidad AS peso_unid "
                        + intoMostrar21
                        + "FROM movimientos_merca d, mercaderias m, sublineas s, #tmp_mercaderias t, merca_canales mc ";
                }
                break;
            case "SL":
                if (sinIva) {
                    sql += "SELECT "
                        + "    m.cod_sublinea as codigo, s.xdesc as xdesc, m.nrelacion, m.npeso_caja, m.npeso_unidad, "  
                        + "    (SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) AS cajas_ventas, "
                        + "    ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0) AS unid_ventas, " 		
                        + "    SUM(d.igravadas+d.iexentas) as ttotal, ((SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) ) * m.npeso_caja AS peso_cajas, " 
                        + "    (ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0)) * m.npeso_unidad AS peso_unid " 
                        + intoMostrar21
                        + "FROM movimientos_merca d, mercaderias m, sublineas s, #tmp_mercaderias t, merca_canales mc ";
                } else {
                    sql += "SELECT "
                        + "    m.cod_sublinea as codigo, s.xdesc as xdesc, m.nrelacion, m.npeso_caja, m.npeso_unidad, " 
                        + "    (SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) AS cajas_ventas, "
                        + "    ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0) AS unid_ventas, "		
                        + "    SUM(d.igravadas+d.iexentas+d.impuestos)  as ttotal, ((SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) ) * M.NPESO_CAJA AS peso_cajas, "
                        + "    (ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0)) * m.npeso_unidad AS peso_unid "
                        + intoMostrar21
                        + "FROM movimientos_merca d, mercaderias m, sublineas s, #tmp_mercaderias t , merca_canales mc ";
                }
                break;
            case "L":
                if (sinIva) {
                    sql += "SELECT "
                        + "    s.cod_linea as codigo, l.xdesc as xdesc, m.nrelacion, m.npeso_caja, m.npeso_unidad, "
                        + "    (SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) AS cajas_ventas, "
                        + "    ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0) AS unid_ventas, "		
                        + "    SUM(d.igravadas+d.iexentas)  as ttotal, ((SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) ) * M.NPESO_CAJA AS peso_cajas, "
                        + "    (ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (- 1) * m.nrelacion,0)) * m.npeso_unidad AS peso_unid "
                        + intoMostrar21
                        + "FROM movimientos_merca d, mercaderias m, sublineas s, lineas l, #tmp_mercaderias t, merca_canales mc ";
                } else {
                    sql += "SELECT "
                        + "    s.cod_linea as codigo, l.xdesc as xdesc, m.nrelacion, m.npeso_caja, m.npeso_unidad, "
                        + "    (SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (-1) AS cajas_ventas, "
                        + "    ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (-1) * m.nrelacion,0) AS unid_ventas, "		
                        + "    SUM(d.igravadas+d.iexentas+d.impuestos) as ttotal, ((SUM(d.cant_cajas) + ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (-1) ) * m.npeso_caja AS peso_cajas, "
                        + "    (ROUND((SUM(d.cant_unid) / m.nrelacion - ROUND(SUM(d.cant_unid) / m.nrelacion, 0, 1)) * (-1) * m.nrelacion,0)) * m.npeso_unidad AS peso_unid "
                        + intoMostrar21
                        + "FROM movimientos_merca d, mercaderias m, sublineas s, lineas l, #tmp_mercaderias t, merca_canales mc ";
                }
        }
        sql += "WHERE d.cod_empr = 2 "
            + "AND d.cod_empr = m.cod_empr "
            + "AND d.cod_merca = m.cod_merca "
            + "AND m.cod_sublinea = s.cod_sublinea "
            + "AND d.cod_merca = mc.cod_merca "
            + "AND d.ctipo_docum IN ('FCO','FCR','CPV','NCV') "
            + "AND (d.fmovim BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat) + "' "
            + "AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "') ";
        if (Objects.nonNull(proveedor)) {
            sql += "AND m.cod_proveed = " + proveedor.getCodProveed() + " ";
        }
        if (Objects.nonNull(sublinea)) {
            sql += "AND m.cod_sublinea = " + sublinea.getCodSublinea() + " ";
        }
        if (Objects.nonNull(linea)) {
            sql += "AND s.cod_linea = " + linea.getCodLinea() + " ";
        }
        if (Objects.nonNull(deposito)) {
            sql += "AND d.cod_depo = " + deposito.getDepositosPK().getCodDepo() + " ";
        }
        if (Objects.nonNull(canal)) {
            sql += "AND mc.cod_canal = '" + canal.getCodCanal() + "' ";
        }
        sql += "AND m.cod_merca COLLATE DATABASE_DEFAULT = t.cod_merca COLLATE DATABASE_DEFAULT ";
        switch(discriminado) {
            case "M":
                sql += "GROUP BY d.cod_merca, m.xdesc,"
                    + "m.nrelacion, m.npeso_caja, m.npeso_unidad ";
                break;
            case "SL":
                sql += "GROUP BY m.cod_sublinea, s.xdesc,"
                    + "m.nrelacion, m.npeso_caja, m.npeso_unidad ";
                break;
            case "L":
                sql += "AND s.cod_linea = l.cod_linea "
                    + "GROUP BY s.cod_linea, l.xdesc, "
                    + "m.nrelacion, m.npeso_caja, m.npeso_unidad ";
        }
        // Generamos la tabla temporal mostrar21
        stmt.execute(sql);
        // Generamos la tabla temporal mostrar agrupado por codigo
        // de mercaderia y descripcion
        sql = "SELECT "
            + "    codigo, xdesc, "
            + "    SUM(cajas_ventas) as cajas_ventas, " 
            + "    SUM(unid_ventas) as unid_ventas , "   
            + "    SUM(ttotal)  as ttotal, " 
            + "    SUM(peso_cajas) as peso_vcajas, " 
            + "    SUM(peso_unid) as peso_vunid " 
            + "INTO #MOSTRAR2 "
            + "FROM #MOSTRAR21 " 
            + "GROUP BY codigo, xdesc ";
        stmt.execute(sql);
    }
    
    public List<Object[]> listarParaExcel(Statement stmt, String[] columnas, String sql){
        List<Object[]> lista = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Object[] row = new Object[columnas.length];
                for (int i = 0; i < columnas.length; i++) {
                  row[i] = rs.getObject(columnas[i]);
                }
                lista.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ComprasVentasFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public void insertarMercaderiasSeleccionadas(Statement stmt,
            List<Mercaderias> mercaderias, List<Mercaderias> mercaderiasActivas) throws SQLException {
        stmt.execute("CREATE TABLE #tmp_mercaderias ( "
                    + "cod_merca CHAR(13), "
                    + "cod_barra CHAR(20), "
                    + "xdesc CHAR(50), "
                    + "nrelacion SMALLINT, "
                    + "cant_cajas integer, "
                    + "cant_unid integer "
                    + ")");
        if (mercaderias.size() > 0) {
            Mercaderias aux;
            for (Mercaderias m : mercaderias) {
                aux = mercaderiasActivas.stream()
                        .filter(ma -> ma.equals(m))
                        .findAny()
                        .orElse(null);
                if(Objects.nonNull(aux)){
                    stmt.execute("INSERT INTO #tmp_mercaderias (cod_merca, "
                        + "cod_barra, xdesc, nrelacion,cant_cajas, cant_unid ) "
                        + "VALUES ('" + aux.getMercaderiasPK().getCodMerca()
                        + "', '" + aux.getCodBarra() + "', '" + aux.getXdesc()
                        + "', " + aux.getNrelacion() + ",0,0 )");
                }
            }
        } else {
            stmt.execute("INSERT INTO #tmp_mercaderias "
                    + "SELECT m.cod_merca, m.cod_barra, m.xdesc, m.nrelacion, 1 as cant_cajas, 1 as cant_unid "
                    + "FROM mercaderias m, existencias e "
                    + "WHERE m.cod_merca = e.cod_merca "
                    + "AND m.mestado = 'A' "
                    + "AND e.cod_depo = 1");
        }
    }

    private void mergeTmpCompraVenta(Statement stmt) throws SQLException {
        String sql = "";
        // las mercaderías que tienen ventas y compras deben aparecer solo 1 vez
        sql += "SELECT "
            + "    m.codigo, m.xdesc, m.cajas_compras, m.unid_compras, " 
            + "    m.ttotal as tot_compras, m.peso_ccajas, m.peso_cunid, " 
            + "    m2.cajas_ventas, m2.unid_ventas, m2.ttotal as tot_ventas, "
            + "    m2.peso_vcajas, m2.peso_vunid "
            + "INTO #CUR_AMBOS "
            + "FROM #MOSTRAR m, #MOSTRAR2 m2 "
            + "WHERE m.codigo = m2.codigo ";
        stmt.execute(sql);
        // las mercaderías que solo tuvieron compras
        sql = "SELECT "
            + "    m.codigo, m.xdesc, m.cajas_compras, m.unid_compras, "
            + "    m.ttotal as tot_compras, m.peso_ccajas, m.peso_cunid, "
            + "    0 as cajas_ventas, 0 as unid_ventas, 0 as tot_ventas, "
            + "    0 as peso_vcajas, 0 as peso_vunid "
            + "INTO #CUR_COMPRAS "
            + "FROM #MOSTRAR m "
            + "WHERE codigo NOT IN (SELECT DISTINCT codigo FROM #MOSTRAR2) ";
        stmt.execute(sql);
        // las mercaderias que solo tuvieron ventas
        sql = "SELECT "
            + "    m2.codigo, m2.xdesc, 0 as cajas_compras, 0 as unid_compras, "
            + "    0 as tot_compras, 0 as peso_ccajas, 0 as peso_cunid, "
            + "    m2.cajas_ventas, m2.unid_ventas, m2.ttotal as tot_ventas, "
            + "    m2.peso_vcajas, m2.peso_vunid "
            + "INTO #CUR_VENTAS "
            + "FROM #MOSTRAR2 m2 "
            + "WHERE codigo NOT IN (SELECT DISTINCT codigo FROM #MOSTRAR) ";
        stmt.execute(sql);
        // unión de todos los datos
        sql = "SELECT "
            + "    codigo, xdesc, cajas_compras, unid_compras, "
            + "    tot_compras, peso_ccajas, peso_cunid, "
            + "    cajas_ventas,  unid_ventas, tot_ventas, "
            + "    peso_vcajas, peso_vunid "
            + "INTO #ULTIMO "
            + "FROM #CUR_AMBOS ";
        stmt.execute(sql);
        sql = "INSERT INTO #ULTIMO "
            + "SELECT "
            + "    codigo, xdesc, cajas_compras, unid_compras, "
            + "    tot_compras, peso_ccajas, peso_cunid, "
            + "    cajas_ventas,  unid_ventas, tot_ventas, "
            + "    peso_vcajas, peso_vunid "
            + "FROM #CUR_COMPRAS ";
        stmt.execute(sql);
        sql = "INSERT INTO #ULTIMO "
            + "SELECT "
            + "    codigo, xdesc, cajas_compras, unid_compras, "
            + "    tot_compras, peso_ccajas, peso_cunid, "
            + "    cajas_ventas,  unid_ventas, tot_ventas, "
            + "    peso_vcajas, peso_vunid "
            + "FROM #CUR_VENTAS ";
        stmt.execute(sql);
    }

    public String generateSelectUltimo() {
        String sql = "SELECT "
                    + "    codigo, xdesc, cajas_compras, unid_compras, "
                    + "    tot_compras, peso_ccajas, peso_cunid, "
                    + "    cajas_ventas,  unid_ventas, tot_ventas, "
                    + "    peso_vcajas, peso_vunid "
                    + "FROM #ULTIMO "
                    + "ORDER BY codigo";
        return sql;
    }
}
