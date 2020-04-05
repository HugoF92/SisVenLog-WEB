package bean.listados;

import dao.ConductoresFacade;
import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.ExcelFacade;
import dao.MercaderiasFacade;
import dao.RemisionesDetFacade;
import dao.RemisionesFacade;
import dao.RemisionesFacturasFacade;
import dao.TransportistasFacade;
import entidad.Conductores;
import entidad.Depositos;
import entidad.Empleados;
import entidad.EmpleadosPK;
import entidad.Existencias;
import entidad.Remisiones;
import entidad.RemisionesDet;
import entidad.RemisionesDetPK;
import entidad.RemisionesFacturas;
import entidad.RemisionesFacturasPK;
import entidad.RemisionesPK;
import entidad.Transportistas;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
public class genRemisiones {
    
    private Date fecha;
    @EJB
    private RemisionesFacade remisionesFacade;
    private Integer part1 , part2 ,part3, envioInicial, envioFinal;
    private Depositos deposito;
    @EJB
    private DepositosFacade depositoFacade;
    String discriminar;
    private Conductores conductor;
    private Transportistas transportista;
    private Empleados empleado;
    @EJB 
    private ConductoresFacade conductoresFacade;
    @EJB
    private TransportistasFacade transportistasFacade;
    @EJB
    private EmpleadosFacade empleadosFacade;
    @EJB
    private MercaderiasFacade mercaderiasFacade;
    @EJB
    private RemisionesDetFacade remisionesDetFacade;
    @EJB
    private RemisionesFacturasFacade remisionesFacturasFacade;
    @EJB
    private ExcelFacade excelFacade;    
    
    
    
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {
        Calendar c = Calendar.getInstance();
        Date now = new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+1);
        this.fecha = now;
        List<Remisiones> remisionesNro = remisionesFacade.buscarNroInicialRemision();
        this.part1 = 1;
        this.part2 = 1;
        this.part3 = 1;
        if(!remisionesNro.isEmpty()){
            Long nroRemisionNuevo = remisionesNro.get(0).getRemisionesPK().getNroRemision()+1;
            if(remisionesFacade.verificarNroRemision(nroRemisionNuevo.intValue())==0) this.part3 = nroRemisionNuevo.intValue();
        } 
        this.discriminar = "1";
    }
    
    public void ejecutar(String tipo) {
        Integer l_nro_remision = this.part3 - 1;
        Integer l_max_lineas = 20;
        RemisionesPK rePk = new RemisionesPK();
        RemisionesDetPK reDePk = new RemisionesDetPK();
        Integer l_lineas = 0;
        switch (this.discriminar){    
            case "1":
                List<Existencias> existencias = mercaderiasFacade.listaGrabDetRemision2(this.deposito.getDepositosPK().getCodDepo());
                rePk.setCodEmpr((short) 2);
                reDePk.setCodEmpr((short) 2);
                for (Existencias e : existencias) {
                    String l_npunto_est = String.format("%1s", String.valueOf(this.part1)).replace(' ', '0');
                    String l_npunto_exp = String.format("%2s", String.valueOf(this.part2)).replace(' ', '0');
                    String l_npunto_sec = String.format("%7s", String.valueOf(l_nro_remision)).replace(' ', '0');
                    String l_new_remision = l_npunto_est + l_npunto_exp + l_npunto_sec;
                    l_lineas++;
                    if (l_lineas > l_max_lineas) {
                        l_nro_remision++;
                        rePk.setNroRemision(Integer.parseInt(l_new_remision));
                        Remisiones re = new Remisiones(rePk);
                        re.setFremision(this.fecha);
                        re.setCodDepo(this.deposito.getDepositosPK().getCodDepo());
                        re.setCodConductor(conductor.getCodConductor());
                        re.setCodTransp(transportista.getCodTransp());
                        re.setCodEntregador(empleado.getEmpleadosPK().getCodEmpleado());
                        re.setMtipo('O');
                        re.setMestado('A');
                        String est = String.format("%3s", String.valueOf(this.part1)).replace(' ', '0');
                        String exp = String.format("%3s", String.valueOf(this.part2)).replace(' ', '0');
                        String sec = String.format("%7s", String.valueOf(l_nro_remision)).replace(' ', '0');
                        re.setXnroRemision(est + "-" + exp + "-" + sec);
                        int c = remisionesFacade.verificarNroRemision(Integer.parseInt(l_new_remision));
                        if (c == 0) {
                            remisionesFacade.create(re);
                            l_lineas = 1;
                            l_nro_remision--;
                        }
                    }
                    String l_cod_merca = e.getMercaderias().getMercaderiasPK().getCodMerca();
                    long l_cant_cajas = e.getCantCajas();
                    long l_cant_unid = e.getCantUnid();
                    String l_xdesc = e.getMercaderias().getXdesc();
                    reDePk.setNroRemision(Long.parseLong(l_new_remision));
                    reDePk.setCodMerca(l_cod_merca);
                    RemisionesDet remiDet = new RemisionesDet(reDePk);
                    remiDet.setXdesc(l_xdesc);
                    remiDet.setCantCajas(Integer.parseInt(String.valueOf(l_cant_cajas)));
                    remiDet.setCantUnid(Integer.parseInt(String.valueOf(l_cant_unid)));
                    int c = remisionesFacade.verificarNroRemisionDet(String.valueOf(remiDet.getRemisionesDetPK().getNroRemision()), remiDet.getRemisionesDetPK().getCodMerca());
                    if (c == 0) {
                        remisionesDetFacade.create(remiDet);
                    }
                    ins_factura_remision(l_new_remision, l_cod_merca, this.deposito.getDepositosPK().getCodDepo());
                }
                break;
            case "2":
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                List<Object[]> remisionComplementaria = remisionesFacade.
                        grabarRemisionComplementaria(formato.format(this.fecha), this.envioInicial,this.envioFinal);
                rePk.setCodEmpr((short) 2);
                reDePk.setCodEmpr((short) 2);
                for (Object[] o : remisionComplementaria) {
                    String l_npunto_est = String.format("%1s", String.valueOf(this.part1)).replace(' ', '0');
                    String l_npunto_exp = String.format("%2s", String.valueOf(this.part2)).replace(' ', '0');
                    String l_npunto_sec = String.format("%7s", String.valueOf(l_nro_remision)).replace(' ', '0');
                    String l_new_remision = l_npunto_est + l_npunto_exp + l_npunto_sec;
                    l_lineas++;
                if (l_lineas > 20) {
                    l_nro_remision++;
                    rePk.setNroRemision(Integer.parseInt(l_new_remision));
                    Remisiones re = new Remisiones(rePk);
                    re.setFremision(this.fecha);
                    re.setCodDepo(this.deposito.getDepositosPK().getCodDepo());
                    re.setCodConductor(conductor.getCodConductor());
                    re.setCodTransp(transportista.getCodTransp());
                    re.setCodEntregador(empleado.getEmpleadosPK().getCodEmpleado());
                    re.setMtipo('O');
                    re.setMestado('A');
                    String est = String.format("%3s", String.valueOf(this.part1)).replace(' ', '0');
                    String exp = String.format("%3s", String.valueOf(this.part1)).replace(' ', '0');
                    String sec = String.format("%7s", String.valueOf(l_nro_remision)).replace(' ', '0');
                    re.setXnroRemision(est + "-" + exp + "-" + sec);
                    int c = remisionesFacade.verificarNroRemision(Integer.parseInt(l_new_remision));
                    if (c == 0) {
                        remisionesFacade.create(re);
                        l_lineas = 1;
                        l_nro_remision--;
                    }
                }
                String l_cod_merca = (String) o[0];
                BigDecimal l_cant_cajas = (BigDecimal) o[3];
                BigDecimal l_cant_unid = (BigDecimal) o[4];
                String l_xdesc = (String) o[1];
                reDePk.setNroRemision(Long.parseLong(l_new_remision));
                reDePk.setCodMerca(l_cod_merca);
                RemisionesDet remiDet = new RemisionesDet(reDePk);
                remiDet.setXdesc(l_xdesc);
                remiDet.setCantCajas(Integer.parseInt(String.valueOf(l_cant_cajas)));
                remiDet.setCantUnid(Integer.parseInt(String.valueOf(l_cant_unid)));
                int c = remisionesFacade.verificarNroRemisionDet(String.valueOf(remiDet.getRemisionesDetPK().getNroRemision()), remiDet.getRemisionesDetPK().getCodMerca());
                if (c == 0) {
                    remisionesDetFacade.create(remiDet);
                }
            }
                break;
                        
        }
    }
    
        //Insertar factura remision
    private void ins_factura_remision(String l_new_remision, String l_merca, short l_cod_depo_destino) {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

        List<Object[]> lista = remisionesFacade.insertarFacturasRemisiones(formato.format(this.fecha), l_cod_depo_destino, l_merca);
        RemisionesFacturasPK reFaPk = new RemisionesFacturasPK();
        reFaPk.setCodEmpr((short) 2);
        reFaPk.setNroRemision(Long.parseLong(l_new_remision));
        for (Object[] o : lista) {
            String l_nrofact = (String) o[1];
            String l_ctipo_docum = (String) o[0];
            int e = remisionesFacade.verificarRemisionFactura(l_new_remision, l_nrofact, l_ctipo_docum);
            if (e == 0) {
                reFaPk.setNrofact(Long.valueOf(l_nrofact));
                reFaPk.setCtipoDocum(l_ctipo_docum);
                RemisionesFacturas re = new RemisionesFacturas(reFaPk);
                remisionesFacturasFacade.create(re);
            }
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getPart1() {
        return part1;
    }

    public void setPart1(Integer part1) {
        this.part1 = part1;
    }

    public Integer getPart2() {
        return part2;
    }

    public void setPart2(Integer part2) {
        this.part2 = part2;
    }

    public Integer getPart3() {
        return part3;
    }

    public void setPart3(Integer part3) {
        this.part3 = part3;
    }

    public Depositos getDeposito() {
        return deposito;
    }

    public void setDeposito(Depositos deposito) {
        this.deposito = deposito;
    }

    public String getDiscriminar() {
        return discriminar;
    }

    public void setDiscriminar(String discriminar) {
        this.discriminar = discriminar;
    }

    public Conductores getConductor() {
        return conductor;
    }

    public void setConductor(Conductores conductor) {
        this.conductor = conductor;
    }

    public Integer getEnvioInicial() {
        return envioInicial;
    }

    public void setEnvioInicial(Integer envioInicial) {
        this.envioInicial = envioInicial;
    }

    public Integer getEnvioFinal() {
        return envioFinal;
    }

    public void setEnvioFinal(Integer envioFinal) {
        this.envioFinal = envioFinal;
    }

    public Transportistas getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportistas transportista) {
        this.transportista = transportista;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<Depositos> getListaDepositos() {
        return depositoFacade.listarDepositosByEmp(2);
    }
    
    public List<Conductores> getListaConductores(){
        if(this.deposito==null) return null;
        return conductoresFacade.listaConductoresPorDeposito(this.deposito.getDepositosPK().getCodDepo());
    }
    
    public List<Transportistas> getListaTransportistas(){
        if(this.deposito==null) return null;
        return transportistasFacade.listaTransportistasPorDeposito(this.deposito.getDepositosPK().getCodDepo());
    }
    
    public List<Empleados> getListaEntregador(){
        if(this.deposito==null) return null;
        return empleadosFacade.listarEntregadorPorDeposito(this.deposito.getDepositosPK().getCodDepo());
    }
}
