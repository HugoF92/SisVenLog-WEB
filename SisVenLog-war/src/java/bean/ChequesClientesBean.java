/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.BancosFacade;
import dao.ChequesFacade;
import dao.ClientesFacade;
import dao.EmpleadosFacade;
import dao.FacturasFacade;
import dao.TiposDocumentosFacade;
import entidad.Bancos;
import entidad.Cheques;
import entidad.ChequesDet;
import entidad.ChequesDetPK;
import entidad.ChequesPK;
import entidad.Clientes;
import entidad.Empleados;
import entidad.Facturas;
import entidad.FacturasPK;
import entidad.TiposDocumentos;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ViewScoped
@Named
public class ChequesClientesBean implements Serializable {

    final private List<SelectItem> bancos = new ArrayList<>();
    final private List<SelectItem> empleados = new ArrayList<>();
    final private List<SelectItem> facturaTipos = new ArrayList<>();
    final private List<String> facturaTiposUsados = Arrays.asList("CPV", "FCO");

    final private short codEmpr = Short.parseShort("2");

    @EJB
    private BancosFacade bancosFacade;
    @EJB
    private ChequesFacade chequesFacade;
    @EJB
    private ClientesFacade clientesFacade;
    @EJB
    private FacturasFacade facturasFacade;
    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;
    @EJB
    private EmpleadosFacade empleadosFacade;

    private LazyDataModel<Cheques> lazyDataModel;
    private LazyDataModel<Facturas> lazyDataModelFacturas;
    private List<ChequesDet> chequesDets;
    private List<Clientes> listaClientes;
    private List<Facturas> facturasSeleccionadas;

    private Cheques cheques, chequesSeleccionado;
    private ChequesDet chequesDet, chequesDetSeleccionado;
    private Clientes clienteSeleccionado;
    private String titulo, codClienteSeleccionado, nombreClienteSeleccionado,
            filtro, facturaTipo;
    private Long totalFacturas;

    private boolean visualizar, modificar, eliminar,
            deshabilitarBotonModificar = true,
            deshabilitarBotonEliminar = true,
            agregarFacturas = true;

    private void listarBancos() {
        List<Bancos> bs = bancosFacade.listarBancos();
        for (Bancos b : bs) {
            bancos.add(new SelectItem(b.getCodBanco(), b.getXdesc()));
        }
    }

    private void listarEmpleados() {
        List<Empleados> es = empleadosFacade.listarEntregador();
        for (Empleados e : es) {
            empleados.add(new SelectItem(e.getEmpleadosPK().getCodEmpleado(), e.getEmpleadosPK().getCodEmpleado() + " - " + e.getXnombre()));
        }
    }

    private void listarFacturaTipos() {
        List<TiposDocumentos> tds = tiposDocumentosFacade.listarTiposDocumentosUsados(facturaTiposUsados);
        for (TiposDocumentos td : tds) {
            facturaTipos.add(new SelectItem(td.getCtipoDocum(), td.getXdesc()));
        }
    }

