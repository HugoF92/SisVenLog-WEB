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
@Table(name = "roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r")
    , @NamedQuery(name = "Roles.findByCodEmpr", query = "SELECT r FROM Roles r WHERE r.rolesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Roles.findByCodRol", query = "SELECT r FROM Roles r WHERE r.rolesPK.codRol = :codRol")
    , @NamedQuery(name = "Roles.findByXdesc", query = "SELECT r FROM Roles r WHERE r.xdesc = :xdesc")
    , @NamedQuery(name = "Roles.findByCusuario", query = "SELECT r FROM Roles r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "Roles.findByFalta", query = "SELECT r FROM Roles r WHERE r.falta = :falta")})
public class Roles implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roles")
    private Collection<RolesAplicaciones> rolesAplicacionesCollection;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RolesPK rolesPK;
   //@Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
   //@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;

    public Roles() {
    }

    public Roles(RolesPK rolesPK) {
        this.rolesPK = rolesPK;
    }

    public Roles(short codEmpr, int codRol) {
        this.rolesPK = new RolesPK(codEmpr, codRol);
    }

    public RolesPK getRolesPK() {
        return rolesPK;
    }

    public void setRolesPK(RolesPK rolesPK) {
        this.rolesPK = rolesPK;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolesPK != null ? rolesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roles)) {
            return false;
        }
        Roles other = (Roles) object;
        if ((this.rolesPK == null && other.rolesPK != null) || (this.rolesPK != null && !this.rolesPK.equals(other.rolesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Roles[ rolesPK=" + rolesPK + " ]";
    }

    @XmlTransient
    public Collection<RolesAplicaciones> getRolesAplicacionesCollection() {
        return rolesAplicacionesCollection;
    }

    public void setRolesAplicacionesCollection(Collection<RolesAplicaciones> rolesAplicacionesCollection) {
        this.rolesAplicacionesCollection = rolesAplicacionesCollection;
    }
    
}
