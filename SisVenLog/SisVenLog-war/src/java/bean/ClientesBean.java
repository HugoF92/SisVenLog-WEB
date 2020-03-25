package bean;

import dao.CiudadesFacade;
import dao.ClientesFacade;
import dao.RutasFacade;
import dao.TiposClientesFacade;
import entidad.Ciudades;
import entidad.Clientes;
import entidad.Rutas;
import entidad.TiposClientes;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
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
        this.clientes.setMtipoPersona("F");
        this.reloadDias();

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
        listaClientes = clientesFacade.getListaClientesPorFechaAltaDesc();
        return listaClientes;
    }

    public void insertar() {
        short codEmpresa = 2;
        try {
                Integer maxCod = this.clientesFacade.getMaxId();
                clientes.setCodEmpr(codEmpresa);
                clientes.setMtipoPersona(clientes.getMtipoPersona());
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
                clientes.setCodEstado("A");
                clientes.setCodRuta(clientes.getCodRuta());
                clientes.setXdiasVisita(concatenarDias());
                clientes.setXobs(clientes.getXobs());
                clientes.setMformaPago(Character.MIN_VALUE);
                clientesFacade.create(clientes);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));
                PrimeFaces.current().executeScript("PF('dlgNuevoCliente').hide();");
                instanciar();
            
                
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            
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
                clientes.setCodEstado(clientes.getCodEstado());
                clientes.setCodRuta(clientes.getCodRuta());
                clientes.setXdiasVisita(concatenarDias());
                clientes.setXobs(clientes.getXobs());
                
                clientesFacade.edit(clientes);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();
                
                reloadDias();

                PrimeFaces.current().executeScript("PF('dlgEditarCliente').hide();");


        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
       try {

            clientesFacade.remove(clientes);
            this.clientes = new Clientes();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            reload();
            PrimeFaces.current().executeScript("PF('dlgInacCliente').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }
    
   public void reload() throws IOException {
    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
   }

    public void onRowSelect(SelectEvent event) {
        
        String diasSeleccionads  = this.clientes.getXdiasVisita();
        char l = 'L';
        char t = 'T';
        char m = 'M';
        char j = 'J';
        char v = 'V';
        char s = 'S';
        
        for (char c : diasSeleccionads.toCharArray()) {
            if(c==l){
                this.setLunes(true);
            }
            if(c==t){
                this.setMartes(true);
            }
            if(c==m){
                this.setMiercoles(true);
            }
            if(c==j){
                this.setJueves(true) ;
            }
            if(c==v){
                this.setViernes(true);
            }
            if(c==s){
                this.setSabado(true) ;
            }
        }

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
            PrimeFaces.current().executeScript("PF('dlgSinGuardadCliente').show();");
        } else {
            PrimeFaces.current().executeScript("PF('dlgNuevoCliente').hide();");
        }

    }
    
    public void cerrarDialogosAgregar() {
        
        PrimeFaces.current().executeScript("PF('dlgSinGuardadCliente').hide();");
        PrimeFaces.current().executeScript("PF('dlgNuevoCliente').hide();");
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
    
    public String concatenarDias(){
        String dias = "";
        if(lunes){
            dias=dias+"L";
        }
        if(martes){
            dias=dias+"T";
        }
        if(miercoles){
            dias=dias+"M";
        }
        if(jueves){
            dias=dias+"J";
        }
        if(viernes){
            dias=dias+"V";
        }
        if(sabado){
            dias=dias+"S";
        }
        
        return dias.trim();
        
    }
    
    
    public String estadoCliente(String estado){
        if(estado.equals("A")){
            return "ACTIVO";
        }
        return "OTRO";
    }
    
    public void reloadDias(){
        this.lunes = false;
        this.martes = false;
        this.miercoles = false;
        this.jueves = false;
        this.viernes = false;
        this.sabado = false;
    }

}
