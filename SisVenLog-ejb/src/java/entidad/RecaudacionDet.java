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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "recaudacion_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecaudacionDet.findAll", query = "SELECT r FROM RecaudacionDet r")
    , @NamedQuery(name = "RecaudacionDet.findByCodEmpr", query = "SELECT r FROM RecaudacionDet r WHERE r.recaudacionDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RecaudacionDet.findByNroPlanilla", query = "SELECT r FROM RecaudacionDet r WHERE r.recaudacionDetPK.nroPlanilla = :nroPlanilla")
    , @NamedQuery(name = "RecaudacionDet.findByCodZona", query = "SELECT r FROM RecaudacionDet r WHERE r.recaudacionDetPK.codZona = :codZona")
    , @NamedQuery(name = "RecaudacionDet.findByTventas", query = "SELECT r FROM RecaudacionDet r WHERE r.tventas = :tventas")
    , @NamedQuery(name = "RecaudacionDet.findByTnotasDev", query = "SELECT r FROM RecaudacionDet r WHERE r.tnotasDev = :tnotasDev")
    , @NamedQuery(name = "RecaudacionDet.findByTcreditos", query = "SELECT r FROM RecaudacionDet r WHERE r.tcreditos = :tcreditos")
    , @NamedQuery(name = "RecaudacionDet.findByTnotasOtras", query = "SELECT r FROM RecaudacionDet r WHERE r.tnotasOtras = :tnotasOtras")
    , @NamedQuery(name = "RecaudacionDet.findByTnotasAtras", query = "SELECT r FROM RecaudacionDet r WHERE r.tnotasAtras = :tnotasAtras")
    , @NamedQuery(name = "RecaudacionDet.findByFalta", query = "SELECT r FROM RecaudacionDet r WHERE r.falta = :falta")
    , @NamedQuery(name = "RecaudacionDet.findByCusuario", query = "SELECT r FROM RecaudacionDet r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "RecaudacionDet.findByFultimModif", query = "SELECT r FROM RecaudacionDet r WHERE r.fultimModif = :fultimModif")
    , @NamedQuery(name = "RecaudacionDet.findByCusuarioModif", query = "SELECT r FROM RecaudacionDet r WHERE r.cusuarioModif = :cusuarioModif")})
public class RecaudacionDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecaudacionDetPK recaudacionDetPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tventas")
    private long tventas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tnotas_dev")
    private long tnotasDev;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tcreditos")
    private long tcreditos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tnotas_otras")
    private long tnotasOtras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tnotas_atras")
    private long tnotasAtras;
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
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_planilla", referencedColumnName = "nro_planilla", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Recaudacion recaudacion;

    public RecaudacionDet() {
    }

    public RecaudacionDet(RecaudacionDetPK recaudacionDetPK) {
        this.recaudacionDetPK = recaudacionDetPK;
    }

    public RecaudacionDet(RecaudacionDetPK recaudacionDetPK, long tventas, long tnotasDev, long tcreditos, long tnotasOtras, long tnotasAtras) {
        this.recaudacionDetPK = recaudacionDetPK;
        this.tventas = tventas;
        this.tnotasDev = tnotasDev;
        this.tcreditos = tcreditos;
        this.tnotasOtras = tnotasOtras;
        this.tnotasAtras = tnotasAtras;
    }

    public RecaudacionDet(short codEmpr, long nroPlanilla, String codZona) {
        this.recaudacionDetPK = new RecaudacionDetPK(codEmpr, nroPlanilla, codZona);
    }

    public RecaudacionDetPK getRecaudacionDetPK() {
        return recaudacionDetPK;
    }

    public void setRecaudacionDetPK(RecaudacionDetPK recaudacionDetPK) {
        this.recaudacionDetPK = recaudacionDetPK;
    }

    public long getTventas() {
        return tventas;
    }

    public void setTventas(long tventas) {
        this.tventas = tventas;
    }

    public long getTnotasDev() {
        return tnotasDev;
    }

    public void setTnotasDev(long tnotasDev) {
        this.tnotasDev = tnotasDev;
    }

    public long getTcreditos() {
        return tcreditos;
    }

    public void setTcreditos(long tcreditos) {
        this.tcreditos = tcreditos;
    }

    public long getTnotasOtras() {
        return tnotasOtras;
    }

    public void setTnotasOtras(long tnotasOtras) {
        this.tnotasOtras = tnotasOtras;
    }

    public long getTnotasAtras() {
        return tnotasAtras;
    }

    public void setTnotasAtras(long tnotasAtras) {
        this.tnotasAtras = tnotasAtras;
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

    public Recaudacion getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(Recaudacion recaudacion) {
        this.recaudacion = recaudacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recaudacionDetPK != null ? recaudacionDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecaudacionDet)) {
            return false;
        }
        RecaudacionDet other = (RecaudacionDet) object;
        if ((this.recaudacionDetPK == null && other.recaudacionDetPK != null) || (this.recaudacionDetPK != null && !this.recaudacionDetPK.equals(other.recaudacionDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RecaudacionDet[ recaudacionDetPK=" + recaudacionDetPK + " ]";
    }
    
}
