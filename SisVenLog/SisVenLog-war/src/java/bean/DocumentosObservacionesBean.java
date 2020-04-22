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
public class DocumentosObservacionesBean implements Serializable {

       
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    
    @EJB
    private ProveedoresFacade proveedoresFacade;
    
    @EJB
    private DocumVariosFacade documVariosFacade;
    
    @EJB
    private NotasComprasFacade notasComprasFacade;
    
    
    private String codProveedor;
    private String codTipoDocumento = null;
    private String nroDocumentoEst;
    private String nroDocumentoExp;
    private Long nroDocumentoNumero;
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

    public DocumentosObservacionesBean() {
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
        this.setNroDocumentoNumero(0L);
        this.documVarios = new DocumVarios();
        this.notasCompras = new NotasCompras();
        this.documVariosPK = new DocumVariosPK();
        this.setHabBtnBuscar(true);
        this.setHabBtnAct(false);
        this.setHabBtnInac(true);
        

        listarTiposDocumentos();
        listarProveedores();

    }
    
    @SuppressWarnings("Convert2Diamond")
    public void limpiar() {
        this.documVarios = new DocumVarios();
        this.notasCompras = new NotasCompras();
        this.setHabBtnBuscar(true);
        this.setHabBtnAct(false);
        this.setHabBtnInac(true);
        this.nroDocumentoNumero = 0L;
        this.nroDocumentoExp = "";
        this.nroDocumentoEst = "";
        this.codTipoDocumento = null;
        this.observacion = "";
        listarTiposDocumentos();
        listarProveedores();
        
        
    }
    
