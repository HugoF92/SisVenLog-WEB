package bean.buscadores;

import bean.listados.LiMovMercaBean;
import dao.MercaderiasFacade;
import entidad.Mercaderias;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.*;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class BuscadorMercaderiaBean implements Serializable {

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    private Mercaderias mercaderias;
    private List<Mercaderias> listaMercaderias;

    private String filtro = "";

    @ManagedProperty("#{liMovMercaBean}")
    private LiMovMercaBean liMovMercaBean;

    public BuscadorMercaderiaBean() throws IOException {

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

    public LiMovMercaBean getLiMovMercaBean() {
        return liMovMercaBean;
    }

    public void setLiMovMercaBean(LiMovMercaBean liMovMercaBean) {
        this.liMovMercaBean = liMovMercaBean;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaMercaderias = new ArrayList<Mercaderias>();
        this.mercaderias = new Mercaderias();
        
        listarMercaderiasBuscador();
    }

    public void nuevo() {
        this.mercaderias = new Mercaderias();
    }

    public void listarMercaderiasBuscador() {
        listaMercaderias = mercaderiasFacade.buscarPorFiltro(this.getFiltro());
    }

    public void onRowSelect(SelectEvent event) {

        
        if (this.mercaderias != null) {

            if (this.mercaderias.getXdesc() != null) {
                liMovMercaBean.setMercaderias(mercaderias);
//                RequestContext.getCurrentInstance().update("agreDepoPnlCond");
                PrimeFaces.current().ajax().update("agreDepoPnlCond");
                PrimeFaces.current().executeScript("PF('dlgBusMerca').hide();");
            }

        }

    }

}
