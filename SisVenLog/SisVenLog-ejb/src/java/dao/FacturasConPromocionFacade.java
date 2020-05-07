package dao;

import entidad.CanalesVenta;
import entidad.Mercaderias;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.ejb.Stateless;
import util.DateUtil;
import entidad.Promociones;
import entidad.PromocionesPK;

/**
 *
 * @author Arsenio
 */
@Stateless
public class FacturasConPromocionFacade {
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    public void generateTableImpDatos(Statement stmt, Date fechaDesde,
            Date fechaHasta, Promociones promocion, String tipoDocumento,
            CanalesVenta canalVenta, Boolean sinIVA, Boolean incluirPedidos) throws SQLException {
        String sql;
        //TODO: cambiar fecha para entorno local yyyy/dd/MM y para el server
        // de prueba yyyy/MM/dd
        String dateFormat = "yyyy/MM/dd";
        createTempTableCurpro(promocion, fechaDesde, dateFormat, fechaHasta, stmt);
        // Para cada promocion seleccionada (1 o todas)
        sql = "SELECT nro_promo, xdesc, mcalculo FROM #curpro";
        ResultSet rs = stmt.executeQuery(sql);
        List<Promociones> promocionesSeleccionadas = new ArrayList<>();
        while (rs.next()) {
            Promociones promo = new Promociones();
            promo.setPromocionesPK(new PromocionesPK());
            promo.getPromocionesPK().setNroPromo(Long.parseLong(rs.getString("nro_promo")));
            promo.setXdesc(rs.getString("xdesc"));
            promo.setMcalculo(Objects.isNull(rs.getString("mcalculo"))? Character.MIN_VALUE:rs.getString("mcalculo").charAt(0));
            promocionesSeleccionadas.add(promo);
        }
        for(Promociones promoSelected: promocionesSeleccionadas) {
            Long nroPromo = promoSelected.getPromocionesPK().getNroPromo();
            switch(tipoDocumento) {
                case "FCR":
                case "CPV":
                case "FCO":
                    createTempTableMostrar(stmt, fechaDesde, dateFormat, fechaHasta,
                    nroPromo, canalVenta, tipoDocumento);
                    populateTempTableImpDatos(stmt, promoSelected,
                            promocion, sinIVA, "#mostrar");
                    //Facturas con notas de promocion
                    createTempTableMostrar3(stmt, fechaDesde, dateFormat, fechaHasta,
                            nroPromo, canalVenta, tipoDocumento);
                    populateTempTableImpDatos(stmt, promoSelected,
                            promocion, sinIVA, "#mostrar3");
                    break;
                case "NCV":
                    createTempTableMostrar2(stmt, fechaDesde, dateFormat, fechaHasta,
                    nroPromo, canalVenta, tipoDocumento);
                    populateTempTableImpDatosMostrar2(stmt, promoSelected, sinIVA);
                    break;
                case "": // TODOS
                    createTempTableMostrar(stmt, fechaDesde, dateFormat, fechaHasta,
                    nroPromo, canalVenta, tipoDocumento);
                    populateTempTableImpDatos(stmt, promoSelected,
                            promocion, sinIVA, "#mostrar");
                    //Facturas con notas de promocion
                    createTempTableMostrar3(stmt, fechaDesde, dateFormat, fechaHasta,
                            nroPromo, canalVenta, tipoDocumento);
                    populateTempTableImpDatos(stmt, promoSelected,
                            promocion, sinIVA, "#mostrar3");
                    createTempTableMostrar2(stmt, fechaDesde, dateFormat, fechaHasta,
                    nroPromo, canalVenta, tipoDocumento);
                    populateTempTableImpDatosMostrar2(stmt, promoSelected, sinIVA);
                    break;
            }
            if(incluirPedidos){
                createTempTableMostrar1(stmt, fechaDesde, dateFormat, fechaHasta,
                    nroPromo, canalVenta);
                populateTempTableImpDatos(stmt, promoSelected, promocion,
                        sinIVA, "#mostrar1");
            }
        }
        // Eliminamos de la tabla todas las filas con codigo promo y codigo de mercaderia
        // que no esten en la tabla promociones det para ese mismo numero
        sql = "DELETE id FROM #impdatos id "
            + "WHERE NOT EXISTS "
            + "(SELECT DISTINCT d.cod_merca, d.nro_promo "
            + "FROM promociones_det d, promociones p "
            + "WHERE p.nro_promo = d.nro_promo "
            + "AND p.cod_empr = 2 "
            + "AND d.cod_empr = 2 "
            + "AND d.cod_merca = id.cod_merca "
            + "AND d.nro_promo = id.nro_promo) ";
       stmt.execute(sql);
    }

