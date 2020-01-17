/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class PppPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fppp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fppp;

    public PppPK() {
    }

    public PppPK(short codEmpr, String codMerca, Date fppp) {
        this.codEmpr = codEmpr;
        this.codMerca = codMerca;
        this.fppp = fppp;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public Date getFppp() {
        return fppp;
    }

    public void setFppp(Date fppp) {
        this.fppp = fppp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (fppp != null ? fppp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PppPK)) {
            return false;
        }
        PppPK other = (PppPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if ((this.fppp == null && other.fppp != null) || (this.fppp != null && !this.fppp.equals(other.fppp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PppPK[ codEmpr=" + codEmpr + ", codMerca=" + codMerca + ", fppp=" + fppp + " ]";
    }
    
}
