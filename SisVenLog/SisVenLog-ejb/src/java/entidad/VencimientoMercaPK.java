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
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class VencimientoMercaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofact")
    private long nrofact;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private long codProveed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aitem")
    private short aitem;

    public VencimientoMercaPK() {
    }

    public VencimientoMercaPK(short codEmpr, String ctipoDocum, long nrofact, long codProveed, String codMerca, short aitem) {
        this.codEmpr = codEmpr;
        this.ctipoDocum = ctipoDocum;
        this.nrofact = nrofact;
        this.codProveed = codProveed;
        this.codMerca = codMerca;
        this.aitem = aitem;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public long getNrofact() {
        return nrofact;
    }

    public void setNrofact(long nrofact) {
        this.nrofact = nrofact;
    }

    public long getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(long codProveed) {
        this.codProveed = codProveed;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public short getAitem() {
        return aitem;
    }

    public void setAitem(short aitem) {
        this.aitem = aitem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (int) nrofact;
        hash += (int) codProveed;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (int) aitem;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VencimientoMercaPK)) {
            return false;
        }
        VencimientoMercaPK other = (VencimientoMercaPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if (this.nrofact != other.nrofact) {
            return false;
        }
        if (this.codProveed != other.codProveed) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if (this.aitem != other.aitem) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.VencimientoMercaPK[ codEmpr=" + codEmpr + ", ctipoDocum=" + ctipoDocum + ", nrofact=" + nrofact + ", codProveed=" + codProveed + ", codMerca=" + codMerca + ", aitem=" + aitem + " ]";
    }
    
}
