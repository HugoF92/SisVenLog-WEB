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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "escala_categc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EscalaCategc.findAll", query = "SELECT e FROM EscalaCategc e")
    , @NamedQuery(name = "EscalaCategc.findByCodMerca", query = "SELECT e FROM EscalaCategc e WHERE e.escalaCategcPK.codMerca = :codMerca")
    , @NamedQuery(name = "EscalaCategc.findByCodZona", query = "SELECT e FROM EscalaCategc e WHERE e.escalaCategcPK.codZona = :codZona")
    , @NamedQuery(name = "EscalaCategc.findByCantCajas", query = "SELECT e FROM EscalaCategc e WHERE e.cantCajas = :cantCajas")
    , @NamedQuery(name = "EscalaCategc.findByFrigeDesde", query = "SELECT e FROM EscalaCategc e WHERE e.escalaCategcPK.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "EscalaCategc.findByFrigeHasta", query = "SELECT e FROM EscalaCategc e WHERE e.frigeHasta = :frigeHasta")})
public class EscalaCategc implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EscalaCategcPK escalaCategcPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_cajas")
    private short cantCajas;
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;

    public EscalaCategc() {
    }

    public EscalaCategc(EscalaCategcPK escalaCategcPK) {
        this.escalaCategcPK = escalaCategcPK;
    }

    public EscalaCategc(EscalaCategcPK escalaCategcPK, short cantCajas) {
        this.escalaCategcPK = escalaCategcPK;
        this.cantCajas = cantCajas;
    }

    public EscalaCategc(String codMerca, String codZona, Date frigeDesde) {
        this.escalaCategcPK = new EscalaCategcPK(codMerca, codZona, frigeDesde);
    }

    public EscalaCategcPK getEscalaCategcPK() {
        return escalaCategcPK;
    }

    public void setEscalaCategcPK(EscalaCategcPK escalaCategcPK) {
        this.escalaCategcPK = escalaCategcPK;
    }

    public short getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(short cantCajas) {
        this.cantCajas = cantCajas;
    }

    public Date getFrigeHasta() {
        return frigeHasta;
    }

    public void setFrigeHasta(Date frigeHasta) {
        this.frigeHasta = frigeHasta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (escalaCategcPK != null ? escalaCategcPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscalaCategc)) {
            return false;
        }
        EscalaCategc other = (EscalaCategc) object;
        if ((this.escalaCategcPK == null && other.escalaCategcPK != null) || (this.escalaCategcPK != null && !this.escalaCategcPK.equals(other.escalaCategcPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EscalaCategc[ escalaCategcPK=" + escalaCategcPK + " ]";
    }
    
}
