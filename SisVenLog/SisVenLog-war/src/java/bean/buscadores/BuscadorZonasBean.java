package bean.buscadores;

import bean.DepositosBean;
import bean.RutasBean;
import dao.ZonasFacade;
import entidad.Zonas;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.*;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class BuscadorZonasBean {

    @EJB
    private ZonasFacade zonasFacade;

    private Zonas zonas;
    private List<Zonas> listaZonas;

    private String filtro = "";

    @ManagedProperty("#{depositosBean}")
    private DepositosBean depositosBean;

    @ManagedProperty("#{rutasBean}")
    private RutasBean rutasBean;

    public BuscadorZonasBean() throws IOException {

    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
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

    public DepositosBean getDepositosBean() {
        return depositosBean;
    }

    public void setDepositosBean(DepositosBean depositosBean) {
        this.depositosBean = depositosBean;
    }

    public RutasBean getRutasBean() {
        return rutasBean;
    }

    public void setRutasBean(RutasBean rutasBean) {
        this.rutasBean = rutasBean;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaZonas = new ArrayList<Zonas>();
        this.zonas = new Zonas();
        
        listarZonasBuscador();
    }

    public void nuevo() {
        this.zonas = new Zonas();
    }

    public void listarZonasBuscador() {
        listaZonas = zonasFacade.buscarPorFiltro(filtro);
    }

    public void onRowSelect(SelectEvent event) {

        if (this.zonas != null) {
            
            //VER DESDE QUE VENTANA SE LLAMA

            if (this.zonas.getXdesc() != null) {
                depositosBean.setZonas(this.zonas);
                rutasBean.setZonas(this.zonas);
                PrimeFaces.current().ajax().update("agreDepoPnlZona");
                PrimeFaces.current().ajax().update("editDepoPnlZona");
                PrimeFaces.current().ajax().update("agreRutaPnlZona");
                PrimeFaces.current().ajax().update("editRutaPnlZona");
                PrimeFaces.current().executeScript("PF('dlgBusZona').hide();");
            }

        }

    }

}
