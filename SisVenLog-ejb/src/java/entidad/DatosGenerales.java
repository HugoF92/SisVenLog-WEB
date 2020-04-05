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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "datos_generales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosGenerales.findAll", query = "SELECT d FROM DatosGenerales d")
    , @NamedQuery(name = "DatosGenerales.findByCodEmpr", query = "SELECT d FROM DatosGenerales d WHERE d.datosGeneralesPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "DatosGenerales.findByTempPath", query = "SELECT d FROM DatosGenerales d WHERE d.tempPath = :tempPath")
    , @NamedQuery(name = "DatosGenerales.findByFrigeDesde", query = "SELECT d FROM DatosGenerales d WHERE d.datosGeneralesPK.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "DatosGenerales.findByFrigeHasta", query = "SELECT d FROM DatosGenerales d WHERE d.frigeHasta = :frigeHasta")
    , @NamedQuery(name = "DatosGenerales.findByPalmpath", query = "SELECT d FROM DatosGenerales d WHERE d.palmpath = :palmpath")
    , @NamedQuery(name = "DatosGenerales.findByCusuario", query = "SELECT d FROM DatosGenerales d WHERE d.cusuario = :cusuario")
    , @NamedQuery(name = "DatosGenerales.findByFalta", query = "SELECT d FROM DatosGenerales d WHERE d.falta = :falta")
    , @NamedQuery(name = "DatosGenerales.findByPcostoFinan", query = "SELECT d FROM DatosGenerales d WHERE d.pcostoFinan = :pcostoFinan")})
public class DatosGenerales implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DatosGeneralesPK datosGeneralesPK;
    @Size(max = 30)
    @Column(name = "tempPath")
    private String tempPath;
    @Column(name = "frige_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frigeHasta;
    @Size(max = 50)
    @Column(name = "palmpath")
    private String palmpath;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pcosto_finan")
    private BigDecimal pcostoFinan;

    public DatosGenerales() {
    }

    public DatosGenerales(DatosGeneralesPK datosGeneralesPK) {
        this.datosGeneralesPK = datosGeneralesPK;
    }

    public DatosGenerales(short codEmpr, Date frigeDesde) {
        this.datosGeneralesPK = new DatosGeneralesPK(codEmpr, frigeDesde);
    }

    public DatosGeneralesPK getDatosGeneralesPK() {
        return datosGeneralesPK;
    }

    public void setDatosGeneralesPK(DatosGeneralesPK datosGeneralesPK) {
        this.datosGeneralesPK = datosGeneralesPK;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public Date getFrigeHasta() {
        return frigeHasta;
    }

    public void setFrigeHasta(Date frigeHasta) {
        this.frigeHasta = frigeHasta;
    }

    public String getPalmpath() {
        return palmpath;
    }

    public void setPalmpath(String palmpath) {
        this.palmpath = palmpath;
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

    public BigDecimal getPcostoFinan() {
        return pcostoFinan;
    }

    public void setPcostoFinan(BigDecimal pcostoFinan) {
        this.pcostoFinan = pcostoFinan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datosGeneralesPK != null ? datosGeneralesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosGenerales)) {
            return false;
        }
        DatosGenerales other = (DatosGenerales) object;
        if ((this.datosGeneralesPK == null && other.datosGeneralesPK != null) || (this.datosGeneralesPK != null && !this.datosGeneralesPK.equals(other.datosGeneralesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DatosGenerales[ datosGeneralesPK=" + datosGeneralesPK + " ]";
    }
    
}
