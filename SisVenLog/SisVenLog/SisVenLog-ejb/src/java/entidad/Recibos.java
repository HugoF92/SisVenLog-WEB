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
@Table(name = "recibos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recibos.findAll", query = "SELECT r FROM Recibos r")
    , @NamedQuery(name = "Recibos.findByCodEmpr", query = "SELECT r FROM Recibos r WHERE r.recibosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Recibos.findByNrecibo", query = "SELECT r FROM Recibos r WHERE r.recibosPK.nrecibo = :nrecibo")
    , @NamedQuery(name = "Recibos.findByCodCliente", query = "SELECT r FROM Recibos r WHERE r.codCliente = :codCliente")
    , @NamedQuery(name = "Recibos.findByFrecibo", query = "SELECT r FROM Recibos r WHERE r.frecibo = :frecibo")
    , @NamedQuery(name = "Recibos.findByIrecibo", query = "SELECT r FROM Recibos r WHERE r.irecibo = :irecibo")
    , @NamedQuery(name = "Recibos.findByIefectivo", query = "SELECT r FROM Recibos r WHERE r.iefectivo = :iefectivo")
    , @NamedQuery(name = "Recibos.findByIretencion", query = "SELECT r FROM Recibos r WHERE r.iretencion = :iretencion")
    , @NamedQuery(name = "Recibos.findByIcheques", query = "SELECT r FROM Recibos r WHERE r.icheques = :icheques")
    , @NamedQuery(name = "Recibos.findByXobs", query = "SELECT r FROM Recibos r WHERE r.xobs = :xobs")
    , @NamedQuery(name = "Recibos.findByMestado", query = "SELECT r FROM Recibos r WHERE r.mestado = :mestado")
    , @NamedQuery(name = "Recibos.findByCusuario", query = "SELECT r FROM Recibos r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "Recibos.findByFalta", query = "SELECT r FROM Recibos r WHERE r.falta = :falta")
    , @NamedQuery(name = "Recibos.findByFanul", query = "SELECT r FROM Recibos r WHERE r.fanul = :fanul")})
public class Recibos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecibosPK recibosPK;
    @Column(name = "cod_cliente")
    private Integer codCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frecibo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frecibo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "irecibo")
    private long irecibo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iefectivo")
    private long iefectivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iretencion")
    private long iretencion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "icheques")
    private long icheques;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "xobs")
    private String xobs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recibos")
    private Collection<RecibosCheques> recibosChequesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recibos")
    private Collection<RecibosDet> recibosDetCollection;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "recibos")
    private Collection<MovimientosCobradores> movimientosCobradoresCollection;*/

    public Recibos() {
    }

    public Recibos(RecibosPK recibosPK) {
        this.recibosPK = recibosPK;
    }

    public Recibos(RecibosPK recibosPK, Date frecibo, long irecibo, long iefectivo, long iretencion, long icheques, String xobs, Character mestado) {
        this.recibosPK = recibosPK;
        this.frecibo = frecibo;
        this.irecibo = irecibo;
        this.iefectivo = iefectivo;
        this.iretencion = iretencion;
        this.icheques = icheques;
        this.xobs = xobs;
        this.mestado = mestado;
    }

    public Recibos(short codEmpr, long nrecibo) {
        this.recibosPK = new RecibosPK(codEmpr, nrecibo);
    }

    public RecibosPK getRecibosPK() {
        return recibosPK;
    }

    public void setRecibosPK(RecibosPK recibosPK) {
        this.recibosPK = recibosPK;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public Date getFrecibo() {
        return frecibo;
    }

    public void setFrecibo(Date frecibo) {
        this.frecibo = frecibo;
    }

    public long getIrecibo() {
        return irecibo;
    }

    public void setIrecibo(long irecibo) {
        this.irecibo = irecibo;
    }

    public long getIefectivo() {
        return iefectivo;
    }

    public void setIefectivo(long iefectivo) {
        this.iefectivo = iefectivo;
    }

    public long getIretencion() {
        return iretencion;
    }

    public void setIretencion(long iretencion) {
        this.iretencion = iretencion;
    }

    public long getIcheques() {
        return icheques;
    }

    public void setIcheques(long icheques) {
        this.icheques = icheques;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
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

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    @XmlTransient
    public Collection<RecibosCheques> getRecibosChequesCollection() {
        return recibosChequesCollection;
    }

    public void setRecibosChequesCollection(Collection<RecibosCheques> recibosChequesCollection) {
        this.recibosChequesCollection = recibosChequesCollection;
    }

    @XmlTransient
    public Collection<RecibosDet> getRecibosDetCollection() {
        return recibosDetCollection;
    }

    public void setRecibosDetCollection(Collection<RecibosDet> recibosDetCollection) {
        this.recibosDetCollection = recibosDetCollection;
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
        hash += (recibosPK != null ? recibosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recibos)) {
            return false;
        }
        Recibos other = (Recibos) object;
        if ((this.recibosPK == null && other.recibosPK != null) || (this.recibosPK != null && !this.recibosPK.equals(other.recibosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Recibos[ recibosPK=" + recibosPK + " ]";
    }
    
}
