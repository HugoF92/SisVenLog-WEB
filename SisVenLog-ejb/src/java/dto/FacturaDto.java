/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.Facturas;

/**
 *
 * @author dadob
 */
public class FacturaDto {
    
    private Facturas factura;
    private String nombreDeposito;
    private String nombreVendedor;
    private String descripcionCanal;
    private Short codRuta;
    private Character ctipoCliente;
    private String tipoDocumentoDescripcion;

    public Facturas getFactura() {
        return factura;
    }

    public void setFactura(Facturas factura) {
        this.factura = factura;
    }

    public String getNombreDeposito() {
        return nombreDeposito;
    }

    public void setNombreDeposito(String nombreDeposito) {
        this.nombreDeposito = nombreDeposito;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getDescripcionCanal() {
        return descripcionCanal;
    }

    public void setDescripcionCanal(String descripcionCanal) {
        this.descripcionCanal = descripcionCanal;
    }

    public Short getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(Short codRuta) {
        this.codRuta = codRuta;
    }

    public Character getCtipoCliente() {
        return ctipoCliente;
    }

    public void setCtipoCliente(Character ctipoCliente) {
        this.ctipoCliente = ctipoCliente;
    }

    public String getTipoDocumentoDescripcion() {
        return tipoDocumentoDescripcion;
    }

    public void setTipoDocumentoDescripcion(String tipoDocumentoDescripcion) {
        this.tipoDocumentoDescripcion = tipoDocumentoDescripcion;
    }
    
    
}
