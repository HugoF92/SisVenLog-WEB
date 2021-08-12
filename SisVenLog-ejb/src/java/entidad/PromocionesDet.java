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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marcos Brizuela
 */

@Entity
@Table(name = "promociones_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PromocionesDet.findAll", query = "SELECT p FROM PromocionesDet p")
    , @NamedQuery(name = "PromocionesDet.findByCodEmpr", query = "SELECT p FROM PromocionesDet p WHERE p.promocionesDetPK.codEmpresa = :codEmpr")
    , @NamedQuery(name = "PromocionesDet.findByNroPromo", query = "SELECT p FROM PromocionesDet p WHERE p.promocionesDetPK.nroPromocion = :nroPromo")
//    , @NamedQuery(name = "PromocionesDet.findByCodZona", query = "SELECT p FROM PromocionesDet p WHERE p.promocionesDetPK.codZona = :codZona")    
    , @NamedQuery(name = "PromocionesDet.findByCodMerca", query = "SELECT p FROM PromocionesDet p WHERE p.mercaderia.mercaderiasPK.codMerca = :codMerca")
    , @NamedQuery(name = "PromocionesDet.findByUnidIni", query = "SELECT p FROM PromocionesDet p WHERE p.unidadIni = :unidadIni")
    , @NamedQuery(name = "PromocionesDet.findByUnidFin", query = "SELECT p FROM PromocionesDet p WHERE p.unidadFin = :unidadFin")
    , @NamedQuery(name = "PromocionesDet.findByCajaIni", query = "SELECT p FROM PromocionesDet p WHERE p.cajasIni = :cajasIni")
    , @NamedQuery(name = "PromocionesDet.findByCajaFin", query = "SELECT p FROM PromocionesDet p WHERE p.cajasFin = :cajasFin")
    , @NamedQuery(name = "PromocionesDet.findByKilosIni", query = "SELECT p FROM PromocionesDet p WHERE p.kilosIni = :kilosIni")
    , @NamedQuery(name = "PromocionesDet.findByKilosFin", query = "SELECT p FROM PromocionesDet p WHERE p.kilosFin = :kilosFin")
    , @NamedQuery(name = "PromocionesDet.findByPdesc", query = "SELECT p FROM PromocionesDet p WHERE p.pDesc = :ipresup")
    , @NamedQuery(name = "PromocionesDet.findByPorUnid", query = "SELECT p FROM PromocionesDet p WHERE p.porUnidad = :mexcluyente")
    , @NamedQuery(name = "PromocionesDet.findByPorCaja", query = "SELECT p FROM PromocionesDet p WHERE p.porCaja = :nroPromoGral")
    , @NamedQuery(name = "PromocionesDet.findByEstado", query = "SELECT p FROM PromocionesDet p WHERE p.mestado = :mestado")})
