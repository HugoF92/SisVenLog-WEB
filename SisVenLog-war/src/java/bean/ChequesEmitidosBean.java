/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.BancosFacade;
import dao.ChequesEmitidosFacade;
import dao.ComprasFacade;
import dao.ProveedoresFacade;
import dao.TiposDocumentosFacade;
import entidad.Bancos;
import entidad.ChequesEmitidos;
import entidad.ChequesEmitidosDet;
import entidad.ChequesEmitidosDetPK;
import entidad.ChequesEmitidosPK;
import entidad.Compras;
import entidad.Proveedores;
import entidad.TiposDocumentos;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ViewScoped
@Named
public class ChequesEmitidosBean implements Serializable {

    @EJB
    private ChequesEmitidosFacade chequesEmitidosFacade;

    @EJB
    private ProveedoresFacade proveedoresFacade;

    @EJB
    private BancosFacade bancosFacade;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private ComprasFacade comprasFacade;

    private LazyDataModel<ChequesEmitidos> lazyDataModel;

    final private short codEmpr = Short.parseShort("2");
    final private List<SelectItem> selectItemsProveedores = new ArrayList<>();
    final private List<SelectItem> selectItemsBancos = new ArrayList<>();
    final private List<SelectItem> selectItemsTiposDocumentos = new ArrayList<>();
    final private List<SelectItem> selectItemsCompras = new ArrayList<>();

    private ChequesEmitidos chequesEmitidos;
    private ChequesEmitidos chequesEmitidosSeleccionado;
    private ChequesEmitidosDet chequesEmitidosDet;

    private String titulo;
    private boolean visualizar;
    private boolean modificar;
    private boolean deshabilitarBotonModificar = true;
    private boolean deshabilitarBotonEliminar = true;

    @PostConstruct
    public void init() {
        prepararDialog("ag");

        selectItemsProveedores.add(new SelectItem(""));
        selectItemsBancos.add(new SelectItem(""));
        selectItemsTiposDocumentos.add(new SelectItem(""));
        selectItemsCompras.add(new SelectItem(""));

        listarChequesEmitidos();
        listarProveedores();
        listarBancos();
        listarTiposDocumentos();
    }

