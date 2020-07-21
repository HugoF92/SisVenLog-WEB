package bean.listados;

import dao.DepositosFacade;
import dao.DivisionesFacade;
import dao.MercaderiasFacade;
import dao.StockValorizadoFacade;
import dao.SublineasFacade;
import entidad.Depositos;
import entidad.Divisiones;
import entidad.Mercaderias;
import entidad.Sublineas;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import util.LlamarReportes;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Arsenio
 */
@ManagedBean
@SessionScoped
public class LiStockValBean {

    private Date fechaHasta;

    private Depositos deposito;

    private List<Depositos> depositos;

    private Sublineas sublinea;

    private List<Sublineas> sublineas;

    private Divisiones division;

    private List<Divisiones> divisiones;
    
    private String discriminar;
    
    private Boolean conPrecioCosto;
    
    private Boolean sinIVA;

    private DualListModel<Mercaderias> mercaderias;
    private List<Mercaderias> mercaderiasActivas;

    @EJB
    private DepositosFacade depositosFacade;

    @EJB
    private SublineasFacade sublineasFacade;

    @EJB
    private DivisionesFacade divisionesFacade;

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private StockValorizadoFacade stockValorizadoFacade;

    @PostConstruct
    public void instanciar(){
        this.fechaHasta = new Date();
        this.discriminar = "PM";
        this.deposito = null;
        this.depositos = this.depositosFacade.obtenerDepositosByTipo("F");
        this.sublinea = null;
        this.sublineas = this.sublineasFacade.listarSublineasActivas();
        this.division = null;
        this.divisiones = this.divisionesFacade.listarDivisionesActivas();
        mercaderiasActivas = mercaderiasFacade.listarMercaderiasActivas();
        mercaderias = new DualListModel<>(mercaderiasActivas, new ArrayList<>());
    }

    public void ejecutar(String tipo) {
        try {
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            stockValorizadoFacade.insertarMercaderiasSeleccionadas(conexion.createStatement(),
                    mercaderias.getTarget(), mercaderiasActivas);
            stockValorizadoFacade.generateTableMostrar(conexion, fechaHasta,
                    deposito, sublinea, division, discriminar, conPrecioCosto,
                    sinIVA);
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                Depositos dep = depositos.stream()
                        .filter(d -> (d.getDepositosPK().getCodDepo() == 
                                (deposito != null? deposito.getDepositosPK().getCodDepo():null)))
                        .findAny()
                        .orElse(null);
                Sublineas sub = sublineas.stream()
                        .filter(s -> (s.getCodSublinea() == 
                                (sublinea != null? sublinea.getCodSublinea():-1)))
                        .findAny()
                        .orElse(null);
                Divisiones div = divisiones.stream()
                        .filter(d -> (d.getCodDivision() == 
                                (division != null? division.getCodDivision():-1)))
                        .findAny()
                        .orElse(null);
                rep.reporteStockValorizado(fechaHasta, dep, sub,
                        div, discriminar, conPrecioCosto, sinIVA, usuarioImpresion);
            } else {
                String sql = stockValorizadoFacade.generateSelectMostrar();
                String[] columnas = new String[]{
                    "cod_merca",
                    "xdesc",
                    "cant_cajas",
                    "cant_unid",
                    "iprecio_caja",
                    "iprecio_unid",
                    "tgrav_10",
                    "tgrav_5",
                    "timpu_10",
                    "timpu_5",
                    "texe",
                    "ttotal"
                };
                List<Object[]> lista = stockValorizadoFacade.listarParaExcel(conexion.createStatement(), columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rrotainv");
            }
            } catch (SQLException ex) {
            Logger.getLogger(LiRotaInvBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public Divisiones getDivision() {
        return division;
    }

    public void setDivision(Divisiones division) {
        this.division = division;
    }

    public List<Divisiones> getDivisiones() {
        return divisiones;
    }

    public void setDivisiones(List<Divisiones> divisiones) {
        this.divisiones = divisiones;
    }

    public String getDiscriminar() {
        return discriminar;
    }

    public void setDiscriminar(String discriminar) {
        this.discriminar = discriminar;
    }

    public Boolean getConPrecioCosto() {
        return conPrecioCosto;
    }

    public void setConPrecioCosto(Boolean conPrecioCosto) {
        this.conPrecioCosto = conPrecioCosto;
    }
    
    public Boolean getSinIVA() {
        return sinIVA;
    }

    public void setSinIVA(Boolean sinIVA) {
        this.sinIVA = sinIVA;
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
