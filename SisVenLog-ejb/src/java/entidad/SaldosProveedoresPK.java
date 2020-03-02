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

/**
 *
 * @author admin
 */
@Embeddable
public class SaldosProveedoresPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_EMPR")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private short codProveed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fmovim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fmovim;

    public SaldosProveedoresPK() {
    }

    public SaldosProveedoresPK(short codEmpr, short codProveed, Date fmovim) {
        this.codEmpr = codEmpr;
        this.codProveed = codProveed;
        this.fmovim = fmovim;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(short codProveed) {
        this.codProveed = codProveed;
    }

    public Date getFmovim() {
        return fmovim;
    }

    public void setFmovim(Date fmovim) {
        this.fmovim = fmovim;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codProveed;
        hash += (fmovim != null ? fmovim.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaldosProveedoresPK)) {
            return false;
        }
        SaldosProveedoresPK other = (SaldosProveedoresPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codProveed != other.codProveed) {
            return false;
        }
        if ((this.fmovim == null && other.fmovim != null) || (this.fmovim != null && !this.fmovim.equals(other.fmovim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.SaldosProveedoresPK[ codEmpr=" + codEmpr + ", codProveed=" + codProveed + ", fmovim=" + fmovim + " ]";
    }
    
}
