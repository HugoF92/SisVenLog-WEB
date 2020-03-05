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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "facturas_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturasDet.findAll", query = "SELECT f FROM FacturasDet f")
    , @NamedQuery(name = "FacturasDet.findByCodEmpr", query = "SELECT f FROM FacturasDet f WHERE f.facturasDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "FacturasDet.findByNrofact", query = "SELECT f FROM FacturasDet f WHERE f.facturasDetPK.nrofact = :nrofact")
    , @NamedQuery(name = "FacturasDet.findByCtipoDocum", query = "SELECT f FROM FacturasDet f WHERE f.facturasDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "FacturasDet.findByCodMerca", query = "SELECT f FROM FacturasDet f WHERE f.facturasDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "FacturasDet.findByXdesc", query = "SELECT f FROM FacturasDet f WHERE f.xdesc = :xdesc")
    , @NamedQuery(name = "FacturasDet.findByCantCajas", query = "SELECT f FROM FacturasDet f WHERE f.cantCajas = :cantCajas")
    , @NamedQuery(name = "FacturasDet.findByCantUnid", query = "SELECT f FROM FacturasDet f WHERE f.cantUnid = :cantUnid")
    , @NamedQuery(name = "FacturasDet.findByIprecioCaja", query = "SELECT f FROM FacturasDet f WHERE f.iprecioCaja = :iprecioCaja")
    , @NamedQuery(name = "FacturasDet.findByIprecioUnidad", query = "SELECT f FROM FacturasDet f WHERE f.iprecioUnidad = :iprecioUnidad")
    , @NamedQuery(name = "FacturasDet.findByPdesc", query = "SELECT f FROM FacturasDet f WHERE f.pdesc = :pdesc")
    , @NamedQuery(name = "FacturasDet.findByIgravadas", query = "SELECT f FROM FacturasDet f WHERE f.igravadas = :igravadas")
    , @NamedQuery(name = "FacturasDet.findByIexentas", query = "SELECT f FROM FacturasDet f WHERE f.iexentas = :iexentas")
    , @NamedQuery(name = "FacturasDet.findByImpuestos", query = "SELECT f FROM FacturasDet f WHERE f.impuestos = :impuestos")
    , @NamedQuery(name = "FacturasDet.findByIdescuentos", query = "SELECT f FROM FacturasDet f WHERE f.idescuentos = :idescuentos")
    , @NamedQuery(name = "FacturasDet.findByItotal", query = "SELECT f FROM FacturasDet f WHERE f.itotal = :itotal")
    , @NamedQuery(name = "FacturasDet.findByInotas", query = "SELECT f FROM FacturasDet f WHERE f.inotas = :inotas")
    , @NamedQuery(name = "FacturasDet.findByNroPromo", query = "SELECT f FROM FacturasDet f WHERE f.nroPromo = :nroPromo")
    , @NamedQuery(name = "FacturasDet.findByCajasBonif", query = "SELECT f FROM FacturasDet f WHERE f.cajasBonif = :cajasBonif")
    , @NamedQuery(name = "FacturasDet.findByUnidBonif", query = "SELECT f FROM FacturasDet f WHERE f.unidBonif = :unidBonif")
    , @NamedQuery(name = "FacturasDet.findByIcajaDif", query = "SELECT f FROM FacturasDet f WHERE f.icajaDif = :icajaDif")
    , @NamedQuery(name = "FacturasDet.findByIunidDif", query = "SELECT f FROM FacturasDet f WHERE f.iunidDif = :iunidDif")
    , @NamedQuery(name = "FacturasDet.findByPimpues", query = "SELECT f FROM FacturasDet f WHERE f.pimpues = :pimpues")
    , @NamedQuery(name = "FacturasDet.findByFfactur", query = "SELECT f FROM FacturasDet f WHERE f.facturasDetPK.ffactur = :ffactur")})
