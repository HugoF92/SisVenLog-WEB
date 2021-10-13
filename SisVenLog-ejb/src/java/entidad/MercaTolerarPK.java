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
 * 
 * @author admin
 */
@Embeddable
public class MercaTolerarPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofact")
    private long nrofact;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private short codProveed;

    public MercaTolerarPK() {
    }

    public MercaTolerarPK(short codEmpr, long nrofact, String codMerca, short codProveed) {
        this.codEmpr = codEmpr;
        this.nrofact = nrofact;
        this.codMerca = codMerca;
        this.codProveed = codProveed;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNrofact() {
        return nrofact;
    }

    public void setNrofact(long nrofact) {
        this.nrofact = nrofact;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(short codProveed) {
        this.codProveed = codProveed;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nrofact;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (int) codProveed;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MercaTolerarPK)) {
            return false;
        }
        MercaTolerarPK other = (MercaTolerarPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nrofact != other.nrofact) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if (this.codProveed != other.codProveed) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MercaTolerarPK[ codEmpr=" + codEmpr + ", nrofact=" + nrofact + ", codMerca=" + codMerca + ", codProveed=" + codProveed + " ]";
    }
    
}
