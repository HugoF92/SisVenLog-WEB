package bean;

import dao.BancosFacade;
import entidad.Bancos;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.*;
import javax.faces.bean.*;
import javax.faces.context.*;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class ImpresionChequesBean {
    
    @EJB
    private BancosFacade bancosFacade;

    private Integer nroinicial;
    private Integer nrofinal;
    private Integer zona;
    
    private Bancos bancos = new Bancos();
    private List<Bancos> listaBancos = new ArrayList<Bancos>();

    private String destination = System.getProperty("java.io.tmpdir");
    private String tipo = "";

    public ImpresionChequesBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        nroinicial = 1;
        nrofinal = 1;
        zona = 1;

    }
    
    public List<Bancos> listarBancos(){
        this.listaBancos = bancosFacade.findAll();
        return listaBancos;
    }

    public void impresion() {
        try {
            
            if (this.nroinicial == null || this.nrofinal == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe cargar ambos numeros", null));
                return;
            }
            
            if (this.nroinicial < this.nrofinal) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El numero final no puede ser menor al numero inicial", null));
                return;
            }
            
            if (this.bancos == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el banco", null));
                return;
            }
            
            if (this.tipo == null || this.tipo == "") {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el tipo de cheque", null));
                return;
            }
            
            LlamarReportes rep = new LlamarReportes();
            
            if (this.tipo == "DIA") {
                rep.impresionChequeDia(this.nroinicial, this.nrofinal, Integer.parseInt(this.bancos.getCodBanco()+""));
            }else{
                rep.impresionChequeDife(this.nroinicial, this.nrofinal, Integer.parseInt(this.bancos.getCodBanco()+""));
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atencion.", "Proceso de impresion correcto."));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error durante la impresion", null));
        }
    }
    
    public void vistaPrevia() {
        try {
            
            if (this.nroinicial == null || this.nrofinal == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe cargar ambos numeros", null));
                return;
            }
            
            if (this.nroinicial < this.nrofinal) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El numero final no puede ser menor al numero inicial", null));
                return;
            }
            
            if (this.bancos == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el banco", null));
                return;
            }
            
            if (this.tipo == null || this.tipo == "") {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el tipo de cheque", null));
                return;
            }
            
            LlamarReportes rep = new LlamarReportes();
            
            if (this.tipo == "DIA") {
                rep.impresionChequeDia(this.nroinicial, this.nrofinal, Integer.parseInt(this.bancos.getCodBanco()+""));
            }else{
                rep.impresionChequeDife(this.nroinicial, this.nrofinal, Integer.parseInt(this.bancos.getCodBanco()+""));
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atencion.", "Proceso de impresion correcto."));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error durante la impresion", null));
        }
    }
    

    //Getters & Setters

    public Integer getNroinicial() {
        return nroinicial;
    }

    public void setNroinicial(Integer nroinicial) {
        this.nroinicial = nroinicial;
    }

    public Integer getNrofinal() {
        return nrofinal;
    }

    public void setNrofinal(Integer nrofinal) {
        this.nrofinal = nrofinal;
    }

    public Integer getZona() {
        return zona;
    }

    public void setZona(Integer zona) {
        this.zona = zona;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
    

}