    private void listarCheques() {
        lazyDataModel = new LazyDataModel<Cheques>() {
            @Override
            public List<Cheques> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                String nroCheque = filters.get("chequesPK.nroCheque") != null
                        ? filters.get("chequesPK.nroCheque").toString() : null;
                String xdesc = filters.get("bancos.xdesc") != null ? filters.get("bancos.xdesc").toString() : null;
                String xnombre = filters.get("codCliente.xnombre") != null
                        ? filters.get("codCliente.xnombre").toString() : null;
                Long icheque = filters.get("icheque") != null
                        ? Long.parseLong(filters.get("icheque").toString()) : null;
                Date fcheque = filters.get("fcheque") != null ? (Date) filters.get("fcheque") : null;
                Integer codCliente = filters.get("codCliente.codCliente") != null
                        ? Integer.parseInt(filters.get("codCliente.codCliente").toString()) : null;

                this.setRowCount(chequesFacade.countList(
                        nroCheque,
                        xdesc,
                        fcheque,
                        icheque,
                        codCliente,
                        xnombre
                ).intValue());

                return chequesFacade.getList(
                        nroCheque,
                        xdesc,
                        fcheque,
                        icheque,
                        codCliente,
                        xnombre,
                        sortField,
                        sortOrder.name(),
                        new int[]{first, pageSize}
                );
            }

            @Override
            public Cheques getRowData(String rowKey) {
                String tempIndex = rowKey;

                if (tempIndex != null) {
                    for (Cheques ce : lazyDataModel.getWrappedData()) {
                        if (String.valueOf(ce.getChequesPK().getCodEmpr()).concat(" ")
                                .concat(String.valueOf(ce.getChequesPK().getCodBanco())).concat(" ")
                                .concat(ce.getChequesPK().getNroCheque()).concat(" ")
                                .concat(ce.getChequesPK().getXcuentaBco()).equals(rowKey)) {
                            return ce;
                        }
                    }
                }

                return null;
            }

            @Override
            public Object getRowKey(Cheques ch) {
                ChequesPK pk = ch != null ? ch.getChequesPK() : null;

                return pk != null ? pk.getCodEmpr() + " " + pk.getCodBanco() + " " + pk.getNroCheque() + " " + pk.getXcuentaBco() : null;
            }
        };
    }

    private void failOperation(String message, boolean throwRuntimeException) {
        FacesMessage msg = new FacesMessage(message);
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        if (throwRuntimeException) {
            throw new RuntimeException();
        }
    }

    private Date validarFechaCheque(int min, int max, Date fecha) {
        if (fecha != null) {
            LocalDateTime fcheque = fecha.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            LocalDateTime actual = LocalDateTime.now();
            LocalDateTime minDate = actual.minusDays(min);
            LocalDateTime maxDate = actual.plusDays(max);

            if (fcheque.isBefore(minDate.minusDays(1))) {
                fecha = new Date();
                failOperation("Ingrese una fecha Mayor/Igual al " + minDate.format(DateTimeFormatter.ofPattern("dd/MM/yy")), false);
            } else if (fcheque.isAfter(maxDate)) {
                fecha = new Date();
                failOperation("Ingrese una fecha Menor/Igual al " + maxDate.format(DateTimeFormatter.ofPattern("dd/MM/yy")), false);
            }
        }
        return fecha;
    }

    @PostConstruct
    public void init() {
        prepararDialog("ag");

        bancos.add(new SelectItem(""));
        empleados.add(new SelectItem(""));
        facturaTipos.add(new SelectItem(""));

        listarBancos();
        listarEmpleados();
        listarFacturaTipos();
        listarCheques();
    }

    public void onRowSelect(SelectEvent event) {
        deshabilitarBotonModificar = false;
        deshabilitarBotonEliminar = false;
    }

    public void prepararDialog(String operacion) {
        if (operacion.equalsIgnoreCase("ag")) {
            titulo = "Nuevo cheque";
            modificar = false;
            visualizar = false;
            eliminar = false;

            cheques = new Cheques();
            cheques.setChequesPK(new ChequesPK());
            cheques.getChequesPK().setCodEmpr(codEmpr);
            cheques.setFcheque(new Date());
            cheques.setFemision(new Date());

            chequesDets = new ArrayList<>();
            facturasSeleccionadas = new ArrayList<>();

            totalFacturas = null;
            codClienteSeleccionado = null;
            nombreClienteSeleccionado = null;
            filtro = null;
            facturaTipo = null;
        } else {
            if (operacion.equalsIgnoreCase("mo")) {
                titulo = "Modificar cheque";
                modificar = true;
                visualizar = false;
                eliminar = false;
            } else if (operacion.equalsIgnoreCase("vi")) {
                titulo = "Visualizar cheque";
                modificar = false;
                visualizar = true;
                eliminar = false;
            } else if (operacion.equalsIgnoreCase("el")) {
                titulo = "Eliminar cheque";
                modificar = false;
                visualizar = true;
                eliminar = true;
            }
            cheques = chequesSeleccionado;
        }
        codClienteSeleccionado = null;
        nombreClienteSeleccionado = null;
    }

    public void verificarCliente() {
        if (codClienteSeleccionado != null) {
            if (codClienteSeleccionado == null || codClienteSeleccionado.isEmpty()) {
                //mostrar busqueda de clientes
                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').show();");
            } else {
                try {
                    Clientes clienteBuscado = clientesFacade.find(codClienteSeleccionado);
                    if (clienteBuscado == null) {
                        //mostrar busqueda de clientes
                        RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').show();");
                    } else {
                        nombreClienteSeleccionado = clienteBuscado.getXnombre();
                    }
                } catch (Exception e) {
                    failOperation("Error en la lectura de datos de clientes.", false);
                }
            }
        }
    }

    public void onRowClientesSelect(SelectEvent event) {
        if (clienteSeleccionado != null) {
            if (clienteSeleccionado.getXnombre() != null) {
                codClienteSeleccionado = clienteSeleccionado.getCodCliente().toString();
                nombreClienteSeleccionado = clienteSeleccionado.getXnombre();

                RequestContext.getCurrentInstance().execute("PF('dlgBusClieConsultaDocumentoVenta').hide();");
                RequestContext.getCurrentInstance().execute("PF('dlgAgregar').show();");
                RequestContext.getCurrentInstance().update("cabecera");

                generarDetalles();
            }
            if (cheques.getCodEntregador() != null) {
                comprobarFacturasPorEntregador();
            }
        }
    }

    public void inicializarBuscadorClientes() {
        listaClientes = new ArrayList<>();
        clienteSeleccionado = new Clientes();
        filtro = "";
        listarClientesBuscador();
    }

    public void listarClientesBuscador() {
        try {
            listaClientes = clientesFacade.buscarPorFiltro(filtro);
        } catch (Exception e) {
            failOperation("Error en la lectura de datos de clientes.", false);
        }
    }

    public void comprobarExistenciaNroCheque() {
        if (!modificar && cheques.getChequesPK().getNroCheque() != null
                && !cheques.getChequesPK().getNroCheque().isEmpty()) {
            Bancos b = bancosFacade.find(cheques.getChequesPK().getCodBanco());
            if (b != null) {
                Long count = chequesFacade.countList(cheques.getChequesPK().getNroCheque(), b.getXdesc().trim(), null, null, null, null);
                if (count > 0) {
                    cheques.getChequesPK().setNroCheque(null);
                    failOperation("Ya existe este Nro. de Cheque", false);
                } else {
                    generarDetalles();
                }
            }
        }
    }

    public void generarDetalles() {
        if (codClienteSeleccionado != null && cheques.getFemision() != null
                && cheques.getChequesPK().getCodBanco() != 0 && cheques.getChequesPK().getNroCheque() != null) {
            chequesDets = new ArrayList<>();
            totalFacturas = 0L;

            List facturasTipos = new ArrayList<>();
            if (facturaTipo == null) {
                facturasTipos = facturaTiposUsados;
            } else {
                facturasTipos.add(facturaTipo);
            }

            List<Facturas> facturas = facturasFacade.obtenerFacturasContadoConSaldoActivas(
                    Integer.parseInt(codClienteSeleccionado),
                    codEmpr,
                    cheques.getFemision(),
                    facturasTipos
            );

            for (Facturas f : facturas) {
                ChequesDetPK chequesDetPK = new ChequesDetPK();
                chequesDetPK.setCodEmpr(codEmpr);
                chequesDetPK.setCodBanco(cheques.getChequesPK().getCodBanco());
                chequesDetPK.setNroCheque(cheques.getChequesPK().getNroCheque());
                chequesDetPK.setNrofact(f.getFacturasPK().getNrofact());
                chequesDetPK.setCtipoDocum(f.getFacturasPK().getCtipoDocum());

                ChequesDet cd = new ChequesDet();
                cd.setChequesDetPK(chequesDetPK);
                cd.setFfactur(f.getFacturasPK().getFfactur());
                cd.setIpagado(f.getIsaldo());
                cd.setTtotal(f.getTtotal());

                totalFacturas += f.getIsaldo();

                chequesDets.add(cd);
            }

            cheques.setIcheque(totalFacturas);
        }
    }

    public void agregarDetalles() {
        chequesDets = new ArrayList<>();
        totalFacturas = 0L;

        for (Facturas f : facturasSeleccionadas) {
            ChequesDetPK chequesDetPK = new ChequesDetPK();
            chequesDetPK.setCodEmpr(codEmpr);
            chequesDetPK.setCodBanco(cheques.getChequesPK().getCodBanco());
            chequesDetPK.setNroCheque(cheques.getChequesPK().getNroCheque());
            chequesDetPK.setNrofact(f.getFacturasPK().getNrofact());
            chequesDetPK.setCtipoDocum(f.getFacturasPK().getCtipoDocum());

            ChequesDet cd = new ChequesDet();
            cd.setChequesDetPK(chequesDetPK);
            cd.setFfactur(f.getFacturasPK().getFfactur());
            cd.setIpagado(f.getIsaldo());
            cd.setTtotal(f.getTtotal());

            totalFacturas += f.getIsaldo();

            chequesDets.add(cd);
        }
    }

    public void validarFechaCheque() {
        cheques.setFcheque(validarFechaCheque(5, 2, cheques.getFcheque()));
    }

    public void validarFechaEmision() {
        cheques.setFemision(validarFechaCheque(5, 5, cheques.getFemision()));
        generarDetalles();
    }

    public String getXdesc(String ctipoDocum) {
        TiposDocumentos tiposDocumentos = tiposDocumentosFacade.find(ctipoDocum);
        return tiposDocumentos != null ? tiposDocumentos.getXdesc() : "";
    }

    public String getXfactura(int index, short codEmpr, long nrofact, String ctipoDocum, Date ffactur) {
        FacturasPK facturasPK = new FacturasPK(codEmpr, nrofact, ctipoDocum, ffactur);
        Facturas f = facturasFacade.find(facturasPK);

        if (f != null) {
            String xfactura = f.getXfactura();
            String[] xfacturaSplitted = xfactura.split("-");
            return xfacturaSplitted[index];
        } else {
            return "";
        }
    }

    public Long parseLong(String string) {
        return Long.parseLong(string);
    }

    public void comprobarFacturasPorEntregador() {
        if (cheques.getFemision() != null && cheques.getCodEntregador() != null && codClienteSeleccionado != null) {
            List<Facturas> facturas = facturasFacade.obtenerFacturasContadoPorEntregadorFechaEmision(codEmpr,
                    cheques.getFemision(), cheques.getCodEntregador(), facturaTiposUsados, Integer.parseInt(codClienteSeleccionado));

            if (facturas.isEmpty()) {
                failOperation("No existen Facturas de Vta. para este entregador", false);
                agregarFacturas = false;
                chequesDets = new ArrayList<>();
            } else {
                if (!agregarFacturas) {
                    chequesDets = new ArrayList<>();
                    agregarFacturas = true;
                }
            }
        }
    }

    public void listarFacturas() {
        lazyDataModelFacturas = new LazyDataModel<Facturas>() {
            @Override
            public List<Facturas> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                String xfactura = filters.get("xfactura") != null ? filters.get("xfactura").toString() : null;
                Date ffactur = filters.get("facturasPK.ffactur") != null ? (Date) filters.get("facturasPK.ffactur") : null;

                List facturasTipos = new ArrayList<>();
                if (facturaTipo == null) {
                    facturasTipos = facturaTiposUsados;
                } else {
                    facturasTipos.add(facturaTipo);
                }

                this.setRowCount(facturasFacade.countList(
                        xfactura,
                        facturasTipos,
                        ffactur,
                        Integer.parseInt(codClienteSeleccionado)
                ).intValue());

                return facturasFacade.getList(
                        xfactura,
                        facturasTipos,
                        ffactur,
                        Integer.parseInt(codClienteSeleccionado),
                        sortField,
                        sortOrder.name(),
                        new int[]{first, pageSize}
                );
            }

            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public Facturas getRowData(String rowKey) {
                String tempIndex = rowKey;

                if (tempIndex != null) {
                    for (Facturas ce : lazyDataModelFacturas.getWrappedData()) {
                        if (String.valueOf(ce.getFacturasPK().getCodEmpr()).concat(" ")
                                .concat(String.valueOf(ce.getFacturasPK().getCtipoDocum())).concat(" ")
                                .concat(dateFormat.format(ce.getFacturasPK().getFfactur())).concat(" ")
                                .concat(String.valueOf(ce.getFacturasPK().getNrofact())).equals(rowKey)) {
                            return ce;
                        }
                    }
                }

                return null;
            }

            @Override
            public Object getRowKey(Facturas ch) {
                FacturasPK pk = ch != null ? ch.getFacturasPK() : null;

                return pk != null ? pk.getCodEmpr() + " " + pk.getCtipoDocum() + " " + dateFormat.format(pk.getFfactur()) + " " + pk.getNrofact() : null;
            }
        };
    }

    public LazyDataModel<Cheques> getLazyDataModel() {
        return lazyDataModel;
    }

    public LazyDataModel<Facturas> getLazyDataModelFacturas() {
        return lazyDataModelFacturas;
    }

    public List<ChequesDet> getChequesDets() {
        return chequesDets;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public List<Facturas> getFacturasSeleccionadas() {
        return facturasSeleccionadas;
    }

    public void setFacturasSeleccionadas(List<Facturas> facturasSeleccionadas) {
        this.facturasSeleccionadas = facturasSeleccionadas;
    }

    public Cheques getCheques() {
        return cheques;
    }

    public Cheques getChequesSeleccionado() {
        return chequesSeleccionado;
    }

    public void setChequesSeleccionado(Cheques chequesSeleccionado) {
        this.chequesSeleccionado = chequesSeleccionado;
    }

    public ChequesDet getChequesDet() {
        return chequesDet;
    }

    public ChequesDet getChequesDetSeleccionado() {
        return chequesDetSeleccionado;
    }

    public Clientes getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Clientes clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNombreClienteSeleccionado() {
        return nombreClienteSeleccionado;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getCodClienteSeleccionado() {
        return codClienteSeleccionado;
    }

    public void setCodClienteSeleccionado(String codClienteSeleccionado) {
        this.codClienteSeleccionado = codClienteSeleccionado;
    }

    public String getFacturaTipo() {
        return facturaTipo;
    }

    public void setFacturaTipo(String facturaTipo) {
        this.facturaTipo = facturaTipo;
    }

    public Long getTotalFacturas() {
        return totalFacturas;
    }

    public void setTotalFacturas(Long totalFacturas) {
        this.totalFacturas = totalFacturas;
    }

    public boolean isVisualizar() {
        return visualizar;
    }

    public boolean isModificar() {
        return modificar;
    }

    public boolean isEliminar() {
        return eliminar;
    }

    public boolean isDeshabilitarBotonModificar() {
        return deshabilitarBotonModificar;
    }

    public boolean isDeshabilitarBotonEliminar() {
        return deshabilitarBotonEliminar;
    }

    public boolean isAgregarFacturas() {
        return agregarFacturas;
    }

    public List<SelectItem> getBancos() {
        return bancos;
    }

    public List<SelectItem> getEmpleados() {
        return empleados;
    }

    public List<SelectItem> getFacturaTipos() {
        return facturaTipos;
    }
}
