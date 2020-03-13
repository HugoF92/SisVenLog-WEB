package bean.listados;

import dao.ExcelFacade;
import dao.LiVtaGravFacade;
import dao.MercaderiasFacade;
import dao.SublineasFacade;
import entidad.Mercaderias;
import entidad.Sublineas;
import entidad.TiposVentas;
import entidad.Zonas;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;
import org.primefaces.PrimeFaces;
import static util.DateUtil.dateToString;
import util.ExceptionHandlerView;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class LiVtaGravBean {

    @EJB
    private LiVtaGravFacade liVtaGravFacade;
    
    @EJB
    private SublineasFacade sublineasFacade;
    
    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Date fechaDesde;
    private Date fechaHasta;

    private Sublineas sublineas;
    private List<Sublineas> listaSublineas;

    private Zonas zonas;
    private List<Zonas> listaZonas;

    private TiposVentas tiposVentas;
    private List<TiposVentas> listaTiposVentas;
    private String tipo;
        
    private List<Mercaderias> mercaSource;
    private List<Mercaderias> mercaTarget;
    private DualListModel<Mercaderias> mercaderias;
    
    private String contenidoError;
    private String tituloError;

    public LiVtaGravBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        
        this.sublineas = new Sublineas();
        this.listaSublineas = new ArrayList<>();
        
        this.zonas = new Zonas();
        this.listaZonas = new ArrayList<>();
        
        this.tiposVentas = new TiposVentas();
        this.listaTiposVentas = new ArrayList<>();
        
        this.tipo = "";
        
        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();
        mercaTarget = new ArrayList<Mercaderias>();

        mercaderias = new DualListModel<>(mercaSource, mercaTarget);

    }

    public List<Sublineas> listarSubLineas() {
        List<Sublineas> sublineasVar = sublineasFacade.listarSublineasConMercaderias();
        setListaSublineas(sublineasVar);
        return getListaSublineas();
    }

    public void ejecutar(String operacion) {
        try {
            String sql = liVtaGravFacade.crearSQL(getSublineas(), getTiposVentas(), getZonas(),
                    getFechaDesde(), getFechaHasta(), getMercaderias().getTarget(), getTipo());

            LlamarReportes rep = new LlamarReportes();

            if (operacion.equals("ARCH")) {
                List<Object[]> resultado = liVtaGravFacade.ejecutarSql(sql);

                String[] columnas = {"nmes", "nanno", "ctipo_vta", "xdesc_vta", "cod_merca", "xdesc", "igravadas"};

                rep.exportarExcel(columnas, resultado, "livtagrav");
            } else {
                rep.reporteLiVtaGrav(sql, dateToString(getFechaDesde()), dateToString(getFechaHasta()), getZonas().getXdesc(), getTiposVentas().getXdesc(), operacion);
            }
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en Lectura de Datos de Facturas.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
        }
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public Sublineas getSublineas() {
        return sublineas;
    }

    public List<Sublineas> getListaSublineas() {
        return listaSublineas;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public TiposVentas getTiposVentas() {
        return tiposVentas;
    }

    public List<TiposVentas> getListaTiposVentas() {
        return listaTiposVentas;
    }

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public String getTipo() {
        return tipo;
    }

    public String getContenidoError() {
        return contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public void setSublineas(Sublineas sublineas) {
        this.sublineas = sublineas;
    }

    public void setListaSublineas(List<Sublineas> listaSublineas) {
        this.listaSublineas = listaSublineas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public void setTiposVentas(TiposVentas tiposVentas) {
        this.tiposVentas = tiposVentas;
    }

    public void setListaTiposVentas(List<TiposVentas> listaTiposVentas) {
        this.listaTiposVentas = listaTiposVentas;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }

}
