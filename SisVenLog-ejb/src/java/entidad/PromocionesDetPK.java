package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Marcos Brizuela
 */

@Embeddable
public class PromocionesDetPK implements Serializable {
    
    @NotNull
    @Basic(optional = false)
    @Column(name = "cod_empr")
    private Integer codEmpresa;
    @NotNull
    @Basic(optional = false)
    @Column(name = "nro_promo")
    private Long nroPromocion;
    @NotNull
    @Basic(optional = false)
    @Column(name = "aitem")
    private Integer aItem;
    
    public PromocionesDetPK() {}

    public PromocionesDetPK(Long nroPromocion) {
        this.codEmpresa = 2;
        this.nroPromocion = nroPromocion;
    }
    
    public PromocionesDetPK(Integer codEmpresa, Long nroPromocion, Integer aItem) {
        this.codEmpresa = codEmpresa;
        this.nroPromocion = nroPromocion;
        this.aItem = aItem;
    }

    public Integer getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Long getNroPromocion() {
        return nroPromocion;
    }

    public void setNroPromocion(Long nroPromocion) {
        this.nroPromocion = nroPromocion;
    }

    public Integer getAItem() {
        return aItem;
    }

    public void setAItem(Integer aItem) {
        this.aItem = aItem;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PromocionesDetPK)) {
            return false;
        }
        PromocionesDetPK other = (PromocionesDetPK) object;
        if (this.codEmpresa != other.codEmpresa) {
            return false;
        }
        if (this.nroPromocion != other.nroPromocion) {
            return false;
        }
        if (this.aItem != other.aItem) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PromocionesDetPK[ codEmpr=" + codEmpresa + ", nroPromo=" + nroPromocion + ", aItem=" + aItem + " ]";
    }
    
}
