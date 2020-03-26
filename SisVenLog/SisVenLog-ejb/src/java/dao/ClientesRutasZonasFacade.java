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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
            sql += "SELECT     c.cod_cliente, "
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

        sql +=    "INTO #MOSTRAR "
                + "FROM clientes c, tipos_clientes t, zonas z, rutas r "
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
                    sql += " AND c.falta BETWEEN '"
                            + DateUtil.dateToString(fechaAltaDesde, "yyyy/dd/MM")
                            + "' AND '" + DateUtil.dateToString(fechaAltaHasta, "yyyy/dd/MM") + "' ";
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

    public String generateSelectMostrar(Boolean conRuteo) {
        String sql = "";
        if (conRuteo) {
            sql += "SELECT     cod_cliente, "
                    + "        xpropietario, "
                    + "        xnombre, "
                    + "        cod_estado, "
                    + "        xruc, "
                    + "        xdirec, "
                    + "        cod_zona, "
                    + "        xdesc_zona, "
                    + "        cod_ruta, "
                    + "        xdesc_ruta, "
                    + "        ctipo_cliente, "
                    + "        xdesc_tipo, "
                    + "        xtelef ";
        } else {
            sql += "SELECT  cod_cliente, "
                    + "     xnombre, "
                    + "     xdirec, "
                    + "     cod_ruta, "
                    + "     xdesc_ruta ";
        }
        sql += "FROM #MOSTRAR ";
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
            Logger.getLogger(ClientesRutasZonasFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
