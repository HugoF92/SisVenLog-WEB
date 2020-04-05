package bean;

import dao.GruposCargaFacade;
import entidad.GruposCarga;
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
public class GrupoCargaBean implements Serializable {

    @EJB
    private GruposCargaFacade gruposCargaFacade;

   
    private GruposCarga gruposCarga = new GruposCarga();
    private List<GruposCarga> listaGruposCarga = new ArrayList<GruposCarga>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public GrupoCargaBean() {

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

        listaGruposCarga = new ArrayList<GruposCarga>();
        this.gruposCarga = new GruposCarga();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.gruposCarga = new GruposCarga();
        listaGruposCarga = new ArrayList<GruposCarga>();
 
    }

    public List<GruposCarga> listar() {
        listaGruposCarga = gruposCargaFacade.findAll();
        return listaGruposCarga;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<GruposCarga> aux = new ArrayList<GruposCarga>();

            if ("".equals(this.gruposCarga.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = gruposCargaFacade.buscarPorDescripcion(gruposCarga.getXdesc());

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


                gruposCarga.setXdesc(gruposCarga.getXdesc().toUpperCase());
                gruposCarga.setFalta(new Date());
                gruposCarga.setCusuario("admin");
               
               // gruposCargaFacade.insertarGruposCarga(gruposCarga);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                RequestContext.getCurrentInstance().execute("PF('dlgNuevGruposCarga').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.gruposCarga.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                gruposCarga.setXdesc(gruposCarga.getXdesc().toUpperCase());
                gruposCargaFacade.edit(gruposCarga);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditGruposCarga').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            gruposCargaFacade.remove(gruposCarga);
            this.gruposCarga = new GruposCarga();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacGruposCarga').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.gruposCarga.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (gruposCarga != null) {

            if (gruposCarga.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarGruposCarga').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevGruposCarga').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarGruposCarga').hide();");
            RequestContext.getCurrentInstance().execute("PF('dlgNuevGruposCarga').hide();");

    }

}
