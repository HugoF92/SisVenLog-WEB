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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "movimientos_merca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimientosMerca.findAll", query = "SELECT m FROM MovimientosMerca m")
    , @NamedQuery(name = "MovimientosMerca.findByNroMovim", query = "SELECT m FROM MovimientosMerca m WHERE m.nroMovim = :nroMovim")
    , @NamedQuery(name = "MovimientosMerca.findByCodEmpr", query = "SELECT m FROM MovimientosMerca m WHERE m.codEmpr = :codEmpr")
    , @NamedQuery(name = "MovimientosMerca.findByCodMerca", query = "SELECT m FROM MovimientosMerca m WHERE m.codMerca = :codMerca")
    , @NamedQuery(name = "MovimientosMerca.findByCodVendedor", query = "SELECT m FROM MovimientosMerca m WHERE m.codVendedor = :codVendedor")
    , @NamedQuery(name = "MovimientosMerca.findByCodCliente", query = "SELECT m FROM MovimientosMerca m WHERE m.codCliente = :codCliente")
    , @NamedQuery(name = "MovimientosMerca.findByCodProveed", query = "SELECT m FROM MovimientosMerca m WHERE m.codProveed = :codProveed")
    , @NamedQuery(name = "MovimientosMerca.findByCodDepo", query = "SELECT m FROM MovimientosMerca m WHERE m.codDepo = :codDepo")
    , @NamedQuery(name = "MovimientosMerca.findByCodFdepo", query = "SELECT m FROM MovimientosMerca m WHERE m.codFdepo = :codFdepo")
    , @NamedQuery(name = "MovimientosMerca.findByCantCajas", query = "SELECT m FROM MovimientosMerca m WHERE m.cantCajas = :cantCajas")
    , @NamedQuery(name = "MovimientosMerca.findByCodEntregador", query = "SELECT m FROM MovimientosMerca m WHERE m.codEntregador = :codEntregador")
    , @NamedQuery(name = "MovimientosMerca.findByCantUnid", query = "SELECT m FROM MovimientosMerca m WHERE m.cantUnid = :cantUnid")
    , @NamedQuery(name = "MovimientosMerca.findByCodRuta", query = "SELECT m FROM MovimientosMerca m WHERE m.codRuta = :codRuta")
    , @NamedQuery(name = "MovimientosMerca.findByCodZona", query = "SELECT m FROM MovimientosMerca m WHERE m.codZona = :codZona")
    , @NamedQuery(name = "MovimientosMerca.findByPrecioCaja", query = "SELECT m FROM MovimientosMerca m WHERE m.precioCaja = :precioCaja")
    , @NamedQuery(name = "MovimientosMerca.findByPrecioUnidad", query = "SELECT m FROM MovimientosMerca m WHERE m.precioUnidad = :precioUnidad")
    , @NamedQuery(name = "MovimientosMerca.findByNpeso", query = "SELECT m FROM MovimientosMerca m WHERE m.npeso = :npeso")
    , @NamedQuery(name = "MovimientosMerca.findByManulado", query = "SELECT m FROM MovimientosMerca m WHERE m.manulado = :manulado")
    , @NamedQuery(name = "MovimientosMerca.findByFmovim", query = "SELECT m FROM MovimientosMerca m WHERE m.fmovim = :fmovim")
    , @NamedQuery(name = "MovimientosMerca.findByCtipoDocum", query = "SELECT m FROM MovimientosMerca m WHERE m.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "MovimientosMerca.findByNdocum", query = "SELECT m FROM MovimientosMerca m WHERE m.ndocum = :ndocum")
    , @NamedQuery(name = "MovimientosMerca.findByIgravadas", query = "SELECT m FROM MovimientosMerca m WHERE m.igravadas = :igravadas")
    , @NamedQuery(name = "MovimientosMerca.findByIexentas", query = "SELECT m FROM MovimientosMerca m WHERE m.iexentas = :iexentas")
    , @NamedQuery(name = "MovimientosMerca.findByImpuestos", query = "SELECT m FROM MovimientosMerca m WHERE m.impuestos = :impuestos")
    , @NamedQuery(name = "MovimientosMerca.findByNprecioPpp", query = "SELECT m FROM MovimientosMerca m WHERE m.nprecioPpp = :nprecioPpp")
    , @NamedQuery(name = "MovimientosMerca.findByFalta", query = "SELECT m FROM MovimientosMerca m WHERE m.falta = :falta")
    , @NamedQuery(name = "MovimientosMerca.findByCusuario", query = "SELECT m FROM MovimientosMerca m WHERE m.cusuario = :cusuario")
    , @NamedQuery(name = "MovimientosMerca.findByNrofact", query = "SELECT m FROM MovimientosMerca m WHERE m.nrofact = :nrofact")
    , @NamedQuery(name = "MovimientosMerca.findByFacCtipoDocum", query = "SELECT m FROM MovimientosMerca m WHERE m.facCtipoDocum = :facCtipoDocum")
    , @NamedQuery(name = "MovimientosMerca.findByOldCajas", query = "SELECT m FROM MovimientosMerca m WHERE m.oldCajas = :oldCajas")})
