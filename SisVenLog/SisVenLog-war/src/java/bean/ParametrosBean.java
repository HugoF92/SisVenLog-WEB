package bean;

import dao.ParametrosFacade;
import entidad.Parametros;
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
public class ParametrosBean implements Serializable {

    @EJB
    private ParametrosFacade parametrosFacade;

    private String filtro = "";

    private Parametros parametros = new Parametros();
    private List<Parametros> listaParametros = new ArrayList<Parametros>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public ParametrosBean() {

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

    public Parametros getParametros() {
        return parametros;
    }

    public void setParametros(Parametros parametros) {
        this.parametros = parametros;
    }

    public List<Parametros> getListaParametros() {
        return listaParametros;
    }

    public void setListaParametros(List<Parametros> listaParametros) {
        this.listaParametros = listaParametros;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaParametros = new ArrayList<Parametros>();
        this.parametros = new Parametros();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

//    RequestContext.getCurrentInstance().update("formParametros");
        PrimeFaces.current().ajax().update("formParametros");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.parametros = new Parametros();
    }

    public List<Parametros> listar() {
        listaParametros = parametrosFacade.findAll();
        return listaParametros;
    }


    public void insertar() {
        try {

            parametros.setXdesc(parametros.getXdesc().toUpperCase());
            parametros.setFalta(new Date());
            parametros.setCusuario("admin");

            parametrosFacade.create(parametros);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            PrimeFaces.current().executeScript("PF('dlgNuevParametros').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.parametros.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                parametros.setXdesc(parametros.getXdesc().toUpperCase());
                parametrosFacade.edit(parametros);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditParametros').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            parametrosFacade.remove(parametros);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacParametros').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.parametros.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (parametros != null) {

            if (parametros.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarParametros').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevParametros').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        PrimeFaces.current().executeScript("PF('dlgSinGuardarParametros').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevParametros').hide();");

    }
    
    
     public String tipoDato(String tipo) {

        String retorno = "";

        String aux = tipo;

        if (aux.equals("N")) {
            retorno = "NUMERICO";
        }
        if (aux.equals("A")) {
            retorno = "ALFANUMERICO";
        }
        return retorno;

    }

}
