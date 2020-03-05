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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "metas_proveedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetasProveedores.findAll", query = "SELECT m FROM MetasProveedores m")
    , @NamedQuery(name = "MetasProveedores.findByCodEmpr", query = "SELECT m FROM MetasProveedores m WHERE m.metasProveedoresPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "MetasProveedores.findByCodProveed", query = "SELECT m FROM MetasProveedores m WHERE m.metasProveedoresPK.codProveed = :codProveed")
    , @NamedQuery(name = "MetasProveedores.findByCodLinea", query = "SELECT m FROM MetasProveedores m WHERE m.metasProveedoresPK.codLinea = :codLinea")
    , @NamedQuery(name = "MetasProveedores.findByNtoneladas", query = "SELECT m FROM MetasProveedores m WHERE m.ntoneladas = :ntoneladas")
    , @NamedQuery(name = "MetasProveedores.findByNcajas", query = "SELECT m FROM MetasProveedores m WHERE m.ncajas = :ncajas")
    , @NamedQuery(name = "MetasProveedores.findByIventasGs", query = "SELECT m FROM MetasProveedores m WHERE m.iventasGs = :iventasGs")
    , @NamedQuery(name = "MetasProveedores.findByFrigeDesde", query = "SELECT m FROM MetasProveedores m WHERE m.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "MetasProveedores.findByFrigeHasta", query = "SELECT m FROM MetasProveedores m WHERE m.frigeHasta = :frigeHasta")
    , @NamedQuery(name = "MetasProveedores.findByNanno", query = "SELECT m FROM MetasProveedores m WHERE m.metasProveedoresPK.nanno = :nanno")
    , @NamedQuery(name = "MetasProveedores.findByNmes", query = "SELECT m FROM MetasProveedores m WHERE m.metasProveedoresPK.nmes = :nmes")})
public class MetasProveedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MetasProveedoresPK metasProveedoresPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ntoneladas")
    private BigDecimal ntoneladas;
    @Column(name = "ncajas")
    private Integer ncajas;
    @Column(name = "iventas_gs")
    private Long iventasGs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeDesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;
    @JoinColumn(name = "cod_linea", referencedColumnName = "cod_linea", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Lineas lineas;
    @JoinColumn(name = "cod_proveed", referencedColumnName = "cod_proveed", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proveedores proveedores;

    public MetasProveedores() {
    }

    public MetasProveedores(MetasProveedoresPK metasProveedoresPK) {
        this.metasProveedoresPK = metasProveedoresPK;
    }

    public MetasProveedores(MetasProveedoresPK metasProveedoresPK, Date frigeDesde, Date frigeHasta) {
        this.metasProveedoresPK = metasProveedoresPK;
        this.frigeDesde = frigeDesde;
        this.frigeHasta = frigeHasta;
    }

    public MetasProveedores(short codEmpr, short codProveed, short codLinea, short nanno, short nmes) {
        this.metasProveedoresPK = new MetasProveedoresPK(codEmpr, codProveed, codLinea, nanno, nmes);
    }

    public MetasProveedoresPK getMetasProveedoresPK() {
        return metasProveedoresPK;
    }

    public void setMetasProveedoresPK(MetasProveedoresPK metasProveedoresPK) {
        this.metasProveedoresPK = metasProveedoresPK;
    }

    public BigDecimal getNtoneladas() {
        return ntoneladas;
    }

    public void setNtoneladas(BigDecimal ntoneladas) {
        this.ntoneladas = ntoneladas;
    }

    public Integer getNcajas() {
        return ncajas;
    }

    public void setNcajas(Integer ncajas) {
        this.ncajas = ncajas;
    }

    public Long getIventasGs() {
        return iventasGs;
    }

    public void setIventasGs(Long iventasGs) {
        this.iventasGs = iventasGs;
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

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public Lineas getLineas() {
        return lineas;
    }

    public void setLineas(Lineas lineas) {
        this.lineas = lineas;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metasProveedoresPK != null ? metasProveedoresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetasProveedores)) {
            return false;
        }
        MetasProveedores other = (MetasProveedores) object;
        if ((this.metasProveedoresPK == null && other.metasProveedoresPK != null) || (this.metasProveedoresPK != null && !this.metasProveedoresPK.equals(other.metasProveedoresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MetasProveedores[ metasProveedoresPK=" + metasProveedoresPK + " ]";
    }
    
}
