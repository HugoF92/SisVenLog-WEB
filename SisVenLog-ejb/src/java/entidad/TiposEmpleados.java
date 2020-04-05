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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "tipos_empleados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposEmpleados.findAll", query = "SELECT t FROM TiposEmpleados t")
    , @NamedQuery(name = "TiposEmpleados.findByCtipoEmp", query = "SELECT t FROM TiposEmpleados t WHERE t.ctipoEmp = :ctipoEmp")
    , @NamedQuery(name = "TiposEmpleados.findByXdesc", query = "SELECT t FROM TiposEmpleados t WHERE t.xdesc = :xdesc")
    , @NamedQuery(name = "TiposEmpleados.findByMfijoSis", query = "SELECT t FROM TiposEmpleados t WHERE t.mfijoSis = :mfijoSis")
    , @NamedQuery(name = "TiposEmpleados.findByFalta", query = "SELECT t FROM TiposEmpleados t WHERE t.falta = :falta")
    , @NamedQuery(name = "TiposEmpleados.findByCusuario", query = "SELECT t FROM TiposEmpleados t WHERE t.cusuario = :cusuario")
    , @NamedQuery(name = "TiposEmpleados.findByFultimModif", query = "SELECT t FROM TiposEmpleados t WHERE t.fultimModif = :fultimModif")
    , @NamedQuery(name = "TiposEmpleados.findByCusuarioModif", query = "SELECT t FROM TiposEmpleados t WHERE t.cusuarioModif = :cusuarioModif")})
public class TiposEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
   //@NotNull
   //@Size(min = 1, max = 2)
    @Column(name = "ctipo_emp")
    private String ctipoEmp;
   //@Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Basic(optional = false)
   //@NotNull
    @Column(name = "mfijo_sis")
    private Character mfijoSis;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
   //@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
   //@Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @OneToMany(mappedBy = "ctipoEmp")
    private Collection<Empleados> empleadosCollection;

    public TiposEmpleados() {
    }

    public TiposEmpleados(String ctipoEmp) {
        this.ctipoEmp = ctipoEmp;
    }

    public TiposEmpleados(String ctipoEmp, Character mfijoSis) {
        this.ctipoEmp = ctipoEmp;
        this.mfijoSis = mfijoSis;
    }

    public String getCtipoEmp() {
        return ctipoEmp;
    }

    public void setCtipoEmp(String ctipoEmp) {
        this.ctipoEmp = ctipoEmp;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
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

    @XmlTransient
    public Collection<Empleados> getEmpleadosCollection() {
        return empleadosCollection;
    }

    public void setEmpleadosCollection(Collection<Empleados> empleadosCollection) {
        this.empleadosCollection = empleadosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctipoEmp != null ? ctipoEmp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposEmpleados)) {
            return false;
        }
        TiposEmpleados other = (TiposEmpleados) object;
        if ((this.ctipoEmp == null && other.ctipoEmp != null) || (this.ctipoEmp != null && !this.ctipoEmp.equals(other.ctipoEmp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.TiposEmpleados[ ctipoEmp=" + ctipoEmp + " ]";
    }
    
}
