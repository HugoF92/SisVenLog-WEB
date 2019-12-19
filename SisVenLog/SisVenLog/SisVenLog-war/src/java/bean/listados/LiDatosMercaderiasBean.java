package bean.listados;

import dao.DivisionesFacade;
import dao.ExcelFacade;
import dao.MercaderiasFacade;
import dao.PersonalizedFacade;
import dao.SublineasFacade;
import entidad.Divisiones;
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
public class LiDatosMercaderiasBean {

    @EJB
    private MercaderiasFacade mercaderiasFacade;

    @EJB
    private SublineasFacade sublineasFacade;

    @EJB
    private DivisionesFacade divisionesFacade;

    @EJB
    private PersonalizedFacade personalizedFacade;

    @EJB
    private ExcelFacade excelFacade;

    private Sublineas sublineas;
    private Divisiones divisiones;

    private List<Sublineas> listaSublineas = new ArrayList<Sublineas>();
    private List<Divisiones> listaDivisiones = new ArrayList<Divisiones>();

    String estado = "";

    /*private List<Mercaderias> listaMercaderias;
    private List<Mercaderias> listaMercaderiasSeleccionadas;

    private DualListModel<Mercaderias> mercaderias;*/
    public LiDatosMercaderiasBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.sublineas = new Sublineas();
        this.divisiones = new Divisiones();

        estado = "";

        //Cities
        /*List<Mercaderias> mercaSource = new ArrayList<Mercaderias>();
        List<Mercaderias> mercaTarget = new ArrayList<Mercaderias>();

        mercaSource = mercaderiasFacade.listarMercaderiasActivasDepo1();

        mercaderias = new DualListModel<Mercaderias>(mercaSource, mercaTarget);*/
    }

    public List<Sublineas> listarSublineas() {
        this.listaSublineas = sublineasFacade.listarSublineasConMercaderias();
        return this.listaSublineas;
    }

    public List<Divisiones> listarDivisiones() {
        this.listaDivisiones = divisionesFacade.listarSubdiviciones();
        return this.listaDivisiones;
    }

    public void ejecutar(String tipo) {

        /* personalizedFacade.ejecutarSentenciaSQL("drop table tmp_mercaderias");
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
         */
        Integer codSublinea = 0;
        String descSublinea = "";

        Integer codDivision = 0;
        String descDivision = "";

        if (sublineas == null) {
            codSublinea = 0;
            descSublinea = "TODOS";
        } else {
            codSublinea = Integer.parseInt(sublineas.getCodSublinea() + "");
            descSublinea = sublineasFacade.find(sublineas.getCodSublinea()).getXdesc();
        }

        if (divisiones == null) {
            codDivision = 0;
            descDivision = "TODOS";
        } else {
            codDivision = Integer.parseInt(divisiones.getCodDivision() + "");
            descDivision = divisionesFacade.find(divisiones.getCodDivision()).getXdesc();
        }

        estado = "TODOS";

        LlamarReportes rep = new LlamarReportes();

        if (tipo.equals("VIST")) {
            rep.reporteLiDatosMer(codSublinea, descSublinea, codDivision, descDivision, estado, "admin", tipo);
        } else {

            List<Object[]> lista = new ArrayList<Object[]>();

            String[] columnas = new String[12];

            columnas[0] = "cod_division";
            columnas[1] = "xdesc_division";
            columnas[2] = "cod_categoria";
            columnas[3] = "xdesc_categoria";
            columnas[4] = "cod_linea";
            columnas[5] = "xdesc_linea";
            columnas[6] = "cod_sublinea";
            columnas[7] = "xdesc_sublinea";
            columnas[8] = "cod_merca";
            columnas[9] = "xdesc";
            columnas[10] = "mestado";
            columnas[11] = "mexiste";

            String sql = "SELECT d.cod_division, d.xdesc as xdesc_division, g.cod_categoria,\n"
                    + "g.xdesc as xdesc_categoria, l.cod_linea, l.xdesc as xdesc_linea,\n"
                    + "s.cod_sublinea, s.xdesc as xdesc_sublinea, m.cod_merca, m.xdesc, m.mestado, m.mexiste\n"
                    + "FROM mercaderias m, sublineas s, lineas l, categorias g, divisiones d\n"
                    + "WHERE m.cod_sublinea = s.cod_sublinea\n"
                    + "AND s.cod_linea = l.cod_linea\n"
                    + "AND l.cod_categoria = g.cod_categoria\n"
                    + "AND g.cod_division = d.cod_division\n"
                    + "AND (d.cod_division = "+codDivision+" or "+codDivision+"=0)\n"
                    + "AND (s.cod_sublinea = "+codSublinea+" or "+codSublinea+"=0)\n"
                    + "AND (m.mestado =  $P{estado} or $P{estado} = 'TODOS')\n"
                    + "ORDER BY d.cod_division, g.cod_categoria, l.cod_linea, s.cod_sublinea, m.cod_merca";

            lista = excelFacade.listarParaExcel(sql);

            rep.exportarExcel(columnas, lista, "lidatosmerca");
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
    public Sublineas getSublineas() {
        return sublineas;
    }

    public void setSublineas(Sublineas sublineas) {
        this.sublineas = sublineas;
    }

    public Divisiones getDivisiones() {
        return divisiones;
    }

    public void setDivisiones(Divisiones divisiones) {
        this.divisiones = divisiones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Sublineas> getListaSublineas() {
        return listaSublineas;
    }

    public void setListaSublineas(List<Sublineas> listaSublineas) {
        this.listaSublineas = listaSublineas;
    }

    public List<Divisiones> getListaDivisiones() {
        return listaDivisiones;
    }

    public void setListaDivisiones(List<Divisiones> listaDivisiones) {
        this.listaDivisiones = listaDivisiones;
    }

    /*public List<Mercaderias> getListaMercaderias() {
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
    }*/
}