    private void listarChequesEmitidos() {
        lazyDataModel = new LazyDataModel<ChequesEmitidos>() {
            @Override
            public List<ChequesEmitidos> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                String xdesc = filters.get("bancos.xdesc") != null ? filters.get("bancos.xdesc").toString() : null;
                String xnombre = filters.get("codProveed.xnombre") != null ? filters.get("codProveed.xnombre").toString() : null;
                long icheque = filters.get("icheque") != null ? (Long) filters.get("icheque") : -1;
                Date fcheque = filters.get("fcheque") != null ? (Date) filters.get("fcheque") : null;
                this.setRowCount(chequesEmitidosFacade.countChequesEmitidos(
                        Short.parseShort("2"),
                        xdesc,
                        xnombre,
                        icheque,
                        fcheque
                ).intValue());
                return chequesEmitidosFacade.listadoChequesEmitidos(
                        Short.parseShort("2"),
                        xdesc,
                        xnombre,
                        icheque,
                        fcheque,
                        sortField,
                        sortOrder.name(),
                        new int[]{first, pageSize}
                );
            }

            @Override
            public ChequesEmitidos getRowData(String rowKey) {
                String tempIndex = rowKey;
                if (tempIndex != null) {
                    for (ChequesEmitidos ce : lazyDataModel.getWrappedData()) {
                        if (String.valueOf(ce.getChequesEmitidosPK().getCodEmpr()).concat(" ")
                                .concat(String.valueOf(ce.getChequesEmitidosPK().getCodBanco())).concat(" ")
                                .concat(ce.getChequesEmitidosPK().getNroCheque()).equals(rowKey)) {
                            return ce;
                        }
                    }
                }

                return null;
            }

            @Override
            public Object getRowKey(ChequesEmitidos ch) {
                ChequesEmitidosPK pk = ch != null ? ch.getChequesEmitidosPK() : null;
                return pk != null ? pk.getCodEmpr() + " " + pk.getCodBanco() + " " + pk.getNroCheque() : null;
            }
        };
    }

    public void onRowSelect(SelectEvent event) {
        deshabilitarBotonModificar = false;
        deshabilitarBotonEliminar = false;
    }

    public void prepararDialog(String operacion) {
        if (operacion.equalsIgnoreCase("ag")) {
            titulo = "Nuevo cheque emitido";
            modificar = false;
            visualizar = false;
            chequesEmitidos = new ChequesEmitidos();
            chequesEmitidos.setChequesEmitidosPK(new ChequesEmitidosPK());
            chequesEmitidos.getChequesEmitidosPK().setCodEmpr(codEmpr);
            chequesEmitidosDet = new ChequesEmitidosDet();
            chequesEmitidosDet.setChequesEmitidosDetPK(new ChequesEmitidosDetPK());
            chequesEmitidos.setFcheque(new Date());
            chequesEmitidos.setFemision(new Date());
        } else if (operacion.equalsIgnoreCase("mo")) {
            titulo = "Modificar cheque emitido";
            modificar = true;
            visualizar = false;
            chequesEmitidos = chequesEmitidosSeleccionado;
        }
    }

    private void listarProveedores() {
        List<Proveedores> proveedores = proveedoresFacade.listarProveedoresActivos();
        for (Proveedores p : proveedores) {
            selectItemsProveedores.add(new SelectItem(p, p.getXnombre()));
        }
    }

    private void listarBancos() {
        List<Bancos> bs = bancosFacade.listarBancos();
        for (Bancos b : bs) {
            selectItemsBancos.add(new SelectItem(b.getCodBanco(), b.getXdesc()));
        }
    }

    private void listarTiposDocumentos() {
        List<TiposDocumentos> tiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoCOCCVC();
        for (TiposDocumentos td : tiposDocumentos) {
            selectItemsTiposDocumentos.add(new SelectItem(td.getCtipoDocum(), td.getXdesc()));
        }
    }

    public void listarCompras() {
        if (chequesEmitidosDet.getChequesEmitidosDetPK().getCtipoDocum() != null && chequesEmitidos.getCodProveed().getCodProveed() != null) {
            List<Compras> comprasList;
            if (chequesEmitidosDet.getFfactur() == null) {
                comprasList = comprasFacade.comprasPorCodEmprMaxFfacturCtipoDocumCodProveed(codEmpr, chequesEmitidosDet.getChequesEmitidosDetPK().getCtipoDocum(), chequesEmitidos.getCodProveed().getCodProveed());
            } else {
                comprasList = comprasFacade.comprasPorCodEmprFfacturCtipoDocumCodProveed(codEmpr, chequesEmitidosDet.getFfactur(), chequesEmitidosDet.getChequesEmitidosDetPK().getCtipoDocum(), chequesEmitidos.getCodProveed().getCodProveed());
            }
            DateFormat dateFormat = new SimpleDateFormat("[yyyy-mm-dd]");
            for (Compras c : comprasList) {
                selectItemsTiposDocumentos.add(new SelectItem(c, dateFormat.format(c.getComprasPK().getFfactur()) + " - " + c.getComprasPK().getNrofact()));
            }
        }
    }

    public void agregarChequeEmitido() {
        if (modificar) {
            chequesEmitidosFacade.edit(chequesEmitidos);
        } else if (chequesEmitidosFacade.countChequesEmitidosPorNroChequeCodBanco(
                chequesEmitidos.getChequesEmitidosPK().getNroCheque(),
                chequesEmitidos.getChequesEmitidosPK().getCodBanco()) == 0) {
            chequesEmitidosFacade.create(chequesEmitidos);
        }
    }

    public void eliminarChequeEmitido() {
        chequesEmitidosFacade.remove(chequesEmitidosSeleccionado);
    }

    public LazyDataModel<ChequesEmitidos> getLazyDataModel() {
        return lazyDataModel;
    }

    public List<SelectItem> getSelectItemsProveedores() {
        return selectItemsProveedores;
    }

    public List<SelectItem> getSelectItemsBancos() {
        return selectItemsBancos;
    }

    public List<SelectItem> getSelectItemsTiposDocumentos() {
        return selectItemsTiposDocumentos;
    }

    public List<SelectItem> getSelectItemsCompras() {
        return selectItemsCompras;
    }

    public ChequesEmitidos getChequesEmitidos() {
        return chequesEmitidos;
    }

    public ChequesEmitidos getChequesEmitidosSeleccionado() {
        return chequesEmitidosSeleccionado;
    }

    public void setChequesEmitidosSeleccionado(ChequesEmitidos chequesEmitidosSeleccionado) {
        this.chequesEmitidosSeleccionado = chequesEmitidosSeleccionado;
    }

    public ChequesEmitidosDet getChequesEmitidosDet() {
        return chequesEmitidosDet;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isVisualizar() {
        return visualizar;
    }

    public boolean isModificar() {
        return modificar;
    }

    public boolean isDeshabilitarBotonModificar() {
        return deshabilitarBotonModificar;
    }

    public boolean isDeshabilitarBotonEliminar() {
        return deshabilitarBotonEliminar;
    }

}
