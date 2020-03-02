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
import javax.persistence.JoinColumns;
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
 * @author admin
 */
@Entity
@Table(name = "pagos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pagos.findAll", query = "SELECT p FROM Pagos p")
    , @NamedQuery(name = "Pagos.findByCodEmpr", query = "SELECT p FROM Pagos p WHERE p.pagosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Pagos.findByNroPago", query = "SELECT p FROM Pagos p WHERE p.pagosPK.nroPago = :nroPago")
    , @NamedQuery(name = "Pagos.findByCodEmpleado", query = "SELECT p FROM Pagos p WHERE p.codEmpleado = :codEmpleado")
    , @NamedQuery(name = "Pagos.findByFpago", query = "SELECT p FROM Pagos p WHERE p.fpago = :fpago")
    , @NamedQuery(name = "Pagos.findByIpagado", query = "SELECT p FROM Pagos p WHERE p.ipagado = :ipagado")
    , @NamedQuery(name = "Pagos.findByCusuario", query = "SELECT p FROM Pagos p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "Pagos.findByFalta", query = "SELECT p FROM Pagos p WHERE p.falta = :falta")
    , @NamedQuery(name = "Pagos.findByCusuarioModif", query = "SELECT p FROM Pagos p WHERE p.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Pagos.findByFultimModif", query = "SELECT p FROM Pagos p WHERE p.fultimModif = :fultimModif")})
public class Pagos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PagosPK pagosPK;
    @Column(name = "cod_empleado")
    private Short codEmpleado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fpago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fpago;
    @Column(name = "ipagado")
    private Long ipagado;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pagos")
    private Collection<PagosDet> pagosDetCollection;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona")})
    @ManyToOne(optional = false)
    private Zonas zonas;

    public Pagos() {
    }

    public Pagos(PagosPK pagosPK) {
        this.pagosPK = pagosPK;
    }

    public Pagos(PagosPK pagosPK, Date fpago) {
        this.pagosPK = pagosPK;
        this.fpago = fpago;
    }

    public Pagos(short codEmpr, short nroPago) {
        this.pagosPK = new PagosPK(codEmpr, nroPago);
    }

    public PagosPK getPagosPK() {
        return pagosPK;
    }

    public void setPagosPK(PagosPK pagosPK) {
        this.pagosPK = pagosPK;
    }

    public Short getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(Short codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    public Date getFpago() {
        return fpago;
    }

    public void setFpago(Date fpago) {
        this.fpago = fpago;
    }

    public Long getIpagado() {
        return ipagado;
    }

    public void setIpagado(Long ipagado) {
        this.ipagado = ipagado;
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

    @XmlTransient
    public Collection<PagosDet> getPagosDetCollection() {
        return pagosDetCollection;
    }

    public void setPagosDetCollection(Collection<PagosDet> pagosDetCollection) {
        this.pagosDetCollection = pagosDetCollection;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
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
        hash += (pagosPK != null ? pagosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pagos)) {
            return false;
        }
        Pagos other = (Pagos) object;
        if ((this.pagosPK == null && other.pagosPK != null) || (this.pagosPK != null && !this.pagosPK.equals(other.pagosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Pagos[ pagosPK=" + pagosPK + " ]";
    }
    
}
