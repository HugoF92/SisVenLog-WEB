package bean.listados;

import bean.LoginBean;
import dao.ExcelFacade;
import dao.ClientesFacade;
import dao.LiChequesFacade;
import entidad.Bancos;
import entidad.Clientes;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import static util.DateUtil.dateToString;
import util.ExceptionHandlerView;
import util.LlamarReportes;

@ManagedBean
@javax.enterprise.context.RequestScoped
public class LiChequesBean {

    @EJB
    private ExcelFacade excelFacade;

    @EJB
    private ClientesFacade clientesFacade;
    
    @EJB
    private LiChequesFacade liChequesFacade;

    private Bancos bancos;
    private List<Bancos> listaBancos;

    private Clientes cliente;
    private List<Clientes> listaClientes;
    private Integer codCliente;
    private String nombreCliente;
    private Boolean todosCliente;

    private Zonas zona;
    private List<Zonas> listaZonas;

    private Date chequeDesde;
    private Date chequeHasta;

    private Date emisionDesde;
    private Date emisionHasta;

    private Date cobroDesde;
    private Date cobroHasta;

    //Selecciones
    private String discriminado;
    private String tipo;
    private String cobrado;

    private String contenidoError;
    private String tituloError;

    public LiChequesBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.bancos = new Bancos();
        this.listaBancos = new ArrayList<Bancos>();

        this.cliente = new Clientes();
        this.listaClientes = new ArrayList<Clientes>();
        this.todosCliente = true;
        //this.cliente = new Clientes();

        this.zona = new Zonas(new ZonasPK());
        this.listaZonas = new ArrayList<Zonas>();

        this.chequeDesde = new Date();
        this.chequeHasta = new Date();

        this.emisionDesde = new Date();
        this.emisionHasta = new Date();

        this.cobroDesde = new Date();
        this.cobroHasta = new Date();

