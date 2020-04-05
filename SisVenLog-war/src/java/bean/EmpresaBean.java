package bean;

import dao.EmpresasFacade;
import entidad.Empresas;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class EmpresaBean implements Serializable {

    @EJB
    private EmpresasFacade empresasFacade;

    private String filtro = "";

    private Empresas empresas = new Empresas();
    private List<Empresas> listaEmpresas = new ArrayList<Empresas>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public EmpresaBean() {

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

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public List<Empresas> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Empresas> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaEmpresas = new ArrayList<Empresas>();
        this.empresas = new Empresas();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";

        listar();
        RequestContext.getCurrentInstance().update("formEmpresa");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.empresas = new Empresas();
    }

    public List<Empresas> listar() {
        listaEmpresas = empresasFacade.findAll();
        return listaEmpresas;
    }

    public void insertar() {
        try {

            empresas.setXrazonSocial(empresas.getXrazonSocial().toUpperCase());
            empresas.setXruc(empresas.getXruc().toUpperCase());
            empresas.setXdirec(empresas.getXdirec().toUpperCase());
            empresas.setXcontacto(empresas.getXcontacto().toUpperCase());
            empresas.setXtelef(empresas.getXtelef().toUpperCase());
            empresas.setFalta(new Date());
            empresas.setCusuario("admin");

            empresasFacade.insertarEmpresa(empresas);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevEmpresa').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.empresas.getXrazonSocial())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                empresas.setXrazonSocial(empresas.getXrazonSocial().toUpperCase());
                empresasFacade.edit(empresas);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditEmpresa').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            empresasFacade.remove(empresas);
            this.empresas = new Empresas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacEmpresa').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.empresas.getXrazonSocial()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (empresas != null) {

            if (empresas.getXrazonSocial()!= null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarEmpresa').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevEmpresa').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarEmpresa').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevEmpresa').hide();");

    }

}
