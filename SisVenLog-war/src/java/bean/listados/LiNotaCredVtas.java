/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.CanalesVentaFacade;
import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.ConceptosDocumentosFacade;
import dao.MercaderiasFacade;
import dao.ProveedoresFacade;
import dao.TiposDocumentosFacade;
import entidad.CanalesVenta;
import entidad.ConceptosDocumentos;
import entidad.Depositos;
import entidad.Empleados;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Proveedores;
import entidad.TiposDocumentos;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;
import util.DateUtil;
import util.LlamarReportes;

/**
 *
 * @author Bruno Brizuela
 */
@ManagedBean
@ViewScoped
public class LiNotaCredVtas {
    
    private Date desde;    
    private Date hasta; 
    private Empleados vendedor;
    private Empleados entregador;
    private TiposDocumentos tipoFactura;
    private CanalesVenta canal;
    private ConceptosDocumentos concepto;
    private Depositos deposito;
    private Proveedores proveedor;
    
    private Boolean resumido = false;
    private Boolean sinIVA = false;
    private String seleccion;
    private String estado;
    
    private List<Depositos> listaDepositos;
    private List<TiposDocumentos> listaTiposDocumentos;
    private List<Empleados> listaEntregador;
    private List<Empleados> listaVendedor;
    private List<ConceptosDocumentos> listaConceptosDocumentos;
    //private List<Lineas> listaLineas;    
    private List<CanalesVenta> listaCanales;
    private List<Proveedores> listaProveedores;
    
    private List<Mercaderias> listaMercaderias;
    private List<Mercaderias> listaMercaderiasSeleccionadas;
    private DualListModel<Mercaderias> mercaderias;
        
    @EJB
    private DepositosFacade depositoFacade;
    @EJB
    private EmpleadosFacade empleadoFacade;
    @EJB
    private TiposDocumentosFacade tipoDocumFacade; 
    @EJB
    private CanalesVentaFacade canalFacade;
    @EJB
    private ConceptosDocumentosFacade conceptoDocumFacade;
    @EJB
    private ProveedoresFacade proveedoresFacade;    
    @EJB
    private MercaderiasFacade mercaderiasFacade;
    @EJB
    private ExcelFacade excelFacade;
    
    @PostConstruct
    public void init() {
        this.seleccion = new String("1");        
        this.desde = new Date();
        this.hasta = new Date();
        listaDepositos = depositoFacade.listarDepositosActivos();
        listaTiposDocumentos = tipoDocumFacade.listarTipoDocumentoFacturaVtaCredito();
        listaEntregador = empleadoFacade.listarEmpPorEmpresaTipoEstado("2", "E", "A");
        listaVendedor = empleadoFacade.listarEmpPorEmpresaTipoEstado("2", "V", "A");
        listaConceptosDocumentos = conceptoDocumFacade.listarTipoDocumentoFacturaVtaCredito();
        listaCanales = canalFacade.listarCanalesOrdenadoXDesc();
        listaProveedores = proveedoresFacade.listarProveedoresActivos();
        
        List<Mercaderias> mercaSource = new ArrayList<Mercaderias>();
        List<Mercaderias> mercaTarget = new ArrayList<Mercaderias>();
        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();
        mercaderias = new DualListModel<Mercaderias>(mercaSource, mercaTarget);
    }
    
