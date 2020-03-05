package bean.buscadores;

import bean.DepositosBean;
import dao.ConductoresFacade;
import entidad.Conductores;
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
public class BuscadorConductoresBean {

    @EJB
    private ConductoresFacade conductoresFacade;

    private Conductores conductores;
    private List<Conductores> listaConductores;

    private String filtro = "";

    @ManagedProperty("#{depositosBean}")
    private DepositosBean depositosBean;

    public BuscadorConductoresBean() throws IOException {

    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Conductores getConductores() {
        return conductores;
    }

    public void setConductores(Conductores conductores) {
        this.conductores = conductores;
    }

    public List<Conductores> getListaConductores() {
        return listaConductores;
    }

    public void setListaConductores(List<Conductores> listaConductores) {
        this.listaConductores = listaConductores;
    }

    public DepositosBean getDepositosBean() {
        return depositosBean;
    }

    public void setDepositosBean(DepositosBean depositosBean) {
        this.depositosBean = depositosBean;
    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaConductores = new ArrayList<Conductores>();
        this.conductores = new Conductores();
        
        listarConductoresBuscador();
    }

    public void nuevo() {
        this.conductores = new Conductores();
    }

    public void listarConductoresBuscador() {
        listaConductores = conductoresFacade.buscarPorFiltro(filtro);
    }

    public void onRowSelect(SelectEvent event) {

        
        if (this.conductores != null) {

            if (this.conductores.getXconductor() != null) {
                depositosBean.setConductores(this.conductores);
//                RequestContext.getCurrentInstance().update("agreDepoPnlCond");
                PrimeFaces.current().ajax().update("agreDepoPnlCond");
//                RequestContext.getCurrentInstance().update("editDepoPnlCond");
                PrimeFaces.current().ajax().update("editDepoPnlCond");
                PrimeFaces.current().executeScript("PF('dlgBusCond').hide();");
            }

        }

    }

}
