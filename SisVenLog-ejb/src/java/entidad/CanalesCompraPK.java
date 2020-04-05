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
 * @author Hugo
 */
@Embeddable
public class CanalesCompraPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private short codProveed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ccanal_compra")
    private String ccanalCompra;

    public CanalesCompraPK() {
    }

    public CanalesCompraPK(short codProveed, String ccanalCompra) {
        this.codProveed = codProveed;
        this.ccanalCompra = ccanalCompra;
    }

    public short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(short codProveed) {
        this.codProveed = codProveed;
    }

    public String getCcanalCompra() {
        return ccanalCompra;
    }

    public void setCcanalCompra(String ccanalCompra) {
        this.ccanalCompra = ccanalCompra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codProveed;
        hash += (ccanalCompra != null ? ccanalCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CanalesCompraPK)) {
            return false;
        }
        CanalesCompraPK other = (CanalesCompraPK) object;
        if (this.codProveed != other.codProveed) {
            return false;
        }
        if ((this.ccanalCompra == null && other.ccanalCompra != null) || (this.ccanalCompra != null && !this.ccanalCompra.equals(other.ccanalCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CanalesCompraPK[ codProveed=" + codProveed + ", ccanalCompra=" + ccanalCompra + " ]";
    }
    
}
