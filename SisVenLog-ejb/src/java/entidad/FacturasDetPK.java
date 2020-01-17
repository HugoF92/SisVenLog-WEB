/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class FacturasDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofact")
    private long nrofact;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;

    public FacturasDetPK() {
    }

    public FacturasDetPK(short codEmpr, long nrofact, String ctipoDocum, String codMerca, Date ffactur) {
        this.codEmpr = codEmpr;
        this.nrofact = nrofact;
        this.ctipoDocum = ctipoDocum;
        this.codMerca = codMerca;
        this.ffactur = ffactur;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNrofact() {
        return nrofact;
    }

    public void setNrofact(long nrofact) {
        this.nrofact = nrofact;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nrofact;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (ffactur != null ? ffactur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturasDetPK)) {
            return false;
        }
        FacturasDetPK other = (FacturasDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nrofact != other.nrofact) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if ((this.ffactur == null && other.ffactur != null) || (this.ffactur != null && !this.ffactur.equals(other.ffactur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.FacturasDetPK[ codEmpr=" + codEmpr + ", nrofact=" + nrofact + ", ctipoDocum=" + ctipoDocum + ", codMerca=" + codMerca + ", ffactur=" + ffactur + " ]";
    }
    
}
