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
@Table(name = "estados_clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadosClientes.findAll", query = "SELECT e FROM EstadosClientes e")
    , @NamedQuery(name = "EstadosClientes.findByCodEstado", query = "SELECT e FROM EstadosClientes e WHERE e.codEstado = :codEstado")
    , @NamedQuery(name = "EstadosClientes.findByXdesc", query = "SELECT e FROM EstadosClientes e WHERE e.xdesc = :xdesc")
    , @NamedQuery(name = "EstadosClientes.findByMprioridad", query = "SELECT e FROM EstadosClientes e WHERE e.mprioridad = :mprioridad")
    , @NamedQuery(name = "EstadosClientes.findByMfijoSis", query = "SELECT e FROM EstadosClientes e WHERE e.mfijoSis = :mfijoSis")
    , @NamedQuery(name = "EstadosClientes.findByFalta", query = "SELECT e FROM EstadosClientes e WHERE e.falta = :falta")
    , @NamedQuery(name = "EstadosClientes.findByCusuario", query = "SELECT e FROM EstadosClientes e WHERE e.cusuario = :cusuario")
    , @NamedQuery(name = "EstadosClientes.findByFultimModif", query = "SELECT e FROM EstadosClientes e WHERE e.fultimModif = :fultimModif")
    , @NamedQuery(name = "EstadosClientes.findByCusuarioModif", query = "SELECT e FROM EstadosClientes e WHERE e.cusuarioModif = :cusuarioModif")})
public class EstadosClientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_estado")
    private String codEstado;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "mprioridad")
    private Character mprioridad;
    @Column(name = "mfijo_sis")
    private Character mfijoSis;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    public EstadosClientes() {
    }

    public EstadosClientes(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Character getMprioridad() {
        return mprioridad;
    }

    public void setMprioridad(Character mprioridad) {
        this.mprioridad = mprioridad;
    }

    public Character getMfijoSis() {
        return mfijoSis;
    }

    public void setMfijoSis(Character mfijoSis) {
        this.mfijoSis = mfijoSis;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codEstado != null ? codEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadosClientes)) {
            return false;
        }
        EstadosClientes other = (EstadosClientes) object;
        if ((this.codEstado == null && other.codEstado != null) || (this.codEstado != null && !this.codEstado.equals(other.codEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EstadosClientes[ codEstado=" + codEstado + " ]";
    }
    
}
