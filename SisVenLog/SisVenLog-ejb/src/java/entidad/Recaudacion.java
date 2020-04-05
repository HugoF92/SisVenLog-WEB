/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "recaudacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recaudacion.findAll", query = "SELECT r FROM Recaudacion r")
    , @NamedQuery(name = "Recaudacion.findByCodEmpr", query = "SELECT r FROM Recaudacion r WHERE r.recaudacionPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Recaudacion.findByNroPlanilla", query = "SELECT r FROM Recaudacion r WHERE r.recaudacionPK.nroPlanilla = :nroPlanilla")
    , @NamedQuery(name = "Recaudacion.findByFplanilla", query = "SELECT r FROM Recaudacion r WHERE r.fplanilla = :fplanilla")
    , @NamedQuery(name = "Recaudacion.findByCodEntregador", query = "SELECT r FROM Recaudacion r WHERE r.codEntregador = :codEntregador")
    , @NamedQuery(name = "Recaudacion.findByTventas", query = "SELECT r FROM Recaudacion r WHERE r.tventas = :tventas")
    , @NamedQuery(name = "Recaudacion.findByTnotasDev", query = "SELECT r FROM Recaudacion r WHERE r.tnotasDev = :tnotasDev")
    , @NamedQuery(name = "Recaudacion.findByTchequesDia", query = "SELECT r FROM Recaudacion r WHERE r.tchequesDia = :tchequesDia")
    , @NamedQuery(name = "Recaudacion.findByKcheques", query = "SELECT r FROM Recaudacion r WHERE r.kcheques = :kcheques")
    , @NamedQuery(name = "Recaudacion.findByNroBoleta", query = "SELECT r FROM Recaudacion r WHERE r.nroBoleta = :nroBoleta")
    , @NamedQuery(name = "Recaudacion.findByCodBanco", query = "SELECT r FROM Recaudacion r WHERE r.codBanco = :codBanco")
    , @NamedQuery(name = "Recaudacion.findByTefectivo", query = "SELECT r FROM Recaudacion r WHERE r.tefectivo = :tefectivo")
    , @NamedQuery(name = "Recaudacion.findByTmonedas", query = "SELECT r FROM Recaudacion r WHERE r.tmonedas = :tmonedas")
    , @NamedQuery(name = "Recaudacion.findByTcreditos", query = "SELECT r FROM Recaudacion r WHERE r.tcreditos = :tcreditos")
    , @NamedQuery(name = "Recaudacion.findByTgastos", query = "SELECT r FROM Recaudacion r WHERE r.tgastos = :tgastos")
    , @NamedQuery(name = "Recaudacion.findByTnotasOtras", query = "SELECT r FROM Recaudacion r WHERE r.tnotasOtras = :tnotasOtras")
    , @NamedQuery(name = "Recaudacion.findByTdiferencia", query = "SELECT r FROM Recaudacion r WHERE r.tdiferencia = :tdiferencia")
    , @NamedQuery(name = "Recaudacion.findByFalta", query = "SELECT r FROM Recaudacion r WHERE r.falta = :falta")
    , @NamedQuery(name = "Recaudacion.findByCusuario", query = "SELECT r FROM Recaudacion r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "Recaudacion.findByFultimModif", query = "SELECT r FROM Recaudacion r WHERE r.fultimModif = :fultimModif")
    , @NamedQuery(name = "Recaudacion.findByCusuarioModif", query = "SELECT r FROM Recaudacion r WHERE r.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Recaudacion.findByTchequesDif", query = "SELECT r FROM Recaudacion r WHERE r.tchequesDif = :tchequesDif")
    , @NamedQuery(name = "Recaudacion.findByCodZona", query = "SELECT r FROM Recaudacion r WHERE r.codZona = :codZona")
    , @NamedQuery(name = "Recaudacion.findByNplanillaCob", query = "SELECT r FROM Recaudacion r WHERE r.nplanillaCob = :nplanillaCob")
    , @NamedQuery(name = "Recaudacion.findByTpagares", query = "SELECT r FROM Recaudacion r WHERE r.tpagares = :tpagares")
    , @NamedQuery(name = "Recaudacion.findByTdepositos", query = "SELECT r FROM Recaudacion r WHERE r.tdepositos = :tdepositos")
    , @NamedQuery(name = "Recaudacion.findByTnotasAtras", query = "SELECT r FROM Recaudacion r WHERE r.tnotasAtras = :tnotasAtras")})
