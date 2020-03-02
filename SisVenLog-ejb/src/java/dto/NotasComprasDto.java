/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.NotasCompras;

/**
 *
 * @author dadob
 */
public class NotasComprasDto {
    
    private NotasCompras notaCompra;
    private String nombreProveedor;

    public NotasCompras getNotaCompra() {
        return notaCompra;
    }

    public void setNotaCompra(NotasCompras notaCompra) {
        this.notaCompra = notaCompra;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }
    
    
}
