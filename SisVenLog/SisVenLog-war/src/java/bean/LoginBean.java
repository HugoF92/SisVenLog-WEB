package bean;

import dao.RolesAplicacionesFacade;
import dao.UsuariosFacade;
import entidad.RolesAplicaciones;
import entidad.Usuarios;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import util.conectar_BD;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private RolesAplicacionesFacade rolesAplicacionesFacade;

    private RolesAplicaciones rolesAplicaciones;

    private String user = "";
    private String password = "";

    private Usuarios usuarios;

    private conectar_BD bd = new conectar_BD();

    @EJB
    UsuariosFacade usuariosFacade;

    public LoginBean() {

        //instanciar();
    }

    public void ingresar() {
        try {

            if (bd.conectar(user, password)) {

                ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
                String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", this.user);

                //this.user = "";
                this.password = "";

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido.", this.user));

                String a = ctxPath + "/principal.xhtml";
                ctx.redirect(a);
                
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN, "Credenciales Invalidas", "favor Verificar"));
            }

            /*if (ingresar.equals("activo")) {

                ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
                String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();

                try {

                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", this.user);

                    this.user = "";
                    this.password = "";

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido.", this.user));

                    String a = ctxPath + "/principal.xhtml";
                    ctx.redirect(a);

                } catch (IOException e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al direccionar la página.", null));
                    //return;
                }

            } else {
                if (ingresar.equals("bloqueado")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario se encuentra bloqueado.", null));
                    //return;
                } else if (ingresar.equals("inactivo")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario se encuentra inactivo.", null));
                    //return;
                } else if (ingresar.equals("vacio")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario inexistente o Contraseña incorrecta.", null));
                    //return;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Verifique los datos y vuelva a intentarlo", null));
                    //return;
                }
            }*/
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al iniciar sesión", null));
        }
    }

    public String validarPermisosMenu(String aplicacion) {

        String respuesta;
        
        String usu = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();

        try {

            rolesAplicaciones = rolesAplicacionesFacade.validarPermisosMenu(usu, aplicacion);

            if (rolesAplicaciones != null) {

                respuesta = aplicacion+".xhtml";

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Atención", "Privilegios insuficientes para utilizar este modulo"));

                respuesta = "principal.xhtml";
            }

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al validar permisos de usaurio en el menu", e.getMessage()));

            respuesta = "principal";
        }

        return respuesta;
    }

    /*Getter & Setters*/
    public RolesAplicaciones getRolesAplicaciones() {
        return rolesAplicaciones;
    }

    public void setRolesAplicaciones(RolesAplicaciones rolesAplicaciones) {
        this.rolesAplicaciones = rolesAplicaciones;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

}
