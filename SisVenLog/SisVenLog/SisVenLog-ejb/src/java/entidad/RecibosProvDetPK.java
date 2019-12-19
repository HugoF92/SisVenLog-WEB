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
public class RecibosProvDetPK implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofact")
    private long nrofact;

    public RecibosProvDetPK() {
    }

    public RecibosProvDetPK(short codEmpr, long nrecibo, short codProveed, String ctipoDocum, long nrofact) {
        this.codEmpr = codEmpr;
        this.nrecibo = nrecibo;
        this.codProveed = codProveed;
        this.ctipoDocum = ctipoDocum;
        this.nrofact = nrofact;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nrecibo;
        hash += (int) codProveed;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (int) nrofact;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosProvDetPK)) {
            return false;
        }
        RecibosProvDetPK other = (RecibosProvDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nrecibo != other.nrecibo) {
            return false;
        }
        if (this.codProveed != other.codProveed) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if (this.nrofact != other.nrofact) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosProvDetPK[ codEmpr=" + codEmpr + ", nrecibo=" + nrecibo + ", codProveed=" + codProveed + ", ctipoDocum=" + ctipoDocum + ", nrofact=" + nrofact + " ]";
    }
    
}
