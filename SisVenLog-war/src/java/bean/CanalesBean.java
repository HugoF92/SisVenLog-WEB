package bean;

import bean.buscadores.BuscadorBean;
import dao.CanalesCompraFacade;
import dto.buscadorDto;
import entidad.CanalesCompra;
import entidad.CanalesCompraPK;
import entidad.Proveedores;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class CanalesBean implements Serializable {
    
    @EJB
    private CanalesCompraFacade canalesFacade;
    
    private CanalesCompra canales = new CanalesCompra();
    private Proveedores proveedores = new Proveedores();
    private String codProveedor;
    private String nomProveedor;
    private buscadorDto resultadoProveedor;
    
    private List<CanalesCompra> listaCanales = new ArrayList<CanalesCompra>();
    
    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;
    
    @ManagedProperty("#{buscadorBean}")
    private BuscadorBean buscadorBean;
    
    public CanalesBean() {

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
    
    public CanalesCompra getCanales() {
        return canales;
    }
    
    public void setCanales(CanalesCompra canales) {
        this.canales = canales;
    }
    
    public List<CanalesCompra> getListaCanales() {
        return listaCanales;
    }
    
    public void setListaCanales(List<CanalesCompra> listaCanales) {
        this.listaCanales = listaCanales;
    }
    
    public Proveedores getProveedores() {
        return proveedores;
    }
    
    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }
    
    public BuscadorBean getBuscadorBean() {
        return buscadorBean;
    }
    
    public void setBuscadorBean(BuscadorBean buscadorBean) {
        this.buscadorBean = buscadorBean;
    }

    public String getCodProveedor() {
        return codProveedor;
    }

    public void setCodProveedor(String codProveedor) {
        this.codProveedor = codProveedor;
    }

    public String getNomProveedor() {
        return nomProveedor;
    }

    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor = nomProveedor;
    }

    public buscadorDto getResultadoProveedor() {
        return resultadoProveedor;
    }

    public void setResultadoProveedor(buscadorDto resultadoProveedor) {
        this.resultadoProveedor = resultadoProveedor;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        
        listaCanales = new ArrayList<CanalesCompra>();
        this.canales = new CanalesCompra(new CanalesCompraPK());
        this.proveedores = new Proveedores();
        this.resultadoProveedor = new buscadorDto();
        
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);
        
        codProveedor = "";
        nomProveedor = "";
        
        listar();
        
        buscadorBean.instanciar();

        //RequestContext.getCurrentInstance().update("formPersonas");
    }
    
    public void nuevo() {
        this.canales = new CanalesCompra();
        listaCanales = new ArrayList<CanalesCompra>();
        
    }
    
    public List<CanalesCompra> listar() {
        listaCanales = canalesFacade.findAll();
        return listaCanales;
    }

      
    public void insertar() {
        try {
            
            proveedores.setCodProveed(new Short(buscadorBean.getResultado().getDato1()));
            
            canales.setProveedores(proveedores);
            canales.getCanalesCompraPK().setCodProveed(new Short(buscadorBean.getResultado().getDato1()));
            canales.setXdesc(canales.getXdesc().toUpperCase());
            canales.setFalta(new Date());
            canales.setCusuario("admin");
            
            canalesFacade.create(canales);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));
            
            RequestContext.getCurrentInstance().execute("PF('dlgNuevCanales').hide();");
            
            instanciar();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }
    
    public void editar() {
        try {
            
            if ("".equals(this.canales.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;
                
            } else {
                
                canales.setXdesc(canales.getXdesc().toUpperCase());
                canalesFacade.edit(canales);
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));
                
                instanciar();
                
                listar();
                
                RequestContext.getCurrentInstance().execute("PF('dlgEditCanales').hide();");
                
            }
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }
    
    public void borrar() {
        try {
            
            canalesFacade.remove(canales);
            this.canales = new CanalesCompra();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacCanales').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }
    
    public void onRowSelect(SelectEvent event) {
        
        if ("" != this.canales.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }
        
    }
    
    public void verificarCargaDatos() {
        
        boolean cargado = false;
        
        if (canales != null) {
            
            if (canales.getXdesc() != null) {
                cargado = true;
            }
        }
        
        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarCanales').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevCanales').hide();");
        }
        
    }
    
    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarCanales').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevCanales').hide();");
        
    }
    
    public void buscarProveedor() {
        
        buscadorBean.instanciar();
        
        
        buscadorBean.setVentana("maCanales");
        buscadorBean.getColumnas().add("Código");
        buscadorBean.getColumnas().add("RUC");
        buscadorBean.getColumnas().add("Razón Social");
        
        buscadorBean.setSql("select top 100 cod_proveed, coalesce(xruc,'0') as xruc, xnombre\n"
                + "from proveedores \n"
                + "where cod_proveed is not null");
        
        buscadorBean.getFiltros().add("xruc");
        buscadorBean.getFiltros().add("xnombre");
        
        buscadorBean.setTitulo("Proveedor");
        
        RequestContext.getCurrentInstance().execute("PF('dlgBuscador').show();");
        
    }
    
}
