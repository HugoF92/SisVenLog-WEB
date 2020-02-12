package bean;

import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.EnviosDetFacade;
import dao.EnviosFacade;
import dao.ExistenciasFacade;
import dao.MercaderiasFacade;
import dao.MovimientosMercaFacade;
import entidad.CanalesVenta;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Empleados;
import entidad.Empresas;
import entidad.Envios;
import entidad.EnviosDet;
import entidad.EnviosDetPK;
import entidad.EnviosPK;
import entidad.Existencias;
import entidad.ExistenciasPK;
import entidad.MovimientosMerca;
import entidad.Mercaderias;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import java.util.Map;

@ManagedBean
@SessionScoped
public class EnviosBean extends LazyDataModel<Envios> implements Serializable {

    @EJB
    private EnviosFacade enviosFacade;

    @EJB
    private EnviosDetFacade enviosDetFacade;

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private ExistenciasFacade existenciasFacade;

    @EJB
    private EmpleadosFacade empleadosFacade;

    @EJB
    private DepositosFacade depositosFacade;

    @EJB
    private MovimientosMercaFacade movimientosMercaFacade;

    private LazyDataModel<Envios> model;

    private Envios envios = null;
    private List<Envios> listaEnvios = null;

    private EnviosDet enviosDet = null;
    private List<EnviosDet> listaEnviosDet = null;

    @SuppressWarnings("FieldMayBeFinal")
    private List<EnviosDet> listaEnviosDetCollection = null;

    private CanalesVenta canalesVenta = null;
    private Depositos origen = null;
    private Depositos destino = null;

    private Empleados empleados = null;

    private MovimientosMerca movimientosMerca = null;
    private List<MovimientosMerca> listaMovimientosMerca = null;

    private Mercaderias mercaderias = null;

    private Existencias existencias = null;

    private Empresas empresas = null;

    EnviosDetPK enviosDetPkk = null;
    EnviosPK enviosPkk = null;

    DepositosPK depositoPkk = null;

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    private boolean vaciarCamion;

    private Integer cajas;
    private Integer unidades;

    private Integer cantidadItem;

    private Double calculoPesos;

    private String articulo = "";

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaEnvios = new ArrayList<Envios>();
        this.enviosPkk = new EnviosPK();
        this.envios = new Envios();
//        BigDecimal respuesta = this.enviosFacade.getMaxNroEnvio();
//        this.enviosPkk.setNroEnvio(respuesta.longValue());
        this.envios.setEnviosPK(enviosPkk);
        this.listaEnviosDet = new ArrayList<EnviosDet>();
        this.empleados = new Empleados();
        this.listaEnviosDetCollection = new ArrayList<EnviosDet>();
        this.vaciarCamion = false;

        model = new LazyDataModel<Envios>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Envios> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                //List<Envios> envioss;
                int count = 0;
                if (filters.size() == 0) {
                    listaEnvios = enviosFacade.findRangeSort(new int[]{first, pageSize});
                    count = enviosFacade.count();
                } else {
                    if (filters.size() < 2) {
                        //dd/MM/yyyy
                        String filtroNroEnvio = (String) filters.get("enviosPK.nroEnvio");
                        listaEnvios = enviosFacade.buscarEnviosPorNroEnvioFecha(filtroNroEnvio, new int[]{first, pageSize});
                        count = enviosFacade.CountBuscarEnviosPorNroEnvioFecha(filtroNroEnvio);
                    }

                }

