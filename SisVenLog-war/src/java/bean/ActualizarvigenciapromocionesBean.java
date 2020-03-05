package bean;

import dao.PromocionesFacade;
import entidad.Promociones;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import util.ExceptionHandlerView;

@Named(value = "actualizarvigenciapromocionesBean")
@ViewScoped
public class ActualizarvigenciapromocionesBean implements Serializable {

    @Inject
    private PromocionesFacade promocionFacade;

    private Promociones promocion;
    private Date l_ffinal, l_fnueva;
    private String lpromos, nro_promo, x_nro_promo,filtro;
    private List<Promociones> listaPromociones;
    

    @PostConstruct
    public void init() {
        promocion = new Promociones();
        this.lpromos = "";
        this.nro_promo="";
        filtro="";
        listaPromociones = promocionFacade.findAllOrderXDesc();
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public ActualizarvigenciapromocionesBean() {
    }

    public List<Promociones> listar() {
        listaPromociones = promocionFacade.findAllOrderXDesc();
        return listaPromociones;
    }

    public List<Promociones> getListaPromociones() {
        return listaPromociones;
    }

    public void setListaPromociones(List<Promociones> listaPromociones) {
        this.listaPromociones = listaPromociones;
    }

    public PromocionesFacade getPromocionFacade() {
        return promocionFacade;
    }

    public void setPromocionFacade(PromocionesFacade promocionFacade) {
        this.promocionFacade = promocionFacade;
    }

    public String getLpromos() {
        return lpromos;
    }

    public void setLpromos(String lpromos) {
        this.lpromos = lpromos;
    }

    public String getNro_promo() {
        return nro_promo;
    }

    public void setNro_promo(String nro_promo) {
        this.nro_promo = nro_promo;
    }

    public String getX_nro_promo() {
        return x_nro_promo;
    }

    public void setX_nro_promo(String x_nro_promo) {
        this.x_nro_promo = x_nro_promo;
    }

    public Date getL_ffinal() {
        return l_ffinal;
    }

    public void setL_ffinal(Date l_ffinal) {
        this.l_ffinal = l_ffinal;
    }

    public Date getL_fnueva() {
        return l_fnueva;
    }

    public void setL_fnueva(Date l_fnueva) {
        this.l_fnueva = l_fnueva;
    }

    public Promociones getPromocion() {
        return promocion;
    }

    public void setPromocion(Promociones promocion) {
        this.promocion = promocion;
    }

    public void actualizarPromocion() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String l_sente = " UPDATE promociones SET frige_hasta = '" + formato.format(this.l_fnueva) + "' WHERE ";
        l_sente = l_sente + "   nro_promo IN ( " + this.lpromos + ")";
        System.out.println("l_sente : " + l_sente);

        Integer respuesta = promocionFacade.actualizarVigenciaPromocion(l_sente);

        if (respuesta != -1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso ", "Fin de Actualización"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso ", "Error de Actualización"));
        }
    }

    public void onChangePromocion() {
        this.nro_promo = String.valueOf(promocion.getPromocionesPK().getNroPromo());
        visualizarPromo();
    }

    public void visualizarPromo() {
        if (this.nro_promo.equals("")) {
            this.nro_promo = "";
            this.x_nro_promo = "";
        } else {

            List<Promociones> lista = promocionFacade.findByNroPromo(this.nro_promo);
            System.out.println(lista);
            if (!lista.isEmpty()) {
                promocion = lista.get(0);
                this.x_nro_promo = promocion.getXdesc();
                if (this.lpromos.length() > 0) {
                    this.lpromos += "," + this.nro_promo;
                } else {
                    this.lpromos = this.nro_promo;
                }
            } else {
                this.nro_promo = "";
                this.x_nro_promo = "";
                promocion = new Promociones();
            }
        }
    }
    
     public void listarPromocionBuscador(){
        try{
            listaPromociones = promocionFacade.buscarPorFiltro(filtro);
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        
    }

}
