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
 * @author Hugo
 */
@Embeddable
public class RolesPK implements Serializable {

    @Basic(optional = false)
   //@NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
   //@NotNull
    @Column(name = "cod_rol")
    private Integer codRol;

    public RolesPK() {
    }

    public RolesPK(short codEmpr, Integer codRol) {
        this.codEmpr = codEmpr;
        this.codRol = codRol;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public Integer getCodRol() {
        return codRol;
    }

    public void setCodRol(Integer codRol) {
        this.codRol = codRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codRol;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolesPK)) {
            return false;
        }
        RolesPK other = (RolesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codRol != other.codRol) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RolesPK[ codEmpr=" + codEmpr + ", codRol=" + codRol + " ]";
    }
    
}