    public void ejecutarListado(String tipo){
        try{
            LlamarReportes rep = new LlamarReportes();
            String fdesde = DateUtil.formaterDateToString(desde, "yyyy-MM-dd");
            String fhasta = DateUtil.formaterDateToString(hasta, "yyyy-MM-dd");
            String extras = "";
            String sql = null;
            String[] columnas = null;
            String titulo = null;
            String reporte = null;

            if (this.entregador != null){
                extras += " AND d.cod_vendedor = '"+this.entregador.getEmpleadosPK().getCodEmpleado()+"' ";
            }
            if (this.vendedor != null){
                extras += " AND d.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
            }
            if (this.tipoFactura != null){
                extras += " AND s.cod_linea = " + this.tipoFactura.getCtipoDocum()+ " ";
            }
            if (this.concepto != null){
                extras += " AND m.cod_sublinea = " + this.concepto.getConceptosDocumentosPK() +" ";
            }
            if (this.deposito != null){
                extras += " AND d.cod_ruta = " + this.deposito.getDepositosPK()  +" ";
            }
            if (this.canal != null){
                extras += " AND mc.cod_canal = '" + this.canal.getCodCanal()  +"' ";
            }
            if (this.proveedor != null){
                extras += " AND m.cod_proveed = " + this.proveedor.getCodProveed()  +" ";
            }
            if (mercaderias.getTarget().size() > 0) {
                listaMercaderiasSeleccionadas = mercaderias.getTarget();
                extras += " AND m.cod_merca IN (";
                for (int i = 0; i < listaMercaderiasSeleccionadas.size(); i++) {
                    MercaderiasPK pk = listaMercaderiasSeleccionadas.get(i).getMercaderiasPK();
                    extras += " '" + pk.getCodMerca() + "',";
                }
                extras = extras.substring(0, extras.length()-1) + " ) ";
            }

            columnas = new String[14];
            columnas[0] = "cod_vendedor";
            
            if ( 1==1 || this.seleccion.equals("5") || this.seleccion.equals("6") 
                    || this.seleccion.equals("7") || this.seleccion.equals("11")  ) {
                sql = preEjecutarSQL2();
            } else {
                sql = preEjecutarSQL();
            }
            
            titulo = "DETALLE DE VENTAS POR ";
            switch ( this.seleccion ) {
                case "1":
                    if (this.resumido) {
                        titulo = " RESUMEN DE VENTAS POR MERCADERIA";
                        reporte = "rresmerca";
                    } else {
                        titulo += " ZONA Y MERCADERIA";
                        reporte = "rdetzona";
                    }
                    break;
            }
            
            System.out.println(sql);
            
            if (tipo.equals("VIST")){
                String usuImprime = "";//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                Map param = new HashMap();
                param.put("sql", sql);
                param.put("fdesde", this.desde);
                param.put("fhasta", this.hasta);
                param.put("titulo", titulo);
                param.put("usuImprime", usuImprime);
                param.put("nombreRepo", reporte); 

                if (this.vendedor != null) param.put("vendedor", empleadoFacade.getEmpeladoFromList(this.vendedor, this.listaVendedor).getXnombre());
                else param.put("vendedor", "TODOS");
                
                if (this.canal != null) param.put("canal", canalFacade.getCanalVentaFromList(this.canal, this.listaCanales).getXdesc()); 
                else param.put("canal", "TODOS");
                
                if (this.proveedor != null) param.put("proveedor", proveedoresFacade.getProveedorFromList(this.proveedor, this.listaProveedores).getXnombre()); 
                else param.put("proveedor", "TODOS");
            
                rep.reporteLiContClientes(param, tipo, reporte);
                
            } else {
                List<Object[]> lista = new ArrayList<Object[]>();
                lista = excelFacade.listarParaExcel(sql);
                rep.exportarExcel(columnas, lista, reporte);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al ejecutar listado"));
        }
    }
    
    public String preEjecutarSQL(){
        return "";
    }
    
    public String preEjecutarSQL2(){
        return "";
    }    
    
    /* GETTER Y SETTER */

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Empleados getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
    }

    public Empleados getEntregador() {
        return entregador;
    }

    public void setEntregador(Empleados entregador) {
        this.entregador = entregador;
    }

    public TiposDocumentos getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(TiposDocumentos tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public CanalesVenta getCanal() {
        return canal;
    }

    public void setCanal(CanalesVenta canal) {
        this.canal = canal;
    }

    public ConceptosDocumentos getConcepto() {
        return concepto;
    }

    public void setConcepto(ConceptosDocumentos concepto) {
        this.concepto = concepto;
    }

    public Depositos getDeposito() {
        return deposito;
    }

    public void setDeposito(Depositos deposito) {
        this.deposito = deposito;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public Boolean getResumido() {
        return resumido;
    }

    public void setResumido(Boolean resumido) {
        this.resumido = resumido;
    }

    public Boolean getSinIVA() {
        return sinIVA;
    }

    public void setSinIVA(Boolean sinIVA) {
        this.sinIVA = sinIVA;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    public List<Depositos> getListaDepositos() {
        return listaDepositos;
    }

    public void setListaDepositos(List<Depositos> listaDepositos) {
        this.listaDepositos = listaDepositos;
    }

    public List<TiposDocumentos> getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

    public List<Empleados> getListaEntregador() {
        return listaEntregador;
    }

    public void setListaEntregador(List<Empleados> listaEntregador) {
        this.listaEntregador = listaEntregador;
    }

    public List<Empleados> getListaVendedor() {
        return listaVendedor;
    }

    public void setListaVendedor(List<Empleados> listaVendedor) {
        this.listaVendedor = listaVendedor;
    }

    public List<ConceptosDocumentos> getListaConceptosDocumentos() {
        return listaConceptosDocumentos;
    }

    public void setListaConceptosDocumentos(List<ConceptosDocumentos> listaConceptosDocumentos) {
        this.listaConceptosDocumentos = listaConceptosDocumentos;
    }

    public List<CanalesVenta> getListaCanales() {
        return listaCanales;
    }

    public void setListaCanales(List<CanalesVenta> listaCanales) {
        this.listaCanales = listaCanales;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public List<Mercaderias> getListaMercaderias() {
        return listaMercaderias;
    }

    public void setListaMercaderias(List<Mercaderias> listaMercaderias) {
        this.listaMercaderias = listaMercaderias;
    }

    public List<Mercaderias> getListaMercaderiasSeleccionadas() {
        return listaMercaderiasSeleccionadas;
    }

    public void setListaMercaderiasSeleccionadas(List<Mercaderias> listaMercaderiasSeleccionadas) {
        this.listaMercaderiasSeleccionadas = listaMercaderiasSeleccionadas;
    }

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}
