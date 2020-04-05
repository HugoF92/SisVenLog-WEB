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
@Table(name = "metas_vendedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetasVendedores.findAll", query = "SELECT m FROM MetasVendedores m")
    , @NamedQuery(name = "MetasVendedores.findByCodEmpleado", query = "SELECT m FROM MetasVendedores m WHERE m.metasVendedoresPK.codEmpleado = :codEmpleado")
    , @NamedQuery(name = "MetasVendedores.findByCodEmpr", query = "SELECT m FROM MetasVendedores m WHERE m.metasVendedoresPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "MetasVendedores.findByCodZona", query = "SELECT m FROM MetasVendedores m WHERE m.metasVendedoresPK.codZona = :codZona")
    , @NamedQuery(name = "MetasVendedores.findByCodSublinea", query = "SELECT m FROM MetasVendedores m WHERE m.metasVendedoresPK.codSublinea = :codSublinea")
    , @NamedQuery(name = "MetasVendedores.findByNcajas", query = "SELECT m FROM MetasVendedores m WHERE m.ncajas = :ncajas")
    , @NamedQuery(name = "MetasVendedores.findByNunidad", query = "SELECT m FROM MetasVendedores m WHERE m.nunidad = :nunidad")
    , @NamedQuery(name = "MetasVendedores.findByNtotPeso", query = "SELECT m FROM MetasVendedores m WHERE m.ntotPeso = :ntotPeso")
    , @NamedQuery(name = "MetasVendedores.findByItotalVta", query = "SELECT m FROM MetasVendedores m WHERE m.itotalVta = :itotalVta")
    , @NamedQuery(name = "MetasVendedores.findByFrigeDesde", query = "SELECT m FROM MetasVendedores m WHERE m.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "MetasVendedores.findByFrigeHasta", query = "SELECT m FROM MetasVendedores m WHERE m.frigeHasta = :frigeHasta")
    , @NamedQuery(name = "MetasVendedores.findByNmes", query = "SELECT m FROM MetasVendedores m WHERE m.metasVendedoresPK.nmes = :nmes")
    , @NamedQuery(name = "MetasVendedores.findByNanno", query = "SELECT m FROM MetasVendedores m WHERE m.metasVendedoresPK.nanno = :nanno")
    , @NamedQuery(name = "MetasVendedores.findByMestado", query = "SELECT m FROM MetasVendedores m WHERE m.mestado = :mestado")
    , @NamedQuery(name = "MetasVendedores.findByPesototlinea", query = "SELECT m FROM MetasVendedores m WHERE m.pesototlinea = :pesototlinea")
    , @NamedQuery(name = "MetasVendedores.findByGstotlinea", query = "SELECT m FROM MetasVendedores m WHERE m.gstotlinea = :gstotlinea")
    , @NamedQuery(name = "MetasVendedores.findByPesototsub", query = "SELECT m FROM MetasVendedores m WHERE m.pesototsub = :pesototsub")
    , @NamedQuery(name = "MetasVendedores.findByGstotsub", query = "SELECT m FROM MetasVendedores m WHERE m.gstotsub = :gstotsub")
    , @NamedQuery(name = "MetasVendedores.findByPpartisub", query = "SELECT m FROM MetasVendedores m WHERE m.ppartisub = :ppartisub")
    , @NamedQuery(name = "MetasVendedores.findByPpartisubGs", query = "SELECT m FROM MetasVendedores m WHERE m.ppartisubGs = :ppartisubGs")
    , @NamedQuery(name = "MetasVendedores.findByPesosubMeta", query = "SELECT m FROM MetasVendedores m WHERE m.pesosubMeta = :pesosubMeta")
    , @NamedQuery(name = "MetasVendedores.findByGssubMeta", query = "SELECT m FROM MetasVendedores m WHERE m.gssubMeta = :gssubMeta")
    , @NamedQuery(name = "MetasVendedores.findByPpartidet", query = "SELECT m FROM MetasVendedores m WHERE m.ppartidet = :ppartidet")
    , @NamedQuery(name = "MetasVendedores.findByPpartidetGs", query = "SELECT m FROM MetasVendedores m WHERE m.ppartidetGs = :ppartidetGs")
    , @NamedQuery(name = "MetasVendedores.findByPesoMeta", query = "SELECT m FROM MetasVendedores m WHERE m.pesoMeta = :pesoMeta")
    , @NamedQuery(name = "MetasVendedores.findByCajasMeta", query = "SELECT m FROM MetasVendedores m WHERE m.cajasMeta = :cajasMeta")
    , @NamedQuery(name = "MetasVendedores.findByGsMeta", query = "SELECT m FROM MetasVendedores m WHERE m.gsMeta = :gsMeta")
    , @NamedQuery(name = "MetasVendedores.findByPesoMetaMod", query = "SELECT m FROM MetasVendedores m WHERE m.pesoMetaMod = :pesoMetaMod")
    , @NamedQuery(name = "MetasVendedores.findByCajasMetaMod", query = "SELECT m FROM MetasVendedores m WHERE m.cajasMetaMod = :cajasMetaMod")
    , @NamedQuery(name = "MetasVendedores.findByGsMetaMod", query = "SELECT m FROM MetasVendedores m WHERE m.gsMetaMod = :gsMetaMod")
    , @NamedQuery(name = "MetasVendedores.findByFalta", query = "SELECT m FROM MetasVendedores m WHERE m.falta = :falta")
    , @NamedQuery(name = "MetasVendedores.findByCusuario", query = "SELECT m FROM MetasVendedores m WHERE m.cusuario = :cusuario")
    , @NamedQuery(name = "MetasVendedores.findByFultimModif", query = "SELECT m FROM MetasVendedores m WHERE m.fultimModif = :fultimModif")
    , @NamedQuery(name = "MetasVendedores.findByCusuarioModif", query = "SELECT m FROM MetasVendedores m WHERE m.cusuarioModif = :cusuarioModif")})
