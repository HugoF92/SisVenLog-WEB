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
public class ImpDatoDto {
    
    private BigDecimal pDesc;
    private int unidBonif;
    private Date fInicial;
    private Date fFinal;
    private long nroFact;
    private Long nroPromo;
    private Long nroPromoGeneral;
    private Character mCalculo;
    private int cantCajas;
    private int cantUnidades;
    private long ibonific;
    private long costoCaja;
    private long costoUnidad;
    private long idescto;
    private long ttotal;

    public BigDecimal getpDesc() {
        return pDesc;
    }

    public void setpDesc(BigDecimal pDesc) {
        this.pDesc = pDesc;
    }

    public int getUnidBonif() {
        return unidBonif;
    }

    public void setUnidBonif(int unidBonif) {
        this.unidBonif = unidBonif;
    }

    public Date getfInicial() {
        return fInicial;
    }

    public void setfInicial(Date fInicial) {
        this.fInicial = fInicial;
    }

    public Date getfFinal() {
        return fFinal;
    }

    public void setfFinal(Date fFinal) {
        this.fFinal = fFinal;
    }

    public long getNroFact() {
        return nroFact;
    }

    public void setNroFact(long nroFact) {
        this.nroFact = nroFact;
    }

    public Long getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(Long nroPromo) {
        this.nroPromo = nroPromo;
    }

    public Long getNroPromoGeneral() {
        return nroPromoGeneral;
    }

    public void setNroPromoGeneral(Long nroPromoGeneral) {
        this.nroPromoGeneral = nroPromoGeneral;
    }

    public Character getmCalculo() {
        return mCalculo;
    }

    public void setmCalculo(Character mCalculo) {
        this.mCalculo = mCalculo;
    }

    public int getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(int cantCajas) {
        this.cantCajas = cantCajas;
    }

    public int getCantUnidades() {
        return cantUnidades;
    }

    public void setCantUnidades(int cantUnidades) {
        this.cantUnidades = cantUnidades;
    }

    public long getIbonific() {
        return ibonific;
    }

    public void setIbonific(long ibonific) {
        this.ibonific = ibonific;
    }

    public long getCostoCaja() {
        return costoCaja;
    }

    public void setCostoCaja(long costoCaja) {
        this.costoCaja = costoCaja;
    }

    public long getCostoUnidad() {
        return costoUnidad;
    }

    public void setCostoUnidad(long costoUnidad) {
        this.costoUnidad = costoUnidad;
    }

    public long getIdescto() {
        return idescto;
    }

    public void setIdescto(long idescto) {
        this.idescto = idescto;
    }

    public long getTtotal() {
        return ttotal;
    }

    public void setTtotal(long ttotal) {
        this.ttotal = ttotal;
    }
        
}
