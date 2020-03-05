package bean.listados;

import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.ExcelFacade;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Empleados;
import entidad.EmpleadosPK;
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
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class LiControlPedBean {

    @EJB
    private EmpleadosFacade empleadosFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Empleados empleados;
    private List<Empleados> listaEmpleados;

    @EJB
    private DepositosFacade depositosFacade;

    private Depositos depositos;
    private List<Depositos> listaDepositos;

    private Date desde;
    private Date hasta;

    public LiControlPedBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.empleados = new Empleados(new EmpleadosPK());
        this.listaEmpleados = new ArrayList<Empleados>();

        this.depositos = new Depositos(new DepositosPK());
        this.listaDepositos = new ArrayList<Depositos>();

        this.desde = new Date();
        this.hasta = new Date();
    }

    public List<Empleados> listarEmpleados() {
        this.listaEmpleados = empleadosFacade.listarEmpleadosActivos();
        return this.listaEmpleados;
    }

    public List<Depositos> listarDepositos() {
        this.listaDepositos = depositosFacade.listarDepositosActivos();
        return this.listaDepositos;
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        Integer empleado = 0;
        String nomEmple = "";
        Integer deposito = 0;
        String descDepo = "";

        Empleados auxEmple = new Empleados();
        Depositos auxDepo = new Depositos();

        if (this.empleados == null) {
            empleado = 0;
            nomEmple = "TODOS";
        } else {
            empleado = Integer.parseInt(this.empleados.getEmpleadosPK().getCodEmpleado() + "");
            auxEmple = empleadosFacade.getNombreEmpleado(empleado);
            nomEmple = auxEmple.getXnombre();
        }

        if (this.depositos == null) {
            deposito = 0;
            descDepo = "TODOS";
        } else {
            deposito = Integer.parseInt(this.depositos.getDepositosPK().getCodDepo() + "");
            auxDepo = depositosFacade.getNombreDeposito(deposito);
            descDepo = auxDepo.getXdesc();
        }

        if (tipo.equals("VIST")) {
            rep.reporteLiConsDoc(dateToString(desde), dateToString(hasta), dateToString2(desde), dateToString2(hasta), empleado, "admin", tipo, nomEmple, descDepo, deposito);
        } else {

            List<Object[]> lista = new ArrayList<Object[]>();

            String[] columnas = new String[4];

            columnas[0] = "cod_zona";
            columnas[1] = "xdesc_zona";
            columnas[2] = "mestado";
            columnas[3] = "kfilas";

            String sql = "SELECT r.cod_zona, z.xdesc as xdesc_zona, p.mestado, ISNULL(COUNT(*),0) as kfilas\n"
                    + "	   FROM pedidos p, RUTAS R, clientes c, zonas z\n"
                    + "	   WHERE p.cod_cliente = c.cod_cliente\n"
                    + "	     AND c.cod_ruta = r.cod_ruta\n"
                    + "	     AND r.cod_zona = z.cod_zona\n"
                    + "	     AND p.fpedido BETWEEN '"+fdesde+"' AND '"+fhasta+"'\n"
                    + "		 AND (p.cod_depo = "+deposito+" or "+deposito+"=0)\n"
                    + "         AND (p.cod_vendedor = "+empleado+" or "+empleado+"=0)\n"
                    + "GROUP BY r.cod_zona, z.xdesc, p.mestado\n"
                    + "UNION ALL\n"
                    + "\n"
                    + "SELECT r.cod_zona, z.xdesc as xdesc_zona, 'S' as mestado, ISNULL(COUNT(*),0) as kfilas\n"
                    + "	   FROM pedidos p, RUTAS R, clientes c, zonas z\n"
                    + "	   WHERE p.cod_cliente = c.cod_cliente\n"
                    + "	     AND c.cod_ruta = r.cod_ruta\n"
                    + "	     AND r.cod_zona = z.cod_zona\n"
                    + "	     AND p.fpedido BETWEEN '"+fdesde+"' AND '"+fhasta+"'\n"
                    + "	     AND p.mestado = 'E'\n"
                    + "	     AND p.ffactur IS NULL\n"
                    + "	     AND (p.cod_depo = "+deposito+" or "+deposito+"=0)\n"
                    + "		AND (p.cod_vendedor = "+empleado+" or "+empleado+"=0)\n"
                    + "GROUP BY r.cod_zona, z.xdesc, p.mestado";

            lista = excelFacade.listarParaExcel(sql);

            rep.exportarExcel(columnas, lista, "locontrolped");
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

    //Getters & Setters
    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public List<Empleados> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
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

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public List<Depositos> getListaDepositos() {
        return listaDepositos;
    }

    public void setListaDepositos(List<Depositos> listaDepositos) {
        this.listaDepositos = listaDepositos;
    }

}
