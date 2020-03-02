package bean.consultas;

import dao.PersonalizedFacade;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import util.LlamarReportes;

@ManagedBean
@SessionScoped
public class ActuPedidosBean {
    
    @EJB
    private PersonalizedFacade personalizedFacade;

    private Date fechaActual;
    private Date fechaNueva;
    private String nrosPedido;

    public ActuPedidosBean() throws IOException {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        
    }

    public void ejecutar() {
        
        
        String factual = "";
        String fnueva = "";
        
        
        if (this.fechaActual == null) {
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ingrese la fecha de pedidos a actualizar"));
            return;
            
        }else{
           factual = dateToString(fechaActual); 
        }
        
        
        if (this.fechaNueva == null) {
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ingrese la nueva fecha de pedidos"));
            return;
            
        }else{
            fnueva = dateToString(fechaNueva);
        }
        
         
        if (this.nrosPedido.trim().equals("")) {
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "Ingrese los nros de pedidos"));
            return;
                                    
        }
        
        
      String sentencia = "update pedidos set fpedido = '"+fnueva+"' \n"
              + "where cod_empr = 2 and nro_pedido in ("+nrosPedido+") \n"
              + "and cast(fpedido as date) = '"+factual+"' and mestado = 'N' ";
      
      //actulizamos los pedidos
     int actu =  personalizedFacade.ejecutarSentenciaSQL(sentencia);
      
      
      
     //Integer resultado =  personalizedFacade.ejecutarConsultaRetornaInt("SELECT @@ROWCOUNT AS KFILAS");
     
     
        if (actu <= 0) {
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atencion", "No se encontraron datos a actualizar"));
            return;
            
        }else{
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Atencion", "Se han actualizado " + actu + " pedidos"));
            return;
            
        }
      
      
      
        

        /*rep.reporteLiProcesamientos(dateToString(desde), dateToString(hasta), dateToString2(desde), dateToString2(hasta), "admin", tipo);
*/
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

    private String dateToString2(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }

    //Getters & Setters

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Date getFechaNueva() {
        return fechaNueva;
    }

    public void setFechaNueva(Date fechaNueva) {
        this.fechaNueva = fechaNueva;
    }

    public String getNrosPedido() {
        return nrosPedido;
    }

    public void setNrosPedido(String nrosPedido) {
        this.nrosPedido = nrosPedido;
    }
    
    
    

}
