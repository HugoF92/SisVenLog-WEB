/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Asus
 */
public class RecibosComprasDto implements Serializable {
    private String ctipoDocum;
    private long ncuota;
    private Date frecibo;
    private long nrecibo;
    private Long nrofact;
    private String ctipo;
    private Date ffactur;
    private long iefectivo;
    private String nroCheque;
    private long ipagado;
    private long moneda;
    private long cotizacion;
    private short codProveed;
    private long itotal;
    private BigInteger ntimbrado;
    private BigInteger factTimbrado;
    private String notaTimbrado;

    public RecibosComprasDto(String ctipoDocum, long ncuota, Date frecibo, long nrecibo, Long nrofact, String ctipo, Date ffactur, long iefectivo, String nroCheque, long ipagado, long moneda, long cotizacion, short codProveed, long itotal, BigInteger ntimbrado, BigInteger factTimbrado, String notaTimbrado) {
        this.ctipoDocum = ctipoDocum;
        this.ncuota = ncuota;
        this.frecibo = frecibo;
        this.nrecibo = nrecibo;
        this.nrofact = nrofact;
        this.ctipo = ctipo;
        this.ffactur = ffactur;
        this.iefectivo = iefectivo;
        this.nroCheque = nroCheque;
        this.ipagado = ipagado;
        this.moneda = moneda;
        this.cotizacion = cotizacion;
        this.codProveed = codProveed;
        this.itotal = itotal;
        this.ntimbrado = ntimbrado;
        this.factTimbrado = factTimbrado;
        this.notaTimbrado = notaTimbrado;
    }

    public RecibosComprasDto() {
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

    public Long getNrofact() {
        return nrofact;
    }

    public String getCtipo() {
        return ctipo;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public long getIefectivo() {
        return iefectivo;
    }

    public String getNroCheque() {
        return nroCheque;
    }

    public long getIpagado() {
        return ipagado;
    }

    public long getMoneda() {
        return moneda;
    }

    public long getCotizacion() {
        return cotizacion;
    }

    public short getCodProveed() {
        return codProveed;
    }

    public long getItotal() {
        return itotal;
    }

    public BigInteger getNtimbrado() {
        return ntimbrado;
    }

    public BigInteger getFactTimbrado() {
        return factTimbrado;
    }

    public String getNotaTimbrado() {
        return notaTimbrado;
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

    public void setNrofact(Long nrofact) {
        this.nrofact = nrofact;
    }

    public void setCtipo(String ctipo) {
        this.ctipo = ctipo;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public void setIefectivo(long iefectivo) {
        this.iefectivo = iefectivo;
    }

    public void setNroCheque(String nroCheque) {
        this.nroCheque = nroCheque;
    }

    public void setIpagado(long ipagado) {
        this.ipagado = ipagado;
    }

    public void setMoneda(long moneda) {
        this.moneda = moneda;
    }

    public void setCotizacion(long cotizacion) {
        this.cotizacion = cotizacion;
    }

    public void setCodProveed(short codProveed) {
        this.codProveed = codProveed;
    }

    public void setItotal(long itotal) {
        this.itotal = itotal;
    }

    public void setNtimbrado(BigInteger ntimbrado) {
        this.ntimbrado = ntimbrado;
    }

    public void setFactTimbrado(BigInteger factTimbrado) {
        this.factTimbrado = factTimbrado;
    }

    public void setNotaTimbrado(String notaTimbrado) {
        this.notaTimbrado = notaTimbrado;
    }
}