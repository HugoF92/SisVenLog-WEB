package bean;

import dao.TransportistasFacade;
import entidad.Transportistas;
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
public class TransportistasBean implements Serializable {

    @EJB
    private TransportistasFacade transportistasFacade;

    private String filtro = "";

    private Transportistas transportistas = new Transportistas();
    private List<Transportistas> listaTransportistas = new ArrayList<Transportistas>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public TransportistasBean() {

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

    public Transportistas getTransportistas() {
        return transportistas;
    }

    public void setTransportistas(Transportistas transportistas) {
        this.transportistas = transportistas;
    }

    public List<Transportistas> getListaTransportistas() {
        return listaTransportistas;
    }

    public void setListaTransportistas(List<Transportistas> listaTransportistas) {
        this.listaTransportistas = listaTransportistas;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaTransportistas = new ArrayList<Transportistas>();
        this.transportistas = new Transportistas();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";

        listar();
        RequestContext.getCurrentInstance().update("formTransportistas");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.transportistas = new Transportistas();
    }

    public List<Transportistas> listar() {
        listaTransportistas = transportistasFacade.findAll();
        return listaTransportistas;
    }

    public void insertar() {
        try {

            transportistas.setXtransp(transportistas.getXtransp().toUpperCase());
            transportistas.setXruc(transportistas.getXruc().toUpperCase());
            transportistas.setXdomicilio(transportistas.getXdomicilio().toUpperCase());
            transportistas.setFalta(new Date());
            transportistas.setCusuario("admin");

            transportistasFacade.insertarTransportistas(transportistas);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevTrans').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.transportistas.getXtransp())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                transportistas.setXtransp(transportistas.getXtransp().toUpperCase());
                transportistasFacade.edit(transportistas);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditTrans').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            transportistasFacade.remove(transportistas);
            this.transportistas = new Transportistas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacTrans').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.transportistas.getXtransp()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (transportistas != null) {

            if (transportistas.getXtransp() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarTrans').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevTrans').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarTrans').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevTrans').hide();");

    }

}
