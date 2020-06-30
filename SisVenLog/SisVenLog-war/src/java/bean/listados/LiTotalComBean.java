package bean.listados;

import dao.CanalesCompraFacade;
import dao.ProveedoresFacade;
import dao.TiposDocumentosFacade;
import dao.TotalesComprasFacade;
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
public class LiTotalComBean {
    
    private Date fechaDesde;

    private Date fechaHasta;

    private Proveedores proveedor;

    private List<Proveedores> proveedores;

    private TiposDocumentos tipoDocumento;

    private List<TiposDocumentos> tiposDocumentos;

    @EJB
    private ProveedoresFacade proveedoresFacade;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private TotalesComprasFacade totalesComprasFacade;
    
    @PostConstruct
    public void instanciar() {
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        this.tiposDocumentos = tiposDocumentosFacade.getTipoDocumentosByCtipo(Arrays.asList("FCC", "CVC", "COC", "NCC", "NDC"));
        this.proveedores = proveedoresFacade.listarProveedoresActivos();
    }
    
    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            String tdoc = tipoDocumento == null? "": tipoDocumento.getCtipoDocum();
            totalesComprasFacade.generateTableMosdatos(stmt, fechaDesde, fechaHasta,
                    tdoc, proveedor);
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                TiposDocumentos td = tiposDocumentos.stream()
                        .filter(tdd -> tdd.equals(tipoDocumento))
                        .findAny()
                        .orElse(null);

                Proveedores prov = proveedores.stream()
                        .filter(p -> p.getCodProveed()
                                .equals(proveedor != null ? proveedor.getCodProveed() : null))
                        .findAny()
                        .orElse(null);
                rep.reporteTotalCompras(fechaDesde, fechaHasta, prov, td,  usuarioImpresion);
            } else {
                String sql = totalesComprasFacade.generateSelectMosdatos();
                String[] columnas = new String[]{
                    "ctipo_docum",
                    "xnombre",
                    "texentas",
                    "tgravadas_10",
                    "tgravadas_5",
                    "timpuestos_10",
                    "timpuestos_5",
                    "ttotal"
                };
                List<Object[]> lista = totalesComprasFacade.listarParaExcel(stmt, columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rtotalcom");
            }
            } catch (SQLException ex) {
            Logger.getLogger(LiTotalComBean.class.getName()).log(Level.SEVERE, null, ex);
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
}
