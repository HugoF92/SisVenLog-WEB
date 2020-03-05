/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "pedidos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedidos.findAll", query = "SELECT p FROM Pedidos p")
    , @NamedQuery(name = "Pedidos.findByCodEmpr", query = "SELECT p FROM Pedidos p WHERE p.pedidosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Pedidos.findByNroPedido", query = "SELECT p FROM Pedidos p WHERE p.pedidosPK.nroPedido = :nroPedido")
    , @NamedQuery(name = "Pedidos.findByCtipoDocum", query = "SELECT p FROM Pedidos p WHERE p.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "Pedidos.findByCtipoVta", query = "SELECT p FROM Pedidos p WHERE p.ctipoVta = :ctipoVta")
    , @NamedQuery(name = "Pedidos.findByCodVendedor", query = "SELECT p FROM Pedidos p WHERE p.codVendedor = :codVendedor")
    , @NamedQuery(name = "Pedidos.findByCodDepoEnvio", query = "SELECT p FROM Pedidos p WHERE p.codDepoEnvio = :codDepoEnvio")
    , @NamedQuery(name = "Pedidos.findByCodEntregador", query = "SELECT p FROM Pedidos p WHERE p.codEntregador = :codEntregador")
    , @NamedQuery(name = "Pedidos.findByCodCanal", query = "SELECT p FROM Pedidos p WHERE p.codCanal = :codCanal")
    , @NamedQuery(name = "Pedidos.findByCodRuta", query = "SELECT p FROM Pedidos p WHERE p.codRuta = :codRuta")
    , @NamedQuery(name = "Pedidos.findByCodCliente", query = "SELECT p FROM Pedidos p WHERE p.codCliente = :codCliente")
    , @NamedQuery(name = "Pedidos.findByCodDepo", query = "SELECT p FROM Pedidos p WHERE p.codDepo = :codDepo")
    , @NamedQuery(name = "Pedidos.findByFpedido", query = "SELECT p FROM Pedidos p WHERE p.fpedido = :fpedido")
    , @NamedQuery(name = "Pedidos.findByNpesoAcum", query = "SELECT p FROM Pedidos p WHERE p.npesoAcum = :npesoAcum")
    , @NamedQuery(name = "Pedidos.findByFanul", query = "SELECT p FROM Pedidos p WHERE p.fanul = :fanul")
    , @NamedQuery(name = "Pedidos.findByFfactur", query = "SELECT p FROM Pedidos p WHERE p.ffactur = :ffactur")
    , @NamedQuery(name = "Pedidos.findByFenvio", query = "SELECT p FROM Pedidos p WHERE p.fenvio = :fenvio")
    , @NamedQuery(name = "Pedidos.findByTgravadas", query = "SELECT p FROM Pedidos p WHERE p.tgravadas = :tgravadas")
    , @NamedQuery(name = "Pedidos.findByTexentas", query = "SELECT p FROM Pedidos p WHERE p.texentas = :texentas")
    , @NamedQuery(name = "Pedidos.findByTimpuestos", query = "SELECT p FROM Pedidos p WHERE p.timpuestos = :timpuestos")
    , @NamedQuery(name = "Pedidos.findByTdescuentos", query = "SELECT p FROM Pedidos p WHERE p.tdescuentos = :tdescuentos")
    , @NamedQuery(name = "Pedidos.findByTtotal", query = "SELECT p FROM Pedidos p WHERE p.ttotal = :ttotal")
    , @NamedQuery(name = "Pedidos.findByMestado", query = "SELECT p FROM Pedidos p WHERE p.mestado = :mestado")
    , @NamedQuery(name = "Pedidos.findByCusuario", query = "SELECT p FROM Pedidos p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "Pedidos.findByFalta", query = "SELECT p FROM Pedidos p WHERE p.falta = :falta")
    , @NamedQuery(name = "Pedidos.findByCusuarioModif", query = "SELECT p FROM Pedidos p WHERE p.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Pedidos.findByFultimModif", query = "SELECT p FROM Pedidos p WHERE p.fultimModif = :fultimModif")
    , @NamedQuery(name = "Pedidos.findByFinicioAlta", query = "SELECT p FROM Pedidos p WHERE p.finicioAlta = :finicioAlta")
    , @NamedQuery(name = "Pedidos.findByMorigen", query = "SELECT p FROM Pedidos p WHERE p.morigen = :morigen")
    , @NamedQuery(name = "Pedidos.findByNplazoCheque", query = "SELECT p FROM Pedidos p WHERE p.nplazoCheque = :nplazoCheque")
    , @NamedQuery(name = "Pedidos.findByFfinalAlta", query = "SELECT p FROM Pedidos p WHERE p.ffinalAlta = :ffinalAlta")})
