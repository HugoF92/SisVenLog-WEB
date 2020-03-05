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
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class CuposVendedorPK implements Serializable {

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
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeDesde;

    public CuposVendedorPK() {
    }

    public CuposVendedorPK(short codEmpr, short codVendedor, String codMerca, Date frigeDesde) {
        this.codEmpr = codEmpr;
        this.codVendedor = codVendedor;
        this.codMerca = codMerca;
        this.frigeDesde = frigeDesde;
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

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public Date getFrigeDesde() {
        return frigeDesde;
    }

    public void setFrigeDesde(Date frigeDesde) {
        this.frigeDesde = frigeDesde;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) codVendedor;
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (frigeDesde != null ? frigeDesde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuposVendedorPK)) {
            return false;
        }
        CuposVendedorPK other = (CuposVendedorPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.codVendedor != other.codVendedor) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if ((this.frigeDesde == null && other.frigeDesde != null) || (this.frigeDesde != null && !this.frigeDesde.equals(other.frigeDesde))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CuposVendedorPK[ codEmpr=" + codEmpr + ", codVendedor=" + codVendedor + ", codMerca=" + codMerca + ", frigeDesde=" + frigeDesde + " ]";
    }
    
}
