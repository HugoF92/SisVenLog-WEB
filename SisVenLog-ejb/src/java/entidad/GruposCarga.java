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
@Table(name = "grupos_carga")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GruposCarga.findAll", query = "SELECT g FROM GruposCarga g")
    , @NamedQuery(name = "GruposCarga.findByCodGcarga", query = "SELECT g FROM GruposCarga g WHERE g.codGcarga = :codGcarga")
    , @NamedQuery(name = "GruposCarga.findByXdesc", query = "SELECT g FROM GruposCarga g WHERE g.xdesc = :xdesc")
    , @NamedQuery(name = "GruposCarga.findByFalta", query = "SELECT g FROM GruposCarga g WHERE g.falta = :falta")
    , @NamedQuery(name = "GruposCarga.findByCusuario", query = "SELECT g FROM GruposCarga g WHERE g.cusuario = :cusuario")
    , @NamedQuery(name = "GruposCarga.findByFultimModif", query = "SELECT g FROM GruposCarga g WHERE g.fultimModif = :fultimModif")
    , @NamedQuery(name = "GruposCarga.findByCusuarioModif", query = "SELECT g FROM GruposCarga g WHERE g.cusuarioModif = :cusuarioModif")})
public class GruposCarga implements Serializable {

    @Size(max = 30)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_gcarga")
    private Short codGcarga;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @OneToMany(mappedBy = "codGcarga")
    private Collection<Sublineas> sublineasCollection;

    public GruposCarga() {
    }

    public GruposCarga(Short codGcarga) {
        this.codGcarga = codGcarga;
    }

    public Short getCodGcarga() {
        return codGcarga;
    }

    public void setCodGcarga(Short codGcarga) {
        this.codGcarga = codGcarga;
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

    @XmlTransient
    public Collection<Sublineas> getSublineasCollection() {
        return sublineasCollection;
    }

    public void setSublineasCollection(Collection<Sublineas> sublineasCollection) {
        this.sublineasCollection = sublineasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codGcarga != null ? codGcarga.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GruposCarga)) {
            return false;
        }
        GruposCarga other = (GruposCarga) object;
        if ((this.codGcarga == null && other.codGcarga != null) || (this.codGcarga != null && !this.codGcarga.equals(other.codGcarga))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.GruposCarga[ codGcarga=" + codGcarga + " ]";
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

    
}
