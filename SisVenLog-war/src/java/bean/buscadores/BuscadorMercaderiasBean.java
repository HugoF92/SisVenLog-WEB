package bean.buscadores;

import bean.DepositosBean;
import bean.EnviosBean;
import bean.RutasBean;
import dao.MercaderiasFacade;
import entidad.Mercaderias;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class BuscadorMercaderiasBean {

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    private Mercaderias mercaderias;
    private List<Mercaderias> listaMercaderias;

    private String filtro = "";

    @ManagedProperty("#{enviosBean}")
    private EnviosBean enviosBean;

    public BuscadorMercaderiasBean() throws IOException {

    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    public List<Mercaderias> getListaMercaderias() {
        return listaMercaderias;
    }

    public void setListaMercaderias(List<Mercaderias> listaMercaderias) {
        this.listaMercaderias = listaMercaderias;
    }

    public EnviosBean getEnviosBean() {
        return enviosBean;
    }

    public void setEnviosBean(EnviosBean enviosBean) {
        this.enviosBean = enviosBean;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaMercaderias = new ArrayList<Mercaderias>();
        this.mercaderias = new Mercaderias();
        
        //listarMercaderiasBuscador();
        
        listarMercaderiasBuscador10();
    }

    public void nuevo() {
        this.mercaderias = new Mercaderias();
    }

    public void listarMercaderiasBuscador() {
        listaMercaderias = mercaderiasFacade.buscarPorFiltro(filtro);
    }

     public void listarMercaderiasBuscador10() {
        listaMercaderias = mercaderiasFacade.findAll();
    }
    public void onRowSelect(SelectEvent event) {

        if (this.mercaderias != null) {
            

            if (this.mercaderias.getXdesc() != null) {
                enviosBean.setMercaderias(this.mercaderias);
                RequestContext.getCurrentInstance().update("agreEnvioPnlArticulo");
                RequestContext.getCurrentInstance().execute("PF('dlgBusMercaderia').hide();");
            }

        }

    }

}
