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
 * @author Administrador
 */
@Entity
@Table(name = "mercaderias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mercaderias.findAll", query = "SELECT m FROM Mercaderias m")
    , @NamedQuery(name = "Mercaderias.findByCodEmpr", query = "SELECT m FROM Mercaderias m WHERE m.mercaderiasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Mercaderias.findByCodMerca", query = "SELECT m FROM Mercaderias m WHERE m.mercaderiasPK.codMerca = :codMerca")
    , @NamedQuery(name = "Mercaderias.findByXdesc", query = "SELECT m FROM Mercaderias m WHERE m.xdesc = :xdesc")
    , @NamedQuery(name = "Mercaderias.findByCodBarra", query = "SELECT m FROM Mercaderias m WHERE m.codBarra = :codBarra")
    , @NamedQuery(name = "Mercaderias.findByCodAnterior", query = "SELECT m FROM Mercaderias m WHERE m.codAnterior = :codAnterior")
    , @NamedQuery(name = "Mercaderias.findByXfoto", query = "SELECT m FROM Mercaderias m WHERE m.xfoto = :xfoto")
    , @NamedQuery(name = "Mercaderias.findByNprecioPpp", query = "SELECT m FROM Mercaderias m WHERE m.nprecioPpp = :nprecioPpp")
    , @NamedQuery(name = "Mercaderias.findByMgravExe", query = "SELECT m FROM Mercaderias m WHERE m.mgravExe = :mgravExe")
    , @NamedQuery(name = "Mercaderias.findByNrelaProvee", query = "SELECT m FROM Mercaderias m WHERE m.nrelaProvee = :nrelaProvee")
    , @NamedQuery(name = "Mercaderias.findByNrelacion", query = "SELECT m FROM Mercaderias m WHERE m.nrelacion = :nrelacion")
    , @NamedQuery(name = "Mercaderias.findByNpesoUnidad", query = "SELECT m FROM Mercaderias m WHERE m.npesoUnidad = :npesoUnidad")
    , @NamedQuery(name = "Mercaderias.findByNpesoCaja", query = "SELECT m FROM Mercaderias m WHERE m.npesoCaja = :npesoCaja")
    , @NamedQuery(name = "Mercaderias.findByMestado", query = "SELECT m FROM Mercaderias m WHERE m.mestado = :mestado")
    , @NamedQuery(name = "Mercaderias.findByXobs", query = "SELECT m FROM Mercaderias m WHERE m.xobs = :xobs")
    , @NamedQuery(name = "Mercaderias.findByCusuario", query = "SELECT m FROM Mercaderias m WHERE m.cusuario = :cusuario")
    , @NamedQuery(name = "Mercaderias.findByFalta", query = "SELECT m FROM Mercaderias m WHERE m.falta = :falta")
    , @NamedQuery(name = "Mercaderias.findByFultimModif", query = "SELECT m FROM Mercaderias m WHERE m.fultimModif = :fultimModif")
    , @NamedQuery(name = "Mercaderias.findByCusuarioModif", query = "SELECT m FROM Mercaderias m WHERE m.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Mercaderias.findByMexiste", query = "SELECT m FROM Mercaderias m WHERE m.mexiste = :mexiste")
    , @NamedQuery(name = "Mercaderias.findByMpromoPack", query = "SELECT m FROM Mercaderias m WHERE m.mpromoPack = :mpromoPack")})
