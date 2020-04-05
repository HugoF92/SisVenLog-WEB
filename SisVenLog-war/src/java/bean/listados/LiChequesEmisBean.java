package bean.listados;

import dao.ExcelFacade;
import dao.ProveedoresFacade;
import dao.PersonalizedFacade;
import dao.BancosFacade;
import entidad.Proveedores;
import entidad.Bancos;
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
public class LiChequesEmisBean {

    @EJB
    private ProveedoresFacade proveedoresFacade;

    @EJB
    private BancosFacade BancosFacade;

    @EJB
    private PersonalizedFacade personalizedFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Bancos Bancos;
    private List<Bancos> listaBancos;

    private Proveedores proveedores;
    private List<Proveedores> listaProveedores;

    private Date chequeDesde;
    private Date chequeHasta;

    private Date emisionDesde;
    private Date emisionHasta;

    private Date cobroDesde;
    private Date cobroHasta;

    private String estado;

    private String discriminado;

    public LiChequesEmisBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.proveedores = new Proveedores();
        this.listaProveedores = new ArrayList<Proveedores>();

        this.Bancos = new Bancos();
        this.listaBancos = new ArrayList<Bancos>();
        
        setEstado("T");
        setDiscriminado("D");

        /*this.chequeDesde = new Date();
        this.chequeHasta = new Date();

        this.emisionDesde = new Date();
        this.emisionHasta = new Date();

        this.cobroDesde = new Date();
        this.cobroHasta = new Date();*/
    }

    public void ejecutar(String tipo) {

        try {

            String fdesde = "";
            String fhasta = "";
            String emidesde = "";
            String emihasta = "";
            String cobrodesde = "";
            String cobrohasta = "";

            LlamarReportes rep = new LlamarReportes();

            //String tipoDoc = "";
            String banco = "";
            String descbanco = "";
            String prov = "";
            String descProv = "";

            StringBuilder sql = new StringBuilder();

            if (this.Bancos == null) {
                banco = "0";
                descbanco = "TODOS";
            } else {
                banco = this.Bancos.getCodBanco() + "";
                descbanco = BancosFacade.find(this.Bancos.getCodBanco()).getXdesc();
            }

            if (this.proveedores == null) {
                prov = "0";
                descProv = "TODOS";
            } else {
                prov = this.proveedores.getCodProveed() + "";
                descProv = proveedoresFacade.find(proveedores.getCodProveed()).getXnombre();
            }

            sql.append(" SELECT     c.*, b.cod_banco AS cod_banco2, b.xdesc AS xdesc_banco, \n"
                    + "	 b.xdesc AS xdesc2_banco, t.xnombre, r.nrecibo, R.NRECIBO, R.IPAGADO \n"
                    + "	 FROM         cheques_emitidos c INNER JOIN \n"
                    + "                      bancos b ON c.cod_banco = b.cod_banco INNER JOIN \n"
                    + "                      proveedores t ON c.cod_proveed = t.cod_proveed LEFT OUTER JOIN \n"
                    + "                      recibos_prov_cheques r ON c.nro_cheque = r.nro_cheque AND c.cod_banco = r.cod_banco AND r.cod_empr = 2 \n"
                    + "              WHERE c.cod_empr = 2 \n"
                    + "             \n");
            if (chequeDesde != null && chequeHasta != null) {

                fdesde = dateToString(chequeDesde);
                fhasta = dateToString(chequeHasta);

                sql.append("AND  (c.fcheque BETWEEN '" + fdesde + "' AND '" + fhasta + "') \n");
            }

            if (emisionDesde != null && emisionHasta != null) {

                emidesde = dateToString(emisionDesde);
                emihasta = dateToString(emisionHasta);

                sql.append("AND  (c.femision BETWEEN '" + emidesde + "' AND '" + emihasta + "') \n");
            }

            if (cobroDesde != null && cobroHasta != null) {

                cobrodesde = dateToString(cobroDesde);
                cobrohasta = dateToString(cobroHasta);
                sql.append("AND  (c.fcobro BETWEEN '" + cobrodesde + "' AND '" + cobrohasta + "') \n");
            }

            if (estado.equals("S")) {
                sql.append(" AND c.fcobro IS NOT NULL \n");
            } else if (estado.equals("N")) {
                sql.append(" AND c.fcobro IS NULL \n");
            }

            sql.append("AND (c.cod_banco = " + banco + "  or " + banco + " = 0 )\n");
            sql.append("AND (c.cod_proveed = " + prov + " or " + prov + " = 0 ) \n");

            if (discriminado.equals("N")) {
                sql.append("ORDER BY c.nro_cheque, c.fcheque \n");
            } else {
                sql.append("ORDER BY c.cod_banco, c.nro_cheque, c.fcheque \n");
            }

            System.out.println("SQL lichequeemis: " + sql.toString());

            if (tipo.equals("VIST")) {
                rep.reporteLiCheqEmis(sql.toString(), fdesde, fhasta
                        ,emidesde, emihasta
                        ,cobrodesde, cobrohasta
                        ,prov, banco,"admin", tipo);
            } else if (tipo.equals("ARCH")) {
                List<Object[]> lista = new ArrayList<Object[]>();

                String[] columnas = new String[6];

                columnas[0] = " ";
                columnas[1] = " ";
                columnas[2] = " ";
                columnas[3] = " ";
                columnas[4] = " ";
                columnas[5] = " ";

                lista = excelFacade.listarParaExcel(sql.toString());

                rep.exportarExcel(columnas, lista, "liaplica");
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
    public Bancos getBancos() {
        return Bancos;
    }

    public void setBancos(Bancos Bancos) {
        this.Bancos = Bancos;
    }

    public List<Bancos> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Bancos> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public Date getChequeDesde() {
        return chequeDesde;
    }

    public void setChequeDesde(Date chequeDesde) {
        this.chequeDesde = chequeDesde;
    }

    public Date getChequeHasta() {
        return chequeHasta;
    }

    public void setChequeHasta(Date chequeHasta) {
        this.chequeHasta = chequeHasta;
    }

    public Date getEmisionDesde() {
        return emisionDesde;
    }

    public void setEmisionDesde(Date emisionDesde) {
        this.emisionDesde = emisionDesde;
    }

    public Date getEmisionHasta() {
        return emisionHasta;
    }

    public void setEmisionHasta(Date emisionHasta) {
        this.emisionHasta = emisionHasta;
    }

    public Date getCobroDesde() {
        return cobroDesde;
    }

    public void setCobroDesde(Date cobroDesde) {
        this.cobroDesde = cobroDesde;
    }

    public Date getCobroHasta() {
        return cobroHasta;
    }

    public void setCobroHasta(Date cobroHasta) {
        this.cobroHasta = cobroHasta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDiscriminado() {
        return discriminado;
    }

    public void setDiscriminado(String discriminado) {
        this.discriminado = discriminado;
    }

}
