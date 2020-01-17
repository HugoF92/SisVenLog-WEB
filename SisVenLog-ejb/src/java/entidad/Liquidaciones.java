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
@Table(name = "liquidaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Liquidaciones.findAll", query = "SELECT l FROM Liquidaciones l")
    , @NamedQuery(name = "Liquidaciones.findByCodEmpr", query = "SELECT l FROM Liquidaciones l WHERE l.liquidacionesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Liquidaciones.findByNroLiquid", query = "SELECT l FROM Liquidaciones l WHERE l.liquidacionesPK.nroLiquid = :nroLiquid")
    , @NamedQuery(name = "Liquidaciones.findByCodEmpleado", query = "SELECT l FROM Liquidaciones l WHERE l.codEmpleado = :codEmpleado")
    , @NamedQuery(name = "Liquidaciones.findByFinicial", query = "SELECT l FROM Liquidaciones l WHERE l.finicial = :finicial")
    , @NamedQuery(name = "Liquidaciones.findByFfinal", query = "SELECT l FROM Liquidaciones l WHERE l.ffinal = :ffinal")
    , @NamedQuery(name = "Liquidaciones.findByTcomis", query = "SELECT l FROM Liquidaciones l WHERE l.tcomis = :tcomis")
    , @NamedQuery(name = "Liquidaciones.findByFalta", query = "SELECT l FROM Liquidaciones l WHERE l.falta = :falta")
    , @NamedQuery(name = "Liquidaciones.findByCusuario", query = "SELECT l FROM Liquidaciones l WHERE l.cusuario = :cusuario")
    , @NamedQuery(name = "Liquidaciones.findByNmes", query = "SELECT l FROM Liquidaciones l WHERE l.nmes = :nmes")
    , @NamedQuery(name = "Liquidaciones.findByNanno", query = "SELECT l FROM Liquidaciones l WHERE l.nanno = :nanno")
    , @NamedQuery(name = "Liquidaciones.findByMestado", query = "SELECT l FROM Liquidaciones l WHERE l.mestado = :mestado")})
public class Liquidaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LiquidacionesPK liquidacionesPK;
    @Column(name = "cod_empleado")
    private Short codEmpleado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "finicial")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finicial;
    @Column(name = "ffinal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffinal;
    @Column(name = "tcomis")
    private Long tcomis;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nmes")
    private short nmes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nanno")
    private short nanno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "liquidaciones")
    private Collection<LiquidacionesDet> liquidacionesDetCollection;

    public Liquidaciones() {
    }

    public Liquidaciones(LiquidacionesPK liquidacionesPK) {
        this.liquidacionesPK = liquidacionesPK;
    }

    public Liquidaciones(LiquidacionesPK liquidacionesPK, Date finicial, short nmes, short nanno, Character mestado) {
        this.liquidacionesPK = liquidacionesPK;
        this.finicial = finicial;
        this.nmes = nmes;
        this.nanno = nanno;
        this.mestado = mestado;
    }

    public Liquidaciones(short codEmpr, int nroLiquid) {
        this.liquidacionesPK = new LiquidacionesPK(codEmpr, nroLiquid);
    }

    public LiquidacionesPK getLiquidacionesPK() {
        return liquidacionesPK;
    }

    public void setLiquidacionesPK(LiquidacionesPK liquidacionesPK) {
        this.liquidacionesPK = liquidacionesPK;
    }

    public Short getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(Short codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    public Date getFinicial() {
        return finicial;
    }

    public void setFinicial(Date finicial) {
        this.finicial = finicial;
    }

    public Date getFfinal() {
        return ffinal;
    }

    public void setFfinal(Date ffinal) {
        this.ffinal = ffinal;
    }

    public Long getTcomis() {
        return tcomis;
    }

    public void setTcomis(Long tcomis) {
        this.tcomis = tcomis;
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

    public short getNmes() {
        return nmes;
    }

    public void setNmes(short nmes) {
        this.nmes = nmes;
    }

    public short getNanno() {
        return nanno;
    }

    public void setNanno(short nanno) {
        this.nanno = nanno;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    @XmlTransient
    public Collection<LiquidacionesDet> getLiquidacionesDetCollection() {
        return liquidacionesDetCollection;
    }

    public void setLiquidacionesDetCollection(Collection<LiquidacionesDet> liquidacionesDetCollection) {
        this.liquidacionesDetCollection = liquidacionesDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (liquidacionesPK != null ? liquidacionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Liquidaciones)) {
            return false;
        }
        Liquidaciones other = (Liquidaciones) object;
        if ((this.liquidacionesPK == null && other.liquidacionesPK != null) || (this.liquidacionesPK != null && !this.liquidacionesPK.equals(other.liquidacionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Liquidaciones[ liquidacionesPK=" + liquidacionesPK + " ]";
    }
    
}
