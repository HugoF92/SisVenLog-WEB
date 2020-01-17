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
 * @author HOME
 */
@Entity
@Table(name = "bancos")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "Bancos.findAll", query = "SELECT b FROM Bancos b")
    , @NamedQuery(name = "Bancos.findByCodBanco", query = "SELECT b FROM Bancos b WHERE b.codBanco = :codBanco")
    , @NamedQuery(name = "Bancos.findByXdesc", query = "SELECT b FROM Bancos b WHERE b.xdesc = :xdesc")
    , @NamedQuery(name = "Bancos.findByFalta", query = "SELECT b FROM Bancos b WHERE b.falta = :falta")
    , @NamedQuery(name = "Bancos.findByCusuario", query = "SELECT b FROM Bancos b WHERE b.cusuario = :cusuario")
    , @NamedQuery(name = "Bancos.findByFultimModif", query = "SELECT b FROM Bancos b WHERE b.fultimModif = :fultimModif")
    , @NamedQuery(name = "Bancos.findByCusuarioModif", query = "SELECT b FROM Bancos b WHERE b.cusuarioModif = :cusuarioModif")})
public class Bancos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    ////@NotNull
    @Column(name = "cod_banco")
    private Short codBanco;
    ////@Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    ////@Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    ////@Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @OneToMany(mappedBy = "codBanco")
    private Collection<CuentasCorrientes> cuentasCorrientesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bancos")
    private Collection<Cheques> chequesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bancos")
    private Collection<ChequesEmitidos> chequesEmitidosCollection;
    @OneToMany(mappedBy = "codBanco")
    private Collection<CuentasProveedores> cuentasProveedoresCollection;
        
    public Bancos() {
    }

    public Bancos(Short codBanco) {
        this.codBanco = codBanco;
    }

    public Short getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(Short codBanco) {
        this.codBanco = codBanco;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codBanco != null ? codBanco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bancos)) {
            return false;
        }
        Bancos other = (Bancos) object;
        if ((this.codBanco == null && other.codBanco != null) || (this.codBanco != null && !this.codBanco.equals(other.codBanco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Bancos[ codBanco=" + codBanco + " ]";
    }

    @XmlTransient
    public Collection<CuentasCorrientes> getCuentasCorrientesCollection() {
        return cuentasCorrientesCollection;
    }

    public void setCuentasCorrientesCollection(Collection<CuentasCorrientes> cuentasCorrientesCollection) {
        this.cuentasCorrientesCollection = cuentasCorrientesCollection;
    }

    @XmlTransient
    public Collection<Cheques> getChequesCollection() {
        return chequesCollection;
    }

    public void setChequesCollection(Collection<Cheques> chequesCollection) {
        this.chequesCollection = chequesCollection;
    }

    @XmlTransient
    public Collection<ChequesEmitidos> getChequesEmitidosCollection() {
        return chequesEmitidosCollection;
    }

    public void setChequesEmitidosCollection(Collection<ChequesEmitidos> chequesEmitidosCollection) {
        this.chequesEmitidosCollection = chequesEmitidosCollection;
    }

    @XmlTransient
    public Collection<CuentasProveedores> getCuentasProveedoresCollection() {
        return cuentasProveedoresCollection;
    }

    public void setCuentasProveedoresCollection(Collection<CuentasProveedores> cuentasProveedoresCollection) {
        this.cuentasProveedoresCollection = cuentasProveedoresCollection;
    }
    
}
