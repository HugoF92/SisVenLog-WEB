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
@Table(name = "ped_promo_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedPromoDet.findAll", query = "SELECT p FROM PedPromoDet p")
    , @NamedQuery(name = "PedPromoDet.findByCodEmpr", query = "SELECT p FROM PedPromoDet p WHERE p.pedPromoDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "PedPromoDet.findByNroPedido", query = "SELECT p FROM PedPromoDet p WHERE p.pedPromoDetPK.nroPedido = :nroPedido")
    , @NamedQuery(name = "PedPromoDet.findByCodMerca", query = "SELECT p FROM PedPromoDet p WHERE p.pedPromoDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "PedPromoDet.findByNroPromo", query = "SELECT p FROM PedPromoDet p WHERE p.pedPromoDetPK.nroPromo = :nroPromo")
    , @NamedQuery(name = "PedPromoDet.findByBonifCajas", query = "SELECT p FROM PedPromoDet p WHERE p.bonifCajas = :bonifCajas")
    , @NamedQuery(name = "PedPromoDet.findByBonifUnid", query = "SELECT p FROM PedPromoDet p WHERE p.bonifUnid = :bonifUnid")
    , @NamedQuery(name = "PedPromoDet.findByPdesc", query = "SELECT p FROM PedPromoDet p WHERE p.pdesc = :pdesc")})
public class PedPromoDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedPromoDetPK pedPromoDetPK;
    @Column(name = "bonif_cajas")
    private Integer bonifCajas;
    @Column(name = "bonif_unid")
    private Integer bonifUnid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pdesc")
    private BigDecimal pdesc;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_pedido", referencedColumnName = "nro_pedido", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private HpedidosDet hpedidosDet;

    public PedPromoDet() {
    }

    public PedPromoDet(PedPromoDetPK pedPromoDetPK) {
        this.pedPromoDetPK = pedPromoDetPK;
    }

    public PedPromoDet(short codEmpr, long nroPedido, String codMerca, short nroPromo) {
        this.pedPromoDetPK = new PedPromoDetPK(codEmpr, nroPedido, codMerca, nroPromo);
    }

    public PedPromoDetPK getPedPromoDetPK() {
        return pedPromoDetPK;
    }

    public void setPedPromoDetPK(PedPromoDetPK pedPromoDetPK) {
        this.pedPromoDetPK = pedPromoDetPK;
    }

    public Integer getBonifCajas() {
        return bonifCajas;
    }

    public void setBonifCajas(Integer bonifCajas) {
        this.bonifCajas = bonifCajas;
    }

    public Integer getBonifUnid() {
        return bonifUnid;
    }

    public void setBonifUnid(Integer bonifUnid) {
        this.bonifUnid = bonifUnid;
    }

    public BigDecimal getPdesc() {
        return pdesc;
    }

    public void setPdesc(BigDecimal pdesc) {
        this.pdesc = pdesc;
    }

    public HpedidosDet getHpedidosDet() {
        return hpedidosDet;
    }

    public void setHpedidosDet(HpedidosDet hpedidosDet) {
        this.hpedidosDet = hpedidosDet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedPromoDetPK != null ? pedPromoDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedPromoDet)) {
            return false;
        }
        PedPromoDet other = (PedPromoDet) object;
        if ((this.pedPromoDetPK == null && other.pedPromoDetPK != null) || (this.pedPromoDetPK != null && !this.pedPromoDetPK.equals(other.pedPromoDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PedPromoDet[ pedPromoDetPK=" + pedPromoDetPK + " ]";
    }
    
}
