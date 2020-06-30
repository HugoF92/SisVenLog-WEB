package bean.listados;

import dao.ClientesPagosAtrasadosFacade;
import dao.ClientesFacade;
import dao.ZonasFacade;
import entidad.Clientes;
import entidad.Zonas;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
public class LiAtrasosBean implements Serializable {
        
    private Date fechaDesde;

    private Date fechaHasta;

    private Zonas zona;

    private List<Zonas> listaZonas;

    private String discriminar;

    private List<Clientes> listadoClientes;

    private List<Clientes> listadoClientesSeleccionados;

    private Boolean todosClientes;

    private Boolean seleccionarClientes;

    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private ClientesFacade clientesFacade;

    @EJB
    private ClientesPagosAtrasadosFacade clientesPagosAtrasadosFacade;

    @PostConstruct
    public void instanciar(){
        this.listadoClientes = new ArrayList<>();
        this.listadoClientesSeleccionados = new ArrayList<>();
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        this.zona = null;
        this.discriminar = "PC";
        this.todosClientes = true;
        this.listaZonas = this.zonasFacade.findAll();
        this.listadoClientes = this.clientesFacade.findAll();
    }

    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            clientesPagosAtrasadosFacade.generateTmpTableMostrar(stmt, zona,
                    discriminar, fechaDesde, fechaHasta, todosClientes,
                    listadoClientesSeleccionados);
            if (tipo.equals("VIST")) {
                Zonas z = null;
                if(zona!=null){
                    z = zonasFacade.find(zona.getZonasPK());
                }
                Object usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
                rep.reporteClientesPagosAtrasados(DateUtil.formaterDateToString(fechaDesde),
                        DateUtil.formaterDateToString(fechaHasta), z, discriminar,
                        StringUtil.convertirListaAString(listadoClientesSeleccionados.stream().distinct().collect(Collectors.toList())),
                        (usuarioImpresion == null)? "":usuarioImpresion.toString());
            } else {
                String[] columnas = new String[]{
                            "cod_zona",
                            "cod_cliente",
                            "xnombre",
                            "nplazo_credito",
                            "ndocum",
                            "ctipo_docum",
                            "idocum",
                            "iefectivo",
                            "icheques",
                            "fdocum",
                            "fvenc",
                            "fpago",
                            "dias_atraso"
                };
                List<Object[]> lista = clientesPagosAtrasadosFacade.listarParaExcel(stmt, columnas,
                        clientesPagosAtrasadosFacade.generateSelectMostrar());
                conexion.close();
                
                rep.exportarExcel(columnas, lista,
                        "PC".equals(discriminar)? "ratrasocli": "ratrasozon");
            }
        } catch (SQLException e) {
            System.out.println(e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al generar archivo.",
                            "Error al generar archivo."));
        }
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
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

    public String getDiscriminar() {
        return discriminar;
    }

    public void setDiscriminar(String discriminar) {
        this.discriminar = discriminar;
    }

    public List<Clientes> getListadoClientes() {
        return listadoClientes;
    }

    public void setListadoClientes(List<Clientes> listadoClientes) {
        this.listadoClientes = listadoClientes;
    }

    public List<Clientes> getListadoClientesSeleccionados() {
        return listadoClientesSeleccionados;
    }

    public void setListadoClientesSeleccionados(List<Clientes> listadoClientesSeleccionados) {
        this.listadoClientesSeleccionados = listadoClientesSeleccionados;
    }

    public Boolean getTodosClientes() {
        return todosClientes;
    }

    public void setTodosClientes(Boolean todosClientes) {
        this.todosClientes = todosClientes;
        if(todosClientes)
            this.listadoClientesSeleccionados.clear();
    }

    public Boolean getSeleccionarClientes() {
        return seleccionarClientes;
    }

    public void setSeleccionarClientes(Boolean seleccionarClientes) {
        this.seleccionarClientes = seleccionarClientes;
    }
}
