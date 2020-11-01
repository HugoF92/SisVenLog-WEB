/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import bean.selectormercaderias.SelectorDatosBean;
import dao.CanalesVentaFacade;
import dao.TiposClientesFacade;
import dao.EmpleadosFacade;
import dao.GenericFacadeSQL;
import dao.LineasFacade;
import dao.MercaderiasFacade;
import dao.ProveedoresFacade;
import dao.SublineasFacade;
import dao.TiposVentasFacade;
import dao.ZonasFacade;
import entidad.CanalesVenta;
import entidad.Clientes;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Proveedores;
import entidad.Sublineas;
import entidad.TiposClientes;
import entidad.TiposVentas;
import entidad.TmpDatos;
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
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import util.DateUtil;
import util.LlamarReportes;
import util.StringUtil;
import util.XLSUtils;

/**
 *
 * @author Bruno Brizuela
 */
@ManagedBean
@ViewScoped
public class livtascli2Bean {
    
    private Date desde;    
    private Date hasta; 
    private Empleados vendedor;
    private Zonas zona;    
    private Sublineas subLinea;
    private Lineas linea;
    private CanalesVenta canal;
    private Proveedores proveedor;
    private TiposClientes tipoCliente;
    private TiposVentas tipoVentas;
    
    private Boolean checkCliente;
    private Boolean seleccionarClientes;
    private List<Clientes> listadoClientesSeleccionados;
    
    private Boolean sinIVA = false;
    private String seleccion;
    private String seleccion2;
        
    private List<Empleados> listaVendedor;
    private List<Sublineas> listaSubLinea;
    private List<Zonas> listaZonas;
    private List<Lineas> listaLineas;    
    private List<TiposVentas> listaTiposVentas;
    private List<TiposClientes> listaTiposClientes;    
    private List<CanalesVenta> listaCanales;
    private List<Proveedores> listaProveedores;
    
    private List<Mercaderias> listaMercaderias;
    private List<Mercaderias> listaMercaderiasSeleccionadas;
    private DualListModel<Mercaderias> mercaderias;
    
    @EJB
    private EmpleadosFacade empleadoFacade;    
    @EJB
    private CanalesVentaFacade canalFacade;    
    @EJB
    private ProveedoresFacade proveedoresFacade;    
    @EJB
    private MercaderiasFacade mercaderiasFacade;
    @EJB
    private ZonasFacade zonasFacade;    
    @EJB
    private SublineasFacade sublineasFacade;
    @EJB
    private LineasFacade lineasFacade;
    @EJB
    private TiposVentasFacade tiposVentasFacade;
    @EJB
    private TiposClientesFacade tiposClientesFacade; 
    @EJB
    private GenericFacadeSQL sqlFacade;
    @Inject
    private XLSUtils xlsUtil;    
    
    
    @PostConstruct
    public void init() {
        this.seleccion = new String("1");
        this.seleccion2 = new String("1");        
        this.desde = new Date();
        this.hasta = new Date();
        
        this.checkCliente = true;
        this.seleccionarClientes = false;
        
        this.listaVendedor = empleadoFacade.listarEmpPorEmpresaTipoEstado("2", "V", "A");
        this.listaProveedores = proveedoresFacade.listarProveedoresActivos();        
        this.listaZonas = zonasFacade.listarZonas();
        this.listaSubLinea = sublineasFacade.listarSublineasConMercaderias();
        this.listaLineas = lineasFacade.listarLineasOrdenadoXCategoria();       
        this.listaCanales = canalFacade.listarCanalesOrdenadoXDesc();
        this.listaTiposVentas = tiposVentasFacade.findAll();
        this.listaTiposClientes = tiposClientesFacade.listarTiposClientes();
        this.listadoClientesSeleccionados = new ArrayList();
        
        List<Mercaderias> mercaSource = new ArrayList<Mercaderias>();
        List<Mercaderias> mercaTarget = new ArrayList<Mercaderias>();
        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();
        mercaderias = new DualListModel<Mercaderias>(mercaSource, mercaTarget);
    }
    
