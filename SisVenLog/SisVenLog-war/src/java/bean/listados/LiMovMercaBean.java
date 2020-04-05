package bean.listados;

import dao.ExcelFacade;
import dao.MercaderiasFacade;
import dao.PersonalizedFacade;
import dto.LiMercaSinDto;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.IOException;
import java.io.Serializable;
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
public class LiMovMercaBean {


    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private PersonalizedFacade personalizedFacade;

    @EJB
    private ExcelFacade excelFacade;
    
    private Mercaderias mercaderias;
    
    private Zonas zonas;
    private List<Zonas> listaZonas;

    private Date desde;
    private Date hasta;


    public LiMovMercaBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {


        this.zonas = new Zonas(new ZonasPK());
        this.mercaderias = new Mercaderias(new MercaderiasPK());
        this.listaZonas = new ArrayList<Zonas>();

        this.desde = new Date();
        this.hasta = new Date();


    }

    public void ejecutar(String tipo) {


        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        //String tipoDoc = "";
        String zona = "";

        StringBuilder sql = new StringBuilder();

    
        if (this.zonas == null) {
            zona = "TODOS";
        } else {
            zona = this.zonas.getZonasPK().getCodZona();
        }

        

        System.out.println("SQL limovimerca: " + sql.toString());

        if (tipo.equals("VIST")) {
            //rep.reporteLiMercaSin(sql.toString(), dateToString2(desde), dateToString2(hasta), tipoDocumento, "admin", zona, tipo);
        } else if (tipo.equals("ARCH")) {
            List<LiMercaSinDto> auxExcel = new ArrayList<LiMercaSinDto>();

            auxExcel = excelFacade.listarLiMercaSin(sql.toString());

            rep.excelLiMercaSin(auxExcel);
        }

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

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

}
