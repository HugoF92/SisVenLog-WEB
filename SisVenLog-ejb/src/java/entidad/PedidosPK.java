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
public class PedidosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_pedido")
    private long nroPedido;

    public PedidosPK() {
    }

    public PedidosPK(short codEmpr, long nroPedido) {
        this.codEmpr = codEmpr;
        this.nroPedido = nroPedido;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroPedido;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidosPK)) {
            return false;
        }
        PedidosPK other = (PedidosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroPedido != other.nroPedido) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PedidosPK[ codEmpr=" + codEmpr + ", nroPedido=" + nroPedido + " ]";
    }
    
}
