package bean;

import dao.PersonalizedFacade;
import dao.TiposDocumentosFacade;
import dto.AuxiliarImpresionMasivaDto;
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
public class ImpresionMasivaFacturasBean {

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private PersonalizedFacade personalizedFacade;

    private TiposDocumentos tiposDocumentos;
    private List<TiposDocumentos> listaTiposDocumentos;

    private AuxiliarImpresionMasivaDto auxiliarImpresionMasivaDto;
    private List<AuxiliarImpresionMasivaDto> listaAuxiliarImpresionMasivaDto;

    private Integer estabInicial;
    private Integer expedInicial;
    private Integer SecueInicial;
    private Integer SecueFinal;

    private String destination = System.getProperty("java.io.tmpdir");

    public ImpresionMasivaFacturasBean() throws IOException {

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

            if (this.validarCorrelatividad()) {
                int nroInicial = 0;
                int nroFinal = 0;

                nroInicial = Integer.parseInt(this.estabInicial + String.format("%02d", this.expedInicial) + String.format("%07d", this.SecueInicial));
                nroFinal = Integer.parseInt(this.estabInicial + String.format("%02d", this.expedInicial) + String.format("%07d", this.SecueFinal));

                System.err.println("nroInicial: " + nroInicial);
                System.err.println("nroFinal: " + nroFinal);

                LlamarReportes rep = new LlamarReportes();

                //generamos los pdf para imprimir
                rep.impresionFactura(nroInicial, nroFinal);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Impresion enviada", "En breve comenzara la impresion, aguarde."));

            }

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error durante la impresion", null));
        }
    }

    public void vistaPrevia() {
        try {

            if (this.validarCorrelatividad()) {
                int nroInicial = 0;
                int nroFinal = 0;

                nroInicial = Integer.parseInt(this.estabInicial + String.format("%02d", this.expedInicial) + String.format("%07d", this.SecueInicial));
                nroFinal = Integer.parseInt(this.estabInicial + String.format("%02d", this.expedInicial) + String.format("%07d", this.SecueFinal));

                System.err.println("nroInicial: " + nroInicial);
                System.err.println("nroFinal: " + nroFinal);

                LlamarReportes rep = new LlamarReportes();

                //generamos los pdf para imprimir
                rep.vistaPreviaFactura(nroInicial, nroFinal);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Impresion enviada", "En breve comenzara la impresion, aguarde."));

            }

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

    public AuxiliarImpresionMasivaDto getAuxiliarImpresionMasivaDto() {
        return auxiliarImpresionMasivaDto;
    }

    public void setAuxiliarImpresionMasivaDto(AuxiliarImpresionMasivaDto auxiliarImpresionMasivaDto) {
        this.auxiliarImpresionMasivaDto = auxiliarImpresionMasivaDto;
    }

    public List<AuxiliarImpresionMasivaDto> getListaAuxiliarImpresionMasivaDto() {
        return listaAuxiliarImpresionMasivaDto;
    }

    public void setListaAuxiliarImpresionMasivaDto(List<AuxiliarImpresionMasivaDto> listaAuxiliarImpresionMasivaDto) {
        this.listaAuxiliarImpresionMasivaDto = listaAuxiliarImpresionMasivaDto;
    }

    public boolean validarCorrelatividad() {

        boolean respuesta = true;

        try {

            int nroInicial = 0;
            int nroFinal = 0;

            nroInicial = Integer.parseInt(this.estabInicial + String.format("%02d", this.expedInicial) + String.format("%07d", this.SecueInicial));
            nroFinal = Integer.parseInt(this.estabInicial + String.format("%02d", this.expedInicial) + String.format("%07d", this.SecueFinal));

            System.err.println("nroInicial: " + nroInicial);
            System.err.println("nroFinal: " + nroFinal);

            this.listaAuxiliarImpresionMasivaDto = personalizedFacade.listarSecuenciasFacturas(nroInicial, nroFinal);

            for (int i = 0; i < listaAuxiliarImpresionMasivaDto.size(); i++) {

                if (!listaAuxiliarImpresionMasivaDto.get(0).getEstado().equals("A")) {

                } else {
                    if ((i + 1) < listaAuxiliarImpresionMasivaDto.size()) {
                        if ((listaAuxiliarImpresionMasivaDto.get(i + 1).getSecuencia() - listaAuxiliarImpresionMasivaDto.get(i).getSecuencia()) == 1 
                                && listaAuxiliarImpresionMasivaDto.get(0).getEstado().equals("A")) {

                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "La numeracion se interrumpe en " + listaAuxiliarImpresionMasivaDto.get(i).getFactura()));
                            return false;
                        }

                    }
                }

            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al validar correlatividad", null));
        }

        return respuesta;
    }

}
