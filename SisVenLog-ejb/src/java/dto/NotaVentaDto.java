/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.NotasVentas;

/**
 *
 * @author dadob
 */
public class NotaVentaDto {
    
    private NotasVentas notaVenta;
    private String nombreCliente;

    public NotasVentas getNotaVenta() {
        return notaVenta;
    }

    public void setNotaVenta(NotasVentas notaVenta) {
        this.notaVenta = notaVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
    
}
