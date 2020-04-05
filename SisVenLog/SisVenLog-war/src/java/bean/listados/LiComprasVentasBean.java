package bean.listados;

import dao.CanalesVentaFacade;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import dao.ComprasVentasFacade;
import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.LineasFacade;
import dao.MercaderiasFacade;
import dao.ProveedoresFacade;
import dao.RutasFacade;
import dao.SublineasFacade;
import dao.ZonasFacade;
import entidad.CanalesVenta;
import entidad.Depositos;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Mercaderias;
import entidad.Proveedores;
import entidad.Rutas;
import entidad.Sublineas;
import entidad.Zonas;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;
import javax.faces.application.FacesMessage;
import util.DateUtil;
import util.LlamarReportes;

/**
 *
 * @author Arsenio
 */
@ManagedBean
@SessionScoped
public class LiComprasVentasBean {

    private Date fechaDesde;

    private Date fechaHasta;

    private Depositos deposito;

    private List<Depositos> depositos;

    private Proveedores proveedor;

    private List<Proveedores> proveedores;

    private Sublineas sublinea;

    private List<Sublineas> sublineas;

    private Lineas linea;

    private List<Lineas> lineas;

    private CanalesVenta canal;

    private List<CanalesVenta> canales;

    private Boolean sinIva;

    private String discriminado;

    private DualListModel<Mercaderias> mercaderias;
    private List<Mercaderias> mercaderiasActivas;

    @EJB
    private DepositosFacade depositosFacade;

    @EJB
    private ProveedoresFacade proveedoresFacade;

    @EJB
    private SublineasFacade sublineasFacade;

    @EJB
    private LineasFacade lineasFacade;

    @EJB
    private CanalesVentaFacade canalesFacade;

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private ComprasVentasFacade comprasVentasFacade;

    @PostConstruct
    public void instanciar() {
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();

        this.deposito = null;
        this.depositos = depositosFacade.listarDepositosActivos();
        
        this.proveedor = null;
        this.proveedores = proveedoresFacade.buscarProveedoresActivos();
        
        this.sublinea = null;
        this.sublineas = sublineasFacade.listarSublineasActivas();
        
        this.linea = null;
        this.lineas = this.lineasFacade.listarLineasActivas();
                
        this.canal = null;
        this.canales = canalesFacade.listarCanalesVenta();
        
        this.sinIva = false;
        this.discriminado = "M";

        mercaderiasActivas = mercaderiasFacade.listarMercaderiasActivas();
        mercaderias = new DualListModel<>(mercaderiasActivas, new ArrayList<>());
    }

    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            // Insertamos las mercaderias seleccionadas en la tabla tmp_mercaderias
            comprasVentasFacade.insertarMercaderiasSeleccionadas(stmt,
                    mercaderias.getTarget(), mercaderiasActivas);
            comprasVentasFacade.generateTableUltimo(stmt, fechaDesde, fechaHasta,
                    deposito, proveedor, sublinea, linea, canal, sinIva, discriminado);
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                Proveedores prov = proveedores.stream()
                        .filter(p -> p.equals(proveedor))
                        .findAny()
                        .orElse(null);
                CanalesVenta cv = canales.stream()
                        .filter(c -> c.equals(canal))
                        .findAny()
                        .orElse(null);
                rep.reporteComprasVentas(fechaDesde, fechaHasta, prov, cv,
                        sinIva, discriminado, usuarioImpresion);
            } else {
                String sql = comprasVentasFacade.generateSelectUltimo();
                String[] columnas = new String[]{
                    "codigo",
                    "xdesc",
                    "cajas_compras",
                    "unid_compras",
                    "tot_compras",
                    "peso_ccajas",
                    "peso_cunid",
                    "cajas_ventas",
                    "unid_ventas",
                    "tot_ventas",
                    "peso_vcajas",
                    "peso_vunid"
                };
                List<Object[]> lista = comprasVentasFacade.listarParaExcel(stmt, columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rcompvtas");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LiComprasVentasBean.class.getName()).log(Level.SEVERE, null, ex);
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

    public Depositos getDeposito() {
        return deposito;
    }

    public void setDeposito(Depositos deposito) {
        this.deposito = deposito;
    }

    public List<Depositos> getDepositos() {
        return depositos;
    }

    public void setDepositos(List<Depositos> depositos) {
        this.depositos = depositos;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public List<Proveedores> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedores> proveedores) {
        this.proveedores = proveedores;
    }

    public Sublineas getSublinea() {
        return sublinea;
    }

    public void setSublinea(Sublineas sublinea) {
        this.sublinea = sublinea;
    }

    public List<Sublineas> getSublineas() {
        return sublineas;
    }

    public void setSublineas(List<Sublineas> sublineas) {
        this.sublineas = sublineas;
    }

    public Lineas getLinea() {
        return linea;
    }

    public void setLinea(Lineas linea) {
        this.linea = linea;
    }

    public List<Lineas> getLineas() {
        return lineas;
    }

    public void setLineas(List<Lineas> lineas) {
        this.lineas = lineas;
    }

    public CanalesVenta getCanal() {
        return canal;
    }

    public void setCanal(CanalesVenta canal) {
        this.canal = canal;
    }

    public List<CanalesVenta> getCanales() {
        return canales;
    }

    public void setCanales(List<CanalesVenta> canales) {
        this.canales = canales;
    }

    public Boolean getSinIva() {
        return sinIva;
    }

    public void setSinIva(Boolean sinIva) {
        this.sinIva = sinIva;
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

    public String getDiscriminado() {
        return discriminado;
    }

    public void setDiscriminado(String discriminado) {
        this.discriminado = discriminado;
    }
}
