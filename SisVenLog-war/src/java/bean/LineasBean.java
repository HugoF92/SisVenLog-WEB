package bean;

import dao.CategoriasFacade;
import dao.LineasFacade;
import entidad.Categorias;
import entidad.Lineas;
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
public class LineasBean implements Serializable {

    @EJB
    private LineasFacade xdescFacade;
    
    @EJB
    private CategoriasFacade categoriasFacade;

    private String filtro = "";

    private Lineas xdesc = new Lineas();
    private Categorias categorias = new Categorias();
    private List<Lineas> listaLineas = new ArrayList<Lineas>();
    private List<Categorias> listaCategorias = new ArrayList<Categorias>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public LineasBean() {

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

    public Lineas getLineas() {
        return xdesc;
    }

    public void setLineas(Lineas xdesc) {
        this.xdesc = xdesc;
    }

    public List<Lineas> getListaLineas() {
        return listaLineas;
    }

    public void setListaLineas(List<Lineas> listaLineas) {
        this.listaLineas = listaLineas;
    }

    public Categorias getCategorias() {
        return categorias;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public List<Categorias> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<Categorias> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaLineas = new ArrayList<Lineas>();
        this.xdesc = new Lineas();
        listaCategorias = new ArrayList<Categorias>();
        this.categorias = new Categorias();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
       listar();

    RequestContext.getCurrentInstance().update("formLineas");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.xdesc = new Lineas();
    }

    public List<Lineas> listar() {
        //listaLineas = xdescFacade.findAll();
        listaLineas = xdescFacade.listarLineasOrdenadoXCategoria();
        return listaLineas;
    }
    
    public List<Categorias> listarCategorias() {
        listaCategorias = categoriasFacade.findAll();
        return listaCategorias;
    }


    public void insertar() {
        try {
            
            if (categorias == null ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención ", "Debe seleccionar una categoría"));
                return;
            }

            xdesc.setXdesc(xdesc.getXdesc().toUpperCase());
           
            xdesc.setFalta(new Date());
            xdesc.setCusuario("admin");
            xdesc.setCodCATEGORIA(categorias);

            xdescFacade.insertarLineas(xdesc);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevLineas').hide();");

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
                xdescFacade.edit(xdesc);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditLineas').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            xdescFacade.remove(xdesc);
            this.xdesc = new Lineas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacLineas').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.xdesc.getXdesc()) {
            this.setHabBtnEdit(false);
            this.categorias = this.xdesc.getCodCATEGORIA();
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
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarLineas').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevLineas').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarLineas').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevLineas').hide();");

    }

}

