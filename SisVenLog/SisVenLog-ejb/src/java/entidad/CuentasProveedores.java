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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * @author Edu
 */
@Entity
@Table(name = "cuentas_proveedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuentasProveedores.findAll", query = "SELECT c FROM CuentasProveedores c")
    , @NamedQuery(name = "CuentasProveedores.findByNroMovim", query = "SELECT c FROM CuentasProveedores c WHERE c.nroMovim = :nroMovim")
    , @NamedQuery(name = "CuentasProveedores.findByFmovim", query = "SELECT c FROM CuentasProveedores c WHERE c.fmovim = :fmovim")
    , @NamedQuery(name = "CuentasProveedores.findByCtipoDocum", query = "SELECT c FROM CuentasProveedores c WHERE c.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "CuentasProveedores.findByNdocumCheq", query = "SELECT c FROM CuentasProveedores c WHERE c.ndocumCheq = :ndocumCheq")
    , @NamedQuery(name = "CuentasProveedores.findByMindice", query = "SELECT c FROM CuentasProveedores c WHERE c.mindice = :mindice")
    , @NamedQuery(name = "CuentasProveedores.findByCodEmpr", query = "SELECT c FROM CuentasProveedores c WHERE c.codEmpr = :codEmpr")
    , @NamedQuery(name = "CuentasProveedores.findByFacCtipoDocum", query = "SELECT c FROM CuentasProveedores c WHERE c.facCtipoDocum = :facCtipoDocum")
    , @NamedQuery(name = "CuentasProveedores.findByNrofact", query = "SELECT c FROM CuentasProveedores c WHERE c.nrofact = :nrofact")
    , @NamedQuery(name = "CuentasProveedores.findByManulado", query = "SELECT c FROM CuentasProveedores c WHERE c.manulado = :manulado")
    , @NamedQuery(name = "CuentasProveedores.findByCusuario", query = "SELECT c FROM CuentasProveedores c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "CuentasProveedores.findByFalta", query = "SELECT c FROM CuentasProveedores c WHERE c.falta = :falta")
    , @NamedQuery(name = "CuentasProveedores.findByTexentas", query = "SELECT c FROM CuentasProveedores c WHERE c.texentas = :texentas")
    , @NamedQuery(name = "CuentasProveedores.findByTgravadas", query = "SELECT c FROM CuentasProveedores c WHERE c.tgravadas = :tgravadas")
    , @NamedQuery(name = "CuentasProveedores.findByTimpuestos", query = "SELECT c FROM CuentasProveedores c WHERE c.timpuestos = :timpuestos")
    , @NamedQuery(name = "CuentasProveedores.findByIpagado", query = "SELECT c FROM CuentasProveedores c WHERE c.ipagado = :ipagado")
    , @NamedQuery(name = "CuentasProveedores.findByIsaldo", query = "SELECT c FROM CuentasProveedores c WHERE c.isaldo = :isaldo")
    , @NamedQuery(name = "CuentasProveedores.findByIretencion", query = "SELECT c FROM CuentasProveedores c WHERE c.iretencion = :iretencion")
    , @NamedQuery(name = "CuentasProveedores.findByFvenc", query = "SELECT c FROM CuentasProveedores c WHERE c.fvenc = :fvenc")
    , @NamedQuery(name = "CuentasProveedores.findByOtrCtipoDocum", query = "SELECT c FROM CuentasProveedores c WHERE c.otrCtipoDocum = :otrCtipoDocum")
    , @NamedQuery(name = "CuentasProveedores.findByOtrNdocum", query = "SELECT c FROM CuentasProveedores c WHERE c.otrNdocum = :otrNdocum")
    , @NamedQuery(name = "CuentasProveedores.findByCcanalCompra", query = "SELECT c FROM CuentasProveedores c WHERE c.ccanalCompra = :ccanalCompra")
    , @NamedQuery(name = "CuentasProveedores.findByOtrFdocum", query = "SELECT c FROM CuentasProveedores c WHERE c.otrFdocum = :otrFdocum")
    , @NamedQuery(name = "CuentasProveedores.findByFfactur", query = "SELECT c FROM CuentasProveedores c WHERE c.ffactur = :ffactur")})
