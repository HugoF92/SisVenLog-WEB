/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "vencimiento_merca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VencimientoMerca.findAll", query = "SELECT v FROM VencimientoMerca v")
    , @NamedQuery(name = "VencimientoMerca.findByCodEmpr", query = "SELECT v FROM VencimientoMerca v WHERE v.vencimientoMercaPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "VencimientoMerca.findByCtipoDocum", query = "SELECT v FROM VencimientoMerca v WHERE v.vencimientoMercaPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "VencimientoMerca.findByNrofact", query = "SELECT v FROM VencimientoMerca v WHERE v.vencimientoMercaPK.nrofact = :nrofact")
    , @NamedQuery(name = "VencimientoMerca.findByCodProveed", query = "SELECT v FROM VencimientoMerca v WHERE v.vencimientoMercaPK.codProveed = :codProveed")
    , @NamedQuery(name = "VencimientoMerca.findByCodMerca", query = "SELECT v FROM VencimientoMerca v WHERE v.vencimientoMercaPK.codMerca = :codMerca")
    , @NamedQuery(name = "VencimientoMerca.findByAitem", query = "SELECT v FROM VencimientoMerca v WHERE v.vencimientoMercaPK.aitem = :aitem")
    , @NamedQuery(name = "VencimientoMerca.findByCantCajas", query = "SELECT v FROM VencimientoMerca v WHERE v.cantCajas = :cantCajas")
    , @NamedQuery(name = "VencimientoMerca.findByCantUnid", query = "SELECT v FROM VencimientoMerca v WHERE v.cantUnid = :cantUnid")
    , @NamedQuery(name = "VencimientoMerca.findByFvenc", query = "SELECT v FROM VencimientoMerca v WHERE v.fvenc = :fvenc")
    , @NamedQuery(name = "VencimientoMerca.findByFfactur", query = "SELECT v FROM VencimientoMerca v WHERE v.ffactur = :ffactur")})
public class VencimientoMerca implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VencimientoMercaPK vencimientoMercaPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_cajas")
    private short cantCajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_unid")
    private short cantUnid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fvenc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvenc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;

    public VencimientoMerca() {
    }

    public VencimientoMerca(VencimientoMercaPK vencimientoMercaPK) {
        this.vencimientoMercaPK = vencimientoMercaPK;
    }

    public VencimientoMerca(VencimientoMercaPK vencimientoMercaPK, short cantCajas, short cantUnid, Date fvenc, Date ffactur) {
        this.vencimientoMercaPK = vencimientoMercaPK;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
        this.fvenc = fvenc;
        this.ffactur = ffactur;
    }

    public VencimientoMerca(short codEmpr, String ctipoDocum, long nrofact, long codProveed, String codMerca, short aitem) {
        this.vencimientoMercaPK = new VencimientoMercaPK(codEmpr, ctipoDocum, nrofact, codProveed, codMerca, aitem);
    }

    public VencimientoMercaPK getVencimientoMercaPK() {
        return vencimientoMercaPK;
    }

    public void setVencimientoMercaPK(VencimientoMercaPK vencimientoMercaPK) {
        this.vencimientoMercaPK = vencimientoMercaPK;
    }

    public short getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(short cantCajas) {
        this.cantCajas = cantCajas;
    }

    public short getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(short cantUnid) {
        this.cantUnid = cantUnid;
    }

    public Date getFvenc() {
        return fvenc;
    }

    public void setFvenc(Date fvenc) {
        this.fvenc = fvenc;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vencimientoMercaPK != null ? vencimientoMercaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VencimientoMerca)) {
            return false;
        }
        VencimientoMerca other = (VencimientoMerca) object;
        if ((this.vencimientoMercaPK == null && other.vencimientoMercaPK != null) || (this.vencimientoMercaPK != null && !this.vencimientoMercaPK.equals(other.vencimientoMercaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.VencimientoMerca[ vencimientoMercaPK=" + vencimientoMercaPK + " ]";
    }
    
}
