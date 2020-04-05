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
 * @author Hugo
 */
@Entity
@Table(name = "canales_compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CanalesCompra.findAll", query = "SELECT c FROM CanalesCompra c")
    , @NamedQuery(name = "CanalesCompra.findByCodProveed", query = "SELECT c FROM CanalesCompra c WHERE c.canalesCompraPK.codProveed = :codProveed")
    , @NamedQuery(name = "CanalesCompra.findByCcanalCompra", query = "SELECT c FROM CanalesCompra c WHERE c.canalesCompraPK.ccanalCompra = :ccanalCompra")
    , @NamedQuery(name = "CanalesCompra.findByXdesc", query = "SELECT c FROM CanalesCompra c WHERE c.xdesc = :xdesc")
    , @NamedQuery(name = "CanalesCompra.findByFalta", query = "SELECT c FROM CanalesCompra c WHERE c.falta = :falta")
    , @NamedQuery(name = "CanalesCompra.findByCusuario", query = "SELECT c FROM CanalesCompra c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "CanalesCompra.findByFultimModif", query = "SELECT c FROM CanalesCompra c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "CanalesCompra.findByCusuarioModif", query = "SELECT c FROM CanalesCompra c WHERE c.cusuarioModif = :cusuarioModif")})
public class CanalesCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CanalesCompraPK canalesCompraPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private Proveedores proveedores;

    public CanalesCompra() {
    }

    public CanalesCompra(CanalesCompraPK canalesCompraPK) {
        this.canalesCompraPK = canalesCompraPK;
    }

    public CanalesCompra(short codProveed, String ccanalCompra) {
        this.canalesCompraPK = new CanalesCompraPK(codProveed, ccanalCompra);
    }

    public CanalesCompraPK getCanalesCompraPK() {
        return canalesCompraPK;
    }

    public void setCanalesCompraPK(CanalesCompraPK canalesCompraPK) {
        this.canalesCompraPK = canalesCompraPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
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

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (canalesCompraPK != null ? canalesCompraPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CanalesCompra)) {
            return false;
        }
        CanalesCompra other = (CanalesCompra) object;
        if ((this.canalesCompraPK == null && other.canalesCompraPK != null) || (this.canalesCompraPK != null && !this.canalesCompraPK.equals(other.canalesCompraPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CanalesCompra[ canalesCompraPK=" + canalesCompraPK + " ]";
    }
    
}
