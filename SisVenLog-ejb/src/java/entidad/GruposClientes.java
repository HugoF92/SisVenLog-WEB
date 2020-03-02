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
@Table(name = "grupos_clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GruposClientes.findAll", query = "SELECT g FROM GruposClientes g")
    , @NamedQuery(name = "GruposClientes.findByCodGrupo", query = "SELECT g FROM GruposClientes g WHERE g.codGrupo = :codGrupo")
    , @NamedQuery(name = "GruposClientes.findByXdesc", query = "SELECT g FROM GruposClientes g WHERE g.xdesc = :xdesc")
    , @NamedQuery(name = "GruposClientes.findByFalta", query = "SELECT g FROM GruposClientes g WHERE g.falta = :falta")
    , @NamedQuery(name = "GruposClientes.findByCusuario", query = "SELECT g FROM GruposClientes g WHERE g.cusuario = :cusuario")
    , @NamedQuery(name = "GruposClientes.findByFultimModif", query = "SELECT g FROM GruposClientes g WHERE g.fultimModif = :fultimModif")
    , @NamedQuery(name = "GruposClientes.findByCusuarioModif", query = "SELECT g FROM GruposClientes g WHERE g.cusuarioModif = :cusuarioModif")})
public class GruposClientes implements Serializable {

    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_grupo")
    private Short codGrupo;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gruposClientes")
    private Collection<ClientesAgrupados> clientesAgrupadosCollection;

    public GruposClientes() {
    }

    public GruposClientes(Short codGrupo) {
        this.codGrupo = codGrupo;
    }

    public Short getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(Short codGrupo) {
        this.codGrupo = codGrupo;
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
    public Collection<ClientesAgrupados> getClientesAgrupadosCollection() {
        return clientesAgrupadosCollection;
    }

    public void setClientesAgrupadosCollection(Collection<ClientesAgrupados> clientesAgrupadosCollection) {
        this.clientesAgrupadosCollection = clientesAgrupadosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codGrupo != null ? codGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GruposClientes)) {
            return false;
        }
        GruposClientes other = (GruposClientes) object;
        if ((this.codGrupo == null && other.codGrupo != null) || (this.codGrupo != null && !this.codGrupo.equals(other.codGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.GruposClientes[ codGrupo=" + codGrupo + " ]";
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
