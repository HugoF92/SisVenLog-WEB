package bean;

import dao.TiposDocumentosFacade;
import entidad.TiposDocumentos;
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
public class ImpresionNotaCreditoBean {

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    private TiposDocumentos tiposDocumentos;
    private List<TiposDocumentos> listaTiposDocumentos;

    private Integer estabInicial;
    private Integer expedInicial;
    private Integer SecueInicial;
    private Integer SecueFinal;

    private String destination = System.getProperty("java.io.tmpdir");

    public ImpresionNotaCreditoBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.tiposDocumentos = new TiposDocumentos();
        this.listaTiposDocumentos = new ArrayList<TiposDocumentos>();

        estabInicial = 1;
        expedInicial = 1;
        SecueInicial = 1;
        SecueFinal = 1;

    }

    public List<TiposDocumentos> listarTiposDocummentos() {
        listaTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoImpresionMasivaFacturas();
        return listaTiposDocumentos;
    }

    public void impresion() {
        try {
            
            int nroInicial = 0;
            int nroFinal = 0;
            
            
            nroInicial = Integer.parseInt(this.estabInicial + String.format("%02d",this.expedInicial)+String.format("%07d",this.SecueInicial));
            nroFinal = Integer.parseInt(this.estabInicial + String.format("%02d",this.expedInicial)+String.format("%07d",this.SecueFinal));

            
            System.err.println("nroInicial: " + nroInicial);
            System.err.println("nroFinal: " + nroFinal);
            
            LlamarReportes rep = new LlamarReportes();

            //generamos los pdf para imprimir
            rep.impresionNotaCredito(nroInicial, nroFinal);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atencion.", "Proceso de impresion correcto."));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error durante la impresion", null));
        }
    }
    
    public void vistaPrevia() {
        try {
            
            int nroInicial = 0;
            int nroFinal = 0;
            
            
            nroInicial = Integer.parseInt(this.estabInicial + String.format("%02d",this.expedInicial)+String.format("%07d",this.SecueInicial));
            nroFinal = Integer.parseInt(this.estabInicial + String.format("%02d",this.expedInicial)+String.format("%07d",this.SecueFinal));

            
            System.err.println("nroInicial: " + nroInicial);
            System.err.println("nroFinal: " + nroFinal);
            
            LlamarReportes rep = new LlamarReportes();

            //generamos los pdf para imprimir
            rep.vistaPreviaNotaCredito(nroInicial, nroFinal);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atencion.", "Proceso de impresion correcto."));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error durante la impresion", null));
        }
    }
    

    //Getters & Setters
    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public List<TiposDocumentos> getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

    public Integer getEstabInicial() {
        return estabInicial;
    }

    public void setEstabInicial(Integer estabInicial) {
        this.estabInicial = estabInicial;
    }

    public Integer getExpedInicial() {
        return expedInicial;
    }

    public void setExpedInicial(Integer expedInicial) {
        this.expedInicial = expedInicial;
    }

    public Integer getSecueInicial() {
        return SecueInicial;
    }

    public void setSecueInicial(Integer SecueInicial) {
        this.SecueInicial = SecueInicial;
    }

    public Integer getSecueFinal() {
        return SecueFinal;
    }

    public void setSecueFinal(Integer SecueFinal) {
        this.SecueFinal = SecueFinal;
    }

}
