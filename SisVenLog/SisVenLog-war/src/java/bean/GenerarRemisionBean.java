package bean;

import dao.ConductoresFacade;
import dao.DepositosFacade;
import dao.EmpleadosFacade;
import dao.MercaderiasFacade;
import dao.RemisionesDetFacade;
import dao.RemisionesFacade;
import dao.RemisionesFacturasFacade;
import dao.TransportistasFacade;
import entidad.Conductores;
import entidad.Depositos;
import entidad.Empleados;
import entidad.Existencias;
import entidad.Mercaderias;
import entidad.Remisiones;
import entidad.RemisionesDet;
import entidad.RemisionesDetPK;
import entidad.RemisionesFacturas;
import entidad.RemisionesFacturasPK;
import entidad.RemisionesPK;
import entidad.Transportistas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "generarRemisionBean")
@ViewScoped
public class GenerarRemisionBean implements Serializable {

    @EJB
    private DepositosFacade depositoFacade;
    @EJB
    private EmpleadosFacade empleadoFacade;
    @EJB
    private TransportistasFacade transportistaFacade;
    @EJB
    private ConductoresFacade conductorFacade;
    @EJB
    private RemisionesFacade remisionFacade;
    @EJB
    private RemisionesDetFacade remisionDetFacade;
    @EJB
    private MercaderiasFacade mercaderiaFacade;
    @EJB
    private RemisionesFacturasFacade remisionFacturaFacade;
    
    private List<Depositos> listaDepoOrigen, listaDepoDestino;
    private List<Empleados> listaEmpleados;
    private List<Conductores> listaConductor;
    private List<Transportistas> listaTransportista;

    private Empleados empleado;
    private Depositos depoOrigen, depoDestino;
    private Conductores conductor;
    private Transportistas transportista;

    private Integer nroEnvioInicia, nroEnvioFinal;
    private Date fechaRemision;

    private Integer estabInicial;
    private Integer expedInicial;
    private Integer SecueInicial;

    private String radioButton;

