/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.CanalesVenta;
import entidad.Empleados;
import entidad.Pedidos;
import java.util.Date;
import java.util.Objects;
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

    public String generateSql(Date fechaInicial, Date fechaFinal, String estado,
            Empleados vendedor, CanalesVenta canalVenta) {
        String sql = ""
                    + "SELECT    tp.vencod, "
                    + "		 tp.facnro, "
                    + "		 tp.codnuevo, "
                    + "		 tp.facfecha, "
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
                    + "		 tp.cod_vendedor    as cod_vendedor "
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
                sql += " AND estado = '" + lEstado + "' ";
            }

            if(estado.equals("3")) {
                sql += " AND ( estado = '" + lEstado + "' OR (estado = 'M' AND msg_error != '')) ";
            }

            if(Objects.nonNull(vendedor)){
                sql += " AND tp.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado()  + " ";
            }

            if(Objects.nonNull(canalVenta)){
                sql += " AND tp.cod_canal = '" + canalVenta.getCodCanal() + "' ";
            }

//            sql += "ORDER BY cod_vendedor, nroped ";

            // Si selecciono los estados de (Todos, Rechazados)
            if(estado.equals("1") || estado.equals("3")) {
                sql += " UNION "
                    + " "
                    + "SELECT   e.xnombre       as vencod, "
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
                    + "AND p.fpedido BETWEEN '" + DateUtil.dateToString(fechaInicial, "yyyy/dd/MM HH:mm:ss") + "' "
                    + "AND '" + DateUtil.dateToString(fechaFinal, "yyyy/dd/MM HH:mm:ss") + "' "
                    + "AND p.mestado = 'R' ";

                    if(Objects.nonNull(vendedor)){
                        sql += " AND p.cod_vendedor = " + vendedor.getEmpleadosPK().getCodEmpleado()  + " ";
                    }

                    if(Objects.nonNull(canalVenta)){
                        sql += " AND p.cod_canal = '" + canalVenta.getCodCanal() + "' ";
                    }
            }

            sql += " ORDER BY cod_vendedor, nroped";
        return sql;
    }
}
