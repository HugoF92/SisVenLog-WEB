/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "corte_inventario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CorteInventario.findAll", query = "SELECT c FROM CorteInventario c")
    , @NamedQuery(name = "CorteInventario.findByCodEmpr", query = "SELECT c FROM CorteInventario c WHERE c.corteInventarioPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "CorteInventario.findByCodDepo", query = "SELECT c FROM CorteInventario c WHERE c.corteInventarioPK.codDepo = :codDepo")
    , @NamedQuery(name = "CorteInventario.findByCodMerca", query = "SELECT c FROM CorteInventario c WHERE c.corteInventarioPK.codMerca = :codMerca")
    , @NamedQuery(name = "CorteInventario.findByFmovim", query = "SELECT c FROM CorteInventario c WHERE c.corteInventarioPK.fmovim = :fmovim")
    , @NamedQuery(name = "CorteInventario.findByCantCajas", query = "SELECT c FROM CorteInventario c WHERE c.cantCajas = :cantCajas")
    , @NamedQuery(name = "CorteInventario.findByCantUnid", query = "SELECT c FROM CorteInventario c WHERE c.cantUnid = :cantUnid")})
public class CorteInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CorteInventarioPK corteInventarioPK;
    @Column(name = "cant_cajas")
    private Integer cantCajas;
    @Column(name = "cant_unid")
    private Integer cantUnid;

    public CorteInventario() {
    }

    public CorteInventario(CorteInventarioPK corteInventarioPK) {
        this.corteInventarioPK = corteInventarioPK;
    }

    public CorteInventario(short codEmpr, short codDepo, String codMerca, Date fmovim) {
        this.corteInventarioPK = new CorteInventarioPK(codEmpr, codDepo, codMerca, fmovim);
    }

    public CorteInventarioPK getCorteInventarioPK() {
        return corteInventarioPK;
    }

    public void setCorteInventarioPK(CorteInventarioPK corteInventarioPK) {
        this.corteInventarioPK = corteInventarioPK;
    }

    public Integer getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(Integer cantCajas) {
        this.cantCajas = cantCajas;
    }

    public Integer getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(Integer cantUnid) {
        this.cantUnid = cantUnid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (corteInventarioPK != null ? corteInventarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorteInventario)) {
            return false;
        }
        CorteInventario other = (CorteInventario) object;
        if ((this.corteInventarioPK == null && other.corteInventarioPK != null) || (this.corteInventarioPK != null && !this.corteInventarioPK.equals(other.corteInventarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CorteInventario[ corteInventarioPK=" + corteInventarioPK + " ]";
    }
    
}
