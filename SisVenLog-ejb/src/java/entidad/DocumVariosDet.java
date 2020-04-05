/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "docum_varios_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumVariosDet.findAll", query = "SELECT d FROM DocumVariosDet d")
    , @NamedQuery(name = "DocumVariosDet.findByCodEmpr", query = "SELECT d FROM DocumVariosDet d WHERE d.documVariosDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "DocumVariosDet.findByCtipoDocum", query = "SELECT d FROM DocumVariosDet d WHERE d.documVariosDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "DocumVariosDet.findByNdocum", query = "SELECT d FROM DocumVariosDet d WHERE d.documVariosDetPK.ndocum = :ndocum")
    , @NamedQuery(name = "DocumVariosDet.findByCodMerca", query = "SELECT d FROM DocumVariosDet d WHERE d.documVariosDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "DocumVariosDet.findByXdesc", query = "SELECT d FROM DocumVariosDet d WHERE d.xdesc = :xdesc")
    , @NamedQuery(name = "DocumVariosDet.findByCantCajas", query = "SELECT d FROM DocumVariosDet d WHERE d.cantCajas = :cantCajas")
    , @NamedQuery(name = "DocumVariosDet.findByCantUnid", query = "SELECT d FROM DocumVariosDet d WHERE d.cantUnid = :cantUnid")
    , @NamedQuery(name = "DocumVariosDet.findByIexentas", query = "SELECT d FROM DocumVariosDet d WHERE d.iexentas = :iexentas")
    , @NamedQuery(name = "DocumVariosDet.findByIgravadas", query = "SELECT d FROM DocumVariosDet d WHERE d.igravadas = :igravadas")
    , @NamedQuery(name = "DocumVariosDet.findByImpuestos", query = "SELECT d FROM DocumVariosDet d WHERE d.impuestos = :impuestos")
    , @NamedQuery(name = "DocumVariosDet.findByCajasFis", query = "SELECT d FROM DocumVariosDet d WHERE d.cajasFis = :cajasFis")
    , @NamedQuery(name = "DocumVariosDet.findByUnidFis", query = "SELECT d FROM DocumVariosDet d WHERE d.unidFis = :unidFis")
    , @NamedQuery(name = "DocumVariosDet.findByPimpues", query = "SELECT d FROM DocumVariosDet d WHERE d.pimpues = :pimpues")
    , @NamedQuery(name = "DocumVariosDet.findByItotal", query = "SELECT d FROM DocumVariosDet d WHERE d.itotal = :itotal")})
public class DocumVariosDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumVariosDetPK documVariosDetPK;
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
    @Column(name = "iexentas")
    private Long iexentas;
    @Column(name = "igravadas")
    private Long igravadas;
    @Column(name = "impuestos")
    private Long impuestos;
    @Column(name = "cajas_fis")
    private Long cajasFis;
    @Column(name = "unid_fis")
    private Long unidFis;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pimpues")
    private BigDecimal pimpues;
    @Column(name = "itotal")
    private Long itotal;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "ctipo_docum", referencedColumnName = "ctipo_docum", insertable = false, updatable = false)
        , @JoinColumn(name = "ndocum", referencedColumnName = "ndocum", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private DocumVarios documVarios;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_promo", referencedColumnName = "nro_promo")})
    @ManyToOne(optional = false)
    private Promociones promociones;

    public DocumVariosDet() {
    }

    public DocumVariosDet(DocumVariosDetPK documVariosDetPK) {
        this.documVariosDetPK = documVariosDetPK;
    }

    public DocumVariosDet(DocumVariosDetPK documVariosDetPK, long cantCajas, long cantUnid, BigDecimal pimpues) {
        this.documVariosDetPK = documVariosDetPK;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.pimpues = pimpues;
    }

    public DocumVariosDet(short codEmpr, String ctipoDocum, int ndocum, String codMerca) {
        this.documVariosDetPK = new DocumVariosDetPK(codEmpr, ctipoDocum, ndocum, codMerca);
    }

    public DocumVariosDetPK getDocumVariosDetPK() {
        return documVariosDetPK;
    }

    public void setDocumVariosDetPK(DocumVariosDetPK documVariosDetPK) {
        this.documVariosDetPK = documVariosDetPK;
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

    public Long getIexentas() {
        return iexentas;
    }

    public void setIexentas(Long iexentas) {
        this.iexentas = iexentas;
    }

    public Long getIgravadas() {
        return igravadas;
    }

    public void setIgravadas(Long igravadas) {
        this.igravadas = igravadas;
    }

    public Long getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Long impuestos) {
        this.impuestos = impuestos;
    }

    public Long getCajasFis() {
        return cajasFis;
    }

    public void setCajasFis(Long cajasFis) {
        this.cajasFis = cajasFis;
    }

    public Long getUnidFis() {
        return unidFis;
    }

    public void setUnidFis(Long unidFis) {
        this.unidFis = unidFis;
    }

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
    }

    public Long getItotal() {
        return itotal;
    }

    public void setItotal(Long itotal) {
        this.itotal = itotal;
    }

    public DocumVarios getDocumVarios() {
        return documVarios;
    }

    public void setDocumVarios(DocumVarios documVarios) {
        this.documVarios = documVarios;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    public Promociones getPromociones() {
        return promociones;
    }

    public void setPromociones(Promociones promociones) {
        this.promociones = promociones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documVariosDetPK != null ? documVariosDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumVariosDet)) {
            return false;
        }
        DocumVariosDet other = (DocumVariosDet) object;
        if ((this.documVariosDetPK == null && other.documVariosDetPK != null) || (this.documVariosDetPK != null && !this.documVariosDetPK.equals(other.documVariosDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DocumVariosDet[ documVariosDetPK=" + documVariosDetPK + " ]";
    }
    
}
