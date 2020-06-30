package bean.listados;

import dao.CanalesVendedoresFacade;
import dao.ClientesFacade;
import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.ExistenciasFacade;
import dao.MercaderiasFacade;
import dao.PedidosFacade;
import dao.PreciosFacade;
import dao.PromocionesFacade;
import dao.RutasFacade;
import dao.TiposClientesFacade;
import dao.TiposDocumentosFacade;
import entidad.Clientes;
import entidad.Depositos;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.Existencias;
import entidad.Mercaderias;
import entidad.Precios;
import entidad.Promociones;
import entidad.Rutas;
import entidad.TiposDocumentos;
import entidad.TmpPedidos;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class genMasPedidos implements Serializable{
    
    private Empleados vendedor;
    
    private Date fecha;
    
    Integer cliMigrados ,cliProcesados,cliRechazados;
    Integer pedMigrados ,pedProcesados,pedRechazados;
    
    List<Object[]> tmpClientes , tmpPedidos;
    
    @EJB
    private EmpleadosFacade vendedoresFacade;
    @EJB
    private ExcelFacade excelFacade;    
    @EJB
    private PedidosFacade pedidoFacade;
    @EJB
    private PromocionesFacade promocionesFacade;
    @EJB
    private MercaderiasFacade mercaderiasFacade;
    @EJB
    private CanalesVendedoresFacade canalesVendedoresFacade;
    @EJB
    private ClientesFacade clientesFacade;
    @EJB
    private TiposClientesFacade tipoClienteFacade;
    @EJB
    private RutasFacade rutasFacade;
    @EJB
    private DepositosFacade depositosFacade;
    @EJB
    private ExistenciasFacade existenciasFacade;
    @EJB
    private PreciosFacade preciosFacade;
    @EJB
    private TiposDocumentosFacade tipoDocumentoFacade;
    
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
        LlamarReportes rep = new LlamarReportes();
        Short codVendedor = null;
        if(this.vendedor != null){
            codVendedor = this.vendedor.getEmpleadosPK().getCodEmpleado();   
        }
        this.nuevosClientes(codVendedor);
        this.nuevosPedidos(codVendedor,this.fecha);
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

    public void nuevosPedidos(Short codVendedor,Date fecha){
        List<TmpPedidos> pedidos = this.pedidoFacade.datosPendientesPedidos(codVendedor,fecha);
        for(TmpPedidos pedido : pedidos){
            Short codVendedorX = (codVendedor==null)?pedido.getCodVendedor():codVendedor;
            String sql = null;
            this.setPedProcesados(this.getPedProcesados()+1);
            if(pedido.getNroPromo()!=null){
                List<Promociones> promociones = this.promocionesFacade.findByNroPromo(pedido.getNroPromo().toString(),"2");
                Promociones promoActual = promociones.get(0);
                if(fecha.getTime() >= promoActual.getFrigeDesde().getTime() && fecha.getTime() <= promoActual.getFrigeHasta().getTime()){
                    Mercaderias mercaderia = this.mercaderiasFacade.buscarPorCodigoMercaderia(pedido.getArtcod());
                    if(mercaderia == null){
                        pedido.setMsgError("No existe la mercaderia en Mercaderias");
                        pedido.setEstado("R");
                    }else{
                        if(pedido.getCodCanal() == null){
                            pedido.setMsgError("Canal del Pedido no definido");
                            pedido.setEstado("R");
                        }else{
                            Boolean existeMercaEnCanal =this.mercaderiasFacade.buscarMercaderiaEnCanalDeVenta(pedido.getArtcod(),pedido.getCodCanal());
                            if(!existeMercaEnCanal){
                                pedido.setMsgError("Articulo no pertenece al canal del pedido");
                                pedido.setEstado("R");
                            }else{
                                Boolean existeVendedorCanal = this.canalesVendedoresFacade.buscarCanalEnCanalDeVendedores(codVendedorX,pedido.getCodCanal());
                                if(!existeVendedorCanal){
                                    pedido.setMsgError("El canal no corresponde al vendedor");
                                    pedido.setEstado("R");
                                }else{
                                    if(pedido.getCodnuevo()==null){
                                        Integer codNuevo = this.clientesFacade.tmpClientesCodNuevo(pedido.getClicod(),codVendedorX);
                                        if(codNuevo==null){
                                            Clientes cliente = (Clientes) this.clientesFacade.buscarPorFiltro(pedido.getClicod());
                                            if(!cliente.getCodEstado().equals("A")){
                                                pedido.setMsgError("Cliente inactivo");
                                                pedido.setEstado("R");
                                            }else{
                                                if(cliente.getMformaPago()=='C' && cliente.getNplazoImpresion() <= 0 && pedido.getNplazoCheque()==null){
                                                    pedido.setNplazoCheque(new BigDecimal(cliente.getNplazoImpresion()));
                                                    pedido.setCodnuevo(cliente.getCodCliente());
                                                    //update tmp_pedido
                                                }
                                                Boolean dont = false;
                                                for(String s : "0,5,8,15,18,20,21,25,30,38,45".split(",")){
                                                    if(!s.equals(pedido.getNplazoCheque().toString())){
                                                        dont = true;
                                                    }
                                                }                       
                                                if(!dont){
                                                    String lctipoDocum = pedido.getFactipfac().equals("Contado")?"FCO":(
                                                            (pedido.getFactipfac().equals("Credito")||pedido.getFactipfac().equals("Crédito")))?"FCR":"";
                                                    if(cliente.getMformaPago().equals("C") && !lctipoDocum.equals("FCO")){
                                                        pedido.setMsgError("Tipo de Factura invalido y/o Plazo del Cheque");
                                                        pedido.setEstado("R");
                                                    }else if(cliente.getMformaPago().equals("F") && !lctipoDocum.equals("FCR")){
                                                        pedido.setMsgError("tipo de factura invalido");
                                                        pedido.setEstado("R");      
                                                    }else if(cliente.getMformaPago().equals("T") && (!lctipoDocum.equals("FCO") || 
                                                            pedido.getNplazoCheque().compareTo(BigDecimal.ZERO) > 0)){
                                                        pedido.setMsgError("tipo de Factura invalido y / o Plazo del Cheque");
                                                        pedido.setEstado("R");      
                                                    }else if(cliente.getMformaPago().equals("") && (!lctipoDocum.equals("FCO") || 
                                                            pedido.getNplazoCheque().compareTo(BigDecimal.ZERO) > 0)){
                                                        pedido.setMsgError("tipo de Factura invalido y / o Plazo del Cheque");
                                                        pedido.setEstado("R");      
                                                    }else if(pedido.getNplazoCheque().compareTo(new BigDecimal(cliente.getNplazoCredito())) >0){
                                                        pedido.setMsgError("Plazo del cheque  superior al plazo credito");
                                                        pedido.setEstado("R");      
                                                    }else{
                                                        List<Short> codDepo = this.tipoClienteFacade.getCodDepoByTipoClienteDepo(cliente.getCtipoCliente());
                                                        Short mdepo = codDepo.contains(new Short("22"))&&pedido.getCodCanal().equals("AJ")?new Short("22"):null;
                                                        mdepo = codDepo.contains(new Short("1"))&&pedido.getCodCanal().equals("T")?new Short("1"):null;
                                                        if(mdepo==null) for(Short s  : codDepo) mdepo = s;
                                                        if((pedido.getCodCanal().equals("AJ") && mdepo!=22) || 
                                                                (pedido.getCodCanal().equals("T") && mdepo!=1)){
                                                            pedido.setMsgError("Canal de venta y deposito no corresponden");
                                                            pedido.setEstado("R");          
                                                        }else{
                                                            Rutas ruta = this.rutasFacade.findByCodigo(cliente.getCodRuta());
                                                            List<Depositos> depositos = 
                                                                    this.depositosFacade.findByZonayTipo(ruta.getZonas().getZonasPK().getCodZona(),"M");
                                                            if(depositos.isEmpty()){
                                                                pedido.setMsgError("No existe camion relacionado a Zona ");
                                                                pedido.setEstado("R");      
                                                            }else{
                                                                Short camion = null;
                                                                for(Depositos d : depositos) camion = d.getDepositosPK().getCodDepo();
                                                                if (!(cliente.getNplazoCredito()<=0 || cliente.getIlimiteCompra()<=0 || 
                                                                    !cliente.getMformaPago().equals("F")) && 
                                                                    (pedido.getFactipfac().equals("Credito") || pedido.getFacnro().equals("Crédito"))){
                                                                    pedido.setMsgError("El tipo de Factura solo puede ser contado");
                                                                    pedido.setEstado("R");      
                                                                }else{
                                                                    if(pedido.getDetcancajas()==null && pedido.getDetcanunid()==null && 
                                                                        pedido.getDetcajbonif()==null && pedido.getDetunibonif()==null){
                                                                        pedido.setMsgError("No existen cajas ni unidades pedidas");
                                                                        pedido.setEstado("R");                                                                       
                                                                    }else{
                                                                        Existencias existencas = this.existenciasFacade.
                                                                            buscarexistenciasPorCodigoDepositoMerca(pedido.getArtcod(),camion,"2");
                                                                        if(existencas==null){
                                                                            pedido.setMsgError("No existe el articulo en el deposito");
                                                                            pedido.setEstado("R");      
                                                                        }else{
                                                                            BigDecimal lPedUnid = pedido.getDetcancajas().
                                                                                multiply(new BigDecimal(existencas.getNrelacion())).
                                                                                add(pedido.getDetcanunid()).add(pedido.getDetunibonif()).
                                                                                add(pedido.getDetcajbonif().
                                                                                    multiply(new BigDecimal(existencas.getNrelacion()))); 
                                                                            Long lTotUnid = existencas.getCantCajas()*existencas.getNrelacion()
                                                                                    +existencas.getCantUnid();
                                                                            if(lTotUnid==0) lTotUnid = lPedUnid.longValue();
                                                                            Long lresAnt = lTotUnid - lPedUnid.longValue();
                                                                            if(existencas.getCantCajas()*existencas.getNrelacion()
                                                                                    +existencas.getCantUnid()-lresAnt > 0){
                                                                                if( existencas.getCantCajas()*existencas.getNrelacion()
                                                                                    +existencas.getCantUnid()-lresAnt >
                                                                                    pedido.getDetcancajas().longValue()*existencas.getNrelacion()
                                                                                        +pedido.getDetcanunid().longValue() ){
                                                                                    Long lDifUnid = existencas.getCantCajas()*existencas.getNrelacion()
                                                                                        +existencas.getCantUnid()-lresAnt -
                                                                                        pedido.getDetcancajas().longValue()*existencas.getNrelacion()
                                                                                        +pedido.getDetcanunid().longValue();
                                                                                    if(lDifUnid>0){
                                                                                        if( pedido.getDetunibonif().longValue()+
                                                                                            (pedido.getDetcajbonif().longValue()*
                                                                                                existencas.getNrelacion())>0){
                                                                                            lTotUnid -= pedido.getDetunibonif().longValue()+
                                                                                                (pedido.getDetcajbonif().longValue()*
                                                                                                    existencas.getNrelacion());
                                                                                            Integer lNewCajBonifInt = 
                                                                                                lDifUnid.intValue()/existencas.getNrelacion().intValue();
                                                                                            Long lNewUnidBonif = lDifUnid - 
                                                                                                    (lNewCajBonifInt*existencas.getNrelacion());
                                                                                            if(lNewCajBonifInt*existencas.getNrelacion().longValue()+
                                                                                                lNewUnidBonif > pedido.getDetunibonif().longValue() +
                                                                                                (pedido.getDetcajbonif().longValue()*existencas.getNrelacion())){
                                                                                                lNewCajBonifInt = 0;
                                                                                                lNewUnidBonif = new Long("0");
                                                                                            }
                                                                                            lTotUnid += lNewCajBonifInt*existencas.getNrelacion()+lNewUnidBonif;
                                                                                            pedido.setDetcajbonif(new BigDecimal(lNewCajBonifInt));
                                                                                            pedido.setDetunibonif(new BigDecimal(lNewUnidBonif));
                                                                                            pedido.setMsgError("Cantidad Modificada");
                                                                                            pedido.setEstado("R");
                                                                                        }
                                                                                    } 
                                                                                }else{
                                                                                    lresAnt = lTotUnid - lPedUnid.longValue();
                                                                                    Long lTotDepo = existencas.getCantCajas() * existencas.getNrelacion()+
                                                                                        existencas.getCantUnid() - lresAnt;
                                                                                    Integer lnewCajas = lTotDepo.intValue()/
                                                                                            existencas.getNrelacion().intValue();
                                                                                    Long lNewUnid = lTotDepo - (lnewCajas * existencas.getNrelacion());
                                                                                    lTotUnid = lresAnt - (lnewCajas*existencas.getNrelacion())+lNewUnid;
                                                                                    pedido.setDetcancajas(new BigDecimal(lnewCajas));
                                                                                    pedido.setDetcanunid(new BigDecimal(lNewUnid));
                                                                                    pedido.setDetunibonif(BigDecimal.ZERO);
                                                                                    pedido.setDetcajbonif(BigDecimal.ZERO);
                                                                                    pedido.setMsgError("Cantidad modificada");
                                                                                    pedido.setEstado("R");
                                                                                }
                                                                            }else{
                                                                                pedido.setMsgError("Cantidad Insuficiente en el deposito");
                                                                                pedido.setEstado("R");
                                                                            }
                                                                        }
                                                                        Precios precio = this.preciosFacade.
                                                                            findPreciosByDepoMerca("2",camion,pedido.getArtcod(),pedido.getFactipovta());
                                                                        if(precio==null){
                                                                            pedido.setMsgError("Mercaderia sin precio en lista, deposito.");
                                                                            pedido.setEstado("R");
                                                                        }else{
                                                                            if(precio.getIprecioCaja()==0 || precio.getIprecioUnidad()==0){
                                                                                pedido.setMsgError("Mercaderia con precio caja/unidad = 0");
                                                                                pedido.setEstado("R");                                                                                
                                                                            }else{
                                                                                if(pedido.getDetdescto().compareTo(BigDecimal.ZERO)>0){
                                                                                    BigDecimal maxDescuento = this.clientesFacade.clienteMaxDescuento(
                                                                                        mercaderia.getCodSublinea().getCodSublinea(),pedido.getCodnuevo());
                                                                                    if(maxDescuento.compareTo(pedido.getDetdescto())<0){
                                                                                        pedido.setMsgError("Descuento supera el maximo permitido.");
                                                                                        pedido.setEstado("R");                                                                                
                                                                                    }
                                                                                }
                                                                            }
                                                                            TiposDocumentos tipoDocumento = this.tipoDocumentoFacade.
                                                                                    getTipoDocumentosByCtipo(lctipoDocum);
                                                                            if(tipoDocumento==null){
                                                                                pedido.setMsgError("No existe el documento: "+lctipoDocum);
                                                                                pedido.setEstado("R");                                                                                
                                                                            }
                                                                            Object[] promoDesc = this.promocionesFacade.getNroPromoAndPdesc(pedido.getCodnuevo(),this.fecha, pedido.getFactipovta());
                                                                            Long lExentas = (precio.getIprecioCaja()*pedido.getDetcancajas().longValue())+(precio.getIprecioUnidad()*pedido.getDetcanunid().longValue());
                                                                            
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
//                                sql = "INSERT INTO pedidos (cod_empr, cod_cliente, ctipo_docum, ctipo_vta, cod_vendedor,cod_canal, cod_ruta, cod_depo,  fpedido,"+
//                                        "npeso_acum, texentas, tgravadas, timpuestos,tdescuentos, ttotal, mestado, morigen, nplazo_cheque)"+
//                                        "VALUES ( 2,"+tmpCodNuevo.get(0)[0]+",'"+lctipoDocum+"','"+obj[5]+"',"+codVendedorX+","+canales.get(0)[0]+","+tmpCodNuevo.get(0)[7]+
//                                        ","+camion+",'"+fecha+"', 0,0, 0, 0, 0, 0, 'N','P',"+obj[36]+")";
//                                excelFacade.executeInsert(sql);
//                            }else{
//                                //Plazo del cheque no se encuentra en el rango
//                                //Rechazar con estado 'R' en pediidos tmp         
//                                
//                            }
//                        } else {
//                            // cliente inactivo
//                            //Rechazar con estado 'R' en pediidos tmp                        
//                        }
//                    }else{
//                        //El canal no corresponde al vendedor
//                        //Rechazar con estado 'R' en pediidos tmp                        
//                    }
//                }else{
//                    //Rechazar con estado 'R' en pediidos tmp
//                    //Rechazado por promo
                }
            }
        }
    }
}
