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
public class ClientesCtasBancariasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente")
    private long codCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_banco")
    private int codBanco;

    public ClientesCtasBancariasPK() {
    }

    public ClientesCtasBancariasPK(long codCliente, int codBanco) {
        this.codCliente = codCliente;
        this.codBanco = codBanco;
    }

    public long getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(long codCliente) {
        this.codCliente = codCliente;
    }

    public int getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(int codBanco) {
        this.codBanco = codBanco;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codCliente;
        hash += (int) codBanco;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientesCtasBancariasPK)) {
            return false;
        }
        ClientesCtasBancariasPK other = (ClientesCtasBancariasPK) object;
        if (this.codCliente != other.codCliente) {
            return false;
        }
        if (this.codBanco != other.codBanco) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dao.ClientesCtasBancariasPK[ codCliente=" + codCliente + ", codBanco=" + codBanco + " ]";
    }
    
}
