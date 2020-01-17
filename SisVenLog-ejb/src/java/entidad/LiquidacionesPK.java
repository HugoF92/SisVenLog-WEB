/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author admin
 */
@Embeddable
public class LiquidacionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_liquid")
    private int nroLiquid;

    public LiquidacionesPK() {
    }

    public LiquidacionesPK(short codEmpr, int nroLiquid) {
        this.codEmpr = codEmpr;
        this.nroLiquid = nroLiquid;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public int getNroLiquid() {
        return nroLiquid;
    }

    public void setNroLiquid(int nroLiquid) {
        this.nroLiquid = nroLiquid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroLiquid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LiquidacionesPK)) {
            return false;
        }
        LiquidacionesPK other = (LiquidacionesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroLiquid != other.nroLiquid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.LiquidacionesPK[ codEmpr=" + codEmpr + ", nroLiquid=" + nroLiquid + " ]";
    }
    
}
