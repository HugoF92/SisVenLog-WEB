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
 * @author Hugo
 */
@Entity
@Table(name = "empresas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresas.findAll", query = "SELECT e FROM Empresas e")
    , @NamedQuery(name = "Empresas.findByCodEmpr", query = "SELECT e FROM Empresas e WHERE e.codEmpr = :codEmpr")
    , @NamedQuery(name = "Empresas.findByXrazonSocial", query = "SELECT e FROM Empresas e WHERE e.xrazonSocial = :xrazonSocial")
    , @NamedQuery(name = "Empresas.findByXtelef", query = "SELECT e FROM Empresas e WHERE e.xtelef = :xtelef")
    , @NamedQuery(name = "Empresas.findByXruc", query = "SELECT e FROM Empresas e WHERE e.xruc = :xruc")
    , @NamedQuery(name = "Empresas.findByXdirec", query = "SELECT e FROM Empresas e WHERE e.xdirec = :xdirec")
    , @NamedQuery(name = "Empresas.findByXcontacto", query = "SELECT e FROM Empresas e WHERE e.xcontacto = :xcontacto")
    , @NamedQuery(name = "Empresas.findByXlogo", query = "SELECT e FROM Empresas e WHERE e.xlogo = :xlogo")
    , @NamedQuery(name = "Empresas.findByCusuario", query = "SELECT e FROM Empresas e WHERE e.cusuario = :cusuario")
    , @NamedQuery(name = "Empresas.findByFalta", query = "SELECT e FROM Empresas e WHERE e.falta = :falta")
    , @NamedQuery(name = "Empresas.findByFultimModif", query = "SELECT e FROM Empresas e WHERE e.fultimModif = :fultimModif")
    , @NamedQuery(name = "Empresas.findByCusuarioModif", query = "SELECT e FROM Empresas e WHERE e.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Empresas.findByXaviso1Fvta", query = "SELECT e FROM Empresas e WHERE e.xaviso1Fvta = :xaviso1Fvta")
    , @NamedQuery(name = "Empresas.findByXaviso2Fvta", query = "SELECT e FROM Empresas e WHERE e.xaviso2Fvta = :xaviso2Fvta")})
public class Empresas implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresas")
    private Collection<Mercaderias> mercaderiasCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresas")
    private Collection<Empleados> empleadosCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@NotNull
    @Column(name = "cod_empr")
    private Short codEmpr;
    //@Size(max = 50)
    @Column(name = "xrazon_social")
    private String xrazonSocial;
    //@Size(max = 15)
    @Column(name = "xtelef")
    private String xtelef;
    //@Size(max = 15)
    @Column(name = "xruc")
    private String xruc;
    //@Size(max = 50)
    @Column(name = "xdirec")
    private String xdirec;
    //@Size(max = 50)
    @Column(name = "xcontacto")
    private String xcontacto;
    //@Size(max = 50)
    @Column(name = "xlogo")
    private String xlogo;
    //@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    //@Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    //@Size(max = 80)
    @Column(name = "xaviso1_fvta")
    private String xaviso1Fvta;
    //@Size(max = 80)
    @Column(name = "xaviso2_fvta")
    private String xaviso2Fvta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresas")
    private Collection<Depositos> depositosCollection;

    public Empresas() {
    }

    public Empresas(Short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public Short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(Short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getXrazonSocial() {
        return xrazonSocial;
    }

    public void setXrazonSocial(String xrazonSocial) {
        this.xrazonSocial = xrazonSocial;
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

    public String getXlogo() {
        return xlogo;
    }

    public void setXlogo(String xlogo) {
        this.xlogo = xlogo;
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

    public String getXaviso1Fvta() {
        return xaviso1Fvta;
    }

    public void setXaviso1Fvta(String xaviso1Fvta) {
        this.xaviso1Fvta = xaviso1Fvta;
    }

    public String getXaviso2Fvta() {
        return xaviso2Fvta;
    }

    public void setXaviso2Fvta(String xaviso2Fvta) {
        this.xaviso2Fvta = xaviso2Fvta;
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
        hash += (codEmpr != null ? codEmpr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresas)) {
            return false;
        }
        Empresas other = (Empresas) object;
        if ((this.codEmpr == null && other.codEmpr != null) || (this.codEmpr != null && !this.codEmpr.equals(other.codEmpr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Empresas[ codEmpr=" + codEmpr + " ]";
    }

    @XmlTransient
    public Collection<Empleados> getEmpleadosCollection() {
        return empleadosCollection;
    }

    public void setEmpleadosCollection(Collection<Empleados> empleadosCollection) {
        this.empleadosCollection = empleadosCollection;
    }

    @XmlTransient
    public Collection<Mercaderias> getMercaderiasCollection() {
        return mercaderiasCollection;
    }

    public void setMercaderiasCollection(Collection<Mercaderias> mercaderiasCollection) {
        this.mercaderiasCollection = mercaderiasCollection;
    }
    
}
