package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
import entidad.Empleados;
import entidad.EmpleadosPK;
import java.math.BigDecimal;
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
            String sql = null;
            this.setPedProcesados(this.getPedProcesados()+1);
            if(obj[35]!=null){
                sql = " select frige_desde,frige_hasta from promociones where nro_promo = "+obj[35];
                List<Object[]> promo = excelFacade.listarParaExcel(sql);   
                Timestamp desde = (Timestamp) promo.get(0)[0];
                Timestamp hasta = (Timestamp) promo.get(0)[1];
                if(this.getFecha().getTime() >= desde.getTime() && this.getFecha().getTime() <= hasta.getTime()){
                    
                }else{
                    
                }
            }
        }
    }
}
