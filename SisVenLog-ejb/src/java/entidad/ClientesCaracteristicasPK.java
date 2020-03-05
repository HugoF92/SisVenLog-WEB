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
public class ClientesCaracteristicasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente")
    private int codCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_caract")
    private short codCaract;

    public ClientesCaracteristicasPK() {
    }

    public ClientesCaracteristicasPK(int codCliente, short codCaract) {
        this.codCliente = codCliente;
        this.codCaract = codCaract;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public short getCodCaract() {
        return codCaract;
    }

    public void setCodCaract(short codCaract) {
        this.codCaract = codCaract;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codCliente;
        hash += (int) codCaract;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientesCaracteristicasPK)) {
            return false;
        }
        ClientesCaracteristicasPK other = (ClientesCaracteristicasPK) object;
        if (this.codCliente != other.codCliente) {
            return false;
        }
        if (this.codCaract != other.codCaract) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ClientesCaracteristicasPK[ codCliente=" + codCliente + ", codCaract=" + codCaract + " ]";
    }
    
}
