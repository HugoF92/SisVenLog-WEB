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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "categorias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categorias.findAll", query = "SELECT c FROM Categorias c")
    , @NamedQuery(name = "Categorias.findByCodCategoria", query = "SELECT c FROM Categorias c WHERE c.codCategoria = :codCategoria")
    , @NamedQuery(name = "Categorias.findByXdesc", query = "SELECT c FROM Categorias c WHERE c.xdesc = :xdesc")
    , @NamedQuery(name = "Categorias.findByFalta", query = "SELECT c FROM Categorias c WHERE c.falta = :falta")
    , @NamedQuery(name = "Categorias.findByCusuario", query = "SELECT c FROM Categorias c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "Categorias.findByFultimModif", query = "SELECT c FROM Categorias c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "Categorias.findByCusuarioModif", query = "SELECT c FROM Categorias c WHERE c.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Categorias.findByNordenEnvio", query = "SELECT c FROM Categorias c WHERE c.nordenEnvio = :nordenEnvio")})
public class Categorias implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "norden_envio")
    private short nordenEnvio;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_CATEGORIA")
    private Short codCategoria;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @OneToMany(mappedBy = "codCATEGORIA")
    private Collection<Lineas> lineasCollection;
    @JoinColumn(name = "cod_division", referencedColumnName = "cod_division")
    @ManyToOne
    private Divisiones codDivision;

    public Categorias() {
    }

    public Categorias(Short codCategoria) {
        this.codCategoria = codCategoria;
    }

    public Categorias(Short codCategoria, String xdesc, short nordenEnvio) {
        this.codCategoria = codCategoria;
        this.xdesc = xdesc;
        this.nordenEnvio = nordenEnvio;
    }

    public Short getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(Short codCategoria) {
        this.codCategoria = codCategoria;
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

    public short getNordenEnvio() {
        return nordenEnvio;
    }

    public void setNordenEnvio(short nordenEnvio) {
        this.nordenEnvio = nordenEnvio;
    }

    @XmlTransient
    public Collection<Lineas> getLineasCollection() {
        return lineasCollection;
    }

    public void setLineasCollection(Collection<Lineas> lineasCollection) {
        this.lineasCollection = lineasCollection;
    }

    public Divisiones getCodDivision() {
        return codDivision;
    }

    public void setCodDivision(Divisiones codDivision) {
        this.codDivision = codDivision;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCategoria != null ? codCategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categorias)) {
            return false;
        }
        Categorias other = (Categorias) object;
        if ((this.codCategoria == null && other.codCategoria != null) || (this.codCategoria != null && !this.codCategoria.equals(other.codCategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Categorias[ codCategoria=" + codCategoria + " ]";
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
