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
public class LiquidacionesDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_liquid")
    private int nroLiquid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nitem")
    private short nitem;

    public LiquidacionesDetPK() {
    }

    public LiquidacionesDetPK(short codEmpr, int nroLiquid, short nitem) {
        this.codEmpr = codEmpr;
        this.nroLiquid = nroLiquid;
        this.nitem = nitem;
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

    public short getNitem() {
        return nitem;
    }

    public void setNitem(short nitem) {
        this.nitem = nitem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroLiquid;
        hash += (int) nitem;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LiquidacionesDetPK)) {
            return false;
        }
        LiquidacionesDetPK other = (LiquidacionesDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroLiquid != other.nroLiquid) {
            return false;
        }
        if (this.nitem != other.nitem) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.LiquidacionesDetPK[ codEmpr=" + codEmpr + ", nroLiquid=" + nroLiquid + ", nitem=" + nitem + " ]";
    }
    
}
