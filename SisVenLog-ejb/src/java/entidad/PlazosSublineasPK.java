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
public class PlazosSublineasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente")
    private int codCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_sublinea")
    private short codSublinea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeDesde;

    public PlazosSublineasPK() {
    }

    public PlazosSublineasPK(int codCliente, short codSublinea, Date frigeDesde) {
        this.codCliente = codCliente;
        this.codSublinea = codSublinea;
        this.frigeDesde = frigeDesde;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public short getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(short codSublinea) {
        this.codSublinea = codSublinea;
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
        hash += (int) codCliente;
        hash += (int) codSublinea;
        hash += (frigeDesde != null ? frigeDesde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlazosSublineasPK)) {
            return false;
        }
        PlazosSublineasPK other = (PlazosSublineasPK) object;
        if (this.codCliente != other.codCliente) {
            return false;
        }
        if (this.codSublinea != other.codSublinea) {
            return false;
        }
        if ((this.frigeDesde == null && other.frigeDesde != null) || (this.frigeDesde != null && !this.frigeDesde.equals(other.frigeDesde))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PlazosSublineasPK[ codCliente=" + codCliente + ", codSublinea=" + codSublinea + ", frigeDesde=" + frigeDesde + " ]";
    }
    
}
