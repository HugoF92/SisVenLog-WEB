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
@Table(name = "cheques_emitidos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChequesEmitidos.findAll", query = "SELECT c FROM ChequesEmitidos c")
    , @NamedQuery(name = "ChequesEmitidos.findByCodEmpr", query = "SELECT c FROM ChequesEmitidos c WHERE c.chequesEmitidosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "ChequesEmitidos.findByCodBanco", query = "SELECT c FROM ChequesEmitidos c WHERE c.chequesEmitidosPK.codBanco = :codBanco")
    , @NamedQuery(name = "ChequesEmitidos.findByNroCheque", query = "SELECT c FROM ChequesEmitidos c WHERE c.chequesEmitidosPK.nroCheque = :nroCheque")
    , @NamedQuery(name = "ChequesEmitidos.findByXcuentaBco", query = "SELECT c FROM ChequesEmitidos c WHERE c.xcuentaBco = :xcuentaBco")
    , @NamedQuery(name = "ChequesEmitidos.findByFcheque", query = "SELECT c FROM ChequesEmitidos c WHERE c.fcheque = :fcheque")
    , @NamedQuery(name = "ChequesEmitidos.findByIcheque", query = "SELECT c FROM ChequesEmitidos c WHERE c.icheque = :icheque")
    , @NamedQuery(name = "ChequesEmitidos.findByFemision", query = "SELECT c FROM ChequesEmitidos c WHERE c.femision = :femision")
    , @NamedQuery(name = "ChequesEmitidos.findByFalta", query = "SELECT c FROM ChequesEmitidos c WHERE c.falta = :falta")
    , @NamedQuery(name = "ChequesEmitidos.findByCusuario", query = "SELECT c FROM ChequesEmitidos c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "ChequesEmitidos.findByFcobro", query = "SELECT c FROM ChequesEmitidos c WHERE c.fcobro = :fcobro")
    , @NamedQuery(name = "ChequesEmitidos.findByIretencion", query = "SELECT c FROM ChequesEmitidos c WHERE c.iretencion = :iretencion")})
public class ChequesEmitidos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ChequesEmitidosPK chequesEmitidosPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "xcuenta_bco")
    private String xcuentaBco;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fcheque")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcheque;
    @Basic(optional = false)
    @NotNull
    @Column(name = "icheque")
    private long icheque;
    @Basic(optional = false)
    @NotNull
    @Column(name = "femision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date femision;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fcobro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcobro;
    @Column(name = "iretencion")
    private Long iretencion;
    @JoinColumn(name = "cod_banco", referencedColumnName = "cod_banco", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Bancos bancos;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed")
    @ManyToOne(optional = false)
    private Proveedores codProveed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chequesEmitidos")
    private Collection<ChequesEmitidosDet> chequesEmitidosDetCollection;

    public ChequesEmitidos() {
    }

    public ChequesEmitidos(ChequesEmitidosPK chequesEmitidosPK) {
        this.chequesEmitidosPK = chequesEmitidosPK;
    }

    public ChequesEmitidos(ChequesEmitidosPK chequesEmitidosPK, String xcuentaBco, Date fcheque, long icheque, Date femision) {
        this.chequesEmitidosPK = chequesEmitidosPK;
        this.xcuentaBco = xcuentaBco;
        this.fcheque = fcheque;
        this.icheque = icheque;
        this.femision = femision;
    }

    public ChequesEmitidos(short codEmpr, short codBanco, String nroCheque) {
        this.chequesEmitidosPK = new ChequesEmitidosPK(codEmpr, codBanco, nroCheque);
    }

    public ChequesEmitidosPK getChequesEmitidosPK() {
        return chequesEmitidosPK;
    }

    public void setChequesEmitidosPK(ChequesEmitidosPK chequesEmitidosPK) {
        this.chequesEmitidosPK = chequesEmitidosPK;
    }

    public String getXcuentaBco() {
        return xcuentaBco;
    }

    public void setXcuentaBco(String xcuentaBco) {
        this.xcuentaBco = xcuentaBco;
    }

    public Date getFcheque() {
        return fcheque;
    }

    public void setFcheque(Date fcheque) {
        this.fcheque = fcheque;
    }

    public long getIcheque() {
        return icheque;
    }

    public void setIcheque(long icheque) {
        this.icheque = icheque;
    }

    public Date getFemision() {
        return femision;
    }

    public void setFemision(Date femision) {
        this.femision = femision;
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

    public Date getFcobro() {
        return fcobro;
    }

    public void setFcobro(Date fcobro) {
        this.fcobro = fcobro;
    }

    public Long getIretencion() {
        return iretencion;
    }

    public void setIretencion(Long iretencion) {
        this.iretencion = iretencion;
    }

    public Bancos getBancos() {
        return bancos;
    }

    public void setBancos(Bancos bancos) {
        this.bancos = bancos;
    }

    public Proveedores getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(Proveedores codProveed) {
        this.codProveed = codProveed;
    }

    @XmlTransient
    public Collection<ChequesEmitidosDet> getChequesEmitidosDetCollection() {
        return chequesEmitidosDetCollection;
    }

    public void setChequesEmitidosDetCollection(Collection<ChequesEmitidosDet> chequesEmitidosDetCollection) {
        this.chequesEmitidosDetCollection = chequesEmitidosDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chequesEmitidosPK != null ? chequesEmitidosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChequesEmitidos)) {
            return false;
        }
        ChequesEmitidos other = (ChequesEmitidos) object;
        if ((this.chequesEmitidosPK == null && other.chequesEmitidosPK != null) || (this.chequesEmitidosPK != null && !this.chequesEmitidosPK.equals(other.chequesEmitidosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ChequesEmitidos[ chequesEmitidosPK=" + chequesEmitidosPK + " ]";
    }
    
}
