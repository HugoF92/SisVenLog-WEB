/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Asus
 */
public class RecibosVentasDto implements Serializable {
    private String ctipoDocum;
    private long ncuota;
    private Date frecibo;
    private long nrecibo;
    private String ctipo;
    private long ndocum;
    private Date ffactur;
    private Double iefectivo;
    private String nroCheque;
    private Double ipagado;
    private Double moneda;
    private Double cotizacion;

    public RecibosVentasDto(String ctipoDocum, long ncuota, Date frecibo, long nrecibo, String ctipo, long ndocum, Date ffactur, Double iefectivo, String nroCheque, Double ipagado, Double moneda, Double cotizacion) {
        this.ctipoDocum = ctipoDocum;
        this.ncuota = ncuota;
        this.frecibo = frecibo;
        this.nrecibo = nrecibo;
        this.ctipo = ctipo;
        this.ndocum = ndocum;
        this.ffactur = ffactur;
        this.iefectivo = iefectivo;
        this.nroCheque = nroCheque;
        this.ipagado = ipagado;
        this.moneda = moneda;
        this.cotizacion = cotizacion;
    }

    public RecibosVentasDto() {
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public long getNcuota() {
        return ncuota;
    }

    public Date getFrecibo() {
        return frecibo;
    }

    public long getNrecibo() {
        return nrecibo;
    }

    public String getCtipo() {
        return ctipo;
    }

    public long getNdocum() {
        return ndocum;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public Double getIefectivo() {
        return iefectivo;
    }

    public String getNroCheque() {
        return nroCheque;
    }

    public Double getIpagado() {
        return ipagado;
    }

    public Double getMoneda() {
        return moneda;
    }

    public Double getCotizacion() {
        return cotizacion;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public void setNcuota(long ncuota) {
        this.ncuota = ncuota;
    }

    public void setFrecibo(Date frecibo) {
        this.frecibo = frecibo;
    }

    public void setNrecibo(long nrecibo) {
        this.nrecibo = nrecibo;
    }

    public void setCtipo(String ctipo) {
        this.ctipo = ctipo;
    }

    public void setNdocum(long ndocum) {
        this.ndocum = ndocum;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public void setIefectivo(Double iefectivo) {
        this.iefectivo = iefectivo;
    }

    public void setNroCheque(String nroCheque) {
        this.nroCheque = nroCheque;
    }

    public void setIpagado(Double ipagado) {
        this.ipagado = ipagado;
    }

    public void setMoneda(Double moneda) {
        this.moneda = moneda;
    }

    public void setCotizacion(Double cotizacion) {
        this.cotizacion = cotizacion;
    }
}
