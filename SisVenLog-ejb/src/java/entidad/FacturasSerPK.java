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
public class FacturasSerPK implements Serializable {

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
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_servicio")
    private short codServicio;

    public FacturasSerPK() {
    }

    public FacturasSerPK(short codEmpr, long nrofact, String ctipoDocum, Date ffactur, short codServicio) {
        this.codEmpr = codEmpr;
        this.nrofact = nrofact;
        this.ctipoDocum = ctipoDocum;
        this.ffactur = ffactur;
        this.codServicio = codServicio;
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

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public short getCodServicio() {
        return codServicio;
    }

    public void setCodServicio(short codServicio) {
        this.codServicio = codServicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nrofact;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (ffactur != null ? ffactur.hashCode() : 0);
        hash += (int) codServicio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturasSerPK)) {
            return false;
        }
        FacturasSerPK other = (FacturasSerPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nrofact != other.nrofact) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if ((this.ffactur == null && other.ffactur != null) || (this.ffactur != null && !this.ffactur.equals(other.ffactur))) {
            return false;
        }
        if (this.codServicio != other.codServicio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.FacturasSerPK[ codEmpr=" + codEmpr + ", nrofact=" + nrofact + ", ctipoDocum=" + ctipoDocum + ", ffactur=" + ffactur + ", codServicio=" + codServicio + " ]";
    }
    
}
