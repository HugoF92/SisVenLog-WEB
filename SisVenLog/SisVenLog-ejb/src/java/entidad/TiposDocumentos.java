/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author WORK
 */
@Entity
@Table(name = "tipos_documentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposDocumentos.findAll", query = "SELECT t FROM TiposDocumentos t")
    , @NamedQuery(name = "TiposDocumentos.findByCtipoDocum", query = "SELECT t FROM TiposDocumentos t WHERE t.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "TiposDocumentos.findByXdesc", query = "SELECT t FROM TiposDocumentos t WHERE t.xdesc = :xdesc")
    , @NamedQuery(name = "TiposDocumentos.findByMindice", query = "SELECT t FROM TiposDocumentos t WHERE t.mindice = :mindice")
    , @NamedQuery(name = "TiposDocumentos.findByMdebCred", query = "SELECT t FROM TiposDocumentos t WHERE t.mdebCred = :mdebCred")
    , @NamedQuery(name = "TiposDocumentos.findByMafectaStockRes", query = "SELECT t FROM TiposDocumentos t WHERE t.mafectaStockRes = :mafectaStockRes")
    , @NamedQuery(name = "TiposDocumentos.findByMafectaCtacte", query = "SELECT t FROM TiposDocumentos t WHERE t.mafectaCtacte = :mafectaCtacte")
    , @NamedQuery(name = "TiposDocumentos.findByMincluIva", query = "SELECT t FROM TiposDocumentos t WHERE t.mincluIva = :mincluIva")
    , @NamedQuery(name = "TiposDocumentos.findByMfijoSis", query = "SELECT t FROM TiposDocumentos t WHERE t.mfijoSis = :mfijoSis")
    , @NamedQuery(name = "TiposDocumentos.findByNmaxFilas", query = "SELECT t FROM TiposDocumentos t WHERE t.nmaxFilas = :nmaxFilas")
    , @NamedQuery(name = "TiposDocumentos.findByCusuario", query = "SELECT t FROM TiposDocumentos t WHERE t.cusuario = :cusuario")
    , @NamedQuery(name = "TiposDocumentos.findByFalta", query = "SELECT t FROM TiposDocumentos t WHERE t.falta = :falta")
    , @NamedQuery(name = "TiposDocumentos.findByCusuarioModif", query = "SELECT t FROM TiposDocumentos t WHERE t.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "TiposDocumentos.findByFultimModif", query = "SELECT t FROM TiposDocumentos t WHERE t.fultimModif = :fultimModif")
    , @NamedQuery(name = "TiposDocumentos.findByXordenPpp", query = "SELECT t FROM TiposDocumentos t WHERE t.xordenPpp = :xordenPpp")
    , @NamedQuery(name = "TiposDocumentos.findByCodContado", query = "SELECT t FROM TiposDocumentos t WHERE t.codContado = :codContado")
    , @NamedQuery(name = "TiposDocumentos.findByCodCredito", query = "SELECT t FROM TiposDocumentos t WHERE t.codCredito = :codCredito")
    , @NamedQuery(name = "TiposDocumentos.findByCodCtacble10", query = "SELECT t FROM TiposDocumentos t WHERE t.codCtacble10 = :codCtacble10")
    , @NamedQuery(name = "TiposDocumentos.findByCodCtacble5", query = "SELECT t FROM TiposDocumentos t WHERE t.codCtacble5 = :codCtacble5")
    , @NamedQuery(name = "TiposDocumentos.findByCodCtacblex", query = "SELECT t FROM TiposDocumentos t WHERE t.codCtacblex = :codCtacblex")})
