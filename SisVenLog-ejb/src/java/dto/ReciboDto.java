/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.Recibos;
import java.util.Date;

/**
 *
 * @author jvera
 */
public class ReciboDto {
    private Recibos recibos;
    private Integer codCliente;
    private String nombreCliente;
    private String tipoDocumento;
    private long nroDocumento;
    private String descBanco;
    private Date fechaAnulacion;
    private char tipodet;
    private Integer codCliente2;
    private String nombreCliente2;
    private long iTotal;
    
    
    public Recibos getRecibos() {
        return recibos;
    }

    public void setRecibos(Recibos recibos) {
        this.recibos = recibos;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public long getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(long nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getDescBanco() {
        return descBanco;
    }

    public void setDescBanco(String descBanco) {
        this.descBanco = descBanco;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public char getTipodet() {
        return tipodet;
    }

    public void setTipodet(char tipodet) {
        this.tipodet = tipodet;
    }

    public Integer getCodCliente2() {
        return codCliente2;
    }

    public void setCodCliente2(Integer codCliente2) {
        this.codCliente2 = codCliente2;
    }

    public String getNombreCliente2() {
        return nombreCliente2;
    }

    public void setNombreCliente2(String nombreCliente2) {
        this.nombreCliente2 = nombreCliente2;
    }

    public long getiTotal() {
        return iTotal;
    }

    public void setiTotal(long iTotal) {
        this.iTotal = iTotal;
    }
    
}
