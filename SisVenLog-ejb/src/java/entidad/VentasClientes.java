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
import javax.persistence.JoinColumns;
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
@Table(name = "ventas_clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VentasClientes.findAll", query = "SELECT v FROM VentasClientes v")
    , @NamedQuery(name = "VentasClientes.findByCodEmpr", query = "SELECT v FROM VentasClientes v WHERE v.ventasClientesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "VentasClientes.findByCtipoVta", query = "SELECT v FROM VentasClientes v WHERE v.ventasClientesPK.ctipoVta = :ctipoVta")
    , @NamedQuery(name = "VentasClientes.findByCodCliente", query = "SELECT v FROM VentasClientes v WHERE v.ventasClientesPK.codCliente = :codCliente")
    , @NamedQuery(name = "VentasClientes.findByXobs", query = "SELECT v FROM VentasClientes v WHERE v.xobs = :xobs")
    , @NamedQuery(name = "VentasClientes.findByFalta", query = "SELECT v FROM VentasClientes v WHERE v.falta = :falta")
    , @NamedQuery(name = "VentasClientes.findByCusuario", query = "SELECT v FROM VentasClientes v WHERE v.cusuario = :cusuario")})
public class VentasClientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VentasClientesPK ventasClientesPK;
    @Size(max = 50)
    @Column(name = "xobs")
    private String xobs;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "ctipo_vta", referencedColumnName = "ctipo_vta", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private TiposVentas tiposVentas;

    public VentasClientes() {
    }

    public VentasClientes(VentasClientesPK ventasClientesPK) {
        this.ventasClientesPK = ventasClientesPK;
    }

    public VentasClientes(short codEmpr, Character ctipoVta, int codCliente) {
        this.ventasClientesPK = new VentasClientesPK(codEmpr, ctipoVta, codCliente);
    }

    public VentasClientesPK getVentasClientesPK() {
        return ventasClientesPK;
    }

    public void setVentasClientesPK(VentasClientesPK ventasClientesPK) {
        this.ventasClientesPK = ventasClientesPK;
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

    public TiposVentas getTiposVentas() {
        return tiposVentas;
    }

    public void setTiposVentas(TiposVentas tiposVentas) {
        this.tiposVentas = tiposVentas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ventasClientesPK != null ? ventasClientesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VentasClientes)) {
            return false;
        }
        VentasClientes other = (VentasClientes) object;
        if ((this.ventasClientesPK == null && other.ventasClientesPK != null) || (this.ventasClientesPK != null && !this.ventasClientesPK.equals(other.ventasClientesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.VentasClientes[ ventasClientesPK=" + ventasClientesPK + " ]";
    }
    
}
