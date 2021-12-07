package bean;

import dao.EmpresasFacade;
import dao.ComprasFacade;
import dao.MercaderiasFacade;
import dao.ProveedoresFacade;
import dao.MercaTolerarFacade;
import dto.CompraDto;
import entidad.Empresas;
import entidad.Compras;
import entidad.ComprasDet;
import entidad.Mercaderias;
import entidad.Proveedores;
import entidad.MercaTolerar;
import entidad.MercaTolerarPK;
import entidad.MercaderiasPK;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import multitenancy.TenantInterceptor;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
//@Named
//@ViewScoped
//@Interceptors(TenantInterceptor.class)
public class MercaTolerarBean implements Serializable {

    @EJB
    //@Inject
    private MercaTolerarFacade mercaTolerarFacade;

    @EJB
    private EmpresasFacade empresaFacade;

    @EJB
    private ComprasFacade comprasFacade;

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private ProveedoresFacade proveedoresFacade;

    private String filtro = "";
    private String nroPuntoEstabLbl = "0";
    private String nroPuntoExpedLbl = "0";
    private String nroFactLbl = "0";
    private long nroFact;
    private String txtproveedores;
    private String ffactur;

    private MercaTolerar mercaTolerar = new MercaTolerar();
    private Mercaderias mercaderias = new Mercaderias();
    private Proveedores proveedores = new Proveedores();
    private Compras compras = new Compras();
    private List<MercaTolerar> listaMercaTolerar = new ArrayList<MercaTolerar>();
    private List<CompraDto> aux_compras = new ArrayList<CompraDto>();
    private List<Proveedores> listaProveedores = new ArrayList<Proveedores>();
    private List<Mercaderias> listaMercaderias = new ArrayList<Mercaderias>();
    private Collection<ComprasDet> comprasDetCollection;

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public MercaTolerarBean() {

        //instanciar();
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

    public MercaTolerar getMercaTolerar() {
        return mercaTolerar;
    }

    public void setMercaTolerar(MercaTolerar mercaTolerar) {
        this.mercaTolerar = mercaTolerar;
    }

    public List<MercaTolerar> getListaMercaTolerar() {
        listaMercaTolerar = mercaTolerarFacade.findAll();
        return listaMercaTolerar;
    }

    public void setListaMercaTolerar(List<MercaTolerar> listaMercaTolerar) {
        this.listaMercaTolerar = listaMercaTolerar;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public List<Mercaderias> getListaMercaderias() {
        return listaMercaderias;
    }

    public void setListaMercaderias(List<Mercaderias> listaMercaderias) {
        this.listaMercaderias = listaMercaderias;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    public String getFfactur() {
        return ffactur;
    }

    public void setFfactur(String ffactur) {
        this.ffactur = ffactur;
    }

    public Compras getCompras() {
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
    }

    public String getNroPuntoEstabLbl() {
        return nroPuntoEstabLbl;
    }

    public void setNroPuntoEstabLbl(String nroPuntoEstabLbl) {
        this.nroPuntoEstabLbl = nroPuntoEstabLbl;
    }

    public String getNroPuntoExpedLbl() {
        return nroPuntoExpedLbl;
    }

    public void setNroPuntoExpedLbl(String nroPuntoExpedLbl) {
        this.nroPuntoExpedLbl = nroPuntoExpedLbl;
    }

    public String getNroFactLbl() {
        return nroFactLbl;
    }

    public void setNroFactLbl(String nroFactLbl) {
        this.nroFactLbl = nroFactLbl;
    }

    public long getNroFact() {
        return nroFact;
    }

    public void setNroFact(long nroFact) {
        this.nroFact = nroFact;
    }

    public String getTxtproveedores() {
        return txtproveedores;
    }

    public void setTxtproveedores(String txtproveedores) {
        this.txtproveedores = txtproveedores;
    }

    public Collection<ComprasDet> getComprasDetCollection() {
        return comprasDetCollection;
    }

    public void setComprasDetCollection(Collection<ComprasDet> comprasDetCollection) {
        this.comprasDetCollection = comprasDetCollection;
    }

    @PostConstruct
    public void instanciar() {
        //new ArrayList<>()
        listaMercaTolerar = new ArrayList<MercaTolerar>();
        this.mercaTolerar = new MercaTolerar();
        this.mercaTolerar.setMercaTolerarPK(new MercaTolerarPK());

        this.mercaderias = new Mercaderias(new MercaderiasPK());

        this.proveedores = new Proveedores();

        this.listaProveedores = new ArrayList<>();
        this.listaMercaderias = new ArrayList<>();
        this.listaMercaTolerar = new ArrayList<>();

        this.nroPuntoEstabLbl = "0";
        this.nroPuntoExpedLbl = "0";
        this.nroFactLbl = "0";
        this.ffactur = "";
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";

        listar();

        //RequestContext.getCurrentInstance().update("formTolerancia");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.mercaTolerar = new MercaTolerar();
        listaMercaTolerar = new ArrayList<MercaTolerar>();
        this.mercaTolerar = new MercaTolerar(new MercaTolerarPK());

        this.mercaderias = new Mercaderias(new MercaderiasPK());
        this.proveedores = new Proveedores();
    }

    public List<MercaTolerar> listar() {
        listaMercaTolerar = mercaTolerarFacade.findAll();
        return listaMercaTolerar;
    }

    public void buscarPorNroFactura(long lNrofact) {
        try {
            //this.aux_compras = comprasFacade.buscarFacturaCompraPorNroFactura(lNrofact);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "El numero de factura compra ingresado no existe."));
        }
    }

    public Boolean validarMercaTolerar() {
        boolean valido = true;

        try {
            if ((nroPuntoEstabLbl.equals(0) || nroPuntoEstabLbl.equals(""))
                    || (nroPuntoExpedLbl.equals(0) || nroPuntoExpedLbl.equals(""))
                    || (nroFactLbl.equals(0) || nroFactLbl.equals(""))) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe ingresar todos los campos de la factura"));
            }

            if ("".equals(this.mercaTolerar.getXobs())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            }
            if ("".equals(this.mercaTolerar.getItolerar())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar un importe de tolerancia."));
            }
            if (mercaderias == null) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar una mercaderia"));
            }
            if (proveedores == null) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar un proveedor"));
            }
        } catch (Exception e) {
            valido = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al validar los datos."));
        }
        return valido;
    }

