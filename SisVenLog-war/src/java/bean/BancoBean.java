package bean;

import dao.BancosFacade;
import entidad.Bancos;
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
public class BancoBean implements Serializable {

    @EJB
    private BancosFacade bancosFacade;

   
    private Bancos bancos = new Bancos();
    private List<Bancos> listaBancos = new ArrayList<Bancos>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public BancoBean() {

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

    public Bancos getBancos() {
        return bancos;
    }

    public void setBancos(Bancos bancos) {
        this.bancos = bancos;
    }

    public List<Bancos> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Bancos> listaBancos) {
        this.listaBancos = listaBancos;
    }

        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaBancos = new ArrayList<Bancos>();
        this.bancos = new Bancos();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.bancos = new Bancos();
        listaBancos = new ArrayList<Bancos>();
 
    }

    public List<Bancos> listar() {
        listaBancos = bancosFacade.findAll();
        return listaBancos;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<Bancos> aux = new ArrayList<Bancos>();

            if ("".equals(this.bancos.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = bancosFacade.buscarPorDescripcion(bancos.getXdesc());

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


                bancos.setXdesc(bancos.getXdesc().toUpperCase());
                bancos.setFalta(new Date());
                bancos.setCusuario("admin");
               
                bancosFacade.insertarBancos(bancos);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                RequestContext.getCurrentInstance().execute("PF('dlgNuevBanc').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.bancos.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                bancos.setXdesc(bancos.getXdesc().toUpperCase());
                bancosFacade.edit(bancos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditBanc').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            bancosFacade.remove(bancos);
            this.bancos = new Bancos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacBanc').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.bancos.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (bancos != null) {

            if (bancos.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarBanc').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevBanc').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarBanc').hide();");
            RequestContext.getCurrentInstance().execute("PF('dlgNuevBanc').hide();");

    }

}
