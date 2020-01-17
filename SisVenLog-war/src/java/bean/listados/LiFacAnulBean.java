package bean.listados;

import dao.ExcelFacade;
import dao.PromocionesFacade;
import dao.EmpleadosFacade;
import entidad.Promociones;
import entidad.PromocionesPK;
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
public class LiFacAnulBean {

    @EJB
    private EmpleadosFacade empleadosFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Empleados empleados;
    private List<Empleados> listaEmpleados;

    private Date desde;
    private Date hasta;

    public LiFacAnulBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.empleados = new Empleados(new EmpleadosPK());
        this.listaEmpleados = new ArrayList<Empleados>();

        this.desde = new Date();
        this.hasta = new Date();
    }

    public List<Empleados> listarEmpleados() {
        this.listaEmpleados = empleadosFacade.listarEntregador();
        return this.listaEmpleados;
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String codEntregador = "";
        String nomEntregador = "";

        if (this.empleados == null) {
            codEntregador = "0";
            nomEntregador = "TODOS";
        } else {
            codEntregador = this.empleados.getEmpleadosPK().getCodEmpleado() + "";
            nomEntregador = empleadosFacade.find(this.empleados.getEmpleadosPK()).getXnombre();
        }

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT f.cod_entregador, e.xnombre, f.cmotivo, m.xdesc,  \n"
                + " f.nrofact, f.ctipo_docum, f.xrazon_social, f.ffactur, f.fanul, f.ttotal, f.cod_zona, z.xdesc as xzona \n"
                + " FROM facturas f LEFT OUTER JOIN motivos m ON f.cmotivo = m.cmotivo, empleados e , zonas z \n"
                + "	 WHERE f.cod_empr = 2 and f.cod_entregador = e.cod_empleado  \n"
                + "	   AND f.mestado = 'X' \n"
                + "	   AND f.cod_zona = z.cod_zona \n"
                + "	   AND f.ffactur BETWEEN '" + fdesde + "' AND '" + fhasta + "' \n"
                + " AND (f.cod_entregador = " + codEntregador + " or " + codEntregador + " = 0) \n"
                + " ORDER BY f.cod_entregador, f.cmotivo");

        if (tipo.equals("VIST")) {
            rep.reporteLiFactAnul(sql.toString(), dateToString2(desde),
                    dateToString2(hasta), nomEntregador, "admin", tipo);
        } else {

            List<Object[]> lista = new ArrayList<Object[]>();

            String[] columnas = new String[11];

            columnas[0] = "cod_entregador";
            columnas[1] = "xnombre";
            columnas[2] = "cmotivo";
            columnas[3] = "xdesc";
            columnas[4] = "nrofact";
            columnas[5] = "ctipo_docum";
            columnas[6] = "xrazon_social";
            columnas[7] = "ffactur";
            columnas[8] = "ttotal";
            columnas[9] = "cod_zona";
            columnas[10] = "xzona";

            lista = excelFacade.listarParaExcel(sql.toString());

            rep.exportarExcel(columnas, lista, "lifactanul");
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

}
