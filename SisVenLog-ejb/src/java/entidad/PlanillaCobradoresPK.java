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
public class PlanillaCobradoresPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nplanilla")
    private short nplanilla;

    public PlanillaCobradoresPK() {
    }

    public PlanillaCobradoresPK(short codEmpr, short nplanilla) {
        this.codEmpr = codEmpr;
        this.nplanilla = nplanilla;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getNplanilla() {
        return nplanilla;
    }

    public void setNplanilla(short nplanilla) {
        this.nplanilla = nplanilla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nplanilla;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanillaCobradoresPK)) {
            return false;
        }
        PlanillaCobradoresPK other = (PlanillaCobradoresPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nplanilla != other.nplanilla) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PlanillaCobradoresPK[ codEmpr=" + codEmpr + ", nplanilla=" + nplanilla + " ]";
    }
    
}
