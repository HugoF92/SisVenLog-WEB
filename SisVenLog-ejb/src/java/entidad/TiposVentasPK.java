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
public class TiposVentasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctipo_vta")
    private Character ctipoVta;

    public TiposVentasPK() {
    }

    public TiposVentasPK(short codEmpr, Character ctipoVta) {
        this.codEmpr = codEmpr;
        this.ctipoVta = ctipoVta;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
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
        hash += (int) codEmpr;
        hash += (ctipoVta != null ? ctipoVta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposVentasPK)) {
            return false;
        }
        TiposVentasPK other = (TiposVentasPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.ctipoVta == null && other.ctipoVta != null) || (this.ctipoVta != null && !this.ctipoVta.equals(other.ctipoVta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.TiposVentasPK[ codEmpr=" + codEmpr + ", ctipoVta=" + ctipoVta + " ]";
    }
    
}
