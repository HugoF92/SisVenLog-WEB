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
@Table(name = "cuentas_corrientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuentasCorrientes.findAll", query = "SELECT c FROM CuentasCorrientes c")
    , @NamedQuery(name = "CuentasCorrientes.findByNroMovim", query = "SELECT c FROM CuentasCorrientes c WHERE c.nroMovim = :nroMovim")
    , @NamedQuery(name = "CuentasCorrientes.findByFmovim", query = "SELECT c FROM CuentasCorrientes c WHERE c.fmovim = :fmovim")
    , @NamedQuery(name = "CuentasCorrientes.findByCtipoDocum", query = "SELECT c FROM CuentasCorrientes c WHERE c.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "CuentasCorrientes.findByNdocumCheq", query = "SELECT c FROM CuentasCorrientes c WHERE c.ndocumCheq = :ndocumCheq")
    , @NamedQuery(name = "CuentasCorrientes.findByCodEmpr", query = "SELECT c FROM CuentasCorrientes c WHERE c.codEmpr = :codEmpr")
    , @NamedQuery(name = "CuentasCorrientes.findByNrofact", query = "SELECT c FROM CuentasCorrientes c WHERE c.nrofact = :nrofact")
    , @NamedQuery(name = "CuentasCorrientes.findByFacCtipoDocum", query = "SELECT c FROM CuentasCorrientes c WHERE c.facCtipoDocum = :facCtipoDocum")
    , @NamedQuery(name = "CuentasCorrientes.findByTexentas", query = "SELECT c FROM CuentasCorrientes c WHERE c.texentas = :texentas")
    , @NamedQuery(name = "CuentasCorrientes.findByIpagado", query = "SELECT c FROM CuentasCorrientes c WHERE c.ipagado = :ipagado")
    , @NamedQuery(name = "CuentasCorrientes.findByIretencion", query = "SELECT c FROM CuentasCorrientes c WHERE c.iretencion = :iretencion")
    , @NamedQuery(name = "CuentasCorrientes.findByIsaldo", query = "SELECT c FROM CuentasCorrientes c WHERE c.isaldo = :isaldo")
    , @NamedQuery(name = "CuentasCorrientes.findByFalta", query = "SELECT c FROM CuentasCorrientes c WHERE c.falta = :falta")
    , @NamedQuery(name = "CuentasCorrientes.findByManulado", query = "SELECT c FROM CuentasCorrientes c WHERE c.manulado = :manulado")
    , @NamedQuery(name = "CuentasCorrientes.findByCusuario", query = "SELECT c FROM CuentasCorrientes c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "CuentasCorrientes.findByTgravadas", query = "SELECT c FROM CuentasCorrientes c WHERE c.tgravadas = :tgravadas")
    , @NamedQuery(name = "CuentasCorrientes.findByTimpuestos", query = "SELECT c FROM CuentasCorrientes c WHERE c.timpuestos = :timpuestos")
    , @NamedQuery(name = "CuentasCorrientes.findByMindice", query = "SELECT c FROM CuentasCorrientes c WHERE c.mindice = :mindice")
    , @NamedQuery(name = "CuentasCorrientes.findByFvenc", query = "SELECT c FROM CuentasCorrientes c WHERE c.fvenc = :fvenc")
    , @NamedQuery(name = "CuentasCorrientes.findByXcuentaBco", query = "SELECT c FROM CuentasCorrientes c WHERE c.xcuentaBco = :xcuentaBco")
    , @NamedQuery(name = "CuentasCorrientes.findByFfactur", query = "SELECT c FROM CuentasCorrientes c WHERE c.ffactur = :ffactur")})
public class CuentasCorrientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_movim")
    private Long nroMovim;
    @Column(name = "fmovim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fmovim;
    @Size(max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Size(max = 15)
    @Column(name = "ndocum_cheq")
    private String ndocumCheq;
    @Column(name = "cod_empr")
    private Short codEmpr;
    @Column(name = "nrofact")
    private Long nrofact;
    @Size(max = 3)
    @Column(name = "fac_ctipo_docum")
    private String facCtipoDocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "texentas")
    private long texentas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ipagado")
    private long ipagado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iretencion")
    private long iretencion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isaldo")
    private long isaldo;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "manulado")
    private Short manulado;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tgravadas")
    private long tgravadas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timpuestos")
    private long timpuestos;
    @Column(name = "mindice")
    private Short mindice;
    @Column(name = "fvenc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvenc;
    //@Size(max = 20)
    @Column(name = "xcuenta_bco")
    private String xcuentaBco;
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
    @JoinColumn(name = "cod_banco", referencedColumnName = "cod_banco")
    @ManyToOne
    private Bancos codBanco;
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente")
    @ManyToOne
    private Clientes codCliente;

    public CuentasCorrientes() {
    }

    public CuentasCorrientes(Long nroMovim) {
        this.nroMovim = nroMovim;
    }

    public CuentasCorrientes(Long nroMovim, long texentas, long ipagado, long iretencion, long isaldo, long tgravadas, long timpuestos) {
        this.nroMovim = nroMovim;
        this.texentas = texentas;
        this.ipagado = ipagado;
        this.iretencion = iretencion;
        this.isaldo = isaldo;
        this.tgravadas = tgravadas;
        this.timpuestos = timpuestos;
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

    public Short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(Short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public Long getNrofact() {
        return nrofact;
    }

    public void setNrofact(Long nrofact) {
        this.nrofact = nrofact;
    }

    public String getFacCtipoDocum() {
        return facCtipoDocum;
    }

    public void setFacCtipoDocum(String facCtipoDocum) {
        this.facCtipoDocum = facCtipoDocum;
    }

    public long getTexentas() {
        return texentas;
    }

    public void setTexentas(long texentas) {
        this.texentas = texentas;
    }

    public long getIpagado() {
        return ipagado;
    }

    public void setIpagado(long ipagado) {
        this.ipagado = ipagado;
    }

    public long getIretencion() {
        return iretencion;
    }

    public void setIretencion(long iretencion) {
        this.iretencion = iretencion;
    }

    public long getIsaldo() {
        return isaldo;
    }

    public void setIsaldo(long isaldo) {
        this.isaldo = isaldo;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
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

    public Short getMindice() {
        return mindice;
    }

    public void setMindice(Short mindice) {
        this.mindice = mindice;
    }

    public Date getFvenc() {
        return fvenc;
    }

    public void setFvenc(Date fvenc) {
        this.fvenc = fvenc;
    }

    public String getXcuentaBco() {
        return xcuentaBco;
    }

    public void setXcuentaBco(String xcuentaBco) {
        this.xcuentaBco = xcuentaBco;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
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
        if (!(object instanceof CuentasCorrientes)) {
            return false;
        }
        CuentasCorrientes other = (CuentasCorrientes) object;
        if ((this.nroMovim == null && other.nroMovim != null) || (this.nroMovim != null && !this.nroMovim.equals(other.nroMovim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CuentasCorrientes[ nroMovim=" + nroMovim + " ]";
    }

    public Bancos getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(Bancos codBanco) {
        this.codBanco = codBanco;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }
    
}
