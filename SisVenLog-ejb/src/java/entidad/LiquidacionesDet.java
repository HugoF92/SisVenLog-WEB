/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "liquidaciones_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LiquidacionesDet.findAll", query = "SELECT l FROM LiquidacionesDet l")
    , @NamedQuery(name = "LiquidacionesDet.findByCodEmpr", query = "SELECT l FROM LiquidacionesDet l WHERE l.liquidacionesDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "LiquidacionesDet.findByNroLiquid", query = "SELECT l FROM LiquidacionesDet l WHERE l.liquidacionesDetPK.nroLiquid = :nroLiquid")
    , @NamedQuery(name = "LiquidacionesDet.findByNitem", query = "SELECT l FROM LiquidacionesDet l WHERE l.liquidacionesDetPK.nitem = :nitem")
    , @NamedQuery(name = "LiquidacionesDet.findByNroFactura", query = "SELECT l FROM LiquidacionesDet l WHERE l.nroFactura = :nroFactura")
    , @NamedQuery(name = "LiquidacionesDet.findByNtoneladasVtas", query = "SELECT l FROM LiquidacionesDet l WHERE l.ntoneladasVtas = :ntoneladasVtas")
    , @NamedQuery(name = "LiquidacionesDet.findByNcajasVtas", query = "SELECT l FROM LiquidacionesDet l WHERE l.ncajasVtas = :ncajasVtas")
    , @NamedQuery(name = "LiquidacionesDet.findByItotalVtas", query = "SELECT l FROM LiquidacionesDet l WHERE l.itotalVtas = :itotalVtas")
    , @NamedQuery(name = "LiquidacionesDet.findByPcumplido", query = "SELECT l FROM LiquidacionesDet l WHERE l.pcumplido = :pcumplido")
    , @NamedQuery(name = "LiquidacionesDet.findByItotalCumplido", query = "SELECT l FROM LiquidacionesDet l WHERE l.itotalCumplido = :itotalCumplido")
    , @NamedQuery(name = "LiquidacionesDet.findByPcomis", query = "SELECT l FROM LiquidacionesDet l WHERE l.pcomis = :pcomis")
    , @NamedQuery(name = "LiquidacionesDet.findByIcomis", query = "SELECT l FROM LiquidacionesDet l WHERE l.icomis = :icomis")})
public class LiquidacionesDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LiquidacionesDetPK liquidacionesDetPK;
    @Column(name = "nro_factura")
    private Integer nroFactura;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ntoneladas_vtas")
    private BigDecimal ntoneladasVtas;
    @Column(name = "ncajas_vtas")
    private Integer ncajasVtas;
    @Column(name = "itotal_vtas")
    private Long itotalVtas;
    @Column(name = "pcumplido")
    private BigDecimal pcumplido;
    @Column(name = "itotal_cumplido")
    private Long itotalCumplido;
    @Column(name = "pcomis")
    private BigDecimal pcomis;
    @Column(name = "icomis")
    private Long icomis;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_liquid", referencedColumnName = "nro_liquid", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Liquidaciones liquidaciones;
    @JoinColumn(name = "cod_sublinea", referencedColumnName = "cod_sublinea")
    @ManyToOne
    private Sublineas codSublinea;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona")})
    @ManyToOne(optional = false)
    private Zonas zonas;

    public LiquidacionesDet() {
    }

    public LiquidacionesDet(LiquidacionesDetPK liquidacionesDetPK) {
        this.liquidacionesDetPK = liquidacionesDetPK;
    }

    public LiquidacionesDet(short codEmpr, int nroLiquid, short nitem) {
        this.liquidacionesDetPK = new LiquidacionesDetPK(codEmpr, nroLiquid, nitem);
    }

    public LiquidacionesDetPK getLiquidacionesDetPK() {
        return liquidacionesDetPK;
    }

    public void setLiquidacionesDetPK(LiquidacionesDetPK liquidacionesDetPK) {
        this.liquidacionesDetPK = liquidacionesDetPK;
    }

    public Integer getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(Integer nroFactura) {
        this.nroFactura = nroFactura;
    }

    public BigDecimal getNtoneladasVtas() {
        return ntoneladasVtas;
    }

    public void setNtoneladasVtas(BigDecimal ntoneladasVtas) {
        this.ntoneladasVtas = ntoneladasVtas;
    }

    public Integer getNcajasVtas() {
        return ncajasVtas;
    }

    public void setNcajasVtas(Integer ncajasVtas) {
        this.ncajasVtas = ncajasVtas;
    }

    public Long getItotalVtas() {
        return itotalVtas;
    }

    public void setItotalVtas(Long itotalVtas) {
        this.itotalVtas = itotalVtas;
    }

    public BigDecimal getPcumplido() {
        return pcumplido;
    }

    public void setPcumplido(BigDecimal pcumplido) {
        this.pcumplido = pcumplido;
    }

    public Long getItotalCumplido() {
        return itotalCumplido;
    }

    public void setItotalCumplido(Long itotalCumplido) {
        this.itotalCumplido = itotalCumplido;
    }

    public BigDecimal getPcomis() {
        return pcomis;
    }

    public void setPcomis(BigDecimal pcomis) {
        this.pcomis = pcomis;
    }

    public Long getIcomis() {
        return icomis;
    }

    public void setIcomis(Long icomis) {
        this.icomis = icomis;
    }

    public Liquidaciones getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(Liquidaciones liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public Sublineas getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(Sublineas codSublinea) {
        this.codSublinea = codSublinea;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (liquidacionesDetPK != null ? liquidacionesDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LiquidacionesDet)) {
            return false;
        }
        LiquidacionesDet other = (LiquidacionesDet) object;
        if ((this.liquidacionesDetPK == null && other.liquidacionesDetPK != null) || (this.liquidacionesDetPK != null && !this.liquidacionesDetPK.equals(other.liquidacionesDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.LiquidacionesDet[ liquidacionesDetPK=" + liquidacionesDetPK + " ]";
    }
    
}
