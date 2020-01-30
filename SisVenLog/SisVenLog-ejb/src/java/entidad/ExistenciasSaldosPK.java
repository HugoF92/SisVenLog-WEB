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
public class ExistenciasSaldosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_depo")
    private short codDepo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fmovim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fmovim;

    public ExistenciasSaldosPK() {
    }

    public ExistenciasSaldosPK(short codEmpr, short codDepo, String codMerca, Date fmovim) {
        this.codEmpr = codEmpr;
        this.codDepo = codDepo;
        this.codMerca = codMerca;
        this.fmovim = fmovim;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(short codDepo) {
        this.codDepo = codDepo;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
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
        hash += (int) codDepo;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (fmovim != null ? fmovim.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExistenciasSaldosPK)) {
            return false;
        }
        ExistenciasSaldosPK other = (ExistenciasSaldosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codDepo != other.codDepo) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if ((this.fmovim == null && other.fmovim != null) || (this.fmovim != null && !this.fmovim.equals(other.fmovim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ExistenciasSaldosPK[ codEmpr=" + codEmpr + ", codDepo=" + codDepo + ", codMerca=" + codMerca + ", fmovim=" + fmovim + " ]";
    }
    
}
