/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.CanalesVentaFacade;
import dao.EmpleadosFacade;
import dao.ExcelFacade;
import entidad.CanalesVenta;
import entidad.Empleados;
import java.io.Serializable;
import java.util.ArrayList;
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
            
            String fecInicial = DateUtil.dateToString(this.fechaInicial, "yyyy/MM/dd");
            String fecFinal = DateUtil.dateToString(this.fechaFinal, "yyyy/MM/dd");
            String fechaInicialHora = DateUtil.dateToString(this.fechaInicial, "yyyy/dd/MM HH:mm:ss");
            String fechaFinalHora = DateUtil.dateToString(this.fechaFinal, "yyyy/dd/MM HH:mm:ss");
            Integer codVendedor = Objects.nonNull(this.vendedor) ? new Integer(this.vendedor.getEmpleadosPK().getCodEmpleado()) : null;
            String codCanal = Objects.nonNull(this.canalVenta) ? this.canalVenta.getCodCanal() : null;

            rep.reporteLiMigraPedidos(fecInicial, fecFinal, fechaInicialHora, fechaFinalHora, codVendedor, codCanal, this.estado);
            
        } else if (tipoArchivo.equals("XLS")){
            List<Object[]> lista = new ArrayList<>();

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
                    + "WHERE tp.facfecha BETWEEN '" + DateUtil.dateToString(this.fechaInicial, "yyyy/MM/dd") + "' "
                    + "AND '" + DateUtil.dateToString(this.fechaFinal, "yyyy/MM/dd") + "' ";
            
            String lEstado = "";
            if(this.estado.equals("2")){
                lEstado = " ";
            } else if(this.estado.equals("3")) {
                lEstado = "R";
            } else if(this.estado.equals("4")) {
                lEstado = "M";
            }
            
            if(this.estado.equals("2") || this.estado.equals("4")) {
                sql += " AND estado = '" + lEstado + "' ";
            }
            
            if(this.estado.equals("3")) {
                sql += " AND ( estado = '" + lEstado + "' OR (estado = 'M' AND msg_error != '')) ";
            }
            
            if(Objects.nonNull(this.vendedor)){
                sql += " AND tp.cod_vendedor = " + this.vendedor.getEmpleadosPK().getCodEmpleado()  + " ";
            }
            
            if(Objects.nonNull(this.canalVenta)){
                sql += " AND tp.cod_canal = '" + this.canalVenta.getCodCanal() + "' ";
            }
            
//            sql += "ORDER BY cod_vendedor, nroped ";
            
            // Si selecciono los estados de (Todos, Rechazados)
            if(this.estado.equals("1") || this.estado.equals("3")) {
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
                    + "AND p.fpedido BETWEEN '" + DateUtil.dateToString(this.fechaInicial, "yyyy/dd/MM HH:mm:ss") + "' "
                    + "AND '" + DateUtil.dateToString(this.fechaFinal, "yyyy/dd/MM HH:mm:ss") + "' "
                    + "AND p.mestado = 'R' ";
                
                    if(Objects.nonNull(this.vendedor)){
                        sql += " AND p.cod_vendedor = " + this.vendedor.getEmpleadosPK().getCodEmpleado()  + " ";
                    }

                    if(Objects.nonNull(this.canalVenta)){
                        sql += " AND p.cod_canal = '" + this.canalVenta.getCodCanal() + "' ";
                    }
            }
            
            sql += " ORDER BY cod_vendedor, nroped";

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
