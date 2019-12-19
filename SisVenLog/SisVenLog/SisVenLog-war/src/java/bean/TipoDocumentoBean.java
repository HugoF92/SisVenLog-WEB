package bean;

import dao.TiposDocumentosFacade;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class TipoDocumentoBean implements Serializable {

    @EJB
    private TiposDocumentosFacade tipoDocumentoFacade;

    private String filtro = "";

    private TiposDocumentos tipoDocumento = new TiposDocumentos();
    private List<TiposDocumentos> listaTipoDocumento = new ArrayList<TiposDocumentos>();

    private boolean habBtnEdit;
    private boolean habBtnAct;
    private boolean habBtnInac;

    public TipoDocumentoBean() {

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

    public TiposDocumentos getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentos tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<TiposDocumentos> getListaTipoDocumento() {
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List<TiposDocumentos> listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaTipoDocumento = new ArrayList<TiposDocumentos>();
        this.tipoDocumento = new TiposDocumentos();
        this.setHabBtnEdit(true);
        this.setHabBtnAct(true);
        this.setHabBtnInac(true);

        this.filtro = "";
        
        tipoDocumento.setMdebCred('D');
        tipoDocumento.setMafectaCtacte('N');
        tipoDocumento.setMincluIva('N');
        tipoDocumento.setMfijoSis('N');
        tipoDocumento.setMafectaStockRes('S');
        
        listar();

    RequestContext.getCurrentInstance().update("formTipoDocumento");
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void nuevo() {
        this.tipoDocumento = new TiposDocumentos();
    }

    public List<TiposDocumentos> listar() {
        listaTipoDocumento = tipoDocumentoFacade.findAll();
        return listaTipoDocumento;
    }


    public void insertar() {
        try {
            
            
            
            if (tipoDocumento.getMdebCred()!= null &&
                    tipoDocumento.getMdebCred().equals("D")) {
                tipoDocumento.setMindice(new Float(1));
            }else{
               tipoDocumento.setMindice(new Float(-1)); 
            }

            tipoDocumento.setXdesc(tipoDocumento.getXdesc().toUpperCase());
            tipoDocumento.setFalta(new Date());
            tipoDocumento.setCusuario("admin");

            tipoDocumentoFacade.create(tipoDocumento);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El registro fue creado con exito."));

            RequestContext.getCurrentInstance().execute("PF('dlgNuevTipoDocumento').hide();");

            instanciar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void editar() {
        try {

            if ("".equals(this.tipoDocumento.getXdesc())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campo requerido", "Debe ingresar una descripcion."));
                return;

            } else {

                tipoDocumento.setXdesc(tipoDocumento.getXdesc().toUpperCase());
                tipoDocumentoFacade.edit(tipoDocumento);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado con exito."));

                instanciar();

                listar();

                RequestContext.getCurrentInstance().execute("PF('dlgEditTipoDocumento').hide();");

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void borrar() {
        try {

            tipoDocumentoFacade.remove(tipoDocumento);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            instanciar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacTipoDocumento').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", e.getMessage()));
        }
    }

    public void onRowSelect(SelectEvent event) {

        if ("" != this.tipoDocumento.getXdesc()) {
            this.setHabBtnEdit(false);
        } else {
            this.setHabBtnEdit(true);
        }

        
    }
    
     public String tipoDocumentoDC(Float indice) {

        String retorno = "";

        Float aux = indice;

        if (aux < 0) {
            retorno = "CREDITO";
        }
        if (aux >= 0) {
            retorno = "DEBITO";
        }
        return retorno;

    }

    public void verificarCargaDatos() {

        boolean cargado = false;

        if (tipoDocumento != null) {

            if (tipoDocumento.getXdesc() != null) {
                cargado = true;
            }
        }

        if (cargado) {
            RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarTipoDocumento').show();");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgNuevTipoDocumento').hide();");
        }

    }

    public void cerrarDialogosAgregar() {
        RequestContext.getCurrentInstance().execute("PF('dlgSinGuardarTipoDocumento').hide();");
        RequestContext.getCurrentInstance().execute("PF('dlgNuevTipoDocumento').hide();");

    }

}
