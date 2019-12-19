/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.ChequesEmitidos;

/**
 *
 * @author Edu
 */
public class ChequeProveedorDto {
    
    private ChequesEmitidos chequeEmitido;
    private String nombreBanco;
    private String nombreProveedor;
    private boolean chequeEmitidoSeleccionado;

    public ChequeProveedorDto() {
    }
    
    public ChequesEmitidos getChequeEmitido() {
        return chequeEmitido;
    }

    public void setChequeEmitido(ChequesEmitidos chequeEmitido) {
        this.chequeEmitido = chequeEmitido;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public boolean isChequeEmitidoSeleccionado() {
        return chequeEmitidoSeleccionado;
    }

    public void setChequeEmitidoSeleccionado(boolean chequeEmitidoSeleccionado) {
        this.chequeEmitidoSeleccionado = chequeEmitidoSeleccionado;
    }

}
