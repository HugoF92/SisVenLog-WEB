package bean;

import dao.TiposClientesFacade;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.TiposClientes;
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
public class TipoCliBean implements Serializable {

    @EJB
    private TiposClientesFacade tipoCliFacade;

    private String filtro = "";

    private TiposClientes tipoCli = new TiposClientes();
    private List<TiposClientes> listaTipoCli = new ArrayList<TiposClientes>();
    
    private Depositos depositos = new Depositos(new DepositosPK());

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public TipoCliBean() {

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

    public TiposClientes getTipoCli() {
        return tipoCli;
    }

    public void setTipoCli(TiposClientes tipoCli) {
        this.tipoCli = tipoCli;
    }

    public List<TiposClientes> getListaTipoCli() {
        return listaTipoCli;
    }

    public void setListaTipoCli(List<TiposClientes> listaTipoCli) {
        this.listaTipoCli = listaTipoCli;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaTipoCli = new ArrayList<TiposClientes>();
        this.tipoCli = new TiposClientes();
        depositos = new Depositos(new DepositosPK());
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

    RequestContext.getCurrentInstance().update("formTipoCli");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.tipoCli = new TiposClientes();
    }

    public List<TiposClientes> listar() {
        listaTipoCli = tipoCliFacade.findAll();
        return listaTipoCli;
    }


    public void insertar() {
        try {

            tipoCli.setXdesc(tipoCli.getXdesc().toUpperCase());
            tipoCli.setFalta(new Date());
            tipoCli.setCusuario("admin");

            tipoCliFacade.create(tipoCli);
            tipoCliFacade.insertarTipoCliDeposito(tipoCli, depositos);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevTipoCli').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.tipoCli.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                tipoCli.setXdesc(tipoCli.getXdesc().toUpperCase());
                tipoCliFacade.edit(tipoCli);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditTipoCli').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            tipoCliFacade.remove(tipoCli);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacTipoCli').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.tipoCli.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (tipoCli != null) {

            if (tipoCli.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarTipoCli').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevTipoCli').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarTipoCli').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevTipoCli').hide();");

    }

}
