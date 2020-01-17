/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "ped_compras_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedComprasDet.findAll", query = "SELECT p FROM PedComprasDet p")
    , @NamedQuery(name = "PedComprasDet.findByCodEmpr", query = "SELECT p FROM PedComprasDet p WHERE p.pedComprasDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "PedComprasDet.findByNroPedido", query = "SELECT p FROM PedComprasDet p WHERE p.pedComprasDetPK.nroPedido = :nroPedido")
    , @NamedQuery(name = "PedComprasDet.findByCodMerca", query = "SELECT p FROM PedComprasDet p WHERE p.pedComprasDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "PedComprasDet.findByCantCajas", query = "SELECT p FROM PedComprasDet p WHERE p.cantCajas = :cantCajas")
    , @NamedQuery(name = "PedComprasDet.findByCantUnid", query = "SELECT p FROM PedComprasDet p WHERE p.cantUnid = :cantUnid")
    , @NamedQuery(name = "PedComprasDet.findByItolerar", query = "SELECT p FROM PedComprasDet p WHERE p.itolerar = :itolerar")
    , @NamedQuery(name = "PedComprasDet.findByCodBarra", query = "SELECT p FROM PedComprasDet p WHERE p.codBarra = :codBarra")})
public class PedComprasDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedComprasDetPK pedComprasDetPK;
    @Column(name = "cant_cajas")
    private Long cantCajas;
    @Column(name = "cant_unid")
    private Long cantUnid;
    @Column(name = "itolerar")
    private Long itolerar;
    @Size(max = 20)
    @Column(name = "cod_barra")
    private String codBarra;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_pedido", referencedColumnName = "nro_pedido", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private PedCompras pedCompras;

    public PedComprasDet() {
    }

    public PedComprasDet(PedComprasDetPK pedComprasDetPK) {
        this.pedComprasDetPK = pedComprasDetPK;
    }

    public PedComprasDet(short codEmpr, long nroPedido, String codMerca) {
        this.pedComprasDetPK = new PedComprasDetPK(codEmpr, nroPedido, codMerca);
    }

    public PedComprasDetPK getPedComprasDetPK() {
        return pedComprasDetPK;
    }

    public void setPedComprasDetPK(PedComprasDetPK pedComprasDetPK) {
        this.pedComprasDetPK = pedComprasDetPK;
    }

    public Long getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(Long cantCajas) {
        this.cantCajas = cantCajas;
    }

    public Long getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(Long cantUnid) {
        this.cantUnid = cantUnid;
    }

    public Long getItolerar() {
        return itolerar;
    }

    public void setItolerar(Long itolerar) {
        this.itolerar = itolerar;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public PedCompras getPedCompras() {
        return pedCompras;
    }

    public void setPedCompras(PedCompras pedCompras) {
        this.pedCompras = pedCompras;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedComprasDetPK != null ? pedComprasDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedComprasDet)) {
            return false;
        }
        PedComprasDet other = (PedComprasDet) object;
        if ((this.pedComprasDetPK == null && other.pedComprasDetPK != null) || (this.pedComprasDetPK != null && !this.pedComprasDetPK.equals(other.pedComprasDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PedComprasDet[ pedComprasDetPK=" + pedComprasDetPK + " ]";
    }
    
}
