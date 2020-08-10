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
    
        
    private String seleccion;
    private Boolean resumido = false;
    private Boolean sinIVA = false;
    
    @PostConstruct
    public void instanciar() {
        this.seleccion = new String("1");        
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
            String fdesde = DateUtil.formaterDateToString(desde, "yyyy-MM-dd");
            String fhasta = DateUtil.formaterDateToString(hasta, "yyyy-MM-dd");
            String extras = "";
            String sql = null;
            String[] columnas = null;
            String titulo = null;
            String reporte = null;
            
            if (this.zona != null){
                extras += " AND d.cod_zona = '"+this.zona.getZonasPK().getCodZona()+"' ";
            }
            if (this.vendedor != null){
                extras += " AND d.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
            }
            if (this.linea != null){
                extras += " AND s.cod_linea = " + this.linea.getCodLinea() + " ";
            }
            if (this.subLinea != null){
                extras += " AND m.cod_sublinea = " + this.subLinea.getCodSublinea() +" ";
            }
            if (this.ruta != null){
                extras += " AND d.cod_ruta = " + this.ruta.getRutasPK().getCodRuta()  +" ";
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
            columnas[1] = "xnombre";
            columnas[2] = "cod_zona";            
            columnas[3] = "xdesc_zona";
            columnas[4] = "cod_merca";
            columnas[5] = "xdesc_merca";
            columnas[6] = "cod_sublinea";
            columnas[7] = "xdesc_sublinea";
            columnas[8] = "cod_linea";
            columnas[9] = "xdesc_linea";
            columnas[10] = "cant_cajas";
            columnas[11] = "cant_unid";            
            columnas[12] = "tpeso_vta";
            columnas[13] = "ttotal";            
            
            sql = " SELECT cod_vendedor, xnombre, cod_zona, xdesc_zona, cod_merca, " 
                + "     xdesc_merca, cod_sublinea, xdesc_sublinea, cod_linea, xdesc_linea, " 
                + " cast(((cant_cajas * nrelacion) + cant_unid)/nrelacion as int) as cant_cajas, " 
                + " ((cant_cajas * nrelacion) + cant_unid) - cast(((cant_cajas * nrelacion) " 
                + " + cant_unid)/nrelacion as int) * nrelacion  as cant_unid,  " 
                + " ((cant_cajas * nrelacion) + cant_unid) * npeso_unidad as tpeso_vta, ttotal "
                + " FROM ( select d.cod_vendedor, e.xnombre, d.cod_zona, z.xdesc as xdesc_zona,  "
                + " d.cod_merca, m.xdesc as xdesc_merca, s.cod_sublinea, "
                + " s.xdesc as xdesc_sublinea, m.nrelacion, SUM(d.cant_cajas) * -1 as cant_cajas, "
                + " SUM(d.cant_unid) * -1 as cant_unid, ";
           
            if (this.sinIVA) {
                sql += " SUM(d.igravadas+d.iexentas)  as ttotal, ";
            } else {
                sql += " SUM(d.igravadas+d.iexentas+d.impuestos)  as ttotal,  ";
            }
            
            sql += " l.cod_linea, l.xdesc as xdesc_linea, m.npeso_caja, m.npeso_unidad  "
                + " FROM movimientos_merca  d, empleados e, zonas z, sublineas s, mercaderias m, " 
                + " merca_canales mc, rutas r, lineas l "
                + " WHERE  "
                + "    d.cod_empr = 2 "
                + " AND d.cod_vendedor = e.cod_empleado "
		+ " 	and d.cod_empr = e.cod_empr "
		
		+ " 	AND d.cod_zona = z.cod_zona "
		+ " 	AND d.cod_empr = z.cod_empr "
                
                + "  AND d.cod_empr = m.cod_empr "
                + "  AND d.cod_merca = m.cod_merca "
			
		+ " 	AND m.cod_sublinea = s.cod_sublinea "
			
		+ " 	AND s.cod_linea = l.cod_linea "
			
		+ " 	AND d.cod_empr = mc.cod_empr "
		+ " 	AND d.cod_merca = mc.cod_merca "
		
                + " AND d.cod_empr = r.cod_empr "
		+ "	AND d.cod_zona = r.cod_zona "
		+ "	AND d.cod_ruta = r.cod_ruta "
                    
                + " AND d.ctipo_docum IN ('FCO','FCR','CPV','NCV','NDV') " 
                //+ " AND d.fmovim between '"+fdesde+"' AND '"+fhasta+"'"
                + " AND d.fmovim > '"+fdesde+"' "
		+ " AND d.fmovim < '"+fhasta+"' "
                + extras +
                " GROUP BY d.cod_zona, z.xdesc, d.cod_vendedor, e.xnombre, " 
                + " s.cod_sublinea, s.xdesc, d.cod_merca, m.xdesc, M.NRELACION, "
                + " l.cod_linea, l.xdesc, m.npeso_caja, m.npeso_unidad "
                    ;
            sql += " ) as mostrar  ";
            if (this.resumido) {
                switch ( this.seleccion ) {
                    case "1":
                        sql += " ORDER BY cod_merca ";
                        break;
                    case "2":
                        sql += " ORDER BY  xdesc_merca ";
                        break;
                    case "3":
                        sql += " ORDER BY  cod_linea ";
                        break;
                    case "4":
                        sql += " ORDER BY  cod_vendedor, cod_merca ";
                        break;
                }                
            } else {
                if (this.seleccion.equals("1") || this.seleccion.equals("2")){
                    sql +=  " ORDER BY cod_zona, cod_vendedor, cod_sublinea, cod_merca ";
                } else {
                    sql += " ORDER BY cod_zona, cod_vendedor, cod_linea, cod_merca ";
                }
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
                case "2":
                    if (this.resumido) {
                        titulo = " RESUMEN DE VENTAS POR SUBLINEA";
                        reporte = "rressubli";
                    } else {
                        titulo += " ZONA Y SUBLINEA";
                        reporte = "rdetzona2";
                    }
                    break;
                case "3":
                    if (this.resumido) {
                        titulo = " RESUMEN DE VENTAS POR LINEA ";
                        reporte = "rreslinea";
                    } else {
                        titulo += " ZONA Y LINEA";
                        reporte = "rdetzona3";
                    }
                    break;
                case "4":
                    if (this.resumido) {
                        titulo = " RESUMEN DE VENTAS POR MERCADERIA ";
                        reporte = "rresvenmerca";
                    } else {
                        titulo += " ZONA Y MERCADERIA ";
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
                
                if (this.zona != null) param.put("zona", zonasFacade.getZonaFromLis(this.zona, this.listaZonas).getXdesc());
                else param.put("zona", "TODOS");
                
                if (this.linea != null) param.put("linea", lineasFacade.getLineaFromList(this.linea, this.listaLineas).getXdesc()); 
                else param.put("linea", "TODOS");
                
                if (this.subLinea != null) param.put("sublinea", sublineasFacade.getSubLineaFromList(this.subLinea, this.listaSubLineas).getXdesc()); 
                else param.put("sublinea", "TODOS");
                
                if (this.ruta != null) param.put("ruta", rutasFacade.getRutaFromList(this.ruta, this.listaRutas).getXdesc()); 
                else param.put("ruta", "TODOS");
                
                if (this.canal != null) param.put("canal", canalFacade.getCanalVentaFromList(this.canal, this.listaCanales).getXdesc()); 
                else param.put("canal", "TODOS");
                
                if (this.proveedor != null) param.put("proveedor", proveedoresFacade.getProveedorFromList(this.proveedor, this.listaProveedores).getXnombre()); 
                else param.put("proveedor", "TODOS");
            
                rep.reporteLiContClientes(param, tipo, reporte);
            } else {
                List<Object[]> lista = new ArrayList<Object[]>();
                
                System.out.println("antes de sql " + System.currentTimeMillis());
                lista = excelFacade.listarParaExcel(sql);
                System.out.println("post sql " + System.currentTimeMillis());
                rep.exportarExcel(columnas, lista, reporte);                
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al ejecutar listado"));
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

    public void setResumido(Boolean resumido) {
        this.resumido = resumido;
    }
    
    public Boolean getResumido() {
        return resumido;
    }

    public Boolean getSinIVA() {
        return sinIVA;
    }

    public void setSinIVA(Boolean sinIVA) {
        this.sinIVA = sinIVA;
    }
    
    
}
