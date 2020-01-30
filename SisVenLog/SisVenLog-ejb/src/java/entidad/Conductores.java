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
 * @author Hugo
 */
@Entity
@Table(name = "conductores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conductores.findAll", query = "SELECT c FROM Conductores c")
    , @NamedQuery(name = "Conductores.findByCodConductor", query = "SELECT c FROM Conductores c WHERE c.codConductor = :codConductor")
    , @NamedQuery(name = "Conductores.findByXconductor", query = "SELECT c FROM Conductores c WHERE c.xconductor = :xconductor")
    , @NamedQuery(name = "Conductores.findByXdocum", query = "SELECT c FROM Conductores c WHERE c.xdocum = :xdocum")
    , @NamedQuery(name = "Conductores.findByXdomicilio", query = "SELECT c FROM Conductores c WHERE c.xdomicilio = :xdomicilio")
    , @NamedQuery(name = "Conductores.findByCusuario", query = "SELECT c FROM Conductores c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "Conductores.findByFalta", query = "SELECT c FROM Conductores c WHERE c.falta = :falta")
    , @NamedQuery(name = "Conductores.findByCusuarioModif", query = "SELECT c FROM Conductores c WHERE c.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Conductores.findByFultimModif", query = "SELECT c FROM Conductores c WHERE c.fultimModif = :fultimModif")})
public class Conductores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@NotNull
    @Column(name = "cod_conductor")
    private Short codConductor;
    //@Size(max = 50)
    @Column(name = "xconductor")
    private String xconductor;
    //@Size(max = 30)
    @Column(name = "xdocum")
    private String xdocum;
    //@Size(max = 50)
    @Column(name = "xdomicilio")
    private String xdomicilio;
    //@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    //@Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @OneToMany(mappedBy = "codConductor")
    private Collection<Depositos> depositosCollection;

    public Conductores() {
    }

    public Conductores(Short codConductor) {
        this.codConductor = codConductor;
    }

    public Short getCodConductor() {
        return codConductor;
    }

    public void setCodConductor(Short codConductor) {
        this.codConductor = codConductor;
    }

    public String getXconductor() {
        return xconductor;
    }

    public void setXconductor(String xconductor) {
        this.xconductor = xconductor;
    }

    public String getXdocum() {
        return xdocum;
    }

    public void setXdocum(String xdocum) {
        this.xdocum = xdocum;
    }

    public String getXdomicilio() {
        return xdomicilio;
    }

    public void setXdomicilio(String xdomicilio) {
        this.xdomicilio = xdomicilio;
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
    public Collection<Depositos> getDepositosCollection() {
        return depositosCollection;
    }

    public void setDepositosCollection(Collection<Depositos> depositosCollection) {
        this.depositosCollection = depositosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codConductor != null ? codConductor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conductores)) {
            return false;
        }
        Conductores other = (Conductores) object;
        if ((this.codConductor == null && other.codConductor != null) || (this.codConductor != null && !this.codConductor.equals(other.codConductor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Conductores[ codConductor=" + codConductor + " ]";
    }
    
}
