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
public class RolesAplicacionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_rol")
    private int codRol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "cod_aplicacion")
    private String codAplicacion;

    public RolesAplicacionesPK() {
    }

    public RolesAplicacionesPK(short codEmpr, int codRol, String codAplicacion) {
        this.codEmpr = codEmpr;
        this.codRol = codRol;
        this.codAplicacion = codAplicacion;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public int getCodRol() {
        return codRol;
    }

    public void setCodRol(int codRol) {
        this.codRol = codRol;
    }

    public String getCodAplicacion() {
        return codAplicacion;
    }

    public void setCodAplicacion(String codAplicacion) {
        this.codAplicacion = codAplicacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codRol;
        hash += (codAplicacion != null ? codAplicacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolesAplicacionesPK)) {
            return false;
        }
        RolesAplicacionesPK other = (RolesAplicacionesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codRol != other.codRol) {
            return false;
        }
        if ((this.codAplicacion == null && other.codAplicacion != null) || (this.codAplicacion != null && !this.codAplicacion.equals(other.codAplicacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RolesAplicacionesPK[ codEmpr=" + codEmpr + ", codRol=" + codRol + ", codAplicacion=" + codAplicacion + " ]";
    }
    
}
