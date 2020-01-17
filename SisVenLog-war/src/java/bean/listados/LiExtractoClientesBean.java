package bean.listados;

import dao.CanalesCompraFacade;
import dao.DepositosFacade;
import dao.ClientesFacade;
import dao.ExcelFacade;
import dto.LiMercaSinDto;
import entidad.CanalesCompra;
import entidad.CanalesCompraPK;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Clientes;
import entidad.Clientes;
import java.io.IOException;
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
public class LiExtractoClientesBean {

    @EJB
    private ClientesFacade clientesFacade;
    
    
    @EJB
    private ExcelFacade excelFacade;


    private Clientes clientes;
    private List<Clientes> listaClientes;

    private Date desde;
    private Date hasta;

    private Boolean todos;

    private String clientesSel;

    public LiExtractoClientesBean() throws IOException {

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

        if (this.clientes != null) {
            clie = Integer.parseInt(this.clientes.getCodCliente() + "");
            descclie = clientesFacade.find(this.clientes.getCodCliente()).getXnombre();
        } else {
            clie = 0;
            descclie = "TODOS";
        }

            sql.append("SELECT c.cod_cliente,c.ctipo_docum, c.ndocum_cheq, c.fac_ctipo_docum, c.nrofact, \n"
                + "   c. fvenc, c.fmovim, t.xnombre, t.nplazo_credito, \n"
                + "  (c.texentas+c.tgravadas+c.timpuestos+c.ipagado) as imovim,  \n"
                + "  mindice \n"
                + " FROM cuentas_corrientes c, clientes t \n"
                + " WHERE fmovim BETWEEN'"+fdesde+"' AND '"+fhasta+"'  \n"
                + " AND ( c.fac_ctipo_docum = 'FCR' \n"
                + " OR c.ctipo_docum IN ('CHQ','CHC','PAG','PAC') )\n"
                + " AND fmovim >= '01/09/2005' \n"
                + " AND c.cod_cliente = t.cod_cliente \n");
        
        if (this.clientesSel != "") {
            sql.append("AND c.cod_cliente IN ("+this.clientesSel+") \n");
        }
        
        sql.append(" ORDER BY c.cod_cliente, fmovim, c.ctipo_docum, c.ndocum_cheq \n");
        
        if (tipo.equals("VIST")) {
            rep.reporteLiExtractoCliente(sql.toString(), dateToString2(desde), dateToString2(hasta), "admin", tipo);
        } else if (tipo.equals("ARCH")) {
            List<Object[]> auxExcel = new ArrayList<Object[]>();

            auxExcel = excelFacade.listarParaExcel(sql.toString());

            rep.excelLiExtractoCliente(auxExcel);
        }



        rep.reporteLiExtractoProveedor(clie, descclie, canal, desccanal, dateToString(desde), dateToString2(desde), dateToString(hasta), dateToString2(hasta), "admin", tipo);
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
                    }else{
                        setClientesSel( getClientesSel() + "," + clientes.getCodCliente());
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

}
