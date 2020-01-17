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
public class DepositosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_depo")
    private short codDepo;

    public DepositosPK() {
    }

    public DepositosPK(short codEmpr, short codDepo) {
        this.codEmpr = codEmpr;
        this.codDepo = codDepo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codDepo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepositosPK)) {
            return false;
        }
        DepositosPK other = (DepositosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codDepo != other.codDepo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DepositosPK[ codEmpr=" + codEmpr + ", codDepo=" + codDepo + " ]";
    }
    
}