    public void ejecutarListado(String tipo){
        try{      
            Long inicio = System.nanoTime();
            LlamarReportes rep = new LlamarReportes();                      
            String sql = null;
            String sqlReport = null;
            String titulo = null;
            String reporte = null;
            this.listadoClientesSeleccionados = new ArrayList<Clientes>();
            
            if (!checkCliente) {
                for (TmpDatos t : sqlFacade.getDatosSelctor("select * from tmp_datos order by codigo")) {
                    Clientes c = new Clientes();
                    c.setCodCliente(Integer.valueOf(t.getCodigo()));
                    c.setXnombre(t.getDescripcion());
                    this.listadoClientesSeleccionados.add(c);
                }
            }
            
            this.preEjecutarSQL();
            Long fin = System.nanoTime();
            System.out.println("exel total sql " + (fin-inicio)/1000000 + "msegundos" );
            switch ( this.seleccion2 ) {
                case "1":
                    switch ( this.seleccion ) {
                        case "1":
                            reporte = "rvtascli2";
                            titulo = "LISTADO DE VENTAS POR CLIENTE";
                            sqlReport = "select * from mostrar m";
                            break; 
                        case "2":
                            reporte = "rvtascli1";
                            titulo = "LISTADO DE VENTAS POR CLIENTE";
                            sqlReport = "select * from mostrar m";
                            break; 
                        case "3":
                            reporte = "rvtascli";
                            titulo = "LISTADO DE VENTAS POR CLIENTE";
                            sqlReport = "select * from mostrar m";
                            break; 
                        case "4":
                            reporte = "rvtascli3";
                            titulo = "LISTADO DE VENTAS POR CLIENTE";
                            sql = " SELECT DISTINCT cod_cliente, xnombre, ctipo_docum, nrofact, cconc, ffactur, ttotal, "
                                + " tgravadas_10, nro_pedido, fpedido, tgravadas_5, "
                                + " timpuestos_10, timpuestos_5, "
                                + " texentas , xdesc as xdesc_depo, nplazo_cheque "
                                + " FROM mostrar m, depositos d "
                                + " WHERE m.cod_depo = d.cod_depo ";
                                //+ " INTO mostrar2 " ;
                            sqlReport = "select t.* from ( " + sql + " ) t ORDER BY cod_cliente, ffactur, nrofact";
                            break;
                        case "5":
                            reporte = "rvtastipvta";
                            titulo = "VENTAS DE MERCADERIAS POR TIPO DE PRECIO Y CLIENTE";
                            if (this.sinIVA) {
                                sql = " SELECT M.cod_merca, m.xdesc_merca, m.ctipo_vta, m.cod_cliente, m.xnombre, "
                                    + " SUM(m.cant_cajas) as cant_cajas, SUM(m.cant_unid) AS cant_unid, "
                                    + " SUM((p.iprecio_caja * m.cant_cajas) + (p.iprecio_unidad * m.cant_unid)) as itot_costo, "
                                    + " SUM((p2.iprecio_caja * m.cant_cajas) + (p2.iprecio_unidad * m.cant_unid)) as itot_lista, "
                                    + " p.iprecio_caja , p.iprecio_unidad,  "
                                    + " p2.iprecio_caja as ilista_caja, p2.iprecio_unidad as ilista_unidad "
                                    + " FROM mostrar m, precios p , PRECIOS p2 "
                                    + " WHERE p.cod_merca  = m.cod_merca "
                                    + "   AND p.ctipo_vta = 'X' "
                                    + "   AND p2.cod_merca = m.cod_merca "
                                    + "   AND p2.ctipo_vta = m.ctipo_vta "
                                    + "   AND m.fventa between p2.frige_desde AND p2.frige_hasta "
                                    + "   AND m.fventa between p.frige_desde AND p.frige_hasta "
                                    //+ " INTO mostrar2 "
                                    + " GROUP BY m.cod_merca, m.xdesc_merca, m.ctipo_vta, m.cod_cliente, m.xnombre, p.iprecio_caja , p.iprecio_unidad ,"
                                    + " p2.iprecio_caja, p2.iprecio_unidad, m.iret_caja, m.iret_unidad, m.idev_caja, m.idev_unidad ";
                                    //+ " ORDER BY m.cod_Merca, m.ctipo_vta, m.cod_cliente ";
                            } else {
                                sql = " SELECT M.cod_merca, m.xdesc_merca, m.ctipo_vta, m.cod_cliente, m.xnombre, "
                                + " SUM(m.cant_cajas) as cant_cajas, SUM(m.cant_unid) AS cant_unid, "
                                + " SUM(((p.iprecio_caja + (p.iprecio_caja * (i.pimpues/100)))  * m.cant_cajas) + (p.iprecio_unidad +(p.iprecio_unidad * (i.pimpues/100))* m.cant_unid)) as itot_costo, "
                                + " SUM(((p2.iprecio_caja + (p.iprecio_caja * (i.pimpues/100)))* m.cant_cajas) + (p2.iprecio_unidad + (p.iprecio_unidad * (i.pimpues/100))* m.cant_unid)) as itot_lista, "
                                + " ROUND(p.iprecio_caja + (p.iprecio_caja * (i.pimpues/100)),0) as iprecio_caja, ROUND(p.iprecio_unidad + (p.iprecio_unidad * (i.pimpues/100)),0) as iprecio_unidad, "
                                + " ROUND(p2.iprecio_caja + (p2.iprecio_caja * (i.pimpues/100)),0) as ilista_caja, ROUND(p2.iprecio_unidad + (p2.iprecio_unidad * (i.pimpues/100)),0)  as ilista_unidad , "
                                + " m.iret_caja, M.iret_unidad, m.idev_caja, m.idev_unidad "
                                + " FROM mostrar m, precios p , PRECIOS p2, merca_impuestos mi, impuestos i "
                                + " WHERE p.cod_merca  = m.cod_merca "
                                + " AND p.ctipo_vta = 'X' "
                                + " AND p2.cod_merca = m.cod_merca "
                                + " AND p2.ctipo_vta = m.ctipo_vta "
                                + " AND m.cod_merca = mi.cod_Merca "
                                + " AND mi.cod_impu = i.cod_impu "
                                + " AND m.fventa between p2.frige_desde AND p2.frige_hasta "
                                + " AND m.fventa between p.frige_desde AND p.frige_hasta "
                                //+ " INTO mostrar2 "
                                + " GROUP BY m.cod_merca, m.xdesc_merca, m.ctipo_vta, m.cod_cliente, m.xnombre, p.iprecio_caja , p.iprecio_unidad ,"
                                + " p2.iprecio_caja, p2.iprecio_unidad, i.pimpues, m.iret_caja, m.iret_unidad, m.idev_caja, m.idev_unidad ";
                                //+ " ORDER BY m.cod_Merca, m.ctipo_vta, m.cod_cliente";
                            }
                            sqlReport = "select t.* from ( " + sql + " ) t ORDER BY cod_Merca, ctipo_vta, cod_cliente";
                            break;
                    }
                    break;
                case "2":
                    reporte = "RVTASCLI4";
                    titulo = "LISTADO DE VENTAS POR CLIENTE";
                    sqlReport = "select * from mostrar m";
                    break;
                case "3":
                    reporte = "RVTASCLI5";
                    titulo = "LISTADO DE VENTAS POR CLIENTE";
                    sql = " select "
                        + " ISNULL(( SELECT SUM(itotal)  "
                        + " FROM mostrar "
                        + " WHERE (nplazo_cheque = 0 "
                        + " OR  nplazo_cheque is null ) "
                        + "     AND ctipo_docum = 'FCO' ), 0) as tot_fco, "
                        + " ISNULL(( SELECT SUM(itotal) "
                        + "    FROM mostrar "
                        + "     WHERE (nplazo_cheque = 0 "
                        + "     OR  nplazo_cheque is null ) "
                        + "     AND ctipo_docum = 'FCR' ),0) as tot_fcr, "
                        + " ISNULL(( SELECT SUM(itotal)  "
                        + "     FROM mostrar "
                        + "     WHERE nplazo_cheque > 0 "
                        + "     AND nplazo_cheque <= 15 ),0) as tot_che_1, "
                        + " ISNULL(( SELECT SUM(itotal) tot_che  "
                        + "     FROM mostrar "
                        + "     WHERE  nplazo_cheque > 15),0) as tot_che_2, "
                        + " ISNULL(( SELECT count(distinct cod_cliente) ttot_cli  "
                        + "     FROM mostrar ),0) as ttot_cli "
                        + " into totales ";
                    
                    sqlFacade.executeQuery(sql);
           
                    sql = "SELECT cod_vendedor, "
		        + " xvendedor, SUM(cant_cajas) as cant_cajas, SUM(cant_unid) AS cant_unid, "
                        + " SUM(itotal) as itotal, count(distinct cod_cliente) as tot_cli "
                        + " FROM mostrar "
                        //+ " INTO mostrar2 "
                        + " GROUP BY cod_vendedor, xvendedor ";
                    sqlReport = "select t.*, tot.* from ( " + sql + " ) t, totales tot ";
                    break;
            }
            
            System.err.println("reporte: "+reporte);
            System.err.println("sqlReport: "+sqlReport);
            
            if (tipo.equals("VIST")){
                String usuImprime = "";//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                Map param = new HashMap();
                param.put("sql", sqlReport);
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
                
                if (this.subLinea != null) param.put("sublinea", sublineasFacade.getSubLineaFromList(this.subLinea, this.listaSubLinea).getXdesc()); 
                else param.put("sublinea", "TODOS");
                                
                if (this.canal != null) param.put("canal_vta", canalFacade.getCanalVentaFromList(this.canal, this.listaCanales).getXdesc()); 
                else param.put("canal_vta", "TODOS");
                
                if (this.proveedor != null) param.put("proveedor", proveedoresFacade.getProveedorFromList(this.proveedor, this.listaProveedores).getXnombre()); 
                else param.put("proveedor", "TODOS");
                
                if (this.tipoCliente != null) param.put("tipo_cliente", tiposClientesFacade.getTiposClientesFromList(this.tipoCliente, this.listaTiposClientes).getXdesc()); 
                else param.put("tipo_cliente", "TODOS");
                
                if (this.tipoVentas != null) param.put("tipoVentas", tiposVentasFacade.getTipoVentaFromList(this.tipoVentas, this.listaTiposVentas).getXdesc()); 
                else param.put("tipoVentas", "TODOS");
                
                /*if (1 != 2) param.put("precio_vta", tiposVentasFacade.getTiposVentasFromList(this.tipoVentas, this.listaTiposVentas).getXdesc()); 
                else */param.put("precio_vta", "TODOS");
                
                if(!listadoClientesSeleccionados.isEmpty()) param.put("cliente", "(" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ")");
                else param.put("cliente", "TODOS");
                
                param.put("sinIva", this.sinIVA);
            
                rep.reporteLiContClientes(param, tipo, reporte);
                
            } else {
                //ResultSet r = excelFacade.ejecutarSQLQueryParaExcelGenerico(sqlReport);
                xlsUtil.exportarExcelGenerico(sqlReport, reporte);
                                
                /*inicio = System.nanoTime();
                List<Object[]> lista = new ArrayList<Object[]>();
                String[] columnas = new String[40];
                lista = excelFacade.listarParaExcel(sqlReport);
                fin = System.nanoTime();
                System.out.println("exel total sql " + (fin-inicio)/1000000 + "msegundos" );
                rep.exportarExcel(columnas, lista, reporte);  
                */
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al ejecutar listado"));
        }
    }
    
    public void preEjecutarSQL() throws Exception {
        String fdesde = DateUtil.formaterDateToString(this.desde, "yyyy-MM-dd");
        String fhasta = DateUtil.formaterDateToString(this.hasta, "yyyy-MM-dd");  
        
        String sql1 = "IF OBJECT_ID('Mostrarx') IS NOT NULL DROP TABLE Mostrarx\n ";
        String sql2 = "IF OBJECT_ID('Mostrarc') IS NOT NULL DROP TABLE Mostrarc\n ";
        String sql3 = " IF OBJECT_ID('mostrar') IS NOT NULL DROP TABLE mostrar\n "
                + " IF Object_id('mostrarh') IS NOT NULL DROP TABLE mostrarh\n "
                + " IF Object_id('mostrar1') IS NOT NULL DROP TABLE mostrar1\n "
                + " IF Object_id('curnotas') IS NOT NULL DROP TABLE curnotas\n "
                //+ " IF Object_id('curnotas1') IS NOT NULL DROP TABLE curnotas1\n "
                + " IF Object_id('totales') IS NOT NULL DROP TABLE totales\n ";
        String sql4 = null;
        String sql5 = null;
        
        sql1 = "SELECT a.cod_zona, z.xdesc AS xdesc_zona, a.cod_cliente, a.xrazon_social as xnombre, "
                + " s.cod_sublinea, s.xdesc AS xdesc_sublinea, r.xdesc AS xdesc_ruta, a.ctipo_docum,   "
                + " a.nrofact AS nrofact, a.ffactur, f.cod_merca, m.xdesc AS xdesc_merca, f.cant_cajas AS cant_cajas, "
                + " f.cant_unid AS cant_unid,   f.impuestos AS timpuestos, f.igravadas AS tgravadas, "
                + " f.iexentas AS texentas,     a.ttotal as total_fac,  a.nro_pedido, a.cod_depo, p.fpedido, "
                + " a.ttotal, a.nplazo_cheque,  a.tgravadas_10, a.tgravadas_5, a.timpuestos_10, a.timpuestos_5, "
                + " f.itotal  as itotal, a.cod_vendedor, e.xnombre as xvendedor,   a.ctipo_vta , A.ffactur as fventa, "
                + " a.ttotal + a.timpuestos as itot_sin, f.itotal + f.impuestos as det_sin, f.iprecio_caja, "
                + " f.iprecio_unidad "
                + " FROM  facturas_det f INNER JOIN facturas a ON f.cod_empr = a.cod_empr "
                + " AND f.nrofact = a.nrofact "
                + " AND f.ctipo_docum = a.ctipo_docum AND f.ffactur = a.ffactur INNER JOIN "
                + " mercaderias m ON f.cod_empr = m.cod_empr AND f.cod_merca = m.cod_merca INNER JOIN "
                + " clientes c ON a.cod_cliente = c.cod_cliente AND a.cod_empr = c.cod_empr INNER JOIN " 
                + " sublineas s ON m.cod_sublinea = s.cod_sublinea INNER JOIN " 
                + " rutas r ON a.cod_ruta = r.cod_ruta AND a.cod_empr = r.cod_empr INNER JOIN   "
                + " zonas z ON a.cod_zona = z.cod_zona AND a.cod_empr = z.cod_empr  "
                + " LEFT OUTER JOIN   "
                + " pedidos p ON a.nro_pedido = p.nro_pedido AND a.cod_empr = p.cod_empr INNER JOIN "
                + " empleados e ON a.cod_vendedor = e.cod_empleado AND a.cod_empr = e.cod_empr  "
                + " WHERE (f.cod_empr = 2) AND a.cod_empr = 2 "
                + " AND (a.mestado = 'A') "
                + " AND (a.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+"') " ;
        
        if(!listadoClientesSeleccionados.isEmpty()){
            sql1 += " AND c.cod_cliente in (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ")";
        }
        if (mercaderias.getTarget().size() > 0) {
            listaMercaderiasSeleccionadas = mercaderias.getTarget();
            sql1 += " AND m.cod_merca IN (";
            for (int i = 0; i < listaMercaderiasSeleccionadas.size(); i++) {
                MercaderiasPK pk = listaMercaderiasSeleccionadas.get(i).getMercaderiasPK();
                sql1 += " '" + pk.getCodMerca() + "',";
            }
            sql1 = sql1.substring(0, sql1.length()-1) + " ) ";
        }        
        if (this.zona != null){ 
            sql1 += " AND r.cod_zona = '" + this.zona.getZonasPK().getCodZona() +"' ";
        }
        if (this.subLinea != null){ 
            sql1 += " AND m.cod_sublinea = '"+this.subLinea.getCodSublinea() +"' ";
        }
        if (this.linea != null){ 
            sql1 += " AND s.cod_linea = '"+this.linea.getCodLinea() +"' ";
        }
        if (this.vendedor != null){ 
            sql1 += " AND a.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
        }        
        if (this.canal != null){ 
            sql1 += " AND a.cod_canal  = '" + this.canal.getCodCanal()  +"' ";
        }
        if (this.proveedor != null){ 
            sql1 += " AND m.cod_proveed = '" + this.proveedor.getCodProveed() +"' ";
        }
        if (this.tipoCliente != null){ 
            sql1 += " AND c.ctipo_cliente = '" + this.tipoCliente.getCtipoCliente() +"' ";
        }
        if (this.tipoVentas != null){ 
            sql1 += " AND a.ctipo_vta  = '" + this.tipoVentas.getTiposVentasPK().getCtipoVta() +"' ";
        }
        
        if (this.canal != null){
            if ("CR".equals(this.canal.getCodCanal())){
                sql1 += " AND (a.nplazo_cheque >0 or (a.fvenc != '' AND a.fvenc IS NOT NULL)) " ;
            } else if ("CO".equals(this.canal.getCodCanal())) {
                sql1 += " AND a.nplazo_cheque= 0 AND (a.fvenc = '' OR a.fvenc IS NULL) " ;
            }
        }        
        //Query q = em.createNativeQuery(sql1);
        //q.executeUpdate();
        //q.getResultList();        
        //em.clear();
        //sqlFacade.executeQuery(sql1);
        // Mostrarx cursor con las mercaderias vendidas         
        sql2 = " SELECT t.* FROM \n ( "
                + " SELECT  f.cod_zona, z.xdesc AS xdesc_zona, n.cod_cliente, f.xrazon_social as xnombre, "
                + " s.cod_sublinea,    s.xdesc AS xdesc_sublinea, r.xdesc AS xdesc_ruta, f.ctipo_docum,  "
                + " N.nro_nota AS nrofact, N.CCONC, n.fdocum, d.cod_merca, m.xdesc AS xdesc_merca, " 
                + " d.cant_cajas AS cant_cajas, d.cant_unid AS cant_unid, "
                + " 0 as timpuestos, 0 as tgravadas, n.texentas as texentas, 0 as total_fac, n.nrofact as nro_pedido, "
                + " n.cod_depo, f.ffactur as fpedido, n.ttotal, f.cod_vendedor, e.xnombre as xvendedor, f.nplazo_cheque, "
                + " SUM(d.igravadas) as tgravadas_10, 0 as tgravadas_5, SUM(d.impuestos) as timpuestos_10, "
                + " 000000000 as timpuestos_5, 000000000 as itotal,   f.ctipo_vta , f.ffactur as fventa, "
                + " sum(d.igravadas + d.impuestos + d.iexentas) as itot_sin, sum(d.igravadas + d.impuestos + d.iexentas) as det_sin, "
                + " n.ctipo_docum as not_ctipo_docum, d.iprecio_caja, d.iprecio_unid "
                + " FROM notas_ventas N, facturas f, notas_ventas_det d, zonas z, sublineas s, mercaderias m, "
                + " rutas r, clientes c, empleados e "
                + " WHERE n.fdocum BETWEEN '"+fdesde+"' AND '"+fhasta+"' "
                + "      AND  n.cod_empr = f.cod_empr "
                + "      AND n.nrofact = f.nrofact  "
                + "      AND n.ffactur = f.ffactur "
                + "                   AND n.fac_ctipo_docum = f.ctipo_docum  "
                + "                   AND n.cod_empr = d.cod_empr  "
                + "                   AND n.fdocum = d.fdocum  "
                + "                   AND n.nro_nota = d.nro_nota  "
                + "                   AND n.ctipo_docum = d.ctipo_docum  "
                + "                   AND f.cod_zona = z.cod_zona  "
                + "                   AND f.cod_empr = z.cod_empr  "
                + "                   AND d.cod_merca = m.cod_merca  "
                + "                   AND d.cod_empr = m.cod_empr  "
                + "                   AND m.cod_sublinea = s.cod_sublinea  "
                + "                   AND n.cod_cliente = c.cod_cliente   "
                + "                   AND n.cod_empr = c.cod_empr  "
                + "                   AND c.cod_ruta = r.cod_ruta  "
                + "                   AND c.cod_empr = r.cod_empr  "
                + "                   AND f.cod_vendedor = e.cod_empleado  "
                + "                   AND f.cod_empr = e.cod_empr   "
                + "                     AND n.cod_empr = 2  "
                + "                     AND d.pimpues = 10   "
                + "                     AND n.mestado = 'A' "
                + "                     AND n.ctipo_docum IN ( 'NDV', 'NCV' ) ";
        
        if(!listadoClientesSeleccionados.isEmpty()){
            sql2 += " AND c.cod_cliente in (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ")";
        }
        if (mercaderias.getTarget().size() > 0) {
            listaMercaderiasSeleccionadas = mercaderias.getTarget();
            sql2 += " AND m.cod_merca IN (";
            for (int i = 0; i < listaMercaderiasSeleccionadas.size(); i++) {
                MercaderiasPK pk = listaMercaderiasSeleccionadas.get(i).getMercaderiasPK();
                sql2 += " '" + pk.getCodMerca() + "',";
            }
            sql2 = sql2.substring(0, sql2.length()-1) + " ) ";
        }        
        if (this.zona != null){ 
            sql2 += " AND r.cod_zona = '" + this.zona.getZonasPK().getCodZona() +"' ";
        }
        if (this.subLinea != null){ 
            sql2 += " AND m.cod_sublinea = '"+this.subLinea.getCodSublinea() +"' ";
        }
        if (this.linea != null){ 
            sql2 += " AND s.cod_linea = '"+this.linea.getCodLinea() +"' ";
        }
        if (this.vendedor != null){ 
            sql2 += " AND f.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
        }        
        if (this.canal != null){ 
            sql2 += " AND f.cod_canal  = '" + this.canal.getCodCanal()  +"' ";
        }
        if (this.proveedor != null){ 
            sql2 += " AND m.cod_proveed = '" + this.proveedor.getCodProveed() +"' ";
        }
        if (this.tipoCliente != null){ 
            sql2 += " AND c.ctipo_cliente = '" + this.tipoCliente.getCtipoCliente() +"' ";
        }
        if (this.tipoVentas != null){ 
            sql2 += " AND f.ctipo_vta  = '" + this.tipoVentas.getTiposVentasPK().getCtipoVta() +"' ";
        }
        
