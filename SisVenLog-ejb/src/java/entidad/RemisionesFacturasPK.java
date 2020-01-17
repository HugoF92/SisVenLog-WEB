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
public class RemisionesFacturasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_remision")
    private long nroRemision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofact")
    private long nrofact;

    public RemisionesFacturasPK() {
    }

    public RemisionesFacturasPK(short codEmpr, long nroRemision, String ctipoDocum, long nrofact) {
        this.codEmpr = codEmpr;
        this.nroRemision = nroRemision;
        this.ctipoDocum = ctipoDocum;
        this.nrofact = nrofact;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNroRemision() {
        return nroRemision;
    }

    public void setNroRemision(long nroRemision) {
        this.nroRemision = nroRemision;
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
        hash += (int) nroRemision;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (int) nrofact;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemisionesFacturasPK)) {
            return false;
        }
        RemisionesFacturasPK other = (RemisionesFacturasPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroRemision != other.nroRemision) {
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
        return "entidad.RemisionesFacturasPK[ codEmpr=" + codEmpr + ", nroRemision=" + nroRemision + ", ctipoDocum=" + ctipoDocum + ", nrofact=" + nrofact + " ]";
    }
    
}
