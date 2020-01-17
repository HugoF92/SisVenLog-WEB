/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
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
@Table(name = "notas_ventas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotasVentas.findAll", query = "SELECT n FROM NotasVentas n")
    , @NamedQuery(name = "NotasVentas.findByCodEmpr", query = "SELECT n FROM NotasVentas n WHERE n.notasVentasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "NotasVentas.findByNroNota", query = "SELECT n FROM NotasVentas n WHERE n.notasVentasPK.nroNota = :nroNota")
    , @NamedQuery(name = "NotasVentas.findByCtipoDocum", query = "SELECT n FROM NotasVentas n WHERE n.notasVentasPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "NotasVentas.findByCodCliente", query = "SELECT n FROM NotasVentas n WHERE n.codCliente = :codCliente")
    , @NamedQuery(name = "NotasVentas.findByNrofact", query = "SELECT n FROM NotasVentas n WHERE n.nrofact = :nrofact")
    , @NamedQuery(name = "NotasVentas.findByCodDepo", query = "SELECT n FROM NotasVentas n WHERE n.codDepo = :codDepo")
    , @NamedQuery(name = "NotasVentas.findByFacCtipoDocum", query = "SELECT n FROM NotasVentas n WHERE n.facCtipoDocum = :facCtipoDocum")
    , @NamedQuery(name = "NotasVentas.findByFdocum", query = "SELECT n FROM NotasVentas n WHERE n.notasVentasPK.fdocum = :fdocum")
    , @NamedQuery(name = "NotasVentas.findByTexentas", query = "SELECT n FROM NotasVentas n WHERE n.texentas = :texentas")
    , @NamedQuery(name = "NotasVentas.findByTgravadas", query = "SELECT n FROM NotasVentas n WHERE n.tgravadas = :tgravadas")
    , @NamedQuery(name = "NotasVentas.findByTimpuestos", query = "SELECT n FROM NotasVentas n WHERE n.timpuestos = :timpuestos")
    , @NamedQuery(name = "NotasVentas.findByTtotal", query = "SELECT n FROM NotasVentas n WHERE n.ttotal = :ttotal")
    , @NamedQuery(name = "NotasVentas.findByIsaldo", query = "SELECT n FROM NotasVentas n WHERE n.isaldo = :isaldo")
    , @NamedQuery(name = "NotasVentas.findByMestado", query = "SELECT n FROM NotasVentas n WHERE n.mestado = :mestado")
    , @NamedQuery(name = "NotasVentas.findByXobs", query = "SELECT n FROM NotasVentas n WHERE n.xobs = :xobs")
    , @NamedQuery(name = "NotasVentas.findByFalta", query = "SELECT n FROM NotasVentas n WHERE n.falta = :falta")
    , @NamedQuery(name = "NotasVentas.findByCusuario", query = "SELECT n FROM NotasVentas n WHERE n.cusuario = :cusuario")
    , @NamedQuery(name = "NotasVentas.findByFanul", query = "SELECT n FROM NotasVentas n WHERE n.fanul = :fanul")
    , @NamedQuery(name = "NotasVentas.findByCusuarioAnul", query = "SELECT n FROM NotasVentas n WHERE n.cusuarioAnul = :cusuarioAnul")
    , @NamedQuery(name = "NotasVentas.findByNroPromo", query = "SELECT n FROM NotasVentas n WHERE n.nroPromo = :nroPromo")
    , @NamedQuery(name = "NotasVentas.findByXnroNota", query = "SELECT n FROM NotasVentas n WHERE n.xnroNota = :xnroNota")
    , @NamedQuery(name = "NotasVentas.findByFfactur", query = "SELECT n FROM NotasVentas n WHERE n.ffactur = :ffactur")})
