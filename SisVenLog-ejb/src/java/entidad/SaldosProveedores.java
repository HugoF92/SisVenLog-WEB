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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "saldos_proveedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SaldosProveedores.findAll", query = "SELECT s FROM SaldosProveedores s")
    , @NamedQuery(name = "SaldosProveedores.findByCodEmpr", query = "SELECT s FROM SaldosProveedores s WHERE s.saldosProveedoresPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "SaldosProveedores.findByCodProveed", query = "SELECT s FROM SaldosProveedores s WHERE s.saldosProveedoresPK.codProveed = :codProveed")
    , @NamedQuery(name = "SaldosProveedores.findByFmovim", query = "SELECT s FROM SaldosProveedores s WHERE s.saldosProveedoresPK.fmovim = :fmovim")
    , @NamedQuery(name = "SaldosProveedores.findByNmes", query = "SELECT s FROM SaldosProveedores s WHERE s.nmes = :nmes")
    , @NamedQuery(name = "SaldosProveedores.findByIsaldo", query = "SELECT s FROM SaldosProveedores s WHERE s.isaldo = :isaldo")
    , @NamedQuery(name = "SaldosProveedores.findByAcumDebi", query = "SELECT s FROM SaldosProveedores s WHERE s.acumDebi = :acumDebi")
    , @NamedQuery(name = "SaldosProveedores.findByAcumCredi", query = "SELECT s FROM SaldosProveedores s WHERE s.acumCredi = :acumCredi")
    , @NamedQuery(name = "SaldosProveedores.findByCcanalCompra", query = "SELECT s FROM SaldosProveedores s WHERE s.ccanalCompra = :ccanalCompra")})
public class SaldosProveedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SaldosProveedoresPK saldosProveedoresPK;
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
    @Size(max = 2)
    @Column(name = "ccanal_compra")
    private String ccanalCompra;

    public SaldosProveedores() {
    }

    public SaldosProveedores(SaldosProveedoresPK saldosProveedoresPK) {
        this.saldosProveedoresPK = saldosProveedoresPK;
    }

    public SaldosProveedores(SaldosProveedoresPK saldosProveedoresPK, short nmes, long isaldo, long acumDebi, long acumCredi) {
        this.saldosProveedoresPK = saldosProveedoresPK;
        this.nmes = nmes;
        this.isaldo = isaldo;
        this.acumDebi = acumDebi;
        this.acumCredi = acumCredi;
    }

    public SaldosProveedores(short codEmpr, short codProveed, Date fmovim) {
        this.saldosProveedoresPK = new SaldosProveedoresPK(codEmpr, codProveed, fmovim);
    }

    public SaldosProveedoresPK getSaldosProveedoresPK() {
        return saldosProveedoresPK;
    }

    public void setSaldosProveedoresPK(SaldosProveedoresPK saldosProveedoresPK) {
        this.saldosProveedoresPK = saldosProveedoresPK;
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

    public String getCcanalCompra() {
        return ccanalCompra;
    }

    public void setCcanalCompra(String ccanalCompra) {
        this.ccanalCompra = ccanalCompra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saldosProveedoresPK != null ? saldosProveedoresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaldosProveedores)) {
            return false;
        }
        SaldosProveedores other = (SaldosProveedores) object;
        if ((this.saldosProveedoresPK == null && other.saldosProveedoresPK != null) || (this.saldosProveedoresPK != null && !this.saldosProveedoresPK.equals(other.saldosProveedoresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.SaldosProveedores[ saldosProveedoresPK=" + saldosProveedoresPK + " ]";
    }
    
}
