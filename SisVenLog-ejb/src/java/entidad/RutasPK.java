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
public class RutasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_ruta")
    private short codRuta;

    public RutasPK() {
    }

    public RutasPK(short codEmpr, short codRuta) {
        this.codEmpr = codEmpr;
        this.codRuta = codRuta;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(short codRuta) {
        this.codRuta = codRuta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codRuta;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RutasPK)) {
            return false;
        }
        RutasPK other = (RutasPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codRuta != other.codRuta) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RutasPK[ codEmpr=" + codEmpr + ", codRuta=" + codRuta + " ]";
    }
    
}
