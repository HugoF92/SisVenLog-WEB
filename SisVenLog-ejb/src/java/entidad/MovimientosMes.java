/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "movimientos_mes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimientosMes.findAll", query = "SELECT m FROM MovimientosMes m")
    , @NamedQuery(name = "MovimientosMes.findByCodEmpr", query = "SELECT m FROM MovimientosMes m WHERE m.movimientosMesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "MovimientosMes.findByCodDepo", query = "SELECT m FROM MovimientosMes m WHERE m.movimientosMesPK.codDepo = :codDepo")
    , @NamedQuery(name = "MovimientosMes.findByCodMerca", query = "SELECT m FROM MovimientosMes m WHERE m.movimientosMesPK.codMerca = :codMerca")
    , @NamedQuery(name = "MovimientosMes.findByCtipoDocum", query = "SELECT m FROM MovimientosMes m WHERE m.movimientosMesPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "MovimientosMes.findByCantCajas", query = "SELECT m FROM MovimientosMes m WHERE m.cantCajas = :cantCajas")
    , @NamedQuery(name = "MovimientosMes.findByCantUnid", query = "SELECT m FROM MovimientosMes m WHERE m.cantUnid = :cantUnid")
    , @NamedQuery(name = "MovimientosMes.findByNprecioPpp", query = "SELECT m FROM MovimientosMes m WHERE m.nprecioPpp = :nprecioPpp")
    , @NamedQuery(name = "MovimientosMes.findByNmes", query = "SELECT m FROM MovimientosMes m WHERE m.movimientosMesPK.nmes = :nmes")
    , @NamedQuery(name = "MovimientosMes.findByNanno", query = "SELECT m FROM MovimientosMes m WHERE m.movimientosMesPK.nanno = :nanno")})
public class MovimientosMes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MovimientosMesPK movimientosMesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_cajas")
    private int cantCajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_unid")
    private int cantUnid;
    @Column(name = "nprecio_ppp")
    private Integer nprecioPpp;

    public MovimientosMes() {
    }

    public MovimientosMes(MovimientosMesPK movimientosMesPK) {
        this.movimientosMesPK = movimientosMesPK;
    }

    public MovimientosMes(MovimientosMesPK movimientosMesPK, int cantCajas, int cantUnid) {
        this.movimientosMesPK = movimientosMesPK;
        this.cantCajas = cantCajas;
        this.cantUnid = cantUnid;
    }

    public MovimientosMes(short codEmpr, short codDepo, String codMerca, String ctipoDocum, short nmes, short nanno) {
        this.movimientosMesPK = new MovimientosMesPK(codEmpr, codDepo, codMerca, ctipoDocum, nmes, nanno);
    }

    public MovimientosMesPK getMovimientosMesPK() {
        return movimientosMesPK;
    }

    public void setMovimientosMesPK(MovimientosMesPK movimientosMesPK) {
        this.movimientosMesPK = movimientosMesPK;
    }

    public int getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(int cantCajas) {
        this.cantCajas = cantCajas;
    }

    public int getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(int cantUnid) {
        this.cantUnid = cantUnid;
    }

    public Integer getNprecioPpp() {
        return nprecioPpp;
    }

    public void setNprecioPpp(Integer nprecioPpp) {
        this.nprecioPpp = nprecioPpp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movimientosMesPK != null ? movimientosMesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientosMes)) {
            return false;
        }
        MovimientosMes other = (MovimientosMes) object;
        if ((this.movimientosMesPK == null && other.movimientosMesPK != null) || (this.movimientosMesPK != null && !this.movimientosMesPK.equals(other.movimientosMesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MovimientosMes[ movimientosMesPK=" + movimientosMesPK + " ]";
    }
    
}
