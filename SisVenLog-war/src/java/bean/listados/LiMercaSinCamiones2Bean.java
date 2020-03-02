package bean.listados;

import dao.DepositosFacade;
import dao.ExcelFacade;
import dao.MercaderiasFacade;
import dao.PersonalizedFacade;
import dao.TiposDocumentosFacade;
import dao.ZonasFacade;
import dto.LiMercaSinDto;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Depositos;
import entidad.DepositosPK;
import entidad.Zonas;
import entidad.ZonasPK;
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
import org.primefaces.model.DualListModel;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class LiMercaSinCamiones2Bean {

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private PersonalizedFacade personalizedFacade;

    @EJB
    private ExcelFacade excelFacade;

    @EJB
    private ZonasFacade zonasFacade;

    @EJB
    private DepositosFacade depositosFacade;

    /*private TiposDocumentos tiposDocumentos;
    private List<TiposDocumentos> listaTiposDocumentos;*/
    private List<Mercaderias> listaMercaderias;
    private List<Mercaderias> listaMercaderiasSeleccionadas;

    private DualListModel<Mercaderias> mercaderias;

    private Depositos depositos;
    private List<Depositos> listaDepositos;

    private Zonas zonas;
    private List<Zonas> listaZonas;

    private Date desde;
    private Date hasta;

    private String operacion;
    private String tipoDocumento;

    public LiMercaSinCamiones2Bean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        /*this.tiposDocumentos = new TiposDocumentos();
        this.listaTiposDocumentos = new ArrayList<TiposDocumentos>();*/
        this.listaMercaderias = new ArrayList<Mercaderias>();
        this.listaMercaderiasSeleccionadas = new ArrayList<Mercaderias>();

        this.depositos = new Depositos(new DepositosPK());
        this.listaDepositos = new ArrayList<Depositos>();
        this.zonas = new Zonas(new ZonasPK());
        this.listaZonas = new ArrayList<Zonas>();

        this.desde = new Date();
        this.hasta = new Date();

        setOperacion("M");
        setTipoDocumento("TODOS");

        //Cities
        List<Mercaderias> mercaSource = new ArrayList<Mercaderias>();
        List<Mercaderias> mercaTarget = new ArrayList<Mercaderias>();

        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();

        mercaderias = new DualListModel<Mercaderias>(mercaSource, mercaTarget);
    }

    /*public List<TiposDocumentos> listarTiposDocumentosAnudoc() {
        this.listaTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoLiAnudoc();
        return this.listaTiposDocumentos;
    }*/
    public void ejecutar(String tipo) {

        personalizedFacade.ejecutarSentenciaSQL("drop table tmp_mercaderias");
        personalizedFacade.ejecutarSentenciaSQL("CREATE  TABLE  tmp_mercaderias (cod_merca CHAR(13), cod_barra CHAR(20), \n"
                + "xdesc CHAR(50), nrelacion SMALLINT, cant_cajas integer, cant_unid integer )");

        if (mercaderias.getTarget().size() > 0) {
            listaMercaderiasSeleccionadas = mercaderias.getTarget();

            for (int i = 0; i < listaMercaderiasSeleccionadas.size(); i++) {

                MercaderiasPK pk = listaMercaderiasSeleccionadas.get(i).getMercaderiasPK();

                Mercaderias aux = new Mercaderias();
                aux = mercaderiasFacade.find(pk);
                personalizedFacade.ejecutarSentenciaSQL("INSERT INTO tmp_mercaderias (cod_merca, cod_barra, xdesc, nrelacion,cant_cajas, cant_unid )\n"
                        + "								VALUES ('" + aux.getMercaderiasPK().getCodMerca() + "', '" + aux.getCodBarra() + "', '" + aux.getXdesc() + "', " + aux.getNrelacion() + ",0,0 )");
            }
        } else {
            personalizedFacade.ejecutarSentenciaSQL("insert into tmp_mercaderias\n"
                    + "select m.cod_merca, m.cod_barra, m.xdesc, m.nrelacion, 1, 1\n"
                    + "from  mercaderias m, existencias e\n"
                    + "where m.cod_merca = e.cod_merca\n"
                    + "and m.mestado = 'A'\n"
                    + "and e.cod_depo = 1");
        }

        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        //String tipoDoc = "";
        String depo = "";
        String zona = "";
        String desczona = "";
        String descdepo = "";

        StringBuilder sql = new StringBuilder();

        /*if (this.tiposDocumentos == null) {
            tipoDoc = "TODOS";
        } else {
            tipoDoc = this.tiposDocumentos.getCtipoDocum();
        }*/
        if (this.zonas == null) {
            zona = "TODOS";
            desczona = "TODOS";
        } else {
            zona = this.zonas.getZonasPK().getCodZona();
            desczona = zonasFacade.find(this.zonas.getZonasPK()).getXdesc();
        }

        if (this.depositos == null) {
            depo = 0+"";
            descdepo = "TODOS";
        } else {
            depo = this.depositos.getDepositosPK().getCodDepo() + "";
            descdepo =depositosFacade.find(this.depositos.getDepositosPK()).getXdesc();
        }

        sql.append("SELECT t.cod_merca, t.xdesc, d.cod_depo, t.cant_cajas, t.cant_unid, d.xdesc as xdesc_depo \n"
                + " FROM  tmp_mercaderias t,  depositos d \n"
                + " WHERE (d.cod_depo= "+depo+" or "+depo+"=0) \n"
                + " AND t.cod_merca IN (SELECT  DISTINCT M.cod_merca \n"
                + " FROM merca_canales m, canales_vendedores v, empleados em, depositos d2 \n"
                + " WHERE m.cod_canal = v.cod_canal \n"
                + " AND v.cod_vendedor = em.cod_empleado \n"
                + " AND em.ctipo_emp LIKE 'V%' \n"
                + " AND em.mestado = 'A' \n"
                + " AND em.cod_depo = d2.cod_depo  \n");

        if (this.zonas != null) {
            sql.append("AND d2.cod_zona = '" + zona + "' \n");
        }
        
        sql.append(") \n");

        sql.append(" AND (t.cant_cajas > 0 OR t.cant_unid > 0 ) AND NOT EXISTS ( SELECT *  \n"
                + " FROM envios mo, envios_det det, depositos dep \n"
                + " WHERE  mo.cod_empr = 2 and mo.fenvio BETWEEN '" + fdesde + "' AND '" + fhasta + "'  \n"
                + " AND  mo.mestado = 'A' AND   mo.depo_origen = " + depo + " AND mo.nro_envio = det.nro_envio \n"
                + " AND det.cod_empr = 2 AND t.cod_merca = det.cod_merca \n"
                + " AND mo.depo_destino = dep.cod_depo \n");

        if (this.zonas != null) {
            sql.append(" AND dep.cod_zona = '" + zona + "' \n");
        }

        sql.append("   ) \n");
        sql.append("ORDER BY t.cod_merca, t.xdesc, d.cod_depo \n");

        System.out.println("SQL limercasincamiones2: " + sql.toString());

        if (tipo.equals("VIST")) {
            rep.reporteLiMercaSin3(sql.toString(), dateToString2(desde), dateToString2(hasta), desczona, "admin", tipo);
        } else if (tipo.equals("ARCH")) {
            List<LiMercaSinDto> auxExcel = new ArrayList<LiMercaSinDto>();

            auxExcel = excelFacade.listarLiMercaSin(sql.toString());

            rep.excelLiMercaSin(auxExcel);
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
    /*public TiposDocumentos getTiposDocumentos() {
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
    }*/
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

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public TiposDocumentosFacade getTiposDocumentosFacade() {
        return tiposDocumentosFacade;
    }

    public void setTiposDocumentosFacade(TiposDocumentosFacade tiposDocumentosFacade) {
        this.tiposDocumentosFacade = tiposDocumentosFacade;
    }

    public List<Mercaderias> getListaMercaderias() {
        return listaMercaderias;
    }

    public void setListaMercaderias(List<Mercaderias> listaMercaderias) {
        this.listaMercaderias = listaMercaderias;
    }

    public List<Mercaderias> getListaMercaderiasSeleccionadas() {
        return listaMercaderiasSeleccionadas;
    }

    public void setListaMercaderiasSeleccionadas(List<Mercaderias> listaMercaderiasSeleccionadas) {
        this.listaMercaderiasSeleccionadas = listaMercaderiasSeleccionadas;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    public List<Zonas> getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(List<Zonas> listaZonas) {
        this.listaZonas = listaZonas;
    }

}
