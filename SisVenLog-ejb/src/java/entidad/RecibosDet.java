/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "recibos_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecibosDet.findAll", query = "SELECT r FROM RecibosDet r")
    , @NamedQuery(name = "RecibosDet.findByCodEmpr", query = "SELECT r FROM RecibosDet r WHERE r.recibosDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RecibosDet.findByNrecibo", query = "SELECT r FROM RecibosDet r WHERE r.recibosDetPK.nrecibo = :nrecibo")
    , @NamedQuery(name = "RecibosDet.findByNdocum", query = "SELECT r FROM RecibosDet r WHERE r.recibosDetPK.ndocum = :ndocum")
    , @NamedQuery(name = "RecibosDet.findByCtipoDocum", query = "SELECT r FROM RecibosDet r WHERE r.recibosDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "RecibosDet.findByItotal", query = "SELECT r FROM RecibosDet r WHERE r.itotal = :itotal")
    , @NamedQuery(name = "RecibosDet.findByTtotal", query = "SELECT r FROM RecibosDet r WHERE r.ttotal = :ttotal")
    , @NamedQuery(name = "RecibosDet.findByIsaldo", query = "SELECT r FROM RecibosDet r WHERE r.isaldo = :isaldo")
    , @NamedQuery(name = "RecibosDet.findByFfactur", query = "SELECT r FROM RecibosDet r WHERE r.ffactur = :ffactur")
    , @NamedQuery(name = "RecibosDet.findByInteres", query = "SELECT r FROM RecibosDet r WHERE r.interes = :interes")
    , @NamedQuery(name = "RecibosDet.findByFalta", query = "SELECT r FROM RecibosDet r WHERE r.falta = :falta")
    , @NamedQuery(name = "RecibosDet.findByCusuario", query = "SELECT r FROM RecibosDet r WHERE r.cusuario = :cusuario")})
public class RecibosDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecibosDetPK recibosDetPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "itotal")
    private long itotal;
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
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
    @Basic(optional = false)
    @NotNull
    @Column(name = "interes")
    private long interes;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nrecibo", referencedColumnName = "nrecibo", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Recibos recibos;

    public RecibosDet() {
    }

    public RecibosDet(RecibosDetPK recibosDetPK) {
        this.recibosDetPK = recibosDetPK;
    }

    public RecibosDet(RecibosDetPK recibosDetPK, long itotal, long ttotal, long isaldo, Date ffactur, long interes) {
        this.recibosDetPK = recibosDetPK;
        this.itotal = itotal;
        this.ttotal = ttotal;
        this.isaldo = isaldo;
        this.ffactur = ffactur;
        this.interes = interes;
    }

    public RecibosDet(short codEmpr, long nrecibo, long ndocum, String ctipoDocum) {
        this.recibosDetPK = new RecibosDetPK(codEmpr, nrecibo, ndocum, ctipoDocum);
    }

    public RecibosDetPK getRecibosDetPK() {
        return recibosDetPK;
    }

    public void setRecibosDetPK(RecibosDetPK recibosDetPK) {
        this.recibosDetPK = recibosDetPK;
    }

    public long getItotal() {
        return itotal;
    }

    public void setItotal(long itotal) {
        this.itotal = itotal;
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

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public long getInteres() {
        return interes;
    }

    public void setInteres(long interes) {
        this.interes = interes;
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

    public Recibos getRecibos() {
        return recibos;
    }

    public void setRecibos(Recibos recibos) {
        this.recibos = recibos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recibosDetPK != null ? recibosDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosDet)) {
            return false;
        }
        RecibosDet other = (RecibosDet) object;
        if ((this.recibosDetPK == null && other.recibosDetPK != null) || (this.recibosDetPK != null && !this.recibosDetPK.equals(other.recibosDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosDet[ recibosDetPK=" + recibosDetPK + " ]";
    }
    
}
