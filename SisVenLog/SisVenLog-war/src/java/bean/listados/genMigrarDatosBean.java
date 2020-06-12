package bean.listados;

import dao.ExcelFacade;
import dao.MovimientosMercaFacade;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Nico
 */
@ManagedBean
@SessionScoped
public class genMigrarDatosBean implements Serializable{
    
    private Date desde;
    private Date hasta;
    
    @EJB
    private MovimientosMercaFacade movimientosMercaFacade;
    
    private final String[] Months = new String[]
        {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    
    private Boolean stock,clientes,proveedores;
    
    @EJB
    private ExcelFacade excelFacade;
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.desde = new Date();
        Date max = movimientosMercaFacade.maxFechaMovimientosMerca();
        Date xmax = new Date(max.getYear(),max.getMonth(),max.getDate()+1);
        this.hasta = xmax;
        
    }
    
    public void ejecutar(String tipo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.hasta);
        cal.add(Calendar.DATE, -300);
        Date limite = cal.getTime();
        if(this.desde.compareTo(limite)<0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", 
                    "Fecha Inicial debe ser Mayor/Igual a "+format.format(limite)));
            return;
        }
        if(this.desde.compareTo(this.hasta)>0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", 
                    "Fecha Inicial debe ser Menor/Igual a Fecha Final"));            
            return;
        }
        Integer MonthNow = (new Date()).getMonth();
        Integer MonthLimit = MonthNow>3?MonthNow-3:0;
        if(this.desde.getMonth() < MonthLimit){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", 
                    "Mes Minimo es "+Months[MonthLimit]));                          
            return;
        }
        Integer MonthInitial = this.desde.getMonth();
        Integer YearInitial = this.desde.getYear();
        while((MonthInitial < this.hasta.getMonth() || YearInitial < this.hasta.getYear()) && YearInitial >= this.hasta.getYear()){
            if(this.stock){
                this.actuExistenciasSaldos(MonthInitial,YearInitial);
            }
            if(this.clientes){
                this.actuSaldosClientes(MonthInitial,YearInitial);
            }
            if(this.proveedores){
                this.actuSaldosProveedores(MonthInitial,YearInitial);
            }
            if(MonthInitial == 12){
                MonthInitial = 1;
                YearInitial++;
            }else{
                MonthInitial++;
            }
        }
        this.insAudProcesos("migrarDatos",this.desde,this.hasta,"ACTU_STOCK="+(this.stock?"1":"0"),"ACTU_CLIENTES="+(this.clientes?"1":"0"),"ACTU_PROVEED="+(this.proveedores?"1":"0")); 
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Atencion", "Fin de Regularizacion de Saldos"));
    }
    
    public Boolean actuExistenciasSaldos(Integer mes,Integer year){
        if(year+1900 < 2006){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Atencion","Solo desde el 2006"));
            return false;
        }
        String fInicial = "01/"+(mes+1)+"/"+(year+1900);
        String lNDia;
        if((mes+1) == 2){
            lNDia = (mes%4 == 0)?"29":"28";
        }else if((mes+1) == 4 || (mes+1) == 6 || (mes+1) == 9 || (mes+1) == 11){
            lNDia = "30";
        }else{
            lNDia = "31";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date fMovim = new Date(year,mes,Integer.parseInt(lNDia));
        Integer mesAnt,anoAnt;
        if((mes+1) == 1){
            mesAnt = 12;
            anoAnt = year - 1;
        }else{
            mesAnt = mes - 1;
            anoAnt = year;
        }
        String sql = " SELECT e.cod_depo,e.cod_merca,e.fmovim,e.saldo_cajas + e.cant_cajas AS saldo_cajas,e.saldo_unid + e.cant_unid AS saldo_unid,m.nrelacion "
            +" FROM existencias_saldos e INNER JOIN mercaderias m ON e.cod_merca = m.cod_merca AND e.cod_empr = m.cod_empr "
            +" WHERE (e.cod_empr = 2) AND ( e.fmovim = (SELECT TOP 1 fmovim FROM existencias_saldos s WHERE s.cod_empr = 2 AND e.cod_merca = s.cod_merca "
            +" AND e.cod_depo = s.cod_depo AND e.cod_empr = s.cod_empr AND fmovim < convert(smalldatetime,'"+fInicial+"',121) ORDER BY fmovim DESC)) "
            +" ORDER BY e.cod_depo, e.cod_merca";
        System.out.println(sql);
        List<Object[]> existencia = excelFacade.listarParaExcel(sql);
        for(Object[] e : existencia){
            BigDecimal saldoCajas = (BigDecimal) e[3];
            BigDecimal nRelacion = (BigDecimal) e[5];
            BigDecimal saldoUnidad = (BigDecimal) e[4];
            Integer lSaldoCajas = saldoCajas.multiply(nRelacion).add(saldoUnidad).divide(nRelacion).intValue();
            Integer ltUnid = saldoCajas.multiply(nRelacion).add(saldoUnidad).intValue();
            Integer lsaldoUnidad;
            if(ltUnid < 0){
                lsaldoUnidad = ((ltUnid*-1)%nRelacion.intValue())*-1;
            }else{
                lsaldoUnidad = (ltUnid%nRelacion.intValue());
            }
            sql = " UPDATE existencias_saldos SET saldo_cajas = "+lSaldoCajas+", saldo_unid = "+lsaldoUnidad+" WHERE YEAR(fmovim) = "+year
                    +" and cod_empr = 2 AND MONTH(fmovim) = "+mes+" AND cod_depo = "+e[0]+" AND cod_merca = "+e[1];
            System.out.println(sql);
            excelFacade.executeInsert(sql);
        }
        sql = " UPDATE existencias SET cant_cajas = v.saldo_cajas+v.cant_cajas, cant_unid = v.saldo_unid + v.cant_unid "
            +" FROM existencias_saldos v WHERE existencias.cod_depo = v.cod_depo AND v.cod_merca = existencias.cod_merca "
            +" AND v.cod_empr = 2 AND MONTH(v.fmovim) = "+mes+" and YEAR(v.fmovim) = "+year+" and existencias.cod_empr = 2 ";
        System.out.println(sql);
        excelFacade.executeInsert(sql);
        sql = " INSERT INTO  existencias (cod_empr, cod_depo, cod_merca, cant_cajas, cant_unid, reser_cajas,  reser_unid, nrelacion) "
            +" SELECT v.cod_empr, V.cod_depo, V.cod_merca,v.saldo_cajas+v.cant_cajas, v.saldo_unid+v.cant_unid, 0, 0, m.nrelacion "
            +" FROM  existencias_saldos v, mercaderias m "
            +" WHERE v.cod_empr = 2 and v.cod_merca = m.cod_merca AND YEAR(v.fmovim) = "+year+" and MONTH(v.fmovim) = "+mes+" AND NOT EXISTS ( "
            +" SELECT * FROM existencias WHERE v.cod_empr = 2 and v.cod_depo = existencias.cod_depo AND v.cod_merca = existencias.cod_merca )";
        System.out.println(sql);
        excelFacade.executeInsert(sql);
        sql = " update existencias set cant_cajas = isnull(ROUND((((existencias.cant_cajas * existencias.nrelacion) + existencias.cant_unid)/existencias.nrelacion),0,1),cant_cajas), "
            +" cant_unid = isnull(((existencias.cant_cajas * existencias.nrelacion) + existencias.cant_unid) - ROUND((((existencias.cant_cajas * existencias.nrelacion)+"
            +" existencias.cant_unid)/existencias.nrelacion),0,1) * nrelacion,cant_unid) WHERE existencias.cod_empr = 2";
        System.out.println(sql);
        excelFacade.executeInsert(sql);
        this.insAudProcesos("migrarDatos",new Date(),new Date(),"ACTU_STOCK=1",null,null);
        return true;
    }
    
    public Boolean actuSaldosClientes(Integer mes,Integer year){
        if(year+1900 < 2006){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Atencion","Solo desde el 2006"));
            return false;
        }
        String lNDia;
        if((mes+1) == 2){
            lNDia = (mes%4 == 0)?"29":"28";
        }else if((mes+1) == 4 || (mes+1) == 6 || (mes+1) == 9 || (mes+1) == 11){
            lNDia = "30";
        }else{
            lNDia = "31";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date fMovim = new Date(year,mes,Integer.parseInt(lNDia));
        Integer mesAnt,anoAnt;
        if((mes+1) == 1){
            mesAnt = 12;
            anoAnt = year - 1;
        }else{
            mesAnt = mes - 1;
            anoAnt = year;
        }
        String sql = " SELECT cod_empr, cod_cliente,fmovim,isaldo + acum_debi - acum_credi as isaldo FROM saldos_clientes WHERE fmovim = ( "
            +" select top 1 fmovim from saldos_clientes s where s.cod_empr = 2 and s.fmovim < convert(smalldatetime,'"+format.format(fMovim)+"',121) "
            +" and s.cod_cliente = saldos_clientes.cod_cliente ORDER BY fmovim desc ) AND cod_empr  = 2 ORDER BY cod_cliente";
        System.out.println(sql);
        List<Object[]> curSal = excelFacade.listarParaExcel(sql);
        for(Object[] e : curSal){
            BigDecimal lsaldo = (BigDecimal) e[3];
            BigDecimal lcodCliente = (BigDecimal) e[1];
            BigDecimal lfecha = (BigDecimal) e[2];
            sql = "UPDATE saldos_clientes SET isaldo = "+lsaldo.floatValue()+" WHERE YEAR(fmovim) = "+year
                +" and cod_empr = 2 AND MONTH(fmovim) = "+mes+" AND cod_cliente = "+lcodCliente.intValue();
        }
        sql = " UPDATE clientes SET fprim_fact = (SELECT MIN(ffactur) FROM facturas f "
            +" WHERE clientes.cod_cliente = f.cod_cliente AND mestado = 'A' AND cod_empr = 2 ) WHERE fprim_fact is null";
        System.out.println(sql);
        excelFacade.executeInsert(sql);
        this.insAudProcesos("migrarDatos",new Date(),new Date(),"ACTU_CLIENTES=1",null,null);
        return true;
    }
    
    public Boolean actuSaldosProveedores(Integer mes,Integer year){
        if(year+1900 < 2006){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Atencion","Solo desde el 2006"));
            return false;
        }
        String lNDia;
        if((mes+1) == 2){
            lNDia = (mes%4 == 0)?"29":"28";
        }else if((mes+1) == 4 || (mes+1) == 6 || (mes+1) == 9 || (mes+1) == 11){
            lNDia = "30";
        }else{
            lNDia = "31";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date fMovim = new Date(year,mes,Integer.parseInt(lNDia));
        Integer mesAnt,anoAnt;
        if((mes+1) == 1){
            mesAnt = 12;
            anoAnt = year - 1;
        }else{
            mesAnt = mes - 1;
            anoAnt = year;
        }
        String sql = " SELECT cod_empr, cod_proveed, fmovim, ccanal_compra,  isaldo + acum_debi - acum_credi as isaldo FROM saldos_proveedores WHERE fmovim = ( "
            +" select top 1 fmovim from saldos_proveedores s where s.cod_empr = 2 AND S.fmovim < convert(smalldatetime,'"+format.format(fMovim)+"',121) "
            +" and s.cod_proveed = saldos_proveedores.cod_proveed ORDER BY fmovim desc ) AND cod_empr  = 2 ORDER BY cod_proveed";
        System.out.println(sql);
        List<Object[]> curSal = excelFacade.listarParaExcel(sql);
        for(Object[] e : curSal){
            BigDecimal lsaldo = (BigDecimal) e[4];
            BigDecimal lcodProveed = (BigDecimal) e[1];
            BigDecimal lcanalCompra = (BigDecimal) e[3];
            Date lfecha =  (Date) e[2];
            sql = " UPDATE saldos_proveedores  SET isaldo = "+lsaldo.intValue()+" WHERE YEAR(fmovim) = "+year+" and cod_empr = 2 AND MONTH(fmovim) = "+mes
                    +" AND cod_proveed = "+lcodProveed.intValue();
            System.out.println(sql);
            excelFacade.executeInsert(sql);
        }
        this.insAudProcesos("MigrarDatos",new Date(),new Date(),"ACTU_PROVEED=1",null,null);
        return false;
    }
    
    public Boolean insAudProcesos(String applicacion,Date fInicial,Date fFinal,String dato1,String dato2,String dato3){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario")==null?"":FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
//        usuario = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario").toString();
        String sql = " INSERT INTO procesamientos(cod_aplicacion, fec_inicial, fec_final, cod_usuario, dato1, dato2, dato3) "
            +" VALUES ('"+applicacion+"','"+format.format(fInicial)+"','"+format.format(fFinal)+"','"+usuario+"','"+dato1+"','"+dato2+"','"+dato3+"')";
        System.out.println(sql);
        excelFacade.executeInsert(sql);
        return true;
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
    
    public Boolean getStock() {
        return stock;
    }

    public void setStock(Boolean stock) {
        this.stock = stock;
    }

    public Boolean getClientes() {
        return clientes;
    }

    public void setClientes(Boolean clientes) {
        this.clientes = clientes;
    }

    public Boolean getProveedores() {
        return proveedores;
    }

    public void setProveedores(Boolean proveedores) {
        this.proveedores = proveedores;
    }
}
