/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class ComprasPK implements Serializable {

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
    private short codProveed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;

    public ComprasPK() {
    }

    public ComprasPK(short codEmpr, String ctipoDocum, long nrofact, short codProveed, Date ffactur) {
        this.codEmpr = codEmpr;
        this.ctipoDocum = ctipoDocum;
        this.nrofact = nrofact;
        this.codProveed = codProveed;
        this.ffactur = ffactur;
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

    public short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(short codProveed) {
        this.codProveed = codProveed;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (int) nrofact;
        hash += (int) codProveed;
        hash += (ffactur != null ? ffactur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComprasPK)) {
            return false;
        }
        ComprasPK other = (ComprasPK) object;
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
        if ((this.ffactur == null && other.ffactur != null) || (this.ffactur != null && !this.ffactur.equals(other.ffactur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ComprasPK[ codEmpr=" + codEmpr + ", ctipoDocum=" + ctipoDocum + ", nrofact=" + nrofact + ", codProveed=" + codProveed + ", ffactur=" + ffactur + " ]";
    }
    
}
