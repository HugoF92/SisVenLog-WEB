package bean.listados;

import dao.ExcelFacade;
import dao.LineasFacade;
import dao.SublineasFacade;
import dto.LiMercaSinDto;
import entidad.Lineas;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Sublineas;
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
public class LiVtaRetornoBean {

    @EJB
    private LineasFacade lineasFacade;

    @EJB
    private SublineasFacade sublineasFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Lineas lineas;
    private List<Lineas> listaLineas;
    private DualListModel<Mercaderias> mercaderias;

    private Sublineas sublineas;
    private List<Sublineas> listaSublineas;

    private Date desde;
    private Date hasta;

    public LiVtaRetornoBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.lineas = new Lineas();
        this.listaLineas = new ArrayList<Lineas>();

        this.sublineas = new Sublineas();
        this.listaSublineas = new ArrayList<Sublineas>();

        this.desde = new Date();
        this.hasta = new Date();

    }

    public void ejecutar(String tipo) {

        try {
            LlamarReportes rep = new LlamarReportes();

            String fdesde = dateToString(desde);
            String fhasta = dateToString(hasta);
            //String tipoDoc = "";
            Short linea = new Short("0");
            Short sublinea = new Short("0");
            String descLinea = "";
            String descSublinea = "";

            StringBuilder sql = new StringBuilder();

            if (this.lineas == null) {
                linea = new Short("0");
                descLinea = "TODOS";
            } else {
                linea = this.lineas.getCodLinea();
                descLinea = lineasFacade.find(this.lineas.getCodLinea()).getXdesc();
            }

            if (this.sublineas == null) {
                sublinea = new Short("0");
                descSublinea = "TODOS";
            } else {
                sublinea = this.sublineas.getCodSublinea();
                descSublinea = sublineasFacade.find(this.sublineas.getCodSublinea()).getXdesc();
            }

            sql.append(" SELECT  d.cod_merca, m.xdesc as xdesc_merca, s.cod_sublinea, s.xdesc as xdesc_sublinea, m.nrelacion, f.ctipo_vta, \n"
                    + " SUM(d.cant_cajas) * -1 as cant_cajas, SUM(d.cant_unid) * -1 as cant_unid,    \n"
                    + "            	 l.cod_linea, l.xdesc as xdesc_linea , r.idevol_caja, r.idevol_unidad, r.iretorno_caja, r.iretorno_unidad, \n"
                    + "            	 r.frige_desde, r.frige_hasta \n"
                    + "    	 FROM movimientos_merca  d, sublineas s, mercaderias m,  lineas l, facturas f, retornos_precios r \n"
                    + "	 WHERE d.cod_empr = 2 \n"
                    + "	 AND s.cod_linea = l.cod_linea \n"
                    + "	 AND d.cod_empr = m.cod_empr \n"
                    + "	 AND d.cod_merca = m.cod_merca \n"
                    + "	 AND d.cod_merca = r.cod_merca \n"
                    + "	 AND f.ctipo_vta = r.ctipo_vta \n"
                    + "	 AND d.fmovim BETWEEN r.frige_desde AND r.frige_hasta \n"
                    + "	 AND m.cod_sublinea = s.cod_sublinea \n"
                    + "	 AND d.fac_ctipo_docum = f.ctipo_docum \n"
                    + "	 AND d.nrofact = f.nrofact \n"
                    + "	 AND f.ffactur >= '01/01/2015' \n"
                    + "	 AND d.ctipo_docum IN ('FCO','FCR','CPV','NCV','NDV') \n"
                    + "	 AND d.fmovim BETWEEN  '" + fdesde + "' AND '" + fhasta + "' \n"
                    + "	 AND (m.cod_sublinea = " + sublinea + " or " + sublinea + " = 0) \n"
                    + "	 AND (s.cod_linea = " + linea + " or " + linea + " = 0) \n"
                    + " GROUP BY  s.cod_sublinea, s.xdesc, d.cod_merca, m.xdesc,M.NRELACION, l.cod_linea, l.xdesc, \n"
                    + " f.ctipo_vta, r.idevol_caja, r.idevol_unidad, r.iretorno_caja, r.iretorno_unidad, r.frige_desde, r.frige_hasta \n"
                    + " ORDER BY  l.cod_linea, s.cod_sublinea, d.cod_merca  ");

            System.out.println("======> SQL: " + sql.toString());

            if (tipo.equals("VIST")) {
                rep.reporteLiVtaRetorno(sql.toString(), dateToString2(desde), dateToString2(hasta), descLinea, "admin", descSublinea, tipo);
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
    public Sublineas getSublineas() {
        return sublineas;
    }

    public void setSublineas(Sublineas sublineas) {
        this.sublineas = sublineas;
    }

    public List<Sublineas> getListaSublineas() {
        return listaSublineas;
    }

    public void setListaSublineas(List<Sublineas> listaSublineas) {
        this.listaSublineas = listaSublineas;
    }

    public DualListModel<Mercaderias> getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(DualListModel<Mercaderias> mercaderias) {
        this.mercaderias = mercaderias;
    }

    public Lineas getLineas() {
        return lineas;
    }

    public void setLineas(Lineas lineas) {
        this.lineas = lineas;
    }

    public List<Lineas> getListaLineas() {
        return listaLineas;
    }

    public void setListaLineas(List<Lineas> listaLineas) {
        this.listaLineas = listaLineas;
    }

}
