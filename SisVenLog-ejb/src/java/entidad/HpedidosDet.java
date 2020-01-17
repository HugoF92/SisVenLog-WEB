/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "hpedidos_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HpedidosDet.findAll", query = "SELECT h FROM HpedidosDet h")
    , @NamedQuery(name = "HpedidosDet.findByCodEmpr", query = "SELECT h FROM HpedidosDet h WHERE h.hpedidosDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "HpedidosDet.findByNroPedido", query = "SELECT h FROM HpedidosDet h WHERE h.hpedidosDetPK.nroPedido = :nroPedido")
    , @NamedQuery(name = "HpedidosDet.findByCodMerca", query = "SELECT h FROM HpedidosDet h WHERE h.hpedidosDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "HpedidosDet.findByNroEnvio", query = "SELECT h FROM HpedidosDet h WHERE h.nroEnvio = :nroEnvio")
    , @NamedQuery(name = "HpedidosDet.findByCantCajas", query = "SELECT h FROM HpedidosDet h WHERE h.cantCajas = :cantCajas")
    , @NamedQuery(name = "HpedidosDet.findByCantUnid", query = "SELECT h FROM HpedidosDet h WHERE h.cantUnid = :cantUnid")
    , @NamedQuery(name = "HpedidosDet.findByPrecioCaja", query = "SELECT h FROM HpedidosDet h WHERE h.precioCaja = :precioCaja")
    , @NamedQuery(name = "HpedidosDet.findByPrecioUnidad", query = "SELECT h FROM HpedidosDet h WHERE h.precioUnidad = :precioUnidad")
    , @NamedQuery(name = "HpedidosDet.findByIexentas", query = "SELECT h FROM HpedidosDet h WHERE h.iexentas = :iexentas")
    , @NamedQuery(name = "HpedidosDet.findByIgravadas", query = "SELECT h FROM HpedidosDet h WHERE h.igravadas = :igravadas")
    , @NamedQuery(name = "HpedidosDet.findByImpuestos", query = "SELECT h FROM HpedidosDet h WHERE h.impuestos = :impuestos")})
public class HpedidosDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HpedidosDetPK hpedidosDetPK;
    @Column(name = "nro_envio")
    private Long nroEnvio;
    @Column(name = "cant_cajas")
    private Integer cantCajas;
    @Column(name = "cant_unid")
    private Integer cantUnid;
    @Column(name = "precio_caja")
    private Long precioCaja;
    @Column(name = "precio_unidad")
    private Long precioUnidad;
    @Column(name = "iexentas")
    private Long iexentas;
    @Column(name = "igravadas")
    private Long igravadas;
    @Column(name = "impuestos")
    private Long impuestos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hpedidosDet")
    private Collection<PedPromoDet> pedPromoDetCollection;

    public HpedidosDet() {
    }

    public HpedidosDet(HpedidosDetPK hpedidosDetPK) {
        this.hpedidosDetPK = hpedidosDetPK;
    }

    public HpedidosDet(short codEmpr, long nroPedido, String codMerca) {
        this.hpedidosDetPK = new HpedidosDetPK(codEmpr, nroPedido, codMerca);
    }

    public HpedidosDetPK getHpedidosDetPK() {
        return hpedidosDetPK;
    }

    public void setHpedidosDetPK(HpedidosDetPK hpedidosDetPK) {
        this.hpedidosDetPK = hpedidosDetPK;
    }

    public Long getNroEnvio() {
        return nroEnvio;
    }

    public void setNroEnvio(Long nroEnvio) {
        this.nroEnvio = nroEnvio;
    }

    public Integer getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(Integer cantCajas) {
        this.cantCajas = cantCajas;
    }

    public Integer getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(Integer cantUnid) {
        this.cantUnid = cantUnid;
    }

    public Long getPrecioCaja() {
        return precioCaja;
    }

    public void setPrecioCaja(Long precioCaja) {
        this.precioCaja = precioCaja;
    }

    public Long getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(Long precioUnidad) {
        this.precioUnidad = precioUnidad;
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

    @XmlTransient
    public Collection<PedPromoDet> getPedPromoDetCollection() {
        return pedPromoDetCollection;
    }

    public void setPedPromoDetCollection(Collection<PedPromoDet> pedPromoDetCollection) {
        this.pedPromoDetCollection = pedPromoDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hpedidosDetPK != null ? hpedidosDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HpedidosDet)) {
            return false;
        }
        HpedidosDet other = (HpedidosDet) object;
        if ((this.hpedidosDetPK == null && other.hpedidosDetPK != null) || (this.hpedidosDetPK != null && !this.hpedidosDetPK.equals(other.hpedidosDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.HpedidosDet[ hpedidosDetPK=" + hpedidosDetPK + " ]";
    }
    
}
