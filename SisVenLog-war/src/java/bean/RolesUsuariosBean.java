package bean;

import dao.ConductoresFacade;
import dao.RolesFacade;
import dao.TransportistasFacade;
import dao.ZonasFacade;
import entidad.Conductores;
import entidad.Roles;
import entidad.RolesPK;
import entidad.Transportistas;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class RolesUsuariosBean implements Serializable {

    @EJB
    private RolesFacade rolesFacade;

    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private TransportistasFacade transportistasFacade;

    @EJB
    private ConductoresFacade conductoresFacade;

    private String filtro = "";

    private Roles roles = new Roles();
    private Zonas zonas = new Zonas();
    private Conductores conductores = new Conductores();
    private Transportistas transportistas = new Transportistas();
    private List<Roles> listaRoles = new ArrayList<Roles>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public RolesUsuariosBean() {

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

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public List<Roles> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Roles> listaRoles) {
        this.listaRoles = listaRoles;
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

        listaRoles = new ArrayList<Roles>();
        this.roles = new Roles();
        this.roles.setRolesPK(new RolesPK());

        this.zonas = new Zonas();
        zonas.setZonasPK(new ZonasPK());

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
        this.roles = new Roles();
    }

    public List<Roles> listar() {
        listaRoles = rolesFacade.findAll();
        return listaRoles;
    }

    public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Roles> aux = new ArrayList<Roles>();

            if ("".equals(this.roles.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
                //aux = rolesFacade.buscarPorDescripcion(roles.getXdesc());

                if (aux.size() > 0) {
                    valido = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ya existe."));
                }
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

                /*if (this.roles.getPersEmail() == null) {
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
                RolesPK pk = null;

                roles.setRolesPK(pk);

                roles.setFalta(new Date());
                roles.setCusuario("admin");

                rolesFacade.create(roles);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                RequestContext.getCurrentInstance().execute("PF('dlgNuevDepo').hide();");

                instanciar();
                        

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void editar() {
        try {

            
            if ("".equals(this.roles.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                
                rolesFacade.edit(roles);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();
                
                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditDepo').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void borrar() {
        try {
 
            rolesFacade.remove(roles);
            this.roles = new Roles();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacPers').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if (null != this.roles.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }


}
