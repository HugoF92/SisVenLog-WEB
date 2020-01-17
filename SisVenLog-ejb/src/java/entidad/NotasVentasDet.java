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
@Table(name = "notas_ventas_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotasVentasDet.findAll", query = "SELECT n FROM NotasVentasDet n")
    , @NamedQuery(name = "NotasVentasDet.findByCodEmpr", query = "SELECT n FROM NotasVentasDet n WHERE n.notasVentasDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "NotasVentasDet.findByCtipoDocum", query = "SELECT n FROM NotasVentasDet n WHERE n.notasVentasDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "NotasVentasDet.findByNroNota", query = "SELECT n FROM NotasVentasDet n WHERE n.notasVentasDetPK.nroNota = :nroNota")
    , @NamedQuery(name = "NotasVentasDet.findByCodMerca", query = "SELECT n FROM NotasVentasDet n WHERE n.notasVentasDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "NotasVentasDet.findByXdesc", query = "SELECT n FROM NotasVentasDet n WHERE n.xdesc = :xdesc")
    , @NamedQuery(name = "NotasVentasDet.findByPdesc", query = "SELECT n FROM NotasVentasDet n WHERE n.pdesc = :pdesc")
    , @NamedQuery(name = "NotasVentasDet.findByCantCajas", query = "SELECT n FROM NotasVentasDet n WHERE n.cantCajas = :cantCajas")
    , @NamedQuery(name = "NotasVentasDet.findByCantUnid", query = "SELECT n FROM NotasVentasDet n WHERE n.cantUnid = :cantUnid")
    , @NamedQuery(name = "NotasVentasDet.findByIexentas", query = "SELECT n FROM NotasVentasDet n WHERE n.iexentas = :iexentas")
    , @NamedQuery(name = "NotasVentasDet.findByIgravadas", query = "SELECT n FROM NotasVentasDet n WHERE n.igravadas = :igravadas")
    , @NamedQuery(name = "NotasVentasDet.findByImpuestos", query = "SELECT n FROM NotasVentasDet n WHERE n.impuestos = :impuestos")
    , @NamedQuery(name = "NotasVentasDet.findByIprecioCaja", query = "SELECT n FROM NotasVentasDet n WHERE n.iprecioCaja = :iprecioCaja")
    , @NamedQuery(name = "NotasVentasDet.findByIprecioUnid", query = "SELECT n FROM NotasVentasDet n WHERE n.iprecioUnid = :iprecioUnid")
    , @NamedQuery(name = "NotasVentasDet.findByPimpues", query = "SELECT n FROM NotasVentasDet n WHERE n.pimpues = :pimpues")
    , @NamedQuery(name = "NotasVentasDet.findByFdocum", query = "SELECT n FROM NotasVentasDet n WHERE n.notasVentasDetPK.fdocum = :fdocum")})
public class NotasVentasDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotasVentasDetPK notasVentasDetPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdesc")
    private BigDecimal pdesc;
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
    @Column(name = "iexentas")
    private long iexentas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igravadas")
    private long igravadas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "impuestos")
    private BigDecimal impuestos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_caja")
    private BigDecimal iprecioCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iprecio_unid")
    private BigDecimal iprecioUnid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pimpues")
    private BigDecimal pimpues;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_nota", referencedColumnName = "nro_nota", insertable = false, updatable = false)
        , @JoinColumn(name = "ctipo_docum", referencedColumnName = "ctipo_docum", insertable = false, updatable = false)
        , @JoinColumn(name = "fdocum", referencedColumnName = "fdocum", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private NotasVentas notasVentas;

    public NotasVentasDet() {
    }

    public NotasVentasDet(NotasVentasDetPK notasVentasDetPK) {
        this.notasVentasDetPK = notasVentasDetPK;
    }

    public NotasVentasDet(NotasVentasDetPK notasVentasDetPK, String xdesc, BigDecimal pdesc, int cantCajas, int cantUnid, long iexentas, long igravadas, BigDecimal impuestos, BigDecimal iprecioCaja, BigDecimal iprecioUnid, BigDecimal pimpues) {
        this.notasVentasDetPK = notasVentasDetPK;
        this.xdesc = xdesc;
        this.pdesc = pdesc;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.iexentas = iexentas;
        this.igravadas = igravadas;
        this.impuestos = impuestos;
        this.iprecioCaja = iprecioCaja;
        this.iprecioUnid = iprecioUnid;
        this.pimpues = pimpues;
    }

    public NotasVentasDet(short codEmpr, String ctipoDocum, long nroNota, String codMerca, Date fdocum) {
        this.notasVentasDetPK = new NotasVentasDetPK(codEmpr, ctipoDocum, nroNota, codMerca, fdocum);
    }

    public NotasVentasDetPK getNotasVentasDetPK() {
        return notasVentasDetPK;
    }

    public void setNotasVentasDetPK(NotasVentasDetPK notasVentasDetPK) {
        this.notasVentasDetPK = notasVentasDetPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public BigDecimal getPdesc() {
        return pdesc;
    }

    public void setPdesc(BigDecimal pdesc) {
        this.pdesc = pdesc;
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

    public BigDecimal getIprecioCaja() {
        return iprecioCaja;
    }

    public void setIprecioCaja(BigDecimal iprecioCaja) {
        this.iprecioCaja = iprecioCaja;
    }

    public BigDecimal getIprecioUnid() {
        return iprecioUnid;
    }

    public void setIprecioUnid(BigDecimal iprecioUnid) {
        this.iprecioUnid = iprecioUnid;
    }

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
    }

    public NotasVentas getNotasVentas() {
        return notasVentas;
    }

    public void setNotasVentas(NotasVentas notasVentas) {
        this.notasVentas = notasVentas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notasVentasDetPK != null ? notasVentasDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotasVentasDet)) {
            return false;
        }
        NotasVentasDet other = (NotasVentasDet) object;
        if ((this.notasVentasDetPK == null && other.notasVentasDetPK != null) || (this.notasVentasDetPK != null && !this.notasVentasDetPK.equals(other.notasVentasDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.NotasVentasDet[ notasVentasDetPK=" + notasVentasDetPK + " ]";
    }
    
}
