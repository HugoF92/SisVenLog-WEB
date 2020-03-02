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

/**
 *
 * @author admin
 */
@Embeddable
public class SerieFacturasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ffinal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ndocum_final")
    private long ndocumFinal;

    public SerieFacturasPK() {
    }

    public SerieFacturasPK(Date ffinal, long ndocumFinal) {
        this.ffinal = ffinal;
        this.ndocumFinal = ndocumFinal;
    }

    public Date getFfinal() {
        return ffinal;
    }

    public void setFfinal(Date ffinal) {
        this.ffinal = ffinal;
    }

    public long getNdocumFinal() {
        return ndocumFinal;
    }

    public void setNdocumFinal(long ndocumFinal) {
        this.ndocumFinal = ndocumFinal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ffinal != null ? ffinal.hashCode() : 0);
        hash += (int) ndocumFinal;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SerieFacturasPK)) {
            return false;
        }
        SerieFacturasPK other = (SerieFacturasPK) object;
        if ((this.ffinal == null && other.ffinal != null) || (this.ffinal != null && !this.ffinal.equals(other.ffinal))) {
            return false;
        }
        if (this.ndocumFinal != other.ndocumFinal) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.SerieFacturasPK[ ffinal=" + ffinal + ", ndocumFinal=" + ndocumFinal + " ]";
    }
    
}
