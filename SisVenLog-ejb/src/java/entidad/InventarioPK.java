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
public class InventarioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_inven")
    private int nroInven;

    public InventarioPK() {
    }

    public InventarioPK(short codEmpr, int nroInven) {
        this.codEmpr = codEmpr;
        this.nroInven = nroInven;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public int getNroInven() {
        return nroInven;
    }

    public void setNroInven(int nroInven) {
        this.nroInven = nroInven;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroInven;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventarioPK)) {
            return false;
        }
        InventarioPK other = (InventarioPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroInven != other.nroInven) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.InventarioPK[ codEmpr=" + codEmpr + ", nroInven=" + nroInven + " ]";
    }
    
}
