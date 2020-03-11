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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Hugo
 */
@Entity
@Table(name = "clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clientes.findAll", query = "SELECT c FROM Clientes c order by c.falta")
    , @NamedQuery(name = "Clientes.findByCodCliente", query = "SELECT c FROM Clientes c WHERE c.codCliente = :codCliente")
    , @NamedQuery(name = "Clientes.findByXnombre", query = "SELECT c FROM Clientes c WHERE c.xnombre = :xnombre")
    , @NamedQuery(name = "Clientes.findByCodEmpr", query = "SELECT c FROM Clientes c WHERE c.codEmpr = :codEmpr")
    , @NamedQuery(name = "Clientes.findByCodRuta", query = "SELECT c FROM Clientes c WHERE c.codRuta = :codRuta")
    , @NamedQuery(name = "Clientes.findByCodCiudad", query = "SELECT c FROM Clientes c WHERE c.codCiudad = :codCiudad")
    , @NamedQuery(name = "Clientes.findByCtipoCliente", query = "SELECT c FROM Clientes c WHERE c.ctipoCliente = :ctipoCliente")
    , @NamedQuery(name = "Clientes.findByXcedula", query = "SELECT c FROM Clientes c WHERE c.xcedula = :xcedula")
    , @NamedQuery(name = "Clientes.findByXruc", query = "SELECT c FROM Clientes c WHERE c.xruc = :xruc")
    , @NamedQuery(name = "Clientes.findByXtelef", query = "SELECT c FROM Clientes c WHERE c.xtelef = :xtelef")
    , @NamedQuery(name = "Clientes.findByXfax", query = "SELECT c FROM Clientes c WHERE c.xfax = :xfax")
    , @NamedQuery(name = "Clientes.findByXdirec", query = "SELECT c FROM Clientes c WHERE c.xdirec = :xdirec")
    , @NamedQuery(name = "Clientes.findByNordenRuta", query = "SELECT c FROM Clientes c WHERE c.nordenRuta = :nordenRuta")
    , @NamedQuery(name = "Clientes.findByXpropietario", query = "SELECT c FROM Clientes c WHERE c.xpropietario = :xpropietario")
    , @NamedQuery(name = "Clientes.findByNplazoCredito", query = "SELECT c FROM Clientes c WHERE c.nplazoCredito = :nplazoCredito")
    , @NamedQuery(name = "Clientes.findByIlimiteCompra", query = "SELECT c FROM Clientes c WHERE c.ilimiteCompra = :ilimiteCompra")
    , @NamedQuery(name = "Clientes.findByMformaPago", query = "SELECT c FROM Clientes c WHERE c.mformaPago = :mformaPago")
    , @NamedQuery(name = "Clientes.findByNriesgo", query = "SELECT c FROM Clientes c WHERE c.nriesgo = :nriesgo")
    , @NamedQuery(name = "Clientes.findByCodGrupo", query = "SELECT c FROM Clientes c WHERE c.codGrupo = :codGrupo")
    , @NamedQuery(name = "Clientes.findByCodEstado", query = "SELECT c FROM Clientes c WHERE c.codEstado = :codEstado")
    , @NamedQuery(name = "Clientes.findByXrazonEstado", query = "SELECT c FROM Clientes c WHERE c.xrazonEstado = :xrazonEstado")
    , @NamedQuery(name = "Clientes.findByFprimFact", query = "SELECT c FROM Clientes c WHERE c.fprimFact = :fprimFact")
    , @NamedQuery(name = "Clientes.findByXcontacto", query = "SELECT c FROM Clientes c WHERE c.xcontacto = :xcontacto")
    , @NamedQuery(name = "Clientes.findByIsaldo", query = "SELECT c FROM Clientes c WHERE c.isaldo = :isaldo")
    , @NamedQuery(name = "Clientes.findByFalta", query = "SELECT c FROM Clientes c WHERE c.falta = :falta")
    , @NamedQuery(name = "Clientes.findByCusuario", query = "SELECT c FROM Clientes c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "Clientes.findByFultimModif", query = "SELECT c FROM Clientes c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "Clientes.findByCusuarioModif", query = "SELECT c FROM Clientes c WHERE c.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Clientes.findByCodAnterior", query = "SELECT c FROM Clientes c WHERE c.codAnterior = :codAnterior")
    , @NamedQuery(name = "Clientes.findByXobs", query = "SELECT c FROM Clientes c WHERE c.xobs = :xobs")
    , @NamedQuery(name = "Clientes.findByXemail", query = "SELECT c FROM Clientes c WHERE c.xemail = :xemail")
    , @NamedQuery(name = "Clientes.findByXctacte", query = "SELECT c FROM Clientes c WHERE c.xctacte = :xctacte")
    , @NamedQuery(name = "Clientes.findByCodBanco", query = "SELECT c FROM Clientes c WHERE c.codBanco = :codBanco")
    , @NamedQuery(name = "Clientes.findByNfrec", query = "SELECT c FROM Clientes c WHERE c.nfrec = :nfrec")
    , @NamedQuery(name = "Clientes.findByXdiasVisita", query = "SELECT c FROM Clientes c WHERE c.xdiasVisita = :xdiasVisita")
    , @NamedQuery(name = "Clientes.findByCcategCliente", query = "SELECT c FROM Clientes c WHERE c.ccategCliente = :ccategCliente")
    , @NamedQuery(name = "Clientes.findByCodCanal", query = "SELECT c FROM Clientes c WHERE c.codCanal = :codCanal")
    , @NamedQuery(name = "Clientes.findByNplazoImpresion", query = "SELECT c FROM Clientes c WHERE c.nplazoImpresion = :nplazoImpresion")})
