package bean.listados;

import bean.selectormercaderias.SelectorDatosBean;
import dao.ExcelFacade;
import dao.LiPagaresFacade;
import dao.ZonasFacade;
import entidad.Clientes;
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
import org.primefaces.context.RequestContext;
import util.DateUtil;
import util.LlamarReportes;
import util.StringUtil;

/**
 *
 * @author mbrizuela
 */
@ManagedBean
@ViewScoped
public class LiMoroCliBean {
    
    private Date facturacionDesde;
    private Date facturacionHasta;
    private Zonas zona;
    private Boolean checkCliente;
    private Integer codigoCliente;
    private String nombreCliente;
    private Clientes cliente;
    private List<Zonas> listaZonas;
    private List<Clientes> listaClientes;
    private String seleccion;
    private Boolean seleccionarClientes;
    private List<Clientes> listadoClientesSeleccionados;
    private String filtro;
    private String contenidoError;
    private String tituloError;
    @EJB
    private ZonasFacade zonaFacade;
    @EJB
    private ExcelFacade excelFacade;
    @EJB
    private LiPagaresFacade pagareFacade;
    
    @PostConstruct
    public void instanciar() {
        this.seleccion = "1";
        this.facturacionDesde = new Date();
        this.facturacionHasta = new Date();
        this.listaZonas = zonaFacade.listarZonasPorEstado("A");
        this.checkCliente = true;
        this.seleccionarClientes = false;
        this.listadoClientesSeleccionados = new ArrayList<Clientes>();
    }

