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
@Table(name = "notas_compras_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotasComprasDet.findAll", query = "SELECT n FROM NotasComprasDet n")
    , @NamedQuery(name = "NotasComprasDet.findByCodEmpr", query = "SELECT n FROM NotasComprasDet n WHERE n.notasComprasDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "NotasComprasDet.findByCodProveed", query = "SELECT n FROM NotasComprasDet n WHERE n.notasComprasDetPK.codProveed = :codProveed")
    , @NamedQuery(name = "NotasComprasDet.findByCtipoDocum", query = "SELECT n FROM NotasComprasDet n WHERE n.notasComprasDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "NotasComprasDet.findByNroNota", query = "SELECT n FROM NotasComprasDet n WHERE n.notasComprasDetPK.nroNota = :nroNota")
    , @NamedQuery(name = "NotasComprasDet.findByCodMerca", query = "SELECT n FROM NotasComprasDet n WHERE n.notasComprasDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "NotasComprasDet.findByXdesc", query = "SELECT n FROM NotasComprasDet n WHERE n.xdesc = :xdesc")
    , @NamedQuery(name = "NotasComprasDet.findByCantCajas", query = "SELECT n FROM NotasComprasDet n WHERE n.cantCajas = :cantCajas")
    , @NamedQuery(name = "NotasComprasDet.findByCantUnid", query = "SELECT n FROM NotasComprasDet n WHERE n.cantUnid = :cantUnid")
    , @NamedQuery(name = "NotasComprasDet.findByIgravadas", query = "SELECT n FROM NotasComprasDet n WHERE n.igravadas = :igravadas")
    , @NamedQuery(name = "NotasComprasDet.findByIexentas", query = "SELECT n FROM NotasComprasDet n WHERE n.iexentas = :iexentas")
    , @NamedQuery(name = "NotasComprasDet.findByImpuestos", query = "SELECT n FROM NotasComprasDet n WHERE n.impuestos = :impuestos")
    , @NamedQuery(name = "NotasComprasDet.findByPdesc", query = "SELECT n FROM NotasComprasDet n WHERE n.pdesc = :pdesc")
    , @NamedQuery(name = "NotasComprasDet.findByIprecioCaja", query = "SELECT n FROM NotasComprasDet n WHERE n.iprecioCaja = :iprecioCaja")
    , @NamedQuery(name = "NotasComprasDet.findByIprecioUnid", query = "SELECT n FROM NotasComprasDet n WHERE n.iprecioUnid = :iprecioUnid")
    , @NamedQuery(name = "NotasComprasDet.findByPimpues", query = "SELECT n FROM NotasComprasDet n WHERE n.pimpues = :pimpues")
    , @NamedQuery(name = "NotasComprasDet.findByFdocum", query = "SELECT n FROM NotasComprasDet n WHERE n.notasComprasDetPK.fdocum = :fdocum")})
public class NotasComprasDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotasComprasDetPK notasComprasDetPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
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
    @Column(name = "igravadas")
    private long igravadas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iexentas")
    private long iexentas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "impuestos")
    private long impuestos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdesc")
    private BigDecimal pdesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_caja")
    private long iprecioCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_unid")
    private long iprecioUnid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pimpues")
    private BigDecimal pimpues;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_nota", referencedColumnName = "nro_nota", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
        , @JoinColumn(name = "ctipo_docum", referencedColumnName = "ctipo_docum", insertable = false, updatable = false)
        , @JoinColumn(name = "fdocum", referencedColumnName = "fdocum", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private NotasCompras notasCompras;

    public NotasComprasDet() {
    }

    public NotasComprasDet(NotasComprasDetPK notasComprasDetPK) {
        this.notasComprasDetPK = notasComprasDetPK;
    }

    public NotasComprasDet(NotasComprasDetPK notasComprasDetPK, String xdesc, int cantCajas, int cantUnid, long igravadas, long iexentas, long impuestos, BigDecimal pdesc, long iprecioCaja, long iprecioUnid, BigDecimal pimpues) {
        this.notasComprasDetPK = notasComprasDetPK;
        this.xdesc = xdesc;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.igravadas = igravadas;
        this.iexentas = iexentas;
        this.impuestos = impuestos;
        this.pdesc = pdesc;
        this.iprecioCaja = iprecioCaja;
        this.iprecioUnid = iprecioUnid;
        this.pimpues = pimpues;
    }

    public NotasComprasDet(short codEmpr, short codProveed, String ctipoDocum, long nroNota, String codMerca, Date fdocum) {
        this.notasComprasDetPK = new NotasComprasDetPK(codEmpr, codProveed, ctipoDocum, nroNota, codMerca, fdocum);
    }

    public NotasComprasDetPK getNotasComprasDetPK() {
        return notasComprasDetPK;
    }

    public void setNotasComprasDetPK(NotasComprasDetPK notasComprasDetPK) {
        this.notasComprasDetPK = notasComprasDetPK;
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

    public long getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(long impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getPdesc() {
        return pdesc;
    }

    public void setPdesc(BigDecimal pdesc) {
        this.pdesc = pdesc;
    }

    public long getIprecioCaja() {
        return iprecioCaja;
    }

    public void setIprecioCaja(long iprecioCaja) {
        this.iprecioCaja = iprecioCaja;
    }

    public long getIprecioUnid() {
        return iprecioUnid;
    }

    public void setIprecioUnid(long iprecioUnid) {
        this.iprecioUnid = iprecioUnid;
    }

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
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

    public NotasCompras getNotasCompras() {
        return notasCompras;
    }

    public void setNotasCompras(NotasCompras notasCompras) {
        this.notasCompras = notasCompras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notasComprasDetPK != null ? notasComprasDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotasComprasDet)) {
            return false;
        }
        NotasComprasDet other = (NotasComprasDet) object;
        if ((this.notasComprasDetPK == null && other.notasComprasDetPK != null) || (this.notasComprasDetPK != null && !this.notasComprasDetPK.equals(other.notasComprasDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.NotasComprasDet[ notasComprasDetPK=" + notasComprasDetPK + " ]";
    }
    
}
