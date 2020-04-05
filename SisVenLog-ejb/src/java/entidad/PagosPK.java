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

/**
 *
 * @author admin
 */
@Embeddable
public class PagosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_pago")
    private short nroPago;

    public PagosPK() {
    }

    public PagosPK(short codEmpr, short nroPago) {
        this.codEmpr = codEmpr;
        this.nroPago = nroPago;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroPago;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagosPK)) {
            return false;
        }
        PagosPK other = (PagosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroPago != other.nroPago) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PagosPK[ codEmpr=" + codEmpr + ", nroPago=" + nroPago + " ]";
    }
    
}
