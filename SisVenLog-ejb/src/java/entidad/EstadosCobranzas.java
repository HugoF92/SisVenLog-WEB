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
@Table(name = "estados_cobranzas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadosCobranzas.findAll", query = "SELECT e FROM EstadosCobranzas e")
    , @NamedQuery(name = "EstadosCobranzas.findByCodEstado", query = "SELECT e FROM EstadosCobranzas e WHERE e.codEstado = :codEstado")
    , @NamedQuery(name = "EstadosCobranzas.findByXdesc", query = "SELECT e FROM EstadosCobranzas e WHERE e.xdesc = :xdesc")
    , @NamedQuery(name = "EstadosCobranzas.findByCusuario", query = "SELECT e FROM EstadosCobranzas e WHERE e.cusuario = :cusuario")
    , @NamedQuery(name = "EstadosCobranzas.findByFalta", query = "SELECT e FROM EstadosCobranzas e WHERE e.falta = :falta")})
public class EstadosCobranzas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_estado")
    private String codEstado;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @OneToMany(mappedBy = "codEstado")
    private Collection<PlanillaCobradores> planillaCobradoresCollection;

    public EstadosCobranzas() {
    }

    public EstadosCobranzas(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
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

    @XmlTransient
    public Collection<PlanillaCobradores> getPlanillaCobradoresCollection() {
        return planillaCobradoresCollection;
    }

    public void setPlanillaCobradoresCollection(Collection<PlanillaCobradores> planillaCobradoresCollection) {
        this.planillaCobradoresCollection = planillaCobradoresCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codEstado != null ? codEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadosCobranzas)) {
            return false;
        }
        EstadosCobranzas other = (EstadosCobranzas) object;
        if ((this.codEstado == null && other.codEstado != null) || (this.codEstado != null && !this.codEstado.equals(other.codEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EstadosCobranzas[ codEstado=" + codEstado + " ]";
    }
    
}
