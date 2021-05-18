/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.BancosFacade;
import dao.ChequesEmitidosDetFacade;
import dao.ChequesEmitidosFacade;
import dao.ComprasFacade;
import dao.CuentasProveedoresFacade;
import dao.ProveedoresFacade;
import dao.TiposDocumentosFacade;
import entidad.Bancos;
import entidad.ChequesEmitidos;
import entidad.ChequesEmitidosDet;
import entidad.ChequesEmitidosDetPK;
import entidad.ChequesEmitidosPK;
import entidad.Compras;
import entidad.CuentasProveedores;
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
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ViewScoped
@Named
public class ChequesEmitidosBean implements Serializable {

    @Resource
    private TransactionSynchronizationRegistry reg;

    @EJB
    private ChequesEmitidosFacade chequesEmitidosFacade;

    @EJB
    private ChequesEmitidosDetFacade chequesEmitidosDetFacade;

    @EJB
    private ProveedoresFacade proveedoresFacade;

    @EJB
    private BancosFacade bancosFacade;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private ComprasFacade comprasFacade;

    @EJB
    private CuentasProveedoresFacade cuentasProveedoresFacade;

    private LazyDataModel<ChequesEmitidos> lazyDataModel;

    final private short codEmpr = Short.parseShort("2");
    final private List<SelectItem> selectItemsProveedores = new ArrayList<>();
    final private List<SelectItem> selectItemsBancos = new ArrayList<>();
    final private List<SelectItem> selectItemsTiposDocumentos = new ArrayList<>();
    final private List<SelectItem> selectItemsCompras = new ArrayList<>();

    private ChequesEmitidos chequesEmitidos, chequesEmitidosSeleccionado;
    private ChequesEmitidosDet chequesEmitidosDet, chequesEmitidosDetSeleccionado;
    private List<ChequesEmitidosDet> chequesEmitidosDets;
    private Compras compras;

