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
@Table(name = "caracteristicas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Caracteristicas.findAll", query = "SELECT c FROM Caracteristicas c")
    , @NamedQuery(name = "Caracteristicas.findByCodCaract", query = "SELECT c FROM Caracteristicas c WHERE c.codCaract = :codCaract")
    , @NamedQuery(name = "Caracteristicas.findByXdesc", query = "SELECT c FROM Caracteristicas c WHERE c.xdesc = :xdesc")
    , @NamedQuery(name = "Caracteristicas.findByMtipoDato", query = "SELECT c FROM Caracteristicas c WHERE c.mtipoDato = :mtipoDato")
    , @NamedQuery(name = "Caracteristicas.findByFalta", query = "SELECT c FROM Caracteristicas c WHERE c.falta = :falta")
    , @NamedQuery(name = "Caracteristicas.findByCusuario", query = "SELECT c FROM Caracteristicas c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "Caracteristicas.findByFultimModif", query = "SELECT c FROM Caracteristicas c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "Caracteristicas.findByCusuarioModif", query = "SELECT c FROM Caracteristicas c WHERE c.cusuarioModif = :cusuarioModif")})
public class Caracteristicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_caract")
    private Short codCaract;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "mtipo_dato")
    private Character mtipoDato;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "caracteristicas")
    private Collection<ClientesCaracteristicas> clientesCaracteristicasCollection;

    public Caracteristicas() {
    }

    public Caracteristicas(Short codCaract) {
        this.codCaract = codCaract;
    }

    public Short getCodCaract() {
        return codCaract;
    }

    public void setCodCaract(Short codCaract) {
        this.codCaract = codCaract;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Character getMtipoDato() {
        return mtipoDato;
    }

    public void setMtipoDato(Character mtipoDato) {
        this.mtipoDato = mtipoDato;
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

    @XmlTransient
    public Collection<ClientesCaracteristicas> getClientesCaracteristicasCollection() {
        return clientesCaracteristicasCollection;
    }

    public void setClientesCaracteristicasCollection(Collection<ClientesCaracteristicas> clientesCaracteristicasCollection) {
        this.clientesCaracteristicasCollection = clientesCaracteristicasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCaract != null ? codCaract.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Caracteristicas)) {
            return false;
        }
        Caracteristicas other = (Caracteristicas) object;
        if ((this.codCaract == null && other.codCaract != null) || (this.codCaract != null && !this.codCaract.equals(other.codCaract))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Caracteristicas[ codCaract=" + codCaract + " ]";
    }
    
}
