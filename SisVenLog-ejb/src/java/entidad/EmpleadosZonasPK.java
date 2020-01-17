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
public class EmpleadosZonasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_zona")
    private String codZona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empleado")
    private short codEmpleado;

    public EmpleadosZonasPK() {
    }

    public EmpleadosZonasPK(short codEmpr, String codZona, short codEmpleado) {
        this.codEmpr = codEmpr;
        this.codZona = codZona;
        this.codEmpleado = codEmpleado;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
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
        hash += (codZona != null ? codZona.hashCode() : 0);
        hash += (int) codEmpleado;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadosZonasPK)) {
            return false;
        }
        EmpleadosZonasPK other = (EmpleadosZonasPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.codZona == null && other.codZona != null) || (this.codZona != null && !this.codZona.equals(other.codZona))) {
            return false;
        }
        if (this.codEmpleado != other.codEmpleado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EmpleadosZonasPK[ codEmpr=" + codEmpr + ", codZona=" + codZona + ", codEmpleado=" + codEmpleado + " ]";
    }
    
}
