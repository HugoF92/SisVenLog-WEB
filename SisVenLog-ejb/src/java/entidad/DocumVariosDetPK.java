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
public class DocumVariosDetPK implements Serializable {

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
    @Column(name = "ndocum")
    private int ndocum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;

    public DocumVariosDetPK() {
    }

    public DocumVariosDetPK(short codEmpr, String ctipoDocum, int ndocum, String codMerca) {
        this.codEmpr = codEmpr;
        this.ctipoDocum = ctipoDocum;
        this.ndocum = ndocum;
        this.codMerca = codMerca;
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

    public int getNdocum() {
        return ndocum;
    }

    public void setNdocum(int ndocum) {
        this.ndocum = ndocum;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (int) ndocum;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumVariosDetPK)) {
            return false;
        }
        DocumVariosDetPK other = (DocumVariosDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if (this.ndocum != other.ndocum) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DocumVariosDetPK[ codEmpr=" + codEmpr + ", ctipoDocum=" + ctipoDocum + ", ndocum=" + ndocum + ", codMerca=" + codMerca + " ]";
    }
    
}
