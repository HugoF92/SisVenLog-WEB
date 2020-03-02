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
public class RecaudacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_planilla")
    private long nroPlanilla;

    public RecaudacionPK() {
    }

    public RecaudacionPK(short codEmpr, long nroPlanilla) {
        this.codEmpr = codEmpr;
        this.nroPlanilla = nroPlanilla;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNroPlanilla() {
        return nroPlanilla;
    }

    public void setNroPlanilla(long nroPlanilla) {
        this.nroPlanilla = nroPlanilla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroPlanilla;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecaudacionPK)) {
            return false;
        }
        RecaudacionPK other = (RecaudacionPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroPlanilla != other.nroPlanilla) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecaudacionPK[ codEmpr=" + codEmpr + ", nroPlanilla=" + nroPlanilla + " ]";
    }
    
}