        if (this.canal != null){
            if ("CR".equals(this.canal.getCodCanal())){
                sql2 += " AND (f.nplazo_cheque >0 or (f.fvenc != '' AND f.fvenc IS NOT NULL)) " ;
            } else if ("CO".equals(this.canal.getCodCanal())) {
                sql2 += " (AND f.nplazo_cheque= 0 AND f.fvenc = '' OR f.fvenc IS NULL) " ;
            }
        }
        
        sql2 += " \n GROUP BY  F.cod_zona, z.xdesc, n.cod_cliente, f.xrazon_social, s.cod_sublinea, "
                + " s.xdesc, r.xdesc, F.ctipo_docum,  "
                + " n.nro_nota, n.cconc, n.fdocum, d.cod_merca, m.xdesc, d.cant_cajas, d.cant_unid, n.nrofact, "
                + " n.cod_depo, n.ttotal, n.texentas, f.ffactur, f.cod_vendedor, e.xnombre, f.nplazo_cheque, "
                + " f.ctipo_vta, n.ctipo_docum, d.iprecio_caja, d.iprecio_unid "
                + " \nUNION ALL\n " 
                + " SELECT  f.cod_zona, z.xdesc AS xdesc_zona, n.cod_cliente, f.xrazon_social as xnombre, s.cod_sublinea, "
                + " s.xdesc AS xdesc_sublinea,  r.xdesc AS xdesc_ruta, F.ctipo_docum,  "
                + " n.nro_nota AS nrofact, n.cconc, n.fdocum, d.cod_merca, m.xdesc AS xdesc_merca, d.cant_cajas AS cant_cajas, "
                + " d.cant_unid AS cant_unid, 0 as timpuestos, 0 as tgravadas, n.texentas as texentas, "
                + " 0 as total_fac, n.nrofact as nro_pedido, n.cod_depo, f.ffactur as fpedido, n.ttotal, f.cod_vendedor, "
                + " e.xnombre as xvendedor, f.nplazo_cheque,   0 as tgravadas_10, SUM(d.igravadas) as tgravadas_5, "
                + " 0 as timpuestos_10, SUM(d.impuestos) as timpuestos_5, "
                + " 0 as itotal ,  f.ctipo_vta, f.ffactur as fventa, sum(d.igravadas + d.impuestos + d.iexentas) as itot_sin, "
                + " sum(d.igravadas + d.impuestos + d.iexentas) as det_sin, n.ctipo_docum as not_ctipo_docum, "
                + " d.iprecio_caja, d.iprecio_unid  "
                + " FROM notas_ventas N, facturas f, notas_ventas_det d, zonas z, sublineas s, "
                + " mercaderias m, clientes c, rutas r, empleados e "
                + " WHERE n.fdocum BETWEEN '"+fdesde+"' AND '"+fhasta+"' "
                + "         AND n.cod_empr = f.cod_empr	 "
                + "         AND n.nrofact = f.nrofact  "
                + "         AND n.ffactur = f.ffactur  "
                + "                   AND n.fac_ctipo_docum = f.ctipo_docum   "
                + "                   AND n.cod_empr = d.cod_empr  "
                + "                   AND n.fdocum = d.fdocum  "
                + "                   AND n.nro_nota = d.nro_nota  "
                + "                   AND n.ctipo_docum = d.ctipo_docum  "
                + "                   AND f.cod_zona = z.cod_zona  "
                + "                   AND f.cod_empr = z.cod_empr "
                + "                   AND d.cod_merca = m.cod_merca "
                + "                   AND d.cod_empr = m.cod_empr "
                + "         AND m.cod_sublinea = s.cod_sublinea  "
                + "                   AND n.cod_cliente = c.cod_cliente  "
                + "                   AND n.cod_empr = c.cod_empr  "
                + "                   AND c.cod_ruta = r.cod_ruta  "
                + "                   AND c.cod_empr = r.cod_empr  "
                + "                   AND f.cod_vendedor = e.cod_empleado  "
                + "                   AND f.cod_empr = e.cod_empr   "
                + "                 AND n.cod_empr = 2  "
                + "                 AND d.pimpues = 5   "
                + "                 AND n.mestado = 'A' "
                + "                 AND n.ctipo_docum IN ( 'NDV', 'NCV' )  ";
 
