/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "boletas_depositos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BoletasDepositos.findAll", query = "SELECT b FROM BoletasDepositos b")
    , @NamedQuery(name = "BoletasDepositos.findByCodEmpr", query = "SELECT b FROM BoletasDepositos b WHERE b.boletasDepositosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "BoletasDepositos.findByNroBoleta", query = "SELECT b FROM BoletasDepositos b WHERE b.boletasDepositosPK.nroBoleta = :nroBoleta")
    , @NamedQuery(name = "BoletasDepositos.findByXcuentaBco", query = "SELECT b FROM BoletasDepositos b WHERE b.xcuentaBco = :xcuentaBco")
    , @NamedQuery(name = "BoletasDepositos.findByFdocum", query = "SELECT b FROM BoletasDepositos b WHERE b.fdocum = :fdocum")
    , @NamedQuery(name = "BoletasDepositos.findByTdeposito", query = "SELECT b FROM BoletasDepositos b WHERE b.tdeposito = :tdeposito")
    , @NamedQuery(name = "BoletasDepositos.findByFalta", query = "SELECT b FROM BoletasDepositos b WHERE b.falta = :falta")
    , @NamedQuery(name = "BoletasDepositos.findByCusuario", query = "SELECT b FROM BoletasDepositos b WHERE b.cusuario = :cusuario")})
public class BoletasDepositos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BoletasDepositosPK boletasDepositosPK;
    @Size(max = 20)
    @Column(name = "xcuenta_bco")
    private String xcuentaBco;
    @Column(name = "fdocum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fdocum;
    @Column(name = "tdeposito")
    private Long tdeposito;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "boletasDepositos")
    private Collection<BoletasDet> boletasDetCollection;
    @JoinColumn(name = "cod_banco", referencedColumnName = "cod_banco")
    @ManyToOne(optional = false)
    private Bancos codBanco;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;

    public BoletasDepositos() {
    }

    public BoletasDepositos(BoletasDepositosPK boletasDepositosPK) {
        this.boletasDepositosPK = boletasDepositosPK;
    }

    public BoletasDepositos(short codEmpr, long nroBoleta) {
        this.boletasDepositosPK = new BoletasDepositosPK(codEmpr, nroBoleta);
    }

    public BoletasDepositosPK getBoletasDepositosPK() {
        return boletasDepositosPK;
    }

    public void setBoletasDepositosPK(BoletasDepositosPK boletasDepositosPK) {
        this.boletasDepositosPK = boletasDepositosPK;
    }

    public String getXcuentaBco() {
        return xcuentaBco;
    }

    public void setXcuentaBco(String xcuentaBco) {
        this.xcuentaBco = xcuentaBco;
    }

    public Date getFdocum() {
        return fdocum;
    }

    public void setFdocum(Date fdocum) {
        this.fdocum = fdocum;
    }

    public Long getTdeposito() {
        return tdeposito;
    }

    public void setTdeposito(Long tdeposito) {
        this.tdeposito = tdeposito;
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

    @XmlTransient
    public Collection<BoletasDet> getBoletasDetCollection() {
        return boletasDetCollection;
    }

    public void setBoletasDetCollection(Collection<BoletasDet> boletasDetCollection) {
        this.boletasDetCollection = boletasDetCollection;
    }

    public Bancos getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(Bancos codBanco) {
        this.codBanco = codBanco;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (boletasDepositosPK != null ? boletasDepositosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoletasDepositos)) {
            return false;
        }
        BoletasDepositos other = (BoletasDepositos) object;
        if ((this.boletasDepositosPK == null && other.boletasDepositosPK != null) || (this.boletasDepositosPK != null && !this.boletasDepositosPK.equals(other.boletasDepositosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.BoletasDepositos[ boletasDepositosPK=" + boletasDepositosPK + " ]";
    }
    
}
