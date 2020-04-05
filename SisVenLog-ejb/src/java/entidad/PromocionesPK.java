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
 * @author Administrador
 */
@Embeddable
public class PromocionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_promo")
    private long nroPromo;

    public PromocionesPK() {
    }

    public PromocionesPK(short codEmpr, long nroPromo) {
        this.codEmpr = codEmpr;
        this.nroPromo = nroPromo;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(long nroPromo) {
        this.nroPromo = nroPromo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroPromo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PromocionesPK)) {
            return false;
        }
        PromocionesPK other = (PromocionesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroPromo != other.nroPromo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PromocionesPK[ codEmpr=" + codEmpr + ", nroPromo=" + nroPromo + " ]";
    }
    
}
