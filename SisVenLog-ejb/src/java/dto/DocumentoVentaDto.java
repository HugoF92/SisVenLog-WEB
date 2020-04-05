/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.TiposDocumentos;
import entidad.Zonas;
import java.util.Date;

/**
 *
 * @author dadob
 */
public class DocumentoVentaDto {
    
    private TiposDocumentos tipoDocumento;
    private Date fechaDocumento;
    private long nroDcumento;
    private long montoDocumento;
    private long montoAPagarDocumento;
    private long saldoDocumento;
    private long interesDocumento;
    private long nroRecibo;
    private Date fechaRecibo;
    private String observacion;
    private Zonas zona;
    private Integer codigoCliente;
    private long saldoFactura;
    private Date fechaVencimiento;

    public TiposDocumentos getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentos tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public long getNroDcumento() {
        return nroDcumento;
    }

    public void setNroDcumento(long nroDcumento) {
        this.nroDcumento = nroDcumento;
    }

    public long getMontoDocumento() {
        return montoDocumento;
    }

    public void setMontoDocumento(long montoDocumento) {
        this.montoDocumento = montoDocumento;
    }

    public long getMontoAPagarDocumento() {
        return montoAPagarDocumento;
    }

    public void setMontoAPagarDocumento(long montoAPagarDocumento) {
        this.montoAPagarDocumento = montoAPagarDocumento;
    }

    public long getSaldoDocumento() {
        return saldoDocumento;
    }

    public void setSaldoDocumento(long saldoDocumento) {
        this.saldoDocumento = saldoDocumento;
    }

    public long getInteresDocumento() {
        return interesDocumento;
    }

    public void setInteresDocumento(long interesDocumento) {
        this.interesDocumento = interesDocumento;
    }

    public long getNroRecibo() {
        return nroRecibo;
    }

    public void setNroRecibo(long nroRecibo) {
        this.nroRecibo = nroRecibo;
    }

    public Date getFechaRecibo() {
        return fechaRecibo;
    }

    public void setFechaRecibo(Date fechaRecibo) {
        this.fechaRecibo = fechaRecibo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Integer codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public long getSaldoFactura() {
        return saldoFactura;
    }

    public void setSaldoFactura(long saldoFactura) {
        this.saldoFactura = saldoFactura;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
}
