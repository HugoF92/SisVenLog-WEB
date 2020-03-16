package dao;

import entidad.Clientes;
import entidad.Empleados;
import entidad.Zonas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DateUtil;
import util.StringUtil;

/**
 *
 * @author Arsenio
 */
@Stateless
public class ClientesCreditosFacade extends AbstractFacade<Clientes> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    public ClientesCreditosFacade() {
        super(Clientes.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public String generateTmpTableMostrar(Zonas zona, Boolean todosClientes,
            List<Clientes> listadoClientesSeleccionados, String estado,
            String modalidadPago) {

        String sql = ""
                + "SELECT c.cod_cliente, r.cod_zona, c.cod_ruta, c.xnombre, "
                + " c.nplazo_credito, c.ilimite_compra, c.cod_estado, c.mforma_pago, "
                + " c.nriesgo, z.xdesc as xdesc_zona, "
                + " c.ctipo_cliente,  e.xdesc as xdesc_estado "
                + " INTO #MOSTRAR " // se guarda en la tabla temporal mostrar
                + " FROM clientes c , zonas z, rutas r, estados_clientes e "
                + " WHERE c.cod_ruta = r.cod_ruta "
                + "   AND c.cod_empr = r.cod_empr "
                + "   AND c.cod_empr = 2 "
                + "   AND r.cod_empr = z.cod_empr "
                + "   AND r.cod_zona = z.cod_zona "
                + "   AND c.cod_estado = e.cod_estado "
                + "   AND c.nplazo_credito > 0 ";

        if (Objects.nonNull(zona)) {
            sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
        }

        if (Objects.nonNull(estado) && !estado.equals("")) {
            sql += " AND c.cod_estado = '" + estado + "' ";
        }

        if (Objects.nonNull(modalidadPago) && !modalidadPago.equals("-1")) {
            sql += " AND c.mforma_pago = '" + modalidadPago + "' ";
        }

        if (!todosClientes) {
            if (!listadoClientesSeleccionados.isEmpty()) {
                sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
            }
        }

        return sql;
    }

    public String generateTmpVentas() {
        String sql = ""
                + "SELECT cod_cliente "
                + "INTO #tmp_ventas "
                + "FROM #MOSTRAR";
        return sql;
    }

    public String generateTmpTableCurfac(Date fechaFacDesde,
            Date fechaFacHasta, Empleados vendedor) {
        String sql = ""
            + "SELECT f.cod_cliente, SUM(f.ttotal) as ttotal "
            + "INTO #CURFAC "
            + "FROM facturas f, #tmp_ventas t "
            + "WHERE mestado = 'A' "
            + "AND f.cod_cliente = t.cod_cliente "
            + "AND f.cod_empr = 2";
        if(fechaFacDesde != null && fechaFacHasta != null){
            sql += "AND f.ffactur BETWEEN '" + DateUtil.dateToString(fechaFacDesde)
                        + "' AND '" + DateUtil.dateToString(fechaFacHasta) + "' ";
        }

        if (Objects.nonNull(vendedor)) {
            sql += " AND f.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado() + " ";
        }
        
        sql += " GROUP BY f.cod_cliente ";

        return sql;
    }

    public String generateTmpTableCurDatosND(Empleados vendedor) {
        String sql = ""
                + "SELECT '' AS cod_zona, '' AS xdesc_zona, m.cod_cliente , "
                + "       m.xnombre, m.nriesgo, m.mforma_pago, "
                + "       m.nplazo_credito, m.ilimite_compra, m.cod_estado, "
                + "       m.xdesc_estado, f.ttotal "
                + "INTO #CURDATOS ";
        if (Objects.isNull(vendedor)) {
            sql += "FROM #MOSTRAR m LEFT JOIN #CURFAC f ";
        } else {
            sql += "FROM #MOSTRAR m INNER JOIN #CURFAC f ";
        }

        sql += "     ON m.cod_cliente = f.cod_cliente "
                + "ORDER BY  m.xnombre";
        return sql;
    }

    public String generateTmpTableCurDatosPZ(Empleados vendedor) {
        String sql = ""
                + "SELECT m.cod_zona, m.xdesc_zona, m.cod_cliente , "
                + "       m.xnombre, m.nriesgo, m.mforma_pago, "
                + "       m.nplazo_credito, m.ilimite_compra, m.cod_estado, "
                + "       m.xdesc_estado, f.ttotal "
                + "INTO #CURDATOS "
                + "FROM      #MOSTRAR m ";
        if (Objects.isNull(vendedor)) {
            sql += "LEFT JOIN #CURFAC f ";
        } else {
            sql += "INNER JOIN #CURFAC f ";
        }
        sql += "ON        m.cod_cliente = f.cod_cliente "
                + "ORDER BY  m.cod_zona, m.xnombre";
        return sql;
    }

    public String generateSelectCurDatos() {
        String sql = ""
                + "SELECT cod_zona, xdesc_zona, cod_cliente , "
                + "       xnombre, nriesgo, mforma_pago, "
                + "       nplazo_credito, ilimite_compra, cod_estado, "
                + "       xdesc_estado, ttotal "
                + "FROM #CURDATOS ";
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
            Logger.getLogger(ClientesCreditosFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
