/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "marcas_comerciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarcasComerciales.findAll", query = "SELECT m FROM MarcasComerciales m")
    , @NamedQuery(name = "MarcasComerciales.findByCmarca", query = "SELECT m FROM MarcasComerciales m WHERE m.cmarca = :cmarca")
    , @NamedQuery(name = "MarcasComerciales.findByXdesc", query = "SELECT m FROM MarcasComerciales m WHERE m.xdesc = :xdesc")
    , @NamedQuery(name = "MarcasComerciales.findByFalta", query = "SELECT m FROM MarcasComerciales m WHERE m.falta = :falta")
    , @NamedQuery(name = "MarcasComerciales.findByCusuario", query = "SELECT m FROM MarcasComerciales m WHERE m.cusuario = :cusuario")
    , @NamedQuery(name = "MarcasComerciales.findByFultimModif", query = "SELECT m FROM MarcasComerciales m WHERE m.fultimModif = :fultimModif")
    , @NamedQuery(name = "MarcasComerciales.findByCusuarioModif", query = "SELECT m FROM MarcasComerciales m WHERE m.cusuarioModif = :cusuarioModif")})
public class MarcasComerciales implements Serializable {

    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @OneToMany(mappedBy = "cmarca")
    private Collection<Mercaderias> mercaderiasCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cmarca")
    private Short cmarca;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;

    public MarcasComerciales() {
    }

    public MarcasComerciales(Short cmarca) {
        this.cmarca = cmarca;
    }

    public Short getCmarca() {
        return cmarca;
    }

    public void setCmarca(Short cmarca) {
        this.cmarca = cmarca;
    }


    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
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
        hash += (cmarca != null ? cmarca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarcasComerciales)) {
            return false;
        }
        MarcasComerciales other = (MarcasComerciales) object;
        if ((this.cmarca == null && other.cmarca != null) || (this.cmarca != null && !this.cmarca.equals(other.cmarca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MarcasComerciales[ cmarca=" + cmarca + " ]";
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


    @XmlTransient
    public Collection<Mercaderias> getMercaderiasCollection() {
        return mercaderiasCollection;
    }

    public void setMercaderiasCollection(Collection<Mercaderias> mercaderiasCollection) {
        this.mercaderiasCollection = mercaderiasCollection;
    }
    
}
