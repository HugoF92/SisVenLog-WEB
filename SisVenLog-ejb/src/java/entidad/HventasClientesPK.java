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
public class HventasClientesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente")
    private int codCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctipo_vta")
    private Character ctipoVta;

    public HventasClientesPK() {
    }

    public HventasClientesPK(int codCliente, Character ctipoVta) {
        this.codCliente = codCliente;
        this.ctipoVta = ctipoVta;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public Character getCtipoVta() {
        return ctipoVta;
    }

    public void setCtipoVta(Character ctipoVta) {
        this.ctipoVta = ctipoVta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codCliente;
        hash += (ctipoVta != null ? ctipoVta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HventasClientesPK)) {
            return false;
        }
        HventasClientesPK other = (HventasClientesPK) object;
        if (this.codCliente != other.codCliente) {
            return false;
        }
        if ((this.ctipoVta == null && other.ctipoVta != null) || (this.ctipoVta != null && !this.ctipoVta.equals(other.ctipoVta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.HventasClientesPK[ codCliente=" + codCliente + ", ctipoVta=" + ctipoVta + " ]";
    }
    
}
