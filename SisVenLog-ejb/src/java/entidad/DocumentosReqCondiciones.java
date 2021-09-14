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
 * @author lmore
 */
@Entity
@Table(name = "documentos_req_condiciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosReqCondiciones.findAll", query = "SELECT d FROM DocumentosReqCondiciones d")
    , @NamedQuery(name = "DocumentosReqCondiciones.findByMtipoPersona", query = "SELECT d FROM DocumentosReqCondiciones d WHERE d.documentosReqCondicionesPK.mtipoPersona = :mtipoPersona")
    , @NamedQuery(name = "DocumentosReqCondiciones.findByCdocum", query = "SELECT d FROM DocumentosReqCondiciones d WHERE d.documentosReqCondicionesPK.cdocum = :cdocum")
    , @NamedQuery(name = "DocumentosReqCondiciones.findByMobligatorio", query = "SELECT d FROM DocumentosReqCondiciones d WHERE d.mobligatorio = :mobligatorio")
    , @NamedQuery(name = "DocumentosReqCondiciones.findByFalta", query = "SELECT d FROM DocumentosReqCondiciones d WHERE d.falta = :falta")
    , @NamedQuery(name = "DocumentosReqCondiciones.findByCusuario", query = "SELECT d FROM DocumentosReqCondiciones d WHERE d.cusuario = :cusuario")
    , @NamedQuery(name = "DocumentosReqCondiciones.findByFultimModif", query = "SELECT d FROM DocumentosReqCondiciones d WHERE d.fultimModif = :fultimModif")
    , @NamedQuery(name = "DocumentosReqCondiciones.findByCusuarioModif", query = "SELECT d FROM DocumentosReqCondiciones d WHERE d.cusuarioModif = :cusuarioModif")})
public class DocumentosReqCondiciones implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "mobligatorio")
    private Character mobligatorio;
    @Size(max = 50)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 50)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumentosReqCondicionesPK documentosReqCondicionesPK;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;

    public DocumentosReqCondiciones() {
    }

    public DocumentosReqCondiciones(DocumentosReqCondicionesPK documentosReqCondicionesPK) {
        this.documentosReqCondicionesPK = documentosReqCondicionesPK;
    }

    public DocumentosReqCondiciones(DocumentosReqCondicionesPK documentosReqCondicionesPK, Character mobligatorio) {
        this.documentosReqCondicionesPK = documentosReqCondicionesPK;
        this.mobligatorio = mobligatorio;
    }

    public DocumentosReqCondiciones(Character mtipoPersona, String cdocum) {
        this.documentosReqCondicionesPK = new DocumentosReqCondicionesPK(mtipoPersona, cdocum);
    }

    public DocumentosReqCondicionesPK getDocumentosReqCondicionesPK() {
        return documentosReqCondicionesPK;
    }

    public void setDocumentosReqCondicionesPK(DocumentosReqCondicionesPK documentosReqCondicionesPK) {
        this.documentosReqCondicionesPK = documentosReqCondicionesPK;
    }


    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
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
        hash += (documentosReqCondicionesPK != null ? documentosReqCondicionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosReqCondiciones)) {
            return false;
        }
        DocumentosReqCondiciones other = (DocumentosReqCondiciones) object;
        if ((this.documentosReqCondicionesPK == null && other.documentosReqCondicionesPK != null) || (this.documentosReqCondicionesPK != null && !this.documentosReqCondicionesPK.equals(other.documentosReqCondicionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DocumentosReqCondiciones[ documentosReqCondicionesPK=" + documentosReqCondicionesPK + " ]";
    }

    public Character getMobligatorio() {
        return mobligatorio;
    }

    public void setMobligatorio(Character mobligatorio) {
        this.mobligatorio = mobligatorio;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }


    
}
