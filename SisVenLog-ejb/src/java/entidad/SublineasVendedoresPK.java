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
public class SublineasVendedoresPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_vendedor")
    private short codVendedor;

    public SublineasVendedoresPK() {
    }

    public SublineasVendedoresPK(short codEmpr, short codVendedor) {
        this.codEmpr = codEmpr;
        this.codVendedor = codVendedor;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codVendedor;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SublineasVendedoresPK)) {
            return false;
        }
        SublineasVendedoresPK other = (SublineasVendedoresPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codVendedor != other.codVendedor) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.SublineasVendedoresPK[ codEmpr=" + codEmpr + ", codVendedor=" + codVendedor + " ]";
    }
    
}
