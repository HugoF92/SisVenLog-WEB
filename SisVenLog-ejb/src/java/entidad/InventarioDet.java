/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "inventario_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventarioDet.findAll", query = "SELECT i FROM InventarioDet i")
    , @NamedQuery(name = "InventarioDet.findByCodEmpr", query = "SELECT i FROM InventarioDet i WHERE i.inventarioDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "InventarioDet.findByNroInven", query = "SELECT i FROM InventarioDet i WHERE i.inventarioDetPK.nroInven = :nroInven")
    , @NamedQuery(name = "InventarioDet.findByCodMerca", query = "SELECT i FROM InventarioDet i WHERE i.inventarioDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "InventarioDet.findByCajasFisicas", query = "SELECT i FROM InventarioDet i WHERE i.cajasFisicas = :cajasFisicas")
    , @NamedQuery(name = "InventarioDet.findByUnidFisicas", query = "SELECT i FROM InventarioDet i WHERE i.unidFisicas = :unidFisicas")
    , @NamedQuery(name = "InventarioDet.findByCajasExist", query = "SELECT i FROM InventarioDet i WHERE i.cajasExist = :cajasExist")
    , @NamedQuery(name = "InventarioDet.findByUnidExist", query = "SELECT i FROM InventarioDet i WHERE i.unidExist = :unidExist")})
public class InventarioDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InventarioDetPK inventarioDetPK;
    @Column(name = "cajas_fisicas")
    private Long cajasFisicas;
    @Column(name = "unid_fisicas")
    private Long unidFisicas;
    @Column(name = "cajas_exist")
    private Long cajasExist;
    @Column(name = "unid_exist")
    private Long unidExist;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;

    public InventarioDet() {
    }

    public InventarioDet(InventarioDetPK inventarioDetPK) {
        this.inventarioDetPK = inventarioDetPK;
    }

    public InventarioDet(short codEmpr, int nroInven, String codMerca) {
        this.inventarioDetPK = new InventarioDetPK(codEmpr, nroInven, codMerca);
    }

    public InventarioDetPK getInventarioDetPK() {
        return inventarioDetPK;
    }

    public void setInventarioDetPK(InventarioDetPK inventarioDetPK) {
        this.inventarioDetPK = inventarioDetPK;
    }

    public Long getCajasFisicas() {
        return cajasFisicas;
    }

    public void setCajasFisicas(Long cajasFisicas) {
        this.cajasFisicas = cajasFisicas;
    }

    public Long getUnidFisicas() {
        return unidFisicas;
    }

    public void setUnidFisicas(Long unidFisicas) {
        this.unidFisicas = unidFisicas;
    }

    public Long getCajasExist() {
        return cajasExist;
    }

    public void setCajasExist(Long cajasExist) {
        this.cajasExist = cajasExist;
    }

    public Long getUnidExist() {
        return unidExist;
    }

    public void setUnidExist(Long unidExist) {
        this.unidExist = unidExist;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inventarioDetPK != null ? inventarioDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventarioDet)) {
            return false;
        }
        InventarioDet other = (InventarioDet) object;
        if ((this.inventarioDetPK == null && other.inventarioDetPK != null) || (this.inventarioDetPK != null && !this.inventarioDetPK.equals(other.inventarioDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.InventarioDet[ inventarioDetPK=" + inventarioDetPK + " ]";
    }
    
}
