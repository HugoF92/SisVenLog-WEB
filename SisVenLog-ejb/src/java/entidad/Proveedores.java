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
@Table(name = "proveedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedores.findAll", query = "SELECT p FROM Proveedores p")
    , @NamedQuery(name = "Proveedores.findByCodProveed", query = "SELECT p FROM Proveedores p WHERE p.codProveed = :codProveed")
    , @NamedQuery(name = "Proveedores.findByCodCanal", query = "SELECT p FROM Proveedores p WHERE p.codCanal = :codCanal")
    , @NamedQuery(name = "Proveedores.findByXnombre", query = "SELECT p FROM Proveedores p WHERE p.xnombre = :xnombre")
    , @NamedQuery(name = "Proveedores.findByXtelef", query = "SELECT p FROM Proveedores p WHERE p.xtelef = :xtelef")
    , @NamedQuery(name = "Proveedores.findByXruc", query = "SELECT p FROM Proveedores p WHERE p.xruc = :xruc")
    , @NamedQuery(name = "Proveedores.findByXdirec", query = "SELECT p FROM Proveedores p WHERE p.xdirec = :xdirec")
    , @NamedQuery(name = "Proveedores.findByIlimiteCredito", query = "SELECT p FROM Proveedores p WHERE p.ilimiteCredito = :ilimiteCredito")
    , @NamedQuery(name = "Proveedores.findByXcontacto", query = "SELECT p FROM Proveedores p WHERE p.xcontacto = :xcontacto")
    , @NamedQuery(name = "Proveedores.findByCusuario", query = "SELECT p FROM Proveedores p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "Proveedores.findByFalta", query = "SELECT p FROM Proveedores p WHERE p.falta = :falta")
    , @NamedQuery(name = "Proveedores.findByCusuarioModif", query = "SELECT p FROM Proveedores p WHERE p.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Proveedores.findByFultimModif", query = "SELECT p FROM Proveedores p WHERE p.fultimModif = :fultimModif")
    , @NamedQuery(name = "Proveedores.findByCodContasys", query = "SELECT p FROM Proveedores p WHERE p.codContasys = :codContasys")})
public class Proveedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private Short codProveed;
    @Column(name = "ilimite_credito")
    private Long ilimiteCredito;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Column(name = "cod_contasys")
    private Short codContasys;
    @Size(max = 2)
    @Column(name = "cod_canal")
    private String codCanal;
    @Size(max = 50)
    @Column(name = "xnombre")
    private String xnombre;
    @Size(max = 15)
    @Column(name = "xtelef")
    private String xtelef;
    @Size(max = 15)
    @Column(name = "xruc")
    private String xruc;
    @Size(max = 50)
    @Column(name = "xdirec")
    private String xdirec;
    @Size(max = 50)
    @Column(name = "xcontacto")
    private String xcontacto;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "mestado")
    private Character mestado;
    @OneToMany(mappedBy = "codProveed")
    private Collection<Mercaderias> mercaderiasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codProveed")
    private Collection<ChequesEmitidos> chequesEmitidosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codProveed")
    private Collection<CuentasProveedores> cuentasProveedoresCollection;

    public Proveedores() {
    }

    public Proveedores(Short codProveed) {
        this.codProveed = codProveed;
    }

    public Short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(Short codProveed) {
        this.codProveed = codProveed;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }


    public Long getIlimiteCredito() {
        return ilimiteCredito;
    }

    public void setIlimiteCredito(Long ilimiteCredito) {
        this.ilimiteCredito = ilimiteCredito;
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

    public Short getCodContasys() {
        return codContasys;
    }

    public void setCodContasys(Short codContasys) {
        this.codContasys = codContasys;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codProveed != null ? codProveed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedores)) {
            return false;
        }
        Proveedores other = (Proveedores) object;
        if ((this.codProveed == null && other.codProveed != null) || (this.codProveed != null && !this.codProveed.equals(other.codProveed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Proveedores[ codProveed=" + codProveed + " ]";
    }

    public String getXnombre() {
        return xnombre;
    }

    public void setXnombre(String xnombre) {
        this.xnombre = xnombre;
    }

    public String getXtelef() {
        return xtelef;
    }

    public void setXtelef(String xtelef) {
        this.xtelef = xtelef;
    }

    public String getXruc() {
        return xruc;
    }

    public void setXruc(String xruc) {
        this.xruc = xruc;
    }

    public String getXdirec() {
        return xdirec;
    }

    public void setXdirec(String xdirec) {
        this.xdirec = xdirec;
    }

    public String getXcontacto() {
        return xcontacto;
    }

    public void setXcontacto(String xcontacto) {
        this.xcontacto = xcontacto;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    @XmlTransient
    public Collection<Mercaderias> getMercaderiasCollection() {
        return mercaderiasCollection;
    }

    public void setMercaderiasCollection(Collection<Mercaderias> mercaderiasCollection) {
        this.mercaderiasCollection = mercaderiasCollection;
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