        setDiscriminado("N"); // (N) No discriminar, (B) Por Banco, (R) Con Recibo
        setTipo("T");         // (T) Todos, (D) Diferidos, (C) Cobro Credito, (A) Al Dia
        setCobrado("T");      // (T) Todos, (C) Cobrados, (N) No cobrados

    }

    public void inicializarBuscadorClientes() {
        listaClientes = new ArrayList<>();
        setCliente(new Clientes());
        //setFiltro("");
        listarClientesBuscador();
    }

    public void listarClientesBuscador() {
        try {
            if(todosCliente || (codCliente == null && nombreCliente.isEmpty())){
                //traer todos los clientes
                listaClientes = clientesFacade.findAll();
            }else{
                if(codCliente == null){
                    listaClientes = clientesFacade.buscarPorNombre(getNombreCliente());
                }else{
                    listaClientes = clientesFacade.buscarPorCodigoNombre(getCodCliente(), getNombreCliente());
                }
            }
           
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error en la lectura de datos de clientes.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
        }

    }
    
     public void onRowClientesSelect(SelectEvent event) {
        if (getCliente() != null) {
            if (getCliente().getXnombre() != null) {
                setCodCliente(getCliente().getCodCliente());
                setNombreCliente(getCliente().getXnombre());
                System.out.println("Log nombreCliente por ajax: " + getCliente().getXnombre());
                PrimeFaces.current().executeScript("PF('dlgBusClieConsultaFactura').hide();");
            }
        }
    }
    
    public void ejecutar(String operacion){
        try {
            LlamarReportes rep = new LlamarReportes();

            String sql = liChequesFacade.crearSQL(getDiscriminado(), getTipo(), getCobrado(), getBancos(), dateToString(getCobroDesde()),
                    dateToString(getCobroHasta()), dateToString(getEmisionDesde()), dateToString(getEmisionHasta()), dateToString(getChequeDesde()),
                    dateToString(getChequeHasta()), getZona(), getTodosCliente(), getCliente().getXnombre(), getCliente().getCodCliente());
            System.out.println("SQL: "+sql);
            if (operacion.equals("ARCH")) {
                List<Object[]> resultado = liChequesFacade.ejecutarSql(sql);
                
                String[] columnas = {"nro_cheque", "xcuenta_bco", "fcheque", "icheque", "cod_cliente", "xnombre",
                                        "cod_banco2", "xdesc2_banco", "frechazo", "fcobro", "femision", "xtitular", "mtipo", "cod_zona"};

                rep.exportarExcel(columnas, resultado, "licheques");
            } else {
                String nomCli;
                if(todosCliente){
                    nomCli = "TODOS";
                }else{
                    nomCli = getCliente().getXnombre();
                }
                
                rep.reporteLiCheques(sql, dateToString(getChequeDesde()), dateToString(getChequeHasta()), getBancos(),LoginBean.user ,nomCli, operacion,
                                     dateToString(getEmisionDesde()), dateToString(getEmisionHasta()), 
                                     dateToString(getCobroDesde()), dateToString(getCobroHasta()),
                                     getZona(),getTipo());
                //rep.reporteLiCheques2(sql, dateToString(getChequeDesde()), dateToString(getChequeHasta()), getBancos().getXdesc(), nomCli, operacion);
            }
        } catch (Exception e) {
            PrimeFaces.current().ajax().update("exceptionDialog");
            contenidoError = ExceptionHandlerView.getStackTrace(e);
            tituloError = "Error al procesar datos.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, contenidoError, tituloError));
            PrimeFaces.current().executeScript("PF('exceptionDialog').show();");
        }
    }

    public ExcelFacade getExcelFacade() {
        return excelFacade;
    }

    public Bancos getBancos() {
        return bancos;
    }

    public List<Bancos> getListaBancos() {
        return listaBancos;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public Zonas getZona() {
        return zona;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public Date getChequeDesde() {
        return chequeDesde;
    }

    public Date getChequeHasta() {
        return chequeHasta;
    }

    public Date getEmisionDesde() {
        return emisionDesde;
    }

    public Date getEmisionHasta() {
        return emisionHasta;
    }

    public Date getCobroDesde() {
        return cobroDesde;
    }

    public Date getCobroHasta() {
        return cobroHasta;
    }

    public String getDiscriminado() {
        return discriminado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCobrado() {
        return cobrado;
    }

    public Boolean getTodosCliente() {
        return todosCliente;
    }

    public String getContenidoError() {
        return contenidoError;
    }

    public String getTituloError() {
        return tituloError;
    }

    public void setExcelFacade(ExcelFacade excelFacade) {
        this.excelFacade = excelFacade;
    }

    public void setBancos(Bancos bancos) {
        this.bancos = bancos;
    }

    public void setListaBancos(List<Bancos> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public void setTodosCliente(Boolean todosCliente) {
        this.todosCliente = todosCliente;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public void setChequeDesde(Date chequeDesde) {
        this.chequeDesde = chequeDesde;
    }

    public void setChequeHasta(Date chequeHasta) {
        this.chequeHasta = chequeHasta;
    }

    public void setEmisionDesde(Date emisionDesde) {
        this.emisionDesde = emisionDesde;
    }

    public void setEmisionHasta(Date emisionHasta) {
        this.emisionHasta = emisionHasta;
    }

    public void setCobroDesde(Date cobroDesde) {
        this.cobroDesde = cobroDesde;
    }

    public void setCobroHasta(Date cobroHasta) {
        this.cobroHasta = cobroHasta;
    }

    public void setDiscriminado(String discriminado) {
        this.discriminado = discriminado;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCobrado(String cobrado) {
        this.cobrado = cobrado;
    }

    public void setContenidoError(String contenidoError) {
        this.contenidoError = contenidoError;
    }

    public void setTituloError(String tituloError) {
        this.tituloError = tituloError;
    }

}
