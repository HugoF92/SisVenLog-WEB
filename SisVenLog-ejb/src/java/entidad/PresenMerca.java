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
@Table(name = "presen_merca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PresenMerca.findAll", query = "SELECT p FROM PresenMerca p")
    , @NamedQuery(name = "PresenMerca.findByCodPresen", query = "SELECT p FROM PresenMerca p WHERE p.codPresen = :codPresen")
    , @NamedQuery(name = "PresenMerca.findByXdesc", query = "SELECT p FROM PresenMerca p WHERE p.xdesc = :xdesc")})
public class PresenMerca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "cod_presen")
    private String codPresen;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;

    public PresenMerca() {
    }

    public PresenMerca(String codPresen) {
        this.codPresen = codPresen;
    }

    public String getCodPresen() {
        return codPresen;
    }

    public void setCodPresen(String codPresen) {
        this.codPresen = codPresen;
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
        hash += (codPresen != null ? codPresen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PresenMerca)) {
            return false;
        }
        PresenMerca other = (PresenMerca) object;
        if ((this.codPresen == null && other.codPresen != null) || (this.codPresen != null && !this.codPresen.equals(other.codPresen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PresenMerca[ codPresen=" + codPresen + " ]";
    }
    
}
