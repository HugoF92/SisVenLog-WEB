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
@Table(name = "parametros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parametros.findAll", query = "SELECT p FROM Parametros p")
    , @NamedQuery(name = "Parametros.findByCodParametro", query = "SELECT p FROM Parametros p WHERE p.codParametro = :codParametro")
    , @NamedQuery(name = "Parametros.findByXdesc", query = "SELECT p FROM Parametros p WHERE p.xdesc = :xdesc")
    , @NamedQuery(name = "Parametros.findByXvalor", query = "SELECT p FROM Parametros p WHERE p.xvalor = :xvalor")
    , @NamedQuery(name = "Parametros.findByFalta", query = "SELECT p FROM Parametros p WHERE p.falta = :falta")
    , @NamedQuery(name = "Parametros.findByCusuario", query = "SELECT p FROM Parametros p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "Parametros.findByFultimModif", query = "SELECT p FROM Parametros p WHERE p.fultimModif = :fultimModif")
    , @NamedQuery(name = "Parametros.findByCusuarioModif", query = "SELECT p FROM Parametros p WHERE p.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Parametros.findByMtipo", query = "SELECT p FROM Parametros p WHERE p.mtipo = :mtipo")})
public class Parametros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cod_parametro")
    private String codParametro;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Size(max = 50)
    @Column(name = "xvalor")
    private String xvalor;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 10)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "mtipo")
    private Character mtipo;

    public Parametros() {
    }

    public Parametros(String codParametro) {
        this.codParametro = codParametro;
    }

    public String getCodParametro() {
        return codParametro;
    }

    public void setCodParametro(String codParametro) {
        this.codParametro = codParametro;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public String getXvalor() {
        return xvalor;
    }

    public void setXvalor(String xvalor) {
        this.xvalor = xvalor;
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

    public Character getMtipo() {
        return mtipo;
    }

    public void setMtipo(Character mtipo) {
        this.mtipo = mtipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codParametro != null ? codParametro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametros)) {
            return false;
        }
        Parametros other = (Parametros) object;
        if ((this.codParametro == null && other.codParametro != null) || (this.codParametro != null && !this.codParametro.equals(other.codParametro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Parametros[ codParametro=" + codParametro + " ]";
    }
    
}
