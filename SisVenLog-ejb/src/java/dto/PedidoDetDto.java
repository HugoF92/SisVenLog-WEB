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
public class PedidoDetDto {
    private Long nroPromo;
    private long totalBonif;
    private String codMerca;
    private Short nrelacion;
    private long reserCajas;
    private long reserUnid;
    private int cantCajas;
    private int cantUnid;
    private int cajasBonif;
    private int unidBonif;
    private long nroDocum;
    private Date fDocum;
    private String codZona;
    private String xDesc;
    private BigDecimal pDesc;
    private long precioCaja;
    private long precioUnidad;
    private String cTipoDocum;
    private String codMercaBonif;
    
    public Long getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(Long nroPromo) {
        this.nroPromo = nroPromo;
    }

    public long getTotalBonif() {
        return totalBonif;
    }

    public void setTotalBonif(long totalBonif) {
        this.totalBonif = totalBonif;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public Short getNrelacion() {
        return nrelacion;
    }

    public void setNrelacion(Short nrelacion) {
        this.nrelacion = nrelacion;
    }

    public long getReserCajas() {
        return reserCajas;
    }

    public void setReserCajas(long reserCajas) {
        this.reserCajas = reserCajas;
    }

    public long getReserUnid() {
        return reserUnid;
    }

    public void setReserUnid(long reserUnid) {
        this.reserUnid = reserUnid;
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

    public int getCajasBonif() {
        return cajasBonif;
    }

    public void setCajasBonif(int cajasBonif) {
        this.cajasBonif = cajasBonif;
    }

    public int getUnidBonif() {
        return unidBonif;
    }

    public void setUnidBonif(int unidBonif) {
        this.unidBonif = unidBonif;
    }

    public long getNroDocum() {
        return nroDocum;
    }

    public void setNroDocum(long nroDocum) {
        this.nroDocum = nroDocum;
    }

    public Date getfDocum() {
        return fDocum;
    }

    public void setfDocum(Date fDocum) {
        this.fDocum = fDocum;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public String getxDesc() {
        return xDesc;
    }

    public void setxDesc(String xDesc) {
        this.xDesc = xDesc;
    }

    public BigDecimal getpDesc() {
        return pDesc;
    }

    public void setpDesc(BigDecimal pDesc) {
        this.pDesc = pDesc;
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

    public String getcTipoDocum() {
        return cTipoDocum;
    }

    public void setcTipoDocum(String cTipoDocum) {
        this.cTipoDocum = cTipoDocum;
    }

    public String getCodMercaBonif() {
        return codMercaBonif;
    }

    public void setCodMercaBonif(String codMercaBonif) {
        this.codMercaBonif = codMercaBonif;
    }
    
    
    
}
