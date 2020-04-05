package bean;

import dao.EmpleadosFacade;
import dao.UsuariosFacade;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.Usuarios;
import entidad.UsuariosPK;
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
public class UsuariosBean implements Serializable {

    @EJB
    private UsuariosFacade usuariosFacade;

    @EJB
    private EmpleadosFacade empleadosFacade;

    private String filtro = "";
    private String usuario = "";

    private Usuarios usuarios = new Usuarios();
    private List<Usuarios> listaUsuarios = new ArrayList<Usuarios>();
    private List<Empleados> listaEmpleados = new ArrayList<Empleados>();

    private Empleados empleados = new Empleados();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public UsuariosBean() {

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

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public List<Usuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<Empleados> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaUsuarios = new ArrayList<Usuarios>();
        listaEmpleados = new ArrayList<Empleados>();
        this.usuarios = new Usuarios();
        this.empleados = new Empleados(new EmpleadosPK());
        this.usuarios.setUsuariosPK(new UsuariosPK());

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        this.usuario = "";

        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public void nuevo() {
        this.usuarios = new Usuarios();
    }

    public List<Usuarios> listar() {
        listaUsuarios = usuariosFacade.findAll();
        return listaUsuarios;
    }

    public List<Empleados> listarEmpleados() {
        this.listaEmpleados = empleadosFacade.listarEmpleados();
        return this.listaEmpleados;
    }

    public void insertar() {
        try {
            
            //usuarios.setUsuariosPK(new UsuariosPK(this.usuario, new Short("2")));
            usuarios.getUsuariosPK().setCodEmpr(new Short("2"));
            usuarios.setEmpleados(empleados);

            usuarios.setFalta(new Date());
            usuarios.setCusuario("admin");

            usuariosFacade.create(usuarios);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            PrimeFaces.current().executeScript("PF('dlgNuevUsuarios').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void editar() {
        try {

                usuarios.setEmpleados(empleados);
                usuariosFacade.edit(usuarios);
                 usuarios.setEmpleados(empleados);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditUsuarios').hide();");


        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void borrar() {
        try {

            usuariosFacade.remove(usuarios);
            this.usuarios = new Usuarios();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacPers').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if (null != this.usuarios.getUsuariosPK().getCodUsuario()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

}