public class MovimientosMerca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el numero de movimiento.")
    @Column(name = "nro_movim")
    private Long nroMovim;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el codigo de empresa.")
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el codigo de merca.")
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Column(name = "cod_vendedor")
    private Short codVendedor;
    @Column(name = "cod_cliente")
    private Integer codCliente;
    @Column(name = "cod_proveed")
    private Short codProveed;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el codigo deposito.")
    @Column(name = "cod_depo")
    private short codDepo;
    @Column(name = "cod_fdepo")
    private Short codFdepo;
    @Column(name = "cant_cajas")
    private Long cantCajas;
    @Column(name = "cod_entregador")
    private Short codEntregador;
    @Column(name = "cant_unid")
    private Long cantUnid;
    @Column(name = "cod_ruta")
    private Short codRuta;
    @Size(max = 2)
    @Column(name = "cod_zona")
    private String codZona;
    @Column(name = "precio_caja")
    private Long precioCaja;
    @Column(name = "precio_unidad")
    private Long precioUnidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "npeso")
    private BigDecimal npeso;
    @Column(name = "manulado")
    private Short manulado;
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
    @Column(name = "ndocum")
    private long ndocum;
    @Column(name = "igravadas")
    private Long igravadas;
    @Column(name = "iexentas")
    private Long iexentas;
    @Column(name = "impuestos")
    private Long impuestos;
    @Column(name = "nprecio_ppp")
    private Integer nprecioPpp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "nrofact")
    private Long nrofact;
    @Size(max = 3)
    @Column(name = "fac_ctipo_docum")
    private String facCtipoDocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "old_cajas")
    private long oldCajas;

    public MovimientosMerca() {
    }

    public MovimientosMerca(Long nroMovim) {
        this.nroMovim = nroMovim;
    }

    public MovimientosMerca(Long nroMovim, short codEmpr, String codMerca, short codDepo, String ctipoDocum, long ndocum, Date falta, String cusuario, long oldCajas) {
        this.nroMovim = nroMovim;
        this.codEmpr = codEmpr;
        this.codMerca = codMerca;
        this.codDepo = codDepo;
        this.ctipoDocum = ctipoDocum;
        this.ndocum = ndocum;
        this.falta = falta;
        this.cusuario = cusuario;
        this.oldCajas = oldCajas;
    }

    public Long getNroMovim() {
        return nroMovim;
    }

    public void setNroMovim(Long nroMovim) {
        this.nroMovim = nroMovim;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public Short getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(Short codVendedor) {
        this.codVendedor = codVendedor;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }

    public Short getCodProveed() {
        return codProveed;
    }

    public void setCodProveed(Short codProveed) {
        this.codProveed = codProveed;
    }

    public short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(short codDepo) {
        this.codDepo = codDepo;
    }

    public Short getCodFdepo() {
        return codFdepo;
    }

    public void setCodFdepo(Short codFdepo) {
        this.codFdepo = codFdepo;
    }

    public Long getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(Long cantCajas) {
        this.cantCajas = cantCajas;
    }

    public Short getCodEntregador() {
        return codEntregador;
    }

    public void setCodEntregador(Short codEntregador) {
        this.codEntregador = codEntregador;
    }

    public Long getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(Long cantUnid) {
        this.cantUnid = cantUnid;
    }

    public Short getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(Short codRuta) {
        this.codRuta = codRuta;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public Long getPrecioCaja() {
        return precioCaja;
    }

    public void setPrecioCaja(Long precioCaja) {
        this.precioCaja = precioCaja;
    }

    public Long getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(Long precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public BigDecimal getNpeso() {
        return npeso;
    }

    public void setNpeso(BigDecimal npeso) {
        this.npeso = npeso;
    }

    public Short getManulado() {
        return manulado;
    }

    public void setManulado(Short manulado) {
        this.manulado = manulado;
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

    public long getNdocum() {
        return ndocum;
    }

    public void setNdocum(long ndocum) {
        this.ndocum = ndocum;
    }

    public Long getIgravadas() {
        return igravadas;
    }

    public void setIgravadas(Long igravadas) {
        this.igravadas = igravadas;
    }

    public Long getIexentas() {
        return iexentas;
    }

    public void setIexentas(Long iexentas) {
        this.iexentas = iexentas;
    }

    public Long getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Long impuestos) {
        this.impuestos = impuestos;
    }

    public Integer getNprecioPpp() {
        return nprecioPpp;
    }

    public void setNprecioPpp(Integer nprecioPpp) {
        this.nprecioPpp = nprecioPpp;
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

    public long getOldCajas() {
        return oldCajas;
    }

    public void setOldCajas(long oldCajas) {
        this.oldCajas = oldCajas;
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
        if (!(object instanceof MovimientosMerca)) {
            return false;
        }
        MovimientosMerca other = (MovimientosMerca) object;
        if ((this.nroMovim == null && other.nroMovim != null) || (this.nroMovim != null && !this.nroMovim.equals(other.nroMovim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MovimientosMerca[ nroMovim=" + nroMovim + " ]";
    }
    
}
