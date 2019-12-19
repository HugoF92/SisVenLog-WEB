/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "empleados_zonas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpleadosZonas.findAll", query = "SELECT e FROM EmpleadosZonas e")
    , @NamedQuery(name = "EmpleadosZonas.findByCodEmpr", query = "SELECT e FROM EmpleadosZonas e WHERE e.empleadosZonasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "EmpleadosZonas.findByCodZona", query = "SELECT e FROM EmpleadosZonas e WHERE e.empleadosZonasPK.codZona = :codZona")
    , @NamedQuery(name = "EmpleadosZonas.findByCodEmpleado", query = "SELECT e FROM EmpleadosZonas e WHERE e.empleadosZonasPK.codEmpleado = :codEmpleado")})
public class EmpleadosZonas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EmpleadosZonasPK empleadosZonasPK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empleadosZonas")
    private Collection<MetasVendedores> metasVendedoresCollection;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_empleado", referencedColumnName = "cod_empleado", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Empleados empleados;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Zonas zonas;

    public EmpleadosZonas() {
    }

    public EmpleadosZonas(EmpleadosZonasPK empleadosZonasPK) {
        this.empleadosZonasPK = empleadosZonasPK;
    }

    public EmpleadosZonas(short codEmpr, String codZona, short codEmpleado) {
        this.empleadosZonasPK = new EmpleadosZonasPK(codEmpr, codZona, codEmpleado);
    }

    public EmpleadosZonasPK getEmpleadosZonasPK() {
        return empleadosZonasPK;
    }

    public void setEmpleadosZonasPK(EmpleadosZonasPK empleadosZonasPK) {
        this.empleadosZonasPK = empleadosZonasPK;
    }

    @XmlTransient
    public Collection<MetasVendedores> getMetasVendedoresCollection() {
        return metasVendedoresCollection;
    }

    public void setMetasVendedoresCollection(Collection<MetasVendedores> metasVendedoresCollection) {
        this.metasVendedoresCollection = metasVendedoresCollection;
    }

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empleadosZonasPK != null ? empleadosZonasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadosZonas)) {
            return false;
        }
        EmpleadosZonas other = (EmpleadosZonas) object;
        if ((this.empleadosZonasPK == null && other.empleadosZonasPK != null) || (this.empleadosZonasPK != null && !this.empleadosZonasPK.equals(other.empleadosZonasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EmpleadosZonas[ empleadosZonasPK=" + empleadosZonasPK + " ]";
    }
    
}
