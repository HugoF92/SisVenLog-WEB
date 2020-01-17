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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "inventario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventario.findAll", query = "SELECT i FROM Inventario i")
    , @NamedQuery(name = "Inventario.findByCodEmpr", query = "SELECT i FROM Inventario i WHERE i.inventarioPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Inventario.findByNroInven", query = "SELECT i FROM Inventario i WHERE i.inventarioPK.nroInven = :nroInven")
    , @NamedQuery(name = "Inventario.findByCodDepo", query = "SELECT i FROM Inventario i WHERE i.codDepo = :codDepo")
    , @NamedQuery(name = "Inventario.findByFmovim", query = "SELECT i FROM Inventario i WHERE i.fmovim = :fmovim")
    , @NamedQuery(name = "Inventario.findByMtipoInv", query = "SELECT i FROM Inventario i WHERE i.mtipoInv = :mtipoInv")
    , @NamedQuery(name = "Inventario.findByCusuario", query = "SELECT i FROM Inventario i WHERE i.cusuario = :cusuario")
    , @NamedQuery(name = "Inventario.findByFalta", query = "SELECT i FROM Inventario i WHERE i.falta = :falta")})
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InventarioPK inventarioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_depo")
    private short codDepo;
    @Column(name = "fmovim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fmovim;
    @Column(name = "mtipo_inv")
    private Character mtipoInv;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresas empresas;

    public Inventario() {
    }

    public Inventario(InventarioPK inventarioPK) {
        this.inventarioPK = inventarioPK;
    }

    public Inventario(InventarioPK inventarioPK, short codDepo) {
        this.inventarioPK = inventarioPK;
        this.codDepo = codDepo;
    }

    public Inventario(short codEmpr, int nroInven) {
        this.inventarioPK = new InventarioPK(codEmpr, nroInven);
    }

    public InventarioPK getInventarioPK() {
        return inventarioPK;
    }

    public void setInventarioPK(InventarioPK inventarioPK) {
        this.inventarioPK = inventarioPK;
    }

    public short getCodDepo() {
        return codDepo;
    }

    public void setCodDepo(short codDepo) {
        this.codDepo = codDepo;
    }

    public Date getFmovim() {
        return fmovim;
    }

    public void setFmovim(Date fmovim) {
        this.fmovim = fmovim;
    }

    public Character getMtipoInv() {
        return mtipoInv;
    }

    public void setMtipoInv(Character mtipoInv) {
        this.mtipoInv = mtipoInv;
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

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inventarioPK != null ? inventarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventario)) {
            return false;
        }
        Inventario other = (Inventario) object;
        if ((this.inventarioPK == null && other.inventarioPK != null) || (this.inventarioPK != null && !this.inventarioPK.equals(other.inventarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Inventario[ inventarioPK=" + inventarioPK + " ]";
    }
    
}
