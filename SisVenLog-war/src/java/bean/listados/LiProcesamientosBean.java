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
public class LiProcesamientosBean {

    @EJB
    private ExcelFacade excelFacade;

    private Date desde;
    private Date hasta;

    public LiProcesamientosBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.desde = new Date();
        this.hasta = new Date();
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);

        if (tipo.equals("VIST")) {
            rep.reporteLiProcesamientos(dateToString(desde), dateToString(hasta), dateToString2(desde), dateToString2(hasta), "admin", tipo);
        } else {

            List<Object[]> lista = new ArrayList<Object[]>();

            String[] columnas = new String[10];

            columnas[0] = "cod_aplicacion";
            columnas[1] = "fec_inicial";
            columnas[2] = "fec_final";
            columnas[3] = "cod_usuario";
            columnas[4] = "fec_alta";
            columnas[5] = "cod_usuario";
            columnas[6] = "dato_1";
            columnas[7] = "dato_2";
            columnas[8] = "dato_3";
            columnas[9] = "descripcion";

            String sql = "SELECT p.*, a.xdesc FROM procesamientos  p, aplicaciones a\n"
                    + "  WHERE p.falta BETWEEN '" + fdesde + "' AND '" + fhasta + "'\n"
                    + "   AND p.cod_aplicacion = a.cod_aplicacion\n"
                    + " ORDER BY p.falta";

            lista = excelFacade.listarParaExcel(sql);

            rep.exportarExcel(columnas, lista, "liprocesamientos");
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
