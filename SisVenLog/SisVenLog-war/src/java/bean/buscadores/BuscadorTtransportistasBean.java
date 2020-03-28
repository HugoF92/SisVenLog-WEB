package bean.buscadores;

import bean.DepositosBean;
import dao.TransportistasFacade;
import entidad.Transportistas;
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
public class BuscadorTtransportistasBean  implements Serializable {

    @EJB
    private TransportistasFacade transportistasFacade;

    private Transportistas transportistas;
    private List<Transportistas> listaTransportistas;

    private String filtro = "";

    @ManagedProperty("#{depositosBean}")
    private DepositosBean depositosBean;

    public BuscadorTtransportistasBean() throws IOException {

    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Transportistas getTransportistas() {
        return transportistas;
    }

    public void setTransportistas(Transportistas transportistas) {
        this.transportistas = transportistas;
    }

    public List<Transportistas> getListaTransportistas() {
        return listaTransportistas;
    }

    public void setListaTransportistas(List<Transportistas> listaTransportistas) {
        this.listaTransportistas = listaTransportistas;
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
        listaTransportistas = new ArrayList<Transportistas>();
        this.transportistas = new Transportistas();
        
        listarTransportistasBuscador();
    }

    public void nuevo() {
        this.transportistas = new Transportistas();
    }

    public void listarTransportistasBuscador() {
        listaTransportistas = transportistasFacade.buscarPorFiltro(filtro);
    }

    public void onRowSelect(SelectEvent event) {

        
        if (this.transportistas != null) {

            if (this.transportistas.getXtransp() != null) {
                depositosBean.setTransportistas(this.transportistas);
//                RequestContext.getCurrentInstance().update("agreDepoPnlTrans");
                PrimeFaces.current().ajax().update("agreDepoPnlTrans");
//                RequestContext.getCurrentInstance().update("editDepoPnlTrans");
                PrimeFaces.current().ajax().update("editDepoPnlTrans");
                PrimeFaces.current().executeScript("PF('dlgBusTrans').hide();");
            }

        }

    }

}
