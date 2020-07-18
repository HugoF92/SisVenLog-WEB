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
    private Boolean conIVA = false;
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
        this.estado = new String("1");
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
            String extras2 = "";
            String sqlReport = null;
            String[] sqls = null;
            String[] columnas = null;
            String titulo = null;
            String reporte = null;

            if (this.entregador != null){
                extras += " AND n.cod_entregador = '"+this.entregador.getEmpleadosPK().getCodEmpleado()+"' ";
            }
            if (this.vendedor != null){
                extras += " AND f.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
            }
            if (this.concepto != null){
                extras += " AND n.cconc = '" + this.concepto.getConceptosDocumentosPK().getCconc() +"' ";
            }
            if (this.canal != null){
                extras += " AND f.cod_canal = '" + this.canal.getCodCanal()  +"' ";
            }
            if (this.deposito != null){
                extras += " AND f.cod_depo = " + this.deposito.getDepositosPK().getCodDepo()  +" ";
            }
            if (mercaderias.getTarget().size() > 0) {
                listaMercaderiasSeleccionadas = mercaderias.getTarget();
                extras2 += " AND nd.cod_merca IN (";
                for (int i = 0; i < listaMercaderiasSeleccionadas.size(); i++) {
                    MercaderiasPK pk = listaMercaderiasSeleccionadas.get(i).getMercaderiasPK();
                    extras2 += " '" + pk.getCodMerca() + "',";
                }
                extras2 = extras2.substring(0, extras2.length()-1) + " ) ";
            }
            if (this.proveedor != null){
                extras2 += " AND m.cod_proveed = " + this.proveedor.getCodProveed()  +" ";
            }
            
            if ( this.resumido || this.seleccion.equals("5") || this.seleccion.equals("6") 
                    || this.seleccion.equals("7") || this.seleccion.equals("11")  ) {                
                sqls = preEjecutarSQL2(fdesde, fhasta, extras, extras2);
            } else {
                sqls = preEjecutarSQL(fdesde, fhasta, extras, extras2);
            }
            
            if ("NDV".equals(this.tipoFactura.getCtipoDocum())){
                titulo = " NOTAS DE DEBITO ";
            } else {
                titulo = " NOTAS DE CREDITO ";
            }
            columnas = new String[14];
            columnas[0] = "cod_vendedor";
            
            switch ( this.seleccion ) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "8":
                case "9":
                case "10":
                    if (!this.resumido) { 
                        columnas = new String[21];
                        columnas[0] = "nro_nota";
                        columnas[1] = "fdocum";
                        columnas[2] = "fac_ctipo_docum";
                        columnas[3] = "cconc";
                        columnas[4] = "texentas";
                        columnas[5] = "tgravadas";
                        columnas[6] = "timpuestos";
                        columnas[7] = "nrofact";
                        columnas[8] = "ctipodocum";
                        columnas[9] = "cod_cliente";
                        columnas[10] = "xnombre";
                        columnas[11] = "ttotal";
                        columnas[12] = "cod_vendedor";
                        columnas[13] = "xnombre_vendedor";
                        columnas[14] = "xdesc_conc";
                        columnas[15] = "fac_ttotal";
                        columnas[16] = "ffactur";
                        columnas[17] = "cod_entregador";
                        columnas[18] = "cod_zona";
                        columnas[19] = "xnombre_entregador";
                        columnas[20] = "mestado";
                    } else{
                        columnas = new String[32];
                        columnas[0] = "nro_nota";
                        columnas[1] = "fdocum";
                        columnas[2] = "fac_ctipo_docum";
                        columnas[3] = "cconc";
                        columnas[4] = "texentas";
                        columnas[5] = "tgravadas";
                        columnas[6] = "timpuestos";
                        columnas[7] = "nrofact";
                        columnas[8] = "ctipo_docum";
                        columnas[9] = "cod_cliente";
                        columnas[10] = "xnombre";
                        columnas[11] = "ttotal";
                        columnas[12] = "cod_vendedor";
                        columnas[13] = "xnombre_vendedor";
                        columnas[14] = "xdesc_conc";
                        columnas[15] = "fac_ttotal";
                        columnas[16] = "ffactur";
                        columnas[17] = "cod_entregador";
                        columnas[18] = "xnombre_entregador";
                        columnas[19] = "cod_merca";
                        columnas[20] = "xdesc";
                        columnas[21] = "cant_cajas";
                        columnas[22] = "cant_unid";
                        columnas[23] = "cod_sublinea";
                        columnas[24] = "xdesc_sublinea";
                        columnas[25] = "igravadas";
                        columnas[26] = "iexentas";
                        columnas[27] = "impuestos";
                        columnas[28] = "xdesc_division";
                        columnas[29] = "cod_division";
                        columnas[30] = "mestado";
                        columnas[31] = "cod_zona";
                    }
                    break;
            }
            
            switch ( this.seleccion ) {
                case "1":
                    if (!this.resumido) {                        
                        reporte = "rnotasvtas";                        
                    } else {                        
                        reporte = "rnotasvtasdet";
                    }                    
                    sqlReport = sqls[0] + sqls[2];
                    break;
                case "2":
                    if (!this.resumido) {
                        reporte = "rnotasvtas2";
                    } else {
                        reporte = "rnotasvtas2det";
                    }
                    sqlReport = sqls[0] + sqls[2];
                    break;
                case "3":
                    if (!this.resumido) {
                        reporte = "rnotasvtas3";
                    } else {
                        reporte = "rnotasvtas3det";
                    }
                    sqlReport = sqls[0];
                    break;
                case "4":
                    if (!this.resumido) {
                        reporte = "rnotasvtas4";
                    } else {
                        reporte = "rnotasvtas4det";
                    }
                    sqlReport = sqls[0];
                    break;
                case "5":
                    columnas = new String[4];
                    columnas[0] = "cod_merca";
                    columnas[1] = "xdesc";
                    columnas[2] = "kcajas";
                    columnas[3] = "kunidad";                    
                    reporte = "rnotasvtasr";
                    sqlReport = sqls[1];
                    break;
                case "6":
                    columnas = new String[7];
                    columnas[0] = "cod_sublinea";
                    columnas[1] = "xdesc_sublinea";
                    columnas[2] = "kcajas";
                    columnas[3] = "kunidad";
                    columnas[4] = "igrabadas";
                    columnas[5] = "iexentas";
                    columnas[6] = "impuestos";
                    reporte = "rnotasvtass";
                    sqlReport = sqls[1];
                    break;
                case "7":
                    columnas = new String[7];
                    columnas[0] = "cod_division";
                    columnas[1] = "xdesc_division";
                    columnas[2] = "kcajas";
                    columnas[3] = "kunidad";
                    columnas[4] = "igrabadas";
                    columnas[5] = "iexentas";
                    columnas[6] = "impuestos";
                    reporte = "rnotasvtasd";
                    sqlReport = sqls[1];
                    break;
                case "8":
                    if (!this.resumido) {
                        reporte = "rnotasvtasc";
                    } else {
                        reporte = "rnotasvtascd";
                    }
                    sqlReport = sqls[0];
                    break;
                case "9":
                    reporte = "rnotasvtasz";
                    sqlReport = sqls[0];
                    break;
                case "10":
                    reporte = "rnotasvtasn";
                    sqlReport = sqls[0];
                    break;
                case "11":
                    columnas = new String[5];
                    columnas[0] = "cod_zona";
                    columnas[1] = "cod_merca";
                    columnas[2] = "xdesc";
                    columnas[3] = "kcajas";
                    columnas[4] = "kunidad";
                    reporte = "rnotasvtaszn";
                    sqlReport = sqls[1];
                    break;
            }
            
            if (this.conIVA) {
                columnas = new String[25];
                columnas[0] = "nro_nota";
                columnas[1] = "fdocum";
                columnas[2] = "fac_ctipo_docum";
                columnas[3] = "cconc";
                columnas[4] = "texentas";
                columnas[5] = "tgravadas";
                columnas[6] = "timpuestos";
                columnas[7] = "nrofact";
                columnas[8] = "ctipodocum";
                columnas[9] = "cod_cliente";
                columnas[10] = "xnombre";
                columnas[11] = "ttotal";
                columnas[12] = "cod_vendedor";
                columnas[13] = "xnombre_vendedor";
                columnas[14] = "xdesc_conc";
                columnas[15] = "fac_ttotal";
                columnas[16] = "ffactur";
                columnas[17] = "cod_entregador";
                columnas[18] = "cod_zona";
                columnas[19] = "xnombre_entregador";
                columnas[20] = "mestado";
                columnas[21] = "tgravadas_10";
                columnas[22] = "tgravadas_5";
                columnas[23] = "timpuestos_10";
                columnas[24] = "timpuestos_5";
                reporte = "rnotasvtas6";
                sqlReport = "SELECT m.*, i.tgravadas_10, i.tgravadas_5, i.timpuestos_10, "
                    + " i.timpuestos_5 "
                    + " FROM (" + sqls[0]  + " ) m, (" + sqls[1] + ") i "
                    + " WHERE m.nro_nota = i.nro_nota ";
            }            
            for (int i=0; i<sqls.length; i++ ) {
                System.out.println(sqls[i]);
            }
            System.out.println("reporte "+reporte);
            System.out.println("sqlReport "+sqlReport);            
            if (tipo.equals("VIST")){
                String usuImprime = "";//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                Map param = new HashMap();
                param.put("sql", sqlReport);
                param.put("fdesde", this.desde);
                param.put("fhasta", this.hasta);
                param.put("titulo", titulo);
                param.put("usuImprime", usuImprime);
                param.put("nombreRepo", reporte); 

                if (this.vendedor != null) param.put("empleado", empleadoFacade.getEmpeladoFromList(this.vendedor, this.listaVendedor).getXnombre());
                else param.put("empleado", "TODOS");
                
                if (this.canal != null) param.put("canal", canalFacade.getCanalVentaFromList(this.canal, this.listaCanales).getXdesc()); 
                else param.put("canal", "TODOS");
                
                if (this.proveedor != null) param.put("proveedor", proveedoresFacade.getProveedorFromList(this.proveedor, this.listaProveedores).getXnombre()); 
                else param.put("proveedor", "TODOS");
                
                if (this.entregador != null) param.put("entregador", empleadoFacade.getEmpeladoFromList(this.entregador, this.listaEntregador).getXnombre());
                else param.put("entregador", "TODOS");
                
                if (this.concepto != null) param.put("concepto", conceptoDocumFacade.getConceptoFromList(this.concepto, this.listaConceptosDocumentos).getXdesc());
                else param.put("concepto", "TODOS");
                
                if (this.deposito != null) param.put("deposito", depositoFacade.getDepositoFromList(this.deposito, this.listaDepositos).getXdesc());
                else param.put("deposito", "TODOS");
                
                rep.reporteLiContClientes(param, tipo, reporte);                
            } else {
                if (this.conIVA) {
                    sqlReport = "SELECT m.*,  "
                        + " i.tgravadas_10 + i.timpuestos_10 tgrav_10, "
                        + " i.tgravadas_5 + i.timpuestos_5 tgrav_5, "
                        + " i.timpuestos_10 * -1 timpu_10, i.timpuestos_5 * -1 timpu_5"
                        + " FROM ( " + sqls[0]  + " ) m, ( " + sqls[1] + " ) i "
                        + " WHERE m.nro_nota = i.nro_nota ";
                } 
                
                List<Object[]> lista = new ArrayList<Object[]>();
                lista = excelFacade.listarParaExcel(sqlReport);
                rep.exportarExcel(columnas, lista, reporte);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al ejecutar listado"));
        }
    }
    
    public String[] preEjecutarSQL(String fdesde, String fhasta, String extras, String extras2){
        String sql = "";
        String aux_sql = "";
        String sql2 = "";
        String sql3 = "";
        String sql_nro_notas = "";
        String sql_order = "";
        
        sql_nro_notas = " SELECT DISTINCT n.nro_nota  FROM notas_ventas n, "
                + " notas_ventas_det nd, mercaderias m " +
                "  WHERE n.cod_empr = 2 " +
                " AND n.fdocum between '"+fdesde+"' AND '"+fhasta+"'" +
                " AND n.ctipo_docum = '"+this.tipoFactura.getCtipoDocum().toUpperCase() +"' " +
		" AND n.nro_nota = nd.nro_nota "+
		" AND n.fdocum = nd.fdocum "+
		" and nd.cod_merca = m.cod_merca ";
        
        if ("NCV".equals(this.tipoFactura.getCtipoDocum())){
            sql_nro_notas += " AND n.ctipo_docum = 'NCV' AND nd.ctipo_docum = 'NCV' ";
            aux_sql += " AND n.ctipo_docum = 'NCV'  ";
        }
        
        if ("NDV".equals(this.tipoFactura.getCtipoDocum())){
            sql_nro_notas += " AND n.ctipo_docum = 'NDV' AND nd.ctipo_docum = 'NDV' ";
            aux_sql += " AND n.ctipo_docum = 'NDV'  ";
        }
                
        if (this.estado.equals("1")){
            sql_nro_notas += " AND n.mestado = 'A' ";
            aux_sql += " AND n.mestado = 'A' ";
            sql2 += " AND t.mestado = 'A' ";
        }else if (this.estado.equals("2")){
            sql_nro_notas += " AND n.mestado = 'X' ";
            aux_sql += " AND n.mestado = 'X' ";
            sql2 += " AND t.mestado = 'X' ";
        }
        
        sql_nro_notas += extras2;
        
        sql = " SELECT n.nro_nota, n.fdocum, n.fac_ctipo_docum, n.cconc, n.texentas, n.tgravadas, " 
            + " n.timpuestos, f.nrofact, f.ctipo_docum, c.cod_cliente, c.xnombre, n.ttotal,  "
            + " f.cod_vendedor, e.xnombre AS xnombre_vendedor, d.xdesc AS xdesc_conc, f.ttotal AS fac_ttotal, "
            + " f.ffactur, n.cod_entregador, f.cod_zona, e2.xnombre AS xnombre_entregador, n.mestado " 
            + " FROM notas_ventas n INNER JOIN " 
            + " facturas f ON n.fac_ctipo_docum = f.ctipo_docum AND n.nrofact = f.nrofact AND n.ffactur = f.ffactur "
            + " AND n.cod_empr = f.cod_empr LEFT OUTER JOIN " 
            + " empleados e2 ON f.cod_empr = e2.cod_empr AND n.cod_entregador = e2.cod_empleado LEFT OUTER JOIN " 
            + " empleados e ON f.cod_vendedor = e.cod_empleado AND f.cod_empr = e.cod_empr INNER JOIN " 
            + " clientes c ON n.cod_cliente = c.cod_cliente AND n.cod_cliente = c.cod_cliente INNER JOIN " 
            + "    conceptos_documentos d ON n.cconc = d.cconc AND n.ctipo_docum = d.ctipo_docum AND n.cconc = d.cconc, (" +sql_nro_notas+ ") t "
            + " WHERE n.cod_empr = 2 AND f.cod_empr = 2 "
            + " AND (n.fdocum BETWEEN '"+fdesde+"' AND '"+fhasta+"') "
            + " AND (n.ctipo_docum = '" + this.tipoFactura.getCtipoDocum().toUpperCase() + "') "
            + " AND n.nro_nota = t.nro_nota  ";
        
        sql += aux_sql + " " + extras;
        
        //if (!this.conIVA)
        switch ( this.seleccion ) {
            case "1":
                sql_order += " ORDER BY n.cconc, n.fdocum, n.nro_nota ";
                break;
            case "2":
                sql_order += " ORDER BY f.cod_vendedor, n.cconc, n.nro_nota, n.fdocum ";
                break;
            case "3":
                sql_order += " ORDER BY n.cod_entregador, n.nro_nota, n.fdocum ";
                break;
            case "4":
                sql_order += " ORDER BY n.fdocum, n.cconc, n.nro_nota ";
                break;            
            case "8":
                sql_order += " ORDER BY c.cod_cliente, n.fdocum, n.cconc, n.nro_nota  ";
                break;
            case "9":
                sql_order += " ORDER BY f.cod_zona, n.cconc, n.nro_nota, n.fdocum ";
                break;
            case "10":
                sql_order += " ORDER BY n.nro_nota ";
                break;
            case "11":
                sql_order += " ORDER BY f.cod_zona ";
                break;
        }                
        
        if (this.conIVA) {
            sql3 = getSQLIVA(fdesde, fhasta);
        } 
        String [] a = {sql,sql3,sql_order};
        return a;
    }
    
    public String[] preEjecutarSQL2(String fdesde, String fhasta, String extras, String extras2){
        String sql = "";
        String sql2 = "";
        String sql_order = "";
        
        sql = " SELECT n.nro_nota, n.fdocum, n.fac_ctipo_docum, n.cconc, n.texentas, "
            + " n.tgravadas, n.timpuestos, f.nrofact, f.ctipo_docum, c.cod_cliente, c.xnombre, n.ttotal,  " 
            + " f.cod_vendedor, e.xnombre AS xnombre_vendedor, d.xdesc AS xdesc_conc, f.ttotal AS fac_ttotal, "
            + " f.ffactur, n.cod_entregador,  "
            + " e2.xnombre AS xnombre_entregador,nd.cod_merca, nd.xdesc, nd.cant_cajas, nd.cant_unid, "
            + " m.cod_sublinea, s.xdesc as xdesc_sublinea, nd.igravadas, nd.iexentas, nd.impuestos, " 
            + " v.xdesc as xdesc_division, V.COD_DIVISION, n.mestado, f.cod_zona " 
            + " FROM notas_ventas n INNER JOIN " 
            + " notas_ventas_det nd ON n.nro_nota = nd.nro_nota INNER JOIN " 
            + " mercaderias m ON nd.cod_merca = m.cod_merca INNER JOIN "
            + " sublineas s ON m.cod_sublinea = s.cod_sublinea INNER JOIN "
            + " lineas l ON  s.cod_linea = l.cod_linea INNER JOIN "
            + " categorias g ON  l.cod_categoria = g.cod_categoria INNER JOIN " 
            + " divisiones v ON  g.cod_division  = v.cod_division  INNER JOIN "
            + " facturas f ON n.fac_ctipo_docum = f.ctipo_docum AND n.nrofact = f.nrofact "
            + " AND n.cod_empr = f.cod_empr AND n.ffactur = f.ffactur  LEFT OUTER JOIN "
            + " empleados e2 ON f.cod_empr = e2.cod_empr AND n.cod_entregador = e2.cod_empleado LEFT OUTER JOIN "
            + " empleados e ON f.cod_vendedor = e.cod_empleado AND f.Cod_empr = e.cod_empr INNER JOIN " 
            + " clientes c ON n.cod_cliente = c.cod_cliente AND n.cod_cliente = c.cod_cliente INNER JOIN " 
            + " conceptos_documentos d ON n.cconc = d.cconc AND n.ctipo_docum = d.ctipo_docum AND n.cconc = d.cconc "
            + " WHERE n.cod_empr = 2 AND f.cod_empr = 2 "
            + " AND n.fdocum = nd.fdocum "
            + "	AND  (n.fdocum BETWEEN '"+fdesde+"' AND '"+fhasta+"') "
            + " AND (n.ctipo_docum = '"+this.tipoFactura.getCtipoDocum().toUpperCase()+"') "
            + extras + " " + extras2;
        
        if ("NCV".equals(this.tipoFactura.getCtipoDocum())){
            sql += " AND n.ctipo_docuM = 'NCV' And d.ctipo_docum = 'NCV'  ";
        }        
        if ("NDV".equals(this.tipoFactura.getCtipoDocum())){
            sql += " AND n.ctipo_docuM = 'NCV' And d.ctipo_docum = 'NDV' ";
        }
        
        if (this.estado.equals("1")){
            sql += " AND n.mestado = 'A' ";
        }else if (this.estado.equals("2")){
            sql += " AND n.mestado = 'X' ";
        }
        
        switch ( this.seleccion ) {
            case "1":
                sql_order += " ORDER BY n.cconc, n.nro_nota, n.fdocum ";
                break;
            case "2":
                sql_order += " ORDER BY f.cod_vendedor, n.cconc, n.nro_nota, n.fdocum  ";
                break;
            case "3":
                sql_order += " ORDER BY n.cod_entregador, n.cconc, n.nro_nota, n.fdocum ";
                break;
            case "4":
                sql_order += " ORDER BY n.fdocum, n.cconc, n.nro_nota ";
                break;            
            case "8":
                sql_order += " ORDER BY C.COD_CLIENTE, n.nro_nota  ";
                break;
        }
        
        switch ( this.seleccion ) {
            case "5":
                sql2 += "SELECT cod_merca, xdesc, sum(cant_cajas) AS KCAJAS, "
                    + " sum(cant_unid) AS KUNID "
                    + " FROM ("+sql+") a" 
                    + " GROUP BY cod_merca, xdesc";
                break;
            case "6":
                sql2 += " SELECT cod_sublinea, xdesc_sublinea, sum(cant_cajas) AS KCAJAS, "
                    + " sum(cant_unid) AS KUNID, SUM(igravadas) as igravadas, "
                    + " sum(iexentas) as iexentas, sum(impuestos) as impuestos " 
                    + " FROM ("+sql+") a" 
                    + "	GROUP BY cod_sublinea, xdesc_sublinea  ";
                break;
            case "7":
                sql2 += " SELECT cod_division, xdesc_division, sum(cant_cajas) AS KCAJAS, "
                    + " sum(cant_unid) AS KUNID, SUM(igravadas) as igravadas, "
                    + " sum(iexentas) as iexentas, sum(impuestos) as impuestos "
                    + " FROM ("+sql+") a" 
                    + " GROUP BY cod_division, xdesc_division";
                break;
            case "11":
                sql2 += " SELECT cod_zona, cod_merca, xdesc, sum(cant_cajas) AS KCAJAS, "
                    + " sum(cant_unid) AS KUNID "
                    + " FROM ("+sql+") a" 
                    + "	GROUP BY cod_zona, cod_merca, xdesc " 
                    + "	ORDER BY cod_zona, cod_merca";
                break;
        }
        
        if (this.conIVA) {
            sql2 = getSQLIVA(fdesde, fhasta);
        } 
        
        String [] a = {sql, sql2, sql_order};
        return a;
    }

    private String getSQLIVA(String fdesde, String fhasta){
        String sql2 = "";
        String sql3 = "";
        sql2 = " SELECT   t.fdocum, t.cconc, t.ctipo_docum, t.nro_nota,  0 AS texentas, "
                + " isnull(SUM(d.igravadas), 0)  AS tgravadas_5, 0 AS tgravadas_10, isnull(SUM(impuestos), 0) AS timpuestos_5, "
                + " 0 AS timpuestos_10 " 
                + " FROM notas_ventas_det d INNER JOIN notas_ventas t " 
                + "     ON d.ctipo_docum = t.ctipo_docum AND d.nro_nota = t.nro_nota  " 
                + " WHERE d.pimpues = 5 and  (d.cod_empr = 2) "
                + "   AND t.fdocum = d.fdocum "
                + " AND t.fdocum BETWEEN '"+fdesde+"' AND '"+fhasta+"'";

            if ("NCV".equals(this.tipoFactura.getCtipoDocum())){
                sql2 += " AND t.ctipo_docum = 'NCV' AND d.ctipo_docum = 'NCV' ";
            }        
            if ("NDV".equals(this.tipoFactura.getCtipoDocum())){
                sql2 += " AND t.ctipo_docum = 'NDV' AND d.ctipo_docum = 'NDV' ";
            }

            sql2 += " GROUP BY t.fdocum, t.cconc, t.ctipo_docum, t.nro_nota " 
                + " UNION ALL "
                + " SELECT t.fdocum, t.cconc, t.ctipo_docum, t.nro_nota, 0 AS texentas, "
                + " 0 AS tgravadas_5, isnull(SUM(d .igravadas), 0) AS tgravadas_10, 0 AS timpuestos_5, "
                + " isnull(SUM(impuestos), 0)  AS timpuestos_10 "
                + " FROM notas_ventas_det d INNER JOIN notas_ventas t " 
                + " ON d.ctipo_docum = t.ctipo_docum AND d.nro_nota = t.nro_nota AND d.fdocum = t.fdocum  " 
                + " WHERE d.pimpues = 10 and (d .cod_empr = 2) "
                + "  AND t.fdocum BETWEEN '"+fdesde+"' AND '"+fhasta+"'";

            if ("NCV".equals(this.tipoFactura.getCtipoDocum())){
                sql2 += " AND t.ctipo_docum = 'NCV' AND d.ctipo_docum = 'NCV' ";
            }        
            if ("NDV".equals(this.tipoFactura.getCtipoDocum())){
                sql2 += " AND t.ctipo_docum = 'NDV' AND d.ctipo_docum = 'NDV' ";
            }

            sql2 += " GROUP BY t.fdocum, t.cconc, t.ctipo_docum, t.nro_nota " 
                + " UNION ALL " 
                + " SELECT t.fdocum, t.cconc, t.ctipo_docum , t.nro_nota, isnull(SUM(iexentas), 0) AS texentas, "
                + " 0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, 0 AS timpuestos_10 " 
                + " FROM notas_ventas_det d INNER JOIN notas_ventas t ON d.nro_nota = t.nro_nota AND "
                + " d.ctipo_docum = t.ctipo_docum AND t.fdocum = d.fdocum AND d .iexentas > 0 "
                + " WHERE (d .cod_empr = 2) "
                + "  AND t.fdocum BETWEEN '"+fdesde+"' AND '"+fhasta+"'";

            if ("NCV".equals(this.tipoFactura.getCtipoDocum())){
                sql2 += " AND t.ctipo_docum = 'NCV' ";
            }        
            if ("NDV".equals(this.tipoFactura.getCtipoDocum())){
                sql2 += " AND t.ctipo_docum = 'NDV' ";
            }

            sql2 += " GROUP BY t.fdocum, t.cconc, t.ctipo_docum, t.nro_nota ";

            sql3 += " SELECT ctipo_docum, nro_nota, SUM(texentas) as texentas, "
                + " sum(tgravadas_5) as tgravadas_5, SUM(tgravadas_10) as tgravadas_10, " 
                + "	SUM(timpuestos_5) as timpuestos_5, sum(timpuestos_10) as timpuestos_10 " 
                + "	FROM ( "+sql2+" ) a GROUP BY ctipo_docum, nro_nota ";
        
        return sql3;
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

    public Boolean getConIVA() {
        return conIVA;
    }

    public void setConIVA(Boolean conIVA) {
        this.conIVA = conIVA;
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
