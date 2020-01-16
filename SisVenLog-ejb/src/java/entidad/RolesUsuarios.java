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
@Table(name = "roles_usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RolesUsuarios.findAll", query = "SELECT r FROM RolesUsuarios r")
    , @NamedQuery(name = "RolesUsuarios.findByCodEmpr", query = "SELECT r FROM RolesUsuarios r WHERE r.rolesUsuariosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RolesUsuarios.findByCodRol", query = "SELECT r FROM RolesUsuarios r WHERE r.rolesUsuariosPK.codRol = :codRol")
    , @NamedQuery(name = "RolesUsuarios.findByCodUsuario", query = "SELECT r FROM RolesUsuarios r WHERE r.rolesUsuariosPK.codUsuario = :codUsuario")
    , @NamedQuery(name = "RolesUsuarios.findByFalta", query = "SELECT r FROM RolesUsuarios r WHERE r.falta = :falta")
    , @NamedQuery(name = "RolesUsuarios.findByCusuario", query = "SELECT r FROM RolesUsuarios r WHERE r.cusuario = :cusuario")})
public class RolesUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RolesUsuariosPK rolesUsuariosPK;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_rol", referencedColumnName = "cod_rol", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Roles roles;
    @JoinColumns({
        @JoinColumn(name = "cod_usuario", referencedColumnName = "cod_usuario", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Usuarios usuarios;

    public RolesUsuarios() {
    }

    public RolesUsuarios(RolesUsuariosPK rolesUsuariosPK) {
        this.rolesUsuariosPK = rolesUsuariosPK;
    }

    public RolesUsuarios(short codEmpr, int codRol, String codUsuario) {
        this.rolesUsuariosPK = new RolesUsuariosPK(codEmpr, codRol, codUsuario);
    }

    public RolesUsuariosPK getRolesUsuariosPK() {
        return rolesUsuariosPK;
    }

    public void setRolesUsuariosPK(RolesUsuariosPK rolesUsuariosPK) {
        this.rolesUsuariosPK = rolesUsuariosPK;
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

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolesUsuariosPK != null ? rolesUsuariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolesUsuarios)) {
            return false;
        }
        RolesUsuarios other = (RolesUsuarios) object;
        if ((this.rolesUsuariosPK == null && other.rolesUsuariosPK != null) || (this.rolesUsuariosPK != null && !this.rolesUsuariosPK.equals(other.rolesUsuariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RolesUsuarios[ rolesUsuariosPK=" + rolesUsuariosPK + " ]";
    }
    
}
