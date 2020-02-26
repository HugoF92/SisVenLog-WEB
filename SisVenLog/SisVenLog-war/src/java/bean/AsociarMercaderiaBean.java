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
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
        mercaderiaSource = new ArrayList<Mercaderias>();
        mercaderiaTarget = new ArrayList<Mercaderias>();
        listaMercaderiaMovAdd = new ArrayList<Mercaderias>();
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
            System.out.println("ACA");
            this.mercaderiaSource = this.mercadoFacade.listarMercaderiasActivasNoEnDeposito(this.depositoSeleccionado.getDepositosPK().getCodDepo());
//            mercaderiaSource = mercadoFacade.listarMercaderiasActivas();
//            List<Existencias> exiTarget = existenciaFacade.listarExistencias(new Short("2"), depositoSeleccionado.getDepositosPK().getCodDepo());
//            for (int i = 0; i < exiTarget.size(); i++) {
//                for (int j = 0; j < mercaderiaSource.size(); j++) {
//                    if (Objects.equals(exiTarget.get(i).getExistenciasPK().getCodMerca(), mercaderiaSource.get(j).getMercaderiasPK().getCodMerca())) {
//                        mercaderiaSource.remove(mercaderiaSource.get(j));
//                    }
//                }
//            }
            mercaderiaTarget = existenciaFacade.listarMercaderiasByExistencias(new Short("2"), depositoSeleccionado.getDepositosPK().getCodDepo());
            dualMercaderia = new DualListModel<>(mercaderiaSource, mercaderiaTarget);
        }
    }

    public void onTransfer(TransferEvent event) {
        for (Object item : event.getItems()) {
            if (event.isAdd()) {
                if (!listaMercaderiaMovAdd.contains(((Mercaderias) item))) {
                    listaMercaderiaMovAdd.add(((Mercaderias) item));
                }
            } else if (event.isRemove()) {
                if (!listaMercaderiaMovRemove.contains(((Mercaderias) item))) {
                    listaMercaderiaMovRemove.add(((Mercaderias) item));
                }
            }
        }
    }

    public void guardar() {
        if (depositoSeleccionado == null) {
            return;
        }

        List<Mercaderias> lista = existenciaFacade.listarMercaderiasByExistencias(new Short("2"), depositoSeleccionado.getDepositosPK().getCodDepo());
        List<Existencias> exiTarget = existenciaFacade.listarExistencias(new Short("2"), depositoSeleccionado.getDepositosPK().getCodDepo());

        for (Mercaderias m : listaMercaderiaMovAdd) {//Lista de agregados
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
                    if (exi.getCantUnid() == 0 && exi.getCantCajas() == 0) {//
                        existenciaFacade.remove(exi);
                    }
                }
            }

        }
        mercaderiaSource = new ArrayList<Mercaderias>();
        mercaderiaTarget = new ArrayList<Mercaderias>();
        listaMercaderiaMovAdd = new ArrayList<Mercaderias>();
        listaMercaderiaMovRemove = new ArrayList<Mercaderias>();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Asociado con éxito."));

    }
}