    private String titulo;
    private boolean visualizar, modificar, deshabilitarBotonModificar = true, deshabilitarBotonEliminar = true;
    private Long icheque, isaldo, ipagado;

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
            chequesEmitidos.setFcheque(new Date());
            chequesEmitidos.setFemision(new Date());
            chequesEmitidosDets = new ArrayList<>();
            prepararDetalle();
        } else if (operacion.equalsIgnoreCase("mo")) {
            titulo = "Modificar cheque emitido";
            modificar = true;
            visualizar = false;
            chequesEmitidos = chequesEmitidosSeleccionado;
            icheque = chequesEmitidos.getIcheque();
            chequesEmitidosDets = chequesEmitidosDetFacade.chequesEmitidosDetPorChequesEmitidos(chequesEmitidos);
        }
    }

    private void prepararDetalle() {
        chequesEmitidosDet = new ChequesEmitidosDet();
        chequesEmitidosDet.setChequesEmitidosDetPK(new ChequesEmitidosDetPK());
        compras = new Compras();
        isaldo = null;
        ipagado = null;
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
            List<Compras> comprasList = comprasFacade.comprasPorCodEmprFfacturCtipoDocumCodProveed(codEmpr, chequesEmitidosDet.getFfactur(),
                    chequesEmitidosDet.getChequesEmitidosDetPK().getCtipoDocum(),
                    chequesEmitidos.getCodProveed().getCodProveed()
            );
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            selectItemsCompras.clear();
            selectItemsCompras.add(new SelectItem(""));
            for (Compras c : comprasList) {
                selectItemsCompras.add(new SelectItem(c, c.getComprasPK().getNrofact() + " - "
                        + dateFormat.format(c.getComprasPK().getFfactur())
                ));
            }
        }
    }

    public void onChangeCompras() {
        isaldo = compras.getIsaldo();
        ipagado = compras.getIsaldo();
    }

    public void agregarChequeEmitidoDetalle() {
        chequesEmitidosDet.getChequesEmitidosDetPK().setNrofact(compras.getComprasPK().getNrofact());
        chequesEmitidosDet.setIsaldo(isaldo);
        chequesEmitidosDet.setIpagado(ipagado);

        chequesEmitidosDets.add(chequesEmitidosDet);

        prepararDetalle();
    }

    @Transactional
    public void agregarChequeEmitido() {
        try {
            prepararChequesEmitidos();

            long totalIpagado = 0;

            if (modificar) {
                chequesEmitidosFacade.edit(chequesEmitidos);

                for (ChequesEmitidosDet ced : chequesEmitidosDets) {

                    totalIpagado += ced.getIpagado();
                }

                if (totalIpagado > 0 && totalIpagado > chequesEmitidos.getIcheque() + chequesEmitidos.getIretencion()) {
                    failOperation("Total del Cheque es inferior al Total de Facturas");
                }
            } else if (chequesEmitidosFacade.countChequesEmitidosPorNroChequeCodBanco(
                    chequesEmitidos.getChequesEmitidosPK().getNroCheque(),
                    chequesEmitidos.getChequesEmitidosPK().getCodBanco()) == 0) {

                chequesEmitidosFacade.create(chequesEmitidos);

                for (ChequesEmitidosDet ced : chequesEmitidosDets) {
                    prepararGuardarCuentasProveedores(ced);

                    prepararGuardarChequesEmitidosDet(ced);

                    totalIpagado += ced.getIpagado();
                }

                if (totalIpagado > 0 && totalIpagado > chequesEmitidos.getIcheque() + chequesEmitidos.getIretencion()) {
                    failOperation("Total del Cheque es inferior al Total de Facturas");
                }
            } else {
                failOperation("Ya existe el cheque nro '" + chequesEmitidos.getChequesEmitidosPK().getNroCheque() + "' "
                        + "en el banco '" + chequesEmitidos.getBancos().getXdesc() + "'");
            }
            successOperation();
        } catch (RuntimeException re) {
            reg.setRollbackOnly();
        }
    }

    private void prepararChequesEmitidos() {
        Bancos banco = bancosFacade.find(chequesEmitidos.getChequesEmitidosPK().getCodBanco());
        chequesEmitidos.setBancos(banco);
        if (icheque <= 0) {
            failOperation("El monto debe ser mayor que cero");
        } else {
            chequesEmitidos.setIcheque(icheque);
        }
        chequesEmitidos.setIretencion(chequesEmitidos.getIretencion() == null ? 0 : chequesEmitidos.getIretencion());
    }

    private void prepararGuardarCuentasProveedores(ChequesEmitidosDet ced) {
        CuentasProveedores cuentasProveedores = new CuentasProveedores();
        cuentasProveedores.setCodEmpr(codEmpr);
        cuentasProveedores.setCtipoDocum("CHE");
        cuentasProveedores.setFmovim(chequesEmitidos.getFemision());
        cuentasProveedores.setNdocumCheq(chequesEmitidos.getChequesEmitidosPK().getNroCheque());
        cuentasProveedores.setIpagado(ced.getIpagado());
        cuentasProveedores.setCodBanco(new Bancos(chequesEmitidos.getChequesEmitidosPK().getCodBanco()));
        cuentasProveedores.setCodProveed(chequesEmitidos.getCodProveed());
        cuentasProveedores.setIsaldo(0);
        cuentasProveedores.setManulado(Short.valueOf("1"));
        cuentasProveedores.setTexentas(0);
        cuentasProveedores.setTgravadas(0);
        cuentasProveedores.setTimpuestos(0);
        cuentasProveedores.setMindice(Short.valueOf("-1"));
        cuentasProveedores.setFvenc(chequesEmitidos.getFcheque());
        cuentasProveedores.setFacCtipoDocum(ced.getChequesEmitidosDetPK().getCtipoDocum());
        cuentasProveedores.setNrofact(ced.getChequesEmitidosDetPK().getNrofact());
        cuentasProveedores.setIretencion(chequesEmitidos.getIretencion());

        cuentasProveedoresFacade.insertarEnCuentasProveedores(cuentasProveedores);
    }

    private void prepararGuardarChequesEmitidosDet(ChequesEmitidosDet ced) {
        ced.getChequesEmitidosDetPK().setCodEmpr(codEmpr);
        ced.getChequesEmitidosDetPK().setCodBanco(chequesEmitidos.getChequesEmitidosPK().getCodBanco());
        ced.getChequesEmitidosDetPK().setNroCheque(chequesEmitidos.getChequesEmitidosPK().getNroCheque());
        ced.setCodProveed(chequesEmitidos.getCodProveed().getCodProveed());
        ced.setInteres(0);
        ced.setTtotal(0);
        ced.setChequesEmitidos(chequesEmitidos);
        chequesEmitidosDetFacade.create(ced);
    }

    private void successOperation() {
        PrimeFaces.current().executeScript("PF('dlgAgregar').hide();");
        FacesMessage msg = new FacesMessage("Operación exitosa");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void failOperation(String message) {
        FacesMessage msg = new FacesMessage(message);
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        throw new RuntimeException();
    }

    @Transactional
    public void eliminarChequeEmitido() {
        try {
            cuentasProveedoresFacade.borrarChequesProveedores(
                    codEmpr, "CHE",
                    chequesEmitidosSeleccionado.getCodProveed().getCodProveed(),
                    chequesEmitidosSeleccionado.getChequesEmitidosPK().getNroCheque(),
                    chequesEmitidosSeleccionado.getBancos().getCodBanco());
            cuentasProveedoresFacade.borrarChequesProveedores(
                    codEmpr, "CHP",
                    chequesEmitidosSeleccionado.getCodProveed().getCodProveed(),
                    chequesEmitidosSeleccionado.getChequesEmitidosPK().getNroCheque(),
                    chequesEmitidosSeleccionado.getBancos().getCodBanco());
            cuentasProveedoresFacade.borrarChequesProveedores(
                    codEmpr, "CHO",
                    chequesEmitidosSeleccionado.getCodProveed().getCodProveed(),
                    chequesEmitidosSeleccionado.getChequesEmitidosPK().getNroCheque(),
                    chequesEmitidosSeleccionado.getBancos().getCodBanco());

            chequesEmitidosDets = chequesEmitidosDetFacade.chequesEmitidosDetPorChequesEmitidos(chequesEmitidosSeleccionado);
            for (ChequesEmitidosDet ced : chequesEmitidosDets) {
                chequesEmitidosDetFacade.remove(ced);
            }

            chequesEmitidosFacade.remove(chequesEmitidosSeleccionado);
            successOperation();
        } catch (RuntimeException re) {
            reg.setRollbackOnly();
        }
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

    public List<ChequesEmitidosDet> getChequesEmitidosDets() {
        return chequesEmitidosDets;
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

    public ChequesEmitidosDet getChequesEmitidosDetSeleccionado() {
        return chequesEmitidosDetSeleccionado;
    }

    public void setChequesEmitidosDetSeleccionado(ChequesEmitidosDet chequesEmitidosDetSeleccionado) {
        this.chequesEmitidosDetSeleccionado = chequesEmitidosDetSeleccionado;
    }

    public Compras getCompras() {
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
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

    public Long getIcheque() {
        return icheque;
    }

    public void setIcheque(Long icheque) {
        this.icheque = icheque;
    }

    public Long getIsaldo() {
        return isaldo;
    }

    public void setIsaldo(Long isaldo) {
        this.isaldo = isaldo;
    }

    public Long getIpagado() {
        return ipagado;
    }

    public void setIpagado(Long ipagado) {
        this.ipagado = ipagado;
    }

}
