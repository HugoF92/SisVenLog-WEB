package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.TiposDocumentosFacade;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.TiposDocumentos;
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
public class LiErroresProcBean {

    @EJB
    private EmpleadosFacade empleadosFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Empleados empleados;
    private List<Empleados> listaEmpleados;

    private Date desde;
    private Date hasta;

    private String operacion;

    public LiErroresProcBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.empleados = new Empleados(new EmpleadosPK());
        this.listaEmpleados = new ArrayList<Empleados>();

        this.desde = new Date();
        this.hasta = new Date();

        setOperacion("A");
    }

    public List<Empleados> listarEmpleados() {
        this.listaEmpleados = empleadosFacade.listarEmpleadosActivos();
        return this.listaEmpleados;
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        Integer empleado = 0;
        String nomEmple = "";

        if (this.empleados == null) {
            empleado = 0;
            nomEmple = "TODOS";

        } else {
            empleado = Integer.parseInt(this.empleados.getEmpleadosPK().getCodEmpleado() + "");
            Empleados auxEmple = new Empleados();
            auxEmple = empleadosFacade.getNombreEmpleado(empleado);
            nomEmple = auxEmple.getXnombre();
        }

        if (tipo.equals("VIST")) {
            rep.reporteLiErroresProc(dateToString(desde), dateToString(hasta), dateToString2(desde), dateToString2(hasta), empleado, "admin", tipo, nomEmple);
        } else {

            List<Object[]> lista = new ArrayList<Object[]>();

            String[] columnas = new String[7];

            columnas[0] = "fproc";
            columnas[1] = "cod_merca";
            columnas[2] = "xdesc_merca";
            columnas[3] = "nroped";
            columnas[4] = "cod_depo";
            columnas[5] = "xdesc_depo";
            columnas[6] = "xmensaje";

            String sql = "SELECT e.fproc, e.cod_merca, m.xdesc as xdesc_merca,\n"
                    + " e.nroped, e.cod_depo, d.xdesc as xdesc_depo, xmensaje\n"
                    + " FROM erroresproc e, pedidos p, depositos d, mercaderias m\n"
                    + " WHERE  fproc between  '"+fdesde+"' and '"+fhasta+"'\n"
                    + " AND e.nroped= p.nro_pedido\n"
                    + " AND e.cod_merca = m.cod_merca\n"
                    + " AND p.cod_empr = 2\n"
                    + " AND e.cod_depo = d.cod_depo\n"
                    + " AND (p.cod_vendedor = "+empleado+" or 0 = "+empleado+")\n"
                    + " ORDER BY e.fproc";

            lista = excelFacade.listarParaExcel(sql);

            rep.exportarExcel(columnas, lista, "lierroresproc");
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

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

}
