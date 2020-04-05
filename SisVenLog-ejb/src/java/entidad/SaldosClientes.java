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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "saldos_clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SaldosClientes.findAll", query = "SELECT s FROM SaldosClientes s")
    , @NamedQuery(name = "SaldosClientes.findByCodCliente", query = "SELECT s FROM SaldosClientes s WHERE s.saldosClientesPK.codCliente = :codCliente")
    , @NamedQuery(name = "SaldosClientes.findByFmovim", query = "SELECT s FROM SaldosClientes s WHERE s.saldosClientesPK.fmovim = :fmovim")
    , @NamedQuery(name = "SaldosClientes.findByFmovimFin", query = "SELECT s FROM SaldosClientes s WHERE s.fmovimFin = :fmovimFin")
    , @NamedQuery(name = "SaldosClientes.findByNmes", query = "SELECT s FROM SaldosClientes s WHERE s.nmes = :nmes")
    , @NamedQuery(name = "SaldosClientes.findByIsaldo", query = "SELECT s FROM SaldosClientes s WHERE s.isaldo = :isaldo")
    , @NamedQuery(name = "SaldosClientes.findByAcumDebi", query = "SELECT s FROM SaldosClientes s WHERE s.acumDebi = :acumDebi")
    , @NamedQuery(name = "SaldosClientes.findByAcumCredi", query = "SELECT s FROM SaldosClientes s WHERE s.acumCredi = :acumCredi")
    , @NamedQuery(name = "SaldosClientes.findByCodEmpr", query = "SELECT s FROM SaldosClientes s WHERE s.saldosClientesPK.codEmpr = :codEmpr")})
public class SaldosClientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SaldosClientesPK saldosClientesPK;
    @Column(name = "fmovim_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fmovimFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nmes")
    private short nmes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isaldo")
    private long isaldo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "acum_debi")
    private long acumDebi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "acum_credi")
    private long acumCredi;

    public SaldosClientes() {
    }

    public SaldosClientes(SaldosClientesPK saldosClientesPK) {
        this.saldosClientesPK = saldosClientesPK;
    }

    public SaldosClientes(SaldosClientesPK saldosClientesPK, short nmes, long isaldo, long acumDebi, long acumCredi) {
        this.saldosClientesPK = saldosClientesPK;
        this.nmes = nmes;
        this.isaldo = isaldo;
        this.acumDebi = acumDebi;
        this.acumCredi = acumCredi;
    }

    public SaldosClientes(int codCliente, Date fmovim, short codEmpr) {
        this.saldosClientesPK = new SaldosClientesPK(codCliente, fmovim, codEmpr);
    }

    public SaldosClientesPK getSaldosClientesPK() {
        return saldosClientesPK;
    }

    public void setSaldosClientesPK(SaldosClientesPK saldosClientesPK) {
        this.saldosClientesPK = saldosClientesPK;
    }

    public Date getFmovimFin() {
        return fmovimFin;
    }

    public void setFmovimFin(Date fmovimFin) {
        this.fmovimFin = fmovimFin;
    }

    public short getNmes() {
        return nmes;
    }

    public void setNmes(short nmes) {
        this.nmes = nmes;
    }

    public long getIsaldo() {
        return isaldo;
    }

    public void setIsaldo(long isaldo) {
        this.isaldo = isaldo;
    }

    public long getAcumDebi() {
        return acumDebi;
    }

    public void setAcumDebi(long acumDebi) {
        this.acumDebi = acumDebi;
    }

    public long getAcumCredi() {
        return acumCredi;
    }

    public void setAcumCredi(long acumCredi) {
        this.acumCredi = acumCredi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saldosClientesPK != null ? saldosClientesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaldosClientes)) {
            return false;
        }
        SaldosClientes other = (SaldosClientes) object;
        if ((this.saldosClientesPK == null && other.saldosClientesPK != null) || (this.saldosClientesPK != null && !this.saldosClientesPK.equals(other.saldosClientesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.SaldosClientes[ saldosClientesPK=" + saldosClientesPK + " ]";
    }
    
}