public class TiposDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    ////@NotNull
    ////@Size(min = 1, max = 3)
    @Column(name = "ctipo_docum")
    private String ctipoDocum;
    ////@Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "mindice")
    private Float mindice;
    @Basic(optional = false)
    ////@NotNull
    @Column(name = "mdeb_cred")
    private Character mdebCred;
    @Basic(optional = false)
    ////@NotNull
    @Column(name = "mafecta_stock_res")
    private Character mafectaStockRes;
    @Basic(optional = false)
    ////@NotNull
    @Column(name = "mafecta_ctacte")
    private Character mafectaCtacte;
    @Basic(optional = false)
    ////@NotNull
    @Column(name = "minclu_iva")
    private Character mincluIva;
    @Basic(optional = false)
    ////@NotNull
    @Column(name = "mfijo_sis")
    private Character mfijoSis;
    @Basic(optional = false)
    ////@NotNull
    @Column(name = "nmax_filas")
    private short nmaxFilas;
    ////@Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    ////@Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Column(name = "xorden_ppp")
    private Character xordenPpp;
    @Column(name = "cod_contado")
    private Short codContado;
    @Column(name = "cod_credito")
    private Short codCredito;
    ////@Size(max = 20)
    @Column(name = "cod_ctacble10")
    private String codCtacble10;
    ////@Size(max = 20)
    @Column(name = "cod_ctacble5")
    private String codCtacble5;
    ////@Size(max = 20)
    @Column(name = "cod_ctacblex")
    private String codCtacblex;

    public TiposDocumentos() {
    }

    public TiposDocumentos(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public TiposDocumentos(String ctipoDocum, String xdesc) {
        this.ctipoDocum = ctipoDocum;
        this.xdesc = xdesc;
    }

    public TiposDocumentos(String ctipoDocum, Character mdebCred, Character mafectaStockRes, Character mafectaCtacte, Character mincluIva, Character mfijoSis, short nmaxFilas) {
        this.ctipoDocum = ctipoDocum;
        this.mdebCred = mdebCred;
        this.mafectaStockRes = mafectaStockRes;
        this.mafectaCtacte = mafectaCtacte;
        this.mincluIva = mincluIva;
        this.mfijoSis = mfijoSis;
        this.nmaxFilas = nmaxFilas;
    }

    public String getCtipoDocum() {
        return ctipoDocum;
    }

    public void setCtipoDocum(String ctipoDocum) {
        this.ctipoDocum = ctipoDocum;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Float getMindice() {
        return mindice;
    }

    public void setMindice(Float mindice) {
        this.mindice = mindice;
    }

    public Character getMdebCred() {
        return mdebCred;
    }

    public void setMdebCred(Character mdebCred) {
        this.mdebCred = mdebCred;
    }

    public Character getMafectaStockRes() {
        return mafectaStockRes;
    }

    public void setMafectaStockRes(Character mafectaStockRes) {
        this.mafectaStockRes = mafectaStockRes;
    }

    public Character getMafectaCtacte() {
        return mafectaCtacte;
    }

    public void setMafectaCtacte(Character mafectaCtacte) {
        this.mafectaCtacte = mafectaCtacte;
    }

    public Character getMincluIva() {
        return mincluIva;
    }

    public void setMincluIva(Character mincluIva) {
        this.mincluIva = mincluIva;
    }

    public Character getMfijoSis() {
        return mfijoSis;
    }

    public void setMfijoSis(Character mfijoSis) {
        this.mfijoSis = mfijoSis;
    }

    public short getNmaxFilas() {
        return nmaxFilas;
    }

    public void setNmaxFilas(short nmaxFilas) {
        this.nmaxFilas = nmaxFilas;
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

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public Character getXordenPpp() {
        return xordenPpp;
    }

    public void setXordenPpp(Character xordenPpp) {
        this.xordenPpp = xordenPpp;
    }

    public Short getCodContado() {
        return codContado;
    }

    public void setCodContado(Short codContado) {
        this.codContado = codContado;
    }

    public Short getCodCredito() {
        return codCredito;
    }

    public void setCodCredito(Short codCredito) {
        this.codCredito = codCredito;
    }

    public String getCodCtacble10() {
        return codCtacble10;
    }

    public void setCodCtacble10(String codCtacble10) {
        this.codCtacble10 = codCtacble10;
    }

    public String getCodCtacble5() {
        return codCtacble5;
    }

    public void setCodCtacble5(String codCtacble5) {
        this.codCtacble5 = codCtacble5;
    }

    public String getCodCtacblex() {
        return codCtacblex;
    }

    public void setCodCtacblex(String codCtacblex) {
        this.codCtacblex = codCtacblex;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctipoDocum != null ? ctipoDocum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposDocumentos)) {
            return false;
        }
        TiposDocumentos other = (TiposDocumentos) object;
        if ((this.ctipoDocum == null && other.ctipoDocum != null) || (this.ctipoDocum != null && !this.ctipoDocum.equals(other.ctipoDocum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.TiposDocumentos[ ctipoDocum =" + ctipoDocum + ", xdesc = "+ xdesc +" ]";
    }
    
}
