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
 * @author Hugo
 */
@Entity
@Table(name = "tmp_datos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TmpDatos.findAll", query = "SELECT t FROM TmpDatos t")
    , @NamedQuery(name = "TmpDatos.findByCodigo", query = "SELECT t FROM TmpDatos t WHERE t.codigo = :codigo")
    , @NamedQuery(name = "TmpDatos.findByDescripcion", query = "SELECT t FROM TmpDatos t WHERE t.descripcion = :descripcion")})
public class TmpDatos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;

    public TmpDatos() {
    }

    public TmpDatos(String codigo) {
        String[] valor = codigo.split("-");
        this.codigo = valor[0];
        this.descripcion = valor[1];
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmpDatos)) {
            return false;
        }
        TmpDatos other = (TmpDatos) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.TmpDatos[ codigo=" + codigo + " ]";
    }
    
}
