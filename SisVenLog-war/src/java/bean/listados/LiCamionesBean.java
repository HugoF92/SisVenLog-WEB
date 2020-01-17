package bean.listados;

import dao.ExcelFacade;
import dao.MercaderiasFacade;
import dao.PersonalizedFacade;
import dao.TiposDocumentosFacade;
import dao.DepositosFacade;
import dto.LiMercaSinDto;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Depositos;
import entidad.DepositosPK;
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
public class LiCamionesBean {

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private DepositosFacade depositosFacade;

    @EJB
    private PersonalizedFacade personalizedFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Depositos depositos;
    private List<Depositos> listaDepositos;

    private Date desde;
    private Date hasta;

    private List<Mercaderias> listaMercaderias;
    private List<Mercaderias> listaMercaderiasSeleccionadas;

    private DualListModel<Mercaderias> mercaderias;

    public LiCamionesBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.depositos = new Depositos(new DepositosPK());
        this.listaDepositos = new ArrayList<Depositos>();

        this.desde = new Date();
        this.hasta = new Date();

        //Cities
        List<Mercaderias> mercaSource = new ArrayList<Mercaderias>();
        List<Mercaderias> mercaTarget = new ArrayList<Mercaderias>();

        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();

        mercaderias = new DualListModel<Mercaderias>(mercaSource, mercaTarget);

    }

    public void ejecutar(String tipo) {

        try {

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
            String descDepo = "";
            String descTipoDoc = "";

            StringBuilder sql = new StringBuilder();

            if (this.depositos == null) {
                depo = "0";
                descDepo = "TODOS";
            } else {
                depo = this.depositos.getDepositosPK().getCodDepo() + "";
                descDepo = depositosFacade.find(this.depositos.getDepositosPK()).getXdesc();
            }

            /*if (this.depositos != null) {
                personalizedFacade.ejecutarSentenciaSQL("SELECT a.cod_merca, a.fmovim, a.cod_depo, a.cant_cajas as cajas_ini, a.cant_unid as unid_ini  \n"
                        + "            INTO tmp_saldos FROM existencias_saldos a  \n"
                        + "			 WHERE a.cod_empr = 2 \n"
                        + "			 AND a.cod_depo = " + depo + "\n"
                        + "				AND fmovim = (SELECT MAX(fmovim) \n"
                        + "						FROM existencias_saldos b\n"
                        + "			 WHERE a.cod_empr = b.cod_empr \n"
                        + "				   AND a.cod_merca = b.cod_merca \n"
                        + "				   AND a.cod_depo = b.cod_depo \n"
                        + "			AND fmovim <= '" + fhasta + "') ");
                
            }*/
            sql.append("SELECT E.cod_merca, m.xdesc, m.cod_barra, m.nrelacion, \n"
                    + "		 e.cant_cajas, e.cant_unid, e.reser_cajas, e.reser_unid, m.mestado \n"
                    + "	 FROM existencias e, mercaderias m\n"
                    + "	 WHERE e.cod_empr = 2 \n"
                    + "	 AND (cod_depo = "+depo+" or "+depo+" = 0)\n"
                    + "	AND e.cod_empr = m.cod_empr \n"
                    + "	AND (e.cant_cajas <> 0 OR e.cant_unid <> 0) \n"
                    + "	 AND   e.cod_merca = m.cod_merca \n"
                    + "	 ORDER BY e.cod_merca ");

            System.out.println("SQL limercasin: " + sql.toString());

            if (tipo.equals("VIST")) {
                rep.reporteLiCamiones(sql.toString(), dateToString(desde), dateToString(hasta), "admin", tipo);
            } else if (tipo.equals("ARCH")) {
                List<Object[]> auxExcel = new ArrayList<Object[]>();

                auxExcel = excelFacade.listarParaExcel(sql.toString());

                rep.excelLiCamiones(auxExcel);
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", e.getMessage()));
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

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }

}
