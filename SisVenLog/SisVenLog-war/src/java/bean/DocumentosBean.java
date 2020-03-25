package bean;

import dao.DocumVariosFacade;
import dao.NotasComprasFacade;
import dao.ProveedoresFacade;
import dao.TiposDocumentosFacade;
import entidad.DocumVarios;
import entidad.DocumVariosPK;
import entidad.NotasCompras;
import entidad.NotasComprasPK;
import entidad.Proveedores;
import entidad.TiposDocumentos;
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
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class DocumentosBean implements Serializable {

       
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private ProveedoresFacade proveedoresFacade;
    
    @EJB
    private DocumVariosFacade documVariosFacade;
    
    @EJB
    private NotasComprasFacade notasComprasFacade;
    
    
    private String codProveedor;
    private String codTipoDocumento;
    private String nroDocumentoEst;
    private String nroDocumentoExp;
    private Long nroDocumentoNumero = 0L;
    private String observacion;
    
    
   
    private DocumVarios documVarios = new DocumVarios();
    private NotasCompras notasCompras= new NotasCompras();
    private DocumVariosPK documVariosPK = new DocumVariosPK();
    
    @SuppressWarnings("Convert2Diamond")
    private List<TiposDocumentos> listaTiposDocumentos = new ArrayList<TiposDocumentos>();
    
    @SuppressWarnings("Convert2Diamond")
    private List<Proveedores> listaProveedores = new ArrayList<Proveedores>();
       
     

    private boolean habBtnBuscar;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public DocumentosBean() {
        this.codTipoDocumento = null;

    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    
    public Long getNroDocumentoNumero() {
        return nroDocumentoNumero;
    }

    public void setNroDocumentoNumero(Long nroDocumentoNumero) {
        this.nroDocumentoNumero = nroDocumentoNumero;
    }

    public String getNroDocumentoEst() {
        return nroDocumentoEst;
    }

    public void setNroDocumentoEst(String nroDocumentoEst) {
        this.nroDocumentoEst = nroDocumentoEst;
    }

    public String getNroDocumentoExp() {
        return nroDocumentoExp;
    }

    public void setNroDocumentoExp(String nroDocumentoExp) {
        this.nroDocumentoExp = nroDocumentoExp;
    }

    public String getCodProveedor() {
        return codProveedor;
    }

    public void setCodProveedor(String codProveedor) {
        this.codProveedor = codProveedor;
    }

    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    public void setCodTipoDocumento(String codTipoDocumento) {
        this.codTipoDocumento = codTipoDocumento;
    }

    
    public boolean isHabBtnBuscar() {
        return habBtnBuscar;
    }

    public void setHabBtnBuscar(boolean habBtnBuscar) {
        this.habBtnBuscar = habBtnBuscar;
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

    public NotasCompras getNotasCompras() {
        return notasCompras;
    }

    public void setNotasCompras(NotasCompras notasCompras) {
        this.notasCompras = notasCompras;
    }

    public TiposDocumentosFacade getTiposDocumentosFacade() {
        return tiposDocumentosFacade;
    }

    public void setTiposDocumentosFacade(TiposDocumentosFacade tiposDocumentosFacade) {
        this.tiposDocumentosFacade = tiposDocumentosFacade;
    }

    public List<TiposDocumentos> getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

    public DocumVarios getDocumVarios() {
        return documVarios;
    }

    public void setDocumVarios(DocumVarios documVarios) {
        this.documVarios = documVarios;
    }

    public ProveedoresFacade getProveedoresFacade() {
        return proveedoresFacade;
    }

    public void setProveedoresFacade(ProveedoresFacade proveedoresFacade) {
        this.proveedoresFacade = proveedoresFacade;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public DocumVariosFacade getDocumVariosFacade() {
        return documVariosFacade;
    }

    public void setDocumVariosFacade(DocumVariosFacade documVariosFacade) {
        this.documVariosFacade = documVariosFacade;
    }

    public DocumVariosPK getDocumVariosPK() {
        return documVariosPK;
    }

    public void setDocumVariosPK(DocumVariosPK documVariosPK) {
        this.documVariosPK = documVariosPK;
    }

    public NotasComprasFacade getNotasComprasFacade() {
        return notasComprasFacade;
    }

    public void setNotasComprasFacade(NotasComprasFacade notasComprasFacade) {
        this.notasComprasFacade = notasComprasFacade;
    }
    
     

    @PostConstruct
    @SuppressWarnings("Convert2Diamond")
    public void instanciar() {
        
        this.documVarios = new DocumVarios();
        this.notasCompras = new NotasCompras();
        this.documVariosPK = new DocumVariosPK();
        this.setHabBtnBuscar(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);
        

        listarTiposDocumentos();
        listarProveedores();

    }
    
    @SuppressWarnings("Convert2Diamond")
    public void limpiar() throws IOException {
        
        this.documVarios = new DocumVarios();
        this.notasCompras = new NotasCompras();

        this.setHabBtnBuscar(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);
        listarTiposDocumentos();
        listarProveedores();
        this.nroDocumentoNumero = 0L;
        this.reload();
        

    }
    
    public void reload() throws IOException {
    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
   }
    
    @SuppressWarnings("Convert2Diamond")
    public void buscar() {
        short codEmpresa = 2;
        if (this.codTipoDocumento.equals("AJ")) {
            DocumVarios documentosVarios = new DocumVarios();
            documVariosPK.setCtipoDocum(this.codTipoDocumento);
//        codigo empresa en durango
            
            documVariosPK.setCodEmpr(codEmpresa);
            documVariosPK.setNdocum(this.nroDocumentoNumero.intValue());
            this.documVarios.setDocumVariosPK(documVariosPK);
            try {
                documentosVarios = this.documVariosFacade.find(this.documVariosPK);

            } catch (Exception e) {
                documentosVarios = null;
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));

            }
            if (documentosVarios == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error al obtener los datos", "Veifique los campos"));
            } else {
                this.codTipoDocumento = documentosVarios.getDocumVariosPK().getCtipoDocum();
                this.nroDocumentoNumero = new Long(documentosVarios.getDocumVariosPK().getNdocum());
                this.observacion = documentosVarios.getXobs() != null && !documentosVarios.getXobs().isEmpty() ? documentosVarios.getXobs() : "No tiene observacion";
            }
        }else{
            NotasCompras notasCompraslocal = new NotasCompras();
            NotasComprasPK notasComprasPK = new NotasComprasPK();
            notasComprasPK.setCodEmpr(codEmpresa);
            notasComprasPK.setCodProveed(Short.parseShort(this.codProveedor));
            notasComprasPK.setCtipoDocum(this.codTipoDocumento);
            String nroConcatenado = this.nroDocumentoEst+this.nroDocumentoExp+this.nroDocumentoNumero.toString();
            notasComprasPK.setNroNota(new Long(nroConcatenado));
            try {
                notasCompraslocal = notasComprasFacade.getNotasComprasByPK(notasComprasPK);
            } catch (Exception e) {
                e.printStackTrace();
                notasCompraslocal = null;
            }
            if(notasCompraslocal!= null){
                System.out.println("no es nulo");
            }
            
        }
        
        
        
        

    }

    @SuppressWarnings("Convert2Diamond")
    public void nuevo() {
        this.documVarios = new DocumVarios();
        this.notasCompras = new NotasCompras();
 
    }
    
    
    
    

    public void verificarCargaDatos() {

       boolean cargado = false;

        if (documVarios != null) {

            if (documVarios.getDocumVariosPK().getCtipoDocum()!= null) {
                cargado = true;
            }
        }

//        if (cargado) {
//            PrimeFaces.current().executeScript("PF('dlgSinGuardadDocumento').show();");
//        } else {
//            PrimeFaces.current().executeScript("PF('dlgNuevoDocumento').hide();");
//        }

    }
    
    public List<TiposDocumentos> listarTiposDocumentos(){
        listaTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentosAJNCC();
        return listaTiposDocumentos;
    }
    
    public List<Proveedores> listarProveedores(){
        listaProveedores = proveedoresFacade.findAll();
        return listaProveedores;
    }
    
    
    
    public void verificarDatos(){

       
    }
    
    
    
    
    
    
    

}
