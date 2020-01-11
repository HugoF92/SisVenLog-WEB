package bean.listados;

import dao.ExcelFacade;
import dao.RutasFacade;
import dao.ZonasFacade;
import entidad.Rutas;
import entidad.Zonas;
import entidad.ZonasPK;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class genStockBean {
    
    private Date desde;
    
    private Date hasta;
    
    private Zonas zonas;
    
    private Rutas rutas;
    
    @EJB
    private ZonasFacade zonasFacade;
    @EJB
    private RutasFacade rutasFacade;
    @EJB
    private ExcelFacade excelFacade;
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
//        this.proveedor = new Proveedores();
        this.desde = new Date();
        this.hasta = new Date();
        this.zonas = new Zonas(new ZonasPK());
    }
    
    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();
        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String fmes = dateToStringMes(new Date());
        String fanho = dateToStringAno(new Date());
        String extras = "" , extras2 = "";
        if (this.rutas != null){
            extras += " AND f.cod_ruta = "+this.rutas.getRutasPK().getCodRuta()+" ";
        }
        if (this.zonas != null){
            extras += " AND f.cod_zona = '"+this.zonas.getZonasPK().getCodZona()+"' ";
        }
        List<Object[]> lista = new ArrayList<>();
        String[] columnas = null;
        String sql = "";
        switch (tipo) {
            case "1" :
                columnas = new String[6];
                columnas[0] = "cod_depo";
                columnas[1] = "cod_merca";
                columnas[2] = "xdesc";
                columnas[3] = "cant_cajas";
                columnas[4] = "cant_unid";
                columnas[5] = "mestado";
                sql = " SELECT P.cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d, depositos e, pedidos p, depositos e2 " +
                    " WHERE p.cod_depo = e.cod_depo " +
                    " AND f.cod_depo = e2.cod_depo AND e2.mtipo IN ('M','E') " +
                    " AND e.mtipo = 'F' AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND p.cod_empr = 2 AND f.cod_empr = d.cod_empr " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur= d.ffactur AND f.cod_empr = p.cod_empr " +
                    " AND f.nro_pedido = p.nro_pedido and F.MESTADO = 'A' " +
                    " AND f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+
                    "' AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista = excelFacade.listarParaExcel(sql);                
                sql = " SELECT f.cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d, depositos e " +
                    " WHERE f.cod_depo = e.cod_depo " +
                    " AND e.mtipo = 'F' AND f.nro_pedido IS NULL " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur = d.ffactur and F.MESTADO = 'A' " +
                    " AND f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+
                    "' AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));   
                sql = " SELECT 1 as cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d,depositos e " +
                    " WHERE f.nro_pedido IS NULL " +
                    " AND f.cod_canal = 'U' AND f.cod_depo = e.cod_depo " +
                    " AND f.cod_empr = e.cod_empr AND e.mtipo IN ('M','E') " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur =d.ffactur and F.MESTADO = 'A' " +
                    " AND f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+
                    "' AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));   
                sql = " SELECT 4 as cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d, depositos e " +
                    " WHERE f.nro_pedido IS NULL " +
                    " AND f.cod_depo = e.cod_depo AND f.cod_empr = e.cod_empr " +
                    " AND e.mtipo IN ('M','E') AND f.cod_canal = 'T' " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur = d.ffactur and F.MESTADO = 'A' " +
                    " AND f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+
                    "' AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));
                sql = " SELECT 22 as cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d, depositos e " +
                    " WHERE f.nro_pedido IS NULL " +
                    " AND f.cod_depo = e.cod_depo AND f.cod_empr = e.cod_empr " +
                    " AND e.mtipo IN ('M','E') AND f.cod_canal = 'AJ' " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur = d.ffactur and F.MESTADO = 'A' " +
                    " AND f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+
                    "' AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));   
                sql = " SELECT 4 as cod_Depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid ,f.mestado " +
                    " FROM facturas f, facturas_det d,depositos e " +
                    " WHERE f.nro_pedido IS NULL " +
                    " AND f.cod_depo = e.cod_depo AND f.cod_empr = e.cod_empr " +
                    " AND e.mtipo IN ('M','E') AND f.cod_canal = 'B' " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur = d.ffactur and F.MESTADO = 'A' " +
                    " AND f.ffactur BETWEEN '"+fdesde+"' AND '"+fhasta+
                    "' AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));   
                rep.exportarExcel(columnas, lista, "BUDGET");
                break;
            case "2":
                columnas = new String[6];
                columnas[0] = "cod_depo";
                columnas[1] = "cod_merca";
                columnas[2] = "xdesc";
                columnas[3] = "cant_cajas";
                columnas[4] = "cant_unid";
                columnas[5] = "mestado";
                sql = " SELECT p.cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d, depositos e, pedidos p, depositos e2 " +
                    " WHERE p.cod_depo = e.cod_depo " +
                    " AND e.mtipo = 'F' AND e2.cod_depo = f.cod_Depo " +
                    " AND e2.cod_empr = f.cod_empr AND e2.mtipo IN ('M','E') " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND p.cod_empr = 2 AND f.cod_empr = d.cod_empr " +
                    " AND f.nrofact = d.nrofact AND f.ffactur = d.ffactur " +
                    " AND f.cod_empr = p.cod_empr AND f.nro_pedido = p.nro_pedido " +
                    " AND f.mestado = 'A' " +
                    " AND MONTH(f.ffactur) = "+fmes+" AND YEAR(f.ffactur) = "+fanho+
                    " AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista = excelFacade.listarParaExcel(sql);
                sql = " SELECT f.cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid ,f.mestado " +
                    " FROM facturas f, facturas_det d, depositos e " +
                    " WHERE f.cod_depo = e.cod_depo " +
                    " AND e.mtipo = 'F' AND f.nro_pedido IS NULL " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur = d.ffactur and F.MESTADO = 'A' " +
                    " AND MONTH(f.ffactur) = "+fmes+" AND YEAR(f.ffactur) = "+fanho+
                    " AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));
                sql = " SELECT 1 as cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d,depositos e " +
                    " WHERE f.nro_pedido IS NULL " +
                    " AND f.cod_canal = 'U' AND f.cod_depo = e.cod_depo " +
                    " AND f.cod_empr = e.cod_empr AND e.mtipo IN ('M','E') " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur = d.ffactur and F.MESTADO = 'A' " +
                    " AND MONTH(f.ffactur) = "+fmes+" AND YEAR(f.ffactur) = "+fanho+
                    " AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));
                sql = " SELECT 4 as cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d, depositos e " +
                    " WHERE f.nro_pedido IS NULL " +
                    " AND f.cod_depo = e.cod_depo AND f.cod_empr = e.cod_empr " +
                    " AND e.mtipo IN ('M','E') AND f.cod_canal = 'T' " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur= d.ffactur and F.MESTADO = 'A' " +
                    " AND MONTH(f.ffactur) = "+fmes+" AND YEAR(f.ffactur) = "+fanho+
                    " AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));
                sql = " SELECT 22 as cod_depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid ,f.mestado " +
                    " FROM facturas f, facturas_det d, depositos e " +
                    " WHERE f.nro_pedido IS NULL " +
                    " AND f.cod_depo = e.cod_depo AND f.cod_empr = e.cod_empr " +
                    " AND e.mtipo IN ('M','E') AND f.cod_canal = 'AJ' " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur = d.ffactur and F.MESTADO = 'A' " +
                    " AND MONTH(f.ffactur) = "+fmes+" AND YEAR(f.ffactur) = "+fanho+
                    " AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));
                sql = " SELECT 4 as cod_Depo, d.cod_merca, d.xdesc, d.cant_cajas as cant_cajas, d.cant_unid as cant_unid , f.mestado " +
                    " FROM facturas f, facturas_det d,depositos e " +
                    " WHERE f.nro_pedido IS NULL " +
                    " AND f.cod_depo = e.cod_depo AND f.cod_empr = e.cod_empr " +
                    " AND e.mtipo IN ('M','E') AND f.cod_canal = 'B' " +
                    " AND f.cod_empr = 2 AND d.cod_empr = 2 " +
                    " AND f.cod_empr = d.cod_empr AND f.nrofact = d.nrofact " +
                    " AND f.ffactur = d.ffactur and F.MESTADO = 'A' " +
                    " AND MONTH(f.ffactur) = "+fmes+" AND YEAR(f.ffactur) = "+fanho+
                    " AND f.ctipo_docum IN ('FCO','FCR','CPV') " +
                    extras +" "; 
                lista.addAll(excelFacade.listarParaExcel(sql));
                rep.exportarExcel(columnas, lista, "FVENTAS");
                break;
            case "3":
                columnas = new String[11];
                columnas[0] = "cod_depo";
                columnas[1] = "cod_merca";
                columnas[2] = "xdesc";
                columnas[3] = "cant_cajas";
                columnas[4] = "cant_unid";
                columnas[5] = "mestado";
                columnas[6] = "nrelacion";
                columnas[7] = "cod_sublin";
                columnas[8] = "reser_caja";
                columnas[9] = "reser_unid";
                columnas[10] = "nppp";
                sql = " SELECT e.cod_depo, e.cod_merca, m.xdesc, e.cant_cajas, e.cant_unid, m.mestado, m.nrelacion, " +
                    " m.cod_sublinea as cod_sublin, e.reser_cajas as reser_caja, e.reser_unid, m.nprecio_ppp as nppp " +
                    " FROM existencias e, mercaderias m , depositos d " +
                    " WHERE e.cod_empr = 2 " +
                    " AND e.cod_depo = d.cod_depo AND e.cod_merca = m.cod_merca " +
                    " AND e.cod_empr = m.cod_empr AND e.cod_empr = d.cod_empr AND d.mtipo = 'F' ";
                lista = excelFacade.listarParaExcel(sql);
                rep.exportarExcel(columnas, lista, "STOCK");
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
    
    private String dateToStringMes(Date fecha) {
        String resultado = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM");
            resultado = dateFormat.format(fecha);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }
        return resultado;
    }
    
    private String dateToStringAno(Date fecha) {
        String resultado = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            resultado = dateFormat.format(fecha);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }
        return resultado;
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
    
    public List<Zonas> getListZonas(){
        return zonasFacade.findAll();
    }

    public Rutas getRutas() {
        return rutas;
    }

    public void setRutas(Rutas rutas) {
        this.rutas = rutas;
    }

    public List<Rutas> getListRutas(){
        return rutasFacade.findAll();
    }
}
