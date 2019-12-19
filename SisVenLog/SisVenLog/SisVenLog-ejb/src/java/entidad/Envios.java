/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "envios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Envios.findAll", query = "SELECT e FROM Envios e")
    , @NamedQuery(name = "Envios.findByCodEmpr", query = "SELECT e FROM Envios e WHERE e.enviosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Envios.findByNroEnvio", query = "SELECT e FROM Envios e WHERE e.enviosPK.nroEnvio = :nroEnvio")
    , @NamedQuery(name = "Envios.findByCodEntregador", query = "SELECT e FROM Envios e WHERE e.codEntregador = :codEntregador")
    , @NamedQuery(name = "Envios.findByCodCanal", query = "SELECT e FROM Envios e WHERE e.codCanal = :codCanal")
    , @NamedQuery(name = "Envios.findByDepoOrigen", query = "SELECT e FROM Envios e WHERE e.depoOrigen = :depoOrigen")
    , @NamedQuery(name = "Envios.findByDepoDestino", query = "SELECT e FROM Envios e WHERE e.depoDestino = :depoDestino")
    , @NamedQuery(name = "Envios.findByFenvio", query = "SELECT e FROM Envios e WHERE e.fenvio = :fenvio")
    , @NamedQuery(name = "Envios.findByFanul", query = "SELECT e FROM Envios e WHERE e.fanul = :fanul")
    , @NamedQuery(name = "Envios.findByFfactur", query = "SELECT e FROM Envios e WHERE e.ffactur = :ffactur")
    , @NamedQuery(name = "Envios.findByTotPeso", query = "SELECT e FROM Envios e WHERE e.totPeso = :totPeso")
    , @NamedQuery(name = "Envios.findByMestado", query = "SELECT e FROM Envios e WHERE e.mestado = :mestado")
    , @NamedQuery(name = "Envios.findByXobs", query = "SELECT e FROM Envios e WHERE e.xobs = :xobs")
    , @NamedQuery(name = "Envios.findByCusuario", query = "SELECT e FROM Envios e WHERE e.cusuario = :cusuario")
    , @NamedQuery(name = "Envios.findByFalta", query = "SELECT e FROM Envios e WHERE e.falta = :falta")
    , @NamedQuery(name = "Envios.findByCusuarioModif", query = "SELECT e FROM Envios e WHERE e.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Envios.findByFultimModif", query = "SELECT e FROM Envios e WHERE e.fultimModif = :fultimModif")
    , @NamedQuery(name = "Envios.findByMtipo", query = "SELECT e FROM Envios e WHERE e.mtipo = :mtipo")})
public class Envios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EnviosPK enviosPK;
    @Column(name = "cod_entregador")
    private Short codEntregador;
    @Size(max = 2)
    @Column(name = "cod_canal")
    private String codCanal;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el deposito origen.")
    @Column(name = "depo_origen")
    private short depoOrigen;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el deposito destino.")
    @Column(name = "depo_destino")
    private short depoDestino;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar la fecha de envio.")
    @Column(name = "fenvio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fenvio;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Column(name = "ffactur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ffactur;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el total peso.")
    @Column(name = "tot_peso")
    private BigDecimal totPeso;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el estado de envio.")
    @Column(name = "mestado")
    private Character mestado;
    @Size(max = 250)
    @Column(name = "xobs")
    private String xobs;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el tipo de envio.")
    @Column(name = "mtipo")
    private Character mtipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "envios")
    private Collection<EnviosDet> enviosDetCollection;

    public Envios() {
    }

    public Envios(EnviosPK enviosPK) {
        this.enviosPK = enviosPK;
    }

    public Envios(EnviosPK enviosPK, short depoOrigen, short depoDestino, Date fenvio, BigDecimal totPeso, Character mestado, Character mtipo) {
        this.enviosPK = enviosPK;
        this.depoOrigen = depoOrigen;
        this.depoDestino = depoDestino;
        this.fenvio = fenvio;
        this.totPeso = totPeso;
        this.mestado = mestado;
        this.mtipo = mtipo;
    }

    public Envios(short codEmpr, long nroEnvio) {
        this.enviosPK = new EnviosPK(codEmpr, nroEnvio);
    }

    public EnviosPK getEnviosPK() {
        return enviosPK;
    }

    public void setEnviosPK(EnviosPK enviosPK) {
        this.enviosPK = enviosPK;
    }

    public Short getCodEntregador() {
        return codEntregador;
    }

    public void setCodEntregador(Short codEntregador) {
        this.codEntregador = codEntregador;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public short getDepoOrigen() {
        return depoOrigen;
    }

    public void setDepoOrigen(short depoOrigen) {
        this.depoOrigen = depoOrigen;
    }

    public short getDepoDestino() {
        return depoDestino;
    }

    public void setDepoDestino(short depoDestino) {
        this.depoDestino = depoDestino;
    }

    public Date getFenvio() {
        return fenvio;
    }

    public void setFenvio(Date fenvio) {
        this.fenvio = fenvio;
    }

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    public Date getFfactur() {
        return ffactur;
    }

    public void setFfactur(Date ffactur) {
        this.ffactur = ffactur;
    }

    public BigDecimal getTotPeso() {
        return totPeso;
    }

    public void setTotPeso(BigDecimal totPeso) {
        this.totPeso = totPeso;
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

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public Character getMtipo() {
        return mtipo;
    }

    public void setMtipo(Character mtipo) {
        this.mtipo = mtipo;
    }

    @XmlTransient
    public Collection<EnviosDet> getEnviosDetCollection() {
        return enviosDetCollection;
    }

    public void setEnviosDetCollection(Collection<EnviosDet> enviosDetCollection) {
        this.enviosDetCollection = enviosDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enviosPK != null ? enviosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Envios)) {
            return false;
        }
        Envios other = (Envios) object;
        if ((this.enviosPK == null && other.enviosPK != null) || (this.enviosPK != null && !this.enviosPK.equals(other.enviosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Envios[ enviosPK=" + enviosPK + " ]";
    }
    
}
