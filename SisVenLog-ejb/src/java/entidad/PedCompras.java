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
@Table(name = "ped_compras")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedCompras.findAll", query = "SELECT p FROM PedCompras p")
    , @NamedQuery(name = "PedCompras.findByCodEmpr", query = "SELECT p FROM PedCompras p WHERE p.pedComprasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "PedCompras.findByNroPedido", query = "SELECT p FROM PedCompras p WHERE p.pedComprasPK.nroPedido = :nroPedido")
    , @NamedQuery(name = "PedCompras.findByCodProveed", query = "SELECT p FROM PedCompras p WHERE p.codProveed = :codProveed")
    , @NamedQuery(name = "PedCompras.findByFpedido", query = "SELECT p FROM PedCompras p WHERE p.fpedido = :fpedido")
    , @NamedQuery(name = "PedCompras.findByFentrega", query = "SELECT p FROM PedCompras p WHERE p.fentrega = :fentrega")
    , @NamedQuery(name = "PedCompras.findByFalta", query = "SELECT p FROM PedCompras p WHERE p.falta = :falta")
    , @NamedQuery(name = "PedCompras.findByCusuario", query = "SELECT p FROM PedCompras p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "PedCompras.findByFultimModif", query = "SELECT p FROM PedCompras p WHERE p.fultimModif = :fultimModif")
    , @NamedQuery(name = "PedCompras.findByCusuarioModif", query = "SELECT p FROM PedCompras p WHERE p.cusuarioModif = :cusuarioModif")})
public class PedCompras implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedComprasPK pedComprasPK;
    @Column(name = "cod_proveed")
    private Long codProveed;
    @Column(name = "fpedido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fpedido;
    @Column(name = "fentrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fentrega;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedCompras")
    private Collection<PedComprasDet> pedComprasDetCollection;

    public PedCompras() {
    }

    public PedCompras(PedComprasPK pedComprasPK) {
        this.pedComprasPK = pedComprasPK;
    }

    public PedCompras(short codEmpr, long nroPedido) {
        this.pedComprasPK = new PedComprasPK(codEmpr, nroPedido);
    }

    public PedComprasPK getPedComprasPK() {
        return pedComprasPK;
    }

    public void setPedComprasPK(PedComprasPK pedComprasPK) {
        this.pedComprasPK = pedComprasPK;
    }

    public Long getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(Long codProveed) {
        this.codProveed = codProveed;
    }

    public Date getFpedido() {
        return fpedido;
    }

    public void setFpedido(Date fpedido) {
        this.fpedido = fpedido;
    }

    public Date getFentrega() {
        return fentrega;
    }

    public void setFentrega(Date fentrega) {
        this.fentrega = fentrega;
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

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    @XmlTransient
    public Collection<PedComprasDet> getPedComprasDetCollection() {
        return pedComprasDetCollection;
    }

    public void setPedComprasDetCollection(Collection<PedComprasDet> pedComprasDetCollection) {
        this.pedComprasDetCollection = pedComprasDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedComprasPK != null ? pedComprasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedCompras)) {
            return false;
        }
        PedCompras other = (PedCompras) object;
        if ((this.pedComprasPK == null && other.pedComprasPK != null) || (this.pedComprasPK != null && !this.pedComprasPK.equals(other.pedComprasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PedCompras[ pedComprasPK=" + pedComprasPK + " ]";
    }
    
}
