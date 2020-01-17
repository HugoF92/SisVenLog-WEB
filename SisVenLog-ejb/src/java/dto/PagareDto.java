/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.Pagares;

/**
 *
 * @author Edu
 */
public class PagareDto {
    
    private Pagares Pagare;
    private String nombreCliente;
    private boolean pagareSeleccionado;

    public Pagares getPagare() {
        return Pagare;
    }

    public void setPagare(Pagares Pagare) {
        this.Pagare = Pagare;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public boolean isPagareSeleccionado() {
        return pagareSeleccionado;
    }

    public void setPagareSeleccionado(boolean pagareSeleccionado) {
        this.pagareSeleccionado = pagareSeleccionado;
    }
    
}
