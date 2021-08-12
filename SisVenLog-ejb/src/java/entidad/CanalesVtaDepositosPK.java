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
 * @author dadob
 */
@Embeddable
public class CanalesVtaDepositosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_canal")
    private String codCanal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_depo")
    private short codDepo;

    public CanalesVtaDepositosPK() {
    }

    public CanalesVtaDepositosPK(String codCanal, short codDepo) {
        this.codCanal = codCanal;
        this.codDepo = codDepo;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(short codDepo) {
        this.codDepo = codDepo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCanal != null ? codCanal.hashCode() : 0);
        hash += (int) codDepo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CanalesVtaDepositosPK)) {
            return false;
        }
        CanalesVtaDepositosPK other = (CanalesVtaDepositosPK) object;
        if ((this.codCanal == null && other.codCanal != null) || (this.codCanal != null && !this.codCanal.equals(other.codCanal))) {
            return false;
        }
        if (this.codDepo != other.codDepo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CanalesVtaDepositosPK[ codCanal=" + codCanal + ", codDepo=" + codDepo + " ]";
    }
    
}
