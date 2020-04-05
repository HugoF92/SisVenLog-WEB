/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "rangos_documentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RangosDocumentos.findAll", query = "SELECT r FROM RangosDocumentos r")
    , @NamedQuery(name = "RangosDocumentos.findByCodEmpr", query = "SELECT r FROM RangosDocumentos r WHERE r.rangosDocumentosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "RangosDocumentos.findByCtipoDocum", query = "SELECT r FROM RangosDocumentos r WHERE r.rangosDocumentosPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "RangosDocumentos.findByNroDocumIni", query = "SELECT r FROM RangosDocumentos r WHERE r.rangosDocumentosPK.nroDocumIni = :nroDocumIni")
    , @NamedQuery(name = "RangosDocumentos.findByNroDocumFin", query = "SELECT r FROM RangosDocumentos r WHERE r.nroDocumFin = :nroDocumFin")
    , @NamedQuery(name = "RangosDocumentos.findByMtipoPapel", query = "SELECT r FROM RangosDocumentos r WHERE r.mtipoPapel = :mtipoPapel")
    , @NamedQuery(name = "RangosDocumentos.findByFalta", query = "SELECT r FROM RangosDocumentos r WHERE r.falta = :falta")
    , @NamedQuery(name = "RangosDocumentos.findByCusuario", query = "SELECT r FROM RangosDocumentos r WHERE r.cusuario = :cusuario")
    , @NamedQuery(name = "RangosDocumentos.findByFultimModif", query = "SELECT r FROM RangosDocumentos r WHERE r.fultimModif = :fultimModif")
    , @NamedQuery(name = "RangosDocumentos.findByCusuarioModif", query = "SELECT r FROM RangosDocumentos r WHERE r.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "RangosDocumentos.findByNtimbrado", query = "SELECT r FROM RangosDocumentos r WHERE r.ntimbrado = :ntimbrado")
    , @NamedQuery(name = "RangosDocumentos.findByNanoFinal", query = "SELECT r FROM RangosDocumentos r WHERE r.nanoFinal = :nanoFinal")
    , @NamedQuery(name = "RangosDocumentos.findByNanoInicial", query = "SELECT r FROM RangosDocumentos r WHERE r.rangosDocumentosPK.nanoInicial = :nanoInicial")
    , @NamedQuery(name = "RangosDocumentos.findByFvtoTimbrado", query = "SELECT r FROM RangosDocumentos r WHERE r.fvtoTimbrado = :fvtoTimbrado")})
public class RangosDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RangosDocumentosPK rangosDocumentosPK;
    @Column(name = "nro_docum_fin")
    private Long nroDocumFin;
    @Column(name = "mtipo_papel")
    private Character mtipoPapel;
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
    @Column(name = "ntimbrado")
    private BigInteger ntimbrado;
    @Column(name = "nano_final")
    private Short nanoFinal;
    @Column(name = "fvto_timbrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvtoTimbrado;

    public RangosDocumentos() {
    }

    public RangosDocumentos(RangosDocumentosPK rangosDocumentosPK) {
        this.rangosDocumentosPK = rangosDocumentosPK;
    }

    public RangosDocumentos(short codEmpr, String ctipoDocum, long nroDocumIni, short nanoInicial) {
        this.rangosDocumentosPK = new RangosDocumentosPK(codEmpr, ctipoDocum, nroDocumIni, nanoInicial);
    }

    public RangosDocumentosPK getRangosDocumentosPK() {
        return rangosDocumentosPK;
    }

    public void setRangosDocumentosPK(RangosDocumentosPK rangosDocumentosPK) {
        this.rangosDocumentosPK = rangosDocumentosPK;
    }

    public Long getNroDocumFin() {
        return nroDocumFin;
    }

    public void setNroDocumFin(Long nroDocumFin) {
        this.nroDocumFin = nroDocumFin;
    }

    public Character getMtipoPapel() {
        return mtipoPapel;
    }

    public void setMtipoPapel(Character mtipoPapel) {
        this.mtipoPapel = mtipoPapel;
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

    public BigInteger getNtimbrado() {
        return ntimbrado;
    }

    public void setNtimbrado(BigInteger ntimbrado) {
        this.ntimbrado = ntimbrado;
    }

    public Short getNanoFinal() {
        return nanoFinal;
    }

    public void setNanoFinal(Short nanoFinal) {
        this.nanoFinal = nanoFinal;
    }

    public Date getFvtoTimbrado() {
        return fvtoTimbrado;
    }

    public void setFvtoTimbrado(Date fvtoTimbrado) {
        this.fvtoTimbrado = fvtoTimbrado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rangosDocumentosPK != null ? rangosDocumentosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RangosDocumentos)) {
            return false;
        }
        RangosDocumentos other = (RangosDocumentos) object;
        if ((this.rangosDocumentosPK == null && other.rangosDocumentosPK != null) || (this.rangosDocumentosPK != null && !this.rangosDocumentosPK.equals(other.rangosDocumentosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.RangosDocumentos[ rangosDocumentosPK=" + rangosDocumentosPK + " ]";
    }
    
}