public class MetasVendedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MetasVendedoresPK metasVendedoresPK;
    @Column(name = "ncajas")
    private Integer ncajas;
    @Column(name = "nunidad")
    private Integer nunidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ntot_peso")
    private BigDecimal ntotPeso;
    @Column(name = "itotal_vta")
    private Long itotalVta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeDesde;
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @Column(name = "pesototlinea")
    private BigDecimal pesototlinea;
    @Column(name = "gstotlinea")
    private Long gstotlinea;
    @Column(name = "pesototsub")
    private BigDecimal pesototsub;
    @Column(name = "gstotsub")
    private Long gstotsub;
    @Column(name = "ppartisub")
    private BigDecimal ppartisub;
    @Column(name = "ppartisub_gs")
    private BigDecimal ppartisubGs;
    @Column(name = "pesosub_meta")
    private BigDecimal pesosubMeta;
    @Column(name = "gssub_meta")
    private Long gssubMeta;
    @Column(name = "ppartidet")
    private BigDecimal ppartidet;
    @Column(name = "ppartidet_gs")
    private BigDecimal ppartidetGs;
    @Column(name = "peso_meta")
    private BigDecimal pesoMeta;
    @Column(name = "cajas_meta")
    private Integer cajasMeta;
    @Column(name = "gs_meta")
    private BigDecimal gsMeta;
    @Column(name = "peso_meta_mod")
    private Long pesoMetaMod;
    @Column(name = "cajas_meta_mod")
    private Integer cajasMetaMod;
    @Column(name = "gs_meta_mod")
    private Long gsMetaMod;
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
        , @JoinColumn(name = "cod_empleado", referencedColumnName = "cod_empleado", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Empleados empleados;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_empleado", referencedColumnName = "cod_empleado", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private EmpleadosZonas empleadosZonas;
    @JoinColumn(name = "cod_linea", referencedColumnName = "cod_linea")
    @ManyToOne
    private Lineas codLinea;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Zonas zonas;

    public MetasVendedores() {
    }

    public MetasVendedores(MetasVendedoresPK metasVendedoresPK) {
        this.metasVendedoresPK = metasVendedoresPK;
    }

    public MetasVendedores(MetasVendedoresPK metasVendedoresPK, Date frigeDesde, Character mestado) {
        this.metasVendedoresPK = metasVendedoresPK;
        this.frigeDesde = frigeDesde;
        this.mestado = mestado;
    }

    public MetasVendedores(short codEmpleado, short codEmpr, String codZona, short codSublinea, short nmes, short nanno) {
        this.metasVendedoresPK = new MetasVendedoresPK(codEmpleado, codEmpr, codZona, codSublinea, nmes, nanno);
    }

    public MetasVendedoresPK getMetasVendedoresPK() {
        return metasVendedoresPK;
    }

    public void setMetasVendedoresPK(MetasVendedoresPK metasVendedoresPK) {
        this.metasVendedoresPK = metasVendedoresPK;
    }

    public Integer getNcajas() {
        return ncajas;
    }

    public void setNcajas(Integer ncajas) {
        this.ncajas = ncajas;
    }

    public Integer getNunidad() {
        return nunidad;
    }

    public void setNunidad(Integer nunidad) {
        this.nunidad = nunidad;
    }

    public BigDecimal getNtotPeso() {
        return ntotPeso;
    }

    public void setNtotPeso(BigDecimal ntotPeso) {
        this.ntotPeso = ntotPeso;
    }

    public Long getItotalVta() {
        return itotalVta;
    }

    public void setItotalVta(Long itotalVta) {
        this.itotalVta = itotalVta;
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

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public BigDecimal getPesototlinea() {
        return pesototlinea;
    }

    public void setPesototlinea(BigDecimal pesototlinea) {
        this.pesototlinea = pesototlinea;
    }

    public Long getGstotlinea() {
        return gstotlinea;
    }

    public void setGstotlinea(Long gstotlinea) {
        this.gstotlinea = gstotlinea;
    }

    public BigDecimal getPesototsub() {
        return pesototsub;
    }

    public void setPesototsub(BigDecimal pesototsub) {
        this.pesototsub = pesototsub;
    }

    public Long getGstotsub() {
        return gstotsub;
    }

    public void setGstotsub(Long gstotsub) {
        this.gstotsub = gstotsub;
    }

    public BigDecimal getPpartisub() {
        return ppartisub;
    }

    public void setPpartisub(BigDecimal ppartisub) {
        this.ppartisub = ppartisub;
    }

    public BigDecimal getPpartisubGs() {
        return ppartisubGs;
    }

    public void setPpartisubGs(BigDecimal ppartisubGs) {
        this.ppartisubGs = ppartisubGs;
    }

    public BigDecimal getPesosubMeta() {
        return pesosubMeta;
    }

    public void setPesosubMeta(BigDecimal pesosubMeta) {
        this.pesosubMeta = pesosubMeta;
    }

    public Long getGssubMeta() {
        return gssubMeta;
    }

    public void setGssubMeta(Long gssubMeta) {
        this.gssubMeta = gssubMeta;
    }

    public BigDecimal getPpartidet() {
        return ppartidet;
    }

    public void setPpartidet(BigDecimal ppartidet) {
        this.ppartidet = ppartidet;
    }

    public BigDecimal getPpartidetGs() {
        return ppartidetGs;
    }

    public void setPpartidetGs(BigDecimal ppartidetGs) {
        this.ppartidetGs = ppartidetGs;
    }

    public BigDecimal getPesoMeta() {
        return pesoMeta;
    }

    public void setPesoMeta(BigDecimal pesoMeta) {
        this.pesoMeta = pesoMeta;
    }

    public Integer getCajasMeta() {
        return cajasMeta;
    }

    public void setCajasMeta(Integer cajasMeta) {
        this.cajasMeta = cajasMeta;
    }

    public BigDecimal getGsMeta() {
        return gsMeta;
    }

    public void setGsMeta(BigDecimal gsMeta) {
        this.gsMeta = gsMeta;
    }

    public Long getPesoMetaMod() {
        return pesoMetaMod;
    }

    public void setPesoMetaMod(Long pesoMetaMod) {
        this.pesoMetaMod = pesoMetaMod;
    }

    public Integer getCajasMetaMod() {
        return cajasMetaMod;
    }

    public void setCajasMetaMod(Integer cajasMetaMod) {
        this.cajasMetaMod = cajasMetaMod;
    }

    public Long getGsMetaMod() {
        return gsMetaMod;
    }

    public void setGsMetaMod(Long gsMetaMod) {
        this.gsMetaMod = gsMetaMod;
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

    public Empleados getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
    }

    public EmpleadosZonas getEmpleadosZonas() {
        return empleadosZonas;
    }

    public void setEmpleadosZonas(EmpleadosZonas empleadosZonas) {
        this.empleadosZonas = empleadosZonas;
    }

    public Lineas getCodLinea() {
        return codLinea;
    }

    public void setCodLinea(Lineas codLinea) {
        this.codLinea = codLinea;
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
        hash += (metasVendedoresPK != null ? metasVendedoresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetasVendedores)) {
            return false;
        }
        MetasVendedores other = (MetasVendedores) object;
        if ((this.metasVendedoresPK == null && other.metasVendedoresPK != null) || (this.metasVendedoresPK != null && !this.metasVendedoresPK.equals(other.metasVendedoresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MetasVendedores[ metasVendedoresPK=" + metasVendedoresPK + " ]";
    }
    
}
