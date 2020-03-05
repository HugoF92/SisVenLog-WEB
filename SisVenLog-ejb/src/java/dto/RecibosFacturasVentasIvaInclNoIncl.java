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
public class RecibosFacturasVentasIvaInclNoIncl implements Serializable {

    private String xrazonSocial;
    private Date ffactur;
    private Long nrofact;
    private String ctipoDocum;
    private Character mtipoPapel;
    private long nroDocumIni;
    private Long nroDocumFin;
    private BigInteger ntimbrado;
    private long ttotal;
    private String xruc;
    private String xfactura;
    private long texentas;
    private long tgravadas10;
    private long tgravadas5;
    private long timpuestos10;
    private long timpuestos5;

    public RecibosFacturasVentasIvaInclNoIncl(String xrazonSocial, Date ffactur, Long nrofact, String ctipoDocum, Character mtipoPapel, long nroDocumIni, Long nroDocumFin, BigInteger ntimbrado, long ttotal, String xruc, String xfactura, long texentas, long tgravadas10, long tgravadas5, long timpuestos10, long timpuestos5) {
        this.xrazonSocial = xrazonSocial;
        this.ffactur = ffactur;
        this.nrofact = nrofact;
        this.ctipoDocum = ctipoDocum;
        this.mtipoPapel = mtipoPapel;
        this.nroDocumIni = nroDocumIni;
        this.nroDocumFin = nroDocumFin;
        this.ntimbrado = ntimbrado;
        this.ttotal = ttotal;
        this.xruc = xruc;
        this.xfactura = xfactura;
        this.texentas = texentas;
        this.tgravadas10 = tgravadas10;
        this.tgravadas5 = tgravadas5;
        this.timpuestos10 = timpuestos10;
        this.timpuestos5 = timpuestos5;
    }

    public RecibosFacturasVentasIvaInclNoIncl() {
    }

    public String getXrazonSocial() {
        return xrazonSocial;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public Long getNrofact() {
        return nrofact;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public Character getMtipoPapel() {
        return mtipoPapel;
    }

    public long getNroDocumIni() {
        return nroDocumIni;
    }

    public Long getNroDocumFin() {
        return nroDocumFin;
    }

    public BigInteger getNtimbrado() {
        return ntimbrado;
    }

    public long getTtotal() {
        return ttotal;
    }

    public String getXruc() {
        return xruc;
    }

    public String getXfactura() {
        return xfactura;
    }

    public long getTexentas() {
        return texentas;
    }

    public long getTgravadas10() {
        return tgravadas10;
    }

    public long getTgravadas5() {
        return tgravadas5;
    }

    public long getTimpuestos10() {
        return timpuestos10;
    }

    public long getTimpuestos5() {
        return timpuestos5;
    }

    public void setXrazonSocial(String xrazonSocial) {
        this.xrazonSocial = xrazonSocial;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public void setNrofact(Long nrofact) {
        this.nrofact = nrofact;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public void setMtipoPapel(Character mtipoPapel) {
        this.mtipoPapel = mtipoPapel;
    }

    public void setNroDocumIni(long nroDocumIni) {
        this.nroDocumIni = nroDocumIni;
    }

    public void setNroDocumFin(Long nroDocumFin) {
        this.nroDocumFin = nroDocumFin;
    }

    public void setNtimbrado(BigInteger ntimbrado) {
        this.ntimbrado = ntimbrado;
    }

    public void setTtotal(long ttotal) {
        this.ttotal = ttotal;
    }

    public void setXruc(String xruc) {
        this.xruc = xruc;
    }

    public void setXfactura(String xfactura) {
        this.xfactura = xfactura;
    }

    public void setTexentas(long texentas) {
        this.texentas = texentas;
    }

    public void setTgravadas10(long tgravadas10) {
        this.tgravadas10 = tgravadas10;
    }

    public void setTgravadas5(long tgravadas5) {
        this.tgravadas5 = tgravadas5;
    }

    public void setTimpuestos10(long timpuestos10) {
        this.timpuestos10 = timpuestos10;
    }

    public void setTimpuestos5(long timpuestos5) {
        this.timpuestos5 = timpuestos5;
    }

}
