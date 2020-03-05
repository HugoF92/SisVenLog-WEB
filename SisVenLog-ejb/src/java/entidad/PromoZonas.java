/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "promo_zonas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PromoZonas.findAll", query = "SELECT p FROM PromoZonas p")
    , @NamedQuery(name = "PromoZonas.findByCodEmpr", query = "SELECT p FROM PromoZonas p WHERE p.promoZonasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "PromoZonas.findByNroPromoGral", query = "SELECT p FROM PromoZonas p WHERE p.promoZonasPK.nroPromoGral = :nroPromoGral")
    , @NamedQuery(name = "PromoZonas.findByCodZona", query = "SELECT p FROM PromoZonas p WHERE p.promoZonasPK.codZona = :codZona")
    , @NamedQuery(name = "PromoZonas.findByPpresup", query = "SELECT p FROM PromoZonas p WHERE p.ppresup = :ppresup")
    , @NamedQuery(name = "PromoZonas.findByFalta", query = "SELECT p FROM PromoZonas p WHERE p.falta = :falta")
    , @NamedQuery(name = "PromoZonas.findByCodUsuario", query = "SELECT p FROM PromoZonas p WHERE p.codUsuario = :codUsuario")
    , @NamedQuery(name = "PromoZonas.findByFultimModif", query = "SELECT p FROM PromoZonas p WHERE p.fultimModif = :fultimModif")
    , @NamedQuery(name = "PromoZonas.findByCusuarioModif", query = "SELECT p FROM PromoZonas p WHERE p.cusuarioModif = :cusuarioModif")})
public class PromoZonas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PromoZonasPK promoZonasPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ppresup")
    private BigDecimal ppresup;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cod_usuario")
    private String codUsuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    public PromoZonas() {
    }

    public PromoZonas(PromoZonasPK promoZonasPK) {
        this.promoZonasPK = promoZonasPK;
    }

    public PromoZonas(PromoZonasPK promoZonasPK, BigDecimal ppresup) {
        this.promoZonasPK = promoZonasPK;
        this.ppresup = ppresup;
    }

    public PromoZonas(short codEmpr, long nroPromoGral, String codZona) {
        this.promoZonasPK = new PromoZonasPK(codEmpr, nroPromoGral, codZona);
    }

    public PromoZonasPK getPromoZonasPK() {
        return promoZonasPK;
    }

    public void setPromoZonasPK(PromoZonasPK promoZonasPK) {
        this.promoZonasPK = promoZonasPK;
    }

    public BigDecimal getPpresup() {
        return ppresup;
    }

    public void setPpresup(BigDecimal ppresup) {
        this.ppresup = ppresup;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promoZonasPK != null ? promoZonasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PromoZonas)) {
            return false;
        }
        PromoZonas other = (PromoZonas) object;
        if ((this.promoZonasPK == null && other.promoZonasPK != null) || (this.promoZonasPK != null && !this.promoZonasPK.equals(other.promoZonasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PromoZonas[ promoZonasPK=" + promoZonasPK + " ]";
    }
    
}
