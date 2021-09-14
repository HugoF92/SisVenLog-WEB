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
 * @author lmore
 */
@Embeddable
public class ClientesDocumentosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente")
    private long codCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nsecuencia")
    private short nsecuencia;

    public ClientesDocumentosPK() {
    }

    public ClientesDocumentosPK(long codCliente, short nsecuencia) {
        this.codCliente = codCliente;
        this.nsecuencia = nsecuencia;
    }

    public long getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(long codCliente) {
        this.codCliente = codCliente;
    }

    public short getNsecuencia() {
        return nsecuencia;
    }

    public void setNsecuencia(short nsecuencia) {
        this.nsecuencia = nsecuencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codCliente;
        hash += (int) nsecuencia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientesDocumentosPK)) {
            return false;
        }
        ClientesDocumentosPK other = (ClientesDocumentosPK) object;
        if (this.codCliente != other.codCliente) {
            return false;
        }
        if (this.nsecuencia != other.nsecuencia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ClientesDocumentosPK[ codCliente=" + codCliente + ", nsecuencia=" + nsecuencia + " ]";
    }
    
}
