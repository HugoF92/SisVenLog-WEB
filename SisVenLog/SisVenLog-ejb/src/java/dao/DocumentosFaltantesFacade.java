package dao;

import entidad.TiposDocumentos;
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
import javax.ejb.Stateless;
import util.DateUtil;


/**
 *
 * @author Arsenio
 */
@Stateless
public class DocumentosFaltantesFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void generateSqlCurdoc(Statement stmt, Long nroDesde, Long nroHasta,
            TiposDocumentos tipoDocumento, Date fechaInicial) throws SQLException {
        String sql = "";
        String intoCurDoc = " INTO #CURDOC ";
        String dateFormat = "yyyy/MM/dd";
        switch(tipoDocumento.getCtipoDocum().trim()){
            case "FAC":
                sql = " SELECT nrofact as ndocum "
                    + intoCurDoc
                    + " FROM facturas f "
                    + " WHERE f.cod_empr = 2  "
	            + " AND ffactur >= '" + DateUtil.dateToString(fechaInicial, dateFormat) + "' "
	            + " AND nrofact BETWEEN " + nroDesde + " AND " + nroHasta;
                break;
            case "FCR":
            case "FCO":
            case "CPV":
                sql = " SELECT nrofact as ndocum "
                    + intoCurDoc
                    + " FROM facturas f "
                    + " WHERE f.ctipo_docum = '" + tipoDocumento.getCtipoDocum()+ "' "
                    + " AND f.cod_empr = 2  "
	            + " AND ffactur >= '" + DateUtil.dateToString(fechaInicial, dateFormat) + "' "
	            + " AND nrofact BETWEEN " + nroDesde + " AND " + nroHasta;
                break;
            case "NCV":
            case "NDV":
                sql = " SELECT nro_nota as ndocum "
                    + intoCurDoc
                    + " FROM notas_ventas f "
                    + " WHERE f.ctipo_docum = '" + tipoDocumento.getCtipoDocum()+ "' "
                    + " AND f.cod_empr = 2  "
	            + " AND f.fdocum >= '" + DateUtil.dateToString(fechaInicial, dateFormat)+ "' "
	            + " AND nro_nota BETWEEN " + nroDesde + " AND " + nroHasta;
                break;
            case "EN":
                sql = " SELECT nro_envio as ndocum "
                    + intoCurDoc
                    + " FROM envios f "
                    + " WHERE f.cod_empr = 2  "
	            + " AND f.fenvio >= '" + DateUtil.dateToString(fechaInicial, dateFormat) + "' "
	            + " AND f.nro_envio BETWEEN " + nroDesde + " AND " + nroHasta;
                break;
            case "REC":
                sql = " SELECT nrecibo as ndocum "
                    + intoCurDoc
                    + " FROM recibos f "
                    + " WHERE f.cod_empr = 2  "
	            + " AND f.frecibo >= '" + DateUtil.dateToString(fechaInicial, dateFormat)+ "' "
	            + " AND f.nrecibo BETWEEN " + nroDesde + " AND " + nroHasta;
                break;
        }
        stmt.execute(sql);
    }

    public void generateTableNumbers(Statement stmt, Long nroDesde, Long nroHasta) throws SQLException {
        String sql = ""
                + "CREATE TABLE #NUMBERS "
                + "( "
                + "	ndocum numeric(15) "
                + ")";
        stmt.execute(sql);
        //Poblamos la tabla temporal
        for(Long i = nroDesde; i <= nroHasta; i++){
            stmt.execute("INSERT INTO #NUMBERS values(" + i + ")");
        }
    }

    public void generateTableMostrar(Statement stmt) throws SQLException {
        String sql = ""
            + "SELECT ndocum "
            + "INTO #MOSTRAR "
            + "FROM #NUMBERS "
            + "WHERE ndocum NOT IN (select ndocum FROM #CURDOC)";
        stmt.execute(sql);
    }

    public String generateSelectMostrar(){
        String sql = ""
            + "SELECT ndocum "
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
            Logger.getLogger(DocumentosFaltantesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
