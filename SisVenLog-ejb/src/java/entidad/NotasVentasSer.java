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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "notas_ventas_ser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotasVentasSer.findAll", query = "SELECT n FROM NotasVentasSer n")
    , @NamedQuery(name = "NotasVentasSer.findByCodEmpr", query = "SELECT n FROM NotasVentasSer n WHERE n.notasVentasSerPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "NotasVentasSer.findByCtipoDocum", query = "SELECT n FROM NotasVentasSer n WHERE n.notasVentasSerPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "NotasVentasSer.findByNroNota", query = "SELECT n FROM NotasVentasSer n WHERE n.notasVentasSerPK.nroNota = :nroNota")
    , @NamedQuery(name = "NotasVentasSer.findByCodServicio", query = "SELECT n FROM NotasVentasSer n WHERE n.notasVentasSerPK.codServicio = :codServicio")
    , @NamedQuery(name = "NotasVentasSer.findByIgravadas", query = "SELECT n FROM NotasVentasSer n WHERE n.igravadas = :igravadas")
    , @NamedQuery(name = "NotasVentasSer.findByIexentas", query = "SELECT n FROM NotasVentasSer n WHERE n.iexentas = :iexentas")
    , @NamedQuery(name = "NotasVentasSer.findByImpuestos", query = "SELECT n FROM NotasVentasSer n WHERE n.impuestos = :impuestos")
    , @NamedQuery(name = "NotasVentasSer.findByItotal", query = "SELECT n FROM NotasVentasSer n WHERE n.itotal = :itotal")
    , @NamedQuery(name = "NotasVentasSer.findByPimpues", query = "SELECT n FROM NotasVentasSer n WHERE n.pimpues = :pimpues")
    , @NamedQuery(name = "NotasVentasSer.findByFdocum", query = "SELECT n FROM NotasVentasSer n WHERE n.notasVentasSerPK.fdocum = :fdocum")})
public class NotasVentasSer implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotasVentasSerPK notasVentasSerPK;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "itotal")
    private long itotal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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

    public NotasVentasSer() {
    }

    public NotasVentasSer(NotasVentasSerPK notasVentasSerPK) {
        this.notasVentasSerPK = notasVentasSerPK;
    }

    public NotasVentasSer(NotasVentasSerPK notasVentasSerPK, long igravadas, long iexentas, long impuestos, long itotal, BigDecimal pimpues) {
        this.notasVentasSerPK = notasVentasSerPK;
        this.igravadas = igravadas;
        this.iexentas = iexentas;
        this.impuestos = impuestos;
        this.itotal = itotal;
        this.pimpues = pimpues;
    }

    public NotasVentasSer(short codEmpr, String ctipoDocum, long nroNota, long codServicio, Date fdocum) {
        this.notasVentasSerPK = new NotasVentasSerPK(codEmpr, ctipoDocum, nroNota, codServicio, fdocum);
    }

    public NotasVentasSerPK getNotasVentasSerPK() {
        return notasVentasSerPK;
    }

    public void setNotasVentasSerPK(NotasVentasSerPK notasVentasSerPK) {
        this.notasVentasSerPK = notasVentasSerPK;
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

    public long getItotal() {
        return itotal;
    }

    public void setItotal(long itotal) {
        this.itotal = itotal;
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
        hash += (notasVentasSerPK != null ? notasVentasSerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotasVentasSer)) {
            return false;
        }
        NotasVentasSer other = (NotasVentasSer) object;
        if ((this.notasVentasSerPK == null && other.notasVentasSerPK != null) || (this.notasVentasSerPK != null && !this.notasVentasSerPK.equals(other.notasVentasSerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.NotasVentasSer[ notasVentasSerPK=" + notasVentasSerPK + " ]";
    }
    
}
