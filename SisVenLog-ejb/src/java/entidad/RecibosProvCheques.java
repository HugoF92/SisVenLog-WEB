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
@Table(name = "recibos_prov_cheques")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecibosProvCheques.findAll", query = "SELECT r FROM RecibosProvCheques r")
    , @NamedQuery(name = "RecibosProvCheques.findByCodEmpr", query = "SELECT r FROM RecibosProvCheques r WHERE r.recibosProvChequesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RecibosProvCheques.findByNrecibo", query = "SELECT r FROM RecibosProvCheques r WHERE r.recibosProvChequesPK.nrecibo = :nrecibo")
    , @NamedQuery(name = "RecibosProvCheques.findByCodProveed", query = "SELECT r FROM RecibosProvCheques r WHERE r.recibosProvChequesPK.codProveed = :codProveed")
    , @NamedQuery(name = "RecibosProvCheques.findByCodBanco", query = "SELECT r FROM RecibosProvCheques r WHERE r.recibosProvChequesPK.codBanco = :codBanco")
    , @NamedQuery(name = "RecibosProvCheques.findByNroCheque", query = "SELECT r FROM RecibosProvCheques r WHERE r.recibosProvChequesPK.nroCheque = :nroCheque")
    , @NamedQuery(name = "RecibosProvCheques.findByIpagado", query = "SELECT r FROM RecibosProvCheques r WHERE r.ipagado = :ipagado")
    , @NamedQuery(name = "RecibosProvCheques.findByCusuario", query = "SELECT r FROM RecibosProvCheques r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "RecibosProvCheques.findByFalta", query = "SELECT r FROM RecibosProvCheques r WHERE r.falta = :falta")
    , @NamedQuery(name = "RecibosProvCheques.findByFrecibo", query = "SELECT r FROM RecibosProvCheques r WHERE r.frecibo = :frecibo")})
public class RecibosProvCheques implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecibosProvChequesPK recibosProvChequesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipagado")
    private long ipagado;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "frecibo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frecibo;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nrecibo", referencedColumnName = "nrecibo", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private RecibosProv recibosProv;

    public RecibosProvCheques() {
    }

    public RecibosProvCheques(RecibosProvChequesPK recibosProvChequesPK) {
        this.recibosProvChequesPK = recibosProvChequesPK;
    }

    public RecibosProvCheques(RecibosProvChequesPK recibosProvChequesPK, long ipagado) {
        this.recibosProvChequesPK = recibosProvChequesPK;
        this.ipagado = ipagado;
    }

    public RecibosProvCheques(short codEmpr, long nrecibo, short codProveed, short codBanco, String nroCheque) {
        this.recibosProvChequesPK = new RecibosProvChequesPK(codEmpr, nrecibo, codProveed, codBanco, nroCheque);
    }

    public RecibosProvChequesPK getRecibosProvChequesPK() {
        return recibosProvChequesPK;
    }

    public void setRecibosProvChequesPK(RecibosProvChequesPK recibosProvChequesPK) {
        this.recibosProvChequesPK = recibosProvChequesPK;
    }

    public long getIpagado() {
        return ipagado;
    }

    public void setIpagado(long ipagado) {
        this.ipagado = ipagado;
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
        hash += (recibosProvChequesPK != null ? recibosProvChequesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosProvCheques)) {
            return false;
        }
        RecibosProvCheques other = (RecibosProvCheques) object;
        if ((this.recibosProvChequesPK == null && other.recibosProvChequesPK != null) || (this.recibosProvChequesPK != null && !this.recibosProvChequesPK.equals(other.recibosProvChequesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosProvCheques[ recibosProvChequesPK=" + recibosProvChequesPK + " ]";
    }
    
}
