package bean;

import dao.DivisionesFacade;
import entidad.Divisiones;
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
public class DivisionesBean implements Serializable {

    @EJB
    private DivisionesFacade divisionesFacade;

    private String filtro = "";

    private Divisiones divisiones = new Divisiones();
    private List<Divisiones> listaDivisiones = new ArrayList<Divisiones>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public DivisionesBean() {

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

    public Divisiones getDivisiones() {
        return divisiones;
    }

    public void setDivisiones(Divisiones divisiones) {
        this.divisiones = divisiones;
    }

    public List<Divisiones> getListaDivisiones() {
        return listaDivisiones;
    }

    public void setListaDivisiones(List<Divisiones> listaDivisiones) {
        this.listaDivisiones = listaDivisiones;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaDivisiones = new ArrayList<Divisiones>();
        this.divisiones = new Divisiones();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

    RequestContext.getCurrentInstance().update("formDivisiones");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.divisiones = new Divisiones();
    }

    public List<Divisiones> listar() {
        listaDivisiones = divisionesFacade.findAll();
        return listaDivisiones;
    }


    public void insertar() {
        try {

            divisiones.setXdesc(divisiones.getXdesc().toUpperCase());
            divisiones.setFalta(new Date());
            divisiones.setCusuario("admin");

            divisionesFacade.insertarDivisiones(divisiones);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevDivisiones').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.divisiones.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                divisiones.setXdesc(divisiones.getXdesc().toUpperCase());
                divisionesFacade.edit(divisiones);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditDivisiones').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            divisionesFacade.remove(divisiones);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacDivisiones').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.divisiones.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (divisiones != null) {

            if (divisiones.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarDivisiones').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevDivisiones').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarDivisiones').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevDivisiones').hide();");

    }

}
