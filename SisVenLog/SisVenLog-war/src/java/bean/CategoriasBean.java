package bean;

import dao.CategoriasFacade;
import entidad.Categorias;
import entidad.Divisiones;
import java.io.Serializable;
import java.util.ArrayList;
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
public class CategoriasBean implements Serializable {

    @EJB
    private CategoriasFacade categoriasFacade;

   
    private Categorias categorias = new Categorias();
    private List<Categorias> listaCategorias = new ArrayList<Categorias>();
    
    private Divisiones divisiones = new Divisiones();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public CategoriasBean() {

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

    public Divisiones getDivisiones() {
        return divisiones;
    }

    public void setDivisiones(Divisiones divisiones) {
        this.divisiones = divisiones;
    }

        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaCategorias = new ArrayList<Categorias>();
        this.categorias = new Categorias();
        
        divisiones = new Divisiones();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.categorias = new Categorias();
        listaCategorias = new ArrayList<Categorias>();
 
    }

    public List<Categorias> listar() {
        listaCategorias = categoriasFacade.findAll();
        return listaCategorias;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Categorias> aux = new ArrayList<Categorias>();

            if ("".equals(this.categorias.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = categoriasFacade.buscarPorDescripcion(categorias.getXdesc());

                /*if (aux.size() > 0) {
                    valido = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ya existe."));
                }
            }
        } catch (Exception e) {
            valido = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al validar los datos."));
        }

        return valido;
    }
*/ 
    public void insertar() {
        try {


                categorias.setXdesc(categorias.getXdesc().toUpperCase());
                categorias.setCodDivision(divisiones);
               
                categoriasFacade.insertarCategorias(categorias);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

//                RequestContext.getCurrentInstance().execute("PF('dlgNuevCategorias').hide();");
                PrimeFaces.current().executeScript("PF('dlgNuevCategorias').hide();");
                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.categorias.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                categorias.setXdesc(categorias.getXdesc().toUpperCase());
                categoriasFacade.edit(categorias);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

//                RequestContext.getCurrentInstance().execute("PF('dlgEditCategorias').hide();");
                PrimeFaces.current().executeScript("PF('dlgEditCategorias').hide();");
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            categoriasFacade.remove(categorias);
            this.categorias = new Categorias();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
//            RequestContext.getCurrentInstance().execute("PF('dlgInacCategorias').hide();");
            PrimeFaces.current().executeScript("PF('dlgInacCategorias').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.categorias.getXdesc()) {
            this.setHabBtnEdit(false);
            divisiones = categorias.getCodDivision();
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (categorias != null) {

            if (categorias.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
//            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarCategorias').show();");
            PrimeFaces.current().executeScript("PF('dlgSinGuardarCategorias').show();");
        } else {
//            RequestContext.getCurrentInstance().execute("PF('dlgNuevCategorias').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevCategorias').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
//            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarCategorias').hide();");
            PrimeFaces.current().executeScript("PF('dlgSinGuardarCategorias').hide();");
//            RequestContext.getCurrentInstance().execute("PF('dlgNuevCategorias').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevCategorias').hide();");
    }

}
