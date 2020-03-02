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
@Table(name = "precios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Precios.findAll", query = "SELECT p FROM Precios p")
    , @NamedQuery(name = "Precios.findByCodEmpr", query = "SELECT p FROM Precios p WHERE p.preciosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Precios.findByCodDepo", query = "SELECT p FROM Precios p WHERE p.preciosPK.codDepo = :codDepo")
    , @NamedQuery(name = "Precios.findByCodMerca", query = "SELECT p FROM Precios p WHERE p.preciosPK.codMerca = :codMerca")
    , @NamedQuery(name = "Precios.findByCtipoVta", query = "SELECT p FROM Precios p WHERE p.preciosPK.ctipoVta = :ctipoVta")
    , @NamedQuery(name = "Precios.findByFrigeDesde", query = "SELECT p FROM Precios p WHERE p.preciosPK.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "Precios.findByFrigeHasta", query = "SELECT p FROM Precios p WHERE p.frigeHasta = :frigeHasta")
    , @NamedQuery(name = "Precios.findByIprecioCaja", query = "SELECT p FROM Precios p WHERE p.iprecioCaja = :iprecioCaja")
    , @NamedQuery(name = "Precios.findByIprecioUnidad", query = "SELECT p FROM Precios p WHERE p.iprecioUnidad = :iprecioUnidad")
    , @NamedQuery(name = "Precios.findByXdescEspecial", query = "SELECT p FROM Precios p WHERE p.xdescEspecial = :xdescEspecial")
    , @NamedQuery(name = "Precios.findByCusuario", query = "SELECT p FROM Precios p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "Precios.findByFalta", query = "SELECT p FROM Precios p WHERE p.falta = :falta")
    , @NamedQuery(name = "Precios.findByCusuarioModif", query = "SELECT p FROM Precios p WHERE p.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Precios.findByFultimModif", query = "SELECT p FROM Precios p WHERE p.fultimModif = :fultimModif")})
public class Precios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PreciosPK preciosPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_caja")
    private long iprecioCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_unidad")
    private long iprecioUnidad;
    @Size(max = 50)
    @Column(name = "xdesc_especial")
    private String xdescEspecial;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
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

    public Precios() {
    }

    public Precios(PreciosPK preciosPK) {
        this.preciosPK = preciosPK;
    }

    public Precios(PreciosPK preciosPK, Date frigeHasta, long iprecioCaja, long iprecioUnidad) {
        this.preciosPK = preciosPK;
        this.frigeHasta = frigeHasta;
        this.iprecioCaja = iprecioCaja;
        this.iprecioUnidad = iprecioUnidad;
    }

    public Precios(short codEmpr, short codDepo, String codMerca, Character ctipoVta, Date frigeDesde) {
        this.preciosPK = new PreciosPK(codEmpr, codDepo, codMerca, ctipoVta, frigeDesde);
    }

    public PreciosPK getPreciosPK() {
        return preciosPK;
    }

    public void setPreciosPK(PreciosPK preciosPK) {
        this.preciosPK = preciosPK;
    }

    public Date getFrigeHasta() {
        return frigeHasta;
    }

    public void setFrigeHasta(Date frigeHasta) {
        this.frigeHasta = frigeHasta;
    }

    public long getIprecioCaja() {
        return iprecioCaja;
    }

    public void setIprecioCaja(long iprecioCaja) {
        this.iprecioCaja = iprecioCaja;
    }

    public long getIprecioUnidad() {
        return iprecioUnidad;
    }

    public void setIprecioUnidad(long iprecioUnidad) {
        this.iprecioUnidad = iprecioUnidad;
    }

    public String getXdescEspecial() {
        return xdescEspecial;
    }

    public void setXdescEspecial(String xdescEspecial) {
        this.xdescEspecial = xdescEspecial;
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

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
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
        hash += (preciosPK != null ? preciosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Precios)) {
            return false;
        }
        Precios other = (Precios) object;
        if ((this.preciosPK == null && other.preciosPK != null) || (this.preciosPK != null && !this.preciosPK.equals(other.preciosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Precios[ preciosPK=" + preciosPK + " ]";
    }
    
}