public class PromocionesDet implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PromocionesDetPK promocionesDetPK;
    //@Column(name = "aitem")
    //private Integer aItem;
    @Column(name = "cod_zona")
    private String codZona;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false),
        @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    private Mercaderias mercaderia;
    @Column(name = "nrelacion")
    private Integer nRelacion;
    @Column(name = "kunid_ini")
    private Integer unidadIni;
    @Column(name = "kunid_fin")
    private Integer unidadFin;
    @Column(name = "kcajas_ini")
    private Integer cajasIni;
    @Column(name = "kcajas_fin")
    private Integer cajasFin;
    @Column(name = "kkilos_ini")
    private Double kilosIni;
    @Column(name = "kkilos_fin")
    private Double kilosFin;
    @Column(name = "por_kunidad")
    private Integer porUnidad;
    @Column(name = "por_kcajas")
    private Integer porCaja;
    @Column(name = "pdesc")
    private Double pDesc;
    @Column(name = "mestado")
    private char mestado;
    @Column(name = "mtodos")
    private char mtodos;
    @Column(name = "my")
    private char my;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false),
        @JoinColumn(name = "cod_merca_bonif", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    private Mercaderias mercaderiaBonif;
    @Column(name = "kunid_bonif")
    private Integer unidadBonif;
    @Column(name = "kmax_unid_bonif")
    private Integer maxUnidadBonif;
    @Column(name = "cod_sublinea")
    private Integer codSublinea;
    @ManyToOne
    @JoinColumn(name = "cod_sublinea", insertable = false, updatable = false)
    private Sublineas sublinea;
    @Column(name = "nrela_pack")
    private Integer nRelaPack;
    @Column(name = "ctipo_cliente")
    private char cTipoCliente;
    @Column(name = "ctipo_vta")
    private char cTipoVenta;
    @Transient
    private boolean esNuevo;
    @Transient
    private boolean esModificado;
    @Transient
    private boolean mtodosBool;
    @Transient
    private boolean myBool;
    @Transient
    private boolean mestadoBool;
    
    public PromocionesDet() {}

    public PromocionesDet(PromocionesDetPK promocionesDetPK) {
        this.promocionesDetPK = promocionesDetPK;
    }

    public PromocionesDet(long nroPromo) {
        this.promocionesDetPK = new PromocionesDetPK(nroPromo);
    }

    public PromocionesDetPK getPromocionesDetPK() {
        return promocionesDetPK;
    }

    public void setPromocionesDetPK(PromocionesDetPK promocionesDetPK) {
        this.promocionesDetPK = promocionesDetPK;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }
    
    public Integer getNRelacion() {
        return nRelacion;
    }

    public void setNRelacion(Integer nRelacion) {
        this.nRelacion = nRelacion;
    }

    public Integer getUnidadIni() {
        return unidadIni;
    }

    public void setUnidadIni(Integer unidadIni) {
        this.unidadIni = unidadIni;
    }

    public Integer getUnidadFin() {
        return unidadFin;
    }

    public void setUnidadFin(Integer unidadFin) {
        this.unidadFin = unidadFin;
    }

    public Integer getCajasIni() {
        return cajasIni;
    }

    public void setCajasIni(Integer cajasIni) {
        this.cajasIni = cajasIni;
    }

    public Integer getCajasFin() {
        return cajasFin;
    }

    public void setCajasFin(Integer cajasFin) {
        this.cajasFin = cajasFin;
    }

    public Double getKilosIni() {
        return kilosIni;
    }

    public void setKilosIni(Double kilosIni) {
        this.kilosIni = kilosIni;
    }

    public Double getKilosFin() {
        return kilosFin;
    }

    public void setKilosFin(Double kilosFin) {
        this.kilosFin = kilosFin;
    }

    public Integer getPorUnidad() {
        return porUnidad;
    }

    public void setPorUnidad(Integer porUnidad) {
        this.porUnidad = porUnidad;
    }

    public Integer getPorCaja() {
        return porCaja;
    }

    public void setPorCaja(Integer porCaja) {
        this.porCaja = porCaja;
    }

    public Double getPDesc() {
        return pDesc;
    }

    public void setPDesc(Double pDesc) {
        this.pDesc = pDesc;
    }

    public char getMestado() {
        return mestado;
    }

    public void setMestado(char mestado) {
        this.mestado = mestado;
    }

    public char getMtodos() {
        return mtodos;
    }

    public void setMtodos(char mtodos) {
        this.mtodos = mtodos;
    }

    public Mercaderias getMercaderia() {
        return mercaderia;
    }

    public void setMercaderia(Mercaderias mercaderia) {
        this.mercaderia = mercaderia;
    }

    /*public Integer getAItem() {
        return aItem;
    }*/

    /*public void setAItem(Integer aItem) {
        this.aItem = aItem;
    }*/

    public Mercaderias getMercaderiaBonif() {
        return mercaderiaBonif;
    }

    public void setMercaderiaBonif(Mercaderias mercaderiaBonif) {
        this.mercaderiaBonif = mercaderiaBonif;
    }

    public Integer getUnidadBonif() {
        return unidadBonif;
    }

    public void setUnidadBonif(Integer unidadBonif) {
        this.unidadBonif = unidadBonif;
    }

    public Integer getMaxUnidadBonif() {
        return maxUnidadBonif;
    }

    public void setMaxUnidadBonif(Integer maxUnidadBonif) {
        this.maxUnidadBonif = maxUnidadBonif;
    }

    public boolean isEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(boolean esNuevo) {
        this.esNuevo = esNuevo;
    }

    public boolean isEsModificado() {
        return esModificado;
    }

    public void setEsModificado(boolean esModificado) {
        this.esModificado = esModificado;
    }

    public char getMy() {
        return my;
    }

    public void setMy(char my) {
        this.my = my;
    }

    public Integer getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(Integer codSublinea) {
        this.codSublinea = codSublinea;
    }

    public Integer getNRelaPack() {
        return nRelaPack;
    }

    public void setNRelaPack(Integer nRelaPack) {
        this.nRelaPack = nRelaPack;
    }

    public char getCTipoCliente() {
        return cTipoCliente;
    }

    public void setCTipoCliente(char cTipoCliente) {
        this.cTipoCliente = cTipoCliente;
    }

    public char getCTipoVenta() {
        return cTipoVenta;
    }

    public void setCTipoVenta(char cTipoVenta) {
        this.cTipoVenta = cTipoVenta;
    }

    public boolean isMtodosBool() {
        return mtodosBool;
    }

    public void setMtodosBool(boolean mtodosBool) {
        this.mtodosBool = mtodosBool;
    }

    public boolean isMyBool() {
        return myBool;
    }

    public void setMyBool(boolean myBool) {
        this.myBool = myBool;
    }

    public boolean isMestadoBool() {
        return mestadoBool;
    }

    public void setMestadoBool(boolean mestadoBool) {
        this.mestadoBool = mestadoBool;
    }
    
    public Sublineas getSublinea() {
        return sublinea;
    }

    public void setSublinea(Sublineas sublinea) {
        this.sublinea = sublinea;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PromocionesDet)) {
            return false;
        }
        PromocionesDet other = (PromocionesDet) object;
        if ((this.promocionesDetPK == null && other.promocionesDetPK != null) || (this.promocionesDetPK != null && !this.promocionesDetPK.equals(other.promocionesDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PromocionesDet[ promocionesDetPK=" + promocionesDetPK + " ]";
    }
    
}
