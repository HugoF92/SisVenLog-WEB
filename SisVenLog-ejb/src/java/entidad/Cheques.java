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
 * @author Edu
 */
@Entity
@Table(name = "cheques")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cheques.findAll", query = "SELECT c FROM Cheques c")
    , @NamedQuery(name = "Cheques.findByCodEmpr", query = "SELECT c FROM Cheques c WHERE c.chequesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Cheques.findByCodBanco", query = "SELECT c FROM Cheques c WHERE c.chequesPK.codBanco = :codBanco")
    , @NamedQuery(name = "Cheques.findByNroCheque", query = "SELECT c FROM Cheques c WHERE c.chequesPK.nroCheque = :nroCheque")
    , @NamedQuery(name = "Cheques.findByXcuentaBco", query = "SELECT c FROM Cheques c WHERE c.chequesPK.xcuentaBco = :xcuentaBco")
    , @NamedQuery(name = "Cheques.findByFcheque", query = "SELECT c FROM Cheques c WHERE c.fcheque = :fcheque")
    , @NamedQuery(name = "Cheques.findByIcheque", query = "SELECT c FROM Cheques c WHERE c.icheque = :icheque")
    , @NamedQuery(name = "Cheques.findByFrechazo", query = "SELECT c FROM Cheques c WHERE c.frechazo = :frechazo")
    , @NamedQuery(name = "Cheques.findByFcobro", query = "SELECT c FROM Cheques c WHERE c.fcobro = :fcobro")
    , @NamedQuery(name = "Cheques.findByFemision", query = "SELECT c FROM Cheques c WHERE c.femision = :femision")
    , @NamedQuery(name = "Cheques.findByXtitular", query = "SELECT c FROM Cheques c WHERE c.xtitular = :xtitular")
    , @NamedQuery(name = "Cheques.findByFalta", query = "SELECT c FROM Cheques c WHERE c.falta = :falta")
    , @NamedQuery(name = "Cheques.findByCusuario", query = "SELECT c FROM Cheques c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "Cheques.findByMtipo", query = "SELECT c FROM Cheques c WHERE c.mtipo = :mtipo")
    , @NamedQuery(name = "Cheques.findByCodEntregador", query = "SELECT c FROM Cheques c WHERE c.codEntregador = :codEntregador")
    , @NamedQuery(name = "Cheques.findByCodZona", query = "SELECT c FROM Cheques c WHERE c.codZona = :codZona")})
public class Cheques implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ChequesPK chequesPK;
    @Column(name = "fcheque")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcheque;
    @Column(name = "icheque")
    private Long icheque;
    @Column(name = "frechazo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frechazo;
    @Column(name = "fcobro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcobro;
    @Column(name = "femision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date femision;
    @Size(max = 50)
    @Column(name = "xtitular")
    private String xtitular;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mtipo")
    private Character mtipo;
    @Column(name = "cod_entregador")
    private Short codEntregador;
    @Size(max = 2)
    @Column(name = "cod_zona")
    private String codZona;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cheques")
    private Collection<ChequesDet> chequesDetCollection;
    @JoinColumn(name = "cod_banco", referencedColumnName = "cod_banco", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Bancos bancos;
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente")
    @ManyToOne
    private Clientes codCliente;

    public Cheques() {
    }

    public Cheques(ChequesPK chequesPK) {
        this.chequesPK = chequesPK;
    }

    public Cheques(ChequesPK chequesPK, Character mtipo) {
        this.chequesPK = chequesPK;
        this.mtipo = mtipo;
    }

    public Cheques(short codEmpr, short codBanco, String nroCheque, String xcuentaBco) {
        this.chequesPK = new ChequesPK(codEmpr, codBanco, nroCheque, xcuentaBco);
    }

    public ChequesPK getChequesPK() {
        return chequesPK;
    }

    public void setChequesPK(ChequesPK chequesPK) {
        this.chequesPK = chequesPK;
    }

    public Date getFcheque() {
        return fcheque;
    }

    public void setFcheque(Date fcheque) {
        this.fcheque = fcheque;
    }

    public Long getIcheque() {
        return icheque;
    }

    public void setIcheque(Long icheque) {
        this.icheque = icheque;
    }

    public Date getFrechazo() {
        return frechazo;
    }

    public void setFrechazo(Date frechazo) {
        this.frechazo = frechazo;
    }

    public Date getFcobro() {
        return fcobro;
    }

    public void setFcobro(Date fcobro) {
        this.fcobro = fcobro;
    }

    public Date getFemision() {
        return femision;
    }

    public void setFemision(Date femision) {
        this.femision = femision;
    }

    public String getXtitular() {
        return xtitular;
    }

    public void setXtitular(String xtitular) {
        this.xtitular = xtitular;
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

    public Character getMtipo() {
        return mtipo;
    }

    public void setMtipo(Character mtipo) {
        this.mtipo = mtipo;
    }

    public Short getCodEntregador() {
        return codEntregador;
    }

    public void setCodEntregador(Short codEntregador) {
        this.codEntregador = codEntregador;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    @XmlTransient
    public Collection<ChequesDet> getChequesDetCollection() {
        return chequesDetCollection;
    }

    public void setChequesDetCollection(Collection<ChequesDet> chequesDetCollection) {
        this.chequesDetCollection = chequesDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chequesPK != null ? chequesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cheques)) {
            return false;
        }
        Cheques other = (Cheques) object;
        if ((this.chequesPK == null && other.chequesPK != null) || (this.chequesPK != null && !this.chequesPK.equals(other.chequesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Cheques[ chequesPK=" + chequesPK + " ]";
    }

    public Bancos getBancos() {
        return bancos;
    }

    public void setBancos(Bancos bancos) {
        this.bancos = bancos;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }
    
}
