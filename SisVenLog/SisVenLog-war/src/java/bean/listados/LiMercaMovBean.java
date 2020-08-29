package bean.listados;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import dao.MovimientosMercaderiasFacade;
import dao.MercaderiasFacade;
import dao.ZonasFacade;
import entidad.Mercaderias;
import entidad.Zonas;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import util.LlamarReportes;

/**
 *
 * @author Arsenio
 */
@ManagedBean
@SessionScoped
public class LiMercaMovBean {

    private Date fechaDesde;

    private Date fechaHasta;

    private Zonas zona;

    private List<Zonas> zonas;

    private Mercaderias mercaderia;

    private List<Mercaderias> mercaderias;

    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private MovimientosMercaderiasFacade movimientosMercaderiasFacade;

    @EJB
    private MercaderiasFacade mercaderiasFacade;


    @PostConstruct
    public void instanciar() {
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        this.zona = null;
        this.zonas = this.zonasFacade.findAll();
        this.mercaderia = null;
        mercaderias = mercaderiasFacade.listarMercaderiasActivas();
    }

    public void ejecutar(String tipo) {
        try {
            if(!entradasValidas()){
                return;
            }
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            //Generamos la tabla temporal mostrar
            movimientosMercaderiasFacade.generateTableMostrar(stmt, fechaDesde,
                    fechaHasta, zona, mercaderia);
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                Zonas zo = zonas.stream()
                        .filter(z -> z.equals(zona))
                        .findAny()
                        .orElse(null);
                Mercaderias merca = mercaderias.stream()
                        .filter(r -> r.equals(mercaderia))
                        .findAny()
                        .orElse(null);
                rep.reporteMovimientosMercaderias(fechaDesde, fechaHasta, zo, merca, usuarioImpresion);
            } else {
                String sql = movimientosMercaderiasFacade.generateSelectMostrar();
                String[] columnas = new String[]{ 
                    "codigo",
                    "inicaja",
                    "vtacaja",
                    "iniuni",
                    "vtauni",
                    "ndocum",
                    "xdesc_docum",
                    "xnombre",
                    "impuestos",
                    "relacion",
                    "igravadas",
                    "iexentas",
                    "impuestos",
                    "sal_stock",
                    "sal_gs",
                    "ppp_uni",
                    "ctipo_docum"
                };
                List<Object[]> lista = movimientosMercaderiasFacade.listarParaExcel(stmt, columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rmercamov");
            }
            
       } catch (SQLException ex) {
            Logger.getLogger(LiClientesNoCompranBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean entradasValidas() {
        if (fechaDesde == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Debe ingresar fecha desde.",
                            "Debe ingresar fecha desde."));
            return false;
        } else if (fechaHasta == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Debe ingresar fecha hasta.",
                                "Debe ingresar fecha hasta."));
                return false;
        } else if (mercaderia == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Debe seleccionar una mercaderia.",
                                "Debe seleccionar una mercaderia."));
                return false;
        }
        return true;
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

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public List<Zonas> getZonas() {
        return zonas;
    }

    public void setZonas(List<Zonas> zonas) {
        this.zonas = zonas;
    }

    public Mercaderias getMercaderia() {
        return mercaderia;
    }

    public void setMercaderia(Mercaderias mercaderia) {
        this.mercaderia = mercaderia;
    }

    public List<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(List<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }
}
