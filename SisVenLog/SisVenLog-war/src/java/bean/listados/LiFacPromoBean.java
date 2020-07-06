package bean.listados;

import dao.CanalesVentaFacade;
import dao.PromocionesFacade;
import dao.FacturasConPromocionFacade;
import dao.MercaderiasFacade;
import dao.TiposDocumentosFacade;
import entidad.TiposDocumentos;
import entidad.CanalesVenta;
import entidad.Promociones;
import entidad.Mercaderias;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import util.LlamarReportes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DualListModel;
import util.DateUtil;

/**
 *
 * @author Arsenio
 */
@ManagedBean
@SessionScoped
public class LiFacPromoBean {

    private Date fechaDesde;

    private Date fechaHasta;

    private Promociones promocion;

    private List<Promociones> promociones;

    private TiposDocumentos tipoDocumento;

    private List<TiposDocumentos> tiposDocumentos;

    private CanalesVenta canalVenta;

    private List<CanalesVenta> canalesVenta;

    private Boolean sinIVA;

    private Boolean incluirPedidos;

    private DualListModel<Mercaderias> mercaderias;
    private List<Mercaderias> mercaderiasActivas;

    @EJB
    private PromocionesFacade promocionesFacade;

    @EJB
    private CanalesVentaFacade canalesVentaFacade;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private FacturasConPromocionFacade facConPromocionFacade;

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @PostConstruct
    public void instanciar(){
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        this.promociones = this.getPromociones();
        this.tiposDocumentos = tiposDocumentosFacade.listarTiposDocumentosLiFacPromo();
        this.canalesVenta = canalesVentaFacade.findAll();
        mercaderiasActivas = mercaderiasFacade.listarMercaderiasActivas();
        mercaderias = new DualListModel<>(mercaderiasActivas, new ArrayList<>());
    }

    public void ejecutar(String tipo) {
        try {
            if (Objects.isNull(this.promocion)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error de validación",
                                "Debe seleccionar una promoción"));
            }
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            String tdoc = tipoDocumento == null? "": tipoDocumento.getCtipoDocum();
            facConPromocionFacade.insertarMercaderiasSeleccionadas(stmt,
                    mercaderias.getTarget(), mercaderiasActivas);
            facConPromocionFacade.generateTableImpDatos(stmt, fechaDesde, fechaHasta,
                    promocion, tdoc, canalVenta, sinIVA, incluirPedidos);
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }

                Promociones promo = promociones.stream()
                        .filter(p -> (p.getPromocionesPK().getNroPromo() == 
                                (promocion != null? promocion.getPromocionesPK().getNroPromo():-1L)))
                        .findAny()
                        .orElse(null);

                CanalesVenta cv = canalesVenta.stream()
                        .filter(c -> c.getCodCanal()
                                .equals(canalVenta != null ? canalVenta.getCodCanal() : null))
                        .findAny()
                        .orElse(null);
                
                TiposDocumentos t = tiposDocumentos.stream()
                        .filter(td -> td.getCtipoDocum().equals(tipoDocumento != null ? tipoDocumento.getCtipoDocum(): null))
                        .findAny()
                        .orElse(null);

                rep.reporteFacPromo(DateUtil.formaterDateToString(fechaDesde),
                        DateUtil.formaterDateToString(fechaHasta),
                        t, promo, cv, sinIVA, usuarioImpresion);
            } else {
                String sql = facConPromocionFacade.generateSelectImpDatos();
                String[] columnas = new String[]{
                    "ctipo_docum",
                    "ndocum",
                    "cconc",
                    "xdesc_promo",
                    "fac_ctipo_docum",
                    "nrofact",
                    "nro_promo",
                    "fdocum",
                    "cod_cliente", 
                    "xnombre", 
                    "cod_merca",
                    "xdesc",
                    "cant_cajas",
                    "cant_unid", 
                    "unid_bonif",
                    "npeso_caja",
                    "npeso_unidad",
                    "itotal",
                    "cod_vendedor",
                    "xvendedor",
                    "ttotal",
                    "ntot_peso",
                    "ntot_pbonif",
                    "ibonific",
                    "idescto",
                    "pdesc",
                    "icosto_unidad",
                    "icosto_caja",
                    "icaja_vta",
                    "iunid_vta"
                };
                List<Object[]> lista = facConPromocionFacade.listarParaExcel(stmt, columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rfactpromo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LiFacPromoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Promociones getPromocion() {
        return promocion;
    }

    public void setPromocion(Promociones promocion) {
        this.promocion = promocion;
    }

    public List<Promociones> getPromociones() {
        this.promociones = promocionesFacade.listarPromocionesLiFacPromo(DateUtil.dateToString(fechaDesde),
                DateUtil.dateToString(fechaHasta));
        return promociones;
    }

    public void setPromociones(List<Promociones> promociones) {
        this.promociones = promociones;
    }

    public TiposDocumentos getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentos tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<TiposDocumentos> getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(List<TiposDocumentos> tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public CanalesVenta getCanalVenta() {
        return canalVenta;
    }

    public void setCanalVenta(CanalesVenta canalVenta) {
        this.canalVenta = canalVenta;
    }

    public List<CanalesVenta> getCanalesVenta() {
        return canalesVenta;
    }

    public void setCanalesVenta(List<CanalesVenta> canalesVenta) {
        this.canalesVenta = canalesVenta;
    }

    public Boolean getSinIVA() {
        return sinIVA;
    }

    public void setSinIVA(Boolean sinIVA) {
        this.sinIVA = sinIVA;
    }

    public Boolean getIncluirPedidos() {
        return incluirPedidos;
    }

    public void setIncluirPedidos(Boolean incluirPedidos) {
        this.incluirPedidos = incluirPedidos;
    }

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }

    public List<Mercaderias> getMercaderiasActivas() {
        return mercaderiasActivas;
    }

    public void setMercaderiasActivas(List<Mercaderias> mercaderiasActivas) {
        this.mercaderiasActivas = mercaderiasActivas;
    }
    
}