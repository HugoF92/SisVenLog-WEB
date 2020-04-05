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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "roles_aplicaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RolesAplicaciones.findAll", query = "SELECT r FROM RolesAplicaciones r")
    , @NamedQuery(name = "RolesAplicaciones.findByCodEmpr", query = "SELECT r FROM RolesAplicaciones r WHERE r.rolesAplicacionesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RolesAplicaciones.findByCodRol", query = "SELECT r FROM RolesAplicaciones r WHERE r.rolesAplicacionesPK.codRol = :codRol")
    , @NamedQuery(name = "RolesAplicaciones.findByCodAplicacion", query = "SELECT r FROM RolesAplicaciones r WHERE r.rolesAplicacionesPK.codAplicacion = :codAplicacion")
    , @NamedQuery(name = "RolesAplicaciones.findByFalta", query = "SELECT r FROM RolesAplicaciones r WHERE r.falta = :falta")
    , @NamedQuery(name = "RolesAplicaciones.findByCusuario", query = "SELECT r FROM RolesAplicaciones r WHERE r.cusuario = :cusuario")})
public class RolesAplicaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RolesAplicacionesPK rolesAplicacionesPK;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_aplicacion", referencedColumnName = "cod_aplicacion", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Aplicaciones aplicaciones;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_rol", referencedColumnName = "cod_rol", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Roles roles;

    public RolesAplicaciones() {
    }

    public RolesAplicaciones(RolesAplicacionesPK rolesAplicacionesPK) {
        this.rolesAplicacionesPK = rolesAplicacionesPK;
    }

    public RolesAplicaciones(short codEmpr, int codRol, String codAplicacion) {
        this.rolesAplicacionesPK = new RolesAplicacionesPK(codEmpr, codRol, codAplicacion);
    }

    public RolesAplicacionesPK getRolesAplicacionesPK() {
        return rolesAplicacionesPK;
    }

    public void setRolesAplicacionesPK(RolesAplicacionesPK rolesAplicacionesPK) {
        this.rolesAplicacionesPK = rolesAplicacionesPK;
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

    public Aplicaciones getAplicaciones() {
        return aplicaciones;
    }

    public void setAplicaciones(Aplicaciones aplicaciones) {
        this.aplicaciones = aplicaciones;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolesAplicacionesPK != null ? rolesAplicacionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolesAplicaciones)) {
            return false;
        }
        RolesAplicaciones other = (RolesAplicaciones) object;
        if ((this.rolesAplicacionesPK == null && other.rolesAplicacionesPK != null) || (this.rolesAplicacionesPK != null && !this.rolesAplicacionesPK.equals(other.rolesAplicacionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RolesAplicaciones[ rolesAplicacionesPK=" + rolesAplicacionesPK + " ]";
    }
    
}
