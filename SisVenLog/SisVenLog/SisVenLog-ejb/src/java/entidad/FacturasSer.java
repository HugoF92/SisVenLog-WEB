/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "facturas_ser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturasSer.findAll", query = "SELECT f FROM FacturasSer f")
    , @NamedQuery(name = "FacturasSer.findByCodEmpr", query = "SELECT f FROM FacturasSer f WHERE f.facturasSerPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "FacturasSer.findByNrofact", query = "SELECT f FROM FacturasSer f WHERE f.facturasSerPK.nrofact = :nrofact")
    , @NamedQuery(name = "FacturasSer.findByCtipoDocum", query = "SELECT f FROM FacturasSer f WHERE f.facturasSerPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "FacturasSer.findByFfactur", query = "SELECT f FROM FacturasSer f WHERE f.facturasSerPK.ffactur = :ffactur")
    , @NamedQuery(name = "FacturasSer.findByCodServicio", query = "SELECT f FROM FacturasSer f WHERE f.facturasSerPK.codServicio = :codServicio")
    , @NamedQuery(name = "FacturasSer.findByIexentas", query = "SELECT f FROM FacturasSer f WHERE f.iexentas = :iexentas")
    , @NamedQuery(name = "FacturasSer.findByIgravadas", query = "SELECT f FROM FacturasSer f WHERE f.igravadas = :igravadas")
    , @NamedQuery(name = "FacturasSer.findByIdescuentos", query = "SELECT f FROM FacturasSer f WHERE f.idescuentos = :idescuentos")
    , @NamedQuery(name = "FacturasSer.findByItotal", query = "SELECT f FROM FacturasSer f WHERE f.itotal = :itotal")
    , @NamedQuery(name = "FacturasSer.findByImpuestos", query = "SELECT f FROM FacturasSer f WHERE f.impuestos = :impuestos")
    , @NamedQuery(name = "FacturasSer.findByPimpues", query = "SELECT f FROM FacturasSer f WHERE f.pimpues = :pimpues")})
public class FacturasSer implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacturasSerPK facturasSerPK;
    @Column(name = "iexentas")
    private Long iexentas;
    @Column(name = "igravadas")
    private Long igravadas;
    @Column(name = "idescuentos")
    private Long idescuentos;
    @Column(name = "itotal")
    private Long itotal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "impuestos")
    private BigDecimal impuestos;
    @Column(name = "pimpues")
    private BigDecimal pimpues;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nrofact", referencedColumnName = "nrofact", insertable = false, updatable = false)
        , @JoinColumn(name = "ctipo_docum", referencedColumnName = "ctipo_docum", insertable = false, updatable = false)
        , @JoinColumn(name = "ffactur", referencedColumnName = "ffactur", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Facturas facturas;

    public FacturasSer() {
    }

    public FacturasSer(FacturasSerPK facturasSerPK) {
        this.facturasSerPK = facturasSerPK;
    }

    public FacturasSer(short codEmpr, long nrofact, String ctipoDocum, Date ffactur, short codServicio) {
        this.facturasSerPK = new FacturasSerPK(codEmpr, nrofact, ctipoDocum, ffactur, codServicio);
    }

    public FacturasSerPK getFacturasSerPK() {
        return facturasSerPK;
    }

    public void setFacturasSerPK(FacturasSerPK facturasSerPK) {
        this.facturasSerPK = facturasSerPK;
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

    public Long getIdescuentos() {
        return idescuentos;
    }

    public void setIdescuentos(Long idescuentos) {
        this.idescuentos = idescuentos;
    }

    public Long getItotal() {
        return itotal;
    }

    public void setItotal(Long itotal) {
        this.itotal = itotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
    }

    public Facturas getFacturas() {
        return facturas;
    }

    public void setFacturas(Facturas facturas) {
        this.facturas = facturas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturasSerPK != null ? facturasSerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturasSer)) {
            return false;
        }
        FacturasSer other = (FacturasSer) object;
        if ((this.facturasSerPK == null && other.facturasSerPK != null) || (this.facturasSerPK != null && !this.facturasSerPK.equals(other.facturasSerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.FacturasSer[ facturasSerPK=" + facturasSerPK + " ]";
    }
    
}
