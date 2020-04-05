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
public class MercaImpuestosPK implements Serializable {

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
    @Column(name = "cod_impu")
    private short codImpu;

    public MercaImpuestosPK() {
    }

    public MercaImpuestosPK(short codEmpr, String codMerca, short codImpu) {
        this.codEmpr = codEmpr;
        this.codMerca = codMerca;
        this.codImpu = codImpu;
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

    public short getCodImpu() {
        return codImpu;
    }

    public void setCodImpu(short codImpu) {
        this.codImpu = codImpu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (int) codImpu;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MercaImpuestosPK)) {
            return false;
        }
        MercaImpuestosPK other = (MercaImpuestosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if (this.codImpu != other.codImpu) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MercaImpuestosPK[ codEmpr=" + codEmpr + ", codMerca=" + codMerca + ", codImpu=" + codImpu + " ]";
    }
    
}
