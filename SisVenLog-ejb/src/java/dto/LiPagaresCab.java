/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Clara
 */
public class LiPagaresCab {
    //factura
    private String tipoDocum;
    private Integer nrofact;
    private Date fechaFactur;
    private BigDecimal iTotal; 
    //pagare
    private Integer nroPagare;
    private Date fechEmision;
    private Date fechVencimiento;
    private Integer codCliente;
    private String nombreCliente;
    private Integer codEntregador;
    private String nombreEntregador;
    private BigDecimal iPagare;
    private String estado;
    
    List<LiPagares> detalles;
    private Integer nroInicial;
    private Integer nroFinal;

    public void setNroInicial(Integer nroInicial) {
        this.nroInicial = nroInicial;
    }

    public void setNroFinal(Integer nroFinal) {
        this.nroFinal = nroFinal;
    }

    public Integer getNroInicial() {
        return nroInicial;
    }

    public Integer getNroFinal() {
        return nroFinal;
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

    public List<LiPagares> getDetalles() {
        return detalles;
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

    public void setDetalles(List<LiPagares> detalles) {
        this.detalles = detalles;
    }

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

    public BigDecimal getiPagare() {
        return iPagare;
    }

    public String getEstado() {
        return estado;
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

    public void setiPagare(BigDecimal iPagare) {
        this.iPagare = iPagare;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
