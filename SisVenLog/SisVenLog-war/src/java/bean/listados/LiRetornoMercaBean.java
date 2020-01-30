package bean.listados;

import dao.DepositosFacade;
import dao.SublineasFacade;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Sublineas;
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
public class LiRetornoMercaBean {

    @EJB
    private SublineasFacade sublineasFacade;

    private Sublineas sublineas;
    private List<Sublineas> listaSublineas;
    
    private Date desde;
    private Date hasta;

    private String operacion;

    public LiRetornoMercaBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.sublineas = new Sublineas();
        this.listaSublineas = new ArrayList<Sublineas>();

        this.desde = new Date();
        this.hasta = new Date();
    }

    public List<Sublineas> listarSublineas() {
        this.listaSublineas = sublineasFacade.findAll();
        return this.listaSublineas;
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        Integer sublinea = 0;
        String descSublinea = "";
        
        if (this.sublineas != null) {
            sublinea = Integer.parseInt(this.sublineas.getCodSublinea()+"");
            descSublinea = sublineasFacade.find(sublineas.getCodSublinea()).getXdesc();
        }else{
            sublinea = 0;
            descSublinea = "TODOS";
        }


       rep.reporteLiRetornoPrecios(sublinea, descSublinea,dateToString(desde), dateToString2(desde), dateToString(hasta), dateToString2(hasta),  "admin", tipo);
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

    public Sublineas getSublineas() {
        return sublineas;
    }

    public void setSublineas(Sublineas sublineas) {
        this.sublineas = sublineas;
    }

    public List<Sublineas> getListaSublineas() {
        return listaSublineas;
    }

    public void setListaSublineas(List<Sublineas> listaSublineas) {
        this.listaSublineas = listaSublineas;
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
