package bean;

import dao.BancosFacade;
import dao.CiudadesFacade;
import dao.ClientesFacade;
import dao.RutasFacade;
import dao.TiposClientesFacade;
import dao.CanalesVentaFacade;
import dao.TiposVentasFacade;
import dao.TablaCuentasBancarias;
import entidad.Bancos;
import entidad.CanalesVenta;
import entidad.Ciudades;
import entidad.Clientes;
import entidad.Rutas;
import entidad.TiposClientes;
import entidad.TiposVentas;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import dao.TablaCuentasBancarias;
import dao.ClientesCtasBancariasFacade;
import dao.ClientesDocumentosFacade;
import dao.ClientesTipoVentasFacade;
import dao.DocumentosReqCondicionesFacade;
import dao.DocumentosRequeridosFacade;
import dao.VentasClientesFacade;
import entidad.ClientesDocumentos;
import entidad.ClientesDocumentosPK;
import entidad.DocumentosReqCondiciones;
import entidad.DocumentosRequeridos;
import entidad.VentasClientes;
import entidad.VentasClientesPK;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import javax.annotation.Resource;
import javax.faces.model.SelectItem;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

@ManagedBean
@SessionScoped
public class ClientesBean implements Serializable {

    /**
     * @return the clientesDocumentos
     */
    public List<ClientesDocumentos> getClientesDocumentos() {
        return clientesDocumentos;
    }

    /**
     * @param clientesDocumentos the clientesDocumentos to set
     */
    public void setClientesDocumentos(List<ClientesDocumentos> clientesDocumentos) {
        this.clientesDocumentos = clientesDocumentos;

    }

    @Resource
    private UserTransaction userTransaction;

    @EJB
    private ClientesFacade clientesFacade;

    @EJB
    private TiposClientesFacade tipoCliFacade;

    @EJB
    private CiudadesFacade ciudadFacade;

    @EJB
    private RutasFacade rutasFacade;

    @EJB
    private CanalesVentaFacade canalesVentaFacade;

    @EJB
    private TiposVentasFacade tiposVentasFacade;

    @EJB
    private BancosFacade bancosFacade;

    @EJB
    private ClientesDocumentosFacade clientesDocumentosFacade;

    @EJB
    private ClientesCtasBancariasFacade clientesCtasBancariasFacade;

    @EJB
    private ClientesTipoVentasFacade clientesTipoVentasFacade;

    @EJB
    private VentasClientesFacade ventasClientesFacade;

    @EJB
    private DocumentosRequeridosFacade documentosRequeridosFacade;

    @EJB
    private DocumentosReqCondicionesFacade documentosReqCondicionesFacade;

    private static final Logger LOGGER = Logger.getLogger(ClientesBean.class.getName());

    private Clientes clientes = new Clientes();

    private CanalesVenta canalesVenta = new CanalesVenta();
    @SuppressWarnings("Convert2Diamond")
    private List<Clientes> listaClientes = new ArrayList<Clientes>();
    @SuppressWarnings("Convert2Diamond")
    private List<TiposClientes> listaDeTipoCliente = new ArrayList<TiposClientes>();
    @SuppressWarnings("Convert2Diamond")
    private List<Ciudades> listaCiudades = new ArrayList<Ciudades>();
    @SuppressWarnings("Convert2Diamond")
    private List<Rutas> listaRutas = new ArrayList<Rutas>();
    @SuppressWarnings("Convert2Diamond")
    private List<CanalesVenta> listaCanalesVenta = new ArrayList<CanalesVenta>();

    @SuppressWarnings("Convert2Diamond")
    private List<dao.TablaClientesDocumentos> tablaClientesDocumentos = new ArrayList<dao.TablaClientesDocumentos>();

    private List<ClientesDocumentos> clientesDocumentos;

    private ClientesDocumentos clienteDocumentoSelected;

//    Dias a elegist
    private boolean lunes;
    private boolean martes;
    private boolean miercoles;
    private boolean jueves;
    private boolean viernes;
    private boolean sabado;

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    private boolean renderedBtnAgregar;
    private boolean renderedBtnEdit;
    private boolean renderedBtnBorrar;

    private int i;
    private boolean botonDatosComercialesEnabled = false;
    private boolean hidden = false;

    private final int CONTADO = 1;
    private final int CREDITO = 2;
    private final int CHEQUE = 3;

    private int clienteDCFormaPago = 1;

    private String estadoClienteDescripcion = "";

    private Long limiteCompraDC = Long.valueOf("0");

    private Short plazoCreditoDC = Short.valueOf("0");

    private Short plazoImpresionDC = Short.valueOf("0");

    private Long limiteTemporalDC = Long.valueOf("0");

    private Date fechaLimiteTemporalDC = new Date();

    private boolean limiteCompraDisabled = false;  // with getter and setter

    private Short riesgoDC = Short.valueOf("0");

    private Double creditoDisponibleDC = Double.valueOf("0");

    private Double deudaPedidosPendientesDC = Double.valueOf("0");

    private ArrayList<TiposVentas> tiposVentas;

    private SelectItem[] tiposVentasVista;

    //private String[] tiposVentasString;
    private char[] tiposVentasSeleccion;

    private List<Bancos> listaBancos;
    private Short[] codigosBancosDC;
    private String[] descBancosDC;
    private String[] cuentaCorrienteDC;
    private Short codigoBancoSelectedDC;
    private String numeroCuentaInputDC;
    private List<TablaCuentasBancarias> tablaCuentasBancarias;

    private TablaCuentasBancarias cuentaBancariaSelected;

    private String clienteEstadoVista;

    public ClientesBean() {

    }

    public boolean isHabBtnEdit() {
        return habBtnEdit;
    }

    public void setHabBtnEdit(boolean habBtnEdit) {
        this.habBtnEdit = habBtnEdit;
    }

    public boolean isHabBtnAct() {
        return habBtnAct;
    }

    public void setHabBtnAct(boolean habBtnAct) {
        this.habBtnAct = habBtnAct;
    }

    public boolean isHabBtnInac() {
        return habBtnInac;
    }

