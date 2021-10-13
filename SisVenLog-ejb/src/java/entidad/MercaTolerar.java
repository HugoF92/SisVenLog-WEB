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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
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
 * @author admin
 */
@Entity
@Table(name = "merca_tolerar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MercaTolerar.findAll", query = "SELECT m FROM MercaTolerar m")
    , @NamedQuery(name = "MercaTolerar.findByCodEmpr", query = "SELECT m FROM MercaTolerar m WHERE m.mercaTolerarPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "MercaTolerar.findByNrofact", query = "SELECT m FROM MercaTolerar m WHERE m.mercaTolerarPK.nrofact = :nrofact")
    , @NamedQuery(name = "MercaTolerar.findByCodMerca", query = "SELECT m FROM MercaTolerar m WHERE m.mercaTolerarPK.codMerca = :codMerca")
    , @NamedQuery(name = "MercaTolerar.findByCodProveed", query = "SELECT m FROM MercaTolerar m WHERE m.mercaTolerarPK.codProveed = :codProveed")
    , @NamedQuery(name = "MercaTolerar.findByItolerar", query = "SELECT m FROM MercaTolerar m WHERE m.itolerar = :itolerar")
    , @NamedQuery(name = "MercaTolerar.findByFalta", query = "SELECT m FROM MercaTolerar m WHERE m.falta = :falta")
    , @NamedQuery(name = "MercaTolerar.findByCusuario", query = "SELECT m FROM MercaTolerar m WHERE m.cusuario = :cusuario")
    , @NamedQuery(name = "MercaTolerar.findByFultimModif", query = "SELECT m FROM MercaTolerar m WHERE m.fultimModif = :fultimModif")
    , @NamedQuery(name = "MercaTolerar.findByCusuarioModif", query = "SELECT m FROM MercaTolerar m WHERE m.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "MercaTolerar.findByXobs", query = "SELECT m FROM MercaTolerar m WHERE m.xobs = :xobs")
    , @NamedQuery(name = "MercaTolerar.findByFfactur", query = "SELECT m FROM MercaTolerar m WHERE m.ffactur = :ffactur")})
public class MercaTolerar implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MercaTolerarPK mercaTolerarPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "itolerar")
    private BigDecimal itolerar;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Size(max = 200)
    @Column(name = "xobs")
    private String xobs;
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
   @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proveedores proveedores;
    
    public MercaTolerar() {
    }

    public MercaTolerar(MercaTolerarPK mercaTolerarPK) {
        this.mercaTolerarPK = mercaTolerarPK;
    }

    public MercaTolerar(MercaTolerarPK mercaTolerarPK, BigDecimal itolerar) {
        this.mercaTolerarPK = mercaTolerarPK;
        this.itolerar = itolerar;
    }

    public MercaTolerar(short codEmpr, long nrofact, String codMerca, short codProveed) {
        this.mercaTolerarPK = new MercaTolerarPK(codEmpr, nrofact, codMerca, codProveed);
    }

    public MercaTolerarPK getMercaTolerarPK() {
        return mercaTolerarPK;
    }

    public void setMercaTolerarPK(MercaTolerarPK mercaTolerarPK) {
        this.mercaTolerarPK = mercaTolerarPK;
    }

    public BigDecimal getItolerar() {
        return itolerar;
    }

    public void setItolerar(BigDecimal itolerar) {
        this.itolerar = itolerar;
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

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mercaTolerarPK != null ? mercaTolerarPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MercaTolerar)) {
            return false;
        }
        MercaTolerar other = (MercaTolerar) object;
        if ((this.mercaTolerarPK == null && other.mercaTolerarPK != null) || (this.mercaTolerarPK != null && !this.mercaTolerarPK.equals(other.mercaTolerarPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MercaTolerar[ mercaTolerarPK=" + mercaTolerarPK + " ]";
    }

}