public class CuentasProveedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_movim")
    private Long nroMovim;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fmovim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fmovim;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ndocum_cheq")
    private String ndocumCheq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mindice")
    private short mindice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Size(max = 3)
    @Column(name = "fac_ctipo_docum")
    private String facCtipoDocum;
    @Column(name = "nrofact")
    private Long nrofact;
    @Column(name = "manulado")
    private Short manulado;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "texentas")
    private long texentas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tgravadas")
    private long tgravadas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timpuestos")
    private long timpuestos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipagado")
    private long ipagado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isaldo")
    private long isaldo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iretencion")
    private long iretencion;
    @Column(name = "fvenc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvenc;
    @Size(max = 3)
    @Column(name = "otr_ctipo_docum")
    private String otrCtipoDocum;
    @Column(name = "otr_ndocum")
    private Long otrNdocum;
    @Size(max = 2)
    @Column(name = "ccanal_compra")
    private String ccanalCompra;
    @Column(name = "otr_fdocum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otrFdocum;
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
    @JoinColumn(name = "cod_banco", referencedColumnName = "cod_banco")
    @ManyToOne
    private Bancos codBanco;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed")
    @ManyToOne(optional = false)
    private Proveedores codProveed;

    public CuentasProveedores() {
    }

    public CuentasProveedores(Long nroMovim) {
        this.nroMovim = nroMovim;
    }

    public CuentasProveedores(Long nroMovim, Date fmovim, String ctipoDocum, String ndocumCheq, short mindice, short codEmpr, long texentas, long tgravadas, long timpuestos, long ipagado, long isaldo, long iretencion) {
        this.nroMovim = nroMovim;
        this.fmovim = fmovim;
        this.ctipoDocum = ctipoDocum;
        this.ndocumCheq = ndocumCheq;
        this.mindice = mindice;
        this.codEmpr = codEmpr;
        this.texentas = texentas;
        this.tgravadas = tgravadas;
        this.timpuestos = timpuestos;
        this.ipagado = ipagado;
        this.isaldo = isaldo;
        this.iretencion = iretencion;
    }

    public Long getNroMovim() {
        return nroMovim;
    }

    public void setNroMovim(Long nroMovim) {
        this.nroMovim = nroMovim;
    }

    public Date getFmovim() {
        return fmovim;
    }

    public void setFmovim(Date fmovim) {
        this.fmovim = fmovim;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public String getNdocumCheq() {
        return ndocumCheq;
    }

    public void setNdocumCheq(String ndocumCheq) {
        this.ndocumCheq = ndocumCheq;
    }

    public short getMindice() {
        return mindice;
    }

    public void setMindice(short mindice) {
        this.mindice = mindice;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getFacCtipoDocum() {
        return facCtipoDocum;
    }

    public void setFacCtipoDocum(String facCtipoDocum) {
        this.facCtipoDocum = facCtipoDocum;
    }

    public Long getNrofact() {
        return nrofact;
    }

    public void setNrofact(Long nrofact) {
        this.nrofact = nrofact;
    }

    public Short getManulado() {
        return manulado;
    }

    public void setManulado(Short manulado) {
        this.manulado = manulado;
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

    public long getTexentas() {
        return texentas;
    }

    public void setTexentas(long texentas) {
        this.texentas = texentas;
    }

    public long getTgravadas() {
        return tgravadas;
    }

    public void setTgravadas(long tgravadas) {
        this.tgravadas = tgravadas;
    }

    public long getTimpuestos() {
        return timpuestos;
    }

    public void setTimpuestos(long timpuestos) {
        this.timpuestos = timpuestos;
    }

    public long getIpagado() {
        return ipagado;
    }

    public void setIpagado(long ipagado) {
        this.ipagado = ipagado;
    }

    public long getIsaldo() {
        return isaldo;
    }

    public void setIsaldo(long isaldo) {
        this.isaldo = isaldo;
    }

    public long getIretencion() {
        return iretencion;
    }

    public void setIretencion(long iretencion) {
        this.iretencion = iretencion;
    }

    public Date getFvenc() {
        return fvenc;
    }

    public void setFvenc(Date fvenc) {
        this.fvenc = fvenc;
    }

    public String getOtrCtipoDocum() {
        return otrCtipoDocum;
    }

    public void setOtrCtipoDocum(String otrCtipoDocum) {
        this.otrCtipoDocum = otrCtipoDocum;
    }

    public Long getOtrNdocum() {
        return otrNdocum;
    }

    public void setOtrNdocum(Long otrNdocum) {
        this.otrNdocum = otrNdocum;
    }

    public String getCcanalCompra() {
        return ccanalCompra;
    }

    public void setCcanalCompra(String ccanalCompra) {
        this.ccanalCompra = ccanalCompra;
    }

    public Date getOtrFdocum() {
        return otrFdocum;
    }

    public void setOtrFdocum(Date otrFdocum) {
        this.otrFdocum = otrFdocum;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public Bancos getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(Bancos codBanco) {
        this.codBanco = codBanco;
    }

    public Proveedores getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(Proveedores codProveed) {
        this.codProveed = codProveed;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nroMovim != null ? nroMovim.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuentasProveedores)) {
            return false;
        }
        CuentasProveedores other = (CuentasProveedores) object;
        if ((this.nroMovim == null && other.nroMovim != null) || (this.nroMovim != null && !this.nroMovim.equals(other.nroMovim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CuentasProveedores[ nroMovim=" + nroMovim + " ]";
    }
    
}
