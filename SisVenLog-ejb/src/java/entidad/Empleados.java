/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Hugo
 */
@Entity
@Table(name = "empleados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleados.findAll", query = "SELECT e FROM Empleados e")
    , @NamedQuery(name = "Empleados.findByCodEmpr", query = "SELECT e FROM Empleados e WHERE e.empleadosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Empleados.findByCodEmpleado", query = "SELECT e FROM Empleados e WHERE e.empleadosPK.codEmpleado = :codEmpleado")
    , @NamedQuery(name = "Empleados.findByXnombre", query = "SELECT e FROM Empleados e WHERE e.xnombre = :xnombre")
    , @NamedQuery(name = "Empleados.findByCodDepo", query = "SELECT e FROM Empleados e WHERE e.codDepo = :codDepo")
    , @NamedQuery(name = "Empleados.findByMestado", query = "SELECT e FROM Empleados e WHERE e.mestado = :mestado")
    , @NamedQuery(name = "Empleados.findByXnroHand", query = "SELECT e FROM Empleados e WHERE e.xnroHand = :xnroHand")
    , @NamedQuery(name = "Empleados.findByXfoto", query = "SELECT e FROM Empleados e WHERE e.xfoto = :xfoto")
    , @NamedQuery(name = "Empleados.findByCusuario", query = "SELECT e FROM Empleados e WHERE e.cusuario = :cusuario")
    , @NamedQuery(name = "Empleados.findByFalta", query = "SELECT e FROM Empleados e WHERE e.falta = :falta")
    , @NamedQuery(name = "Empleados.findByFultimModif", query = "SELECT e FROM Empleados e WHERE e.fultimModif = :fultimModif")
    , @NamedQuery(name = "Empleados.findByCusuarioModif", query = "SELECT e FROM Empleados e WHERE e.cusuarioModif = :cusuarioModif")})
public class Empleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EmpleadosPK empleadosPK;
    @Basic(optional = false)
   //@NotNull
   //@Size(min = 1, max = 50)
    @Column(name = "xnombre")
    private String xnombre;
    @Column(name = "cod_depo")
    private Short codDepo;
    @Basic(optional = false)
   //@NotNull
    @Column(name = "mestado")
    private Character mestado;
   //@Size(max = 3)
    @Column(name = "xnro_hand")
    private String xnroHand;
   //@Size(max = 150)
    @Column(name = "xfoto")
    private String xfoto;
   //@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
   //@Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumn(name = "ctipo_emp", referencedColumnName = "ctipo_emp")
    @ManyToOne
    private TiposEmpleados ctipoEmp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empleados")
    private Collection<Usuarios> usuariosCollection;

    public Empleados() {
    }

    public Empleados(EmpleadosPK empleadosPK) {
        this.empleadosPK = empleadosPK;
    }

    public Empleados(EmpleadosPK empleadosPK, String xnombre, Character mestado) {
        this.empleadosPK = empleadosPK;
        this.xnombre = xnombre;
        this.mestado = mestado;
    }

    public Empleados(short codEmpr, short codEmpleado) {
        this.empleadosPK = new EmpleadosPK(codEmpr, codEmpleado);
    }

    public EmpleadosPK getEmpleadosPK() {
        return empleadosPK;
    }

    public void setEmpleadosPK(EmpleadosPK empleadosPK) {
        this.empleadosPK = empleadosPK;
    }

    public String getXnombre() {
        return xnombre;
    }

    public void setXnombre(String xnombre) {
        this.xnombre = xnombre;
    }

    public Short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(Short codDepo) {
        this.codDepo = codDepo;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public String getXnroHand() {
        return xnroHand;
    }

    public void setXnroHand(String xnroHand) {
        this.xnroHand = xnroHand;
    }

    public String getXfoto() {
        return xfoto;
    }

    public void setXfoto(String xfoto) {
        this.xfoto = xfoto;
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

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public TiposEmpleados getCtipoEmp() {
        return ctipoEmp;
    }

    public void setCtipoEmp(TiposEmpleados ctipoEmp) {
        this.ctipoEmp = ctipoEmp;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empleadosPK != null ? empleadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleados)) {
            return false;
        }
        Empleados other = (Empleados) object;
        if ((this.empleadosPK == null && other.empleadosPK != null) || (this.empleadosPK != null && !this.empleadosPK.equals(other.empleadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Empleados[ empleadosPK=" + empleadosPK + " ]";
    }
    
}