    private void createTempTableCurpro(Promociones promocion, Date fechaDesde,
            String dateFormat, Date fechaHasta, Statement stmt) throws SQLException {
        String sql;
        // creamos la tabla temporal #curpro
        if(Objects.isNull(promocion)){
            sql = "SELECT * "
                    + "INTO #curpro "
                    + "FROM promociones p "
                    + "WHERE cod_empr = 2 "
                    + "AND (p.frige_desde >= '" + DateUtil.dateToString(fechaDesde, dateFormat) + "' "
                    + "	 OR p.frige_hasta >= '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' "
                    + "	 OR '" + DateUtil.dateToString(fechaDesde, dateFormat) + "' BETWEEN p.frige_desde AND p.frige_hasta) ";
        }else {
            sql = "SELECT * "
                    + "INTO #curpro "
                    + "FROM promociones p "
                    + "WHERE nro_promo = " + promocion.getPromocionesPK().getNroPromo()
                    + " AND (p.frige_desde >= '" + DateUtil.dateToString(fechaDesde, dateFormat) + "' "
                    + "      OR p.frige_hasta >= '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' "
                    + "      OR '" + DateUtil.dateToString(fechaDesde, dateFormat) + "' BETWEEN p.frige_desde AND p.frige_hasta) ";
        }
        stmt.execute(sql);
    }

    private void createTempTableMostrar(Statement stmt, Date fechaDesde,
            String dateFormat, Date fechaHasta, Long nroPromo,
            CanalesVenta canalVenta, String tipoDocumento) throws SQLException {
        Boolean exists;
        String sql = "";
        exists = checkIfExistTemp(stmt, "#mostrar");
        if(exists){
            sql = "INSERT INTO #mostrar ";
        }
        sql += " SELECT  f.ctipo_docum, f.nrofact as ndocum, convert(char(3),'') as cconc, d.nro_promo, f.ffactur as fdocum, "
                + "         f.cod_cliente,f.xrazon_social as xnombre,  d.cod_merca, m.xdesc, d.cant_cajas, "
                + "         d.cant_unid, d.unid_bonif, m.npeso_caja, m.npeso_unidad, d.itotal, d.pdesc,  "
                + "         p.iprecio_caja as icosto_caja, p.iprecio_unidad as icosto_unidad, f.cod_vendedor, "
                + "         v.xnombre  as xvendedor,  d.iprecio_caja as icaja_vta, d.iprecio_unidad as iunid_vta, "
                + "         idescuentos as ttotal  ";
        if(!exists){
            sql += " INTO #mostrar ";
        }
        sql += " FROM facturas f, facturas_det d, precios p, mercaderias m, #tmp_mercaderias t, empleados v  "
                + " WHERE f.cod_empr = 2 "
                + " AND v.cod_empleado = f.cod_vendedor "
                + " AND f.mestado = 'A' "
                + " AND p.cod_depo = 1 "
                + " AND f.cod_empr = 2  "
                + " AND d.cod_empr  = 2  "
                + " AND (f.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat)
                + "' AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "') "
                + " AND f.nrofact = d.nrofact  "
                + " AND f.ffactur = d.ffactur  "
                + " AND d.cod_merca COLLATE DATABASE_DEFAULT = t.cod_merca COLLATE DATABASE_DEFAULT  "
                + " AND f.ctipo_docum = d.ctipo_docum  "
                + " AND f.cod_empr = d.cod_empr  "
                + " AND d.cod_merca = p.cod_merca  "
                + " AND d.cod_merca = m.cod_merca  "
                + " AND p.frige_desde <= f.ffactur  "
                + " AND p.frige_hasta >= f.ffactur  "
                + " AND p.ctipo_vta = 'X' "
                + " AND EXISTS (SELECT * "
                + "             FROM facturas_det d1 "
                + "             WHERE d1.cod_empr = 2 "
                + "             AND d1.nro_promo = " + nroPromo + " "
                + "             AND d1.ffactur = f.ffactur "
                + "             AND d1.ctipo_docum = f.ctipo_docum "
                + "             AND d1.nrofact = f.nrofact ) ";
        if(Objects.nonNull(canalVenta)) {
            sql += " AND f.cod_canal  = '" + canalVenta.getCodCanal() + "' ";
        }
        if(!tipoDocumento.isEmpty()){
            sql += " AND f.ctipo_docum  = '" + tipoDocumento + "' ";
        }
        stmt.execute(sql);
    }
    
