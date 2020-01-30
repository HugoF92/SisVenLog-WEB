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
public class RemisionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_remision")
    private long nroRemision;

    public RemisionesPK() {
    }

    public RemisionesPK(short codEmpr, long nroRemision) {
        this.codEmpr = codEmpr;
        this.nroRemision = nroRemision;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroRemision;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemisionesPK)) {
            return false;
        }
        RemisionesPK other = (RemisionesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroRemision != other.nroRemision) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RemisionesPK[ codEmpr=" + codEmpr + ", nroRemision=" + nroRemision + " ]";
    }
    
}
