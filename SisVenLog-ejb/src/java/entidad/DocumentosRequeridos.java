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
 * @author lmore
 */
@Entity
@Table(name = "documentos_requeridos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosRequeridos.findAll", query = "SELECT d FROM DocumentosRequeridos d")
    , @NamedQuery(name = "DocumentosRequeridos.findByCdocum", query = "SELECT d FROM DocumentosRequeridos d WHERE d.cdocum = :cdocum")
    , @NamedQuery(name = "DocumentosRequeridos.findByXdesc", query = "SELECT d FROM DocumentosRequeridos d WHERE d.xdesc = :xdesc")
    , @NamedQuery(name = "DocumentosRequeridos.findByConFecVto", query = "SELECT d FROM DocumentosRequeridos d WHERE d.conFecVto = :conFecVto")
    , @NamedQuery(name = "DocumentosRequeridos.findByFalta", query = "SELECT d FROM DocumentosRequeridos d WHERE d.falta = :falta")
    , @NamedQuery(name = "DocumentosRequeridos.findByCusuario", query = "SELECT d FROM DocumentosRequeridos d WHERE d.cusuario = :cusuario")
    , @NamedQuery(name = "DocumentosRequeridos.findByFultimModif", query = "SELECT d FROM DocumentosRequeridos d WHERE d.fultimModif = :fultimModif")
    , @NamedQuery(name = "DocumentosRequeridos.findByCusuarioModif", query = "SELECT d FROM DocumentosRequeridos d WHERE d.cusuarioModif = :cusuarioModif")})
public class DocumentosRequeridos implements Serializable {

    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "con_fec_vto")
    private String conFecVto;
    @Size(max = 50)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 50)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cdocum")
    private String cdocum;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;

    public DocumentosRequeridos() {
    }

    public DocumentosRequeridos(String cdocum) {
        this.cdocum = cdocum;
    }

    public DocumentosRequeridos(String cdocum, String conFecVto) {
        this.cdocum = cdocum;
        this.conFecVto = conFecVto;
    }

    public String getCdocum() {
        return cdocum;
    }

    public void setCdocum(String cdocum) {
        this.cdocum = cdocum;
    }


    public String getConFecVto() {
        return conFecVto;
    }

    public void setConFecVto(String conFecVto) {
        this.conFecVto = conFecVto;
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
        hash += (cdocum != null ? cdocum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosRequeridos)) {
            return false;
        }
        DocumentosRequeridos other = (DocumentosRequeridos) object;
        if ((this.cdocum == null && other.cdocum != null) || (this.cdocum != null && !this.cdocum.equals(other.cdocum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DocumentosRequeridos[ cdocum=" + cdocum + " ]";
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }


    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    
}
