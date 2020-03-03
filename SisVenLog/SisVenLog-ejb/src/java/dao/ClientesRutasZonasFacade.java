/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import entidad.Rutas;
import entidad.TiposClientes;
import entidad.Zonas;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.DateUtil;
import util.StringUtil;

/**
 *
 * @author Arsenio
 */
@Stateless
public class ClientesRutasZonasFacade extends AbstractFacade<Clientes> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientesRutasZonasFacade() {
        super(Clientes.class);
    }

    public String generateSqlRuteo(Boolean conRuteo, TiposClientes tipoCliente,
            Zonas zona, Rutas ruta, String estado, Date fechaAltaDesde,
            Date fechaAltaHasta, Boolean todosClientes, List<Clientes> listadoClientesSeleccionados) {
        String sql = "";
        if (conRuteo) {
            sql += "SELECT  c.cod_cliente, "
                    + "        c.xpropietario, "
                    + "        c.xnombre, "
                    + "        cod_estado, "
                    + "        c.xruc, "
                    + "        c.xdirec, "
                    + "        r.cod_zona, "
                    + "        z.xdesc as xdesc_zona, "
                    + "        c.cod_ruta, "
                    + "        r.xdesc as xdesc_ruta, "
                    + "        c.ctipo_cliente, "
                    + "        t.xdesc as xdesc_tipo, "
                    + "        c.xtelef ";
        } else {
            sql += "SELECT  c.cod_cliente, "
                    + "        c.xnombre, "
                    + "        c.xdirec, "
                    + "        c.cod_ruta, "
                    + "        r.xdesc as xdesc_ruta ";
        }

        sql += "FROM clientes c, tipos_clientes t, zonas z, rutas r "
                + "WHERE "
                + "    c.ctipo_cliente = t.ctipo_cliente "
                + "    AND c.cod_empr = r.cod_empr "
                + "    AND z.cod_empr = 2 "
                + "    AND r.cod_zona = z.cod_zona "
                + "    AND r.cod_empr = z.cod_empr "
                + "    AND c.cod_ruta = r.cod_ruta ";

        if (Objects.nonNull(tipoCliente)) {
            sql += " AND c.ctipo_cliente = '" + tipoCliente.getCtipoCliente() + "' ";
        }

        if (Objects.nonNull(zona)) {
            sql += " AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
        }

        if (Objects.nonNull(ruta)) {
            sql += " AND c.cod_ruta = " + ruta.getRutasPK().getCodRuta() + " ";
        }

        if (Objects.nonNull(estado)) {
            switch (estado) {
                case "1":
                    sql += " AND cod_estado = 'A' ";
                    break;
                case "2":
                    sql += " AND CONVERT(char(10), c.falta, 103) BETWEEN '"
                            + DateUtil.dateToString(fechaAltaDesde)
                            + "' AND '" + DateUtil.dateToString(fechaAltaHasta) + "' ";
                    break;
                case "3":
                    sql += " AND cod_estado = 'I' ";
                    break;
            }
        }

        if (!todosClientes) {
            if (!listadoClientesSeleccionados.isEmpty()) {
                sql += " AND c.cod_cliente IN (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ")";
            }
        }

        sql += " ORDER BY c.xnombre";
        return sql;
    }
    
}
