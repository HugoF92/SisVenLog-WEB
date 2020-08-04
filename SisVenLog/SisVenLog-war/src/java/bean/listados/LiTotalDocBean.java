package bean.listados;

import dao.TiposDocumentosFacade;
import dao.TotalesDocumentosFacade;
import entidad.TiposDocumentos;
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
public class LiTotalDocBean {
    
    private Date fechaDesde;

    private Date fechaHasta;

    private TiposDocumentos tipoDocumento;

    private List<TiposDocumentos> tiposDocumentos;

    private String opcion;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private TotalesDocumentosFacade totalesDocumentosFacade;
    
    @PostConstruct
    public void instanciar() {
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        this.tiposDocumentos = tiposDocumentosFacade.getTipoDocumentosByCtipo(
                Arrays.asList("FCO", "FCR", "CPV", "FCP", "FCS", "NDV", "NCV", "NCS"));
        this.opcion = "";
    }
    
    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            String tdoc = tipoDocumento == null? "": tipoDocumento.getCtipoDocum();
            totalesDocumentosFacade.generateTableMosdatos(stmt, fechaDesde, fechaHasta,
                    tdoc, (opcion == null? "": opcion));
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                TiposDocumentos td = tiposDocumentos.stream()
                        .filter(tdd -> tdd.equals(tipoDocumento))
                        .findAny()
                        .orElse(null);
                rep.reporteTotalDocumentos(fechaDesde, fechaHasta, td, opcion, usuarioImpresion);
            } else {
                String sql = totalesDocumentosFacade.generateSelectMosdatos();
                String[] columnas = new String[]{
                    "ndocum",
                    "ctipo_docum",
                    "xnombre",
                    "fdocum",
                    "cconc",
                    "cod_cliente",
                    "mestado",
                    "texentas",
                    "tgravadas_10",
                    "tgravadas_5",
                    "timpuestos_10",
                    "timpuestos_5",
                    "ttotal"
                };
                List<Object[]> lista = totalesDocumentosFacade.listarParaExcel(stmt, columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rtotaldoc");
            }
            } catch (SQLException ex) {
            Logger.getLogger(LiTotalDocBean.class.getName()).log(Level.SEVERE, null, ex);
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

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
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