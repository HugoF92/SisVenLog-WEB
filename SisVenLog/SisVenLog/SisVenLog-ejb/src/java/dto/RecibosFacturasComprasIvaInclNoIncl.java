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
public class RecibosFacturasComprasIvaInclNoIncl implements Serializable {

    private String xnombre;
    private Date ffactur;
    private Long nrofact;
    private String ctipoDocum;
    private Double ttotal;
    private String xruc;
    private String xfactura;
    private BigInteger ntimbrado;
    private Double texentas;
    private Double tgravadas10;
    private Double tgravadas5;
    private Double timpuestos10;
    private Double timpuestos5;
    private String cconc;

    public RecibosFacturasComprasIvaInclNoIncl(String xnombre, Date ffactur, Long nrofact, String ctipoDocum, Double ttotal, String xruc, String xfactura, BigInteger ntimbrado, Double texentas, Double tgravadas10, Double tgravadas5, Double timpuestos10, Double timpuestos5, String cconc) {
        this.xnombre = xnombre;
        this.ffactur = ffactur;
        this.nrofact = nrofact;
        this.ctipoDocum = ctipoDocum;
        this.ttotal = ttotal;
        this.xruc = xruc;
        this.xfactura = xfactura;
        this.ntimbrado = ntimbrado;
        this.texentas = texentas;
        this.tgravadas10 = tgravadas10;
        this.tgravadas5 = tgravadas5;
        this.timpuestos10 = timpuestos10;
        this.timpuestos5 = timpuestos5;
        this.cconc = cconc;
    }

    public RecibosFacturasComprasIvaInclNoIncl() {
    }

    public String getXnombre() {
        return xnombre;
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

    public Double getTtotal() {
        return ttotal;
    }

    public String getXruc() {
        return xruc;
    }

    public String getXfactura() {
        return xfactura;
    }

    public BigInteger getNtimbrado() {
        return ntimbrado;
    }

    public Double getTexentas() {
        return texentas;
    }

    public Double getTgravadas10() {
        return tgravadas10;
    }

    public Double getTgravadas5() {
        return tgravadas5;
    }

    public Double getTimpuestos10() {
        return timpuestos10;
    }

    public Double getTimpuestos5() {
        return timpuestos5;
    }

    public String getCconc() {
        return cconc;
    }

    public void setXnombre(String xnombre) {
        this.xnombre = xnombre;
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

    public void setTtotal(Double ttotal) {
        this.ttotal = ttotal;
    }

    public void setXruc(String xruc) {
        this.xruc = xruc;
    }

    public void setXfactura(String xfactura) {
        this.xfactura = xfactura;
    }

    public void setNtimbrado(BigInteger ntimbrado) {
        this.ntimbrado = ntimbrado;
    }

    public void setTexentas(Double texentas) {
        this.texentas = texentas;
    }

    public void setTgravadas10(Double tgravadas10) {
        this.tgravadas10 = tgravadas10;
    }

    public void setTgravadas5(Double tgravadas5) {
        this.tgravadas5 = tgravadas5;
    }

    public void setTimpuestos10(Double timpuestos10) {
        this.timpuestos10 = timpuestos10;
    }

    public void setTimpuestos5(Double timpuestos5) {
        this.timpuestos5 = timpuestos5;
    }

    public void setCconc(String cconc) {
        this.cconc = cconc;
    }
}
