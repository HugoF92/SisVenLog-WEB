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
 * @author Hugo
 */
@Entity
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")
    , @NamedQuery(name = "Usuarios.findByCodUsuario", query = "SELECT u FROM Usuarios u WHERE u.usuariosPK.codUsuario = :codUsuario")
    , @NamedQuery(name = "Usuarios.findByXnombre", query = "SELECT u FROM Usuarios u WHERE u.xnombre = :xnombre")
    , @NamedQuery(name = "Usuarios.findByCodEmpr", query = "SELECT u FROM Usuarios u WHERE u.usuariosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Usuarios.findByCusuario", query = "SELECT u FROM Usuarios u WHERE u.cusuario = :cusuario")
    , @NamedQuery(name = "Usuarios.findByFalta", query = "SELECT u FROM Usuarios u WHERE u.falta = :falta")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuariosPK usuariosPK;
   //@Size(max = 50)
    @Column(name = "xnombre")
    private String xnombre;
   //@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_empleado", referencedColumnName = "cod_empleado")})
    @ManyToOne(optional = false)
    private Empleados empleados;

    public Usuarios() {
    }

    public Usuarios(UsuariosPK usuariosPK) {
        this.usuariosPK = usuariosPK;
    }

    public Usuarios(String codUsuario, short codEmpr) {
        this.usuariosPK = new UsuariosPK(codUsuario, codEmpr);
    }

    public UsuariosPK getUsuariosPK() {
        return usuariosPK;
    }

    public void setUsuariosPK(UsuariosPK usuariosPK) {
        this.usuariosPK = usuariosPK;
    }

    public String getXnombre() {
        return xnombre;
    }

    public void setXnombre(String xnombre) {
        this.xnombre = xnombre;
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

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuariosPK != null ? usuariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.usuariosPK == null && other.usuariosPK != null) || (this.usuariosPK != null && !this.usuariosPK.equals(other.usuariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Usuarios[ usuariosPK=" + usuariosPK + " ]";
    }
    
}
