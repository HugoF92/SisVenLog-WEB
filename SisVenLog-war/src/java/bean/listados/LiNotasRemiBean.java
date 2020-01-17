package bean.listados;

import dao.DepositosFacade;
import dao.EmpleadosFacade;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Empleados;
import entidad.EmpleadosPK;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class LiNotasRemiBean {

    @EJB
    private DepositosFacade depositosFacade;

    private Depositos depositos;
    private List<Depositos> listaDepositos;

    private Date desde;
    private Date hasta;

    private BigDecimal nrodesde;
    private BigDecimal nrohasta;
    private String  estado;
    private Boolean  conDetalles;

    public LiNotasRemiBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.depositos = new Depositos(new DepositosPK());
        this.listaDepositos = new ArrayList<Depositos>();

        this.desde = new Date();
        this.hasta = new Date();

        nrodesde = new BigDecimal("1");
        nrohasta = new BigDecimal("99999999999");
        
        estado = "";
    }

    public List<Depositos> listarDepositos() {
        this.listaDepositos = depositosFacade.listarDepositosActivos();
        return this.listaDepositos;
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String incluirAnul = "";
        String deta = "";
        Integer deposito = 0;

        Depositos auxDepo = new Depositos();
       

        if (this.depositos == null) {
            deposito = 0;
        } else {
            deposito = Integer.parseInt(this.depositos.getDepositosPK().getCodDepo() + "");
            auxDepo = depositosFacade.getNombreDeposito(deposito);
        }
        
        if (estado.equals("Incluir Anulados")) {
            incluirAnul = "X";
        }
        
        if (conDetalles.equals(true)) {
            deta = "S";
        }else{
            deta = "N";
        }

        rep.reporteLiNotasRemisiones(nrodesde, nrohasta, deposito, auxDepo.getXdesc(), dateToString(desde), dateToString2(desde), dateToString(hasta), dateToString2(hasta), incluirAnul, deta, "admin", tipo);
    }

    private String dateToString(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    private String dateToString2(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    //Getters & Setters
    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public List<Depositos> getListaDepositos() {
        return listaDepositos;
    }

    public void setListaDepositos(List<Depositos> listaDepositos) {
        this.listaDepositos = listaDepositos;
    }

    public BigDecimal getNrodesde() {
        return nrodesde;
    }

    public void setNrodesde(BigDecimal nrodesde) {
        this.nrodesde = nrodesde;
    }

    public BigDecimal getNrohasta() {
        return nrohasta;
    }

    public void setNrohasta(BigDecimal nrohasta) {
        this.nrohasta = nrohasta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getConDetalles() {
        return conDetalles;
    }

    public void setConDetalles(Boolean conDetalles) {
        this.conDetalles = conDetalles;
    }

}
