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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "existencias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Existencias.findAll", query = "SELECT e FROM Existencias e")
    , @NamedQuery(name = "Existencias.findByCodEmpr", query = "SELECT e FROM Existencias e WHERE e.existenciasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Existencias.findByCodDepo", query = "SELECT e FROM Existencias e WHERE e.existenciasPK.codDepo = :codDepo")
    , @NamedQuery(name = "Existencias.findByCodMerca", query = "SELECT e FROM Existencias e WHERE e.existenciasPK.codMerca = :codMerca")
    , @NamedQuery(name = "Existencias.findByCantCajas", query = "SELECT e FROM Existencias e WHERE e.cantCajas = :cantCajas")
    , @NamedQuery(name = "Existencias.findByCantUnid", query = "SELECT e FROM Existencias e WHERE e.cantUnid = :cantUnid")
    , @NamedQuery(name = "Existencias.findByReserCajas", query = "SELECT e FROM Existencias e WHERE e.reserCajas = :reserCajas")
    , @NamedQuery(name = "Existencias.findByReserUnid", query = "SELECT e FROM Existencias e WHERE e.reserUnid = :reserUnid")
    , @NamedQuery(name = "Existencias.findByFanul", query = "SELECT e FROM Existencias e WHERE e.fanul = :fanul")
    , @NamedQuery(name = "Existencias.findByNrelacion", query = "SELECT e FROM Existencias e WHERE e.nrelacion = :nrelacion")
    , @NamedQuery(name = "Existencias.findByNunidMinima", query = "SELECT e FROM Existencias e WHERE e.nunidMinima = :nunidMinima")
    , @NamedQuery(name = "Existencias.findByNunidMaxima", query = "SELECT e FROM Existencias e WHERE e.nunidMaxima = :nunidMaxima")
    , @NamedQuery(name = "Existencias.findByNunidIdeal", query = "SELECT e FROM Existencias e WHERE e.nunidIdeal = :nunidIdeal")
    , @NamedQuery(name = "Existencias.findByOldCajas", query = "SELECT e FROM Existencias e WHERE e.oldCajas = :oldCajas")})
public class Existencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExistenciasPK existenciasPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_cajas")
    private long cantCajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_unid")
    private long cantUnid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reser_cajas")
    private long reserCajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reser_unid")
    private long reserUnid;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Column(name = "nrelacion")
    private Short nrelacion;
    @Column(name = "nunid_minima")
    private Long nunidMinima;
    @Column(name = "nunid_maxima")
    private Long nunidMaxima;
    @Column(name = "nunid_ideal")
    private Long nunidIdeal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "old_cajas")
    private long oldCajas;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_depo", referencedColumnName = "cod_depo", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Depositos depositos;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "existencias")
    private Collection<RetornosPrecios> retornosPreciosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "existencias")
    private Collection<Precios> preciosCollection;

    public Existencias() {
    }

    public Existencias(ExistenciasPK existenciasPK) {
        this.existenciasPK = existenciasPK;
    }

    public Existencias(ExistenciasPK existenciasPK, long cantCajas, long cantUnid, long reserCajas, long reserUnid, long oldCajas) {
        this.existenciasPK = existenciasPK;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.reserCajas = reserCajas;
        this.reserUnid = reserUnid;
        this.oldCajas = oldCajas;
    }

    public Existencias(short codEmpr, short codDepo, String codMerca) {
        this.existenciasPK = new ExistenciasPK(codEmpr, codDepo, codMerca);
    }

    public ExistenciasPK getExistenciasPK() {
        return existenciasPK;
    }

    public void setExistenciasPK(ExistenciasPK existenciasPK) {
        this.existenciasPK = existenciasPK;
    }

    public long getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(long cantCajas) {
        this.cantCajas = cantCajas;
    }

    public long getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(long cantUnid) {
        this.cantUnid = cantUnid;
    }

    public long getReserCajas() {
        return reserCajas;
    }

    public void setReserCajas(long reserCajas) {
        this.reserCajas = reserCajas;
    }

    public long getReserUnid() {
        return reserUnid;
    }

    public void setReserUnid(long reserUnid) {
        this.reserUnid = reserUnid;
    }

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    public Short getNrelacion() {
        return nrelacion;
    }

    public void setNrelacion(Short nrelacion) {
        this.nrelacion = nrelacion;
    }

    public Long getNunidMinima() {
        return nunidMinima;
    }

    public void setNunidMinima(Long nunidMinima) {
        this.nunidMinima = nunidMinima;
    }

    public Long getNunidMaxima() {
        return nunidMaxima;
    }

    public void setNunidMaxima(Long nunidMaxima) {
        this.nunidMaxima = nunidMaxima;
    }

    public Long getNunidIdeal() {
        return nunidIdeal;
    }

    public void setNunidIdeal(Long nunidIdeal) {
        this.nunidIdeal = nunidIdeal;
    }

    public long getOldCajas() {
        return oldCajas;
    }

    public void setOldCajas(long oldCajas) {
        this.oldCajas = oldCajas;
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

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    @XmlTransient
    public Collection<RetornosPrecios> getRetornosPreciosCollection() {
        return retornosPreciosCollection;
    }

    public void setRetornosPreciosCollection(Collection<RetornosPrecios> retornosPreciosCollection) {
        this.retornosPreciosCollection = retornosPreciosCollection;
    }

    @XmlTransient
    public Collection<Precios> getPreciosCollection() {
        return preciosCollection;
    }

    public void setPreciosCollection(Collection<Precios> preciosCollection) {
        this.preciosCollection = preciosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (existenciasPK != null ? existenciasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Existencias)) {
            return false;
        }
        Existencias other = (Existencias) object;
        if ((this.existenciasPK == null && other.existenciasPK != null) || (this.existenciasPK != null && !this.existenciasPK.equals(other.existenciasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Existencias[ existenciasPK=" + existenciasPK + " ]";
    }
    
}
