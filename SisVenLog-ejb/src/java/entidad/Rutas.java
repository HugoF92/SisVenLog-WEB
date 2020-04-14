/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "rutas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rutas.findAll", query = "SELECT r FROM Rutas r")
    , @NamedQuery(name = "Rutas.findByCodEmpr", query = "SELECT r FROM Rutas r WHERE r.rutasPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Rutas.findByCodRuta", query = "SELECT r FROM Rutas r WHERE r.rutasPK.codRuta = :codRuta")
    , @NamedQuery(name = "Rutas.findByXdesc", query = "SELECT r FROM Rutas r WHERE r.xdesc = :xdesc")
    , @NamedQuery(name = "Rutas.findByCusuario", query = "SELECT r FROM Rutas r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "Rutas.findByFalta", query = "SELECT r FROM Rutas r WHERE r.falta = :falta")})
public class Rutas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RutasPK rutasPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "cod_ruta")
    private Short codRuta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_zona", referencedColumnName = "cod_zona")})
    @ManyToOne(optional = false)
    private Zonas zonas;

    public Rutas() {
    }

    public Rutas(RutasPK rutasPK) {
        this.rutasPK = rutasPK;
    }

    public Rutas(short codEmpr, short codRuta) {
        this.rutasPK = new RutasPK(codEmpr, codRuta);
    }

    public RutasPK getRutasPK() {
        return rutasPK;
    }

    public void setRutasPK(RutasPK rutasPK) {
        this.rutasPK = rutasPK;
    }

    public Short getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(Short codRuta) {
        this.codRuta = codRuta;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
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

    public Zonas getZonas() {
        return zonas;
    }

    public void setZonas(Zonas zonas) {
        this.zonas = zonas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rutasPK != null ? rutasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rutas)) {
            return false;
        }
        Rutas other = (Rutas) object;
        if ((this.rutasPK == null && other.rutasPK != null) || (this.rutasPK != null && !this.rutasPK.equals(other.rutasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Rutas[ rutasPK=" + rutasPK + " ]";
    }
    
}
