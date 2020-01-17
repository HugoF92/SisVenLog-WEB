/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "remisiones_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemisionesDet.findAll", query = "SELECT r FROM RemisionesDet r")
    , @NamedQuery(name = "RemisionesDet.findByCodEmpr", query = "SELECT r FROM RemisionesDet r WHERE r.remisionesDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RemisionesDet.findByNroRemision", query = "SELECT r FROM RemisionesDet r WHERE r.remisionesDetPK.nroRemision = :nroRemision")
    , @NamedQuery(name = "RemisionesDet.findByCodMerca", query = "SELECT r FROM RemisionesDet r WHERE r.remisionesDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "RemisionesDet.findByXdesc", query = "SELECT r FROM RemisionesDet r WHERE r.xdesc = :xdesc")
    , @NamedQuery(name = "RemisionesDet.findByCantCajas", query = "SELECT r FROM RemisionesDet r WHERE r.cantCajas = :cantCajas")
    , @NamedQuery(name = "RemisionesDet.findByCantUnid", query = "SELECT r FROM RemisionesDet r WHERE r.cantUnid = :cantUnid")})
public class RemisionesDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RemisionesDetPK remisionesDetPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "cant_cajas")
    private Integer cantCajas;
    @Column(name = "cant_unid")
    private Integer cantUnid;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_remision", referencedColumnName = "nro_remision", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Remisiones remisiones;

    public RemisionesDet() {
    }

    public RemisionesDet(RemisionesDetPK remisionesDetPK) {
        this.remisionesDetPK = remisionesDetPK;
    }

    public RemisionesDet(short codEmpr, long nroRemision, String codMerca) {
        this.remisionesDetPK = new RemisionesDetPK(codEmpr, nroRemision, codMerca);
    }

    public RemisionesDetPK getRemisionesDetPK() {
        return remisionesDetPK;
    }

    public void setRemisionesDetPK(RemisionesDetPK remisionesDetPK) {
        this.remisionesDetPK = remisionesDetPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Integer getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(Integer cantCajas) {
        this.cantCajas = cantCajas;
    }

    public Integer getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(Integer cantUnid) {
        this.cantUnid = cantUnid;
    }

    public Remisiones getRemisiones() {
        return remisiones;
    }

    public void setRemisiones(Remisiones remisiones) {
        this.remisiones = remisiones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (remisionesDetPK != null ? remisionesDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemisionesDet)) {
            return false;
        }
        RemisionesDet other = (RemisionesDet) object;
        if ((this.remisionesDetPK == null && other.remisionesDetPK != null) || (this.remisionesDetPK != null && !this.remisionesDetPK.equals(other.remisionesDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RemisionesDet[ remisionesDetPK=" + remisionesDetPK + " ]";
    }
    
}
