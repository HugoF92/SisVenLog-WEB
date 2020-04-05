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
@Table(name = "canales_vendedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CanalesVendedores.findAll", query = "SELECT c FROM CanalesVendedores c")
    , @NamedQuery(name = "CanalesVendedores.findByCodEmpr", query = "SELECT c FROM CanalesVendedores c WHERE c.canalesVendedoresPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "CanalesVendedores.findByCodVendedor", query = "SELECT c FROM CanalesVendedores c WHERE c.canalesVendedoresPK.codVendedor = :codVendedor")
    , @NamedQuery(name = "CanalesVendedores.findByCodCanal", query = "SELECT c FROM CanalesVendedores c WHERE c.canalesVendedoresPK.codCanal = :codCanal")
    , @NamedQuery(name = "CanalesVendedores.findByCusuario", query = "SELECT c FROM CanalesVendedores c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "CanalesVendedores.findByFalta", query = "SELECT c FROM CanalesVendedores c WHERE c.falta = :falta")
    , @NamedQuery(name = "CanalesVendedores.findByCusuarioModif", query = "SELECT c FROM CanalesVendedores c WHERE c.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "CanalesVendedores.findByFultimModif", query = "SELECT c FROM CanalesVendedores c WHERE c.fultimModif = :fultimModif")})
public class CanalesVendedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CanalesVendedoresPK canalesVendedoresPK;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @JoinColumn(name = "cod_canal", referencedColumnName = "cod_canal", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CanalesVenta canalesVenta;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_vendedor", referencedColumnName = "cod_empleado", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Empleados empleados;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;

    public CanalesVendedores() {
    }

    public CanalesVendedores(CanalesVendedoresPK canalesVendedoresPK) {
        this.canalesVendedoresPK = canalesVendedoresPK;
    }

    public CanalesVendedores(short codEmpr, short codVendedor, String codCanal) {
        this.canalesVendedoresPK = new CanalesVendedoresPK(codEmpr, codVendedor, codCanal);
    }

    public CanalesVendedoresPK getCanalesVendedoresPK() {
        return canalesVendedoresPK;
    }

    public void setCanalesVendedoresPK(CanalesVendedoresPK canalesVendedoresPK) {
        this.canalesVendedoresPK = canalesVendedoresPK;
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

    public CanalesVenta getCanalesVenta() {
        return canalesVenta;
    }

    public void setCanalesVenta(CanalesVenta canalesVenta) {
        this.canalesVenta = canalesVenta;
    }

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (canalesVendedoresPK != null ? canalesVendedoresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CanalesVendedores)) {
            return false;
        }
        CanalesVendedores other = (CanalesVendedores) object;
        if ((this.canalesVendedoresPK == null && other.canalesVendedoresPK != null) || (this.canalesVendedoresPK != null && !this.canalesVendedoresPK.equals(other.canalesVendedoresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CanalesVendedores[ canalesVendedoresPK=" + canalesVendedoresPK + " ]";
    }
    
}
