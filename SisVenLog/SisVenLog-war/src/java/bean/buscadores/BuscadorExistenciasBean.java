/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.buscadores;

import bean.EnviosBean;
import dao.ExistenciasFacade;
import entidad.CanalesVenta;
import entidad.Depositos;
import entidad.Existencias;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author sata
 */
@ManagedBean
@SessionScoped
public class BuscadorExistenciasBean extends LazyDataModel<Existencias> implements Serializable {

    @EJB
    private ExistenciasFacade existenciasFacade;

    private Existencias existencias;
    private List<Existencias> listaExistencias;

    private String filtro = "";

    private LazyDataModel<Existencias> model;

    @ManagedProperty("#{enviosBean}")
    private EnviosBean enviosBean;

    public BuscadorExistenciasBean() throws IOException {

    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Existencias getExistencias() {
        return existencias;
    }

    public void setExistencias(Existencias existencias) {
        this.existencias = existencias;
    }

    public List<Existencias> getListaExistencias() {
        return listaExistencias;
    }

    public void setListaExistencias(List<Existencias> listaExistencias) {
        this.listaExistencias = listaExistencias;
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
        listaExistencias = new ArrayList<Existencias>();
        this.existencias = new Existencias();

    }

    public void nuevo() {
        this.existencias = new Existencias();
    }

    public void listarExistenciasBuscador() {
        
    }

    public void listarExistenciasBuscadorPorDeposito() {
        listaExistencias = existenciasFacade.buscarPorCodigoDepositoOrigenTodasMercaSingle(enviosBean.getOrigen().getDepositosPK().getCodDepo());
    }

    public LazyDataModel<Existencias> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<Existencias> model) {
        this.model = model;
    }

    public void onRowSelect(SelectEvent event) {

        if (this.existencias != null) {

            if (this.existencias.getExistenciasPK().getCodMerca() != null) {
                enviosBean.setExistencias(this.existencias);
                enviosBean.setArticulo(this.existencias.getExistenciasPK().getCodMerca());

//                RequestContext.getCurrentInstance().update("descripcionesMerca");
                PrimeFaces.current().ajax().update("descripcionesMerca");
                PrimeFaces.current().executeScript("PF('dlgBusMercaderia').hide();");
            }

        }

    }

    public void cargarExistencias() {
        listaExistencias = new ArrayList<Existencias>();
        this.existencias = new Existencias();
        Depositos depos = enviosBean.getOrigen();
        CanalesVenta canalVen = enviosBean.getCanalesVenta();
        this.filtro="";

        if (depos == null) {
            
            PrimeFaces.current().executeScript("PF('dlgBusMercaderia').hide();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Se requiere deposito origen para continuar."));
            
            return;

        }
        
        if (canalVen == null) {
            
            PrimeFaces.current().executeScript("PF('dlgBusMercaderia').hide();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Se requiere canal de venta para continuar."));
            
            return;

        }

        model = new LazyDataModel<Existencias>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Existencias> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                //List<Envios> envioss;
                int count;
                
                if("".equals(filtro)){
                
                    listaExistencias = existenciasFacade.buscarPorCodigoDepositoOrigenTodasMerca(depos.getDepositosPK().getCodDepo(), new int[]{first, pageSize}, canalVen.getCodCanal());
                    count = existenciasFacade.CountBuscarPorCodigoDepositoOrigenTodasMerca(depos.getDepositosPK().getCodDepo(), canalVen.getCodCanal());
                    model.setRowCount(count);
                }else{
                    listaExistencias = existenciasFacade.buscarListaPorCodigoDepositoOrigenMercaDescripcion(filtro,enviosBean.getOrigen().getDepositosPK().getCodDepo(),new int[]{first, pageSize}, canalVen.getCodCanal() );
                    count = existenciasFacade.CountBuscarPorCodigoDepositoOrigenTodasMercaConFiltro(filtro,depos.getDepositosPK().getCodDepo(),canalVen.getCodCanal());
                    model.setRowCount(count);
                }
                return listaExistencias;
            }

            @Override
            public Existencias getRowData(String rowKey) {
                String tempIndex = rowKey;
                System.out.println("1");
                if (tempIndex != null) {
                    for (Existencias inc : listaExistencias) {
                        if (inc.getExistenciasPK().getCodMerca().equals(rowKey)) {
                            existencias = inc;
                            return inc;
                        }
                    }
                }

                return null;
            }

            @Override
            public Object getRowKey(Existencias existenciass) {
                return existenciass.getExistenciasPK().getCodMerca();
            }
        };

        
    }

}
