/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.Date;

/**
 *
 * @author dadob
 */
public class CuentaCorrienteDto {
    
    private Date fechaMovimiento;
    private long exentas;
    private long gravadas;
    private long impuestos;
    private String concepto;
    private Date fechaVencimiento;
    private long importePagado;
    private String tipoDocumento;
    private String numeroDocumentoCheque;

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public long getExentas() {
        return exentas;
    }

    public void setExentas(long exentas) {
        this.exentas = exentas;
    }

    public long getGravadas() {
        return gravadas;
    }

    public void setGravadas(long gravadas) {
        this.gravadas = gravadas;
    }

    public long getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(long impuestos) {
        this.impuestos = impuestos;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public long getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(long importePagado) {
        this.importePagado = importePagado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumentoCheque() {
        return numeroDocumentoCheque;
    }

    public void setNumeroDocumentoCheque(String numeroDocumentoCheque) {
        this.numeroDocumentoCheque = numeroDocumentoCheque;
    } 
}
