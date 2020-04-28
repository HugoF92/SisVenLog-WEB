/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Clara
 */
public class LiVentas {
    
    //nmes|cod_cliente|xrazon_social|iventas|cod_vendedor|xnombre|
    //nmes|cod_cliente|xrazon_social|ttotal|cod_vendedor|xnombre|ctipo_docum|
    private int nmes;
    private int codCliente;
    private String razonSocialCliente;
    private BigDecimal monto;
    private String codigo;
    private String descripcion;
    private Map<Integer, BigDecimal> montoPorMes;
    private Short codVendedor;
    private String descripcionVendedor;
    private Short codRuta;
    private String descripcionRunta;
    private String codZona;
    private String descripcionZona;

    public int getNmes() {
        return nmes;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public String getRazonSocialCliente() {
        return razonSocialCliente;
    }

    public BigDecimal getMonto() {
        return monto;
    }    

    public String getDescripcion() {
        return descripcion;
    }

    public void setNmes(int nmes) {
        this.nmes = nmes;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public void setRazonSocialCliente(String razonSocialCliente) {
        this.razonSocialCliente = razonSocialCliente;
    }

    public void setMonto(BigDecimal ventas) {
        this.monto = ventas;
    }

    public void setDescripcion(String nombreVendedor) {
        this.descripcion = nombreVendedor;
    }

    public Map<Integer, BigDecimal> getMontoPorMes() {
        return montoPorMes;
    }

    public void setMontoPorMes(Map<Integer, BigDecimal> montoPorMes) {
        this.montoPorMes = montoPorMes;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Short getCodVendedor() {
        return codVendedor;
    }

    public String getDescripcionVendedor() {
        return descripcionVendedor;
    }

    public Short getCodRuta() {
        return codRuta;
    }

    public String getDescripcionRunta() {
        return descripcionRunta;
    }

    public String getCodZona() {
        return codZona;
    }

    public String getDescripcionZona() {
        return descripcionZona;
    }

    public void setCodVendedor(Short codVendedor) {
        this.codVendedor = codVendedor;
    }

    public void setDescripcionVendedor(String descripcionVendedor) {
        this.descripcionVendedor = descripcionVendedor;
    }

    public void setCodRuta(Short codRuta) {
        this.codRuta = codRuta;
    }

    public void setDescripcionRunta(String descripcionRunta) {
        this.descripcionRunta = descripcionRunta;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public void setDescripcionZona(String descripcionZona) {
        this.descripcionZona = descripcionZona;
    }
    
}
