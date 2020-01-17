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
import javax.persistence.CascadeType;
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
@Table(name = "htipos_ventas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HtiposVentas.findAll", query = "SELECT h FROM HtiposVentas h")
    , @NamedQuery(name = "HtiposVentas.findByCtipoVta", query = "SELECT h FROM HtiposVentas h WHERE h.ctipoVta = :ctipoVta")
    , @NamedQuery(name = "HtiposVentas.findByXdesc", query = "SELECT h FROM HtiposVentas h WHERE h.xdesc = :xdesc")
    , @NamedQuery(name = "HtiposVentas.findByCusuario", query = "SELECT h FROM HtiposVentas h WHERE h.cusuario = :cusuario")
    , @NamedQuery(name = "HtiposVentas.findByFalta", query = "SELECT h FROM HtiposVentas h WHERE h.falta = :falta")
    , @NamedQuery(name = "HtiposVentas.findByCusuarioModif", query = "SELECT h FROM HtiposVentas h WHERE h.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "HtiposVentas.findByFultimModif", query = "SELECT h FROM HtiposVentas h WHERE h.fultimModif = :fultimModif")})
public class HtiposVentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ctipo_vta")
    private String ctipoVta;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "htiposVentas")
    private Collection<HventasClientes> hventasClientesCollection;

    public HtiposVentas() {
    }

    public HtiposVentas(String ctipoVta) {
        this.ctipoVta = ctipoVta;
    }

    public String getCtipoVta() {
        return ctipoVta;
    }

    public void setCtipoVta(String ctipoVta) {
        this.ctipoVta = ctipoVta;
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
    public Collection<HventasClientes> getHventasClientesCollection() {
        return hventasClientesCollection;
    }

    public void setHventasClientesCollection(Collection<HventasClientes> hventasClientesCollection) {
        this.hventasClientesCollection = hventasClientesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctipoVta != null ? ctipoVta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HtiposVentas)) {
            return false;
        }
        HtiposVentas other = (HtiposVentas) object;
        if ((this.ctipoVta == null && other.ctipoVta != null) || (this.ctipoVta != null && !this.ctipoVta.equals(other.ctipoVta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.HtiposVentas[ ctipoVta=" + ctipoVta + " ]";
    }
    
}