public class Clientes implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@NotNull
    @Column(name = "cod_cliente")
    private Integer codCliente;
    //@Size(max = 50)
    @Column(name = "xnombre")
    private String xnombre;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Column(name = "cod_ruta")
    private Short codRuta;
    @Column(name = "cod_ciudad")
    private Short codCiudad;
    @Column(name = "ctipo_cliente")
    private Character ctipoCliente;
    @Column(name = "xcedula")
    private Long xcedula;
    //@Size(max = 15)
    @Column(name = "xruc")
    private String xruc;
    //@Size(max = 16)
    @Column(name = "xtelef")
    private String xtelef;
    //@Size(max = 16)
    @Column(name = "xfax")
    private String xfax;
    //@Size(max = 50)
    @Column(name = "xdirec")
    private String xdirec;
    @Column(name = "norden_ruta")
    private Short nordenRuta;
    //@Size(max = 50)
    @Column(name = "xpropietario")
    private String xpropietario;
    @Column(name = "nplazo_credito")
    private Short nplazoCredito;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "ilimite_compra")
    private long ilimiteCompra;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "mforma_pago")
    private Character mformaPago;
    @Column(name = "nriesgo")
    private Short nriesgo;
    @Column(name = "cod_grupo")
    private Short codGrupo;
    //@Size(max = 2)
    @Column(name = "cod_estado")
    private String codEstado;
    //@Size(max = 150)
    @Column(name = "xrazon_estado")
    private String xrazonEstado;
    @Column(name = "fprim_fact")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fprimFact;
    //@Size(max = 50)
    @Column(name = "xcontacto")
    private String xcontacto;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "isaldo")
    private long isaldo;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    //@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    //@Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    //@Size(max = 6)
    @Column(name = "cod_anterior")
    private String codAnterior;
    //@Size(max = 300)
    @Column(name = "xobs")
    private String xobs;
    //@Size(max = 50)
    @Column(name = "xemail")
    private String xemail;
    //@Size(max = 15)
    @Column(name = "xctacte")
    private String xctacte;
    @Column(name = "cod_banco")
    private Short codBanco;
    @Column(name = "nfrec")
    private Short nfrec;
    //@Size(max = 7)
    @Column(name = "xdias_visita")
    private String xdiasVisita;
    //@Size(max = 2)
    @Column(name = "ccateg_cliente")
    private String ccategCliente;
    //@Size(max = 2)
    @Column(name = "cod_canal")
    private String codCanal;
    @Column(name = "nplazo_impresion")
    private Short nplazoImpresion;
    @OneToMany(mappedBy = "codCliente")
    private Collection<CuentasCorrientes> cuentasCorrientesCollection;
    @OneToMany(mappedBy = "codCliente")
    private Collection<Cheques> chequesCollection;
    
    public Clientes() {
    }

    public Clientes(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public Clientes(Integer codCliente, short codEmpr, long ilimiteCompra, Character mformaPago, long isaldo) {
        this.codCliente = codCliente;
        this.codEmpr = codEmpr;
        this.ilimiteCompra = ilimiteCompra;
        this.mformaPago = mformaPago;
        this.isaldo = isaldo;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public String getXnombre() {
        return xnombre;
    }

    public void setXnombre(String xnombre) {
        this.xnombre = xnombre;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public Short getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(Short codRuta) {
        this.codRuta = codRuta;
    }

    public Short getCodCiudad() {
        return codCiudad;
    }

    public void setCodCiudad(Short codCiudad) {
        this.codCiudad = codCiudad;
    }

    public Character getCtipoCliente() {
        return ctipoCliente;
    }

    public void setCtipoCliente(Character ctipoCliente) {
        this.ctipoCliente = ctipoCliente;
    }

    public Long getXcedula() {
        return xcedula;
    }

    public void setXcedula(Long xcedula) {
        this.xcedula = xcedula;
    }

    public String getXruc() {
        return xruc;
    }

    public void setXruc(String xruc) {
        this.xruc = xruc;
    }

    public String getXtelef() {
        return xtelef;
    }

    public void setXtelef(String xtelef) {
        this.xtelef = xtelef;
    }

    public String getXfax() {
        return xfax;
    }

    public void setXfax(String xfax) {
        this.xfax = xfax;
    }

    public String getXdirec() {
        return xdirec;
    }

    public void setXdirec(String xdirec) {
        this.xdirec = xdirec;
    }

    public Short getNordenRuta() {
        return nordenRuta;
    }

    public void setNordenRuta(Short nordenRuta) {
        this.nordenRuta = nordenRuta;
    }

    public String getXpropietario() {
        return xpropietario;
    }

    public void setXpropietario(String xpropietario) {
        this.xpropietario = xpropietario;
    }

    public Short getNplazoCredito() {
        return nplazoCredito;
    }

    public void setNplazoCredito(Short nplazoCredito) {
        this.nplazoCredito = nplazoCredito;
    }

    public long getIlimiteCompra() {
        return ilimiteCompra;
    }

    public void setIlimiteCompra(long ilimiteCompra) {
        this.ilimiteCompra = ilimiteCompra;
    }

    public Character getMformaPago() {
        return mformaPago;
    }

    public void setMformaPago(Character mformaPago) {
        this.mformaPago = mformaPago;
    }

    public Short getNriesgo() {
        return nriesgo;
    }

    public void setNriesgo(Short nriesgo) {
        this.nriesgo = nriesgo;
    }

    public Short getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(Short codGrupo) {
        this.codGrupo = codGrupo;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getXrazonEstado() {
        return xrazonEstado;
    }

    public void setXrazonEstado(String xrazonEstado) {
        this.xrazonEstado = xrazonEstado;
    }

    public Date getFprimFact() {
        return fprimFact;
    }

    public void setFprimFact(Date fprimFact) {
        this.fprimFact = fprimFact;
    }

    public String getXcontacto() {
        return xcontacto;
    }

    public void setXcontacto(String xcontacto) {
        this.xcontacto = xcontacto;
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

    public String getCodAnterior() {
        return codAnterior;
    }

    public void setCodAnterior(String codAnterior) {
        this.codAnterior = codAnterior;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
    }

    public String getXemail() {
        return xemail;
    }

    public void setXemail(String xemail) {
        this.xemail = xemail;
    }

    public String getXctacte() {
        return xctacte;
    }

    public void setXctacte(String xctacte) {
        this.xctacte = xctacte;
    }

    public Short getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(Short codBanco) {
        this.codBanco = codBanco;
    }

    public Short getNfrec() {
        return nfrec;
    }

    public void setNfrec(Short nfrec) {
        this.nfrec = nfrec;
    }

    public String getXdiasVisita() {
        return xdiasVisita;
    }

    public void setXdiasVisita(String xdiasVisita) {
        this.xdiasVisita = xdiasVisita;
    }

    public String getCcategCliente() {
        return ccategCliente;
    }

    public void setCcategCliente(String ccategCliente) {
        this.ccategCliente = ccategCliente;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public Short getNplazoImpresion() {
        return nplazoImpresion;
    }

    public void setNplazoImpresion(Short nplazoImpresion) {
        this.nplazoImpresion = nplazoImpresion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCliente != null ? codCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clientes)) {
            return false;
        }
        Clientes other = (Clientes) object;
        if ((this.codCliente == null && other.codCliente != null) || (this.codCliente != null && !this.codCliente.equals(other.codCliente))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "entidad.Clientes[ codCliente=" + codCliente + " ]";
//    }

    @Override
    public String toString() {
        return "Clientes{" + "codCliente=" + codCliente + ", xnombre=" + xnombre + ", codEmpr=" + codEmpr + ", codRuta=" + codRuta + ", codCiudad=" + codCiudad + ", ctipoCliente=" + ctipoCliente + ", xcedula=" + xcedula + ", xruc=" + xruc + ", xtelef=" + xtelef + ", xfax=" + xfax + ", xdirec=" + xdirec + ", nordenRuta=" + nordenRuta + ", xpropietario=" + xpropietario + ", nplazoCredito=" + nplazoCredito + ", ilimiteCompra=" + ilimiteCompra + ", mformaPago=" + mformaPago + ", nriesgo=" + nriesgo + ", codGrupo=" + codGrupo + ", codEstado=" + codEstado + ", xrazonEstado=" + xrazonEstado + ", fprimFact=" + fprimFact + ", xcontacto=" + xcontacto + ", isaldo=" + isaldo + ", falta=" + falta + ", cusuario=" + cusuario + ", fultimModif=" + fultimModif + ", cusuarioModif=" + cusuarioModif + ", codAnterior=" + codAnterior + ", xobs=" + xobs + ", xemail=" + xemail + ", xctacte=" + xctacte + ", codBanco=" + codBanco + ", nfrec=" + nfrec + ", xdiasVisita=" + xdiasVisita + ", ccategCliente=" + ccategCliente + ", codCanal=" + codCanal + ", nplazoImpresion=" + nplazoImpresion + '}';
    }
    

    @XmlTransient
    public Collection<CuentasCorrientes> getCuentasCorrientesCollection() {
        return cuentasCorrientesCollection;
    }

    public void setCuentasCorrientesCollection(Collection<CuentasCorrientes> cuentasCorrientesCollection) {
        this.cuentasCorrientesCollection = cuentasCorrientesCollection;
    }

    @XmlTransient
    public Collection<Cheques> getChequesCollection() {
        return chequesCollection;
    }

    public void setChequesCollection(Collection<Cheques> chequesCollection) {
        this.chequesCollection = chequesCollection;
    }
    
}
