/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.Compras;

/**
 *
 * @author dadob
 */
public class CompraDto {
    
    private Compras compra;
    private String descripcionDeposito;
    private String nombreProveedor;

    public Compras getCompra() {
        return compra;
    }

    public void setCompra(Compras compra) {
        this.compra = compra;
    }

    public String getDescripcionDeposito() {
        return descripcionDeposito;
    }

    public void setDescripcionDeposito(String descripcionDeposito) {
        this.descripcionDeposito = descripcionDeposito;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

}
