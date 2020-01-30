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
public class AplicacionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "cod_aplicacion")
    private String codAplicacion;

    public AplicacionesPK() {
    }

    public AplicacionesPK(short codEmpr, String codAplicacion) {
        this.codEmpr = codEmpr;
        this.codAplicacion = codAplicacion;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
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
        hash += (codAplicacion != null ? codAplicacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AplicacionesPK)) {
            return false;
        }
        AplicacionesPK other = (AplicacionesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.codAplicacion == null && other.codAplicacion != null) || (this.codAplicacion != null && !this.codAplicacion.equals(other.codAplicacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.AplicacionesPK[ codEmpr=" + codEmpr + ", codAplicacion=" + codAplicacion + " ]";
    }
    
}
