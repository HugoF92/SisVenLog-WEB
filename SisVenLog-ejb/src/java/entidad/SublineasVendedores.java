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
import javax.persistence.OneToOne;
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
@Table(name = "sublineas_vendedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SublineasVendedores.findAll", query = "SELECT s FROM SublineasVendedores s")
    , @NamedQuery(name = "SublineasVendedores.findByCodEmpr", query = "SELECT s FROM SublineasVendedores s WHERE s.sublineasVendedoresPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "SublineasVendedores.findByCodVendedor", query = "SELECT s FROM SublineasVendedores s WHERE s.sublineasVendedoresPK.codVendedor = :codVendedor")
    , @NamedQuery(name = "SublineasVendedores.findByFalta", query = "SELECT s FROM SublineasVendedores s WHERE s.falta = :falta")
    , @NamedQuery(name = "SublineasVendedores.findByCusuario", query = "SELECT s FROM SublineasVendedores s WHERE s.cusuario = :cusuario")})
public class SublineasVendedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SublineasVendedoresPK sublineasVendedoresPK;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_vendedor", referencedColumnName = "cod_empleado", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Empleados empleados;
    @JoinColumn(name = "cod_sublinea", referencedColumnName = "cod_sublinea")
    @ManyToOne(optional = false)
    private Sublineas codSublinea;

    public SublineasVendedores() {
    }

    public SublineasVendedores(SublineasVendedoresPK sublineasVendedoresPK) {
        this.sublineasVendedoresPK = sublineasVendedoresPK;
    }

    public SublineasVendedores(short codEmpr, short codVendedor) {
        this.sublineasVendedoresPK = new SublineasVendedoresPK(codEmpr, codVendedor);
    }

    public SublineasVendedoresPK getSublineasVendedoresPK() {
        return sublineasVendedoresPK;
    }

    public void setSublineasVendedoresPK(SublineasVendedoresPK sublineasVendedoresPK) {
        this.sublineasVendedoresPK = sublineasVendedoresPK;
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

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public Sublineas getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(Sublineas codSublinea) {
        this.codSublinea = codSublinea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sublineasVendedoresPK != null ? sublineasVendedoresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SublineasVendedores)) {
            return false;
        }
        SublineasVendedores other = (SublineasVendedores) object;
        if ((this.sublineasVendedoresPK == null && other.sublineasVendedoresPK != null) || (this.sublineasVendedoresPK != null && !this.sublineasVendedoresPK.equals(other.sublineasVendedoresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.SublineasVendedores[ sublineasVendedoresPK=" + sublineasVendedoresPK + " ]";
    }
    
}
