/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author dadob
 */
public class CantidadDto {
    private int cantidadCajas;
    private int cantidadUnidades;
    private BigDecimal relacion;

    public int getCantidadCajas() {
        return cantidadCajas;
    }

    public void setCantidadCajas(int cantidadCajas) {
        this.cantidadCajas = cantidadCajas;
    }

    public int getCantidadUnidades() {
        return cantidadUnidades;
    }

    public void setCantidadUnidades(int cantidadUnidades) {
        this.cantidadUnidades = cantidadUnidades;
    }

    public BigDecimal getRelacion() {
        return relacion;
    }

    public void setRelacion(BigDecimal relacion) {
        this.relacion = relacion;
    }
    
}
