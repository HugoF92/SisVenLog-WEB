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
@Table(name = "recibos_cheques")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecibosCheques.findAll", query = "SELECT r FROM RecibosCheques r")
    , @NamedQuery(name = "RecibosCheques.findByCodEmpr", query = "SELECT r FROM RecibosCheques r WHERE r.recibosChequesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RecibosCheques.findByNrecibo", query = "SELECT r FROM RecibosCheques r WHERE r.recibosChequesPK.nrecibo = :nrecibo")
    , @NamedQuery(name = "RecibosCheques.findByCodBanco", query = "SELECT r FROM RecibosCheques r WHERE r.recibosChequesPK.codBanco = :codBanco")
    , @NamedQuery(name = "RecibosCheques.findByNroCheque", query = "SELECT r FROM RecibosCheques r WHERE r.recibosChequesPK.nroCheque = :nroCheque")
    , @NamedQuery(name = "RecibosCheques.findByIpagado", query = "SELECT r FROM RecibosCheques r WHERE r.ipagado = :ipagado")
    , @NamedQuery(name = "RecibosCheques.findByCusuario", query = "SELECT r FROM RecibosCheques r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "RecibosCheques.findByFalta", query = "SELECT r FROM RecibosCheques r WHERE r.falta = :falta")})
public class RecibosCheques implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecibosChequesPK recibosChequesPK;
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
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nrecibo", referencedColumnName = "nrecibo", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Recibos recibos;

    public RecibosCheques() {
    }

    public RecibosCheques(RecibosChequesPK recibosChequesPK) {
        this.recibosChequesPK = recibosChequesPK;
    }

    public RecibosCheques(RecibosChequesPK recibosChequesPK, long ipagado) {
        this.recibosChequesPK = recibosChequesPK;
        this.ipagado = ipagado;
    }

    public RecibosCheques(short codEmpr, long nrecibo, short codBanco, String nroCheque) {
        this.recibosChequesPK = new RecibosChequesPK(codEmpr, nrecibo, codBanco, nroCheque);
    }

    public RecibosChequesPK getRecibosChequesPK() {
        return recibosChequesPK;
    }

    public void setRecibosChequesPK(RecibosChequesPK recibosChequesPK) {
        this.recibosChequesPK = recibosChequesPK;
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

    public Recibos getRecibos() {
        return recibos;
    }

    public void setRecibos(Recibos recibos) {
        this.recibos = recibos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recibosChequesPK != null ? recibosChequesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosCheques)) {
            return false;
        }
        RecibosCheques other = (RecibosCheques) object;
        if ((this.recibosChequesPK == null && other.recibosChequesPK != null) || (this.recibosChequesPK != null && !this.recibosChequesPK.equals(other.recibosChequesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosCheques[ recibosChequesPK=" + recibosChequesPK + " ]";
    }
    
}
