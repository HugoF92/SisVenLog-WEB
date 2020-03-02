package bean.listados;

import dao.DepositosFacade;
import dao.TiposDocumentosFacade;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.TiposDocumentos;
import java.io.IOException;
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
public class LiDocuResBean {

    @EJB
    private DepositosFacade depositosFacade;

    private Depositos depositos;
    private List<Depositos> listaDepositos;

    private Date desde;
    private Date hasta;

    private String operacion;

    public LiDocuResBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.depositos = new Depositos(new DepositosPK());
        this.listaDepositos = new ArrayList<Depositos>();

        this.desde = new Date();
        this.hasta = new Date();
    }

    public List<Depositos> listarDepositos() {
        this.listaDepositos = depositosFacade.listarDepositosActivos();
        return this.listaDepositos;
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        Integer deposito = 0;
        
        if (this.depositos == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe seleccionar un deposito"));
            return;
        } else {
            deposito = Integer.parseInt(this.depositos.getDepositosPK().getCodDepo()+"");
        }


       rep.reporteLiDocuRes(deposito, depositosFacade.find(depositos.getDepositosPK()).getXdesc(),dateToString(desde), dateToString2(desde), dateToString(hasta), dateToString2(hasta),  "admin", tipo);
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

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
    

}
