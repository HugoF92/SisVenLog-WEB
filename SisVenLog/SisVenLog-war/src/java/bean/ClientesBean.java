package bean;

import dao.CiudadesFacade;
import dao.ClientesFacade;
import dao.RutasFacade;
import dao.TiposClientesFacade;
import entidad.Ciudades;
import entidad.Clientes;
import entidad.Rutas;
import entidad.TiposClientes;
import java.io.Serializable;
import java.util.ArrayList;
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
public class ClientesBean implements Serializable {

    @EJB
    private ClientesFacade clientesFacade;
    
    @EJB
    private TiposClientesFacade tipoCliFacade;
    
    @EJB
    private CiudadesFacade ciudadFacade;
    
    @EJB
    private RutasFacade rutasFacade;
   
    private Clientes clientes = new Clientes();
    @SuppressWarnings("Convert2Diamond")
    private List<Clientes> listaClientes = new ArrayList<Clientes>();
    @SuppressWarnings("Convert2Diamond")
    private List<TiposClientes> listaDeTipoCliente = new ArrayList<TiposClientes>();
    @SuppressWarnings("Convert2Diamond")
    private List<Ciudades> listaCiudades = new ArrayList<Ciudades>();
    @SuppressWarnings("Convert2Diamond")
    private List<Rutas> listaRutas = new ArrayList<Rutas>();
    
    
//    Dias a elegist
    
    private boolean lunes;
    private boolean martes;
    private boolean miercoles;
    private boolean jueves;
    private boolean viernes;
    private boolean sabado;
    

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public ClientesBean() {

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

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public ClientesFacade getClientesFacade() {
        return clientesFacade;
    }

    public void setClientesFacade(ClientesFacade clientesFacade) {
        this.clientesFacade = clientesFacade;
    }

    public TiposClientesFacade getTipoCliFacade() {
        return tipoCliFacade;
    }

    public void setTipoCliFacade(TiposClientesFacade tipoCliFacade) {
        this.tipoCliFacade = tipoCliFacade;
    }

    public List<TiposClientes> getListaDeTipoCliente() {
        return listaDeTipoCliente;
    }

    public void setListaDeTipoCliente(List<TiposClientes> listaDeTipoCliente) {
        this.listaDeTipoCliente = listaDeTipoCliente;
    }

    public CiudadesFacade getCiudadFacade() {
        return ciudadFacade;
    }

    public void setCiudadFacade(CiudadesFacade ciudadFacade) {
        this.ciudadFacade = ciudadFacade;
    }

    public List<Ciudades> getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Rutas> getListaRutas() {
        return listaRutas;
    }

    public void setListaRutas(List<Rutas> listaRutas) {
        this.listaRutas = listaRutas;
    }

    public RutasFacade getRutasFacade() {
        return rutasFacade;
    }

    public void setRutasFacade(RutasFacade rutasFacade) {
        this.rutasFacade = rutasFacade;
    }

    public boolean isLunes() {
        return lunes;
    }

    public void setLunes(boolean lunes) {
        this.lunes = lunes;
    }

    public boolean isMartes() {
        return martes;
    }

    public void setMartes(boolean martes) {
        this.martes = martes;
    }

    public boolean isMiercoles() {
        return miercoles;
    }

    public void setMiercoles(boolean miercoles) {
        this.miercoles = miercoles;
    }

    public boolean isJueves() {
        return jueves;
    }

    public void setJueves(boolean jueves) {
        this.jueves = jueves;
    }

    public boolean isViernes() {
        return viernes;
    }

    public void setViernes(boolean viernes) {
        this.viernes = viernes;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    
    
    
    
    
    
    
    
    


    @PostConstruct
    @SuppressWarnings("Convert2Diamond")
    public void instanciar() {

        listaClientes = new ArrayList<Clientes>();
        this.clientes = new Clientes();

        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        listar();
        listarTipoCliente();
        listarCiudades();
        listarRutas();

    }

    @SuppressWarnings("Convert2Diamond")
    public void nuevo() {
        this.clientes = new Clientes();
        listaClientes = new ArrayList<Clientes>();
 
    }

    public List<Clientes> listar() {
        listaClientes = clientesFacade.findAll();
        return listaClientes;
    }

    public void insertar() {
        
        try {
                Integer maxCod = this.clientesFacade.getMaxId();
                clientes.setCodCliente(maxCod+1);
                clientes.setXnombre(clientes.getXnombre().toUpperCase());
                clientes.setXcedula(clientes.getXcedula());
                clientes.setXruc(clientes.getXruc().toUpperCase());
                clientes.setCtipoCliente(clientes.getCtipoCliente());
                clientes.setXdirec(clientes.getXdirec());
                clientes.setXcontacto(clientes.getXcontacto());
                clientes.setCodCiudad(clientes.getCodCiudad());
                clientes.setXtelef(clientes.getXtelef());
                clientes.setXfax(clientes.getXfax());
                clientes.setXemail(clientes.getXemail());
                clientes.setXfax(clientes.getXfax());
                clientes.setCodRuta(clientes.getCodRuta());
                
                
                clientes.setCodEstado("A");
                clientesFacade.create(clientes);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

                RequestContext.getCurrentInstance().execute("PF('dlgNuevoCliente').hide();");

                instanciar();

            

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.clientes.getXnombre())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar un Nombre."));

            } else {

                clientes.setXnombre(clientes.getXnombre().toUpperCase());
                clientesFacade.edit(clientes);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditarCliente').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
       
    }

    public void onRowSelect(SelectEvent event) {
        if (!"".equals(this.clientes.getXnombre())) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

    }

    public void verificarCargaDatos() {

       boolean cargado = false;

        if (clientes != null) {

            if (clientes.getXnombre()!= null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardadCliente').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevoCliente').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
        
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardadCliente').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevoCliente').hide();");
    }
    
    public List<TiposClientes> listarTipoCliente() {
        listaDeTipoCliente = tipoCliFacade.findAll();
        return listaDeTipoCliente;
    } 
    
    public List<Ciudades> listarCiudades(){
        listaCiudades = ciudadFacade.findAll();
        return listaCiudades;
    }
    
    public List<Rutas> listarRutas(){
        listaRutas = rutasFacade.findAll();
        return listaRutas;
    }

}