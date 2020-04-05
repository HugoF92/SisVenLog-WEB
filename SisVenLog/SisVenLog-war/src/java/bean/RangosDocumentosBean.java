package bean;

import dao.RangosDocumentosFacade;
import dao.TiposDocumentosFacade;
import entidad.RangosDocumentos;
import entidad.RangosDocumentosPK;
import entidad.TiposDocumentos;
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
public class RangosDocumentosBean implements Serializable {

    @EJB
    private RangosDocumentosFacade rangosDocumentosFacade;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    private String filtro = "";

    private RangosDocumentos rangosDocumentos = new RangosDocumentos();
    private TiposDocumentos tiposDocumentos = new TiposDocumentos();
   
    private List<RangosDocumentos> listaRangosDocumentos = new ArrayList<RangosDocumentos>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public RangosDocumentosBean() {

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

    public RangosDocumentos getRangosDocumentos() {
        return rangosDocumentos;
    }

    public void setRangosDocumentos(RangosDocumentos rangosDocumentos) {
        this.rangosDocumentos = rangosDocumentos;
    }

    public List<RangosDocumentos> getListaRangosDocumentos() {
        return listaRangosDocumentos;
    }

    public void setListaRangosDocumentos(List<RangosDocumentos> listaRangosDocumentos) {
        this.listaRangosDocumentos = listaRangosDocumentos;
    }

    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaRangosDocumentos = new ArrayList<RangosDocumentos>();
        this.rangosDocumentos = new RangosDocumentos(new RangosDocumentosPK());
        this.tiposDocumentos = new TiposDocumentos();
   
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
        this.rangosDocumentos = new RangosDocumentos();
    }

    public List<RangosDocumentos> listar() {
        listaRangosDocumentos = rangosDocumentosFacade.findAll();
        return listaRangosDocumentos;
    }

    public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<RangosDocumentos> aux = new ArrayList<RangosDocumentos>();

            if ("".equals(this.rangosDocumentos.getRangosDocumentosPK())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
                //aux = rangosDocumentosFacade.buscarPorDescripcion(rangosDocumentos.getXdesc());

                if (aux.size() > 0) {
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

    public void insertar() {
        try {

            if (validarDeposito().equals(true)) {

                if (this.tiposDocumentos == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Debe seleccionar un tipo de documento"));
                    return;
                }

                /*if (this.Persona.getPersTelefono() == null && this.Persona.getPersCelular() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Debe cargar telefono o celular."));
                    return;
                }

                if (this.Persona.getPersSexo() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Debe cargar el genero."));
                    return;
                }*/
                //String usu = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
           
               

                rangosDocumentos.getRangosDocumentosPK().setCtipoDocum(tiposDocumentos.getCtipoDocum());
                rangosDocumentos.getRangosDocumentosPK().setCodEmpr(new Short("2"));

                rangosDocumentos.setFalta(new Date());
                rangosDocumentos.setCusuario("admin");

                rangosDocumentosFacade.create(rangosDocumentos);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                PrimeFaces.current().executeScript("PF('dlgNuevRangosDocumentos').hide();");

                instanciar();
                        

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void editar() {
        try {

            
            if ("".equals(this.rangosDocumentos.getRangosDocumentosPK())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                
                rangosDocumentosFacade.edit(rangosDocumentos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();
                
                listar();

                PrimeFaces.current().executeScript("PF('dlgEditRangosDocumentos').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void borrar() {
        try {
 
            rangosDocumentosFacade.remove(rangosDocumentos);
            this.rangosDocumentos = new RangosDocumentos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            PrimeFaces.current().executeScript("PF('dlgInacRangosDocumentos').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if (null != this.rangosDocumentos.getRangosDocumentosPK()) {
            this.setHabBtnEdit(false);
            this.tiposDocumentos = tiposDocumentosFacade.find(rangosDocumentos.getRangosDocumentosPK().getCtipoDocum());
            
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (rangosDocumentos.getRangosDocumentosPK()!= null) {

            if (rangosDocumentos.getRangosDocumentosPK() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            PrimeFaces.current().executeScript("PF('dlgSinGuardarRangosDocumentos').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevRangosDocumentos').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        PrimeFaces.current().executeScript("PF('dlgSinGuardarRangosDocumentos').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevRangosDocumentos').hide();");

    }

}
