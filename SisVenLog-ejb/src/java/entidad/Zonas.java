/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Hugo
 */
@Entity
@Table(name = "zonas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zonas.findAll", query = "SELECT z FROM Zonas z")
    , @NamedQuery(name = "Zonas.findByCodEmpr", query = "SELECT z FROM Zonas z WHERE z.zonasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Zonas.findByCodZona", query = "SELECT z FROM Zonas z WHERE z.zonasPK.codZona = :codZona")
    , @NamedQuery(name = "Zonas.findByXdesc", query = "SELECT z FROM Zonas z WHERE z.xdesc = :xdesc")
    , @NamedQuery(name = "Zonas.findByCusuario", query = "SELECT z FROM Zonas z WHERE z.cusuario = :cusuario")
    , @NamedQuery(name = "Zonas.findByFalta", query = "SELECT z FROM Zonas z WHERE z.falta = :falta")
    , @NamedQuery(name = "Zonas.findByCusuarioModif", query = "SELECT z FROM Zonas z WHERE z.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Zonas.findByFultimModif", query = "SELECT z FROM Zonas z WHERE z.fultimModif = :fultimModif")})
public class Zonas implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zonas")
    private Collection<Rutas> rutasCollection;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZonasPK zonasPK;
    //@Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    //@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    //@Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zonas")
    private Collection<Depositos> depositosCollection;

    public Zonas() {
    }

    public Zonas(ZonasPK zonasPK) {
        this.zonasPK = zonasPK;
    }

    public Zonas(short codEmpr, String codZona) {
        this.zonasPK = new ZonasPK(codEmpr, codZona);
    }

    public ZonasPK getZonasPK() {
        return zonasPK;
    }

    public void setZonasPK(ZonasPK zonasPK) {
        this.zonasPK = zonasPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    @XmlTransient
    public Collection<Depositos> getDepositosCollection() {
        return depositosCollection;
    }

    public void setDepositosCollection(Collection<Depositos> depositosCollection) {
        this.depositosCollection = depositosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zonasPK != null ? zonasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zonas)) {
            return false;
        }
        Zonas other = (Zonas) object;
        if ((this.zonasPK == null && other.zonasPK != null) || (this.zonasPK != null && !this.zonasPK.equals(other.zonasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Zonas[ zonasPK=" + zonasPK + " ]";
    }

    @XmlTransient
    public Collection<Rutas> getRutasCollection() {
        return rutasCollection;
    }

    public void setRutasCollection(Collection<Rutas> rutasCollection) {
        this.rutasCollection = rutasCollection;
    }
    
}
