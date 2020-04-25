/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Clara
 */
public class LiPagares {
    
    private Integer nroPagare;
    private Date fechEmision;
    private Date fechVencimiento;
    private Integer codCliente;
    private String nombreCliente;
    private Integer codEntregador;
    private String nombreEntregador;
    private BigDecimal iPagare;
    private String estado;
    private Date fCobro;
    private String codZona;
    private String tipoDocum;
    private Integer nrofact;
    private Date fechaFactur;
    private BigDecimal iTotal;

    public Integer getNroPagare() {
        return nroPagare;
    }

    public Date getFechEmision() {
        return fechEmision;
    }

    public Date getFechVencimiento() {
        return fechVencimiento;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public Integer getCodEntregador() {
        return codEntregador;
    }

    public String getNombreEntregador() {
        return nombreEntregador;
    }

    public String getEstado() {
        return estado;
    }

    public Date getfCobro() {
        return fCobro;
    }

    public String getCodZona() {
        return codZona;
    }

    public String getTipoDocum() {
        return tipoDocum;
    }

    public Integer getNrofact() {
        return nrofact;
    }

    public Date getFechaFactur() {
        return fechaFactur;
    }

    public BigDecimal getiTotal() {
        return iTotal;
    }

    public void setNroPagare(Integer nroPagare) {
        this.nroPagare = nroPagare;
    }

    public void setFechEmision(Date fechEmision) {
        this.fechEmision = fechEmision;
    }

    public void setFechVencimiento(Date fechVencimiento) {
        this.fechVencimiento = fechVencimiento;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setCodEntregador(Integer codEntregador) {
        this.codEntregador = codEntregador;
    }

    public void setNombreEntregador(String nombreEntregador) {
        this.nombreEntregador = nombreEntregador;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setfCobro(Date fCobro) {
        this.fCobro = fCobro;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public BigDecimal getiPagare() {
        return iPagare;
    }

    public void setiPagare(BigDecimal iPagare) {
        this.iPagare = iPagare;
    }

    public void setTipoDocum(String tipoDocum) {
        this.tipoDocum = tipoDocum;
    }

    public void setNrofact(Integer nrofact) {
        this.nrofact = nrofact;
    }

    public void setFechaFactur(Date fechaFactur) {
        this.fechaFactur = fechaFactur;
    }

    public void setiTotal(BigDecimal iTotal) {
        this.iTotal = iTotal;
    }
}
