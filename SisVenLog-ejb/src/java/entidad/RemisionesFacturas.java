/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "remisiones_facturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemisionesFacturas.findAll", query = "SELECT r FROM RemisionesFacturas r")
    , @NamedQuery(name = "RemisionesFacturas.findByCodEmpr", query = "SELECT r FROM RemisionesFacturas r WHERE r.remisionesFacturasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RemisionesFacturas.findByNroRemision", query = "SELECT r FROM RemisionesFacturas r WHERE r.remisionesFacturasPK.nroRemision = :nroRemision")
    , @NamedQuery(name = "RemisionesFacturas.findByCtipoDocum", query = "SELECT r FROM RemisionesFacturas r WHERE r.remisionesFacturasPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "RemisionesFacturas.findByNrofact", query = "SELECT r FROM RemisionesFacturas r WHERE r.remisionesFacturasPK.nrofact = :nrofact")})
public class RemisionesFacturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RemisionesFacturasPK remisionesFacturasPK;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_remision", referencedColumnName = "nro_remision", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Remisiones remisiones;

    public RemisionesFacturas() {
    }

    public RemisionesFacturas(RemisionesFacturasPK remisionesFacturasPK) {
        this.remisionesFacturasPK = remisionesFacturasPK;
    }

    public RemisionesFacturas(short codEmpr, long nroRemision, String ctipoDocum, long nrofact) {
        this.remisionesFacturasPK = new RemisionesFacturasPK(codEmpr, nroRemision, ctipoDocum, nrofact);
    }

    public RemisionesFacturasPK getRemisionesFacturasPK() {
        return remisionesFacturasPK;
    }

    public void setRemisionesFacturasPK(RemisionesFacturasPK remisionesFacturasPK) {
        this.remisionesFacturasPK = remisionesFacturasPK;
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
        hash += (remisionesFacturasPK != null ? remisionesFacturasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemisionesFacturas)) {
            return false;
        }
        RemisionesFacturas other = (RemisionesFacturas) object;
        if ((this.remisionesFacturasPK == null && other.remisionesFacturasPK != null) || (this.remisionesFacturasPK != null && !this.remisionesFacturasPK.equals(other.remisionesFacturasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RemisionesFacturas[ remisionesFacturasPK=" + remisionesFacturasPK + " ]";
    }
    
}
