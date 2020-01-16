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
 * @author Edu
 */
@Embeddable
public class ChequesEmitidosDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_banco")
    private short codBanco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "nro_cheque")
    private String nroCheque;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofact")
    private long nrofact;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;

    public ChequesEmitidosDetPK() {
    }

    public ChequesEmitidosDetPK(short codEmpr, short codBanco, String nroCheque, long nrofact, String ctipoDocum) {
        this.codEmpr = codEmpr;
        this.codBanco = codBanco;
        this.nroCheque = nroCheque;
        this.nrofact = nrofact;
        this.ctipoDocum = ctipoDocum;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(short codBanco) {
        this.codBanco = codBanco;
    }

    public String getNroCheque() {
        return nroCheque;
    }

    public void setNroCheque(String nroCheque) {
        this.nroCheque = nroCheque;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codBanco;
        hash += (nroCheque != null ? nroCheque.hashCode() : 0);
        hash += (int) nrofact;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChequesEmitidosDetPK)) {
            return false;
        }
        ChequesEmitidosDetPK other = (ChequesEmitidosDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codBanco != other.codBanco) {
            return false;
        }
        if ((this.nroCheque == null && other.nroCheque != null) || (this.nroCheque != null && !this.nroCheque.equals(other.nroCheque))) {
            return false;
        }
        if (this.nrofact != other.nrofact) {
            return false;
        }
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ChequesEmitidosDetPK[ codEmpr=" + codEmpr + ", codBanco=" + codBanco + ", nroCheque=" + nroCheque + ", nrofact=" + nrofact + ", ctipoDocum=" + ctipoDocum + " ]";
    }
    
}
