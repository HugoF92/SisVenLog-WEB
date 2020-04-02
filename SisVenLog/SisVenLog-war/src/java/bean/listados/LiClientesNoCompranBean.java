package bean.listados;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import dao.ClientesNoCompranFacade;
import dao.EmpleadosFacade;
import dao.LineasFacade;
import dao.MercaderiasFacade;
import dao.RutasFacade;
import dao.SublineasFacade;
import dao.ZonasFacade;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Mercaderias;
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
public class LiClientesNoCompranBean {

    private Date fechaDesde;

    private Date fechaHasta;

    private Zonas zona;

    private List<Zonas> zonas;

    private Rutas ruta;

    private List<Rutas> rutas;

    private String estado;

    private Lineas linea;

    private List<Lineas> lineas;

    private Empleados vendedor;

    private List<Empleados> vendedores;

    private DualListModel<Mercaderias> mercaderias;
    private List<Mercaderias> mercaderiasActivas;

    private DualListModel<Sublineas> sublineas;
    private List<Sublineas> sublineasActivas;

    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private RutasFacade rutasFacade;

    @EJB
    private LineasFacade lineasFacade;

    @EJB
    private EmpleadosFacade empleadosFacade;

    @EJB
    private ClientesNoCompranFacade clientesNoCompranFacade;

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private SublineasFacade sublineasFacade;

    @PostConstruct
    public void instanciar() {
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        this.zona = null;
        this.ruta = null;
        this.estado = "T";
        this.zonas = this.zonasFacade.findAll();
        this.rutas = this.rutasFacade.findAll();
        this.lineas = this.lineasFacade.listarLineasClientesNoCompran();
        this.vendedores = this.empleadosFacade.getEmpleadosVendedoresActivosPorCodEmp(2);
        mercaderiasActivas = mercaderiasFacade.listarMercaderiasActivas();
        mercaderias = new DualListModel<>(mercaderiasActivas, new ArrayList<>());
        sublineasActivas = sublineasFacade.listarSublineasActivas();
        sublineas = new DualListModel<>(sublineasActivas, new ArrayList<>());
    }

    public void ejecutar(String tipo) {
        try {
            if(!entradasValidas()){
                return;
            }
            LlamarReportes rep = new LlamarReportes();
            Connection conexion = rep.conexion;
            Statement stmt = conexion.createStatement();
            // Insertamos las mercaderias seleccionadas en la tabla tmp_mercaderias
            clientesNoCompranFacade.insertarMercaderiasSeleccionadas(stmt,
                    mercaderias.getTarget(), mercaderiasActivas);
            clientesNoCompranFacade.insertarSublineasSeleccionadas(stmt,
                    sublineas.getTarget(), sublineasActivas);
            //Generamos la tabla temporal mostrar
            clientesNoCompranFacade.generateTableMostrar(stmt, fechaDesde,
                    fechaHasta, zona, ruta, estado, linea, vendedor);
            if (tipo.equals("VIST")) {
                String usuarioImpresion = "";
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuario")) {
                    usuarioImpresion = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                Zonas zo = zonas.stream()
                        .filter(z -> z.equals(zona))
                        .findAny()
                        .orElse(null);
                Rutas ru = rutas.stream()
                        .filter(r -> r.equals(ruta))
                        .findAny()
                        .orElse(null);
                Lineas li = lineas.stream()
                        .filter(l -> l.equals(linea))
                        .findAny()
                        .orElse(null);
                Empleados vend = vendedores.stream()
                        .filter(v -> v.equals(vendedor))
                        .findAny()
                        .orElse(null);
                String e = "TODOS";
                if(estado.equals("A")){
                    e = "Activos";
                } else if(estado.equals("I")) {
                    e = "Inactivos";
                }
                rep.reporteClienteNoCompran(fechaDesde, fechaHasta, zo, ru, e,
                        li, vend, usuarioImpresion);
            } else {
                String sql = clientesNoCompranFacade.generateSelectMostrar();
                String[] columnas = new String[]{ 
                    "cod_zona",
                    "xdesc_zona",
                    "cod_cliente",
                    "xnombre",
                    "xdirec",
                    "xtelef",
                    "cod_estado"
                };
                List<Object[]> lista = clientesNoCompranFacade.listarParaExcel(stmt, columnas, sql);
                conexion.close();
                rep.exportarExcel(columnas, lista, "rclisinvta");
            }
            
       } catch (SQLException ex) {
            Logger.getLogger(LiClientesNoCompranBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean entradasValidas() {
        if (fechaDesde == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Debe ingresar fecha facturacion desde.",
                            "Debe ingresar fecha facturacion desde."));
            return false;
        } else if (fechaHasta == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Debe ingresar fecha facturacion hasta.",
                                "Debe ingresar fecha facturacion hasta."));
                return false;
        } else if (sublineas.getTarget().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Debe ingresar sublineas.",
                            "Debe ingresar sublineas."));
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

    public Rutas getRuta() {
        return ruta;
    }

    public void setRuta(Rutas ruta) {
        this.ruta = ruta;
    }

    public List<Rutas> getRutas() {
        return rutas;
    }

    public void setRutas(List<Rutas> rutas) {
        this.rutas = rutas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Empleados getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
    }

    public List<Empleados> getVendedores() {
        return vendedores;
    }

    public void setVendedores(List<Empleados> vendedores) {
        this.vendedores = vendedores;
    }

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }

    public DualListModel<Sublineas> getSublineas() {
        return sublineas;
    }

    public void setSublineas(DualListModel<Sublineas> sublineas) {
        this.sublineas = sublineas;
    }

}
