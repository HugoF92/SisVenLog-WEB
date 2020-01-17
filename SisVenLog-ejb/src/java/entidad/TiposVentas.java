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
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tipos_ventas")
@NamedQueries({
    @NamedQuery(name = "TiposVentas.findAll", query = "SELECT t FROM TiposVentas t")})
public class TiposVentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TiposVentasPK tiposVentasPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;

    public TiposVentas() {
    }

    public TiposVentas(TiposVentasPK tiposVentasPK) {
        this.tiposVentasPK = tiposVentasPK;
    }

    public TiposVentas(short codEmpr, Character ctipoVta) {
        this.tiposVentasPK = new TiposVentasPK(codEmpr, ctipoVta);
    }

    public TiposVentasPK getTiposVentasPK() {
        return tiposVentasPK;
    }

    public void setTiposVentasPK(TiposVentasPK tiposVentasPK) {
        this.tiposVentasPK = tiposVentasPK;
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
        hash += (tiposVentasPK != null ? tiposVentasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposVentas)) {
            return false;
        }
        TiposVentas other = (TiposVentas) object;
        if ((this.tiposVentasPK == null && other.tiposVentasPK != null) || (this.tiposVentasPK != null && !this.tiposVentasPK.equals(other.tiposVentasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.TiposVentas[ tiposVentasPK=" + tiposVentasPK + " ]";
    }
    
}
