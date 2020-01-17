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
public class CanalesVendedoresPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_vendedor")
    private short codVendedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_canal")
    private String codCanal;

    public CanalesVendedoresPK() {
    }

    public CanalesVendedoresPK(short codEmpr, short codVendedor, String codCanal) {
        this.codEmpr = codEmpr;
        this.codVendedor = codVendedor;
        this.codCanal = codCanal;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public short getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(short codVendedor) {
        this.codVendedor = codVendedor;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codVendedor;
        hash += (codCanal != null ? codCanal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CanalesVendedoresPK)) {
            return false;
        }
        CanalesVendedoresPK other = (CanalesVendedoresPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codVendedor != other.codVendedor) {
            return false;
        }
        if ((this.codCanal == null && other.codCanal != null) || (this.codCanal != null && !this.codCanal.equals(other.codCanal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CanalesVendedoresPK[ codEmpr=" + codEmpr + ", codVendedor=" + codVendedor + ", codCanal=" + codCanal + " ]";
    }
    
}
