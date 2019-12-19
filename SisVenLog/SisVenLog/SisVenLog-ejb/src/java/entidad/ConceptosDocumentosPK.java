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
public class ConceptosDocumentosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cconc")
    private String cconc;

    public ConceptosDocumentosPK() {
    }

    public ConceptosDocumentosPK(String ctipoDocum, String cconc) {
        this.ctipoDocum = ctipoDocum;
        this.cconc = cconc;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public String getCconc() {
        return cconc;
    }

    public void setCconc(String cconc) {
        this.cconc = cconc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (cconc != null ? cconc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConceptosDocumentosPK)) {
            return false;
        }
        ConceptosDocumentosPK other = (ConceptosDocumentosPK) object;
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if ((this.cconc == null && other.cconc != null) || (this.cconc != null && !this.cconc.equals(other.cconc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ConceptosDocumentosPK[ ctipoDocum=" + ctipoDocum + ", cconc=" + cconc + " ]";
    }
    
}
