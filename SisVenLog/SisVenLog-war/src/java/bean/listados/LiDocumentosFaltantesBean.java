package bean.listados;

import dao.DocumentosFaltantesFacade;
import entidad.TiposDocumentos;
import dao.TiposDocumentosFacade;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import util.LlamarReportes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
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
public class LiDocumentosFaltantesBean {

    private Long nroDesde;

    private Long nroHasta;

    private TiposDocumentos tipoDocumento;

    private List<TiposDocumentos> tiposDocumentos;

    private Date fechaInicial;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private DocumentosFaltantesFacade documentosFaltantesFacade;

    @PostConstruct
    public void instanciar(){
        this.nroDesde = 0L;
        this.nroHasta = 0L;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH,0);
        this.fechaInicial = calendar.getTime();
        // Agregamos al listado de tipos de documentos el solicitado
        List<TiposDocumentos> respuesta = Stream
                .concat(tiposDocumentosFacade.listarTiposDocumentosLiDocumentosFaltantes().stream(),
                        Stream.of(new TiposDocumentos("FAC", "FACTURAS DE VENTA")))
                .collect(Collectors.toList());
        this.tiposDocumentos = respuesta;
        if(!tiposDocumentos.isEmpty())
            this.tipoDocumento = this.tiposDocumentos.get(0);
    }

    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            documentosFaltantesFacade.generateSqlCurdoc(stmt, nroDesde, nroHasta,
                        tipoDocumento, fechaInicial);
            documentosFaltantesFacade.generateTableNumbers(stmt, nroDesde, nroHasta);
            documentosFaltantesFacade.generateTableMostrar(stmt);
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }

                TiposDocumentos t = tiposDocumentos.stream()
                        .filter(td -> tipoDocumento.getCtipoDocum().equals(td.getCtipoDocum()))
                        .findAny()
                        .orElse(null);

//                rep.reporteDocumentosFaltantes(nroDesde, nroHasta,t, fechaInicial, usuarioImpresion);
                
            } else {
                String sql = documentosFaltantesFacade.generateSelectMostrar();
                String[] columnas = new String[]{ "ndocum" };
                List<Object[]> lista = documentosFaltantesFacade.listarParaExcel(stmt, columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rfacfalta");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LiDocumentosFaltantesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Long getNroDesde() {
        return nroDesde;
    }

    public void setNroDesde(Long nroDesde) {
        this.nroDesde = nroDesde;
    }

    public Long getNroHasta() {
        return nroHasta;
    }

    public void setNroHasta(Long nroHasta) {
        this.nroHasta = nroHasta;
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

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }
}
