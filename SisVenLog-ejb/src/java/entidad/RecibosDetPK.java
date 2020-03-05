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
public class RecibosDetPK implements Serializable {

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
    @Column(name = "ndocum")
    private long ndocum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;

    public RecibosDetPK() {
    }

    public RecibosDetPK(short codEmpr, long nrecibo, long ndocum, String ctipoDocum) {
        this.codEmpr = codEmpr;
        this.nrecibo = nrecibo;
        this.ndocum = ndocum;
        this.ctipoDocum = ctipoDocum;
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

    public long getNdocum() {
        return ndocum;
    }

    public void setNdocum(long ndocum) {
        this.ndocum = ndocum;
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
        hash += (int) nrecibo;
        hash += (int) ndocum;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosDetPK)) {
            return false;
        }
        RecibosDetPK other = (RecibosDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nrecibo != other.nrecibo) {
            return false;
        }
        if (this.ndocum != other.ndocum) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosDetPK[ codEmpr=" + codEmpr + ", nrecibo=" + nrecibo + ", ndocum=" + ndocum + ", ctipoDocum=" + ctipoDocum + " ]";
    }
    
}