public class Mercaderias implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MercaderiasPK mercaderiasPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 20)
    @Column(name = "cod_barra")
    private String codBarra;
    @Size(max = 13)
    @Column(name = "cod_anterior")
    private String codAnterior;
    @Size(max = 150)
    @Column(name = "xfoto")
    private String xfoto;
    @Column(name = "nprecio_ppp")
    private Integer nprecioPpp;
    @Column(name = "mgrav_exe")
    private Character mgravExe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nrela_provee")
    private BigDecimal nrelaProvee;
    @Column(name = "nrelacion")
    private BigDecimal nrelacion;
    @Column(name = "npeso_unidad")
    private BigDecimal npesoUnidad;
    @Column(name = "npeso_caja")
    private BigDecimal npesoCaja;
    @Column(name = "mestado")
    private Character mestado;
    @Size(max = 300)
    @Column(name = "xobs")
    private String xobs;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "mexiste")
    private Character mexiste;
    @Column(name = "mpromo_pack")
    private Character mpromoPack;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumn(name = "cmarca", referencedColumnName = "cmarca")
    @ManyToOne
    private MarcasComerciales cmarca;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed")
    @ManyToOne
    private Proveedores codProveed;
    @JoinColumn(name = "cod_sublinea", referencedColumnName = "cod_sublinea")
    @ManyToOne
    private Sublineas codSublinea;

    public Mercaderias() {
    }

    public Mercaderias(MercaderiasPK mercaderiasPK) {
        this.mercaderiasPK = mercaderiasPK;
    }

    public Mercaderias(short codEmpr, String codMerca) {
        this.mercaderiasPK = new MercaderiasPK(codEmpr, codMerca);
    }

    public MercaderiasPK getMercaderiasPK() {
        return mercaderiasPK;
    }

    public void setMercaderiasPK(MercaderiasPK mercaderiasPK) {
        this.mercaderiasPK = mercaderiasPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getCodAnterior() {
        return codAnterior;
    }

    public void setCodAnterior(String codAnterior) {
        this.codAnterior = codAnterior;
    }

    public String getXfoto() {
        return xfoto;
    }

    public void setXfoto(String xfoto) {
        this.xfoto = xfoto;
    }

    public Integer getNprecioPpp() {
        return nprecioPpp;
    }

    public void setNprecioPpp(Integer nprecioPpp) {
        this.nprecioPpp = nprecioPpp;
    }

    public Character getMgravExe() {
        return mgravExe;
    }

    public void setMgravExe(Character mgravExe) {
        this.mgravExe = mgravExe;
    }

    public BigDecimal getNrelaProvee() {
        return nrelaProvee;
    }

    public void setNrelaProvee(BigDecimal nrelaProvee) {
        this.nrelaProvee = nrelaProvee;
    }

    public BigDecimal getNrelacion() {
        return nrelacion;
    }

    public void setNrelacion(BigDecimal nrelacion) {
        this.nrelacion = nrelacion;
    }

    public BigDecimal getNpesoUnidad() {
        return npesoUnidad;
    }

    public void setNpesoUnidad(BigDecimal npesoUnidad) {
        this.npesoUnidad = npesoUnidad;
    }

    public BigDecimal getNpesoCaja() {
        return npesoCaja;
    }

    public void setNpesoCaja(BigDecimal npesoCaja) {
        this.npesoCaja = npesoCaja;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
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

    public Character getMexiste() {
        return mexiste;
    }

    public void setMexiste(Character mexiste) {
        this.mexiste = mexiste;
    }

    public Character getMpromoPack() {
        return mpromoPack;
    }

    public void setMpromoPack(Character mpromoPack) {
        this.mpromoPack = mpromoPack;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public MarcasComerciales getCmarca() {
        return cmarca;
    }

    public void setCmarca(MarcasComerciales cmarca) {
        this.cmarca = cmarca;
    }

    public Proveedores getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(Proveedores codProveed) {
        this.codProveed = codProveed;
    }

    public Sublineas getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(Sublineas codSublinea) {
        this.codSublinea = codSublinea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mercaderiasPK != null ? mercaderiasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mercaderias)) {
            return false;
        }
        Mercaderias other = (Mercaderias) object;
        if ((this.mercaderiasPK == null && other.mercaderiasPK != null) || (this.mercaderiasPK != null && !this.mercaderiasPK.equals(other.mercaderiasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Mercaderias[ mercaderiasPK=" + mercaderiasPK + " ]";
    }
    
}
