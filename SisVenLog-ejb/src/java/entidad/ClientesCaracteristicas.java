/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "clientes_caracteristicas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientesCaracteristicas.findAll", query = "SELECT c FROM ClientesCaracteristicas c")
    , @NamedQuery(name = "ClientesCaracteristicas.findByCodCliente", query = "SELECT c FROM ClientesCaracteristicas c WHERE c.clientesCaracteristicasPK.codCliente = :codCliente")
    , @NamedQuery(name = "ClientesCaracteristicas.findByCodCaract", query = "SELECT c FROM ClientesCaracteristicas c WHERE c.clientesCaracteristicasPK.codCaract = :codCaract")
    , @NamedQuery(name = "ClientesCaracteristicas.findByXvalor", query = "SELECT c FROM ClientesCaracteristicas c WHERE c.xvalor = :xvalor")
    , @NamedQuery(name = "ClientesCaracteristicas.findByMtipoDato", query = "SELECT c FROM ClientesCaracteristicas c WHERE c.mtipoDato = :mtipoDato")})
public class ClientesCaracteristicas implements Serializable {

    @Size(max = 50)
    @Column(name = "xvalor")
    private String xvalor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mtipo_dato")
    private Character mtipoDato;
    @JoinColumn(name = "cod_caract", referencedColumnName = "cod_caract", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Caracteristicas caracteristicas;
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Clientes clientes;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClientesCaracteristicasPK clientesCaracteristicasPK;

    public ClientesCaracteristicas() {
    }

    public ClientesCaracteristicas(ClientesCaracteristicasPK clientesCaracteristicasPK) {
        this.clientesCaracteristicasPK = clientesCaracteristicasPK;
    }

    public ClientesCaracteristicas(ClientesCaracteristicasPK clientesCaracteristicasPK, Character mtipoDato) {
        this.clientesCaracteristicasPK = clientesCaracteristicasPK;
        this.mtipoDato = mtipoDato;
    }

    public ClientesCaracteristicas(int codCliente, short codCaract) {
        this.clientesCaracteristicasPK = new ClientesCaracteristicasPK(codCliente, codCaract);
    }

    public ClientesCaracteristicasPK getClientesCaracteristicasPK() {
        return clientesCaracteristicasPK;
    }

    public void setClientesCaracteristicasPK(ClientesCaracteristicasPK clientesCaracteristicasPK) {
        this.clientesCaracteristicasPK = clientesCaracteristicasPK;
    }


    public Character getMtipoDato() {
        return mtipoDato;
    }

    public void setMtipoDato(Character mtipoDato) {
        this.mtipoDato = mtipoDato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientesCaracteristicasPK != null ? clientesCaracteristicasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientesCaracteristicas)) {
            return false;
        }
        ClientesCaracteristicas other = (ClientesCaracteristicas) object;
        if ((this.clientesCaracteristicasPK == null && other.clientesCaracteristicasPK != null) || (this.clientesCaracteristicasPK != null && !this.clientesCaracteristicasPK.equals(other.clientesCaracteristicasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ClientesCaracteristicas[ clientesCaracteristicasPK=" + clientesCaracteristicasPK + " ]";
    }

    public String getXvalor() {
        return xvalor;
    }

    public void setXvalor(String xvalor) {
        this.xvalor = xvalor;
    }

    public Caracteristicas getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(Caracteristicas caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }
    
}
