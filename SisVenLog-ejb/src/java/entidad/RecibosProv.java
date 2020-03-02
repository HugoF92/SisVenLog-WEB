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
import javax.persistence.FetchType;
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
@Table(name = "recibos_prov")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecibosProv.findAll", query = "SELECT r FROM RecibosProv r")
    , @NamedQuery(name = "RecibosProv.findByCodEmpr", query = "SELECT r FROM RecibosProv r WHERE r.recibosProvPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RecibosProv.findByNrecibo", query = "SELECT r FROM RecibosProv r WHERE r.recibosProvPK.nrecibo = :nrecibo")
    , @NamedQuery(name = "RecibosProv.findByCodProveed", query = "SELECT r FROM RecibosProv r WHERE r.recibosProvPK.codProveed = :codProveed")
    , @NamedQuery(name = "RecibosProv.findByFrecibo", query = "SELECT r FROM RecibosProv r WHERE r.frecibo = :frecibo")
    , @NamedQuery(name = "RecibosProv.findByCusuario", query = "SELECT r FROM RecibosProv r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "RecibosProv.findByFalta", query = "SELECT r FROM RecibosProv r WHERE r.falta = :falta")
    , @NamedQuery(name = "RecibosProv.findByFanul", query = "SELECT r FROM RecibosProv r WHERE r.fanul = :fanul")
    , @NamedQuery(name = "RecibosProv.findByIrecibo", query = "SELECT r FROM RecibosProv r WHERE r.irecibo = :irecibo")
    , @NamedQuery(name = "RecibosProv.findByIefectivo", query = "SELECT r FROM RecibosProv r WHERE r.iefectivo = :iefectivo")
    , @NamedQuery(name = "RecibosProv.findByIretencion", query = "SELECT r FROM RecibosProv r WHERE r.iretencion = :iretencion")
    , @NamedQuery(name = "RecibosProv.findByIcheques", query = "SELECT r FROM RecibosProv r WHERE r.icheques = :icheques")
    , @NamedQuery(name = "RecibosProv.findByXobs", query = "SELECT r FROM RecibosProv r WHERE r.xobs = :xobs")
    , @NamedQuery(name = "RecibosProv.findByMestado", query = "SELECT r FROM RecibosProv r WHERE r.mestado = :mestado")})
public class RecibosProv implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecibosProvPK recibosProvPK;
    @Column(name = "frecibo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frecibo;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
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
    @Size(max = 200)
    @Column(name = "xobs")
    private String xobs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recibosProv", fetch = FetchType.LAZY)
    private Collection<RecibosProvCheques> recibosProvChequesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recibosProv", fetch = FetchType.LAZY)
    private Collection<RecibosProvDet> recibosProvDetCollection;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proveedores proveedores;

    public RecibosProv() {
    }

    public RecibosProv(RecibosProvPK recibosProvPK) {
        this.recibosProvPK = recibosProvPK;
    }

    public RecibosProv(RecibosProvPK recibosProvPK, long irecibo, long iefectivo, long iretencion, long icheques, Character mestado) {
        this.recibosProvPK = recibosProvPK;
        this.irecibo = irecibo;
        this.iefectivo = iefectivo;
        this.iretencion = iretencion;
        this.icheques = icheques;
        this.mestado = mestado;
    }

    public RecibosProv(short codEmpr, long nrecibo, short codProveed) {
        this.recibosProvPK = new RecibosProvPK(codEmpr, nrecibo, codProveed);
    }

    public RecibosProvPK getRecibosProvPK() {
        return recibosProvPK;
    }

    public void setRecibosProvPK(RecibosProvPK recibosProvPK) {
        this.recibosProvPK = recibosProvPK;
    }

    public Date getFrecibo() {
        return frecibo;
    }

    public void setFrecibo(Date frecibo) {
        this.frecibo = frecibo;
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

    @XmlTransient
    public Collection<RecibosProvCheques> getRecibosProvChequesCollection() {
        return recibosProvChequesCollection;
    }

    public void setRecibosProvChequesCollection(Collection<RecibosProvCheques> recibosProvChequesCollection) {
        this.recibosProvChequesCollection = recibosProvChequesCollection;
    }

    @XmlTransient
    public Collection<RecibosProvDet> getRecibosProvDetCollection() {
        return recibosProvDetCollection;
    }

    public void setRecibosProvDetCollection(Collection<RecibosProvDet> recibosProvDetCollection) {
        this.recibosProvDetCollection = recibosProvDetCollection;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recibosProvPK != null ? recibosProvPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecibosProv)) {
            return false;
        }
        RecibosProv other = (RecibosProv) object;
        if ((this.recibosProvPK == null && other.recibosProvPK != null) || (this.recibosProvPK != null && !this.recibosProvPK.equals(other.recibosProvPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecibosProv[ recibosProvPK=" + recibosProvPK + " ]";
    }
    
}