    public void guardar() {
        try {

            if (validarMercaTolerar()) {
                String usuario = null;
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario") != null) {
                    usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                MercaTolerarPK pk = new MercaTolerarPK();
                pk.setCodEmpr(new Short("2"));
                pk.setCodMerca(mercaderias.getMercaderiasPK().getCodMerca());
                pk.setCodProveed(proveedores.getCodProveed());
                pk.setNrofact(obtenerNroFacturaCompleto());
                mercaTolerar.setMercaTolerarPK(pk);
                mercaTolerar.setFalta(new Date());
                mercaTolerar.setFultimModif(new Date());
                mercaTolerar.setCusuario(usuario);
                mercaTolerar.setCusuarioModif(usuario);
                mercaTolerar.setFfactur(compras.getComprasPK().getFfactur());
                mercaTolerar.setProveedores(proveedores);
                //metodo 1
                mercaderias.getMercaderiasPK().setCodEmpr(new Short("2"));
                mercaTolerar.setMercaderias(mercaderias);

                //metodo 2
                //MercaderiasPK pkmer = new MercaderiasPK();
                //pkmer.setCodEmpr(new Short("2"));
                //pkmer.setCodMerca(mercaderias.getMercaderiasPK().getCodMerca());
                //metodo 3
                //Mercaderias mercaderiasNew = new Mercaderias();
                //mercaderiasNew = this.mercaderiasFacade.buscarPorCodigoMercaderia(mercaderias.getMercaderiasPK().getCodMerca());
                //mercaTolerar.setMercaderias(mercaderiasNew);
                mercaTolerarFacade.create(mercaTolerar);

                //mercaTolerarFacade.newInsertarMercaTolerar(mercaTolerar);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));
                //limpiar();
                listar();
                RequestContext.getCurrentInstance().execute("PF('dlgNuevTolerancia').hide();");
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    private long obtenerNroFacturaCompleto() {
        //long nroFacturaCompleto = (long) (nroPuntoEstabLbl * 1000000000.00 + nroPuntoExpedLbl * 10000000.00 + nroFactLbl);
        //while(nroPuntoEstabLbl.length()<3){nroPuntoEstabLbl="0"+nroPuntoEstabLbl;}
        //while(nroPuntoExpedLbl.length()<3){nroPuntoExpedLbl="0"+nroPuntoExpedLbl;}
        //while(nroFactLbl.length()<7){nroFactLbl="0"+nroFactLbl;}
        
        long nroFacturaCompleto = Long.parseLong(nroPuntoEstabLbl + nroPuntoExpedLbl + nroFactLbl);
        return nroFacturaCompleto;
    }

    public void obtenerFacturaCompra() {
        if ((!nroPuntoEstabLbl.equals("0") && !nroPuntoEstabLbl.equals("") && nroPuntoEstabLbl != null)
                && (!nroPuntoExpedLbl.equals("0") && !nroPuntoExpedLbl.equals("") && nroPuntoExpedLbl != null)
                && (!nroFactLbl.equals("0") && !nroFactLbl.equals(""))) {
            //List<CompraDto> facturaCompra = comprasFacade.comprasByNroFactu(nroPuntoEstabLbl+nroPuntoExpedLbl+nroFactLbl);
            List<CompraDto> facturaCompra = comprasFacade.buscarFacturaCompraPorNroFactura(nroPuntoEstabLbl + nroPuntoExpedLbl + nroFactLbl);
            this.nroFact = 0;

            if (facturaCompra == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe numero de factura de compra."));
                //return null;
            } else {
                if (facturaCompra.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe numero de factura de compra."));
                    //return null;
                } else {
                    short codProveed;
                    this.txtproveedores = "";
                    this.nroFact = Long.parseLong(nroPuntoEstabLbl + nroPuntoExpedLbl + nroFactLbl);

                    for (CompraDto faccom : facturaCompra) {
                        compras = faccom.getCompra();
                        this.ffactur = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(compras.getComprasPK().getFfactur());
                        codProveed = compras.getComprasPK().getCodProveed();
                        this.txtproveedores = this.txtproveedores + Long.toString(codProveed) + ",";
                    }
                    this.txtproveedores = this.txtproveedores.substring(0, this.txtproveedores.length() - 1);
                    this.setListaProveedores(proveedoresFacade.proveedorByIds(this.txtproveedores));
                }
            }
        }

    }

    public void obtenerDetalleCompra() {
        if (this.proveedores != null && nroFact != 0) {

            List<Mercaderias> facturaCompraMercaderias = mercaderiasFacade.buscarMercaderiaCompraPorNroFacturaProveedor(nroFact, proveedores.getCodProveed());

            if (facturaCompraMercaderias == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe mercaderias asociadas a la factura de compra."));
                //return null;
            } else {
                if (facturaCompraMercaderias.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención ", "No existe mercaderias asociadas a la factura de compra."));
                    //return null;
                } else {
                    this.setListaMercaderias(facturaCompraMercaderias);
                }
            }
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.mercaTolerar.getProveedores().getXnombre()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void onRowDoubleClick2(SelectEvent event) {

        if ("" != this.mercaTolerar.getProveedores().getXnombre()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void onRowDoubleClick(final SelectEvent event) {
        //MercaTolerar obj = (MercaTolerar) event.getObject();
        //RequestContext.getCurrentInstance().execute("PF('dlgVisuaTolerancia').show();");
        //RequestContext.getCurrentInstance().execute("PF('formVisualizarTolerancia').show();");
        // rest of your logic
        //PrimeFaces current = PrimeFaces.current();
        //current.executeScript("PF('formVisualizarTolerancia').show();");
    }

    public void limpiar() {
        this.nroPuntoEstabLbl = "0";
        this.nroPuntoExpedLbl = "0";
        this.nroFactLbl = "0";
        this.listaMercaTolerar = null;
        this.mercaTolerar = new MercaTolerar();
        this.proveedores = new Proveedores();
        this.mercaderias = new Mercaderias();
        this.listaProveedores = new ArrayList<>();
        this.listaMercaderias = new ArrayList<>();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);
        this.filtro = "";
        RequestContext.getCurrentInstance().update("formTolerancia");
    }
//###############################################################################################################    
//###############################################################################################################    
//############################################################################################################### 

    public void insertar() {
        try {
            if (validarMercaTolerar().equals(true)) {
                MercaTolerarPK mpk = null;
                mpk.setCodEmpr(new Short("2"));
                mpk.setNrofact(obtenerNroFacturaCompleto());
                mpk.setCodMerca(mercaderias.getMercaderiasPK().getCodMerca());
                mpk.setCodProveed(proveedores.getCodProveed());
                mercaTolerar.setMercaTolerarPK(mpk);
                mercaTolerar.setMercaderias(mercaderias);
                mercaTolerar.setProveedores(proveedores);
                mercaTolerar.setCusuario("admin");
                mercaTolerar.setFalta(new Date());
                mercaTolerarFacade.create(mercaTolerar);

                /*                        
                mercaTolerar.getMercaTolerarPK().setCodEmpr(new Short("2"));
                mercaTolerar.getMercaTolerarPK().setNrofact(compras.getComprasPK().getNrofact());
                mercaTolerar.getMercaTolerarPK().setCodMerca(mercaderias.getMercaderiasPK().getCodMerca());
                mercaTolerar.getMercaTolerarPK().setCodProveed(proveedores.getCodProveed());
                //mercaTolerar.setCusuario("admin");
                mercaTolerar.setFalta(new Date());

                mercaTolerarFacade.create(mercaTolerar);
                 */
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));
                RequestContext.getCurrentInstance().update("formTolerancia");
                RequestContext.getCurrentInstance().execute("PF('dlgNuevTolerancia').hide();");

                instanciar();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al validar los datos."));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    private String obtenerNroFacturaCompletoConFormato() {
        String puntoEstablec = String.valueOf(nroPuntoEstabLbl);
        String puntoExped = String.valueOf(nroPuntoExpedLbl);
        String nroFact = String.valueOf(nroFactLbl);
        String resultado = rellenarConCeros(puntoEstablec, 3) + "-" + rellenarConCeros(puntoExped, 3) + "-" + rellenarConCeros(nroFact, 7);
        return resultado;
    }

    private static String rellenarConCeros(String cadena, int numCeros) {
        String ceros = "";
        for (int i = cadena.length(); i < numCeros; i++) {
            ceros += "0";
        }
        return ceros + cadena;
    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('formSinGuardarTolerancia').hide();");
        RequestContext.getCurrentInstance().execute("PF('formNuevaTolerancia').hide();");

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (mercaTolerar != null) {

            if (mercaTolerar.getProveedores().getXnombre() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('formSinGuardarTolerancia').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('formNuevaTolerancia').hide();");
        }

    }
//#########################################################################################################
//#########################################################################################################
//#########################################################################################################

    public void editar() {
        try {

            if ("".equals(this.mercaTolerar.getXobs())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            }
            if ("".equals(this.mercaTolerar.getItolerar()) || "0".equals(this.mercaTolerar.getItolerar())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar un importe."));
                return;

            } else {

                String usuario = null;
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario") != null) {
                    usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                }
                mercaTolerar.setCusuarioModif(usuario);
                mercaTolerar.setFultimModif(new Date());
                mercaTolerarFacade.edit(mercaTolerar);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                //instanciar();
                limpiar();
                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditTolerancia').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {
            mercaTolerarFacade.remove(mercaTolerar);
            this.mercaTolerar = new MercaTolerar(new MercaTolerarPK());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            //instanciar();
            limpiar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacTolerancia').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }
}
