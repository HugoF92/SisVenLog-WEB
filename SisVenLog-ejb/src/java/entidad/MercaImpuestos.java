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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "merca_impuestos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MercaImpuestos.findAll", query = "SELECT m FROM MercaImpuestos m")
    , @NamedQuery(name = "MercaImpuestos.findByCodEmpr", query = "SELECT m FROM MercaImpuestos m WHERE m.mercaImpuestosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "MercaImpuestos.findByCodMerca", query = "SELECT m FROM MercaImpuestos m WHERE m.mercaImpuestosPK.codMerca = :codMerca")
    , @NamedQuery(name = "MercaImpuestos.findByCodImpu", query = "SELECT m FROM MercaImpuestos m WHERE m.mercaImpuestosPK.codImpu = :codImpu")
    , @NamedQuery(name = "MercaImpuestos.findByCusuario", query = "SELECT m FROM MercaImpuestos m WHERE m.cusuario = :cusuario")
    , @NamedQuery(name = "MercaImpuestos.findByFalta", query = "SELECT m FROM MercaImpuestos m WHERE m.falta = :falta")
    , @NamedQuery(name = "MercaImpuestos.findByCusuarioModif", query = "SELECT m FROM MercaImpuestos m WHERE m.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "MercaImpuestos.findByFultimModif", query = "SELECT m FROM MercaImpuestos m WHERE m.fultimModif = :fultimModif")})
public class MercaImpuestos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MercaImpuestosPK mercaImpuestosPK;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;

    public MercaImpuestos() {
    }

    public MercaImpuestos(MercaImpuestosPK mercaImpuestosPK) {
        this.mercaImpuestosPK = mercaImpuestosPK;
    }

    public MercaImpuestos(short codEmpr, String codMerca, short codImpu) {
        this.mercaImpuestosPK = new MercaImpuestosPK(codEmpr, codMerca, codImpu);
    }

    public MercaImpuestosPK getMercaImpuestosPK() {
        return mercaImpuestosPK;
    }

    public void setMercaImpuestosPK(MercaImpuestosPK mercaImpuestosPK) {
        this.mercaImpuestosPK = mercaImpuestosPK;
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

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
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
        hash += (mercaImpuestosPK != null ? mercaImpuestosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MercaImpuestos)) {
            return false;
        }
        MercaImpuestos other = (MercaImpuestos) object;
        if ((this.mercaImpuestosPK == null && other.mercaImpuestosPK != null) || (this.mercaImpuestosPK != null && !this.mercaImpuestosPK.equals(other.mercaImpuestosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MercaImpuestos[ mercaImpuestosPK=" + mercaImpuestosPK + " ]";
    }
    
}
