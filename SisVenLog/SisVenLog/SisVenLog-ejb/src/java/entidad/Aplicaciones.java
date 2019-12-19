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
 * @author admin
 */
@Entity
@Table(name = "aplicaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aplicaciones.findAll", query = "SELECT a FROM Aplicaciones a")
    , @NamedQuery(name = "Aplicaciones.findByCodEmpr", query = "SELECT a FROM Aplicaciones a WHERE a.aplicacionesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Aplicaciones.findByCodAplicacion", query = "SELECT a FROM Aplicaciones a WHERE a.aplicacionesPK.codAplicacion = :codAplicacion")
    , @NamedQuery(name = "Aplicaciones.findByXdesc", query = "SELECT a FROM Aplicaciones a WHERE a.xdesc = :xdesc")
    , @NamedQuery(name = "Aplicaciones.findByFalta", query = "SELECT a FROM Aplicaciones a WHERE a.falta = :falta")
    , @NamedQuery(name = "Aplicaciones.findByCusuario", query = "SELECT a FROM Aplicaciones a WHERE a.cusuario = :cusuario")})
public class Aplicaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AplicacionesPK aplicacionesPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aplicaciones")
    private Collection<RolesAplicaciones> rolesAplicacionesCollection;

    public Aplicaciones() {
    }

    public Aplicaciones(AplicacionesPK aplicacionesPK) {
        this.aplicacionesPK = aplicacionesPK;
    }

    public Aplicaciones(short codEmpr, String codAplicacion) {
        this.aplicacionesPK = new AplicacionesPK(codEmpr, codAplicacion);
    }

    public AplicacionesPK getAplicacionesPK() {
        return aplicacionesPK;
    }

    public void setAplicacionesPK(AplicacionesPK aplicacionesPK) {
        this.aplicacionesPK = aplicacionesPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    @XmlTransient
    public Collection<RolesAplicaciones> getRolesAplicacionesCollection() {
        return rolesAplicacionesCollection;
    }

    public void setRolesAplicacionesCollection(Collection<RolesAplicaciones> rolesAplicacionesCollection) {
        this.rolesAplicacionesCollection = rolesAplicacionesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aplicacionesPK != null ? aplicacionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aplicaciones)) {
            return false;
        }
        Aplicaciones other = (Aplicaciones) object;
        if ((this.aplicacionesPK == null && other.aplicacionesPK != null) || (this.aplicacionesPK != null && !this.aplicacionesPK.equals(other.aplicacionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Aplicaciones[ aplicacionesPK=" + aplicacionesPK + " ]";
    }
    
}
