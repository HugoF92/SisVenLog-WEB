package bean.listados;

import dao.ExcelFacade;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.LlamarReportes;
/**
 *
 * @author Nico
 */
@ManagedBean
@SessionScoped
public class genLibroVentas {
    
    private Date desde;
    
    private Date hasta;

    @EJB
    private ExcelFacade excelFacade;
    
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
        List<Object[]> lista , lista2, lista3 , lista4 , lista5= new ArrayList<>();
        String[] columnas = null;
        String sql = "";
        columnas = new String[12];
        columnas[0] = "ffactur";
        columnas[1] = "nrofact";
        columnas[2] = "ctipo_docum";
        columnas[3] = "mtipo_papel";
        columnas[4] = "nro_docum_ini";
        columnas[5] = "nro_docum_fin";
        columnas[6] = "ttotal";
        columnas[7] = "texentas";
        columnas[8] = "tgravadas_10";
        columnas[9] = "tgravadas_5";
        columnas[10] = "timpuestos_10";
        columnas[11] = "timpuestos_5";
        sql = "(SELECT f.ffactur, f.nrofact,'FAC' AS ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, F.TTOTAL," +
            "SUM(d.iexentas) AS texentas, SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5," +
            "SUM(ABS(d.impuestos)) AS timpuestos_10, 0 AS timpuestos_5"+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact "+
            " AND f.ctipo_docum = d.ctipo_docum "+
            " AND f.ffactur = d.ffactur "+
            " WHERE R.COD_EMPR = 2 AND D.COD_EMPR = 2 AND F.COD_EMPR = 2 "+
            " AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "+
            " AND d.impuestos > 0 AND d.pimpues = 10 AND r.mtipo_papel = 'F' "+
            " GROUP BY f.ffactur, f.nrofact, r.mtipo_papel, r.nro_docum_ini,r.nro_docum_fin, f.ttotal) "+
            " UNION ALL "+
            " (SELECT f.ffactur, f.nrofact,'FAC' AS ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, "+
            " SUM(d.iexentas) AS texentas, 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 AS timpuestos_10,"+
            " SUM(ABS(d.impuestos)) AS timpuestos_5 "+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact "+
            " AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "+
            " WHERE R.COD_EMPR = 2 AND D.COD_EMPR = 2 AND F.COD_EMPR = 2 "+
            " AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "+
            " AND d.impuestos > 0 AND d.pimpues = 5 AND r.mtipo_papel = 'F' "+
            " GROUP BY f.ffactur, f.nrofact, r.mtipo_papel, r.nro_docum_ini,r.nro_docum_fin, f.ttotal) "+
            " UNION ALL "+
            " (SELECT f.ffactur, f.nrofact, 'FAC' as ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, f.ttotal,"+
            " SUM(d.iexentas) AS texentas, 0 AS tgravadas_10, 0 as tgravadas_5, 0 AS timpuestos_10, 0 AS timpuestos_5 "+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact "+
            " AND f.ctipo_docum = d.ctipo_docum "+
            " AND f.ffactur = d.ffactur "+
            " WHERE D.COD_EMPR= 2 AND F.COD_EMPR = 2 AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "+
            " AND d.impuestos = 0 AND d.pimpues = 0 and r.mtipo_papel = 'F' "+
            " GROUP BY f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, f.ttotal) "+
            " UNION ALL "+
            " (SELECT f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, F.TTOTAL, "+
            " SUM(d.iexentas) AS texentas,SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, "+
            " SUM(ABS(d.impuestos)) AS timpuestos_10, 0 AS timpuestos_5 "+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact "+
            " AND f.ctipo_docum = d.ctipo_docum "+
            " AND f.ffactur = d.ffactur "+
            " WHERE R.COD_EMPR = 2 AND D.COD_EMPR = 2 AND F.COD_EMPR= 2 AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "+
            " AND d.impuestos > 0 AND d.pimpues = 10 AND r.mtipo_papel = 'M' "+
            " GROUP BY f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, f.ttotal) "+
            " UNION ALL "+
            " (SELECT f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, "+
            " SUM(d.iexentas) AS texentas, 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, 0 AS timpuestos_10, "+
            " SUM(ABS(d.impuestos)) AS timpuestos_5 "+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact "+
            " AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "+
            " WHERE D.COD_EMPR = 2 AND F.COD_EMPR = 2 AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "+
            " AND d.impuestos > 0 AND d.pimpues = 5 AND r.mtipo_papel = 'M' "+
            " GROUP BY f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, f.ttotal) "+
            " UNION ALL "+
            " (SELECT f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, f.ttotal, "+
            " SUM(d.iexentas) AS texentas,0 AS tgravadas_10, 0 as tgravadas_5, 0 AS timpuestos_10, 0 AS timpuestos_5 "+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact "+
            " AND f.ctipo_docum = d.ctipo_docum "+
            " AND f.ffactur = d.ffactur "+
            " WHERE R.COD_EMPR = 2 AND F.COD_EMPR = 2 AND d.cod_empr = 2 AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "+
            " AND d.impuestos = 0 AND d.pimpues = 0 "+
            " and r.mtipo_papel = 'M' "+
            " GROUP BY f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, f.ttotal )"+
            " ORDER BY f.ffactur, r.mtipo_papel, r.nro_docum_ini,r.nro_docum_fin, f.ttotal ";
        lista = excelFacade.listarParaExcel(sql);
        sql = " (SELECT f.ffactur, f.nrofact,'FAC' AS ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal, " +
            " SUM(d.iexentas) AS texentas, SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, " +
            " SUM(ABS(d.impuestos)) AS timpuestos_10, 0 AS timpuestos_5 "+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact "+
            " AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "+
            " WHERE R.COD_EMPR = 2 AND D.COD_EMPR= 2 AND F.COD_EMPR= 2 "+
            " AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "+
            " AND d.impuestos < 0 AND d.pimpues = 10 AND r.mtipo_papel = 'F' "+
            " GROUP BY f.ffactur, f.nrofact, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal) " +
            " UNION ALL "+
            " (SELECT f.ffactur, f.nrofact,'FAC' AS ctipo_docum, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, " +
            " f.ttotal, SUM(d.iexentas) AS texentas, 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5, "+
            " 0 AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact "+
            " AND f.ctipo_docum = d.ctipo_docum AND f.ffactur = d.ffactur "+
            " WHERE R.COD_EMPR = 2 AND F.COD_EMPR= 2 AND D.COD_EMPR= 2 "+
            " AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) "+
            " AND d.impuestos < 0 AND d.pimpues = 5 AND r.mtipo_papel = 'F' "+
            " GROUP BY f.ffactur, f.nrofact, r.mtipo_papel, r.nro_docum_ini, r.nro_docum_fin, f.ttotal) "+
            " UNION ALL "+
            " (SELECT f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, "+
            " f.ttotal, SUM(d.iexentas) AS texentas,SUM(d.igravadas + d.impuestos) AS tgravadas_10, "+
            " 0 AS tgravadas_5, SUM(ABS(d.impuestos)) AS timpuestos_10, 0 AS timpuestos_5 "+
            " FROM facturas f "+
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum "+
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final "+
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum "+
            " AND f.ffactur = d.ffactur "+
            " WHERE R.COD_EMPR = 2 AND D.COD_EMPR = 2 AND F.COD_EMPR = 2 "+
            " AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) " +
            " AND d.impuestos < 0 AND d.pimpues = 10 AND r.mtipo_papel = 'M' " +
            " GROUP BY f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, f.ttotal) " +
            " UNION ALL " +
            " (SELECT f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, " +
            " f.ttotal, SUM(d.iexentas) AS texentas, 0 AS tgravadas_10, SUM(d.igravadas + d.impuestos) AS tgravadas_5," +
            " 0 AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 " +
            " FROM facturas f " +
            " INNER JOIN rangos_documentos r ON f.ctipo_docum = r.ctipo_docum " +
            " AND YEAR(f.ffactur) BETWEEN r.nano_inicial AND r.nano_final " +
            " INNER JOIN facturas_det d ON f.nrofact = d.nrofact AND f.ctipo_docum = d.ctipo_docum " +
            " AND f.ffactur = d.ffactur " +
            " WHERE R.COD_EMPR = 2 AND D.COD_EMPR = 2 AND F.COD_EMPR = 2 AND (convert(date,f.ffactur) BETWEEN '"+fdesde+"' AND '"+fhasta+"') " +
            " AND (f.mestado = 'A') AND (f.nrofact BETWEEN r.nro_docum_ini AND r.nro_docum_fin) " +
            " AND d.impuestos < 0 AND d.pimpues = 5 AND r.mtipo_papel = 'M' " +
            " GROUP BY f.ffactur, f.nrofact, f.ctipo_docum, r.mtipo_papel,r.nro_docum_ini, r.nro_docum_fin, f.ttotal) " +
            " ORDER BY f.ffactur, r.mtipo_papel, r.nro_docum_ini,r.nro_docum_fin ";
        lista2 = excelFacade.listarParaExcel(sql);
        sql = "(SELECT n.fdocum as ffactur, n.ctipo_docum, n.nro_nota,n.cconc, n.ttotal,'F' as mtipo_papel, 0 as nro_docum_ini,"+
            " 0 as nro_docum_fin, SUM(d.iexentas) AS texentas,SUM(d.igravadas) AS tgravadas_10, 0 AS tgravadas_5, " +
            " SUM(ABS(d.impuestos)) AS timpuestos_10, 0 AS timpuestos_5 " +
            " FROM notas_ventas n "+
            " INNER JOIN notas_ventas_det d ON N.nro_nota = d.nro_nota AND n.ctipo_docum = d.ctipo_docum "+
            " AND n.fdocum = d.fdocum "+
            " WHERE N.COD_EMPR = 2 AND D.COD_EMPR = 2 AND (convert(date,n.fdocum) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (n.mestado = 'A') AND d.impuestos < 0 AND d.pimpues = 10 "+
            " GROUP BY n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal) "+
            " UNION ALL "+
            " (SELECT n.fdocum as ffactur, n.ctipo_docum, n.nro_nota, n.cconc,n.ttotal,'F' as mtipo_papel, 0 AS nro_docum_ini, "+
            " 0 as nro_docum_fin, SUM(d.iexentas) AS texentas, 0 AS tgravadas_10, SUM(d.igravadas) AS tgravadas_5, "+
            " 0 AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "+
            " FROM notas_ventas n "+
            " INNER JOIN notas_ventas_det d ON n.nro_nota = d.nro_nota "+
            " AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum "+
            " WHERE N.COD_EMPR = 2 AND D.COD_EMPR= 2 AND (convert(date,n.fdocum) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (n.mestado = 'A') AND d.impuestos < 0 AND d.pimpues = 5 "+
            " GROUP BY n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota,n.ttotal) "+
            " ORDER BY N.fDOCUM, n.nro_nota ";
        lista3 = excelFacade.listarParaExcel(sql);
        sql = "(SELECT n.fdocum as ffactur, n.ctipo_docum, n.nro_nota, n.cconc,n.ttotal, 'F' as mtipo_papel, "+
            " 0 as nro_docum_ini, 0 as nro_docum_fin, SUM(d.iexentas) AS texentas, "+
            " SUM(d.igravadas + d.impuestos) AS tgravadas_10, 0 AS tgravadas_5, "+
            " SUM(ABS(d.impuestos)) AS timpuestos_10, 0 AS timpuestos_5 "+
            " FROM notas_ventas n "+
            " INNER JOIN notas_ventas_det d ON N.nro_nota = d.nro_nota "+
            " AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum "+
            " WHERE N.COD_EMPR = 2 AND D.COD_EMPR = 2 AND (convert(date,n.fdocum) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (n.mestado = 'A') AND d.impuestos < 0 AND d.pimpues = 10 "+
            " GROUP BY n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota, n.ttotal) "+
            " UNION ALL "+
            " (SELECT n.fdocum as ffactur, n.ctipo_docum, n.nro_nota, n.cconc,n.ttotal,'F' as mtipo_papel, "+
            " 0 AS nro_docum_ini, 0 as nro_docum_fin, SUM(d.iexentas) AS texentas, 0 AS tgravadas_10, "+
            " SUM(d.igravadas + d.impuestos) AS tgravadas_5, 0 AS timpuestos_10, SUM(ABS(d.impuestos)) AS timpuestos_5 "+
            " FROM notas_ventas n "+
            " INNER JOIN notas_ventas_det d ON n.nro_nota = d.nro_nota "+
            " AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum "+
            " WHERE N.COD_EMPR = 2 AND D.COD_EMPR= 2 AND (convert(date,n.fdocum) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (n.mestado = 'A') AND d.impuestos < 0 AND d.pimpues = 5 "+
            " GROUP BY n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal) "+
            " UNION ALL "+
            " (SELECT n.fdocum as ffactur, n.ctipo_docum, n.nro_nota, n.cconc,n.ttotal,'F' as mtipo_papel, 0 AS nro_docum_ini, "+
            " 0 as nro_docum_fin, SUM(d.iexentas) AS texentas, 0 AS tgravadas_10, 0 AS tgravadas_5, "+
            " 0 AS timpuestos_10, 0 AS timpuestos_5 "+
            " FROM notas_ventas n "+
            " INNER JOIN notas_ventas_det d ON n.nro_nota = d.nro_nota "+
            " AND n.ctipo_docum = d.ctipo_docum AND n.fdocum = d.fdocum "+
            " WHERE N.COD_EMPR= 2 AND D.COD_EMPR = 2 AND (convert(date,n.fdocum) BETWEEN '"+fdesde+"' AND '"+fhasta+"') "+
            " AND (n.mestado ='A') AND d.impuestos = 0 AND d.pimpues = 0 "+
            " GROUP BY n.fdocum, n.ctipo_docum, N.CCONC, n.nro_nota, n.ttotal) "+
            " ORDER BY N.fDOCUM, n.nro_nota ";
        lista4 = excelFacade.listarParaExcel(sql);
        lista5.add(columnas);
        lista5.addAll(lista);
        lista5.addAll(lista2);
        lista5.addAll(lista3);
        lista5.addAll(lista4);
        rep.exportarCSV(lista5,"librovta");
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
