package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
import entidad.Empleados;
import entidad.EmpleadosPK;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class genMasPedidos {
    
    private Empleados vendedor;
    
    private Date fecha;
    
    Integer cliMigrados ,cliProcesados,cliRechazados;
    Integer pedMigrados ,pedProcesados,pedRechazados;
    
    List<Object[]> tmpClientes , tmpPedidos;
    
    @EJB
    private EmpleadosFacade vendedoresFacade;
    @EJB
    private ExcelFacade excelFacade;    
    
    
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.vendedor = new Empleados(new EmpleadosPK());
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("DAY: "+dayOfWeek+" time: "+timeOfDay);
        if(timeOfDay > 12){
            now = new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+1);
        }
        if(dayOfWeek == 7){
            now = new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+2);
        }
        this.fecha = now;
        this.cliMigrados = 0;
        this.cliProcesados = 0;
        this.cliRechazados = 0;
        this.pedMigrados = 0;
        this.pedProcesados = 0;
        this.pedRechazados = 0;
    }
    
    public void ejecutar(String tipo) {
        System.out.println("HOLAS");
        LlamarReportes rep = new LlamarReportes();
        Short codVendedor = null;
        if(this.vendedor != null){
            codVendedor = this.vendedor.getEmpleadosPK().getCodEmpleado();   
        }
        this.nuevosClientes(codVendedor);
        this.nuevosPedidos(codVendedor);
//        this.tmpPedidos = this.datosPendientesPedidos(codVendedor,this.getFecha());
//        String fdesde = dateToString(fecha);
//        String extras = "" , extras2 = "";
//        if (this.vendedor != null){
//            extras += " AND f.cod_vendedor = "+this.vendedor.getEmpleadosPK().getCodEmpleado()+" ";
//        }
//        List<Object[]> lista = new ArrayList<Object[]>();
//        String[] columnas = null;
//        String sql = "";
//        lista = excelFacade.listarParaExcel(sql);
//        rep.exportarExcel(columnas, lista, "KCLIDOS");
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
    
    public List<Empleados> getListVendedores(){
        return vendedoresFacade.getEmpleadosVendedoresPedidos(2);
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Empleados getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleados vendedor) {
        this.vendedor = vendedor;
    }

    public Integer getCliMigrados() {
        return cliMigrados;
    }

    public void setCliMigrados(Integer cliMigrados) {
        this.cliMigrados = cliMigrados;
    }

    public Integer getCliProcesados() {
        return cliProcesados;
    }

    public void setCliProcesados(Integer cliProcesados) {
        this.cliProcesados = cliProcesados;
    }

    public Integer getCliRechazados() {
        return cliRechazados;
    }

    public void setCliRechazados(Integer cliRechazados) {
        this.cliRechazados = cliRechazados;
    }

    public Integer getPedMigrados() {
        return pedMigrados;
    }

    public void setPedMigrados(Integer pedMigrados) {
        this.pedMigrados = pedMigrados;
    }

    public Integer getPedProcesados() {
        return pedProcesados;
    }

    public void setPedProcesados(Integer pedProcesados) {
        this.pedProcesados = pedProcesados;
    }

    public Integer getPedRechazados() {
        return pedRechazados;
    }

    public void setPedRechazados(Integer pedRechazados) {
        this.pedRechazados = pedRechazados;
    }

    public List<Object[]> tipocliDepositos(){
        String sql = "SELECT * FROM tipocli_depositos t, depositos d WHERE t.cod_empr = 2 AND t.cod_depo = d.cod_depo AND "+
                "t.cod_empr = d.cod_empr AND mtipo = 'F' AND t.cod_depo IN (1,3,22) ";
        List<Object[]> lista = excelFacade.listarParaExcel(sql);
        return lista;
    }
    
    public List<Object[]> promocionesEscala(Date pedidofecha){
        String fecha = this.dateToString(pedidofecha);
        String sql = "SELECT * FROM promociones WHERE cod_empr = 2 AND mtipo = 'F' AND frige_desde <= '"+fecha+"' AND frige_hasta >= '"+fecha+"' ";
        List<Object[]> lista = excelFacade.listarParaExcel(sql);
        return lista;
    }
    
    public List<Object[]> tiposDocumentos(){
        String sql = "SELECT * FROM tipos_documentos ";
        List<Object[]> lista = excelFacade.listarParaExcel(sql);
        return lista;
    }
    
    public List<Object[]> canalesVendedores(){
        String sql = "SELECT * FROM canales_vendedores WHERE cod_empr = 2 ";
        List<Object[]> lista = excelFacade.listarParaExcel(sql);
        return lista;
    }
    
    public List<Object[]> datosGenerales(Date fechaHoy){
        String fecha = this.dateToString(fechaHoy);
        String sql = "SELECT * FROM datos_generales WHERE cod_empr = 2 AND frige_desde <= '"+fecha+"' AND frige_hasta >= '"+fecha+"' ";
        List<Object[]> lista = excelFacade.listarParaExcel(sql);
        return lista;
    }
    
    public List<Object[]> datosPendientesClientes(Short codVendedor){
        String sql = "SELECT * FROM tmp_clientes WHERE (ESTADO= '' OR ESTADO = 'R') and autorizado = 'S' ";
        if ( codVendedor !=null){
            sql += " AND cod_vendedor = "+codVendedor+" ";
        }
        List<Object[]> lista = excelFacade.listarParaExcel(sql);
        return lista;
    }
    
    public List<Object[]> datosPendientesPedidos(Short codVendedor ,Date fechaPedido){
        String fecha = this.dateToString(fechaPedido);
        String sql = " SELECT * FROM tmp_pedidos WHERE (ESTADO= '' OR ESTADO = 'R') AND facfecha = '"+fecha+"' ";
        if ( codVendedor !=null){
            sql += " AND cod_vendedor = "+codVendedor+" ";
        }
        sql += " order by nroped ";
        List<Object[]> lista = excelFacade.listarParaExcel(sql);
        return lista;
    }
    
    public void nuevosClientes(Short codVendedor){
        this.tmpClientes = this.datosPendientesClientes(codVendedor);
        for(Object[] obj : tmpClientes){
            Short codRuta = null , cliOrden = null , codBanco = null;
            String error = null, cliTipo = null , cliRuc = null , cliTel = null , cliFax = null , cliDire = null , cliDueno = null ,
                cliNroCta = null ,cliObs = null , cliNom = null;
            this.setCliProcesados(this.getCliProcesados()+1);
            String sql = "select * from rutas where xdesc = "+obj[8]+" ";
            List<Object[]> rutas = excelFacade.listarParaExcel(sql);
            if(rutas.isEmpty()){
                error = "No existe Ruta del Cliente";
            }else{
                codRuta = (Short) rutas.get(0)[2];
            }
            if (obj[12] !=null){
                sql = " select * from bancos where xdesc = "+obj[12]+" ";
                List<Object[]> bancos = excelFacade.listarParaExcel(sql);
                if(bancos.isEmpty()){
                    error = " No existe Banco del Cliente";
                }else{
                    codBanco = (Short) bancos.get(0)[0];
                }
            }
            cliNom = (String) obj[2];
            cliTipo = (String) obj[4];
            cliRuc = (String) obj[3];
            cliTel = (String) obj[5];
            cliFax = (String) obj[6];
            cliDire = (String) obj[7];
            cliOrden = (Short) obj[9];
            cliDueno = (String) obj[11];
            cliNroCta = (String) obj[14];
            cliObs = (String) obj[10];
            sql = " INSERT INTO clientes (cod_empr,xnombre, cod_ruta, ctipo_cliente, xruc, xtelef, xfax, xdirec, cod_ciudad, norden_ruta," +
                " xpropietario, mforma_pago, cod_estado, xctacte, cod_banco, nriesgo, ilimite_compra,isaldo, xobs, nplazo_credito) " +
                " VALUES ( 2,'"+cliNom+"', "+codRuta+",'"+cliTipo+"','"+cliRuc+"','"+cliTel+"','"+cliFax+"','"+cliDire+"',1,"+cliOrden+",'"+cliDueno+
                "', 'F', 'A','"+cliNroCta+"',"+codBanco+", 0, 0, 0,'"+cliObs+"', 0)";
            Integer res = excelFacade.executeInsert(sql);
            if (res != null){
                sql = " SELECT @@IDENTITY AS cod_cliente ";
                List<Object[]> resp = excelFacade.listarParaExcel(sql);
                if(resp.isEmpty()){
                    error = " Error en busqueda de Nro. de Cliente Grabado";
                }else{
                    sql = "INSERT INTO ventas_clientes(cod_empr,cod_cliente, ctipo_vta ) VALUES (2,"+resp.get(0)[0]+", 'D') ";
                    List<Object[]> ventaCliente = excelFacade.listarParaExcel(sql);
                    if(ventaCliente.isEmpty()){
                        sql = " update tmp_clientes set codnuevo = "+resp.get(0)[0]+", cod_ruta = "+codRuta+", fec_proc = '"
                            +this.dateToString(new Date())+"', msg_error = '' , estado = 'M' "+
                            "where clicod = '"+obj[0]+"' and cod_vendedor = "+codVendedor+" ";
                        Integer migrados = excelFacade.executeInsert(sql);
                        this.setCliMigrados(this.getCliMigrados()+1);
                    }else{
                        sql = " update tmp_clientes set codnuevo = "+resp.get(0)[0]+" , cod_ruta = "+codRuta+", fec_proc = '"+
                            this.dateToString(new Date())+"' , msg_error = '' , estado = 'R' "+
                            " where clicod = '"+obj[0]+"' and cod_vendedor = "+codVendedor+" ";
                        Integer rechazados = excelFacade.executeInsert(sql);
                        this.setCliRechazados(this.getCliRechazados()+1);
                    }
                }
            }
        }
    }

    public void nuevosPedidos(Short codVendedor){
        this.tmpPedidos = this.datosPendientesPedidos(codVendedor,this.getFecha());
        for(Object[] obj : tmpPedidos){
            Short codVendedorX = (codVendedor==null)?(Short)obj[0]:codVendedor;
            Short nroPromo = null ;
            String artcod = null;
            String sql = null;
            this.setPedProcesados(this.getPedProcesados()+1);
            if(obj[35]!=null){
                nroPromo = (Short) obj[35];
                sql = " select frige_desde,frige_hasta from promociones where nro_promo = "+nroPromo;
                List<Object[]> promo = excelFacade.listarParaExcel(sql);   
                Timestamp desde = (Timestamp) promo.get(0)[0];
                Timestamp hasta = (Timestamp) promo.get(0)[1];
                if(this.getFecha().getTime() >= desde.getTime() && this.getFecha().getTime() <= hasta.getTime()){
                    artcod = (String) obj[7];
                    sql = "select * from mercaderias where cod_merca = '"+artcod+"'";
                    List<Object[]> mercaderias = excelFacade.listarParaExcel(sql);
                    if(mercaderias.isEmpty()){
                        //No existe la mercaderia en Mercaderias
                        //Rechazar con estado 'R' en pediidos tmp                                
                    }
                    if(obj[34]==null){
                        //Canal del Pedido no definido
                        //Rechazar con estado 'R' en pediidos tmp        
                    }
                    BigDecimal tmpCodcanal = (BigDecimal) obj[34];
                    sql = " select cod_canal from merca_canales where cod_merca = "+artcod;
                    List<Object[]> canales = excelFacade.listarParaExcel(sql);
                    BigDecimal ArtCodcanal = (BigDecimal) canales.get(0)[0];
                    if(tmpCodcanal.compareTo(ArtCodcanal)!=0){
                        //Articulo no pertenece al canal del pedido
                        //Rechazar con estado 'R' en pediidos tmp        
                    }
                    sql = " select * from canales_vendedores where cod_vendedor = "+codVendedorX+" and cod_canal = "+canales.get(0)[0]+" order by cod_canal ";
                    List<Object[]> canalesVendedores = excelFacade.listarParaExcel(sql);
                    if(canalesVendedores.isEmpty()){
                        Short codNuevo = null;
                        codNuevo = (Short) obj[32];
                        if(codNuevo==null){
                            sql = " select codnuevo  from tmp_clientes where clicod = cliCod and cod_vendedor = "+codVendedorX+" ";
                            List<Object[]> tmpCodNuevo = excelFacade.listarParaExcel(sql);
                            codNuevo = (Short) tmpCodNuevo.get(0)[0];
                        }
                        sql = " select cod_cliente, cod_estado, mforma_pago, nplazo_impresion, nplazo_credito,ctipo_cliente,ilimite_compra,cod_ruta from clientes where cod_cliente = "+codNuevo+" ";
                        List<Object[]> tmpCodNuevo = excelFacade.listarParaExcel(sql);
                        if ( tmpCodNuevo.get(0)[1].equals("A") ) {
                            sql = " select * from ventas_clientes where cod_cliente = "+tmpCodNuevo.get(0)[0]+" and ctipo_vta = '"+obj[5]+"' ";
                            List<Object[]> ventasCliente = excelFacade.listarParaExcel(sql);
                            String mFormaPago = (String) tmpCodNuevo.get(0)[2];
                            BigDecimal nPlazoImpresion = (BigDecimal) tmpCodNuevo.get(0)[3];
                            BigDecimal nPlazoCheque = (BigDecimal) obj[36];
                            BigDecimal nplazoCredito = (BigDecimal) tmpCodNuevo.get(0)[4];
                            BigDecimal ilimiteCompra = (BigDecimal) tmpCodNuevo.get(0)[6];
                            if (mFormaPago.equals("C") && nPlazoImpresion.compareTo(BigDecimal.ZERO) <=0 && nPlazoCheque==null ){
                                sql = " update tmp_pedidos set nplazo_cheque = "+nPlazoImpresion+" , codnuevo = "+tmpCodNuevo.get(0)[0]+" where facnro = "+obj[0];
                                Integer exq = excelFacade.executeInsert(sql);
                                nPlazoCheque = nPlazoImpresion;
                            }
                            Boolean dont = false;
                            for(String s : "0,5,8,15,18,20,21,25,30,38,45".split(",")){
                                if(!s.equals(nPlazoCheque.toString())){
                                    dont = true;
                                }
                            }
                            if(!dont){
                                String factipfac = (String) obj[27];
                                String lctipoDocum = factipfac.equals("Contado")?"FCO":((factipfac.equals("Credito")||factipfac.equals("Crédito")))?"FCR":"";
                                if(mFormaPago.equals("C") && !lctipoDocum.equals("FCO")){
                                    //Tipo de Factura invalido y/o Plazo del Cheque
                                    //Rechazar con estado 'R' en pediidos tmp         
                                    
                                }
                                if(mFormaPago.equals("F") && !lctipoDocum.equals("FCR")){
                                    // tipo de factura invalido
                                    //Rechazar con estado 'R' en pediidos tmp         
                                    
                                }
                                if(mFormaPago.equals("T") && (!lctipoDocum.equals("FCO") || nPlazoCheque.compareTo(BigDecimal.ZERO) > 0)){
                                    //tipo de Factura invalido y / o Plazo del Cheque
                                    //Rechazar con estado 'R' en pediidos tmp         
                                    
                                }
                                if(mFormaPago.equals("") && (!lctipoDocum.equals("FCO") || nPlazoCheque.compareTo(BigDecimal.ZERO) > 0)){
                                    //tipo de Factura invalido y / o Plazo del Cheque
                                    //Rechazar con estado 'R' en pediidos tmp         
                                                                        
                                }
                                if(nPlazoCheque.compareTo((BigDecimal) tmpCodNuevo.get(0)[3]) >0){
                                    //Plazo del cheque  superior al plazo credito
                                    //Rechazar con estado 'R' en pediidos tmp         
                                
                                }
                                sql = " select cod_depo from tipocli_depositos where ctipo_cliente = "+tmpCodNuevo.get(0)[5];
                                List<Object[]> codDepo = excelFacade.listarParaExcel(sql);
                                String gCodCanal = (String) canales.get(0)[0];
                                Short mdepo = null;
                                for(Object[] depo : codDepo){
                                    mdepo = (Short) depo[0];
                                    if(gCodCanal.equals("AJ") && mdepo.intValue()==22){
                                        break;
                                    }
                                    if(gCodCanal.equals("T") && mdepo.intValue()==1){
                                        break;
                                    }
                                }
                                if((gCodCanal.equals("AJ") && mdepo.intValue()!=22) || (gCodCanal.equals("T") && mdepo.intValue()!=1)){
                                    //" Canal de venta y deposito no corresponden"
                                    //Rechazar con estado 'R' en pediidos tmp         
                                }
                                sql=" select * from depositos where mtipo ='M' and cod_zona =(select cod_zona from rutas where cod_ruta = "+
                                        tmpCodNuevo.get(0)[7]+")";
                                List<Object[]> deposito = excelFacade.listarParaExcel(sql);
                                if(deposito.isEmpty()){
                                    //No existe camion relacionado a Zona 
                                    //Rechazar con estado 'R' en pediidos tmp 
                                    
                                }
                                Short camion = (Short) deposito.get(0)[0];
                                if (!(nplazoCredito.compareTo(BigDecimal.ZERO) <=0 || ilimiteCompra.compareTo(BigDecimal.ZERO) <=0 || 
                                        !mFormaPago.equals("F")) && (factipfac.equals("Credito") || factipfac.equals("Crédito"))){
                                    //El tipo de Factura solo puede ser contado
                                    //Rechazar con estado 'R' en pediidos tmp 
                                }
                                if(obj[10]==null && obj[11]==null && obj[12]==null && obj[13]==null){
                                    //No existen cajas ni unidades pedidas
                                    //Rechazar con estado 'R' en pediidos tmp        
                                }
                                sql="SELECT e.*, m.xdesc FROM existencias e,mercaderias m WHERE e.cod_depo ="+camion+" AND e.cod_merca = '"+artcod+
                                        "' AND e.cod_empr  = 2  AND e.cod_empr = m.cod_empr AND e.cod_merca = m.cod_merca ";
                                List<Object[]> existencias = excelFacade.listarParaExcel(sql);                                
                                if(existencias.isEmpty()){
                                    //No existe el articulo en el deposito
                                    //Rechazar con estado 'R' en pediidos tmp                                     
                                }
                                BigDecimal deCantidadCajas = (BigDecimal) obj[10];
                                BigDecimal deCantidadUnidad = (BigDecimal) obj[11];
                                BigDecimal deCajBonif = (BigDecimal) obj[12];
                                BigDecimal deUniBonif = (BigDecimal) obj[13];
                                Short nrelacion = (Short) existencias.get(0)[8];
                                BigDecimal BigNrelacion = new BigDecimal(nrelacion);
                                BigDecimal lPedUnid = deCantidadCajas.multiply(BigNrelacion).add(deCantidadUnidad).add(deUniBonif).add(deCajBonif.multiply(BigNrelacion));
                                sql="select (cant_cajas*nrelacion)+cant_unid as cantTotal from existencias where cod_empr = 2 and cod_merca = '"+artcod+
                                        "' and cod_depo = "+camion+" and nrelacion = "+nrelacion+" ";
                                List<Object[]> calReservas = excelFacade.listarParaExcel(sql);                                
                                BigDecimal lTotUnid = (BigDecimal) calReservas.get(0)[0];
                                if( lTotUnid.compareTo(deCantidadCajas.multiply(BigNrelacion).add(deCantidadUnidad))> 0){
                                    BigDecimal lresAnt = lTotUnid.subtract(lPedUnid);
                                    BigDecimal existenciasCanCajas = (BigDecimal) existencias.get(0)[3];
                                    BigDecimal existenciasNrelacion = (BigDecimal) existencias.get(0)[8];
                                    BigDecimal existenciasCanUnid = (BigDecimal) existencias.get(0)[4];
                                    if(existenciasCanCajas.multiply(existenciasNrelacion).
                                        add(existenciasCanUnid).subtract(lresAnt).compareTo(BigDecimal.ZERO)>0){
                                        BigDecimal e1 = existenciasCanCajas.multiply(existenciasNrelacion).add(existenciasCanUnid).subtract(lresAnt);
                                        BigDecimal e2 = deCantidadCajas.multiply(existenciasNrelacion).add(deCantidadUnidad);
                                        if(e1.compareTo(e2)>0){
                                            BigDecimal lDifUnd = existenciasCanCajas.multiply(existenciasNrelacion).add(existenciasCanUnid).subtract(lresAnt).
                                                    subtract(deCantidadCajas.multiply(existenciasNrelacion)).add(deCantidadUnidad);
                                            
                                            if(lDifUnd.compareTo(BigDecimal.ZERO)>0){
                                                if(deUniBonif.add(deCajBonif.multiply(existenciasNrelacion)).compareTo(BigDecimal.ZERO) > 0){
                                                    lTotUnid = lTotUnid.subtract(deUniBonif.add(deCajBonif.multiply(existenciasNrelacion)));
                                                    Integer lNewCajBonifINT = lDifUnd.divide(existenciasNrelacion).intValue();
                                                    BigDecimal lNewCajBonif = new BigDecimal(lNewCajBonifINT);
                                                    BigDecimal lNewUnidBonif = lDifUnd.subtract(existenciasNrelacion.multiply(lNewCajBonif));
                                                    BigDecimal c1 = lNewCajBonif.multiply(existenciasNrelacion).add(lNewUnidBonif);
                                                    BigDecimal c2 = deUniBonif.add(deCajBonif.multiply(existenciasNrelacion));
                                                    if(c2.compareTo(BigDecimal.ZERO)>0){
                                                        if(c1.compareTo(c2)>0){
                                                            lNewCajBonif = BigDecimal.ZERO;
                                                            lNewUnidBonif = BigDecimal.ZERO;
                                                        }
                                                    }
                                                    lTotUnid = lTotUnid.add(lNewCajBonif.multiply(existenciasNrelacion)).add(lNewUnidBonif);
                                                    deCajBonif = lNewCajBonif;
                                                    deUniBonif = lNewUnidBonif;
                                                    //Cantidad modificada
                                                    //Rechazar con estado 'R' en pediidos tmp         
                                                }
                                            }
                                        }else{
                                            lresAnt = lTotUnid.subtract(lPedUnid);
                                            BigDecimal lTotDepo = existenciasCanCajas.multiply(existenciasNrelacion).add(existenciasCanUnid).subtract(lresAnt);
                                            BigDecimal lNewCajas = new BigDecimal(lTotDepo.divide(existenciasNrelacion).intValue());
                                            BigDecimal lNewUnid = lTotDepo.subtract(lNewCajas.multiply(existenciasNrelacion));
                                            BigDecimal lNewCajasBonif = BigDecimal.ZERO;
                                            BigDecimal lNewUnidBonif = BigDecimal.ZERO;
                                            lTotUnid = lresAnt.add(lNewCajas.multiply(existenciasNrelacion)).add(lNewUnid);
                                            deCantidadCajas = lNewCajas;
                                            deCantidadUnidad = lNewUnid;
                                            deCajBonif = lNewCajasBonif;
                                            deUniBonif = lNewUnidBonif;
                                            //Cantidad modificada
                                            //Rechazar con estado 'R' en pediidos tmp        
                                        }
                                    }else{
                                        //Cant Insuf en el deposito
                                        //Rechazar con estado 'R' en pediidos tmp        
                                    }
                                }
                                sql = "INSERT INTO pedidos (cod_empr, cod_cliente, ctipo_docum, ctipo_vta, cod_vendedor,cod_canal, cod_ruta, cod_depo,  fpedido,"+
                                        "npeso_acum, texentas, tgravadas, timpuestos,tdescuentos, ttotal, mestado, morigen, nplazo_cheque)"+
                                        "VALUES ( 2,"+tmpCodNuevo.get(0)[0]+",'"+lctipoDocum+"','"+obj[5]+"',"+codVendedorX+","+canales.get(0)[0]+","+tmpCodNuevo.get(0)[7]+
                                        ","+camion+",'"+fecha+"', 0,0, 0, 0, 0, 0, 'N','P',"+obj[36]+")";
                                excelFacade.executeInsert(sql);
                            }else{
                                //Plazo del cheque no se encuentra en el rango
                                //Rechazar con estado 'R' en pediidos tmp         
                                
                            }
                        } else {
                            // cliente inactivo
                            //Rechazar con estado 'R' en pediidos tmp                        
                        }
                    }else{
                        //El canal no corresponde al vendedor
                        //Rechazar con estado 'R' en pediidos tmp                        
                    }
                }else{
                    //Rechazar con estado 'R' en pediidos tmp
                }
            }
        }
    }
}
