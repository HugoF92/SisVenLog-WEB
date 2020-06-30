/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import entidad.ConceptosDocumentos;
import entidad.Proveedores;
import entidad.Sublineas;
import entidad.TiposDocumentos;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.DateUtil;


/**
 *
 * @author jvera
 * 
 */
@Stateless
public class ListadoNotasComprasFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    protected EntityManager getEntityManager() {
        return em;
    }

    public ListadoNotasComprasFacade() {
        
    }
    
    //JLVC 19-05-2020
    public void prepararDatosListadoNotasCompras(Date fechaDesde, 
                                                Date fechaHasta, 
                                                Proveedores proveedor,
                                                TiposDocumentos tipoDocumento,
                                                ConceptosDocumentos conceptoDocumento,
                                                Sublineas sublinea,
                                                String tipoListado){
        String sql = "IF OBJECT_ID('mostrar') IS NOT NULL DROP TABLE mostrar;";
        if (tipoListado.equals("conDetalle")) {
            sql += "SELECT n.nro_nota, n.ctipo_docum as nctipo_docum, n.cconc, n.fdocum, n.cod_proveed as ncod_proveed, f.nrofact, f.ctipo_docum, c.cod_proveed, c.xnombre, \n" +
            "d.xdesc as xdesc_conc, n.com_ctipo_docum, n.tgravadas, n.texentas as texentas2, n.timpuestos,  n.xobs,\n" +
            "f.ttotal as fac_ttotal, f.ffactur, t.xdesc as xdesc_docum, dc.cod_merca, dc.xdesc, dc.igravadas, dc.impuestos, dc.iexentas,\n" +
            "dc.cant_cajas, dc.cant_unid, n.ttotal, n.ntimbrado\n" +
            "INTO mostrar\n" +
            "FROM notas_compras n LEFT JOIN compras f ON n.com_ctipo_docum = f.ctipo_docum\n" +
            "AND n.nrofact = f.nrofact\n" +
            "AND n.cod_empr = f.cod_empr\n" +
            "AND n.cod_proveed = f.cod_proveed\n" +
            "AND n.ffactur = f.ffactur , proveedores c, conceptos_documentos d, tipos_documentos t, notas_compras_det dc , mercaderias m \n" +
            "WHERE n.cod_empr = 2\n" +
            "AND dc.cod_empr = 2\n" +
            "AND n.nro_nota = dc.nro_nota\n" +
            "AND n.cod_proveed = dc.cod_proveed\n" +
            "AND n.ctipo_docum = dc.ctipo_docum\n" +
            "AND n.cod_proveed = c.cod_proveed\n" +
            "AND n.ctipo_docum = d.ctipo_docum\n" +
            "AND n.fdocum = dc.fdocum\n" +
            "AND dc.cod_merca = m.cod_merca\n" +
            "AND n.cconc = d.cconc\n" +
            "AND n.ctipo_docum = t.ctipo_docum\n" +
            "AND n.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde) + "' AND '" + DateUtil.dateToString(fechaHasta) + "'";
            sql += "AND n.mestado = 'A'\n";
            if (sublinea != null) {
                sql += "AND m.cod_sublinea = " + sublinea.getCodLinea() + "\n";
            }
            if (conceptoDocumento != null) {
                sql += "AND n.cconc = '" + conceptoDocumento.getConceptosDocumentosPK().getCconc() + "'\n";
            }
            if (tipoDocumento != null) {
                sql += "AND n.ctipo_docum = '" + tipoDocumento.getCtipoDocum() + "'\n";
            }
            if (proveedor != null) {
                sql += "AND n.cod_proveed = " + proveedor.getCodProveed() + "\n";
            }
        }else{ //resumen
            sql += "SELECT n.nro_nota, n.ctipo_docum as nctipo_docum, n.cconc, n.fdocum, n.cod_proveed as ncod_proveed, f.nrofact, f.ctipo_docum, c.cod_proveed, c.xnombre,\n" +
            "d.xdesc as xdesc_conc, n.com_ctipo_docum, n.tgravadas, n.texentas as texentas2, n.timpuestos, n.xobs, f.ttotal as fac_ttotal, \n" +
            "f.ffactur, t.xdesc as xdesc_docum, 0 as ttotal, n.ntimbrado\n" +
            "INTO mostrar FROM notas_compras n LEFT JOIN compras f ON n.com_ctipo_docum = f.ctipo_docum\n" +
            "AND n.nrofact = f.nrofact\n" +
            "AND n.cod_empr = f.cod_empr\n" +
            "AND n.cod_proveed = f.cod_proveed\n" +
            "AND n.ffactur = f.ffactur, proveedores c, conceptos_documentos d, tipos_documentos t  \n" +
            "WHERE n.cod_empr = 2\n" +
            "AND n.cod_proveed = c.cod_proveed\n" +
            "AND n.ctipo_docum = d.ctipo_docum\n" +
            "AND n.cconc = d.cconc\n" +
            "AND n.ctipo_docum = t.ctipo_docum\n" +
            "AND n.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde) + "' AND '" + DateUtil.dateToString(fechaHasta) + "'";
            sql += "AND n.mestado = 'A'\n";
            if (sublinea != null) {
                sql += "AND exists (select 1 FROM notas_compras_det dd, mercaderias md\n" +
                "WHERE dd.cod_empr =2 AND n.nro_nota = dd.nro_nota AND n.ctipo_docum = dd.ctipo_docum AND n.cod_proveed = dd.cod_proveed AND n.fdocum = dd.fdocum\n" +
                "AND dd.cod_merca = md.cod_merca AND md.cod_sublinea = " + sublinea.getCodLinea() + " )\n";
            }
        }
        sql += "ORDER BY n.fdocum, n.ctipo_docum, n.cconc, n.nro_nota\n";
        
        //carga en TMP_NUMEROS los nros de nota del cursor Mostrar
        sql += "IF OBJECT_ID('tmp_numeros') IS NOT NULL DROP TABLE tmp_numeros;\n"; 
        sql += "CREATE TABLE tmp_numeros (ndocum NUMERIC(12), ctipo_docum CHAR(3), COD_PROVEED SMALLINT, ttotal numeric(12), fdocum smalldatetime)\n";
        sql += "INSERT INTO tmp_numeros(ndocum, ctipo_docum, COD_PROVEED, ttotal, fdocum)\n" +
            "SELECT nro_nota, nctipo_docum, cod_proveed, ttotal, fdocum \n" +
            "FROM mostrar;\n";
        
        //busca los montos discriminados en IVA 10, IVA 5 y exentas para los nros de nota guardados en TMP_NUMEROS. Guarda en el cursor CURDET
        sql += "IF OBJECT_ID('CurDet') IS NOT NULL DROP TABLE CurDet; \n" +
            "SELECT t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, isnull(SUM(d.igravadas), 0) AS tgravadas_5, 0 AS tgravadas_10, \n" +
            "isnull(SUM(d.impuestos), 0) AS timpuestos_5, 0 AS timpuestos_10, t.ttotal \n" +
            "INTO CurDet\n" +
            "FROM notas_compras_det d INNER JOIN tmp_numeros t ON d.nro_nota = t.ndocum \n" +
            "AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.fdocum = t.fdocum \n" +
            "WHERE (d.cod_empr = 2) and d.pimpues = 5 \n" +
            "GROUP BY t.ctipo_docum, t.ndocum, t.cod_proveed, t.ttotal, d.cod_merca \n" +
            "UNION ALL\n" +
            "SELECT t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, 0 AS texentas, 0 AS tgravadas_5, isnull(SUM(d.igravadas), 0) AS tgravadas_10, \n" +
            "0 AS timpuestos_5, isnull(SUM(d.impuestos), 0) AS timpuestos_10, t.ttotal \n" +
            "FROM notas_compras_det d INNER JOIN tmp_numeros t ON d.nro_nota = t.ndocum \n" +
            "AND d.ctipo_docum = t.ctipo_docum AND d.cod_proveed = t.cod_proveed AND d.fdocum = t.fdocum \n" +
            "WHERE (d.cod_empr = 2) and d.pimpues = 10 \n" +
            "GROUP BY t.ctipo_docum, t.ndocum, t.cod_proveed, t.ttotal, d.cod_merca \n" +
            "UNION ALL\n" +
            "SELECT t.ctipo_docum, t.ndocum, t.cod_proveed, d.cod_merca, isnull(SUM(d.iexentas), 0) AS texentas, 0 AS tgravadas_5, 0 AS tgravadas_10, 0 AS timpuestos_5, \n" +
            "0 AS timpuestos_10, t.ttotal \n" +
            "FROM notas_compras_det d INNER JOIN tmp_numeros t ON d.nro_nota = t.ndocum AND d.ctipo_docum = t.ctipo_docum \n" +
            "AND d.cod_proveed = t.cod_proveed AND d.fdocum = t.fdocum AND d.iexentas > 0 \n" +
            "WHERE (d.cod_empr = 2) \n" +
            "GROUP BY t.ctipo_docum, t.ndocum, t.cod_proveed, t.ttotal, d.cod_merca\n";
        
        //Agrupa por tipo documento, nro, proveed, total en cursor Totdet
        sql += "IF OBJECT_ID('TotDet') IS NOT NULL DROP TABLE TotDet; \n" +
            "SELECT ctipo_docum, ndocum, cod_proveed, ttotal, SUM(texentas) as texentas, SUM(tgravadas_5) as tgravadas_5, SUM(tgravadas_10) as tgravadas_10, \n" +
            "ROUND(SUM(timpuestos_5),0) as timpuestos_5, ROUND(sum(timpuestos_10),0) as timpuestos_10\n" +
            "INTO TotDet\n" +
            "FROM CurDet GROUP BY ctipo_docum, ndocum, cod_proveed, ttotal \n";
        
        //que debemos crear la tabla infototdoc para cargar los resumenes
        /*sql+="IF OBJECT_ID('infototdoc') IS NOT NULL DROP TABLE infototdoc; \n" +
            "CREATE TABLE infototdoc (nDOCUM NUMERIC(12), ctipo_docum CHAR(3), ttotal numeric(12))\n" +
            "INSERT INTO infototdoc(nDOCUM, ctipo_docum, ttotal)\n" +
            "SELECT ndocum, ctipo_docum, ttotal\n" +
            "FROM TotDet;\n";*/
        
        //invocacion a reportes
        if (tipoListado.equals("conDetalle")) {
            //prepara el cursor CurSin (no están en Infototdoc) y Curfin (sí están en Infototdoc)
            sql += "IF OBJECT_ID('Curfin') IS NOT NULL DROP TABLE Curfin;\n" +
                "SELECT m.nro_nota, m.cconc, m.fdocum, m.nrofact, m.nctipo_docum, m.xobs, m.ncod_proveed, m.xnombre,\n" +
                "m.xdesc_conc, m.com_ctipo_docum, m.fac_ttotal, m.ffactur, m.xdesc_docum, m.ntimbrado, i.tgravadas_10, i.tgravadas_5, \n" +
                "i.timpuestos_10, i.timpuestos_5, i.texentas, i.ttotal, m.cod_merca, m.xdesc, m.igravadas, m.iexentas, m.impuestos, m.cant_cajas, m.cant_unid\n" +
                "INTO Curfin\n" +
                "FROM mostrar m, TotDet i\n" +
                "WHERE m.nro_nota = i.ndocum AND m.nctipo_docum = i.ctipo_docum AND m.ncod_proveed = i.cod_proveed\n";
        }else{
            sql += "IF OBJECT_ID('Curfin') IS NOT NULL DROP TABLE Curfin; \n" +
                "SELECT m.nro_nota, m.cconc, m.fdocum, m.nrofact, m.nctipo_docum, m.xobs, m.ncod_proveed, m.xnombre, \n" +
                "m.xdesc_conc, m.com_ctipo_docum, m.fac_ttotal, m.ffactur, m.xdesc_docum, m.ntimbrado, i.tgravadas_10, i.tgravadas_5, \n" +
                "i.timpuestos_10, i.timpuestos_5, i.texentas, i.ttotal\n" +
                "INTO Curfin\n" +
                "FROM mostrar m, TotDet i \n" +
                "WHERE m.nro_nota = i.ndocum AND m.nctipo_docum = i.ctipo_docum AND m.ncod_proveed = i.cod_proveed \n";
        }
        //ordena los datos de CurFin en otro cursor CurFin2 por los criterios de corte de control, CurFin2 es el cursor sobre el cual se genera el reporte
        sql += "IF OBJECT_ID('CurFin2') IS NOT NULL DROP TABLE CurFin2;\n" +
            "SELECT * \n" +
            "INTO CurFin2\n" +
            "FROM Curfin\n" +
            "ORDER BY fdocum, nctipo_docum, cconc, nro_nota\n";
        
        //invocacion al reporte correspondiente
        //if (tipoListado != null) {
            if (tipoListado.equals("resumen")) {
                //ordena los datos segun los criterios de corte
                sql += "IF OBJECT_ID('CurFin2') IS NOT NULL DROP TABLE CurFin2;\n" +
                    "SELECT *\n" +
                    "INTO CurFin2\n" +
                    "FROM CurFin\n" +
                    "ORDER BY nctipo_docum, cconc, nro_nota\n";
                //se debe llamar al reporte 'rnotascom3'
            }//else{
                //se debe llamar al reporte 'rnotascomdet'
            //}
        //}else{
            //se debe llamar al reporte 'rnotascom2'  
       //}
        
        //ejecutamos el script sql
        em.clear();
        Query q = em.createNativeQuery(sql);
        q.executeUpdate();
    }
    
    //metodo para armar archivo Excel
    public List<Object[]> generarListadoPedidosExcel(Date fechaDesde, 
                                                Date fechaHasta, 
                                                Proveedores proveedor,
                                                TiposDocumentos tipoDocumento,
                                                ConceptosDocumentos conceptoDocumento,
                                                Sublineas sublinea,
                                                String tipoListado){
        String sql = "";
        
        
        Query q = em.createNativeQuery(sql);      
        System.out.println(q.toString());
        List<Object[]> resultados = q.getResultList();
        
       return resultados;
    }
}