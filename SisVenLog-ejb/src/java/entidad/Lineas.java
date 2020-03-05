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
@Table(name = "lineas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lineas.findAll", query = "SELECT l FROM Lineas l")
    , @NamedQuery(name = "Lineas.findByCodLinea", query = "SELECT l FROM Lineas l WHERE l.codLinea = :codLinea")
    , @NamedQuery(name = "Lineas.findByXdesc", query = "SELECT l FROM Lineas l WHERE l.xdesc = :xdesc")
    , @NamedQuery(name = "Lineas.findByCusuario", query = "SELECT l FROM Lineas l WHERE l.cusuario = :cusuario")
    , @NamedQuery(name = "Lineas.findByFalta", query = "SELECT l FROM Lineas l WHERE l.falta = :falta")
    , @NamedQuery(name = "Lineas.findByCusuarioModif", query = "SELECT l FROM Lineas l WHERE l.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Lineas.findByFultimModif", query = "SELECT l FROM Lineas l WHERE l.fultimModif = :fultimModif")})
public class Lineas implements Serializable {

    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @OneToMany(mappedBy = "codLinea")
    private Collection<Sublineas> sublineasCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_linea")
    private Short codLinea;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @JoinColumn(name = "cod_CATEGORIA", referencedColumnName = "COD_CATEGORIA")
    @ManyToOne
    private Categorias codCATEGORIA;

    public Lineas() {
    }

    public Lineas(Short codLinea) {
        this.codLinea = codLinea;
    }

    public Short getCodLinea() {
        return codLinea;
    }

    public void setCodLinea(Short codLinea) {
        this.codLinea = codLinea;
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

    public Categorias getCodCATEGORIA() {
        return codCATEGORIA;
    }

    public void setCodCATEGORIA(Categorias codCATEGORIA) {
        this.codCATEGORIA = codCATEGORIA;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codLinea != null ? codLinea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lineas)) {
            return false;
        }
        Lineas other = (Lineas) object;
        if ((this.codLinea == null && other.codLinea != null) || (this.codLinea != null && !this.codLinea.equals(other.codLinea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Lineas[ codLinea=" + codLinea + " ]";
    }


    @XmlTransient
    public Collection<Sublineas> getSublineasCollection() {
        return sublineasCollection;
    }

    public void setSublineasCollection(Collection<Sublineas> sublineasCollection) {
        this.sublineasCollection = sublineasCollection;
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
