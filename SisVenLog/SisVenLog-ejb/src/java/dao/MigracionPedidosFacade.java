/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.CanalesVenta;
import entidad.Empleados;
import entidad.Pedidos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.DateUtil;

/**
 *
 * @author Arsenio
 */
@Stateless
public class MigracionPedidosFacade extends AbstractFacade<Pedidos> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MigracionPedidosFacade() {
        super(Pedidos.class);
    }

    public String generateSQLPedCur(Date fechaInicial, Date fechaFinal, String estado,
            Empleados vendedor, CanalesVenta canalVenta) {
        String sql = ""
                + "SELECT        tp.vencod, "
                + "		 tp.facnro, "
                + "		 tp.codnuevo, "
                + "		 CONVERT(date,tp.facfecha,103) AS facfecha, "
                + "		 tp.factipovta, "
                + "		 tp.forpago, "
                + "		 tp.nplazo_cheque, "
                + "		 tp.artcod, "
                + "		 tp.detcancajas, "
                + "		 tp.detcanunid, "
                + "		 tp.detprecio, "
                + "		 tp.estado, "
                + "		 tp.nroped, "
                + "		 c.xnombre, "
                + "		 tp.clicod, "
                + "		 tp.msg_error, "
                + "		 tp.cod_vendedor "
                + " INTO #MIGRADAT "
                + "FROM tmp_pedidos tp LEFT JOIN clientes c "
                + "ON tp.codnuevo = c.cod_cliente "
                + "WHERE CONVERT(DATE, tp.facfecha, 103) BETWEEN '" + DateUtil.dateToString(fechaInicial, "yyyy/MM/dd") + "' "
                + "AND '" + DateUtil.dateToString(fechaFinal, "yyyy/MM/dd") + "' ";
            String lEstado = "";
            switch (estado) {
                case "2":
                    lEstado = " ";
                    break;
                case "3":
                    lEstado = "R";
                    break;
                case "4":
                    lEstado = "M";
                    break;
            }

            if(estado.equals("2") || estado.equals("4")) {
                sql += " AND tp.estado = '" + lEstado + "' ";
            }

            if(estado.equals("3")) {
                sql += " AND ( tp.estado = '" + lEstado + "' OR (tp.estado = 'M' AND tp.msg_error != '')) ";
            }

            if(Objects.nonNull(vendedor)){
                sql += " AND tp.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado()  + " ";
            }

            if(Objects.nonNull(canalVenta)){
                sql += " AND tp.cod_canal = '" + canalVenta.getCodCanal() + "' ";
            }

            sql += "ORDER BY tp.cod_vendedor, tp.nroped ";
                
        return sql;
    }
    
    public String generateSQLCurRec(Date fechaInicial, Date fechaFinal,
            Empleados vendedor, CanalesVenta canalVenta) {
        String sql = "INSERT INTO #MIGRADAT "
                + "SELECT        LEFT(e.xnombre, 24)  as vencod, "
                + "		 p.nro_pedido    as facnro, "
                + "		 p.cod_cliente   as codnuevo, "
                + "		 p.fpedido       as facfecha, "
                + "		 p.ctipo_vta     as factipovta, "
                + "		 p.ctipo_docum   as forpago, "
                + "		 p.nplazo_cheque as nplazo_cheque, "
                + "		 ''              as artcod, "
                + "		 0               as detcancajas, "
                + "		 0               as detcanunid, "
                + "		 0               as detprecio, "
                + "		 p.mestado       as estado, "
                + "		 p.nro_pedido    as nroped, "
                + "		 c.xnombre, "
                + "		 p.cod_cliente   as clicod, "
                + "		 'Nro.Promocion Invalido' as msg_error, "
                + "		 p.cod_vendedor as cod_vendedor "
                + "FROM pedidos p, clientes c, empleados e "
                + "WHERE "
                + "p.cod_empr= 2 "
                + "AND p.cod_vendedor = e.cod_empleado "
                + "AND p.cod_cliente = c.cod_cliente "
                + "AND p.fpedido BETWEEN '" + DateUtil.dateToString(fechaInicial, "yyyy/MM/dd") + "' "
                + "AND '" + DateUtil.dateToString(fechaFinal, "yyyy/MM/dd") + "' "
                + "AND p.mestado = 'R' ";

                if(Objects.nonNull(vendedor)){
                    sql += " AND p.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado()  + " ";
                }

                if(Objects.nonNull(canalVenta)){
                    sql += " AND p.cod_canal = '" + canalVenta.getCodCanal() + "' ";
                }
            sql += " ORDER BY cod_vendedor, nroped";
        return sql;
    }

    public String generateSelectMigradat() {
        String sql = ""
                + "SELECT vencod, facnro, codnuevo, facfecha, factipovta, "
                + "       forpago, nplazo_cheque, artcod, detcancajas, "
                + "       detcanunid, detprecio, estado, nroped, "
                + "       isnull(xnombre, '') as xnombre , clicod, msg_error, cod_vendedor "
                + "FROM #MIGRADAT ";
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
            Logger.getLogger(MigracionPedidosFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
