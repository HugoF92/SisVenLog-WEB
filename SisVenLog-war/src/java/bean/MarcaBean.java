package bean;

import dao.MarcasComercialesFacade;
import entidad.MarcasComerciales;
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
public class MarcaBean implements Serializable {

    @EJB
    private MarcasComercialesFacade marcaFacade;

   
    private MarcasComerciales marca = new MarcasComerciales();
    private List<MarcasComerciales> listaMarca = new ArrayList<MarcasComerciales>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public MarcaBean() {

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

    public MarcasComerciales getMarca() {
        return marca;
    }

    public void setMarca(MarcasComerciales marca) {
        this.marca = marca;
    }

    public List<MarcasComerciales> getListaMarca() {
        return listaMarca;
    }

    public void setListaMarca(List<MarcasComerciales> listaMarca) {
        this.listaMarca = listaMarca;
    }

        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaMarca = new ArrayList<MarcasComerciales>();
        this.marca = new MarcasComerciales();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.marca = new MarcasComerciales();
        listaMarca = new ArrayList<MarcasComerciales>();
 
    }

    public List<MarcasComerciales> listar() {
        listaMarca = marcaFacade.findAll();
        return listaMarca;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Marca> aux = new ArrayList<Marca>();

            if ("".equals(this.marca.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = marcaFacade.buscarPorDescripcion(marca.getXdesc());

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


                marca.setXdesc(marca.getXdesc().toUpperCase());
                marca.setFalta(new Date());
                marca.setCusuario("admin");
               
                marcaFacade.insertarMarca(marca);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                RequestContext.getCurrentInstance().execute("PF('dlgNuevMarca').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.marca.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                marca.setXdesc(marca.getXdesc().toUpperCase());
                marcaFacade.edit(marca);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditMarca').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            marcaFacade.remove(marca);
            this.marca = new MarcasComerciales();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacMarca').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.marca.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (marca != null) {

            if (marca.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarMarca').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevMarca').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarMarca').hide();");
            RequestContext.getCurrentInstance().execute("PF('dlgNuevMarca').hide();");

    }

}
