package dao;

import entidad.CanalesCompra;
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
public class VencProveedoresFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateTableMostrar(Statement stmt, Date fechaDesde,
            Date fechaHasta, String discriminado, String tipoDocumento,
            Proveedores proveedor, CanalesCompra canalCompra) throws SQLException {
        String sql;
        //TODO: cambiar fecha para entorno local yyyy/dd/MM y para el server
        // de prueba yyyy/MM/dd
        String dateFormat = "yyyy/MM/dd";
        if ("FAC".equals(tipoDocumento) || "".equals(tipoDocumento)) {
            sql = "SELECT "
                    + "    t.cod_proveed, p.ctipo_docum AS ctipo_docum, t.xnombre, "
                    + "    convert(char(12),p.nrofact) as nrofact, p.fvenc, "
                    + "    p.ffactur, p.fprov, p.ccanal_compra, m.xdesc AS xdesc_canal, "
                    + "    p.ttotal, p.isaldo AS ideuda "
                    + "INTO #MOSTRAR1 "
                    + "FROM compras p LEFT OUTER JOIN canales_compra m "
                    + "ON p.ccanal_compra = m.ccanal_compra INNER JOIN proveedores t "
                    + "ON p.cod_proveed = t.cod_proveed "
                    + "WHERE (p.fvenc IS NOT NULL) "
                    + "AND p.fvenc BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat)
                    + "' AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' "
                    + "AND (p.isaldo != 0) "
                    + "AND p.mestado = 'A' "
                    + "AND p.cod_empr = 2 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND p.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            if (Objects.nonNull(canalCompra)) {
                sql += "AND p.ccanal_compra = '" + canalCompra.getCanalesCompraPK().getCcanalCompra() + "' ";
            }
            sql += "GROUP BY  t.cod_proveed, p.ctipo_docum, p.nrofact, t.xnombre,"
                    + "    p.fvenc, p.ffactur, p.fprov, p.ccanal_compra, m.xdesc,"
                    + "    p.ttotal, p.isaldo";
            stmt.execute(sql);
        }
        if ("CHE".equals(tipoDocumento) || "".equals(tipoDocumento)) {
            // Inicializamos la fecha 01/01/2006
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2006);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, 0);
            Date fecha01012006 = calendar.getTime();
            sql = "SELECT "
                    + "    t.cod_proveed, 'CHQ' AS ctipo_docum, t.xnombre, e.nro_cheque as nrofact, "
                    + "    e.fcheque as fvenc, e.fcheque as ffactur, e.femision as fprov, "
                    + "    ' ' as ccanal_compra, '' as xdesc_canal, e.icheque as ttotal,  e.icheque * (-1) as ideuda "
                    + "INTO #MOSTRAR2 "
                    + "FROM cheques_emitidos e, proveedores t "
                    + "WHERE  t.cod_proveed = e.cod_proveed "
                    + "AND e.femision BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat)
                    + "' AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' "
                    + "AND e.femision > '" + DateUtil.dateToString(fecha01012006, dateFormat) + "' "
                    + "AND e.cod_empr = 2 "
                    + "AND e.fcobro IS NULL "
                    + "AND NOT exists (SELECT * FROM recibos_prov_cheques r "
                    + "                WHERE r.nro_cheque = e.nro_cheque "
                    + "                AND r.cod_banco = e.cod_banco "
                    + "                AND r.cod_proveed = e.cod_proveed) "
                    + "AND NOT exists (SELECT * FROM cheques_emitidos_det r1 "
                    + "                WHERE r1.nro_cheque = e.nro_cheque "
                    + "                AND r1.cod_banco = e.cod_banco "
                    + "AND r1.cod_proveed = e.cod_proveed) ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND e.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            stmt.execute(sql);
            sql = "INSERT INTO #MOSTRAR2 "
                    + "SELECT "
                    + "    t.cod_proveed, 'CHQ' AS ctipo_docum, t.xnombre, e.nro_cheque as nrofact, "
                    + "    e.fcheque as fvenc, e.fcheque as ffactur, e.femision as fprov, "
                    + "    ' ' as ccanal_compra, '' as xdesc_canal, e.icheque as ttotal, e.icheque as ideuda "
                    + "FROM cheques_emitidos e, proveedores t "
                    + "WHERE t.cod_proveed = e.cod_proveed "
                    + "AND e.femision BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat)
                    + "' AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' "
                    + "AND e.femision > '" + DateUtil.dateToString(fecha01012006, dateFormat) + "' "
                    + "AND e.cod_empr = 2 "
                    + "AND e.fcobro IS NULL "
                    + "AND (exists (SELECT * FROM recibos_prov_cheques r "
                    + "            WHERE r.nro_cheque = e.nro_cheque  "
                    + "            AND r.cod_banco = e.cod_banco "
                    + "            and r.cod_proveed = e.cod_proveed) "
                    + "    OR exists (SELECT * FROM cheques_emitidos_det r1 "
                    + "                WHERE r1.nro_cheque = e.nro_cheque "
                    + "                AND r1.cod_banco = e.cod_banco "
                    + "                AND r1.cod_proveed = e.cod_proveed)) ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND e.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            stmt.execute(sql);
        }
        if ("".equals(tipoDocumento)) {
            sql = "SELECT "
                    + "    t.cod_proveed, p.ctipo_docum AS ctipo_docum, t.xnombre, "
                    + "    convert(char(12),p.nro_nota) as nrofact, p.fdocum as fvenc, "
                    + "    p.fdocum as ffactur, p.fdocum as fprov, '' as ccanal_compra, "
                    + "    ''  AS xdesc_canal, p.ttotal, p.isaldo * (-1) AS ideuda "
                    + "INTO #MOSTRAR3 "
                    + "FROM notas_compras p INNER JOIN proveedores t "
                    + "    ON p.cod_proveed = t.cod_proveed "
                    + "WHERE p.isaldo != 0 "
                    + "AND p.mestado = 'A' "
                    + "AND p.ctipo_docum  = 'NCC' "
                    + "AND (p.nrofact = 0 OR p.nrofact is null) "
                    + "AND p.cod_empr = 2 ";
            if (Objects.nonNull(proveedor)) {
                sql += "AND p.cod_proveed = " + proveedor.getCodProveed() + " ";
            }
            stmt.execute(sql);
        }
        this.joinTempTables(stmt, tipoDocumento, discriminado);
    }

    private void joinTempTables(Statement stmt, String tipoDocumento,
            String discriminado) throws SQLException {
        String sql;
        switch (tipoDocumento) {
            case "":
                sql = "SELECT "
                        + "    cod_proveed, xnombre, ctipo_docum, nrofact, "
                        + "    fvenc, ffactur, fprov, '' as ccanal_compra, "
                        + "    '' as xdesc_canal, ttotal, ideuda   "
                        + "INTO #MOSTRAR "
                        + "FROM #MOSTRAR1 ";
                stmt.execute(sql);
                sql = "INSERT INTO #MOSTRAR "
                        + "SELECT "
                        + "    cod_proveed, xnombre, ctipo_docum, nrofact, "
                        + "    fvenc, ffactur, fprov, '' as ccanal_compra, "
                        + "    '' as xdesc_canal, ttotal, ideuda "
                        + "FROM #MOSTRAR2 ";
                stmt.execute(sql);
                sql = "INSERT INTO #MOSTRAR "
                        + "SELECT "
                        + "    cod_proveed, xnombre, ctipo_docum, nrofact, "
                        + "    fvenc, ffactur, fprov, '' as ccanal_compra, "
                        + "    '' as xdesc_canal, ttotal, ideuda "
                        + "FROM #MOSTRAR3 ";
                stmt.execute(sql);
                break;
            case "FAC":
                sql = "SELECT "
                        + "    cod_proveed, xnombre, ctipo_docum, nrofact, "
                        + "    fvenc, ffactur, fprov, '' as ccanal_compra, "
                        + "    '' as xdesc_canal, ttotal, ideuda "
                        + "INTO #MOSTRAR "
                        + "FROM #MOSTRAR1 ";
                stmt.execute(sql);
                break;
            case "CHE":
                sql = "SELECT "
                        + "    cod_proveed, xnombre, ctipo_docum, nrofact, "
                        + "    fvenc, ffactur, fprov, '' as ccanal_compra, "
                        + "    '' as xdesc_canal, ttotal, ideuda "
                        + "INTO #MOSTRAR "
                        + "FROM #MOSTRAR2 ";
                stmt.execute(sql);
                break;
        }
        if ("ND".equals(discriminado)) { // No discriminar
            sql = "SELECT "
                    + "    cod_proveed as cod_proveed2, xnombre as xnombre2, '' as cod_proveed, "
                    + "	'' as xnombre, ctipo_docum, nrofact, ccanal_compra, xdesc_canal, "
                    + "    ffactur, fprov, fvenc, ttotal, ideuda "
                    + "INTO #MOSDATOS "
                    + "FROM #MOSTRAR "
                    + "WHERE ideuda != 0 "
                    + "ORDER BY fvenc, cod_proveed ";
        } else { // discriminado por proveedor
            sql = "SELECT * , cod_proveed as cod_proveed2, xnombre as xnombre2 "
                    + "INTO #MOSDATOS "
                    + "FROM #MOSTRAR "
                    + "WHERE ideuda != 0 "
                    + "ORDER BY cod_proveed, fvenc ";
        }
        stmt.execute(sql);
    }

    public String generateSelectMosDatos() {
        String sql = ""
                + "SELECT "
                + "cod_proveed, "
                + "xnombre, "
                + "cod_proveed2, "
                + "xnombre2, "
                + "ctipo_docum, "
                + "nrofact, "
                + "ccanal_compra, "
                + "xdesc_canal, "
                + "ffactur, "
                + "fprov, "
                + "fvenc, "
                + "ttotal, "
                + "ideuda "
                + "FROM #MOSDATOS ";
        return sql;
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
            Logger.getLogger(VencProveedoresFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
