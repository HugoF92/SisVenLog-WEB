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
@Table(name = "canales_venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CanalesVenta.findAll", query = "SELECT c FROM CanalesVenta c")
    , @NamedQuery(name = "CanalesVenta.findByCodCanal", query = "SELECT c FROM CanalesVenta c WHERE c.codCanal = :codCanal")
    , @NamedQuery(name = "CanalesVenta.findByXdesc", query = "SELECT c FROM CanalesVenta c WHERE c.xdesc = :xdesc")
    , @NamedQuery(name = "CanalesVenta.findByCusuario", query = "SELECT c FROM CanalesVenta c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "CanalesVenta.findByFalta", query = "SELECT c FROM CanalesVenta c WHERE c.falta = :falta")
    , @NamedQuery(name = "CanalesVenta.findByCusuarioModif", query = "SELECT c FROM CanalesVenta c WHERE c.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "CanalesVenta.findByFultimModif", query = "SELECT c FROM CanalesVenta c WHERE c.fultimModif = :fultimModif")})
public class CanalesVenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_canal")
    private String codCanal;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;

    public CanalesVenta() {
    }

    public CanalesVenta(String codCanal) {
        this.codCanal = codCanal;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCanal != null ? codCanal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CanalesVenta)) {
            return false;
        }
        CanalesVenta other = (CanalesVenta) object;
        if ((this.codCanal == null && other.codCanal != null) || (this.codCanal != null && !this.codCanal.equals(other.codCanal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CanalesVenta[ codCanal=" + codCanal + " ]";
    }
    
}
