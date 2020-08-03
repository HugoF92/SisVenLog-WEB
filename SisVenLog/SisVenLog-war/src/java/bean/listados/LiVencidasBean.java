package bean.listados;

import dao.FacturasVencidasFacade;
import dao.ClientesFacade;
import dao.EmpleadosFacade;
import dao.TiposDocumentosFacade;
import dao.ZonasFacade;
import entidad.Clientes;
import entidad.Empleados;
import entidad.TiposDocumentos;
import entidad.Zonas;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
public class LiVencidasBean implements Serializable {
        
    private Date fechaVencimiento;

    private Date fechaHasta;

    private Zonas zona;

    private List<Zonas> listaZonas;

    private String discriminar;

    private List<Clientes> listadoClientes;

    private List<Clientes> listadoClientesSeleccionados;

    private Boolean todosClientes;

    private Boolean seleccionarClientes;

    private Empleados vendedor;

    private List<Empleados> listaVendedores;
    
    private Integer plazoCreditoDesde;
    
    private Integer plazoCreditoHasta;
    
    private TiposDocumentos tipoDocumento;

    private List<TiposDocumentos> tiposDocumentos;

    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private ClientesFacade clientesFacade;

    @EJB
    private FacturasVencidasFacade facturasVencidasFacade;
    
    @EJB
    private EmpleadosFacade empleadosFacade;
    
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @PostConstruct
    public void instanciar(){
        this.listadoClientes = new ArrayList<>();
        this.listadoClientesSeleccionados = new ArrayList<>();
        this.fechaVencimiento = new Date();
        this.fechaHasta = new Date();
        this.zona = null;
        this.plazoCreditoDesde = 1;
        this.plazoCreditoHasta = 1;
        this.discriminar = "ND";
        this.todosClientes = true;
        this.listaZonas = this.zonasFacade.findAll();
        this.listadoClientes = this.clientesFacade.findAll();
        this.listaVendedores = this.empleadosFacade.listarEmpleadosActivos();
        this.tiposDocumentos = tiposDocumentosFacade.getTipoDocumentosByCtipo(Arrays.asList("FCR", "CHQ", "PAG"));
    }

    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            facturasVencidasFacade.generateTmpTableMostrar(stmt, fechaVencimiento,
                    fechaHasta, zona, discriminar, vendedor, plazoCreditoDesde,
                    plazoCreditoHasta, tipoDocumento, todosClientes, listadoClientesSeleccionados);
            if (tipo.equals("VIST")) {
                Zonas zo = listaZonas.stream()
                        .filter(z -> z.equals(zona))
                        .findAny()
                        .orElse(null);
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                TiposDocumentos td = tiposDocumentos.stream()
                        .filter(tdd -> tdd.equals(tipoDocumento))
                        .findAny()
                        .orElse(null);
                Empleados vend = listaVendedores.stream()
                        .filter(v -> v.equals(vendedor))
                        .findAny()
                        .orElse(null);
                rep.reporteFacturasVencidas(DateUtil.formaterDateToString(fechaVencimiento),
                        DateUtil.formaterDateToString(fechaHasta), zo, discriminar, td, vend, 
                        StringUtil.convertirListaAString(listadoClientesSeleccionados.stream().distinct().collect(Collectors.toList())),
                        usuarioImpresion);
            } else {
                String[] columnas = new String[]{
                            "cod_cliente",
                            "isaldo_inicial",
                            "cod_zona",
                            "xdesc_zona",
                            "xnombre",
                            "nplazo_credito",
                            "ctipo_docum",
                            "ndocum",
                            "fdocum",
                            "fvenc",
                            "ideuda",
                            "xdesc_zona2",
                            "xdesc_zona3"
                };
                List<Object[]> lista = facturasVencidasFacade.listarParaExcel(stmt, columnas,
                        facturasVencidasFacade.generateSelectFinal());
                conexion.close();
                
                rep.exportarExcel(columnas, lista, "rvencidas");
            }
        } catch (SQLException e) {
            System.out.println(e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al generar archivo.",
                            "Error al generar archivo."));
        }
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

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Empleados getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
    }

    public List<Empleados> getListaVendedores() {
        return listaVendedores;
    }

    public void setListaVendedores(List<Empleados> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }

    public Integer getPlazoCreditoDesde() {
        return plazoCreditoDesde;
    }

    public void setPlazoCreditoDesde(Integer plazoCreditoDesde) {
        this.plazoCreditoDesde = plazoCreditoDesde;
    }

    public Integer getPlazoCreditoHasta() {
        return plazoCreditoHasta;
    }

    public void setPlazoCreditoHasta(Integer plazoCreditoHasta) {
        this.plazoCreditoHasta = plazoCreditoHasta;
    }

    public TiposDocumentos getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentos tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<TiposDocumentos> getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(List<TiposDocumentos> tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }
}
