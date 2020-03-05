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
public class EscalaCategcPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_zona")
    private String codZona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeDesde;

    public EscalaCategcPK() {
    }

    public EscalaCategcPK(String codMerca, String codZona, Date frigeDesde) {
        this.codMerca = codMerca;
        this.codZona = codZona;
        this.frigeDesde = frigeDesde;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
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
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (codZona != null ? codZona.hashCode() : 0);
        hash += (frigeDesde != null ? frigeDesde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscalaCategcPK)) {
            return false;
        }
        EscalaCategcPK other = (EscalaCategcPK) object;
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if ((this.codZona == null && other.codZona != null) || (this.codZona != null && !this.codZona.equals(other.codZona))) {
            return false;
        }
        if ((this.frigeDesde == null && other.frigeDesde != null) || (this.frigeDesde != null && !this.frigeDesde.equals(other.frigeDesde))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EscalaCategcPK[ codMerca=" + codMerca + ", codZona=" + codZona + ", frigeDesde=" + frigeDesde + " ]";
    }
    
}