    @PostConstruct
    public void init() {
        listaEmpleados = new ArrayList<Empleados>();
        listaConductor = new ArrayList<Conductores>();
        listaTransportista = new ArrayList<Transportistas>();
        listaDepoDestino = depositoFacade.listarDepositosActivos();
        buscarNroInicialRemision();

        fechaRemision = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaRemision); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, 1);  // numero de días a añadir, o restar en caso de días<0
        fechaRemision = calendar.getTime();

        radioButton = "O";
        estabInicial = 1;
        expedInicial = 1;

    }

    public String getRadioButton() {
        return radioButton;
    }

    public void setRadioButton(String radioButton) {
        this.radioButton = radioButton;
    }

    public Date getFechaRemision() {
        return fechaRemision;
    }

    public void setFechaRemision(Date fechaRemision) {
        this.fechaRemision = fechaRemision;
    }

    public Integer getNroEnvioInicia() {
        return nroEnvioInicia;
    }

    public void setNroEnvioInicia(Integer nroEnvioInicia) {
        this.nroEnvioInicia = nroEnvioInicia;
    }

    public Integer getNroEnvioFinal() {
        return nroEnvioFinal;
    }

    public void setNroEnvioFinal(Integer nroEnvioFinal) {
        this.nroEnvioFinal = nroEnvioFinal;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Depositos getDepoOrigen() {
        return depoOrigen;
    }

    public void setDepoOrigen(Depositos depoOrigen) {
        this.depoOrigen = depoOrigen;
    }

    public Depositos getDepoDestino() {
        return depoDestino;
    }

    public void setDepoDestino(Depositos depoDestino) {
        this.depoDestino = depoDestino;
    }

    public Conductores getConductor() {
        return conductor;
    }

    public void setConductor(Conductores conductor) {
        this.conductor = conductor;
    }

    public Transportistas getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportistas transportista) {
        this.transportista = transportista;
    }

    public List<Depositos> getListaDepoOrigen() {
        return listaDepoOrigen;
    }

    public void setListaDepoOrigen(List<Depositos> listaDepoOrigen) {
        this.listaDepoOrigen = listaDepoOrigen;
    }

    public List<Depositos> getListaDepoDestino() {
        return listaDepoDestino;
    }

    public void setListaDepoDestino(List<Depositos> listaDepoDestino) {
        this.listaDepoDestino = listaDepoDestino;
    }

    public List<Empleados> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Conductores> getListaConductor() {
        return listaConductor;
    }

    public void setListaConductor(List<Conductores> listaConductor) {
        this.listaConductor = listaConductor;
    }

    public List<Transportistas> getListaTransportista() {
        return listaTransportista;
    }

    public void setListaTransportista(List<Transportistas> listaTransportista) {
        this.listaTransportista = listaTransportista;
    }

    public Integer getEstabInicial() {
        return estabInicial;
    }

    public void setEstabInicial(Integer estabInicial) {
        this.estabInicial = estabInicial;
    }

    public Integer getExpedInicial() {
        return expedInicial;
    }

    public void setExpedInicial(Integer expedInicial) {
        this.expedInicial = expedInicial;
    }

    public Integer getSecueInicial() {
        return SecueInicial;
    }

    public void setSecueInicial(Integer SecueInicial) {
        this.SecueInicial = SecueInicial;
    }

    public void procesar() {
        if (radioButton.equals("O")) {//ORIGINALE
            grab_det_remision2();
        } else if (radioButton.equals("C")) {//COMPLEMENTARIA
            grab_det_complementario();
        }
    }

    public void onChangeDeposito() {
        if (depoDestino != null) {
            if (depoDestino.getDepositosPK() != null) {
                if (depoDestino.getDepositosPK().getCodDepo() != 0) {
                    listaTransportista = transportistaFacade.listaTransportistasPorDeposito(depoDestino.getDepositosPK().getCodDepo());
                    if (listaTransportista.isEmpty()) {//Si no hay valor de transportista
                        System.out.println("No hay transportista");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso ", "No hay registro de transportista"));
                        return;
                    }
                    listaConductor = conductorFacade.listaConductoresPorDeposito(depoDestino.getDepositosPK().getCodDepo());
                    if (listaConductor.isEmpty()) {//Si no hay conductor
                        System.out.println("No hay Conductor");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso ", "No hay registro de conductor"));
                        return;
                    }

                    listaEmpleados = empleadoFacade.listarEntregadorPorDeposito(depoDestino.getDepositosPK().getCodDepo());
                    if (listaEmpleados.isEmpty()) {//Si no hay entregador
                        System.out.println("No hay Entregador");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso ", "No hay registro de entregador"));
                        return;
                    }
                }
            }
        }
    }

    //Buscar el ultimo y sumar 1
    private void buscarNroInicialRemision() {
        List<Remisiones> remi = remisionFacade.buscarNroInicialRemision();
        if (remi.isEmpty()) {//si no hay nro remision inicial
            SecueInicial = 1;
        } else {
            System.out.println("nro remicion : " + remi.get(0).getRemisionesPK().getNroRemision());
            String rem = String.valueOf(remi.get(0).getRemisionesPK().getNroRemision()).substring(3);
            System.out.println("nro remicion sacado: " + rem);
            SecueInicial = Integer.parseInt(rem) + 1;
        }
    }

    private void grab_det_remision2() {
        List<Existencias> lista = mercaderiaFacade.listaGrabDetRemision2(depoDestino.getDepositosPK().getCodDepo());
        Integer l_max_lineas = 20;
        Integer l_nro_remision = SecueInicial - 1;

        Integer l_lineas = 0;
        RemisionesPK rePk = new RemisionesPK();
        rePk.setCodEmpr((short) 2);
        RemisionesDetPK reDePk = new RemisionesDetPK();
        reDePk.setCodEmpr((short) 2);
        System.out.println("Comienzo de interacion:"+lista.size());
        for (Existencias e : lista) {
            System.out.println("est : " + estabInicial + " = exp : " + expedInicial + " = sec : " + l_nro_remision);
            String l_npunto_est = String.format("%1s", String.valueOf(estabInicial)).replace(' ', '0');
            String l_npunto_exp = String.format("%2s", String.valueOf(expedInicial)).replace(' ', '0');
            String l_npunto_sec = String.format("%7s", String.valueOf(l_nro_remision)).replace(' ', '0');
            String l_new_remision = l_npunto_est + l_npunto_exp + l_npunto_sec;

            l_lineas++;
            System.out.println("linea : "+l_lineas);
            if (l_lineas > l_max_lineas) {
                l_nro_remision++;
                rePk.setNroRemision(Integer.parseInt(l_new_remision));
                System.out.println("numero remision descp : " + l_nro_remision);
                Remisiones re = new Remisiones(rePk);
                re.setFremision(fechaRemision);
                re.setCodDepo(depoDestino.getDepositosPK().getCodDepo());
                re.setCodConductor(conductor.getCodConductor());
                re.setCodTransp(transportista.getCodTransp());
                re.setCodEntregador(empleado.getEmpleadosPK().getCodEmpleado());
                re.setMtipo('O');
                re.setMestado('A');
                String est = String.format("%3s", String.valueOf(estabInicial)).replace(' ', '0');
                String exp = String.format("%3s", String.valueOf(expedInicial)).replace(' ', '0');
                String sec = String.format("%7s", String.valueOf(l_nro_remision)).replace(' ', '0');
                re.setXnroRemision(est + "-" + exp + "-" + sec);
                System.out.println("x Nro remision : " + re.getXnroRemision());
                int c = remisionFacade.verificarNroRemision(Integer.parseInt(l_new_remision));
                if (c == 0) {
                    System.out.println("Crear remision");
                    grabar_cab_remision(re);
                    l_lineas = 1;
                    l_nro_remision--;
                } else {
                    System.out.println("remision no creado:" + l_new_remision);
                }
            }

            String l_cod_merca = e.getMercaderias().getMercaderiasPK().getCodMerca();
            long l_cant_cajas = e.getCantCajas();
            long l_cant_unid = e.getCantUnid();
            String l_xdesc = e.getMercaderias().getXdesc();

            System.out.println("est : " + l_npunto_est + " = exp : " + l_npunto_exp + " = sec : " + l_npunto_sec);

            reDePk.setNroRemision(Long.parseLong(l_new_remision));
            reDePk.setCodMerca(l_cod_merca);

            System.out.println("NroRemision : " + reDePk.getNroRemision() + " = codMerca : " + reDePk.getCodMerca());

            RemisionesDet remiDet = new RemisionesDet(reDePk);
            remiDet.setXdesc(l_xdesc);
            remiDet.setCantCajas(Integer.parseInt(String.valueOf(l_cant_cajas)));
            remiDet.setCantUnid(Integer.parseInt(String.valueOf(l_cant_unid)));
            int c = remisionFacade.verificarNroRemisionDet(String.valueOf(remiDet.getRemisionesDetPK().getNroRemision()), remiDet.getRemisionesDetPK().getCodMerca());
            System.out.println("NroRemision : " + remiDet.getRemisionesDetPK().getNroRemision() + " = codMerca : " + remiDet.getRemisionesDetPK().getCodMerca() + "   =  c:" + c);
            if (c == 0) {
                System.out.println("Crear detalle remision!!");
                remisionDetFacade.create(remiDet);
            } else {
                System.out.println("existe detalle remision!!");
            }

            ins_factura_remision(l_new_remision, l_cod_merca, depoDestino.getDepositosPK().getCodDepo());
        }
    }

    private void grabar_cab_remision(Remisiones remi) {
        remisionFacade.create(remi);
    }

    //Insertar factura remision
    private void ins_factura_remision(String l_new_remision, String l_merca, short l_cod_depo_destino) {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

        List<Object[]> lista = remisionFacade.insertarFacturasRemisiones(formato.format(fechaRemision), l_cod_depo_destino, l_merca);
        RemisionesFacturasPK reFaPk = new RemisionesFacturasPK();
        reFaPk.setCodEmpr((short) 2);
        reFaPk.setNroRemision(Long.parseLong(l_new_remision));
        System.out.println("Lista insertar Facturas remisiones.");
        for (Object[] o : lista) {
            String l_nrofact = (String) o[1];
            String l_ctipo_docum = (String) o[0];

            int e = remisionFacade.verificarRemisionFactura(l_new_remision, l_nrofact, l_ctipo_docum);
    
            if (e == 0) {
                System.out.println("Insertar remision factura");
                reFaPk.setNrofact(Long.valueOf(l_nrofact));
                reFaPk.setCtipoDocum(l_ctipo_docum);
                RemisionesFacturas re = new RemisionesFacturas(reFaPk);
                remisionFacturaFacade.create(re);
            }
        }
    }

    private void grab_det_complementario() {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Integer l_nro_remision = SecueInicial - 1;

        List<Object[]> lista = remisionFacade.grabarRemisionComplementaria(formato.format(fechaRemision), nroEnvioInicia, nroEnvioFinal);

        Integer l_lineas = 20;
        RemisionesPK rePk = new RemisionesPK();
        rePk.setCodEmpr((short) 2);
        RemisionesDetPK reDePk = new RemisionesDetPK();
        reDePk.setCodEmpr((short) 2);
        System.out.println("Comienzo de iteracion : " + lista.size());
        for (Object[] o : lista) {
            System.out.println("est : " + estabInicial + " = exp : " + expedInicial + " = sec : " + l_nro_remision);
            String l_npunto_est = String.format("%1s", String.valueOf(estabInicial)).replace(' ', '0');
            String l_npunto_exp = String.format("%2s", String.valueOf(expedInicial)).replace(' ', '0');
            String l_npunto_sec = String.format("%7s", String.valueOf(l_nro_remision)).replace(' ', '0');
            System.out.println("est : " + l_npunto_est + " = exp : " + l_npunto_exp + " = sec : " + l_npunto_sec);
            String l_new_remision = l_npunto_est + l_npunto_exp + l_npunto_sec;

            l_lineas++;
            System.out.println("linea : " + l_lineas);
            if (l_lineas > 20) {
                l_nro_remision++;
                rePk.setNroRemision(Integer.parseInt(l_new_remision));
                System.out.println("numero remision descp : " + l_nro_remision);
                Remisiones re = new Remisiones(rePk);
                re.setFremision(fechaRemision);
                re.setCodDepo(depoDestino.getDepositosPK().getCodDepo());
                re.setCodConductor(conductor.getCodConductor());
                re.setCodTransp(transportista.getCodTransp());
                re.setCodEntregador(empleado.getEmpleadosPK().getCodEmpleado());
                re.setMtipo('O');
                re.setMestado('A');
                String est = String.format("%3s", String.valueOf(estabInicial)).replace(' ', '0');
                String exp = String.format("%3s", String.valueOf(expedInicial)).replace(' ', '0');
                String sec = String.format("%7s", String.valueOf(l_nro_remision)).replace(' ', '0');
                re.setXnroRemision(est + "-" + exp + "-" + sec);
                System.out.println("x Nro remision : " + re.getXnroRemision());
                int c = remisionFacade.verificarNroRemision(Integer.parseInt(l_new_remision));
                if (c == 0) {
                    System.out.println("Crear remision");
                    grabar_cab_remision(re);
                    l_lineas = 1;
                    l_nro_remision--;
                } else {
                    System.out.println("remision no creado:" + l_new_remision);
                }
            } else {
                System.out.println("No es mayor a 20");
            }
            System.out.println("Fin de iteracion");

            String l_cod_merca = (String) o[0];
            BigDecimal l_cant_cajas = (BigDecimal) o[3];
            BigDecimal l_cant_unid = (BigDecimal) o[4];
            String l_xdesc = (String) o[1];

            reDePk.setNroRemision(Long.parseLong(l_new_remision));
            reDePk.setCodMerca(l_cod_merca);

            System.out.println("NroRemision : " + reDePk.getNroRemision() + " = codMerca : " + reDePk.getCodMerca());

            RemisionesDet remiDet = new RemisionesDet(reDePk);
            remiDet.setXdesc(l_xdesc);
            remiDet.setCantCajas(Integer.parseInt(String.valueOf(l_cant_cajas)));
            remiDet.setCantUnid(Integer.parseInt(String.valueOf(l_cant_unid)));
            int c = remisionFacade.verificarNroRemisionDet(String.valueOf(remiDet.getRemisionesDetPK().getNroRemision()), remiDet.getRemisionesDetPK().getCodMerca());
            System.out.println("NroRemision : " + remiDet.getRemisionesDetPK().getNroRemision() + " = codMerca : " + remiDet.getRemisionesDetPK().getCodMerca() + "   =  c:" + c);
            if (c == 0) {
                System.out.println("Crear detalle remision!!");
                remisionDetFacade.create(remiDet);
            } else {
                System.out.println("existe detalle remision!!");
            }
        }
    }
   
}
