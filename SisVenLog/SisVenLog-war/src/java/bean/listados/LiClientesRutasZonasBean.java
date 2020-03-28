/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.ClientesFacade;
import dao.ClientesRutasZonasFacade;
import org.primefaces.PrimeFaces;
import javax.faces.event.AjaxBehaviorEvent;
import dao.RutasFacade;
import dao.TiposClientesFacade;
import dao.ZonasFacade;
import entidad.Clientes;
import entidad.Rutas;
import entidad.TiposClientes;
import entidad.Zonas;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private Boolean habilitarCalendario;

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
    private ClientesRutasZonasFacade clientesRutasZonasFacade;
    
    @PostConstruct
    public void instanciar(){
        this.listadoClientes = new ArrayList<>();
        this.fechaAltaDesde = new Date();
        this.fechaAltaHasta = new Date();
        this.tipoCliente = null;
        this.zona = null;
        this.ruta = null;
        this.estado = "4";
        this.habilitarCalendario = false;
        this.todosClientes = true;
        this.seleccionarClientes = false;
        this.listaTiposClientes = this.tiposClientesFacade.findAll();
        this.listaZonas = this.zonasFacade.findAll();
        this.listaRutas = this.rutasFacade.findAll();
        this.listadoClientes = this.clientesFacade.findAll();
    }

    public void ejecutar(String tipo) {
        try {
            // validamos las fechas en caso que el estado sea 2
            if(estado.equals("2")){
                if(!validarFechas())
                    return;
            }
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            stmt.execute(clientesRutasZonasFacade.generateSqlRuteo(conRuteo,
                    tipoCliente, zona, ruta, estado, fechaAltaDesde,
                    fechaAltaHasta, todosClientes,
                    listadoClientesSeleccionados));
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "admin";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                String fechaDesde = fechaAltaDesde == null? "": DateUtil.dateToString(fechaAltaDesde, "dd/MM/yyyy");
                String fechaHasta = fechaAltaHasta == null? "": DateUtil.dateToString(fechaAltaHasta, "dd/MM/yyyy");
                Zonas z = null;
                if(zona!=null){
                    z = zonasFacade.find(zona.getZonasPK());
                }
                Rutas r = null;
                if(ruta!= null){
                    r = rutasFacade.find(ruta.getRutasPK());
                }
                TiposClientes tc = null;
                if(tipoCliente != null){
                    tc = tiposClientesFacade.find(tipoCliente.getCtipoCliente());
                }
                rep.reporteClientesRutasZonas(conRuteo, tc, z, r, estado,
                        fechaDesde, fechaHasta, todosClientes,
                        StringUtil.convertirListaAString(listadoClientesSeleccionados),
                        usuarioImpresion);
            } else {
                String filename;
                List<Object[]> lista;
                String[] columnas;
                if (conRuteo) {
                    columnas = new String[]{
                        "cod_cliente",
                        "xnombre",
                        "xdirec",
                        "cod_ruta",
                        "xdesc_ruta"};
                    filename = "liclientesconruteo";
                } else {
                    columnas = new String[]{
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
                    filename = "liclientessinruteo";
                }
                lista = clientesRutasZonasFacade.listarParaExcel(stmt, columnas,
                        clientesRutasZonasFacade.generateSelectMostrar(conRuteo));
                conexion.close();
                rep.exportarExcel(columnas, lista, filename);
            }
        } catch (SQLException e) {
            System.out.println(e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al generar archivo.",
                            "Error al generar archivo."));
        }
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

    public void handleEstadoChange(AjaxBehaviorEvent event) {
         if (estado.equals("2")) {
            this.setHabilitarCalendario(true);
        } else {
            this.setHabilitarCalendario(false);
        }
        PrimeFaces.current().ajax().update("fechaAltaDesde");
        PrimeFaces.current().ajax().update("fechaAltaHasta");
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

    public Boolean getHabilitarCalendario() {
        return habilitarCalendario;
    }

    public void setHabilitarCalendario(Boolean habilitarCalendario) {
        this.habilitarCalendario = habilitarCalendario;
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