package bean.listados;

import dao.ExcelFacade;
import dao.ProveedoresFacade;
import entidad.Proveedores;
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
public class genBudgetBean {
    
    private Date desde;
    
    private Date hasta;
    
    private Proveedores proveedor;
    
    @EJB
    private ProveedoresFacade proveedoresFacade;
    @EJB
    private ExcelFacade excelFacade;
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.proveedor = new Proveedores();
        this.desde = new Date();
        this.hasta = new Date();
    }
    
    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();
        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String extras = "" , extras2 = "";
        if (this.proveedor != null){
            extras += "AND a.cod_proveed = "+this.proveedor.getCodProveed()+" ";
        }
        List<Object[]> lista = new ArrayList<Object[]>();
        String[] columnas = null;
        String sql = "";
        columnas = new String[13];
        columnas[0] = "cod_merca";
        columnas[1] = "xdesc";
        columnas[2] = "fmovim";
        columnas[3] = "cod_depo";
        columnas[4] = "cod_proveed";
        columnas[5] = "xnomb_prov";
        columnas[6] = "npeso_caja";
        columnas[7] = "npeso_unidad";
        columnas[8] = "nrelacion";
        columnas[9] = "totcajas";
        columnas[10] = "totunid";
        columnas[11] = "icomonto";
        columnas[12] = "ffactur";
        sql = "SELECT a.cod_merca, b.xdesc, a.fmovim, a.cod_depo, a.cod_proveed as cod_proveed, " +
            " p.xnombre as xnomb_prov, b.npeso_caja, b.npeso_unidad, b.nrelacion, " +
            " SUM(a.cant_cajas) as totcajas, SUM(a.cant_unid) as totunid, " +
            " SUM(a.igravadas+a.iexentas+a.impuestos) as icomonto, " +
            " ISNULL( " +
            " (SELECT top 1 " +
            " c.fprov as fprov " +
            " FROM VenlogDb.dbo.compras c, VenlogDb.dbo.compras_det d " +
            " WHERE c.cod_empr = 2 " +
            " AND d.cod_empr= 2 AND c.ffactur = a.fmovim " +
            " AND c.cod_proveed = a.cod_proveed AND d.cod_merca = a.cod_merca " +
            " AND c.cod_empr = d.cod_empr AND c.nrofact = d.nrofact " +
            " AND c.cod_depo = a.cod_depo AND c.ffactur = d.ffactur " +
            " ORDER BY c.fprov DESC " +
            " ) ,a.fmovim) " +
            " as ffactur " +
            " FROM VenlogDb.dbo.movimientos_merca a, VenlogDb.dbo.mercaderias b,VenlogDb.dbo.proveedores p" +
            " WHERE a.cod_empr = 2 AND a.fmovim BETWEEN '"+fdesde+"' AND '"+fhasta+
            "' AND a.ctipo_docum IN ('FCC','COC','CVC','NDC','NCC') " +
            " AND a.cod_merca = b.cod_merca AND a.cod_proveed = p.cod_proveed " +
            " AND a.cod_empr = b.cod_empr "+ 
            extras +
            " GROUP BY a.cod_merca, b.xdesc, a.fmovim, a.cod_depo,a.cod_proveed,p.xnombre, b.npeso_caja, b.npeso_unidad, b.nrelacion "; 
        lista = excelFacade.listarParaExcel(sql);
        rep.exportarExcel(columnas, lista, "P2BUDGET");
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

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }
    
    public List<Proveedores> getProveedoresList(){
        return this.proveedoresFacade.getProveedoresActivos();
    }
}
