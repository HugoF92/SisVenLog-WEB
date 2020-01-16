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
import javax.persistence.ManyToOne;
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
@Table(name = "compras_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComprasDet.findAll", query = "SELECT c FROM ComprasDet c")
    , @NamedQuery(name = "ComprasDet.findByCodEmpr", query = "SELECT c FROM ComprasDet c WHERE c.comprasDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "ComprasDet.findByCtipoDocum", query = "SELECT c FROM ComprasDet c WHERE c.comprasDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "ComprasDet.findByNrofact", query = "SELECT c FROM ComprasDet c WHERE c.comprasDetPK.nrofact = :nrofact")
    , @NamedQuery(name = "ComprasDet.findByCodProveed", query = "SELECT c FROM ComprasDet c WHERE c.comprasDetPK.codProveed = :codProveed")
    , @NamedQuery(name = "ComprasDet.findByCodMerca", query = "SELECT c FROM ComprasDet c WHERE c.comprasDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "ComprasDet.findByXdesc", query = "SELECT c FROM ComprasDet c WHERE c.xdesc = :xdesc")
    , @NamedQuery(name = "ComprasDet.findByCantCajas", query = "SELECT c FROM ComprasDet c WHERE c.cantCajas = :cantCajas")
    , @NamedQuery(name = "ComprasDet.findByCantUnid", query = "SELECT c FROM ComprasDet c WHERE c.cantUnid = :cantUnid")
    , @NamedQuery(name = "ComprasDet.findByIexentas", query = "SELECT c FROM ComprasDet c WHERE c.iexentas = :iexentas")
    , @NamedQuery(name = "ComprasDet.findByIgravadas", query = "SELECT c FROM ComprasDet c WHERE c.igravadas = :igravadas")
    , @NamedQuery(name = "ComprasDet.findByImpuestos", query = "SELECT c FROM ComprasDet c WHERE c.impuestos = :impuestos")
    , @NamedQuery(name = "ComprasDet.findByIdescuentos", query = "SELECT c FROM ComprasDet c WHERE c.idescuentos = :idescuentos")
    , @NamedQuery(name = "ComprasDet.findByIprecioUnid", query = "SELECT c FROM ComprasDet c WHERE c.iprecioUnid = :iprecioUnid")
    , @NamedQuery(name = "ComprasDet.findByIprecioCaja", query = "SELECT c FROM ComprasDet c WHERE c.iprecioCaja = :iprecioCaja")
    , @NamedQuery(name = "ComprasDet.findByItotal", query = "SELECT c FROM ComprasDet c WHERE c.itotal = :itotal")
    , @NamedQuery(name = "ComprasDet.findByPdesc", query = "SELECT c FROM ComprasDet c WHERE c.pdesc = :pdesc")
    , @NamedQuery(name = "ComprasDet.findByInotas", query = "SELECT c FROM ComprasDet c WHERE c.inotas = :inotas")
    , @NamedQuery(name = "ComprasDet.findByPimpues", query = "SELECT c FROM ComprasDet c WHERE c.pimpues = :pimpues")
    , @NamedQuery(name = "ComprasDet.findByFfactur", query = "SELECT c FROM ComprasDet c WHERE c.comprasDetPK.ffactur = :ffactur")
    , @NamedQuery(name = "ComprasDet.findByNorden", query = "SELECT c FROM ComprasDet c WHERE c.norden = :norden")})
public class ComprasDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComprasDetPK comprasDetPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_cajas")
    private long cantCajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_unid")
    private long cantUnid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iexentas")
    private long iexentas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igravadas")
    private long igravadas;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @Column(name = "iprecio_unid")
    private BigDecimal iprecioUnid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_caja")
    private BigDecimal iprecioCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "itotal")
    private long itotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdesc")
    private BigDecimal pdesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inotas")
    private long inotas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pimpues")
    private BigDecimal pimpues;
    @Column(name = "norden")
    private Integer norden;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "ctipo_docum", referencedColumnName = "ctipo_docum", insertable = false, updatable = false)
        , @JoinColumn(name = "nrofact", referencedColumnName = "nrofact", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
        , @JoinColumn(name = "ffactur", referencedColumnName = "ffactur", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Compras compras;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proveedores proveedores;

    public ComprasDet() {
    }

    public ComprasDet(ComprasDetPK comprasDetPK) {
        this.comprasDetPK = comprasDetPK;
    }

    public ComprasDet(ComprasDetPK comprasDetPK, long cantCajas, long cantUnid, long iexentas, long igravadas, BigDecimal impuestos, long idescuentos, BigDecimal iprecioUnid, BigDecimal iprecioCaja, long itotal, BigDecimal pdesc, long inotas, BigDecimal pimpues) {
        this.comprasDetPK = comprasDetPK;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.iexentas = iexentas;
        this.igravadas = igravadas;
        this.impuestos = impuestos;
        this.idescuentos = idescuentos;
        this.iprecioUnid = iprecioUnid;
        this.iprecioCaja = iprecioCaja;
        this.itotal = itotal;
        this.pdesc = pdesc;
        this.inotas = inotas;
        this.pimpues = pimpues;
    }

    public ComprasDet(short codEmpr, String ctipoDocum, long nrofact, short codProveed, String codMerca, Date ffactur) {
        this.comprasDetPK = new ComprasDetPK(codEmpr, ctipoDocum, nrofact, codProveed, codMerca, ffactur);
    }

    public ComprasDetPK getComprasDetPK() {
        return comprasDetPK;
    }

    public void setComprasDetPK(ComprasDetPK comprasDetPK) {
        this.comprasDetPK = comprasDetPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public long getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(long cantCajas) {
        this.cantCajas = cantCajas;
    }

    public long getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(long cantUnid) {
        this.cantUnid = cantUnid;
    }

    public long getIexentas() {
        return iexentas;
    }

    public void setIexentas(long iexentas) {
        this.iexentas = iexentas;
    }

    public long getIgravadas() {
        return igravadas;
    }

    public void setIgravadas(long igravadas) {
        this.igravadas = igravadas;
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

    public BigDecimal getIprecioUnid() {
        return iprecioUnid;
    }

    public void setIprecioUnid(BigDecimal iprecioUnid) {
        this.iprecioUnid = iprecioUnid;
    }

    public BigDecimal getIprecioCaja() {
        return iprecioCaja;
    }

    public void setIprecioCaja(BigDecimal iprecioCaja) {
        this.iprecioCaja = iprecioCaja;
    }

    public long getItotal() {
        return itotal;
    }

    public void setItotal(long itotal) {
        this.itotal = itotal;
    }

    public BigDecimal getPdesc() {
        return pdesc;
    }

    public void setPdesc(BigDecimal pdesc) {
        this.pdesc = pdesc;
    }

    public long getInotas() {
        return inotas;
    }

    public void setInotas(long inotas) {
        this.inotas = inotas;
    }

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
    }

    public Integer getNorden() {
        return norden;
    }

    public void setNorden(Integer norden) {
        this.norden = norden;
    }

    public Compras getCompras() {
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comprasDetPK != null ? comprasDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComprasDet)) {
            return false;
        }
        ComprasDet other = (ComprasDet) object;
        if ((this.comprasDetPK == null && other.comprasDetPK != null) || (this.comprasDetPK != null && !this.comprasDetPK.equals(other.comprasDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ComprasDet[ comprasDetPK=" + comprasDetPK + " ]";
    }
    
}