                model.setRowCount(count);
                return listaEnvios;
            }

            @Override
            public Envios getRowData(String rowKey) {
                String tempIndex = rowKey;
                System.out.println("1");
                if (tempIndex != null) {
                    for (Envios inc : listaEnvios) {
                        if (Long.toString(inc.getEnviosPK().getNroEnvio()).equals(rowKey)) {
                            return inc;
                        }
                    }
                }

                return null;
            }

            @Override
            public Object getRowKey(Envios enviosss) {
                return enviosss.getEnviosPK().getNroEnvio();
            }
        };

        canalesVenta = new CanalesVenta();

        origen = new Depositos();
        origen.setDepositosPK(new DepositosPK());

        destino = new Depositos();
        destino.setDepositosPK(new DepositosPK());

        //origen = new Depositos(new DepositosPK());
        //destino = new Depositos(new DepositosPK());
        this.articulo = "";

        existencias = new Existencias();
        movimientosMerca = new MovimientosMerca();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        envios.setFenvio(new Date());

        setCajas(0);
        setUnidades(0);
        setCantidadItem(0);
        setCalculoPesos(0.0);

    }

    public EnviosBean() {

        //instanciar();
    }

    public List<EnviosDet> getListaEnviosDetCollection() {
        return listaEnviosDetCollection;
    }

    public void setListaEnviosDetCollection(List<EnviosDet> listaEnviosDetCollection) {
        this.listaEnviosDetCollection = listaEnviosDetCollection;
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

    public Envios getEnvios() {
        return envios;
    }

    public void setEnvios(Envios envios) {
        this.envios = envios;
    }

    public List<Envios> getListaEnvios() {
        return listaEnvios;
    }

    public void setListaEnvios(List<Envios> listaEnvios) {
        this.listaEnvios = listaEnvios;
    }

    public EnviosFacade getEnviosFacade() {
        return enviosFacade;
    }

    public void setEnviosFacade(EnviosFacade enviosFacade) {
        this.enviosFacade = enviosFacade;
    }

    public EnviosDetFacade getEnviosDetFacade() {
        return enviosDetFacade;
    }

    public void setEnviosDetFacade(EnviosDetFacade enviosDetFacade) {
        this.enviosDetFacade = enviosDetFacade;
    }

    public EnviosDet getEnviosDet() {
        return enviosDet;
    }

    public void setEnviosDet(EnviosDet EnviosDet) {
        this.enviosDet = EnviosDet;
    }

    public List<EnviosDet> getListaEnviosDet() {
        return listaEnviosDet;
    }

    public void setListaEnviosDet(List<EnviosDet> listaEnviosDet) {
        this.listaEnviosDet = listaEnviosDet;
    }

    public MovimientosMercaFacade getMovimientosMercaFacade() {
        return movimientosMercaFacade;
    }

    public void setMovimientosMercaFacade(MovimientosMercaFacade movimientosMercaFacade) {
        this.movimientosMercaFacade = movimientosMercaFacade;
    }

    public MovimientosMerca getMovimientosMerca() {
        return movimientosMerca;
    }

    public void setMovimientosMerca(MovimientosMerca movimientosMerca) {
        this.movimientosMerca = movimientosMerca;
    }

    public List<MovimientosMerca> getListaMovimientosMerca() {
        return listaMovimientosMerca;
    }

    public void setListaMovimientosMerca(List<MovimientosMerca> listaMovimientosMerca) {
        this.listaMovimientosMerca = listaMovimientosMerca;
    }

    public CanalesVenta getCanalesVenta() {
        return canalesVenta;
    }

    public void setCanalesVenta(CanalesVenta canalesVenta) {
        this.canalesVenta = canalesVenta;
    }

    public Depositos getOrigen() {
        return origen;
    }

    public void setOrigen(Depositos origen) {
        this.origen = origen;
    }

    public Depositos getDestino() {
        return destino;
    }

    public void setDestino(Depositos destino) {
        this.destino = destino;
    }

    public boolean isVaciarCamion() {
        return vaciarCamion;
    }

    public void setVaciarCamion(boolean vaciarCamion) {
        this.vaciarCamion = vaciarCamion;
    }

    public Integer getCajas() {
        return cajas;
    }

    public void setCajas(Integer cajas) {
        this.cajas = cajas;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public Double getCalculoPesos() {
        return calculoPesos;
    }

    public void setCalculoPesos(Double calculoPesos) {
        this.calculoPesos = calculoPesos;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    public void nuevo() {
        this.envios = new Envios();
        listaEnvios = new ArrayList<Envios>();

    }

    public void cargarDetalle() {
        //System.out.println("ESTOY EN CARGAR DETALLES");

        if (cajas <= 0 && unidades <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere que cantidad o unidades sean mayores a cero"));
            return;
        }
        try {
            enviosDet = new EnviosDet();
            enviosDetPkk = new EnviosDetPK();
            enviosDet.setMercaderias(this.existencias.getMercaderias());
            enviosDet.setXdesc(this.existencias.getMercaderias().getXdesc());
            enviosDet.setCantUnid(unidades);
            enviosDet.setCantCajas(cajas);

            enviosDetPkk.setCodMerca(this.existencias.getMercaderias().getMercaderiasPK().getCodMerca());
            enviosDet.setEnviosDetPK(enviosDetPkk);

            /*Verificar duplicados*/
            for (EnviosDet enviosDetallesDuplicados : this.listaEnviosDet) {
                if (enviosDetallesDuplicados.getEnviosDetPK().getCodMerca().equals(enviosDet.getEnviosDetPK().getCodMerca())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Verifique que la Mercaderia no este duplicada en la grilla"));
                    /*limpiar campos*/
                    this.cajas = 0;
                    this.unidades = 0;
                    this.existencias = new Existencias();
                    this.articulo = "";
                    return;
                }
            }

            this.listaEnviosDet.add(enviosDet);
            setCantidadItem(this.cantidadItem + 1);

            /*calculo del peso total*/
            Mercaderias mercaderiasNew = new Mercaderias();
            mercaderiasNew = this.mercaderiasFacade.buscarPorCodigoMercaderia(enviosDet.getEnviosDetPK().getCodMerca());

            Double cantidadesCajas = enviosDet.getCantCajas().doubleValue();
            Double cantidadesUnidades = enviosDet.getCantUnid().doubleValue();
            Double pesosCajas = mercaderiasNew.getNpesoCaja().doubleValue();
            Double pesosUnidades = mercaderiasNew.getNpesoUnidad().doubleValue();

            Double cajasTotales = 0.0;
            Double unidadesTotales = 0.0;
            Double pesoReal = 0.0;
            cajasTotales = cantidadesCajas * pesosCajas;

            unidadesTotales = cantidadesUnidades * pesosUnidades;

            pesoReal = cajasTotales + unidadesTotales;

            setCalculoPesos(getCalculoPesos() + pesoReal);

            /*limpiar campos*/
            this.cajas = 0;
            this.unidades = 0;
            this.existencias = new Existencias();
            this.articulo = "";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Verifique que la Mercaderia tenga npeso y/o nunidad"));
            this.listaEnviosDet = new ArrayList<EnviosDet>();
            return;
        }

    }

    public void insertar() {
        try {
            short codigoEntregador;
            try {
                codigoEntregador = movimientosMerca.getCodEntregador();
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere de un Receptor"));
                return;
            }

            if (origen == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere de un Deposito Origen"));
                return;
            }

            if (destino == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere de un Deposito Destino"));
                return;
            }

            if (canalesVenta == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere de un Canal de Venta"));
                return;
            }

            if (this.calculoPesos <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere de un Total de pesos"));
                return;

            }

            if (this.cantidadItem <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere de al menos un Item"));
                return;
            }

            if ("".equals(this.envios.getXobs())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere de una observacion"));
                return;
            }

            if ((this.envios.getXobs() == null)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Se requiere de una observacion"));
                return;
            }

            enviosPkk = new EnviosPK();
            enviosPkk.setCodEmpr(origen.getDepositosPK().getCodEmpr());
            enviosPkk.setNroEnvio(this.envios.getEnviosPK().getNroEnvio());
            this.envios.setEnviosPK(enviosPkk);
            this.envios.setCodEntregador(codigoEntregador);
            this.envios.setCodCanal(canalesVenta.getCodCanal());
            this.envios.setDepoOrigen(origen.getDepositosPK().getCodDepo());
            this.envios.setDepoDestino(destino.getDepositosPK().getCodDepo());
            this.envios.setFalta(new Date());
            this.envios.setCusuario("admin");
            this.envios.setMestado('A');
            this.envios.setMtipo('D');
            this.envios.setTotPeso(BigDecimal.valueOf(this.calculoPesos));

            Depositos origenn = this.depositosFacade.getDepositoPorCodigo(origen.getDepositosPK().getCodDepo());
            Depositos destinoo = this.depositosFacade.getDepositoPorCodigo(destino.getDepositosPK().getCodDepo());

            for (EnviosDet enviosdetalle : listaEnviosDet) {
                enviosDetPkk = new EnviosDetPK();
                enviosDetPkk.setCodEmpr(origen.getDepositosPK().getCodEmpr());
                enviosDetPkk.setCodMerca(enviosdetalle.getEnviosDetPK().getCodMerca());
                enviosDetPkk.setNroEnvio(this.envios.getEnviosPK().getNroEnvio());
                enviosdetalle.setEnviosDetPK(enviosDetPkk);

                enviosdetalle.setEnvios(this.envios);

                enviosdetalle.setCodCanal(this.canalesVenta.getCodCanal());
                listaEnviosDetCollection.add(enviosdetalle);

                //enviosDetFacade.insertarEnviosDetalle(enviosdetalle);
                /*MOVIMIENTOS MERCA*/
                /**
                 * ******DEPOSITO DESTINO********
                 */
                this.movimientosMerca = new MovimientosMerca();
                this.movimientosMerca.setNroMovim(1L);
                this.movimientosMerca.setCodEmpr(this.envios.getEnviosPK().getCodEmpr());
                this.movimientosMerca.setCodMerca(enviosdetalle.getEnviosDetPK().getCodMerca());
                this.movimientosMerca.setCodDepo(destino.getDepositosPK().getCodDepo());
                this.movimientosMerca.setCantCajas(Long.valueOf(enviosdetalle.getCantCajas()));
                this.movimientosMerca.setCodEntregador(this.envios.getCodEntregador());
                this.movimientosMerca.setCantUnid(Long.valueOf(enviosdetalle.getCantUnid()));
                this.movimientosMerca.setCodZona(destinoo.getZonas().getZonasPK().getCodZona());

                Mercaderias mercaderiasNew = new Mercaderias();
                mercaderiasNew = this.mercaderiasFacade.buscarPorCodigoMercaderia(enviosdetalle.getEnviosDetPK().getCodMerca());
                Double pesosTotales = 0.0;
                Double cantidadesCajas = enviosdetalle.getCantCajas().doubleValue();
                Double cantidadesUnidades = enviosdetalle.getCantUnid().doubleValue();
                Double pesosCajas = mercaderiasNew.getNpesoCaja().doubleValue();
                Double pesosUnidades = mercaderiasNew.getNpesoUnidad().doubleValue();

                Double cajasTotales = 0.0;
                Double unidadesTotales = 0.0;

                cajasTotales = cantidadesCajas * pesosCajas;

                unidadesTotales = cantidadesUnidades * pesosUnidades;

                pesosTotales = cajasTotales + unidadesTotales;

                this.movimientosMerca.setNpeso(BigDecimal.valueOf(pesosTotales));
                this.movimientosMerca.setManulado(new Short("1"));
                this.movimientosMerca.setFmovim(new Date());
                this.movimientosMerca.setCtipoDocum("EN");
                this.movimientosMerca.setNdocum(enviosdetalle.getEnviosDetPK().getNroEnvio());
                this.movimientosMerca.setIexentas(0L);
                this.movimientosMerca.setIgravadas(0L);
                this.movimientosMerca.setImpuestos(0L);
                this.movimientosMerca.setFalta(new Date());
                this.movimientosMerca.setCusuario("admin");
                this.movimientosMerca.setOldCajas(0L);

                movimientosMercaFacade.insertarMovimientosMerca(movimientosMerca);

                /**
                 * ******DEPOSITO ORIGEN********
                 */
                this.movimientosMerca = new MovimientosMerca();
                this.movimientosMerca.setNroMovim(2L);
                cantidadesCajas = (enviosdetalle.getCantCajas().doubleValue()) * -1;
                cantidadesUnidades = (enviosdetalle.getCantUnid().doubleValue()) * -1;
                this.movimientosMerca.setCodEmpr(this.envios.getEnviosPK().getCodEmpr());
                this.movimientosMerca.setCodMerca(enviosdetalle.getEnviosDetPK().getCodMerca());
                this.movimientosMerca.setCodDepo(origen.getDepositosPK().getCodDepo());
                this.movimientosMerca.setCantCajas(cantidadesCajas.longValue());
                //this.movimientosMerca.setCodEntregador(this.envios.getCodEntregador());
                this.movimientosMerca.setCantUnid(cantidadesUnidades.longValue());
                this.movimientosMerca.setCodZona(origenn.getZonas().getZonasPK().getCodZona());

                mercaderiasNew = new Mercaderias();
                mercaderiasNew = this.mercaderiasFacade.buscarPorCodigoMercaderia(enviosdetalle.getEnviosDetPK().getCodMerca());
                pesosTotales = 0.0;
                cantidadesCajas = enviosdetalle.getCantCajas().doubleValue();
                cantidadesUnidades = enviosdetalle.getCantUnid().doubleValue();
                pesosCajas = mercaderiasNew.getNpesoCaja().doubleValue();
                pesosUnidades = mercaderiasNew.getNpesoUnidad().doubleValue();

                cajasTotales = 0.0;
                unidadesTotales = 0.0;

                cajasTotales = cantidadesCajas * pesosCajas;

                unidadesTotales = cantidadesUnidades * pesosUnidades;

                pesosTotales = cajasTotales + unidadesTotales;

                this.movimientosMerca.setNpeso(BigDecimal.valueOf(pesosTotales));
                this.movimientosMerca.setManulado(new Short("1"));
                this.movimientosMerca.setFmovim(new Date());
                this.movimientosMerca.setCtipoDocum("EN");
                this.movimientosMerca.setNdocum(enviosdetalle.getEnviosDetPK().getNroEnvio());
                this.movimientosMerca.setIexentas(0L);
                this.movimientosMerca.setIgravadas(0L);
                this.movimientosMerca.setImpuestos(0L);
                this.movimientosMerca.setFalta(new Date());
                this.movimientosMerca.setCusuario("admin");
                this.movimientosMerca.setOldCajas(0L);

                movimientosMercaFacade.insertarMovimientosMerca(movimientosMerca);

            }

            this.envios.setEnviosDetCollection(listaEnviosDetCollection);

            enviosFacade.insertarEnvios(this.envios);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevEnvios').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editarView() {
        try {

            if ("".equals(this.envios.getFenvio())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecciones un item", "Se necesita seleccionar un item para anular."));
                return;

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void anular() {
        try {
            if ("".equals(this.envios.getFenvio())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecciones un item", "Se necesita seleccionar un item para anular."));
                return;

            } else {
                if (this.envios.getMestado().charValue() != 'A') {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Estado Inválido", "Estado Invalido de Nota de Envio."));
                    return;
                } else {

                    Integer pedidosActivos = 0;

                    pedidosActivos = enviosFacade.buscarFacturasActivas(this.envios.getEnviosPK().getNroEnvio());

                    if (pedidosActivos < 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Error en Busqueda de Facturas Asociadas."));
                        return;
                    }

                    if (pedidosActivos > 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Existen Facturas Activas."));
                        return;
                    }

                    pedidosActivos = 0;
                    pedidosActivos = enviosFacade.updatePedidosPorNroEnvio(this.envios.getEnviosPK().getNroEnvio());
                    if (pedidosActivos < 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Error en Actualizacion de Estado del Pedido."));
                        return;
                    }

                    Collection<EnviosDet> listaEnviosDetCollectionAnular = new ArrayList<EnviosDet>();
                    listaEnviosDetCollectionAnular = this.envios.getEnviosDetCollection();

                    for (EnviosDet enviosDetAnular : listaEnviosDetCollectionAnular) {

                        this.movimientosMerca = new MovimientosMerca();
                        this.movimientosMerca.setNroMovim(3L);
                        Double cantidadesCajasAnular = (enviosDetAnular.getCantCajas().doubleValue());
                        Double cantidadesUnidadesAnular = (enviosDetAnular.getCantUnid().doubleValue());
                        this.movimientosMerca.setCodEmpr(this.envios.getEnviosPK().getCodEmpr());
                        this.movimientosMerca.setCodMerca(enviosDetAnular.getEnviosDetPK().getCodMerca());
                        this.movimientosMerca.setCodDepo(this.envios.getDepoOrigen());
                        this.movimientosMerca.setCantCajas(cantidadesCajasAnular.longValue());
                        this.movimientosMerca.setCantUnid(cantidadesUnidadesAnular.longValue());
                        Depositos origennAnular = this.depositosFacade.getDepositoPorCodigo(this.envios.getDepoOrigen());
                        this.movimientosMerca.setCodZona(origennAnular.getZonas().getZonasPK().getCodZona());

                        Mercaderias mercaderiasAnularNew = new Mercaderias();
                        mercaderiasAnularNew = this.mercaderiasFacade.buscarPorCodigoMercaderia(enviosDetAnular.getEnviosDetPK().getCodMerca());
                        Double pesosTotalesAnular = 0.0;

                        Double pesosCajasAnular = mercaderiasAnularNew.getNpesoCaja().doubleValue();
                        Double pesosUnidadesAnular = mercaderiasAnularNew.getNpesoUnidad().doubleValue();

                        Double cajasTotalesAnuluar = 0.0;
                        Double unidadesTotalesAnular = 0.0;

                        cajasTotalesAnuluar = cantidadesCajasAnular * pesosCajasAnular;

                        unidadesTotalesAnular = cantidadesUnidadesAnular * pesosUnidadesAnular;

                        pesosTotalesAnular = cajasTotalesAnuluar + unidadesTotalesAnular;

                        this.movimientosMerca.setNpeso(BigDecimal.valueOf(pesosTotalesAnular));
                        this.movimientosMerca.setManulado(new Short("-1"));
                        this.movimientosMerca.setFmovim(new Date());
                        this.movimientosMerca.setCtipoDocum("EN");
                        this.movimientosMerca.setNdocum(enviosDetAnular.getEnviosDetPK().getNroEnvio());
                        this.movimientosMerca.setIexentas(0L);
                        this.movimientosMerca.setIgravadas(0L);
                        this.movimientosMerca.setImpuestos(0L);
                        this.movimientosMerca.setFalta(new Date());
                        this.movimientosMerca.setCusuario("admin");
                        this.movimientosMerca.setOldCajas(0L);
                        try {
                            this.movimientosMercaFacade.insertarMovimientosMerca(this.movimientosMerca);

                        } catch (Exception e) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Error Insercion Movimientos Mercaderias - Origen"));
                        }

                        this.movimientosMerca = new MovimientosMerca();
                        this.movimientosMerca.setNroMovim(4L);
                        cantidadesCajasAnular = (enviosDetAnular.getCantCajas().doubleValue()) * -1;
                        cantidadesUnidadesAnular = (enviosDetAnular.getCantUnid().doubleValue()) * -1;
                        this.movimientosMerca.setCodEmpr(this.envios.getEnviosPK().getCodEmpr());
                        this.movimientosMerca.setCodMerca(enviosDetAnular.getEnviosDetPK().getCodMerca());
                        this.movimientosMerca.setCodDepo(this.envios.getDepoDestino());
                        this.movimientosMerca.setCantCajas(cantidadesCajasAnular.longValue());
                        this.movimientosMerca.setCantUnid(cantidadesUnidadesAnular.longValue());
                        Depositos destinoAnular = this.depositosFacade.getDepositoPorCodigo(this.envios.getDepoDestino());
                        this.movimientosMerca.setCodZona(destinoAnular.getZonas().getZonasPK().getCodZona());

                        mercaderiasAnularNew = new Mercaderias();
                        mercaderiasAnularNew = this.mercaderiasFacade.buscarPorCodigoMercaderia(enviosDetAnular.getEnviosDetPK().getCodMerca());
                        pesosTotalesAnular = 0.0;

                        pesosCajasAnular = mercaderiasAnularNew.getNpesoCaja().doubleValue();
                        pesosUnidadesAnular = mercaderiasAnularNew.getNpesoUnidad().doubleValue();

                        cajasTotalesAnuluar = 0.0;
                        unidadesTotalesAnular = 0.0;

                        cajasTotalesAnuluar = cantidadesCajasAnular * pesosCajasAnular;

                        unidadesTotalesAnular = cantidadesUnidadesAnular * pesosUnidadesAnular;

                        pesosTotalesAnular = (cajasTotalesAnuluar*-1) + (unidadesTotalesAnular*-1);

                        this.movimientosMerca.setNpeso(BigDecimal.valueOf(pesosTotalesAnular));
                        this.movimientosMerca.setManulado(new Short("-1"));
                        this.movimientosMerca.setFmovim(new Date());
                        this.movimientosMerca.setCtipoDocum("EN");
                        this.movimientosMerca.setNdocum(enviosDetAnular.getEnviosDetPK().getNroEnvio());
                        this.movimientosMerca.setIexentas(0L);
                        this.movimientosMerca.setIgravadas(0L);
                        this.movimientosMerca.setImpuestos(0L);
                        this.movimientosMerca.setFalta(new Date());
                        this.movimientosMerca.setCusuario("admin");
                        this.movimientosMerca.setOldCajas(0L);
                        try {
                            movimientosMercaFacade.insertarMovimientosMerca(movimientosMerca);

                        } catch (Exception e) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Error Insercion Movimientos Mercaderias - Destino"));
                        }

                    }

                    this.envios.setMestado('X');
                    enviosFacade.edit(envios);

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Nro Envio Anulado con éxito."));

                    instanciar();

                    RequestContext.getCurrentInstance().execute("PF('dlgEditEnvios').hide();");

                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {
        try {
            if (this.envios.getFenvio() == null) {
                this.setHabBtnEdit(true);

            } else {
                this.setHabBtnEdit(false);
                this.canalesVenta = new CanalesVenta();
                this.canalesVenta.setCodCanal(this.envios.getCodCanal());
                depositoPkk = new DepositosPK();
                depositoPkk.setCodDepo(this.envios.getDepoOrigen());
                depositoPkk.setCodEmpr(this.envios.getEnviosPK().getCodEmpr());
                this.origen = new Depositos();
                this.origen.setDepositosPK(new DepositosPK());
                this.origen.setDepositosPK(depositoPkk);
                depositoPkk = new DepositosPK();
                depositoPkk.setCodDepo(this.envios.getDepoDestino());
                depositoPkk.setCodEmpr(this.envios.getEnviosPK().getCodEmpr());
                this.destino = new Depositos();
                this.destino.setDepositosPK(new DepositosPK());
                this.destino.setDepositosPK(depositoPkk);
                this.destino.setDepositosPK(depositoPkk);
                this.movimientosMerca = new MovimientosMerca();
                this.movimientosMerca.setCodEntregador(this.envios.getCodEntregador());
                this.listaEnviosDet = new ArrayList<EnviosDet>();
                this.listaEnviosDet.addAll(this.envios.getEnviosDetCollection());
                this.cantidadItem = this.listaEnviosDet.size();

            }

        } catch (Exception ex) {
            this.setHabBtnEdit(true);
        }
    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (envios != null) {

            if (envios.getXobs() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarEnvios').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevEnvios').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarEnvios').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevEnvios').hide();");

    }

    public void buscadorArticuloTab(AjaxBehaviorEvent event) {
        try {

            if (origen.getDepositosPK().getCodDepo() == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Se requiere deposito origen para continuar."));
                this.articulo = "";
                return;

            }

            if (canalesVenta.getCodCanal() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Se requiere canal de venta para continuar."));
                this.articulo = "";
                return;
            }

            if ("".equals(canalesVenta.getCodCanal())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Se requiere canal de venta para continuar."));
                this.articulo = "";
                return;
            }

            if (!this.articulo.equals("")) {

                this.existencias = (this.existenciasFacade.buscarPorCodigoDepositoOrigenMerca(articulo, this.origen.getDepositosPK().getCodDepo(), canalesVenta.getCodCanal())).get(0);

                if (this.existencias.getMercaderias().getMercaderiasPK().getCodMerca() == null) {

                    this.existencias = new Existencias();
                    this.existencias.setExistenciasPK(new ExistenciasPK());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Mercaderia no encontrada en el deposito origen o Inactiva."));
                    this.articulo = "";
                }

            } else {

                this.existencias = new Existencias();
                this.existencias.setExistenciasPK(new ExistenciasPK());
            }
        } catch (Exception e) {
            this.articulo = "";
            this.existencias = new Existencias();
            this.existencias.setExistenciasPK(new ExistenciasPK());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Mercaderia no encontrada en el deposito origen o Inactiva."));
        }
    }

    public Existencias getExistencias() {
        return existencias;
    }

    public void setExistencias(Existencias existencias) {
        this.existencias = existencias;
    }

    public String getArticulo() {
        return this.articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
        System.out.println(this.articulo);
    }

    public EnviosDetPK getEnviosDetPkk() {
        return enviosDetPkk;
    }

    public void setEnviosDetPkk(EnviosDetPK enviosDetPkk) {
        this.enviosDetPkk = enviosDetPkk;
    }

    public EnviosPK getEnviosPkk() {
        return enviosPkk;
    }

    public void setEnviosPkk(EnviosPK enviosPkk) {
        this.enviosPkk = enviosPkk;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public Integer getCantidadItem() {
        return cantidadItem;
    }

    public void setCantidadItem(Integer cantidadItem) {
        this.cantidadItem = cantidadItem;
    }

    public LazyDataModel<Envios> getModel() {
        return model;

    }

    public void buscadorEntregador(AjaxBehaviorEvent event) {
        try {

            if ((this.destino.getDepositosPK().getCodDepo()) != 0) {

                this.empleados = (this.empleadosFacade.getNombreEmpleadoEntregador(this.destino.getDepositosPK().getCodDepo()));
                this.movimientosMerca.setCodEntregador(this.empleados.getEmpleadosPK().getCodEmpleado());
                if (this.empleados == null) {

                    this.empleados = new Empleados();

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Receptor No encontrado."));

                }

            } else {
                this.movimientosMerca.setCodEntregador(null);
                this.empleados = new Empleados();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error en la busqueda del Receptor"));
            }
        } catch (Exception e) {
            this.movimientosMerca.setCodEntregador(null);
            this.empleados = new Empleados();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error existe mas de un Receptor del deposito destino"));
        }

    }

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public void verView() {
        try {

            if ("".equals(this.envios.getFenvio())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecciones un item", "Se necesita seleccionar un item para visualizar."));
                return;

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void deleteRows(String codMercaSacarLista) {
        try {

            Iterator itr = this.listaEnviosDet.iterator();
            while (itr.hasNext()) {
                EnviosDet enviosDetallesSacar = (EnviosDet) itr.next();
                if (enviosDetallesSacar.getEnviosDetPK().getCodMerca().equals(codMercaSacarLista)) {

                    setCantidadItem(this.cantidadItem - 1);

                    /*calculo del peso total*/
                    Mercaderias mercaderiasNew = new Mercaderias();
                    mercaderiasNew = this.mercaderiasFacade.buscarPorCodigoMercaderia(codMercaSacarLista);
                    Double cantidadesCajas = enviosDetallesSacar.getCantCajas().doubleValue();
                    Double cantidadesUnidades = enviosDetallesSacar.getCantUnid().doubleValue();
                    Double pesosCajas = mercaderiasNew.getNpesoCaja().doubleValue();
                    Double pesosUnidades = mercaderiasNew.getNpesoUnidad().doubleValue();
                    Double cajasTotales = 0.0;
                    Double unidadesTotales = 0.0;
                    Double pesoReal = 0.0;
                    cajasTotales = cantidadesCajas * pesosCajas;
                    unidadesTotales = cantidadesUnidades * pesosUnidades;
                    pesoReal = cajasTotales + unidadesTotales;
                    
                    pesoReal = getCalculoPesos()-pesoReal;
                    if(pesoReal<0.0)
                        pesoReal=0.0;
                    setCalculoPesos(pesoReal);
                    
                    /*ELIMINAR*/
                    itr.remove();
                    return;
                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "No se pudo eliminar de la grilla"));

            return;
        }

    }
}
