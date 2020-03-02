package bean;

import dao.ClientesFacade;
import dao.DocumVariosDetFacade;
import dao.DocumVariosFacade;
import dao.EmpleadosFacade;
import dao.PedidosDetFacade;
import dao.ZonasFacade;
import entidad.Clientes;
import entidad.DocumVarios;
import entidad.DocumVariosDet;
import entidad.DocumVariosPK;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.PedidosDet;
import entidad.Zonas;
import entidad.ZonasPK;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@Named(value = "autoSGMercaBean")
@ViewScoped
public class AutorizacionSalidaGratuitaMercaderiaBean implements Serializable {

    @EJB
    private ClientesFacade clienteFacade;
    @EJB
    private ZonasFacade zonaFacade;
    @EJB
    private EmpleadosFacade empleadoFacade;
    @EJB
    private DocumVariosFacade documentoVarioFacade;
    @EJB
    private PedidosDetFacade pedidoDetFacade;
    @EJB
    private DocumVariosDetFacade documentoVariosDetFacade;

    private Date fechaInicio, fechaFin;
    private Empleados empleado;
    private Zonas zona;
    private Clientes cliente;
    private DocumVarios documVarioSelect;

    private List<Empleados> listaVendedor;
    private List<Zonas> listaZonas;
    private List<DocumVarios> listaDocumentoVarios;
    private List<DocumVariosDet> listaDocumentoVariosDet;
    private List<PedidosDet> listaPedidoDet;

    private Map<DocumVariosPK, Boolean> listaAutorizada;

    @PostConstruct
    public void init() {
        limpiar();

        listaZonas = zonaFacade.listarZonas();
        listaVendedor = empleadoFacade.listarEmpleadosActivos();
        listaDocumentoVarios = new ArrayList<DocumVarios>();
        documVarioSelect = null;
        listaDocumentoVariosDet = new ArrayList<DocumVariosDet>();
        listaPedidoDet = new ArrayList<PedidosDet>();
        listaAutorizada = new HashMap<DocumVariosPK, Boolean>();
    }

    public Map<DocumVariosPK, Boolean> getListaAutorizada() {
        return listaAutorizada;
    }

    public void setListaAutorizada(Map<DocumVariosPK, Boolean> listaAutorizada) {
        this.listaAutorizada = listaAutorizada;
    }

    public List<PedidosDet> getListaPedidoDet() {
        return listaPedidoDet;
    }

    public void setListaPedidoDet(List<PedidosDet> listaPedidoDet) {
        this.listaPedidoDet = listaPedidoDet;
    }

    public List<DocumVariosDet> getListaDocumentoVariosDet() {
        return listaDocumentoVariosDet;
    }

    public void setListaDocumentoVariosDet(List<DocumVariosDet> listaDocumentoVariosDet) {
        this.listaDocumentoVariosDet = listaDocumentoVariosDet;
    }

    public DocumVarios getDocumVarioSelect() {
        return documVarioSelect;
    }

    public void setDocumVarioSelect(DocumVarios documVarioSelect) {
        this.documVarioSelect = documVarioSelect;
    }

    public List<DocumVarios> getListaDocumentoVarios() {
        return listaDocumentoVarios;
    }

    public void setListaDocumentoVarios(List<DocumVarios> listaDocumentoVarios) {
        this.listaDocumentoVarios = listaDocumentoVarios;
    }

    public List<Empleados> getListaVendedor() {
        return listaVendedor;
    }

