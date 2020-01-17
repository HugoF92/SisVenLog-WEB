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
@Table(name = "pedidos_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedidosDet.findAll", query = "SELECT p FROM PedidosDet p")
    , @NamedQuery(name = "PedidosDet.findByCodEmpr", query = "SELECT p FROM PedidosDet p WHERE p.pedidosDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "PedidosDet.findByNroPedido", query = "SELECT p FROM PedidosDet p WHERE p.pedidosDetPK.nroPedido = :nroPedido")
    , @NamedQuery(name = "PedidosDet.findByCodMerca", query = "SELECT p FROM PedidosDet p WHERE p.pedidosDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "PedidosDet.findByXdesc", query = "SELECT p FROM PedidosDet p WHERE p.xdesc = :xdesc")
    , @NamedQuery(name = "PedidosDet.findByCantCajas", query = "SELECT p FROM PedidosDet p WHERE p.cantCajas = :cantCajas")
    , @NamedQuery(name = "PedidosDet.findByCantUnid", query = "SELECT p FROM PedidosDet p WHERE p.cantUnid = :cantUnid")
    , @NamedQuery(name = "PedidosDet.findByCajasBonif", query = "SELECT p FROM PedidosDet p WHERE p.cajasBonif = :cajasBonif")
    , @NamedQuery(name = "PedidosDet.findByUnidBonif", query = "SELECT p FROM PedidosDet p WHERE p.unidBonif = :unidBonif")
    , @NamedQuery(name = "PedidosDet.findByPrecioCaja", query = "SELECT p FROM PedidosDet p WHERE p.precioCaja = :precioCaja")
    , @NamedQuery(name = "PedidosDet.findByPrecioUnidad", query = "SELECT p FROM PedidosDet p WHERE p.precioUnidad = :precioUnidad")
    , @NamedQuery(name = "PedidosDet.findByIexentas", query = "SELECT p FROM PedidosDet p WHERE p.iexentas = :iexentas")
    , @NamedQuery(name = "PedidosDet.findByIgravadas", query = "SELECT p FROM PedidosDet p WHERE p.igravadas = :igravadas")
    , @NamedQuery(name = "PedidosDet.findByImpuestos", query = "SELECT p FROM PedidosDet p WHERE p.impuestos = :impuestos")
    , @NamedQuery(name = "PedidosDet.findByIdescuentos", query = "SELECT p FROM PedidosDet p WHERE p.idescuentos = :idescuentos")
    , @NamedQuery(name = "PedidosDet.findByCajasFact", query = "SELECT p FROM PedidosDet p WHERE p.cajasFact = :cajasFact")
    , @NamedQuery(name = "PedidosDet.findByUnidFact", query = "SELECT p FROM PedidosDet p WHERE p.unidFact = :unidFact")
    , @NamedQuery(name = "PedidosDet.findByCajasEnv", query = "SELECT p FROM PedidosDet p WHERE p.cajasEnv = :cajasEnv")
    , @NamedQuery(name = "PedidosDet.findByUnidEnv", query = "SELECT p FROM PedidosDet p WHERE p.unidEnv = :unidEnv")
    , @NamedQuery(name = "PedidosDet.findByPdesc", query = "SELECT p FROM PedidosDet p WHERE p.pdesc = :pdesc")
    , @NamedQuery(name = "PedidosDet.findByNroPromo", query = "SELECT p FROM PedidosDet p WHERE p.nroPromo = :nroPromo")
    , @NamedQuery(name = "PedidosDet.findByPdescTpr", query = "SELECT p FROM PedidosDet p WHERE p.pdescTpr = :pdescTpr")
    , @NamedQuery(name = "PedidosDet.findByIdescTpr", query = "SELECT p FROM PedidosDet p WHERE p.idescTpr = :idescTpr")
    , @NamedQuery(name = "PedidosDet.findByNroPromoTpr", query = "SELECT p FROM PedidosDet p WHERE p.nroPromoTpr = :nroPromoTpr")})
