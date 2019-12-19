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
@Table(name = "notas_compras")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotasCompras.findAll", query = "SELECT n FROM NotasCompras n")
    , @NamedQuery(name = "NotasCompras.findByCodEmpr", query = "SELECT n FROM NotasCompras n WHERE n.notasComprasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "NotasCompras.findByNroNota", query = "SELECT n FROM NotasCompras n WHERE n.notasComprasPK.nroNota = :nroNota")
    , @NamedQuery(name = "NotasCompras.findByCodProveed", query = "SELECT n FROM NotasCompras n WHERE n.notasComprasPK.codProveed = :codProveed")
    , @NamedQuery(name = "NotasCompras.findByCtipoDocum", query = "SELECT n FROM NotasCompras n WHERE n.notasComprasPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "NotasCompras.findByFdocum", query = "SELECT n FROM NotasCompras n WHERE n.notasComprasPK.fdocum = :fdocum")
    , @NamedQuery(name = "NotasCompras.findByTexentas", query = "SELECT n FROM NotasCompras n WHERE n.texentas = :texentas")
    , @NamedQuery(name = "NotasCompras.findByTgravadas", query = "SELECT n FROM NotasCompras n WHERE n.tgravadas = :tgravadas")
    , @NamedQuery(name = "NotasCompras.findByTimpuestos", query = "SELECT n FROM NotasCompras n WHERE n.timpuestos = :timpuestos")
    , @NamedQuery(name = "NotasCompras.findByFanul", query = "SELECT n FROM NotasCompras n WHERE n.fanul = :fanul")
    , @NamedQuery(name = "NotasCompras.findByIsaldo", query = "SELECT n FROM NotasCompras n WHERE n.isaldo = :isaldo")
    , @NamedQuery(name = "NotasCompras.findByCusuario", query = "SELECT n FROM NotasCompras n WHERE n.cusuario = :cusuario")
    , @NamedQuery(name = "NotasCompras.findByFalta", query = "SELECT n FROM NotasCompras n WHERE n.falta = :falta")
    , @NamedQuery(name = "NotasCompras.findByXobs", query = "SELECT n FROM NotasCompras n WHERE n.xobs = :xobs")
    , @NamedQuery(name = "NotasCompras.findByMestado", query = "SELECT n FROM NotasCompras n WHERE n.mestado = :mestado")
    , @NamedQuery(name = "NotasCompras.findByTtotal", query = "SELECT n FROM NotasCompras n WHERE n.ttotal = :ttotal")
    , @NamedQuery(name = "NotasCompras.findByNtimbrado", query = "SELECT n FROM NotasCompras n WHERE n.ntimbrado = :ntimbrado")})
public class NotasCompras implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotasComprasPK notasComprasPK;
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
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isaldo")
    private long isaldo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 200)
    @Column(name = "xobs")
    private String xobs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ttotal")
    private long ttotal;
    @Column(name = "ntimbrado")
    private BigInteger ntimbrado;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "com_ctipo_docum", referencedColumnName = "ctipo_docum")
        , @JoinColumn(name = "nrofact", referencedColumnName = "nrofact")
        , @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
        , @JoinColumn(name = "ffactur", referencedColumnName = "ffactur")})
    @ManyToOne(optional = false)
    private Compras compras;
    @JoinColumns({
        @JoinColumn(name = "ctipo_docum", referencedColumnName = "ctipo_docum", insertable = false, updatable = false)
        , @JoinColumn(name = "cconc", referencedColumnName = "cconc")})
    @ManyToOne(optional = false)
    private ConceptosDocumentos conceptosDocumentos;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_depo", referencedColumnName = "cod_depo")})
    @ManyToOne(optional = false)
    private Depositos depositos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notasCompras")
    private Collection<NotasComprasDet> notasComprasDetCollection;

    public NotasCompras() {
    }

    public NotasCompras(NotasComprasPK notasComprasPK) {
        this.notasComprasPK = notasComprasPK;
    }

    public NotasCompras(NotasComprasPK notasComprasPK, long texentas, long tgravadas, long timpuestos, long isaldo, String cusuario, Date falta, Character mestado, long ttotal) {
        this.notasComprasPK = notasComprasPK;
        this.texentas = texentas;
        this.tgravadas = tgravadas;
        this.timpuestos = timpuestos;
        this.isaldo = isaldo;
        this.cusuario = cusuario;
        this.falta = falta;
        this.mestado = mestado;
        this.ttotal = ttotal;
    }

    public NotasCompras(short codEmpr, long nroNota, short codProveed, String ctipoDocum, Date fdocum) {
        this.notasComprasPK = new NotasComprasPK(codEmpr, nroNota, codProveed, ctipoDocum, fdocum);
    }

    public NotasComprasPK getNotasComprasPK() {
        return notasComprasPK;
    }

    public void setNotasComprasPK(NotasComprasPK notasComprasPK) {
        this.notasComprasPK = notasComprasPK;
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

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    public long getIsaldo() {
        return isaldo;
    }

    public void setIsaldo(long isaldo) {
        this.isaldo = isaldo;
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

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public long getTtotal() {
        return ttotal;
    }

    public void setTtotal(long ttotal) {
        this.ttotal = ttotal;
    }

    public BigInteger getNtimbrado() {
        return ntimbrado;
    }

    public void setNtimbrado(BigInteger ntimbrado) {
        this.ntimbrado = ntimbrado;
    }

    public Compras getCompras() {
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
    }

    public ConceptosDocumentos getConceptosDocumentos() {
        return conceptosDocumentos;
    }

    public void setConceptosDocumentos(ConceptosDocumentos conceptosDocumentos) {
        this.conceptosDocumentos = conceptosDocumentos;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    @XmlTransient
    public Collection<NotasComprasDet> getNotasComprasDetCollection() {
        return notasComprasDetCollection;
    }

    public void setNotasComprasDetCollection(Collection<NotasComprasDet> notasComprasDetCollection) {
        this.notasComprasDetCollection = notasComprasDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notasComprasPK != null ? notasComprasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotasCompras)) {
            return false;
        }
        NotasCompras other = (NotasCompras) object;
        if ((this.notasComprasPK == null && other.notasComprasPK != null) || (this.notasComprasPK != null && !this.notasComprasPK.equals(other.notasComprasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.NotasCompras[ notasComprasPK=" + notasComprasPK + " ]";
    }
    
}