    public void setListaVendedor(List<Empleados> listaVendedor) {
        this.listaVendedor = listaVendedor;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public void buscar() {
        System.out.println("Fecha inicio : " + fechaInicio);
        System.out.println("Fecha Fin : " + fechaFin);

        String fInicio = null, fFin = null;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        if (fechaInicio != null) {
            fInicio = formato.format(fechaInicio);
        }
        if (fechaFin != null) {
            fFin = formato.format(fechaFin);
        }

        String sql = "SELECT v.*, d.*,c.*,r.*,p.* "
                + " FROM docum_varios v "
                + " INNER JOIN clientes c ON v.cod_cliente = c.cod_cliente "
                + " INNER JOIN rutas r ON c.cod_ruta = r.cod_ruta "
                + " LEFT OUTER JOIN pedidos p ON v.nro_pedido = p.nro_pedido AND p.cod_empr = 2  "
                + " INNER JOIN depositos d ON v.cod_depo = d.cod_depo "
                + " WHERE (v.ctipo_docum = 'SG') "
                + " AND (v.fdocum BETWEEN '" + fInicio + "'  AND '" + fFin + "' )  "
                + " AND (v.cod_empr = 2) AND (v.mestado = 'P')";

        if (zona != null) {
            System.out.println("zona : " + zona.getXdesc());
            if (zona.getZonasPK() != null) {
                if (zona.getZonasPK().getCodZona() != null) {
                    sql += " AND r.cod_zona=" + zona.getZonasPK().getCodZona();
                }
            }
        }

        if (empleado != null) {
            System.out.println("empleado : " + empleado.getXnombre() + " : codEmpleado: " + empleado.getEmpleadosPK().getCodEmpleado());
            if (empleado.getEmpleadosPK() != null) {
                if (empleado.getEmpleadosPK().getCodEmpleado() != 0) {
                    sql += " AND p.cod_vendedor=" + empleado.getEmpleadosPK().getCodEmpleado();
                }
            }
        }

        if (cliente != null) {
            System.out.println("cliente : " + cliente.getXnombre() + " : codCliente: " + cliente.getCodCliente());
            if (cliente.getCodCliente() != null) {
                sql += " AND v.cod_cliente = " + cliente.getCodCliente();
            }
        }

        sql += " ORDER BY v.fdocum, p.cod_vendedor ";

        System.out.println("sql buscador : " + sql);

        listaDocumentoVarios = documentoVarioFacade.listarDocumValidos(sql);

        listaAutorizada = new HashMap<DocumVariosPK, Boolean>();
        System.out.println("Cantidad documento: " + listaDocumentoVarios.size());
        for (DocumVarios d : listaDocumentoVarios) {
            System.out.println("documento : " + d.getDocumVariosPK());
            listaAutorizada.put(d.getDocumVariosPK(), Boolean.FALSE);
        }

    }

    public void limpiar() {
        fechaInicio = null;
        fechaFin = null;
        empleado = new Empleados();
        empleado.setEmpleadosPK(new EmpleadosPK());
        zona = new Zonas();
        zona.setZonasPK(new ZonasPK());
        cliente = new Clientes();
        listaDocumentoVarios = new ArrayList<DocumVarios>();
    }

    public void procesar() {

        for (Map.Entry<DocumVariosPK, Boolean> entry : listaAutorizada.entrySet()) {
            System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            if (entry.getValue()) {
                documentoVarioFacade.actualizarVigenciaPromocion(entry.getKey().getNdocum(), "admin");
            }
        }
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso ", "Fin de Proceso"));
    }

    public void visualizarCliente() {
        Clientes c = clienteFacade.buscarPorCodigo(String.valueOf(cliente.getCodCliente()));
        if (c == null) {
            cliente = new Clientes();
        } else {
            cliente = c;
        }
    }

    public void preparedDetalleSalidaGratuita() {
        System.out.println("Seleccionado : " + documVarioSelect.getXnombre());

        listaDocumentoVariosDet = documentoVariosDetFacade.listarDocumentosVarioDetPorNroDocument(documVarioSelect.getDocumVariosPK().getNdocum());
//        listaDocumentoVariosDet = new ArrayList<DocumVariosDet>();
    }

    public void preparedDetallePedido() {
        System.out.println("Seleccionado : " + documVarioSelect.getNroPedido());

        listaPedidoDet = pedidoDetFacade.listaPedidosDetPorNroPedido(documVarioSelect.getNroPedido());

//        listaPedidoDet = new ArrayList<PedidosDet>();
    }
}