public class Recaudacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecaudacionPK recaudacionPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fplanilla")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fplanilla;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "cod_entregador")
//    private short codEntregador;
//    @JoinColumn(name = "cod_entregador", referencedColumnName = "cod_empleado", insertable = false, updatable = false)
    @JoinColumns({
        @JoinColumn(name = "cod_entregador", referencedColumnName = "cod_empleado"),
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr" , insertable = false , updatable = false)    
    })
    @ManyToOne(optional = false)
    @NotNull
    private Empleados codEntregador;
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
    @Column(name = "tcheques_dia")
    private long tchequesDia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kcheques")
    private long kcheques;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_boleta")
    private long nroBoleta;
    @Column(name = "cod_banco")
    private Short codBanco;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tefectivo")
    private long tefectivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tmonedas")
    private long tmonedas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tcreditos")
    private long tcreditos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tgastos")
    private long tgastos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tnotas_otras")
    private long tnotasOtras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tdiferencia")
    private long tdiferencia;
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
    @Column(name = "tcheques_dif")
    private long tchequesDif;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_zona")
    private String codZona;
    @Column(name = "nplanilla_cob")
    private Long nplanillaCob;
    @Column(name = "tpagares")
    private Long tpagares;
    @Column(name = "tdepositos")
    private Long tdepositos;
    @Column(name = "tnotas_atras")
    private Long tnotasAtras;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recaudacion")
    private Collection<RecaudacionDet> recaudacionDetCollection;

    public Recaudacion() {
    }

    public Recaudacion(RecaudacionPK recaudacionPK) {
        this.recaudacionPK = recaudacionPK;
    }

    public Recaudacion(RecaudacionPK recaudacionPK, Date fplanilla, Empleados codEntregador, long tventas, long tnotasDev, long tchequesDia, long kcheques, long nroBoleta, long tefectivo, long tmonedas, long tcreditos, long tgastos, long tnotasOtras, long tdiferencia, long tchequesDif, String codZona) {
        this.recaudacionPK = recaudacionPK;
        this.fplanilla = fplanilla;
        this.codEntregador = codEntregador;
        this.tventas = tventas;
        this.tnotasDev = tnotasDev;
        this.tchequesDia = tchequesDia;
        this.kcheques = kcheques;
        this.nroBoleta = nroBoleta;
        this.tefectivo = tefectivo;
        this.tmonedas = tmonedas;
        this.tcreditos = tcreditos;
        this.tgastos = tgastos;
        this.tnotasOtras = tnotasOtras;
        this.tdiferencia = tdiferencia;
        this.tchequesDif = tchequesDif;
        this.codZona = codZona;
    }

    public Recaudacion(short codEmpr, long nroPlanilla) {
        this.recaudacionPK = new RecaudacionPK(codEmpr, nroPlanilla);
    }

    public RecaudacionPK getRecaudacionPK() {
        return recaudacionPK;
    }

    public void setRecaudacionPK(RecaudacionPK recaudacionPK) {
        this.recaudacionPK = recaudacionPK;
    }

    public Date getFplanilla() {
        return fplanilla;
    }

    public void setFplanilla(Date fplanilla) {
        this.fplanilla = fplanilla;
    }

    public Empleados getCodEntregador() {
        return codEntregador;
    }

    public void setCodEntregador(Empleados codEntregador) {
        this.codEntregador = codEntregador;
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

    public long getTchequesDia() {
        return tchequesDia;
    }

    public void setTchequesDia(long tchequesDia) {
        this.tchequesDia = tchequesDia;
    }

    public long getKcheques() {
        return kcheques;
    }

    public void setKcheques(long kcheques) {
        this.kcheques = kcheques;
    }

    public long getNroBoleta() {
        return nroBoleta;
    }

    public void setNroBoleta(long nroBoleta) {
        this.nroBoleta = nroBoleta;
    }

    public Short getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(Short codBanco) {
        this.codBanco = codBanco;
    }

    public long getTefectivo() {
        return tefectivo;
    }

    public void setTefectivo(long tefectivo) {
        this.tefectivo = tefectivo;
    }

    public long getTmonedas() {
        return tmonedas;
    }

    public void setTmonedas(long tmonedas) {
        this.tmonedas = tmonedas;
    }

    public long getTcreditos() {
        return tcreditos;
    }

    public void setTcreditos(long tcreditos) {
        this.tcreditos = tcreditos;
    }

    public long getTgastos() {
        return tgastos;
    }

    public void setTgastos(long tgastos) {
        this.tgastos = tgastos;
    }

    public long getTnotasOtras() {
        return tnotasOtras;
    }

    public void setTnotasOtras(long tnotasOtras) {
        this.tnotasOtras = tnotasOtras;
    }

    public long getTdiferencia() {
        return tdiferencia;
    }

    public void setTdiferencia(long tdiferencia) {
        this.tdiferencia = tdiferencia;
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

    public long getTchequesDif() {
        return tchequesDif;
    }

    public void setTchequesDif(long tchequesDif) {
        this.tchequesDif = tchequesDif;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public Long getNplanillaCob() {
        return nplanillaCob;
    }

    public void setNplanillaCob(Long nplanillaCob) {
        this.nplanillaCob = nplanillaCob;
    }

    public Long getTpagares() {
        return tpagares;
    }

    public void setTpagares(Long tpagares) {
        this.tpagares = tpagares;
    }

    public Long getTdepositos() {
        return tdepositos;
    }

    public void setTdepositos(Long tdepositos) {
        this.tdepositos = tdepositos;
    }

    public Long getTnotasAtras() {
        return tnotasAtras;
    }

    public void setTnotasAtras(Long tnotasAtras) {
        this.tnotasAtras = tnotasAtras;
    }

    @XmlTransient
    public Collection<RecaudacionDet> getRecaudacionDetCollection() {
        return recaudacionDetCollection;
    }

    public void setRecaudacionDetCollection(Collection<RecaudacionDet> recaudacionDetCollection) {
        this.recaudacionDetCollection = recaudacionDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recaudacionPK != null ? recaudacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recaudacion)) {
            return false;
        }
        Recaudacion other = (Recaudacion) object;
        if ((this.recaudacionPK == null && other.recaudacionPK != null) || (this.recaudacionPK != null && !this.recaudacionPK.equals(other.recaudacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Recaudacion[ recaudacionPK=" + recaudacionPK + " ]";
    }
    
}
