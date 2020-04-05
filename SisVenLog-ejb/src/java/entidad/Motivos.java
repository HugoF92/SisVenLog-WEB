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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "motivos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Motivos.findAll", query = "SELECT m FROM Motivos m")
    , @NamedQuery(name = "Motivos.findByCmotivo", query = "SELECT m FROM Motivos m WHERE m.cmotivo = :cmotivo")
    , @NamedQuery(name = "Motivos.findByXdesc", query = "SELECT m FROM Motivos m WHERE m.xdesc = :xdesc")
    , @NamedQuery(name = "Motivos.findByFalta", query = "SELECT m FROM Motivos m WHERE m.falta = :falta")
    , @NamedQuery(name = "Motivos.findByCusuario", query = "SELECT m FROM Motivos m WHERE m.cusuario = :cusuario")
    , @NamedQuery(name = "Motivos.findByFultimModif", query = "SELECT m FROM Motivos m WHERE m.fultimModif = :fultimModif")
    , @NamedQuery(name = "Motivos.findByCusuarioModif", query = "SELECT m FROM Motivos m WHERE m.cusuarioModif = :cusuarioModif")})
public class Motivos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cmotivo")
    private Short cmotivo;
    @Size(max = 30)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    public Motivos() {
    }

    public Motivos(Short cmotivo) {
        this.cmotivo = cmotivo;
    }

    public Short getCmotivo() {
        return cmotivo;
    }

    public void setCmotivo(Short cmotivo) {
        this.cmotivo = cmotivo;
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

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cmotivo != null ? cmotivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Motivos)) {
            return false;
        }
        Motivos other = (Motivos) object;
        if ((this.cmotivo == null && other.cmotivo != null) || (this.cmotivo != null && !this.cmotivo.equals(other.cmotivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Motivos[ cmotivo=" + cmotivo + " ]";
    }
    
}
