package bean.listados;

import dao.ExcelFacade;
import dao.TiposDocumentosFacade;
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
public class LiAnudocBean {

    @EJB
    private TiposDocumentosFacade tiposDocumentosFacade;

    private TiposDocumentos tiposDocumentos;
    private List<TiposDocumentos> listaTiposDocumentos;

    private Date desde;
    private Date hasta;

    private String operacion;
    
    
    @EJB
    private ExcelFacade excelFacade;

    public LiAnudocBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.tiposDocumentos = new TiposDocumentos();
        this.listaTiposDocumentos = new ArrayList<TiposDocumentos>();

        this.desde = new Date();
        this.hasta = new Date();

        setOperacion("A");
    }

    public List<TiposDocumentos> listarTiposDocumentosAnudoc() {
        this.listaTiposDocumentos = tiposDocumentosFacade.listarTipoDocumentoLiAnudoc();
        return this.listaTiposDocumentos;
    }

    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();

        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String tipoDoc = "";

        StringBuilder sql = new StringBuilder();
        if (this.tiposDocumentos == null) {
            tipoDoc = "TODOS";
        } else {
            tipoDoc = this.tiposDocumentos.getCtipoDocum();
        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("FCO")
                || this.tiposDocumentos.getCtipoDocum().equals("FCR")
                || this.tiposDocumentos.getCtipoDocum().equals("CPV")) {

            sql.append("SELECT  \n"
                    + "f.ctipo_docum, f.nrofact as ndocum, f.ffactur as fdocum,\n"
                    + "f.cod_cliente as cod_persona, \n"
                    + "f.xrazon_social as xnombre, "
                    + "f.ttotal as ttotal, f.fanul as fanul, \n"
                    + "f.cusuario_anul, '' as origen, '' as destino, '' as cconc\n");

            if (this.operacion.equals("A")) {

                sql.append("from facturas f \n"
                        + "where f.mestado = 'X'\n"
                        + "and f.cod_empr = 2\n"
                        + "and f.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and f.ffactur between '" + fdesde + "' and '" + fhasta + "'");

            } else if (this.operacion.equals("E")) {

                sql.append("from hfacturas f \n"
                        + "where f.cod_empr = 2\n"
                        + "and f.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and f.ffactur between '" + fdesde + "' and '" + fhasta + "'");

            }

        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("FCC")
                || this.tiposDocumentos.getCtipoDocum().equals("CVC")
                || this.tiposDocumentos.getCtipoDocum().equals("COC")) {

            if (this.tiposDocumentos == null) {
                sql.append("\n UNION ALL \n");
            }

            sql.append("SELECT  c.ctipo_docum, c.nrofact as ndocum, "
                    + "c.ffactur as fdocum, \n"
                    + "c.cod_proveed as cod_persona, \n"
                    + "p.xnombre, c.ttotal as ttotal, c.fanul as fanul, c.cusuario_anul, '' as origen, '' as destino, '' as cconc\n");

            if (this.operacion.equals("A")) {

                sql.append("from compras c, proveedores p \n"
                        + "where c.mestado = 'X'\n"
                        + "and c.cod_proveed = p.cod_proveed and c.cod_empr = 2\n"
                        + "and c.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and c.ffactur between '" + fdesde + "' and '" + fhasta + "'");

            } else if (this.operacion.equals("E")) {

                sql.append("from hcompras c, proveedores p \n"
                        + "where c.cod_proveed = p.cod_proveed and c.cod_empr = 2\n"
                        + "and c.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and c.ffactur between '" + fdesde + "' and '" + fhasta + "'");

            }

        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("PED")) {

            if (this.operacion.equals("A")) {

                if (this.tiposDocumentos == null) {
                    sql.append("\n UNION ALL \n");
                }
                sql.append("SELECT  'PED' as ctipo_docum, p.nro_pedido as ndocum, \n"
                        + "p.fpedido as fdocum, \n"
                        + "p.cod_cliente as cod_persona, \n"
                        + "c.xnombre, p.ttotal as ttotal, p.fanul as fanul, \n"
                        + "p.cusuario_modif as cusuario_anul, '' as origen, \n"
                        + "'' as destino, '' as cconc \n");

                sql.append("FROM pedidos p, clientes c \n"
                        + "WHERE CONVERT(char(10), p.fanul, 103) BETWEEN '" + fdesde + "' AND '" + fhasta + "'\n"
                        + "AND p.cod_cliente = c.cod_cliente \n"
                        + "AND p.mestado = 'X' \n"
                        + "AND p.ctipo_docum = '" + tipoDoc + "'\n"
                        + "AND p.cod_empr = 2 ");

            }

        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("EN")) {

            if (this.tiposDocumentos == null) {
                sql.append("\n UNION ALL \n");
            }
            sql.append("SELECT  'EN' as ctipo_docum, e.nro_envio as ndocum, e.fenvio as fdocum, \n"
                    + " 0 as cod_persona,  \n"
                    + " '' as xnombre, 0 as ttotal, fanul as fanul, e.cusuario_modif as cusuario_anul, d1.xdesc as origen,   \n"
                    + " d2.xdesc as destino, '' as cconc\n");

            if (this.operacion.equals("A")) {

                sql.append("from envios e, depositos d1, depositos d2\n"
                        + "where  e.depo_origen  = d1.cod_depo \n"
                        + " AND e.depo_destino = d2.cod_depo \n"
                        + "   AND e.cod_empr = 2 \n"
                        + "   and e.mestado = 'X'\n"
                        + "   and e.fenvio between '" + fdesde + "' and '" + fhasta + "'");

            }

            if (this.operacion.equals("E")) {

                sql.append("from henvios e, depositos d1, depositos d2\n"
                        + "where  e.depo_origen  = d1.cod_depo \n"
                        + " AND e.depo_destino = d2.cod_depo \n"
                        + "   AND e.cod_empr = 2 \n"
                        + "   and e.fenvio between '" + fdesde + "' and '" + fhasta + "'");

            }

        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("NCV")
                || this.tiposDocumentos.getCtipoDocum().equals("NDV")) {

            if (this.tiposDocumentos == null) {
                sql.append("\n UNION ALL \n");
            }
            sql.append("SELECT   n.ctipo_docum as ctipo_docum, n.nro_nota as ndocum, n.fdocum as fdocum ,\n"
                    + " n.cod_cliente as cod_persona, \n"
                    + "c.xnombre, n.ttotal as ttotal, n.fanul as fanul, n.cusuario_anul, d1.xdesc as origen, '' as destino, n.cconc\n");

            if (this.operacion.equals("A")) {

                sql.append("from  notas_ventas n, depositos d1, clientes c\n"
                        + "where n.cod_depo = d1.cod_depo \n"
                        + "and n.cod_cliente = c.cod_cliente\n"
                        + "and n.mestado = 'X'\n"
                        + "and n.cod_empr = 2\n"
                        + "and n.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and n.fdocum between '" + fdesde + "' and '" + fhasta + "'");

            }

            if (this.operacion.equals("E")) {

                sql.append("from  hnotas_ventas n, depositos d1, clientes c\n"
                        + "where n.cod_depo = d1.cod_depo \n"
                        + "and n.cod_cliente = c.cod_cliente\n"
                        + "and n.cod_empr = 2\n"
                        + "and n.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and n.fdocum between '" + fdesde + "' and '" + fhasta + "'");

            }

        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("NCC")
                || this.tiposDocumentos.getCtipoDocum().equals("NDC")
                || this.tiposDocumentos.getCtipoDocum().equals("NDP")) {

            if (this.tiposDocumentos == null) {
                sql.append("\n UNION ALL \n");
            }
            sql.append("SELECT   n.ctipo_docum as ctipo_docum, n.nro_nota as ndocum,\n"
                    + " n.fdocum as fdocum ,\n"
                    + "n.cod_proveed as cod_persona,  \n"
                    + "p.xnombre, n.ttotal as ttotal, fanul as fanul, n.cusuario as cusuario_anul, d1.xdesc as origen, '' as destino, n.cconc\n");

            if (this.operacion.equals("A")) {

                sql.append("from notas_compras n, proveedores p, depositos d1\n"
                        + "where n.cod_proveed = p.cod_proveed\n"
                        + "and n.cod_depo = d1.cod_depo\n"
                        + "and n.cod_empr = 2\n"
                        + "and n.mestado = 'X'\n"
                        + "and n.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and n.fdocum between '" + fdesde + "' and '" + fhasta + "'");

            }

            if (this.operacion.equals("E")) {

                sql.append("from hnotas_compras n, proveedores p, depositos d1\n"
                        + "where n.cod_proveed = p.cod_proveed\n"
                        + "and n.cod_depo = d1.cod_depo\n"
                        + "and n.cod_empr = 2\n"
                        + "and n.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and n.fdocum between '" + fdesde + "' and '" + fhasta + "'");

            }

        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("AJ")
                || this.tiposDocumentos.getCtipoDocum().equals("RM")) {

            if (this.operacion.equals("A")) {

                if (this.tiposDocumentos == null) {
                    sql.append("\n UNION ALL \n");
                }
                sql.append("SELECT   n.ctipo_docum as ctipo_docum, n.ndocum, n.fdocum as fdocum, \n"
                        + "n.cod_cliente as cod_persona,  \n"
                        + "c.xnombre, 0 as ttotal, fanul as fanul, n.cusuario_modif as cusuario_anul, d1.xdesc as origen,'' as destino, '' as cconc\n");

                sql.append("from \n"
                        + "docum_varios n\n"
                        + "left outer join depositos d1 on (n.cod_depo = d1.cod_depo)\n"
                        + "left outer join clientes c on (n.cod_cliente = c.cod_cliente)\n"
                        + "where n.mestado = 'X'\n"
                        + "and n.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and n.fdocum between '" + fdesde + "' and '" + fhasta + "'\n"
                        + "and n.cod_empr = 2");

            }

            if (this.operacion.equals("E")) {

                if (this.tiposDocumentos == null) {
                    sql.append("\n UNION ALL \n");
                }

                sql.append("SELECT   n.ctipo_docum as ctipo_docum, n.nro_nota as ndocum, n.fdocum as fdocum, \n"
                        + "n.cod_proveed as cod_persona,  \n"
                        + "c.xnombre, 0 as ttotal, fanul as fanul, n.cusuario_elim as cusuario_anul, d1.xdesc as origen,'' as destino, '' as cconc\n");

                sql.append("from \n"
                        + "hnotas_compras n\n"
                        + "left outer join depositos d1 on (n.cod_depo = d1.cod_depo)\n"
                        + "left outer join proveedores c on (c.cod_proveed = c.cod_proveed)\n"
                        + "where n.fdocum between '" + fdesde + "' and '" + fhasta + "'\n"
                        + "and n.ctipo_docum = '" + tipoDoc + "'\n"
                        + "and n.cod_empr = 2");

            }

        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("REC")) {

            if (this.tiposDocumentos == null) {
                sql.append("\n UNION ALL \n");
            }
            sql.append("SELECT   'REC' as ctipo_docum, r.nrecibo as ndocum, r.frecibo as fdocum ,\n"
                    + "r.cod_cliente as cod_persona, \n"
                    + "c.xnombre, r.irecibo as ttotal , fanul as fanul, r.cusuario as cusuario_anul, '' as origen, '' as destino, '' as cconc\n");

            if (this.operacion.equals("A")) {

                sql.append("from recibos r, clientes c\n"
                        + "where r.cod_cliente = c.cod_cliente\n"
                        + "and r.cod_empr = 2\n"
                        + "and r.mestado = 'X'\n"
                        + "and r.frecibo between '" + fdesde + "' and '" + fhasta + "'");

            }

            if (this.operacion.equals("E")) {

                sql.append("from hrecibos r, clientes c\n"
                        + "where r.cod_cliente = c.cod_cliente\n"
                        + "and r.cod_empr = 2\n"
                        + "and r.frecibo between '" + fdesde + "' and '" + fhasta + "'");

            }

        }

        if (this.tiposDocumentos == null
                || this.tiposDocumentos.getCtipoDocum().equals("REP")) {

            if (this.tiposDocumentos == null) {
                sql.append("\n UNION ALL \n");
            }
            sql.append("SELECT   'REP' as ctipo_docum, r.nrecibo as ndocum, r.frecibo as fdocum , \n"
                    + " r.cod_proveed as cod_persona,  \n"
                    + "p.xnombre, r.irecibo as ttotal , fanul as fanul, r.cusuario as cusuario_anul, '' as origen, '' as destino, '' as cconc\n");

            if (this.operacion.equals("A")) {

                sql.append("from recibos_prov r, proveedores p\n"
                        + "where r.cod_proveed = p.cod_proveed\n"
                        + "and r.cod_empr = 2\n"
                        + "and r.mestado = 'X'\n"
                        + "and r.frecibo between '" + fdesde + "' and '" + fhasta + "'");

            }

            if (this.operacion.equals("E")) {

                sql.append("from hrecibos_prov r, proveedores p\n"
                        + "where r.cod_proveed = p.cod_proveed\n"
                        + "and r.cod_empr = 2\n"
                        + "and r.frecibo between '" + fdesde + "' and '" + fhasta + "'");

            }

        }

        //verificar la parte de cheques en el AT
        /*if (this.tiposDocumentos == null

            sql.append("\n UNION ALL \n");
            sql.append("\n");

            if (this.operacion.equals("A")) {

                sql.append("");
                || this.tiposDocumentos.getCtipoDocum().equals("CHQ")) {

            }

            if (this.operacion.equals("E")) {

                sql.append("");

            }

        }*/
        System.out.println("SQL lianudoc: " + sql.toString());

        if (tipo.equals("VIST")) {
            rep.reporteLiAnudoc(sql.toString(), dateToString2(desde), dateToString2(hasta), tipoDoc, "admin", this.operacion, tipo);
        } else {

            List<Object[]> lista = new ArrayList<Object[]>();

            String[] columnas = new String[11];

            columnas[0] = "ctipo_docum";
            columnas[1] = "ndocum";
            columnas[2] = "fdocum";
            columnas[3] = "cod_persona";
            columnas[4] = "xnombre";
            columnas[5] = "ttotal";
            columnas[6] = "fanul";
            columnas[7] = "cusuario_anul";
            columnas[8] = "origen";
            columnas[9] = "destino";
            columnas[10] = "cconc";


            lista = excelFacade.listarParaExcel(sql.toString());

            rep.exportarExcel(columnas, lista, "lianudoc");
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

}
