package bean;

import dao.PersonalizedFacade;
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

@ManagedBean
@SessionScoped
public class cambiarEntregadorBean {

    @EJB
    private PersonalizedFacade personalizedFacade;

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    private Date fechaFinal;
    private String operacion;
    private Empleados entregadorOrig = new Empleados();
    private Empleados entregadorNvo = new Empleados();
    private TiposDocumentos tiposDocumentos = new TiposDocumentos();
    private List<TiposDocumentos> listaTiposDocumentos = new ArrayList<TiposDocumentos>();

    public cambiarEntregadorBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        fechaFinal = new Date();
        operacion = "";
        entregadorOrig = new Empleados(new EmpleadosPK());
        entregadorNvo = new Empleados(new EmpleadosPK());
        tiposDocumentos = new TiposDocumentos();
        listaTiposDocumentos = new ArrayList<TiposDocumentos>();

    }

    public List<TiposDocumentos> listarTiposDocumentos() {
        listaTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoCambiarEntregador();
        return listaTiposDocumentos;
    }

    public void ejecutar() {

        String ffinal = "";

        if (this.fechaFinal == null) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ingrese la fecha del documento"));
            return;

        } else {
            ffinal = dateToString(fechaFinal);
        }

        if (this.entregadorOrig.getXnombre() == null) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ingrese el entregador original"));
            return;

        } else {
            entregadorOrig = entregadorOrig;
        }

        if (this.entregadorNvo.getXnombre() == null) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ingrese el nuevo entregador"));
            return;

        }

        StringBuilder sentenciaFacturas = new StringBuilder();
        StringBuilder sentenciaEnvios = new StringBuilder();
        StringBuilder sentenciaNotasVentas = new StringBuilder();
        StringBuilder sentenciaMoviMerca = new StringBuilder();

        /*FACTURAS*/
        sentenciaFacturas.append("UPDATE facturas \n"
                + "SET cod_entregador = " + entregadorNvo.getEmpleadosPK().getCodEmpleado() + " \n"
                + " WHERE cod_empr = 2 AND ffactur = '" + ffinal + "' \n"
                + "AND cod_entregador = " + entregadorOrig.getEmpleadosPK().getCodEmpleado() + " ");

        if (operacion.equals("D")) {

            if (tiposDocumentos.getCtipoDocum().equals("FCR")
                    || tiposDocumentos.getCtipoDocum().equals("FCO")
                    || tiposDocumentos.getCtipoDocum().equals("CPV")) {
                sentenciaFacturas.append("AND ctipo_docum = '" + tiposDocumentos.getCtipoDocum() + "' \n");
            }

        }

        personalizedFacade.ejecutarSentenciaSQL(sentenciaFacturas.toString());

        /*ENVIOS*/
        sentenciaEnvios.append(" UPDATE envios "
                + " SET cod_entregador = " + entregadorNvo.getEmpleadosPK().getCodEmpleado() + " "
                + " WHERE cod_empr = 2 AND fenvio = '" + ffinal + "' "
                + "  AND cod_entregador = " + entregadorOrig.getEmpleadosPK().getCodEmpleado() + " "
        );

        if (operacion.equals("D")) {

            if (tiposDocumentos.getCtipoDocum().equals("EN")) {
                sentenciaEnvios.append(" /*AND nro_envio =  */");
            }

        }

        personalizedFacade.ejecutarSentenciaSQL(sentenciaEnvios.toString());

        /*NOTAS VENTAS*/
        sentenciaNotasVentas.append(" UPDATE notas_ventas "
                + " SET cod_entregador = " + entregadorNvo.getEmpleadosPK().getCodEmpleado() + " "
                + " WHERE cod_empr = 2 AND fdocum = '" + ffinal + "' "
                + "  AND cod_entregador = " + entregadorOrig.getEmpleadosPK().getCodEmpleado() + " ");

        if (operacion.equals("D")) {

            if (tiposDocumentos.getCtipoDocum().equals("NCV")) {
                sentenciaNotasVentas.append("/* AND nro_nota = ?l_nro_nota */");
            }

        }

        personalizedFacade.ejecutarSentenciaSQL(sentenciaNotasVentas.toString());

        /*MOVIMIENTOS MERCADERIAS*/
        sentenciaMoviMerca.append(" UPDATE movimientos_merca "
                + " SET cod_entregador = " + entregadorNvo.getEmpleadosPK().getCodEmpleado() + " "
                + " WHERE cod_empr = 2 AND fmovim = '" + ffinal + "' "
                + "  AND cod_entregador = " + entregadorOrig.getEmpleadosPK().getCodEmpleado() + " ");

        if (operacion.equals("D")) {
            sentenciaMoviMerca.append("AND ctipo_docum IN ('FCR','FCO','CPV','NCV') ");

        } else {
            sentenciaMoviMerca.append("AND ctipo_docum = '" + tiposDocumentos.getCtipoDocum() + "'");
        }

        personalizedFacade.ejecutarSentenciaSQL(sentenciaMoviMerca.toString());

        /*if (operacion.equals("T")) {

        } else if (operacion.equals("D")) {

            if (tiposDocumentos.getCtipoDocum() == null) {

            } else {
                if (tiposDocumentos.getCtipoDocum().equals("")) {

                }
            }

        }*/
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Atencion", "Fin de Actualizacion de Datos"));
        return;


        /*rep.reporteLiProcesamientos(dateToString(desde), dateToString(hasta), dateToString2(desde), dateToString2(hasta), "admin", tipo);
         */
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

    //getter and setters
    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Empleados getEntregadorOrig() {
        return entregadorOrig;
    }

    public void setEntregadorOrig(Empleados entregadorOrig) {
        this.entregadorOrig = entregadorOrig;
    }

    public Empleados getEntregadorNvo() {
        return entregadorNvo;
    }

    public void setEntregadorNvo(Empleados entregadorNvo) {
        this.entregadorNvo = entregadorNvo;
    }

    public String getOperacion() {
        return operacion;
    }

    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public List<TiposDocumentos> getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

}
