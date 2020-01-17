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
public class UsuariosPK implements Serializable {

    @Basic(optional = false)
   //@NotNull
   //@Size(min = 1, max = 30)
    @Column(name = "cod_usuario")
    private String codUsuario;
    @Basic(optional = false)
   //@NotNull
    @Column(name = "cod_empr")
    private short codEmpr;

    public UsuariosPK() {
    }

    public UsuariosPK(String codUsuario, short codEmpr) {
        this.codUsuario = codUsuario;
        this.codEmpr = codEmpr;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codUsuario != null ? codUsuario.hashCode() : 0);
        hash += (int) codEmpr;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosPK)) {
            return false;
        }
        UsuariosPK other = (UsuariosPK) object;
        if ((this.codUsuario == null && other.codUsuario != null) || (this.codUsuario != null && !this.codUsuario.equals(other.codUsuario))) {
            return false;
        }
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.UsuariosPK[ codUsuario=" + codUsuario + ", codEmpr=" + codEmpr + " ]";
    }
    
}
