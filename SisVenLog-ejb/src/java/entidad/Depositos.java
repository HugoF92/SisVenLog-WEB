/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "depositos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Depositos.findAll", query = "SELECT d FROM Depositos d")
    , @NamedQuery(name = "Depositos.findByCodEmpr", query = "SELECT d FROM Depositos d WHERE d.depositosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Depositos.findByCodDepo", query = "SELECT d FROM Depositos d WHERE d.depositosPK.codDepo = :codDepo")
    , @NamedQuery(name = "Depositos.findByXdesc", query = "SELECT d FROM Depositos d WHERE d.xdesc = :xdesc")
    , @NamedQuery(name = "Depositos.findByMtipo", query = "SELECT d FROM Depositos d WHERE d.mtipo = :mtipo")
    , @NamedQuery(name = "Depositos.findByNpesoMax", query = "SELECT d FROM Depositos d WHERE d.npesoMax = :npesoMax")
    , @NamedQuery(name = "Depositos.findByFultimInven", query = "SELECT d FROM Depositos d WHERE d.fultimInven = :fultimInven")
    , @NamedQuery(name = "Depositos.findByFalta", query = "SELECT d FROM Depositos d WHERE d.falta = :falta")
    , @NamedQuery(name = "Depositos.findByCusuario", query = "SELECT d FROM Depositos d WHERE d.cusuario = :cusuario")
    , @NamedQuery(name = "Depositos.findByFultimModif", query = "SELECT d FROM Depositos d WHERE d.fultimModif = :fultimModif")
    , @NamedQuery(name = "Depositos.findByCusuarioModif", query = "SELECT d FROM Depositos d WHERE d.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Depositos.findByNpuntoEst", query = "SELECT d FROM Depositos d WHERE d.npuntoEst = :npuntoEst")
    , @NamedQuery(name = "Depositos.findByNpuntoExp", query = "SELECT d FROM Depositos d WHERE d.npuntoExp = :npuntoExp")
    , @NamedQuery(name = "Depositos.findByXchapa", query = "SELECT d FROM Depositos d WHERE d.xchapa = :xchapa")
    , @NamedQuery(name = "Depositos.findByXmarca", query = "SELECT d FROM Depositos d WHERE d.xmarca = :xmarca")
    , @NamedQuery(name = "Depositos.findByXobs", query = "SELECT d FROM Depositos d WHERE d.xobs = :xobs")})
public class Depositos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DepositosPK depositosPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mtipo")
    private Character mtipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "npeso_max")
    private BigDecimal npesoMax;
    @Column(name = "fultim_inven")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimInven;
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
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "npunto_est")
    private Short npuntoEst;
    @Column(name = "npunto_exp")
    private Short npuntoExp;
    @Size(max = 20)
    @Column(name = "xchapa")
    private String xchapa;
    @Size(max = 30)
    @Column(name = "xmarca")
    private String xmarca;
    @Size(max = 150)
    @Column(name = "xobs")
    private String xobs;
    @JoinColumn(name = "cod_conductor", referencedColumnName = "cod_conductor")
    @ManyToOne
    private Conductores codConductor;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumn(name = "cod_transp", referencedColumnName = "cod_transp")
    @ManyToOne
    private Transportistas codTransp;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona")})
    @ManyToOne(optional = false)
    private Zonas zonas;

    public Depositos() {
    }

    public Depositos(DepositosPK depositosPK) {
        this.depositosPK = depositosPK;
    }

    public Depositos(DepositosPK depositosPK, Character mtipo) {
        this.depositosPK = depositosPK;
        this.mtipo = mtipo;
    }

    public Depositos(short codEmpr, short codDepo) {
        this.depositosPK = new DepositosPK(codEmpr, codDepo);
    }

    public DepositosPK getDepositosPK() {
        return depositosPK;
    }

    public void setDepositosPK(DepositosPK depositosPK) {
        this.depositosPK = depositosPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Character getMtipo() {
        return mtipo;
    }

    public void setMtipo(Character mtipo) {
        this.mtipo = mtipo;
    }

    public BigDecimal getNpesoMax() {
        return npesoMax;
    }

    public void setNpesoMax(BigDecimal npesoMax) {
        this.npesoMax = npesoMax;
    }

    public Date getFultimInven() {
        return fultimInven;
    }

    public void setFultimInven(Date fultimInven) {
        this.fultimInven = fultimInven;
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

    public Short getNpuntoEst() {
        return npuntoEst;
    }

    public void setNpuntoEst(Short npuntoEst) {
        this.npuntoEst = npuntoEst;
    }

    public Short getNpuntoExp() {
        return npuntoExp;
    }

    public void setNpuntoExp(Short npuntoExp) {
        this.npuntoExp = npuntoExp;
    }

    public String getXchapa() {
        return xchapa;
    }

    public void setXchapa(String xchapa) {
        this.xchapa = xchapa;
    }

    public String getXmarca() {
        return xmarca;
    }

    public void setXmarca(String xmarca) {
        this.xmarca = xmarca;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
    }

    public Conductores getCodConductor() {
        return codConductor;
    }

    public void setCodConductor(Conductores codConductor) {
        this.codConductor = codConductor;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public Transportistas getCodTransp() {
        return codTransp;
    }

    public void setCodTransp(Transportistas codTransp) {
        this.codTransp = codTransp;
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
        hash += (depositosPK != null ? depositosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Depositos)) {
            return false;
        }
        Depositos other = (Depositos) object;
        if ((this.depositosPK == null && other.depositosPK != null) || (this.depositosPK != null && !this.depositosPK.equals(other.depositosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Depositos[ depositosPK=" + depositosPK + " ]";
    }
    
}
