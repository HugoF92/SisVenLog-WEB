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
public class NotasVentasDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_nota")
    private long nroNota;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fdocum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fdocum;

    public NotasVentasDetPK() {
    }

    public NotasVentasDetPK(short codEmpr, String ctipoDocum, long nroNota, String codMerca, Date fdocum) {
        this.codEmpr = codEmpr;
        this.ctipoDocum = ctipoDocum;
        this.nroNota = nroNota;
        this.codMerca = codMerca;
        this.fdocum = fdocum;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public long getNroNota() {
        return nroNota;
    }

    public void setNroNota(long nroNota) {
        this.nroNota = nroNota;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public Date getFdocum() {
        return fdocum;
    }

    public void setFdocum(Date fdocum) {
        this.fdocum = fdocum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (int) nroNota;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (fdocum != null ? fdocum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotasVentasDetPK)) {
            return false;
        }
        NotasVentasDetPK other = (NotasVentasDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if (this.nroNota != other.nroNota) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if ((this.fdocum == null && other.fdocum != null) || (this.fdocum != null && !this.fdocum.equals(other.fdocum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.NotasVentasDetPK[ codEmpr=" + codEmpr + ", ctipoDocum=" + ctipoDocum + ", nroNota=" + nroNota + ", codMerca=" + codMerca + ", fdocum=" + fdocum + " ]";
    }
    
}