        if(!listadoClientesSeleccionados.isEmpty()){
            sql2 += " AND c.cod_cliente in (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ")";
        }
        if (mercaderias.getTarget().size() > 0) {
            listaMercaderiasSeleccionadas = mercaderias.getTarget();
            sql2 += " AND m.cod_merca IN (";
            for (int i = 0; i < listaMercaderiasSeleccionadas.size(); i++) {
                MercaderiasPK pk = listaMercaderiasSeleccionadas.get(i).getMercaderiasPK();
                sql2 += " '" + pk.getCodMerca() + "',";
            }
            sql2 = sql2.substring(0, sql2.length()-1) + " ) ";
        }        
        if (this.zona != null){ 
            sql2 += " AND r.cod_zona = '" + this.zona.getZonasPK().getCodZona() +"' ";
        }
        if (this.subLinea != null){ 
            sql2 += " AND m.cod_sublinea = '"+this.subLinea.getCodSublinea() +"' ";
        }
        if (this.linea != null){ 
            sql2 += " AND s.cod_linea = '"+this.linea.getCodLinea() +"' ";
        }
        if (this.vendedor != null){ 
            sql2 += " AND f.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
        }        
        if (this.canal != null){ 
            sql2 += " AND f.cod_canal  = '" + this.canal.getCodCanal()  +"' ";
        }
        if (this.proveedor != null){ 
            sql2 += " AND m.cod_proveed = '" + this.proveedor.getCodProveed() +"' ";
        }
        if (this.tipoCliente != null){ 
            sql2 += " AND c.ctipo_cliente = '" + this.tipoCliente.getCtipoCliente() +"' ";
        }
        if (this.tipoVentas != null){ 
            sql2 += " AND f.ctipo_vta  = '" + this.tipoVentas.getTiposVentasPK().getCtipoVta() +"' ";
        }
        
        if (this.canal != null){
            if ("CR".equals(this.canal.getCodCanal())){
                sql2 += " AND (f.nplazo_cheque >0 or (f.FVENC != '' AND f.fvenc IS NOT NULL))  " ;
            } else if ("CO".equals(this.canal.getCodCanal())) {
                sql2 += " (AND f.nplazo_cheque= 0 AND f.fvenc = '' OR f.fvenc IS NULL) " ;
            }
        }
        
        sql2 += " \nGROUP BY  F.cod_zona, z.xdesc, n.cod_cliente, f.xrazon_social, s.cod_sublinea, "
                + " s.xdesc, r.xdesc, F.ctipo_docum,  "
                + " n.nro_nota, n.cconc, n.fdocum, d.cod_merca, m.xdesc, d.cant_cajas, "
                + " d.cant_unid, n.nrofact, n.cod_depo, n.fdocum,  "
                + " n.ttotal, n.texentas, f.ffactur, f.cod_vendedor, e.xnombre, f.nplazo_cheque, "
                + " f.ctipo_vta, n.ctipo_docum, d.iprecio_caja, d.iprecio_unid "
                + " ) t ";
        
        //q = em.createNativeQuery(sql2);
        //q.getResultList();
        //q.executeUpdate();
        //em.clear();
        //System.err.println("query1: " + sql1);
        //System.err.println("query2: " + sql2);
        //sqlFacade.executeQuery(sql2);
        sqlFacade.executeQuery(sql3);
        //q = em.createNativeQuery("select * from Mostrarc");
        // ejecutar sql2 y ver si trae resultados
        Integer count = (Integer) sqlFacade.executeQueryObject("select count(1) from ("+sql2+") t ");
        System.out.println("objectList: "+count);
        if (count==null | new Integer("0").equals(count)){
            if (this.seleccion.equals("4")){
                if (this.sinIVA) { // sin iva
                    sql3 += " SELECT t.* INTO mostrar FROM ( select cod_zona,  xdesc_zona, cod_cliente, xnombre, "
			+ " xdesc_ruta, ctipo_docum,  nrofact, '   ' as cconc, ffactur, " 
			+ " 0 AS timpuestos, tgravadas + timpuestos as tgravadas, texentas, "
                        + " total_fac - ABS(timpuestos) as total_fac,  "
                        + " nro_pedido, cod_depo, fpedido, "
                        + " ttotal - ABS(timpuestos_10) - ABS(timpuestos_5) as ttotal,"
			+ " tgravadas_10 - ABS(timpuestos_10) as tgravadas_10, "
                        + " tgravadas_5 - ABS(timpuestos_5) as tgravadas_5, "
                        + " 0 as timpuestos_10, 0 as timpuestos_5, cod_vendedor, "
                        + " xvendedor, nplazo_cheque, " 
                        + " cant_cajas, cant_unid, det_sin as itotal "
			+ " FROM ( "+sql1+" ) x ) t ";
                } else { // con iva		      
                    sql3 += " SELECT t.* INTO mostrar FROM (select cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, '   ' as cconc, ffactur,  "
			+ " timpuestos, tgravadas, texentas, total_fac,  "
			+ " nro_pedido, cod_depo, fpedido, ttotal,  "
			+ " tgravadas_10 , tgravadas_5, "
			+ " timpuestos_10, timpuestos_5, cod_vendedor, xvendedor, nplazo_cheque, "
                        + " cant_cajas, cant_unid, det_sin as itotal "
			+ " FROM ( "+sql1+" ) x ) t ";
                }
            } else {
                if (this.sinIVA) { // sin iva 
                    sql3 += " SELECT t.* INTO mostrar1 FROM ( SELECT  cod_zona,  xdesc_zona, cod_cliente,  xnombre, "
                        + " cod_sublinea,  xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum, "
			+ " nrofact, ffactur, cod_merca, xdesc_merca, cant_cajas, cant_unid, "
			+ " timpuestos,  tgravadas + timpuestos as tgravadas,  "
                        + " texentas, total_fac - ABS(timpuestos) as total_fac, "
			+ " nro_pedido, cod_depo, fpedido, ttotal, nplazo_cheque, "
                        + " tgravadas_10 - ABS(timpuestos_10) as tgravadas_10,  "
                        + " tgravadas_5 - ABS(timpuestos_5) as tgravadas_5, "
			+ " 0 as timpuestos_10, timpuestos_5, det_sin as itotal, cod_vendedor, "
			+ " xvendedor, ctipo_vta ,  fventa, iprecio_caja, iprecio_unidad "
			+ " FROM ( "+sql1+" ) x ) t "
			+ "ORDER BY cod_cliente, cod_sublinea, cod_merca ";
                } else {
                    sql3 += " SELECT t.* INTO mostrar1 FROM ( SELECT * "
			+ " FROM ( "+sql1+" ) x ) t "
			+ " ORDER BY cod_cliente, cod_sublinea, cod_merca ";
                }
            }
        } else {
            if (this.seleccion.equals("4")){
                if (this.sinIVA) {
                    sql4 = " SELECT t.* FROM ( SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                        + " xdesc_ruta, ctipo_docum,  "
                        + " nrofact, cconc,  fdocum as ffactur,  "
                        + " timpuestos, tgravadas + timpuestos as tgravadas, texentas, total_fac, nplazo_cheque, "
                        + " nro_pedido, cod_depo, fpedido, sum(itot_sin) * -1 as ttotal, cod_vendedor, xvendedor,  "
                        + " SUM(ABS(tgravadas_10) - ABS(timpuestos_10)) * -1 as tgravadas_10 , "
                        + " SUM(ABS(tgravadas_5) - ABS(timpuestos_5)) * -1 as tgravadas_5,  "
                        + " 0 as timpuestos_10,  0 as timpuestos_5, "
                        + " sum( CASE CCONC " 
                        + "    WHEN 'DEV' THEN cant_cajas * -1" 
                        + "    ELSE 0 " 
                        + " END ) as cant_cajas, sum( CASE CCONC " 
                        + "    WHEN 'DEV' THEN cant_unid * -1 " 
                        + "    ELSE 0  " 
                        + " END) as cant_unid, "
                        + " sum(det_sin) *-1 as itotal  "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE not_ctipo_docum = 'NCV' "
                        + " \nGROUP BY cod_zona,  xdesc_zona, cod_cliente, xnombre,  "
                        + " xdesc_ruta, ctipo_docum,  "
                        + " nrofact, cconc, fdocum,  "
                        + " timpuestos, tgravadas, texentas, total_fac, "
                        + " nro_pedido, cod_depo, fpedido,  cod_vendedor, xvendedor, nplazo_cheque "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                        + " xdesc_ruta, ctipo_docum,  "
                        + " nrofact, cconc,  fdocum as ffactur,  "
                        + " timpuestos, tgravadas + timpuestos as tgravadas, texentas, total_fac, nplazo_cheque, "
                        + " nro_pedido, cod_depo, fpedido, sum(itot_sin)  as ttotal, cod_vendedor, xvendedor,  "
                        + " SUM(ABS(tgravadas_10) - ABS(timpuestos_10))  as tgravadas_10 , "
                        + " SUM(ABS(tgravadas_5) - ABS(timpuestos_5))  as tgravadas_5,  "
                        + " 0 as timpuestos_10,  0 as timpuestos_5, "
                        + " 0 as cant_cajas, 0 as cant_unid, "
                        + " sum(det_sin) *-1  as itotal "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE not_ctipo_docum = 'NDV' "
                        + " \nGROUP BY cod_zona,  xdesc_zona, cod_cliente, xnombre,  "
                        + " xdesc_ruta, ctipo_docum,  "
                        + " nrofact, cconc, fdocum,  "
                        + " timpuestos, tgravadas, texentas, total_fac, "
                        + " nro_pedido, cod_depo, fpedido,  cod_vendedor, xvendedor, nplazo_cheque) t ";
                } else {
                    sql4 = " SELECT t.* FROM ( SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                        + " xdesc_ruta, ctipo_docum,  "
                        + " nrofact, cconc,  fdocum as ffactur,  "
                        + " timpuestos, tgravadas, texentas, total_fac, nplazo_cheque, " 
                        + " nro_pedido, cod_depo, fpedido, ttotal * -1 as ttotal, cod_vendedor, xvendedor, "
                        + " SUM(tgravadas_10) * -1 as tgravadas_10 , SUM(tgravadas_5) * -1 as tgravadas_5, "
                        + " ABS(SUM(timpuestos_10)) * -1 as timpuestos_10,  ABS(SUM(timpuestos_5)) * -1 as timpuestos_5, "
                        + " sum( CASE CCONC " 
                        + "    WHEN 'DEV' THEN cant_cajas * -1" 
                        + "    ELSE 0 " 
                        + " END ) as cant_cajas, sum( CASE CCONC " 
                        + "    WHEN 'DEV' THEN cant_unid * -1 " 
                        + "    ELSE 0  " 
                        + " END) as cant_unid, "
                        + " sum(tgravadas_10+texentas+tgravadas_5+ timpuestos) * -1 as itotal  "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE not_ctipo_docum ='NCV'  "
                        + " \nGROUP BY cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                        + " xdesc_ruta, ctipo_docum, "
                        + " nrofact, cconc, fdocum, "
                        + " timpuestos, tgravadas, texentas, total_fac, "
                        + " nro_pedido, cod_depo, fpedido,  ttotal, cod_vendedor, xvendedor, nplazo_cheque "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                        + " xdesc_ruta, ctipo_docum,  "
                        + " nrofact, cconc,  fdocum as ffactur,  "
                        + " timpuestos, tgravadas, texentas, total_fac, nplazo_cheque, "
                        + " nro_pedido, cod_depo, fpedido, ttotal  as ttotal, cod_vendedor, xvendedor,    "
                        + " SUM(tgravadas_10) as tgravadas_10 , SUM(tgravadas_5)  as tgravadas_5,  "
                        + " ABS(SUM(timpuestos_10))  as timpuestos_10,  ABS(SUM(timpuestos_5))  as timpuestos_5, "
                        + " 0 as cant_cajas, 0 as cant_unid, "
                        + " sum(tgravadas_10+texentas+tgravadas_5+ timpuestos)  as itotal "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE not_ctipo_docum ='NDV'  "
                        + " \nGROUP BY cod_zona,  xdesc_zona, cod_cliente, xnombre,  "
                        + "     xdesc_ruta, ctipo_docum,  "
                        + "     nrofact, cconc, fdocum,  "
                        + "     timpuestos, tgravadas, texentas, total_fac, "
                        + "     nro_pedido, cod_depo, fpedido, ttotal, cod_vendedor, xvendedor, nplazo_cheque) t ";
                }
            
                sql5 = " SELECT tn.* FROM ( SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, "
		+ " xdesc_ruta, ctipo_docum,  "
 		+ " nrofact, cconc,  ffactur,  "
		+ " timpuestos, tgravadas, texentas, total_fac, nplazo_cheque, "
		+ " nro_pedido, cod_depo, fpedido, ttotal  as ttotal, cod_vendedor, xvendedor,  "
		+ " SUM(tgravadas_10) as tgravadas_10 , SUM(tgravadas_5)  as tgravadas_5, "
		+ " ABS(SUM(timpuestos_10))  as timpuestos_10,  ABS(SUM(timpuestos_5))  as timpuestos_5, "
                + " sum(cant_cajas) as cant_cajas, sum(cant_unid) as cant_unid, sum(itotal) as itotal  "
		+ " FROM ( "+sql4+" ) te "
		+ " \nGROUP BY cod_zona,  xdesc_zona, cod_cliente, xnombre,  "
		+ " xdesc_ruta, ctipo_docum,  "
		+ " nrofact, cconc, ffactur,  "
		+ " timpuestos, tgravadas, texentas, total_fac, "
		+ " nro_pedido, cod_depo, fpedido, ttotal, cod_vendedor, xvendedor, nplazo_cheque ) tn ";
            
                if (this.sinIVA) {
                    sql3 += " SELECT tt.* INTO mostrar FROM (  select cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                    + " xdesc_ruta, ctipo_docum,  nrofact, '   ' as cconc, ffactur,  "
                    + " timpuestos, tgravadas, texentas, total_fac - ABS(timpuestos) as total_fac,  "
                    + " nro_pedido, cod_depo, fpedido, itot_sin as ttotal, "
                    + " tgravadas_10 - ABS(timpuestos_10) as tgravadas_10, tgravadas_5 - ABS(timpuestos_5) as tgravadas_5,  "
                    + " 0 as timpuestos_10, 0 as timpuestos_5, cod_vendedor, xvendedor, nplazo_cheque, cant_cajas, cant_unid, det_sin as itotal "
                    + " FROM ( "+sql1+" ) x "
                    + " \nUNION ALL\n "
                    + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                    + " xdesc_ruta, ctipo_docum,  nrofact, cconc, ffactur,  "
                    + " 0 as timpuestos , tgravadas - ABS(timpuestos) as  tgravadas, texentas, total_fac - ABS(timpuestos) as total_fac,  "
                    + " nro_pedido, cod_depo, fpedido, ttotal,  "
                    + " tgravadas_10  , tgravadas_5 , "
                    + " 0 as timpuestos_10, 0 as timpuestos_5 , cod_vendedor, xvendedor, nplazo_cheque, cant_cajas, cant_unid, itotal "
                    + " FROM ( "+sql5+" ) y ) tt ";
                } else {
                    sql3 += " SELECT tt.* INTO mostrar FROM ( select cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, '   ' as cconc, ffactur, "
                        + " timpuestos, tgravadas, texentas, total_fac,  "
                        + " nro_pedido, cod_depo, fpedido, ttotal,  "
                        + " tgravadas_10 , tgravadas_5, "
                        + " timpuestos_10, timpuestos_5, cod_vendedor, xvendedor, nplazo_cheque, cant_cajas, cant_unid, itotal "
                        + " FROM ( "+sql1+" ) x "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, cconc, ffactur,  "
                        + " timpuestos, tgravadas, texentas, total_fac,  "
                        + " nro_pedido, cod_depo, fpedido, ttotal,  "
                        + " tgravadas_10 , tgravadas_5, "
                        + " timpuestos_10, timpuestos_5 , cod_vendedor, xvendedor, nplazo_cheque, cant_cajas, cant_unid, itotal "
                        + " FROM ( "+sql5+" ) y ) tt " ;
                }
            
            } else {
                if (this.sinIVA) {
                    sql3 += " SELECT t.* INTO mostrar1 FROM ( SELECT  cod_zona,  xdesc_zona, "
                        + " cod_cliente, xnombre,     cod_sublinea, xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum, "
                        + " nrofact, '   ' as cconc, ffactur, cod_merca,  xdesc_merca,  cant_cajas,  cant_unid, "
                        + " timpuestos, tgravadas - ABS(timpuestos_10) - ABS(timpuestos_5) as tgravadas, texentas, "
                        + " total_fac - ABS(timpuestos_10) - ABS(timpuestos_5) as total_fac, det_sin as itotal, "
                        + " nro_pedido, cod_depo, fpedido,  ttotal - ABS(timpuestos_10) - ABS(timpuestos_5) as ttotal, "
                        + " cod_vendedor, xvendedor , nplazo_cheque , CTIPO_VTA, fventa, "
                        + " iprecio_caja, iprecio_unidad "
                        + " FROM ( "+sql1+" ) x "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, cod_sublinea, xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, cconc, fdocum as ffactur,  "
                        + " cod_merca, xdesc_merca, cant_cajas * -1, cant_unid * -1, "
                        + " timpuestos, tgravadas - ABS(timpuestos) as tgravadas, texentas, total_fac -  ABS(timpuestos) as total_fac, "
                        + " det_sin * -1 as itotal, "
                        + " nro_pedido, cod_depo, fpedido, ttotal, cod_vendedor, xvendedor  , nplazo_cheque, CTIPO_VTA, fventa,  "
                        + " iprecio_caja, iprecio_unid as iprecio_unidad "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE CCONC = 'DEV' "
                        + " AND Not_CTIPO_DOCUM = 'NCV' "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, cod_sublinea, xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, cconc, fdocum as ffactur,  "
                        + " cod_merca, xdesc_merca, 0 as cant_cajas, 0 as cant_unid , "
                        + " timpuestos, tgravadas - ABS(timpuestos) as tgravadas, texentas, total_fac - ABS(timpuestos) as total_fac, "
                        + " det_sin * -1 as itotal, "
                        + " nro_pedido, cod_depo, fpedido, ttotal, cod_vendedor, xvendedor   , nplazo_cheque, CTIPO_VTA, fventa,  "
                        + " iprecio_caja, iprecio_unid as iprecio_unidad "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE CCONC != 'DEV' "
                        + " AND not_CTIPO_DOCUM = 'NCV' "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, cod_sublinea, xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, cconc, fdocum as ffactur, "
                        + " cod_merca, xdesc_merca, 0 as cant_cajas, 0 as cant_unid, "
                        + " timpuestos, tgravadas - ABS(timpuestos) as tgravadas, texentas, total_fac - ABS(timpuestos) as total_fac, "
                        + " det_sin  as itotal, "
                        + " nro_pedido, cod_depo, fpedido, ttotal, cod_vendedor, xvendedor   , nplazo_cheque, CTIPO_VTA, fventa, "
                        + " iprecio_caja, iprecio_unid as iprecio_unidad "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE not_CTIPO_DOCUM = 'NDV' ) t ORDER BY cod_cliente, cod_sublinea, cod_merca ";
                } else {
                    sql3 += " SELECT t.* INTO mostrar1 FROM ( SELECT  cod_zona,  xdesc_zona, "
                        + " cod_cliente, xnombre, cod_sublinea, xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum,  "
                        + " nrofact, '   ' as cconc, ffactur, cod_merca,  xdesc_merca,  cant_cajas,  cant_unid, "
                        + " timpuestos, tgravadas, texentas, total_fac, itotal, "
                        + " nro_pedido, cod_depo, fpedido,  ttotal, cod_vendedor, "
                        + " xvendedor , nplazo_cheque , CTIPO_VTA, fventa ,  iprecio_caja, iprecio_unidad "
                        + " FROM ( " +sql1+ " ) x "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, cod_sublinea, xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, cconc, fdocum as ffactur,  "
                        + " cod_merca, xdesc_merca, cant_cajas * -1, cant_unid * -1, "
                        + " timpuestos, tgravadas, texentas, total_fac, (tgravadas_10+texentas+tgravadas_5+ timpuestos) * -1 as itotal, "
                        + " nro_pedido, cod_depo, fpedido, ttotal, cod_vendedor, xvendedor  , nplazo_cheque, CTIPO_VTA, fventa,   "
                        + " iprecio_caja, iprecio_unid as iprecio_unidad "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE CCONC = 'DEV' "
                        + " AND not_CTIPO_DOCUM = 'NCV' "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, cod_sublinea, xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, cconc, fdocum as ffactur,  "
                        + " cod_merca, xdesc_merca, 0 as cant_cajas, 0 as cant_unid , "
                        + " timpuestos, tgravadas, texentas, total_fac, (tgravadas_10+texentas+tgravadas_5+ timpuestos) * -1 as itotal, "
                        + " nro_pedido, cod_depo, fpedido, ttotal, cod_vendedor, xvendedor   , nplazo_cheque, CTIPO_VTA, fventa,  "
                        + " iprecio_caja, iprecio_unid as iprecio_unidad "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE CCONC != 'DEV' "
                        + " AND not_CTIPO_DOCUM = 'NCV' "
                        + " \nUNION ALL\n "
                        + " SELECT  cod_zona,  xdesc_zona, cod_cliente, xnombre, cod_sublinea, xdesc_sublinea, "
                        + " xdesc_ruta, ctipo_docum,  nrofact, cconc, fdocum as ffactur,  cod_merca, "
                        + " xdesc_merca, 0 as cant_cajas, 0 as cant_unid, "
                        + " timpuestos, tgravadas, texentas, total_fac, (tgravadas_10+texentas+tgravadas_5+ timpuestos)  as itotal, "
                        + " nro_pedido, cod_depo, fpedido, ttotal, cod_vendedor, xvendedor   , nplazo_cheque, CTIPO_VTA, fventa,  "
                        + " iprecio_caja, iprecio_unid as iprecio_unidad "
                        + " FROM ( "+sql2+" ) c "
                        + " WHERE not_CTIPO_DOCUM = 'NDV' ) t ORDER BY cod_cliente, cod_sublinea, cod_merca ";
                }
                
                //sql3 += " SELECT  * "
		//+ " INTO mostrar1 "
                //+ " FROM mostrarh "		
		//+ " ORDER BY cod_cliente, cod_sublinea, cod_merca ";
                
            }
            
        }
        
        if (!this.seleccion.equals("4")){
            //sql3 +=" SELECT mostrar1.*, 0 as iret_caja,0 as iret_unidad, "
            //    + " 0 AS idev_caja,0 as idev_unidad "
            //    + " INTO mostrar1 FROM mostrar ";
            //sql3 += " \nIF Object_id('mostrar') IS NOT NULL DROP TABLE mostrar\n " ;
            sql3 +=" SELECT * "
                + " INTO mostrar  "
                + " FROM (SELECT mostrar1.*, 0 as iret_caja,0 as iret_unidad, 0 AS idev_caja,0 as idev_unidad FROM mostrar1 ) t "
                + " \n update m "
                + " set m.iret_caja = r.iretorno_caja, "
                + "         m.iret_unidad = r.iretorno_unidad, "
                + "         m.idev_caja = r.idevol_caja, "
                + "         m.idev_unidad = r.idevol_unidad "
                + " from mostrar m inner join retornos_precios r "
                + " on m.cod_merca = r.cod_merca "
                + " and m.ctipo_vta = r.ctipo_vta "
                + " and m.fventa >= r.frige_desde "
                + " and m.fventa <= r.frige_hasta "; 
            
        }
        
        System.out.println("query3: " + sql3);
        //System.out.println("query4: " + sql4);
        //System.out.println("query5: " + sql5);
        //q = em.createNativeQuery(sql3);
        //q.getResultList();
        //q.executeUpdate();
        //em.clear();
        sqlFacade.executeQuery(sql3);
    }
    
    public void marcarTodos() {
        SelectorDatosBean.datosFiltrados = false;
        this.checkCliente = true;
        this.seleccionarClientes = false;
    }
    
    public void llamarSelectorDatos() {
        //cambiar el selector de todos               
        if (this.seleccionarClientes) {
            this.checkCliente = false;
            //RequestContext.getCurrentInstance().update("ocultarBtnPag");
            System.out.println("Mostrar selector de cliente");
            /*SelectorDatosBean.sql = " select cod_cliente, xnombre \n"
                    + "from clientes\n"
                    + "where cod_estado in ('A', 'S') \n"
                    + "and cod_cliente in (select distinct(cod_cliente)\n"
                    + "from pagares) ";*/
            SelectorDatosBean.sql = " select cod_cliente, xnombre \n"
                    + "from clientes\n"
                    + "where cod_estado in ('A', 'S')  ";
            SelectorDatosBean.tabla_temporal = "tmp_datos";
            SelectorDatosBean.campos_tabla_temporal = "codigo, descripcion";

            RequestContext.getCurrentInstance().execute("PF('dlgSelDatos').show();");
            
        } else {
            this.checkCliente = true;
            //RequestContext.getCurrentInstance().update("ocultarBtnPag");
            System.out.println("Ocultar selector de cliente");
        }
        RequestContext.getCurrentInstance().update("mostrarBtnPag");
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

    public Empleados getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
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

    public TiposClientes getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TiposClientes tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public TiposVentas getTipoVentas() {
        return tipoVentas;
    }

    public void setTipoVentas(TiposVentas tipoVentas) {
        this.tipoVentas = tipoVentas;
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

    public String getSeleccion2() {
        return seleccion2;
    }

    public void setSeleccion2(String seleccion2) {
        this.seleccion2 = seleccion2;
    }

    public List<Empleados> getListaVendedor() {
        return listaVendedor;
    }

    public void setListaVendedor(List<Empleados> listaVendedor) {
        this.listaVendedor = listaVendedor;
    }

    public List<Sublineas> getListaSubLinea() {
        return listaSubLinea;
    }

    public void setListaSubLinea(List<Sublineas> listaSubLinea) {
        this.listaSubLinea = listaSubLinea;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public List<Lineas> getListaLineas() {
        return listaLineas;
    }

    public void setListaLineas(List<Lineas> listaLineas) {
        this.listaLineas = listaLineas;
    }

    public List<TiposVentas> getListaTiposVentas() {
        return listaTiposVentas;
    }

    public void setListaTiposVentas(List<TiposVentas> listaTiposVentas) {
        this.listaTiposVentas = listaTiposVentas;
    }

    public List<TiposClientes> getListaTiposClientes() {
        return listaTiposClientes;
    }

    public void setListaTiposClientes(List<TiposClientes> listaTiposClientes) {
        this.listaTiposClientes = listaTiposClientes;
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

    public Boolean getCheckCliente() {
        return checkCliente;
    }

    public void setCheckCliente(Boolean checkCliente) {
        this.checkCliente = checkCliente;
    }

    public Boolean getSeleccionarClientes() {
        return seleccionarClientes;
    }

    public void setSeleccionarClientes(Boolean seleccionarClientes) {
        this.seleccionarClientes = seleccionarClientes;
    }

    
    
    
}
