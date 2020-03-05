package bean.listados;

import dao.ExcelFacade;
import dao.PersonalizedFacade;
import dao.RolesFacade;
import dao.UsuariosFacade;
import entidad.Roles;
import entidad.RolesPK;
import entidad.Usuarios;
import entidad.UsuariosPK;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.*;
import javax.faces.bean.*;
import javax.faces.context.*;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class LiAplicaBean {

    @EJB
    private UsuariosFacade usuariosFacade;

    @EJB
    private ExcelFacade excelFacade;

    @EJB
    private RolesFacade rolesFacade;

    private Roles roles;
    private List<Roles> listaRoles;

    private Usuarios usuarios;
    private List<Usuarios> listaUsuarios;

    private String destination = System.getProperty("java.io.tmpdir");

    public LiAplicaBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.roles = new Roles(new RolesPK());
        this.listaRoles = new ArrayList<Roles>();

        this.usuarios = new entidad.Usuarios(new UsuariosPK());
        this.listaUsuarios = new ArrayList<entidad.Usuarios>();

    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        int rolCodigo = 0;
        String usuaCodigo = "";
        String rol = "";
        String usuario = "";

        if (roles == null) {
            rolCodigo = 0;
            rol = "TODOS";
        } else {
            rolCodigo = roles.getRolesPK().getCodRol();
            rol = rolesFacade.find(roles.getRolesPK()).getXdesc();
        }

        if (usuarios == null) {
            usuario = "TODOS";
            usuaCodigo = "TODOS";
        } else {
            usuaCodigo = usuarios.getUsuariosPK().getCodUsuario();
            usuario = usuariosFacade.find(usuarios.getUsuariosPK()).getXnombre();
        }

        if (tipo.equals("VIST")) {
            rep.reporteLiApli(rolCodigo, usuaCodigo, rol, usuario, tipo, "admin");
        } else {
            
            List<Object[]> lista = new ArrayList<Object[]>();
            
            String[] columnas = new String[6];
            
            columnas[0] = "cod_rol";
            columnas[1] = "rol_xdesc";
            columnas[2] = "cod_aplicacion";
            columnas[3] = "app_xdesc";
            columnas[4] = "xnombre";
            columnas[5] = "cod_usuario";
            
            String sql = "SELECT\n"
                    + "	ra.cod_rol\n"
                    + "	,r.xdesc rol_xdesc\n"
                    + "	, ra.cod_aplicacion\n"
                    + "	, a.xdesc app_xdesc\n"
                    + "	,u.xnombre\n"
                    + "     	, u.cod_usuario\n"
                    + "FROM\n"
                    + "	roles_aplicaciones ra\n"
                    + "	, roles_usuarios ru\n"
                    + "	, aplicaciones a\n"
                    + "	, roles r\n"
                    + "	, usuarios u\n"
                    + "WHERE\n"
                    + "	ra.cod_rol = ru.cod_rol\n"
                    + "    	 AND a.cod_aplicacion = ra.cod_aplicacion\n"
                    + "    	 AND ra.cod_rol = r.cod_rol\n"
                    + "    	 AND ru.cod_usuario = u.cod_usuario\n"
                    + "    	 AND (ra.cod_rol  = "+rolCodigo+" or "+rolCodigo+" = 0)\n"
                    + "    	 AND (u.cod_usuario  =  '"+usuaCodigo+"' or '"+usuaCodigo+"' = 'TODOS')\n"
                    + " ORDER BY r.cod_rol, u.cod_usuario, a.cod_aplicacion";
            
            lista = excelFacade.listarParaExcel(sql);
            
            rep.exportarExcel(columnas, lista, "liaplica");
        }

    }

    //Getters & Setters
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

}
