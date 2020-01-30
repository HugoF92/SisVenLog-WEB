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
@Table(name = "servicios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicios.findAll", query = "SELECT s FROM Servicios s")
    , @NamedQuery(name = "Servicios.findByCodServicio", query = "SELECT s FROM Servicios s WHERE s.codServicio = :codServicio")
    , @NamedQuery(name = "Servicios.findByXdesc", query = "SELECT s FROM Servicios s WHERE s.xdesc = :xdesc")
    , @NamedQuery(name = "Servicios.findByPimpues", query = "SELECT s FROM Servicios s WHERE s.pimpues = :pimpues")
    , @NamedQuery(name = "Servicios.findByPdesc", query = "SELECT s FROM Servicios s WHERE s.pdesc = :pdesc")
    , @NamedQuery(name = "Servicios.findByFalta", query = "SELECT s FROM Servicios s WHERE s.falta = :falta")
    , @NamedQuery(name = "Servicios.findByCsuario", query = "SELECT s FROM Servicios s WHERE s.csuario = :csuario")
    , @NamedQuery(name = "Servicios.findByFultimModif", query = "SELECT s FROM Servicios s WHERE s.fultimModif = :fultimModif")
    , @NamedQuery(name = "Servicios.findByCusuarioModif", query = "SELECT s FROM Servicios s WHERE s.cusuarioModif = :cusuarioModif")})
public class Servicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_servicio")
    private Short codServicio;
    @Size(max = 80)
    @Column(name = "xdesc")
    private String xdesc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pimpues")
    private BigDecimal pimpues;
    @Column(name = "pdesc")
    private BigDecimal pdesc;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "csuario")
    private String csuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    public Servicios() {
    }

    public Servicios(Short codServicio) {
        this.codServicio = codServicio;
    }

    public Short getCodServicio() {
        return codServicio;
    }

    public void setCodServicio(Short codServicio) {
        this.codServicio = codServicio;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public BigDecimal getPimpues() {
        return pimpues;
    }

    public void setPimpues(BigDecimal pimpues) {
        this.pimpues = pimpues;
    }

    public BigDecimal getPdesc() {
        return pdesc;
    }

    public void setPdesc(BigDecimal pdesc) {
        this.pdesc = pdesc;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCsuario() {
        return csuario;
    }

    public void setCsuario(String csuario) {
        this.csuario = csuario;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codServicio != null ? codServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servicios)) {
            return false;
        }
        Servicios other = (Servicios) object;
        if ((this.codServicio == null && other.codServicio != null) || (this.codServicio != null && !this.codServicio.equals(other.codServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Servicios[ codServicio=" + codServicio + " ]";
    }
    
}
