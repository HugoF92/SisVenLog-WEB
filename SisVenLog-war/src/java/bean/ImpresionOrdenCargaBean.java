package bean;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.application.*;
import javax.faces.bean.*;
import javax.faces.context.*;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class ImpresionOrdenCargaBean {

    private Integer nroinicial;
    private Integer nrofinal;

    private String destination = System.getProperty("java.io.tmpdir");

    public ImpresionOrdenCargaBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        nroinicial = 1;
        nrofinal = 1;

    }

    public void impresion() {
        try {
            
            /*if (this.nroinicial == null || this.nrofinal == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe cargar ambos numeros", null));
                return;
            }
            
            if (this.nroinicial < this.nrofinal) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El numero final no puede ser menor al numero inicial", null));
                return;
            }*/
            LlamarReportes rep = new LlamarReportes();

            //generamos los pdf para imprimir
            rep.impresionOrdenCarga(nroinicial, nrofinal);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atencion.", "Proceso de impresion correcto."));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error durante la impresion", null));
        }
    }
    
    public void vistaPrevia() {
        try {
            
            /*if (this.nroinicial == null || this.nrofinal == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe cargar ambos numeros", null));
                return;
            }
            
            if (this.nroinicial < this.nrofinal) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El numero final no puede ser menor al numero inicial", null));
                return;
            }*/
            LlamarReportes rep = new LlamarReportes();

            //generamos los pdf para imprimir
            rep.vistaPreviaOrdenCarga(nroinicial, nrofinal);

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
    

}
