package bean;

import dao.DepositosFacade;
import dao.MercaTolerarFacade;
import dao.MercaderiasFacade;
import dao.ProveedoresFacade;
import entidad.Depositos;
import entidad.MercaTolerar;
import entidad.Mercaderias;
import entidad.Proveedores;
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
public class ToleranciaBean implements Serializable {

    @EJB
    private ProveedoresFacade proveedoresFacade;

    private Proveedores proveedores = new Proveedores();
    private List<Proveedores> listaProveedores = new ArrayList<Proveedores>();

    private MercaderiasFacade mercaderiasFacade;
    private Mercaderias mercaderias = new Mercaderias();
    private List<Mercaderias> listaMercaderias = new ArrayList<Mercaderias>();
    
    private DepositosFacade depositosFacade;
    private Depositos depositos = new Depositos();
    private List<Depositos> listaDepositos = new ArrayList<Depositos>();
    
    private MercaTolerarFacade mercaTolerarFacade;
    private MercaTolerar mercaTolerar = new MercaTolerar();
    private List<MercaTolerar> listaMercaTolerar = new ArrayList<MercaTolerar>();
            
            
    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public ToleranciaBean() {

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

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public MercaderiasFacade getMercaderiasFacade() {
        return mercaderiasFacade;
    }

    public void setMercaderiasFacade(MercaderiasFacade mercaderiasFacade) {
        this.mercaderiasFacade = mercaderiasFacade;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    public List<Mercaderias> getListaMercaderias() {
        return listaMercaderias;
    }

    public void setListaMercaderias(List<Mercaderias> listaMercaderias) {
        this.listaMercaderias = listaMercaderias;
    }

    public ProveedoresFacade getProveedoresFacade() {
        return proveedoresFacade;
    }

    public void setProveedoresFacade(ProveedoresFacade proveedoresFacade) {
        this.proveedoresFacade = proveedoresFacade;
    }

    public DepositosFacade getDepositosFacade() {
        return depositosFacade;
    }

    public void setDepositosFacade(DepositosFacade depositosFacade) {
        this.depositosFacade = depositosFacade;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public List<Depositos> getListaDepositos() {
        return listaDepositos;
    }

    public void setListaDepositos(List<Depositos> listaDepositos) {
        this.listaDepositos = listaDepositos;
    }

    public MercaTolerarFacade getMercaTolerarFacade() {
        return mercaTolerarFacade;
    }

    public void setMercaTolerarFacade(MercaTolerarFacade mercaTolerarFacade) {
        this.mercaTolerarFacade = mercaTolerarFacade;
    }

    public MercaTolerar getMercaTolerar() {
        return mercaTolerar;
    }

    public void setMercaTolerar(MercaTolerar mercaTolerar) {
        this.mercaTolerar = mercaTolerar;
    }

    public List<MercaTolerar> getListaMercaTolerar() {
        return listaMercaTolerar;
    }

    public void setListaMercaTolerar(List<MercaTolerar> listaMercaTolerar) {
        this.listaMercaTolerar = listaMercaTolerar;
    }
    
    

        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaProveedores = new ArrayList<Proveedores>();
        this.proveedores = new Proveedores();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.proveedores = new Proveedores();
        listaProveedores = new ArrayList<Proveedores>();
 
    }

    public List<Proveedores> listar() {
        listaProveedores = proveedoresFacade.findAll();
        return listaProveedores;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Proveedores> aux = new ArrayList<Proveedores>();

            if ("".equals(this.proveedores.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = proveedoresFacade.buscarPorDescripcion(proveedores.getXdesc());

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


                proveedores.setXnombre(proveedores.getXnombre().toUpperCase());
                proveedores.setFalta(new Date());
                proveedores.setCusuario("admin");
               
                proveedoresFacade.insertarProveedores(proveedores);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                PrimeFaces.current().executeScript("PF('dlgNuevProv').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.proveedores.getXnombre())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                proveedores.setXnombre(proveedores.getXnombre().toUpperCase());
                proveedoresFacade.edit(proveedores);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                PrimeFaces.current().executeScript("PF('dlgEditProv').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            proveedoresFacade.remove(proveedores);
            this.proveedores = new Proveedores();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacProv').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.proveedores.getXnombre()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (proveedores != null) {

            if (proveedores.getXnombre()!= null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarProv').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevProv').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarProv').hide();");
            PrimeFaces.current().executeScript("PF('dlgNuevProv').hide();");

    }

}
