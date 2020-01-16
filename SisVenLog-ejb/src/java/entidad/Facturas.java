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
@Table(name = "facturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facturas.findAll", query = "SELECT f FROM Facturas f")
    , @NamedQuery(name = "Facturas.findByCodEmpr", query = "SELECT f FROM Facturas f WHERE f.facturasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Facturas.findByNrofact", query = "SELECT f FROM Facturas f WHERE f.facturasPK.nrofact = :nrofact")
    , @NamedQuery(name = "Facturas.findByCtipoDocum", query = "SELECT f FROM Facturas f WHERE f.facturasPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "Facturas.findByFfactur", query = "SELECT f FROM Facturas f WHERE f.facturasPK.ffactur = :ffactur")
    , @NamedQuery(name = "Facturas.findByCodRuta", query = "SELECT f FROM Facturas f WHERE f.codRuta = :codRuta")
    , @NamedQuery(name = "Facturas.findByCtipoVta", query = "SELECT f FROM Facturas f WHERE f.ctipoVta = :ctipoVta")
    , @NamedQuery(name = "Facturas.findByMestado", query = "SELECT f FROM Facturas f WHERE f.mestado = :mestado")
    , @NamedQuery(name = "Facturas.findByFvenc", query = "SELECT f FROM Facturas f WHERE f.fvenc = :fvenc")
    , @NamedQuery(name = "Facturas.findByTexentas", query = "SELECT f FROM Facturas f WHERE f.texentas = :texentas")
    , @NamedQuery(name = "Facturas.findByTgravadas", query = "SELECT f FROM Facturas f WHERE f.tgravadas = :tgravadas")
    , @NamedQuery(name = "Facturas.findByTimpuestos", query = "SELECT f FROM Facturas f WHERE f.timpuestos = :timpuestos")
    , @NamedQuery(name = "Facturas.findByTdescuentos", query = "SELECT f FROM Facturas f WHERE f.tdescuentos = :tdescuentos")
    , @NamedQuery(name = "Facturas.findByTtotal", query = "SELECT f FROM Facturas f WHERE f.ttotal = :ttotal")
    , @NamedQuery(name = "Facturas.findByIsaldo", query = "SELECT f FROM Facturas f WHERE f.isaldo = :isaldo")
    , @NamedQuery(name = "Facturas.findByXobs", query = "SELECT f FROM Facturas f WHERE f.xobs = :xobs")
    , @NamedQuery(name = "Facturas.findByXdirec", query = "SELECT f FROM Facturas f WHERE f.xdirec = :xdirec")
    , @NamedQuery(name = "Facturas.findByXruc", query = "SELECT f FROM Facturas f WHERE f.xruc = :xruc")
    , @NamedQuery(name = "Facturas.findByXrazonSocial", query = "SELECT f FROM Facturas f WHERE f.xrazonSocial = :xrazonSocial")
    , @NamedQuery(name = "Facturas.findByPinteres", query = "SELECT f FROM Facturas f WHERE f.pinteres = :pinteres")
    , @NamedQuery(name = "Facturas.findByFalta", query = "SELECT f FROM Facturas f WHERE f.falta = :falta")
    , @NamedQuery(name = "Facturas.findByCusuario", query = "SELECT f FROM Facturas f WHERE f.cusuario = :cusuario")
    , @NamedQuery(name = "Facturas.findByFanul", query = "SELECT f FROM Facturas f WHERE f.fanul = :fanul")
    , @NamedQuery(name = "Facturas.findByCusuarioAnul", query = "SELECT f FROM Facturas f WHERE f.cusuarioAnul = :cusuarioAnul")
    , @NamedQuery(name = "Facturas.findByFultimModif", query = "SELECT f FROM Facturas f WHERE f.fultimModif = :fultimModif")
    , @NamedQuery(name = "Facturas.findByCusuarioModif", query = "SELECT f FROM Facturas f WHERE f.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Facturas.findByXtelef", query = "SELECT f FROM Facturas f WHERE f.xtelef = :xtelef")
    , @NamedQuery(name = "Facturas.findByXciudad", query = "SELECT f FROM Facturas f WHERE f.xciudad = :xciudad")
    , @NamedQuery(name = "Facturas.findByTnotas", query = "SELECT f FROM Facturas f WHERE f.tnotas = :tnotas")
    , @NamedQuery(name = "Facturas.findByInteres", query = "SELECT f FROM Facturas f WHERE f.interes = :interes")
    , @NamedQuery(name = "Facturas.findByTgravadas10", query = "SELECT f FROM Facturas f WHERE f.tgravadas10 = :tgravadas10")
    , @NamedQuery(name = "Facturas.findByTgravadas5", query = "SELECT f FROM Facturas f WHERE f.tgravadas5 = :tgravadas5")
    , @NamedQuery(name = "Facturas.findByTimpuestos10", query = "SELECT f FROM Facturas f WHERE f.timpuestos10 = :timpuestos10")
    , @NamedQuery(name = "Facturas.findByTimpuestos5", query = "SELECT f FROM Facturas f WHERE f.timpuestos5 = :timpuestos5")
    , @NamedQuery(name = "Facturas.findByNplazoCheque", query = "SELECT f FROM Facturas f WHERE f.nplazoCheque = :nplazoCheque")
    , @NamedQuery(name = "Facturas.findByXfactura", query = "SELECT f FROM Facturas f WHERE f.xfactura = :xfactura")
    , @NamedQuery(name = "Facturas.findByFvencImpre", query = "SELECT f FROM Facturas f WHERE f.fvencImpre = :fvencImpre")})
