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
@Table(name = "sublineas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sublineas.findAll", query = "SELECT s FROM Sublineas s")
    , @NamedQuery(name = "Sublineas.findByCodSublinea", query = "SELECT s FROM Sublineas s WHERE s.codSublinea = :codSublinea")
    , @NamedQuery(name = "Sublineas.findByXdesc", query = "SELECT s FROM Sublineas s WHERE s.xdesc = :xdesc")
    , @NamedQuery(name = "Sublineas.findByCusuario", query = "SELECT s FROM Sublineas s WHERE s.cusuario = :cusuario")
    , @NamedQuery(name = "Sublineas.findByFalta", query = "SELECT s FROM Sublineas s WHERE s.falta = :falta")
    , @NamedQuery(name = "Sublineas.findByCusuarioModif", query = "SELECT s FROM Sublineas s WHERE s.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Sublineas.findByFultimModif", query = "SELECT s FROM Sublineas s WHERE s.fultimModif = :fultimModif")})
public class Sublineas implements Serializable {

    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @OneToMany(mappedBy = "codSublinea")
    private Collection<Mercaderias> mercaderiasCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_sublinea")
    private Short codSublinea;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @JoinColumn(name = "cod_gcarga", referencedColumnName = "cod_gcarga")
    @ManyToOne
    private GruposCarga codGcarga;
    @JoinColumn(name = "cod_linea", referencedColumnName = "cod_linea")
    @ManyToOne
    private Lineas codLinea;

    public Sublineas() {
    }

    public Sublineas(Short codSublinea) {
        this.codSublinea = codSublinea;
    }

    public Short getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(Short codSublinea) {
        this.codSublinea = codSublinea;
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

    public GruposCarga getCodGcarga() {
        return codGcarga;
    }

    public void setCodGcarga(GruposCarga codGcarga) {
        this.codGcarga = codGcarga;
    }

    public Lineas getCodLinea() {
        return codLinea;
    }

    public void setCodLinea(Lineas codLinea) {
        this.codLinea = codLinea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codSublinea != null ? codSublinea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sublineas)) {
            return false;
        }
        Sublineas other = (Sublineas) object;
        if ((this.codSublinea == null && other.codSublinea != null) || (this.codSublinea != null && !this.codSublinea.equals(other.codSublinea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Sublineas[ codSublinea=" + codSublinea + " ]";
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
