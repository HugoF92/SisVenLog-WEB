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
 * @author admin
 */
@Entity
@Table(name = "planilla_cobradores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanillaCobradores.findAll", query = "SELECT p FROM PlanillaCobradores p")
    , @NamedQuery(name = "PlanillaCobradores.findByCodEmpr", query = "SELECT p FROM PlanillaCobradores p WHERE p.planillaCobradoresPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "PlanillaCobradores.findByNplanilla", query = "SELECT p FROM PlanillaCobradores p WHERE p.planillaCobradoresPK.nplanilla = :nplanilla")
    , @NamedQuery(name = "PlanillaCobradores.findByCodEmpleado", query = "SELECT p FROM PlanillaCobradores p WHERE p.codEmpleado = :codEmpleado")
    , @NamedQuery(name = "PlanillaCobradores.findByFdocum", query = "SELECT p FROM PlanillaCobradores p WHERE p.fdocum = :fdocum")
    , @NamedQuery(name = "PlanillaCobradores.findByTtotal", query = "SELECT p FROM PlanillaCobradores p WHERE p.ttotal = :ttotal")
    , @NamedQuery(name = "PlanillaCobradores.findByFalta", query = "SELECT p FROM PlanillaCobradores p WHERE p.falta = :falta")
    , @NamedQuery(name = "PlanillaCobradores.findByCusuario", query = "SELECT p FROM PlanillaCobradores p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "PlanillaCobradores.findByMestado", query = "SELECT p FROM PlanillaCobradores p WHERE p.mestado = :mestado")})
public class PlanillaCobradores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanillaCobradoresPK planillaCobradoresPK;
    @Column(name = "cod_empleado")
    private Short codEmpleado;
    @Column(name = "fdocum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fdocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ttotal")
    private long ttotal;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumn(name = "cod_estado", referencedColumnName = "cod_estado")
    @ManyToOne
    private EstadosCobranzas codEstado;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "planillaCobradores")
    private Collection<MovimientosCobradores> movimientosCobradoresCollection;*/

    public PlanillaCobradores() {
    }

    public PlanillaCobradores(PlanillaCobradoresPK planillaCobradoresPK) {
        this.planillaCobradoresPK = planillaCobradoresPK;
    }

    public PlanillaCobradores(PlanillaCobradoresPK planillaCobradoresPK, long ttotal, Character mestado) {
        this.planillaCobradoresPK = planillaCobradoresPK;
        this.ttotal = ttotal;
        this.mestado = mestado;
    }

    public PlanillaCobradores(short codEmpr, short nplanilla) {
        this.planillaCobradoresPK = new PlanillaCobradoresPK(codEmpr, nplanilla);
    }

    public PlanillaCobradoresPK getPlanillaCobradoresPK() {
        return planillaCobradoresPK;
    }

    public void setPlanillaCobradoresPK(PlanillaCobradoresPK planillaCobradoresPK) {
        this.planillaCobradoresPK = planillaCobradoresPK;
    }

    public Short getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(Short codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    public Date getFdocum() {
        return fdocum;
    }

    public void setFdocum(Date fdocum) {
        this.fdocum = fdocum;
    }

    public long getTtotal() {
        return ttotal;
    }

    public void setTtotal(long ttotal) {
        this.ttotal = ttotal;
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

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public EstadosCobranzas getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(EstadosCobranzas codEstado) {
        this.codEstado = codEstado;
    }

    /*@XmlTransient
    public Collection<MovimientosCobradores> getMovimientosCobradoresCollection() {
        return movimientosCobradoresCollection;
    }

    public void setMovimientosCobradoresCollection(Collection<MovimientosCobradores> movimientosCobradoresCollection) {
        this.movimientosCobradoresCollection = movimientosCobradoresCollection;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planillaCobradoresPK != null ? planillaCobradoresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanillaCobradores)) {
            return false;
        }
        PlanillaCobradores other = (PlanillaCobradores) object;
        if ((this.planillaCobradoresPK == null && other.planillaCobradoresPK != null) || (this.planillaCobradoresPK != null && !this.planillaCobradoresPK.equals(other.planillaCobradoresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PlanillaCobradores[ planillaCobradoresPK=" + planillaCobradoresPK + " ]";
    }
    
}
