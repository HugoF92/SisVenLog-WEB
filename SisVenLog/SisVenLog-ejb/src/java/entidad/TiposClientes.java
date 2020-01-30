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
@Table(name = "tipos_clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposClientes.findAll", query = "SELECT t FROM TiposClientes t")
    , @NamedQuery(name = "TiposClientes.findByCtipoCliente", query = "SELECT t FROM TiposClientes t WHERE t.ctipoCliente = :ctipoCliente")
    , @NamedQuery(name = "TiposClientes.findByXdesc", query = "SELECT t FROM TiposClientes t WHERE t.xdesc = :xdesc")
    , @NamedQuery(name = "TiposClientes.findByCusuario", query = "SELECT t FROM TiposClientes t WHERE t.cusuario = :cusuario")
    , @NamedQuery(name = "TiposClientes.findByFalta", query = "SELECT t FROM TiposClientes t WHERE t.falta = :falta")
    , @NamedQuery(name = "TiposClientes.findByCusuarioModif", query = "SELECT t FROM TiposClientes t WHERE t.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "TiposClientes.findByFultimModif", query = "SELECT t FROM TiposClientes t WHERE t.fultimModif = :fultimModif")})
public class TiposClientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ctipo_cliente")
    private String ctipoCliente;
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

    public TiposClientes() {
    }

    public TiposClientes(String ctipoCliente) {
        this.ctipoCliente = ctipoCliente;
    }

    public String getCtipoCliente() {
        return ctipoCliente;
    }

    public void setCtipoCliente(String ctipoCliente) {
        this.ctipoCliente = ctipoCliente;
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
        hash += (ctipoCliente != null ? ctipoCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposClientes)) {
            return false;
        }
        TiposClientes other = (TiposClientes) object;
        if ((this.ctipoCliente == null && other.ctipoCliente != null) || (this.ctipoCliente != null && !this.ctipoCliente.equals(other.ctipoCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.TiposClientes[ ctipoCliente=" + ctipoCliente + " ]";
    }
    
}
