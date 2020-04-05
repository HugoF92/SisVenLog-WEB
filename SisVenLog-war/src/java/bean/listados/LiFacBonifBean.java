package bean.listados;

import dao.ExcelFacade;
import dao.PromocionesFacade;
import dao.TiposDocumentosFacade;
import entidad.Promociones;
import entidad.PromocionesPK;
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
public class LiFacBonifBean {

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private ExcelFacade excelFacade;

    private TiposDocumentos tiposDocumentos;
    private List<TiposDocumentos> listaTiposDocumentos;

    @EJB
    private PromocionesFacade promocionesFacade;

    private Promociones promociones;
    private List<Promociones> listaPromociones;

    private Date desde;
    private Date hasta;

    private String operacion;

    public LiFacBonifBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.tiposDocumentos = new TiposDocumentos();
        this.listaTiposDocumentos = new ArrayList<TiposDocumentos>();

        this.promociones = new Promociones(new PromocionesPK());
        this.listaPromociones = new ArrayList<Promociones>();

        this.desde = new Date();
        this.hasta = new Date();

        setOperacion("A");
    }

    public List<TiposDocumentos> listarTipoDocumentoLiFacBonif() {
        this.listaTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoLiFacBonif();
        return this.listaTiposDocumentos;
    }

    public void listarPromociones() {
        this.listaPromociones = promocionesFacade.listarPromocionesLiFacBonif(dateToString(desde), dateToString(hasta));

    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String tipoDoc = "";
        String promoDesc = "";
        Integer tipoDocumento = 0;
        Integer nroPromo = 0;

        if (this.tiposDocumentos == null) {
            tipoDoc = "TODOS";
        } else {
            tipoDoc = this.tiposDocumentos.getCtipoDocum();
        }

        if (this.promociones == null) {
            nroPromo = 0;

            promoDesc = "TODOS";
        } else {
            nroPromo = Integer.parseInt(this.promociones.getPromocionesPK().getNroPromo() + "");
            promoDesc = this.promociones.getXdesc();
        }

        if (tipo.equals("VIST")) {
            rep.reporteLiFacBonif(nroPromo, tipoDoc, fdesde, fhasta, dateToString2(desde),
                    dateToString2(hasta), tipoDoc, promoDesc, "admin", tipo);
        } else {

            List<Object[]> lista = new ArrayList<Object[]>();

            String[] columnas = new String[13];

            columnas[0] = "nrofact";
            columnas[1] = "ffactur";
            columnas[2] = "ctipo_docum";
            columnas[3] = "cod_cliente";
            columnas[4] = "xrazon_social";
            columnas[5] = "cod_vendedor";
            columnas[6] = "cod_merca";
            columnas[7] = "xdesc";
            columnas[8] = "cajas_bonif";
            columnas[9] = "unid_bonif";
            columnas[10] = "xnombre";
            columnas[11] = "nro_promo";
            columnas[12] = "xpromo";

            String sql = "SELECT  f.nrofact, f.ffactur, f.ctipo_docum, f.cod_cliente, f.xrazon_social, f.cod_vendedor,\n"
                    + " d.cod_merca, m.xdesc, d.cajas_bonif, d.unid_bonif, v.xnombre, d.nro_promo, p.xdesc as xpromo\n"
                    + " FROM facturas f, facturas_det d, mercaderias m, tmp_mercaderias t, empleados v, promociones p\n"
                    + " WHERE     (f.cod_empr = 2)\n"
                    + " AND v.cod_empleado = f.cod_vendedor\n"
                    + " AND (f.mestado = 'A')\n"
                    + " AND f.cod_empr = 2\n"
                    + " AND p.nro_promo= d.nro_promo\n"
                    + " AND d.cod_empr  = 2\n"
                    + " AND (f.ffactur BETWEEN '"+fdesde+"' AND  '"+fhasta+"')\n"
                    + " AND f.nrofact = d.nrofact\n"
                    + " AND f.ffactur = d.ffactur\n"
                    + " AND d.cod_merca = t.cod_merca\n"
                    + " AND (d.cajas_bonif > 0 OR d.unid_bonif > 0)\n"
                    + " AND f.ctipo_docum = d.ctipo_docum\n"
                    + " AND f.cod_empr = d.cod_empr\n"
                    + " AND d.cod_merca = m.cod_merca\n"
                    + "AND (d.nro_promo  =  "+nroPromo+" or "+nroPromo+" =0)\n"
                    + "AND (f.ctipo_docum  = '"+tipoDoc+"' OR '"+tipoDoc+"' = 'TODOS')\n"
                    + "ORDER BY d.nro_promo, f.cod_vendedor, f.nrofact";

            lista = excelFacade.listarParaExcel(sql);

            rep.exportarExcel(columnas, lista, "lifactbonif");
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
    public TiposDocumentos getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(TiposDocumentos tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public List<TiposDocumentos> getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public void setListaTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
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

    public Promociones getPromociones() {
        return promociones;
    }

    public void setPromociones(Promociones promociones) {
        this.promociones = promociones;
    }

    public List<Promociones> getListaPromociones() {
        return listaPromociones;
    }

    public void setListaPromociones(List<Promociones> listaPromociones) {
        this.listaPromociones = listaPromociones;
    }

}
