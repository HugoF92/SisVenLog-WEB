/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.LiFactPesoFacade;
import dao.LineasFacade;
import dao.MercaderiasFacade;
import dao.PromocionesFacade;
import dao.TiposDocumentosFacade;
import dto.ListaStringDto;
import entidad.Clientes;
import entidad.Empleados;
import entidad.Lineas;
import entidad.Mercaderias;
import entidad.Promociones;
import entidad.Sublineas;
import entidad.TiposDocumentos;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;
import util.LlamarReportes;

/**
 *
 * @author Asus
 */
@ManagedBean
@SessionScoped
public class LiFactPesoBean {
    @EJB
    private LiFactPesoFacade liFactPesoFacade;
    
    @EJB
    private LineasFacade lineasFacade;
    
    @EJB
    private EmpleadosFacade empleadosFacade;
    
    @EJB
    private TiposDocumentosFacade tiposdocumentosFacade;
    
    @EJB
    private PromocionesFacade promocionesFacade;
    
    @EJB
    private MercaderiasFacade mercaderiasFacade;
    
    private Lineas lineas;
    private List<Lineas> listaLineas;
    
    private Sublineas sublineas;
    private List<Sublineas> listaSublineas;
    
    private TiposDocumentos tiposDocumentos;
    private List<TiposDocumentos> listaTiposDocumentos;
    
    private Promociones promocion;
    private List<Promociones> listaPromociones;
    
    private Empleados vendedor;
    private List<Empleados> listaVendedores;
    
    private Clientes clientes;
    private List<Clientes> listaClientes;
    private Boolean todosCliente;
    
    private Date fechaDesde;
    private Date fechaHasta;
    
    private List<Mercaderias> mercaSource;
    private List<Mercaderias> mercaTarget;
    private DualListModel<Mercaderias> mercaderias;
    
    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.lineas = new Lineas();
        this.listaLineas = new ArrayList<>();

        this.sublineas = new Sublineas();
        this.listaSublineas = new ArrayList<>();
        
        this.tiposDocumentos = new TiposDocumentos();
        this.listaTiposDocumentos = new ArrayList<>();
        
        this.promocion = new Promociones();
        this.listaPromociones = new ArrayList<>();
        
        this.vendedor = new Empleados();
        this.listaVendedores = new ArrayList<>();
        
        this.clientes = new Clientes();
        this.listaClientes = new ArrayList<>();
        this.todosCliente = true;
        
        this.fechaDesde = new Date();
        this.fechaHasta = new Date();
        
        this.mercaSource = new ArrayList<>();
        this.mercaTarget = new ArrayList<>();
        
        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();

        mercaderias = new DualListModel<>(mercaSource, mercaTarget);

    }

    public List<Promociones> listarPromociones() {
        List<Promociones> promocionesVar = promocionesFacade.findAllOrderXDesc();
        setListaPromociones(promocionesVar);
        return getListaPromociones();
    }
    
    public List<TiposDocumentos> listarTipoDocumento() {
        List<TiposDocumentos> tiposDocumentosVar = tiposdocumentosFacade.listarTipoDocumentoKilajeEnFactura();
        setListaTiposDocumentos(tiposDocumentosVar);
        return getListaTiposDocumentos();
    }
    
    public List<Empleados> listarEmpleadosActivos() {
        List<Empleados> vendedoresVar = empleadosFacade.listarEmpleadosActivos();
        setListaVendedores(vendedoresVar);
        return getListaVendedores();
    }
    
    public List<Lineas> listarLineas() {
        List<Lineas> lineasVar = lineasFacade.listarLineas();
        setListaLineas(lineasVar);
        return getListaLineas();
    }
    
    public void ejecutar(String operacion){
        
        String usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
        
        ListaStringDto resultado = liFactPesoFacade.ejecutar(getLineas(), getSublineas(), getPromocion(), getTiposDocumentos(),
                getVendedor(), getClientes(), getFechaDesde(), getFechaHasta(), getTodosCliente(), getMercaderias().getTarget());
        
        LlamarReportes rep = new LlamarReportes();

        if (operacion.equals("ARCH")){            
            String[] columnas = {"ctipo_docum","nrofact","ffactur","cod_cliente","x_nombre","cod_merca","xdesc","cant_cajas","cant_unid","tpeso_cajas","tpeso_unid","itotal","cod_vendedor","xvendedor","nro_promo","nfactura","pdesc"};

            rep.exportarExcel(columnas, resultado.getLista(), "lifactpeso");
        }else rep.reporteLiFactPeso(resultado.getSql(),  dateToString(getFechaDesde()), dateToString(getFechaHasta()), usuario, getVendedor().getXnombre(), getLineas().getXdesc(), getSublineas().getXdesc(), operacion);
    }
    
    private String dateToString(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }
            
    public Lineas getLineas() {
        return lineas;
    }

    public List<Lineas> getListaLineas() {
        return listaLineas;
    }

    public Sublineas getSublineas() {
        return sublineas;
    }

    public List<Sublineas> getListaSublineas() {
        return listaSublineas;
    }

    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public List<TiposDocumentos> getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public Promociones getPromocion() {
        return promocion;
    }

    public List<Promociones> getListaPromociones() {
        return listaPromociones;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }
    
    public Empleados getVendedor() {
        return vendedor;
    }
    
    public List<Empleados> getListaVendedores() {
        return listaVendedores;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public Boolean getTodosCliente() {
        return todosCliente;
    }

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setLineas(Lineas lineas) {
        this.lineas = lineas;
    }

    public void setListaLineas(List<Lineas> listaLineas) {
        this.listaLineas = listaLineas;
    }

    public void setSublineas(Sublineas sublineas) {
        this.sublineas = sublineas;
    }

    public void setListaSublineas(List<Sublineas> listaSublineas) {
        this.listaSublineas = listaSublineas;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

    public void setPromocion(Promociones promocion) {
        this.promocion = promocion;
    }

    public void setListaPromociones(List<Promociones> listaPromociones) {
        this.listaPromociones = listaPromociones;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public void setListaVendedores(List<Empleados> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }
    
    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public void setTodosCliente(Boolean todosCliente) {
        this.todosCliente = todosCliente;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }
    
}
