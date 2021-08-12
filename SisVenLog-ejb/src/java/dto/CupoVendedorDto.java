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
public class CupoVendedorDto {
    
    private short totalCajas;
    private short totalUnidades;
    private BigDecimal relacion;

    public short getTotalCajas() {
        return totalCajas;
    }

    public void setTotalCajas(short totalCajas) {
        this.totalCajas = totalCajas;
    }

    public short getTotalUnidades() {
        return totalUnidades;
    }

    public void setTotalUnidades(short totalUnidades) {
        this.totalUnidades = totalUnidades;
    }

    public BigDecimal getRelacion() {
        return relacion;
    }

    public void setRelacion(BigDecimal relacion) {
        this.relacion = relacion;
    }
       
}
