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
@Table(name = "cupos_vendedor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuposVendedor.findAll", query = "SELECT c FROM CuposVendedor c")
    , @NamedQuery(name = "CuposVendedor.findByCodEmpr", query = "SELECT c FROM CuposVendedor c WHERE c.cuposVendedorPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "CuposVendedor.findByCodVendedor", query = "SELECT c FROM CuposVendedor c WHERE c.cuposVendedorPK.codVendedor = :codVendedor")
    , @NamedQuery(name = "CuposVendedor.findByCodMerca", query = "SELECT c FROM CuposVendedor c WHERE c.cuposVendedorPK.codMerca = :codMerca")
    , @NamedQuery(name = "CuposVendedor.findByCodZona", query = "SELECT c FROM CuposVendedor c WHERE c.codZona = :codZona")
    , @NamedQuery(name = "CuposVendedor.findByFrigeDesde", query = "SELECT c FROM CuposVendedor c WHERE c.cuposVendedorPK.frigeDesde = :frigeDesde")
    , @NamedQuery(name = "CuposVendedor.findByKcajas", query = "SELECT c FROM CuposVendedor c WHERE c.kcajas = :kcajas")
    , @NamedQuery(name = "CuposVendedor.findByKunid", query = "SELECT c FROM CuposVendedor c WHERE c.kunid = :kunid")
    , @NamedQuery(name = "CuposVendedor.findByFalta", query = "SELECT c FROM CuposVendedor c WHERE c.falta = :falta")
    , @NamedQuery(name = "CuposVendedor.findByCusuario", query = "SELECT c FROM CuposVendedor c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "CuposVendedor.findByFultimModif", query = "SELECT c FROM CuposVendedor c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "CuposVendedor.findByCusuarioModif", query = "SELECT c FROM CuposVendedor c WHERE c.cusuarioModif = :cusuarioModif")})
public class CuposVendedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CuposVendedorPK cuposVendedorPK;
    @Size(max = 2)
    @Column(name = "cod_zona")
    private String codZona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kcajas")
    private short kcajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kunid")
    private short kunid;
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

    public CuposVendedor() {
    }

    public CuposVendedor(CuposVendedorPK cuposVendedorPK) {
        this.cuposVendedorPK = cuposVendedorPK;
    }

    public CuposVendedor(CuposVendedorPK cuposVendedorPK, short kcajas, short kunid) {
        this.cuposVendedorPK = cuposVendedorPK;
        this.kcajas = kcajas;
        this.kunid = kunid;
    }

    public CuposVendedor(short codEmpr, short codVendedor, String codMerca, Date frigeDesde) {
        this.cuposVendedorPK = new CuposVendedorPK(codEmpr, codVendedor, codMerca, frigeDesde);
    }

    public CuposVendedorPK getCuposVendedorPK() {
        return cuposVendedorPK;
    }

    public void setCuposVendedorPK(CuposVendedorPK cuposVendedorPK) {
        this.cuposVendedorPK = cuposVendedorPK;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public short getKcajas() {
        return kcajas;
    }

    public void setKcajas(short kcajas) {
        this.kcajas = kcajas;
    }

    public short getKunid() {
        return kunid;
    }

    public void setKunid(short kunid) {
        this.kunid = kunid;
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
        hash += (cuposVendedorPK != null ? cuposVendedorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuposVendedor)) {
            return false;
        }
        CuposVendedor other = (CuposVendedor) object;
        if ((this.cuposVendedorPK == null && other.cuposVendedorPK != null) || (this.cuposVendedorPK != null && !this.cuposVendedorPK.equals(other.cuposVendedorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.CuposVendedor[ cuposVendedorPK=" + cuposVendedorPK + " ]";
    }
    
}
