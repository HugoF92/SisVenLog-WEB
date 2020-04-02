package dao;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Mercaderias;
import entidad.Rutas;
import entidad.Sublineas;
import entidad.Zonas;
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
public class ClientesNoCompranFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateTableMostrar(Statement stmt, Date fechaDesde, Date fechaHasta,
            Zonas zona, Rutas ruta, String estado, Lineas linea, Empleados vendedor) throws SQLException {
        String sql = "";
        String dateFormat = "yyyy/MM/dd";
        sql += "SELECT DISTINCT r.cod_zona, z.xdesc AS xdesc_zona, c.cod_cliente, c.xnombre, c.xdirec, "
        + " c.xtelef, c.cod_estado "
        + " INTO #MOSTRAR "
        + "FROM rutas r, zonas z, clientes c , empleados_zonas ez "
        + "WHERE "
        + "r.cod_zona = z.cod_zona "
        + "AND c.cod_ruta = r.cod_ruta "
        + "AND ez.cod_zona = z.cod_zona "
        + "AND c.cod_cliente NOT IN "
        + "(SELECT cod_cliente "
        + " FROM facturas f, facturas_det d, mercaderias m, #TMP_SUBLINEAS t, #TMP_MERCADERIAS tm, sublineas s, lineas li "
        + " WHERE f.nrofact = d.nrofact "
        + " AND f.cod_empr= 2 "
        + " AND d.cod_empr= 2 "
        + " AND f.mestado = 'A' "
        + " AND (f.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat) + "' "
        + " AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "') "
        + " AND f.ctipo_docum IN ('FCR','FCO') "
        + " AND d.cod_merca = m.cod_merca "
        + " AND t.cod_sublinea = s.cod_sublinea "
        + " AND s.cod_linea = li.cod_linea "
        + " AND m.cod_merca COLLATE DATABASE_DEFAULT =  tm.cod_merca COLLATE DATABASE_DEFAULT "
        + " AND m.cod_sublinea = t.cod_sublinea ";
        if (Objects.nonNull(linea)) {
            sql += " AND li.cod_linea = " + linea.getCodLinea() + " ) ";
        } else {
            sql += ") ";
        }

        if (Objects.nonNull(zona)) {
            sql += " AND z.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
        }

        if (Objects.nonNull(ruta)) {
            sql += " AND r.cod_ruta = " + ruta.getRutasPK().getCodRuta();
        }

        if (Objects.nonNull(vendedor)) {
            sql += " AND ez.cod_empleado = " + vendedor.getEmpleadosPK().getCodEmpleado() + " ";
        }
        switch (estado) {
            case "A":
                sql += " AND c.cod_estado = 'A' ";
                break;
            case "I":
                sql += " AND c.cod_estado = 'I' ";
        }

        sql += " ORDER BY r.cod_zona, c.cod_cliente";
        stmt.execute(sql);
    }

    public String generateSelectMostrar(){
        String sql = ""
            + "SELECT cod_zona, "
                + " xdesc_zona, "
                + " cod_cliente, "
                + " xnombre, "
                + " xdirec, "
                + " xtelef,"
                + " cod_estado "
            + "FROM #MOSTRAR ";
        return sql;
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
            Logger.getLogger(ClientesNoCompranFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public void insertarMercaderiasSeleccionadas(Statement stmt,
            List<Mercaderias> mercaderias, List<Mercaderias> mercaderiasActivas) throws SQLException {
        stmt.execute("CREATE TABLE #TMP_MERCADERIAS ( "
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
                    stmt.execute("INSERT INTO #TMP_MERCADERIAS (cod_merca, "
                        + "cod_barra, xdesc, nrelacion,cant_cajas, cant_unid ) "
                        + "VALUES ('" + aux.getMercaderiasPK().getCodMerca()
                        + "', '" + aux.getCodBarra() + "', '" + aux.getXdesc()
                        + "', " + aux.getNrelacion() + ",0,0 )");
                }
            }
        } else {
            stmt.execute("INSERT INTO #TMP_MERCADERIAS "
                    + "SELECT m.cod_merca, m.cod_barra, m.xdesc, m.nrelacion, 1 as cant_cajas, 1 as cant_unid "
                    + "FROM mercaderias m, existencias e "
                    + "WHERE m.cod_merca = e.cod_merca "
                    + "AND m.mestado = 'A' "
                    + "AND e.cod_depo = 1");
        }
    }

    public void insertarSublineasSeleccionadas(Statement stmt,
        List<Sublineas> sublineas, List<Sublineas> sublineasActivas) throws SQLException {
        stmt.execute("CREATE TABLE #TMP_SUBLINEAS ( "
                + "	cod_sublinea smallint, "
                + "	xdesc varchar(50) , "
                + "	cod_linea smallint, "
                + "	cusuario char(30), "
                + "	falta smalldatetime, "
                + "	cusuario_modif char(30), "
                + "	fultim_modif smalldatetime, "
                + "	cod_gcarga smallint "
                + ")");
        if (sublineas.size() > 0) {
            Sublineas aux;
            String dateFormat = "yyyy/MM/dd";
            for (Sublineas s : sublineas) {
                aux = sublineasActivas.stream()
                        .filter(sa -> sa.equals(s))
                        .findAny()
                        .orElse(null);
                if(Objects.nonNull(aux)){
                    stmt.execute("INSERT INTO #TMP_SUBLINEAS (cod_sublinea, xdesc, cod_linea, "
                        + "cusuario, falta, cusuario_modif, fultim_modif, cod_gcarga) "
                        + "VALUES (" + aux.getCodSublinea()+",'" + aux.getXdesc() + "', "
                        + aux.getCodLinea().getCodLinea() + ", "
                        + "'" + aux.getCusuario() + "', "
                        + "'" + DateUtil.dateToString(aux.getFalta(), dateFormat) + "', "
                        + "'" + aux.getCusuarioModif() + "', "
                        + "'" + DateUtil.dateToString(aux.getFultimModif(), dateFormat) + "', "
                        + aux.getCodGcarga().getCodGcarga() + " )");
                }
            }
        }
    }
}