   @SuppressWarnings("Convert2Diamond")
    public void insertOrUpdate(){
        if (this.codTipoDocumento.equals("AJ")) {
            this.documVarios.setXobs(this.observacion.toUpperCase());
            try {
                this.documVarios = this.documVariosFacade.verificarObjecto(documVarios);
                this.documVariosFacade.edit(documVarios);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"", "Datos Guardados"));
            } catch (Exception e) {
                e.printStackTrace();
                this.setHabBtnAct(false);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar", e.getMessage()+" - "+e.getLocalizedMessage()));

            }
        }else{
            try {
                this.notasCompras.setXobs(this.observacion);
                this.notasCompras = notasComprasFacade.verificarObjecto(this.notasCompras);
                notasComprasFacade.edit(notasCompras);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"", "Datos Guardados"));
            } catch (Exception e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar", e.getMessage()+" - "+e.getLocalizedMessage()));
                this.setHabBtnAct(false);
            }
            
        }   
        this.limpiar();
    }
    
    public void reloadPage() throws IOException {
    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
   }
    
    @SuppressWarnings("Convert2Diamond")
    public void buscar() {
        short codEmpresa = 2;
        Long numeroNumero = this.nroDocumentoNumero;
        if(this.codTipoDocumento != null){
            if (this.codTipoDocumento.equals("AJ")) {
            DocumVarios documentosVarios = new DocumVarios();
            documVariosPK.setCtipoDocum(this.codTipoDocumento);
//          codigo empresa en durango
            documVariosPK.setCodEmpr(codEmpresa);
            documVariosPK.setNdocum(this.nroDocumentoNumero.intValue());
            this.documVarios.setDocumVariosPK(documVariosPK);
            try {
                this.documVarios = this.documVariosFacade.find(this.documVariosPK);
                documentosVarios = this.documVarios;
                if(documentosVarios!= null){
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"", "Datos Obtenidos")); 
                }
                
            } catch (Exception e) {
                documentosVarios = null;
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));

            }
            if (documentosVarios == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "Documento no encontrado"));
                this.setHabBtnAct(false);
            } else {
                this.codTipoDocumento = documentosVarios.getDocumVariosPK().getCtipoDocum();
                this.nroDocumentoNumero = new Long(documentosVarios.getDocumVariosPK().getNdocum());
                this.observacion = documentosVarios.getXobs() != null && !documentosVarios.getXobs().isEmpty() ? documentosVarios.getXobs().toUpperCase().trim() : " ";
                this.setHabBtnAct(true);
            }
        }else{
            NotasCompras notasCompraslocal = new NotasCompras();
            try {
            NotasComprasPK notasComprasPK = new NotasComprasPK();
//          codigo empresa en durango
            notasComprasPK.setCodEmpr(codEmpresa);
            notasComprasPK.setCodProveed(Short.parseShort(this.codProveedor));
            notasComprasPK.setCtipoDocum(this.codTipoDocumento);
            String nroConcatenado = this.nroDocumentoEst+conversorNumeroExp(this.nroDocumentoExp)+conversorNumeroNota(this.nroDocumentoNumero.toString());
            notasComprasPK.setNroNota(new Long(nroConcatenado));
                this.notasCompras = notasComprasFacade.getNotasComprasByPK(notasComprasPK);
                notasCompraslocal = this.notasCompras;
                if(notasCompraslocal!= null){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"", "Datos Obtenidos"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                notasCompraslocal = null;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
                this.setHabBtnAct(false);

                
            }
            if(notasCompraslocal== null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", "Documento no encontrado"));
            }else{
                this.codTipoDocumento = notasCompraslocal.getNotasComprasPK().getCtipoDocum();
                this.nroDocumentoEst = this.nroDocumentoEst;
                this.nroDocumentoExp = this.nroDocumentoExp;
                this.nroDocumentoNumero = numeroNumero;
                this.observacion = notasCompraslocal.getXobs()!=null && !notasCompraslocal.getXobs().isEmpty()? notasCompraslocal.getXobs().toUpperCase().trim():"";
                this.setHabBtnAct(true);
            }
            
        }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un tipo de documento."));
        }
        
        
    }

    @SuppressWarnings("Convert2Diamond")
    public void nuevo() {
        this.documVarios = new DocumVarios();
        this.notasCompras = new NotasCompras();
 
    }
        
    public List<TiposDocumentos> listarTiposDocumentos(){
        listaTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentosAJNCC();
        return listaTiposDocumentos;
    }
    
    public List<Proveedores> listarProveedores(){
        listaProveedores = proveedoresFacade.findAll();
        return listaProveedores;
    }
    
    
    
    public boolean onTipoDocumentoDropDownChange(){
        if(this.codTipoDocumento != null  && !this.codTipoDocumento.equals("AJ")){
            if(this.getNroDocumentoNumero()!= null && this.getNroDocumentoNumero().intValue()==0){
                this.setNroDocumentoNumero(0L);
            }
            return true;
        }
        return false;
       
    }
    
    
    
    public String conversorNumeroNota(String numeroIngresado){
        String numero = "0000000";
        int cantidadDigitosNumeroIngresado = numeroIngresado.length();
        int numeroFin = numero.length()-cantidadDigitosNumeroIngresado;
        switch(cantidadDigitosNumeroIngresado){
            case 1:
                numero = numero.substring(0,numeroFin);
                break;
            case 2:
                numero = numero.substring(0,numeroFin);
                break;
            case 3:
                numero = numero.substring(0,numeroFin);
                break;
            case 4:
                numero = numero.substring(0,numeroFin);
                break;
            case 5:
                numero = numero.substring(0,numeroFin);
                break;
            case 6:
                numero = numero.substring(0,numeroFin);
                break;
            case 7:
                numero = numeroIngresado;
                break;
        }
        if(cantidadDigitosNumeroIngresado<7){
            numero = numero+numeroIngresado;
        }
        
        return numero;
    }
    
    
    public String conversorNumeroExp(String numeroIngresado){
        String numero = "00";
        int cantidadDigitosNumeroIngresado = numeroIngresado.length();
        int numeroFin = numero.length()-cantidadDigitosNumeroIngresado;
        switch(cantidadDigitosNumeroIngresado){
            case 1:
                numero = numero.substring(0,numeroFin);
                break;
            case 2:
                numero = numeroIngresado;
                break;
        }
        if(cantidadDigitosNumeroIngresado==1){
            numero = numero+numeroIngresado;
        }
        return numero;
    }
    
    

}
