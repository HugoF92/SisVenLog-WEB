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
@Table(name = "plazos_sublineas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlazosSublineas.findAll", query = "SELECT p FROM PlazosSublineas p")
    , @NamedQuery(name = "PlazosSublineas.findByCodCliente", query = "SELECT p FROM PlazosSublineas p WHERE p.plazosSublineasPK.codCliente = :codCliente")
    , @NamedQuery(name = "PlazosSublineas.findByCodSublinea", query = "SELECT p FROM PlazosSublineas p WHERE p.plazosSublineasPK.codSublinea = :codSublinea")
    , @NamedQuery(name = "PlazosSublineas.findByNplazoCredito", query = "SELECT p FROM PlazosSublineas p WHERE p.nplazoCredito = :nplazoCredito")
    , @NamedQuery(name = "PlazosSublineas.findByFrigeDesde", query = "SELECT p FROM PlazosSublineas p WHERE p.plazosSublineasPK.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "PlazosSublineas.findByFrigeHasta", query = "SELECT p FROM PlazosSublineas p WHERE p.frigeHasta = :frigeHasta")})
public class PlazosSublineas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlazosSublineasPK plazosSublineasPK;
    @Column(name = "nplazo_credito")
    private Short nplazoCredito;
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;

    public PlazosSublineas() {
    }

    public PlazosSublineas(PlazosSublineasPK plazosSublineasPK) {
        this.plazosSublineasPK = plazosSublineasPK;
    }

    public PlazosSublineas(int codCliente, short codSublinea, Date frigeDesde) {
        this.plazosSublineasPK = new PlazosSublineasPK(codCliente, codSublinea, frigeDesde);
    }

    public PlazosSublineasPK getPlazosSublineasPK() {
        return plazosSublineasPK;
    }

    public void setPlazosSublineasPK(PlazosSublineasPK plazosSublineasPK) {
        this.plazosSublineasPK = plazosSublineasPK;
    }

    public Short getNplazoCredito() {
        return nplazoCredito;
    }

    public void setNplazoCredito(Short nplazoCredito) {
        this.nplazoCredito = nplazoCredito;
    }

    public Date getFrigeHasta() {
        return frigeHasta;
    }

    public void setFrigeHasta(Date frigeHasta) {
        this.frigeHasta = frigeHasta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plazosSublineasPK != null ? plazosSublineasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlazosSublineas)) {
            return false;
        }
        PlazosSublineas other = (PlazosSublineas) object;
        if ((this.plazosSublineasPK == null && other.plazosSublineasPK != null) || (this.plazosSublineasPK != null && !this.plazosSublineasPK.equals(other.plazosSublineasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PlazosSublineas[ plazosSublineasPK=" + plazosSublineasPK + " ]";
    }
    
}
