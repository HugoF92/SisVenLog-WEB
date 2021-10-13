package bean;

import dao.EmpresasFacade;
import dao.ComprasFacade;
import dao.MercaderiasFacade;
import dao.ProveedoresFacade;
import dao.MercaTolerarFacade;
import entidad.Empresas;
import entidad.Compras;
import entidad.Mercaderias;
import entidad.Proveedores;
import entidad.MercaTolerar;
import entidad.MercaTolerarPK;
import entidad.MercaderiasPK;
import java.io.Serializable;
import java.math.BigDecimal;
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
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class MercaTolerarBean implements Serializable {

    @EJB
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
    private Short nroPuntoEstabLbl;
    private Short nroPuntoExpedLbl;
    private long nroFactLbl;
    private MercaTolerar mercaTolerar;
    private List<MercaTolerar> listaMercaTolerar;
    private Empresas empresas;
    private Compras compras;
    private Mercaderias mercaderias;
    private Proveedores proveedores;

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
        this.listaMercaTolerar = this.mercaTolerarFacade.findAll();
        return listaMercaTolerar;
    }

    public void setListaMercaTolerar(List<MercaTolerar> listaMercaTolerar) {
        this.listaMercaTolerar = listaMercaTolerar;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public Compras getCompras() {
        return compras;
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

    public Short getNroPuntoEstabLbl() {
        return nroPuntoEstabLbl;
    }

    public void setNroPuntoEstabLbl(Short nroPuntoEstabLbl) {
        this.nroPuntoEstabLbl = nroPuntoEstabLbl;
    }

    public Short getNroPuntoExpedLbl() {
        return nroPuntoExpedLbl;
    }

    public void setNroPuntoExpedLbl(Short nroPuntoExpedLbl) {
        this.nroPuntoExpedLbl = nroPuntoExpedLbl;
    }

    public long getNroFactLbl() {
        return nroFactLbl;
    }

    public void setNroFactLbl(long nroFactLbl) {
        this.nroFactLbl = nroFactLbl;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public List<MercaTolerar> listar() {
        listaMercaTolerar = mercaTolerarFacade.findAll();
        return listaMercaTolerar;
    }    
//#########################################################################################################
//#########################################################################################################
//#########################################################################################################
    @PostConstruct
    public void init() {
        this.mercaTolerar = new MercaTolerar(new MercaTolerarPK());
        this.proveedores = new Proveedores();
        MercaderiasPK pk_mercaderia = new MercaderiasPK();
        this.mercaderias = new Mercaderias(pk_mercaderia);
    }
    
    public void guardar() {
        try {

            if ((nroPuntoEstabLbl.equals(0) || nroPuntoEstabLbl.equals(""))
                    || (nroPuntoExpedLbl.equals(0) || nroPuntoExpedLbl.equals(""))
                    || (nroFactLbl == 0)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe ingresar todos los campos de la factura"));
                return;
            }

            if (mercaderias == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar una mercaderia"));
                return;
            }
            if (proveedores == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar un proveedor"));
                return;
            }
            MercaTolerarPK pk = new MercaTolerarPK();
            pk.setCodEmpr(new Short("2"));
            pk.setCodMerca(mercaderias.getMercaderiasPK().getCodMerca());
            pk.setCodProveed(proveedores.getCodProveed());
            pk.setNrofact(obtenerNroFacturaCompleto());
            mercaTolerar.setMercaTolerarPK(pk);
            //mercaTolerar.setCusuario("admin");
            mercaTolerar.setFalta(new Date());

            mercaTolerarFacade.create(mercaTolerar);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));
            limpiar();
            RequestContext.getCurrentInstance().execute("PF('dlgNuevTolerancia').hide();");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }
    
    public void limpiar() {
        this.nroPuntoEstabLbl = 0;
        this.nroPuntoExpedLbl = 0;
        this.nroFactLbl = 0;
        this.mercaTolerar = new MercaTolerar();
        this.proveedores = new Proveedores();
        this.mercaderias = new Mercaderias();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);
        this.filtro = "";
        RequestContext.getCurrentInstance().update("formTolerancia");
    }
    
    private long obtenerNroFacturaCompleto() {
        long nroFacturaCompleto = (long) (nroPuntoEstabLbl * 1000000000.00 + nroPuntoExpedLbl * 10000000.00 + nroFactLbl);
        return nroFacturaCompleto;
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
    
    public void onRowSelect(SelectEvent event) {

        if ("" != this.mercaTolerar.getProveedores().getXnombre()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

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

            if ("".equals(this.proveedores.getXnombre())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                proveedores.setXnombre(proveedores.getXnombre().toUpperCase());
                proveedoresFacade.edit(proveedores);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                //instanciar();
                limpiar();
                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditProv').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            proveedoresFacade.remove(proveedores);
            this.proveedores = new Proveedores();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            //instanciar();
            limpiar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacProv').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    //Operaciones
    //Instanciar objetos
    //@PostConstruct
/*    
    public void instanciar() {
        mercaTolerar = new MercaTolerar();
        mercaTolerar.setProveedores(new Proveedores());
        mercaTolerar.setMercaderias(new Mercaderias());
        mercaTolerar.setMercaTolerarPK(new MercaTolerarPK());
        mercaTolerar.setItolerar(BigDecimal.ZERO);

        listaMercaTolerar = new ArrayList<MercaTolerar>();
        //this.mercaTolerar = new MercaTolerar(new MercaTolerarPK());
        this.mercaTolerar = new MercaTolerar();

        this.empresas = new Empresas();

        this.mercaderias = new Mercaderias();

        this.proveedores = new Proveedores();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);
        this.filtro = "";
        RequestContext.getCurrentInstance().update("formTolerancia");
    }

    public void insertar() {
        try {

            if (mercaderias == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar una mercaderia"));
                return;
            }
            if (proveedores == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar un proveedor"));
                return;
            }
            mercaTolerar.getMercaTolerarPK().setCodEmpr(new Short("2"));
            mercaTolerar.getMercaTolerarPK().setNrofact(compras.getComprasPK().getNrofact());
            mercaTolerar.getMercaTolerarPK().setCodMerca(mercaderias.getMercaderiasPK().getCodMerca());
            mercaTolerar.getMercaTolerarPK().setCodProveed(proveedores.getCodProveed());
            //mercaTolerar.setCusuario("admin");
            mercaTolerar.setFalta(new Date());

            mercaTolerarFacade.create(mercaTolerar);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevTolerancia').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }
    
*/
}
