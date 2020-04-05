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
 * @author admin
 */
@Entity
@Table(name = "clientes_agrupados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientesAgrupados.findAll", query = "SELECT c FROM ClientesAgrupados c")
    , @NamedQuery(name = "ClientesAgrupados.findByCodGrupo", query = "SELECT c FROM ClientesAgrupados c WHERE c.clientesAgrupadosPK.codGrupo = :codGrupo")
    , @NamedQuery(name = "ClientesAgrupados.findByCodCliente", query = "SELECT c FROM ClientesAgrupados c WHERE c.clientesAgrupadosPK.codCliente = :codCliente")
    , @NamedQuery(name = "ClientesAgrupados.findByCusuario", query = "SELECT c FROM ClientesAgrupados c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "ClientesAgrupados.findByFalta", query = "SELECT c FROM ClientesAgrupados c WHERE c.falta = :falta")})
public class ClientesAgrupados implements Serializable {

    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClientesAgrupadosPK clientesAgrupadosPK;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Clientes clientes;
    @JoinColumn(name = "cod_grupo", referencedColumnName = "cod_grupo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GruposClientes gruposClientes;

    public ClientesAgrupados() {
    }

    public ClientesAgrupados(ClientesAgrupadosPK clientesAgrupadosPK) {
        this.clientesAgrupadosPK = clientesAgrupadosPK;
    }

    public ClientesAgrupados(short codGrupo, int codCliente) {
        this.clientesAgrupadosPK = new ClientesAgrupadosPK(codGrupo, codCliente);
    }

    public ClientesAgrupadosPK getClientesAgrupadosPK() {
        return clientesAgrupadosPK;
    }

    public void setClientesAgrupadosPK(ClientesAgrupadosPK clientesAgrupadosPK) {
        this.clientesAgrupadosPK = clientesAgrupadosPK;
    }


    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public GruposClientes getGruposClientes() {
        return gruposClientes;
    }

    public void setGruposClientes(GruposClientes gruposClientes) {
        this.gruposClientes = gruposClientes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientesAgrupadosPK != null ? clientesAgrupadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientesAgrupados)) {
            return false;
        }
        ClientesAgrupados other = (ClientesAgrupados) object;
        if ((this.clientesAgrupadosPK == null && other.clientesAgrupadosPK != null) || (this.clientesAgrupadosPK != null && !this.clientesAgrupadosPK.equals(other.clientesAgrupadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ClientesAgrupados[ clientesAgrupadosPK=" + clientesAgrupadosPK + " ]";
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }
    
}
