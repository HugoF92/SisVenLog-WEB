/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dadob
 */
@Entity
@Table(name = "impuestos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Impuestos.findAll", query = "SELECT i FROM Impuestos i")
    , @NamedQuery(name = "Impuestos.findByCodImpu", query = "SELECT i FROM Impuestos i WHERE i.codImpu = :codImpu")
    , @NamedQuery(name = "Impuestos.findByXdesc", query = "SELECT i FROM Impuestos i WHERE i.xdesc = :xdesc")
    , @NamedQuery(name = "Impuestos.findByPimpues", query = "SELECT i FROM Impuestos i WHERE i.pimpues = :pimpues")
    , @NamedQuery(name = "Impuestos.findByMfijoSis", query = "SELECT i FROM Impuestos i WHERE i.mfijoSis = :mfijoSis")
    , @NamedQuery(name = "Impuestos.findByIfijo", query = "SELECT i FROM Impuestos i WHERE i.ifijo = :ifijo")
    , @NamedQuery(name = "Impuestos.findByMreten", query = "SELECT i FROM Impuestos i WHERE i.mreten = :mreten")
    , @NamedQuery(name = "Impuestos.findByFalta", query = "SELECT i FROM Impuestos i WHERE i.falta = :falta")
    , @NamedQuery(name = "Impuestos.findByCusuario", query = "SELECT i FROM Impuestos i WHERE i.cusuario = :cusuario")
    , @NamedQuery(name = "Impuestos.findByFultimModif", query = "SELECT i FROM Impuestos i WHERE i.fultimModif = :fultimModif")
    , @NamedQuery(name = "Impuestos.findByCusuarioModif", query = "SELECT i FROM Impuestos i WHERE i.cusuarioModif = :cusuarioModif")})
public class Impuestos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_impu")
    private Short codImpu;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pimpues")
    private BigDecimal pimpues;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mfijo_sis")
    private Character mfijoSis;
    @Column(name = "ifijo")
    private Integer ifijo;
    @Column(name = "mreten")
    private Character mreten;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    public Impuestos() {
    }

    public Impuestos(Short codImpu) {
        this.codImpu = codImpu;
    }

    public Impuestos(Short codImpu, BigDecimal pimpues, Character mfijoSis) {
        this.codImpu = codImpu;
        this.pimpues = pimpues;
        this.mfijoSis = mfijoSis;
    }

    public Short getCodImpu() {
        return codImpu;
    }

    public void setCodImpu(Short codImpu) {
        this.codImpu = codImpu;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
    }

    public Character getMfijoSis() {
        return mfijoSis;
    }

    public void setMfijoSis(Character mfijoSis) {
        this.mfijoSis = mfijoSis;
    }

    public Integer getIfijo() {
        return ifijo;
    }

    public void setIfijo(Integer ifijo) {
        this.ifijo = ifijo;
    }

    public Character getMreten() {
        return mreten;
    }

    public void setMreten(Character mreten) {
        this.mreten = mreten;
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
        hash += (codImpu != null ? codImpu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Impuestos)) {
            return false;
        }
        Impuestos other = (Impuestos) object;
        if ((this.codImpu == null && other.codImpu != null) || (this.codImpu != null && !this.codImpu.equals(other.codImpu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Impuestos[ codImpu=" + codImpu + " ]";
    }
    
}