    private void populateTempTableImpDatos(Statement stmt, Promociones promoSelected,
            Promociones promocion, Boolean sinIVA, String tmpTableName) throws SQLException {
        Boolean exists;
        String sql = "";
        exists = checkIfExistTemp(stmt, "#impdatos");
        if(exists){
            sql = "INSERT INTO #impdatos ";
        }
        sql += "SELECT m.ctipo_docum, m.ndocum, m.cconc, '" + promoSelected.getXdesc() + "' as xdesc_promo, "
            + "m.ctipo_docum as fac_ctipo_docum, m.ndocum as nrofact, "
            + "m.nro_promo, m.fdocum, m.cod_cliente, m.xnombre, "
            + "m.cod_merca, m.xdesc, m.cant_cajas, m.cant_unid, "
            + "m.unid_bonif, m.npeso_caja, m.npeso_unidad, "
            + "m.itotal, m.cod_vendedor, m.xvendedor, m.ttotal, "
            + "((m.npeso_caja * m.cant_cajas) + (m.npeso_unidad * m.cant_unid)) as ntot_peso, "
            + "CASE "
            + "    WHEN m.unid_bonif > 0 THEN (m.npeso_unidad * m.unid_bonif) "
            + "    ELSE 0 "
            + "END as ntot_pbonif, "
            + "CASE "
            + "	WHEN m.unid_bonif > 0 AND ('' = '" + promoSelected.getMcalculo() + "' OR '" + promoSelected.getMcalculo() + "' is null OR '" + promoSelected.getMcalculo() + "' = 'C') THEN (m.icosto_unidad * m.unid_bonif) "
            + "	WHEN '" + promoSelected.getMcalculo() + "' = 'N' AND m.pdesc = 0 THEN m.ttotal "
            + "	ELSE 0 "
            + "END as ibonific, "
            + "CASE "
            + "	WHEN ('' = '" + promoSelected.getMcalculo() + "' OR '" + promoSelected.getMcalculo() + "' is null OR '" + promoSelected.getMcalculo() + "' = 'C') AND m.pdesc > 0 THEN ((m.pdesc/100) * ((m.icosto_caja * m.cant_cajas) + (m.icosto_unidad * m.cant_unid))) "
            + "	WHEN '" + promoSelected.getMcalculo() + "' = 'N' AND m.pdesc = 0 THEN m.ttotal "
            + "	ELSE 0 "
            + "END as idescto, ";
        // campo pdesc
        if(Objects.nonNull(promocion) &&
                promoSelected.getPromocionesPK().getNroPromo() != promocion.getPromocionesPK().getNroPromo()){
            sql += "0 as pdesc, ";
        } else {
            sql += "m.pdesc, ";
        }
        if(!sinIVA) { // con IVA
            sql += "(m.icosto_caja + (m.icosto_caja * ISNULL(i.pimpues, 0)/100)) as icosto_caja, "
                + "(m.icosto_unidad + (m.icosto_unidad * ISNULL(i.pimpues, 0)/100)) as icosto_unidad, "
                + "m.icaja_vta, m.iunid_vta ";
        } else { //sin IVA
            sql += "(m.icaja_vta/(1+(ISNULL(i.pimpues, 0)/100))) as icaja_vta, "
                + "(m.iunid_vta/(1+(ISNULL(i.pimpues, 0)/100))) as iunid_vta, "
                + "m.icosto_caja, m.icosto_unidad ";
        }
        if(!exists){
            sql += "INTO #impdatos ";
        }
        sql += "FROM " + tmpTableName + " m "
            + "INNER JOIN (merca_impuestos mi "
            + "			LEFT JOIN impuestos i "
            + " 				ON mi.cod_impu = i.cod_impu) "
            + "ON m.cod_merca = mi.cod_merca ";
        stmt.execute(sql);
    }

