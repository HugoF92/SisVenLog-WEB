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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "pagares_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PagaresDet.findAll", query = "SELECT p FROM PagaresDet p")
    , @NamedQuery(name = "PagaresDet.findByCodEmpr", query = "SELECT p FROM PagaresDet p WHERE p.pagaresDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "PagaresDet.findByNpagare", query = "SELECT p FROM PagaresDet p WHERE p.pagaresDetPK.npagare = :npagare")
    , @NamedQuery(name = "PagaresDet.findByNrofact", query = "SELECT p FROM PagaresDet p WHERE p.pagaresDetPK.nrofact = :nrofact")
    , @NamedQuery(name = "PagaresDet.findByCtipoDocum", query = "SELECT p FROM PagaresDet p WHERE p.pagaresDetPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "PagaresDet.findByItotal", query = "SELECT p FROM PagaresDet p WHERE p.itotal = :itotal")
    , @NamedQuery(name = "PagaresDet.findByFfactur", query = "SELECT p FROM PagaresDet p WHERE p.ffactur = :ffactur")
    , @NamedQuery(name = "PagaresDet.findByIsaldo", query = "SELECT p FROM PagaresDet p WHERE p.isaldo = :isaldo")})
public class PagaresDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PagaresDetPK pagaresDetPK;
    @Column(name = "itotal")
    private Long itotal;
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
    @Column(name = "isaldo")
    private Long isaldo;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "npagare", referencedColumnName = "npagare", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Pagares pagares;

    public PagaresDet() {
    }

    public PagaresDet(PagaresDetPK pagaresDetPK) {
        this.pagaresDetPK = pagaresDetPK;
    }

    public PagaresDet(short codEmpr, long npagare, long nrofact, String ctipoDocum) {
        this.pagaresDetPK = new PagaresDetPK(codEmpr, npagare, nrofact, ctipoDocum);
    }

    public PagaresDetPK getPagaresDetPK() {
        return pagaresDetPK;
    }

    public void setPagaresDetPK(PagaresDetPK pagaresDetPK) {
        this.pagaresDetPK = pagaresDetPK;
    }

    public Long getItotal() {
        return itotal;
    }

    public void setItotal(Long itotal) {
        this.itotal = itotal;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public Long getIsaldo() {
        return isaldo;
    }

    public void setIsaldo(Long isaldo) {
        this.isaldo = isaldo;
    }

    public Pagares getPagares() {
        return pagares;
    }

    public void setPagares(Pagares pagares) {
        this.pagares = pagares;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagaresDetPK != null ? pagaresDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagaresDet)) {
            return false;
        }
        PagaresDet other = (PagaresDet) object;
        if ((this.pagaresDetPK == null && other.pagaresDetPK != null) || (this.pagaresDetPK != null && !this.pagaresDetPK.equals(other.pagaresDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PagaresDet[ pagaresDetPK=" + pagaresDetPK + " ]";
    }
    
}
