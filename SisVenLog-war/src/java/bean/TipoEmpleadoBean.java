package bean;

import dao.TiposEmpleadosFacade;
import entidad.TiposEmpleados;
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
public class TipoEmpleadoBean implements Serializable {

    @EJB
    private TiposEmpleadosFacade tipoEmpleadoFacade;

   
    private TiposEmpleados tipoEmpleado = new TiposEmpleados();
    private List<TiposEmpleados> listaTipoEmpleado = new ArrayList<TiposEmpleados>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public TipoEmpleadoBean() {

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

    public TiposEmpleados getTipoEmpleado() {
        return tipoEmpleado;
    }

    public void setTipoEmpleado(TiposEmpleados tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public List<TiposEmpleados> getListaTipoEmpleado() {
        return listaTipoEmpleado;
    }

    public void setListaTipoEmpleado(List<TiposEmpleados> listaTipoEmpleado) {
        this.listaTipoEmpleado = listaTipoEmpleado;
    }

        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaTipoEmpleado = new ArrayList<TiposEmpleados>();
        this.tipoEmpleado = new TiposEmpleados();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.tipoEmpleado = new TiposEmpleados();
        listaTipoEmpleado = new ArrayList<TiposEmpleados>();
 
    }

    public List<TiposEmpleados> listar() {
        listaTipoEmpleado = tipoEmpleadoFacade.findAll();
        return listaTipoEmpleado;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<TipoEmpleado> aux = new ArrayList<TipoEmpleado>();

            if ("".equals(this.tipoEmpleado.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = tipoEmpleadoFacade.buscarPorDescripcion(tipoEmpleado.getXdesc());

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


                tipoEmpleado.setXdesc(tipoEmpleado.getXdesc().toUpperCase());
                tipoEmpleado.setFalta(new Date());
                tipoEmpleado.setCusuario("admin");
                tipoEmpleado.setMfijoSis('S');
               
                tipoEmpleadoFacade.create(tipoEmpleado);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                RequestContext.getCurrentInstance().execute("PF('dlgNuevtipoEmpleado').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.tipoEmpleado.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                tipoEmpleado.setXdesc(tipoEmpleado.getXdesc().toUpperCase());
                tipoEmpleadoFacade.edit(tipoEmpleado);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditTipoEmpleado').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            tipoEmpleadoFacade.remove(tipoEmpleado);
            this.tipoEmpleado = new TiposEmpleados();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacTipoEmpleado').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.tipoEmpleado.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (tipoEmpleado != null) {

            if (tipoEmpleado.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarTipoEmpleado').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevTipoEmpleado').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarTipoEmpleado').hide();");
            RequestContext.getCurrentInstance().execute("PF('dlgNuevTipoEmpleado').hide();");

    }

}
