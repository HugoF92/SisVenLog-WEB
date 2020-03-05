package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
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
public class LiTotRetenBean {

    private Date desde;
    private Date hasta;

    @EJB
    private ExcelFacade excelFacade;

    public LiTotRetenBean() throws IOException {

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
            rep.reporteTotReten(dateToString(desde), dateToString(hasta), dateToString2(desde), dateToString2(hasta), "admin", tipo);
        } else {

            List<Object[]> lista = new ArrayList<Object[]>();

            String[] columnas = new String[7];

            columnas[0] = "nrecibo";
            columnas[1] = "cod_cliente";
            columnas[2] = "xnombre";
            columnas[3] = "irecibo";
            columnas[4] = "frecibo";
            columnas[5] = "iretencion";
            columnas[6] = "mtipo";

            String sql = "SELECT  r.nrecibo, r.cod_cliente, c.xnombre, r.irecibo, r.frecibo, r.iretencion, 'V' as mtipo\n"
                    + "    	 FROM recibos  r, clientes c\n"
                    + "	 WHERE r.cod_empr = 2\n"
                    + "	 AND r.cod_cliente = c.cod_cliente\n"
                    + "	 AND r.iretencion > 0\n"
                    + "	 AND r.mestado = 'A'\n"
                    + "	 AND r.frecibo BETWEEN  '" + fdesde + "' AND '" + fhasta + "'\n"
                    + " UNION ALL\n"
                    + "SELECT r2.nrecibo, r2.cod_proveed as cod_cliente, p.xnombre, r2.irecibo, r2.frecibo, r2.iretencion, 'C' as mtipo\n"
                    + "    	 FROM recibos_prov  r2, proveedores p\n"
                    + "	 WHERE r2.cod_empr = 2\n"
                    + "	 AND r2.cod_proveed = p.cod_proveed\n"
                    + "	 AND r2.iretencion > 0\n"
                    + "	 AND r2.frecibo BETWEEN  '" + fdesde + "' AND '" + fhasta + "'\n"
                    + "	 AND r2.mestado = 'A'\n"
                    + "ORDER BY  7, 1";

            lista = excelFacade.listarParaExcel(sql);

            rep.exportarExcel(columnas, lista, "litotreten");
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
