package bean;

import dao.ConductoresFacade;
import dao.DepositosFacade;
import dao.TransportistasFacade;
import dao.ZonasFacade;
import entidad.Conductores;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Transportistas;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class DepositosBean implements Serializable {

    @EJB
    private DepositosFacade depositosFacade;

    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private TransportistasFacade transportistasFacade;

    @EJB
    private ConductoresFacade conductoresFacade;

    private String filtro = "";

    private Depositos depositos = new Depositos();
    private Zonas zonas = new Zonas();
    private Conductores conductores = new Conductores();
    private Transportistas transportistas = new Transportistas();
    private List<Depositos> listaDepositos = new ArrayList<Depositos>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public DepositosBean() {

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

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public List<Depositos> getListaDepositos() {
        return listaDepositos;
    }

    public void setListaDepositos(List<Depositos> listaDepositos) {
        this.listaDepositos = listaDepositos;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public Transportistas getTransportistas() {
        return transportistas;
    }

    public void setTransportistas(Transportistas transportistas) {
        this.transportistas = transportistas;
    }

    public Conductores getConductores() {
        return conductores;
    }

    public void setConductores(Conductores conductores) {
        this.conductores = conductores;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaDepositos = new ArrayList<Depositos>();
        this.depositos = new Depositos();
        this.depositos.setDepositosPK(new DepositosPK());

        this.zonas = new Zonas(new ZonasPK());

        this.transportistas = new Transportistas();
        this.conductores = new Conductores();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";

        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.depositos = new Depositos();
        listaDepositos = new ArrayList<Depositos>();
        this.depositos = new Depositos(new DepositosPK());

        this.zonas = new Zonas(new ZonasPK());

        this.transportistas = new Transportistas();
        this.conductores = new Conductores();

    }

    public List<Depositos> listar() {
        listaDepositos = depositosFacade.findAll();
        return listaDepositos;
    }

    public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Depositos> aux = new ArrayList<Depositos>();

            if ("".equals(this.depositos.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
                aux = depositosFacade.buscarPorDescripcion(depositos.getXdesc());

                /*if (aux.size() > 0) {
                    valido = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ya existe."));
                }*/
            }
        } catch (Exception e) {
            valido = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al validar los datos."));
        }

        return valido;
    }

    public void insertar() {
        try {

            if (validarDeposito().equals(true)) {

                /*if (this.depositos.getPersEmail() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Debe cargar e-mail."));
                    return;
                }

                if (this.Persona.getPersTelefono() == null && this.Persona.getPersCelular() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Debe cargar telefono o celular."));
                    return;
                }

                if (this.Persona.getPersSexo() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Debe cargar el genero."));
                    return;
                }*/
                //String usu = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
                DepositosPK pk = null;

                depositos.setDepositosPK(pk);

                depositos.setZonas(zonas);
                depositos.setXdesc(depositos.getXdesc().toUpperCase());
                depositos.setCodTransp(transportistas);
                depositos.setCodConductor(conductores);
                depositos.setFalta(new Date());
                depositos.setCusuario("admin");

                depositosFacade.insertarDeposito(depositos);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

//                RequestContext.getCurrentInstance().update("formDepositoss");
                PrimeFaces.current().ajax().update("formDepositoss");
//                RequestContext.getCurrentInstance().execute("PF('dlgNuevDepo').hide();");
                PrimeFaces.current().executeScript("PF('dlgNuevDepo').hide();");
                instanciar();

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.depositos.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                depositos.setCusuarioModif("a");
                depositos.setFultimModif(new Date());
                depositos.setZonas(zonas);
                depositos.setCodConductor(conductores);
                depositos.setCodTransp(transportistas);

                depositosFacade.edit(depositos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

//                RequestContext.getCurrentInstance().execute("PF('dlgEditDepo').hide();");
                PrimeFaces.current().executeScript("PF('dlgEditDepo').hide();");
            }

        } catch (Exception e) {
            
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() throws SQLException {
        try {

            depositosFacade.remove(depositos);
            this.depositos = new Depositos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
//            RequestContext.getCurrentInstance().execute("PF('dlgInacPers').hide();");
            PrimeFaces.current().executeScript("PF('dlgInacPers').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.depositos.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void getDatosEditar() {

        this.zonas = depositos.getZonas();
        this.conductores = depositos.getCodConductor();
        this.transportistas = depositos.getCodTransp();

    }

    public void buscadorZonaTab(AjaxBehaviorEvent event) {
        try {

            if (this.zonas != null) {
                if (!this.zonas.getZonasPK().getCodZona().equals("")) {

                    this.zonas = this.zonasFacade.buscarPorCodigo(this.zonas.getZonasPK().getCodZona());

                    if (this.zonas == null) {

                        this.zonas = new Zonas();
                        this.zonas.setZonasPK(new ZonasPK());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "No encontrado."));
                    }
                }
            } else {

                this.zonas = new Zonas();
                this.zonas.setZonasPK(new ZonasPK());
            }
        } catch (Exception e) {

            this.zonas = new Zonas();
            this.zonas.setZonasPK(new ZonasPK());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error en la busqueda"));
        }
    }

    public void buscadorTransoprtistaTab(AjaxBehaviorEvent event) {
        try {

            if (this.transportistas.getCodTransp() != null) {
                if (!this.transportistas.getCodTransp().equals("")) {

                    this.transportistas = this.transportistasFacade.buscarPorCodigo(this.transportistas.getCodTransp() + "");

                    if (this.transportistas == null) {

                        this.transportistas = new Transportistas();

                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "No encontrado."));
                    }
                }
            } else {

                this.transportistas = new Transportistas();
            }
        } catch (Exception e) {

            this.transportistas = new Transportistas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error en la busqueda"));
        }
    }

    public void buscadorConductorTab(AjaxBehaviorEvent event) {
        try {

            if (this.conductores.getCodConductor() != null) {
                if (!this.conductores.getCodConductor().equals("")) {

                    this.conductores = this.conductoresFacade.buscarPorCodigo(this.conductores.getCodConductor() + "");

                    if (this.conductores == null) {

                        this.conductores = new Conductores();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "No encontrado."));
                    }
                }
            } else {

                this.conductores = new Conductores();
            }
        } catch (Exception e) {

            this.conductores = new Conductores();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error en la busqueda"));
        }
    }

    public String tipoDeposito(String tipo) {

        String retorno = "";

        String aux = tipo;

        if (aux.equals("F")) {
            retorno = "Fijo";
        }
        if (aux.equals("M")) {
            retorno = "Movil";
        }
        if (aux.equals("G")) {
            retorno = "Salida Gratuita";
        }
        if (aux.equals("E")) {
            retorno = "Externo";
        }
        return retorno;

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (depositos != null) {

            if (depositos.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
//            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarDepo').show();");
            PrimeFaces.current().executeScript("PF('dlgSinGuardarDepo').show();");
        } else {
//            RequestContext.getCurrentInstance().execute("PF('dlgNuevDepo').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevDepo').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
//        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarDepo').hide();");
        PrimeFaces.current().executeScript("PF('dlgSinGuardarDepo').hide();");
//        RequestContext.getCurrentInstance().execute("PF('dlgNuevDepo').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevDepo').hide();");
    }

}
