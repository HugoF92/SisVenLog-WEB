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
@Table(name = "transportistas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transportistas.findAll", query = "SELECT t FROM Transportistas t")
    , @NamedQuery(name = "Transportistas.findByCodTransp", query = "SELECT t FROM Transportistas t WHERE t.codTransp = :codTransp")
    , @NamedQuery(name = "Transportistas.findByXtransp", query = "SELECT t FROM Transportistas t WHERE t.xtransp = :xtransp")
    , @NamedQuery(name = "Transportistas.findByCusuario", query = "SELECT t FROM Transportistas t WHERE t.cusuario = :cusuario")
    , @NamedQuery(name = "Transportistas.findByFalta", query = "SELECT t FROM Transportistas t WHERE t.falta = :falta")
    , @NamedQuery(name = "Transportistas.findByCusuarioModif", query = "SELECT t FROM Transportistas t WHERE t.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Transportistas.findByFultimModif", query = "SELECT t FROM Transportistas t WHERE t.fultimModif = :fultimModif")
    , @NamedQuery(name = "Transportistas.findByXruc", query = "SELECT t FROM Transportistas t WHERE t.xruc = :xruc")
    , @NamedQuery(name = "Transportistas.findByXdomicilio", query = "SELECT t FROM Transportistas t WHERE t.xdomicilio = :xdomicilio")})
public class Transportistas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@NotNull
    @Column(name = "cod_transp")
    private Short codTransp;
    //@Size(max = 50)
    @Column(name = "xtransp")
    private String xtransp;
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
    //@Size(max = 30)
    @Column(name = "xruc")
    private String xruc;
    //@Size(max = 50)
    @Column(name = "xdomicilio")
    private String xdomicilio;
    @OneToMany(mappedBy = "codTransp")
    private Collection<Depositos> depositosCollection;

    public Transportistas() {
    }

    public Transportistas(Short codTransp) {
        this.codTransp = codTransp;
    }

    public Short getCodTransp() {
        return codTransp;
    }

    public void setCodTransp(Short codTransp) {
        this.codTransp = codTransp;
    }

    public String getXtransp() {
        return xtransp;
    }

    public void setXtransp(String xtransp) {
        this.xtransp = xtransp;
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

    public String getXruc() {
        return xruc;
    }

    public void setXruc(String xruc) {
        this.xruc = xruc;
    }

    public String getXdomicilio() {
        return xdomicilio;
    }

    public void setXdomicilio(String xdomicilio) {
        this.xdomicilio = xdomicilio;
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
        hash += (codTransp != null ? codTransp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transportistas)) {
            return false;
        }
        Transportistas other = (Transportistas) object;
        if ((this.codTransp == null && other.codTransp != null) || (this.codTransp != null && !this.codTransp.equals(other.codTransp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Transportistas[ codTransp=" + codTransp + " ]";
    }
    
}
