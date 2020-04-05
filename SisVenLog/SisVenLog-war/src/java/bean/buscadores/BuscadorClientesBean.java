package bean.buscadores;

import bean.listados.LiExtractoClientesBean;
import bean.listados.LiFacServBean;
import dao.ClientesFacade;
import entidad.Clientes;
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
public class BuscadorClientesBean implements Serializable {

    @EJB
    private ClientesFacade clientesFacade;

    private Clientes clientes;
    private List<Clientes> listaClientes;

    private String filtro = "";

    @ManagedProperty("#{liExtractoClientesBean}")
    private LiExtractoClientesBean liExtractoClientesBean;

    @ManagedProperty("#{liFacServBean}")
    private LiFacServBean liFacServBean;
 
    public BuscadorClientesBean() throws IOException {

    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public LiExtractoClientesBean getLiExtractoClientesBean() {
        return liExtractoClientesBean;
    }

    public void setLiExtractoClientesBean(LiExtractoClientesBean liExtractoClientesBean) {
        this.liExtractoClientesBean = liExtractoClientesBean;
    }

    public LiFacServBean getLiFacServBean() {
        return liFacServBean;
    }

    public void setLiFacServBean(LiFacServBean liFacServBean) {
        this.liFacServBean = liFacServBean;
    }
 

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        listaClientes = new ArrayList<Clientes>();
        this.clientes = new Clientes();
        
        listarClientesBuscador();
    }

    public void nuevo() {
        this.clientes = new Clientes();
    }

    public void listarClientesBuscador() {
        listaClientes = clientesFacade.buscarPorFiltro(filtro);
    }

    public void onRowSelect(SelectEvent event) {
        
        if (this.clientes != null) {

            if (this.clientes.getXnombre() != null) {
                liExtractoClientesBean.setClientes(this.clientes);
                liExtractoClientesBean.todosLosClientes();
                liFacServBean.setClientes(this.clientes);
                liFacServBean.todosLosClientes();
                //RequestContext.getCurrentInstance().update("liExtractoCliPnlClie");
                //RequestContext.getCurrentInstance().update("lifactPnlClie");
                //RequestContext.getCurrentInstance().update("lifactClientesSel");
                PrimeFaces.current().executeScript("PF('dlgBusClie').hide();");
            }

        }

    }

}
