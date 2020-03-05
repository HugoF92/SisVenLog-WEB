package bean;

import dao.GruposCargaFacade;
import dao.RolesAplicacionesFacade;
import entidad.GruposCarga;
import entidad.RolesAplicaciones;
import entidad.RolesAplicacionesPK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class AplicaRolBean implements Serializable {

    @EJB
    private RolesAplicacionesFacade rolesAplicacionesFacade;

    @EJB
    private GruposCargaFacade gruposCargaFacade;

    private String filtro = "";

    private RolesAplicaciones rolesAplicaciones = new RolesAplicaciones();
    private List<RolesAplicaciones> listaRolesAplicaciones = new ArrayList<RolesAplicaciones>();

    private GruposCarga gruposCarga = new GruposCarga();
    private List<GruposCarga> listaGruposCarga = new ArrayList<GruposCarga>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public AplicaRolBean() {

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

    public RolesAplicaciones getRolesAplicaciones() {
        return rolesAplicaciones;
    }

    public void setRolesAplicaciones(RolesAplicaciones rolesAplicaciones) {
        this.rolesAplicaciones = rolesAplicaciones;
    }

    public List<RolesAplicaciones> getListaRolesAplicaciones() {
        return listaRolesAplicaciones;
    }

    public void setListaRolesAplicaciones(List<RolesAplicaciones> listaRolesAplicaciones) {
        this.listaRolesAplicaciones = listaRolesAplicaciones;
    }

    public GruposCarga getGruposCarga() {
        return gruposCarga;
    }

    public void setGruposCarga(GruposCarga gruposCarga) {
        this.gruposCarga = gruposCarga;
    }

    public List<GruposCarga> getListaGruposCarga() {
        return listaGruposCarga;
    }

    public void setListaGruposCarga(List<GruposCarga> listaGruposCarga) {
        this.listaGruposCarga = listaGruposCarga;
    }

    public RolesAplicacionesFacade getRolesAplicacionesFacade() {
        return rolesAplicacionesFacade;
    }

    public void setRolesAplicacionesFacade(RolesAplicacionesFacade rolesAplicacionesFacade) {
        this.rolesAplicacionesFacade = rolesAplicacionesFacade;
    }

    public GruposCargaFacade getGruposCargaFacade() {
        return gruposCargaFacade;
    }

    public void setGruposCargaFacade(GruposCargaFacade gruposCargaFacade) {
        this.gruposCargaFacade = gruposCargaFacade;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaRolesAplicaciones = new ArrayList<RolesAplicaciones>();
        this.rolesAplicaciones = new RolesAplicaciones();
        this.rolesAplicaciones.setRolesAplicacionesPK(new RolesAplicacionesPK());

        listaGruposCarga = new ArrayList<GruposCarga>();
        this.gruposCarga = new GruposCarga();

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
        this.rolesAplicaciones = new RolesAplicaciones();
    }

    public List<RolesAplicaciones> listar() {
        listaRolesAplicaciones = rolesAplicacionesFacade.findAll();
        return listaRolesAplicaciones;
    }

    public List<GruposCarga> listarGruposCarga() {
        listaGruposCarga = gruposCargaFacade.findAll();
        return listaGruposCarga;
    }

    public void insertar() {
        try {

            {

                /*if (this.rolesAplicaciones.getPersEmail() == null) {
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
                RolesAplicacionesPK pk = null;

                rolesAplicaciones.setRolesAplicacionesPK(pk);

                rolesAplicaciones.setFalta(new Date());
                rolesAplicaciones.setCusuario("admin");

                
                
                rolesAplicacionesFacade.insertarRolesAplicaciones(rolesAplicaciones);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

//                RequestContext.getCurrentInstance().execute("PF('dlgNuevRolesAplicaciones').hide();");
                PrimeFaces.current().executeScript("PF('dlgNuevRolesAplicaciones').hide();");

                instanciar();

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.rolesAplicaciones.getRolesAplicacionesPK())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                rolesAplicacionesFacade.edit(rolesAplicaciones);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

//                RequestContext.getCurrentInstance().execute("PF('dlgEditRolesAplicaciones').hide();");
                PrimeFaces.current().executeScript("PF('dlgEditRolesAplicaciones').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void borrar() {
        try {

            rolesAplicacionesFacade.remove(rolesAplicaciones);
            this.rolesAplicaciones = new RolesAplicaciones();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
//            RequestContext.getCurrentInstance().execute("PF('dlgInacRolesAplicaciones').hide();");
            PrimeFaces.current().executeScript("PF('dlgInacRolesAplicaciones').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if (null != this.rolesAplicaciones.getRolesAplicacionesPK()) {
            this.setHabBtnEdit(false);
            this.gruposCarga = this.getGruposCarga();
        } else {
            this.setHabBtnEdit(true);
        }

    }

}
