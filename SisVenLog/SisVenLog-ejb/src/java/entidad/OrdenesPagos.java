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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "ORDENES_PAGOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenesPagos.findAll", query = "SELECT o FROM OrdenesPagos o")
    , @NamedQuery(name = "OrdenesPagos.findByNroOrden", query = "SELECT o FROM OrdenesPagos o WHERE o.nroOrden = :nroOrden")
    , @NamedQuery(name = "OrdenesPagos.findByForden", query = "SELECT o FROM OrdenesPagos o WHERE o.forden = :forden")
    , @NamedQuery(name = "OrdenesPagos.findByCodProveed", query = "SELECT o FROM OrdenesPagos o WHERE o.codProveed = :codProveed")
    , @NamedQuery(name = "OrdenesPagos.findByMEstado", query = "SELECT o FROM OrdenesPagos o WHERE o.mEstado = :mEstado")
    , @NamedQuery(name = "OrdenesPagos.findByXnroCheque", query = "SELECT o FROM OrdenesPagos o WHERE o.xnroCheque = :xnroCheque")
    , @NamedQuery(name = "OrdenesPagos.findByCodBanco", query = "SELECT o FROM OrdenesPagos o WHERE o.codBanco = :codBanco")
    , @NamedQuery(name = "OrdenesPagos.findByITotalOrden", query = "SELECT o FROM OrdenesPagos o WHERE o.iTotalOrden = :iTotalOrden")
    , @NamedQuery(name = "OrdenesPagos.findByITotalRetencion", query = "SELECT o FROM OrdenesPagos o WHERE o.iTotalRetencion = :iTotalRetencion")
    , @NamedQuery(name = "OrdenesPagos.findByXobs", query = "SELECT o FROM OrdenesPagos o WHERE o.xobs = :xobs")})
public class OrdenesPagos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Nro_Orden")
    private Long nroOrden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "forden")
    @Temporal(TemporalType.TIMESTAMP)
    private Date forden;
    @Column(name = "Cod_Proveed")
    private Short codProveed;
    @Column(name = "mEstado")
    private Character mEstado;
    @Size(max = 15)
    @Column(name = "xnro_cheque")
    private String xnroCheque;
    @Column(name = "cod_Banco")
    private Short codBanco;
    @Column(name = "iTotal_Orden")
    private Long iTotalOrden;
    @Column(name = "iTotal_Retencion")
    private Long iTotalRetencion;
    @Size(max = 50)
    @Column(name = "xobs")
    private String xobs;

    public OrdenesPagos() {
    }

    public OrdenesPagos(Long nroOrden) {
        this.nroOrden = nroOrden;
    }

    public OrdenesPagos(Long nroOrden, Date forden) {
        this.nroOrden = nroOrden;
        this.forden = forden;
    }

    public Long getNroOrden() {
        return nroOrden;
    }

    public void setNroOrden(Long nroOrden) {
        this.nroOrden = nroOrden;
    }

    public Date getForden() {
        return forden;
    }

    public void setForden(Date forden) {
        this.forden = forden;
    }

    public Short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(Short codProveed) {
        this.codProveed = codProveed;
    }

    public Character getMEstado() {
        return mEstado;
    }

    public void setMEstado(Character mEstado) {
        this.mEstado = mEstado;
    }

    public String getXnroCheque() {
        return xnroCheque;
    }

    public void setXnroCheque(String xnroCheque) {
        this.xnroCheque = xnroCheque;
    }

    public Short getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(Short codBanco) {
        this.codBanco = codBanco;
    }

    public Long getITotalOrden() {
        return iTotalOrden;
    }

    public void setITotalOrden(Long iTotalOrden) {
        this.iTotalOrden = iTotalOrden;
    }

    public Long getITotalRetencion() {
        return iTotalRetencion;
    }

    public void setITotalRetencion(Long iTotalRetencion) {
        this.iTotalRetencion = iTotalRetencion;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroOrden != null ? nroOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenesPagos)) {
            return false;
        }
        OrdenesPagos other = (OrdenesPagos) object;
        if ((this.nroOrden == null && other.nroOrden != null) || (this.nroOrden != null && !this.nroOrden.equals(other.nroOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.OrdenesPagos[ nroOrden=" + nroOrden + " ]";
    }
    
}
