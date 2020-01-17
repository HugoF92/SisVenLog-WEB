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
 * @author Administrador
 */
@Entity
@Table(name = "promociones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Promociones.findAll", query = "SELECT p FROM Promociones p")
    , @NamedQuery(name = "Promociones.findByCodEmpr", query = "SELECT p FROM Promociones p WHERE p.promocionesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Promociones.findByNroPromo", query = "SELECT p FROM Promociones p WHERE p.promocionesPK.nroPromo = :nroPromo")
    , @NamedQuery(name = "Promociones.findByXdesc", query = "SELECT p FROM Promociones p WHERE p.xdesc = :xdesc")
    , @NamedQuery(name = "Promociones.findByFrigeDesde", query = "SELECT p FROM Promociones p WHERE p.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "Promociones.findByFrigeHasta", query = "SELECT p FROM Promociones p WHERE p.frigeHasta = :frigeHasta")
    , @NamedQuery(name = "Promociones.findByMtipo", query = "SELECT p FROM Promociones p WHERE p.mtipo = :mtipo")
    , @NamedQuery(name = "Promociones.findByFalta", query = "SELECT p FROM Promociones p WHERE p.falta = :falta")
    , @NamedQuery(name = "Promociones.findByCusuario", query = "SELECT p FROM Promociones p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "Promociones.findByFultimModif", query = "SELECT p FROM Promociones p WHERE p.fultimModif = :fultimModif")
    , @NamedQuery(name = "Promociones.findByCusuarioModif", query = "SELECT p FROM Promociones p WHERE p.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Promociones.findByIpresup", query = "SELECT p FROM Promociones p WHERE p.ipresup = :ipresup")
    , @NamedQuery(name = "Promociones.findByMexcluyente", query = "SELECT p FROM Promociones p WHERE p.mexcluyente = :mexcluyente")
    , @NamedQuery(name = "Promociones.findByNroPromoGral", query = "SELECT p FROM Promociones p WHERE p.nroPromoGral = :nroPromoGral")
    , @NamedQuery(name = "Promociones.findByXobs", query = "SELECT p FROM Promociones p WHERE p.xobs = :xobs")
    , @NamedQuery(name = "Promociones.findByMvalidar", query = "SELECT p FROM Promociones p WHERE p.mvalidar = :mvalidar")
    , @NamedQuery(name = "Promociones.findByMafectap", query = "SELECT p FROM Promociones p WHERE p.mafectap = :mafectap")
    , @NamedQuery(name = "Promociones.findByXdescGral", query = "SELECT p FROM Promociones p WHERE p.xdescGral = :xdescGral")
    , @NamedQuery(name = "Promociones.findByIpresupOrig", query = "SELECT p FROM Promociones p WHERE p.ipresupOrig = :ipresupOrig")
    , @NamedQuery(name = "Promociones.findByPmargen", query = "SELECT p FROM Promociones p WHERE p.pmargen = :pmargen")
    , @NamedQuery(name = "Promociones.findByCcategCliente", query = "SELECT p FROM Promociones p WHERE p.ccategCliente = :ccategCliente")
    , @NamedQuery(name = "Promociones.findByMcalculo", query = "SELECT p FROM Promociones p WHERE p.mcalculo = :mcalculo")})
public class Promociones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PromocionesPK promocionesPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeDesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;
    @Column(name = "mtipo")
    private Character mtipo;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipresup")
    private long ipresup;
    @Column(name = "mexcluyente")
    private Character mexcluyente;
    @Column(name = "nro_promo_gral")
    private Long nroPromoGral;
    @Size(max = 250)
    @Column(name = "xobs")
    private String xobs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mvalidar")
    private Character mvalidar;
    @Column(name = "mafectap")
    private Character mafectap;
    @Size(max = 30)
    @Column(name = "xdesc_gral")
    private String xdescGral;
    @Column(name = "ipresup_orig")
    private Long ipresupOrig;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pmargen")
    private BigDecimal pmargen;
    @Size(max = 2)
    @Column(name = "ccateg_cliente")
    private String ccategCliente;
    @Column(name = "mcalculo")
    private Character mcalculo;

    public Promociones() {
    }

    public Promociones(PromocionesPK promocionesPK) {
        this.promocionesPK = promocionesPK;
    }

    public Promociones(PromocionesPK promocionesPK, Date frigeDesde, Date frigeHasta, long ipresup, Character mvalidar, BigDecimal pmargen) {
        this.promocionesPK = promocionesPK;
        this.frigeDesde = frigeDesde;
        this.frigeHasta = frigeHasta;
        this.ipresup = ipresup;
        this.mvalidar = mvalidar;
        this.pmargen = pmargen;
    }

    public Promociones(short codEmpr, long nroPromo) {
        this.promocionesPK = new PromocionesPK(codEmpr, nroPromo);
    }

    public PromocionesPK getPromocionesPK() {
        return promocionesPK;
    }

    public void setPromocionesPK(PromocionesPK promocionesPK) {
        this.promocionesPK = promocionesPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Date getFrigeDesde() {
        return frigeDesde;
    }

    public void setFrigeDesde(Date frigeDesde) {
        this.frigeDesde = frigeDesde;
    }

    public Date getFrigeHasta() {
        return frigeHasta;
    }

    public void setFrigeHasta(Date frigeHasta) {
        this.frigeHasta = frigeHasta;
    }

    public Character getMtipo() {
        return mtipo;
    }

    public void setMtipo(Character mtipo) {
        this.mtipo = mtipo;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
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

    public long getIpresup() {
        return ipresup;
    }

    public void setIpresup(long ipresup) {
        this.ipresup = ipresup;
    }

    public Character getMexcluyente() {
        return mexcluyente;
    }

    public void setMexcluyente(Character mexcluyente) {
        this.mexcluyente = mexcluyente;
    }

    public Long getNroPromoGral() {
        return nroPromoGral;
    }

    public void setNroPromoGral(Long nroPromoGral) {
        this.nroPromoGral = nroPromoGral;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
    }

    public Character getMvalidar() {
        return mvalidar;
    }

    public void setMvalidar(Character mvalidar) {
        this.mvalidar = mvalidar;
    }

    public Character getMafectap() {
        return mafectap;
    }

    public void setMafectap(Character mafectap) {
        this.mafectap = mafectap;
    }

    public String getXdescGral() {
        return xdescGral;
    }

    public void setXdescGral(String xdescGral) {
        this.xdescGral = xdescGral;
    }

    public Long getIpresupOrig() {
        return ipresupOrig;
    }

    public void setIpresupOrig(Long ipresupOrig) {
        this.ipresupOrig = ipresupOrig;
    }

    public BigDecimal getPmargen() {
        return pmargen;
    }

    public void setPmargen(BigDecimal pmargen) {
        this.pmargen = pmargen;
    }

    public String getCcategCliente() {
        return ccategCliente;
    }

    public void setCcategCliente(String ccategCliente) {
        this.ccategCliente = ccategCliente;
    }

    public Character getMcalculo() {
        return mcalculo;
    }

    public void setMcalculo(Character mcalculo) {
        this.mcalculo = mcalculo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promocionesPK != null ? promocionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Promociones)) {
            return false;
        }
        Promociones other = (Promociones) object;
        if ((this.promocionesPK == null && other.promocionesPK != null) || (this.promocionesPK != null && !this.promocionesPK.equals(other.promocionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Promociones[ promocionesPK=" + promocionesPK + " ]";
    }
    
}
