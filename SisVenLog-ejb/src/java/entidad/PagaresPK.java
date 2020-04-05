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
public class PagaresPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "npagare")
    private long npagare;

    public PagaresPK() {
    }

    public PagaresPK(short codEmpr, long npagare) {
        this.codEmpr = codEmpr;
        this.npagare = npagare;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) npagare;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagaresPK)) {
            return false;
        }
        PagaresPK other = (PagaresPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.npagare != other.npagare) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PagaresPK[ codEmpr=" + codEmpr + ", npagare=" + npagare + " ]";
    }
    
}
