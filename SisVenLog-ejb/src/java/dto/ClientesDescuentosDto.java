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
public class ClientesDescuentosDto {
    
    private Integer codCliente;
    private Short codSublinea;
    private BigDecimal pDescMax;

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public Short getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(Short codSublinea) {
        this.codSublinea = codSublinea;
    }

    public BigDecimal getpDescMax() {
        return pDescMax;
    }

    public void setpDescMax(BigDecimal pDescMax) {
        this.pDescMax = pDescMax;
    }
    
    
}
