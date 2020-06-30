package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.ZonasFacade;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.LlamarReportes;

/**
 *
 * @author Nico
 */
@ManagedBean
@SessionScoped
public class LiRecaudacionBean implements Serializable{
    
    private Date desde,hasta;
    private Empleados entregador;
    private Zonas zonas,zonasCamion;
    
    @EJB
    private ZonasFacade zonasFacade;
    @EJB
    private EmpleadosFacade vendedoresFacade;
    @EJB
    private ExcelFacade excelFacade;
        
    private String seleccion,nuevos,salidas;
    private Boolean nuevo;
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.seleccion = new String();
        this.entregador = new Empleados(new EmpleadosPK());
        this.zonas = new Zonas(new ZonasPK());
        this.nuevo =  false;
        this.desde = new Date();
        this.hasta = new Date();
        this.salidas = new String();
        this.seleccion = "1";
        this.salidas = "1";
    }
    
    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();
        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String lNombreReport = "";
        Integer codEntregador = this.entregador != null ? (int)this.entregador.getEmpleadosPK().getCodEmpleado() : null;
        Zonas zona = this.zonas != null? this.zonas : (this.zonasCamion != null? this.zonasCamion: null);
        String codZona = zona != null ? zona.getZonasPK().getCodZona() : null;
        String[] columnas = null;
        List<Object[]> lista = new ArrayList<Object[]>();
        switch (this.seleccion){
            case "1":
                lNombreReport = "RRECAUDACION";
                if(tipo.equals("ARCH")){
                    columnas = new String[22];
                    columnas[0] = "fplanilla";
                    columnas[1] = "cod_zona";
                    columnas[2] = "cod_entregador";
                    columnas[3] = "xnombre";
                    columnas[4] = "nro_planilla";
                    columnas[5] = "tventas";
                    columnas[6] = "tnotas_dev";
                    columnas[7] = "tcheques_dia";
                    columnas[8] = "kcheques";
                    columnas[9] = "tefectivo";
                    columnas[10] = "tdepositos";
                    columnas[11] = "tmonedas";
                    columnas[12] = "tgastos";
                    columnas[13] = "tcreditos";
                    columnas[14] = "tcheques_dif";
                    columnas[15] = "tpagares";
                    columnas[16] = "tnotas_otras";
                    columnas[17] = "tnotas_atras";
                    columnas[18] = "tdiferencia";
                    columnas[19] = "cconc";
                    columnas[20] = "xdesc_conc";
                    columnas[21] = "ttotal";
                    String sql = " SELECT r.fplanilla, r.cod_zona, r.cod_entregador,e.xnombre, r.nro_planilla, r.tventas, r.tnotas_dev,"
                            +" r.tcheques_dia, r.kcheques, r.tefectivo, r.tdepositos, r.tmonedas, r.tgastos, r.tcreditos, r.tcheques_dif, r.tpagares,"
                            +" r.tnotas_otras, r.tnotas_atras,  r.tdiferencia, '' as cconc, '' as xdesc_conc, 0 as ttotal "
                            +" FROM recaudacion r, empleados e "
                            +" where r.fplanilla BETWEEN convert(smalldatetime,'"+fdesde+"',121) AND convert(smalldatetime,'"+fhasta+"',121) "
                            +" and e.cod_empr = 2 "
                            +" and  r.cod_empr = 2 "
                            +" and e.cod_empleado = r.cod_entregador "
                            +" and r.cod_entregador = ISNULL("+codEntregador+",r.cod_entregador) "
                            +" UNION ALL "
                            +" SELECT n.fdocum as fplanilla, r.cod_zona, f.cod_entregador, e.xnombre, r.nro_planilla, r.tventas, r.tnotas_dev, "
                            +" r.tcheques_dia, r.kcheques, r.tefectivo, r.tdepositos, r.tmonedas, r.tgastos, r.tcreditos, r.tcheques_dif, r.tpagares, "
                            +" r.tnotas_otras, r.tnotas_atras, r.tdiferencia,N.CCONC, c.xdesc as xdesc_conc, SUM(n.ttotal) as ttotal "
                            +" FROM notas_ventas n, empleados e, facturas f, recaudacion r, conceptos_documentos c "
                            +" WHERE f.cod_empr = 2 AND n.cod_empr = 2 and e.cod_empr = 2 "
                            +" AND (n.fdocum BETWEEN convert(smalldatetime,'"+fdesde+"',121) AND convert(smalldatetime,'"+fhasta+"',121))  AND (n.cconc != 'DEV') "
                            +" AND n.ctipo_docum = 'NCV' "
                            +" AND e.cod_empleado = n.cod_entregador "
                            +" AND n.cod_entregador = r.cod_entregador "
                            +" AND n.cconc = c.cconc "
                            +" AND n.ctipo_docum = c.ctipo_docum "
                            +" AND r.fplanilla = n.fdocum "
                            +" AND n.nrofact = f.nrofact "
                            +" AND n.ffactur = f.ffactur "
                            +" AND n.fac_ctipo_docum = f.ctipo_docum " 
                            +" AND n.mestado  = 'A' "
                            +" and n.cod_entregador = ISNULL("+codEntregador+",n.cod_entregador) "
                            +" GROUP BY fdocum, r.cod_zona, f.cod_entregador, e.xnombre, r.nro_planilla, r.tventas, r.tnotas_dev, "
                            +" r.tcheques_dia, r.kcheques, r.tefectivo, r.tdepositos, r.tmonedas, r.tgastos, r.tcreditos, r.tcheques_dif, r.tpagares, "
                            +" r.tnotas_otras, r.tnotas_atras,r.tdiferencia, n.cconc, c.xdesc "
                            +" ORDER BY 3,1,2 ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }
                break;
            case "2":
                lNombreReport = "RDETCRED";
                if(tipo.equals("ARCH")){
                    columnas = new String[46];
                    columnas[0] = "cod_empr";
                    columnas[1] = "nrofact";
                    columnas[2] = "ctipo_docum";
                    columnas[3] = "ffactur";
                    columnas[4] = "cod_zona";
                    columnas[5] = "cod_ruta";
                    columnas[6] = "cod_depo";
                    columnas[7] = "ctipo_vta";
                    columnas[8] = "cod_vendedor";
                    columnas[9] = "cod_canal";
                    columnas[10] = "cod_entregador";
                    columnas[11] = "nro_pedido";
                    columnas[12] = "cod_cliente";
                    columnas[13] = "mestado";
                    columnas[14] = "fvenc";
                    columnas[15] = "texentas";
                    columnas[16] = "tgravadas";
                    columnas[17] = "timpuestos";
                    columnas[18] = "tdescuentos";
                    columnas[19] = "ttotal";
                    columnas[20] = "isaldo";
                    columnas[21] = "xobs";
                    columnas[22] = "xdirec";
                    columnas[23] = "xruc";
                    columnas[24] = "xrazon_social";
                    columnas[25] = "pinteres";
                    columnas[26] = "falta";
                    columnas[27] = "cusuario";
                    columnas[28] = "fanul";
                    columnas[29] = "cusuario_anul";
                    columnas[30] = "fultim_modif";
                    columnas[31] = "cusuario_modif";
                    columnas[32] = "xtelef";
                    columnas[33] = "xciudad";
                    columnas[34] = "tnotas";
                    columnas[35] = "interes";
                    columnas[36] = "tgravadas_10";
                    columnas[37] = "tgravadas_5";
                    columnas[38] = "timpuestos_10";
                    columnas[39] = "timpuestos_5";
                    columnas[40] = "nplazo_cheque";
                    columnas[41] = "xfactura";
                    columnas[42] = "cmotivo";
                    columnas[43] = "fvenc_impre";
                    columnas[44] = "xnombre_cliente";
                    columnas[45] = "xentregador";
                    String sql = " SELECT f.*, c.xnombre as xnombre_cliente, e.xnombre as xentregador "
                        +" FROM facturas f, CLIENTES C, empleados e WHERE f.mestado = 'A' "
                        + " AND f.ffactur between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121) "
                        + "AND f.ctipo_docum  = 'FCR' and f.cod_cliente = c.cod_cliente AND f.cod_entregador = e.cod_empleado "
                        +" and NOT EXISTS ( SELECT * FROM recibos_det D, recibos r where d.nrecibo = r.nrecibo "
                        +" AND r.frecibo between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121) "
                        +" AND r.mestado = 'A'  AND f.cod_cliente  = r.cod_cliente AND d.ctipo_docum = f.ctipo_docum AND d.ndocum = f.nrofact "
                        +" ) and f.cod_entregador = ISNULL("+codEntregador+",f.cod_entregador) "
                        +" ORDER BY  f.ffactur, f.cod_zona, f.cod_entregador, f.nrofact ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }
                break;
            case "3":
                lNombreReport = "RDETDIFER";
                if(tipo.equals("ARCH")){
                    columnas = new String[11];
                    columnas[0] = "nro_cheque";
                    columnas[1] = "fcheque";
                    columnas[2] = "femision";
                    columnas[3] = "cod_banco";
                    columnas[4] = "icheque";
                    columnas[5] = "cod_cliente";
                    columnas[6] = "xnombre_cliente";
                    columnas[7] = "xdesc_banco";
                    columnas[8] = "cod_zona";
                    columnas[9] = "cod_entregador";
                    columnas[10] = "xentregador";
                    String sql = " SELECT distinct c.nro_cheque, c.fcheque, c.femision, c.cod_banco, c.icheque,c.cod_cliente, "
                            + " c2.xnombre as xnombre_cliente, b.xdesc as xdesc_banco, f.cod_zona, f.cod_entregador , e.xnombre as xentregador "
                            +" FROM cheques c, clientes c2, bancos b, recibos_det d, facturas f, recibos_cheques h, empleados e "
                            +" WHERE c.femision between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121) "
                            +" AND c.mtipo = 'D'  AND c.cod_cliente = c2.cod_cliente AND c.cod_banco = b.cod_banco "
                            +" and  d.cod_empr  = 2 and F.COD_EMPR = 2 and h.cod_empr = 2 AND d.ctipo_docum = f.ctipo_docum "
                            +" AND d.nrecibo = h.nrecibo AND h.cod_banco = c.cod_banco AND f.mestado = 'A' "
                            +" and f.cod_entregador = ISNULL("+codEntregador+",f.cod_entregador) "
                            +" UNION ALL "
                            +" SELECT distinct c.nro_cheque, c.fcheque, c.femision, c.cod_banco, c.icheque,c.cod_cliente, "
                            +" c2.xnombre as xnombre_cliente, b.xdesc as xdesc_banco, f.cod_zona, f.cod_entregador, e.xnombre as xentregador "
                            +" FROM cheques c, clientes c2, bancos b, cheques_det d, facturas f , empleados e "
                            +" WHERE c.femision between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121) "
                            +" AND c.cod_cliente = c2.cod_cliente AND c.cod_banco = b.cod_banco AND c.mtipo = 'D' "
                            +" AND f.ffactur > convert(smalldatetime,'01/01/2013',121) "
                            +" and  d.cod_empr = 2 and f.cod_empr = 2 AND d.nro_cheque = c.nro_cheque AND d.cod_banco = c.cod_banco "
                            +" AND d.nrofact = f.nrofact AND f.cod_entregador = e.cod_empleado aND d.ctipo_docum = f.ctipo_docum AND f.mestado = 'A' "
                            +" and f.cod_entregador = ISNULL("+codEntregador+",f.cod_entregador) "
                            +" order by femision,cod_zona,cod_entregador,nro_cheque ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }
                break;
            case "4":
                lNombreReport = "RRESRECAU";
                if(tipo.equals("ARCH")){
                    columnas = new String[18];
                    columnas[0] = "fplanilla";
                    columnas[1] = "tventas";
                    columnas[2] = "tnotas_dev";
                    columnas[3] = "tcheques_dia";
                    columnas[4] = "kcheques";
                    columnas[5] = "tefectivo";
                    columnas[6] = "tmonedas";
                    columnas[7] = "tgastos";
                    columnas[8] = "tcreditos";
                    columnas[9] = "tcheques_dif";
                    columnas[10] = "tpagares";
                    columnas[11] = "tnotas_otras";
                    columnas[12] = "tnotas_Atras";
                    columnas[13] = "tdiferencia";
                    columnas[14] = "cconc";
                    columnas[15] = "xdesc_conc";
                    columnas[16] = "ttotal";
                    columnas[17] = "tdepositos";
                    
                    String sql = " SELECT r.fplanilla, ISNULL(SUM(r.tventas),0) as tventas, ISNULL(SUM(r.tnotas_dev),0) as tnotas_dev, "
                            +" ISNULL(SUM(r.tcheques_dia),0) as tcheques_dia,ISNULL(SUM(r.kcheques),0) as kcheques, "
                            + "ISNULL(SUM(r.tefectivo),0) as tefectivo,ISNULL(SUM(r.tmonedas),0) as tmonedas,ISNULL(SUM(r.tgastos),0) as tgastos, "
                            +" ISNULL(SUM(r.tcreditos),0) as tcreditos, ISNULL(SUM(r.tcheques_dif),0) as tcheques_dif, "
                            +" ISNULL(SUM(r.tpagares),0) as tpagares,ISNULL(SUM(r.tnotas_otras),0) as tnotas_otras, "
                            +" ISNULL(SUM(r.tnotas_atras),0) as tnotas_Atras,ISNULL(SUM(r.tdiferencia),0) as tdiferencia, '' as cconc, "
                            +" '' as xdesc_conc, 0 as ttotal,ISNULL(SUM(r.tdepositos),0) as tdepositos "
                            +" FROM recaudacion r "
                            +" WHERE r.cod_empr = 2  "
                            +" AND (r.fplanilla between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121)) "
                            +" and r.cod_entregador = ISNULL("+codEntregador+",r.cod_entregador) "
                            +" GROUP BY r.fplanilla "
                            +" UNION ALL "
                            +" SELECT n.fdocum as fplanilla, ISNULL(SUM(r.tventas),0) as tventas, ISNULL(SUM(r.tnotas_dev),0) as tnotas_dev, "
                            +" ISNULL(SUM(r.tcheques_dia),0) as tcheques_dia, ISNULL(SUM(r.kcheques),0) as kcheques, "
                            +" ISNULL(SUM(r.tefectivo),0) as tefectivo, ISNULL(SUM(r.tmonedas),0) as tmonedas,ISNULL(SUM(r.tgastos),0) as tgastos, "
                            +" ISNULL(SUM(r.tcreditos),0), ISNULL(SUM(r.tcheques_dif),0) as tcheques_dif, ISNULL(SUM(r.tpagares),0) as tpagares, "
                            +" ISNULL(SUM(r.tnotas_otras),0) as tnotas_otras,ISNULL(SUM(r.tnotas_atras),0) as tnotas_atras, "
                            +" ISNULL(SUM(r.tdiferencia),0) as tdiferencia,N.CCONC, c.xdesc as xdesc_conc, SUM(n.ttotal) as ttotal, "
                            +" ISNULL(SUM(r.tdepositos),0) as tdepositos "
                            +" FROM notas_ventas n, facturas f, recaudacion r, conceptos_documentos c "
                            +" WHERE f.cod_empr = 2 AND n.cod_empr = 2 "
                            +" AND (n.fdocum between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121))  "
                            +" AND (n.cconc != 'DEV') AND n.ctipo_docum = 'NCV' AND n.cconc = c.cconc AND r.fplanilla = n.fdocum "
                            +" AND n.nrofact = f.nrofact AND n.ctipo_docum = c.ctipo_docum AND n.fac_ctipo_docum = f.ctipo_docum AND n.mestado  = 'A' "
                            +" AND n.ffactur = f.ffactur AND f.cod_entregador = r.cod_entregador "
                            +" and n.cod_entregador = ISNULL("+codEntregador+",n.cod_entregador) "
                            +" GROUP BY fdocum, n.cconc, c.xdesc "
                            +" ORDER BY 1,14,15 ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }
                break;
            case "5":
                lNombreReport = "RMESRECAU";
                if(tipo.equals("ARCH")){
                    columnas = new String[18];
                    columnas[0] = "cod_zona";
                    columnas[1] = "tventas";
                    columnas[2] = "tnotas_dev";
                    columnas[3] = "tcheques_dia";
                    columnas[4] = "kcheques";
                    columnas[5] = "tefectivo";
                    columnas[6] = "tdepositos";
                    columnas[7] = "tmonedas";
                    columnas[8] = "tgastos";
                    columnas[9] = "tcreditos";
                    columnas[10] = "tcheques_dif";
                    columnas[11] = "tpagares";
                    columnas[12] = "tnotas_otras";
                    columnas[13] = "tnotas_atras";
                    columnas[14] = "tdiferencia";
                    columnas[15] = "cconc";
                    columnas[16] = "xdesc_conc";
                    columnas[17] = "ttotal";
                    
                    String sql = " SELECT r.cod_zona, ISNULL(SUM(r.tventas),0) as tventas, ISNULL(SUM(r.tnotas_dev),0) as tnotas_dev, "
                            +" ISNULL(SUM(r.tcheques_dia),0) as tcheques_dia, ISNULL(SUM(r.kcheques),0) as kcheques, "
                            +" ISNULL(SUM(r.tefectivo),0) as tefectivo,ISNULL(SUM(r.tdepositos),0) as tdepositos, "
                            +" ISNULL(SUM(r.tmonedas),0) as tmonedas,ISNULL(SUM(r.tgastos),0) as tgastos, ISNULL(SUM(r.tcreditos),0) as tcreditos, "
                            +" ISNULL(SUM(r.tcheques_dif),0) as tcheques_dif, ISNULL(SUM(r.tpagares),0) as tpagares,"
                            +" ISNULL(SUM(r.tnotas_otras),0) as tnotas_otras, ISNULL(SUM(r.tnotas_atras),0) as tnotas_atras, "
                            +" ISNULL(SUM(r.tdiferencia),0) as tdiferencia, '' as cconc, '' as xdesc_conc, 0 as ttotal "
                            +" FROM recaudacion r "
                            +" WHERE r.cod_empr = 2 "
                            +" AND (r.fplanilla between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121)) "
                            +" and r.cod_entregador = ISNULL("+codEntregador+",r.cod_entregador) and r.cod_zona = ISNULL('"+codZona+"',r.cod_zona) "
                            +" GROUP BY r.cod_zona "
                            +"ORDER BY 1 ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }
                break;
            case "6":
                lNombreReport = "RRESRECAU";
                if(tipo.equals("ARCH")){
                    columnas = new String[18];
                    columnas[0] = "fplanilla";
                    columnas[1] = "tventas";
                    columnas[2] = "tnotas_dev";
                    columnas[3] = "tcheques_dia";
                    columnas[4] = "kcheques";
                    columnas[5] = "tefectivo";
                    columnas[6] = "tdepositos";
                    columnas[7] = "tmonedas";
                    columnas[8] = "tgastos";
                    columnas[9] = "tcreditos";
                    columnas[10] = "tcheques_dif";
                    columnas[11] = "tnotas_otras";
                    columnas[12] = "tdiferencia";
                    columnas[13] = "tnotas_atras";
                    columnas[14] = "tpagares";
                    columnas[15] = "CCONC";
                    columnas[16] = "xdesc_conc";
                    columnas[17] = "ttotal";
                    
                    String sql = " SELECT '' as fplanilla, ISNULL(SUM(r.tventas),0) as tventas, ISNULL(SUM(r.tnotas_dev),0) as tnotas_dev, "
                            +" ISNULL(SUM(r.tcheques_dia),0) as tcheques_dia, ISNULL(SUM(r.kcheques),0) as kcheques, "
                            +" ISNULL(SUM(r.tefectivo),0) as tefectivo,ISNULL(SUM(r.tdepositos), 0) as tdepositos,ISNULL(SUM(r.tmonedas),0) as tmonedas, "
                            +" ISNULL(SUM(r.tgastos),0) as tgastos,ISNULL(SUM(r.tcreditos),0) as tcreditos,"
                            +" ISNULL(SUM(r.tcheques_dif),0) as tcheques_dif,ISNULL(SUM(r.tnotas_otras),0) as tnotas_otras,"
                            +" ISNULL(SUM(r.tdiferencia),0) as tdiferencia,ISNULL(SUM(r.tnotas_atras), 0) as tnotas_atras,"
                            +" ISNULL(SUM(r.tpagares), 0) as tpagares,'' AS CCONC, '' AS  xdesc_conc, 0  as ttotal "
                            +" FROM recaudacion r  WHERE r.cod_empr = 2 "
                            +" AND (r.fplanilla between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121)) "
                            +" and R.cod_entregador = ISNULL("+codEntregador+",R.cod_entregador) "
                            +" UNION ALL "
                            +" SELECT '' as fplanilla,ISNULL(SUM(r.tventas),0) as tventas,ISNULL(SUM(r.tnotas_dev),0) as tnotas_dev,"
                            +" ISNULL(SUM(r.tcheques_dia),0) as tcheques_dia, ISNULL(SUM(r.kcheques),0) as kcheques, "
                            +" ISNULL(SUM(r.tefectivo),0) as tefectivo,ISNULL(SUM(r.tdepositos),0) as tdepositos,ISNULL(SUM(r.tmonedas),0) as tmonedas, "
                            +" ISNULL(SUM(r.tgastos),0) as tgastos, ISNULL(SUM(r.tcreditos),0),ISNULL(SUM(r.tcheques_dif),0) as tcheques_dif, "
                            +" ISNULL(SUM(r.tnotas_otras),0) as tnotas_otras,ISNULL(SUM(r.tdiferencia),0) as tdiferencia, "
                            +" ISNULL(SUM(r.tnotas_atras), 0) as tnotas_atras,ISNULL(SUM(r.tpagares), 0) as tpagares,N.CCONC, c.xdesc as xdesc_conc, "
                            +" SUM(n.ttotal) as ttotal "
                            +" FROM notas_ventas n, facturas f, recaudacion r, conceptos_documentos c "
                            +" WHERE f.cod_empr = 2 AND n.cod_empr = 2 "
                            +" AND (n.fdocum between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121)) "
                            +" AND (n.cconc != 'DEV') AND n.cconc = c.cconc AND r.fplanilla = n.fdocum AND n.nrofact = f.nrofact "
                            +" AND n.ctipo_docum = c.ctipo_docum AND n.fac_ctipo_docum = f.ctipo_docum AND n.mestado = 'A' AND n.ffactur= f.ffactur "
                            +" AND f.cod_entregador = r.cod_entregador and n.cod_entregador = ISNULL("+codEntregador+",n.cod_entregador) "
                            +" GROUP BY n.cconc, c.xdesc "
                            +" ORDER BY 1,16,17 ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }
                break;
            case "7":
                lNombreReport = "RDIFRECAU";
                if(tipo.equals("ARCH")){
                    columnas = new String[5];
                    columnas[0] = "fplanilla";
                    columnas[1] = "cod_zona";
                    columnas[2] = "cod_entregador";
                    columnas[3] = "xnombre";
                    columnas[4] = "tdiferencia";
                    
                    String sql = " SELECT r.fplanilla,r.cod_zona,r.cod_entregador,e.xnombre,r.tdiferencia "
                            +" FROM recaudacion r ,empleados e "
                            +" WHERE r.cod_empr = 2 "
                            +" AND (r.fplanilla between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121)) "
                            +" AND r.cod_entregador = e.cod_empleado and r.cod_entregador = ISNULL("+codEntregador+",r.cod_entregador) "
                            +" and r.cod_zona = ISNULL('"+codZona+"',r.cod_zona) "
                            +" ORDER BY 1 "
                            +" ORDER BY 3 ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }
                break;
            case "8":
                lNombreReport = "RTOTDIFRECAU";
                if(tipo.equals("ARCH")){
                    columnas = new String[5];
                    columnas[0] = "fplanilla";
                    columnas[1] = "cod_zona";
                    columnas[2] = "cod_entregador";
                    columnas[3] = "xnombre";
                    columnas[4] = "tdiferencia";
                    
                    String sql = " SELECT r.fplanilla,r.cod_zona,r.cod_entregador,e.xnombre,r.tdiferencia "
                            +" FROM recaudacion r ,empleados e "
                            +" WHERE r.cod_empr = 2 "
                            +" AND (r.fplanilla between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121)) "
                            +" AND r.cod_entregador = e.cod_empleado and r.cod_entregador = ISNULL("+codEntregador+",r.cod_entregador) "
                            +" and r.cod_zona = ISNULL('"+codZona+"',r.cod_zona) "
                            +" ORDER BY 1 "
                            +" ORDER BY 3 ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }
                break;
            case "9":
                lNombreReport = "RRECAUDA_ZONA";
                if(tipo.equals("ARCH")){
                    columnas = new String[24];
                    columnas[0] = "fplanilla";
                    columnas[1] = "cod_zona";
                    columnas[2] = "cod_entregador";
                    columnas[3] = "xdesc";
                    columnas[4] = "xnombre";
                    columnas[5] = "nro_planilla";
                    columnas[6] = "tventas";
                    columnas[7] = "tnotas_dev";
                    columnas[8] = "tcheques_dia";
                    columnas[9] = "kcheques";
                    columnas[10] = "tefectivo";
                    columnas[11] = "tdepositos";
                    columnas[12] = "tmonedas";
                    columnas[13] = "tgastos";
                    columnas[14] = "tcreditos";
                    columnas[15] = "tcheques_dif";
                    columnas[16] = "tpagares";
                    columnas[17] = "tnotas_otras";
                    columnas[18] = "tnotas_atras";
                    columnas[19] = "tdiferencia";
                    columnas[20] = "xnombre";
                    columnas[21] = "cconc";
                    columnas[22] = "xdesc_conc";
                    columnas[23] = "ttotal";
                    
                    String sql = " SELECT r.fplanilla,d.cod_zona,r.cod_entregador,z.xdesc,e.xnombre,r.nro_planilla,d.tventas,d.tnotas_dev, "
                            +" r.tcheques_dia,r.kcheques,r.tefectivo,r.tdepositos,r.tmonedas,r.tgastos,d.tcreditos,r.tcheques_dif,r.tpagares, "
                            +" d.tnotas_otras,d.tnotas_atras,r.tdiferencia,e.xnombre,'' as cconc,'' as xdesc_conc, 0 as ttotal "
                            +" FROM recaudacion r,empleados e,recaudacion_det d ,zonas z "
                            +" WHERE r.cod_empr = 2 AND e.cod_empr = 2 "
                            +" AND (r.fplanilla between convert(smalldatetime,'"+fdesde+"',121) and convert(smalldatetime,'"+fhasta+"',121)) "
                            +" AND r.cod_entregador = e.cod_empleado AND r.nro_planilla = d.nro_planilla AND d.cod_zona = z.cod_zona "
                            +" and r.cod_entregador = ISNULL("+codEntregador+",r.cod_entregador) AND d.cod_zona = ISNULL('"+codZona+"',d.cod_zona) "
                            +" ORDER BY 1,3 ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf",lNombreReport.toLowerCase()+".jasper");
                }                
                break;
            case "10":
                lNombreReport = "RRESCABENT";
                if(tipo.equals("ARCH")){
                    columnas = new String[22];
                    columnas[0] = "fplanilla";
                    columnas[1] = "cod_zona";
                    columnas[2] = "cod_entregador";
                    columnas[3] = "xnombre";
                    columnas[4] = "nro_planilla";
                    columnas[5] = "tventas";
                    columnas[6] = "tnotas_dev";
                    columnas[7] = "tcheques_dia";
                    columnas[8] = "kcheques";
                    columnas[9] = "tefectivo";
                    columnas[10] = "tdepositos";
                    columnas[11] = "tmonedas";
                    columnas[12] = "tgastos";
                    columnas[13] = "tcreditos";
                    columnas[14] = "tcheques_dif";
                    columnas[15] = "tpagares";
                    columnas[16] = "tnotas_otras";
                    columnas[17] = "tnotas_atras";
                    columnas[18] = "tdiferencia";
                    columnas[19] = "cconc";
                    columnas[20] = "xdesc_conc";
                    columnas[21] = "ttotal";
                    String sql = " SELECT r.fplanilla, r.cod_zona, r.cod_entregador,e.xnombre, r.nro_planilla, r.tventas, r.tnotas_dev,"
                            +" r.tcheques_dia, r.kcheques, r.tefectivo, r.tdepositos, r.tmonedas, r.tgastos, r.tcreditos, r.tcheques_dif, r.tpagares,"
                            +" r.tnotas_otras, r.tnotas_atras,  r.tdiferencia, '' as cconc, '' as xdesc_conc, 0 as ttotal "
                            +" FROM recaudacion r, empleados e "
                            +" where r.fplanilla BETWEEN convert(smalldatetime,'"+fdesde+"',121) AND convert(smalldatetime,'"+fhasta+"',121) "
                            +" and e.cod_empr = 2 "
                            +" and  r.cod_empr = 2 "
                            +" and e.cod_empleado = r.cod_entregador "
                            +" and r.cod_entregador = ISNULL("+codEntregador+",r.cod_entregador) "
                            +" UNION ALL "
                            +" SELECT n.fdocum as fplanilla, r.cod_zona, f.cod_entregador, e.xnombre, r.nro_planilla, r.tventas, r.tnotas_dev, "
                            +" r.tcheques_dia, r.kcheques, r.tefectivo, r.tdepositos, r.tmonedas, r.tgastos, r.tcreditos, r.tcheques_dif, r.tpagares, "
                            +" r.tnotas_otras, r.tnotas_atras, r.tdiferencia,N.CCONC, c.xdesc as xdesc_conc, SUM(n.ttotal) as ttotal "
                            +" FROM notas_ventas n, empleados e, facturas f, recaudacion r, conceptos_documentos c "
                            +" WHERE f.cod_empr = 2 AND n.cod_empr = 2 and e.cod_empr = 2 "
                            +" AND (n.fdocum BETWEEN convert(smalldatetime,'"+fdesde+"',121) AND convert(smalldatetime,'"+fhasta+"',121))  AND (n.cconc != 'DEV') "
                            +" AND n.ctipo_docum = 'NCV' "
                            +" AND e.cod_empleado = n.cod_entregador "
                            +" AND n.cod_entregador = r.cod_entregador "
                            +" AND n.cconc = c.cconc "
                            +" AND n.ctipo_docum = c.ctipo_docum "
                            +" AND r.fplanilla = n.fdocum "
                            +" AND n.nrofact = f.nrofact "
                            +" AND n.ffactur = f.ffactur "
                            +" AND n.fac_ctipo_docum = f.ctipo_docum " 
                            +" AND n.mestado  = 'A' "
                            +" and n.cod_entregador = ISNULL("+codEntregador+",n.cod_entregador) "
                            +" GROUP BY fdocum, r.cod_zona, f.cod_entregador, e.xnombre, r.nro_planilla, r.tventas, r.tnotas_dev, "
                            +" r.tcheques_dia, r.kcheques, r.tefectivo, r.tdepositos, r.tmonedas, r.tgastos, r.tcreditos, r.tcheques_dif, r.tpagares, "
                            +" r.tnotas_otras, r.tnotas_atras,r.tdiferencia, n.cconc, c.xdesc "
                            +" ORDER BY 3,1,2 ";
                    lista = excelFacade.listarParaExcel(sql);
                    rep.exportarExcel(columnas, lista, lNombreReport);
                } else {
                    Map param = new HashMap();
                    java.sql.Date sDesde = new java.sql.Date(desde.getTime());
                    java.sql.Date sHasta = new java.sql.Date(hasta.getTime());
                    param.put("desde",sDesde);
                    param.put("hasta",sHasta);
                    param.put("cod_entregador",codEntregador);
                    param.put("usuarioImpresion", "admin");
                    rep.llamarReporte(param,lNombreReport.toLowerCase()+".pdf","RRECAUDACION".toLowerCase()+".jasper");
                }
                break;
        }

    }
    
    private String dateToString(Date fecha) {
        String resultado = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            resultado = dateFormat.format(fecha);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }
        return resultado;
    }
    
    public List<Zonas> getListZonas(){
        return zonasFacade.findAll();
    }
    
    public List<Empleados> getListVendedores(){
        return vendedoresFacade.getEmpleadosVendedoresActivosPorCodEmp(2);
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

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
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

    public Empleados getEntregador() {
        return entregador;
    }

    public void setEntregador(Empleados entregador) {
        this.entregador = entregador;
    }

    public Zonas getZonasCamion() {
        return zonasCamion;
    }

    public void setZonasCamion(Zonas zonasCamion) {
        this.zonasCamion = zonasCamion;
    }

    public String getSalidas() {
        return salidas;
    }

    public void setSalidas(String salidas) {
        this.salidas = salidas;
    }

}
