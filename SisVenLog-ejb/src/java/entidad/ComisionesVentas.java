/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "comisiones_ventas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComisionesVentas.findAll", query = "SELECT c FROM ComisionesVentas c")
    , @NamedQuery(name = "ComisionesVentas.findByCodEmpr", query = "SELECT c FROM ComisionesVentas c WHERE c.comisionesVentasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "ComisionesVentas.findByCtipoEmp", query = "SELECT c FROM ComisionesVentas c WHERE c.comisionesVentasPK.ctipoEmp = :ctipoEmp")
    , @NamedQuery(name = "ComisionesVentas.findByCodSublinea", query = "SELECT c FROM ComisionesVentas c WHERE c.comisionesVentasPK.codSublinea = :codSublinea")
    , @NamedQuery(name = "ComisionesVentas.findByCodZona", query = "SELECT c FROM ComisionesVentas c WHERE c.comisionesVentasPK.codZona = :codZona")
    , @NamedQuery(name = "ComisionesVentas.findByPcomis", query = "SELECT c FROM ComisionesVentas c WHERE c.pcomis = :pcomis")
    , @NamedQuery(name = "ComisionesVentas.findByFrigeDesde", query = "SELECT c FROM ComisionesVentas c WHERE c.comisionesVentasPK.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "ComisionesVentas.findByFrigeHasta", query = "SELECT c FROM ComisionesVentas c WHERE c.frigeHasta = :frigeHasta")})
public class ComisionesVentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComisionesVentasPK comisionesVentasPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pcomis")
    private BigDecimal pcomis;
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Zonas zonas;

    public ComisionesVentas() {
    }

    public ComisionesVentas(ComisionesVentasPK comisionesVentasPK) {
        this.comisionesVentasPK = comisionesVentasPK;
    }

    public ComisionesVentas(short codEmpr, String ctipoEmp, short codSublinea, String codZona, Date frigeDesde) {
        this.comisionesVentasPK = new ComisionesVentasPK(codEmpr, ctipoEmp, codSublinea, codZona, frigeDesde);
    }

    public ComisionesVentasPK getComisionesVentasPK() {
        return comisionesVentasPK;
    }

    public void setComisionesVentasPK(ComisionesVentasPK comisionesVentasPK) {
        this.comisionesVentasPK = comisionesVentasPK;
    }

    public BigDecimal getPcomis() {
        return pcomis;
    }

    public void setPcomis(BigDecimal pcomis) {
        this.pcomis = pcomis;
    }

    public Date getFrigeHasta() {
        return frigeHasta;
    }

    public void setFrigeHasta(Date frigeHasta) {
        this.frigeHasta = frigeHasta;
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
        hash += (comisionesVentasPK != null ? comisionesVentasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComisionesVentas)) {
            return false;
        }
        ComisionesVentas other = (ComisionesVentas) object;
        if ((this.comisionesVentasPK == null && other.comisionesVentasPK != null) || (this.comisionesVentasPK != null && !this.comisionesVentasPK.equals(other.comisionesVentasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ComisionesVentas[ comisionesVentasPK=" + comisionesVentasPK + " ]";
    }
    
}
