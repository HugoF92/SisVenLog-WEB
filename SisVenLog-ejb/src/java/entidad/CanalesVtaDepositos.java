/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dadob
 */
@Entity
@Table(name = "canales_vta_depositos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CanalesVtaDepositos.findAll", query = "SELECT c FROM CanalesVtaDepositos c")
    , @NamedQuery(name = "CanalesVtaDepositos.findByCodCanal", query = "SELECT c FROM CanalesVtaDepositos c WHERE c.canalesVtaDepositosPK.codCanal = :codCanal")
    , @NamedQuery(name = "CanalesVtaDepositos.findByCodDepo", query = "SELECT c FROM CanalesVtaDepositos c WHERE c.canalesVtaDepositosPK.codDepo = :codDepo")
    , @NamedQuery(name = "CanalesVtaDepositos.findByFalta", query = "SELECT c FROM CanalesVtaDepositos c WHERE c.falta = :falta")
    , @NamedQuery(name = "CanalesVtaDepositos.findByCusuAlta", query = "SELECT c FROM CanalesVtaDepositos c WHERE c.cusuAlta = :cusuAlta")
    , @NamedQuery(name = "CanalesVtaDepositos.findByFultimModif", query = "SELECT c FROM CanalesVtaDepositos c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "CanalesVtaDepositos.findByCusuModif", query = "SELECT c FROM CanalesVtaDepositos c WHERE c.cusuModif = :cusuModif")})
public class CanalesVtaDepositos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CanalesVtaDepositosPK canalesVtaDepositosPK;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusu_alta")
    private String cusuAlta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 10)
    @Column(name = "cusu_modif")
    private String cusuModif;
    
    @Transient
    private String depositoNombre;

    public CanalesVtaDepositos() {
    }

    public CanalesVtaDepositos(CanalesVtaDepositosPK canalesVtaDepositosPK) {
        this.canalesVtaDepositosPK = canalesVtaDepositosPK;
    }

    public CanalesVtaDepositos(String codCanal, short codDepo) {
        this.canalesVtaDepositosPK = new CanalesVtaDepositosPK(codCanal, codDepo);
    }

    public CanalesVtaDepositosPK getCanalesVtaDepositosPK() {
        return canalesVtaDepositosPK;
    }

    public void setCanalesVtaDepositosPK(CanalesVtaDepositosPK canalesVtaDepositosPK) {
        this.canalesVtaDepositosPK = canalesVtaDepositosPK;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuAlta() {
        return cusuAlta;
    }

    public void setCusuAlta(String cusuAlta) {
        this.cusuAlta = cusuAlta;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public String getCusuModif() {
        return cusuModif;
    }

    public void setCusuModif(String cusuModif) {
        this.cusuModif = cusuModif;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (canalesVtaDepositosPK != null ? canalesVtaDepositosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CanalesVtaDepositos)) {
            return false;
        }
        CanalesVtaDepositos other = (CanalesVtaDepositos) object;
        if ((this.canalesVtaDepositosPK == null && other.canalesVtaDepositosPK != null) || (this.canalesVtaDepositosPK != null && !this.canalesVtaDepositosPK.equals(other.canalesVtaDepositosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CanalesVtaDepositos[ canalesVtaDepositosPK=" + canalesVtaDepositosPK + " ]";
    }

    public String getDepositoNombre() {
        return depositoNombre;
    }

    public void setDepositoNombre(String depositoNombre) {
        this.depositoNombre = depositoNombre;
    }
    
    
}
