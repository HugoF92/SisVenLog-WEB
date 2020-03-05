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
public class RecaudacionDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_planilla")
    private long nroPlanilla;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_zona")
    private String codZona;

    public RecaudacionDetPK() {
    }

    public RecaudacionDetPK(short codEmpr, long nroPlanilla, String codZona) {
        this.codEmpr = codEmpr;
        this.nroPlanilla = nroPlanilla;
        this.codZona = codZona;
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

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroPlanilla;
        hash += (codZona != null ? codZona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecaudacionDetPK)) {
            return false;
        }
        RecaudacionDetPK other = (RecaudacionDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroPlanilla != other.nroPlanilla) {
            return false;
        }
        if ((this.codZona == null && other.codZona != null) || (this.codZona != null && !this.codZona.equals(other.codZona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecaudacionDetPK[ codEmpr=" + codEmpr + ", nroPlanilla=" + nroPlanilla + ", codZona=" + codZona + " ]";
    }
    
}
