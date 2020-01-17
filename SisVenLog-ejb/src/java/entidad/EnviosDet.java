/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "envios_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnviosDet.findAll", query = "SELECT e FROM EnviosDet e")
    , @NamedQuery(name = "EnviosDet.findByCodEmpr", query = "SELECT e FROM EnviosDet e WHERE e.enviosDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "EnviosDet.findByCodMerca", query = "SELECT e FROM EnviosDet e WHERE e.enviosDetPK.codMerca = :codMerca")
    , @NamedQuery(name = "EnviosDet.findByNroEnvio", query = "SELECT e FROM EnviosDet e WHERE e.enviosDetPK.nroEnvio = :nroEnvio")
    , @NamedQuery(name = "EnviosDet.findByXdesc", query = "SELECT e FROM EnviosDet e WHERE e.xdesc = :xdesc")
    , @NamedQuery(name = "EnviosDet.findByCantCajas", query = "SELECT e FROM EnviosDet e WHERE e.cantCajas = :cantCajas")
    , @NamedQuery(name = "EnviosDet.findByCantUnid", query = "SELECT e FROM EnviosDet e WHERE e.cantUnid = :cantUnid")
    , @NamedQuery(name = "EnviosDet.findByCodCanal", query = "SELECT e FROM EnviosDet e WHERE e.codCanal = :codCanal")})
public class EnviosDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EnviosDetPK enviosDetPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "cant_cajas")
    private Integer cantCajas;
    @Column(name = "cant_unid")
    private Integer cantUnid;
    @Size(max = 2)
    @Column(name = "cod_canal")
    private String codCanal;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_envio", referencedColumnName = "nro_envio", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Envios envios;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_merca", referencedColumnName = "cod_merca", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mercaderias mercaderias;

    public EnviosDet() {
    }

    public EnviosDet(EnviosDetPK enviosDetPK) {
        this.enviosDetPK = enviosDetPK;
    }

    public EnviosDet(short codEmpr, String codMerca, long nroEnvio) {
        this.enviosDetPK = new EnviosDetPK(codEmpr, codMerca, nroEnvio);
    }

    public EnviosDetPK getEnviosDetPK() {
        return enviosDetPK;
    }

    public void setEnviosDetPK(EnviosDetPK enviosDetPK) {
        this.enviosDetPK = enviosDetPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Integer getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(Integer cantCajas) {
        this.cantCajas = cantCajas;
    }

    public Integer getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(Integer cantUnid) {
        this.cantUnid = cantUnid;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public Envios getEnvios() {
        return envios;
    }

    public void setEnvios(Envios envios) {
        this.envios = envios;
    }

    public Mercaderias getMercaderias() {
        return mercaderias;
    }

    public void setMercaderias(Mercaderias mercaderias) {
        this.mercaderias = mercaderias;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enviosDetPK != null ? enviosDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnviosDet)) {
            return false;
        }
        EnviosDet other = (EnviosDet) object;
        if ((this.enviosDetPK == null && other.enviosDetPK != null) || (this.enviosDetPK != null && !this.enviosDetPK.equals(other.enviosDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EnviosDet[ enviosDetPK=" + enviosDetPK + " ]";
    }
    
}
