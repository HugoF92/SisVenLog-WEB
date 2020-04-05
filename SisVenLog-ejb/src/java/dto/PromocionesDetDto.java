/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author dadob
 */
public class PromocionesDetDto {
     private Integer nroPromo;
    private String codMerca;
    private String xDesc;
    private Character mY;
    private BigDecimal kKilosIni;
    private BigDecimal kKilosFin;
    private BigDecimal kCajasIni;
    private BigDecimal kCajasFin;
    private BigDecimal kUnidIni;
    private BigDecimal kUnidFin;
    private short porKUnidad;
    private short porKCajas;
    private BigDecimal kUnidBonif;
    private BigDecimal pDesc;
    private short kMaxUnidBonif;
    private Character cTipoCliente;
    private Character mTodos;
    private short nRelaPack;
    private BigDecimal iDesc;
    private short codSublinea;
    private short nRelacion;
    private Character cTipoVenta;

    public Integer getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(Integer nroPromo) {
        this.nroPromo = nroPromo;
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

    public Character getmY() {
        return mY;
    }

    public void setmY(Character mY) {
        this.mY = mY;
    }

    public BigDecimal getkKilosIni() {
        return kKilosIni;
    }

    public void setkKilosIni(BigDecimal kKilosIni) {
        this.kKilosIni = kKilosIni;
    }

    public BigDecimal getkKilosFin() {
        return kKilosFin;
    }

    public void setkKilosFin(BigDecimal kKilosFin) {
        this.kKilosFin = kKilosFin;
    }

    public BigDecimal getkCajasIni() {
        return kCajasIni;
    }

    public void setkCajasIni(BigDecimal kCajasIni) {
        this.kCajasIni = kCajasIni;
    }

    public BigDecimal getkCajasFin() {
        return kCajasFin;
    }

    public void setkCajasFin(BigDecimal kCajasFin) {
        this.kCajasFin = kCajasFin;
    }

    public BigDecimal getkUnidIni() {
        return kUnidIni;
    }

    public void setkUnidIni(BigDecimal kUnidIni) {
        this.kUnidIni = kUnidIni;
    }

    public BigDecimal getkUnidFin() {
        return kUnidFin;
    }

    public void setkUnidFin(BigDecimal kUnidFin) {
        this.kUnidFin = kUnidFin;
    }

    public short getPorKUnidad() {
        return porKUnidad;
    }

    public void setPorKUnidad(short porKUnidad) {
        this.porKUnidad = porKUnidad;
    }

    public short getPorKCajas() {
        return porKCajas;
    }

    public void setPorKCajas(short porKCajas) {
        this.porKCajas = porKCajas;
    }

    public BigDecimal getkUnidBonif() {
        return kUnidBonif;
    }

    public void setkUnidBonif(BigDecimal kUnidBonif) {
        this.kUnidBonif = kUnidBonif;
    }

    public BigDecimal getpDesc() {
        return pDesc;
    }

    public void setpDesc(BigDecimal pDesc) {
        this.pDesc = pDesc;
    }

    public short getkMaxUnidBonif() {
        return kMaxUnidBonif;
    }

    public void setkMaxUnidBonif(short kMaxUnidBonif) {
        this.kMaxUnidBonif = kMaxUnidBonif;
    }

    public Character getcTipoCliente() {
        return cTipoCliente;
    }

    public void setcTipoCliente(Character cTipoCliente) {
        this.cTipoCliente = cTipoCliente;
    }

    public Character getmTodos() {
        return mTodos;
    }

    public void setmTodos(Character mTodos) {
        this.mTodos = mTodos;
    }

    public short getnRelaPack() {
        return nRelaPack;
    }

    public void setnRelaPack(short nRelaPack) {
        this.nRelaPack = nRelaPack;
    }

    public BigDecimal getiDesc() {
        return iDesc;
    }

    public void setiDesc(BigDecimal iDesc) {
        this.iDesc = iDesc;
    }

    public short getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(short codSublinea) {
        this.codSublinea = codSublinea;
    }

    public short getnRelacion() {
        return nRelacion;
    }

    public void setnRelacion(short nRelacion) {
        this.nRelacion = nRelacion;
    }

    public Character getcTipoVenta() {
        return cTipoVenta;
    }

    public void setcTipoVenta(Character cTipoVenta) {
        this.cTipoVenta = cTipoVenta;
    }
    
}
