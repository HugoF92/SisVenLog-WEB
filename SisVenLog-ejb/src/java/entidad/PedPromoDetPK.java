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
public class PedPromoDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_pedido")
    private long nroPedido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_promo")
    private short nroPromo;

    public PedPromoDetPK() {
    }

    public PedPromoDetPK(short codEmpr, long nroPedido, String codMerca, short nroPromo) {
        this.codEmpr = codEmpr;
        this.nroPedido = nroPedido;
        this.codMerca = codMerca;
        this.nroPromo = nroPromo;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNroPedido() {
        return nroPedido;
    }

    public void setNroPedido(long nroPedido) {
        this.nroPedido = nroPedido;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public short getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(short nroPromo) {
        this.nroPromo = nroPromo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroPedido;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (int) nroPromo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedPromoDetPK)) {
            return false;
        }
        PedPromoDetPK other = (PedPromoDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroPedido != other.nroPedido) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if (this.nroPromo != other.nroPromo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PedPromoDetPK[ codEmpr=" + codEmpr + ", nroPedido=" + nroPedido + ", codMerca=" + codMerca + ", nroPromo=" + nroPromo + " ]";
    }
    
}
