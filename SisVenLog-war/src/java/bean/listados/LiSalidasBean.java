package bean.listados;

import dao.ZonasFacade;
import dao.ExcelFacade;
import dao.PersonalizedFacade;
import dao.TiposDocumentosFacade;
import entidad.Mercaderias;
import entidad.TiposDocumentos;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class LiSalidasBean {

    @EJB
    private PersonalizedFacade personalizedFacade;

    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Zonas zonas;
    private TiposDocumentos tiposDocumentos;

    private DualListModel<Mercaderias> mercaderias;

    private Date desde;
    private Date hasta;

    private boolean conDetalles;
    private boolean conAnulados;
    private boolean resumenMerca;
    private boolean pedidosFacturas;

    private Integer envioDesde;
    private Integer enviohasta;

    private String facturas;
    private String pedidos;

    public LiSalidasBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.zonas = new Zonas(new ZonasPK());
        this.tiposDocumentos = new TiposDocumentos();

        this.desde = new Date();
        this.hasta = new Date();

    }

    public void ejecutar(String tipo) {

        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String anulados = "";
        String zona = "";
        String tipoDocumento = "";

        StringBuilder sql = new StringBuilder();

        if (!conDetalles) {
            sql.append("SELECT DISTINCT v.* , r.cod_zona, z.xdesc as xzona, d.xdesc as xdepo, \n");
        } else if (conDetalles) {
            sql.append("SELECT DISTINCT v.*,dv.* , r.cod_zona, z.xdesc as xzona, d.xdesc as xdepo, \n");
        }

        sql.append(" c.xdirec, c.cod_ciudad, ci.xdesc  as xciudad , e2.xnombre as xentregador \n"
                + "		 FROM docum_varios v INNER JOIN \n"
                + "		 clientes c ON c.cod_cliente = v.cod_cliente \n"
                + "		 INNER JOIN rutas r ON c.cod_ruta = r.cod_ruta \n"
                + "		 INNER JOIN zonas z ON r.cod_zona = z.cod_zona \n"
                + "		 INNER JOIN  depositos d ON d.cod_depo = v.cod_depo  \n"
                + "		 LEFT OUTER JOIN pedidos f ON v.nro_pedido = f.nro_pedido and f.cod_empr = 2 \n"
                + "		 LEFT OUTER JOIN empleados e2 ON f.cod_entregador = e2.cod_empleado \n"
                + "		 , tmp_mercaderias t,  \n"
                + "		 docum_varios_det dv, \n"
                + "		 ciudades ci   \n"
                + "		 WHERE v.cod_empr = 2  \n"
                + "		 AND t.cod_merca = dv.cod_merca \n"
                + "		 AND v.ndocum = dv.ndocum \n"
                + "		 AND c.cod_ciudad = ci.cod_ciudad \n"
                + "		 AND v.ctipo_docum = dv.ctipo_docum \n"
                + "		 AND dv.cod_empr= 2 \n"
                + "		 AND v.ndocum BETWEEN " + this.envioDesde + " AND " + this.enviohasta + " \n"
                + "		 AND v.fdocum BETWEEN '" + fdesde + "' AND '" + fhasta + "' ");

        if (zonas != null) {
            zona = zonasFacade.find(zonas.getZonasPK()).getXdesc();
            sql.append(" AND r.cod_zona = '" + zonas.getZonasPK().getCodZona() + "' \n");
        } else {
            zona = "TODOS";
        }

        if (conAnulados) {
            anulados = "CON ANULADOS";
            sql.append(" AND v.mestado IN ('A','X','I','P')  \n");

        } else {
            anulados = "SIN ANULADOS";
            sql.append(" AND v.mestado IN ('I', 'A','P') \n");

        }

        if (tiposDocumentos != null) {
            tipoDocumento = tiposDocumentosFacade.find(tiposDocumentos.getCtipoDocum()).getXdesc();
            sql.append(" AND v.ctipo_docum =  '" + tiposDocumentos.getCtipoDocum() + "' \n");

        } else {
            sql.append(" AND v.ctipo_docum IN ('SG','SM') \n");
            tipoDocumento = "TODOS";
        }

        sql.append(" ORDER BY v.fdocum, v.ndocum  \n");

        System.out.println("=====> SQL : " + sql.toString());

        int opcion = 0;

        if (!conDetalles) {
            opcion = 1;
        } else if (conDetalles) {
            opcion = 2;
        }

        if (tipo.equals("VIST")) {

            rep.reporteLiSalidas(sql.toString(), dateToString2(desde), dateToString2(hasta),
                    this.envioDesde, this.enviohasta, zona, anulados, "admin", tipo, opcion);
        } else if (tipo.equals("ARCH")) {
            /*List<LiMercaSinDto> auxExcel = new ArrayList<LiMercaSinDto>();

            auxExcel = excelFacade.listarLiMercaSin(sql.toString());

            rep.excelLiMercaSin(auxExcel);*/
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

    private String dateToString2(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    //Getters & Setters
    public Zonas getDepositoOrigen() {
        return zonas;
    }

    public void setDepositoOrigen(Zonas zonas) {
        this.zonas = zonas;
    }

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
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

    public boolean isConDetalles() {
        return conDetalles;
    }

    public void setConDetalles(boolean conDetalles) {
        this.conDetalles = conDetalles;
    }

    public boolean isConAnulados() {
        return conAnulados;
    }

    public void setConAnulados(boolean conAnulados) {
        this.conAnulados = conAnulados;
    }

    public boolean isResumenMerca() {
        return resumenMerca;
    }

    public void setResumenMerca(boolean resumenMerca) {
        this.resumenMerca = resumenMerca;
    }

    public boolean isPedidosFacturas() {
        return pedidosFacturas;
    }

    public void setPedidosFacturas(boolean pedidosFacturas) {
        this.pedidosFacturas = pedidosFacturas;
    }

    public Integer getEnvioDesde() {
        return envioDesde;
    }

    public void setEnvioDesde(Integer envioDesde) {
        this.envioDesde = envioDesde;
    }

    public Integer getEnviohasta() {
        return enviohasta;
    }

    public void setEnviohasta(Integer enviohasta) {
        this.enviohasta = enviohasta;
    }

    public String getFacturas() {
        return facturas;
    }

    public void setFacturas(String facturas) {
        this.facturas = facturas;
    }

    public String getPedidos() {
        return pedidos;
    }

    public void setPedidos(String pedidos) {
        this.pedidos = pedidos;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

}
