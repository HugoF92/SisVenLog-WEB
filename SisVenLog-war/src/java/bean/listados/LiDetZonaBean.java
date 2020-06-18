/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.CanalesVentaFacade;
import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.LineasFacade;
import dao.MercaderiasFacade;
import dao.ProveedoresFacade;
import dao.RutasFacade;
import dao.SublineasFacade;
import dao.ZonasFacade;
import entidad.CanalesVenta;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Proveedores;
import entidad.Rutas;
import entidad.Sublineas;
import entidad.Zonas;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DualListModel;
import util.DateUtil;
import util.LlamarReportes;

/**
 *
 * @author bbrizuela
 */
@ManagedBean
@ViewScoped
public class LiDetZonaBean {
    
    private Date desde;    
    private Date hasta;    
    private Zonas zona;    
    private Sublineas subLinea;
    private Lineas linea;
    private Rutas ruta;
    private Empleados vendedor;
    private CanalesVenta canal;
    private Proveedores proveedor;
    private Mercaderias mercaderia;
    
    private List<Zonas> listaZonas;
    private List<Sublineas> listaSubLineas;
    private List<Rutas> listaRutas;
    private List<Lineas> listaLineas;
    private List<Empleados> listaVendedor;
    private List<CanalesVenta> listaCanales;
    private List<Proveedores> listaProveedores;
    
    private List<Mercaderias> listaMercaderias;
    private List<Mercaderias> listaMercaderiasSeleccionadas;
    private DualListModel<Mercaderias> mercaderias;
    
    @EJB
    private ZonasFacade zonasFacade;
    @EJB
    private EmpleadosFacade empleadoFacade;
    @EJB
    private SublineasFacade sublineasFacade;
    @EJB
    private LineasFacade lineasFacade;    
    @EJB
    private RutasFacade rutasFacade;
    @EJB
    private CanalesVentaFacade canalFacade;
    @EJB
    private ProveedoresFacade proveedoresFacade;
    @EJB
    private MercaderiasFacade mercaderiasFacade;
    @EJB
    private ExcelFacade excelFacade;
    
        
    private String seleccion,nuevos;
    private Boolean nuevo;
    
    @PostConstruct
    public void instanciar() {
        this.seleccion = new String("1");        
        this.nuevo =  false;
        this.desde = new Date();
        this.hasta = new Date();
        
        //this.vendedor = new Empleados(new EmpleadosPK());
        //this.zona = new Zonas(new ZonasPK());        
        
        this.listaZonas = zonasFacade.listarZonas();
        this.listaSubLineas = sublineasFacade.listarSublineasConMercaderias();
        this.listaLineas = lineasFacade.listarLineasOrdenadoXCategoria();        
        this.listaRutas = rutasFacade.listarRutasOrdenadoXDesc();        
        this.listaVendedor = empleadoFacade.getEmpleadosVendedoresActivosPorCodEmp(2);
        this.listaCanales = canalFacade.listarCanalesOrdenadoXDesc();
        this.listaProveedores = proveedoresFacade.getProveedoresActivos();
        
        List<Mercaderias> mercaSource = new ArrayList<Mercaderias>();
        List<Mercaderias> mercaTarget = new ArrayList<Mercaderias>();

        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();

        mercaderias = new DualListModel<Mercaderias>(mercaSource, mercaTarget);
    }

    public void ejecutarListado(String tipo){
        try{
            LlamarReportes rep = new LlamarReportes();
            String fdesde = DateUtil.formaterDateToString(desde, "yyyy/MM/dd");
            String fhasta = DateUtil.formaterDateToString(hasta, "yyyy/MM/dd");
            String extras = "";
            String sql = null;
            String[] columnas = null;
            String titulo = null;
            String reporte = null;
            
            if (this.zona != null){
                extras += " AND f.cod_zona = '"+this.zona.getZonasPK().getCodZona()+"' ";
            }
            if (this.vendedor != null){
                extras += " AND f.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
            }
            if (this.linea != null){
                extras += " AND l.cod_linea = " + this.linea.getCodLinea() + " ";
            }
            if (this.subLinea != null){
                extras += " AND l.cod_sublinea = " + this.subLinea.getCodSublinea() +" ";
            }
            if (this.ruta != null){
                extras += " AND l.cod_ruta = " + this.ruta.getRutasPK().getCodRuta()  +" ";
            }
            if (this.canal != null){
                extras += " AND l.cod_canal = " + this.canal.getCodCanal()  +" ";
            }
            if (this.proveedor != null){
                extras += " AND l.cod_proveed = " + this.proveedor.getCodProveed()  +" ";
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
            
            switch ( this.seleccion ) {
                case "1":
                    titulo = "POR PROVEEDOR";
                    reporte = "rkclientes6";
                    columnas = new String[3];
                    columnas[0] = "cod_proveed";
                    columnas[1] = "xnombre";
                    columnas[2] = "cant_clientes";
                    sql = " "
                    + extras +
                        "";
                    break;
                case "2":
                    break;
                    
            }
            
        } catch (Exception e) {
            
        }        
    }
    
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

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public Sublineas getSubLinea() {
        return subLinea;
    }

    public void setSubLinea(Sublineas subLinea) {
        this.subLinea = subLinea;
    }

    public Lineas getLinea() {
        return linea;
    }

    public void setLinea(Lineas linea) {
        this.linea = linea;
    }

    public Rutas getRuta() {
        return ruta;
    }

    public void setRuta(Rutas ruta) {
        this.ruta = ruta;
    }

    public Empleados getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
    }

    public CanalesVenta getCanal() {
        return canal;
    }

    public void setCanal(CanalesVenta canal) {
        this.canal = canal;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public Mercaderias getMercaderia() {
        return mercaderia;
    }

    public void setMercaderia(Mercaderias mercaderia) {
        this.mercaderia = mercaderia;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public List<Sublineas> getListaSubLineas() {
        return listaSubLineas;
    }

    public void setListaSubLineas(List<Sublineas> listaSubLineas) {
        this.listaSubLineas = listaSubLineas;
    }

    public List<Rutas> getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(List<Rutas> listaRutas) {
        this.listaRutas = listaRutas;
    }

    public List<Lineas> getListaLineas() {
        return listaLineas;
    }

    public void setListaLineas(List<Lineas> listaLineas) {
        this.listaLineas = listaLineas;
    }

    public List<Empleados> getListaVendedor() {
        return listaVendedor;
    }

    public void setListaVendedor(List<Empleados> listaVendedor) {
        this.listaVendedor = listaVendedor;
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

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    public String getNuevos() {
        return nuevos;
    }

    public void setNuevos(String nuevos) {
        this.nuevos = nuevos;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }
    
    
}
