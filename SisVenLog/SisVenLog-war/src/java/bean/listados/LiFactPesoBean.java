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
import dao.SublineasFacade;
import dao.TiposDocumentosFacade;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import util.ExceptionHandlerView;
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
    private SublineasFacade sublineasFacade;
    
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
    
    //para manejo de errores
    private String contenidoError;
    private String tituloError;
    
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
        
        this.mercaSource = new ArrayList<Mercaderias>();
        this.mercaTarget = new ArrayList<Mercaderias>();
        
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
    
    public List<Sublineas> listarSublineas() {
        listaSublineas = sublineasFacade.findAll();
        return listaSublineas;
    }
    
    public void ejecutar(String operacion){//agregar controles de si algun litado no se selecciona o linea, sublinea, vendedor, promocion, tipo de documento o la mercaderia target
        try {
            String usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();

            String sql = liFactPesoFacade.crearSql(getLineas(), getSublineas(), getPromocion(), getTiposDocumentos(),
                    getVendedor(), getClientes(), getFechaDesde(), getFechaHasta(), getTodosCliente(), getMercaderias().getTarget());

            LlamarReportes rep = new LlamarReportes();

            if (operacion.equals("ARCH")) {
                List<Object[]> resultado = liFactPesoFacade.ejecutarSql(sql);
                
                String[] columnas = {"ctipo_docum", "nrofact", "ffactur", "cod_cliente", "x_nombre", "cod_merca", "xdesc", "cant_cajas", "cant_unid", "tpeso_cajas", "tpeso_unid", "itotal", "cod_vendedor", "xvendedor", "nro_promo", "nfactura", "pdesc"};

                rep.exportarExcel(columnas, resultado, "lifactpeso");
            } else {
                rep.reporteLiFactPeso(sql, dateToString(getFechaDesde()), dateToString(getFechaHasta()), usuario, getVendedor().getXnombre(), getLineas().getXdesc(), getSublineas().getXdesc(), operacion);
            }
        } catch (Exception e) {
            RequestContext.getCurrentInstance().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al procesar datos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));
            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
        }
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

    public String getContenidoError() {
        return contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public void setLineas(Lineas lineas) {
        for(int i=0; i < getListaLineas().size(); i++){
            if(getListaLineas().get(i).getCodLinea() == lineas.getCodLinea()){
                this.lineas = getListaLineas().get(i);
                break;
            }
        }
    }

    public void setListaLineas(List<Lineas> listaLineas) {
        this.listaLineas = listaLineas;
    }

    public void setSublineas(Sublineas sublineas) {
        for(int i=0; i < getListaSublineas().size(); i++){
            if(getListaSublineas().get(i).getCodSublinea() == sublineas.getCodSublinea()){
                this.sublineas = getListaSublineas().get(i);
                break;
            }
        }
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
         for(int i=0; i < getListaVendedores().size(); i++){
            if(getListaVendedores().get(i).getEmpleadosPK().getCodEmpleado() == vendedor.getEmpleadosPK().getCodEmpleado()){
                this.vendedor = getListaVendedores().get(i);
                break;
            }
        }
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

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }
    
}
