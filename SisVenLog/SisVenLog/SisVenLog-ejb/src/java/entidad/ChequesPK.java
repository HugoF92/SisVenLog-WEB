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
public class ChequesPK implements Serializable {

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
    @Size(min = 1, max = 20)
    @Column(name = "xcuenta_bco")
    private String xcuentaBco;

    public ChequesPK() {
    }

    public ChequesPK(short codEmpr, short codBanco, String nroCheque, String xcuentaBco) {
        this.codEmpr = codEmpr;
        this.codBanco = codBanco;
        this.nroCheque = nroCheque;
        this.xcuentaBco = xcuentaBco;
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

    public String getXcuentaBco() {
        return xcuentaBco;
    }

    public void setXcuentaBco(String xcuentaBco) {
        this.xcuentaBco = xcuentaBco;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codBanco;
        hash += (nroCheque != null ? nroCheque.hashCode() : 0);
        hash += (xcuentaBco != null ? xcuentaBco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChequesPK)) {
            return false;
        }
        ChequesPK other = (ChequesPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codBanco != other.codBanco) {
            return false;
        }
        if ((this.nroCheque == null && other.nroCheque != null) || (this.nroCheque != null && !this.nroCheque.equals(other.nroCheque))) {
            return false;
        }
        if ((this.xcuentaBco == null && other.xcuentaBco != null) || (this.xcuentaBco != null && !this.xcuentaBco.equals(other.xcuentaBco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ChequesPK[ codEmpr=" + codEmpr + ", codBanco=" + codBanco + ", nroCheque=" + nroCheque + ", xcuentaBco=" + xcuentaBco + " ]";
    }
    
}