public class FacturasDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacturasDetPK facturasDetPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_cajas")
    private int cantCajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_unid")
    private int cantUnid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_caja")
    private long iprecioCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_unidad")
    private long iprecioUnidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdesc")
    private BigDecimal pdesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igravadas")
    private long igravadas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iexentas")
    private long iexentas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "impuestos")
    private BigDecimal impuestos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idescuentos")
    private long idescuentos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "itotal")
    private long itotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inotas")
    private long inotas;
    @Column(name = "nro_promo")
    private Integer nroPromo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cajas_bonif")
    private long cajasBonif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unid_bonif")
    private long unidBonif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "icaja_dif")
    private long icajaDif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iunid_dif")
    private long iunidDif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pimpues")
    private BigDecimal pimpues;

    public FacturasDet() {
    }

    public FacturasDet(FacturasDetPK facturasDetPK) {
        this.facturasDetPK = facturasDetPK;
    }

    public FacturasDet(FacturasDetPK facturasDetPK, int cantCajas, int cantUnid, long iprecioCaja, long iprecioUnidad, BigDecimal pdesc, long igravadas, long iexentas, BigDecimal impuestos, long idescuentos, long itotal, long inotas, long cajasBonif, long unidBonif, long icajaDif, long iunidDif, BigDecimal pimpues) {
        this.facturasDetPK = facturasDetPK;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.iprecioCaja = iprecioCaja;
        this.iprecioUnidad = iprecioUnidad;
        this.pdesc = pdesc;
        this.igravadas = igravadas;
        this.iexentas = iexentas;
        this.impuestos = impuestos;
        this.idescuentos = idescuentos;
        this.itotal = itotal;
        this.inotas = inotas;
        this.cajasBonif = cajasBonif;
        this.unidBonif = unidBonif;
        this.icajaDif = icajaDif;
        this.iunidDif = iunidDif;
        this.pimpues = pimpues;
    }

    public FacturasDet(short codEmpr, long nrofact, String ctipoDocum, String codMerca, Date ffactur) {
        this.facturasDetPK = new FacturasDetPK(codEmpr, nrofact, ctipoDocum, codMerca, ffactur);
    }

    public FacturasDetPK getFacturasDetPK() {
        return facturasDetPK;
    }

    public void setFacturasDetPK(FacturasDetPK facturasDetPK) {
        this.facturasDetPK = facturasDetPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public int getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(int cantCajas) {
        this.cantCajas = cantCajas;
    }

    public int getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(int cantUnid) {
        this.cantUnid = cantUnid;
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

    public BigDecimal getPdesc() {
        return pdesc;
    }

    public void setPdesc(BigDecimal pdesc) {
        this.pdesc = pdesc;
    }

    public long getIgravadas() {
        return igravadas;
    }

    public void setIgravadas(long igravadas) {
        this.igravadas = igravadas;
    }

    public long getIexentas() {
        return iexentas;
    }

    public void setIexentas(long iexentas) {
        this.iexentas = iexentas;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public long getIdescuentos() {
        return idescuentos;
    }

    public void setIdescuentos(long idescuentos) {
        this.idescuentos = idescuentos;
    }

    public long getItotal() {
        return itotal;
    }

    public void setItotal(long itotal) {
        this.itotal = itotal;
    }

    public long getInotas() {
        return inotas;
    }

    public void setInotas(long inotas) {
        this.inotas = inotas;
    }

    public Integer getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(Integer nroPromo) {
        this.nroPromo = nroPromo;
    }

    public long getCajasBonif() {
        return cajasBonif;
    }

    public void setCajasBonif(long cajasBonif) {
        this.cajasBonif = cajasBonif;
    }

    public long getUnidBonif() {
        return unidBonif;
    }

    public void setUnidBonif(long unidBonif) {
        this.unidBonif = unidBonif;
    }

    public long getIcajaDif() {
        return icajaDif;
    }

    public void setIcajaDif(long icajaDif) {
        this.icajaDif = icajaDif;
    }

    public long getIunidDif() {
        return iunidDif;
    }

    public void setIunidDif(long iunidDif) {
        this.iunidDif = iunidDif;
    }

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturasDetPK != null ? facturasDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturasDet)) {
            return false;
        }
        FacturasDet other = (FacturasDet) object;
        if ((this.facturasDetPK == null && other.facturasDetPK != null) || (this.facturasDetPK != null && !this.facturasDetPK.equals(other.facturasDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.FacturasDet[ facturasDetPK=" + facturasDetPK + " ]";
    }
    
}
