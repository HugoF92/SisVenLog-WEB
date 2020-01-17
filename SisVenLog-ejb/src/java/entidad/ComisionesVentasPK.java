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
public class ComisionesVentasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ctipo_emp")
    private String ctipoEmp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_sublinea")
    private short codSublinea;
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

    public ComisionesVentasPK() {
    }

    public ComisionesVentasPK(short codEmpr, String ctipoEmp, short codSublinea, String codZona, Date frigeDesde) {
        this.codEmpr = codEmpr;
        this.ctipoEmp = ctipoEmp;
        this.codSublinea = codSublinea;
        this.codZona = codZona;
        this.frigeDesde = frigeDesde;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getCtipoEmp() {
        return ctipoEmp;
    }

    public void setCtipoEmp(String ctipoEmp) {
        this.ctipoEmp = ctipoEmp;
    }

    public short getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(short codSublinea) {
        this.codSublinea = codSublinea;
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
        hash += (int) codEmpr;
        hash += (ctipoEmp != null ? ctipoEmp.hashCode() : 0);
        hash += (int) codSublinea;
        hash += (codZona != null ? codZona.hashCode() : 0);
        hash += (frigeDesde != null ? frigeDesde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComisionesVentasPK)) {
            return false;
        }
        ComisionesVentasPK other = (ComisionesVentasPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.ctipoEmp == null && other.ctipoEmp != null) || (this.ctipoEmp != null && !this.ctipoEmp.equals(other.ctipoEmp))) {
            return false;
        }
        if (this.codSublinea != other.codSublinea) {
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
        return "entidad.ComisionesVentasPK[ codEmpr=" + codEmpr + ", ctipoEmp=" + ctipoEmp + ", codSublinea=" + codSublinea + ", codZona=" + codZona + ", frigeDesde=" + frigeDesde + " ]";
    }
    
}
