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
@Table(name = "tipos_notas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposNotas.findAll", query = "SELECT t FROM TiposNotas t")
    , @NamedQuery(name = "TiposNotas.findByTipoNota", query = "SELECT t FROM TiposNotas t WHERE t.tipoNota = :tipoNota")
    , @NamedQuery(name = "TiposNotas.findByDescripcion", query = "SELECT t FROM TiposNotas t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "TiposNotas.findByMfijoSis", query = "SELECT t FROM TiposNotas t WHERE t.mfijoSis = :mfijoSis")})
public class TiposNotas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipo_nota")
    private String tipoNota;
    @Size(max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "mfijo_sis")
    private Character mfijoSis;

    public TiposNotas() {
    }

    public TiposNotas(String tipoNota) {
        this.tipoNota = tipoNota;
    }

    public String getTipoNota() {
        return tipoNota;
    }

    public void setTipoNota(String tipoNota) {
        this.tipoNota = tipoNota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Character getMfijoSis() {
        return mfijoSis;
    }

    public void setMfijoSis(Character mfijoSis) {
        this.mfijoSis = mfijoSis;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoNota != null ? tipoNota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposNotas)) {
            return false;
        }
        TiposNotas other = (TiposNotas) object;
        if ((this.tipoNota == null && other.tipoNota != null) || (this.tipoNota != null && !this.tipoNota.equals(other.tipoNota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.TiposNotas[ tipoNota=" + tipoNota + " ]";
    }
    
}
