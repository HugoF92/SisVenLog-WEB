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
public class PagosDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_pago")
    private short nroPago;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_instr")
    private String codInstr;

    public PagosDetPK() {
    }

    public PagosDetPK(short codEmpr, short nroPago, String codInstr) {
        this.codEmpr = codEmpr;
        this.nroPago = nroPago;
        this.codInstr = codInstr;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getNroPago() {
        return nroPago;
    }

    public void setNroPago(short nroPago) {
        this.nroPago = nroPago;
    }

    public String getCodInstr() {
        return codInstr;
    }

    public void setCodInstr(String codInstr) {
        this.codInstr = codInstr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroPago;
        hash += (codInstr != null ? codInstr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagosDetPK)) {
            return false;
        }
        PagosDetPK other = (PagosDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroPago != other.nroPago) {
            return false;
        }
        if ((this.codInstr == null && other.codInstr != null) || (this.codInstr != null && !this.codInstr.equals(other.codInstr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PagosDetPK[ codEmpr=" + codEmpr + ", nroPago=" + nroPago + ", codInstr=" + codInstr + " ]";
    }
    
}
