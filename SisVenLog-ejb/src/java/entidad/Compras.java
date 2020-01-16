/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "compras")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compras.findAll", query = "SELECT c FROM Compras c")
    , @NamedQuery(name = "Compras.findByCodEmpr", query = "SELECT c FROM Compras c WHERE c.comprasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Compras.findByCtipoDocum", query = "SELECT c FROM Compras c WHERE c.comprasPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "Compras.findByNrofact", query = "SELECT c FROM Compras c WHERE c.comprasPK.nrofact = :nrofact")
    , @NamedQuery(name = "Compras.findByCodProveed", query = "SELECT c FROM Compras c WHERE c.comprasPK.codProveed = :codProveed")
    , @NamedQuery(name = "Compras.findByCcanalCompra", query = "SELECT c FROM Compras c WHERE c.ccanalCompra = :ccanalCompra")
    , @NamedQuery(name = "Compras.findByFfactur", query = "SELECT c FROM Compras c WHERE c.comprasPK.ffactur = :ffactur")
    , @NamedQuery(name = "Compras.findByFprov", query = "SELECT c FROM Compras c WHERE c.fprov = :fprov")
    , @NamedQuery(name = "Compras.findByFvenc", query = "SELECT c FROM Compras c WHERE c.fvenc = :fvenc")
    , @NamedQuery(name = "Compras.findByMestado", query = "SELECT c FROM Compras c WHERE c.mestado = :mestado")
    , @NamedQuery(name = "Compras.findByTexentas", query = "SELECT c FROM Compras c WHERE c.texentas = :texentas")
    , @NamedQuery(name = "Compras.findByTgravadas", query = "SELECT c FROM Compras c WHERE c.tgravadas = :tgravadas")
    , @NamedQuery(name = "Compras.findByTimpuestos", query = "SELECT c FROM Compras c WHERE c.timpuestos = :timpuestos")
    , @NamedQuery(name = "Compras.findByTdescuentos", query = "SELECT c FROM Compras c WHERE c.tdescuentos = :tdescuentos")
    , @NamedQuery(name = "Compras.findByTtotal", query = "SELECT c FROM Compras c WHERE c.ttotal = :ttotal")
    , @NamedQuery(name = "Compras.findByIsaldo", query = "SELECT c FROM Compras c WHERE c.isaldo = :isaldo")
    , @NamedQuery(name = "Compras.findByXobs", query = "SELECT c FROM Compras c WHERE c.xobs = :xobs")
    , @NamedQuery(name = "Compras.findByCusuario", query = "SELECT c FROM Compras c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "Compras.findByFalta", query = "SELECT c FROM Compras c WHERE c.falta = :falta")
    , @NamedQuery(name = "Compras.findByCusuarioModif", query = "SELECT c FROM Compras c WHERE c.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Compras.findByFultimModif", query = "SELECT c FROM Compras c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "Compras.findByTnotas", query = "SELECT c FROM Compras c WHERE c.tnotas = :tnotas")
    , @NamedQuery(name = "Compras.findByFanul", query = "SELECT c FROM Compras c WHERE c.fanul = :fanul")
    , @NamedQuery(name = "Compras.findByCusuarioAnul", query = "SELECT c FROM Compras c WHERE c.cusuarioAnul = :cusuarioAnul")
    , @NamedQuery(name = "Compras.findByNdiasPlazo", query = "SELECT c FROM Compras c WHERE c.ndiasPlazo = :ndiasPlazo")
    , @NamedQuery(name = "Compras.findByXfactura", query = "SELECT c FROM Compras c WHERE c.xfactura = :xfactura")
    , @NamedQuery(name = "Compras.findByNtimbrado", query = "SELECT c FROM Compras c WHERE c.ntimbrado = :ntimbrado")
    , @NamedQuery(name = "Compras.findByNroPedido", query = "SELECT c FROM Compras c WHERE c.nroPedido = :nroPedido")})
public class Compras implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComprasPK comprasPK;
    @Size(max = 2)
    @Column(name = "ccanal_compra")
    private String ccanalCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fprov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fprov;
    @Column(name = "fvenc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvenc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "texentas")
    private long texentas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tgravadas")
    private long tgravadas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timpuestos")
    private long timpuestos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tdescuentos")
    private long tdescuentos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ttotal")
    private long ttotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isaldo")
    private long isaldo;
    @Size(max = 200)
    @Column(name = "xobs")
    private String xobs;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "tnotas")
    private long tnotas;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Size(max = 30)
    @Column(name = "cusuario_anul")
    private String cusuarioAnul;
    @Column(name = "ndias_plazo")
    private Short ndiasPlazo;
    @Size(max = 15)
    @Column(name = "xfactura")
    private String xfactura;
    @Column(name = "ntimbrado")
    private BigInteger ntimbrado;
    @Column(name = "nro_pedido")
    private Long nroPedido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compras")
    private Collection<NotasCompras> notasComprasCollection;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_depo", referencedColumnName = "cod_depo")})
    @ManyToOne(optional = false)
    private Depositos depositos;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proveedores proveedores;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compras")
    private Collection<ComprasDet> comprasDetCollection;

    public Compras() {
    }

    public Compras(ComprasPK comprasPK) {
        this.comprasPK = comprasPK;
    }

    public Compras(ComprasPK comprasPK, Date fprov, Character mestado, long texentas, long tgravadas, long timpuestos, long tdescuentos, long ttotal, long isaldo, long tnotas) {
        this.comprasPK = comprasPK;
        this.fprov = fprov;
        this.mestado = mestado;
        this.texentas = texentas;
        this.tgravadas = tgravadas;
        this.timpuestos = timpuestos;
        this.tdescuentos = tdescuentos;
        this.ttotal = ttotal;
        this.isaldo = isaldo;
        this.tnotas = tnotas;
    }

    public Compras(short codEmpr, String ctipoDocum, long nrofact, short codProveed, Date ffactur) {
        this.comprasPK = new ComprasPK(codEmpr, ctipoDocum, nrofact, codProveed, ffactur);
    }

    public ComprasPK getComprasPK() {
        return comprasPK;
    }

    public void setComprasPK(ComprasPK comprasPK) {
        this.comprasPK = comprasPK;
    }

    public String getCcanalCompra() {
        return ccanalCompra;
    }

    public void setCcanalCompra(String ccanalCompra) {
        this.ccanalCompra = ccanalCompra;
    }

    public Date getFprov() {
        return fprov;
    }

    public void setFprov(Date fprov) {
        this.fprov = fprov;
    }

    public Date getFvenc() {
        return fvenc;
    }

    public void setFvenc(Date fvenc) {
        this.fvenc = fvenc;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public long getTexentas() {
        return texentas;
    }

    public void setTexentas(long texentas) {
        this.texentas = texentas;
    }

    public long getTgravadas() {
        return tgravadas;
    }

    public void setTgravadas(long tgravadas) {
        this.tgravadas = tgravadas;
    }

    public long getTimpuestos() {
        return timpuestos;
    }

    public void setTimpuestos(long timpuestos) {
        this.timpuestos = timpuestos;
    }

    public long getTdescuentos() {
        return tdescuentos;
    }

    public void setTdescuentos(long tdescuentos) {
        this.tdescuentos = tdescuentos;
    }

    public long getTtotal() {
        return ttotal;
    }

    public void setTtotal(long ttotal) {
        this.ttotal = ttotal;
    }

    public long getIsaldo() {
        return isaldo;
    }

    public void setIsaldo(long isaldo) {
        this.isaldo = isaldo;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
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

    public long getTnotas() {
        return tnotas;
    }

    public void setTnotas(long tnotas) {
        this.tnotas = tnotas;
    }

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    public String getCusuarioAnul() {
        return cusuarioAnul;
    }

    public void setCusuarioAnul(String cusuarioAnul) {
        this.cusuarioAnul = cusuarioAnul;
    }

    public Short getNdiasPlazo() {
        return ndiasPlazo;
    }

    public void setNdiasPlazo(Short ndiasPlazo) {
        this.ndiasPlazo = ndiasPlazo;
    }

    public String getXfactura() {
        return xfactura;
    }

    public void setXfactura(String xfactura) {
        this.xfactura = xfactura;
    }

    public BigInteger getNtimbrado() {
        return ntimbrado;
    }

    public void setNtimbrado(BigInteger ntimbrado) {
        this.ntimbrado = ntimbrado;
    }

    public Long getNroPedido() {
        return nroPedido;
    }

    public void setNroPedido(Long nroPedido) {
        this.nroPedido = nroPedido;
    }

    @XmlTransient
    public Collection<NotasCompras> getNotasComprasCollection() {
        return notasComprasCollection;
    }

    public void setNotasComprasCollection(Collection<NotasCompras> notasComprasCollection) {
        this.notasComprasCollection = notasComprasCollection;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    @XmlTransient
    public Collection<ComprasDet> getComprasDetCollection() {
        return comprasDetCollection;
    }

    public void setComprasDetCollection(Collection<ComprasDet> comprasDetCollection) {
        this.comprasDetCollection = comprasDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comprasPK != null ? comprasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compras)) {
            return false;
        }
        Compras other = (Compras) object;
        if ((this.comprasPK == null && other.comprasPK != null) || (this.comprasPK != null && !this.comprasPK.equals(other.comprasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Compras[ comprasPK=" + comprasPK + " ]";
    }
    
}
