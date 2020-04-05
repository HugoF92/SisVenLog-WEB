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
public class RangosDocumentosPK implements Serializable {

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
    @Column(name = "nro_docum_ini")
    private long nroDocumIni;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nano_inicial")
    private short nanoInicial;

    public RangosDocumentosPK() {
    }

    public RangosDocumentosPK(short codEmpr, String ctipoDocum, long nroDocumIni, short nanoInicial) {
        this.codEmpr = codEmpr;
        this.ctipoDocum = ctipoDocum;
        this.nroDocumIni = nroDocumIni;
        this.nanoInicial = nanoInicial;
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

    public long getNroDocumIni() {
        return nroDocumIni;
    }

    public void setNroDocumIni(long nroDocumIni) {
        this.nroDocumIni = nroDocumIni;
    }

    public short getNanoInicial() {
        return nanoInicial;
    }

    public void setNanoInicial(short nanoInicial) {
        this.nanoInicial = nanoInicial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        hash += (int) nroDocumIni;
        hash += (int) nanoInicial;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RangosDocumentosPK)) {
            return false;
        }
        RangosDocumentosPK other = (RangosDocumentosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        if (this.nroDocumIni != other.nroDocumIni) {
            return false;
        }
        if (this.nanoInicial != other.nanoInicial) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RangosDocumentosPK[ codEmpr=" + codEmpr + ", ctipoDocum=" + ctipoDocum + ", nroDocumIni=" + nroDocumIni + ", nanoInicial=" + nanoInicial + " ]";
    }
    
}
