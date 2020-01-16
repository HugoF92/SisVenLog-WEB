/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.Envios;

/**
 *
 * @author dadob
 */
public class EnvioDto {
    
    private Envios envio;
    private String descripcionCanal;
    private String nombreEmpleado;
    private String depositoOrigen;
    private String depositoDestino;

    public Envios getEnvio() {
        return envio;
    }

    public void setEnvio(Envios envio) {
        this.envio = envio;
    }

    public String getDescripcionCanal() {
        return descripcionCanal;
    }

    public void setDescripcionCanal(String descripcionCanal) {
        this.descripcionCanal = descripcionCanal;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getDepositoOrigen() {
        return depositoOrigen;
    }

    public void setDepositoOrigen(String depositoOrigen) {
        this.depositoOrigen = depositoOrigen;
    }

    public String getDepositoDestino() {
        return depositoDestino;
    }

    public void setDepositoDestino(String depositoDestino) {
        this.depositoDestino = depositoDestino;
    }
}
