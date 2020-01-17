/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "remisiones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Remisiones.findAll", query = "SELECT r FROM Remisiones r")
    , @NamedQuery(name = "Remisiones.findByCodEmpr", query = "SELECT r FROM Remisiones r WHERE r.remisionesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Remisiones.findByNroRemision", query = "SELECT r FROM Remisiones r WHERE r.remisionesPK.nroRemision = :nroRemision")
    , @NamedQuery(name = "Remisiones.findByFremision", query = "SELECT r FROM Remisiones r WHERE r.fremision = :fremision")
    , @NamedQuery(name = "Remisiones.findByCodDepo", query = "SELECT r FROM Remisiones r WHERE r.codDepo = :codDepo")
    , @NamedQuery(name = "Remisiones.findByCodConductor", query = "SELECT r FROM Remisiones r WHERE r.codConductor = :codConductor")
    , @NamedQuery(name = "Remisiones.findByCodTransp", query = "SELECT r FROM Remisiones r WHERE r.codTransp = :codTransp")
    , @NamedQuery(name = "Remisiones.findByCodEntregador", query = "SELECT r FROM Remisiones r WHERE r.codEntregador = :codEntregador")
    , @NamedQuery(name = "Remisiones.findByMtipo", query = "SELECT r FROM Remisiones r WHERE r.mtipo = :mtipo")
    , @NamedQuery(name = "Remisiones.findByMestado", query = "SELECT r FROM Remisiones r WHERE r.mestado = :mestado")
    , @NamedQuery(name = "Remisiones.findByFalta", query = "SELECT r FROM Remisiones r WHERE r.falta = :falta")
    , @NamedQuery(name = "Remisiones.findByCusuario", query = "SELECT r FROM Remisiones r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "Remisiones.findByFultimModif", query = "SELECT r FROM Remisiones r WHERE r.fultimModif = :fultimModif")
    , @NamedQuery(name = "Remisiones.findByCusuarioModifi", query = "SELECT r FROM Remisiones r WHERE r.cusuarioModifi = :cusuarioModifi")
    , @NamedQuery(name = "Remisiones.findByFanul", query = "SELECT r FROM Remisiones r WHERE r.fanul = :fanul")
    , @NamedQuery(name = "Remisiones.findByXnroRemision", query = "SELECT r FROM Remisiones r WHERE r.xnroRemision = :xnroRemision")
    , @NamedQuery(name = "Remisiones.findByCmotivo", query = "SELECT r FROM Remisiones r WHERE r.cmotivo = :cmotivo")})
public class Remisiones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RemisionesPK remisionesPK;
    @Column(name = "fremision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fremision;
    @Column(name = "cod_depo")
    private Short codDepo;
    @Column(name = "cod_conductor")
    private Short codConductor;
    @Column(name = "cod_transp")
    private Short codTransp;
    @Column(name = "cod_entregador")
    private Short codEntregador;
    @Column(name = "mtipo")
    private Character mtipo;
    @Column(name = "mestado")
    private Character mestado;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modifi")
    private String cusuarioModifi;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Size(max = 15)
    @Column(name = "xnro_remision")
    private String xnroRemision;
    @Column(name = "cmotivo")
    private Short cmotivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remisiones")
    private Collection<RemisionesDet> remisionesDetCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remisiones")
    private Collection<RemisionesFacturas> remisionesFacturasCollection;

    public Remisiones() {
    }

    public Remisiones(RemisionesPK remisionesPK) {
        this.remisionesPK = remisionesPK;
    }

    public Remisiones(short codEmpr, long nroRemision) {
        this.remisionesPK = new RemisionesPK(codEmpr, nroRemision);
    }

    public RemisionesPK getRemisionesPK() {
        return remisionesPK;
    }

    public void setRemisionesPK(RemisionesPK remisionesPK) {
        this.remisionesPK = remisionesPK;
    }

    public Date getFremision() {
        return fremision;
    }

    public void setFremision(Date fremision) {
        this.fremision = fremision;
    }

    public Short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(Short codDepo) {
        this.codDepo = codDepo;
    }

    public Short getCodConductor() {
        return codConductor;
    }

    public void setCodConductor(Short codConductor) {
        this.codConductor = codConductor;
    }

    public Short getCodTransp() {
        return codTransp;
    }

    public void setCodTransp(Short codTransp) {
        this.codTransp = codTransp;
    }

    public Short getCodEntregador() {
        return codEntregador;
    }

    public void setCodEntregador(Short codEntregador) {
        this.codEntregador = codEntregador;
    }

    public Character getMtipo() {
        return mtipo;
    }

    public void setMtipo(Character mtipo) {
        this.mtipo = mtipo;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
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

    public String getCusuarioModifi() {
        return cusuarioModifi;
    }

    public void setCusuarioModifi(String cusuarioModifi) {
        this.cusuarioModifi = cusuarioModifi;
    }

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    public String getXnroRemision() {
        return xnroRemision;
    }

    public void setXnroRemision(String xnroRemision) {
        this.xnroRemision = xnroRemision;
    }

    public Short getCmotivo() {
        return cmotivo;
    }

    public void setCmotivo(Short cmotivo) {
        this.cmotivo = cmotivo;
    }

    @XmlTransient
    public Collection<RemisionesDet> getRemisionesDetCollection() {
        return remisionesDetCollection;
    }

    public void setRemisionesDetCollection(Collection<RemisionesDet> remisionesDetCollection) {
        this.remisionesDetCollection = remisionesDetCollection;
    }

    @XmlTransient
    public Collection<RemisionesFacturas> getRemisionesFacturasCollection() {
        return remisionesFacturasCollection;
    }

    public void setRemisionesFacturasCollection(Collection<RemisionesFacturas> remisionesFacturasCollection) {
        this.remisionesFacturasCollection = remisionesFacturasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (remisionesPK != null ? remisionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remisiones)) {
            return false;
        }
        Remisiones other = (Remisiones) object;
        if ((this.remisionesPK == null && other.remisionesPK != null) || (this.remisionesPK != null && !this.remisionesPK.equals(other.remisionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Remisiones[ remisionesPK=" + remisionesPK + " ]";
    }
    
}
