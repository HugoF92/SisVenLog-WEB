package bean;

import dao.DepositosFacade;
import dao.ExistenciasFacade;
import dao.MercaderiasFacade;
import entidad.Depositos;
import entidad.Existencias;
import entidad.Mercaderias;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named(value = "asociarMercaderiaBean")
@ViewScoped
public class AsociarMercaderiaBean implements Serializable {

    @EJB
    private DepositosFacade depositoFacade;
    @EJB
    private MercaderiasFacade mercadoFacade;
    @EJB
    private ExistenciasFacade existenciaFacade;

    private List<Depositos> listaDepositos;
    private Depositos depositoSeleccionado;

    private DualListModel<Mercaderias> dualMercaderia;

    private List<Mercaderias> mercaderiaSource;
    private List<Mercaderias> mercaderiaTarget;
    private List<Mercaderias> listaMercaderiaMovAdd, listaMercaderiaMovRemove;

    @PostConstruct
    public void init() {
        listaDepositos = depositoFacade.listarDepositosActivos();
//        mercaderiaSource = new ArrayList<Mercaderias>();
//        mercaderiaTarget = new ArrayList<Mercaderias>();
//        listaMercaderiaMovAdd = new ArrayList<Mercaderias>();
        listaMercaderiaMovRemove = new ArrayList<Mercaderias>();
        dualMercaderia = new DualListModel<>(new ArrayList<Mercaderias>(), new ArrayList<Mercaderias>());
    }

    public DualListModel<Mercaderias> getDualMercaderia() {
        return dualMercaderia;
    }

    public void setDualMercaderia(DualListModel<Mercaderias> dualMercaderia) {
        this.dualMercaderia = dualMercaderia;
    }

    public List<Mercaderias> getMercaderiaSource() {
        return mercaderiaSource;
    }

    public void setMercaderiaSource(List<Mercaderias> mercaderiaSource) {
        this.mercaderiaSource = mercaderiaSource;
    }

    public List<Mercaderias> getMercaderiaTarget() {
        return mercaderiaTarget;
    }

    public void setMercaderiaTarget(List<Mercaderias> mercaderiaTarget) {
        this.mercaderiaTarget = mercaderiaTarget;
    }

    public List<Depositos> getListaDepositos() {
        return listaDepositos;
    }

    public void setListaDepositos(List<Depositos> listaDepositos) {
        this.listaDepositos = listaDepositos;
    }

    public Depositos getDepositoSeleccionado() {
        return depositoSeleccionado;
    }

    public void setDepositoSeleccionado(Depositos depositoSeleccionado) {
        this.depositoSeleccionado = depositoSeleccionado;
    }

    public void onChangeDepositoSelected() {
        if (depositoSeleccionado != null) {
            dualMercaderia.setSource(this.mercadoFacade.listarMercaderiasActivasNoEnDeposito(this.depositoSeleccionado.getDepositosPK().getCodDepo()));
            dualMercaderia.setTarget(existenciaFacade.listarMercaderiasByExistencias(new Short("2"), depositoSeleccionado.getDepositosPK().getCodDepo()));
//            dualMercaderia.setSource(mercaderiaSource);
//            mercaderiaTarget);
//            dualMercaderia = new DualListModel<>(mercaderiaSource, mercaderiaTarget);
        }
    }

    public void onTransfer(TransferEvent event) {
        if(event.isRemove()){
            for (Object item : event.getItems()) {
                List<Existencias> e = existenciaFacade.findExistenciasByMerc(depositoSeleccionado.getDepositosPK().getCodDepo(),((Mercaderias) item).getMercaderiasPK().getCodMerca());
                Boolean flag = false;
                if(!e.isEmpty()){
                    for(Existencias ex : e){
                        if(ex.getCantCajas()>0 || ex.getCantUnid()>0 || ex.getReserCajas()>0 || ex.getReserUnid()>0){
                            this.getDualMercaderia().getTarget().add((Mercaderias)item);
                            this.getDualMercaderia().getSource().remove((Mercaderias)item);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "No se puede desasociar la mercaderia "+((Mercaderias) item).getXdesc()+" ya que posee stock en el deposito."));
                            flag = true;
                        }
                    }
                }
                if (!flag && !listaMercaderiaMovRemove.contains(((Mercaderias) item))) {
                    listaMercaderiaMovRemove.add(((Mercaderias) item));
                }
            }
        }
        PrimeFaces.current().executeScript("PF('blockUI').hide();");
    }

    public void guardar() {
        if (depositoSeleccionado == null) return;
        List<Existencias> exiTarget = existenciaFacade.listarExistencias(new Short("2"), depositoSeleccionado.getDepositosPK().getCodDepo());
        for (Mercaderias m : this.dualMercaderia.getTarget()){
          boolean existe = false;
          for (Existencias exi : exiTarget) {//Lista de existencias que hay en la base de datos.
              if (exi.getExistenciasPK().getCodMerca().equals(m.getMercaderiasPK().getCodMerca())) {//Verificar mercaderiasiguales
                  existe = true;
              }
          }
          if (!existe) {
              Existencias e = new Existencias(new Short("2"), depositoSeleccionado.getDepositosPK().getCodDepo(), m.getMercaderiasPK().getCodMerca());
              e.setCantCajas(0);
              e.setCantUnid(0);
              e.setReserCajas(0);
              e.setReserUnid(0);
              existenciaFacade.create(e);
          }
        }
        for (Mercaderias m : listaMercaderiaMovRemove) {//Lista de eliminados
            for (Existencias exi : exiTarget) {//Lista de existencias que hay en la base de datos.
                if (exi.getExistenciasPK().getCodMerca().equals(m.getMercaderiasPK().getCodMerca())) {//Verificar mercaderiasiguales
                    existenciaFacade.remove(exi);
                }
            }
        }
        listaMercaderiaMovRemove = new ArrayList<Mercaderias>();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Asociado con éxito."));
    }
}