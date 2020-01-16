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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "existencias_saldos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExistenciasSaldos.findAll", query = "SELECT e FROM ExistenciasSaldos e")
    , @NamedQuery(name = "ExistenciasSaldos.findByCodEmpr", query = "SELECT e FROM ExistenciasSaldos e WHERE e.existenciasSaldosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "ExistenciasSaldos.findByCodDepo", query = "SELECT e FROM ExistenciasSaldos e WHERE e.existenciasSaldosPK.codDepo = :codDepo")
    , @NamedQuery(name = "ExistenciasSaldos.findByCodMerca", query = "SELECT e FROM ExistenciasSaldos e WHERE e.existenciasSaldosPK.codMerca = :codMerca")
    , @NamedQuery(name = "ExistenciasSaldos.findByCantCajas", query = "SELECT e FROM ExistenciasSaldos e WHERE e.cantCajas = :cantCajas")
    , @NamedQuery(name = "ExistenciasSaldos.findByCantUnid", query = "SELECT e FROM ExistenciasSaldos e WHERE e.cantUnid = :cantUnid")
    , @NamedQuery(name = "ExistenciasSaldos.findByNprecioPpp", query = "SELECT e FROM ExistenciasSaldos e WHERE e.nprecioPpp = :nprecioPpp")
    , @NamedQuery(name = "ExistenciasSaldos.findByFmovim", query = "SELECT e FROM ExistenciasSaldos e WHERE e.existenciasSaldosPK.fmovim = :fmovim")
    , @NamedQuery(name = "ExistenciasSaldos.findBySaldoCajas", query = "SELECT e FROM ExistenciasSaldos e WHERE e.saldoCajas = :saldoCajas")
    , @NamedQuery(name = "ExistenciasSaldos.findBySaldoUnid", query = "SELECT e FROM ExistenciasSaldos e WHERE e.saldoUnid = :saldoUnid")})
public class ExistenciasSaldos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExistenciasSaldosPK existenciasSaldosPK;
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
    @Column(name = "nprecio_ppp")
    private int nprecioPpp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "saldo_cajas")
    private long saldoCajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "saldo_unid")
    private long saldoUnid;

    public ExistenciasSaldos() {
    }

    public ExistenciasSaldos(ExistenciasSaldosPK existenciasSaldosPK) {
        this.existenciasSaldosPK = existenciasSaldosPK;
    }

    public ExistenciasSaldos(ExistenciasSaldosPK existenciasSaldosPK, long cantCajas, long cantUnid, int nprecioPpp, long saldoCajas, long saldoUnid) {
        this.existenciasSaldosPK = existenciasSaldosPK;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.nprecioPpp = nprecioPpp;
        this.saldoCajas = saldoCajas;
        this.saldoUnid = saldoUnid;
    }

    public ExistenciasSaldos(short codEmpr, short codDepo, String codMerca, Date fmovim) {
        this.existenciasSaldosPK = new ExistenciasSaldosPK(codEmpr, codDepo, codMerca, fmovim);
    }

    public ExistenciasSaldosPK getExistenciasSaldosPK() {
        return existenciasSaldosPK;
    }

    public void setExistenciasSaldosPK(ExistenciasSaldosPK existenciasSaldosPK) {
        this.existenciasSaldosPK = existenciasSaldosPK;
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

    public int getNprecioPpp() {
        return nprecioPpp;
    }

    public void setNprecioPpp(int nprecioPpp) {
        this.nprecioPpp = nprecioPpp;
    }

    public long getSaldoCajas() {
        return saldoCajas;
    }

    public void setSaldoCajas(long saldoCajas) {
        this.saldoCajas = saldoCajas;
    }

    public long getSaldoUnid() {
        return saldoUnid;
    }

    public void setSaldoUnid(long saldoUnid) {
        this.saldoUnid = saldoUnid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (existenciasSaldosPK != null ? existenciasSaldosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExistenciasSaldos)) {
            return false;
        }
        ExistenciasSaldos other = (ExistenciasSaldos) object;
        if ((this.existenciasSaldosPK == null && other.existenciasSaldosPK != null) || (this.existenciasSaldosPK != null && !this.existenciasSaldosPK.equals(other.existenciasSaldosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ExistenciasSaldos[ existenciasSaldosPK=" + existenciasSaldosPK + " ]";
    }
    
}
