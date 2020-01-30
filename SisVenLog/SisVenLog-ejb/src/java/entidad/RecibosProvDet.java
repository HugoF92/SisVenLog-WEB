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
@Table(name = "recibos_prov_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecibosProvDet.findAll", query = "SELECT r FROM RecibosProvDet r")
    , @NamedQuery(name = "RecibosProvDet.findByCodEmpr", query = "SELECT r FROM RecibosProvDet r WHERE r.recibosProvDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RecibosProvDet.findByNrecibo", query = "SELECT r FROM RecibosProvDet r WHERE r.recibosProvDetPK.nrecibo = :nrecibo")
    , @NamedQuery(name = "RecibosProvDet.findByCodProveed", query = "SELECT r FROM RecibosProvDet r WHERE r.recibosProvDetPK.codProveed = :codProveed")
    , @NamedQuery(name = "RecibosProvDet.findByCtipoDocum", query = "SELECT r FROM RecibosProvDet r WHERE r.recibosProvDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "RecibosProvDet.findByNrofact", query = "SELECT r FROM RecibosProvDet r WHERE r.recibosProvDetPK.nrofact = :nrofact")
    , @NamedQuery(name = "RecibosProvDet.findByItotal", query = "SELECT r FROM RecibosProvDet r WHERE r.itotal = :itotal")
    , @NamedQuery(name = "RecibosProvDet.findByTtotal", query = "SELECT r FROM RecibosProvDet r WHERE r.ttotal = :ttotal")
    , @NamedQuery(name = "RecibosProvDet.findByIsaldo", query = "SELECT r FROM RecibosProvDet r WHERE r.isaldo = :isaldo")
    , @NamedQuery(name = "RecibosProvDet.findByFfactur", query = "SELECT r FROM RecibosProvDet r WHERE r.ffactur = :ffactur")
    , @NamedQuery(name = "RecibosProvDet.findByInteres", query = "SELECT r FROM RecibosProvDet r WHERE r.interes = :interes")
    , @NamedQuery(name = "RecibosProvDet.findByFalta", query = "SELECT r FROM RecibosProvDet r WHERE r.falta = :falta")
    , @NamedQuery(name = "RecibosProvDet.findByCusuario", query = "SELECT r FROM RecibosProvDet r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "RecibosProvDet.findByFrecibo", query = "SELECT r FROM RecibosProvDet r WHERE r.frecibo = :frecibo")})
public class RecibosProvDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecibosProvDetPK recibosProvDetPK;
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
    @Column(name = "frecibo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frecibo;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nrecibo", referencedColumnName = "nrecibo", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private RecibosProv recibosProv;

    public RecibosProvDet() {
    }

    public RecibosProvDet(RecibosProvDetPK recibosProvDetPK) {
        this.recibosProvDetPK = recibosProvDetPK;
    }

    public RecibosProvDet(RecibosProvDetPK recibosProvDetPK, long itotal, long ttotal, long isaldo, Date ffactur, long interes) {
        this.recibosProvDetPK = recibosProvDetPK;
        this.itotal = itotal;
        this.ttotal = ttotal;
        this.isaldo = isaldo;
        this.ffactur = ffactur;
        this.interes = interes;
    }

    public RecibosProvDet(short codEmpr, long nrecibo, short codProveed, String ctipoDocum, long nrofact) {
        this.recibosProvDetPK = new RecibosProvDetPK(codEmpr, nrecibo, codProveed, ctipoDocum, nrofact);
    }

    public RecibosProvDetPK getRecibosProvDetPK() {
        return recibosProvDetPK;
    }

    public void setRecibosProvDetPK(RecibosProvDetPK recibosProvDetPK) {
        this.recibosProvDetPK = recibosProvDetPK;
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

    public Date getFrecibo() {
        return frecibo;
    }

    public void setFrecibo(Date frecibo) {
        this.frecibo = frecibo;
    }

    public RecibosProv getRecibosProv() {
        return recibosProv;
    }

    public void setRecibosProv(RecibosProv recibosProv) {
        this.recibosProv = recibosProv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recibosProvDetPK != null ? recibosProvDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosProvDet)) {
            return false;
        }
        RecibosProvDet other = (RecibosProvDet) object;
        if ((this.recibosProvDetPK == null && other.recibosProvDetPK != null) || (this.recibosProvDetPK != null && !this.recibosProvDetPK.equals(other.recibosProvDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosProvDet[ recibosProvDetPK=" + recibosProvDetPK + " ]";
    }
    
}