    private Boolean checkIfExistTemp(Statement stmt, String tmpTable) throws SQLException {
            ResultSet res = stmt.executeQuery("select ISNUMERIC "
                    + "(OBJECT_ID(N'tempdb.." + tmpTable + "')) as existe");
            res.next();
            return "1".equals(res.getString("existe"));
    }

    public void insertarMercaderiasSeleccionadas(Statement stmt,
            List<Mercaderias> mercaderias, List<Mercaderias> mercaderiasActivas) throws SQLException {
        stmt.execute("CREATE TABLE #tmp_mercaderias ( "
                    + "cod_merca CHAR(13), "
                    + "cod_barra CHAR(20), "
                    + "xdesc CHAR(50), "
                    + "nrelacion SMALLINT, "
                    + "cant_cajas integer, "
                    + "cant_unid integer "
                    + ")");
        if (mercaderias.size() > 0) {
            Mercaderias aux;
            for (Mercaderias m : mercaderias) {
                aux = mercaderiasActivas.stream()
                        .filter(ma -> ma.equals(m))
                        .findAny()
                        .orElse(null);
                if(Objects.nonNull(aux)){
                    stmt.execute("INSERT INTO #tmp_mercaderias (cod_merca, "
                        + "cod_barra, xdesc, nrelacion,cant_cajas, cant_unid ) "
                        + "VALUES ('" + aux.getMercaderiasPK().getCodMerca()
                        + "', '" + aux.getCodBarra() + "', '" + aux.getXdesc()
                        + "', " + aux.getNrelacion() + ",0,0 )");
                }
            }
        } else {
            stmt.execute("INSERT INTO #tmp_mercaderias "
                    + "SELECT m.cod_merca, m.cod_barra, m.xdesc, m.nrelacion, 1 as cant_cajas, 1 as cant_unid "
                    + "FROM mercaderias m, existencias e "
                    + "WHERE m.cod_merca = e.cod_merca "
                    + "AND m.mestado = 'A' "
                    + "AND e.cod_depo = 1");
        }
    }
    
