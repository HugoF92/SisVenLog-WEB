package bean;

import dao.ClientesCreditosFacade;
import dao.ClientesFacade;
import dao.EmpleadosFacade;
import dao.ZonasFacade;
import entidad.Clientes;
import entidad.Empleados;
import entidad.Zonas;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static java.time.temporal.ChronoUnit.DAYS;
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
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.PrimeFaces;
import util.DateUtil;
import util.LlamarReportes;
import util.StringUtil;

/**
 *
 * @author Arsenio
 */
@ManagedBean
@SessionScoped
public class ClientesCreditosBean implements Serializable {
    
    private Date fechaFacDesde;

    private Date fechaFacHasta;

    private Zonas zona;

    private List<Zonas> listaZonas;
    
    private Empleados vendedor;

    private List<Empleados> listaVendedores;
    
    private Integer nroPromedio;

    private String modalidadPago;

    private String estado;

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
    private EmpleadosFacade empleadosFacade;

    @EJB
    private ClientesCreditosFacade clientesCreditosFacade;

    @PostConstruct
    public void instanciar(){
        this.listadoClientes = new ArrayList<>();
        this.fechaFacDesde = null;
        this.fechaFacHasta = null;
        this.zona = null;
        this.nroPromedio = 1;
        this.estado = "";
        this.modalidadPago = "-1";
        this.discriminar = "ND";
        this.todosClientes = true;
        this.listaVendedores = this.empleadosFacade.listarEmpleadosActivos();
        this.listaZonas = this.zonasFacade.findAll();
        this.listadoClientes = this.clientesFacade.findAll();
    }

    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            if(this.nroPromedio <= 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Atencion",
                            "El numero de Dividir ventas debe ser mayor a cero(0)."));
                return;
            }
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            stmt.execute(clientesCreditosFacade.generateTmpTableMostrar(zona,
                    todosClientes, listadoClientesSeleccionados, estado, modalidadPago));
            stmt.execute(clientesCreditosFacade.generateTmpVentas());
            stmt.execute(clientesCreditosFacade.generateTmpTableCurfac(fechaFacDesde, fechaFacHasta, vendedor));
            if (discriminar.equals("ND")) {
                stmt.execute(clientesCreditosFacade.generateTmpTableCurDatosND(vendedor));
            } else {
                stmt.execute(clientesCreditosFacade.generateTmpTableCurDatosPZ(vendedor));
            }
            if (tipo.equals("VIST")) {
                Zonas z = null;
                if(zona!=null){
                    z = zonasFacade.find(zona.getZonasPK());
                }
                Empleados v = null;
                if(vendedor != null){
                    v = empleadosFacade.find(vendedor.getEmpleadosPK());
                }
                Object usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
                rep.reporteClientesCreditos(DateUtil.formaterDateToString(fechaFacDesde),
                        DateUtil.formaterDateToString(fechaFacHasta), z, this.nroPromedio,v, discriminar,
                        StringUtil.convertirListaAString(listadoClientesSeleccionados.stream().distinct().collect(Collectors.toList())),
                        (usuarioImpresion == null)? "":usuarioImpresion.toString());
            } else {
                String[] columnas = new String[]{
                    "cod_zona",
                    "xdesc_zona",
                    "cod_cliente",
                    "xnombre",
                    "nriesgo",
                    "mforma_pago",
                    "nplazo_credito",
                    "ilimite_compra",
                    "cod_estado",
                    "xdesc_estado",
                    "ttotal"
                };
                List<Object[]> lista = clientesCreditosFacade.listarParaExcel(stmt, columnas,
                        clientesCreditosFacade.generateSelectCurDatos());
                conexion.close();
                rep.exportarExcel(columnas, lista, "rcliconcred");
            }
        } catch (SQLException e) {
            System.out.println(e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al generar archivo.",
                            "Error al generar archivo."));
        }
    }

    public void onDateSelect(AjaxBehaviorEvent event) {
        if (fechaFacDesde != null && fechaFacHasta != null) {
            long noOfDaysBetween = DAYS.between(fechaFacDesde.toInstant(),fechaFacHasta.toInstant());
            if (noOfDaysBetween < 0) {
                this.nroPromedio = 0;
            } else {
                this.nroPromedio = Math.toIntExact(noOfDaysBetween / 30L);
            }
            //update input
            PrimeFaces.current().ajax().update("numeroPromedio");
        }
    }

    public Date getFechaFacDesde() {
        return fechaFacDesde;
    }

    public void setFechaFacDesde(Date fechaFacDesde) {
        this.fechaFacDesde = fechaFacDesde;
    }

    public Date getFechaFacHasta() {
        return fechaFacHasta;
    }

    public void setFechaFacHasta(Date fechaFacHasta) {
        this.fechaFacHasta = fechaFacHasta;
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

    public Integer getNroPromedio() {
        return nroPromedio;
    }

    public void setNroPromedio(Integer nroPromedio) {
        this.nroPromedio = nroPromedio;
    }

    public String getModalidadPago() {
        return modalidadPago;
    }

    public void setModalidadPago(String modalidadPago) {
        this.modalidadPago = modalidadPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
    }

    public Boolean getSeleccionarClientes() {
        return seleccionarClientes;
    }

    public void setSeleccionarClientes(Boolean seleccionarClientes) {
        this.seleccionarClientes = seleccionarClientes;
    }

    
    

}
