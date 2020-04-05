/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.FacturasDet;
import java.math.BigDecimal;

/**
 *
 * @author dadob
 */
public class FacturaDetDto {
    private BigDecimal pimpues;
    private BigDecimal totalImpuestos;
    private long totalIgravadas;
    private FacturasDet facturasDet;
    private Short nRelacion;
    private String descPromo;
    private Character mPromoPackDet;
    private Short codSublineaDet;
    private BigDecimal nPesoCajas;
    private BigDecimal nPesoUnidad;

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
    }

    public BigDecimal getTotalImpuestos() {
        return totalImpuestos;
    }

    public void setTotalImpuestos(BigDecimal totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }

    public long getTotalIgravadas() {
        return totalIgravadas;
    }

    public void setTotalIgravadas(long totalIgravadas) {
        this.totalIgravadas = totalIgravadas;
    }

    public FacturasDet getFacturasDet() {
        return facturasDet;
    }

    public void setFacturasDet(FacturasDet facturasDet) {
        this.facturasDet = facturasDet;
    }

    public Short getnRelacion() {
        return nRelacion;
    }

    public void setnRelacion(Short nRelacion) {
        this.nRelacion = nRelacion;
    }

    public String getDescPromo() {
        return descPromo;
    }

    public void setDescPromo(String descPromo) {
        this.descPromo = descPromo;
    }

    public Character getmPromoPackDet() {
        return mPromoPackDet;
    }

    public void setmPromoPackDet(Character mPromoPackDet) {
        this.mPromoPackDet = mPromoPackDet;
    }

    public Short getCodSublineaDet() {
        return codSublineaDet;
    }

    public void setCodSublineaDet(Short codSublineaDet) {
        this.codSublineaDet = codSublineaDet;
    }

    public BigDecimal getnPesoCajas() {
        return nPesoCajas;
    }

    public void setnPesoCajas(BigDecimal nPesoCajas) {
        this.nPesoCajas = nPesoCajas;
    }

    public BigDecimal getnPesoUnidad() {
        return nPesoUnidad;
    }

    public void setnPesoUnidad(BigDecimal nPesoUnidad) {
        this.nPesoUnidad = nPesoUnidad;
    }
    
}
