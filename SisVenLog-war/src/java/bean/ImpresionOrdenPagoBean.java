package bean;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.application.*;
import javax.faces.bean.*;
import javax.faces.context.*;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class ImpresionOrdenPagoBean {

    private Integer nroinicial;
    private Integer nrofinal;

    private String destination = System.getProperty("java.io.tmpdir");

    public ImpresionOrdenPagoBean() throws IOException {

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
            
            LlamarReportes rep = new LlamarReportes();

            //generamos los pdf para imprimir
            rep.impresionOrdenPago(nroinicial, nrofinal);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atencion.", "Proceso de impresion correcto."));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error durante la impresion", null));
        }
    }
    
    public void vistaPrevia() {
        try {
            
            LlamarReportes rep = new LlamarReportes();

            //generamos los pdf para imprimir
            rep.vistaPreviaOrdenPago(nroinicial, nrofinal);

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
