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
 * @author dadob
 */
public class NotaVentaDetDto {
    
    private String cconc;
    private String ctipoDocum;
    private long nroNota;
    private Long nroPromo;
    private Date fDocum;
    private String codMerca;
    private String xDesc;
    private int cantCajas;
    private int cantUnid;
    private String codZona;
    private long iexentas;
    private long igravadas;
    private BigDecimal impuestos;
    private long ttotal;
    private BigDecimal pdesc;
    private long precioCaja;
    private long precioUnidad;
    private String facCtipoDocum;
    private Long nrofact;
    private BigDecimal nrelacion;

    public String getCconc() {
        return cconc;
    }

    public void setCconc(String cconc) {
        this.cconc = cconc;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public long getNroNota() {
        return nroNota;
    }

    public void setNroNota(long nroNota) {
        this.nroNota = nroNota;
    }

    public Long getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(Long nroPromo) {
        this.nroPromo = nroPromo;
    }

    public Date getfDocum() {
        return fDocum;
    }

    public void setfDocum(Date fDocum) {
        this.fDocum = fDocum;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public String getxDesc() {
        return xDesc;
    }

    public void setxDesc(String xDesc) {
        this.xDesc = xDesc;
    }

    public int getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(int cantCajas) {
        this.cantCajas = cantCajas;
    }

    public int getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(int cantUnid) {
        this.cantUnid = cantUnid;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public long getIexentas() {
        return iexentas;
    }

    public void setIexentas(long iexentas) {
        this.iexentas = iexentas;
    }

    public long getIgravadas() {
        return igravadas;
    }

    public void setIgravadas(long igravadas) {
        this.igravadas = igravadas;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public long getTtotal() {
        return ttotal;
    }

    public void setTtotal(long ttotal) {
        this.ttotal = ttotal;
    }

    public BigDecimal getPdesc() {
        return pdesc;
    }

    public void setPdesc(BigDecimal pdesc) {
        this.pdesc = pdesc;
    }

    public long getPrecioCaja() {
        return precioCaja;
    }

    public void setPrecioCaja(long precioCaja) {
        this.precioCaja = precioCaja;
    }

    public long getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(long precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public String getFacCtipoDocum() {
        return facCtipoDocum;
    }

    public void setFacCtipoDocum(String facCtipoDocum) {
        this.facCtipoDocum = facCtipoDocum;
    }

    public Long getNrofact() {
        return nrofact;
    }

    public void setNrofact(Long nrofact) {
        this.nrofact = nrofact;
    }

    public BigDecimal getNrelacion() {
        return nrelacion;
    }

    public void setNrelacion(BigDecimal nrelacion) {
        this.nrelacion = nrelacion;
    }
    
    
    
}
