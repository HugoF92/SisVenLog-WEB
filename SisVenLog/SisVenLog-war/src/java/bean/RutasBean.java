package bean;

import dao.RutasFacade;
import dao.ZonasFacade;
import entidad.Rutas;
import entidad.Zonas;
import entidad.ZonasPK;
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
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class RutasBean implements Serializable {

    @EJB
    private RutasFacade rutasFacade;

    @EJB
    private ZonasFacade zonasFacade;

    private String filtro = "";

    private Rutas rutas = new Rutas();
    private List<Rutas> listaRutas = new ArrayList<Rutas>();
    
    private Zonas zonas = new Zonas();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public RutasBean() {

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

    public Rutas getRutas() {
        return rutas;
    }

    public void setRutas(Rutas rutas) {
        this.rutas = rutas;
    }

    public List<Rutas> getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(List<Rutas> listaRutas) {
        this.listaRutas = listaRutas;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaRutas = new ArrayList<Rutas>();
        this.rutas = new Rutas();
        zonas = new Zonas(new ZonasPK());
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

//    RequestContext.getCurrentInstance().update("formRutas");
        PrimeFaces.current().ajax().update("formRutas");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.rutas = new Rutas();
    }

    public List<Rutas> listar() {
        listaRutas = rutasFacade.findAll();
        return listaRutas;
    }
    
    public void getDatosEditar() {
        setZonas(rutas.getZonas());
    }


    public void insertar() {
        try {

            rutas.setXdesc(rutas.getXdesc().toUpperCase());
            rutas.setFalta(new Date());
            rutas.setCusuario("admin");
            rutas.setZonas(zonas);

            rutasFacade.insertarRutas(rutas);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            PrimeFaces.current().executeScript("PF('dlgNuevRutas').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.rutas.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                rutas.setXdesc(rutas.getXdesc().toUpperCase());
                rutas.setZonas(zonas);
                rutasFacade.edit(rutas);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditRutas').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            rutasFacade.remove(rutas);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacRutas').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.rutas.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (rutas != null) {

            if (rutas.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarRutas').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevRutas').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        PrimeFaces.current().executeScript("PF('dlgSinGuardarRutas').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevRutas').hide();");

    }
    
    public void buscadorZonaTab(AjaxBehaviorEvent event) {
        try {

            if (this.zonas != null) {
                if (!this.zonas.getZonasPK().getCodZona().equals("")) {

                    this.zonas = this.zonasFacade.buscarPorCodigo(this.zonas.getZonasPK().getCodZona());

                    if (this.zonas == null) {

                        this.zonas = new Zonas();
                        this.zonas.setZonasPK(new ZonasPK());
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "No encontrado."));
                    }
                }
            } else {

                this.zonas = new Zonas();
                this.zonas.setZonasPK(new ZonasPK());
            }
        } catch (Exception e) {

            this.zonas = new Zonas();
            this.zonas.setZonasPK(new ZonasPK());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error en la busqueda"));
        }
    }

}
