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
public class ClientesAgrupadosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_grupo")
    private short codGrupo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente")
    private int codCliente;

    public ClientesAgrupadosPK() {
    }

    public ClientesAgrupadosPK(short codGrupo, int codCliente) {
        this.codGrupo = codGrupo;
        this.codCliente = codCliente;
    }

    public short getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(short codGrupo) {
        this.codGrupo = codGrupo;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codGrupo;
        hash += (int) codCliente;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientesAgrupadosPK)) {
            return false;
        }
        ClientesAgrupadosPK other = (ClientesAgrupadosPK) object;
        if (this.codGrupo != other.codGrupo) {
            return false;
        }
        if (this.codCliente != other.codCliente) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ClientesAgrupadosPK[ codGrupo=" + codGrupo + ", codCliente=" + codCliente + " ]";
    }
    
}
