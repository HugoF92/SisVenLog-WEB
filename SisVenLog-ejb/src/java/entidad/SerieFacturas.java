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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "serie_facturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SerieFacturas.findAll", query = "SELECT s FROM SerieFacturas s")
    , @NamedQuery(name = "SerieFacturas.findByFfinal", query = "SELECT s FROM SerieFacturas s WHERE s.serieFacturasPK.ffinal = :ffinal")
    , @NamedQuery(name = "SerieFacturas.findByNdocumFinal", query = "SELECT s FROM SerieFacturas s WHERE s.serieFacturasPK.ndocumFinal = :ndocumFinal")
    , @NamedQuery(name = "SerieFacturas.findByNserie", query = "SELECT s FROM SerieFacturas s WHERE s.nserie = :nserie")
    , @NamedQuery(name = "SerieFacturas.findByCtipoDocum", query = "SELECT s FROM SerieFacturas s WHERE s.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "SerieFacturas.findByFalta", query = "SELECT s FROM SerieFacturas s WHERE s.falta = :falta")
    , @NamedQuery(name = "SerieFacturas.findByCusuario", query = "SELECT s FROM SerieFacturas s WHERE s.cusuario = :cusuario")
    , @NamedQuery(name = "SerieFacturas.findByFultimModif", query = "SELECT s FROM SerieFacturas s WHERE s.fultimModif = :fultimModif")
    , @NamedQuery(name = "SerieFacturas.findByCusuarioModif", query = "SELECT s FROM SerieFacturas s WHERE s.cusuarioModif = :cusuarioModif")})
public class SerieFacturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SerieFacturasPK serieFacturasPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nserie")
    private int nserie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    public SerieFacturas() {
    }

    public SerieFacturas(SerieFacturasPK serieFacturasPK) {
        this.serieFacturasPK = serieFacturasPK;
    }

    public SerieFacturas(SerieFacturasPK serieFacturasPK, int nserie, String ctipoDocum) {
        this.serieFacturasPK = serieFacturasPK;
        this.nserie = nserie;
        this.ctipoDocum = ctipoDocum;
    }

    public SerieFacturas(Date ffinal, long ndocumFinal) {
        this.serieFacturasPK = new SerieFacturasPK(ffinal, ndocumFinal);
    }

    public SerieFacturasPK getSerieFacturasPK() {
        return serieFacturasPK;
    }

    public void setSerieFacturasPK(SerieFacturasPK serieFacturasPK) {
        this.serieFacturasPK = serieFacturasPK;
    }

    public int getNserie() {
        return nserie;
    }

    public void setNserie(int nserie) {
        this.nserie = nserie;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serieFacturasPK != null ? serieFacturasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SerieFacturas)) {
            return false;
        }
        SerieFacturas other = (SerieFacturas) object;
        if ((this.serieFacturasPK == null && other.serieFacturasPK != null) || (this.serieFacturasPK != null && !this.serieFacturasPK.equals(other.serieFacturasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.SerieFacturas[ serieFacturasPK=" + serieFacturasPK + " ]";
    }
    
}
