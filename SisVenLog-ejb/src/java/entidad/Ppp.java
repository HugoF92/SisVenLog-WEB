/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "ppp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ppp.findAll", query = "SELECT p FROM Ppp p")
    , @NamedQuery(name = "Ppp.findByCodEmpr", query = "SELECT p FROM Ppp p WHERE p.pppPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Ppp.findByCodMerca", query = "SELECT p FROM Ppp p WHERE p.pppPK.codMerca = :codMerca")
    , @NamedQuery(name = "Ppp.findByFppp", query = "SELECT p FROM Ppp p WHERE p.pppPK.fppp = :fppp")
    , @NamedQuery(name = "Ppp.findByNprecioPpp", query = "SELECT p FROM Ppp p WHERE p.nprecioPpp = :nprecioPpp")
    , @NamedQuery(name = "Ppp.findByNpppCaja", query = "SELECT p FROM Ppp p WHERE p.npppCaja = :npppCaja")
    , @NamedQuery(name = "Ppp.findByFalta", query = "SELECT p FROM Ppp p WHERE p.falta = :falta")
    , @NamedQuery(name = "Ppp.findByCusuario", query = "SELECT p FROM Ppp p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "Ppp.findByNcajasPpp", query = "SELECT p FROM Ppp p WHERE p.ncajasPpp = :ncajasPpp")
    , @NamedQuery(name = "Ppp.findByNunidPpp", query = "SELECT p FROM Ppp p WHERE p.nunidPpp = :nunidPpp")})
public class Ppp implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PppPK pppPK;
    @Column(name = "nprecio_ppp")
    private Integer nprecioPpp;
    @Column(name = "nppp_caja")
    private Long npppCaja;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "ncajas_ppp")
    private Long ncajasPpp;
    @Column(name = "nunid_ppp")
    private Long nunidPpp;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;

    public Ppp() {
    }

    public Ppp(PppPK pppPK) {
        this.pppPK = pppPK;
    }

    public Ppp(short codEmpr, String codMerca, Date fppp) {
        this.pppPK = new PppPK(codEmpr, codMerca, fppp);
    }

    public PppPK getPppPK() {
        return pppPK;
    }

    public void setPppPK(PppPK pppPK) {
        this.pppPK = pppPK;
    }

    public Integer getNprecioPpp() {
        return nprecioPpp;
    }

    public void setNprecioPpp(Integer nprecioPpp) {
        this.nprecioPpp = nprecioPpp;
    }

    public Long getNpppCaja() {
        return npppCaja;
    }

    public void setNpppCaja(Long npppCaja) {
        this.npppCaja = npppCaja;
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

    public Long getNcajasPpp() {
        return ncajasPpp;
    }

    public void setNcajasPpp(Long ncajasPpp) {
        this.ncajasPpp = ncajasPpp;
    }

    public Long getNunidPpp() {
        return nunidPpp;
    }

    public void setNunidPpp(Long nunidPpp) {
        this.nunidPpp = nunidPpp;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pppPK != null ? pppPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ppp)) {
            return false;
        }
        Ppp other = (Ppp) object;
        if ((this.pppPK == null && other.pppPK != null) || (this.pppPK != null && !this.pppPK.equals(other.pppPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Ppp[ pppPK=" + pppPK + " ]";
    }
    
}
