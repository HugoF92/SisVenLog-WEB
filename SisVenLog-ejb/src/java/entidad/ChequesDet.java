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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Edu
 */
@Entity
@Table(name = "cheques_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChequesDet.findAll", query = "SELECT c FROM ChequesDet c")
    , @NamedQuery(name = "ChequesDet.findByCodBanco", query = "SELECT c FROM ChequesDet c WHERE c.chequesDetPK.codBanco = :codBanco")
    , @NamedQuery(name = "ChequesDet.findByNroCheque", query = "SELECT c FROM ChequesDet c WHERE c.chequesDetPK.nroCheque = :nroCheque")
    , @NamedQuery(name = "ChequesDet.findByNrofact", query = "SELECT c FROM ChequesDet c WHERE c.chequesDetPK.nrofact = :nrofact")
    , @NamedQuery(name = "ChequesDet.findByCodEmpr", query = "SELECT c FROM ChequesDet c WHERE c.chequesDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "ChequesDet.findByCtipoDocum", query = "SELECT c FROM ChequesDet c WHERE c.chequesDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "ChequesDet.findByIpagado", query = "SELECT c FROM ChequesDet c WHERE c.ipagado = :ipagado")
    , @NamedQuery(name = "ChequesDet.findByIsaldo", query = "SELECT c FROM ChequesDet c WHERE c.isaldo = :isaldo")
    , @NamedQuery(name = "ChequesDet.findByFfactur", query = "SELECT c FROM ChequesDet c WHERE c.ffactur = :ffactur")
    , @NamedQuery(name = "ChequesDet.findByInteres", query = "SELECT c FROM ChequesDet c WHERE c.interes = :interes")
    , @NamedQuery(name = "ChequesDet.findByTtotal", query = "SELECT c FROM ChequesDet c WHERE c.ttotal = :ttotal")})
public class ChequesDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ChequesDetPK chequesDetPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipagado")
    private long ipagado;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "ttotal")
    private long ttotal;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_banco", referencedColumnName = "cod_banco", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_cheque", referencedColumnName = "nro_cheque", insertable = false, updatable = false)
        , @JoinColumn(name = "xcuenta_bco", referencedColumnName = "xcuenta_bco")})
    @ManyToOne(optional = false)
    private Cheques cheques;

    public ChequesDet() {
    }

    public ChequesDet(ChequesDetPK chequesDetPK) {
        this.chequesDetPK = chequesDetPK;
    }

    public ChequesDet(ChequesDetPK chequesDetPK, long ipagado, long isaldo, Date ffactur, long interes, long ttotal) {
        this.chequesDetPK = chequesDetPK;
        this.ipagado = ipagado;
        this.isaldo = isaldo;
        this.ffactur = ffactur;
        this.interes = interes;
        this.ttotal = ttotal;
    }

    public ChequesDet(short codBanco, String nroCheque, long nrofact, short codEmpr, String ctipoDocum) {
        this.chequesDetPK = new ChequesDetPK(codBanco, nroCheque, nrofact, codEmpr, ctipoDocum);
    }

    public ChequesDetPK getChequesDetPK() {
        return chequesDetPK;
    }

    public void setChequesDetPK(ChequesDetPK chequesDetPK) {
        this.chequesDetPK = chequesDetPK;
    }

    public long getIpagado() {
        return ipagado;
    }

    public void setIpagado(long ipagado) {
        this.ipagado = ipagado;
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

    public long getTtotal() {
        return ttotal;
    }

    public void setTtotal(long ttotal) {
        this.ttotal = ttotal;
    }

    public Cheques getCheques() {
        return cheques;
    }

    public void setCheques(Cheques cheques) {
        this.cheques = cheques;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chequesDetPK != null ? chequesDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChequesDet)) {
            return false;
        }
        ChequesDet other = (ChequesDet) object;
        if ((this.chequesDetPK == null && other.chequesDetPK != null) || (this.chequesDetPK != null && !this.chequesDetPK.equals(other.chequesDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ChequesDet[ chequesDetPK=" + chequesDetPK + " ]";
    }
    
}
