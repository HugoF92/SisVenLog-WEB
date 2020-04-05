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
public class SaldosClientesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente")
    private int codCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fmovim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fmovim;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;

    public SaldosClientesPK() {
    }

    public SaldosClientesPK(int codCliente, Date fmovim, short codEmpr) {
        this.codCliente = codCliente;
        this.fmovim = fmovim;
        this.codEmpr = codEmpr;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public Date getFmovim() {
        return fmovim;
    }

    public void setFmovim(Date fmovim) {
        this.fmovim = fmovim;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codCliente;
        hash += (fmovim != null ? fmovim.hashCode() : 0);
        hash += (int) codEmpr;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaldosClientesPK)) {
            return false;
        }
        SaldosClientesPK other = (SaldosClientesPK) object;
        if (this.codCliente != other.codCliente) {
            return false;
        }
        if ((this.fmovim == null && other.fmovim != null) || (this.fmovim != null && !this.fmovim.equals(other.fmovim))) {
            return false;
        }
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.SaldosClientesPK[ codCliente=" + codCliente + ", fmovim=" + fmovim + ", codEmpr=" + codEmpr + " ]";
    }
    
}
