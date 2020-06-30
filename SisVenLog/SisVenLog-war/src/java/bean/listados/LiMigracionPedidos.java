/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.CanalesVentaFacade;
import dao.EmpleadosFacade;
import dao.MigracionPedidosFacade;
import entidad.CanalesVenta;
import entidad.Empleados;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.*;
import util.DateUtil;
import util.LlamarReportes;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 *
 * @author Arsenio
 */
@ManagedBean
@SessionScoped
public class LiMigracionPedidos implements Serializable {
    
    private static final long serialVersionUID = 4L;

    @EJB
    private EmpleadosFacade empleadosFacade;

    @EJB
    private CanalesVentaFacade canalesVenFacade;

    @EJB
    private MigracionPedidosFacade migracionPedidosFacade;

    private Date fechaInicial;
    private Date fechaFinal;
    
    private CanalesVenta canalVenta;
    private List<CanalesVenta> listaCanalesVentas;
    
    private Empleados vendedor;
    private List<Empleados> listaVendedores;

    private String estado;

    public LiMigracionPedidos() {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.fechaInicial = new Date();
        this.fechaFinal = new Date();
        this.canalVenta = new CanalesVenta();
        this.listaVendedores = this.empleadosFacade.getEmpleadosVendedores();
        this.listaCanalesVentas = this.canalesVenFacade.findAll();
        this.estado = "1"; // equivale a todos
    }
    
    public void generarArchivo(String tipoArchivo) {
        try {
            if(!validarFechas())
                return;
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            stmt.execute(this.migracionPedidosFacade.generateSQLPedCur(fechaInicial,
                    fechaFinal, estado, vendedor, canalVenta));
            // Si selecciono los estados de (Todos, Rechazados)
            if(estado.equals("1") || estado.equals("3")) {
                stmt.execute(this.migracionPedidosFacade.generateSQLCurRec(fechaInicial,
                    fechaFinal, vendedor, canalVenta));
            }
            if(tipoArchivo.equals("PDF")){
                String fecInicial = DateUtil.dateToString(this.fechaInicial, "dd/MM/yyyy");
                String fecFinal = DateUtil.dateToString(this.fechaFinal, "dd/MM/yyyy");
                String vend = Objects.nonNull(this.vendedor) ? empleadosFacade.find(this.vendedor.getEmpleadosPK()).getXnombre() : null;
                String canal = Objects.nonNull(this.canalVenta) ? canalesVenFacade.find(this.canalVenta.getCodCanal()).getXdesc() : null;
                Object usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
                rep.reporteLiMigraPedidos(fecInicial, fecFinal, vend, canal,
                        this.estado, (usuarioImpresion == null)? "":usuarioImpresion.toString());
            } else if (tipoArchivo.equals("XLS")){
                String[] columnas = new String[] {
                    "vencod", 
                    "facnro", 
                    "codnuevo", 
                    "facfecha", 
                    "factipovta", 
                    "forpago", 
                    "nplazo_cheque", 
                    "artcod", 
                    "detcancajas", 
                    "detcanunid", 
                    "detprecio", 
                    "estado", 
                    "nroped", 
                    "xnombre",
                    "clicod",
                    "msg_error",
                    "cod_vendedor"
                };
                List<Object[]> lista;
                lista = migracionPedidosFacade.listarParaExcel(stmt, columnas,
                        migracionPedidosFacade.generateSelectMigradat());
                conexion.close();
                rep.exportarExcel(columnas, lista, "limigrapedidos");
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
        if (fechaInicial == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Debe ingresar fecha desde.",
                            "Debe ingresar fecha desde."));
            return false;
        } else {
            if (fechaFinal == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Debe ingresar fecha hasta.",
                                "Debe ingresar fecha hasta."));
                return false;
            }
        }
        return true;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
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

    public CanalesVenta getCanalVenta() {
        return canalVenta;
    }

    public void setCanalVenta(CanalesVenta canalVenta) {
        this.canalVenta = canalVenta;
    }

    public List<CanalesVenta> getListaCanalesVentas() {
        return listaCanalesVentas;
    }

    public void setListaCanalesVentas(List<CanalesVenta> listaCanalesVentas) {
        this.listaCanalesVentas = listaCanalesVentas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
