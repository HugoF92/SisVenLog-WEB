/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "ciudades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ciudades.findAll", query = "SELECT c FROM Ciudades c")
    , @NamedQuery(name = "Ciudades.findByCodCiudad", query = "SELECT c FROM Ciudades c WHERE c.codCiudad = :codCiudad")
    , @NamedQuery(name = "Ciudades.findByXdesc", query = "SELECT c FROM Ciudades c WHERE c.xdesc = :xdesc")})
public class Ciudades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_ciudad")
    private Short codCiudad;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;

    public Ciudades() {
    }

    public Ciudades(Short codCiudad) {
        this.codCiudad = codCiudad;
    }

    public Short getCodCiudad() {
        return codCiudad;
    }

    public void setCodCiudad(Short codCiudad) {
        this.codCiudad = codCiudad;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCiudad != null ? codCiudad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ciudades)) {
            return false;
        }
        Ciudades other = (Ciudades) object;
        if ((this.codCiudad == null && other.codCiudad != null) || (this.codCiudad != null && !this.codCiudad.equals(other.codCiudad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Ciudades[ codCiudad=" + codCiudad + " ]";
    }
    
}
