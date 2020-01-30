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
public class RecibosProvChequesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrecibo")
    private long nrecibo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private short codProveed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_banco")
    private short codBanco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "nro_cheque")
    private String nroCheque;

    public RecibosProvChequesPK() {
    }

    public RecibosProvChequesPK(short codEmpr, long nrecibo, short codProveed, short codBanco, String nroCheque) {
        this.codEmpr = codEmpr;
        this.nrecibo = nrecibo;
        this.codProveed = codProveed;
        this.codBanco = codBanco;
        this.nroCheque = nroCheque;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNrecibo() {
        return nrecibo;
    }

    public void setNrecibo(long nrecibo) {
        this.nrecibo = nrecibo;
    }

    public short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(short codProveed) {
        this.codProveed = codProveed;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nrecibo;
        hash += (int) codProveed;
        hash += (int) codBanco;
        hash += (nroCheque != null ? nroCheque.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosProvChequesPK)) {
            return false;
        }
        RecibosProvChequesPK other = (RecibosProvChequesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nrecibo != other.nrecibo) {
            return false;
        }
        if (this.codProveed != other.codProveed) {
            return false;
        }
        if (this.codBanco != other.codBanco) {
            return false;
        }
        if ((this.nroCheque == null && other.nroCheque != null) || (this.nroCheque != null && !this.nroCheque.equals(other.nroCheque))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosProvChequesPK[ codEmpr=" + codEmpr + ", nrecibo=" + nrecibo + ", codProveed=" + codProveed + ", codBanco=" + codBanco + ", nroCheque=" + nroCheque + " ]";
    }
    
}
