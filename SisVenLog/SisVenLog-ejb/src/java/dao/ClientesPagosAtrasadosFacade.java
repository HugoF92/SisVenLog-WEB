package dao;

import entidad.Clientes;
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
public class ClientesPagosAtrasadosFacade extends AbstractFacade<Clientes> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    public ClientesPagosAtrasadosFacade() {
        super(Clientes.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateTmpTableMostrar(Statement stmt, Zonas zona,
            String discriminar, Date fechaDesde, Date fechaHasta,
            Boolean todosClientes,List<Clientes> listadoClientesSeleccionados) throws SQLException {

        String sql = ""
                + "SELECT ru.cod_zona, "
                + "       f.cod_cliente, "
                + "       f.xrazon_social                 AS xnombre, "
                + "       c.nplazo_credito, "
                + "       f.nrofact                       AS ndocum, "
                + "       f.ctipo_docum, "
                + "       f.ttotal                        AS idocum, "
                + "       r.iefectivo, "
                + "       r.icheques, "
                + "       f.ffactur                       AS fdocum, "
                + "       f.fvenc                         AS fvenc, "
                + "       r.frecibo                       AS fpago, "
                + "       DATEDIFF(d, f.fvenc, r.frecibo) AS dias_dif "
                + "INTO #DATOS "
                + "FROM   facturas f "
                + "       INNER JOIN recibos_det d "
                + "               ON f.nrofact = d.ndocum "
                + "                  AND f.ctipo_docum = d.ctipo_docum "
                + "                  AND f.ffactur = d.ffactur "
                + "       INNER JOIN recibos r "
                + "               ON d.nrecibo = r.nrecibo "
                + "                  AND f.fvenc < r.frecibo "
                + "       INNER JOIN clientes c "
                + "               ON f.cod_cliente = c.cod_cliente, "
                + "       rutas ru "
                + "WHERE  ( r.frecibo BETWEEN '" + DateUtil.dateToString(fechaDesde) + "' "
                + "       AND '" + DateUtil.dateToString(fechaHasta) + "') "
                + "       AND ( f.cod_empr = 2 ) "
                + "       AND ( d.cod_empr = 2 ) "
                + "       AND ( r.cod_empr = 2 ) "
                + "       AND c.cod_ruta = ru.cod_ruta "
                + "       AND DATEDIFF(d, fvenc, r.frecibo) > 0";

        if (Objects.nonNull(zona)) {
            sql += " AND ru.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
        }

        if (!todosClientes) {
            if (!listadoClientesSeleccionados.isEmpty()) {
                sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
            }
        }

        stmt.execute(sql);
        
        sql = "INSERT INTO #DATOS "
               + "SELECT r.cod_zona, "
               + "       h.cod_cliente, "
               + "       c.xnombre, "
               + "       c.nplazo_credito, "
               + "       h.nro_cheque                                          AS ndocum, "
               + "       'CHQ'                                                 AS ctipo_docum, "
               + "       h.icheque                                             AS idocum, "
               + "       0                                                     AS iefectivo, "
               + "       0                                                     AS icheques, "
               + "       h.femision                                            AS fdocum, "
               + "       h.femision                                            AS fvenc, "
               + "       h.fcheque                                             AS fpago, "
               + "       DATEDIFF(d, h.femision, h.fcheque) - c.nplazo_credito AS dias_dif "
               + "FROM   cheques h "
               + "       INNER JOIN rutas r "
               + "                  INNER JOIN clientes c "
               + "                          ON r.cod_ruta = c.cod_ruta "
               + "                             AND r.cod_empr = c.cod_empr "
               + "               ON h.cod_cliente = c.cod_cliente "
               + "WHERE  (h.femision BETWEEN '" + DateUtil.dateToString(fechaDesde) + "' "
               + "       AND '" + DateUtil.dateToString(fechaHasta) + "') "
               + "       AND ( h.cod_empr = 2 ) "
               + "       AND DATEDIFF(d, h.femision, h.fcheque) > 0 "
               + "       AND DATEDIFF(d, h.femision, h.fcheque) > c.nplazo_credito";
        
        if (Objects.nonNull(zona)) {
            sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
        }

        if (!todosClientes) {
            if (!listadoClientesSeleccionados.isEmpty()) {
                sql += " AND h.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ") ";
            }
        }

        stmt.execute(sql);

        if("PC".equals(discriminar)){
            sql = "SELECT *, dias_dif AS dias_atraso "
                + "INTO   #mostrar "
                + "FROM   #DATOS d "
                + "ORDER  BY d.cod_cliente";
        } else {
            sql = "SELECT *, dias_dif AS dias_atraso "
                + "INTO   #mostrar "
                + "FROM   #DATOS d "
                + "ORDER  BY cod_zona, cod_cliente ";
        }
        
        stmt.execute(sql);
        
    }



    public String generateSelectMostrar() {
        String sql = ""
                + "SELECT cod_zona, cod_cliente, xnombre, nplazo_credito, "
                + "ndocum, ctipo_docum, idocum, iefectivo, icheques, fdocum, "
                + "fvenc, fpago, dias_atraso "
                + "FROM #mostrar ";
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
