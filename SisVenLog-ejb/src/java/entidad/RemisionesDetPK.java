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
public class RemisionesDetPK implements Serializable {

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
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;

    public RemisionesDetPK() {
    }

    public RemisionesDetPK(short codEmpr, long nroRemision, String codMerca) {
        this.codEmpr = codEmpr;
        this.nroRemision = nroRemision;
        this.codMerca = codMerca;
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

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroRemision;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemisionesDetPK)) {
            return false;
        }
        RemisionesDetPK other = (RemisionesDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroRemision != other.nroRemision) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RemisionesDetPK[ codEmpr=" + codEmpr + ", nroRemision=" + nroRemision + ", codMerca=" + codMerca + " ]";
    }
    
}
