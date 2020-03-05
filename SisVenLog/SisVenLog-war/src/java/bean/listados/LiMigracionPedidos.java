/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.CanalesVentaFacade;
import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.MigracionPedidosFacade;
import entidad.CanalesVenta;
import entidad.Empleados;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.*;
import util.DateUtil;
import util.LlamarReportes;

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
    private ExcelFacade excelFacade;

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
        LlamarReportes rep = new LlamarReportes();
        if(tipoArchivo.equals("PDF")){
            
            String fecInicial = DateUtil.dateToString(this.fechaInicial, "yyyy-MM-dd");
            String fecFinal = DateUtil.dateToString(this.fechaFinal, "yyyy-MM-dd");
            String fechaInicialHora = DateUtil.dateToString(this.fechaInicial, "yyyy-dd-MM");
            String fechaFinalHora = DateUtil.dateToString(this.fechaFinal, "yyyy-dd-MM");
            Integer codVendedor = Objects.nonNull(this.vendedor) ? new Integer(this.vendedor.getEmpleadosPK().getCodEmpleado()) : null;
            String codCanal = Objects.nonNull(this.canalVenta) ? this.canalVenta.getCodCanal() : null;
            String canal = Objects.nonNull(this.canalVenta) ? this.canalVenta.getXdesc() : null;

            rep.reporteLiMigraPedidos(fecInicial, fecFinal, fechaInicialHora, fechaFinalHora, codVendedor, codCanal, canal, this.estado);
            
        } else if (tipoArchivo.equals("XLS")){
            List<Object[]> lista;
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
            
            String sql = this.migracionPedidosFacade.generateSql(fechaInicial,
                    fechaFinal, estado, vendedor, canalVenta);
            lista = excelFacade.listarParaExcel(sql);

            rep.exportarExcel(columnas, lista, "limigrapedidos");
        }
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
        if(canalVenta != null)
            System.out.println("Canal de venta seleccionado " + canalVenta.getCodCanal());
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
