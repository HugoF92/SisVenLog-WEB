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
    
    private String tipoDocum;
    private Integer nrofact;
    private Date fechaFactur;
    private BigDecimal iTotal;    
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
    
}
