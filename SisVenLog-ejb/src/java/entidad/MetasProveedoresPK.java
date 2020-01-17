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
public class MetasProveedoresPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private short codProveed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_linea")
    private short codLinea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nanno")
    private short nanno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nmes")
    private short nmes;

    public MetasProveedoresPK() {
    }

    public MetasProveedoresPK(short codEmpr, short codProveed, short codLinea, short nanno, short nmes) {
        this.codEmpr = codEmpr;
        this.codProveed = codProveed;
        this.codLinea = codLinea;
        this.nanno = nanno;
        this.nmes = nmes;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(short codProveed) {
        this.codProveed = codProveed;
    }

    public short getCodLinea() {
        return codLinea;
    }

    public void setCodLinea(short codLinea) {
        this.codLinea = codLinea;
    }

    public short getNanno() {
        return nanno;
    }

    public void setNanno(short nanno) {
        this.nanno = nanno;
    }

    public short getNmes() {
        return nmes;
    }

    public void setNmes(short nmes) {
        this.nmes = nmes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codProveed;
        hash += (int) codLinea;
        hash += (int) nanno;
        hash += (int) nmes;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetasProveedoresPK)) {
            return false;
        }
        MetasProveedoresPK other = (MetasProveedoresPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codProveed != other.codProveed) {
            return false;
        }
        if (this.codLinea != other.codLinea) {
            return false;
        }
        if (this.nanno != other.nanno) {
            return false;
        }
        if (this.nmes != other.nmes) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MetasProveedoresPK[ codEmpr=" + codEmpr + ", codProveed=" + codProveed + ", codLinea=" + codLinea + ", nanno=" + nanno + ", nmes=" + nmes + " ]";
    }
    
}