    public void ejecutarListado(String tipo){
        try{
            LlamarReportes rep = new LlamarReportes();
            String fFacturacionDesde = DateUtil.formaterDateToString(facturacionDesde, "yyyy/MM/dd");
            String fFacturacionHasta = DateUtil.formaterDateToString(facturacionHasta, "yyyy/MM/dd");    
            
            String reporte = "";
            String[] columnas = null;
            
            String query; //mostrar (tmp_saldos)
            String query2; //mostrar1
            String query3; //mostrar2
            String query4; //mostrar3
            String query5; //mostrar
            String query6; //mostrarZ
            String queryReport;
            String extraWhere1 = "";
            String extraWhere2 = "";
            
            if (zona != null) {
                extraWhere2 += "AND r.cod_zona = '" + zona.getZonasPK().getCodZona() + "' ";
            }
            this.listadoClientesSeleccionados = new ArrayList<Clientes>();
            if (!checkCliente) {
                this.listadoClientesSeleccionados = new ArrayList<Clientes>();
                for (TmpDatos t : pagareFacade.getDatosSelctor("select distinct * from tmp_datos order by codigo")) {
                    Clientes c = new Clientes();
                    c.setCodCliente(Integer.valueOf(t.getCodigo()));
                    c.setXnombre(t.getDescripcion());
                    this.listadoClientesSeleccionados.add(c);
                }
            }
            if (!this.listadoClientesSeleccionados.isEmpty()) {
                extraWhere1 += "AND cod_cliente IN (" + StringUtil.convertirListaAString(this.listadoClientesSeleccionados) + ") ";
            }
            
            query = 
                "SELECT DISTINCT " +
                    "cod_cliente, fmovim, isaldo + acum_debi - acum_credi AS iultima " +
                "FROM " +
                    "saldos_clientes s1 " +
                "WHERE " +
                    "fmovim = " +
                        "(SELECT MAX(fmovim) " +
                        "FROM saldos_clientes s2 " +
                        "WHERE s2.cod_empr = 2 " +
                        "AND s1.cod_cliente = s2.cod_cliente " +
                        "AND s1.cod_empr = s2.cod_empr " +
                        "AND fmovim < '" + fFacturacionHasta + "' " +
                        "AND isaldo + acum_debi - acum_credi > 0) " +
                    extraWhere1;

            query2 = 
                "SELECT " +
                    "r.cod_zona, z.xdesc as xdesc_zona, t.cod_cliente, t.xnombre, t.nplazo_credito, c.nrofact, c.fvenc, " +
                    "s.iultima, SUM((c.texentas+c.tgravadas+c.timpuestos)*mindice) + SUM(ipagado*mindice) AS ideuda_tot " +
                "FROM " +
                    "cuentas_corrientes c, rutas r, clientes t, zonas z, (" + query + ") s " +
                "WHERE " +
                    "c.fmovim > s.fmovim " +
                    "AND c.fmovim <= '" + fFacturacionHasta + "' " +
                    "AND r.cod_zona = z.cod_zona " +
                    "AND r.cod_empr = z.cod_empr " +
                    "AND t.cod_ruta = r.cod_ruta " +
                    "AND t.cod_empr = r.cod_empr " +
                    "AND t.cod_empr = 2 " +
                    "AND c.cod_empr = 2 " +
                    "AND c.cod_cliente = t.cod_cliente " +
                    "AND c.cod_cliente = s.cod_cliente " +
                    "AND (c.fac_ctipo_docum = 'FCR' OR c.ctipo_docum IN ('CHQ','CHC')) " +
                    extraWhere2 +
                "GROUP BY " +
                    "r.cod_zona, z.xdesc, t.cod_cliente, t.xnombre, t.nplazo_credito, c.nrofact, c.fvenc, s.iultima " +
                "HAVING " +
                    "(SUM((c.texentas+c.tgravadas+c.timpuestos)*mindice) + SUM(ipagado*mindice)) > 0 ";

            query3 = 
                "SELECT " +
                    "cod_zona, xdesc_zona, cod_cliente, xnombre, nplazo_credito, SUM(ideuda_tot) AS ideuda_venc " +
                "FROM " +
                    "(" + query2 + ") m " + 
                "WHERE " + 
                    "fvenc <= '" + fFacturacionHasta + "' " +
                "GROUP BY " + 
                    "cod_zona, xdesc_zona, cod_cliente, xnombre, nplazo_credito ";

            query4 = 
                "SELECT " +
                    "cod_zona, xdesc_zona, cod_cliente, xnombre, nplazo_credito, iultima, SUM(ideuda_tot) as ideuda_tot " +
                "FROM " +
                    "(" + query2 + ") m " +
                "GROUP BY " +
                    "cod_zona, xdesc_zona, cod_cliente, xnombre, nplazo_credito, iultima ";

            query5 = 
                "SELECT " + 
                    "a.cod_zona, a.xdesc_zona, a.cod_cliente, a.xnombre, a.nplazo_credito, b.ideuda_tot, a.ideuda_venc " +
                "FROM " +
                    "(" + query3 + ") a, (" + query4 + ") b " +
                "WHERE " + 
                    "a.cod_zona = b.cod_zona " +
                    "AND a.cod_cliente = b.cod_cliente ";

            query6 = 
                "SELECT " +
                    "r.cod_zona, (SUM((c.texentas+c.tgravadas+c.timpuestos)*mindice)) AS ifacturado " +
                "FROM " +
                    "cuentas_corrientes c, clientes t, rutas r " +
                "WHERE " +
                    "c.fmovim between '" + fFacturacionDesde + "' AND '" + fFacturacionHasta + "' " +
                    "AND c.cod_cliente = t.cod_cliente " +
                    "AND t.cod_ruta = r.cod_ruta " +
                    "AND (c.ctipo_docum = 'FCR' OR c.ctipo_docum = 'CHQ' OR (c.ctipo_docum = 'NCV' AND c.fac_ctipo_docum = 'FCR')) " +
                    extraWhere2 + 
                "GROUP BY r.cod_zona ";

            queryReport = 
                "SELECT " + 
                    "m.*, z.ifacturado " +
                "FROM " + 
                    "(" + query5 + ") m, (" + query6 + ") z " +
                "WHERE " +
                    "ideuda_tot > 0 " +
                    "AND m.cod_zona = z.cod_zona " +
                "ORDER BY " +
                    "m.cod_zona, m.cod_cliente ";
            
            if (seleccion.equals("1")) {
                reporte = "RMOROCLI";
            } else if (seleccion.equals("2")) {
                reporte = "RMOROZONA";    
            }
            
            columnas = new String[8];
            columnas[0] = "cod_zona";
            columnas[1] = "xdesc_zona";
            columnas[2] = "cod_cliente";
            columnas[3] = "xnombre";
            columnas[4] = "nplazo_credito";
            columnas[5] = "ideuda_tot";
            columnas[6] = "ideuda_venc";
            columnas[7] = "ifacturado";
            
            //System.out.println("QUERY: " + queryReport);
            
            if (tipo.equals("VIST") && !seleccion.equals("8") ) {
                String usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                Map param = new HashMap();
                param.put("sql", queryReport);
                param.put("fdesde", facturacionDesde);
                param.put("fhasta", facturacionHasta);
                param.put("usuImprime", usuImprime);
                param.put("nombreRepo", reporte);

                if (this.zona != null) param.put("zona", zonaFacade.getZonaFromList(this.zona, this.listaZonas).getXdesc());
                else param.put("zona", "TODOS");
                
                if (!this.listadoClientesSeleccionados.isEmpty()) {
                    param.put("cliente", StringUtil.convertirListaAString(this.listadoClientesSeleccionados));
                } else param.put("cliente", "TODOS");
                
                rep.reporteGenerico(param, tipo, reporte);
            } else {
                List<Object[]> lista = new ArrayList<Object[]>();
                lista = excelFacade.listarParaExcel(queryReport);
                rep.exportarExcel(columnas, lista, reporte);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al ejecutar listado"));
        }        
    }
    
    public void marcarTodos() {
        SelectorDatosBean.datosFiltrados = false;
        this.checkCliente = true;
        this.seleccionarClientes = false;
    }
    
    public void llamarSelectorDatos() {
        if (this.seleccionarClientes) {
            this.checkCliente = false;
            SelectorDatosBean.sql = 
                    "SELECT cod_cliente, xnombre "
                    + "FROM clientes "
                    + "WHERE cod_estado IN ('A', 'S')";
            SelectorDatosBean.tabla_temporal = "tmp_datos";
            SelectorDatosBean.campos_tabla_temporal = "codigo, descripcion";
            RequestContext.getCurrentInstance().execute("PF('dlgSelDatos').show();");
        } else {
            this.checkCliente = true;
        }
        RequestContext.getCurrentInstance().update("mostrarBtnMor");
    }
    
    public Date getFacturacionDesde() {
        return facturacionDesde;
    }

    public void setFacturacionDesde(Date facturacionDesde) {
        this.facturacionDesde = facturacionDesde;
    }

    public Date getFacturacionHasta() {
        return facturacionHasta;
    }

    public void setFacturacionHasta(Date facturacionHasta) {
        this.facturacionHasta = facturacionHasta;
    }
    
    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
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

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Integer codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<Clientes> getListadoClientesSeleccionados() {
        return listadoClientesSeleccionados;
    }

    public void setListadoClientesSeleccionados(List<Clientes> listadoClientesSeleccionados) {
        this.listadoClientesSeleccionados = listadoClientesSeleccionados;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getContenidoError() {
        return contenidoError;
    }

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }

}
