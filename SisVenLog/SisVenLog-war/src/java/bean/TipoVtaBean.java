package bean;

import dao.TiposVentasFacade;
import entidad.TiposVentas;
import entidad.TiposVentasPK;
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
public class TipoVtaBean implements Serializable {

    @EJB
    private TiposVentasFacade tipoVtaFacade;

    private String filtro = "";

    private TiposVentas tipoVta = new TiposVentas(new TiposVentasPK());
    private List<TiposVentas> listaTipoVta = new ArrayList<TiposVentas>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public TipoVtaBean() {

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

    public TiposVentas getTipoVta() {
        return tipoVta;
    }

    public void setTipoVta(TiposVentas tipoVta) {
        this.tipoVta = tipoVta;
    }

    public List<TiposVentas> getListaTipoVta() {
        return listaTipoVta;
    }

    public void setListaTipoVta(List<TiposVentas> listaTipoVta) {
        this.listaTipoVta = listaTipoVta;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaTipoVta = new ArrayList<TiposVentas>();
        this.tipoVta = new TiposVentas(new TiposVentasPK());
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
        this.tipoVta = new TiposVentas(new TiposVentasPK());
    }

    public List<TiposVentas> listar() {
        listaTipoVta = tipoVtaFacade.findAll();
        return listaTipoVta;
    }


    public void insertar() {
        try {

            tipoVta.getTiposVentasPK().setCodEmpr(new Short("2"));
            tipoVta.setXdesc(tipoVta.getXdesc().toUpperCase());
            tipoVta.setFalta(new Date());
            tipoVta.setCusuario("admin");

            tipoVtaFacade.create(tipoVta);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            PrimeFaces.current().executeScript("PF('dlgNuevTipoVta').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.tipoVta.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                tipoVta.setXdesc(tipoVta.getXdesc().toUpperCase());
                tipoVtaFacade.edit(tipoVta);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditTipoVta').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            tipoVtaFacade.remove(tipoVta);
            this.tipoVta = new TiposVentas(new TiposVentasPK());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacTipoVta').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.tipoVta.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (tipoVta != null) {

            if (tipoVta.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarTipoVta').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevTipoVta').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        PrimeFaces.current().executeScript("PF('dlgSinGuardarTipoVta').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevTipoVta').hide();");

    }

}
