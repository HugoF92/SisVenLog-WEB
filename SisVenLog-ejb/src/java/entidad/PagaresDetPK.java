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
public class PagaresDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "npagare")
    private long npagare;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofact")
    private long nrofact;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;

    public PagaresDetPK() {
    }

    public PagaresDetPK(short codEmpr, long npagare, long nrofact, String ctipoDocum) {
        this.codEmpr = codEmpr;
        this.npagare = npagare;
        this.nrofact = nrofact;
        this.ctipoDocum = ctipoDocum;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNpagare() {
        return npagare;
    }

    public void setNpagare(long npagare) {
        this.npagare = npagare;
    }

    public long getNrofact() {
        return nrofact;
    }

    public void setNrofact(long nrofact) {
        this.nrofact = nrofact;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) npagare;
        hash += (int) nrofact;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagaresDetPK)) {
            return false;
        }
        PagaresDetPK other = (PagaresDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.npagare != other.npagare) {
            return false;
        }
        if (this.nrofact != other.nrofact) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PagaresDetPK[ codEmpr=" + codEmpr + ", npagare=" + npagare + ", nrofact=" + nrofact + ", ctipoDocum=" + ctipoDocum + " ]";
    }
    
}