    public void setHabBtnInac(boolean habBtnInac) {
        this.habBtnInac = habBtnInac;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public ClientesFacade getClientesFacade() {
        return clientesFacade;
    }

    public void setClientesFacade(ClientesFacade clientesFacade) {
        this.clientesFacade = clientesFacade;
    }

    public TiposClientesFacade getTipoCliFacade() {
        return tipoCliFacade;
    }

    public void setTipoCliFacade(TiposClientesFacade tipoCliFacade) {
        this.tipoCliFacade = tipoCliFacade;
    }

    public List<TiposClientes> getListaDeTipoCliente() {
        return listaDeTipoCliente;
    }

    public void setListaDeTipoCliente(List<TiposClientes> listaDeTipoCliente) {
        this.listaDeTipoCliente = listaDeTipoCliente;
    }

    public CiudadesFacade getCiudadFacade() {
        return ciudadFacade;
    }

    public void setCiudadFacade(CiudadesFacade ciudadFacade) {
        this.ciudadFacade = ciudadFacade;
    }

    public List<Ciudades> getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Rutas> getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(List<Rutas> listaRutas) {
        this.listaRutas = listaRutas;
    }

    public RutasFacade getRutasFacade() {
        return rutasFacade;
    }

    public void setRutasFacade(RutasFacade rutasFacade) {
        this.rutasFacade = rutasFacade;
    }

    public boolean isLunes() {
        return lunes;
    }

    public void setLunes(boolean lunes) {
        this.lunes = lunes;
    }

    public boolean isMartes() {
        return martes;
    }

    public void setMartes(boolean martes) {
        this.martes = martes;
    }

    public boolean isMiercoles() {
        return miercoles;
    }

    public void setMiercoles(boolean miercoles) {
        this.miercoles = miercoles;
    }

    public boolean isJueves() {
        return jueves;
    }

    public void setJueves(boolean jueves) {
        this.jueves = jueves;
    }

    public boolean isViernes() {
        return viernes;
    }

    public void setViernes(boolean viernes) {
        this.viernes = viernes;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    @PostConstruct
    @SuppressWarnings("Convert2Diamond")
    public void instanciar() {

        this.setRenderedBtnAgregar(true);
        this.setRenderedBtnEdit(true);
        this.setRenderedBtnBorrar(true);

        String parametro = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parametro") == null ? "" : FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parametro");
        LOGGER.log(Level.INFO, "parametro=" + parametro == null ? "null" : parametro);
        if (parametro.equalsIgnoreCase("D")) {

            this.setRenderedBtnAgregar(false);
            this.setRenderedBtnEdit(false);
            this.setRenderedBtnBorrar(false);
//        
        } else {
            this.setRenderedBtnAgregar(true);
            this.setRenderedBtnEdit(true);
            this.setRenderedBtnBorrar(true);
        }
        listaClientes = new ArrayList<Clientes>();
        this.clientes = new Clientes();
        this.clientes.setMtipoPersona('F');
        this.reloadDias();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);
        listar();
        listarTipoCliente();
        listarCiudades();
        listarRutas();
        listarCanalesVenta();

        clienteDCFormaPago = CONTADO;

        if (this.clientes.getCodEstado() == "I") {
            this.setEstadoClienteDescripcion("INACTIVO");
        } else if (this.clientes.getCodEstado() == "A") {
            this.setEstadoClienteDescripcion("ACTIVO");
        } else {
            this.setEstadoClienteDescripcion("SUSPENDIDO");
        }

        tiposVentas = new ArrayList<TiposVentas>(tiposVentasFacade.findAll());

        //tiposVentasString = new String[tiposVentas.size()];
        tiposVentasVista = new SelectItem[tiposVentas.size()];
        tiposVentasSeleccion = new char[tiposVentas.size()];
        Object[] objArr = tiposVentas.toArray();
        int i = 0;
        for (Object obj : objArr) {
            TiposVentas temp = (TiposVentas) obj;
            SelectItem elemento = new SelectItem(temp.getTiposVentasPK().getCtipoVta(), temp.getXdesc());
            tiposVentasVista[i++] = elemento;
        }

        listaBancos = bancosFacade.findAll();
        codigosBancosDC = new Short[listaBancos.size()];
        setDescBancosDC(new String[listaBancos.size()]);
        objArr = listaBancos.toArray();
        i = 0;
        for (Object obj : objArr) {
            Bancos temp = (Bancos) obj;
            codigosBancosDC[i] = temp.getCodBanco();
            getDescBancosDC()[i] = temp.getXdesc();
            i++;
        }

        FacesContext context = FacesContext.getCurrentInstance();
        String url = context.getExternalContext().getRequestContextPath() + "/maclientes.xhtml";
       // try {
            LOGGER.info(url);
            //context.responseComplete(); 
            //context.getExternalContext().redirect(url);
       // } catch (IOException ex) {
          //  ex.printStackTrace();
//            RequestContext.getCurrentInstance().update("exceptionDialog");
//            contenidoError = ExceptionHandlerView.getStackTrace(ex);
//            tituloError = "Error la redirección a la página manotascom.";
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, tituloError));            
//            RequestContext.getCurrentInstance().execute("PF('exceptionDialog').show();");
       // }

    }

    public void disableBtn() {
        this.setHabBtnAct(false);
        this.setHabBtnEdit(false);
    }

    public void enableBtn() {
        this.setRenderedBtnAgregar(true);
        this.setRenderedBtnBorrar(true);
    }

    public void deshabilitarBotonesMaclientes() {

        this.setRenderedBtnAgregar(false);
        this.setRenderedBtnEdit(false);
        this.setRenderedBtnBorrar(false);
        LOGGER.log(Level.INFO, "se esconden botones de mantenimiento cliente");

    }

    @SuppressWarnings("Convert2Diamond")
    public void nuevo() {
        this.clientes = new Clientes();
        listaClientes = new ArrayList<Clientes>();

    }

    public List<Clientes> listar() {
        listaClientes = clientesFacade.getListaClientesPorFechaAltaDesc();
        return listaClientes;
    }

    public void insertar() {
        short codEmpresa = 2;
        boolean error = false;
        if (this.clientes.getMtipoPersona().equals("F") && this.verificarExisteErrorPersonaFisica()) {
            error = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Requerido", "Si es Persona Física, campo cédula es requerido."));

        }
        if (this.clientes.getMtipoPersona().equals("J") && this.verificarExisteErrorPersonaJuridica()) {
            error = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Requerido", "Si es Persona Juridica, campo RUC es requerido."));

        }
        if (!error) {
            try {

                Integer maxCod = this.clientesFacade.getMaxId();
                clientes.setCodEmpr(codEmpresa);
                clientes.setMtipoPersona(clientes.getMtipoPersona());
                clientes.setCodCliente(maxCod + 1);
                clientes.setXnombre(clientes.getXnombre().toUpperCase());
                clientes.setXcedula(clientes.getXcedula());
                clientes.setXruc(clientes.getXruc().toUpperCase());
                clientes.setCtipoCliente(clientes.getCtipoCliente());
                clientes.setXdirec(clientes.getXdirec());
                clientes.setXcontacto(clientes.getXcontacto());
                clientes.setCodCiudad(clientes.getCodCiudad());
                clientes.setXtelef(clientes.getXtelef());
                clientes.setXfax(clientes.getXfax());
                clientes.setXemail(clientes.getXemail());
                clientes.setCodEstado("A");
                clientes.setCodRuta(clientes.getCodRuta());
                clientes.setXdiasVisita(concatenarDias());
                clientes.setXobs(clientes.getXobs());
                //espacion en blanco por defecto
                Character d = new Character(' ');
                clientes.setMformaPago(d);
                clientes.setFalta(new Date());
                clientesFacade.create(clientes);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));
                PrimeFaces.current().executeScript("PF('dlgNuevoCliente').hide();");
                instanciar();

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al Crear", e);
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
            }
        }

    }

    public void editar() {
        try {
            boolean error = false;
            if (this.clientes.getMtipoPersona() != null
                    && this.clientes.getMtipoPersona().equals('F')
                    && this.verificarExisteErrorPersonaFisica()) {
                error = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Requerido", "Si es Persona Física, campo Cédula es requerido."));

            }
            if (this.clientes.getMtipoPersona() != null
                    && this.clientes.getMtipoPersona().equals("J")
                    && this.verificarExisteErrorPersonaJuridica()) {
                error = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Requerido", "Si es Persona Juridica, campo RUC es requerido."));

            }
            if (clientes.getMtipoPersona() == null || clientes.getMtipoPersona().toString().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Verificar", "Tipo Persona se encuentra nulo."));
            }
            if (!error) {
                clientes.setXnombre(clientes.getXnombre().toUpperCase());
                clientes.setXcedula(clientes.getXcedula());
                clientes.setXruc(clientes.getXruc().toUpperCase());
                clientes.setCtipoCliente(clientes.getCtipoCliente());
                clientes.setXdirec(clientes.getXdirec());
                clientes.setXcontacto(clientes.getXcontacto());
                clientes.setCodCiudad(clientes.getCodCiudad());
                clientes.setXtelef(clientes.getXtelef());
                clientes.setXfax(clientes.getXfax());
                clientes.setXemail(clientes.getXemail());
                clientes.setCodEstado(clientes.getCodEstado());
                clientes.setCodRuta(clientes.getCodRuta());
                clientes.setXdiasVisita(concatenarDias());
                clientes.setXobs(clientes.getXobs());
                clientes.setFultimModif(new Date());
                clientesFacade.edit(clientes);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                reloadDias();

                PrimeFaces.current().executeScript("PF('dlgEditarCliente').hide();");

            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al Actualizar", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        String msg = "";
        try {
            msg = clientesFacade.remover(clientes);
            if (!msg.equals("Eliminado con exito")) {
                throw new Exception(msg);
            }
            this.clientes = new Clientes();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacCliente').hide();");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al borrar", e);
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void onRowSelect(SelectEvent event) {

        String diasSeleccionads = this.clientes.getXdiasVisita();
        char l = 'L';
        char t = 'T';
        char m = 'M';
        char j = 'J';
        char v = 'V';
        char s = 'S';

        for (char c : diasSeleccionads.toCharArray()) {
            if (c == l) {
                this.setLunes(true);
            }
            if (c == t) {
                this.setMartes(true);
            }
            if (c == m) {
                this.setMiercoles(true);
            }
            if (c == j) {
                this.setJueves(true);
            }
            if (c == v) {
                this.setViernes(true);
            }
            if (c == s) {
                this.setSabado(true);
            }
        }

        if (!"".equals(this.clientes.getXnombre())) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (clientes != null) {

            if (clientes.getXnombre() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardadCliente').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevoCliente').hide();");
        }

    }

    public void cerrarDialogosAgregar() {

        PrimeFaces.current().executeScript("PF('dlgSinGuardadCliente').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevoCliente').hide();");
    }

    public List<TiposClientes> listarTipoCliente() {
        listaDeTipoCliente = tipoCliFacade.findAll();
        return listaDeTipoCliente;
    }

    public List<Ciudades> listarCiudades() {
        listaCiudades = ciudadFacade.findAll();
        return listaCiudades;
    }

    public List<Rutas> listarRutas() {
        listaRutas = rutasFacade.findAll();
        return listaRutas;
    }

    public List<CanalesVenta> listarCanalesVenta() {
        setListaCanalesVenta(canalesVentaFacade.findAll());
        return getListaCanalesVenta();
    }

    public String concatenarDias() {
        String dias = "";
        if (lunes) {
            dias = dias + "L";
        }
        if (martes) {
            dias = dias + "T";
        }
        if (miercoles) {
            dias = dias + "M";
        }
        if (jueves) {
            dias = dias + "J";
        }
        if (viernes) {
            dias = dias + "V";
        }
        if (sabado) {
            dias = dias + "S";
        }

        return dias.trim();

    }

    public String estadoCliente(String estado) {
        String resultado = "";
        if (null == this.clientes.getCodEstado()) {
            resultado = ("SUSPENDIDO");
        } else {
            switch (this.clientes.getCodEstado().trim()) {
                case "I":
                    resultado = ("INACTIVO");
                    break;
                case "A":
                    resultado = ("ACTIVO");
                    break;
                default:
                    resultado = ("SUSPENDIDO");
                    break;
            }
        }
        return resultado;
    }

    public void reloadDias() {
        this.lunes = false;
        this.martes = false;
        this.miercoles = false;
        this.jueves = false;
        this.viernes = false;
        this.sabado = false;
    }

    public boolean verificarExisteErrorPersonaFisica() {
        if (this.clientes.getXcedula() == null) {
            return true;
        } else if (this.clientes.getXcedula() != null && this.clientes.getXcedula().intValue() == 0) {
            return true;
        }
        return false;
    }

    public boolean verificarExisteErrorPersonaJuridica() {
        if (this.clientes.getXruc() == null) {
            return true;
        } else if (this.clientes.getXruc() != null && this.clientes.getXruc().isEmpty()) {
            return true;
        }
        return false;
    }

    public void hideOrShow() {

        LOGGER.log(Level.INFO, "codigoCliente: " + clientes.getCodCliente() + ": hidden: " + hidden);

        if (!hidden) {

            hidden = true;
        } else {

            hidden = false;
        }

        getDeudaCliente();
        if (this.clientes.getFprimFact() == null) {

            this.clientes.setFprimFact(this.clientesFacade.getFechaPrimeraFactura(this.clientes.getCodCliente()));

        }
        //if (hidden) {
        if (true) {

            clientes = actualizarDatosBean(clientes);

            getDeudaCliente();

            setClientesDocumentos(getClientesDocumentos2(clientes));
            tablaCuentasBancarias = new ArrayList<TablaCuentasBancarias>();

            tablaCuentasBancarias = clientesCtasBancariasFacade.getCuentasBancarias(this.clientes); //return hidden;

            //LOGGER.log(Level.INFO, "codigo banco cliente: " + tablaCuentasBancarias.get(0).getCodigoBanco() + ":");
            //tablaCuentasBancarias.get(0).setDescBanco("itau");
            for (int i = 0; i < tablaCuentasBancarias.size(); i++) {

                for (int j = 0; j < codigosBancosDC.length; j++) {

                    LOGGER.log(Level.INFO, "codigo banco cliente: " + tablaCuentasBancarias.get(i).getCodigoBanco() + ":");
                    LOGGER.log(Level.INFO, "codigo banco dc: " + codigosBancosDC[j].intValue() + ":");

                    if (Integer.valueOf(tablaCuentasBancarias.get(i).getCodigoBanco()).compareTo(codigosBancosDC[j].intValue()) == 0) { // son el mismo banco

                        tablaCuentasBancarias.get(i).setDescBanco(descBancosDC[j]);
                        tablaCuentasBancarias.get(i).setDeletedProvisorio(false);
                        break;

                    }
                }

            }

            clientesDocumentos = getDocumentosPresentados(clientes);
            Logger.getLogger(ClientesBean.class.getName()).log(Level.INFO, null, "codCliente: " + clientes.getCodCliente());
            clienteEstadoVista = estadoCliente(clientes.getCodEstado());

            Logger.getLogger(ClientesBean.class.getName()).log(Level.INFO, "codCliente: " + clientes.getCodCliente() + "setVentasClientes", "");
            setVentasClientes();

        }
    }

    public void setHiddenFalse() {
        this.hidden = false;
        limpiarFormulario();
    }

    public void guardarDatosComerciales() {

        Character l_formap = ' ';
        boolean error = false;

        //ClientesPK clienteGuardarPK = new ClientesPK();
        try {
            //validaciones previas
            switch (clienteDCFormaPago) {
                case CONTADO:
                    l_formap = ' ';
                    break;
                case CREDITO:
                    l_formap = 'F';
                    break;
                case CHEQUE:
                    l_formap = 'C';
                    break;
                default:
                    break;
            }
            Logger.getLogger(ClientesBean.class.getName()).log(Level.INFO, null, "forma de pago=" + l_formap);

            if (clienteDCFormaPago == CREDITO || clienteDCFormaPago == CHEQUE) {
                if (limiteCompraDC <= 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Requerido", "Limite de compra debe ser mayor a cero"));
                    return;
                } else if (plazoCreditoDC <= 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Requerido", "Plazo de credito debe ser mayor a cero"));
                    return;
                } else if (plazoImpresionDC <= 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Requerido", "Limite de compra debe ser mayor a cero"));
                    return;
                }
            }

            for (dao.TablaClientesDocumentos elemento : tablaClientesDocumentos) {

                if (elemento.getConFecVto().equalsIgnoreCase("S")
                        && elemento.getFvencimiento() == null) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Requerido", "Plazo de credito debe ser mayor a cero"));
                    return;

                }

                if (elemento.getmObligatorio().equalsIgnoreCase("S")
                        && elemento.isMarcado() == false) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Requerido",
                            "El documento " + elemento.getxDesc() + "es requerido, verificar"));
                    return;

                }

            }

            //copiar los datos al entity
            //Clientes clienteGuardar = clientes;
            /* clienteGuardar.setIlimiteCompra(this.getLimiteCompraDC());
            clienteGuardar.setNplazoCredito(this.getPlazoCreditoDC());
            clienteGuardar.setNplazoImpresion(this.getPlazoImpresionDC());
            clienteGuardar.setIlimiteTemp(this.getLimiteTemporalDC());
            //clienteGuardar.setFlimiteTemp(this.getFechaLimiteTemporalDC().getTime());
            clienteGuardar.setNriesgo(this.getRiesgoDC());
            clienteGuardar.setMformaPago(l_formap);

            clienteGuardar.setClientesDocumentosCollection(clientesDocumentos); */
//            clienteGuardar.setVentasClientesCollection(crearListaVentasClientes());
            List<VentasClientes> ventasClientesGuardar = crearListaVentasClientes();

            //transaccion de guardado
            userTransaction.begin();

            //clientesTipoVentasFacade.mergeClientesTipoVenta(tiposVentasSeleccion, clienteGuardar, tiposVentas);
            clientesCtasBancariasFacade.mergeCtasBancarias(tablaCuentasBancarias, clientes.getCodCliente(), clientes, listaBancos);

            clientesDocumentosFacade.updateDocumentosRequeridos(clientesDocumentos);
            clientesFacade.updateDatosComerciales(clientes);
            ventasClientesFacade.guardarVentasClientes(ventasClientesGuardar, clientes);

            userTransaction.commit();

            limpiarFormulario();

            Logger.getLogger(ClientesBean.class.getName()).log(Level.INFO, null, "datos comerciales guardados");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "datos comerciales guardados", "datos comerciales guardados"));
            RequestContext.getCurrentInstance().execute("PF('mensajes').show();");
        } catch (Exception ex) {
            try {
                userTransaction.rollback();
            } catch (IllegalStateException ex1) {
                Logger.getLogger(ClientesBean.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (SecurityException ex1) {
                Logger.getLogger(ClientesBean.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (SystemException ex1) {
                Logger.getLogger(ClientesBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ClientesBean.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            Logger.getLogger(ClientesBean.class.getName()).log(Level.SEVERE, null, "ocurrio un error");
            RequestContext.getCurrentInstance().update("mensajes");
            //contenidoError = ExceptionHandlerView.getStackTrace(e);
            String tituloError = "Error al guardar datos comerciales.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, tituloError, ex.getMessage()));
            RequestContext.getCurrentInstance().execute("PF('mensajes').show();");

        }

    }

    public void tcdonCellEdit(CellEditEvent event) {

        int fila = Integer.valueOf(event.getRowIndex());

//        //validacion de si se ingresa fecha de vencimiento
//        if (this.tablaClientesDocumentos.get(fila).getConFecVto().equalsIgnoreCase("S")
//                && this.tablaClientesDocumentos.get(fila).getFvencimiento() == null) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_WARN,
//                            "Atención",
//                            "Se debe ingresar fecha de vencimiento"));
//            return;
//        }
//
//        //validacion de si el documento obligatorio es presentado
//        if (this.tablaClientesDocumentos.get(fila).isMarcado() == false
//                && this.tablaClientesDocumentos.get(fila).getmObligatorio().equalsIgnoreCase("S")) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_WARN,
//                            "Atención",
//                            "Documento a presentar es obligatorio:" + this.tablaClientesDocumentos.get(fila).getxDesc()));
//
//        }
    }

//    public List<TablaCuentasBancarias> deleteProvisorioCuentasBancarias() {
//        
//        Integer codigoBanco = getCuentaBancariaSelected();
//        
//        LOGGER.log(Level.INFO, "codigoBanco: " + codigoBanco);
//        
//        for (int i = 0; i < tablaCuentasBancarias.size(); i++) {
//            
//            if (Integer.getInteger(tablaCuentasBancarias.get(i).getCodigoBanco()).compareTo(codigoBanco) == 0) {
//                
//                tablaCuentasBancarias.get(i).setDeletedProvisorio(true);
//                break;
//            }
//            
//        }
//        
//        return tablaCuentasBancarias;
//        
//    }
    public void deleteClienteDocumento() {

        int ii = 0;
        for (Iterator<ClientesDocumentos> elemento = clientesDocumentos.iterator(); elemento.hasNext();) {

            ClientesDocumentos elemento_ = elemento.next();

            if (elemento_.getClientesDocumentosPK().getNsecuencia() == clienteDocumentoSelected.getClientesDocumentosPK().getNsecuencia()) {

                clientesDocumentos.remove(ii);
                break;

            } else {
                ii++;
            }

        }

    }

    /*    public void agregarClienteDocumento() {

        ClientesDocumentos agregar = new ClientesDocumentos();
        ClientesDocumentosPK agregarpk = new ClientesDocumentosPK();
        agregar.setCusuario(String.valueOf(clientes.getCodCliente()));

        agregarpk.setCodCliente(clientes.getCodCliente());

        agregarpk.setNsecuencia((short) (getSecuencia() + 1));

        agregar.setClientesDocumentosPK(agregarpk);

        clientesDocumentos.add(agregar);

    } */
    public Short getSecuencia() {

        Short resultado = 0;

        for (ClientesDocumentos elemento : clientesDocumentos) {

            if (resultado < elemento.getClientesDocumentosPK().getNsecuencia()) {

                resultado = elemento.getClientesDocumentosPK().getNsecuencia();

            }

        }

        return resultado;

    }

    public void deleteCtaBancaria() {

        TablaCuentasBancarias codigoBanco = getCuentaBancariaSelected();

//        LOGGER.log(Level.INFO, "cuentaBancariaSelected: {0}", codigoBanco.getCodigoBanco());
//        LOGGER.log(Level.INFO, "cuentas bancarias size: {0}", tablaCuentasBancarias.size());
//        
        for (int ii = 0; ii < tablaCuentasBancarias.size(); ii++) {

            LOGGER.log(Level.INFO, "tablaCuentasBancarias: {0}", (tablaCuentasBancarias.get(ii).getCodigoBanco()));
            LOGGER.log(Level.INFO, "codigoBanco: {0}", (codigoBanco.getCodigoBanco()));

            if (Integer.parseInt(tablaCuentasBancarias.get(ii).getCodigoBanco()) == (Integer.parseInt(codigoBanco.getCodigoBanco()))) {

                tablaCuentasBancarias.remove(ii);
                break;

            }

        }

    }

    public List<TablaCuentasBancarias> updateCuentasBancarias() {

        Short codigoBanco = codigoBancoSelectedDC;
        String descBanco = "";
        String numeroCuenta = numeroCuentaInputDC;
        TablaCuentasBancarias elementoTabla = new TablaCuentasBancarias();

        for (Bancos elemento : listaBancos) {

            if (Objects.equals(codigoBanco, elemento.getCodBanco())) {
                descBanco = elemento.getXdesc();
                break;
            }

        }

        elementoTabla.setCodigoBanco(String.valueOf(codigoBanco));
        elementoTabla.setDescBanco(descBanco);
        elementoTabla.setNroCuentaCorriente(numeroCuenta);

        tablaCuentasBancarias.add(elementoTabla);

        return tablaCuentasBancarias;

    }

    public Double getDeudaCliente() {

        if (clientes.getIlimiteCompra() <= 0) {
            deudaPedidosPendientesDC = 0.0;
        } else {

            deudaPedidosPendientesDC = clientesFacade.getDeudaPedidosPendientes(clientes.getCodCliente());

        }
        LOGGER.log(Level.INFO, "deuda cliente: " + clientes.getCodCliente() + ":" + deudaPedidosPendientesDC);
        return deudaPedidosPendientesDC;

    }

    public Double getCreditoDisponible() {

        try {

            if (this.limiteCompraDC <= 0) {

                LOGGER.log(Level.INFO, "codigo cliente: " + clientes.getCodCliente() + ":" + 0);
                return 0.0;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                LOGGER.log(Level.INFO, "codigo cliente: " + sdf.parse(sdf.format(new Date())) + ":" + sdf.parse(sdf.format(this.fechaLimiteTemporalDC)));
            } catch (ParseException ex) {
                Logger.getLogger(ClientesBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (this.limiteCompraDC > 0 && sdf.parse(sdf.format(this.fechaLimiteTemporalDC)).compareTo(sdf.parse(sdf.format(new Date()))) >= 0) {
                creditoDisponibleDC = this.limiteCompraDC + this.limiteTemporalDC - this.deudaPedidosPendientesDC;
            } else if (this.limiteCompraDC > 0 && sdf.parse(sdf.format(this.fechaLimiteTemporalDC)).compareTo(sdf.parse(sdf.format(new Date()))) < 0) {

                creditoDisponibleDC = this.limiteCompraDC - this.deudaPedidosPendientesDC;

            }
            LOGGER.log(Level.INFO, "codigo cliente: " + clientes.getCodCliente() + ":" + creditoDisponibleDC);

        } catch (ParseException ex) {
            Logger.getLogger(ClientesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return creditoDisponibleDC;
    }

//    public List<dao.TablaClientesDocumentos> getClientesDocumentos() {
//        List<dao.TablaClientesDocumentos> resultado = clientesDocumentosFacade.buscarDocumentosRequeridos(String.valueOf(this.clientes.getCodCliente()));
////        LOGGER.log(Level.INFO, "codigoCliente: " + this.clientes.getCodCliente() + ":" + (resultado.isEmpty() ? "sin resultados" : resultado.get(0).getFvencimiento()));
////        LOGGER.log(Level.INFO, "clientesDocumentos: " + this.clientes.getCodCliente() + ":" + (resultado.isEmpty() ? "sin resultados" : resultado.get(0).getSecuencia()));
//
//        return resultado;
//    }
    public List<ClientesDocumentos> getClientesDocumentos2(Clientes cliente) {

        List<ClientesDocumentos> resultado = clientesDocumentosFacade.buscarDocumentosCodCliente(cliente);

        return resultado;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void cambiaFechaLimiteTemporalDC() {
        LOGGER.log(Level.INFO, "codigo cliente: " + this.fechaLimiteTemporalDC);
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the listaCanalesVenta
     */
    public List<CanalesVenta> getListaCanalesVenta() {
        return listaCanalesVenta;
    }

    /**
     * @param listaCanalesVenta the listaCanalesVenta to set
     */
    public void setListaCanalesVenta(List<CanalesVenta> listaCanalesVenta) {
        this.listaCanalesVenta = listaCanalesVenta;
    }

    /**
     * @return the canalesVenta
     */
    public CanalesVenta getCanalesVenta() {
        return canalesVenta;
    }

    /**
     * @param canalesVenta the canalesVenta to set
     */
    public void setCanalesVenta(CanalesVenta canalesVenta) {
        this.canalesVenta = canalesVenta;
    }

    /**
     * @return the clienteDCFormaPago
     */
    public int getClienteDCFormaPago() {
        return clienteDCFormaPago;
    }

    /**
     * @param clienteDCFormaPago the clienteDCFormaPago to set
     */
    public void setClienteDCFormaPago(int clienteDCFormaPago) {
        this.clienteDCFormaPago = clienteDCFormaPago;
    }

    /**
     * @return the estadoClienteDescripcion
     */
    public String getEstadoClienteDescripcion() {
        return estadoClienteDescripcion;
    }

    /**
     * @param estadoClienteDescripcion the estadoClienteDescripcion to set
     */
    public void setEstadoClienteDescripcion(String estadoClienteDescripcion) {
        this.estadoClienteDescripcion = estadoClienteDescripcion;
    }

    /**
     * @return the limiteCompraDC
     */
    public Long getLimiteCompraDC() {
        return limiteCompraDC;
    }

    /**
     * @param limiteCompraDC the limiteCompraDC to set
     */
    public void setLimiteCompraDC(Long limiteCompraDC) {
        this.limiteCompraDC = limiteCompraDC;
    }

    public boolean checkFormaPagoContado() {

        if (this.clienteDCFormaPago == CONTADO) {
            limiteCompraDisabled = true;
        } else {
            limiteCompraDisabled = false;
        }
        LOGGER.log(Level.INFO, "Limite Compra: " + String.valueOf(limiteCompraDisabled));

        return limiteCompraDisabled;

    }

    /**
     * @return the limiteCompraEnabled
     */
    public boolean isLimiteCompraDisabled() {
        return limiteCompraDisabled;
    }

    /**
     * @param limiteCompraEnabled the limiteCompraEnabled to set
     */
    public void setLimiteCompraDisabled(boolean limiteCompraDisabled) {
        this.limiteCompraDisabled = limiteCompraDisabled;
    }

    /**
     * @return the PlazoCreditoDC
     */
    public Short getPlazoCreditoDC() {
        return plazoCreditoDC;
    }

    /**
     * @param PlazoCreditoDC the PlazoCreditoDC to set
     */
    public void setPlazoCreditoDC(Short plazoCreditoDC) {
        this.plazoCreditoDC = plazoCreditoDC;
    }

    /**
     * @return the plazoImpresionDC
     */
    public Short getPlazoImpresionDC() {
        return plazoImpresionDC;
    }

    /**
     * @param plazoImpresionDC the plazoImpresionDC to set
     */
    public void setPlazoImpresionDC(Short plazoImpresionDC) {
        this.plazoImpresionDC = plazoImpresionDC;
    }

    /**
     * @return the limiteTemporalDC
     */
    public Long getLimiteTemporalDC() {
        return limiteTemporalDC;
    }

    /**
     * @param limiteTemporalDC the limiteTemporalDC to set
     */
    public void setLimiteTemporalDC(Long limiteTemporalDC) {
        this.limiteTemporalDC = limiteTemporalDC;
    }

    /**
     * @return the riesgoDC
     */
    public Short getRiesgoDC() {
        return riesgoDC;
    }

    /**
     * @param riesgoDC the riesgoDC to set
     */
    public void setRiesgoDC(Short riesgoDC) {
        this.riesgoDC = riesgoDC;
    }

    /**
     * @return the creditoDisponibleDC
     */
    public Double getCreditoDisponibleDC() {
        return creditoDisponibleDC;
    }

    /**
     * @param creditoDisponibleDC the creditoDisponibleDC to set
     */
    public void setCreditoDisponibleDC(Double creditoDisponibleDC) {
        this.creditoDisponibleDC = creditoDisponibleDC;
    }

    /**
     * @return the deudaPedidosPendientesDC
     */
    public Double getDeudaPedidosPendientesDC() {
        return deudaPedidosPendientesDC;
    }

    /**
     * @param deudaPedidosPendientesDC the deudaPedidosPendientesDC to set
     */
    public void setDeudaPedidosPendientesDC(Double deudaPedidosPendientesDC) {
        this.deudaPedidosPendientesDC = deudaPedidosPendientesDC;
    }

    /**
     * @return the tiposVentas
     */
    public List<TiposVentas> getTiposVentas() {
        return tiposVentas;
    }

    /**
     * @param tiposVentas the tiposVentas to set
     */
    public void setTiposVentas(ArrayList<TiposVentas> tiposVentas) {
        this.tiposVentas = tiposVentas;
    }

    /**
     * @return the tiposVentasSeleccion
     */
    public char[] getTiposVentasSeleccion() {
        return tiposVentasSeleccion;
    }

    /**
     * @param tiposVentasSeleccion the tiposVentasSeleccion to set
     */
    public void setTiposVentasSeleccion(char[] tiposVentasSeleccion) {
        this.tiposVentasSeleccion = tiposVentasSeleccion;
    }

//    /**
//     * @return the tiposVentasString
//     */
//    public String[] getTiposVentasString() {
//        return tiposVentasString;
//    }
//
//    /**
//     * @param tiposVentasString the tiposVentasString to set
//     */
//    public void setTiposVentasString(String[] tiposVentasString) {
//        this.tiposVentasString = tiposVentasString;
//    }
    /**
     * @return the bancosFacade
     */
    public BancosFacade getBancosFacade() {
        return bancosFacade;
    }

    /**
     * @param bancosFacade the bancosFacade to set
     */
    public void setBancosFacade(BancosFacade bancosFacade) {
        this.bancosFacade = bancosFacade;
    }

    /**
     * @return the listaBancos
     */
    public List<Bancos> getListaBancos() {
        return listaBancos;
    }

    /**
     * @param listaBancos the listaBancos to set
     */
    public void setListaBancos(List<Bancos> listaBancos) {
        this.listaBancos = listaBancos;
    }

    /**
     * @return the cuentaCorrienteDC
     */
    public String[] getCuentaCorrienteDC() {
        return cuentaCorrienteDC;
    }

    /**
     * @param cuentaCorrienteDC the cuentaCorrienteDC to set
     */
    public void setCuentaCorrienteDC(String[] cuentaCorrienteDC) {
        this.cuentaCorrienteDC = cuentaCorrienteDC;
    }

    /**
     * @return the descBancosDC
     */
    public String[] getDescBancosDC() {
        return descBancosDC;
    }

    /**
     * @param descBancosDC the descBancosDC to set
     */
    public void setDescBancosDC(String[] descBancosDC) {
        this.descBancosDC = descBancosDC;
    }

    /**
     * @return the codigoBancoSelectedDC
     */
    public Short getCodigoBancoSelectedDC() {
        return codigoBancoSelectedDC;
    }

    /**
     * @param codigoBancoSelectedDC the codigoBancoSelectedDC to set
     */
    public void setCodigoBancoSelectedDC(Short codigoBancoSelectedDC) {
        this.codigoBancoSelectedDC = codigoBancoSelectedDC;
    }

    /**
     * @return the numeroCuentaInputDC
     */
    public String getNumeroCuentaInputDC() {
        return numeroCuentaInputDC;
    }

    /**
     * @param numeroCuentaInputDC the numeroCuentaInputDC to set
     */
    public void setNumeroCuentaInputDC(String numeroCuentaInputDC) {
        this.numeroCuentaInputDC = numeroCuentaInputDC;
    }

    /**
     * @return the tablaCuentasBancarias
     */
    public List<TablaCuentasBancarias> getTablaCuentasBancarias() {
        return tablaCuentasBancarias;
    }

    /**
     * @param tablaCuentasBancarias the tablaCuentasBancarias to set
     */
    public void setTablaCuentasBancarias(List<TablaCuentasBancarias> tablaCuentasBancarias) {
        this.tablaCuentasBancarias = tablaCuentasBancarias;
    }

    /**
     * @return the fechaLimiteTemporalDC
     */
    public Date getFechaLimiteTemporalDC() {
        return fechaLimiteTemporalDC;
    }

    /**
     * @param fechaLimiteTemporalDC the fechaLimiteTemporalDC to set
     */
    public void setFechaLimiteTemporalDC(Date fechaLimiteTemporalDC) {
        this.fechaLimiteTemporalDC = fechaLimiteTemporalDC;
    }

    /**
     * @return the tablaClientesDocumentos
     */
    public List<dao.TablaClientesDocumentos> getTablaClientesDocumentos() {
        return tablaClientesDocumentos;
    }

    /**
     * @param tablaClientesDocumentos the tablaClientesDocumentos to set
     */
    public void setTablaClientesDocumentos(List<dao.TablaClientesDocumentos> tablaClientesDocumentos) {
        this.tablaClientesDocumentos = tablaClientesDocumentos;
    }

    /**
     * @return the renderedBtnEdit
     */
    public boolean isRenderedBtnEdit() {
        return renderedBtnEdit;
    }

    /**
     * @param renderedBtnEdit the renderedBtnEdit to set
     */
    public void setRenderedBtnEdit(boolean renderedBtnEdit) {
        this.renderedBtnEdit = renderedBtnEdit;
    }

    /**
     * @return the renderedBtnAgregar
     */
    public boolean isRenderedBtnAgregar() {
        return renderedBtnAgregar;
    }

    /**
     * @param renderedBtnAgregar the renderedBtnAgregar to set
     */
    public void setRenderedBtnAgregar(boolean renderedBtnAgregar) {
        this.renderedBtnAgregar = renderedBtnAgregar;
    }

    /**
     * @return the renderedBtnBorrar
     */
    public boolean isRenderedBtnBorrar() {
        return renderedBtnBorrar;
    }

    /**
     * @param renderedBtnBorrar the renderedBtnBorrar to set
     */
    public void setRenderedBtnBorrar(boolean renderedBtnBorrar) {
        this.renderedBtnBorrar = renderedBtnBorrar;
    }

    /**
     * @return the cuentaBancariaSelected
     */
    public TablaCuentasBancarias getCuentaBancariaSelected() {
        return cuentaBancariaSelected;
    }

    /**
     * @param cuentaBancariaSelected the cuentaBancariaSelected to set
     */
    public void setCuentaBancariaSelected(TablaCuentasBancarias cuentaBancariaSelected) {
        this.cuentaBancariaSelected = cuentaBancariaSelected;
    }

    /**
     * @return the tiposVentasVista
     */
    public SelectItem[] getTiposVentasVista() {
        return tiposVentasVista;
    }

    /**
     * @param tiposVentasVista the tiposVentasVista to set
     */
    public void setTiposVentasVista(SelectItem[] tiposVentasVista) {
        this.tiposVentasVista = tiposVentasVista;
    }

    /**
     * @return the clienteDocumentoSelected
     */
    public ClientesDocumentos getClienteDocumentoSelected() {
        return clienteDocumentoSelected;
    }

    /**
     * @param clienteDocumentoSelected the clienteDocumentoSelected to set
     */
    public void setClienteDocumentoSelected(ClientesDocumentos clienteDocumentoSelected) {
        this.clienteDocumentoSelected = clienteDocumentoSelected;
    }

    /**
     * @return the clienteEstadoVista
     */
    public String getClienteEstadoVista() {
        return clienteEstadoVista;
    }

    /**
     * @param clienteEstadoVista the clienteEstadoVista to set
     */
    public void setClienteEstadoVista(String clienteEstadoVista) {
        this.clienteEstadoVista = clienteEstadoVista;
    }

    private void setVentasClientes() {

        tiposVentasVista = new SelectItem[tiposVentas.size()];
        tiposVentasSeleccion = new char[tiposVentas.size()];
        Object[] objArr = tiposVentas.toArray();
        int i = 0;
        for (Object obj : objArr) {
            TiposVentas temp = (TiposVentas) obj;
            SelectItem elemento = new SelectItem(temp.getTiposVentasPK().getCtipoVta(), temp.getXdesc());
            Logger.getLogger(ClientesBean.class.getName()).log(Level.INFO, temp.getXdesc(), "setVentasClientes");
            tiposVentasVista[i] = elemento;
            tiposVentasSeleccion[i] = ' ';
            i++;
        }

        List<VentasClientes> lista_ = ventasClientesFacade.getVentasClientesCodCliente(clientes);
        tiposVentasSeleccion = new char[tiposVentas.size()];
        int ii = 0;
        for (VentasClientes elemento : lista_) {

            tiposVentasSeleccion[ii] = (elemento.getVentasClientesPK().getCtipoVta());
            ii++;

        }
        Logger.getLogger(ClientesBean.class.getName()).log(Level.INFO, String.copyValueOf(tiposVentasSeleccion), "setVentasClientes");

    }

    private List<VentasClientes> crearListaVentasClientes() {

        ArrayList<VentasClientes> resultado = new ArrayList<VentasClientes>();

        for (int ii = 0; ii < tiposVentasSeleccion.length; ii++) {

            for (TiposVentas elemento : tiposVentas) {

                if (elemento.getTiposVentasPK().getCtipoVta().equals(tiposVentasSeleccion[ii])) {

                    VentasClientes elementoAgregar = new VentasClientes();
                    VentasClientesPK elementoAgregarPK = new VentasClientesPK();

                    elementoAgregarPK.setCodCliente(clientes.getCodCliente());
                    elementoAgregarPK.setCodEmpr(clientes.getCodEmpr());
                    elementoAgregarPK.setCtipoVta(tiposVentasSeleccion[ii]);

                    elementoAgregar.setVentasClientesPK(elementoAgregarPK);
                    //elementoAgregar.setClientess(clientes);

                    resultado.add(elementoAgregar);

                    break;
                }

            }

        }

        return resultado;

    }

    private List<ClientesDocumentos> getDocumentosPresentados(Clientes clientes) {
        List<ClientesDocumentos> resultado = clientesDocumentosFacade.buscarDocumentosCodCliente(clientes);
        resultado.forEach((elemento) -> {
            elemento.setPresentado(true);
        });

        //poner bien el campo obligatorio
        List<DocumentosReqCondiciones> drc = documentosReqCondicionesFacade
                .getDocumentosReqCondiciones(clientes);

        resultado.forEach((_item) -> {
            for (DocumentosReqCondiciones elementoDRC : drc) {

                if (_item.getCdocum().equalsIgnoreCase(elementoDRC.getDocumentosReqCondicionesPK().getCdocum())) {

                    _item.setObligatorio(elementoDRC.getMobligatorio().equals('S'));

                }

            }
        });

        //documentos a presentar
        List<DocumentosRequeridos> documentosRequeridos = documentosRequeridosFacade.buscarDocumentosRequeridos(clientes, drc);

        for (DocumentosRequeridos elemento : documentosRequeridos) {

            DocumentosReqCondiciones condiciones = getCondiciones(elemento, drc);

            boolean docPresentado = false;
            for (ClientesDocumentos presentado : resultado) {

                if (elemento.getCdocum().equalsIgnoreCase(presentado.getCdocum())) {

                    docPresentado = true;
                    presentado.setNombreDocumento(elemento.getXdesc());
                    presentado.setCdocum(elemento.getCdocum());
                    break;
                }

            }

            if (!docPresentado) {

                ClientesDocumentos nuevo = new ClientesDocumentos();
                ClientesDocumentosPK nuevoPK = new ClientesDocumentosPK();

                nuevoPK.setCodCliente(clientes.getCodCliente());
                nuevoPK.setNsecuencia(getNextSecuencia(resultado));

                nuevo.setClientesDocumentosPK(nuevoPK);

                nuevo.setNombreDocumento(elemento.getXdesc());

                nuevo.setCdocum(elemento.getCdocum());

                nuevo.setObligatorio(condiciones.getMobligatorio().equals('S'));

                resultado.add(nuevo);

            }

        }

        return resultado;
    }

    private short getNextSecuencia(List<ClientesDocumentos> clientesDocumentosPresentados) {

        short resultado = 0;

        for (ClientesDocumentos elemento : clientesDocumentosPresentados) {

            if (elemento.getClientesDocumentosPK().getNsecuencia() > resultado) {

                resultado = elemento.getClientesDocumentosPK().getNsecuencia();

            }

        }

        return (short) (resultado + 1);

    }

    private DocumentosReqCondiciones getCondiciones(DocumentosRequeridos dr, List<DocumentosReqCondiciones> documentosRequeridos) {

        DocumentosReqCondiciones resultado = null;

        for (DocumentosReqCondiciones elemento : documentosRequeridos) {

            if (elemento.getDocumentosReqCondicionesPK().getCdocum().equals(dr.getCdocum())) {

                resultado = elemento;

            }

        }

        return resultado;

    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private Clientes actualizarDatosBean(Clientes clientes) {
        Clientes resultado = clientesFacade.buscarPorCodigo(String.valueOf(clientes.getCodCliente()));

        Logger.getLogger(ClientesBean.class.getName()).log(Level.INFO, "cliente: " + resultado.getFprimFact(), "");

        if (resultado.getFprimFact() == null) {

            resultado.setFprimFact(parseDate("1970-01-01"));

        }

        if (resultado.getIlimiteTemp() == null) {

            resultado.setIlimiteTemp(Long.valueOf("0"));

        }

        return resultado;
    }

    private void limpiarFormulario() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        clientes = null;
        clientesDocumentos = null;
        clienteDocumentoSelected = null;
        tiposVentasSeleccion = null;
        //tiposVentasVista=null;
        tablaCuentasBancarias = null;
    }

}