public class NotasVentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotasVentasPK notasVentasPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente")
    private int codCliente;
    @Column(name = "nrofact")
    private Long nrofact;
    @Column(name = "cod_depo")
    private Short codDepo;
    @Size(max = 3)
    @Column(name = "fac_ctipo_docum")
    private String facCtipoDocum;
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
    @Column(name = "ttotal")
    private long ttotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isaldo")
    private long isaldo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @Size(max = 200)
    @Column(name = "xobs")
    private String xobs;
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
    @Column(name = "nro_promo")
    private Long nroPromo;
    @Size(max = 15)
    @Column(name = "xnro_nota")
    private String xnroNota;
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notasVentas")
    private Collection<NotasVentasDet> notasVentasDetCollection;
    @JoinColumns({
        @JoinColumn(name = "ctipo_docum", referencedColumnName = "ctipo_docum", insertable = false, updatable = false)
        , @JoinColumn(name = "cconc", referencedColumnName = "cconc")})
    @ManyToOne(optional = false)
    private ConceptosDocumentos conceptosDocumentos;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_entregador", referencedColumnName = "cod_empleado")})
    @ManyToOne(optional = false)
    private Empleados empleados;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notasVentas")
    private Collection<NotasVentasSer> notasVentasSerCollection;

    public NotasVentas() {
    }

    public NotasVentas(NotasVentasPK notasVentasPK) {
        this.notasVentasPK = notasVentasPK;
    }

    public NotasVentas(NotasVentasPK notasVentasPK, int codCliente, long texentas, long tgravadas, long timpuestos, long ttotal, long isaldo, Character mestado) {
        this.notasVentasPK = notasVentasPK;
        this.codCliente = codCliente;
        this.texentas = texentas;
        this.tgravadas = tgravadas;
        this.timpuestos = timpuestos;
        this.ttotal = ttotal;
        this.isaldo = isaldo;
        this.mestado = mestado;
    }

    public NotasVentas(short codEmpr, long nroNota, String ctipoDocum, Date fdocum) {
        this.notasVentasPK = new NotasVentasPK(codEmpr, nroNota, ctipoDocum, fdocum);
    }

    public NotasVentasPK getNotasVentasPK() {
        return notasVentasPK;
    }

    public void setNotasVentasPK(NotasVentasPK notasVentasPK) {
        this.notasVentasPK = notasVentasPK;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public Long getNrofact() {
        return nrofact;
    }

    public void setNrofact(Long nrofact) {
        this.nrofact = nrofact;
    }

    public Short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(Short codDepo) {
        this.codDepo = codDepo;
    }

    public String getFacCtipoDocum() {
        return facCtipoDocum;
    }

    public void setFacCtipoDocum(String facCtipoDocum) {
        this.facCtipoDocum = facCtipoDocum;
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

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
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

    public Long getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(Long nroPromo) {
        this.nroPromo = nroPromo;
    }

    public String getXnroNota() {
        return xnroNota;
    }

    public void setXnroNota(String xnroNota) {
        this.xnroNota = xnroNota;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    @XmlTransient
    public Collection<NotasVentasDet> getNotasVentasDetCollection() {
        return notasVentasDetCollection;
    }

    public void setNotasVentasDetCollection(Collection<NotasVentasDet> notasVentasDetCollection) {
        this.notasVentasDetCollection = notasVentasDetCollection;
    }

    public ConceptosDocumentos getConceptosDocumentos() {
        return conceptosDocumentos;
    }

    public void setConceptosDocumentos(ConceptosDocumentos conceptosDocumentos) {
        this.conceptosDocumentos = conceptosDocumentos;
    }

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    @XmlTransient
    public Collection<NotasVentasSer> getNotasVentasSerCollection() {
        return notasVentasSerCollection;
    }

    public void setNotasVentasSerCollection(Collection<NotasVentasSer> notasVentasSerCollection) {
        this.notasVentasSerCollection = notasVentasSerCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notasVentasPK != null ? notasVentasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotasVentas)) {
            return false;
        }
        NotasVentas other = (NotasVentas) object;
        if ((this.notasVentasPK == null && other.notasVentasPK != null) || (this.notasVentasPK != null && !this.notasVentasPK.equals(other.notasVentasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.NotasVentas[ notasVentasPK=" + notasVentasPK + " ]";
    }
    
}
