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
@Table(name = "cheques_emitidos_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChequesEmitidosDet.findAll", query = "SELECT c FROM ChequesEmitidosDet c")
    , @NamedQuery(name = "ChequesEmitidosDet.findByCodEmpr", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.chequesEmitidosDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "ChequesEmitidosDet.findByCodBanco", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.chequesEmitidosDetPK.codBanco = :codBanco")
    , @NamedQuery(name = "ChequesEmitidosDet.findByNroCheque", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.chequesEmitidosDetPK.nroCheque = :nroCheque")
    , @NamedQuery(name = "ChequesEmitidosDet.findByNrofact", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.chequesEmitidosDetPK.nrofact = :nrofact")
    , @NamedQuery(name = "ChequesEmitidosDet.findByCtipoDocum", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.chequesEmitidosDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "ChequesEmitidosDet.findByCodProveed", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.codProveed = :codProveed")
    , @NamedQuery(name = "ChequesEmitidosDet.findByIpagado", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.ipagado = :ipagado")
    , @NamedQuery(name = "ChequesEmitidosDet.findByIsaldo", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.isaldo = :isaldo")
    , @NamedQuery(name = "ChequesEmitidosDet.findByFfactur", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.ffactur = :ffactur")
    , @NamedQuery(name = "ChequesEmitidosDet.findByInteres", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.interes = :interes")
    , @NamedQuery(name = "ChequesEmitidosDet.findByTtotal", query = "SELECT c FROM ChequesEmitidosDet c WHERE c.ttotal = :ttotal")})
public class ChequesEmitidosDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ChequesEmitidosDetPK chequesEmitidosDetPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_proveed")
    private short codProveed;
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
        , @JoinColumn(name = "nro_cheque", referencedColumnName = "nro_cheque", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private ChequesEmitidos chequesEmitidos;

    public ChequesEmitidosDet() {
    }

    public ChequesEmitidosDet(ChequesEmitidosDetPK chequesEmitidosDetPK) {
        this.chequesEmitidosDetPK = chequesEmitidosDetPK;
    }

    public ChequesEmitidosDet(ChequesEmitidosDetPK chequesEmitidosDetPK, short codProveed, long ipagado, long isaldo, Date ffactur, long interes, long ttotal) {
        this.chequesEmitidosDetPK = chequesEmitidosDetPK;
        this.codProveed = codProveed;
        this.ipagado = ipagado;
        this.isaldo = isaldo;
        this.ffactur = ffactur;
        this.interes = interes;
        this.ttotal = ttotal;
    }

    public ChequesEmitidosDet(short codEmpr, short codBanco, String nroCheque, long nrofact, String ctipoDocum) {
        this.chequesEmitidosDetPK = new ChequesEmitidosDetPK(codEmpr, codBanco, nroCheque, nrofact, ctipoDocum);
    }

    public ChequesEmitidosDetPK getChequesEmitidosDetPK() {
        return chequesEmitidosDetPK;
    }

    public void setChequesEmitidosDetPK(ChequesEmitidosDetPK chequesEmitidosDetPK) {
        this.chequesEmitidosDetPK = chequesEmitidosDetPK;
    }

    public short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(short codProveed) {
        this.codProveed = codProveed;
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

    public ChequesEmitidos getChequesEmitidos() {
        return chequesEmitidos;
    }

    public void setChequesEmitidos(ChequesEmitidos chequesEmitidos) {
        this.chequesEmitidos = chequesEmitidos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chequesEmitidosDetPK != null ? chequesEmitidosDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChequesEmitidosDet)) {
            return false;
        }
        ChequesEmitidosDet other = (ChequesEmitidosDet) object;
        if ((this.chequesEmitidosDetPK == null && other.chequesEmitidosDetPK != null) || (this.chequesEmitidosDetPK != null && !this.chequesEmitidosDetPK.equals(other.chequesEmitidosDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ChequesEmitidosDet[ chequesEmitidosDetPK=" + chequesEmitidosDetPK + " ]";
    }
    
}
