package bean;

import dao.GruposCargaFacade;
import dao.LineasFacade;
import dao.SublineasFacade;
import entidad.GruposCarga;
import entidad.Lineas;
import entidad.Sublineas;
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
public class SublineasBean implements Serializable {

    @EJB
    private SublineasFacade xdescFacade;

    @EJB
    private GruposCargaFacade gruposCargaFacade;

    @EJB
    private LineasFacade lineasFacade;

    private String filtro = "";

    private Sublineas xdesc = new Sublineas();
    private List<Sublineas> listaSublineas = new ArrayList<Sublineas>();

    private Lineas lineas = new Lineas();
    private List<Lineas> listaLineas = new ArrayList<Lineas>();

    private GruposCarga gruposCarga = new GruposCarga();
    private List<GruposCarga> listaGruposCarga = new ArrayList<GruposCarga>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public SublineasBean() {

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

    public Sublineas getSublineas() {
        return xdesc;
    }

    public void setSublineas(Sublineas xdesc) {
        this.xdesc = xdesc;
    }

    public List<Sublineas> getListaSublineas() {
        return listaSublineas;
    }

    public void setListaSublineas(List<Sublineas> listaSublineas) {
        this.listaSublineas = listaSublineas;
    }

    public Lineas getLineas() {
        return lineas;
    }

    public void setLineas(Lineas lineas) {
        this.lineas = lineas;
    }

    public List<Lineas> getListaLineas() {
        return listaLineas;
    }

    public void setListaLineas(List<Lineas> listaLineas) {
        this.listaLineas = listaLineas;
    }

    public GruposCarga getGruposCarga() {
        return gruposCarga;
    }

    public void setGruposCarga(GruposCarga gruposCarga) {
        this.gruposCarga = gruposCarga;
    }

    public List<GruposCarga> getListaGruposCarga() {
        return listaGruposCarga;
    }

    public void setListaGruposCarga(List<GruposCarga> listaGruposCarga) {
        this.listaGruposCarga = listaGruposCarga;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaSublineas = new ArrayList<Sublineas>();
        this.xdesc = new Sublineas();
        listaGruposCarga = new ArrayList<GruposCarga>();
        this.gruposCarga = new GruposCarga();
        listaLineas = new ArrayList<Lineas>();
        this.lineas = new Lineas();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        listar();

        RequestContext.getCurrentInstance().update("formSublineas");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.xdesc = new Sublineas();
    }

    public List<Sublineas> listar() {
        listaSublineas = xdescFacade.findAll();
        return listaSublineas;
    }

    public List<GruposCarga> listarGruposCarga() {
        listaGruposCarga = gruposCargaFacade.findAll();
        return listaGruposCarga;
    }

    public List<Lineas> listarLineas() {
        listaLineas = lineasFacade.findAll();
        return listaLineas;
    }

    public void insertar() {
        try {
            
            if (lineas == null ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar una linea"));
                return;
            }
            
            if (gruposCarga == null ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar un grupo de carga"));
                return;
            }

            xdesc.setXdesc(xdesc.getXdesc().toUpperCase());

            xdesc.setFalta(new Date());
            xdesc.setCusuario("admin");
            xdesc.setCodGcarga(gruposCarga);
            xdesc.setCodLinea(lineas);

            xdescFacade.insertarSublineas(xdesc);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevSublineas').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.xdesc.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                xdesc.setXdesc(xdesc.getXdesc().toUpperCase());
                xdesc.setCodLinea(lineas);
                xdesc.setCodGcarga(gruposCarga);
                xdescFacade.edit(xdesc);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditSublineas').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            xdescFacade.remove(xdesc);
            this.xdesc = new Sublineas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacSublineas').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.xdesc.getXdesc()) {
            this.setHabBtnEdit(false);
            this.gruposCarga = this.xdesc.getCodGcarga();
            this.lineas = this.xdesc.getCodLinea();
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (xdesc != null) {

            if (xdesc.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarSublineas').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevSublineas').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarSublineas').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevSublineas').hide();");

    }

}
