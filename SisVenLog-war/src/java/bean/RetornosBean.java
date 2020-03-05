package bean;


import dao.DepositosFacade;
import dao.MercaImpuestosFacade;
import dao.MercaderiasFacade;
import dao.RetornosPreciosFacade;
import entidad.Depositos;
import entidad.MercaImpuestos;
import entidad.Mercaderias;
import entidad.RetornosPrecios;
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
public class RetornosBean implements Serializable {

    @EJB
    private RetornosPreciosFacade retornosPreciosFacade;
    private RetornosPrecios retornosPrecios = new RetornosPrecios();
    private List<RetornosPrecios> listaRetornosPrecios = new ArrayList<RetornosPrecios>();

    private MercaderiasFacade mercaderiasFacade;
    private Mercaderias mercaderias = new Mercaderias();
    private List<Mercaderias> listaMercaderias = new ArrayList<Mercaderias>();
    
    private MercaImpuestosFacade mercaImpuestosFacade;
    private MercaImpuestos mercaImpuestos = new MercaImpuestos();
    private List<MercaImpuestos> listaMercaImpuestos = new ArrayList<MercaImpuestos>();
    
    
    private DepositosFacade depositosFacade;
    private Depositos depositos = new Depositos();
    private List<Depositos> listaDepositos = new ArrayList<Depositos>();
    
    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public RetornosBean() {

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

    public RetornosPrecios getRetornosPrecios() {
        return retornosPrecios;
    }

    public void setRetornosPrecios(RetornosPrecios retornosPrecios) {
        this.retornosPrecios = retornosPrecios;
    }

    public List<RetornosPrecios> getListaRetornosPrecios() {
        return listaRetornosPrecios;
    }

    public void setListaRetornosPrecios(List<RetornosPrecios> listaRetornosPrecios) {
        this.listaRetornosPrecios = listaRetornosPrecios;
    }

    public RetornosPreciosFacade getRetornosPreciosFacade() {
        return retornosPreciosFacade;
    }

    public void setRetornosPreciosFacade(RetornosPreciosFacade retornosPreciosFacade) {
        this.retornosPreciosFacade = retornosPreciosFacade;
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

    public MercaImpuestosFacade getMercaImpuestosFacade() {
        return mercaImpuestosFacade;
    }

    public void setMercaImpuestosFacade(MercaImpuestosFacade mercaImpuestosFacade) {
        this.mercaImpuestosFacade = mercaImpuestosFacade;
    }

    public MercaImpuestos getMercaImpuestos() {
        return mercaImpuestos;
    }

    public void setMercaImpuestos(MercaImpuestos mercaImpuestos) {
        this.mercaImpuestos = mercaImpuestos;
    }

    public List<MercaImpuestos> getListaMercaImpuestos() {
        return listaMercaImpuestos;
    }

    public void setListaMercaImpuestos(List<MercaImpuestos> listaMercaImpuestos) {
        this.listaMercaImpuestos = listaMercaImpuestos;
    }

    
    
        //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        listaRetornosPrecios = new ArrayList<RetornosPrecios>();
        this.retornosPrecios = new RetornosPrecios();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        
        listar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }

    
    public void nuevo() {
        this.retornosPrecios = new RetornosPrecios();
        listaRetornosPrecios = new ArrayList<RetornosPrecios>();
 
    }

    public List<RetornosPrecios> listar() {
        listaRetornosPrecios = retornosPreciosFacade.findAll();
        return listaRetornosPrecios;
    }

    /*public Boolean validarDeposito() {
        boolean valido = true;

        try {
            List<RetornosPrecios> aux = new ArrayList<RetornosPrecios>();

            if ("".equals(this.retornosPrecios.getXdesc())) {
                valido = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
            } else {
              //  aux = retornosPreciosFacade.buscarPorDescripcion(retornosPrecios.getXdesc());

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


                retornosPrecios.setRetornosPreciosPK(retornosPrecios.getRetornosPreciosPK());
                retornosPrecios.setFalta(new Date());
                retornosPrecios.setCusuario("admin");
               
               // retornosPreciosFacade.insertarRetornosPrecios(retornosPrecios);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                RequestContext.getCurrentInstance().execute("PF('dlgNuevRet').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.retornosPrecios.getRetornosPreciosPK())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                retornosPrecios.setRetornosPreciosPK(retornosPrecios.getRetornosPreciosPK());
                retornosPreciosFacade.edit(retornosPrecios);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditRet').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            retornosPreciosFacade.remove(retornosPrecios);
            this.retornosPrecios = new RetornosPrecios();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacRet').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.retornosPrecios.getRetornosPreciosPK().getCodMerca()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (retornosPrecios != null) {

            if (retornosPrecios.getRetornosPreciosPK()!= null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarRet').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevRet').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarRet').hide();");
            RequestContext.getCurrentInstance().execute("PF('dlgNuevRet').hide();");

    }

}
