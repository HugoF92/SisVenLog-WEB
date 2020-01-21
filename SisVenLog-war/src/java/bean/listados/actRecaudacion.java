package bean.listados;

import dao.EmpleadosFacade;
import dao.ExcelFacade;
import entidad.Empleados;
import entidad.EmpleadosPK;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class actRecaudacion {
    
    private Date desde;
    
    private Date hasta;
    
    private Empleados entregador;
    
    @EJB
    private EmpleadosFacade entregadorFacade;
    @EJB
    private ExcelFacade excelFacade;
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        this.entregador = new Empleados(new EmpleadosPK());
        this.desde = new Date();
        this.hasta = new Date();
    }
    
    public void ejecutar(String tipo) {
        LlamarReportes rep = new LlamarReportes();
        String fdesde = dateToString(desde);
        String fhasta = dateToString(hasta);
        String extras = "" , extras2 = "";
        if (this.entregador != null){
            extras += " AND COD_ENTREGADOR = "+this.entregador.getEmpleadosPK().getCodEmpleado()+" ";
        }
        List<Object[]> lista = new ArrayList<Object[]>();
        String sql = " select cod_entregador , fplanilla , kcheques , tcheques_dia ," +
            " tefectivo , tgastos , tmonedas , tdepositos , nro_planilla , cod_empr , cod_zona from recaudacion where cod_empr = 2 and convert(date,fplanilla) " +
            " between '"+fdesde+"' and '"+fhasta+"' "+extras+" ";
        lista = excelFacade.listarParaExcel(sql);
        for(Object[] row : lista){
//            List<BigDecimal> datos = calcDatos2((short)row[0],(String)row[1]);
            List<BigDecimal> datos = calcDatos2((short)row[0],(new SimpleDateFormat("yyyy/MM/dd").format(row[1])));
            BigDecimal ltdiferencia = BigDecimal.ZERO;
            BigDecimal x3 = ((BigDecimal)row[3])==null?BigDecimal.ZERO:((BigDecimal)row[3]);
            BigDecimal x4 = ((BigDecimal)row[4])==null?BigDecimal.ZERO:((BigDecimal)row[4]);
            BigDecimal x5 = ((BigDecimal)row[5])==null?BigDecimal.ZERO:((BigDecimal)row[5]);
            BigDecimal x6 = ((BigDecimal)row[6])==null?BigDecimal.ZERO:((BigDecimal)row[6]);
            BigDecimal x7 = ((BigDecimal)row[7])==null?BigDecimal.ZERO:((BigDecimal)row[7]);

            ltdiferencia = (((((((((datos.get(0).subtract(datos.get(1)).subtract(datos.get(2))).
                    subtract(datos.get(4))).subtract(x4)).subtract(x7)).subtract(x3)).subtract(x5)).
                    subtract(datos.get(3))).subtract(datos.get(6))).subtract(x6)).
                    multiply((BigDecimal.ONE).negate());
            String d = " update recaudacion "+
                " set tdiferencia = "+ltdiferencia+", " +
                " tventas = "+datos.get(0)+", "+
                " tnotas_dev = "+datos.get(1)+", "+
                " tcreditos = "+datos.get(2)+", "+
                " tnotas_otras = "+datos.get(4)+", "+
                " tcheques_dif = "+datos.get(3)+", "+
                " tnotas_atras = "+datos.get(5)+", "+
                " tpagares = "+datos.get(6)+" "+
                " WHERE cod_empr = 2 "+
                " AND cod_entregador = "+(short)row[0]+" "+
                " AND convert(date,fplanilla) = '"+(new SimpleDateFormat("yyyy/MM/dd").format(row[1]))+"' ";
            excelFacade.ejecutarQuery(d);
            String del = " delete from recaudacion_det " +
                " WHERE nro_planilla = "+row[8]+" ";
            excelFacade.ejecutarQuery(del);
            String ins = " insert into recaudacion_det(cod_empr, nro_planilla, " +
                " tventas, tnotas_dev, tcreditos, tnotas_otras, cod_zona,tnotas_atras) " +
                " values ( "+row[9]+","+row[8]+","+datos.get(0)+", " +
                " "+datos.get(1)+", "+datos.get(2)+", "+datos.get(4)+", " +
                " '"+row[10]+"', "+datos.get(5)+") ";
            excelFacade.ejecutarQuery(ins);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Actualizacion Terminada.","") );
    }
    
    public List<BigDecimal> calcDatos2(short codEntregador , String lFecha){
        BigDecimal ltventas = BigDecimal.ZERO;
        BigDecimal ltnotaDev = BigDecimal.ZERO;
        BigDecimal ltcreditos = BigDecimal.ZERO;
        BigDecimal ltchequesDif = BigDecimal.ZERO;
        BigDecimal ltnotasOtras = BigDecimal.ZERO;
        BigDecimal ltnotasAtras = BigDecimal.ZERO;
        BigDecimal ltpagares = BigDecimal.ZERO;
        String codZona = "";
        List<Object[]> lista = new ArrayList<Object[]>();
        String curDet = " SELECT cod_zona, SUM(ttotal) as ttotal "
                + " FROM facturas WHERE mestado = 'A' "
                + " AND convert(date,ffactur) ='"+lFecha+ "' AND cod_entregador = "+codEntregador
                + " GROUP BY cod_zona ";
        lista = excelFacade.listarParaExcel(curDet);
        for(Object[] row : lista){
            codZona = (String)row[0];
            String recDet = " insert into recaudacion(cod_empr,fplanilla,cod_entregador,cod_zona,tventas) " +
                    " values(2,convert(smalldatetime,'"+lFecha+"',101),"+codEntregador+",'"+codZona+"',"+row[1]+")";
            ltventas = ltventas.add((BigDecimal)row[1]);
            excelFacade.ejecutarQuery(recDet);
        }
        String curNot = " select f.cod_zona,ISNULL(SUM(N.ttotal),0) as ttotal " +
                " FROM notas_ventas N, facturas f " +
                " WHERE n.mestado = 'A' " +
                " AND convert(date,n.fdocum) = '"+lFecha+"'"+
                " AND f.cod_empr = 2 " +
                " AND f.ffactur = n.ffactur " +
                " AND f.ctipo_docum = n.fac_ctipo_docum " +
                " AND f.nrofact = n.nrofact " +
                " AND n.ctipo_docum = 'NCV' " +
                " AND n.cconc = 'DEV' " +
                " AND n.cod_empr = 2 " +
                " AND n.cod_entregador = "+codEntregador+
                " AND (EXISTS (SELECT * FROM facturas f " +
                " where n.fac_ctipo_docum = f.ctipo_docum " +
                " AND n.nrofact = f.nrofact " +
                " AND n.ffactur = f.ffactur " +
                " AND f.ctipo_docum ='FCO' " +
                " AND cod_empr = 2 " +
                " AND f.cod_entregador = "+codEntregador+") " +
                " OR (EXISTS (SELECT * FROM facturas f " +
                " where n.fac_ctipo_docum = f.ctipo_docum " +
                " AND n.nrofact = f.nrofact " +
                " AND n.ffactur = f.ffactur " +
                " AND f.ctipo_docum ='FCR' " +
                " AND convert(date,f.ffactur) = '"+lFecha+"'" +
                " AND cod_empr = 2 " +
                " AND f.cod_entregador = "+codEntregador+
                " ))) "+" GROUP BY f.cod_zona ";
        lista = excelFacade.listarParaExcel(curNot);
        for(Object[] row : lista){
            codZona = (String)row[0];
            String recDet = " insert into recaudacion(cod_empr,fplanilla,cod_entregador,cod_zona,tnotas_dev) " +
                    " values(2,convert(smalldatetime,'"+lFecha+"',101),"+codEntregador+",'"+codZona+"',"+row[1]+")";
            ltnotaDev = ltnotaDev.add((BigDecimal)row[1]);
            excelFacade.ejecutarQuery(recDet);
        }
        curDet = " SELECT f.cod_zona,ISNULL(SUM(F.ttotal),0) as ttotal " +
                    " FROM facturas f " +
                    " WHERE f.mestado = 'A' " +
                    " AND cod_empr = 2 " +
                    " AND convert(date,ffactur) = '"+lFecha+"' "+
                    " AND ctipo_docum = 'FCR' " +
                    " AND cod_entregador ="+codEntregador+
                    " and NOT EXISTS (SELECT * FROM " +
                    " recibos_det D, recibos r " +
                    " where d.nrecibo = r.nrecibo " +
                    " AND convert(date,r.frecibo) = '"+lFecha+"' "+
                    " AND r.mestado = 'A' " +
                    " AND r.cod_empr = 2 " +
                    " AND f.cod_cliente = r.cod_cliente " +
                    " AND d.ctipo_docum = f.ctipo_docum " +
                    " AND d.ndocum = f.nrofact) " +
                    " GROUP BY f.cod_zona ";
        lista = excelFacade.listarParaExcel(curDet);
        for(Object[] row : lista){
            codZona = (String)row[0];
            String recDet = " insert into recaudacion(cod_empr,fplanilla,cod_entregador,cod_zona,tcreditos) " +
                    " values(2,convert(smalldatetime,'"+lFecha+"',101),"+codEntregador+",'"+codZona+"',"+row[1]+")";
            ltcreditos = ltcreditos.add((BigDecimal)row[1]);
            excelFacade.ejecutarQuery(recDet);
        }
        curDet = " SELECT f.cod_zona, ISNULL(SUM(f.ttotal - d.itotal),0) as itotal " +
            " FROM facturas f , recibos_det d, recibos r "+
            " WHERE f.mestado = 'A' "+
            " AND f.cod_empr = 2 "+
            " AND convert(date,f.ffactur) = '"+lFecha+"'"+
            " AND f.ctipo_docum = 'FCR' "+
            " AND f.cod_entregador = "+codEntregador+
            " AND f.ttotal > d.itotal "+
            " and d.nrecibo = r.nrecibo "+
            " AND convert(date,r.frecibo) = '"+lFecha+"'"+
            " AND r.mestado = 'A' "+
            " AND r.cod_empr = 2 "+
            " AND f.cod_cliente = r.cod_cliente "+
            " AND d.ctipo_docum = f.ctipo_docum "+
            " AND d.ndocum = f.nrofact "+
            " GROUP BY f.cod_zona ";
        lista = excelFacade.listarParaExcel(curDet);
        for(Object[] row : lista){
            codZona = (String)row[0];
            String recDet = " insert into recaudacion(cod_empr,fplanilla,cod_entregador,cod_zona,tcreditos) " +
                    " values(2,convert(smalldatetime,'"+lFecha+"',101),"+codEntregador+",'"+codZona+"',"+row[1]+")";
            ltcreditos = ltcreditos.add((BigDecimal)row[1]);
            excelFacade.ejecutarQuery(recDet);
        }
        curDet = " SELECT f.cod_zona,ISNULL(SUM(N.ttotal),0) as ttotal "+
            " FROM notas_ventas N, facturas f "+
            " WHERE n.mestado = 'A' "+
            " AND n.cod_empr = 2 "+
            " AND convert(date,n.ffactur) = '"+lFecha+"' "+
            " AND convert(date,n.fdocum) = '"+lFecha+"' "+
            " AND n.ctipo_docum = 'NCV' "+
            " AND n.fac_ctipo_docum = 'FCR' "+
            " AND n.ffactur = f.ffactur "+
            " AND n.fac_ctipo_docum = f.ctipo_docum "+
            " AND n.nrofact = f.nrofact "+
            " AND n.cod_entregador ="+codEntregador+" "+
            " and f.cod_empr = 2 "+
            " GROUP BY f.cod_zona ";
        lista = excelFacade.listarParaExcel(curDet);    
        for(Object[] row : lista){
            codZona = (String)row[0];
            String recDet = " update recaudacion_det set tcreditos = ( tcreditos - "+row[1]+" ) where cod_zona = '"+codZona+"' ";
            ltcreditos = ltcreditos.subtract((BigDecimal)row[1]);
            excelFacade.ejecutarQuery(recDet);
        }
        String curTot = "SELECT ISNULL(SUM(icheque),0) as ttotal " +
                " FROM cheques c " +
                " WHERE convert(date,femision) =  '"+lFecha+"'" +
                " AND mtipo = 'D' " +
                " AND cod_empr= 2 " +
                " AND cod_entregador ="+codEntregador+" ";
        lista = excelFacade.listarParaExcel(curTot);
        for(Object row : lista){
            ltchequesDif = ltchequesDif.add((BigDecimal)row);
        }
        curDet = " SELECT f.cod_zona,ISNULL(SUM(n.ttotal),0) as ttotal " +
            " FROM notas_ventas n, facturas f "+
            " WHERE n.mestado = 'A' "+
            " AND convert(date,n.fdocum) = '"+lFecha+"' "+
            " AND n.ctipo_docum = 'NCV' "+
            " AND n.cconc != 'DEV' "+
            " AND n.cod_empr = 2 "+
            " AND n.cod_entregador = "+codEntregador+" "+
            " AND n.fac_ctipo_docum = f.ctipo_docum "+
            " AND n.nrofact = f.nrofact "+
            " AND f.ffactur = n.fdocum "+
            " AND f.cod_empr= 2 "+
            " GROUP BY f.cod_zona ";
        lista = excelFacade.listarParaExcel(curDet);
        for(Object[] row : lista){
            codZona = (String)row[0];
            String recDet = " insert into recaudacion(cod_empr,fplanilla,cod_entregador,cod_zona,tnotas_otras) " +
                    " values(2,convert(smalldatetime,'"+lFecha+"',101),"+codEntregador+",'"+codZona+"',"+row[1]+")";
            ltnotasOtras = ltnotasOtras.add((BigDecimal)row[1]);
            excelFacade.ejecutarQuery(recDet);
        }
        curDet = " SELECT f.cod_zona,ISNULL(SUM(n.ttotal),0) as ttotal " +
            " FROM notas_ventas N, facturas f "+
            " WHERE n.mestado = 'A' "+
            " AND f.mestado = 'A' "+
            " AND convert(date,fdocum) = '"+lFecha+"' "+
            " AND n.ctipo_docum = 'NCV' "+
            " AND n.cod_empr = 2 "+
            " AND n.nrofact = f.nrofact "+
            " AND n.ffactur = f.ffactur "+
            " AND n.fac_ctipo_docum = f.ctipo_docum "+
            " AND f.cod_empr = 2 "+
            " AND n.cod_entregador = "+codEntregador+" "+
            " AND n.cod_entregador = f.cod_entregador "+
            " AND n.fac_ctipo_docum = 'FCR' "+
            " AND n.fdocum != f.ffactur "+
            " GROUP BY f.cod_zona ";
        lista = excelFacade.listarParaExcel(curDet);
        for(Object[] row : lista){
            codZona = (String)row[0];
            String recDet = " insert into recaudacion(cod_empr,fplanilla,cod_entregador,cod_zona,tnotas_atras) " +
                    " values(2,convert(smalldatetime,'"+lFecha+"',101),"+codEntregador+",'"+codZona+"',"+row[1]+")";
            ltnotasAtras = ltnotasAtras.add((BigDecimal)row[1]);
            excelFacade.ejecutarQuery(recDet);
        }
        curTot = " SELECT ISNULL(SUM(ipagare),0) as ipagare " +
            " FROM pagares p "+
            " WHERE p.mestado = 'A' "+
            " AND convert(date,femision) = '"+lFecha+"' "+
            " AND p.cod_empr = 2 "+
            " AND p.cod_entregador = "+codEntregador+" ";
        lista = excelFacade.listarParaExcel(curTot);
        for(Object row : lista){
            ltpagares = ltpagares.add((BigDecimal)row);
        }
        List<BigDecimal> l = new ArrayList<>();
        l.add(ltventas);
        l.add(ltnotaDev);
        l.add(ltcreditos);
        l.add(ltchequesDif);
        l.add(ltnotasOtras);
        l.add(ltnotasAtras);
        l.add(ltpagares);
        return l;
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

    public Empleados getEntregador() {
        return this.entregador;
    }

    public void setEntregador(Empleados entregador) {
        this.entregador = entregador;
    }
    
    public List<Empleados> getEntregadorList(){
        return this.entregadorFacade.listarEntregador();
    }
}
