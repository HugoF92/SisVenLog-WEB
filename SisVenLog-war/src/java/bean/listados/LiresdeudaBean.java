/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import bean.selectormercaderias.SelectorDatosBean;
import dao.GenericFacadeSQL;
import dao.TiposDocumentosFacade;
import dao.ZonasFacade;
import entidad.Clientes;
import entidad.TiposDocumentos;
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
public class LiresdeudaBean {
    
    private Date desde;    
    private Date hasta; 
    private Zonas zona; 
    private TiposDocumentos tipoDocumento;
    
    private Boolean checkCliente;
    private Boolean seleccionarClientes;
    private List<Clientes> listadoClientesSeleccionados;
    
    private Boolean conNotas = false;
    private String seleccion;
    private String seleccion2;
    
    private List<Zonas> listaZonas;
    private List<TiposDocumentos> listaTiposDocumentos;
    
    @EJB
    private ZonasFacade zonasFacade;    
    @EJB
    private TiposDocumentosFacade tipoDocumFacade;
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
        
        this.conNotas = false;
        this.checkCliente = true;
        this.listaZonas = zonasFacade.listarZonas();
        this.listaTiposDocumentos = tipoDocumFacade.listarTipoDocumentoDeudas();    
        this.listadoClientesSeleccionados = new ArrayList();
        
    }
    
    public void ejecutarListado(String tipo){
        try{      
            Long inicio = System.nanoTime();
            LlamarReportes rep = new LlamarReportes();                      
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
                     
            sqlReport = this.preEjecutarSQL(tipo);

            Long fin = System.nanoTime();
            System.out.println("exel total sql " + (fin-inicio)/1000000 + "msegundos" );
            if ( this.seleccion.equals("1") ) {                    
                //sqlReport = "select * from otrosdat m";
                switch ( this.seleccion2 ) {
                    case "2":
                        reporte = "rresdeudacli";
                        titulo = "RESUMEN DE DEUDAS";
                        break;
                    default:
                        titulo = "DEUDAS PENDIENTES";
                        if (conNotas){
                            reporte = "rdeudacli2";                                
                        } else {
                            reporte = "rdeudacli";
                        }
                }
            } else {
                //sqlReport = "select * from otrosdat m";                
                switch ( this.seleccion2 ) {
                    case "2":
                        reporte = "rresdeudazon";
                        titulo = "RESUMEN DE DEUDAS";
                        break;
                    default:
                        titulo = "DEUDAS PENDIENTES";
                        if (conNotas){
                            reporte = "rdeudazon2";                                
                        } else {
                            reporte = "rdeudazon";
                        }
                }
            }
            
            System.err.println("reporte: "+reporte);
            System.err.println("sqlReport: "+sqlReport);
            
            if (tipo.equals("VIST")){
                String usuImprime = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                Map param = new HashMap();
                param.put("sql", sqlReport);
                param.put("fdesde", this.desde);
                param.put("fhasta", this.hasta);
                param.put("titulo", titulo);
                param.put("usuImprime", usuImprime);
                param.put("nombreRepo", reporte);
                
                if (this.zona != null) param.put("zona", zonasFacade.getZonaFromLis(this.zona, this.listaZonas).getXdesc());
                else param.put("zona", "TODOS");
                
                if(!listadoClientesSeleccionados.isEmpty()) param.put("cliente", StringUtil.convertirListaAString(listadoClientesSeleccionados));
                else param.put("cliente", "TODOS");
            
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
    
    private String preEjecutarSQL(String tipo) throws Exception {
        String fdesde = DateUtil.formaterDateToString(this.desde, "yyyy-MM-dd");
        String fhasta = DateUtil.formaterDateToString(this.hasta, "yyyy-MM-dd");  
        String fdesdeSP = null;
                
        if (this.seleccion2.equals("3")){
            fdesde = DateUtil.formaterDateToString(this.hasta, "yyyy-MM-dd");  
            fdesdeSP = DateUtil.formaterDateToString(this.hasta, "yyyy-MM-dd");  
        } else {
            fdesdeSP = "2005-09-01";
            fdesde = "2005-09-01";
        }
        String sqlReturn = null;        
        String sql = null;//"select t.* into mostrar from ( ";
        String sql1 = null;//"IF OBJECT_ID('mostrar1') IS NOT NULL DROP TABLE mostrar1\n"
        //        + "IF OBJECT_ID('mostrar2') IS NOT NULL DROP TABLE mostrar2\n"
        //        + "IF OBJECT_ID('mostrar') IS NOT NULL DROP TABLE mostrar\n";
        String sql2 = null;
        String sql3 = null;
        //sqlFacade.executeQuery(sql1);
        
        //sql1 += "select t.* into mostrar1 from ( ";
        if (this.tipoDocumento == null || "FCR".equals(this.tipoDocumento.getCtipoDocum())) {
            
            sql1 =  " SELECT     r.cod_zona, z.xdesc AS xdesc_zona, " 
                + " t.cod_cliente, c.fac_ctipo_docum AS ctipo_docum, "
                + " CAST(c.nrofact AS char(15)) AS ndocum, f.ttotal, f.tnotas, t.xnombre,  "
                + " t.nplazo_credito, f.ffactur AS fdocum, c.fvenc, t.ilimite_compra, "
                + " SUM((c.texentas + c.tgravadas + c.timpuestos) * c.mindice) - "
                + " SUM(c.ipagado) AS ideuda "
                + " FROM         facturas f INNER JOIN "
                +             "  cuentas_corrientes c  "
                +             " ON f.cod_empr = c.cod_empr "
                +             " AND f.nrofact = c.nrofact "
                +             " AND f.ffactur = c.ffactur "
                +             " AND f.ctipo_docum = c.fac_ctipo_docum INNER JOIN "
                +             "  rutas r INNER JOIN "
                +             "  zonas z ON r.cod_zona = z.cod_zona AND r.cod_empr = z.cod_empr INNER JOIN "
                + "  clientes t ON r.cod_ruta = t.cod_ruta AND r.cod_empr = t.cod_empr ON c.cod_empr = t.cod_empr "
                + " AND c.cod_cliente = t.cod_cliente "	
                + " WHERE f.mestado = 'A' AND (c.fmovim between '"+fdesde+"' AND '"+fhasta+"' ) "
                + " AND (c.cod_empr = 2) AND (c.fac_ctipo_docum = 'FCR') and f.cod_empr = 2 ";
		
            if (this.zona != null){ 
                sql1 += " AND r.cod_zona = '" + this.zona.getZonasPK().getCodZona() +"' ";
            }

            if(!listadoClientesSeleccionados.isEmpty()){
                sql1 += " AND c.cod_cliente in (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ")";
            }

            if (this.seleccion2.equals("3")){
                sql1 += " AND f.ffactur = " + fdesde + " ";
            }

            sql1 += " GROUP BY  r.cod_zona, z.xdesc, t.cod_cliente, c.fac_ctipo_docum, "
                    + " c.nrofact, f.ttotal, f.tnotas,t.xnombre, " 
                    + " t.nplazo_credito, f.ffactur, c.fvenc, t.ilimite_compra ";            
        }
        
        if ( this.tipoDocumento == null ) {
            sql1 += " UNION ALL ";
        }
        
        if ( this.tipoDocumento == null || "CHQ".equals(this.tipoDocumento.getCtipoDocum()) ) {
                        
            sql1 += " SELECT r.cod_zona, z.xdesc AS xdesc_zona, t.cod_cliente, 'CHQ' AS ctipo_docum, "
                    + " c.ndocum_cheq AS ndocum, h.icheque as ttotal, 0 as tnotas, t.xnombre, t.nplazo_credito,  "
                    + "  h.femision AS fdocum, c.fvenc, t.ilimite_compra, SUM(c.ipagado * c.mindice) AS ideuda "
                    + " FROM         rutas r INNER JOIN "
                    + "  zonas z ON r.cod_zona = z.cod_zona AND r.cod_empr = z.cod_empr INNER JOIN "
                    + "  clientes t ON r.cod_ruta = t.cod_ruta AND r.cod_empr = t.cod_empr INNER JOIN "
                    + "  cuentas_corrientes c  "
                    + "   ON t.cod_empr = c.cod_empr "
                    + " AND t.cod_cliente = c.cod_cliente INNER JOIN "
                    + "  cheques h ON c.cod_empr = h.cod_empr AND c.ndocum_cheq = h.nro_cheque AND "
                    + " c.cod_banco = h.cod_banco AND c.cod_cliente = h.cod_cliente "
                    + " WHERE  (c.fmovim between '"+fdesde+"' AND '"+fhasta+"') AND (c.cod_empr = 2) "
                    + " AND (c.ctipo_docum IN ('CHQ', 'CHC')) ";

            if (this.zona != null){ 
                sql1 += " AND r.cod_zona = '" + this.zona.getZonasPK().getCodZona() +"' ";
            }

            if(!listadoClientesSeleccionados.isEmpty()){
                sql1 += " AND c.cod_cliente in (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ")";
            }

            sql1 += " GROUP BY  r.cod_zona, z.xdesc, t.cod_cliente, c.ndocum_cheq, h.icheque, t.xnombre, "
                + " t.nplazo_credito, h.femision, c.fvenc, t.ilimite_compra ";

        }
        
        //sql1 += " ) t " ;
        
        System.err.println("query1: " + sql1);
        //sqlFacade.executeQuery(sql1); 
        //sql += " select * from mostrar1 ";        
        if ( this.tipoDocumento == null || "PAG".equals(this.tipoDocumento.getCtipoDocum()) ) {
            
            //sql1 = "select t.* into mostrar2 from ( ";
            sql2 =  " SELECT r.cod_zona, z.xdesc AS xdesc_zona, t.cod_cliente, 'PAG' AS ctipo_docum, "
                    + " c.ndocum_cheq AS ndocum, 0 as ttotal, 0 as tnotas, t.xnombre, t.nplazo_credito,  "
                    + "  h.femision AS fdocum, c.fvenc, t.ilimite_compra, SUM(c.ipagado * c.mindice) AS ideuda "
                    + " FROM  rutas r INNER JOIN "
                    + "  zonas z ON r.cod_zona = z.cod_zona AND r.cod_empr = z.cod_empr INNER JOIN "
                    + "  clientes t ON r.cod_ruta = t.cod_ruta AND r.cod_empr = t.cod_empr INNER JOIN "
                    + "  cuentas_corrientes c  "
                    + "   ON t.cod_empr = c.cod_empr " 
                    + " AND t.cod_cliente = c.cod_cliente INNER JOIN " 
                    + "  pagares h ON c.cod_empr = h.cod_empr AND c.ndocum_cheq = CAST(h.nPAGARE AS CHAR) " 
                    + " AND c.cod_cliente = h.cod_cliente "
                    + " WHERE (c.fmovim between '"+fdesde+"' AND '"+fhasta+"') AND (c.cod_empr = 2) "
                    + " AND (c.ctipo_docum IN ('PAG', 'PAC')) ";
	
            if (this.zona != null){ 
                sql2 += " AND r.cod_zona = '" + this.zona.getZonasPK().getCodZona() +"' ";
            }

            if(!listadoClientesSeleccionados.isEmpty()){
                sql2 += " AND c.cod_cliente in (" + StringUtil.convertirListaAString(listadoClientesSeleccionados) + ")";
            }

            sql2 += " GROUP BY  r.cod_zona, z.xdesc, t.cod_cliente, c.ndocum_cheq, t.xnombre, "
                + " t.nplazo_credito, h.femision, c.fvenc, t.ilimite_compra ";
	
            //sql1 += " ) t ";
            
            System.err.println("query2: " + sql2);
            //sqlFacade.executeQuery(sql1);            
        }
        
        sql = " IF OBJECT_ID('mostrar2') IS NOT NULL " +
            "	select t.* into mostrar from (   " +
            "		select * from mostrar1   " +
            "		UNION ALL  " +
            "		select * from mostrar2   " +
            "	) t " +
            " else  " +
            "	select t.* into mostrar from (   " +
            "		select * from mostrar1   " +
            "	) t ";
        System.err.println("query: " + sql);
        //sqlFacade.executeQuery(sql);
        
        if (sql1 !=null & sql2!=null)
            sql = sql1 + "\n UNION ALL \n" + sql2 ;
        else 
            sql = sql1 ;
        
        sql2 = " IF OBJECT_ID('datos_inf') IS NOT NULL DROP TABLE datos_inf; "
            + "IF OBJECT_ID('cursal') IS NOT NULL DROP TABLE cursal; "
            + "IF OBJECT_ID('otrosdat') IS NOT NULL DROP TABLE otrosdat; "
            + "IF OBJECT_ID('curcli') IS NOT NULL DROP TABLE curcli; "
            + "IF OBJECT_ID('finzon') IS NOT NULL DROP TABLE finzon;";
        sqlFacade.executeQuery(sql2);
        
        sql2 = 
        " DECLARE "
        + "     @cod_empresa int, "
        + "     @l_negativos int, "
        + "     @l_agregar int, "
        + "     @l_corte_cliente int, "
        + "     @isaldo_inicial int, "
        + "     @l_finicial date = '"+fdesdeSP+"', "

        + "     @cod_cliente int,  "
        + "     @xnombre varchar(100), "
        + "     @ctipo_docum varchar(3), "
        + "     @ndocum varchar(15), "
        + "     @fdocum datetime, "
        + "     @fvence datetime, "
        + "     @cod_zona char(2), "
        + "     @xdesc_zona varchar(50), "
        + "     @nplazo_credito numeric(3,0), "
        + "     @ideuda numeric(38,0), "
        + "     @ttotal numeric(18,0), "
        + "     @ilimite_compra numeric(12,0), "
        + "     @l_ideuda numeric(38,0), "
        + "     @ctipo_documnv varchar(3), "
	+ "     @ttotalnv numeric(18,0), "
	+ "     @nro_nota varchar(20) \n"

        + " create table datos_inf (cod_cliente int, "
        + "                                    xnombre varchar(50), "
        + "                                    ctipo_docum varchar(3), "
        + "                                    ndocum varchar(15), "
        + "                                    fdocum datetime, "
        + "                                    fvence datetime, "
        + "                                    cod_zona varchar(2), "
        + "                                    xdesc_zona varchar(50), "
        + "                                    nplazo_credito numeric(3,0), "
        + "                                    ideuda numeric(38,0), " 
        + "                                    ttotal numeric(18,0), "
        + "                                    saldo_inicial numeric(38,0), "
        + "                                    limite_compra numeric(12,0), "
        + "                                    iven_30 int, "
        + "                                    ivenc_60 int,"
        + "                                    cod_proveed varchar(20), "
        + "                                    izona_saldo numeric(38,0)) \n"
        + " BEGIN	 \n"
        + "    set @l_corte_cliente = 0; \n"
        + "    set @cod_empresa = 2; \n"
        + "    set @l_agregar = 0; \n";
 
        if (this.seleccion2.equals("3")){
            //sql2 += "    SELECT * INTO curcli FROM ( "+sql+" ) t WHERE ideuda > 0 ORDER BY COD_CLIENTE ";
            sql3 = "    SELECT * FROM ( "+sql+" ) te WHERE te.ideuda > 0 \n";
            sql2 += "    DECLARE curdatos CURSOR FOR  \n"
            + "            SELECT cod_cliente, xnombre, ctipo_docum, ndocum, fdocum, fvenc, cod_zona, xdesc_zona, nplazo_credito, ideuda, ttotal, ilimite_compra \n"
            + "            FROM ("+sql+") t WHERE ideuda > 0 ORDER BY COD_CLIENTE \n";
        } else {
            //sql2 += "    SELECT * INTO curcli FROM ( "+sql+" ) t WHERE ideuda != 0 ORDER BY COD_CLIENTE ";
            sql3 = "    SELECT * FROM ( "+sql+" ) t WHERE ideuda != 0 \n";
            sql2 += "    DECLARE curdatos CURSOR FOR  \n"
            + "            SELECT cod_cliente, xnombre, ctipo_docum, ndocum, fdocum, fvenc, cod_zona, xdesc_zona, nplazo_credito, ideuda, ttotal, ilimite_compra \n"
            + "            FROM ("+sql+") t WHERE ideuda != 0 ORDER BY COD_CLIENTE \n";
        }        
        //sql2 += "    DECLARE curdatos CURSOR FOR  "
        //+ "            SELECT cod_cliente, xnombre, ctipo_docum, ndocum, fdocum, fvenc, cod_zona, xdesc_zona, nplazo_credito, ideuda, ttotal, ilimite_compra "
        //+ "            FROM curcli  "

        sql2 += "    OPEN curdatos  \n"
        + "            FETCH NEXT FROM curdatos INTO @cod_cliente, @xnombre, @ctipo_docum, @ndocum, @fdocum, @fvence, @cod_zona, @xdesc_zona, @nplazo_credito, @ideuda, @ttotal, @ilimite_compra "
        + "            WHILE @@FETCH_STATUS = 0  \n"
        + "            BEGIN  \n"
        + "                    IF  @l_corte_cliente != @cod_cliente \n"
        + "                    BEGIN \n"
        + "                            set @l_corte_cliente = @cod_cliente \n"
        + "                            set @isaldo_inicial = dbo.[calc_saldo_inicial_cliente](02, @cod_cliente, @l_finicial)  \n"
 
        + "                            set @l_negativos = (SELECT SUM(ideuda) from ("+sql+") t where ideuda<0 and cod_cliente=@cod_cliente) \n";
        
        if (this.seleccion2.equals("2")){
            sql2 += "                  set @l_negativos = (SELECT SUM(ideuda) from ("+sql+") t where ideuda<0 and cod_cliente=@cod_cliente) \n"
            + "                        set @isaldo_inicial = @isaldo_inicial + @l_negativos  \n";
        }
            
        
        sql2 += "                       set @l_agregar = 1 \n"
        + "                    END \n";
        
        if (this.seleccion2.equals("2")){
            sql2 += "                    IF @ideuda<0 \n"
            + "                    BEGIN "
            + "                        set @l_ideuda = 0;  \n"
            + "                    END \n"
            + "                    ELSE \n"
            + "                    BEGIN \n"
            + "                        set @l_ideuda = @ideuda \n"
            + "                    END \n";
        } else 
            sql2 += " set @l_ideuda = @ideuda \n";
        
        sql2 += "               IF @isaldo_inicial IS NULL \n"
        + "                    BEGIN \n"
        + "                        set @isaldo_inicial = 0; \n "
        + "                    END \n"
        + "                    ELSE \n"
        + "                    BEGIN \n"
        + "                        set @isaldo_inicial = @isaldo_inicial;\n "
        + "                    END \n"        
        + "                    IF @l_agregar = 1 \n"
        + "                    BEGIN \n"
        + "                            insert into datos_inf (cod_cliente, xnombre, ctipo_docum, ndocum, fdocum, fvence, cod_zona, xdesc_zona, nplazo_credito, ideuda, ttotal, saldo_inicial, iven_30, limite_compra )  \n"
        + "                            values  \n"
        + "                            (@cod_cliente, @xnombre, @ctipo_docum, @ndocum, @fdocum, @fvence, @cod_zona, @xdesc_zona, @nplazo_credito, @l_ideuda, @ttotal, @isaldo_inicial, 1, @ilimite_compra) \n"
        + "                            set @l_agregar = 0 \n"
        + "                    END   \n"
        + "                    ELSE    \n"
        + "                    BEGIN  \n"
        + "                            insert into datos_inf (cod_cliente, xnombre, ctipo_docum, ndocum, fdocum, fvence, cod_zona, xdesc_zona, nplazo_credito, ideuda, ttotal, saldo_inicial, iven_30, limite_compra)  \n"
        + "                            values  \n"
        + "                            (@cod_cliente, @xnombre, @ctipo_docum, @ndocum, @fdocum, @fvence, @cod_zona, @xdesc_zona, @nplazo_credito, @l_ideuda, @ttotal, 0, 0, @ilimite_compra) \n"
        + "                    END \n"
 
        + "            FETCH NEXT FROM curdatos INTO @cod_cliente, @xnombre, @ctipo_docum, @ndocum, @fdocum, @fvence, @cod_zona, @xdesc_zona, @nplazo_credito, @ideuda, @ttotal, @ilimite_compra \n"
        + "            END  \n"

        + "    CLOSE curdatos  \n"
        + "    DEALLOCATE curdatos  \n";

        if (!this.seleccion2.equals("3")){
            sql2 += "    DECLARE curdatosres CURSOR FOR  \n"
            + "            SELECT DISTINCT cod_cliente, xnombre, cod_zona, xdesc_zona, nplazo_credito, ilimite_compra  \n"
            + "            FROM ( "+sql+" ) t WHERE cod_cliente NOT IN (SELECT cod_cliente FROM ("+sql3+") t ) \n"

            + "    OPEN curdatosres  \n"
            + "            FETCH NEXT FROM curdatosres INTO @cod_cliente, @xnombre, @cod_zona, @xdesc_zona, @nplazo_credito, @ilimite_compra \n"
            + "            WHILE @@FETCH_STATUS = 0  \n"
            + "            BEGIN  \n"
            + "                    set @isaldo_inicial = dbo.[calc_saldo_inicial_cliente](02, @cod_cliente, @l_finicial)  \n"
            + "                    set @l_negativos = (SELECT SUM(ideuda) from ( "+sql+" ) t where ideuda<0 and cod_cliente=@cod_cliente) \n"
            + "                    set @isaldo_inicial = @isaldo_inicial + @l_negativos \n"

            + "                    IF @isaldo_inicial != 0 and @isaldo_inicial IS NOT NULL \n"
            + "                    BEGIN \n"
            + "                            insert into datos_inf (cod_cliente, xnombre, cod_zona, xdesc_zona, nplazo_credito, ideuda, saldo_inicial, iven_30, limite_compra)  \n"
            + "                            values  \n"
            + "                            (@cod_cliente, @xnombre, @cod_zona, @xdesc_zona, @nplazo_credito, 0, @isaldo_inicial, 1, @ilimite_compra)  \n"

            + "                    END \n"

            + "            FETCH NEXT FROM curdatosres INTO @cod_cliente, @xnombre, @cod_zona, @xdesc_zona, @nplazo_credito, @ilimite_compra \n"
            + "            END  \n"

            + "    CLOSE curdatosres  \n"
            + "    DEALLOCATE curdatosres  \n"

            + "            DELETE FROM datos_inf  "
            + "                WHERE cod_cliente IN ( "
            + "                        select cod_cliente FROM ( SELECT cod_cliente, saldo_inicial, sum(ideuda)  as tdeuda FROM datos_inf  "
            + "                                        GROUP BY cod_cliente, saldo_inicial ) t WHERE saldo_inicial + tdeuda = 0  "
            + "                        ) \n";
            
        } else {
            sql2 += "  UPDATE datos_inf SET saldo_inicial=0 \n";
        }
        
        if (this.seleccion.equals("2")){
            sql2 += " DECLARE finzon CURSOR FOR \n"
            + "                SELECT cod_zona, sum(saldo_inicial) as izona_saldo FROM (  \n"
            + "                        SELECT DISTINCT cod_zona, cod_cliente, saldo_inicial FROM datos_inf ) t \n"
            + "                GROUP BY cod_zona \n"

            + "        OPEN finzon  "
            + "                FETCH NEXT FROM finzon INTO @cod_zona, @isaldo_inicial \n"
            + "                WHILE @@FETCH_STATUS = 0  \n"
            + "                BEGIN  \n"

            + "                        update datos_inf  "
            + "                        set izona_saldo = @isaldo_inicial \n"
            + "                        where cod_zona = @cod_zona \n"

            + "                FETCH NEXT FROM finzon INTO @cod_zona, @isaldo_inicial "
            + "                END  \n"

            + "        CLOSE finzon  \n"
            + "        DEALLOCATE finzon \n";
        }
        
        if (this.conNotas){
            sql2 += " DECLARE curdat CURSOR FOR "
            + "	SELECT cod_cliente, xnombre, ctipo_docum, ndocum, fdocum, fvence, cod_zona, xdesc_zona, ideuda, ttotal  "
            + " FROM datos_inf where ctipo_docum ='FCR' AND ivenc_60 != 0 ; "

            + "     OPEN curdat "
            + "     FETCH NEXT FROM curdat INTO @cod_cliente, @xnombre, @ctipo_docum, @ndocum, @fdocum, @fvence, @cod_zona, @xdesc_zona, @ideuda, @ttotal "
            + "    WHILE @@FETCH_STATUS = 0  "
            + "    BEGIN  "

            + "                DECLARE curdat2 CURSOR FOR  "
            + "                        SELECT ctipo_docum, nro_nota, ttotal FROM notas_ventas 	 "
            + "                        WHERE COD_EMPR = 2 AND mestado = 'A' AND fac_ctipo_docum = @ctipo_docum  AND nrofact = @ndocum ; "

            + "                OPEN curdat2  "
            + "                        FETCH NEXT FROM curdat2 INTO @ctipo_documnv, @nro_nota, @ttotalnv "
            + "                        WHILE @@FETCH_STATUS = 0  "
            + "                        BEGIN  "

            + "                                insert into datos_inf (cod_cliente, xnombre, ctipo_docum, ndocum, fdocum, fvence, cod_zona, xdesc_zona, ideuda, ttotal, cod_proveed, saldo_inicial)  "
            + "                                values  "
            + "                                (@cod_cliente, @xnombre, @ctipo_documnv, @ndocum, @fdocum, @fvence, @cod_zona, @xdesc_zona, @ideuda, @ttotalnv, @nro_nota, 0 ) "

            + "                        FETCH NEXT FROM curdat2 INTO @ctipo_documnv, @nro_nota, @ttotalnv "
            + "                        END  "

            + "                CLOSE curdat2  "
            + "                DEALLOCATE curdat2 "

            + "        FETCH NEXT FROM curdat INTO @cod_cliente, @xnombre, @ctipo_docum, @ndocum, @fdocum, @fvence, @cod_zona, @xdesc_zona, @ideuda, @ttotal "
            + "        END  "

            + "     CLOSE curdat  "
            + "     DEALLOCATE curdat  ";
        }   
        
        if (tipo.equals("VIST")){
            if (this.seleccion.equals("1")){
                sqlReturn = "    SELECT * FROM datos_inf  "
                    + "    ORDER BY xnombre, cod_cliente, ctipo_docum, ndocum ";
            } else {
                sqlReturn = "    SELECT * FROM datos_inf  "
                    + "    ORDER BY cod_zona, cod_cliente, xnombre, ctipo_docum, ndocum ";
            }
        } else {
            if (this.seleccion.equals("1")){
                sqlReturn = "select cod_cliente, xnombre, saldo_inicial, ideuda_cheque, "  
                    + "            ideuda_fact, ideuda_pag, (saldo_inicial + ideuda_cheque + ideuda_fact + ideuda_pag) as ideuda_total  "
                    //+ "    into otrosdat "
                    + "    FROM ( SELECT cod_cliente, xnombre, saldo_inicial,  "
                    + "                    SUM (CASE  "
                    + "                                    WHEN ctipo_docum = 'CHQ' OR ctipo_docum = 'CHC'  "
                    + "                                       THEN ideuda  "
                    + "                                       ELSE 0  "
                    + "                       END) as ideuda_cheque,  "
                    + "                    SUM (CASE  "
                    + "                                    WHEN ctipo_docum NOT IN ('PAG','CHQ','CHC')  "
                    + "                                       THEN ideuda  "
                    + "                                       ELSE 0  "
                    + "                       END) as ideuda_fact, "
                    + "                    SUM (CASE  "
                    + "                                    WHEN ctipo_docum = 'PAG'  "
                    + "                                       THEN ideuda  "
                    + "                                       ELSE 0  "
                    + "                       END) as ideuda_pag "
                    + "            FROM datos_inf  "
                    + "            GROUP BY cod_cliente, xnombre, saldo_inicial ) t ";
            } else {
                sqlReturn = "select cod_zona, cod_cliente, xnombre, saldo_inicial, ideuda_cheque, "  
                    + "            ideuda_fact, ideuda_pag, (saldo_inicial + ideuda_cheque + ideuda_fact + ideuda_pag) as ideuda_total  "
                    //+ "    into otrosdat "
                    + "    FROM ( SELECT cod_zona, cod_cliente, xnombre, saldo_inicial,  "
                    + "                    SUM (CASE  "
                    + "                                    WHEN ctipo_docum = 'CHQ' OR ctipo_docum = 'CHC'  "
                    + "                                       THEN ideuda  "
                    + "                                       ELSE 0  "
                    + "                       END) as ideuda_cheque,  "
                    + "                    SUM (CASE  "
                    + "                                    WHEN ctipo_docum NOT IN ('PAG','CHQ','CHC')  "
                    + "                                       THEN ideuda  "
                    + "                                       ELSE 0  "
                    + "                       END) as ideuda_fact, "
                    + "                    SUM (CASE  "
                    + "                                    WHEN ctipo_docum = 'PAG'  "
                    + "                                       THEN ideuda  "
                    + "                                       ELSE 0  "
                    + "                       END) as ideuda_pag "
                    + "            FROM datos_inf  "
                    + "            GROUP BY cod_zona, cod_cliente, xnombre, saldo_inicial ) t ";
            }            
        }
            
        sql2 += " END; ";
        
        System.err.println("query: " + sql2);
	sqlFacade.executeQuery(sql2);
        
        return sqlReturn;
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
    
    /* GETTERS Y SETTERS */

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

    public List<Clientes> getListadoClientesSeleccionados() {
        return listadoClientesSeleccionados;
    }

    public void setListadoClientesSeleccionados(List<Clientes> listadoClientesSeleccionados) {
        this.listadoClientesSeleccionados = listadoClientesSeleccionados;
    }

    public Boolean getConNotas() {
        return conNotas;
    }

    public void setConNotas(Boolean conNotas) {
        this.conNotas = conNotas;
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

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public TiposDocumentos getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentos tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<TiposDocumentos> getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }
    
    
    
}
