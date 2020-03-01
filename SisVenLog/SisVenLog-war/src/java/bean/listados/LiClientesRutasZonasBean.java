/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.ClientesFacade;
import dao.ExcelFacade;
import dao.RutasFacade;
import dao.TiposClientesFacade;
import dao.ZonasFacade;
import entidad.Clientes;
import entidad.Rutas;
import entidad.TiposClientes;
import entidad.Zonas;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.DateUtil;
import util.LlamarReportes;
import util.StringUtil;

/**
 *
 * @author Arsenio
 */
@ManagedBean
@SessionScoped
public class LiClientesRutasZonasBean implements Serializable{

    private Date fechaAltaDesde;

    private Date fechaAltaHasta;

    private TiposClientes tipoCliente;

    private List<TiposClientes> listaTiposClientes;

    private Zonas zona;

    private List<Zonas> listaZonas;

    private Rutas ruta;

    private List<Rutas> listaRutas;

    private String estado;

    private Boolean conRuteo;

    private List<Clientes> listadoClientes;

    private List<Clientes> listadoClientesSeleccionados;

    private Boolean todosClientes;

    private Boolean seleccionarClientes;

    @EJB
    private TiposClientesFacade tiposClientesFacade;
   
    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private RutasFacade rutasFacade;
            
    @EJB
    private ClientesFacade clientesFacade;

    @EJB
    private ExcelFacade excelFacade;
    
    @PostConstruct
    public void instanciar(){
        this.listadoClientes = new ArrayList<>();
        this.fechaAltaDesde = null;
        this.fechaAltaHasta = null;
        this.tipoCliente = null;
        this.zona = null;
        this.ruta = null;
        this.estado = "4";
        this.listaTiposClientes = this.tiposClientesFacade.findAll();
        this.listaZonas = this.zonasFacade.findAll();
        this.listaRutas = this.rutasFacade.findAll();
        this.listadoClientes = this.clientesFacade.findAll();
    }

    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            if (tipo.equals("VIST")) {
                rep.reporteClientesRutasZonas(conRuteo, tipoCliente,
                        zona, ruta, estado, DateUtil.dateToString(fechaAltaDesde),
                        DateUtil.dateToString(fechaAltaHasta), todosClientes,
                        StringUtil.convertirListaAString(listadoClientesSeleccionados),
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString());
            } else {
                if (conRuteo) {
                    List<Object[]> lista;
                    String[] columnas = new String[]{
                        "cod_cliente",
                        "xpropietario",
                        "xnombre",
                        "cod_estado",
                        "xruc",
                        "xdirec",
                        "cod_zona",
                        "xdesc_zona",
                        "cod_ruta",
                        "xdesc_ruta",
                        "ctipo_cliente",
                        "xdesc_tipo",
                        "xtelef"
                    };

                    String sql = ""
                    + "SELECT  c.cod_cliente, "
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
                    lista = excelFacade.listarParaExcel(getConditionalsQueryFields(sql));
                    rep.exportarExcel(columnas, lista, "liclientesconruteo");

                } else {
                    List<Object[]> lista;
                    String[] columnas = new String[]{
                        "cod_cliente",
                        "xnombre",
                        "xdirec",
                        "cod_ruta",
                        "xdesc_ruta"};

                    String sql = ""
                    + "SELECT  c.cod_cliente, "
                    + "        c.xnombre, "
                    + "        c.xdirec, "
                    + "        c.cod_ruta, "
                    + "        r.xdesc as xdesc_ruta ";
                    lista = excelFacade.listarParaExcel(getConditionalsQueryFields(sql));
                    rep.exportarExcel(columnas, lista, "liclientessinruteo");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al generar archivo.",
                            "Error al generar archivo."));
        }
    }

    private String getConditionalsQueryFields(String sql) {
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
                    if(validarFechas()) {
                        sql += " AND CONVERT(char(10), c.falta, 103) BETWEEN '"
                        + DateUtil.dateToString(fechaAltaDesde)
                        + "' AND '" + DateUtil.dateToString(fechaAltaHasta) + "' ";
                    }
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

    private Boolean validarFechas() {
        if (fechaAltaDesde == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Debe ingresar fecha desde.",
                            "Debe ingresar fecha desde."));
            return false;
        } else {
            if (fechaAltaHasta == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Debe ingresar fecha hasta.",
                                "Debe ingresar fecha hasta."));
                return false;
            }
        }
        return true;
    }
    
    public Date getFechaAltaDesde() {
        return fechaAltaDesde;
    }

    public void setFechaAltaDesde(Date fechaAltaDesde) {
        this.fechaAltaDesde = fechaAltaDesde;
    }

    public Date getFechaAltaHasta() {
        return fechaAltaHasta;
    }

    public void setFechaAltaHasta(Date fechaAltaHasta) {
        this.fechaAltaHasta = fechaAltaHasta;
    }

    public TiposClientes getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TiposClientes tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public List<TiposClientes> getListaTiposClientes() {
        return listaTiposClientes;
    }

    public void setListaTiposClientes(List<TiposClientes> listaTiposClientes) {
        this.listaTiposClientes = listaTiposClientes;
    }

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public Rutas getRuta() {
        return ruta;
    }

    public void setRuta(Rutas ruta) {
        this.ruta = ruta;
    }

    public List<Rutas> getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(List<Rutas> listaRutas) {
        this.listaRutas = listaRutas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getConRuteo() {
        return conRuteo;
    }

    public void setConRuteo(Boolean conRuteo) {
        this.conRuteo = conRuteo;
    }

    public List<Clientes> getListadoClientesSeleccionados() {
        return listadoClientesSeleccionados;
    }

    public void setListadoClientesSeleccionados(List<Clientes> listadoClientesSeleccionados) {
        this.listadoClientesSeleccionados = listadoClientesSeleccionados;
    }
    
    public List<Clientes> getListadoClientes() {
        return listadoClientes;
    }

    public void setListadoClientes(List<Clientes> listadoClientes) {
        this.listadoClientes = listadoClientes;
    }

    public Boolean getTodosClientes() {
        return todosClientes;
    }

    public void setTodosClientes(Boolean todosClientes) {
        this.todosClientes = todosClientes;
    }

    public Boolean getSeleccionarClientes() {
        return seleccionarClientes;
    }

    public void setSeleccionarClientes(Boolean seleccionarClientes) {
        this.seleccionarClientes = seleccionarClientes;
    }    
}