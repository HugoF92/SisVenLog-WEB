/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.Cheques;
import java.util.Date;

/**
 *
 * @author dadob
 */
public class ChequeDetalleDto {
    
    private String tipoDocumento;
    private String nroCheque;
    private String nombreBanco;
    private Date fechaVencimiento;
    private long importePagado;
    private Date fechaEmision;
    private Cheques cheque;

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNroCheque() {
        return nroCheque;
    }

    public void setNroCheque(String nroCheque) {
        this.nroCheque = nroCheque;
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

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public Cheques getCheque() {
        return cheque;
    }

    public void setCheque(Cheques cheque) {
        this.cheque = cheque;
    }
}
