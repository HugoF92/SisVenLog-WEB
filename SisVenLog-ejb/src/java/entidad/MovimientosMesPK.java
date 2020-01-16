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
public class MovimientosMesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_depo")
    private short codDepo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nmes")
    private short nmes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nanno")
    private short nanno;

    public MovimientosMesPK() {
    }

    public MovimientosMesPK(short codEmpr, short codDepo, String codMerca, String ctipoDocum, short nmes, short nanno) {
        this.codEmpr = codEmpr;
        this.codDepo = codDepo;
        this.codMerca = codMerca;
        this.ctipoDocum = ctipoDocum;
        this.nmes = nmes;
        this.nanno = nanno;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(short codDepo) {
        this.codDepo = codDepo;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public short getNmes() {
        return nmes;
    }

    public void setNmes(short nmes) {
        this.nmes = nmes;
    }

    public short getNanno() {
        return nanno;
    }

    public void setNanno(short nanno) {
        this.nanno = nanno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codDepo;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (int) nmes;
        hash += (int) nanno;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientosMesPK)) {
            return false;
        }
        MovimientosMesPK other = (MovimientosMesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codDepo != other.codDepo) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if (this.nmes != other.nmes) {
            return false;
        }
        if (this.nanno != other.nanno) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MovimientosMesPK[ codEmpr=" + codEmpr + ", codDepo=" + codDepo + ", codMerca=" + codMerca + ", ctipoDocum=" + ctipoDocum + ", nmes=" + nmes + ", nanno=" + nanno + " ]";
    }
    
}
