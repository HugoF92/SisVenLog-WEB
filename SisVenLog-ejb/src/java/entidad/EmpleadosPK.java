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
public class EmpleadosPK implements Serializable {

    @Basic(optional = false)
   //@NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
   //@NotNull
    @Column(name = "cod_empleado")
    private short codEmpleado;

    public EmpleadosPK() {
    }

    public EmpleadosPK(short codEmpr, short codEmpleado) {
        this.codEmpr = codEmpr;
        this.codEmpleado = codEmpleado;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(short codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codEmpleado;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadosPK)) {
            return false;
        }
        EmpleadosPK other = (EmpleadosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codEmpleado != other.codEmpleado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EmpleadosPK[ codEmpr=" + codEmpr + ", codEmpleado=" + codEmpleado + " ]";
    }
    
}
