package bean.listados;

import dao.DepositosFacade;
import dao.ExcelFacade;
import dao.MercaderiasFacade;
import dao.PersonalizedFacade;
import dao.TiposDocumentosFacade;
import dto.LiMercaSinDto;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class LiEnviosBean {

    @EJB
    private PersonalizedFacade personalizedFacade;

    @EJB
    private DepositosFacade depositosFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Depositos depositoOrigen;
    private Depositos depositoDestino;

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

    public LiEnviosBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.depositoOrigen = new Depositos(new DepositosPK());
        this.depositoDestino = new Depositos(new DepositosPK());

        this.desde = new Date();
        this.hasta = new Date();

    }

    public void ejecutar(String tipo) {

        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String anulados = "";
        String origen = "";
        String destino = "";

        StringBuilder sql = new StringBuilder();

        if (this.conDetalles) {
            /*con detalles*/

            sql.append("SELECT *, d.xdesc as xdesc_merca,  d1.xdesc as xdesc_origen, d2.xdesc as xdesc_destino, \n"
                    + " c.xdesc as xdesc_canal \n"
                    + " FROM envios e LEFT JOIN envios_det D ON e.cod_empr = d.cod_empr AND e.nro_envio = d.nro_envio, "
                    + " empleados p, canales_venta c, \n"
                    + " depositos d1, depositos d2 \n"
                    + " WHERE e.cod_empr = 2 \n"
                    + " AND e.cod_canal = c.cod_canal \n"
                    + " AND e.cod_empr = p.cod_empr \n"
                    + " AND e.cod_entregador = p.cod_empleado \n"
                    + " AND e.Depo_origen = d1.cod_depo \n"
                    + " AND e.depo_destino = d2.cod_Depo \n"
                    + " AND e.cod_empr = d1.cod_empr \n"
                    + " AND e.cod_empr = d2.cod_empr \n"
                    + " AND e.nro_envio BETWEEN " + envioDesde + " AND " + enviohasta + "  \n"
                    + " AND e.fenvio BETWEEN '" + fdesde + "' AND '" + fhasta + "'  \n");
        }

        if (this.pedidosFacturas) {
            /*Pedidos y facturas*/
            sql.append("SELECT e.*,pv.nro_pedido, pe.mestado as mped_estado, fa.nrofact, fa.mestado as mfac_estado, \n"
                    + " p.xnombre, d1.xdesc as xdesc_origen, d2.xdesc as xdesc_destino, c.xdesc as xdesc_canal, fa.xrazon_social \n"
                    + " FROM envios e, pedidos_envios pv, empleados p, canales_venta c,"
                    + " depositos d1, depositos d2 , pedidos pe "
                    + "LEFT OUTER JOIN  facturas fa \n"
                    + " ON pe.nro_pedido = fa.nro_pedido and fa.cod_empr = 2 \n"
                    + " WHERE e.cod_empr = 2 \n"
                    + " AND e.cod_entregador = p.cod_empleado \n"
                    + " AND e.cod_empr = p.cod_empr \n"
                    + " AND e.cod_canal = c.cod_canal \n"
                    + " AND e.Depo_origen = d1.cod_depo \n"
                    + " AND e.depo_destino = d2.cod_Depo \n"
                    + " AND e.cod_empr = d1.cod_empr \n"
                    + " AND e.cod_empr = d2.cod_empr \n"
                    + " AND pv.cod_empr = 2 \n"
                    + " AND pv.nro_envio = e.nro_envio \n"
                    + " AND pv.nro_pedido = pe.nro_pedido \n"
                    + " AND pe.cod_empr = 2 \n"
                    + " AND e.nro_envio BETWEEN " + envioDesde + " AND " + enviohasta + "  \n"
                    + " AND e.fenvio BETWEEN '" + fdesde + "' AND '" + fhasta + "'  \n");

            if (this.pedidos != null) {

                sql.append(" AND pe.nro_pedido IN (" + this.pedidos + ") \n");
            }

            if (facturas != null) {

                sql.append(" AND fa.nrofact IN (" + this.facturas + ") \n");
            }

        } else {
            sql.append("SELECT *, d1.xdesc as xdesc_origen, d2.xdesc as xdesc_destino, c.xdesc as xdesc_canal \n"
                    + " FROM envios e, empleados p, canales_venta c, depositos d1, depositos d2\n"
                    + " WHERE e.cod_empr = 2 \n"
                    + " AND e.cod_entregador = p.cod_empleado \n"
                    + " AND e.cod_empr = p.cod_empr \n"
                    + " AND e.cod_canal = c.cod_canal \n"
                    + " AND e.Depo_origen = d1.cod_depo \n"
                    + " AND e.depo_destino = d2.cod_Depo \n"
                    + " AND e.cod_empr = d1.cod_empr \n"
                    + " AND e.cod_empr = d2.cod_empr \n"
                    + " AND e.nro_envio BETWEEN " + envioDesde + " AND " + enviohasta + "  \n"
                    + " AND e.fenvio BETWEEN '" + fdesde + "' AND '" + fhasta + "'  \n");
        }

        if (depositoOrigen != null) {
            origen = depositosFacade.find(depositoOrigen.getDepositosPK()).getXdesc();
            sql.append(" AND e.depo_origen = " + depositoOrigen.getDepositosPK().getCodDepo() + " \n");
        } else {
            origen = "TODOS";
        }

        if (depositoDestino != null) {
            origen = depositosFacade.find(depositoDestino.getDepositosPK()).getXdesc();
            sql.append(" AND e.depo_destino = " + depositoDestino.getDepositosPK().getCodDepo() + " \n");
        } else {
            destino = "TODOS";
        }

        if (conAnulados) {
            anulados = "CON ANULADOS";
            sql.append(" AND e.mestado IN ('A','X') \n");

        } else {
            anulados = "SIN ANULADOS";
            sql.append(" AND e.mestado = 'A' \n");

        }

        if (this.resumenMerca) {
            sql.append(" ORDER BY e.fenvio, e.depo_destino, D.COD_MERCA \n");

        } else {

            sql.append(" ORDER BY e.fenvio, e.nro_envio \n");
        }

        System.out.println("=====> SQL : " + sql.toString());

        if (tipo.equals("VIST")) {

            rep.reporteLiEnvios1(sql.toString(), dateToString2(desde), dateToString2(hasta),
                    origen, destino, anulados, "admin", tipo);
            //rep.reporteLiMercaSin(sql.toString(), dateToString2(desde), dateToString2(hasta), tipoDocumento, "admin", zona, tipo);
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
    public Depositos getDepositoOrigen() {
        return depositoOrigen;
    }

    public void setDepositoOrigen(Depositos depositoOrigen) {
        this.depositoOrigen = depositoOrigen;
    }

    public Depositos getDepositoDestino() {
        return depositoDestino;
    }

    public void setDepositoDestino(Depositos depositoDestino) {
        this.depositoDestino = depositoDestino;
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

}
