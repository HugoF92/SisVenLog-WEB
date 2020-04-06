package bean.listados;

import dao.CanalesCompraFacade;
import dao.ProveedoresFacade;
import dao.VencProveedoresFacade;
import entidad.TiposDocumentos;
import entidad.CanalesCompra;
import entidad.Proveedores;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import util.LlamarReportes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Arsenio
 */
@ManagedBean
@SessionScoped
public class LiVencProveedoresBean {

    private Date fechaDesde;

    private Date fechaHasta;

    private Proveedores proveedor;

    private List<Proveedores> proveedores;

    private TiposDocumentos tipoDocumento;

    private List<TiposDocumentos> tiposDocumentos;

    private String discriminado;

    private CanalesCompra canalCompra;

    private List<CanalesCompra> canalesCompra;

    @EJB
    private ProveedoresFacade proveedoresFacade;

    @EJB
    private CanalesCompraFacade canalesCompraFacade;

    @EJB
    private VencProveedoresFacade vencProveedoresFacade;

    @PostConstruct
    public void instanciar(){
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        this.discriminado = "ND";
        // Agregamos al listado de tipos de documentos el solicitado
        List<TiposDocumentos> respuesta = Arrays.asList(
                new TiposDocumentos("FAC", "FACTURAS DE COMPRA"),
                new TiposDocumentos("CHE", "CHEQUES EMITIDOS"));
        this.tiposDocumentos = respuesta;
        this.proveedores = proveedoresFacade.listarProveedoresActivos();
        this.canalesCompra = canalesCompraFacade.findAll();
    }

    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            vencProveedoresFacade.generateTableMostrar(stmt, fechaDesde, fechaHasta,
                    discriminado, "", proveedor, canalCompra);
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }

                Proveedores prov = proveedores.stream()
                        .filter(p -> p.getCodProveed()
                                .equals(proveedor != null ? proveedor.getCodProveed() : null))
                        .findAny()
                        .orElse(null);

                CanalesCompra cc = canalesCompra.stream()
                        .filter(c -> c.getCanalesCompraPK().getCcanalCompra()
                                .equals(canalCompra != null ? canalCompra.getCanalesCompraPK().getCcanalCompra() : null))
                        .findAny()
                        .orElse(null);

                rep.reporteVencProveedores(fechaDesde, fechaHasta,
                        prov, cc, discriminado, usuarioImpresion);
            } else {
                String sql = vencProveedoresFacade.generateSelectMosDatos();
                String[] columnas = new String[]{
                    "cod_proveed",
                    "xnombre",
                    "cod_proveed2",
                    "xnombre2",
                    "ctipo_docum",
                    "nrofact",
                    "ccanal_compra",
                    "xdesc_canal",
                    "ffactur",
                    "fprov",
                    "fvenc",
                    "ttotal",
                    "ideuda"
                };
                List<Object[]> lista = vencProveedoresFacade.listarParaExcel(stmt, columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rvencidascom");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LiVencProveedoresBean.class.getName()).log(Level.SEVERE, null, ex);
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

    public String getDiscriminado() {
        return discriminado;
    }

    public void setDiscriminado(String discriminado) {
        this.discriminado = discriminado;
    }

    public CanalesCompra getCanalCompra() {
        return canalCompra;
    }

    public void setCanalCompra(CanalesCompra canalCompra) {
        this.canalCompra = canalCompra;
    }

    public List<CanalesCompra> getCanalesCompra() {
        return canalesCompra;
    }

    public void setCanalesCompra(List<CanalesCompra> canalesCompra) {
        this.canalesCompra = canalesCompra;
    }
}
