/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "retornos_precios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RetornosPrecios.findAll", query = "SELECT r FROM RetornosPrecios r")
    , @NamedQuery(name = "RetornosPrecios.findByCodEmpr", query = "SELECT r FROM RetornosPrecios r WHERE r.retornosPreciosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RetornosPrecios.findByCodDepo", query = "SELECT r FROM RetornosPrecios r WHERE r.retornosPreciosPK.codDepo = :codDepo")
    , @NamedQuery(name = "RetornosPrecios.findByCodMerca", query = "SELECT r FROM RetornosPrecios r WHERE r.retornosPreciosPK.codMerca = :codMerca")
    , @NamedQuery(name = "RetornosPrecios.findByCtipoVta", query = "SELECT r FROM RetornosPrecios r WHERE r.retornosPreciosPK.ctipoVta = :ctipoVta")
    , @NamedQuery(name = "RetornosPrecios.findByIretornoCaja", query = "SELECT r FROM RetornosPrecios r WHERE r.iretornoCaja = :iretornoCaja")
    , @NamedQuery(name = "RetornosPrecios.findByIretornoUnidad", query = "SELECT r FROM RetornosPrecios r WHERE r.iretornoUnidad = :iretornoUnidad")
    , @NamedQuery(name = "RetornosPrecios.findByIdevolCaja", query = "SELECT r FROM RetornosPrecios r WHERE r.idevolCaja = :idevolCaja")
    , @NamedQuery(name = "RetornosPrecios.findByIdevolUnidad", query = "SELECT r FROM RetornosPrecios r WHERE r.idevolUnidad = :idevolUnidad")
    , @NamedQuery(name = "RetornosPrecios.findByFrigeDesde", query = "SELECT r FROM RetornosPrecios r WHERE r.retornosPreciosPK.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "RetornosPrecios.findByFrigeHasta", query = "SELECT r FROM RetornosPrecios r WHERE r.frigeHasta = :frigeHasta")
    , @NamedQuery(name = "RetornosPrecios.findByFalta", query = "SELECT r FROM RetornosPrecios r WHERE r.falta = :falta")
    , @NamedQuery(name = "RetornosPrecios.findByFultimModif", query = "SELECT r FROM RetornosPrecios r WHERE r.fultimModif = :fultimModif")
    , @NamedQuery(name = "RetornosPrecios.findByCusuario", query = "SELECT r FROM RetornosPrecios r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "RetornosPrecios.findByCusuarioModif", query = "SELECT r FROM RetornosPrecios r WHERE r.cusuarioModif = :cusuarioModif")})
public class RetornosPrecios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RetornosPreciosPK retornosPreciosPK;
    @Column(name = "iretorno_caja")
    private Long iretornoCaja;
    @Column(name = "iretorno_unidad")
    private Long iretornoUnidad;
    @Column(name = "idevol_caja")
    private Long idevolCaja;
    @Column(name = "idevol_unidad")
    private Long idevolUnidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_depo", referencedColumnName = "cod_depo", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Existencias existencias;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "ctipo_vta", referencedColumnName = "ctipo_vta", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private TiposVentas tiposVentas;

    public RetornosPrecios() {
    }

    public RetornosPrecios(RetornosPreciosPK retornosPreciosPK) {
        this.retornosPreciosPK = retornosPreciosPK;
    }

    public RetornosPrecios(RetornosPreciosPK retornosPreciosPK, Date frigeHasta) {
        this.retornosPreciosPK = retornosPreciosPK;
        this.frigeHasta = frigeHasta;
    }

    public RetornosPrecios(short codEmpr, short codDepo, String codMerca, Character ctipoVta, Date frigeDesde) {
        this.retornosPreciosPK = new RetornosPreciosPK(codEmpr, codDepo, codMerca, ctipoVta, frigeDesde);
    }

    public RetornosPreciosPK getRetornosPreciosPK() {
        return retornosPreciosPK;
    }

    public void setRetornosPreciosPK(RetornosPreciosPK retornosPreciosPK) {
        this.retornosPreciosPK = retornosPreciosPK;
    }

    public Long getIretornoCaja() {
        return iretornoCaja;
    }

    public void setIretornoCaja(Long iretornoCaja) {
        this.iretornoCaja = iretornoCaja;
    }

    public Long getIretornoUnidad() {
        return iretornoUnidad;
    }

    public void setIretornoUnidad(Long iretornoUnidad) {
        this.iretornoUnidad = iretornoUnidad;
    }

    public Long getIdevolCaja() {
        return idevolCaja;
    }

    public void setIdevolCaja(Long idevolCaja) {
        this.idevolCaja = idevolCaja;
    }

    public Long getIdevolUnidad() {
        return idevolUnidad;
    }

    public void setIdevolUnidad(Long idevolUnidad) {
        this.idevolUnidad = idevolUnidad;
    }

    public Date getFrigeHasta() {
        return frigeHasta;
    }

    public void setFrigeHasta(Date frigeHasta) {
        this.frigeHasta = frigeHasta;
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

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public Existencias getExistencias() {
        return existencias;
    }

    public void setExistencias(Existencias existencias) {
        this.existencias = existencias;
    }

    public TiposVentas getTiposVentas() {
        return tiposVentas;
    }

    public void setTiposVentas(TiposVentas tiposVentas) {
        this.tiposVentas = tiposVentas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (retornosPreciosPK != null ? retornosPreciosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RetornosPrecios)) {
            return false;
        }
        RetornosPrecios other = (RetornosPrecios) object;
        if ((this.retornosPreciosPK == null && other.retornosPreciosPK != null) || (this.retornosPreciosPK != null && !this.retornosPreciosPK.equals(other.retornosPreciosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RetornosPrecios[ retornosPreciosPK=" + retornosPreciosPK + " ]";
    }
    
}
