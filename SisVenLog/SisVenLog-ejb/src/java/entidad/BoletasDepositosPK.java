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
public class BoletasDepositosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_boleta")
    private long nroBoleta;

    public BoletasDepositosPK() {
    }

    public BoletasDepositosPK(short codEmpr, long nroBoleta) {
        this.codEmpr = codEmpr;
        this.nroBoleta = nroBoleta;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNroBoleta() {
        return nroBoleta;
    }

    public void setNroBoleta(long nroBoleta) {
        this.nroBoleta = nroBoleta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroBoleta;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoletasDepositosPK)) {
            return false;
        }
        BoletasDepositosPK other = (BoletasDepositosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroBoleta != other.nroBoleta) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.BoletasDepositosPK[ codEmpr=" + codEmpr + ", nroBoleta=" + nroBoleta + " ]";
    }
    
}