public class PedidosDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedidosDetPK pedidosDetPK;
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
    @Column(name = "cajas_bonif")
    private int cajasBonif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unid_bonif")
    private int unidBonif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_caja")
    private long precioCaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_unidad")
    private long precioUnidad;
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
    private long impuestos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idescuentos")
    private long idescuentos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cajas_fact")
    private int cajasFact;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unid_fact")
    private int unidFact;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cajas_env")
    private int cajasEnv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unid_env")
    private int unidEnv;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdesc")
    private BigDecimal pdesc;
    @Column(name = "nro_promo")
    private Long nroPromo;
    @Column(name = "pdesc_tpr")
    private BigDecimal pdescTpr;
    @Column(name = "idesc_tpr")
    private Long idescTpr;
    @Column(name = "nro_promo_tpr")
    private Long nroPromoTpr;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_pedido", referencedColumnName = "nro_pedido", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Pedidos pedidos;

    public PedidosDet() {
    }

    public PedidosDet(PedidosDetPK pedidosDetPK) {
        this.pedidosDetPK = pedidosDetPK;
    }

    public PedidosDet(PedidosDetPK pedidosDetPK, int cantCajas, int cantUnid, int cajasBonif, int unidBonif, long precioCaja, long precioUnidad, long iexentas, long igravadas, long impuestos, long idescuentos, int cajasFact, int unidFact, int cajasEnv, int unidEnv, BigDecimal pdesc) {
        this.pedidosDetPK = pedidosDetPK;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.cajasBonif = cajasBonif;
        this.unidBonif = unidBonif;
        this.precioCaja = precioCaja;
        this.precioUnidad = precioUnidad;
        this.iexentas = iexentas;
        this.igravadas = igravadas;
        this.impuestos = impuestos;
        this.idescuentos = idescuentos;
        this.cajasFact = cajasFact;
        this.unidFact = unidFact;
        this.cajasEnv = cajasEnv;
        this.unidEnv = unidEnv;
        this.pdesc = pdesc;
    }

    public PedidosDet(short codEmpr, long nroPedido, String codMerca) {
        this.pedidosDetPK = new PedidosDetPK(codEmpr, nroPedido, codMerca);
    }

    public PedidosDetPK getPedidosDetPK() {
        return pedidosDetPK;
    }

    public void setPedidosDetPK(PedidosDetPK pedidosDetPK) {
        this.pedidosDetPK = pedidosDetPK;
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

    public int getCajasBonif() {
        return cajasBonif;
    }

    public void setCajasBonif(int cajasBonif) {
        this.cajasBonif = cajasBonif;
    }

    public int getUnidBonif() {
        return unidBonif;
    }

    public void setUnidBonif(int unidBonif) {
        this.unidBonif = unidBonif;
    }

    public long getPrecioCaja() {
        return precioCaja;
    }

    public void setPrecioCaja(long precioCaja) {
        this.precioCaja = precioCaja;
    }

    public long getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(long precioUnidad) {
        this.precioUnidad = precioUnidad;
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

    public long getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(long impuestos) {
        this.impuestos = impuestos;
    }

    public long getIdescuentos() {
        return idescuentos;
    }

    public void setIdescuentos(long idescuentos) {
        this.idescuentos = idescuentos;
    }

    public int getCajasFact() {
        return cajasFact;
    }

    public void setCajasFact(int cajasFact) {
        this.cajasFact = cajasFact;
    }

    public int getUnidFact() {
        return unidFact;
    }

    public void setUnidFact(int unidFact) {
        this.unidFact = unidFact;
    }

    public int getCajasEnv() {
        return cajasEnv;
    }

    public void setCajasEnv(int cajasEnv) {
        this.cajasEnv = cajasEnv;
    }

    public int getUnidEnv() {
        return unidEnv;
    }

    public void setUnidEnv(int unidEnv) {
        this.unidEnv = unidEnv;
    }

    public BigDecimal getPdesc() {
        return pdesc;
    }

    public void setPdesc(BigDecimal pdesc) {
        this.pdesc = pdesc;
    }

    public Long getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(Long nroPromo) {
        this.nroPromo = nroPromo;
    }

    public BigDecimal getPdescTpr() {
        return pdescTpr;
    }

    public void setPdescTpr(BigDecimal pdescTpr) {
        this.pdescTpr = pdescTpr;
    }

    public Long getIdescTpr() {
        return idescTpr;
    }

    public void setIdescTpr(Long idescTpr) {
        this.idescTpr = idescTpr;
    }

    public Long getNroPromoTpr() {
        return nroPromoTpr;
    }

    public void setNroPromoTpr(Long nroPromoTpr) {
        this.nroPromoTpr = nroPromoTpr;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    public Pedidos getPedidos() {
        return pedidos;
    }

    public void setPedidos(Pedidos pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidosDetPK != null ? pedidosDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidosDet)) {
            return false;
        }
        PedidosDet other = (PedidosDet) object;
        if ((this.pedidosDetPK == null && other.pedidosDetPK != null) || (this.pedidosDetPK != null && !this.pedidosDetPK.equals(other.pedidosDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PedidosDet[ pedidosDetPK=" + pedidosDetPK + " ]";
    }
    
}
