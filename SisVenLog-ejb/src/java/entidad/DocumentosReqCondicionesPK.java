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
 * @author lmore
 */
@Embeddable
public class DocumentosReqCondicionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "mtipo_persona")
    private Character mtipoPersona;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cdocum")
    private String cdocum;

    public DocumentosReqCondicionesPK() {
    }

    public DocumentosReqCondicionesPK(Character mtipoPersona, String cdocum) {
        this.mtipoPersona = mtipoPersona;
        this.cdocum = cdocum;
    }

    public Character getMtipoPersona() {
        return mtipoPersona;
    }

    public void setMtipoPersona(Character mtipoPersona) {
        this.mtipoPersona = mtipoPersona;
    }

    public String getCdocum() {
        return cdocum;
    }

    public void setCdocum(String cdocum) {
        this.cdocum = cdocum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mtipoPersona != null ? mtipoPersona.hashCode() : 0);
        hash += (cdocum != null ? cdocum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosReqCondicionesPK)) {
            return false;
        }
        DocumentosReqCondicionesPK other = (DocumentosReqCondicionesPK) object;
        if ((this.mtipoPersona == null && other.mtipoPersona != null) || (this.mtipoPersona != null && !this.mtipoPersona.equals(other.mtipoPersona))) {
            return false;
        }
        if ((this.cdocum == null && other.cdocum != null) || (this.cdocum != null && !this.cdocum.equals(other.cdocum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DocumentosReqCondicionesPK[ mtipoPersona=" + mtipoPersona + ", cdocum=" + cdocum + " ]";
    }
    
}
