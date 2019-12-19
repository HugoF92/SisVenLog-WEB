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
public class RecibosProvPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrecibo")
    private long nrecibo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private short codProveed;

    public RecibosProvPK() {
    }

    public RecibosProvPK(short codEmpr, long nrecibo, short codProveed) {
        this.codEmpr = codEmpr;
        this.nrecibo = nrecibo;
        this.codProveed = codProveed;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNrecibo() {
        return nrecibo;
    }

    public void setNrecibo(long nrecibo) {
        this.nrecibo = nrecibo;
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
        hash += (int) nrecibo;
        hash += (int) codProveed;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosProvPK)) {
            return false;
        }
        RecibosProvPK other = (RecibosProvPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nrecibo != other.nrecibo) {
            return false;
        }
        if (this.codProveed != other.codProveed) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosProvPK[ codEmpr=" + codEmpr + ", nrecibo=" + nrecibo + ", codProveed=" + codProveed + " ]";
    }
    
}
