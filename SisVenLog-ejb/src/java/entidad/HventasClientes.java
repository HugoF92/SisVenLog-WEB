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
@Table(name = "hventas_clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HventasClientes.findAll", query = "SELECT h FROM HventasClientes h")
    , @NamedQuery(name = "HventasClientes.findByCodCliente", query = "SELECT h FROM HventasClientes h WHERE h.hventasClientesPK.codCliente = :codCliente")
    , @NamedQuery(name = "HventasClientes.findByCtipoVta", query = "SELECT h FROM HventasClientes h WHERE h.hventasClientesPK.ctipoVta = :ctipoVta")
    , @NamedQuery(name = "HventasClientes.findByFanul", query = "SELECT h FROM HventasClientes h WHERE h.fanul = :fanul")
    , @NamedQuery(name = "HventasClientes.findByXobs", query = "SELECT h FROM HventasClientes h WHERE h.xobs = :xobs")
    , @NamedQuery(name = "HventasClientes.findByFalta", query = "SELECT h FROM HventasClientes h WHERE h.falta = :falta")
    , @NamedQuery(name = "HventasClientes.findByCusuario", query = "SELECT h FROM HventasClientes h WHERE h.cusuario = :cusuario")
    , @NamedQuery(name = "HventasClientes.findByFultimModif", query = "SELECT h FROM HventasClientes h WHERE h.fultimModif = :fultimModif")
    , @NamedQuery(name = "HventasClientes.findByCusuarioModif", query = "SELECT h FROM HventasClientes h WHERE h.cusuarioModif = :cusuarioModif")})
public class HventasClientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HventasClientesPK hventasClientesPK;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Size(max = 50)
    @Column(name = "xobs")
    private String xobs;
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
    @JoinColumn(name = "ctipo_vta", referencedColumnName = "ctipo_vta", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HtiposVentas htiposVentas;

    public HventasClientes() {
    }

    public HventasClientes(HventasClientesPK hventasClientesPK) {
        this.hventasClientesPK = hventasClientesPK;
    }

    public HventasClientes(int codCliente, Character ctipoVta) {
        this.hventasClientesPK = new HventasClientesPK(codCliente, ctipoVta);
    }

    public HventasClientesPK getHventasClientesPK() {
        return hventasClientesPK;
    }

    public void setHventasClientesPK(HventasClientesPK hventasClientesPK) {
        this.hventasClientesPK = hventasClientesPK;
    }

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
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

    public HtiposVentas getHtiposVentas() {
        return htiposVentas;
    }

    public void setHtiposVentas(HtiposVentas htiposVentas) {
        this.htiposVentas = htiposVentas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hventasClientesPK != null ? hventasClientesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HventasClientes)) {
            return false;
        }
        HventasClientes other = (HventasClientes) object;
        if ((this.hventasClientesPK == null && other.hventasClientesPK != null) || (this.hventasClientesPK != null && !this.hventasClientesPK.equals(other.hventasClientesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.HventasClientes[ hventasClientesPK=" + hventasClientesPK + " ]";
    }
    
}