    public List<Object[]> listarParaExcel(Statement stmt, String[] columnas, String sql) {
        List<Object[]> lista = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = new Object[columnas.length];
                for (int i = 0; i < columnas.length; i++) {
                    row[i] = rs.getObject(columnas[i]);
                }
                lista.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FacturasConPromocionFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    private void createTempTableMostrar3(Statement stmt, Date fechaDesde,
            String dateFormat, Date fechaHasta, Long nroPromo,
            CanalesVenta canalVenta, String tipoDocumento) throws SQLException {
        Boolean exists;
        String sql = "";
        exists = checkIfExistTemp(stmt, "#mostrar3");
        if(exists){
            sql = "INSERT INTO #mostrar3 ";
        }
        sql += "SELECT "
            + "    f.ctipo_docum, f.nrofact as ndocum, convert(char(3),'') as cconc, d.nro_promo, f.ffactur as fdocum, "
            + "    f.cod_cliente,f.xrazon_social as xnombre, d.cod_merca, m.xdesc, d.cant_cajas, "
            + "    d.cant_unid, 0 AS unid_bonif, m.npeso_caja, m.npeso_unidad, d.idescuentos as ttotal, "
            + "    d.itotal, 0 as pdesc,  p.iprecio_caja as icosto_caja, p.iprecio_unidad as icosto_unidad, "
            + "    f.cod_vendedor, v.xnombre as xvendedor,  "
            + "    d.iprecio_caja as icaja_vta, d.iprecio_unidad as iunid_vta  ";
        if(!exists){
            sql += " INTO #mostrar3 ";
        }
        sql += "FROM facturas f, facturas_det d, precios p, mercaderias m, #tmp_mercaderias t, notas_ventas n1, empleados v "
            + "WHERE f.cod_empr = 2 "
            + "AND (f.mestado = 'A') "
            + "AND (p.cod_depo = 1 ) "
            + "AND f.cod_empr = 2 "
            + "AND f.cod_vendedor = v.cod_empleado "
            + "AND d.cod_empr  = 2 "
            + "AND (f.ffactur BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat)
            + "' AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' ) "
            + "AND f.nrofact = d.nrofact "
            + "AND f.ffactur = d.ffactur "
            + "AND f.ctipo_docum = d.ctipo_docum "
            + "AND f.cod_empr = d.cod_empr "
            + "AND d.cod_merca = p.cod_merca "
            + "AND d.cod_merca COLLATE DATABASE_DEFAULT = t.cod_merca COLLATE DATABASE_DEFAULT "
            + "AND d.cod_merca = m.cod_merca "
            + "AND p.frige_desde <= CONVERT(char(10),f.ffactur,103) "
            + "AND p.frige_hasta >= CONVERT(char(10),f.ffactur,103) "
            + "AND p.ctipo_vta = 'X' "
            + "AND f.nrofact = n1.nrofact "
            + "AND f.ffactur = n1.ffactur "
            + "AND f.ctipo_docum = n1.fac_ctipo_docum "
            + "AND n1.cconc IN ('BON','DPR','DIP','INT') "
            + "AND n1.mestado ='A' "
            + "AND n1.nro_promo = " + nroPromo + " "
            + "AND n1.cod_empr = 2 "
            + "AND v.cod_empr = 2 "
            + "AND p.cod_empr = 2 "
            + "AND n1.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat)
            + "' AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' ";
        if(Objects.nonNull(canalVenta)) {
            sql += " AND f.cod_canal  = '" + canalVenta.getCodCanal() + "' ";
        }
        if(!tipoDocumento.isEmpty()){
            sql += " AND f.ctipo_docum  = '" + tipoDocumento + "' ";
        }
        stmt.execute(sql);
    }

    private void createTempTableMostrar2(Statement stmt, Date fechaDesde,
            String dateFormat, Date fechaHasta, Long nroPromo,
            CanalesVenta canalVenta, String tipoDocumento) throws SQLException {
        Boolean exists;
        String sql = "";
        exists = checkIfExistTemp(stmt, "#mostrar2");
        if(exists){
            sql = "INSERT INTO #mostrar2 ";
        }
        sql += "SELECT "
            + "    n.cconc, n.ctipo_docum, n.nro_nota as ndocum, n.nro_promo, n.fdocum as fdocum, "
            + "    n.cod_cliente,c.xnombre, d.cod_merca, m.xdesc, d.cant_cajas, d.cant_unid, "
            + "    CASE n.cconc "
            + "        WHEN 'BON' THEN 0 "
            + "        ELSE d.cant_cajas "
            + "    END as unid_bonif, "
            + "    m.nrelacion, d.igravadas, d.iexentas, d.impuestos, m.npeso_caja, m.npeso_unidad, "
            + "    d.pdesc, p.iprecio_caja as icosto_caja, p.iprecio_unidad as icosto_unidad, "
            + "    n.fac_ctipo_docum, n.nrofact, f.cod_vendedor, v.xnombre as xvendedor,    "
            + "	d.iprecio_caja as icaja_vta, d.iprecio_unid as iunid_vta, d.igravadas+d.iexentas  ttotal ";
        if(!exists){
            sql += " INTO #mostrar2 ";
        }   
        sql += "FROM notas_ventas n, notas_ventas_det d, precios p, clientes c, mercaderias m, facturas f, empleados v "
            + "WHERE n.cod_empr = 2 "
            + "AND (n.mestado = 'A') "
            + "AND (p.cod_depo = 1 )   "
            + "AND f.cod_vendedor = v.cod_empleado "
            + "AND n.nrofact = f.nrofact "
            + "AND f.ffactur = n.ffactur "
            + "AND n.cod_empr = f.cod_empr "
            + "AND f.cod_empr  = 2 "
            + "AND n.fac_ctipo_docum = f.ctipo_docum "
            + "AND (n.fdocum BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat)
            + "' AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' ) "
            + "AND n.nro_nota = d.nro_nota "
            + "AND n.ctipo_docum = d.ctipo_docum "
            + "AND n.fdocum = d.fdocum "
            + "AND n.cod_empr = d.cod_empr "
            + "AND d.cod_merca = p.cod_merca "
            + "AND p.frige_desde <= n.fdocum "
            + "AND p.frige_hasta >= n.fdocum	 "
            + "AND d.cod_merca = m.cod_merca "
            + "AND p.ctipo_vta = 'X' "
            + "AND n.cod_cliente = c.cod_cliente	 "
            + "AND n.cconc IN ('BON','DPR','DIP','INT') "
            + "AND n.nro_promo = " + nroPromo + "  ";
        if(Objects.nonNull(canalVenta)) {
            sql += " AND f.cod_canal  = '" + canalVenta.getCodCanal() + "' ";
        }
        if(!tipoDocumento.isEmpty()){
            sql += " AND n.ctipo_docum  = '" + tipoDocumento + "' ";
        }
        stmt.execute(sql);
    }

    private void populateTempTableImpDatosMostrar2(Statement stmt,
            Promociones promoSelected, Boolean sinIVA) throws SQLException {
        Boolean exists;
        String sql = "";
        exists = checkIfExistTemp(stmt, "#impdatos");
        if(exists){
            sql = "INSERT INTO #impdatos ";
        }
        sql += "SELECT m.ctipo_docum, m.ndocum, m.cconc, '" + promoSelected.getXdesc() + "' as xdesc_promo, "
            + "    m.fac_ctipo_docum, m.nrofact, m.nro_promo, m.fdocum, "
            + "    m.cod_cliente, m.xnombre, m.cod_merca, "
            + "    m.xdesc, m.cant_cajas, "
            + "    CASE m.cconc "
            + "        WHEN 'BON' THEN 0 "
            + "        ELSE m.cant_unid "
            + "    END as cant_unid, "
            + "    m.unid_bonif, m.npeso_caja, m.npeso_unidad, "
            + "    CASE m.cconc "
            + "        WHEN 'DIP' "
            + "        THEN "
            + "            CASE "
            + "                WHEN m.impuestos < 0 "
            + "                THEN m.igravadas + m.iexentas "
            + "                ELSE m.igravadas + m.iexentas + m.impuestos "
            + "            END "
            + "        ELSE 0 "
            + "    END as itotal, "
            + "    m.cod_vendedor, m.xvendedor, m.ttotal, "
            + "((m.npeso_caja * m.cant_cajas) + (m.npeso_unidad * m.cant_unid)) as ntot_peso, "
            + "CASE "
            + "    WHEN m.unid_bonif > 0 THEN (m.npeso_unidad * m.unid_bonif) "
            + "    ELSE 0 "
            + "END as ntot_pbonif, "
            + "CASE "
            + "	WHEN m.unid_bonif > 0 AND ('' = '" + promoSelected.getMcalculo() + "' OR '" + promoSelected.getMcalculo() + "' is null OR '" + promoSelected.getMcalculo() + "' = 'C') THEN (m.icosto_unidad * m.unid_bonif) "
            + "	WHEN '" + promoSelected.getMcalculo() + "' = 'N' AND m.pdesc = 0 THEN m.ttotal "
            + "	ELSE 0 "
            + "END as ibonific, "
            + "CASE "
            + "	WHEN ('' = '" + promoSelected.getMcalculo() + "' OR '" + promoSelected.getMcalculo() + "' is null OR '" + promoSelected.getMcalculo() + "' = 'C') AND m.pdesc > 0 THEN ((m.pdesc/100) * ((m.icosto_caja * m.cant_cajas) + (m.icosto_unidad * m.cant_unid))) "
            + "	WHEN '" + promoSelected.getMcalculo() + "' = 'N' AND m.pdesc = 0 THEN m.ttotal "
            + "	ELSE 0 "
            + "END as idescto, "
            + "    CASE m.cconc "
            + "        WHEN 'DIP' THEN 0 "
            + "        ELSE m.pdesc "
            + "    END as pdesc, ";
        if(!sinIVA) { // con IVA
            sql += "(m.icosto_caja + (m.icosto_caja * ISNULL(i.pimpues, 0)/100)) as icosto_caja, "
                + "(m.icosto_unidad + (m.icosto_unidad * ISNULL(i.pimpues, 0)/100)) as icosto_unidad, "
                + "m.icaja_vta, m.iunid_vta ";
        } else { //sin IVA
            sql += "(m.icaja_vta/(1+(ISNULL(i.pimpues, 0)/100))) as icaja_vta, "
                + "(m.iunid_vta/(1+(ISNULL(i.pimpues, 0)/100))) as iunid_vta, "
                + "m.icosto_caja, m.icosto_unidad ";
        }
        if(!exists){
            sql += "INTO #impdatos ";
        }
        sql += "FROM #mostrar2 m "
            + "INNER JOIN (merca_impuestos mi "
            + "			LEFT JOIN impuestos i "
            + " 				ON mi.cod_impu = i.cod_impu) "
            + "ON m.cod_merca = mi.cod_merca ";
        stmt.execute(sql);
    }

    private void createTempTableMostrar1(Statement stmt, Date fechaDesde,
            String dateFormat, Date fechaHasta, Long nroPromo,
            CanalesVenta canalVenta) throws SQLException {
        Boolean exists;
        String sql = "";
        exists = checkIfExistTemp(stmt, "#mostrar1");
        if(exists){
            sql = "INSERT INTO #mostrar1 ";
        }
        sql += "SELECT "
            + "   f.nro_pedido as ndocum, convert(char(3),'') as cconc, d.nro_promo, f.fpedido as fdocum, "
            + "   d.cod_merca, d.cant_cajas, d.cant_unid, d.unid_bonif, r.cod_zona, "
            + "   d.xdesc, d.pdesc, d.precio_caja as icaja_vta, d.precio_unidad as iunid_vta, p.iprecio_caja as icosto_caja, "
            + "   p.iprecio_unidad as icosto_unidad, 'PED' as ctipo_docum, "
            + "   c.cod_cliente, c.xnombre, f.cod_vendedor, v.xnombre as xvendedor, "
            + "   m.npeso_caja, m.npeso_unidad, (d.igravadas + d.iexentas) as ttotal, "
            + "   CASE "
            + "       WHEN d.impuestos < 0 "
            + "       THEN d.igravadas + d.iexentas "
            + "       ELSE d.igravadas + d.iexentas + d.impuestos "
            + "   END as itotal ";
        if(!exists){
            sql += " INTO #mostrar1 ";
        }
        sql += "FROM pedidos f, pedidos_det d, mercaderias m, precios p, clientes c, rutas r, empleados v "
            + "WHERE (f.cod_empr = 2) "
            + "AND v.cod_empleado = f.cod_vendedor "
            + "AND d.cod_merca = m.cod_merca "
            + "AND f.mestado IN ('N','E') "
            + "AND (p.cod_depo = 1 ) "
            + "AND f.cod_empr = 2 "
            + "AND f.cod_cliente = c.cod_cliente "
            + "AND c.cod_ruta = r.cod_ruta "
            + "AND d.cod_empr  = 2 "
            + "AND (F.fpedido BETWEEN '" + DateUtil.dateToString(fechaDesde, dateFormat)
            + "' AND '" + DateUtil.dateToString(fechaHasta, dateFormat) + "' ) "
            + "AND f.fpedido > CAST(CONVERT(VARCHAR,GETDATE(),112) AS DATETIME) "
            + "AND f.nro_pedido = d.nro_pedido "
            + "AND f.cod_empr = d.cod_empr "
            + "AND d.cod_merca = p.cod_merca "
            + "AND p.frige_desde <= f.fpedido "
            + "AND p.frige_hasta >= f.fpedido "
            + "AND p.ctipo_vta = 'X' "
            + "AND d.nro_promo = " + nroPromo + " ";
        if(Objects.nonNull(canalVenta)) {
            sql += " AND f.cod_canal  = '" + canalVenta.getCodCanal() + "' ";
        }
        stmt.execute(sql);
    }

    public String generateSelectImpDatos() {
        String sql = "SELECT "
                    + "    ctipo_docum, ndocum, cconc, xdesc_promo, fac_ctipo_docum, "
                    + "    nrofact, nro_promo, fdocum, cod_cliente, xnombre, cod_merca, "
                    + "    xdesc, cant_cajas, cant_unid,  unid_bonif, npeso_caja, "
                    + "    npeso_unidad, itotal, cod_vendedor, xvendedor, ttotal, "
                    + "    ntot_peso, ntot_pbonif, ibonific, idescto, pdesc, icosto_unidad, "
                    + "    icosto_caja, icaja_vta, iunid_vta  "
                    + "FROM #impdatos "
                    + "ORDER BY nro_promo, ctipo_docum, ndocum ";
        return sql;
    }
}