public class Pedidos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedidosPK pedidosPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctipo_vta")
    private Character ctipoVta;
    @Column(name = "cod_vendedor")
    private Short codVendedor;
    @Column(name = "cod_depo_envio")
    private Short codDepoEnvio;
    @Column(name = "cod_entregador")
    private Short codEntregador;
    @Size(max = 2)
    @Column(name = "cod_canal")
    private String codCanal;
    @Column(name = "cod_ruta")
    private Short codRuta;
    @Column(name = "cod_cliente")
    private Integer codCliente;
    @Column(name = "cod_depo")
    private Short codDepo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fpedido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fpedido;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "npeso_acum")
    private BigDecimal npesoAcum;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
    @Column(name = "fenvio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fenvio;
    @Column(name = "tgravadas")
    private Long tgravadas;
    @Column(name = "texentas")
    private Long texentas;
    @Column(name = "timpuestos")
    private Long timpuestos;
    @Column(name = "tdescuentos")
    private Long tdescuentos;
    @Column(name = "ttotal")
    private Long ttotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Column(name = "finicio_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finicioAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "morigen")
    private Character morigen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nplazo_cheque")
    private short nplazoCheque;
    @Column(name = "ffinal_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffinalAlta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedidos")
    private Collection<Facturas> facturasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedidos")
    private Collection<PedidosDet> pedidosDetCollection;

    public Pedidos() {
    }

    public Pedidos(PedidosPK pedidosPK) {
        this.pedidosPK = pedidosPK;
    }

    public Pedidos(PedidosPK pedidosPK, String ctipoDocum, Character ctipoVta, Date fpedido, Character mestado, Character morigen, short nplazoCheque) {
        this.pedidosPK = pedidosPK;
        this.ctipoDocum = ctipoDocum;
        this.ctipoVta = ctipoVta;
        this.fpedido = fpedido;
        this.mestado = mestado;
        this.morigen = morigen;
        this.nplazoCheque = nplazoCheque;
    }

    public Pedidos(short codEmpr, long nroPedido) {
        this.pedidosPK = new PedidosPK(codEmpr, nroPedido);
    }

    public PedidosPK getPedidosPK() {
        return pedidosPK;
    }

    public void setPedidosPK(PedidosPK pedidosPK) {
        this.pedidosPK = pedidosPK;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public Character getCtipoVta() {
        return ctipoVta;
    }

    public void setCtipoVta(Character ctipoVta) {
        this.ctipoVta = ctipoVta;
    }

    public Short getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(Short codVendedor) {
        this.codVendedor = codVendedor;
    }

    public Short getCodDepoEnvio() {
        return codDepoEnvio;
    }

    public void setCodDepoEnvio(Short codDepoEnvio) {
        this.codDepoEnvio = codDepoEnvio;
    }

    public Short getCodEntregador() {
        return codEntregador;
    }

    public void setCodEntregador(Short codEntregador) {
        this.codEntregador = codEntregador;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public Short getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(Short codRuta) {
        this.codRuta = codRuta;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public Short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(Short codDepo) {
        this.codDepo = codDepo;
    }

    public Date getFpedido() {
        return fpedido;
    }

    public void setFpedido(Date fpedido) {
        this.fpedido = fpedido;
    }

    public BigDecimal getNpesoAcum() {
        return npesoAcum;
    }

    public void setNpesoAcum(BigDecimal npesoAcum) {
        this.npesoAcum = npesoAcum;
    }

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public Date getFenvio() {
        return fenvio;
    }

    public void setFenvio(Date fenvio) {
        this.fenvio = fenvio;
    }

    public Long getTgravadas() {
        return tgravadas;
    }

    public void setTgravadas(Long tgravadas) {
        this.tgravadas = tgravadas;
    }

    public Long getTexentas() {
        return texentas;
    }

    public void setTexentas(Long texentas) {
        this.texentas = texentas;
    }

    public Long getTimpuestos() {
        return timpuestos;
    }

    public void setTimpuestos(Long timpuestos) {
        this.timpuestos = timpuestos;
    }

    public Long getTdescuentos() {
        return tdescuentos;
    }

    public void setTdescuentos(Long tdescuentos) {
        this.tdescuentos = tdescuentos;
    }

    public Long getTtotal() {
        return ttotal;
    }

    public void setTtotal(Long ttotal) {
        this.ttotal = ttotal;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public Date getFinicioAlta() {
        return finicioAlta;
    }

    public void setFinicioAlta(Date finicioAlta) {
        this.finicioAlta = finicioAlta;
    }

    public Character getMorigen() {
        return morigen;
    }

    public void setMorigen(Character morigen) {
        this.morigen = morigen;
    }

    public short getNplazoCheque() {
        return nplazoCheque;
    }

    public void setNplazoCheque(short nplazoCheque) {
        this.nplazoCheque = nplazoCheque;
    }

    public Date getFfinalAlta() {
        return ffinalAlta;
    }

    public void setFfinalAlta(Date ffinalAlta) {
        this.ffinalAlta = ffinalAlta;
    }

    @XmlTransient
    public Collection<Facturas> getFacturasCollection() {
        return facturasCollection;
    }

    public void setFacturasCollection(Collection<Facturas> facturasCollection) {
        this.facturasCollection = facturasCollection;
    }

    @XmlTransient
    public Collection<PedidosDet> getPedidosDetCollection() {
        return pedidosDetCollection;
    }

    public void setPedidosDetCollection(Collection<PedidosDet> pedidosDetCollection) {
        this.pedidosDetCollection = pedidosDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidosPK != null ? pedidosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedidos)) {
            return false;
        }
        Pedidos other = (Pedidos) object;
        if ((this.pedidosPK == null && other.pedidosPK != null) || (this.pedidosPK != null && !this.pedidosPK.equals(other.pedidosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Pedidos[ pedidosPK=" + pedidosPK + " ]";
    }
    
}
