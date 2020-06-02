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
@Table(name = "divisiones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Divisiones.findAll", query = "SELECT d FROM Divisiones d")
    , @NamedQuery(name = "Divisiones.findByCodDivision", query = "SELECT d FROM Divisiones d WHERE d.codDivision = :codDivision")
    , @NamedQuery(name = "Divisiones.findByXdesc", query = "SELECT d FROM Divisiones d WHERE d.xdesc = :xdesc")
    , @NamedQuery(name = "Divisiones.findByCusuario", query = "SELECT d FROM Divisiones d WHERE d.cusuario = :cusuario")
    , @NamedQuery(name = "Divisiones.findByFalta", query = "SELECT d FROM Divisiones d WHERE d.falta = :falta")})
public class Divisiones implements Serializable {

    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @OneToMany(mappedBy = "codDivision")
    private Collection<Categorias> categoriasCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_division")
    private Short codDivision;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "mestado")
    private Character mestado;

    public Divisiones() {
    }

    public Divisiones(Short codDivision) {
        this.codDivision = codDivision;
    }

    public Short getCodDivision() {
        return codDivision;
    }

    public void setCodDivision(Short codDivision) {
        this.codDivision = codDivision;
    }


    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codDivision != null ? codDivision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Divisiones)) {
            return false;
        }
        Divisiones other = (Divisiones) object;
        if ((this.codDivision == null && other.codDivision != null) || (this.codDivision != null && !this.codDivision.equals(other.codDivision))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Divisiones[ codDivision=" + codDivision + " ]";
    }


    @XmlTransient
    public Collection<Categorias> getCategoriasCollection() {
        return categoriasCollection;
    }

    public void setCategoriasCollection(Collection<Categorias> categoriasCollection) {
        this.categoriasCollection = categoriasCollection;
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
