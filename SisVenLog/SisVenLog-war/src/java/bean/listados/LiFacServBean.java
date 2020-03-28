package bean.listados;

import dao.ClientesFacade;
import dao.ExcelFacade;
import entidad.Clientes;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class LiFacServBean {

    @EJB
    private ClientesFacade clientesFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Clientes clientes;
    private List<Clientes> listaClientes;

    private Date desde;
    private Date hasta;

    private Boolean todos;
    private Boolean conDetalles;

    private String estados;

    private String clientesSel;

    public LiFacServBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.clientes = new Clientes();
        this.listaClientes = new ArrayList<Clientes>();

        this.desde = new Date();
        this.hasta = new Date();

        setTodos(false);
        setClientesSel("");
        setEstados("A");
        setConDetalles(false);
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        StringBuilder sql = new StringBuilder();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        Integer clie = 0;
        String descclie = "";
        String canal = "";
        String desccanal = "";

        if (this.clientes.getCodCliente() != null) {
            clie = Integer.parseInt(this.clientes.getCodCliente() + "");
            descclie = clientesFacade.find(this.clientes.getCodCliente()).getXnombre();
        } else {
            clie = 0;
            descclie = "TODOS";
        }

        if (conDetalles) { // sin detalles
            sql.append(" SELECT f.CTIPO_DOCUM, f.nrofact, f.ffactur, 0 as nro_nota, f.tgravadas, f.texentas, f.timpuestos, f.ttotal, f.mestado, \n"
                    + " F.xrazon_social as xnombre, f.isaldo, f.mestado, f.cod_cliente, f.tgravadas_10, f.tgravadas_5, f.timpuestos_10, f.timpuestos_5, f.texentas \n"
                    + " FROM facturas f \n"
                    + " WHERE f.cod_empr = 2 \n"
                    + " AND ctipo_docum IN ('FCS', 'FCP')\n "
                    + " AND f.ffactur BETWEEN ?l_finicial AND ?l_ffinal \n");
        } else {
            sql.append("SELECT f.*,f.xrazon_social as xnombre, d.*, s.xdesc \n"
                    + " FROM facturas f,  facturas_ser d, servicios s \n"
                    + " WHERE f.cod_empr = 2 \n"
                    + " AND d.cod_empr = 2 \n"
                    + " AND f.ctipo_docum IN ('FCS','FCP') \n"
                    + " AND f.cod_empr = d.cod_empr \n"
                    + " AND f.nrofact = d.nrofact \n"
                    + " AND f.ffactur = d.ffactur \n"
                    + " AND d.cod_servicio = s.cod_servicio \n"
                    + " AND f.ctipo_docum = d.ctipo_docum \n"
                    + " AND f.ffactur BETWEEN '" + fdesde + "' AND '" + fhasta + "' \n");
        }

        if (estados.equals("A")) {
            sql.append(" AND f.mestado = 'A' ");
        } else if (estados.equals("X")) {
            sql.append(" AND f.mestado = 'X' ");
        }

//clientes seleccionados
        if (!clientesSel.equals("")) {
            sql.append(" AND f.cod_cliente IN (" + clientesSel + ")");
        }

        if (conDetalles) {
            sql.append("GROUP BY n.ctipo_docum, n.nro_nota, n.nrofact, n.fdocum, \n"
                    + "f.cod_cliente, f.xrazon_social, \n"
                    + "n.tgravadas, n.timpuestos, n.texentas, f.mestado ");
        }

        if (tipo.equals("VIST")) {
            rep.reporteLiFactServ(sql.toString(), dateToString2(desde), dateToString2(hasta), "admin", tipo, conDetalles);
        } else if (tipo.equals("ARCH")) {
            List<Object[]> auxExcel = new ArrayList<Object[]>();

            auxExcel = excelFacade.listarParaExcel(sql.toString());

            rep.excelLifactServ(auxExcel);
        }

    }

    private String dateToString(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    private String dateToString2(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    public void todosLosClientes() {

        if (this.todos == true) {
            this.clientes = new Clientes();
            clientesSel = "";
        }

    }

    public void buscadorClienteTab(AjaxBehaviorEvent event) {
        try {

            if (this.clientes != null) {
                if (!this.clientes.getCodCliente().equals("")) {

                    this.clientes = this.clientesFacade.buscarPorCodigo(this.clientes.getCodCliente() + "");

                    if (getClientesSel().equals("")) {
                        setClientesSel(getClientesSel() + clientes.getCodCliente());
                    } else {
                        setClientesSel(getClientesSel() + "," + clientes.getCodCliente());
                    }

                    if (this.clientes == null) {

                        this.clientes = new Clientes();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "No encontrado."));
                    }
                }
            } else {

                this.clientes = new Clientes();
            }
        } catch (Exception e) {

            this.clientes = new Clientes();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error en la busqueda"));
        }
    }

    //Getters & Setters
    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes sublineas) {
        this.clientes = sublineas;
    }

    public List<Clientes> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Clientes> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Boolean getTodos() {
        return todos;
    }

    public void setTodos(Boolean todos) {
        this.todos = todos;
    }

    public String getClientesSel() {
        return clientesSel;
    }

    public void setClientesSel(String clientesSel) {
        this.clientesSel = clientesSel;
    }

    public Boolean getConDetalles() {
        return conDetalles;
    }

    public void setConDetalles(Boolean conDetalles) {
        this.conDetalles = conDetalles;
    }

    public String getEstados() {
        return estados;
    }

    public void setEstados(String estados) {
        this.estados = estados;
    }

}
