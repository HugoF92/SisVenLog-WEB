package bean.listados;

import dao.CanalesCompraFacade;
import dao.DepositosFacade;
import dao.ProveedoresFacade;
import entidad.CanalesCompra;
import entidad.CanalesCompraPK;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Proveedores;
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
public class LiExtractoProvBean {

    @EJB
    private ProveedoresFacade proveedoresFacade;

    @EJB
    private CanalesCompraFacade canalesCompraFacade;

    private Proveedores proveedores;
    private List<Proveedores> listaProveedores;

    private CanalesCompra canalesCompra;
    private List<CanalesCompra> listaCanalesCompra;

    private Date desde;
    private Date hasta;

    private String operacion;

    public LiExtractoProvBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.proveedores = new Proveedores();
        this.listaProveedores = new ArrayList<Proveedores>();

        this.canalesCompra = new CanalesCompra(new CanalesCompraPK());
        this.listaCanalesCompra = new ArrayList<CanalesCompra>();

        this.desde = new Date();
        this.hasta = new Date();
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        Integer prov = 0;
        String descprov = "";
        String canal = "";
        String desccanal = "";

        if (this.proveedores != null) {
            prov = Integer.parseInt(this.proveedores.getCodProveed() + "");
            descprov = proveedoresFacade.find(this.proveedores.getCodProveed()).getXnombre();
        } else {
            prov = 0;
            descprov = "TODOS";
        }

        if (this.canalesCompra != null) {
            canal = this.canalesCompra.getCanalesCompraPK().getCcanalCompra();
            desccanal = canalesCompraFacade.find(canalesCompra.getCanalesCompraPK()).getXdesc();
        }else{
            canal = "TODOS";
            desccanal = "TODOS";
        }

        rep.reporteLiExtractoProveedor(prov, descprov, canal, desccanal, dateToString(desde), dateToString2(desde), dateToString(hasta), dateToString2(hasta), "admin", tipo);
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
    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores sublineas) {
        this.proveedores = sublineas;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
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

    public CanalesCompra getCanalesCompra() {
        return canalesCompra;
    }

    public void setCanalesCompra(CanalesCompra canalesCompra) {
        this.canalesCompra = canalesCompra;
    }

    public List<CanalesCompra> getListaCanalesCompra() {
        return listaCanalesCompra;
    }

    public void setListaCanalesCompra(List<CanalesCompra> listaCanalesCompra) {
        this.listaCanalesCompra = listaCanalesCompra;
    }

}