public class Facturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacturasPK facturasPK;
    @Column(name = "cod_ruta")
    private Short codRuta;
    @Column(name = "ctipo_vta")
    private Character ctipoVta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @Column(name = "fvenc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvenc;
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
    @Size(max = 50)
    @Column(name = "xobs")
    private String xobs;
    @Size(max = 50)
    @Column(name = "xdirec")
    private String xdirec;
    @Size(max = 15)
    @Column(name = "xruc")
    private String xruc;
    @Size(max = 50)
    @Column(name = "xrazon_social")
    private String xrazonSocial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pinteres")
    private BigDecimal pinteres;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Size(max = 30)
    @Column(name = "cusuario_anul")
    private String cusuarioAnul;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Size(max = 20)
    @Column(name = "xtelef")
    private String xtelef;
    @Size(max = 50)
    @Column(name = "xciudad")
    private String xciudad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tnotas")
    private long tnotas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "interes")
    private long interes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tgravadas_10")
    private long tgravadas10;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tgravadas_5")
    private long tgravadas5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timpuestos_10")
    private long timpuestos10;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timpuestos_5")
    private long timpuestos5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nplazo_cheque")
    private short nplazoCheque;
    @Size(max = 15)
    @Column(name = "xfactura")
    private String xfactura;
    @Column(name = "fvenc_impre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvencImpre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturas")
    private Collection<FacturasSer> facturasSerCollection;
    @JoinColumn(name = "cod_canal", referencedColumnName = "cod_canal")
    @ManyToOne
    private CanalesVenta codCanal;
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente")
    @ManyToOne
    private Clientes codCliente;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_depo", referencedColumnName = "cod_depo")})
    @ManyToOne(optional = false)
    private Depositos depositos;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_entregador", referencedColumnName = "cod_empleado")})
    @ManyToOne(optional = false)
    private Empleados empleados;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_vendedor", referencedColumnName = "cod_empleado")})
    @ManyToOne(optional = false)
    private Empleados empleados1;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumn(name = "cmotivo", referencedColumnName = "cmotivo")
    @ManyToOne
    private Motivos cmotivo;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_pedido", referencedColumnName = "nro_pedido")})
    @ManyToOne(optional = false)
    private Pedidos pedidos;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona")})
    @ManyToOne(optional = false)
    private Zonas zonas;

    public Facturas() {
    }

    public Facturas(FacturasPK facturasPK) {
        this.facturasPK = facturasPK;
    }

    public Facturas(FacturasPK facturasPK, Character mestado, long texentas, long tgravadas, long timpuestos, long tdescuentos, long ttotal, long isaldo, BigDecimal pinteres, long tnotas, long interes, long tgravadas10, long tgravadas5, long timpuestos10, long timpuestos5, short nplazoCheque) {
        this.facturasPK = facturasPK;
        this.mestado = mestado;
        this.texentas = texentas;
        this.tgravadas = tgravadas;
        this.timpuestos = timpuestos;
        this.tdescuentos = tdescuentos;
        this.ttotal = ttotal;
        this.isaldo = isaldo;
        this.pinteres = pinteres;
        this.tnotas = tnotas;
        this.interes = interes;
        this.tgravadas10 = tgravadas10;
        this.tgravadas5 = tgravadas5;
        this.timpuestos10 = timpuestos10;
        this.timpuestos5 = timpuestos5;
        this.nplazoCheque = nplazoCheque;
    }

    public Facturas(short codEmpr, long nrofact, String ctipoDocum, Date ffactur) {
        this.facturasPK = new FacturasPK(codEmpr, nrofact, ctipoDocum, ffactur);
    }

    public FacturasPK getFacturasPK() {
        return facturasPK;
    }

    public void setFacturasPK(FacturasPK facturasPK) {
        this.facturasPK = facturasPK;
    }

    public Short getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(Short codRuta) {
        this.codRuta = codRuta;
    }

    public Character getCtipoVta() {
        return ctipoVta;
    }

    public void setCtipoVta(Character ctipoVta) {
        this.ctipoVta = ctipoVta;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public Date getFvenc() {
        return fvenc;
    }

    public void setFvenc(Date fvenc) {
        this.fvenc = fvenc;
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

    public String getXdirec() {
        return xdirec;
    }

    public void setXdirec(String xdirec) {
        this.xdirec = xdirec;
    }

    public String getXruc() {
        return xruc;
    }

    public void setXruc(String xruc) {
        this.xruc = xruc;
    }

    public String getXrazonSocial() {
        return xrazonSocial;
    }

    public void setXrazonSocial(String xrazonSocial) {
        this.xrazonSocial = xrazonSocial;
    }

    public BigDecimal getPinteres() {
        return pinteres;
    }

    public void setPinteres(BigDecimal pinteres) {
        this.pinteres = pinteres;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
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

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public String getXtelef() {
        return xtelef;
    }

    public void setXtelef(String xtelef) {
        this.xtelef = xtelef;
    }

    public String getXciudad() {
        return xciudad;
    }

    public void setXciudad(String xciudad) {
        this.xciudad = xciudad;
    }

    public long getTnotas() {
        return tnotas;
    }

    public void setTnotas(long tnotas) {
        this.tnotas = tnotas;
    }

    public long getInteres() {
        return interes;
    }

    public void setInteres(long interes) {
        this.interes = interes;
    }

    public long getTgravadas10() {
        return tgravadas10;
    }

    public void setTgravadas10(long tgravadas10) {
        this.tgravadas10 = tgravadas10;
    }

    public long getTgravadas5() {
        return tgravadas5;
    }

    public void setTgravadas5(long tgravadas5) {
        this.tgravadas5 = tgravadas5;
    }

    public long getTimpuestos10() {
        return timpuestos10;
    }

    public void setTimpuestos10(long timpuestos10) {
        this.timpuestos10 = timpuestos10;
    }

    public long getTimpuestos5() {
        return timpuestos5;
    }

    public void setTimpuestos5(long timpuestos5) {
        this.timpuestos5 = timpuestos5;
    }

    public short getNplazoCheque() {
        return nplazoCheque;
    }

    public void setNplazoCheque(short nplazoCheque) {
        this.nplazoCheque = nplazoCheque;
    }

    public String getXfactura() {
        return xfactura;
    }

    public void setXfactura(String xfactura) {
        this.xfactura = xfactura;
    }

    public Date getFvencImpre() {
        return fvencImpre;
    }

    public void setFvencImpre(Date fvencImpre) {
        this.fvencImpre = fvencImpre;
    }

    @XmlTransient
    public Collection<FacturasSer> getFacturasSerCollection() {
        return facturasSerCollection;
    }

    public void setFacturasSerCollection(Collection<FacturasSer> facturasSerCollection) {
        this.facturasSerCollection = facturasSerCollection;
    }

    public CanalesVenta getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(CanalesVenta codCanal) {
        this.codCanal = codCanal;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public Empleados getEmpleados1() {
        return empleados1;
    }

    public void setEmpleados1(Empleados empleados1) {
        this.empleados1 = empleados1;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public Motivos getCmotivo() {
        return cmotivo;
    }

    public void setCmotivo(Motivos cmotivo) {
        this.cmotivo = cmotivo;
    }

    public Pedidos getPedidos() {
        return pedidos;
    }

    public void setPedidos(Pedidos pedidos) {
        this.pedidos = pedidos;
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
        hash += (facturasPK != null ? facturasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facturas)) {
            return false;
        }
        Facturas other = (Facturas) object;
        if ((this.facturasPK == null && other.facturasPK != null) || (this.facturasPK != null && !this.facturasPK.equals(other.facturasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Facturas[ facturasPK=" + facturasPK + " ]";
    }
    
}
